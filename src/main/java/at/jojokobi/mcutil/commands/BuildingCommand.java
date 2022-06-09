package at.jojokobi.mcutil.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import at.jojokobi.mcutil.CommandUtil;
import at.jojokobi.mcutil.building.Building;
import at.jojokobi.mcutil.generation.GenerationHandler;
import at.jojokobi.mcutil.generation.population.Structure;

public class BuildingCommand implements TabExecutor{
	
	public static final String COMMAND_NAME = "building";
	
	private File folder;
	
	public BuildingCommand(File folder) {
		super();
		this.folder = folder;
		folder.mkdirs();
	}
	
	private Building loadBuilding(String name) {
		Building building = null;
		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(new File(folder, name + ".yml"));
			building = config.getSerializable("building", Building.class);
		} catch (IOException | InvalidConfigurationException e) {
			
		}
		return building;
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
					Location loc = new Location(CommandUtil.getWorld(sender), x, y, z);
					//Execute
					if (success) {
						switch (action.toLowerCase()) {
						case "create":
						{
							//Width and height
							int width = 0;
							int height = 0;
							if (args.length >= 7) {
								try {
									width = Integer.parseInt(args[5]);
									height = Integer.parseInt(args[6]);
								}
								catch (NumberFormatException e) {
									sender.sendMessage("You need to specify a width and height for this action!");
									break;
								}
							}
							//Check if it exists
							if (loadBuilding(name) != null) {
								sender.sendMessage("You need to specify a width and height for this action!");
							}
							else {
								
							}
						}
							break;
						case "overwrite":
							break;
						case "place":
						{
							Building building = loadBuilding(name);
							if (building == null) {
								sender.sendMessage("The building " + name + " does not exist!");
							}
							else {
								building.buildWithMarkSigns(loc, true);
							}
						}
							break;
						default:
							success = false;
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
