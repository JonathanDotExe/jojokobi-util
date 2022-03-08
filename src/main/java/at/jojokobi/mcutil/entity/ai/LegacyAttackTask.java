package at.jojokobi.mcutil.entity.ai;

import java.util.Set;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;

import at.jojokobi.mcutil.entity.Attacker;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityUtil;
import at.jojokobi.mcutil.entity.Targeter;
@Deprecated
public class LegacyAttackTask extends LegacyFollowTask {
	
	private int cooldown = 0;

	public LegacyAttackTask(Entity follow, double followDistance) {
		super(follow, followDistance);
	}
	
	public LegacyAttackTask(Entity follow) {
		this(follow, 30);
	}

	@Override
	public void tick(CustomEntity<?> entity) {
		super.tick(entity);
		
		if (entity instanceof Attacker && getFollow().getEntity() instanceof Damageable) {
			Damageable enemy = (Damageable) getFollow().getEntity();
			Attacker attacker = (Attacker) entity;
			//Check if enemy is in range
			if (entity.getEntity().getLocation().distance(enemy.getLocation()) <= attacker.getAttackRange() && (attacker.attackWhenNoLineOfSight() || canSee(entity.getEntity(), getFollow().getEntity()))) {
				//Check if can attack
				if (cooldown <= 0) {
					//Attack
					cooldown = attacker.getAttackDelay();
					//Multitarget
					if (attacker.isMultiTarget() && attacker instanceof Targeter) {
						Targeter targeter = (Targeter) attacker;
						Set<Damageable> targets = targeter.findTargets(entity.getEntity());
						targets.add(enemy);
						attacker.attack(targets);
					}
					else {
						attacker.attack(enemy);
					}
				}
				entity.setGoal(attacker.createInRangeLocatable(entity.getEntity(), enemy));
				entity.getEntity().teleport(entity.getEntity().getLocation().setDirection(getFollow().getLocation().subtract(entity.getEntity().getLocation()).toVector()));
//				if (!attacker.chaseWhenInRange()) {
//					if (!entity.getGoal().getLocation().equals(entity.getEntity().getLocation())) {
//						entity.setGoal(new EntityLocatable(entity.getEntity()));
//						entity.getEntity().teleport(entity.getEntity().getLocation().setDirection(getFollow().getLocation().subtract(entity.getEntity().getLocation()).toVector()));
//					}
//				}
			}
			
			//Check if enemy is killed and stop
			if (attacker.defeatedEnemy(enemy)) {
				//entity.setTask(null);
			}
		}
		//Reduce cooldown
		if (cooldown > 0) {
			cooldown--;
		}
	}
	
	private boolean canSee (Entity entity, Entity other) {
//		Vector diff = other.getLocation().add(0, 1, 0).subtract(entity.getLocation().add(0, 1, 0)).toVector();
//		RayTraceResult result = null;
//		if (diff.lengthSquared() != 0) {
//			result = entity.getWorld().rayTraceBlocks(entity.getLocation(), diff, entity.getLocation().distance(other.getLocation()));
//		}
//		boolean canSee = entity.getWorld() == other.getWorld() && (result == null || result.getHitEntity() == other);
		
//		diff.normalize();
//		Location otherPlace = other.getLocation();
//		Location place = entity.getLocation();
//		for (; place.getX() - diff.getX() > otherPlace.getX() == place.getX() > otherPlace.getX() && canSee; place.add(diff)) {
//			canSee = place.getWorld().isChunkLoaded(place.getBlockX()/TerrainGenUtil.CHUNK_WIDTH, place.getBlockZ()/TerrainGenUtil.CHUNK_LENGTH) && !place.getBlock().getType().isSolid();
//		}
		return EntityUtil.canHit(entity, other);
	}

}
