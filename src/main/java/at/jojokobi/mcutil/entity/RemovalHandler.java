package at.jojokobi.mcutil.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class RemovalHandler implements Listener{

	public static final String REMOVE_CUSTOM_ENTITY_TAG = "remove_custom_entity";
	
	@Deprecated
	public RemovalHandler() {
		
	}
	
	@EventHandler
	public void onChunkLoad (ChunkLoadEvent event) {
		for (Entity entity : event.getChunk().getEntities()) {
			if (entity.getScoreboardTags().contains(REMOVE_CUSTOM_ENTITY_TAG)) {
				entity.eject();
				entity.remove();
			}
		}
	}
	
	public static void markForRemoval (Entity entity) {
		entity.addScoreboardTag(REMOVE_CUSTOM_ENTITY_TAG);
	}
	
}
