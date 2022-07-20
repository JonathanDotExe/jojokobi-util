package at.jojokobi.mcutil.gui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


@FunctionalInterface
public interface InventoryGUIListener {

	void onClick(ItemStack item, int index, ClickType click);
	
}
