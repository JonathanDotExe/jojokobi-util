package at.jojokobi.mcutil.entity;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;

public interface Targeter {

	public boolean isTarget (Entity entity);
	
	public default double getTargetingRange () {
		return 30;
	}
	
	public default Set<Damageable> findTargets (Entity entity) {
		Set<Damageable> targets = new HashSet<>();
		for (Entity e : entity.getNearbyEntities(getTargetingRange(), getTargetingRange(), getTargetingRange())) {
			if (isTarget(e) && e instanceof Damageable) {
				targets.add((Damageable) e);
			}
		}
		return targets;
	}

}
