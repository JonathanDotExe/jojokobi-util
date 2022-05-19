package at.jojokobi.mcutil.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import at.jojokobi.mcutil.SerializableMap;
import at.jojokobi.mcutil.TimeUUIDGenerator;
import at.jojokobi.mcutil.UUIDGenerator;
import at.jojokobi.mcutil.generation.GenerationHandler;
import at.jojokobi.mcutil.gui.InventoryGUIHandler;

public class EntityHandler implements Listener {

	public static final String ENTITIES_ELEMENT = "entities";

	private Map<UUID, CustomEntity<?>> entities;
	private Map<Entity, CustomEntity<?>> entitiesByEntity;
	private EntityTypeHandler<CustomEntity<?>> handler = new EntityTypeHandler<>();
	private UUIDGenerator uuidGenerator = new TimeUUIDGenerator();
	private InventoryGUIHandler guiHandler;
	private Plugin plugin;
	private String savefile;

	private List<LegacySaveFolder> legacySaveFolders = new ArrayList<>();

	@Deprecated
	public EntityHandler(Plugin plugin, InventoryGUIHandler guiHandler, String savefile) {
		//Maps
		MultiIndexMap<UUID, CustomEntity<?>> index = new MultiIndexMap<UUID, CustomEntity<?>>();
		this.entities = index;
		this.entitiesByEntity = index.addIndex(e -> e.getEntity());
		
		this.plugin = plugin;
		this.guiHandler = guiHandler;
		this.savefile = savefile;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			// Entity Loop
			@Override
			public void run() {
				for (CustomEntity<?> entity : getEntities()) {
					try {
						entity.loop();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, 5L, 5L);
	}

	public <T extends CustomEntity<?>> T getCustomEntityForEntity(Entity e, Class<T> clazz) {
		CustomEntity<?> entity = entitiesByEntity.get(e);
		return clazz.isInstance(entity) ? clazz.cast(entity) : null;
	}

	public CustomEntity<?> getCustomEntityForEntity(Entity e) {
		return entitiesByEntity.get(e);
	}

	public <T extends CustomEntity<?>> T getEntity(EntityCriteria<T> crit) {
		T entity = null;
		for (Iterator<T> iterator = getEntities(crit.getFilterClass()).iterator(); iterator.hasNext()
				&& entity == null;) {
			T temp = iterator.next();
			if (crit.matches(temp)) {
				entity = temp;
			}
		}
		return entity;
	}

	@EventHandler
	public void onWorldLoad(WorldLoadEvent event) {
		Bukkit.getScheduler().runTask(plugin, () -> {
			for (LegacySaveFolder folder : legacySaveFolders) {
				File file = new File(Bukkit.getWorldContainer(),
						event.getWorld().getName() + File.separator + folder.getSaveFolder());
				if (file.exists() && file.isDirectory()) {
					for (File f : file.listFiles()) {
						if (f.getName().matches("chunk_[0-9-]*_[0-9-]*.xml")) {
							getChunkFromFilename(event.getWorld(), f.getName()).load();
							legacyLoad(f, event.getWorld(), folder);
						}
					}
					file.renameTo(new File(Bukkit.getWorldContainer(),
							event.getWorld().getName() + File.separator + folder.getSaveFolder() + "_old"));
				} else {
					System.out.println("The save folder " + file.getAbsolutePath() + " does not exist!");
				}
			}
		});
	}

	public void addLegacySaveFolder(LegacySaveFolder folder) {
		legacySaveFolders.add(folder);
	}

	private void load(Chunk chunk) {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				File folder = new File(Bukkit.getWorldContainer(),
						chunk.getWorld().getName() + File.separator + savefile);
				// Legacy File
				File legacyFile = new File(folder, GenerationHandler.getSaveName(chunk) + ".yml");
				loadFile(legacyFile);
				legacyFile.renameTo(new File(folder, GenerationHandler.getSaveName(chunk) + "_old.yml"));
				// Plugin files
				for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
					File pluginFile = new File(folder,
							plugin.getName() + File.separator + GenerationHandler.getSaveName(chunk) + ".yml");
					loadFile(pluginFile);
				}
			}
		}, 1L);
	}

	private void loadFile(File file) {
		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(file);
			Object obj = config.get(ENTITIES_ELEMENT);
			if (obj instanceof SerializableMap) {
				SerializableMap entities = (SerializableMap) obj;
				for (Map.Entry<String, ?> e : entities.getData().entrySet()) {
					if (e.getValue() instanceof CustomEntity<?>) {
						try {
							CustomEntity<?> entity = (CustomEntity<?>) e.getValue();
							entity.setHandler(EntityHandler.this);
							addSavedEntity(entity, UUID.fromString(e.getKey()));
						} catch (IllegalArgumentException exc) {
							exc.printStackTrace();
						}
					}
				}
			} else {
				// Legacy
				List<?> entities = config.getList(ENTITIES_ELEMENT, new ArrayList<>());
				for (Object e : entities) {
					if (e instanceof CustomEntity<?>) {
						CustomEntity<?> entity = (CustomEntity<?>) e;
						entity.setHandler(EntityHandler.this);
						addSavedEntity(entity);
					}
				}
			}
		} catch (FileNotFoundException e) {

		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void legacyLoad(File file, World world, LegacySaveFolder folder) {
//		File folder = new File(Bukkit.getWorldContainer(), chunk.getWorld().getName() + File.separator + savefile);
//		folder.mkdirs();
		try (InputStream input = new FileInputStream(file)) {
			// Initialization
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(input);
			document.getDocumentElement().normalize();
			// Load
			NodeList entities = document.getElementsByTagName(CustomEntity.ENTITY_TAG);
			for (int i = 0; i < entities.getLength(); i++) {
				if (entities.item(i).getNodeType() == Node.ELEMENT_NODE) {
					String namespace = ((Element) entities.item(i)).getAttribute(CustomEntity.NAMESPACE_TAG);
					String key = ((Element) entities.item(i)).getAttribute(CustomEntity.KEY_TAG);
					Element entityElement = (Element) entities.item(i);
					Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
						@Override
						public void run() {
							CustomEntityType<? extends CustomEntity<?>> type = handler.getItem(namespace, key);
							CustomEntity<?> entity = null;
							if (type == null) {
								entity = folder.getStandardInstance(world);
							} else {
								entity = type.createInstance(new Location(world, 0, 0, 0), EntityHandler.this);
							}
							entity.legacyParseXML(entityElement);
//							entity.setSave(true);
//							entity.setDespawnTicks(-1);
							addSavedEntity(entity);
						}
					}, 1L);
				}
			}
		} catch (FileNotFoundException e) {
//			System.out.println("No File for Chunk " + chunk.getX() + "/" + chunk.getZ() + " found!");
		} catch (ParserConfigurationException | IOException | SAXException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onSave(WorldSaveEvent event) {
		for (Chunk chunk : event.getWorld().getLoadedChunks()) {
			save(chunk);
		}
	}

	private void save(Chunk chunk) {
		File folder = new File(Bukkit.getWorldContainer(), chunk.getWorld().getName() + File.separator + savefile);
		folder.mkdirs();
//		File file = new File(folder, GenerationHandler.getSaveName(chunk) + ".yml");
		List<CustomEntity<?>> entities = getEntitiesInChunk(chunk);
		Map<UUID, CustomEntity<?>> save = new HashMap<UUID, CustomEntity<?>>();
		for (CustomEntity<?> e : entities) {
			if (e.isSave()) {
				save.put(getUniqueID(e), e);
			}
		}
		// Plugin Files
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			File pluginFolder = new File(folder, plugin.getName());
			pluginFolder.mkdirs();
			File file = new File(pluginFolder, GenerationHandler.getSaveName(chunk) + ".yml");
			Map<String, CustomEntity<?>> pluginEntities = new HashMap<String, CustomEntity<?>>();
			for (Map.Entry<UUID, CustomEntity<?>> e : save.entrySet()) {
				if (e.getValue().getPlugin() == plugin.getClass()) {
					pluginEntities.put(e.getKey() + "", e.getValue());
				}
			}
			if (!pluginEntities.isEmpty()) {
				saveFile(pluginEntities, file);
			} else if (file.exists() && file.isFile()) {
				file.delete();
			}
		}
	}

	private void saveFile(Map<String, CustomEntity<?>> save, File file) {
		FileConfiguration config = new YamlConfiguration();
		config.set(ENTITIES_ELEMENT, new SerializableMap(save));
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Chunk getChunkFromFilename(World world, String filename) {
		String[] split = filename.split("_");
		int x = Integer.parseInt(split[1]);
		int z = Integer.parseInt(split[2].split(Pattern.quote("."))[0]);
		Chunk chunk = world.getChunkAt(x, z);
		return chunk;
	}

	@EventHandler
	public void onEntityMount(EntityMountEvent event) {
		CustomEntity<?> entity = getCustomEntityForEntity(event.getMount());
		if (entity != null) {
			entity.onGetMounted(event);
		}
	}

	@EventHandler
	public void onEntityDismount(EntityDismountEvent event) {
		CustomEntity<?> entity = getCustomEntityForEntity(event.getDismounted());
		if (entity != null) {
			entity.onGetDismounted(event);
		}
	}

	@EventHandler
	public void onPotionSplash(PotionSplashEvent event) {
		for (LivingEntity e : event.getAffectedEntities()) {
			CustomEntity<?> entity = getCustomEntityForEntity(e);
			if (entity != null) {
				entity.onPotionSplash(event);
			}
		}
	}

	@EventHandler
	public void onEntityRegainHealth(EntityRegainHealthEvent event) {
		CustomEntity<?> entity = getCustomEntityForEntity(event.getEntity());
		if (entity != null) {
			entity.onRegainHealth(event);
		}
	}

//	public void saveEntities (File file) {
//		try (FileOutputStream output = new FileOutputStream(file)){
//			//Initialization
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			factory.setNamespaceAware(true);
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			Document document = builder.newDocument();
//			//Root-Element
//			Element element = document.createElement(ENTITIES_ELEMENT);
//			element.setAttribute("xmlns", "https://jojokobi.lima-city.de/mcutil");
//			element.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
//			element.setAttribute("xsi:schemaLocation", "https://jojokobi.lima-city.de/mcutil https://jojokobi.lima-city.de/mcutil/entities");
//			document.appendChild(element);
//			//Entities
//			for (T entity : entities) {
//				if (entity.isSave()) {
//					element.appendChild(entity.saveToXML(document));
//				}
//			}
//			//Save to File
//			TransformerFactory transformerFactory = TransformerFactory.newInstance();
//			Transformer transformer = transformerFactory.newTransformer();
//			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//			DOMSource source = new DOMSource(document);
//			StreamResult result = new StreamResult(output);
//			transformer.transform(source, result);
//		}
//		catch (ParserConfigurationException | TransformerException | IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void loadEntities (File file) {
////		System.out.println("loading");
//		try (InputStream input = new FileInputStream(file)) {
//			//Initialization
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			factory.setNamespaceAware(true);
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			Document document = builder.parse(input);
//			document.getDocumentElement().normalize();
//			//Load
//			NodeList entities = document.getElementsByTagName(CustomEntity.ENTITY_TAG);
//			for (int i = 0; i < entities.getLength(); i++) {
//				if (entities.item(i).getNodeType() == Node.ELEMENT_NODE) {
//					T entity = getStandardInstance();
//					entity.parseXML((Element) entities.item(i));
//					entity.setSave(true);
//					entity.setDespawnTicks(-1);
//					addEntity(entity);
//				}
//			}
//		}
//		catch (ParserConfigurationException | IOException | SAXException e) {
//			e.printStackTrace();
//		}
//	}
//	

	public UUID addEntity(CustomEntity<?> entity) {
		UUID uuid = null;
		if (entity != null && entity.canSpawn() && !entities.containsValue(entity)) {
			entity.setHandler(this);
			entity.spawn();
			RemovalHandler.markForRemoval(entity.getEntity());
			uuid = uuidGenerator.nextUUID();
			entities.put(uuid, entity);
		}
		return uuid;
	}

	private void addEntity(CustomEntity<?> entity, UUID uuid) {
		if (entity != null && entity.canSpawn() && !entities.containsValue(entity)) {
			entity.setHandler(this);
			entity.spawn();
			RemovalHandler.markForRemoval(entity.getEntity());
			entities.put(uuid, entity);
		}
	}

	public UUID addSavedEntity(CustomEntity<?> entity) {
		entity.setSave(true);
		entity.setDespawnTicks(-1);
		return addEntity(entity);
	}

	public void addSavedEntity(CustomEntity<?> entity, UUID uuid) {
		entity.setSave(true);
		entity.setDespawnTicks(-1);
		addEntity(entity, uuid);
	}

	public void removeEntity(UUID uuid) {
		entities.remove(uuid);
	}

	public UUID getUniqueID(CustomEntity<? extends Entity> entity) {
		for (Map.Entry<UUID, CustomEntity<?>> e : entities.entrySet()) {
			if (e.getValue() == entity) {
				return e.getKey();
			}
		}
		return null;
	}

	public CustomEntity<?> getEntity(UUID uuid) {
		return entities.get(uuid);
	}

	public void removeEntity(CustomEntity<? extends Entity> entity) {
		entities.remove(getUniqueID(entity));
	}

	public <T extends CustomEntity<?>> List<T> getEntities(Class<T> clazz) {
		ArrayList<T> entities = new ArrayList<>();
		for (CustomEntity<?> entity : getEntities()) {
			if (clazz.isAssignableFrom(entity.getClass())) {
				entities.add(clazz.cast(entity));
			}
		}
		return entities;
	}

	public List<CustomEntity<?>> getEntities() {
		return new ArrayList<CustomEntity<?>>(entities.values());
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		CustomEntity<?> entity = getCustomEntityForEntity(event.getEntity());
		if (entity != null) {
			entity.onDamage(event);
		}
	}

	@EventHandler
	public void onArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
		passInteractEvent(event);
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractAtEntityEvent event) {
		passInteractEvent(event);
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		CustomEntity<?> entity = getCustomEntityForEntity(event.getDamager());
		if (entity != null) {
			entity.onDamageOther(event);
		}
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if (event.getEntity().getShooter() instanceof Entity) {
			CustomEntity<?> entity = getCustomEntityForEntity((Entity) event.getEntity().getShooter());
			if (entity != null) {
				entity.onProjectileHit(event);
			}
		}
	}

	protected void passInteractEvent(PlayerInteractEntityEvent event) {
		for (CustomEntity<?> entity : getEntities()) {
			if (entity.getEntity() == event.getRightClicked()) {
				entity.onInteract(event);
			}
		}
	}

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {
		load(event.getChunk());
//		for (T entity : entities) {
//			if (!entity.isLoaded() && entity.getLocation().getChunk() == event.getChunk()) {
//				entity.spawn();
//			}
//		}
	}

	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event) {
		save(event.getChunk());
		removeEntities(Arrays.asList(event.getChunk().getEntities()), false);
//		for (Entity entity : event.getChunk().getEntities()) {
//			for (T e : getEntities()) {
//				if (entity == e.getEntity() && !e.canDespawn()) {
//					e.setLoaded(false);
//					e.getEntity().getLocation().getChunk().load();
//					e.getEntity().remove();
//				}
//			}
//		}
	}

	@EventHandler
	public void onWorldUnload(WorldUnloadEvent event) {
//		removeEntities(event.getWorld().getEntities(),true);
	}

	@EventHandler
	public void onPluginEnabled(PluginEnableEvent event) {
//		if (event.getPlugin().equals(this.plugin)) {
//			loadEntities(savefile);
//		}
	}

	@EventHandler
	public void onPluginDisabled(PluginDisableEvent event) {
		if (event.getPlugin().equals(this.plugin)) {
//			saveEntities(savefile);
			// Save all chunks
			for (World world : Bukkit.getWorlds()) {
				for (Chunk chunk : world.getLoadedChunks()) {
					save(chunk);
				}
			}
			// Remove entities
			for (CustomEntity<?> e : getEntities()) {
				e.delete();
			}
		}
	}

	@EventHandler
	public void onTransform(EntityTransformEvent event) {
		CustomEntity<?> entity = getCustomEntityForEntity(event.getEntity());
		if (entity != null) {
			entity.onTransform(event);
		}
	}
	
	@EventHandler
	public void onEntityPortal(EntityPortalEvent event) {
		CustomEntity<?> entity = getCustomEntityForEntity(event.getEntity());
		if (entity != null) {
			entity.onPortalTeleport(event);
		}
	}

	public List<CustomEntity<?>> getEntitiesInChunk(Chunk chunk) {
		List<CustomEntity<?>> entities = new ArrayList<>();
		for (Entity entity : chunk.getEntities()) {
			CustomEntity<?> found = getCustomEntityForEntity(entity);
			if (found != null) {
				entities.add(found);
			}
		}
		return entities;
	}

	public List<CustomEntity<?>> getEntitiesInChunks(World world, int x, int z, int width, int length) {
		List<CustomEntity<?>> entities = new ArrayList<>();
		for (int i = x; i < x + width; i++) {
			for (int j = z; j < z + length; j++) {
				Chunk chunk = world.getChunkAt(i, j);
				for (Entity entity : chunk.getEntities()) {
					CustomEntity<?> found = getCustomEntityForEntity(entity);
					if (found != null) {
						entities.add(found);
					}
				}
			}
		}
		return entities;
	}

	private void removeEntities(List<Entity> entities, boolean notSaved) {
		for (Entity entity : entities) {
			for (CustomEntity<?> e : getEntities()) {
				if (entity == e.getEntity()) {
					if (notSaved && e.isSave()) {

					} else {
						e.delete();
					}
				}
			}
		}
	}

	public InventoryGUIHandler getGuiHandler() {
		return guiHandler;
	}

	public String getSavefile() {
		return savefile;
	}

	public void setSavefile(String savefile) {
		this.savefile = savefile;
	}

	public EntityTypeHandler<CustomEntity<?>> getHandler() {
		return handler;
	}

	public void runTaskLater(Runnable task, long delay) {
		Bukkit.getScheduler().runTaskLater(plugin, task, delay);
	}

	public static abstract class LegacySaveFolder {

		private String saveFolder;

		public LegacySaveFolder(String saveFolder) {
			super();
			this.saveFolder = saveFolder;
		}

		public String getSaveFolder() {
			return saveFolder;
		}

		public abstract CustomEntity<?> getStandardInstance(World world);

	}

}
