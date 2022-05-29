package at.jojokobi.mcutil.entity;

import java.util.Set;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;

import at.jojokobi.mcutil.locatables.EntityLocatable;
import at.jojokobi.mcutil.locatables.Locatable;

public interface Attacker{

	public void attack (Damageable entity);
	
	public int getAttackDelay ();
	
	public default void attack (Set<Damageable> entities) {
		for (Damageable damageable : entities) {
			attack(damageable);
		}
	}
	
	public double getAttackRange ();
	
	public default boolean chaseWhenInRange () {
		return true;
	}
	
	public default Locatable createInRangeLocatable (Entity attacker, Entity target) {
		if (chaseWhenInRange()) {
			return new EntityLocatable(target);
		}
		else {
			return new EntityLocatable(attacker);
		}
	}
	
	public default boolean defeatedEnemy (Damageable enemy) {
		return enemy.isDead();
	}
	
	public default boolean attackWhenNoLineOfSight () {
		return false;
	}
	
	public default boolean isMultiTarget ()  {
		return false;
	}
	
}
