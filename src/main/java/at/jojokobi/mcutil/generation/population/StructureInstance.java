package at.jojokobi.mcutil.generation.population;

import org.bukkit.Location;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class StructureInstance<T extends Structure> {

	public static final String STRUCTURE_ID_ELEMENT = "id";
	public static final String STRUCTURE_X_ELEMENT = "x";
	public static final String STRUCTURE_Y_ELEMENT = "y";
	public static final String STRUCTURE_Z_ELEMENT = "z";
	public static final String STRUCTURE_WIDTH_ELEMENT = "width";
	public static final String STRUCTURE_HEIGHT_ELEMENT = "height";
	public static final String STRUCTURE_LENGTH_ELEMENT = "length";
	
	private T structure;
	private Location location;
	private int width = 0;
	private int height = 0;
	private int length = 0;

	public StructureInstance(T structure, Location location, int width, int height, int length) {
		super();
		this.structure = structure;
		this.location = location.clone();
		this.width = width;
		this.height = height;
		this.length = length;
	}
	
	public void save(Document document, Element element) {
		Element id = document.createElement(STRUCTURE_ID_ELEMENT);
		id.setTextContent(structure.getIdentifier());
		Element x = document.createElement(STRUCTURE_X_ELEMENT);
		x.setTextContent(location.getBlockX() + "");
		Element y = document.createElement(STRUCTURE_Y_ELEMENT);
		y.setTextContent(location.getBlockY() + "");
		Element z = document.createElement(STRUCTURE_Z_ELEMENT);
		z.setTextContent(location.getBlockZ() + "");
		Element width = document.createElement(STRUCTURE_WIDTH_ELEMENT);
		width.setTextContent(getWidth() + "");
		Element height = document.createElement(STRUCTURE_HEIGHT_ELEMENT);
		height.setTextContent(getHeight() + "");
		Element length = document.createElement(STRUCTURE_LENGTH_ELEMENT);
		length.setTextContent(getLength() + "");
		element.appendChild(id);
		element.appendChild(x);
		element.appendChild(y);
		element.appendChild(z);
		element.appendChild(width);
		element.appendChild(height);
		element.appendChild(length);
	}

	public Location getLocation () {
		return location.clone();
	}
	
	public int getX() {
		return location.getBlockX();
	}
	
	public int getY() {
		return location.getBlockY();
	}
	
	public int getZ() {
		return location.getBlockZ();
	}

	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
	}


	public int getLength() {
		return length;
	}

	public T getStructure() {
		return structure;
	}
	
	public void parseXML (Element element) {
		//Location
		// X
		NodeList x = element.getElementsByTagName(STRUCTURE_X_ELEMENT);
		if (x.getLength() > 0) {
			location.setX(Integer.parseInt(x.item(0).getTextContent()));
		}
		// Y
		NodeList y = element.getElementsByTagName(STRUCTURE_Y_ELEMENT);
		if (y.getLength() > 0) {
			location.setY(Integer.parseInt(y.item(0).getTextContent()));
		}
		// Z
		NodeList z = element.getElementsByTagName(STRUCTURE_Z_ELEMENT);
		if (z.getLength() > 0) {
			location.setZ(Integer.parseInt(z.item(0).getTextContent()));
		}
		//Width
		NodeList width = element.getElementsByTagName(STRUCTURE_WIDTH_ELEMENT);
		if (width.getLength() > 0) {
			this.width = Integer.parseInt(width.item(0).getTextContent());
		}
		//Height
		NodeList height = element.getElementsByTagName(STRUCTURE_HEIGHT_ELEMENT);
		if (height.getLength() > 0) {
			this.height = Integer.parseInt(height.item(0).getTextContent());
		}
		//Length
		NodeList length = element.getElementsByTagName(STRUCTURE_LENGTH_ELEMENT);
		if (length.getLength() > 0) {
			this.length = Integer.parseInt(length.item(0).getTextContent());
		}
	}

}
