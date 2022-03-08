package at.jojokobi.mcutil.entity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.entity.EntityDamageEvent;

import at.jojokobi.mcutil.TypedMap;

public class HealthComponent implements EntityComponent{

	private HealthAccessor accessor;

	public HealthComponent(HealthAccessor accessor) {
		super();
		this.accessor = accessor;
	}

	@Override
	public void loop(CustomEntity<?> entity) {
		
	}
	
	@Override
	public void onDamage(CustomEntity<?> entity, EntityDamageEvent event) {
		EntityComponent.super.onDamage(entity, event);
		accessor.onDamage(entity, event);
	}

	@Override
	public Map<String, Object> serialize(CustomEntity<?> entity) {
		Map<String, Object> map = new HashMap<>();
		map.put("health", accessor.getHealth(entity));
		return map;
	}
	
	@Override
	public void deserialize(Map<String, Object> map, CustomEntity<?> entity) {
		TypedMap tMap = new TypedMap(map);
		accessor.setHealth(tMap.getDouble("health"), entity);
	}
	
	public double getHealth (CustomEntity<?> entity) {
		return accessor.getHealth(entity);
	}
	
	public void setHealth (double health, CustomEntity<?> entity) {
		accessor.setHealth(health,entity);
	}
	
	public double getMaxHealth (CustomEntity<?> entity) {
		return accessor.getMaxHealth(entity);
	}

}
