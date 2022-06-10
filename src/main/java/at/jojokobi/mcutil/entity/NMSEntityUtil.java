package at.jojokobi.mcutil.entity;

import java.lang.reflect.Field;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftTNTPrimed;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.item.PrimedTnt;;

public final class NMSEntityUtil {

	private NMSEntityUtil() {

	}

	public static void rotateVehicle(Entity entity, float yaw, float pitch) {
		CraftEntity craftEntity = (CraftEntity) entity;
		net.minecraft.world.entity.Entity e = craftEntity.getHandle();
		e.setXRot(pitch);
		e.setYRot(yaw);
	}

	public static void rotateVehicle(Entity entity, Vector rotation) {
		Location place = entity.getLocation().setDirection(rotation);
		rotateVehicle(entity, place.getYaw(), place.getPitch());
	}

	public static void setTNTSource(TNTPrimed tnt, LivingEntity source) {
		PrimedTnt nmsTNT = ((CraftTNTPrimed) tnt).getHandle();
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
		Mob nmsEntity = (Mob) ((CraftEntity) entity).getHandle();
//		nmsEntity.goalSelector = new PathfinderGoalSelector(((CraftWorld) entity.getWorld()).getHandle().getMethodProfiler());
//		nmsEntity.targetSelector = new PathfinderGoalSelector(((CraftWorld) entity.getWorld()).getHandle().getMethodProfiler());
		GoalSelector goalSelector = nmsEntity.goalSelector;
		GoalSelector targetSelector = nmsEntity.targetSelector;

		//Clear brain
		Brain<?> controller = nmsEntity.getBrain();
		controller.removeAllBehaviors();

		/*try {
			

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
			 *
			
			Field eField = BehaviorController.class.getDeclaredField("e");
			eField.setAccessible(true);
			eField.set(controller, new LinkedHashMap<>());
			
			Field gField = BehaviorController.class.getDeclaredField("f");
			gField.setAccessible(true);
			gField.set(controller, new TreeMap<>());
			
			Field hField = BehaviorController.class.getDeclaredField("h");
			hField.setAccessible(true);
			hField.set(controller, new HashMap<>());

			Field iField = BehaviorController.class.getDeclaredField("i");
			iField.setAccessible(true);
			iField.set(controller, new HashMap<>());

			Field jField = BehaviorController.class.getDeclaredField("j");
			jField.setAccessible(true);
			jField.set(controller, new HashSet<>());
			
			Field kField = BehaviorController.class.getDeclaredField("k");
			kField.setAccessible(true);
			kField.set(controller, new HashSet<>());

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}*/

		/*try {
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
		}*/
		//Clear goal selector
		goalSelector.removeAllGoals();
		targetSelector.removeAllGoals();

		nmsEntity.goalSelector.addGoal(0, new RandomLookAroundGoal(nmsEntity));
		nmsEntity.goalSelector.addGoal(1, new LookAtPlayerGoal(nmsEntity, Mob.class, 0.0f));
		/*nmsEntity.bQ.a(0, new PathfinderGoalRandomLookaround(nmsEntity));
		nmsEntity.bQ.a(1, new PathfinderGoalLookAtPlayer(nmsEntity, EntityHuman.class, 0.0f));*/
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
