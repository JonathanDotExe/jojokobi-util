package at.jojokobi.mcutil.item;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import at.jojokobi.mcutil.Identifiable;
import at.jojokobi.mcutil.JojokobiUtilPlugin;
import at.jojokobi.mcutil.NamespacedEntry;

public abstract class CustomItem implements Listener, Identifiable {

	private Material material = Material.AIR;
	private String name = "Item";
	private final String identifier;
	private final String namespace;
	private boolean hideFlags = false;
	private int maxStackSize = 0;
	private short meta = 1;
	private int damage = 0;
	private double speed = 0;

	private boolean helmet = false;

	public static final String IDENTIFIER_TAG = "identifier";
	public static final String NAMESPACE_TAG = "namespace";

	private final NamespacedKey identifierKey;
	private final NamespacedKey namespaceKey;

	public CustomItem(String namespace, String identifier) {
		this.identifier = identifier;
		this.namespace = namespace;
		identifierKey = createUtilKey(IDENTIFIER_TAG);
		namespaceKey = createUtilKey(NAMESPACE_TAG);
	}

	protected void setMeta(short meta) {
		this.meta = meta;
	}

	public short getMeta() {
		return meta;
	}

	public ItemStack createItem() {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setCustomModelData((int) this.meta);
		meta.setDisplayName(name);
		meta.setUnbreakable(true);
		if (hideFlags) {
			meta.addItemFlags(ItemFlag.values());
		}
		// Modifier
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(new UUID(13436, 894654),
				"generic.attackDamage", damage, Operation.ADD_NUMBER, EquipmentSlot.HAND));
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(new UUID(13436, 894655),
				"generic.attackSpeed", speed, Operation.ADD_NUMBER, EquipmentSlot.HAND));
		// Set meta
		setItemDataString(meta, identifierKey, getIdentifier());
		setItemDataString(meta, namespaceKey, getNamespace());
		
		item.setItemMeta(meta);
		// Identifier and namespace
//		ItemUtil.setNBTString(item, NAMESPACE_TAG, getNamespace());
//		ItemUtil.setNBTString(item, IDENTIFIER_TAG, getIdentifier());
		// Modifiers
//		net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
//		NBTTagCompound root = nmsItem.getTag() != null ? nmsItem.getTag() : new NBTTagCompound();
//		NBTTagList modifiers = new NBTTagList();
//		//Damage
//		NBTTagCompound damage = new NBTTagCompound();
//		damage.setString("AttributeName", "generic.attackDamage");
//		damage.setString("Name", "generic.attackDamage");
//		damage.setInt("Amount", this.damage);
//		damage.setString("Slot", "mainhand");
//		damage.setInt("Operation", 0);
//		damage.setInt("UUIDLeast", 894654);
//		damage.setInt("UUIDMost", 13436);
//		modifiers.add(damage);
//		//Speed
//		NBTTagCompound speed = new NBTTagCompound();
//		speed.setString("AttributeName", "generic.attackSpeed");
//		speed.setString("Name", "generic.attackSpeed");
//		speed.setDouble("Amount", this.speed);
//		speed.setString("Slot", "mainhand");
//		speed.setInt("Operation", 0);
//		speed.setInt("UUIDLeast", 894654);
//		speed.setInt("UUIDMost", 13436);
//		modifiers.add(speed);
//		root.set("AttributeModifiers", modifiers);
//		nmsItem.setTag(root);
//		item.setItemMeta(CraftItemStack.getItemMeta(nmsItem));
		return item;
	}

	public boolean isItem(ItemStack item) {
		boolean isItem = item != null && item.hasItemMeta();
		if (isItem) {
			// Identfier compatiblity check
			if (ItemUtil.getNBTString(item, "identitfier").equals(getIdentifier())) {
				ItemUtil.setNBTString(item, IDENTIFIER_TAG, getIdentifier());
				ItemUtil.removeNBTTag(item, "identitfier");
			}
			// Namespace compatiblity check
			if (ItemUtil.getNBTString(item, IDENTIFIER_TAG).equals(getIdentifier())
					&& NamespacedEntry.validateNamespace(ItemUtil.getNBTString(item, NAMESPACE_TAG))) {
				ItemUtil.setNBTString(item, NAMESPACE_TAG, getNamespace());
			}
			if (ItemUtil.getNBTString(item, IDENTIFIER_TAG).equals(getIdentifier()) && ItemUtil.getNBTString(item, NAMESPACE_TAG).equals(getNamespace())) {
				fixItem(item);
			}
			ItemMeta meta = item.getItemMeta();
			PersistentDataContainer con = meta.getPersistentDataContainer();
			isItem = con.has(identifierKey, PersistentDataType.STRING)
					&& getIdentifier().equals(con.get(identifierKey, PersistentDataType.STRING))
					&& con.has(namespaceKey, PersistentDataType.STRING)
					&& getNamespace().equals(con.get(namespaceKey, PersistentDataType.STRING));
			if (isItem) {
				if (!meta.hasCustomModelData()) {
					if (item.getType() != getMaterial()) {
						item.setType(getMaterial());
					}
					meta.setCustomModelData((int) getMeta());
					if (meta instanceof Damageable) {
						((Damageable) meta).setDamage(0);
					}
					item.setItemMeta(meta);
				}
			}
		}
		return isItem;
	}

	protected void fixItem(ItemStack item) {
		ItemUtil.removeNBTTag(item, IDENTIFIER_TAG);
		setItemDataString(item, identifierKey, getIdentifier());
		
		ItemUtil.removeNBTTag(item, NAMESPACE_TAG);
		setItemDataString(item, namespaceKey, getNamespace());
	}

	protected NamespacedKey createUtilKey(String key) {
		return new NamespacedKey(JavaPlugin.getPlugin(JojokobiUtilPlugin.class), key);
	}

	protected void registerRecipe() {
		Bukkit.addRecipe(getRecipe());
	}

	public abstract Recipe getRecipe();

	public boolean onUse(ItemStack item, PlayerInteractEvent event) {
		return onUse(item, event.getPlayer());
	}

	public void onHit(ItemStack item, EntityDamageByEntityEvent event) {
		onHit(item, event.getDamager(), event.getEntity());
	}

	public abstract boolean onUse(ItemStack item, Player player);

	public abstract void onHit(ItemStack item, Entity damager, Entity defender);

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		ItemStack cursor = event.getCursor();
		ItemStack current = event.getCurrentItem();
//		Map<String, Object> serialized = cursor.serialize();
//		for (String key : serialized.keySet()) {
//			System.out.println(key + ":" + serialized.get(key).getClass().getName());
//		}
		if (maxStackSize > 0 && isItem(current) && isItem(cursor) && current.isSimilar(cursor)) {
			if (event.isLeftClick()) {
				while (current.getAmount() < maxStackSize && cursor.getAmount() > 0) {
					cursor.setAmount(cursor.getAmount() - 1);
					current.setAmount(current.getAmount() + 1);
				}
				event.setCancelled(true);
			} else if (event.isRightClick()) {
				if (current.getAmount() < maxStackSize && cursor.getAmount() > 0) {
					cursor.setAmount(cursor.getAmount() - 1);
					current.setAmount(current.getAmount() + 1);
				}
				event.setCancelled(true);
			}
			if (event.getWhoClicked() instanceof Player) {
				((Player) event.getWhoClicked()).updateInventory();
			}
		} else if (isHelmet() && isItem(cursor) && (current == null || current.getType() == Material.AIR)
				&& event.getSlotType() == SlotType.ARMOR && event.getSlot() == 39) {
			event.setCurrentItem(new ItemStack(cursor));
			cursor.setAmount(0);
			event.setCancelled(true);
		}
	}
	
	protected <T,Z> void setItemData (ItemMeta meta, NamespacedKey key, PersistentDataType<T, Z> type, Z data) {
		meta.getPersistentDataContainer().set(key, type, data);
	}
	
	protected <T,Z> void setItemData (ItemStack item, NamespacedKey key, PersistentDataType<T, Z> type, Z data) {
		ItemMeta meta = item.getItemMeta();
		setItemData(meta, key, type, data);
		item.setItemMeta(meta);
	}
	
	protected void setItemDataString (ItemMeta meta, NamespacedKey key, String data) {
		meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, data);
	}
	
	protected void setItemDataInt (ItemMeta meta, NamespacedKey key, int data) {
		meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, data);
	}
	
	protected void setItemDataString (ItemStack item, NamespacedKey key, String data) {
		ItemMeta meta = item.getItemMeta();
		setItemDataString(meta, key, data);
		item.setItemMeta(meta);
	}
	
	protected void setItemDataInt (ItemStack item, NamespacedKey key, int data) {
		ItemMeta meta = item.getItemMeta();
		setItemDataInt(meta, key, data);
		item.setItemMeta(meta);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			ItemStack held = event.getItem();
			if (held != null && isItem(held) && onUse(held, event)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof LivingEntity) {
			ItemStack item = ((LivingEntity) event.getDamager()).getEquipment().getItemInMainHand();
			if (isItem(item)) {
				onHit(item, event);
			}
		}
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	public Material getMaterial() {
		return material;
	}

	public String getName() {
		return name;
	}

	public boolean isHideFlags() {
		return hideFlags;
	}

	public int getMaxStackSize() {
		return maxStackSize;
	}

	protected void setMaterial(Material material) {
		this.material = material;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setHideFlags(boolean hideFlags) {
		this.hideFlags = hideFlags;
	}

	protected void setMaxStackSize(int maxStackSize) {
		this.maxStackSize = maxStackSize;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public boolean isHelmet() {
		return helmet;
	}

	public void setHelmet(boolean helmet) {
		this.helmet = helmet;
	}

}
