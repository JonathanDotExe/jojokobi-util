package at.jojokobi.mcutil;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class NamespacedEntry implements Identifiable, ConfigurationSerializable{

	private final String identifier;
	private final String namespace;

	public NamespacedEntry(String namespace, String identifier) {
		super();
		this.identifier = identifier;
		this.namespace = namespace;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NamespacedEntry other = (NamespacedEntry) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (namespace == null) {
			if (other.namespace != null)
				return false;
		} else if (!namespace.equals(other.namespace))
			return false;
		return true;
	}
	
	public static boolean validateNamespace (String namespace) {
		return namespace != null && !namespace.isEmpty() && !namespace.contains(":");
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put("namespace", namespace);
		map.put("key", identifier);
		return map;
	}
	
	public static NamespacedEntry deserialize (Map<String, Object> map) {
		NamespacedEntry entry = new NamespacedEntry(map.get("namespace") + "", map.get("key") + "");
//		if (entry.identifier == null || entry.namespace == null) {
//			entry = null;
//		}
		return entry;
	}
	
	@Override
	public NamespacedEntry toNamespacedEntry() {
		return this;
	}

}
