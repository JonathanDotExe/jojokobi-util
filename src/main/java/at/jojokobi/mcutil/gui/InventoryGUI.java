package at.jojokobi.mcutil.gui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class InventoryGUI {
	
	private Player owner;
	private Inventory inventory;
	private InventoryGUI next;
	
	private Map<Integer, InventoryGUIListener> events = new HashMap<Integer, InventoryGUIListener>();
	
	public static final String FILLER_NAME = " # # # ";
	public static final int INV_ROW = 9;
	
	public InventoryGUI (Player owner, Inventory inventory) {
		this.owner = owner;
		this.inventory = inventory;
	}
	
	protected void initGUI () {
		inventory.clear();
		events.clear();
	}
	
	protected void addButton (ItemStack button, int index, InventoryGUIListener action) {
		inventory.setItem(index, button);
		if (action == null) {
			events.remove(index);
		}
		else {
			events.put(index, action);
		}
	}
	
	protected void fillEmpty (ItemStack fill) {
		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) == null) {
				inventory.setItem(i, fill);
			}
		}
	}
	
	public void show () {
		owner.openInventory(inventory);
	}
	
	public void close () {
		owner.closeInventory();
	}
	
	public Player getOwner() {
		return owner;
	}

	public Inventory getInventory() {
		return inventory;
	}
	
	public InventoryGUI getNext() {
		return next;
	}

	public void setNext(InventoryGUI next) {
		this.next = next;
	}

	protected void onInventoryOpen (InventoryOpenEvent event) {
		
	}
	
	protected void onInventoryClick (InventoryClickEvent event) {
		if (inventory.contains(event.getCurrentItem())) {
			InventoryGUIListener l = events.get(event.getSlot());
			if (l != null) {
				l.onClick(event.getCurrentItem(), event.getSlot(), event.getClick());
			}
			onButtonPress(event.getCurrentItem(), event.getSlot(), event.getClick());
			
		}
		event.setCancelled(true);
	}
	
	protected InventoryGUI onInventoryClose (InventoryCloseEvent event) {
		return getNext();
	}
	
	protected void onButtonPress(ItemStack button, int slot, ClickType click) {
		
	}
	
	public static ItemStack getFiller () {
		ItemStack filler = new ItemStack(Material.GLASS_PANE);
		ItemMeta meta = filler.getItemMeta();
		meta.setDisplayName(FILLER_NAME);
		filler.setItemMeta(meta);
		return filler;
	}
	
}
