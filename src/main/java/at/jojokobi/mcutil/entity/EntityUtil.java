package at.jojokobi.mcutil.entity;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.FluidCollisionMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public final class EntityUtil {

	private EntityUtil() {
		
	}
	
	public static boolean canHit (Entity entity, Entity other) {
		Vector diff = other.getLocation().add(0, 1.4, 0).subtract(entity.getLocation().add(0,1.4, 0)).toVector();
		RayTraceResult result = null;
		if (diff.lengthSquared() != 0) {
			result = entity.getWorld().rayTraceBlocks(entity.getLocation(), diff, entity.getLocation().distance(other.getLocation()), FluidCollisionMode.NEVER, true);
		}
		boolean canSee = entity.getWorld() == other.getWorld() && (result == null || result.getHitEntity() == other || result.getHitEntity() == entity);
		return canSee;
	}
	
	public static List<Player> getNearbyPlayers (Entity entity, double x, double y, double z) {
		List<Player> players = new ArrayList<>();
		for (Entity e : entity.getNearbyEntities(x, y, z)) {
			if (e instanceof Player) {
				players.add((Player) e);
			}
		}
		return players;
	}
	
	public static String getEntityName (EntityType type) {
		StringBuilder builder = new StringBuilder();
		char[] chars = type.toString().toLowerCase().replace('_', ' ').toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (i == 0 || chars[i - 1] == ' ') {
				builder.append(Character.toUpperCase(chars[i]));
			}
			else {
				builder.append(chars[i]);
			}
		}
		return builder.toString();
	}

}
