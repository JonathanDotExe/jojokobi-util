package at.jojokobi.mcutil.building;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class Building implements ConfigurationSerializable{

	private List<BuildingBlock> blocks;
	private List<BuildingMark> marks;
	
	
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
		return null;
	}
	
	
}
