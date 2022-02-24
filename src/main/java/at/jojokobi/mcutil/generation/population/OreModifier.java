package at.jojokobi.mcutil.generation.population;

import org.bukkit.block.BlockState;

public class OreModifier implements BlockModifier {
	
	private long seed = 0;
	
	public OreModifier(long seed) {
		this.seed = seed;
	}
	
	@Override
	public void modify(BlockState block) {
		Ore.coal.generateOre(block, seed);
		Ore.iron.generateOre(block, seed);
		Ore.gold.generateOre(block, seed);
		Ore.redstone.generateOre(block, seed);
		Ore.diamond.generateOre(block, seed);
		Ore.lapis.generateOre(block, seed);
	}

}
