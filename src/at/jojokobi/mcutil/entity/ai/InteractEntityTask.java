package at.jojokobi.mcutil.entity.ai;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.locatables.Locatable;
import at.jojokobi.mcutil.locatables.WrapperLocatable;

public class InteractEntityTask implements EntityTask {
	
	private TaskCondition condition;
	private double radius;
	private Entity goal;
	
	public InteractEntityTask(TaskCondition condition, double radius) {
		super();
		this.condition = condition;
		this.radius = radius;
	}

	
	@Override
	public boolean canApply(CustomEntity<?> entity) {
		List<Entity> entities = entity.getEntity().getNearbyEntities(radius, radius, radius);
		for (Entity e : entities) {
			if (isInteracting(e, entity)) {
				if (e instanceof Player) {
					condition.request(entity, true);
					break;
				}
				else {
					condition.request(entity, false);
				}
			}
		}
		return (goal != null || entities.size() > 0) && condition.canApply(entity);
	}
	
	@Override
	public Locatable apply(CustomEntity<?> entity) {
		Vector dir = goal.getLocation().toVector().subtract(entity.getEntity().getLocation().toVector());
		if (dir.length() != 0) {
			dir.normalize();
		}
		return new WrapperLocatable(goal.getLocation().add(dir));
	}

	@Override
	public void activate(CustomEntity<?> entity) {
		condition.activate(entity);
		List<Entity> entities = entity.getEntity().getNearbyEntities(radius, radius, radius);
		
		for (Entity e : entities) {
			if (isInteracting(e, entity)) {
				if (e instanceof Player) {
					goal = e;
					break;
				}
				else {
					goal = e;
				}
			}
		}
	}

	@Override
	public void deactivate(CustomEntity<?> entity) {
		condition.deactivate(entity);
		goal = null;
	}
	
	private boolean isInteracting(Entity e, CustomEntity<?> entity) {
		Vector dir = entity.getEntity().getLocation().toVector().subtract(e.getLocation().toVector());
		if (dir.length() != 0) {
			dir.normalize();
		}
		else {
			return false;
		}
		Vector entityDir = e.getLocation().getDirection();
		return entityDir.subtract(dir).length() < 0.2;
	}
}
