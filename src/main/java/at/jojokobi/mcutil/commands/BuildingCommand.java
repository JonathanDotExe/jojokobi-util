package at.jojokobi.mcutil.commands;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import at.jojokobi.mcutil.CommandUtil;
import at.jojokobi.mcutil.generation.GenerationHandler;
import at.jojokobi.mcutil.generation.population.Structure;

public class BuildingCommand implements TabExecutor{
	
	public static final String COMMAND_NAME = "building";
	
	private File folder;
	
	public BuildingCommand(File folder) {
		super();
		this.folder = folder;
	}

	@Override
	///building <create|overwrite|place> <name> <x> <y> <z> [width] [height]
	public boolean onCommand(CommandSender sender, Command command, String text, String[] args) {
		boolean success = true;
		if (text.equalsIgnoreCase(COMMAND_NAME)) {
			if (sender.isOp()) {
				//Name and action
				if (args.length >= 2) {
					String action = args[0];
					String name = args[1];
					//Coordinates
					int x = 0;
					int y = 0;
					int z = 0;
					try {
						x = CommandUtil.parseXCoordinate(sender, args.length >= 3 ? args[2] : "~");
						y = CommandUtil.parseYCoordinate(sender, args.length >= 4 ? args[3] : "~");
						z = CommandUtil.parseZCoordinate(sender, args.length >= 5 ? args[4] : "~");
					} catch (Exception e) {
						sender.sendMessage(e.getMessage());
						success = false;
					}
					//Execute
					if (success) {
						switch (action.toLowerCase()) {
						case 
						}
					}
				}
			}
		}
		else {
			success = false;
		}
		return success;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		String lastArg = args[args.length - 1];
		return genHandler.getStructures().stream().map(spawn -> spawn.getIdentifier()).filter(s -> s.contains(lastArg)).collect(Collectors.toList());
	}

}
