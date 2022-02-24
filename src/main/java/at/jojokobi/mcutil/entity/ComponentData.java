package at.jojokobi.mcutil.entity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import at.jojokobi.mcutil.SerializableMap;

public class ComponentData implements ConfigurationSerializable {
	
	private static final String CLASS_KEY = "class";
	private static final String DATA_KEY = "data";
	
	private Class<?> clazz;
	private Map<String, Object> data;
	
	public ComponentData(Class<?> clazz, Map<String, Object> data) {
		super();
		this.clazz = clazz;
		this.data = data;
	}

	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> map = new HashMap<>();
		map.put(CLASS_KEY, clazz.getName());
		map.put(DATA_KEY, new SerializableMap(data));
		return map;
	}
	
	public static ComponentData deserialize (Map<String, Object> map) {
		Class<?> clazz = null;
		Map<String, Object> data = null;
		try {
			clazz = Class.forName(map.get(CLASS_KEY) + "");
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (map.get(DATA_KEY) instanceof SerializableMap) {
			data = new HashMap<>(((SerializableMap) map.get(DATA_KEY)).getData());
		}
		return new ComponentData(clazz, data);
	}
	
	public boolean isValid () {
		return clazz != null && data != null;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public Map<String, Object> getData() {
		return data;
	}

}
