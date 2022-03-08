package at.jojokobi.mcutil.music;

import org.bukkit.entity.Player;

public class MusicPlayer {

	private Music music;
	private long start;
	private Player player;
	private boolean repeat = false;
	private boolean stopped = false;
	
	public MusicPlayer(Music music, Player player, boolean repeat) {
		super();
		this.music = music;
		this.player = player;
		this.repeat = repeat;
	}
	
	public void start () {
		start = System.currentTimeMillis();
		player.stopSound(music.getSound());
		player.playSound(player.getLocation(), music.getSound(), 1, 1);
	}
	
	public boolean canRestart() {
		return repeat && !stopped && start + music.getDuration() * 1000 <= System.currentTimeMillis();
	}
	
	public boolean ended() {
		return (!repeat && start + music.getDuration()*1000 <= System.currentTimeMillis()) || stopped;
	}
	
	public void stop () {
		player.stopSound(music.getSound());
		stopped = true;
	}
	
	public Music getMusic() {
		return music;
	}
	public long getStart() {
		return start;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public boolean isStopped() {
		return stopped;
	}

}
