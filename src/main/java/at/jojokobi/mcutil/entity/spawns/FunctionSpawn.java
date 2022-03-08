package at.jojokobi.mcutil.entity.spawns;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.bukkit.Location;

import at.jojokobi.mcutil.NamespacedEntry;
import at.jojokobi.mcutil.entity.CustomEntity;

public class FunctionSpawn implements CustomSpawn{
	
	private String namespace;
	private String identifier;
	private Function<Location, CustomEntity<?>> function;

	public FunctionSpawn(NamespacedEntry entry, Function<Location, CustomEntity<?>> function) {
		this(entry.getNamespace(), entry.getIdentifier(), function);
	}
	
	public FunctionSpawn(String namespace, String identifier, Function<Location, CustomEntity<?>> function) {
		super();
		this.namespace = namespace;
		this.identifier = identifier;
		this.function = function;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public List<CustomEntity<?>> spawn(Location loc) {
		return Arrays.asList(function.apply(loc));
	}

}
