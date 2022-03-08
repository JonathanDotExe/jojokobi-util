package at.jojokobi.mcutil.music;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MusicHandler {

	private List<MusicPlayer> players = new ArrayList<>();
	
	@Deprecated
	public MusicHandler(Plugin plugin) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				for (Iterator<MusicPlayer> iterator = players.iterator(); iterator.hasNext();) {
					MusicPlayer music = iterator.next();
					if (music.canRestart()) {
						music.start();
					}
					else if (music.ended()) {
						music.stop();
						iterator.remove();
					}
				}
			}
		}, 20L, 20L);
	}
	
	public MusicPlayer getMusic (Player player) {
		MusicPlayer music = null;
		for (int i = 0; i < players.size() && music == null; i++) {
			if (players.get(i).getPlayer() == player) {
				music = players.get(i);
			}
		}
		return music;
	}
	
	public void playMusic (Music music, Player player, boolean repeat) {
		MusicPlayer old = getMusic(player);
		if (old != null) {
			old.stop();
			players.remove(old);
		}
		MusicPlayer mPlayer = new MusicPlayer(music, player, repeat);
		mPlayer.start();
		players.add(mPlayer);
	}

}
