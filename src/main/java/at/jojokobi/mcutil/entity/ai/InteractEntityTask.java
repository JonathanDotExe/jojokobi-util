package at.jojokobi.mcutil.entity.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.locatables.Locatable;
import at.jojokobi.mcutil.locatables.WrapperLocatable;

public class InteractEntityTask implements EntityTask {
	
	private TaskCondition condition;
	private double radius;
	private Entity goal;
	
	public InteractEntityTask(TaskCondition condition, double radius) {
		super();
		this.condition = condition;
		this.radius = radius;
	}

	
	@Override
	public boolean canApply(CustomEntity<?> entity) {
		List<Entity> entities = null;
		if (goal == null) {
			entities = getTargets(entity);
			for (Entity e : entities) {
				if (isInteracting(e, entity)) {
					if (e instanceof Player) {
						condition.request(entity, true);
						break;
					}
					else {
						condition.request(entity, false);
					}
				}
			}
		}
		else {
			entities = new ArrayList<Entity>();
		}

		return (goal != null || entities.size() > 0) && condition.canApply(entity);
	}
	
	@Override
	public Locatable apply(CustomEntity<?> entity) {
		Vector dir = goal.getLocation().toVector().subtract(entity.getEntity().getLocation().toVector());
		if (dir.length() != 0) {
			dir.normalize();
		}
		return new WrapperLocatable(entity.getEntity().getLocation().add(dir));
	}

	@Override
	public void activate(CustomEntity<?> entity) {
		condition.activate(entity);
		List<Entity> entities = getTargets(entity);
		
		for (Entity e : entities) {
			if (isInteracting(e, entity)) {
				if (e instanceof Player) {
					goal = e;
					break;
				}
				else {
					goal = e;
				}
			}
		}
		goal = entities.get(new Random().nextInt(entities.size()));
	}

	@Override
	public void deactivate(CustomEntity<?> entity) {
		condition.deactivate(entity);
		goal = null;
	}
	
	private boolean isInteracting(Entity e, CustomEntity<?> entity) {
		Vector dir = entity.getEntity().getLocation().toVector().subtract(e.getLocation().toVector());
		dir.setY(0);
		if (dir.length() != 0) {
			dir.normalize();
		}
		Vector entityDir = e.getLocation().getDirection();
		entityDir.setY(0);
		if (entityDir.length() != 0) {
			entityDir.normalize();
		}
		return entityDir.subtract(dir).length() < 0.3;
	}
	
	private List<Entity> getTargets(CustomEntity<?> entity) {
		return entity.getEntity().getNearbyEntities(radius, radius, radius).stream().filter(e -> (e instanceof LivingEntity && !(e instanceof Monster) && !(e instanceof ArmorStand)) || entity.getHandler().getCustomEntityForEntity(e) != null).collect(Collectors.toList());
	}
}
