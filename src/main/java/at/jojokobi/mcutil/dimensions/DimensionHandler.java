package at.jojokobi.mcutil.dimensions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.Plugin;

public class DimensionHandler implements Listener {
	
	private List<CustomDimension> dimensions = new ArrayList<>();
	
//	@EventHandler
//	public void onWorldLoad (WorldLoadEvent event) {
//		World world = event.getWorld();
//		CustomDimension dim = getDimension(world);
//		if (dim != null) {
//			world.setG
//		}
//	}
	
	private Plugin plugin;
	
	@Deprecated
	public DimensionHandler(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onWorldInit (WorldInitEvent event) {
		System.out.println("Initilized world: " + event.getWorld().getName());
		Bukkit.getScheduler().runTask(plugin, () -> {
			for (CustomDimension dim : dimensions) {
				getDimensionWorld(event.getWorld(), dim);
			}
		});
	}
	
	public void unloadWorlds(CustomDimension dimension) {
		System.out.println("Unloading dimension worlds of dimension " + dimension.getName());
		for (World world : Bukkit.getWorlds()) {
			if (dimension.isDimension(world)) {
				Bukkit.unloadWorld(world, true);
			}
		}
	}
	
	/**
	 * 
	 * Registers a dimension type
	 * 
	 * Alls dimensions must be registred before the worlds are loaded
	 * 
	 * @param dimension
	 */
	public void addDimension (CustomDimension dimension) {
		dimensions.add(dimension);
	}
	
	public CustomDimension getDimension (World world) {
		CustomDimension dimension = null;
		for (Iterator<CustomDimension> iter = dimensions.iterator(); iter.hasNext() && dimension == null;) {
			CustomDimension dim = iter.next();
			if (dim.isDimension(world)) {
				dimension = dim;
			}
		}
		return dimension;
	}
	
	public World getNormalWorld (World world) {
		return getDimension(world).getNormalWorld(world);
	}
	
	public World getDimensionWorld (World world, CustomDimension dim) {
		World dimWorld = null;
		if (world.getEnvironment() == Environment.NORMAL && getDimension(world) == null) {
			dimWorld = Bukkit.createWorld(new WorldCreator(world.getName() + "_" + dim.getSaveName()).generator(dim.createGenerator()).seed(world.getSeed() + dim.getSeedOffset()));
		}
			
		return dimWorld;
	}

}
