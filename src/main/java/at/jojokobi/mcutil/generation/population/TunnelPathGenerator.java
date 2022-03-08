package at.jojokobi.mcutil.generation.population;

import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.Material;

public class TunnelPathGenerator implements VillagePathGenerator{
	
	private int thickness = 4;
	

	public TunnelPathGenerator(int thickness) {
		super();
		this.thickness = thickness;
	}

	public TunnelPathGenerator() {
		
	}

	public int getThickness() {
		return thickness;
	}

	public void setThickness(int thickness) {
		this.thickness = thickness;
	}

	@Override
	public void generatePath(Location place, int width, int length, boolean up, boolean right, boolean bottom,
			boolean left, Function<Material, Material> blockFunction) {
		int startX = width/2 - thickness/2;
		int startZ = length/2 - thickness/2;
		
		//Center
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < length; z++) {
				if ((x >= startX && x < thickness + startX && ((up && z < length/2) || (bottom && z > length/2) || (z >= startZ && z < thickness + startZ))) ||
						(z >= startZ && z < thickness + startZ && ((left && x < width/2) || (right && x > width/2)))) {
					for (int y = 0; y < thickness; y++) {
						Location blockPlace = place.clone().add(x, y, z);
						blockPlace.getBlock().setType(blockFunction.apply(blockPlace.getBlock().getType()));
					}
				}
			}
		}
	}
	
}
