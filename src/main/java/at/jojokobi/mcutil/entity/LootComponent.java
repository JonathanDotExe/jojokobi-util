package at.jojokobi.mcutil.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.mcutil.loot.LootInventory;

public class LootComponent implements EntityComponent {
	
	private LootInventory loot;
	private int experience;

	public LootComponent(LootInventory loot, int experience) {
		super();
		this.loot = loot;
		this.experience = experience;
	}

	@Override
	public void loop(CustomEntity<?> entity) {
		
	}
	
	@Override
	public void onDamage(CustomEntity<?> entity, EntityDamageEvent event) {
		EntityComponent.super.onDamage(entity, event);
		HealthComponent health = entity.getComponent(HealthComponent.class);
		if (health.getHealth(entity) - event.getFinalDamage() < 0.5) {
			for (ItemStack stack : loot.populateLoot(new Random(), null)) {
				entity.getEntity().getWorld().dropItemNaturally(entity.getEntity().getLocation(), stack);
			}
			entity.getEntity().getWorld().spawn(entity.getEntity().getLocation(), ExperienceOrb.class, o -> o.setExperience(experience));
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
