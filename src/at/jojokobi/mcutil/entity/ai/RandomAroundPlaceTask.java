package at.jojokobi.mcutil.entity.ai;

import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.locatables.Locatable;
import at.jojokobi.mcutil.locatables.WrapperLocatable;

public class RandomAroundPlaceTask implements EntityTask{
	
	private Location goal;
	private double goalRange = 10;
	private double maxDistance = 10;
	private double startDistance = 3;
	private Function<CustomEntity<?>, Location> place;
	private int timeout = 0;
	
	public RandomAroundPlaceTask(Function<CustomEntity<?>, Location> place) {
		this (place, 10, 10, 2);
	}

	public RandomAroundPlaceTask(Function<CustomEntity<?>, Location> place, double goalRange, double maxDistance, double startDistance) {
		super();
		this.goalRange = goalRange;
		this.maxDistance = maxDistance;
		this.startDistance = startDistance;
		this.place = place;
	}

	@Override
	public boolean canApply(CustomEntity<?> entity) {
		Location place = this.place.apply(entity);
		return entity.getEntity().getWorld() == place.getWorld() && (goal != null && entity.getEntity().getLocation().distanceSquared(place) <= maxDistance) || (goal == null && entity.getEntity().getLocation().distanceSquared(place) <= startDistance);
	}

	@Override
	public Locatable apply(CustomEntity<?> entity) {
		if (timeout <= 0) {
			Vector direction = new Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
			if (direction.length() != 0) {
				direction.normalize();
				direction.multiply((Math.random() + 0.5) * goalRange);
			}
			goal = entity.getEntity().getLocation().add(direction);
			timeout = 24;
		}
		timeout--;
		return new WrapperLocatable(goal);
	}

	@Override
	public void activate(CustomEntity<?> entity) {
		timeout = 0;
	}

	@Override
	public void deactivate(CustomEntity<?> entity) {
		goal = null;
	}

}
