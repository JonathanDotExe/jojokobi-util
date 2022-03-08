package at.jojokobi.mcutil.commands;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.spawns.CustomSpawn;
import at.jojokobi.mcutil.entity.spawns.CustomSpawnsHandler;

public class SpawnCustomCommand implements TabExecutor{

	public static final String COMMAND_NAME = "spawncustom";
	
	private EntityHandler handler;
	private CustomSpawnsHandler spawnHandler;
	
	public SpawnCustomCommand(EntityHandler handler, CustomSpawnsHandler spawnHandler) {
		super();
		this.handler = handler;
		this.spawnHandler = spawnHandler;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {
		if (name.equalsIgnoreCase(COMMAND_NAME)) {
			if (args.length >= 1) {
				String[] split = args[0].split(Pattern.quote(":"));
				CustomSpawn spawn = null;
				if (split.length >= 2 && (spawn = spawnHandler.getItem(split[0], split[1])) != null) {
					if(sender.isOp()) {
						Location loc = getLocation(sender);
						if(args.length >= 4) {
							try {
								int x = Integer.parseInt(args[1]);
								int y = Integer.parseInt(args[2]);
								int z = Integer.parseInt(args[3]);
								loc = new Location(loc.getWorld(), x, y, z);
							} catch (NumberFormatException e) {
								return false;
							}
						}
						if(loc == null)
							return false;

						spawnEntity(spawn, loc);
						return true;
					} else {
						sender.sendMessage("This command can only be executed by a sender with operator permissions!");
					}
				}
				else {
					sender.sendMessage("There is no entity with identifier " + args[0] + "!");
				}
			}
			else {
				for (CustomSpawn spawn : spawnHandler.getItemList()) {
					sender.sendMessage(spawn.getNamespace() + ":" + spawn.getIdentifier());
				}
				return true;
			}
		}
		return false;
	}

	private void spawnEntity(CustomSpawn spawn, Location loc) {
		for (CustomEntity<?> entity : spawn.spawn(loc)) {
			handler.addEntity(entity);
		}
	}

	private Location getLocation (CommandSender sender) {
		Location loc = null;
		if(sender instanceof Player) {
			loc = ((Player) sender).getLocation();
		} else if(sender instanceof BlockCommandSender) {
			loc = ((BlockCommandSender) sender).getBlock().getLocation();
		}

		return loc;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		String lastArg = args[args.length - 1];
		return spawnHandler.getItemList().stream().map(spawn -> spawn.stringify()).filter(s -> s.contains(lastArg)).collect(Collectors.toList());
	}

}
