package at.jojokobi.mcutil.commands;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import at.jojokobi.mcutil.generation.GenerationHandler;
import at.jojokobi.mcutil.generation.population.Structure;

public class GenerateCommand implements TabExecutor{
	
	public static final String COMMAND_NAME = "generate";
	
	private GenerationHandler genHandler;
	

	public GenerateCommand(GenerationHandler genHandler) {
		super();
		this.genHandler = genHandler;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String text, String[] args) {
		if (text.equalsIgnoreCase(COMMAND_NAME)) {
			if (sender.isOp() && sender instanceof Player) {
				if (args.length >= 1) {
					String identifier = args[0];
					Structure struc = genHandler.getStructure(identifier);
					if (struc != null) {
						struc.generate(((Player) sender).getLocation(),((Player) sender).getWorld().getSeed()).forEach(genHandler::addStructureInstance);
						return true;
					}
					else {
						sender.sendMessage("There is no structure named " + identifier + "!");
					}
				}
				else {
					for (Structure struc : genHandler.getStructures()) {
						sender.sendMessage(struc.getIdentifier());
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

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		String lastArg = args[args.length - 1];
		return genHandler.getStructures().stream().map(spawn -> spawn.getIdentifier()).filter(s -> s.contains(lastArg)).collect(Collectors.toList());
	}

}
