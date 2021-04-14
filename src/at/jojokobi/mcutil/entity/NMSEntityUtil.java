package at.jojokobi.mcutil.entity;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftTNTPrimed;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_16_R3.BehaviorController;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTNTPrimed;
import net.minecraft.server.v1_16_R3.PathfinderGoal;
import net.minecraft.server.v1_16_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_16_R3.PathfinderGoalSelector;

public final class NMSEntityUtil {

	private NMSEntityUtil() {

	}

	public static void rotateVehicle(Entity entity, float yaw, float pitch) {
		CraftEntity craftEntity = (CraftEntity) entity;
		craftEntity.getHandle().yaw = yaw;
		craftEntity.getHandle().pitch = pitch;
	}

	public static void rotateVehicle(Entity entity, Vector rotation) {
		Location place = entity.getLocation().setDirection(rotation);
		rotateVehicle(entity, place.getYaw(), place.getPitch());
	}

	public static void setTNTSource(TNTPrimed tnt, LivingEntity source) {
		EntityTNTPrimed nmsTNT = ((CraftTNTPrimed) tnt).getHandle();
		try {
			Field field = nmsTNT.getClass().getDeclaredField("source");
			field.setAccessible(true);
			field.set(nmsTNT, ((CraftLivingEntity) source).getHandle());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

	}

//	public static void setBoundingBox (Entity entity, float width, float length) {
//		((CraftEntity) entity).getHandle().width = width;
//		((CraftEntity) entity).getHandle().length = length;
//	}

	public static void clearGoals(LivingEntity entity) {
		EntityInsentient nmsEntity = (EntityInsentient) ((CraftEntity) entity).getHandle();
//		nmsEntity.goalSelector = new PathfinderGoalSelector(((CraftWorld) entity.getWorld()).getHandle().getMethodProfiler());
//		nmsEntity.targetSelector = new PathfinderGoalSelector(((CraftWorld) entity.getWorld()).getHandle().getMethodProfiler());
		PathfinderGoalSelector goalSelector = nmsEntity.goalSelector;
		PathfinderGoalSelector targetSelector = nmsEntity.targetSelector;

		try {
			BehaviorController<?> controller = nmsEntity.getBehaviorController();

			/*
			 * Replace memoriesField with this in 1.14 Field aField =
			 * BehaviorController.class.getDeclaredField("a"); aField.setAccessible(true);
			 * aField.set(controller, new HashMap<>());
			 */

			// Field memoriesField = BehaviorController.class.getDeclaredField("memories");
			// memoriesField.setAccessible(true);
			// memoriesField.set(controller, new HashMap<>());

			/*
			 * Replace sensors field with this in 1.14.1 Field bField =
			 * BehaviorController.class.getDeclaredField("b"); bField.setAccessible(true);
			 * bField.set(controller, new LinkedHashMap<>());
			 */

			Field sensorsField = BehaviorController.class.getDeclaredField("sensors");
			sensorsField.setAccessible(true);
			sensorsField.set(controller, new LinkedHashMap<>());
			
			Field eField = BehaviorController.class.getDeclaredField("e");
			eField.setAccessible(true);
			eField.set(controller, new TreeMap<>());
			
			Field gField = BehaviorController.class.getDeclaredField("g");
			gField.setAccessible(true);
			gField.set(controller, new HashMap<>());

			Field hField = BehaviorController.class.getDeclaredField("h");
			hField.setAccessible(true);
			hField.set(controller, new HashMap<>());

			Field iField = BehaviorController.class.getDeclaredField("i");
			iField.setAccessible(true);
			iField.set(controller, new HashSet<>());
			
			Field jField = BehaviorController.class.getDeclaredField("j");
			jField.setAccessible(true);
			jField.set(controller, new HashSet<>());

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		try {
			Field dField;
			dField = PathfinderGoalSelector.class.getDeclaredField("d");
			dField.setAccessible(true);
			dField.set(goalSelector, new LinkedHashSet<>());
			dField.set(targetSelector, new LinkedHashSet<>());

			Field cField;
			cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			cField.set(goalSelector, new EnumMap<>(PathfinderGoal.Type.class));
			cField.set(targetSelector, new EnumMap<>(PathfinderGoal.Type.class));

			Field fField;
			fField = PathfinderGoalSelector.class.getDeclaredField("f");
			fField.setAccessible(true);
			fField.set(goalSelector, EnumSet.noneOf(PathfinderGoal.Type.class));
			fField.set(targetSelector, EnumSet.noneOf(PathfinderGoal.Type.class));
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		nmsEntity.targetSelector.a(0, new PathfinderGoalRandomLookaround(nmsEntity));
		nmsEntity.targetSelector.a(1, new PathfinderGoalLookAtPlayer(nmsEntity, EntityHuman.class, 0.0f));
	}

//	public static void test (org.bukkit.World w) {
//		World world = ((CraftWorld) w).getHandle();
//		
//		try {
//			Field field = Village.class.getDeclaredField("j");
//			field.setAccessible(true);
//			for (Village village : world.af().getVillages()) {
//				System.out.println(field.get(village));
//			}
//		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
//			e.printStackTrace();
//		}
//	}

}
