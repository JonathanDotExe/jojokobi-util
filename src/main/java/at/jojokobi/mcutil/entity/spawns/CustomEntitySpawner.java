package at.jojokobi.mcutil.entity.spawns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class CustomEntitySpawner {
	
	private List<CustomEntitySpawnData> entities = new ArrayList<CustomEntitySpawnData>();

	public CustomEntitySpawner(Plugin plugin) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			//Spawn algorithm
			for (Player player : Bukkit.getOnlinePlayers()) {
				for (CustomEntitySpawnData data : entities) {
					int count = (int) player.getNearbyEntities(data.getMaxPlayerDistance(), 512, data.getMaxPlayerDistance()).stream().filter(e -> e instanceof LivingEntity).count();
					//Spawn
					for (int i = 0; i < data.getTries(); i++) {
						if (count < data.getMaxEntitiesAround() && Math.random() < data.getChance() && data.canSpawn(player)) {
							double x = Math.random() * (data.getMaxPlayerDistance() - data.getMinPlayerDistance()) * Math.signum(Math.random() - 0.5);
							double z = Math.random() * (data.getMaxPlayerDistance() - data.getMinPlayerDistance()) * Math.signum(Math.random() - 0.5);
							
							Vector vec = new Vector(x, 0, z);
							Location loc = player.getLocation().add(vec);
							loc.setY(loc.getWorld().getHighestBlockYAt(loc));
							
							//Spawn in air
							if (loc.getY() <= 0) {
								loc.setY(100);
							}
							
							int c = (int) (Math.random() * data.getSpawnGroupSize() + 1);
							for (int j = 0; j < c; j++) {
								data.getSpawn().spawn(loc);
								System.out.println("Spawned at " + loc);
							}
							
							count += c;
						}
					}
				}
			}
		}, 20 * 20, 20 * 20);
	}
	
	public void addSpawn(CustomEntitySpawnData d) {
		entities.add(d);
	}

}
