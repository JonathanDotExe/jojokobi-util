package at.jojokobi.mcutil.generation;

import java.util.List;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import at.jojokobi.mcutil.generation.population.BlockModifier;;

public final class BasicGenUtil {
	
	private BasicGenUtil () {
		
	}
	
	public static void generateCube (Location place, Material block, int width, int height, int length) {
		generateCube(place, block, null, width, height, length);
	}
	
	public static void generateCube (Location place, Material block, BlockModifier modifier, int width, int height, int length) {
		generateCube(place, block, b -> true, modifier, width, height, length);
	}
	
	public static void generateCube (Location place, Material block, Material replace, BlockModifier modifier, int width, int height, int length) {
		generateCube(place, block, b -> b.getType() == replace || replace == null, modifier, width, height, length);
	}
	
	public static void generateCube (Location place, Material block, Predicate<Block> replace, BlockModifier modifier, int width, int height, int length) {
		generateCube(place, block, replace, modifier, width, height, length, true);
	}
	
	public static void generateCube (Location place, Material block, Predicate<Block> replace, BlockModifier modifier, int width, int height, int length, boolean applyPhysics) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					Location blockplace = new Location (place.getWorld(), x + place.getBlockX(), y + place.getBlockY(), z + place.getBlockZ());
					if (replace.test(blockplace.getBlock())) {
						blockplace.getBlock().setType(block, applyPhysics);
						if (modifier != null) {
							BlockState state = blockplace.getBlock().getState();
							state.setType(block);
							modifier.modify(state);
							state.update(true, applyPhysics);
						}
					}
				}
			}
		}
	}
	
	public static void updateBlocks (List<BlockState> states) {
		for (BlockState state : states) {
			state.update(true);
		}
	}
	
}
