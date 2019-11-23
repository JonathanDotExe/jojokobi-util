package at.jojokobi.mcutil.commands;

import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.spawns.CustomSpawn;
import at.jojokobi.mcutil.entity.spawns.CustomSpawnsHandler;

public class SpawnCustomCommand implements CommandExecutor{

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
			if (sender.isOp() && sender instanceof Player) {
				if (args.length >= 1) {
					String[] split = args[0].split(Pattern.quote(":"));
					CustomSpawn spawn = null;
					if (split.length >= 2 && (spawn = spawnHandler.getItem(split[0], split[1])) != null) {
						Location loc = ((Player) sender).getLocation();
						for (CustomEntity<?> entity : spawn.spawn(loc)) {
							handler.addEntity(entity);
						}
						return true;
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
			else {
				sender.sendMessage("This command can only be executed by a player with operator permissions!");
			}
		}
		return false;
	}

}
