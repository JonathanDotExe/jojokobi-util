package at.jojokobi.mcutil.entity.ai;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.locatables.Locatable;
import at.jojokobi.mcutil.locatables.WrapperLocatable;

public class ReturnToSpawnAtNightTask implements EntityTask {

	@Override
	public boolean canApply(CustomEntity<?> entity) {
		return entity.getEntity().getWorld().getTime() >= 12300;
	}

	@Override
	public Locatable apply(CustomEntity<?> entity) {
		return new WrapperLocatable(entity.getSpawnPoint());
	}

	@Override
	public void activate(CustomEntity<?> entity) {
		
	}

	@Override
	public void deactivate(CustomEntity<?> entity) {
		
	}

}
