package at.jojokobi.mcutil.entity;

import org.bukkit.event.entity.EntityDamageEvent;

public interface HealthAccessor {
	
	public double getHealth (CustomEntity<?> entity);
	
	public void setHealth (double health, CustomEntity<?> entity);
	
	public double getMaxHealth (CustomEntity<?> entity);
	
	public void onDamage (CustomEntity<?> entity, EntityDamageEvent event);

}
