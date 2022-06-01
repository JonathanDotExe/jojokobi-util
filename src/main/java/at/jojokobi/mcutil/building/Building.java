package at.jojokobi.mcutil.building;

import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class Building implements ConfigurationSerializable{

	private List<BuildingBlock> blocks;
	private List<BuildingMark> marks;

	@Override
	public Map<String, Object> serialize() {
		return null;
	}
	
	
}
