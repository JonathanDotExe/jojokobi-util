package at.jojokobi.mcutil;

import java.util.List;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

public class SerializationUtil {

	private SerializationUtil() {
		
	}
	
	public static void registerSerializables (String prefix, List<Class<? extends ConfigurationSerializable>> classes) {
		for (Class<? extends ConfigurationSerializable> clazz : classes) {
			ConfigurationSerialization.registerClass(clazz, prefix + clazz.getSimpleName());
			ConfigurationSerialization.registerClass(clazz);
		}
	}

}
