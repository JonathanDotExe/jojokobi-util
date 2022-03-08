package at.jojokobi.mcutil.entity.ai;

import org.bukkit.entity.Entity;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.locatables.EntityLocatable;
@Deprecated
public class LegacyFollowTask implements LegacyEntityTask {
	
	private EntityLocatable follow;
	private double followDistance;

	public LegacyFollowTask(Entity follow, double followDistance) {
		this.follow = new EntityLocatable(follow);
		this.followDistance = followDistance;
	}
	
	public LegacyFollowTask(Entity follow) {
		this(follow, 30);
	}

	@Override
	public void tick(CustomEntity<?> entity) {
		if (entity.getGoal() != follow) {
			entity.setGoal(follow);
		}
		
		if (followDistance >= 0 && entity.getEntity().getLocation().distanceSquared(follow.getLocation()) > followDistance * followDistance) {
			//entity.setTask(null);
		}
	}

	public EntityLocatable getFollow() {
		return follow;
	}

}
