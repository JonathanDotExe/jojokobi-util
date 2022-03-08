package at.jojokobi.mcutil.io;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public final class XMLUtil {

	public static final String LOCATITON_TAG = "location";
	public static final String X_TAG = "x";
	public static final String Y_TAG = "y";
	public static final String Z_TAG = "z";
	public static final String WORLD_TAG = "world";
	public static final String YAW_TAG = "yaw";
	public static final String PITCH_TAG = "pitch";
	
	public static final String ITEM_TAG = "item";
	public static final String MATERIAL_TAG = "material";
	public static final String DAMAGE_TAG = "damage";
	public static final String AMOUNT_TAG = "amount";
	public static final String NBT_TAG = "nbt";

	private XMLUtil() {

	}

	public static Element locationToXML(Document document, Location location, String tagName) {
		Element element = document.createElement(tagName);
		// World
		Element world = document.createElement(WORLD_TAG);
		world.setTextContent(location.getWorld().getName());
		// X
		Element x = document.createElement(X_TAG);
		x.setTextContent(location.getX() + "");
		// Y
		Element y = document.createElement(Y_TAG);
		y.setTextContent(location.getY() + "");
		// Z
		Element z = document.createElement(Z_TAG);
		z.setTextContent(location.getZ() + "");

		// Yaw
		Element yaw = document.createElement(YAW_TAG);
		yaw.setTextContent(location.getYaw() + "");
		// Pitch
		Element pitch = document.createElement(PITCH_TAG);
		pitch.setTextContent(location.getPitch() + "");
		// Add
		element.appendChild(world);
		element.appendChild(x);
		element.appendChild(y);
		element.appendChild(z);
		element.appendChild(yaw);
		element.appendChild(pitch);
		return element;
	}
	
	public static Element locationToXML(Document document, Location location) {
		return locationToXML(document, location, LOCATITON_TAG);
	}

	public static Location xmlToLocation(Element element) {
		World world = Bukkit.getWorld(element.getElementsByTagName(WORLD_TAG).item(0).getTextContent());
		Location loc = new Location(world, 0, 0, 0);
		// X
		NodeList x = element.getElementsByTagName(X_TAG);
		if (x.getLength() > 0) {
			loc.setX(Double.parseDouble(x.item(0).getTextContent()));
		}
		// Y
		NodeList y = element.getElementsByTagName(Y_TAG);
		if (y.getLength() > 0) {
			loc.setY(Double.parseDouble(y.item(0).getTextContent()));
		}
		// Z
		NodeList z = element.getElementsByTagName(Z_TAG);
		if (z.getLength() > 0) {
			loc.setZ(Double.parseDouble(z.item(0).getTextContent()));
		}
		// Yaw
		NodeList yaw = element.getElementsByTagName(YAW_TAG);
		if (yaw.getLength() > 0) {
			loc.setYaw(Float.parseFloat(yaw.item(0).getTextContent()));
		}
		// Yaw
		NodeList pitch = element.getElementsByTagName(PITCH_TAG);
		if (pitch.getLength() > 0) {
			loc.setPitch(Float.parseFloat(pitch.item(0).getTextContent()));
		}
//		System.out.println(loc);
		return loc;
	}

}
