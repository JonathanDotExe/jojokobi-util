package at.jojokobi.mcutil.entity.ai;

import at.jojokobi.mcutil.Identifiable;
import at.jojokobi.mcutil.entity.CustomEntity;

@Deprecated
public interface EntityAI extends Identifiable{

	public void changeAI (CustomEntity<?> entity);
	
}
