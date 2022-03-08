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
import at.jojokobi.mcutil.entity.Targeter;
import at.jojokobi.mcutil.locatables.EntityLocatable;
import at.jojokobi.mcutil.locatables.Locatable;

public class AttackTask implements EntityTask {
	
	private Predicate<Entity> targetPredicate;
	private double targetingRange = 30;
	private Damageable target = null;
	private int cooldown = 0;
	
	public AttackTask(Class<? extends Entity> clazz) {
		this(e -> clazz.isInstance(e));
	}

	public AttackTask(Predicate<Entity> targetPredicate, double targetingRange) {
		super();
		this.targetPredicate = targetPredicate;
		this.targetingRange = targetingRange;
	}
	
	public AttackTask(Predicate<Entity> targetPredicate) {
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
		else if (entity instanceof Attacker) {
			Attacker attacker = (Attacker) entity;
			//In Range
			if (entity.getEntity().getLocation().distance(target.getLocation()) <= attacker.getAttackRange() && (attacker.attackWhenNoLineOfSight() || EntityUtil.canHit(entity.getEntity(), target))) {
				//Check if can attack
				if (cooldown <= 0) {
					//Attack
					cooldown = attacker.getAttackDelay();
					//Multitarget
					if (attacker.isMultiTarget() && attacker instanceof Targeter) {
						Targeter targeter = (Targeter) attacker;
						Set<Damageable> targets = targeter.findTargets(entity.getEntity());
						targets.add(target);
						attacker.attack(targets);
					}
					else {
						attacker.attack(target);
					}
					goal = attacker.createInRangeLocatable(entity.getEntity(), target);
					entity.getEntity().teleport(entity.getEntity().getLocation().setDirection(target.getLocation().subtract(entity.getEntity().getLocation()).toVector()));
				}
			}
			else {
				goal = new EntityLocatable(target);
			}
		}
		else {
			goal = new EntityLocatable(target);
		}
		//Invalidate dead target
		if (target != null && (entity instanceof Attacker ? ((Attacker) entity).defeatedEnemy(target) : target.isDead())) {
			target = null;
		}
		cooldown--;
		
		return goal;
	}
	
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
