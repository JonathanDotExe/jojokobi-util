package at.jojokobi.mcutil.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.jojokobi.mcutil.generation.BasicGenUtil;
import at.jojokobi.mcutil.generation.GenerationHandler;
import at.jojokobi.mcutil.generation.population.StructureInstance;

public class RemoveStructureCommand implements CommandExecutor{
	
	public static final String COMMAND_NAME = "removestruc";
	
	private GenerationHandler genHandler;
	

	public RemoveStructureCommand(GenerationHandler genHandler) {
		super();
		this.genHandler = genHandler;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String text, String[] args) {
		if (text.equalsIgnoreCase(COMMAND_NAME)) {
			if (sender.isOp() && sender instanceof Player) {
				Location loc = ((Player) sender).getLocation();
				for (StructureInstance<?> inst : genHandler.getInstancesAt(loc)) {
					genHandler.removeStructureInstance(inst);
					BasicGenUtil.generateCube(loc, Material.AIR, inst.getStructure().getWidth(), inst.getStructure().getHeight(), inst.getStructure().getLength());
					sender.sendMessage("Removed " + inst.getStructure().getIdentifier() + "!");
				}
				return true;
			}
			else {
				sender.sendMessage("This command can only be executed by player a player with operator permissions!");
			}
		}
		return false;
	}

}
