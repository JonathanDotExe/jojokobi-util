package at.jojokobi.mcutil.entity.ai;

import org.bukkit.util.Vector;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.locatables.EntityLocatable;
import at.jojokobi.mcutil.locatables.RelativeLocateable;
@Deprecated
public class LegacyRandomTask implements LegacyEntityTask{

	private int rotateInterval;
	
	public LegacyRandomTask(int rotateInterval) {
		super();
		this.rotateInterval = rotateInterval;
	}

	public LegacyRandomTask() {
		this(20);
	}

	@Override
	public void tick(CustomEntity<?> entity) {
		if (entity.getTime() % rotateInterval == 0) {
			Vector direction = new Vector(Math.random()*20-10, Math.random()*20-10, Math.random()*20-10);
			if (direction.getX() != 0 || direction.getY() != 0 || direction.getZ() != 0) {
				direction.normalize();
				direction.multiply(entity.getMaxGoalDistance() + 1);
			}
			entity.setGoal(new RelativeLocateable(new EntityLocatable(entity.getEntity()), direction));
		}
	}

	public int getRotateInterval() {
		return rotateInterval;
	}

	public void setRotateInterval(int rotateInterval) {
		this.rotateInterval = rotateInterval;
	}

}
