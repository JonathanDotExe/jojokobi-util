package at.jojokobi.mcutil.entity.ai;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.Ownable;
import at.jojokobi.mcutil.locatables.Locatable;
import at.jojokobi.mcutil.locatables.WrapperLocatable;

public class SitTask implements EntityTask {

	@Override
	public boolean canApply(CustomEntity<?> entity) {
		return entity instanceof Ownable && ((Ownable) entity).getOwner() != null && entity.getComponent(SitComponent.class) != null && entity.getComponent(SitComponent.class).isSitting();
	}

	@Override
	public Locatable apply(CustomEntity<?> entity) {
		Locatable goal = null;
		if (entity.getComponent(SitComponent.class).getSitLocation() != null) {
			goal = new WrapperLocatable(entity.getComponent(SitComponent.class).getSitLocation());
		}
		return goal;
	}

	@Override
	public void activate(CustomEntity<?> entity) {
		
	}

	@Override
	public void deactivate(CustomEntity<?> entity) {
		
	}

}
