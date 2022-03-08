package at.jojokobi.mcutil;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public final class VectorUtil {

	private VectorUtil() {
		
	}
	
	public static Vector interpolate (Vector vector1, Vector vector2, double progress) {
		Vector vector = new Vector();
		vector.setX(interpolate(vector1.getX(), vector2.getX(), progress));
		vector.setY(interpolate(vector1.getY(), vector2.getY(), progress));
		vector.setZ(interpolate(vector1.getZ(), vector2.getZ(), progress));
		return vector;
	}
	
	public static Location interpolate (Location place1, Location place2, double progress) {
		Location place = interpolate(place1.toVector(), place2.toVector(), progress).toLocation(place1.getWorld());
		place.setDirection(interpolate(place1.getDirection(), place2.getDirection(), progress));
		return place;
	}
	
	public static double interpolate (double num1, double num2, double progress) {
		return num1 + (num2 - num1) * progress;
	}

}
