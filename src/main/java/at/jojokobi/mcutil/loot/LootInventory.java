package at.jojokobi.mcutil.loot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import at.jojokobi.mcutil.TypedMap;

public class LootInventory implements ConfigurationSerializable {
	
	public static final String ITEM_ELEMENT = "item";
	
	private List<LootItem> items = new ArrayList<LootItem>();
	
	public List<ItemStack> populateLoot(Random random, LootContext lootContext) {
		List<ItemStack> loot = new ArrayList<>();
		for (LootItem item : items) {
			ItemStack stack = item.generateItemStack(random);
			if (stack != null && stack.getType() != Material.AIR) {
				loot.add(stack);
			}
		}
		return loot;
	}
	
	public void fillInventory (Inventory inventory, Random random, LootContext lootContext) {
		for (ItemStack item : populateLoot(random, lootContext)) {
			inventory.addItem(item);
		}
	}

	public void addItem(LootItem item) {
		items.add(item);
	}
	
	public List<LootItem> getItems() {
		return items;
	}

	public void setItems(List<LootItem> items) {
		this.items = items;
	}

	public static LootInventory fromXML (Element element) {
		LootInventory inventory = new LootInventory();
		NodeList items = element.getElementsByTagName(ITEM_ELEMENT);
		for (int i = 0; i < items.getLength(); i++) {
			if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
				inventory.addItem(LootItem.fromXML((Element) items.item(i)));
			}
		}
		return inventory;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object> ();
		map.put("items", items);
		return map;
	}

	public static LootInventory deserialize (Map<String, Object> map) {
		TypedMap tMap = new TypedMap(map);
		LootInventory inv = new LootInventory();
		inv.items = tMap.getList("items", LootItem.class);
		return inv;
	}
	
}
