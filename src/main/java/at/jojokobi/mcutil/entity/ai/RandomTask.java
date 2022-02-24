package at.jojokobi.mcutil.entity.ai;

import org.bukkit.Location;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.locatables.Locatable;
import at.jojokobi.mcutil.locatables.WrapperLocatable;

public class RandomTask implements EntityTask{
	
	private Location goal;
	private double goalRange = 10;
	private int timeout = 0;
	private boolean raytrace = false;
	private boolean yMove = true;
	
	public RandomTask() {
		this (10);
	}
	
	public RandomTask(double goalRange) {
		this(goalRange, false, true);
	}

	public RandomTask(double goalRange, boolean raytrace, boolean yMove) {
		super();
		this.goalRange = goalRange;
		this.raytrace = raytrace;
		this.yMove = true;
	}

	@Override
	public boolean canApply(CustomEntity<?> entity) {
		return true;
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
					RayTraceResult rt = entity.getEntity().getWorld().rayTraceBlocks(entity.getEntity().getLocation(), direction, range);
					if (rt != null) {
						range = rt.getHitPosition().distance(entity.getEntity().getLocation().toVector());
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
		
	}

	@Override
	public void deactivate(CustomEntity<?> entity) {
		
	}

}
