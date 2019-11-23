package at.jojokobi.mcutil.commands;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.sun.management.internal.HotSpotDiagnostic;

public class DumpHeapCommand implements CommandExecutor{

	public static final String COMMAND_NAME = "dumpheap";

	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String text, String[] args) {
		if (text.equalsIgnoreCase(COMMAND_NAME)) {
			if (sender.isOp()) {
				MBeanServer server = ManagementFactory.getPlatformMBeanServer();
				HotSpotDiagnostic mxBean;
				try {
					mxBean = ManagementFactory.newPlatformMXBeanProxy(server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnostic.class);
					mxBean.dumpHeap("heap_" + System.currentTimeMillis() + ".txt", true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				sender.sendMessage("You need operator permissions to perform this command!");
			}
		}
		return false;
	}

}
