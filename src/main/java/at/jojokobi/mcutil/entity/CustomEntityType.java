package at.jojokobi.mcutil.entity;

import org.bukkit.Location;

import at.jojokobi.mcutil.Identifiable;

public interface CustomEntityType<E extends CustomEntity<?>> extends Identifiable {

	public E createInstance (Location loc, EntityHandler handler);
	
}
