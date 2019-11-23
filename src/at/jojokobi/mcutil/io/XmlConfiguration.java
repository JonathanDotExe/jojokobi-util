package at.jojokobi.mcutil.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlConfiguration extends FileConfiguration{

	public XmlConfiguration() {
		
	}

	@Override
	protected String buildHeader() {
		return "<!--" + options().header() + " !-->";
	}

	@Override
	public void loadFromString(String string) throws InvalidConfigurationException {
		InputStream input = new ByteArrayInputStream(string.getBytes());
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(input);
			document.getDocumentElement().normalize();
			
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new InvalidConfigurationException(e);
		}
	}

	@Override
	public String saveToString() {
		return null;
	}

}
