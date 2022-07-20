package at.jojokobi.mcutil.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class ListGUI<T extends ListGUIEntry> extends InventoryGUI {
	
	private List<T> items = new ArrayList<>();
	private ItemStack backButton = generateBackItem();
	private ItemStack nextButton = generateNextItem();
	private int page = 0;
	private int startIndex = 0;
	private int itemsPerPage = 9;

	public ListGUI(Player owner, Inventory inventory) {
		super(owner, inventory);
	}

	@Override
	protected void initGUI() {
		super.initGUI();
		//List
		for (int i = 0; i < Math.min(items.size() - itemsPerPage * page, itemsPerPage); i++) {
			addButton(items.get(itemsPerPage * page + i).getIcon(), startIndex + i, (item, index, click) -> {
				int pageIndex = index - startIndex;
				if (pageIndex < getItemsPerPage() && pageIndex >= 0) {
					int listIndex = getPage() * getItemsPerPage() + pageIndex;
					if (listIndex >= 0 && listIndex < items.size()) {
						clickItem(items.get(listIndex), listIndex, click);
					}
				}
			});
		}
		if (page > 0) {
			//Back Button
			addButton(backButton, getInventory().getSize() - INV_ROW, (item, index, click) -> setPage(page - 1));
		}
		if (itemsPerPage * (page + 1) < items.size()) {
			//Next Button
			addButton(nextButton, getInventory().getSize() - 1, (item, index, click) -> setPage(page + 1));
		}
	}
	
	public static ItemStack generateBackItem () {
		ItemStack item = new ItemStack(Material.IRON_INGOT);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Go Back");
		item.setItemMeta(meta);
		return item;
	}
	
	protected abstract void clickItem(T item, int index, ClickType click);
	
	public static ItemStack generateNextItem () {
		ItemStack item = new ItemStack(Material.GOLD_INGOT);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Next");
		item.setItemMeta(meta);
		return item;
	}
	
	protected List<T> getItems() {
		return items;
	}

	protected void setItems(List<T> items) {
		this.items = items;
	}

	protected int getPage() {
		return page;
	}

	protected void setPage(int page) {
		if (page < 0) {
			this.page = (int) Math.ceil(items.size()/itemsPerPage);
		}
		else if (page * itemsPerPage > items.size())  {
			this.page = 0;
		}
		else {
			this.page = page;
		}
		initGUI();
	}

	protected int getStartIndex() {
		return startIndex;
	}

	protected void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	protected int getItemsPerPage() {
		return itemsPerPage;
	}

	protected void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	protected ItemStack getBackButton() {
		return backButton;
	}

	protected void setBackButton(ItemStack backButton) {
		this.backButton = backButton;
	}

	protected ItemStack getNextButton() {
		return nextButton;
	}

	protected void setNextButton(ItemStack nextButton) {
		this.nextButton = nextButton;
	}

}
