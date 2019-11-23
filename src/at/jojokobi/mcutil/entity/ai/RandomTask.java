package at.jojokobi.mcutil.entity.ai;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.locatables.Locatable;
import at.jojokobi.mcutil.locatables.WrapperLocatable;

public class RandomTask implements EntityTask{
	
	private Location goal;
	private double goalRange = 10;
	private int timeout = 0;
	
	public RandomTask() {
		this (10);
	}

	public RandomTask(double goalRange) {
		super();
		this.goalRange = goalRange;
	}

	@Override
	public boolean canApply(CustomEntity<?> entity) {
		return true;
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
		
	}

	@Override
	public void deactivate(CustomEntity<?> entity) {
		
	}

}
