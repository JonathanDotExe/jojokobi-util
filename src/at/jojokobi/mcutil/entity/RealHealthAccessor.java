package at.jojokobi.mcutil.entity;

import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * 
 * CustomEntities with this HealthAccessor need to have entities that both implement {@link Damageable} and {@link Attributable} as their base.
 * 
 * @author jojo0
 *
 */
public class RealHealthAccessor implements HealthAccessor{

	@Override
	public double getHealth(CustomEntity<?> entity) {
		return ((Damageable) entity.getEntity()).getHealth();
	}

	@Override
	public void setHealth(double health, CustomEntity<?> entity) {
		((Damageable) entity.getEntity()).setHealth(health);
	}

	@Override
	public double getMaxHealth(CustomEntity<?> entity) {
		return ((Attributable) entity.getEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
	}

	@Override
	public void onDamage(CustomEntity<?> entity, EntityDamageEvent event) {
		
	}

}
