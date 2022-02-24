package at.jojokobi.mcutil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Handler <T extends Identifiable>{

	Map<NamespacedEntry, T> items = new HashMap<>();
	
	
	public Map<NamespacedEntry, T> getItems() {
		return Collections.unmodifiableMap(items);
	}
	
	public T getItem (NamespacedEntry entry) {
		T item = items.get(entry);
		if (item == null) {
			item = getStandardInstance(entry.getNamespace(), entry.getIdentifier());
		}
		return item;
	}
	
	public T getItem (String namespace, String identifier) {
		return getItem(new NamespacedEntry(namespace, identifier));
	}
	
	public void addItem (T item) {
		items.put(new NamespacedEntry(item.getNamespace(), item.getIdentifier()), item);
	}
	
	public void loadItems (ItemProvider<T> provider) {
		for (T t : provider.provide()) {
			addItem(t);
		}
	}
	
	public List<T> getItemList () {
		return new ArrayList<T>(items.values());
	}
	
	protected abstract T getStandardInstance (String namespace, String identifier);
}
