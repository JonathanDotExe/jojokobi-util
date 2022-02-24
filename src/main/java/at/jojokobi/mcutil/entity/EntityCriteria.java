package at.jojokobi.mcutil.entity;

public interface EntityCriteria<T extends CustomEntity<?>> {
	
	public boolean matches (T entity);
	
	public Class<T> getFilterClass ();

}
