package at.jojokobi.mcutil.entity.ai;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.Ownable;
import at.jojokobi.mcutil.locatables.EntityLocatable;
import at.jojokobi.mcutil.locatables.Locatable;

public class FollowOwnerTask implements EntityTask {

	@Override
	public boolean canApply(CustomEntity<?> entity) {
		return entity instanceof Ownable && ((Ownable) entity).getOwner() != null && (entity.getComponent(SitComponent.class) == null || !entity.getComponent(SitComponent.class).isSitting());
	}

	@Override
	public Locatable apply(CustomEntity<?> entity) {
		Locatable goal = null;
		Entity owner = Bukkit.getEntity(((Ownable) entity).getOwner());
		if (owner != null && owner.getWorld() == entity.getEntity().getWorld()) {
			goal = new EntityLocatable(owner);
		}
		return goal;
	}

	@Override
	public boolean isSprint() {
		return true;
	}
	
	@Override
	public void activate(CustomEntity<?> entity) {
		
	}

	@Override
	public void deactivate(CustomEntity<?> entity) {
		
	}
	
}
