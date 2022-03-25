package at.jojokobi.mcutil.entity.spawns;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import at.jojokobi.mcutil.NamespacedEntry;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;

public class CustomEntitySpawnerHandler implements Listener {

	private NamespacedKey namespaceKey;
	private NamespacedKey keyKey;
	
	private EntityHandler handler;
	
	public CustomEntitySpawnerHandler(EntityHandler handler, Plugin plugin) {
		this.handler = handler;
		namespaceKey = new NamespacedKey(plugin, "entity_namespace");
		keyKey = new NamespacedKey(plugin, "entity_key");
	}
	
	@EventHandler
	public void onSpawnerSpawn (SpawnerSpawnEvent event) {
		CreatureSpawner spawner = event.getSpawner();
		String namespace = spawner.getPersistentDataContainer().getOrDefault(namespaceKey, PersistentDataType.STRING, null);
		String key = spawner.getPersistentDataContainer().getOrDefault(keyKey, PersistentDataType.STRING, null);
		
		if (namespace != null && key != null) {
			CustomSpawn spawn = CustomSpawnsHandler.getInstance().getItem(new NamespacedEntry(namespace, key));
			if (spawn != null) {
				event.setCancelled(true);
				for (CustomEntity<?> entity : spawn.spawn(event.getLocation())) {
					handler.addEntity(entity);
				}
			}
		}
	}
	
	public void makeCustomSpawner (CreatureSpawner spawner, NamespacedEntry entry) {
		spawner.getPersistentDataContainer().set(namespaceKey, PersistentDataType.STRING, entry.getNamespace());
		spawner.getPersistentDataContainer().set(keyKey, PersistentDataType.STRING, entry.getIdentifier());
	}
	
	public void makeCustomSpawner (Block block, NamespacedEntry entry) {
		CreatureSpawner spawner = (CreatureSpawner) block.getState();
		makeCustomSpawner(spawner, entry);
		spawner.update();
	}

}
