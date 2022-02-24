package at.jojokobi.mcutil.item;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public abstract class PlaceableItem extends CustomItem {
	

	private Rotation rotation = Rotation.EXACT;
	
	public PlaceableItem(String namespace, String identifier) {
		super(namespace, identifier);
	}
	
	@EventHandler
	public void onPlayerInteract (PlayerInteractEvent event) {
		if (isItem(event.getPlayer().getInventory().getItemInMainHand())) {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Player player = event.getPlayer();
				ItemStack item = player.getInventory().getItemInMainHand();
				if (player.getGameMode() != GameMode.CREATIVE) {
					item.setAmount(item.getAmount() - 1);
				}
				Location place = event.getClickedBlock().getLocation();
				place.add(0, 1, 0);
				onPlaceItem(player, getItemEntity(place, player.getLocation().getDirection().multiply(-1)));
				event.setCancelled(true);
			}
		}
	}
	
	protected void onPlaceItem (Player player, ArmorStand entity) {
		
	}
	
	@EventHandler
	public void onArmorStandManipulate (PlayerArmorStandManipulateEvent event) {
		ItemStack item = event.getArmorStandItem();
		if (item != null && isItem(item)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityDamage (EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (isItemEntity(entity) && canRemove(event)) {
			Location place = entity.getLocation();
			spawnDrops(place);
			entity.remove();
		}
	}
	
	protected boolean canRemove (EntityDamageEvent event) {
		return true;
	}
	
	public ArmorStand getItemEntity (Location place, Vector facing) {
		switch (rotation) {
		case CARDINAL:
			place.setDirection(new Vector(Math.round(facing.getX()), Math.round(facing.getY()), Math.round(facing.getZ())));
			break;
		case EXACT:
			place.setDirection(facing);
			break;
		default:
			break;
		}
		place.add(0.5, 0, 0.5);
		ArmorStand stand = (ArmorStand) place.getWorld().spawnEntity(place, EntityType.ARMOR_STAND);
		stand.setVisible(false);
		stand.getEquipment().setHelmet(createItem());
		return stand;
	}
	
	public void spawnDrops (Location place) {
		place.getWorld().dropItemNaturally(place, createItem());
	}
	
	public boolean isItemEntity (Entity entity) {
		boolean isEntity = false;
		if (entity instanceof ArmorStand) {
			ArmorStand stand = (ArmorStand) entity;
			ItemStack stack = stand.getEquipment().getHelmet();
			if (ItemUtil.getNBTString(stack, IDENTIFIER_TAG).equals(getIdentifier()) && ItemUtil.getNBTString(stack, NAMESPACE_TAG).equals(getNamespace())) {
				fixItem(stack);
				stand.getEquipment().setHelmet(stack);
			}
			if (stack != null && isItem(stack)) {
				isEntity = true;
			}
		}
		return isEntity;
	}

	public Rotation getRotation() {
		return rotation;
	}

	protected void setRotation(Rotation rotation) {
		this.rotation = rotation;
	}
	
}
