package at.jojokobi.mcutil.dimensions;


import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public interface CustomDimension {
	
	public String getSaveName ();
	
	public String getName ();

	public ChunkGenerator createGenerator ();
	
	public long getSeedOffset();
	
	public default boolean isDimension (World world) {
		return world.getName().endsWith(getSaveName());
	}
	
	public default World getNormalWorld (World world) {
		return Bukkit.getWorld(world.getName().substring(0, world.getName().length() - getSaveName().length() - 1));
	}

}
