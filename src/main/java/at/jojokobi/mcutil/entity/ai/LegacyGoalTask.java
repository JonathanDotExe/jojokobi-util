package at.jojokobi.mcutil.entity.ai;


import org.bukkit.Location;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.locatables.Locatable;
import at.jojokobi.mcutil.locatables.WrapperLocatable;
@Deprecated
public class LegacyGoalTask implements LegacyEntityTask{
	
	private Locatable goal;
	
	public LegacyGoalTask(Locatable goal) {
		super();
		this.goal = goal;
	}

	public LegacyGoalTask(Location goal) {
		this(new WrapperLocatable(goal));
	}

	@Override
	public void tick(CustomEntity<?> entity) {
		if (entity.getGoal() != goal) {
			entity.setGoal(goal);
		}
	}

}
