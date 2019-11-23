package at.jojokobi.mcutil.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class ListGUI extends InventoryGUI{
	
	private List<ItemStack> items = new ArrayList<>();
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
		//List
		for (int i = 0; i < Math.min(items.size() - itemsPerPage * page, itemsPerPage); i++) {
			addButton(items.get(itemsPerPage * page + i), startIndex + i);
		}
		//Back Button
		addButton(backButton, getInventory().getSize() - INV_ROW);
		//Next Button
		addButton(nextButton, getInventory().getSize() - 1);
	}
	
	public boolean checkPageButton (ItemStack button) {
		boolean isButton = false;
		if (backButton.equals(button)) {
			setPage(page - 1); 
			isButton = true;
		}
		else if (nextButton.equals(button)) {
			setPage(page + 1); 
			isButton = true;
		}
		return isButton;
	}
	
	public static ItemStack generateBackItem () {
		ItemStack item = new ItemStack(Material.IRON_INGOT);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Go Back");
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack generateNextItem () {
		ItemStack item = new ItemStack(Material.GOLD_INGOT);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Next");
		item.setItemMeta(meta);
		return item;
	}
	
	protected List<ItemStack> getItems() {
		return items;
	}

	protected void setItems(List<ItemStack> items) {
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
