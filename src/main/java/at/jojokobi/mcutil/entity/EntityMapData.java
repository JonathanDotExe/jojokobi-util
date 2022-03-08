package at.jojokobi.mcutil.entity;

import java.util.HashMap;
import java.util.Map;

public class EntityMapData implements EntityData {
	
	private Map<String, Object> data;

	public EntityMapData(Map<String, Object> data) {
		super();
		this.data = data;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	@Override
	public Map<String, Object> serialize() {
		return data;
	}
	
	public static EntityData deserialize (Map<String, Object> map) {
		return new EntityMapData(new HashMap<>(map));
	}

	@Override
	public Object get(String key) {
		return data.get(key);
	}

}
