package at.jojokobi.mcutil;

import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class CommandUtil {
	
	public static int parseCoordinate(CommandSender sender, String string, Function<Location, Integer> f) throws Exception {
		string = string.trim();
		int coord = 0;
		try {
			//Relative
			if (string.startsWith("~")) {
				Location pos = null;
				if (sender instanceof Entity) {
					pos = ((Entity) sender).getLocation();
				}
				else if (sender instanceof BlockCommandSender) {
					pos = ((BlockCommandSender) sender).getBlock().getLocation();
				}
				else {
					throw new Exception("To execute this command from the console, you must supply absolute coordinates!");
				}
				
				//Offset
				int offset = 0;
				if (string.length() > 1) {
					offset = Integer.parseInt(string.substring(1));
				}
				coord = f.apply(pos) + offset;
			}
			//Absolute
			else {
				coord = Integer.parseInt(string);
			}
		}
		catch (NumberFormatException e) {
			throw new Exception("Please enter valid numbers, optionally preceeded by ~");
		}
		return coord;
	}

	public static int parseXCoordinate(CommandSender sender, String string) throws Exception {
		return parseCoordinate(sender, string, l -> l.getBlockX());
	}
	
	public static int parseYCoordinate(CommandSender sender, String string) throws Exception {
		return parseCoordinate(sender, string, l -> l.getBlockY());
	}
	
	public static int parseZCoordinate(CommandSender sender, String string) throws Exception {
		return parseCoordinate(sender, string, l -> l.getBlockZ());
	}
	
	public static World getWorld(CommandSender sender) {
		World world = null;
		if (sender instanceof Entity) {
			world = ((Entity) sender).getWorld();
		}
		else if (sender instanceof BlockCommandSender) {
			world = ((BlockCommandSender) sender).getBlock().getWorld();
		}
		else {
			world = Bukkit.getWorlds().get(0);
		}
		return world;
	}
	
}
