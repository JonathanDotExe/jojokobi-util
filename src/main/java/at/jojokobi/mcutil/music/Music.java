package at.jojokobi.mcutil.music;

public class Music {

	private String sound;
	private long duration;
	
	public Music(String sound, long duration) {
		super();
		this.sound = sound;
		this.duration = duration;
	}
	public String getSound() {
		return sound;
	}
	public long getDuration() {
		return duration;
	}

}
