package at.jojokobi.mcutil.entity.ai;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.entity.Entity;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.locatables.EntityLocatable;
import at.jojokobi.mcutil.locatables.Locatable;

public class CarryTask implements EntityTask {
	
	private Predicate<Entity> predicate;
	private double range = 5;
	
	private Entity target;

	public CarryTask(Predicate<Entity> predicate, double range) {
		super();
		this.predicate = predicate;
		this.range = range;
	}

	@Override
	public boolean canApply(CustomEntity<?> entity) {
		return (target != null && target.isValid()) || !getTargets(entity).isEmpty();
	}

	@Override
	public Locatable apply(CustomEntity<?> entity) {
		Locatable goal = null;
		if (target == null || !target.isValid()) {
			activate(entity);
		}
		if (target.getLocation().distanceSquared(entity.getEntity().getLocation()) <= entity.getMaxGoalDistance() * entity.getMaxGoalDistance()) {
			entity.getEntity().addPassenger(target);
			target = null;
		}
		else {
			goal = new EntityLocatable(target);
		}
		return goal;
	}
	
	private List<Entity> getTargets (CustomEntity<?> entity) {
		return Arrays.asList(entity.getEntity().getNearbyEntities(range, range, range).stream().filter(e -> e.getVehicle() != entity.getEntity() && predicate.test(e)).toArray(Entity[]::new));
	}

	@Override
	public void activate(CustomEntity<?> entity) {
		target = getTargets(entity).get(0);
	}

	@Override
	public void deactivate(CustomEntity<?> entity) {
		target = null;
	}

}
