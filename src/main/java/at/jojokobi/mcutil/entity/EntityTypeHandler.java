package at.jojokobi.mcutil.entity;


import at.jojokobi.mcutil.Handler;

public class EntityTypeHandler<T extends CustomEntity<?>> extends Handler<CustomEntityType<? extends T>>{

	public EntityTypeHandler() {
		
	}

	@Override
	protected CustomEntityType<T> getStandardInstance(String namespace, String identifier) {
		return null;
	}

}
