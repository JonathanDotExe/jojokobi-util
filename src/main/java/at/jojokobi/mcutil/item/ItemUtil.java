package at.jojokobi.mcutil.item;

import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public final class ItemUtil {

	private ItemUtil() {
		
	}
	
	public static void printTagCompount (ItemStack item) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		CompoundTag root = nmsItem.hasTag() ? nmsItem.getTag() : new CompoundTag();
		System.out.println(root);
	}
	
	public static ItemStack rename (ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	public static void setNBTTag (ItemStack item, String key, Tag value) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		CompoundTag root = nmsItem.hasTag() ? nmsItem.getTag() : new CompoundTag();
		root.put (key, value);
		item.setItemMeta(CraftItemStack.getItemMeta(nmsItem));
	}
	
	public static void removeNBTTag (ItemStack item, String key) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		CompoundTag root = nmsItem.hasTag() ? nmsItem.getTag() : new CompoundTag();
		root.remove (key);
		item.setItemMeta(CraftItemStack.getItemMeta(nmsItem));
	}
	
	public static Tag getNBTTag (ItemStack item, String key) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		Tag base = null;
		CompoundTag root = nmsItem.getTag();
		if (root != null) {
			base = root.get(key);
		}
		return base;
	}
	
	public static void setNBTString (ItemStack item, String key, String value) {
		setNBTTag(item, key, StringTag.valueOf(value));
	}
	
	public static String getNBTString (ItemStack item, String key) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		String value = "";
		CompoundTag root = nmsItem.getTag();
		if (root != null) {
			value = root.getString(key);
		}
		return value;
	}
	
	public static void setNBTInt (ItemStack item, String key, int value) {
		setNBTTag(item, key, IntTag.valueOf(value));
	}
	
	public static int getNBTInt (ItemStack item, String key) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		int value = 0;
		CompoundTag root = nmsItem.getTag();
		if (root != null) {
			value = root.getInt(key);
		}
		return value;
	}
	
	public static void setNBTByte (ItemStack item, String key, byte value) {
		setNBTTag(item, key, ByteTag.valueOf(value));
	}
	
	public static byte getNBTByte (ItemStack item, String key) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		byte value = 0;
		CompoundTag root = nmsItem.getTag();
		if (root != null) {
			value = root.getByte(key);
		}
		return value;
	}
	
	public static void setNBTBoolean (ItemStack item, String key, boolean value) {
		setNBTTag(item, key, ByteTag.valueOf(value ? (byte) 1 : (byte) 0));
	}
	
	public static boolean getNBTBoolean (ItemStack item, String key) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		boolean value = false;
		CompoundTag root = nmsItem.getTag();
		if (root != null) {
			value = root.getBoolean(key);
		}
		return value;
	}

}
