package at.jojokobi.mcutil.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MultiIndexMap<K, V> implements Map<K, V>{
	
	private Map<K, V> entities = new HashMap<>();

	public Map<K, V> immutable() {
		return Collections.unmodifiableMap(entities);
	}
	
	@Override
	public V put(K k, V v) {
		V o = entities.put(k, v);
		//TODO indices
		return o;
	}
	
	@Override
	public V remove(Object key) {
		V o = entities.remove(key);
		//TODO indices
		return o;
	}

	@Override
	public int size() {
		return entities.size();
	}

	@Override
	public boolean isEmpty() {
		return entities.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return entities.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return entities.containsValue(value);
	}

	@Override
	public V get(Object key) {
		return entities.get(key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	@Override
	public void clear() {
		entities.clear();
		//TODO Indices
	}

	@Override
	public Set<K> keySet() {
		return immutable().keySet();
	}

	@Override
	public Collection<V> values() {
		return immutable().values();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return immutable().entrySet();
	}

}
