package at.jojokobi.mcutil;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class TimeUUIDGenerator implements UUIDGenerator {

	private long lastTime = 0;
	private Set<Long> longs = new HashSet<Long>();
	
	@Override
	public UUID nextUUID() {
		UUID uuid = null;
		Random random = new Random();
		while (uuid == null) {
			//Clear
			long time = System.currentTimeMillis();
			if (lastTime != time) {
				longs.clear();
				lastTime = time;
			}
			uuid = new UUID(lastTime, random.nextLong());
			if (longs.contains(uuid.getLeastSignificantBits())) {
				uuid = null;
			}
		}
		
		longs.add(uuid.getLeastSignificantBits());
		return uuid;
	}

}
