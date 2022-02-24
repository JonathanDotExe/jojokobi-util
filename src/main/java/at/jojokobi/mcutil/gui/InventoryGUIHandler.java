package at.jojokobi.mcutil.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.Plugin;

public class InventoryGUIHandler implements Listener{
	
	private List<InventoryGUI> guis = new ArrayList<>();
	private Plugin plugin;
	
	@Deprecated
	public InventoryGUIHandler(Plugin plugin) {
		super();
		this.plugin = plugin;
	}

	protected List<InventoryGUI> getRealGUIs () {
		return guis;
	}
	
	public List<InventoryGUI> getGUIs () {
		return Collections.unmodifiableList(guis);
	}
	
	public void addGUI (InventoryGUI gui) {
		guis.add(gui);
	}
	
	public boolean removeGUI (InventoryGUI gui) {
		return guis.remove(gui);
	}
	
	public InventoryGUI getPlayersGUI (Player player) {
		InventoryGUI gui = null;
		for (int i = 0; i < guis.size() && gui == null; i++) {
			if (guis.get(i).getOwner() == player) {
				gui = guis.get(i);
			}
		}
		return gui;
	}
	
	@EventHandler
	public void onInventoryOpen (InventoryOpenEvent event) {
		for (InventoryGUI gui : new ArrayList<>(guis)) {
			if (event.getInventory().equals(gui.getInventory())) {
				gui.onInventoryOpen(event);
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick (InventoryClickEvent event) {
		for (InventoryGUI gui : new ArrayList<>(guis)) {
			if (event.getInventory().equals(gui.getInventory())) {
				gui.onInventoryClick(event);
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose (InventoryCloseEvent event) {
		for (InventoryGUI gui : new ArrayList<>(guis)) {
			if (event.getInventory().equals(gui.getInventory())) {
				guis.remove(gui);
				InventoryGUI newGui = gui.onInventoryClose(event);
				if (newGui != null) {
					addGUI(newGui);
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							newGui.show();
						}
					}, 0L);
				}
			}
		}
	}
	
}
