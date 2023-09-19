package at.jojokobi.mcutil.entity;

import org.bukkit.event.entity.EntityDamageEvent;

public class PseudoHealthAccessor implements HealthAccessor {
	
	private double health;
	private double maxHealth;

	public PseudoHealthAccessor(double maxHealth) {
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}

	@Override
	public double getHealth(CustomEntity<?> entity) {
		return health;
	}

	@Override
	public void setHealth(double health, CustomEntity<?> entity) {
		this.health = Math.min(maxHealth, health);
	}

	@Override
	public double getMaxHealth(CustomEntity<?> entity) {
		return maxHealth;
	}

	@Override
	public void onDamage(CustomEntity<?> entity, EntityDamageEvent event) {
		event.setCancelled(true);
		//Apply damage later so the entire processing cycle deals with the same values
		entity.getHandler().runTaskLater(() -> {
			health -= event.getFinalDamage();
			if (health <= 0.0) {
				entity.delete();
			}
		}, 0);
	}

}
