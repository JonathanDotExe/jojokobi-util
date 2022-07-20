package at.jojokobi.mcutil.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public abstract class TextInputGUI extends InventoryGUI{
	
	private ItemStack item;

	public TextInputGUI(Player owner, ItemStack item, String text) {
		super(owner, Bukkit.createInventory(owner, InventoryType.ANVIL, text));
		this.item = item;
		initGUI();
	}

	@Override
	protected void initGUI() {
		super.initGUI();
		addButton(item, 0, null);
	}

	@Override
	protected void onButtonPress(ItemStack button, int slot, ClickType click) {
		close();
	}

	@Override
	protected InventoryGUI onInventoryClose(InventoryCloseEvent event) {
		ItemStack item = getInventory().getItem(2);
		if (item != null) {
			processText(item.getItemMeta().getDisplayName());
		}
		return super.onInventoryClose(event);
	}
	
	public abstract void processText (String text);

}
