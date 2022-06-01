package at.jojokobi.mcutil.building;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import at.jojokobi.mcutil.TypedMap;

public class BuildingMark implements ConfigurationSerializable {
	
	private int x;
	private int y;
	private int z;
	
	private String mark;

	public BuildingMark(int x, int y, int z, String mark) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.mark = mark;;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("x", x);
		map.put("y", y);
		map.put("z", z);
		map.put("mark", mark);
		return map;
	}
	
	public static BuildingMark deserialize(Map<String, Object> map) {
		TypedMap m = new TypedMap(map);
		return new BuildingMark(m.getInt("x"), m.getInt("y"), m.getInt("z"), m.getString("mark"));
	}

}
