package at.jojokobi.mcutil.building;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import at.jojokobi.mcutil.TypedMap;
import at.jojokobi.mcutil.generation.BasicGenUtil;

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
					Block block = loc.getBlock().getRelative(x, y, z);
					if (block.getState() instanceof Sign) {
						Sign sign = (Sign) block.getState();
						//Mark
						if (sign.getLine(0).length() > 0 && sign.getLine(3).length() > 0 && sign.getLine(0).chars().allMatch(c -> c == '#') && sign.getLine(3).chars().allMatch(c -> c == '#')) {
							building.marks.add(new BuildingMark(x, y, z, sign.getLine(1)));
							isBlock = false;
						}
					}
					//Block
					if (isBlock && block.getType() != Material.AIR && block.getType() != Material.CAVE_AIR) {
						building.blocks.add(new BuildingBlock(x, y, z, block.getBlockData()));
					}
				}
			}
		}
		return building;
	}
	
	public static Building loadBuilding(InputStream stream) {
		Building building = null;
		YamlConfiguration config = new YamlConfiguration();
		try (InputStreamReader reader = new InputStreamReader(stream)){
			config.load(reader);
			building = config.getSerializable("building", Building.class);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			building = new Building();
		}
		return building;
	}
	
	public void buildWithMarkSigns(Location loc, boolean physicsUpdate) {
		buildWithMarkSigns(loc, 0, physicsUpdate);
	}
	
	public void buildWithMarkSigns(Location loc, int rotations, boolean physicsUpdate) {
		build(loc, (place, str) -> {
			place.getBlock().setType(Material.OAK_SIGN);
			Sign sign = (Sign) place.getBlock().getState();
			sign.setLine(0, "####");
			sign.setLine(1, str);
			sign.setLine(3, "####");
			sign.update(false, false);
		}, rotations, physicsUpdate);
	}
	
	public void build(Location loc, BiConsumer<Location, String> markInterpreter, int rotations, boolean physicsUpdate) {
		//rotations = how many 90 degree rotations should be applied
		//Blocks
		for (BuildingBlock block : blocks) {
			BlockData data = block.getBlock().clone();
			if (data instanceof Rotatable) {
				((Rotatable) data).setRotation(BasicGenUtil.rotateBlockface90Degerees(((Rotatable) data).getRotation(), rotations));
			}
			BasicGenUtil.getRotatedRelative(loc.getBlock(), block.getX(), block.getY(), block.getZ(), width, length, rotations).setBlockData(data, physicsUpdate);
		}
		//Marks
		for (BuildingMark mark : marks) {
			markInterpreter.accept(BasicGenUtil.getRotatedRelative(loc, mark.getX(), mark.getY(), mark.getZ(), width, length, rotations), mark.getMark());
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
