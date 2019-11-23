package at.jojokobi.mcutil.generation.population;

import static at.jojokobi.mcutil.generation.TerrainGenUtil.*;

import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;

import at.jojokobi.mcutil.generation.TerrainGenUtil;

public abstract class Structure {
	
	private int width = 0;
	private int length = 0;
	private int height = 0;
	private int chance = 4;
	private double scaleFactor = 1;
	private int xModifier = 1;
	private int zModifier = 1;
	private Environment dimension = null;
	
	public Structure(int width, int length, int height, int chance, double scaleFactor) {
		this.width = width;
		this.length = length;
		this.height = height;
		this.chance = chance;
		this.scaleFactor = scaleFactor;
	}

	public int calculatePlacementY (int width, int length, Location place) {
/*		int startY = place.getWorld().getHighestBlockYAt(place);
		Location endplace = place.clone();
		endplace.add(new Vector (endplace.getX() + width, 0, endplace.getZ() + length));
		int endY = endplace.getWorld().getHighestBlockYAt(endplace);
		return Math.round((startY + endY)/2.0f);*/
		return getTerrainHeight(place);
	}
	
	public abstract List<StructureInstance<? extends Structure>> generate(Location loc, long seed);
	
	public abstract String getIdentifier ();
	
	public List<StructureInstance<? extends Structure>> generate (Chunk chunk, long seed) {
		Location place = new Location(chunk.getWorld(), chunk.getX() * CHUNK_WIDTH, 1, chunk.getZ() * CHUNK_LENGTH);
		place.setY(calculatePlacementY(getWidth(), getLength(), place));
		return generate(place, seed);
	}
	
	public boolean canGenerate (Chunk chunk, long seed) {
		Random random = new Random (generateValueBasedSeed(seed, chunk.getX() * CHUNK_WIDTH + getxModifier(), 1, chunk.getZ() * CHUNK_LENGTH + getzModifier(), scaleFactor));
		return chance > 0 && random.nextInt(chance) == 0 && (dimension == null || dimension == chunk.getWorld().getEnvironment());
	}
	
	public int getxModifier() {
		return xModifier;
	}

	public int getzModifier() {
		return zModifier;
	}

	public void setzModifier(int yFactor) {
		this.zModifier = yFactor;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getChance() {
		return chance;
	}

	public void setChance(int chance) {
		this.chance = chance;
	}

	public double getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(double scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public Environment getDimension() {
		return dimension;
	}

	public void setDimension(Environment dimension) {
		this.dimension = dimension;
	}

	public void setxModifier(int xModifier) {
		this.xModifier = xModifier;
	}
	
	protected long generateValueBeasedSeed (Location loc, long seed) {
		return TerrainGenUtil.generateValueBasedSeed(seed, loc.getBlockX() + getxModifier(), 1, loc.getBlockZ() + getzModifier(), 1);
	}
	
	public StructureInstance<? extends Structure> getStandardInstance (World world) {
		return getStandardInstance(new Location(world, 0, 0, 0));
	}
	
	public abstract StructureInstance<? extends Structure> getStandardInstance (Location location);

}
