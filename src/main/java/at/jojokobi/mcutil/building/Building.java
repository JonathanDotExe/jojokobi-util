package at.jojokobi.mcutil.building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import at.jojokobi.mcutil.TypedMap;

public class Building implements ConfigurationSerializable{

	private int width;
	private int height;
	private int length;
	private List<BuildingBlock> blocks = new ArrayList<BuildingBlock>();
	private List<BuildingMark> marks = new ArrayList<BuildingMark>();
	
	public static Building createBuilding(Location loc, int width, int height, int length) {
		Building building = new Building();
		building.width = width;
		building.height = height;
		building.length = length;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					boolean isBlock = true;
					if (loc.getBlock().getState() instanceof Sign) {
						Sign sign = (Sign) loc.getBlock().getState();
						//Mark
						if ("####".equals(sign.getLine(0)) && "####".equals(sign.getLine(3))) {
							building.marks.add(new BuildingMark(x, y, z, sign.getLine(1)));
							isBlock = false;
						}
					}
					//Block
					if (isBlock && loc.getBlock().getType() != Material.AIR && loc.getBlock().getType() != Material.CAVE_AIR) {
						building.blocks.add(new BuildingBlock(x, y, z, loc.getBlock().getBlockData()));
					}
				}
			}
		}
		return building;
	}
	
	public void buildWithMarkSigns(Location loc, boolean physicsUpdate) {
		build(loc, (place, str) -> {
			place.getBlock().setType(Material.OAK_SIGN);
			Sign sign = (Sign) place.getBlock().getState();
			sign.setLine(0, "####");
			sign.setLine(1, str);
			sign.setLine(3, "####");
		}, physicsUpdate);
	}
	
	public void build(Location loc, BiConsumer<Location, String> markInterpreter, boolean physicsUpdate) {
		//Blocks
		for (BuildingBlock block : blocks) {
			loc.getBlock().getRelative(block.getX(), block.getY(), block.getZ()).setBlockData(block.getBlock(), physicsUpdate);
		}
		//Marks
		for (BuildingMark mark : marks) {
			markInterpreter.accept(loc.clone().add(mark.getX(), mark.getY(), mark.getZ()), mark.getMark());
		}
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("blocks", blocks);
		map.put("marks", marks);
		map.put("width", width);
		map.put("height", height);
		map.put("length", length);
		return map;
	}
	
	public static Building deserialize(Map<String, Object> map) {
		TypedMap m = new TypedMap(map);
		Building building = new Building();
		building.blocks.addAll(m.getList("blocks", BuildingBlock.class));
		building.marks.addAll(m.getList("marks", BuildingMark.class));
		building.width = m.getInt("width");
		building.height = m.getInt("height");
		building.length = m.getInt("length");
		
		return building;
	}
	
	
}
