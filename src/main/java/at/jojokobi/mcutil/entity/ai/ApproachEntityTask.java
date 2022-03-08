package at.jojokobi.mcutil.entity.ai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;

import at.jojokobi.mcutil.entity.Attacker;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityUtil;
import at.jojokobi.mcutil.locatables.EntityLocatable;
import at.jojokobi.mcutil.locatables.Locatable;

public abstract class ApproachEntityTask implements EntityTask {
	
	private Predicate<Entity> targetPredicate;
	private double targetingRange = 30;
	private Entity target = null;
	
	public ApproachEntityTask(Class<? extends Entity> clazz) {
		this(e -> clazz.isInstance(e));
	}

	public ApproachEntityTask(Predicate<Entity> targetPredicate, double targetingRange) {
		super();
		this.targetPredicate = targetPredicate;
		this.targetingRange = targetingRange;
	}
	
	public ApproachEntityTask(Predicate<Entity> targetPredicate) {
		this(targetPredicate, 30);
	}
	
	@Override
	public boolean isSprint() {
		return true;
	}

	@Override
	public boolean canApply(CustomEntity<?> entity) {
		return target != null || !findTargets(entity).isEmpty();
	}

	@Override
	public Locatable apply(CustomEntity<?> entity) {
		Locatable goal = null;
		//Check if target is valid
		if (target == null || entity.getEntity().getWorld() != target.getWorld() || entity.getEntity().getLocation().distance(target.getLocation()) >= targetingRange) {
			determineTarget(entity);
		}
		else {
			Attacker attacker = (Attacker) entity;
			//In Range
			if (entity.getEntity().getLocation().distance(target.getLocation()) <= attacker.getAttackRange() && (attacker.attackWhenNoLineOfSight() || EntityUtil.canHit(entity.getEntity(), target))) {
				interact(target);
			}
			else {
				goal = new EntityLocatable(target);
			}
		}
		
		return goal;
	}
	
	protected abstract void interact(Entity entity);
	
	private Set<Damageable> findTargets (CustomEntity<?> entity) {
		Set<Damageable> targets = new HashSet<>();
		for (Entity e : entity.getEntity().getNearbyEntities(targetingRange, targetingRange, targetingRange)) {
			if (targetPredicate.test(e) && e instanceof Damageable) {
				targets.add((Damageable) e);
			}
		}
		return targets;
	}
	
	private void determineTarget (CustomEntity<?> entity) {
		List<Damageable> targets = new ArrayList<>(findTargets(entity));
		target = targets.isEmpty() ? null : targets.get(new Random().nextInt(targets.size()));
	}

	@Override
	public void activate(CustomEntity<?> entity) {
		determineTarget(entity);
	}

	@Override
	public void deactivate(CustomEntity<?> entity) {
		target = null;
	}

}
