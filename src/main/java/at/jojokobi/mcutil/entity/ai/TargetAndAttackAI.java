package at.jojokobi.mcutil.entity.ai;

import at.jojokobi.mcutil.entity.CustomEntity;

@Deprecated
public abstract class TargetAndAttackAI implements EntityAI{

	@Override
	public void changeAI(CustomEntity<?> entity) {
//		if (!(entity.getTask() instanceof LegacyAttackTask) && entity instanceof Targeter) {
//			Targeter targeter = (Targeter) entity;
//			
//			//Look for enemy
//			List<Damageable> targets = new ArrayList<>(targeter.findTargets(entity.getEntity()));
//			
//			if (!targets.isEmpty()) {
//				//entity.setTask(new LegacyAttackTask(targets.get(new Random().nextInt(targets.size()))));
//			}
//		}
	}
	
}
