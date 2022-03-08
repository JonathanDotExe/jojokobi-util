package at.jojokobi.mcutil.entity.ai;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityComponent;
import at.jojokobi.mcutil.entity.Ownable;

public class SitComponent implements EntityComponent {
	
	private boolean sitting = true;
	private Location sitLocation = null;
	
	@Override
	public void loop(CustomEntity<?> entity) {
		if (sitLocation == null) {
			sitLocation = entity.getSpawnPoint();
		}
	}
	
	@Override
	public Map<String, Object> serialize(CustomEntity<?> entity) {
		Map<String, Object> map = new HashMap<String, Object> ();
		map.put("sitLocation", sitLocation);
		return map;
	}
	
	@Override
	public void deserialize(Map<String, Object> map, CustomEntity<?> entity) {
		if (map.get("sitLocation") instanceof Location) {
			sitLocation = (Location) map.get("sitLocation");
		}
	}
	
	@Override
	public void onInteract(CustomEntity<?> entity, PlayerInteractEntityEvent event) {
		EntityComponent.super.onInteract(entity, event);
		if (entity instanceof Ownable && event.getPlayer().getUniqueId().equals(((Ownable) entity).getOwner())) {
			sitting = !sitting;
			sitLocation = entity.getEntity().getLocation();
		}
	}

	public boolean isSitting() {
		return sitting;
	}

	public void setSitting(boolean sitting) {
		this.sitting = sitting;
	}
	
	@Nullable
	public Location getSitLocation() {
		return sitLocation;
	}

	public void setSitLocation(@Nullable Location sitLocation) {
		this.sitLocation = sitLocation;
	}

}
