package at.jojokobi.mcutil.locatables;

import org.bukkit.Location;

public class WrapperLocatable implements Locatable {

	private Location location;

	public WrapperLocatable(Location location) {
		super();
		this.location = location;
	}

	@Override
	public Location getLocation() {
		return location;
	}

}
