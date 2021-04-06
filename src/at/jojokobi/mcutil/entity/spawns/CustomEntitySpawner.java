package at.jojokobi.mcutil.entity.spawns;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class CustomEntitySpawner {
	
	private List<CustomEntitySpawnData> entities;

	public CustomEntitySpawner(Plugin plugin) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			
		}, 20 * 30, 20 * 30);
	}
	
	public void addSpawn(CustomEntitySpawnData d) {
		entities.add(d);
	}

}
