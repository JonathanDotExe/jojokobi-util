package at.jojokobi.mcutil.entity.spawns;

import java.util.function.BiPredicate;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CustomEntitySpawnData {
	
	private CustomSpawn spawn;
	private double chance;
	private int tries;

	private int spawnGroupSize = 1;
	private int maxEntitiesAround = 48;
	private int minPlayerDistance = 24;
	private int maxPlayerDistance = 64;
	private BiPredicate<Location, Player> canSpawn;
	
	public CustomEntitySpawnData(CustomSpawn spawn, double chance, int tries) {
		super();
		this.spawn = spawn;
		this.chance = chance;
		this.tries = tries;
	}

	public CustomSpawn getSpawn() {
		return spawn;
	}

	public CustomEntitySpawnData setSpawn(CustomSpawn spawn) {
		this.spawn = spawn;
		return this;
	}

	public double getChance() {
		return chance;
	}

	public CustomEntitySpawnData setChance(double chance) {
		this.chance = chance;
		return this;
	}

	public int getTries() {
		return tries;
	}

	public CustomEntitySpawnData setTries(int tries) {
		this.tries = tries;
		return this;
	}

	public int getSpawnGroupSize() {
		return spawnGroupSize;
	}

	public CustomEntitySpawnData setSpawnGroupSize(int spawnGroupSize) {
		this.spawnGroupSize = spawnGroupSize;
		return this;
	}

	public int getMaxEntitiesAround() {
		return maxEntitiesAround;
	}

	public CustomEntitySpawnData setMaxEntitiesAround(int maxEntitiesAround) {
		this.maxEntitiesAround = maxEntitiesAround;
		return this;
	}

	public int getMinPlayerDistance() {
		return minPlayerDistance;
	}

	public CustomEntitySpawnData setMinPlayerDistance(int minPlayerDistance) {
		this.minPlayerDistance = minPlayerDistance;
		return this;
	}

	public int getMaxPlayerDistance() {
		return maxPlayerDistance;
	}

	public CustomEntitySpawnData setMaxPlayerDistance(int maxPlayerDistance) {
		this.maxPlayerDistance = maxPlayerDistance;
		return this;
	}

	public BiPredicate<Location, Player> getCanSpawn() {
		return canSpawn;
	}

	public CustomEntitySpawnData setCanSpawn(BiPredicate<Location, Player> canSpawn) {
		this.canSpawn = canSpawn;
		return this;
	}
	
	public boolean canSpawn(Location loc, Player player) {
		return canSpawn == null || canSpawn.test(loc, player);
	}

}
