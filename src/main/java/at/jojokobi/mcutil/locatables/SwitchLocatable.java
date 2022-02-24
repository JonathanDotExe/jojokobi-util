package at.jojokobi.mcutil.locatables;

import org.bukkit.Location;

public class SwitchLocatable implements Locatable{
	
	private Locatable l1;
	private Locatable l2;
	
	boolean x;
	boolean y;
	boolean z;
	
	public SwitchLocatable(Locatable l1, Locatable l2, boolean x, boolean y, boolean z) {
		super();
		this.l1 = l1;
		this.l2 = l2;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Location getLocation() {
		Location p1 = l1.getLocation();
		Location p2 = l2.getLocation();
		return new Location(p1.getWorld(), x ? p1.getX() : p2.getX(), y ? p1.getY() : p2.getY(), z ? p1.getZ() : p2.getZ());
	}
	
}
