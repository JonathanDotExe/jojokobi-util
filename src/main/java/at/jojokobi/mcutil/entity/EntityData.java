package at.jojokobi.mcutil.entity;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public interface EntityData extends ConfigurationSerializable{

	public Object get (String key);
	
}
