package at.jojokobi.mcutil.entity.ai;

import at.jojokobi.mcutil.entity.CustomEntity;

public interface TaskCondition {
	
	public boolean canApply(CustomEntity<?> entity);
	
	public void request(CustomEntity<?> entity, boolean priority);
	
	public void activate(CustomEntity<?> entity);

	public void deactivate(CustomEntity<?> entity);

}
