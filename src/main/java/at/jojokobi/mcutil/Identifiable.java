package at.jojokobi.mcutil;

public interface Identifiable {
	
	public String getIdentifier ();
	
	public String getNamespace ();
	
	public default NamespacedEntry toNamespacedEntry () {
		return new NamespacedEntry(getNamespace(), getIdentifier());
	}
	
	public default String stringify() {
		return getNamespace() + ":" + getIdentifier();
	}
	
}
