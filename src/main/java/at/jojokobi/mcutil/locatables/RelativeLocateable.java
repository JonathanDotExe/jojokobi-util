package at.jojokobi.mcutil.locatables;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class RelativeLocateable implements Locatable{
	
	private Locatable base;
	private Vector offset;
	
	public RelativeLocateable(Locatable base, Vector offset) {
		super();
		this.base = base;
		this.offset = offset;
	}

	@Override
	public Location getLocation() {
		return base.getLocation().clone().add(offset);
	}

}
