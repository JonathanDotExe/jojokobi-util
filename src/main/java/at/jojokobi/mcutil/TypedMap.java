package at.jojokobi.mcutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class TypedMap {

	private Map<String, Object> map = new HashMap<> ();
	
	public TypedMap(Map<String, Object> map) {
		super();
		this.map = map;
	}

	public Object get (String key) {
		return map.get(key);
	}
	
	public <T extends Enum<T>> T getEnum (String key, Class<T> clazz, T orElse) {
		T t = orElse;
		try {
			t = Enum.valueOf(clazz, (map.get(key) + "").toUpperCase());
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public <T> T get (String key, Class<T> clazz, T orElse) {
		T t = orElse;
		if (clazz.isInstance(map.get(key))) {
			t = clazz.cast(map.get(key));
		}
		return t;
	}
	
	public <T> List<T> getList (String key, Class<T> clazz) {
		List<T> list = new ArrayList<>();
		if (map.get(key) instanceof List<?>) {
			for (Object obj : (List<?>) map.get(key)) {
				if (clazz.isInstance(obj)) {
					list.add(clazz.cast(obj));
				}
			}
		}
		return list;
	}
	
	public <T extends Enum<T>> List<T> getEnumList (String key, Class<T> clazz) {
		List<T> list = new ArrayList<>();
		if (map.get(key) instanceof List<?>) {
			for (Object obj : (List<?>) map.get(key)) {
				try {
					list.add(Enum.valueOf(clazz, (obj + "").toUpperCase()));
				}
				catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	public String getString (String key) {
		return map.get(key) + "";
	}
	
	public int getInt (String key) {
		int number = 0;
		try {
			number = Integer.parseInt(map.get(key) + "");
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return number;
	}
	
	public long getLong (String key) {
		long number = 0;
		try {
			number = Long.parseLong(map.get(key) + "");
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return number;
	}
	
	public float getFloat (String key) {
		float number = 0;
		try {
			number = Float.parseFloat(map.get(key) + "");
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return number;
	}
	
	public double getDouble (String key) {
		double number = 0;
		try {
			number = Double.parseDouble(map.get(key) + "");
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return number;
	}
	
	public byte getByte (String key) {
		byte number = 0;
		try {
			number = Byte.parseByte(map.get(key) + "");
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return number;
	}
	
	public boolean getBoolean (String key) {
		boolean bool = false;
		try {
			bool = Boolean.parseBoolean(map.get(key) + "");
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return bool;
	}
	
	public void putEnumList (String key, List<?> list) {
		List<String> strings = new ArrayList<>();
		for (Object object : list) {
			strings.add(object + "");
		}
		map.put(key, strings);
	}
	
	public UUID getUUID (String key, Supplier<UUID> orElse) {
		UUID number = null;
		try {
			number = UUID.fromString(map.get(key) + "");
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			number = orElse.get();
		}
		return number;
	}

}
