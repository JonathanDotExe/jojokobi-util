package at.jojokobi.mcutil.generation.population;

import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.Material;

public interface VillagePathGenerator {
	
	public void generatePath (Location place, int width, int length, boolean up, boolean right, boolean bottom, boolean left, Function<Material, Material> blockFunction);

}
