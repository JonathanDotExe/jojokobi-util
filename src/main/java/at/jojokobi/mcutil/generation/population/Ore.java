package at.jojokobi.mcutil.generation.population;

import static at.jojokobi.mcutil.generation.TerrainGenUtil.*;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

public class Ore {
	
	private Material material = Material.COAL_ORE;
	private BlockModifier modifier = BlockModifier.getEmptyModifier();
	private int size = 17;
	private int tries = 20;
	private int minHeight = 0;
	private int maxHeight = 128;
	
	public static final Ore coal = new Ore (Material.COAL_ORE, BlockModifier.getEmptyModifier(), 17, 20, 0, 128);
	public static final Ore iron = new Ore (Material.IRON_ORE, BlockModifier.getEmptyModifier(), 9, 20, 0, 64);
	public static final Ore gold = new Ore (Material. GOLD_ORE, BlockModifier.getEmptyModifier(), 8, 2, 0, 16);
	public static final Ore redstone = new Ore (Material.REDSTONE_ORE, BlockModifier.getEmptyModifier(), 8, 8, 0, 16);
	public static final Ore diamond = new Ore (Material.DIAMOND_ORE, BlockModifier.getEmptyModifier(), 8, 1, 0, 16);
	public static final Ore lapis = new Ore (Material.LAPIS_ORE, BlockModifier.getEmptyModifier(), 7, 1, 0, 32);
	
	
	public Ore(Material material, BlockModifier modifier, int size, int tries, int minHeight, int maxHeight) {
		this.material = material;
		this.modifier = modifier;
		this.size = size;
		this.tries = tries;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}
	
/*	public void generateOres (Location place, long seed) {
		Random random = new Random (seed);
		int chance = width*width/chance;
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < width; z++) {
				Location oreplace = new Location (place.getWorld(), place.getBlockX() + x, random.nextInt(maxHeight - minHeight) + minHeight, place.getBlockZ() + z);
				if (oreplace.getBlock().getType().isSolid()) {
					if (random.nextInt (chance) == 0) {
						generateOre(oreplace.clone(), seed * random.nextInt(Math.abs(random.nextInt())));
					}
				}
			}
		}
	}*/
	
	public void generateOre (BlockState state, long seed) {
		Location place = state.getLocation();
		long randseed = generateValueBasedSeed(seed, place.getBlockX(), place.getBlockY(), place.getBlockZ()); //FIXME size
		Random random = new Random (randseed);
		int chance = CHUNK_WIDTH*CHUNK_LENGTH/tries;
		if (randseed != 0 && random.nextInt(chance) == 0 && place.getBlockY() <= maxHeight && place.getBlockY() >= minHeight) {
			state.setType(material);
			modifier.modify(state);
		}
/*		for (int i = 0; i < size; i++) {
			BlockState state = place.getBlock().getState();
			state.setType (material);
			modifier.modify(state);
			state.update(true);
			place.setX(place.getX() + random.nextInt(3)-1);
			place.setZ(place.getZ() + random.nextInt(3)-1);
			System.out.println("Erz platziert!");
		}*/
	}

	public Material getMaterial() {
		return material;
	}

	public BlockModifier getModifier() {
		return modifier;
	}

	public int getSize() {
		return size;
	}

	public int getTries() {
		return tries;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public int getMaxHeight() {
		return maxHeight;
	}
	
	

}
