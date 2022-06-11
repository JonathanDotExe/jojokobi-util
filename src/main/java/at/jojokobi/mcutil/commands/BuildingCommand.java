package at.jojokobi.mcutil.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import at.jojokobi.mcutil.CommandUtil;
import at.jojokobi.mcutil.building.Building;

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
	
	private boolean saveBuilding(String name, Building building) {
		FileConfiguration config = new YamlConfiguration();
		try {
			config.set("building", building);
			config.save(new File(folder, name + ".yml"));
		} catch (IOException e) {
			return false;
		}
		return true;
	}


	@Override
	///building <create|overwrite|place> [name] [x] [y] [z] [width] [height] [length]
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
							int length = 0;
							if (args.length >= 8) {
								try {
									width = Integer.parseInt(args[5]);
									height = Integer.parseInt(args[6]);
									length = Integer.parseInt(args[7]);
								}
								catch (NumberFormatException e) {
									sender.sendMessage("You need to specify a width, height and length for this action!");
									break;
								}
							}
							else {
								sender.sendMessage("You need to specify a width, height and length for this action!");
								break;
							}
							//Check if it exists
							if (loadBuilding(name) != null) {
								sender.sendMessage("The building " + name + " already exists!");
							}
							else {
								Building build = Building.createBuilding(loc, width, height, length);
								if (saveBuilding(name, build)) {
									sender.sendMessage("The building " + name + " was sucessfully created!");
								}
								else {
									sender.sendMessage("Failed to save building!");
								}
							}
						}
							break;
						case "overwrite":
						{
							//Width and height
							int width = 0;
							int height = 0;
							int length = 0;
							if (args.length >= 8) {
								try {
									width = Integer.parseInt(args[5]);
									height = Integer.parseInt(args[6]);
									length = Integer.parseInt(args[7]);
								}
								catch (NumberFormatException e) {
									sender.sendMessage("You need to specify a width, height and length for this action!");
									break;
								}
							}
							else {
								sender.sendMessage("You need to specify a width, height and length for this action!");
								break;
							}
							//Check if it exists
							Building build = Building.createBuilding(loc, width, height, length);
							if (saveBuilding(name, build)) {
								sender.sendMessage("The building " + name + " was sucessfully saved!");
							}
							else {
								sender.sendMessage("Failed to save building!");
							}
						}
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
			//List
			for (File file : folder.listFiles()) {
				if (file.getName().endsWith(".yml")) {
					sender.sendMessage("Buildings: " + getBuildings() + "");
				}
			}
		}
		return success;
	}

	private List<String> getBuildings() {
		List<String> buildings = new ArrayList<String>();
		for (File file : folder.listFiles()) {
			if (file.getName().endsWith(".yml")) {
				buildings.add(FilenameUtils.removeExtension(file.getName()));
			}
		}
		return buildings;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		switch (args.length) {
		case 1:
			return Arrays.asList("place", "create", "overwrite", "list");
		case 2:
			return getBuildings();
		default:
			return new ArrayList<>();
		}
	}

}
