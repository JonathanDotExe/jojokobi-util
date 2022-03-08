package at.jojokobi.mcutil.item;

import java.util.Map;

import org.bukkit.inventory.ItemStack;

import at.jojokobi.mcutil.Handler;
import at.jojokobi.mcutil.NamespacedEntry;

public final class ItemHandler extends Handler<CustomItem>{
	
	private static ItemHandler handler = new ItemHandler();
	
	private ItemHandler() {
		super();
	}
	
	public static Map<NamespacedEntry, CustomItem> getAllItems() {
		return handler.getItems();
	}
	
	public static CustomItem getCustomItem (String namespace, String identifier) {
		return handler.getItem(namespace, identifier);
	}
	
	public static ItemStack getItemStack (String namespace, String identifier) {
		return getCustomItem(namespace, identifier).createItem();
	}
	
	public static void addCustomItem (CustomItem item) {
		handler.addItem(item);
	}
	
	public static CustomItem getCustomItem (ItemStack item) {
		for (CustomItem custom : handler.getItemList()) {
			if (custom.isItem(item)) {
				return custom;
			}
		}
		return null;
	}
	
	public static <T extends CustomItem> T getCustomItem (Class<T> clazz) {
		return clazz.cast(handler.getItemList().stream().filter(e -> clazz.isInstance(e)).findFirst().orElseGet(() -> null));
	}
	
	public static ItemStack getItemStack (Class<? extends CustomItem> clazz) {
		return getCustomItem(clazz).createItem();
	}
	
	@Override
	protected CustomItem getStandardInstance(String identifier, String namespace) {
		return null;
	}

}
