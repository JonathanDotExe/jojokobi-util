package at.jojokobi.mcutil.generation.population;

import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.Material;

import at.jojokobi.mcutil.generation.TerrainGenUtil;

public class CleanPathGenerator implements VillagePathGenerator{
	
	private int thickness = 4;
	

	public CleanPathGenerator(int thickness) {
		super();
		this.thickness = thickness;
	}

	public CleanPathGenerator() {
		
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
					Location blockPlace = place.clone().add(x, 0, z);
					blockPlace.setY(TerrainGenUtil.getTerrainHeight(blockPlace) - 1);
					blockPlace.getBlock().setType(blockFunction.apply(blockPlace.getBlock().getType()));
				}
			}
		}
	}
	
}
