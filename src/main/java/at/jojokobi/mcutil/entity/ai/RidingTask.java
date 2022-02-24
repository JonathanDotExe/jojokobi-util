package at.jojokobi.mcutil.entity.ai;

import org.bukkit.entity.Entity;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.locatables.EntityLocatable;
import at.jojokobi.mcutil.locatables.Locatable;
import at.jojokobi.mcutil.locatables.RelativeLocateable;

public class RidingTask implements EntityTask {

	@Override
	public boolean canApply(CustomEntity<?> entity) {
		return !entity.getEntity().getPassengers().isEmpty();
	}

	@Override
	public Locatable apply(CustomEntity<?> entity) {
		Locatable goal = null;
		if (!entity.getEntity().getPassengers().isEmpty()) {
			Entity passenger = entity.getEntity().getPassengers().get(0);
			goal = new RelativeLocateable(new EntityLocatable(entity.getEntity()),
					passenger.getLocation().getDirection().multiply(entity.getMaxGoalDistance() + 1));
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
