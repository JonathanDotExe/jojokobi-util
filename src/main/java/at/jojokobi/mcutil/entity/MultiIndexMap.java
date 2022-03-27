package at.jojokobi.mcutil.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

class MapIndex<T, V> {
	private Map<T, V> map = new HashMap<>();
	private Function<V, T> converter;
	
	public MapIndex(Function<V, T> converter) {
		super();
		this.converter = converter;
	}
	public Map<T, V> getMap() {
		return map;
	}
	public Function<V, T> getConverter() {
		return converter;
	}
	
	public void put(V v) {
		map.put(converter.apply(v), v);
	}
	
	public void remove(V v) {
		map.remove(converter.apply(v));
	}
	
}

/**
 * Index values must be unique and are not allowed to change
 * 
 * 
 * @author jojo0
 *
 * @param <K>
 * @param <V>
 */
public class MultiIndexMap<K, V> implements Map<K, V>{
	
	private Map<K, V> entities = new HashMap<>();
	private List<MapIndex<?, V>> indices = new ArrayList<>();

	public Map<K, V> immutable() {
		return Collections.unmodifiableMap(entities);
	}
	
	public <T> Map<T, V> addIndex(Function<V, T> converter) {
		if (!isEmpty()) {
			throw new IllegalStateException("Index can't be created as there are already items in the map!");
		}
		MapIndex<T, V> index = new MapIndex<>(converter);
		indices.add(index);
		return Collections.unmodifiableMap(index.getMap());
	}
	
	@Override
	public V put(K k, V v) {
		V o = remove(k);
		//Check indices
		for (MapIndex<?, V> index : indices) {
			if (index.getMap().containsKey(index.getConverter().apply(v))) {
				throw new IllegalStateException("Map already contains values for one of the indices!");
			}
		}
		//Put
		entities.put(k, v);
		//Index
		for (MapIndex<?, V> index : indices) {
			index.put(v);
		}
		return o;
	}
	
	@Override
	public V remove(Object key) {
		V o = entities.remove(key);
		//Remove indices
		if (o != null) {
			for (MapIndex<?, V> index : indices) {
				index.remove(o);
			}
		}
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
		for (MapIndex<?, V> index : indices) {
			index.getMap().clear();
		}
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
