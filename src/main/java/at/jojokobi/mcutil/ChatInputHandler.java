package at.jojokobi.mcutil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class ChatInputHandler implements Listener{

	private Plugin plugin;
	private Map<Player, Queue<InputProcessor>> playerQueues = new HashMap<>();
	
	public ChatInputHandler(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerChat (AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		Queue<InputProcessor> queue = playerQueues.get(player);
		if (queue != null && !queue.isEmpty()) {
			InputProcessor processor = queue.poll();
			if (!event.isAsynchronous()) {
				processor.process(player, event.getMessage());
			}
			else {
				Bukkit.getScheduler().runTask(plugin, new Runnable() {
					@Override
					public void run() {
						processor.process(player, event.getMessage());
					}
				});
			}
			event.setCancelled(true);
		}
		
	}
	
	public void onPlayerQuit (PlayerQuitEvent event) {
		playerQueues.remove(event.getPlayer());
	}
	
	public void requestPlayerInput (Player player, InputProcessor proccessor) {
		Queue<InputProcessor> queue = playerQueues.get(player);
		//Add to existing queue
		if (queue != null) {
			queue.offer(proccessor);
		}
		//Create new queue
		else {
			queue = new LinkedList<>();
			queue.offer(proccessor);
			playerQueues.put(player, queue);
		}
	}
	
	public interface InputProcessor {
		
		public void process(Player player, String input);
		
	}

}
