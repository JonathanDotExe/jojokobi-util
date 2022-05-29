package at.jojokobi.mcutil.generation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.Plugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;

public class GenerationHandler implements Listener {

	public static final String STRUCTURES_ELEMENT = "structures";
	public static final String STRUCTURE_ELEMENT = "structure";

	private List<Structure> structures = new ArrayList<>();
	private List<StructureInstance<? extends Structure>> instances = new ArrayList<>();
	private String saveFolder = "mcutil" + File.separator + "structures";

	private List<String> legacySaveFolders = new ArrayList<>();
	private Map<String, GeneratorWorldConfig> configs = new HashMap<>();

	private Plugin plugin;

	@Deprecated
	public GenerationHandler(Plugin plugin, String saveFolder) {
		super();
		this.plugin = plugin;
		this.saveFolder = saveFolder;
	}

	@EventHandler
	public void onWorldLoad(WorldLoadEvent event) {
		Bukkit.getScheduler().runTask(plugin, () -> {
			for (String name : legacySaveFolders) {
				File folder = new File(Bukkit.getWorldContainer(), event.getWorld().getName() + File.separator + name);
				if (folder.exists() && folder.isDirectory()) {
					for (File file : folder.listFiles()) {
						if (file.isFile() && file.getName().endsWith(".xml")) {
							load(name + File.separator + file.getName(), event.getWorld());
						}
					}
					folder.renameTo(new File(Bukkit.getWorldContainer(),
							event.getWorld().getName() + File.separator + name + "_old"));
				}
			}
		});
	}

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {
		Chunk chunk = event.getChunk();
		load(chunk);
	}

	@EventHandler
	public void onChunkPopulate(ChunkPopulateEvent event) {
		Chunk chunk = event.getChunk();
		//Generate
		for (Structure structure : structures) {
			GeneratorWorldConfig config = getWorldConfig(event.getWorld().getName());
			if ((config == null || (config.isGenerateStructures() && !config.getDontGenerate().contains(structure.getIdentifier()))) && structure.canGenerate(chunk, chunk.getWorld().getSeed())) {
				Bukkit.getScheduler().runTask(plugin, () -> {
					instances.addAll(structure.generateNaturally(chunk, chunk.getWorld().getSeed()));
				});
			}
		}
	}

	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event) {
		save(event.getChunk());
	}

	public void onDisable() {
		for (World world : Bukkit.getWorlds()) {
			for (Chunk chunk : world.getLoadedChunks()) {
				save(chunk);
			}
		}
	}
	
	@EventHandler
	public void onSave(WorldSaveEvent event) {
		for (Chunk chunk : event.getWorld().getLoadedChunks()) {
			save(chunk);
		}
	}

	private void save(Chunk chunk) {
		List<StructureInstance<? extends Structure>> save = new ArrayList<>();
		// Get Structures to Save
		for (StructureInstance<? extends Structure> struc : new ArrayList<>(instances)) {
			if (TerrainGenUtil.isInChunk(struc.getLocation(), chunk)) {
				save.add(struc);
			}
		}
		// Save them
		if (!save.isEmpty()) {
			File folder = new File(Bukkit.getWorldContainer(),
					chunk.getWorld().getName() + File.separator + saveFolder);
			folder.mkdirs();
			try (OutputStream out = new FileOutputStream(new File(folder, getSaveName(chunk) + ".xml"))) {
				// Initialize Document
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setNamespaceAware(true);
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.newDocument();
				// Root-Element
				Element root = document.createElement(STRUCTURES_ELEMENT);
				root.setAttribute("xmlns", "https://jojokobi.lima-city.de/pokemine");
				root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
				root.setAttribute("xsi:schemaLocation",
						"https://jojokobi.lima-city.de/mcutil https://jojokobi.lima-city.de/mcutil/structures");
				document.appendChild(root);
				// Iterate over Structures and serialize them to XML
				for (StructureInstance<? extends Structure> struc : save) {
					Element element = document.createElement(STRUCTURE_ELEMENT);
					struc.save(document, element);
					root.appendChild(element);
				}
				// Write to file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(out);
				transformer.transform(source, result);
				// Remove structures
				instances.removeAll(save);
			} catch (ParserConfigurationException | IOException | TransformerException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean load(Chunk chunk) {
		return load(saveFolder + getSaveName(chunk) + ".xml", chunk.getWorld());
	}

	private boolean load(String filename, World world) {
		boolean found = false;
		try (InputStream input = new FileInputStream(
				new File(Bukkit.getWorldContainer(), world.getName() + File.separator + filename))) {
			// Initialize Document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(input);
			found = true;
			// Load Structures
			NodeList strucNodes = document.getElementsByTagName(STRUCTURE_ELEMENT);
			for (int i = 0; i < strucNodes.getLength(); i++) {
				if (strucNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element strucElement = (Element) strucNodes.item(i);
					Structure structure = null;
					if (strucElement.getElementsByTagName(StructureInstance.STRUCTURE_ID_ELEMENT).getLength() > 0
							&& (structure = getStructure(
									strucElement.getElementsByTagName(StructureInstance.STRUCTURE_ID_ELEMENT).item(0)
											.getTextContent())) != null) {
						StructureInstance<? extends Structure> strucInstance = structure.getStandardInstance(world);
						strucInstance.parseXML(strucElement);
						instances.add(strucInstance);
					}
				}
			}
		} catch (FileNotFoundException e) {

		} catch (ParserConfigurationException | IOException | SAXException e) {
			e.printStackTrace();
		}
		return found;
	}

	public static String getSaveName(Chunk chunk) {
		return "chunk_" + chunk.getX() + "_" + chunk.getZ();
	}

	public void addStructure(Structure struc) {
		structures.add(struc);
	}

	public Structure getStructure(String id) {
		Structure struc = null;
		for (int i = 0; i < structures.size() && struc == null; i++) {
			if (structures.get(i).getIdentifier().equals(id)) {
				struc = structures.get(i);
			}
		}
		return struc;
	}

	public List<Structure> getStructures() {
		return new ArrayList<>(structures);
	}

	public void addStructureInstance(StructureInstance<?> inst) {
		instances.add(inst);
	}
	
	public void removeStructureInstance(StructureInstance<?> inst) {
		instances.remove(inst);
	}

	public List<StructureInstance<?>> getInstancesAt(Location place) {
		List<StructureInstance<?>> instances = new ArrayList<>();
		double x = place.getX();
		double y = place.getY();
		double z = place.getZ();
		for (StructureInstance<?> s : this.instances) {
			if (s.getX() < x && x < s.getX() + s.getWidth() && s.getY() < y && y < s.getY() + s.getHeight()
					&& s.getZ() < z && z < s.getZ() + s.getLength()) {
				instances.add(s);
			}
		}
		return instances;
	}
	
	public StructureInstance<?> getInstanceAt(Location place) {
		List<StructureInstance<?>> strucs = getInstancesAt(place);
		return strucs.isEmpty() ? null : strucs.get(0);
	}

	public StructureInstance<? extends Structure> getInstanceInArea(Location place, int width, int height, int length) {
		StructureInstance<? extends Structure> struc = null;
		double x = place.getX();
		double y = place.getY();
		double z = place.getZ();
		for (int i = 0; i < instances.size() && struc == null; i++) {
			StructureInstance<? extends Structure> s = instances.get(i);
			if (s.getX() <= x + width && s.getX() + s.getWidth() >= x && s.getY() <= y + height
					&& s.getY() + s.getHeight() >= y && s.getZ() <= z + length && s.getZ() + s.getLength() >= z) {
				struc = s;
			}
		}
		return struc;
	}

	public String getSaveFolder() {
		return saveFolder;
	}

	public void addLegacySaveFolder(String folder) {
		legacySaveFolders.add(folder);
	}
	
	public GeneratorWorldConfig getWorldConfig(String name) {
		return configs.get(name);
	}

	public void setWorldConfig(String name, GeneratorWorldConfig config) {
		configs.put(name, config);
	}
	
}
