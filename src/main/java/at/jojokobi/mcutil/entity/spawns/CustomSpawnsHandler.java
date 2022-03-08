package at.jojokobi.mcutil.entity.spawns;

import at.jojokobi.mcutil.Handler;

public class CustomSpawnsHandler extends Handler<CustomSpawn>{
	
	private static CustomSpawnsHandler instance;
	
	public static CustomSpawnsHandler getInstance () {
		if (instance == null) {
			instance = new CustomSpawnsHandler();
		}
		return instance;
	}

	@Override
	protected CustomSpawn getStandardInstance(String namespace, String identifier) {
		return null;
	}

}
