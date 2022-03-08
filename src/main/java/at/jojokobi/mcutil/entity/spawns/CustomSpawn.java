package at.jojokobi.mcutil.entity.spawns;

import java.util.List;

import org.bukkit.Location;

import at.jojokobi.mcutil.Identifiable;
import at.jojokobi.mcutil.entity.CustomEntity;

public interface CustomSpawn extends Identifiable{
	
	public List<CustomEntity<?>> spawn (Location loc);

}
