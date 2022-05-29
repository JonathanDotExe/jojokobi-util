package at.jojokobi.mcutil.generation.population;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.Material;

public class VillageSpreader {

	private List<Structure> houses = new ArrayList<>();
	
	private int width = 8;
	private int height = 8;
	private int unitWidth = 16;
	private int unitHeight = 16;
	private boolean forceHeight = false;
	private VillagePathGenerator pathGenerator = new CleanPathGenerator();
	private Function<Material, Material> blockFunction = b -> Material.GRAVEL;
	private float stepMultiplier = 1;
	
	public VillageSpreader(Structure... houses) {
		this.houses.addAll(Arrays.asList(houses));
	}
	
//	public VillageNode generateVillage (Random random, int x, int z) {
//		VillageNode node = null;
//		if (Math.abs(x) < xSpread && Math.abs(z) < zSpread) {
//			node = new VillageNode();
//			//House or Path
//			if (random.nextBoolean()) {
//				node.setHouse(houses.get(random.nextInt(houses.size())));
//			}
//			
//			if (random.nextBoolean()) {
//				node.setLeft(generateVillage(random, x - 1, z));
//			}
//			if (random.nextBoolean()) {
//				node.setTop(generateVillage(random, x, z - 1));
//			}
//			if (random.nextBoolean()) {
//				node.setRight(generateVillage(random, x + 1, z));
//			}
//			if (random.nextBoolean()) {
//				node.setBottom(generateVillage(random, x, z + 1));
//			}
//		}
//		return node;
//	}
	
	public List<StructureInstance<? extends Structure>> generateVillage (Random random, long seed, Location place) {
		VillageNode[][] area = generateVillageMap(random);
		return generateVillage(area, seed, place);
	}
	
	public boolean isForceHeight() {
		return forceHeight;
	}

	public void setForceHeight(boolean forceHeight) {
		this.forceHeight = forceHeight;
	}

	public List<StructureInstance<? extends Structure>> generateVillage (VillageNode[][] area, long seed, Location place) {
		List<StructureInstance<? extends Structure>> strucs = new ArrayList<>();
		for (int z = 0; z < area.length; z++) {
			for (int x = 0; x < area[z].length; x++) {
				VillageNode node = area[z][x];
				if (node != null) {
					Location housePlace = place.clone().add(x * unitWidth, 0, z * unitHeight);
					pathGenerator.generatePath(housePlace, unitWidth, unitHeight, node.isTop(), node.isRight(), node.isBottom(), node.isLeft(), blockFunction);
					if (node.getHouse() != null) {
						housePlace.add(unitWidth/2 - node.getHouse().getWidth()/2, 0, unitHeight/2 - node.getHouse().getLength()/2);
						if (!forceHeight) {
							housePlace.setY(node.getHouse().calculatePlacementY(node.getHouse().getWidth(), node.getHouse().getLength(), housePlace));
						}
						strucs.addAll(node.getHouse().generateNaturally(housePlace, seed));
					}
				}
			}
		}
		return strucs;
	}
	
	public VillageNode[][] generateVillageMap (Random random) {
		VillageNode[][] area = new VillageNode[height][width];
		generateVillageMap(random, area, width/2, height/2, 0);
		return area;
	}

	public VillageNode generateVillageMap (Random random, VillageNode[][] area, int x, int z, int steps) {
		if (z >= 0 && z < area.length && x >= 0 && x < area[z].length && area[z][x] == null) {
			VillageNode node = new VillageNode();
			//House or Path
			if (random.nextBoolean()) {
				node.setHouse(houses.get(random.nextInt(houses.size())));
			}
			area[z][x] = node;
			steps++;
			int chance = Math.max(Math.round(steps * stepMultiplier), 1);
			if (random.nextInt(chance) == 0) {
				VillageNode n = generateVillageMap(random, area, x - 1, z, steps);
				if (n != null) {
					node.setLeft(true);
					n.setRight(true);
				}
			}
			if (random.nextInt(chance) == 0) {
				VillageNode n = generateVillageMap(random, area, x, z - 1, steps);
				if (n != null) {
					node.setTop(true);
					n.setBottom(true);
				}
			}
			if (random.nextInt(chance) == 0) {
				VillageNode n = generateVillageMap(random, area, x + 1, z, steps);
				if (n != null) {
					node.setRight(true);
					n.setLeft(true);
				}
			}
			if (random.nextInt(chance) == 0) {
				VillageNode n = generateVillageMap(random, area, x, z + 1, steps);
				if (n != null) {
					node.setBottom(true);
					n.setTop(true);
				}
			}
			return node;
		}
		return null;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getUnitWidth() {
		return unitWidth;
	}

	public void setUnitWidth(int unitWidth) {
		this.unitWidth = unitWidth;
	}

	public int getUnitHeight() {
		return unitHeight;
	}

	public void setUnitHeight(int unitHeight) {
		this.unitHeight = unitHeight;
	}

	public VillagePathGenerator getPathGenerator() {
		return pathGenerator;
	}

	public void setPathGenerator(VillagePathGenerator pathGenerator) {
		this.pathGenerator = pathGenerator;
	}

	public Function<Material, Material> getBlockFunction() {
		return blockFunction;
	}

	public void setBlockFunction(Function<Material, Material> blockFunction) {
		this.blockFunction = blockFunction;
	}

	public float getStepMultiplier() {
		return stepMultiplier;
	}

	public void setStepMultiplier(float stepMultiplier) {
		this.stepMultiplier = stepMultiplier;
	}
	
}
