package at.jojokobi.mcutil.entity.ai;

import org.bukkit.entity.Entity;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.locatables.EntityLocatable;
import at.jojokobi.mcutil.locatables.RelativeLocateable;
@Deprecated
public class LegacyRidingTask implements LegacyEntityTask {

	public LegacyRidingTask() {

	}

	@Override
	public void tick(CustomEntity<?> entity) {
		if (!entity.getEntity().getPassengers().isEmpty()) {
			Entity passenger = entity.getEntity().getPassengers().get(0);
			entity.setGoal(new RelativeLocateable(new EntityLocatable(entity.getEntity()),
					passenger.getLocation().getDirection().multiply(entity.getMaxGoalDistance() + 1)));
		} else {
			//entity.setTask(null);
			entity.setGoal(null);
		}
	}

}
