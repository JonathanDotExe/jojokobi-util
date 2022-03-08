package at.jojokobi.mcutil.item;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public abstract class CustomTool extends CustomItem{

	public static final String DURABILITY_TAG = "durability";
	
	private int maxDurability = 0;
	private Material repairMaterial = null;
	
	private final NamespacedKey durabilityKey;
	
	public CustomTool(String namespace, String identifier) {
		super(namespace, identifier);
		durabilityKey = createUtilKey(DURABILITY_TAG);
	}
	
	@Override
	public ItemStack createItem() {
		ItemStack item = super.createItem();
		setDurability(item, maxDurability);
		return item;
	}
	
	public void setDurability (ItemStack item, int durability) {
		ItemMeta meta = item.getItemMeta();
		setItemDataInt(meta, durabilityKey, durability);
		meta.setLore(Arrays.asList(durability + "/" + maxDurability));
		item.setItemMeta(meta);
	}
	
	public int getDurability (ItemStack item) {
		return item.getItemMeta().getPersistentDataContainer().get(durabilityKey, PersistentDataType.INTEGER);
	}
	
	@Override
	protected void fixItem(ItemStack item) {
		super.fixItem(item);
		int durability = ItemUtil.getNBTInt(item, DURABILITY_TAG);
		ItemUtil.removeNBTTag(item, DURABILITY_TAG);
		setDurability(item, durability);
	}
	
	@Override
	public boolean onUse(ItemStack item, PlayerInteractEvent event) {
		boolean used = useItem(item, event);
		if (used) {
			int durability = getDurability(item);
			if (durability > 0) {
				setDurability(item, durability - 1);
			}
			else {
				item.setAmount(0);
			}
		}
		return used;
	}
	
	@Override
	public void onHit(ItemStack item, EntityDamageByEntityEvent event) {
		super.onHit(item, event);
		if (hit(item, event)) {
			int durability = getDurability(item);
			if (durability > 0) {
				setDurability(item, durability - 1);
			}
			else {
				item.setAmount(0);
			}
		}
	}
	
	@Override
	public boolean onUse(ItemStack item, Player player) {
		return false;
	}
	
	@Override
	public void onHit(ItemStack item, Entity damager, Entity defender) {
		
	}
	
	@EventHandler
	public void onPrepareAnvil (PrepareAnvilEvent event) {
		if (repairMaterial != null && isItem(event.getInventory().getItem(0)) && event.getInventory().getItem(1) != null && event.getInventory().getItem(1).getType() == repairMaterial) {
			ItemStack result = event.getInventory().getItem(0).clone();
			setDurability(result, Math.min(getMaxDurability(), getDurability(result) + getMaxDurability()/4 * event.getInventory().getItem(1).getAmount()));
			event.getInventory().setRepairCost(event.getInventory().getItem(1).getAmount());
			event.getInventory().setItem(2, result);
			event.setResult(result);
		}
	}
	
	@Override
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		super.onInventoryClick(event);
		if (event.getInventory() != null && event.getInventory().getType() == InventoryType.ANVIL && event.getInventory().getItem(1) != null && event.getInventory().getItem(1).getType() == repairMaterial&& event.getRawSlot() == 2 && isItem(event.getCurrentItem())) {
			event.getWhoClicked().getInventory().addItem(event.getCurrentItem());
			event.getInventory().remove(event.getClickedInventory().getItem(0));
			event.getInventory().remove(event.getClickedInventory().getItem(1));
			event.getInventory().remove(event.getClickedInventory().getItem(2));
		}
	}
	
	public boolean useItem (ItemStack item, PlayerInteractEvent event) {
		return useItem(item, event.getPlayer());
	}
	
	public boolean hit (ItemStack item, EntityDamageByEntityEvent event) {
		return hit(item, event.getDamager(), event.getEntity());
	}
	
	public abstract boolean useItem (ItemStack item, Player player);
	
	public abstract boolean hit (ItemStack item, Entity damager, Entity defender);

	public int getMaxDurability() {
		return maxDurability;
	}

	protected void setMaxDurability(int durability) {
		this.maxDurability = durability;
	}

	public Material getRepairMaterial() {
		return repairMaterial;
	}

	public void setRepairMaterial(Material repairMaterial) {
		this.repairMaterial = repairMaterial;
	}
	
}
