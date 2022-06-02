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

	private List<BuildingBlock> blocks = new ArrayList<BuildingBlock>();
	private List<BuildingMark> marks = new ArrayList<BuildingMark>();
	
	
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

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("blocks", blocks);
		map.put("marks", marks);
		return map;
	}
	
	public static Building deserialize(Map<String, Object> map) {
		TypedMap m = new TypedMap(map);
		Building building = new Building();
		building.blocks.addAll(m.getList("blocks", BuildingBlock.class));
		building.marks.addAll(m.getList("marks", BuildingMark.class));

		return building;
	}
	
	
}
