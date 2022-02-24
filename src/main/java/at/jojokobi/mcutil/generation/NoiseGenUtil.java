package at.jojokobi.mcutil.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import at.jojokobi.mcutil.generation.population.BlockModifier;

@Deprecated
public final class NoiseGenUtil {
	
	private NoiseGenUtil () {
		
	}
	
	public static List<BlockState> generateNoise (Location place, Material block, BlockModifier modifier, int width, int height, int length, int seed, double multiplier) {
		int[][] blocks = calculateNoise (place, width, height, length, seed, multiplier);
		return statesFromArray(blocks, place, height, block, modifier);
	}
	
	public static List<BlockState> generateDoubleNoise (Location place, Material block, BlockModifier modifier, int width, int height, int length, int seed, double multiplier) {
		Random random = new Random (seed);
		int[][] blocks1 = calculateNoise (place, width, height, length, seed, multiplier);
		int[][] blocks2 = calculateNoise (place, width, height, length, random.nextInt(), multiplier);
		int[][] blocks = makeAverageNoise (blocks1, blocks2);
		return statesFromArray(blocks, place, height, block, modifier);
	}
	
	public static int[][] calculateNoise (Location place, int width, int height, int length, int seed, double multiplier) {
		int[][] blocks = new int[width][length];
//		Random random = new Random ();
		SimplexNoiseGenerator generator = new SimplexNoiseGenerator (seed);
		//Make Noise Array
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < length; z++) {
				blocks[x][z] = (int) Math.round(height/2 * generator.noise((x + place.getBlockX()) * multiplier, (z + place.getBlockZ()) * multiplier));
//				random.setSeed(TerrainGenerationUtilities.generateValueBasedSeed(seed, x, 1, z, multiplier));
//				blocks[x][z] = random.nextInt(height) - height/2;
			}
		}
		return blocks;
	}
	
	private static List<BlockState> statesFromArray (int[][] blocks, Location place, int height, Material block, BlockModifier modifier) {
		List<BlockState> states = new ArrayList<BlockState> ();
		for (int x = 0; x < blocks[0].length; x++) {
			for (int z = 0; z < blocks.length; z++) {
				for (int y = -height/2; y < blocks[x][z] ; y++) {
					Location blockplace = new Location (place.getWorld(), x + place.getBlockX(), y + height/2 + place.getBlockY(), z + place.getBlockZ());
					BlockState state = blockplace.getBlock().getState();
					state.setType(block);
					modifier.modify(state);
					states.add(state);
				}
			}
		}
		return states;
	}
	
	private static int[][] makeAverageNoise (int[][] noise1, int[][] noise2) {
		int width = noise1[0].length < noise2[0].length ? noise1[0].length : noise2[0].length;
		int length = noise1.length < noise2.length ? noise1.length : noise2.length;
		//Calculate the average
		int[][] noise = new int[width][length];
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < length; z++) {
				noise[x][z] = (int) Math.round(average (new double[] {noise1[x][z], noise2[x][z]}));
			}
		}
		return noise;
	}
	
	
	private static double average (double[] doubles) {
		double sum = 0.0;
		for (double d : doubles) {
			sum += d;
		}
		double result = sum/doubles.length;
		return result;
	}
	
}
