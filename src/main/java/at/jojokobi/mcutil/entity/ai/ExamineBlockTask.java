package at.jojokobi.mcutil.entity.ai;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.block.Block;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.locatables.Locatable;
import at.jojokobi.mcutil.locatables.WrapperLocatable;

public class ExamineBlockTask implements EntityTask{
	
	private Predicate<Block> verifier;
	private Consumer<Block> action;
	
	private int range;
	
	private Block target;
	
	

	public ExamineBlockTask(Predicate<Block> verifier, Consumer<Block> action, int range) {
		super();
		this.verifier = verifier;
		this.action = action;
		this.range = range;
	}

	@Override
	public boolean canApply(CustomEntity<?> entity) {
		return target != null || findTarget(entity.getEntity().getLocation()) != null;
	}

	@Override
	public Locatable apply(CustomEntity<?> entity) {
		if (target == null) {
			target = findTarget(entity.getEntity().getLocation());
		}
		entity.setGoal(new WrapperLocatable(target.getLocation()));
		if (entity.reachedGoal()) {
			action.accept(target);
			target = null;
		}
		return target != null ? new WrapperLocatable(target.getLocation()) : null;
	}

	@Override
	public void activate(CustomEntity<?> entity) {
		target = findTarget(entity.getEntity().getLocation());
	}

	@Override
	public void deactivate(CustomEntity<?> entity) {
		target = null;
	}
	
	private Block findTarget (Location loc) {
		for (int x = loc.getBlockX() - range; x < loc.getBlockX() + range; x++) {
			for (int z = loc.getBlockZ() - range; z < loc.getBlockZ() + range; z++) {
				Location place = new Location(loc.getWorld(), x, 0, z);
				place.setY(place.getWorld().getHighestBlockYAt(place) - 1);
				if (verifier.test(place.getBlock())) {
					return place.getBlock();
				}
			}
		}
		return null;
	}

}
