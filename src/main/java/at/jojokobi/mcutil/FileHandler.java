package at.jojokobi.mcutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import at.jojokobi.mcutil.Identifiable;

@Deprecated
public abstract class FileHandler<T extends Identifiable> extends Handler<T>{
	
	public static final String FILES_XML = "/files.xml";
	public static final String FILE_ELEMENT = "file";
	
	public void loadAll (File from) {
		for (File file : from.listFiles()) {
			if (file.isFile()) {
				if (file.getName().endsWith(".xml")) {
					try {
						addItem(load(new FileInputStream(file)));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void loadAll (URL from)  {
		try {
			URL filesURL = new URL(from, FILES_XML);
			try (InputStream input = filesURL.openStream()) {
				//Initialization
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setNamespaceAware(true);
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(input);
				document.getDocumentElement().normalize();
				
				//Load
				NodeList files = document.getElementsByTagName(FILE_ELEMENT);
				for (int i = 0; i < files.getLength(); i++) {
					Node file = files.item(i);
					String fileName = file.getTextContent();
					URL url = new URL(from, fileName);
					System.out.println(url.toString());
					addItem(load(url.openStream()));
				}
			}
			catch (ParserConfigurationException | IOException | SAXException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public void loadAll (String from)  {
		try (InputStream input = getClass().getResourceAsStream(from + FILES_XML)) {
			//Initialization
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(input);
			document.getDocumentElement().normalize();
			//Load
			NodeList files = document.getElementsByTagName(FILE_ELEMENT);
			for (int i = 0; i < files.getLength(); i++) {
				Node file = files.item(i);
				String fileName = file.getTextContent();
				System.out.println(fileName);
				addItem(load(getClass().getResourceAsStream(from + "/"+ fileName)));
			}
		}
		catch (ParserConfigurationException | IOException | SAXException e) {
			e.printStackTrace();
		}
	}
	
	public abstract T load(InputStream input);
	
}
