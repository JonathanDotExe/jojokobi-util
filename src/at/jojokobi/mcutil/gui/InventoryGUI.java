package at.jojokobi.mcutil.gui;

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
	
	public static final String FILLER_NAME = " # # # ";
	public static final int INV_ROW = 9;
	
	public InventoryGUI (Player owner, Inventory inventory) {
		this.owner = owner;
		this.inventory = inventory;
	}
	
	protected abstract void initGUI ();
	
	protected void addButton (ItemStack button, int index) {
		inventory.setItem(index, button);
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
	
	protected abstract void onButtonPress (ItemStack button, ClickType click);

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
			onButtonPress(event.getCurrentItem(), event.getClick());
		}
		event.setCancelled(true);
	}
	
	protected InventoryGUI onInventoryClose (InventoryCloseEvent event) {
		return getNext();
	}
	
	public static ItemStack getFiller () {
		ItemStack filler = new ItemStack(Material.GLASS_PANE);
		ItemMeta meta = filler.getItemMeta();
		meta.setDisplayName(FILLER_NAME);
		filler.setItemMeta(meta);
		return filler;
	}
	
}
