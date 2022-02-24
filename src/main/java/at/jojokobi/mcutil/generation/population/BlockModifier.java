package at.jojokobi.mcutil.generation.population;

import org.bukkit.block.BlockState;

public interface BlockModifier {
	
	public void modify (BlockState block);
	
	public static BlockModifier getEmptyModifier () {
		BlockModifier modifier = new BlockModifier () {
			
			@Override
			public void modify(BlockState block) {
				
			}
			
		};
		return modifier;
	}
	
}
