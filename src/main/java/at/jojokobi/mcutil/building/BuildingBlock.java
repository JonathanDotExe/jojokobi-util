package at.jojokobi.mcutil.building;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import at.jojokobi.mcutil.TypedMap;

public class BuildingBlock implements ConfigurationSerializable {
	
	private int x;
	private int y;
	private int z;
	
	private BlockData block;

	public BuildingBlock(int x, int y, int z, BlockData block) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.block = block;
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

	public BlockData getBlock() {
		return block;
	}

	public void setBlock(BlockData block) {
		this.block = block;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("x", x);
		map.put("y", y);
		map.put("z", z);
		map.put("block", block.getAsString());
		return map;
	}
	
	public static BuildingBlock deserialize(Map<String, Object> map) {
		TypedMap m = new TypedMap(map);
		return new BuildingBlock(m.getInt("x"), m.getInt("y"), m.getInt("z"), Bukkit.getServer().createBlockData(m.getString("block")));
	}

}
