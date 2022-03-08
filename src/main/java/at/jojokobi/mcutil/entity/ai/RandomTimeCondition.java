package at.jojokobi.mcutil.entity.ai;

import java.util.Random;

import at.jojokobi.mcutil.entity.CustomEntity;

public class RandomTimeCondition implements TaskCondition {
	
	private int minTaskTime;
	private int maxTaskTime;
	private int minWaitTime;
	private int maxWaitTime;
	
	private int switchTime = 0;
	private ApplyState state = ApplyState.WAIT;
	private boolean requested = false;
	
	private Random random = new Random();
	
	public RandomTimeCondition(int minTaskTime, int maxTaskTime, int minWaitTime, int maxWaitTime) {
		super();
		this.minTaskTime = minTaskTime;
		this.maxTaskTime = maxTaskTime;
		this.minWaitTime = minWaitTime;
		this.maxWaitTime = maxWaitTime;
	}

	@Override
	public boolean canApply(CustomEntity<?> entity) {
		return state == ApplyState.APPLY ? entity.getTime() < switchTime : entity.getTime() >= switchTime;
	}
	
	@Override
	public void request(CustomEntity<?> entity, boolean priority) {
		if ((state == ApplyState.WAIT && !requested) || (priority && state != ApplyState.APPLY)) {
			switchTime = entity.getTime();
			requested = true;
		}
	}

	@Override
	public void activate(CustomEntity<?> entity) {
		switchTime = entity.getTime() + random.nextInt(maxTaskTime - minTaskTime + 1) + minTaskTime;
		state = ApplyState.APPLY;
	}

	@Override
	public void deactivate(CustomEntity<?> entity) {
		switchTime = entity .getTime() + random.nextInt(maxWaitTime - minWaitTime + 1) + minWaitTime;
		state = requested ? ApplyState.LOCKED : ApplyState.WAIT;
		requested = false;
	}	

}

enum ApplyState {
	APPLY, WAIT, LOCKED;
}