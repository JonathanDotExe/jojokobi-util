package at.jojokobi.mcutil.entity.ai;

import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.util.RayTraceResult;
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
	private boolean raytrace = false;
	private boolean yMove = true;
	
	public RandomAroundPlaceTask(Function<CustomEntity<?>, Location> place) {
		this (place, 10, 10, 2, false, true);
	}


	public RandomAroundPlaceTask(Function<CustomEntity<?>, Location> place, double goalRange, double maxDistance, 
			double startDistance, boolean raytrace, boolean yMove) {
		super();
		this.goalRange = goalRange;
		this.maxDistance = maxDistance;
		this.startDistance = startDistance;
		this.place = place;
		this.raytrace = raytrace;
		this.yMove = yMove;
	}


	@Override
	public boolean canApply(CustomEntity<?> entity) {
		Location place = this.place.apply(entity);
		return entity.getEntity().getWorld() == place.getWorld() && ((goal != null && entity.getEntity().getLocation().distanceSquared(place) <= maxDistance * maxDistance) || (goal == null && entity.getEntity().getLocation().distanceSquared(place) <= startDistance * startDistance));
	}

	@Override
	public Locatable apply(CustomEntity<?> entity) {
		if (timeout <= 0) {
			Vector direction = new Vector(Math.random() - 0.5, yMove ? (Math.random() - 0.5) : 0, Math.random() - 0.5);
			if (direction.length() != 0) {
				direction.normalize();
				double range = Math.random() * goalRange;
				direction.multiply(Math.random() < 0.5 ? -1 : 1);
				if (raytrace) {
					RayTraceResult rt = entity.getEntity().getWorld().rayTraceBlocks(entity.getEntity().getLocation().add(0, 1, 0), direction, range);
					if (rt != null) {
						range = rt.getHitPosition().distance(entity.getEntity().getLocation().add(0, 1, 0).toVector());
					}
				}
				direction.multiply(range);
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
