package at.jojokobi.mcutil;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class SerializableMap implements ConfigurationSerializable {
	
	private Map<String, ?> data;

	public SerializableMap(Map<String, ?> data) {
		super();
		this.data = data;
	}

	public Map<String, ?> getData() {
		return data;
	}

	public void setData(Map<String, ?> data) {
		this.data = data;
	}

	@Override
	public Map<String, Object> serialize() {
		return new HashMap<>(data);
	}
	
	public static SerializableMap deserialize (Map<String, Object> map) {
		map.remove("==");
		return new SerializableMap(new HashMap<>(map));
	}

}
