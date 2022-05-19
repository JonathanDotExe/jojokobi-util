package at.jojokobi.mcutil.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import at.jojokobi.mcutil.entity.ai.EntityTask;
import at.jojokobi.mcutil.io.XMLUtil;
import at.jojokobi.mcutil.locatables.Locatable;

public abstract class CustomEntity <E extends Entity> implements ConfigurationSerializable{
	
	private static final double MAX_TEMP_LOCATION_DISTANCE = 2;
	private static final double MAX_TEMP_TIME = 10 * 4;
	
	public static final String ENTITY_TAG = "entity";
	public static final String TICKS_LIVED_TAG = "ticks_lived";
	public static final String SPAWN_POINT_TAG = "spawn_point";
	public static final String DATA_TAG = "data";
	public static final String COMPONENTS_TAG = "components";
	
	public static final String AI_TAG = "ai";
	public static final String NAMESPACE_TAG = "namespace";
	public static final String KEY_TAG = "key";
	
	private E entity;
	private Location savePoint;
	private Location spawnPoint;
	private int initialLivedTicks = 1;
	private EntityHandler handler;
//	@Deprecated
//	private EntityAI ai;
//	@Deprecated
//	private LegacyEntityTask task;
	private List<EntityTask> tasks = new ArrayList<>();
	private EntityTask selectedTask = null;
	private final CustomEntityType<?> type;
	private boolean teleportToGoal = false;
	private Location tempLocation = null;
	private int tempTime = 0;
	
	private Locatable goal;
	private int time = 0;
	
	private int despawnTicks = -1;
	private boolean save = false;
	private double maxGoalDistance = 3;
	
	private Map<Class<? extends EntityComponent>, EntityComponent> components = new HashMap<>();
	
	
//	private boolean loaded = true;
	
	
	public CustomEntity(Location place, EntityHandler handler, CustomEntityType<?> type) {
		this.handler = handler;
		if (place != null) {
			this.spawnPoint = place.clone();
			this.savePoint = spawnPoint.clone();
			this.tempLocation = spawnPoint.clone();
		}
		this.type = type;
	}
	
	protected void spawn () {
		if (entity != null) {
			entity.remove();
		}
//		loaded = true;
		tempLocation = savePoint.clone();
//		savePoint.getBlock().getChunk().load();
		entity = createEntity(savePoint);
		entity.setTicksLived(initialLivedTicks);
		getComponents().forEach(c -> c.onSpawn(this));
	}
	
	public boolean canSpawn() {
		return true;
	}
	
	public void loop () {
//		spawnPoint = entity.getLocation();
		if ((entity.isDead()) || entity.getLocation().getY() < 0) {
			delete();
		}
		else {
//			if (ai != null) {
//				ai.changeAI(this);
//			}
//				
//			if (task != null) {
//				task.tick(this);
//			}
			
			manageAI();
			
			//Components
			getComponents().forEach(c -> c.loop(this));
			//Move
			if (goal != null && getEntity().getLocation().getWorld() == goal.getLocation().getWorld()) {
				Vector velocity = goal.getLocation().clone().subtract(getEntity().getLocation()).toVector();
				if (canMove() && (velocity.getX() != 0 || velocity.getY() != 0 || velocity.getZ() != 0)) {
					if (reachedGoal()) {
						NMSEntityUtil.rotateVehicle(entity, velocity);						
					}
					else {
						move(velocity);
					}
				}
			}
			//Despawn
			if (despawnTicks >= 0 && canDespawn() && getEntity().getTicksLived() > despawnTicks) {
				delete();
			}
			//Determine if entity is stuck
			if (reachedGoal() || entity.getLocation().getWorld() != tempLocation.getWorld() || entity.getLocation().distanceSquared(tempLocation) > MAX_TEMP_LOCATION_DISTANCE * MAX_TEMP_LOCATION_DISTANCE) {
				tempLocation = entity.getLocation();
				tempTime = time;
			}
		}
		time++;
	}
	
	private void manageAI () {
		EntityTask newTask = null;
		for (Iterator<EntityTask> iterator = tasks.iterator(); iterator.hasNext() && newTask == null;) {
			EntityTask task = iterator.next();
			if (task.canApply(this)) {
				newTask = task;
			}
		}
		
		if (newTask != selectedTask) {
			if (selectedTask != null) {
				selectedTask.deactivate(this);
			}
			selectedTask = newTask;
			if (selectedTask != null) {
				selectedTask.activate(this);
			}
		}
		
		if (selectedTask != null) {
			goal = selectedTask.apply(this);
		}
		else {
			goal = null;
		}
	}
	
	public List<EntityTask> getTasks() {
		return new ArrayList<>(tasks);
	}

	public boolean reachedGoal () {
		return goal != null && getEntity().getLocation().getWorld() == goal.getLocation().getWorld() && getEntity().getLocation().distanceSquared(goal.getLocation()) < maxGoalDistance * maxGoalDistance;
	}
	
	
	public abstract Class<? extends JavaPlugin> getPlugin ();
	
	protected void move (Vector velocity) {
		Location place = entity.getLocation();
		velocity.normalize();
		velocity.multiply(getGeneralSpeedMultiplier());
		NMSEntityUtil.rotateVehicle(entity, velocity);
		//Ride
		if (getEntity().getVehicle() == null) {
			//Teleport
			if (teleportToGoal && time - tempTime > MAX_TEMP_TIME) {
				getEntity().teleport(goal.getLocation());
			}
			//Fly
			else if (canFly()) {
				velocity.multiply(getFlySpeed() * getFlySpeedModifier(velocity));
				if (entity.getPassengers().size() > 0 && entity.getPassengers().get(0) instanceof Player) {
					Player player = (Player) entity.getPassengers().get(0);
					player.playSound(player.getLocation(), Sound.ITEM_ELYTRA_FLYING, 1, 1);
				}
			}
			//Swim
			else if ((place.getBlock().getType() == Material.WATER) && canSwim()) {
				velocity.multiply(getSwimSpeed());
				velocity.setY(velocity.getY() >= 0 ? 0.2 : -0.2);
			}
			//Lava Swim
			else if ((place.getBlock().getType() == Material.LAVA) && canLavaSwim()) {
				velocity.multiply(getLavaSwimSpeed());
				velocity.setY(velocity.getY() >= 0 ? 0.1 : -0.1);
			}
			//Walk
			else {
				double speed = selectedTask != null && selectedTask.isSprint() ? getSprintSpeed() : getWalkSpeed();
				velocity.multiply(speed);
				double distanceX = (getEntity().getWidth()/2.0 + 0.2) * (velocity.getX() < 0 ? -1 : 1);
				double distanceZ = (getEntity().getWidth()/2.0 + 0.2) * (velocity.getZ() < 0 ? -1 : 1);
				if (place.clone().add(distanceX, 0, distanceZ).getBlock().getType().isSolid() || place.clone().add(distanceX, 0, 0).getBlock().getType().isSolid() || place.clone().add(0, 0, distanceZ).getBlock().getType().isSolid()) {
					//Jump
//					if ((canJump() && place.clone().add(0, -0.1, 0).getBlock().getType().isSolid())) {
//						velocity.setY(getJumpSpeed());
//					}
					//Climb
					if (!jump(velocity) && canClimb()) {
						velocity.setY(getClimbSpeed());
					}
				}
				else {
					velocity.setY(entity.getVelocity().getY());
				}
			}
			getEntity().setVelocity(velocity);
		}
	}
	
	public boolean jump (Vector velocity) {
		Location place = entity.getLocation();
		boolean jump = (canJump() && place.clone().add(0, -0.1, 0).getBlock().getType().isSolid());
		if (jump) {
			velocity.setY(getJumpSpeed());
		}
		return jump;
	}
	
	public boolean jump () {
		Vector velocity = entity.getVelocity();
		boolean jump = jump(velocity);
		getEntity().setVelocity(velocity);
		return jump;
	}
	
	public void setSpawnPoint(Location spawnPoint) {
		this.spawnPoint = spawnPoint;
	}

	protected abstract E createEntity (Location place);
	
	public void addComponent (EntityComponent comp) {
		components.put(comp.getClass(), comp);
	}
	
	public <T> T getComponent (Class<T> clazz) {
		return components.containsKey(clazz) ? clazz.cast(components.get(clazz)) : null;
	}
	
	private List<EntityComponent> getComponents () {
		return Arrays.asList(components.entrySet().stream().map(e -> e.getValue()).toArray(EntityComponent[]::new));
	}
	
	/**
	 * Event passed by the handler
	 * 
	 * @param event
	 */
	protected void onDamageOther (EntityDamageByEntityEvent event) {
		for (EntityTask task : tasks) {
			task.onDamageOther(this, event);
		}
		getComponents().forEach(c -> c.onDamageOther(this, event));
	}
	
	/**
	 * Event passed by the handler
	 * 
	 * @param event
	 */
	protected void onDamage (EntityDamageEvent event) {
		for (EntityTask task : tasks) {
			task.onDamage(this, event);
		}
		getComponents().forEach(c -> c.onDamage(this, event));
	}
	
	/**
	 * Event passed by the handler
	 * 
	 * @param event
	 */
	protected void onGetMounted (EntityMountEvent event) {
		for (EntityTask task : tasks) {
			task.onGetMounted(this, event);
		}
		getComponents().forEach(c -> c.onGetMounted(this, event));
	}
	
	/**
	 * Event passed by the handler
	 * 
	 * @param event
	 */
	protected void onGetDismounted (EntityDismountEvent event) {
		for (EntityTask task : tasks) {
			task.onGetDismounted(this, event);
		}
		getComponents().forEach(c -> c.onGetDismounted(this, event));
	}
	
	/**
	 * Event passed by the handler
	 * 
	 * @param event
	 */
	protected void onProjectileHit(ProjectileHitEvent event) {
		for (EntityTask task : tasks) {
			task.onProjectileHit(this, event);
		}
		getComponents().forEach(c -> c.onProjectileHit(this, event));
	}
	
	/**
	 * Event passed by the handler
	 * 
	 * @param event
	 */
	protected void onInteract (PlayerInteractEntityEvent event) {
		event.setCancelled(true);
		for (EntityTask task : tasks) {
			task.onInteract(this, event);
		}
		getComponents().forEach(c -> c.onInteract(this, event));
	}
	
	/**
	 * Event passed by the handler
	 * 
	 * @param event
	 */
	protected void onPotionSplash (PotionSplashEvent event) {
		for (EntityTask task : tasks) {
			task.onPotionSplash(this, event);
		}
		getComponents().forEach(c -> c.onPotionSplash(this, event));
	}
	
	/**
	 * Event passed by the handler
	 * 
	 * @param event
	 */
	protected void onRegainHealth (EntityRegainHealthEvent event) {
		for (EntityTask task : tasks) {
			task.onRegainHealth(this, event);
		}
		getComponents().forEach(c -> c.onRegainHealth(this, event));
	}
	
	/**
	 * Event passed by the handler
	 * 
	 * @param event
	 */
	protected void onTransform (EntityTransformEvent event) {
		event.setCancelled(true);
		for (EntityTask task : tasks) {
			task.onTranform(this, event);
		}
		getComponents().forEach(c -> c.onTransform(this, event));
	}
	
	/**
	 * Event passed by the handler
	 * 
	 * @param event
	 */
	protected void onPortalTeleport (EntityPortalEvent event) {
		event.setCancelled(true);
		for (EntityTask task : tasks) {
			task.onPortalTeleport(this, event);
		}
		getComponents().forEach(c -> c.onPortalTeleport(this, event));
	}
	
//	public boolean isLoaded () {
//		return loaded;
//	}
//	
//	public void setLoaded(boolean loaded) {
//		this.loaded = loaded;
//	}

	protected boolean canWalk () {
		return true;
	}
	
	protected boolean canFly () {
		return false;
	}
	
	protected boolean canSwim () {
		return true;
	}
	
	protected boolean canMove() {
		return true;
	}
	
	protected boolean canLavaSwim() {
		return false;
	}
	
	protected boolean canClimb() {
		return false;
	}
	
	protected boolean canJump() {
		return true;
	}
	
	protected double getGeneralSpeedMultiplier () {
		return 1;
	}
	
	protected double getSprintSpeed () {
		return 1;
	}
	
	protected double getWalkSpeed () {
		return 0.2;
	}
	
	protected double getSwimSpeed () {
		return 1;
	}
	
	protected double getFlySpeed () {
		return 1;
	}
	
	protected double getLavaSwimSpeed () {
		return 1;
	}
	
	protected double getClimbSpeed () {
		return 0.2;
	}
	
	protected double getJumpSpeed () {
		return 0.5;
	}
	
	protected boolean canDespawn () {
		return true;
	}
	
	private float getFlySpeedModifier (Vector velocity) {
		float modifier = 1;
		float pitch = new Location(null, 0, 0, 0).setDirection(velocity).getPitch();
		if (pitch > -15) {
			modifier = 1.5f;
		}
		else if (pitch > -35) {
			modifier = 1.2f;
		}
		else if (pitch < -50) {
			modifier = 0.8f;
		}
		return modifier;
	}

	public E getEntity() {
		return entity;
	}

	public EntityHandler getHandler() {
		return handler;
	}
	
	public void delete () {
		getComponents().forEach(c -> c.onDelete(this));
		if (entity != null) {
			entity.getLocation().getChunk().load();
			entity.remove();
		}
		handler.removeEntity(this);
	}

	public Locatable getGoal() {
		return goal;
	}

	public void setGoal(Locatable goal) {
		this.goal = goal;
	}

	public int getTime() {
		return time;
	}

	public int getDespawnTicks() {
		return despawnTicks;
	}

	public void setDespawnTicks(int despawnTicks) {
		this.despawnTicks = despawnTicks;
	}

	public boolean isSave() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
	}
	
	@Override
	public final Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put(XMLUtil.LOCATITON_TAG, getEntity().getLocation());
		map.put(TICKS_LIVED_TAG, getEntity().getTicksLived());
		map.put(SPAWN_POINT_TAG, spawnPoint);
//		if (ai != null) {
//			map.put(AI_TAG, ai.toNamespacedEntry());
//		}
		map.put(DATA_TAG, saveData());
		//Components
		List<ComponentData> datas = new ArrayList<>();
		for (EntityComponent comp : getComponents()) {
			datas.add(new ComponentData(comp.getClass(), comp.serialize(this)));
		}
		map.put(COMPONENTS_TAG, datas);
		return map;
	}
	
	public final void load (Map<String, Object> map) {
		//Location
		if (map.get(XMLUtil.LOCATITON_TAG) instanceof Location) {
			this.savePoint = (Location) map.get(XMLUtil.LOCATITON_TAG);
		}
		else {
			System.err.println("Error: " + map.get(XMLUtil.LOCATITON_TAG) + " is no location");
		}
		//Ticks lived
		try {
			this.initialLivedTicks = Integer.parseInt("" + map.get(TICKS_LIVED_TAG));
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		//Spawn
		if (map.get(SPAWN_POINT_TAG) instanceof Location) {
			this.spawnPoint = (Location) map.get(SPAWN_POINT_TAG);
		}
		else {
			System.err.println("Error: " + map.get(SPAWN_POINT_TAG) + " is no location");
		}
		//AI
//		if (map.get(AI_TAG) instanceof NamespacedEntry) {
//			NamespacedEntry entry = (NamespacedEntry) map.get(AI_TAG);
//			this.ai = AIHandler.getAIController(entry.getNamespace(), entry.getIdentifier());
//		}
		//Data
		if (map.get(DATA_TAG) instanceof EntityMapData) {
			loadData((EntityMapData) map.get(DATA_TAG));
		}
		else {
			System.out.println("Error: " + map.get(DATA_TAG) + " is no EntityMapData");
		}
		//Components
		if (map.get(COMPONENTS_TAG) instanceof List<?>) {
			List<?> datas = (List<?>) map.get(COMPONENTS_TAG);
			for (Object object : datas) {
				if (object instanceof ComponentData) {
					ComponentData data = (ComponentData) object;
					if (data.isValid() && getComponent(data.getClazz()) instanceof EntityComponent) {
						((EntityComponent) getComponent(data.getClazz())).deserialize(data.getData(), this);
					}
				}
			}
		}
	}
	
	protected abstract void loadData (EntityMapData data);
	
	protected abstract EntityMapData saveData ();
	
	public final Element legacySaveToXML (Document document) {
		Element element = document.createElement(ENTITY_TAG);
		//Type
		element.setAttribute(NAMESPACE_TAG, getType().getNamespace());
		element.setAttribute(KEY_TAG, getType().getIdentifier());
		//Location
		Element location = XMLUtil.locationToXML(document, getEntity().getLocation());
		//Ticks lived
		Element ticksLived = document.createElement(TICKS_LIVED_TAG);
		ticksLived.setTextContent(getEntity().getTicksLived() + "");
		//Spawn
		Element spawnPoint = XMLUtil.locationToXML(document, this.spawnPoint, SPAWN_POINT_TAG);
		//Ai
//		Element ai = document.createElement(AI_TAG);
//		if (getAi() != null) {
//			Element namespace = document.createElement(NAMESPACE_TAG);
//			namespace.setTextContent(getAi().getNamespace());
//			
//			Element key = document.createElement(KEY_TAG);
//			key.setTextContent(getAi().getIdentifier());
//			
//			ai.appendChild(namespace);
//			ai.appendChild(key);
//		}
		//Data
		Element data = document.createElement(DATA_TAG);
		legacySaveData(data, document);
		
		//Append children
		element.appendChild(ticksLived);
		element.appendChild(location);
		element.appendChild(spawnPoint);
//		element.appendChild(ai);
		element.appendChild(data);
		return element;
	}
	
	public final void legacyParseXML (Element element) {
		//Ticks Lived
		NodeList ticksLived = element.getElementsByTagName(TICKS_LIVED_TAG);
		if (ticksLived.getLength() > 0) {
//			System.out.println("ticks lived");
			setInitialLivedTicks(Integer.parseInt(ticksLived.item(0).getTextContent()));
		}
		//Location
		NodeList locations = element.getElementsByTagName(XMLUtil.LOCATITON_TAG);
		if (locations.getLength() > 0 && locations.item(0).getNodeType() == Node.ELEMENT_NODE) {
//			System.out.println("location");
			savePoint = XMLUtil.xmlToLocation((Element)locations.item(0));
		}
		//Spawn Point
		Node spawn = element.getElementsByTagName(SPAWN_POINT_TAG).item(0);
		if (spawn != null && spawn.getNodeType() == Node.ELEMENT_NODE) {
			spawnPoint = XMLUtil.xmlToLocation((Element) spawn);
		}
		else {
			spawnPoint = savePoint.clone();
		}
		//AI
//		Node ai = element.getElementsByTagName(AI_TAG).item(0);
//		if (ai != null && ai instanceof Element) {
//			Node namespace = ((Element) ai).getElementsByTagName(NAMESPACE_TAG).item(0);
//			Node key = ((Element) ai).getElementsByTagName(KEY_TAG).item(0);
//			if (namespace != null && key != null) {
//				setAi(AIHandler.getAIController(namespace.getTextContent(), key.getTextContent()));
//			}
//			else {
//				setAi(null);
//			}
//		}
//		else {
//			setAi(null);
//		}
		//Data
		legacyParseData(element);
	}
	
	public void legacyParseData (Element element) {
		
	}
	
	public void legacySaveData (Element element, Document document) {
		
	}
	
	public Location getSavePoint () {
		return savePoint.clone();
	}
	
	public void setSavePoint (Location savePoint) {
		this.savePoint = savePoint;
	}

	public int getInitialLivedTicks() {
		return initialLivedTicks;
	}

	public void setInitialLivedTicks(int initialLivedTicks) {
		this.initialLivedTicks = Math.max(1, initialLivedTicks);
	}

//	@Deprecated
//	public LegacyEntityTask getTask() {
//		return task;
//	}
//
//	@Deprecated
//	public void setTask(LegacyEntityTask ai) {
//		this.task = ai;
//	}

	public double getMaxGoalDistance() {
		return maxGoalDistance;
	}

	public void setMaxGoalDistance(double maxGoalDistance) {
		this.maxGoalDistance = maxGoalDistance;
	}

	public Location getSpawnPoint() {
		return spawnPoint;
	}

//	@Deprecated
//	public EntityAI getAi() {
//		return ai;
//	}
//
//	@Deprecated
//	public void setAi(EntityAI aiManager) {
//		this.ai = aiManager;
//	}

	public CustomEntityType<?> getType() {
		return type;
	}

	public boolean isTeleportToGoal() {
		return teleportToGoal;
	}

	public void setTeleportToGoal(boolean teleportToGoal) {
		this.teleportToGoal = teleportToGoal;
	}

	void setHandler(EntityHandler handler) {
		this.handler = handler;
	}
	
	public void addEntityTask (EntityTask task) {
		tasks.add(task);
	}

	public EntityTask getSelectedTask() {
		return selectedTask;
	}
	
//	public void teleport(Location loc) {
////		if (!loc.getChunk().isLoaded()) {
////			loc.getChunk().load();
////		}
//		getEntity().teleport(loc);
//	}

}
