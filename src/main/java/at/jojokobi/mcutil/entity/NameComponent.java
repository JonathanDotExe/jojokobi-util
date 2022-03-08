package at.jojokobi.mcutil.entity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class NameComponent implements EntityComponent {
	
	private String name = null;
	private static final String NAME_KEY = "name";
	
	@Override
	public void onSpawn(CustomEntity<?> entity) {
		EntityComponent.super.onSpawn(entity);
		if (name != null) {
			entity.getEntity().setCustomNameVisible(true);
			entity.getEntity().setCustomName(name);
		}
		else {
			entity.getEntity().setCustomNameVisible(false);
		}
	}
	
	@Override
	public void onInteract(CustomEntity<?> entity, PlayerInteractEntityEvent event) {
		EntityComponent.super.onInteract(entity, event);
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if (item != null && item.getType() == Material.NAME_TAG) {
			name = item.getItemMeta().getDisplayName();
			entity.getEntity().setCustomName(name);
			entity.getEntity().setCustomNameVisible(true);
		}
	}

	@Override
	public void loop(CustomEntity<?> entity) {
		
	}

	@Override
	public Map<String, Object> serialize(CustomEntity<?> entity) {
		HashMap<String, Object> map = new HashMap<>();
		if (name != null) {
			map.put(NAME_KEY, name);
		}
		return map;
	}

	@Override
	public void deserialize(Map<String, Object> map, CustomEntity<?> entity) {
		if (map.get(NAME_KEY) != null) {
			name = map.get(NAME_KEY) + "";
		}
	}

}
