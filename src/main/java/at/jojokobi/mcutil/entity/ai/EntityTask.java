package at.jojokobi.mcutil.entity.ai;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.locatables.Locatable;

public interface EntityTask {

	public boolean canApply (CustomEntity<?> entity);
	
	public Locatable apply (CustomEntity<?> entity);
	
	public void activate (CustomEntity<?> entity);
	
	public void deactivate (CustomEntity<?> entity);
	
	public default boolean isSprint () {
		return false;
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

	public default void onTranform(CustomEntity<?> entity, EntityTransformEvent event) {
		
	}
	
	public default void onPortalTeleport(CustomEntity<?> entity, EntityTeleportEvent event) {
		
	}
	
}
