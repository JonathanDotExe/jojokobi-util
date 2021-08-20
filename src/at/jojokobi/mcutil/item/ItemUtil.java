package at.jojokobi.mcutil.item;

import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;


public final class ItemUtil {

	private ItemUtil() {
		
	}
	
	public static void printTagCompount (ItemStack item) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound root = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
		System.out.println(root);
	}
	
	public static ItemStack rename (ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	public static void setNBTTag (ItemStack item, String key, NBTBase value) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound root = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
		root.set (key, value);
		item.setItemMeta(CraftItemStack.getItemMeta(nmsItem));
	}
	
	public static void removeNBTTag (ItemStack item, String key) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound root = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
		root.remove (key);
		item.setItemMeta(CraftItemStack.getItemMeta(nmsItem));
	}
	
	public static NBTBase getNBTTag (ItemStack item, String key) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTBase base = null;
		NBTTagCompound root = nmsItem.getTag();
		if (root != null) {
			base = root.get(key);
		}
		return base;
	}
	
	public static void setNBTString (ItemStack item, String key, String value) {
		setNBTTag(item, key, NBTTagString.a(value));
	}
	
	public static String getNBTString (ItemStack item, String key) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		String value = "";
		NBTTagCompound root = nmsItem.getTag();
		if (root != null) {
			value = root.getString(key);
		}
		return value;
	}
	
	public static void setNBTInt (ItemStack item, String key, int value) {
		setNBTTag(item, key, NBTTagInt.a(value));
	}
	
	public static int getNBTInt (ItemStack item, String key) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		int value = 0;
		NBTTagCompound root = nmsItem.getTag();
		if (root != null) {
			value = root.getInt(key);
		}
		return value;
	}
	
	public static void setNBTByte (ItemStack item, String key, byte value) {
		setNBTTag(item, key, NBTTagByte.a(value));
	}
	
	public static byte getNBTByte (ItemStack item, String key) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		byte value = 0;
		NBTTagCompound root = nmsItem.getTag();
		if (root != null) {
			value = root.getByte(key);
		}
		return value;
	}
	
	public static void setNBTBoolean (ItemStack item, String key, boolean value) {
		setNBTTag(item, key, NBTTagByte.a(value ? (byte) 1 : (byte) 0));
	}
	
	public static boolean getNBTBoolean (ItemStack item, String key) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		boolean value = false;
		NBTTagCompound root = nmsItem.getTag();
		if (root != null) {
			value = root.getBoolean(key);
		}
		return value;
	}

}
