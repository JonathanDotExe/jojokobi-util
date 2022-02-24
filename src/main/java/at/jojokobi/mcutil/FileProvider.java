package at.jojokobi.mcutil;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FileProvider<T extends Identifiable> implements ItemProvider<T> {

	public static final String FILES_XML = "/files.xml";
	public static final String FILE_ELEMENT = "file";

	private FileLoader<T> loader;
	private List<InputStream> inputs = new ArrayList<>();
	private List<Closeable> closeables = new ArrayList<>();

	public FileProvider(FileLoader<T> loader, List<InputStream> inputs, List<Closeable> closeables) {
		this.loader = loader;
		this.inputs = inputs;
		this.closeables = closeables;
	}

	public static <T extends Identifiable> FileProvider<T> fromFolder(FileLoader<T> loader, File folder)
			throws FileNotFoundException {
		ArrayList<InputStream> inputs = new ArrayList<>();
		for (File file : folder.listFiles()) {
			if (file.isFile()) {
				if (file.getName().endsWith(".xml")) {
					inputs.add(new FileInputStream(file));
				}
			}
		}
		return new FileProvider<>(loader, inputs, Arrays.asList());
	}

	public static <T extends Identifiable> FileProvider<T> fromFolder(FileLoader<T> loader, String filesXML)
			throws ParserConfigurationException, IOException, SAXException {
		ArrayList<InputStream> inputs = new ArrayList<>();
		System.out.println("Files XML: " + filesXML + FILES_XML);
		try (InputStream input = loader.getClass().getResourceAsStream(filesXML + FILES_XML)) {
			// Initialization
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(input);
			document.getDocumentElement().normalize();
			// Load
			NodeList files = document.getElementsByTagName(FILE_ELEMENT);
			for (int i = 0; i < files.getLength(); i++) {
				Node file = files.item(i);
				String fileName = file.getTextContent();
				InputStream in = loader.getClass().getResourceAsStream(filesXML + "/" + fileName);
				if (in != null) {
					inputs.add(in);
					System.out.println(fileName);
				} else {
					System.out.println("Skipping " + (filesXML + "/" + fileName) + " because the file doesn't exist!");
				}
			}
		}
		return new FileProvider<>(loader, inputs, Arrays.asList());
	}

	public static <T extends Identifiable> FileProvider<T> fromJar(FileLoader<T> loader, File jarFile, String subpath)
			throws IOException {
		List<InputStream> inputs = new ArrayList<>();
		JarFile jar = new JarFile(jarFile);
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			System.out.println(entry.getName());
			if (!entry.isDirectory() && entry.getName().startsWith(subpath)) {
				System.out.println("loaded");
				inputs.add(jar.getInputStream(entry));
			}
		}
		return new FileProvider<>(loader, inputs, Arrays.asList(jar));
	}

//	public void loadAll (URL from)  {
//		try {
//			URL filesURL = new URL(from, FILES_XML);
//			try (InputStream input = filesURL.openStream()) {
//				//Initialization
//				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//				factory.setNamespaceAware(true);
//				DocumentBuilder builder = factory.newDocumentBuilder();
//				Document document = builder.parse(input);
//				document.getDocumentElement().normalize();
//				
//				//Load
//				NodeList files = document.getElementsByTagName(FILE_ELEMENT);
//				for (int i = 0; i < files.getLength(); i++) {
//					Node file = files.item(i);
//					String fileName = file.getTextContent();
//					URL url = new URL(from, fileName);
//					System.out.println(url.toString());
//					addItem(load(url.openStream()));
//				}
//			}
//			catch (ParserConfigurationException | IOException | SAXException e) {
//				e.printStackTrace();
//			}
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void loadAll (String from)  {
//		try (InputStream input = getClass().getResourceAsStream(from + FILES_XML)) {
//			//Initialization
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			factory.setNamespaceAware(true);
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			Document document = builder.parse(input);
//			document.getDocumentElement().normalize();
//			//Load
//			NodeList files = document.getElementsByTagName(FILE_ELEMENT);
//			for (int i = 0; i < files.getLength(); i++) {
//				Node file = files.item(i);
//				String fileName = file.getTextContent();
//				System.out.println(fileName);
//				addItem(load(getClass().getResourceAsStream(from + "/"+ fileName)));
//			}
//		}
//		catch (ParserConfigurationException | IOException | SAXException e) {
//			e.printStackTrace();
//		}
//	}

	@Override
	public List<T> provide() {
		List<T> loaded = new ArrayList<>();
		for (InputStream in : inputs) {
			try (InputStream input = in) {
				loaded.add(loader.load(input));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		for (Closeable closeable : closeables) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return loaded;
	}

}
