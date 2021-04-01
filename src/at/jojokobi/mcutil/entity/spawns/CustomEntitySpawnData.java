package at.jojokobi.mcutil.entity.spawns;

public class CustomEntitySpawnData {
	
	private CustomSpawn spawn;
	private int minPlayerDistance;
	private int maxPlayerDistance;
	private int spawnGroupSize;
	private int maxEntitiesAround;
	
	public CustomEntitySpawnData(CustomSpawn spawn, int minPlayerDistance, int maxPlayerDistance, int spawnGroupSize,
			int maxEntitiesAround) {
		super();
		this.spawn = spawn;
		this.minPlayerDistance = minPlayerDistance;
		this.maxPlayerDistance = maxPlayerDistance;
		this.spawnGroupSize = spawnGroupSize;
		this.maxEntitiesAround = maxEntitiesAround;
	}

	public CustomSpawn getSpawn() {
		return spawn;
	}

	public int getMinPlayerDistance() {
		return minPlayerDistance;
	}

	public int getMaxPlayerDistance() {
		return maxPlayerDistance;
	}

	public int getSpawnGroupSize() {
		return spawnGroupSize;
	}

	public int getMaxEntitiesAround() {
		return maxEntitiesAround;
	}

}
