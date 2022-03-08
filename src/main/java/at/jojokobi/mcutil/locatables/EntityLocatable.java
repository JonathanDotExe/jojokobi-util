package at.jojokobi.mcutil.locatables;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class EntityLocatable implements Locatable {
	
	private Entity entity;

	public EntityLocatable(Entity entity) {
		super();
		this.entity = entity;
	}

	@Override
	public Location getLocation() {
		return entity.getLocation();
	}

	public Entity getEntity() {
		return entity;
	}

}
