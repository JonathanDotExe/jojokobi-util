package at.jojokobi.mcutil.loot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import at.jojokobi.mcutil.TypedMap;
import at.jojokobi.mcutil.item.CustomTool;
import at.jojokobi.mcutil.item.ItemHandler;

public class LootItem implements ConfigurationSerializable{

	public static final String CHANCE_ELEMENT = "chance";
	public static final String MATERIAL_ELEMENT = "material";
	public static final String ITEM_ELEMENT = "item";
	public static final String MIN_COUNT_ELEMENT_OLD = "min_count";
	public static final String MAX_COUNT_ELEMENT_OLD = "max_count";
	public static final String MIN_COUNT_ELEMENT = "minCount";
	public static final String MAX_COUNT_ELEMENT = "maxCount";
	public static final String ENCHANT_ELEMENT = "enchant";
	
	private double chance = 1;
	private ItemStack item;
	private int maxCount = 1;
	private int minCount = 1;
	private boolean enchant = false;
	private boolean damage = true;

	public LootItem(double chance, ItemStack item, int minCount, int maxCount) {
		this.chance = chance;
		this.item = item;
		if (maxCount >= minCount) {
			this.maxCount = maxCount;
			this.minCount = minCount;
		} else {
			this.maxCount = minCount;
			this.minCount = maxCount;
		}
	}
	
	private LootItem() {
		this(1, new ItemStack(Material.AIR), 1, 1);
	}
	
	public ItemStack generateItemStack(Random random) {
		ItemStack item = null;
		if (random.nextDouble() < chance) {
			item = new ItemStack(this.item);
			int amount = minCount + random.nextInt(maxCount - minCount + 1);
			item.setAmount(amount);
			//damage
			if (damage && item.getItemMeta() instanceof Damageable && item.getType().getMaxDurability() > 0 && !item.getItemMeta().isUnbreakable()) {
				ItemMeta meta =  item.getItemMeta();
				((Damageable) meta).setDamage(random.nextInt(item.getType().getMaxDurability()));
				item.setItemMeta(meta);
			}
			else if (damage && ItemHandler.getCustomItem(item) instanceof CustomTool) {
				CustomTool tool = ((CustomTool) ItemHandler.getCustomItem(item));
				tool.setDurability(item, random.nextInt(tool.getMaxDurability()) + 1);
			}
			//Enchant
			if (enchant) {
				if (item.getItemMeta() instanceof EnchantmentStorageMeta) {
					Enchantment ench = Enchantment.values()[random.nextInt(Enchantment.values().length)];
					EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
					meta.addStoredEnchant(ench, random.nextInt(ench.getMaxLevel()) + 1, true);
					item.setItemMeta(meta);
				}
				else {
					for (int i = 0; i < 3; i++) {
						Enchantment ench = Enchantment.values()[random.nextInt(Enchantment.values().length)];
						
						if (ench.canEnchantItem(item)) {
							item.addEnchantment(ench, random.nextInt(ench.getMaxLevel()) + 1);
						}
					}
				}
			}
		}
		return item;
	}

	public static LootItem fromXML(Element element) {
		LootItem item = new LootItem();
		// Chance
		Node chance = element.getElementsByTagName(CHANCE_ELEMENT).item(0);
		if (chance != null) {
			try {
				item.chance = Double.parseDouble(chance.getTextContent());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		// Material
		Node material = element.getElementsByTagName(MATERIAL_ELEMENT).item(0);
		if (material != null) {
			try {
				item.item = new ItemStack(Material.valueOf(material.getTextContent().toUpperCase()));
				System.out.println(item.item);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		// Min Count
		Node minCount = element.getElementsByTagName(MIN_COUNT_ELEMENT_OLD).item(0);
		if (minCount != null) {
			try {
				item.minCount = Integer.parseInt(minCount.getTextContent());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		// Max Count
		Node maxCount = element.getElementsByTagName(MAX_COUNT_ELEMENT_OLD).item(0);
		if (maxCount != null) {
			try {
				item.maxCount = Integer.parseInt(maxCount.getTextContent());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return item;
	}

	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> map = new HashMap<>();
		map.put(CHANCE_ELEMENT, chance);
		map.put(ITEM_ELEMENT, item);
		map.put(MAX_COUNT_ELEMENT, maxCount);
		map.put(MIN_COUNT_ELEMENT, minCount);
		map.put(ENCHANT_ELEMENT, enchant);
		return map;
	}
	
	public static LootItem deserialize (Map<String, Object> map) {
		TypedMap tMap = new TypedMap(map);
		return new LootItem(tMap.getDouble(CHANCE_ELEMENT), tMap.get(ITEM_ELEMENT, ItemStack.class, new ItemStack(Material.AIR)), tMap.getInt(MIN_COUNT_ELEMENT), tMap.getInt(MAX_COUNT_ELEMENT)).setEnchant(tMap.getBoolean(ENCHANT_ELEMENT));
	}

	public boolean isEnchant() {
		return enchant;
	}

	public LootItem setEnchant(boolean enchant) {
		this.enchant = enchant;
		return this;
	}

	public boolean isDamage() {
		return damage;
	}

	public LootItem setDamage(boolean damage) {
		this.damage = damage;
		return this;
	}

}
