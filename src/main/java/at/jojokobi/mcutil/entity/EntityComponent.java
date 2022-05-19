package at.jojokobi.mcutil.entity;

import java.util.Map;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

public interface EntityComponent {
	
	public void loop (CustomEntity<?> entity);
	
	public default void onSpawn (CustomEntity<?> entity) {
		
	}
	
	public default void onDelete (CustomEntity<?> entity) {
		
	}
	
	public default void onDamageOther (CustomEntity<?> entity, EntityDamageByEntityEvent event) {
		
	}
	
	public default void onDamage (CustomEntity<?> entity, EntityDamageEvent event) {
		
	}
	
	public default void onGetMounted (CustomEntity<?> entity, EntityMountEvent event) {
		
	}
	
	public default void onGetDismounted (CustomEntity<?> entity, EntityDismountEvent event) {
		
	}
	
	public default void onProjectileHit(CustomEntity<?> entity, ProjectileHitEvent event) {
		
	}
	
	public default void onInteract (CustomEntity<?> entity, PlayerInteractEntityEvent event) {
		
	}
	
	public default void onPotionSplash (CustomEntity<?> entity, PotionSplashEvent event) {
		
	}
	
	public default void onRegainHealth (CustomEntity<?> entity, EntityRegainHealthEvent event) {
		
	}
	
	public default void onTransform (CustomEntity<?> entity, EntityTransformEvent event) {
		
	}
	
	public default void onPortalTeleport (CustomEntity<?> entity, EntityPortalEvent event) {
		
	}
	
	
	public Map<String, Object> serialize (CustomEntity<?> entity);
	
	public void deserialize (Map<String, Object> map, CustomEntity<?> entity);

}
