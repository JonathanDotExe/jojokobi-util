package at.jojokobi.mcutil.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

import at.jojokobi.mcutil.generation.population.BlockModifier;
import at.jojokobi.mcutil.generation.population.Ore;
import at.jojokobi.mcutil.generation.population.Structure;

public final class TerrainGenUtil {
	
	public static final int CHUNK_WIDTH = 16;
	public static final int CHUNK_LENGTH = 16;
	public static final int CHUNK_HEIGHT = 256;
	
	private TerrainGenUtil() {
		
	}
	
	public static int getChunkX (Location location) {
		return location.getBlockX()/CHUNK_WIDTH;
	}
	
	public static int getChunkZ (Location location) {
		return location.getBlockZ()/CHUNK_LENGTH;
	}
	
	public static boolean isInChunk (Location loc, Chunk chunk) {
		return getChunkX(loc) == chunk.getX() && getChunkZ(loc) == chunk.getZ();
	}
	
	public static List<BlockState> addLayerStates (Location place, Material block, BlockModifier modifier, int width, int height, int length) {
		List<BlockState> states = new ArrayList<BlockState> ();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					Location blockplace = new Location (place.getWorld(), x + place.getBlockX(), 0, z + place.getBlockZ());
					blockplace.setY(blockplace.getWorld().getHighestBlockYAt(blockplace));
					BlockState state = blockplace.getBlock().getState();
					state.setType(block);
					modifier.modify(state);
					states.add(state);
				}
			}
		}
		return states;
	}
	
	public static void generateTrees (Location place, int width, int length, int tries, int spotsize, long seed, TreeType tree) {
		Random random = new Random (generateValueBasedSeed(seed, place.getBlockX(), 1, place.getBlockZ())); //FIXME spotsize
		int chance = CHUNK_LENGTH * CHUNK_WIDTH / tries;
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < length; z++) {
				random.setSeed(generateValueBasedSeed(seed, place.getBlockX() + x, 1, place.getBlockZ() + z));
				if (random.nextInt (chance) == 0) {
					Location treeplace = new Location (place.getWorld(), place.getBlockX() + x, 0, place.getBlockZ() + z);
					treeplace.setY(treeplace.getWorld().getHighestBlockYAt(treeplace));
					if (!treeplace.getBlock().getType().isSolid()) {
						treeplace.getBlock().setType(Material.AIR);
					}
					treeplace.getWorld().generateTree(treeplace, tree);
				}
			}
		}
	}
	
	public static void bonemealSpots(Location place, int width, int length, int tries, int spotsize, long seed) {
		Random random = new Random (generateValueBasedSeed(seed, place.getBlockX(), 1, place.getBlockZ())); //FIXME spotsize
		int chance = CHUNK_LENGTH * CHUNK_WIDTH / tries;
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < length; z++) {
				random.setSeed(generateValueBasedSeed(seed, place.getBlockX() + x, 1, place.getBlockZ() + z));
				if (random.nextInt (chance) == 0) {
					Location mealplace = new Location (place.getWorld (), place.getX() + x, 1, place.getZ() + z);
					bonemealPlace (mealplace, generateValueBasedSeed(seed, mealplace.getBlockX(), 1, mealplace.getBlockZ()));
				}
			}
		}
	}
	
	public static void generateStructures (Location place, int width, int length, long seed, Structure structure) {
		int startX = place.getChunk().getX();
		int startZ = place.getChunk().getZ();
		for (int x = 0; x < width/CHUNK_WIDTH; x++) {
			for (int z = 0; z < length/CHUNK_LENGTH; z++) {
				Chunk chunk = place.getWorld().getChunkAt(startX + x, startZ + z);
				if (structure.canGenerate(chunk, seed)) {
					structure.generate(chunk, seed);
				}
			}
		}
	}
	
	public static void generateOres(BlockState state, long seed) {
		Ore.coal.generateOre(state, seed);
		Ore.iron.generateOre(state, seed);
		Ore.gold.generateOre(state, seed);
		Ore.redstone.generateOre(state, seed);
		Ore.diamond.generateOre(state, seed);
		Ore.lapis.generateOre(state, seed);
	}
	
	public static void bonemealPlace (Location place, long seed) {
		Random random = new Random (seed);
		place.setY(place.getWorld().getHighestBlockYAt(place) - 1);
		if (place.getBlock().getType() == Material.GRASS) {
			place.setY(place.getY() + 1);
			int nextInt = random.nextInt(20);
			if (nextInt < 8) {
				place.getBlock().setType (Material.GRASS);
			}
			else if (nextInt < 12) {
				place.getBlock().setType (Material.FERN);
			}
			else if (nextInt < 17) {
				place.getBlock().setType (Material.DANDELION);
			}
		}
	}
	
	public static void killItems(World world) {
		for (Entity entity: world.getEntities()) {
			if (entity instanceof Item) {
				entity.remove();
			}
		}
	}
	
	public static long generateValueBasedSeed (long seed, int x, int y, int z) {
		final long prime = 31;
		long result = 1;
		result = prime * result + (int) (seed ^ (seed >>> 32));
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}
	
	
	
	public static int getTerrainHeight (Location loc) {
		Location place = loc.clone();
		place.setY(loc.getWorld().getSeaLevel() - 1);
		int y = loc.getWorld().getSeaLevel() - 1;
		while (place.getBlock().getType().isSolid() || place.getBlock().getType() == Material.WATER || place.getBlock().getType() == Material.LAVA || place.getBlock().getType() == Material.CAVE_AIR) {
			y++;
			place.setY(y);
		}
		return y;
	}
}

