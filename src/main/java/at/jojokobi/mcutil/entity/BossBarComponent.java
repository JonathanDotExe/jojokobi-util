package at.jojokobi.mcutil.entity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class BossBarComponent implements EntityComponent {
	
	private BossBar bossBar;

	public BossBarComponent(String text, BarColor color, BarStyle style) {
		bossBar = Bukkit.createBossBar(text, color, style);
	}

	@Override
	public void loop(CustomEntity<?> entity) {
		//Update Boss Bar
		HealthComponent health = entity.getComponent(HealthComponent.class);
		bossBar.setProgress((health.getHealth(entity))/health.getMaxHealth(entity));
	}
	
	@Override
	public void onDelete(CustomEntity<?> entity) {
		EntityComponent.super.onDelete(entity);
		bossBar.removeAll();
	}
	
	@Override
	public void onDamageOther(CustomEntity<?> entity, EntityDamageByEntityEvent event) {
		EntityComponent.super.onDamageOther(entity, event);
		if (event.getEntity() instanceof Player) {
			bossBar.addPlayer(((Player) event.getEntity()));
		}
	}

	@Override
	public void onDamage(CustomEntity<?> entity, EntityDamageEvent event) {
		EntityComponent.super.onDamage(entity, event);
		if (event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) event).getDamager() instanceof Player) {
			bossBar.addPlayer((Player) ((EntityDamageByEntityEvent) event).getDamager());
		}
	}
	
	@Override
	public Map<String, Object> serialize(CustomEntity<?> entity) {
		return new HashMap<>();
	}

	@Override
	public void deserialize(Map<String, Object> map, CustomEntity<?> entity) {
		
	}

}
