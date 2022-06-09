package at.jojokobi.mcutil;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.java.JavaPlugin;

import at.jojokobi.mcutil.building.Building;
import at.jojokobi.mcutil.building.BuildingBlock;
import at.jojokobi.mcutil.building.BuildingMark;
import at.jojokobi.mcutil.commands.BuildingCommand;
import at.jojokobi.mcutil.commands.GenerateCommand;
import at.jojokobi.mcutil.commands.RemoveStructureCommand;
import at.jojokobi.mcutil.commands.SpawnCustomCommand;
import at.jojokobi.mcutil.dimensions.DimensionHandler;
import at.jojokobi.mcutil.entity.ComponentData;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.EntityMapData;
import at.jojokobi.mcutil.entity.RemovalHandler;
import at.jojokobi.mcutil.entity.spawns.CustomEntitySpawner;
import at.jojokobi.mcutil.entity.spawns.CustomEntitySpawnerHandler;
import at.jojokobi.mcutil.entity.spawns.CustomSpawnsHandler;
import at.jojokobi.mcutil.generation.GenerationHandler;
import at.jojokobi.mcutil.generation.GeneratorWorldConfig;
import at.jojokobi.mcutil.gui.InventoryGUIHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;
import at.jojokobi.mcutil.music.MusicHandler;

public class JojokobiUtilPlugin extends JavaPlugin{
	
	public static final String PLUGIN_NAME = "JojokobiUtil";

	private DimensionHandler dimensionHandler;
	private RemovalHandler removalHandler;
	private GenerationHandler generationHandler;
	private InventoryGUIHandler guiHandler;
	private MusicHandler musicHandler;
	private EntityHandler entityHandler;
	private CustomEntitySpawnerHandler spawnerHandler;
	private CustomEntitySpawner entitySpawner;
	
	@Override
	public void onLoad() {
		super.onLoad();
		registerSerializables();
	}
	
	public static List<Class<? extends ConfigurationSerializable>> getConfigurationSerializables () {
		return Arrays.asList(EntityMapData.class, NamespacedEntry.class, SerializableMap.class, ComponentData.class, LootInventory.class, LootItem.class, BuildingBlock.class, BuildingMark.class, Building.class);
	}
	
	public static void registerSerializables () {
		SerializationUtil.registerSerializables("JojokobiUtil", getConfigurationSerializables());
	}
	
	@SuppressWarnings("deprecation")
	//TODO
	@Override
	public void onEnable() {
		super.onEnable();
		//Handlers
		musicHandler = new MusicHandler(this);
		removalHandler = new RemovalHandler();
		Bukkit.getPluginManager().registerEvents(removalHandler, this);
		guiHandler = new InventoryGUIHandler(this);
		Bukkit.getPluginManager().registerEvents(guiHandler, this);
		entityHandler = new EntityHandler(this, guiHandler, "jojokobiutil" + File.separator + "entities");;
		Bukkit.getPluginManager().registerEvents(entityHandler, this);
		
		generationHandler = new GenerationHandler(this, "mcutil" + File.separator + "structures");
		Bukkit.getPluginManager().registerEvents(generationHandler, this);
		
		dimensionHandler = new DimensionHandler(this);
		Bukkit.getPluginManager().registerEvents(dimensionHandler, this);
		
		spawnerHandler = new CustomEntitySpawnerHandler(entityHandler, this);
		Bukkit.getPluginManager().registerEvents(spawnerHandler, this);
		
		entitySpawner = new CustomEntitySpawner(this, entityHandler);		
		
		//Commands
		GenerateCommand generateCmd = new GenerateCommand(generationHandler);
		getCommand(GenerateCommand.COMMAND_NAME).setExecutor(generateCmd);
		getCommand(RemoveStructureCommand.COMMAND_NAME).setExecutor(new RemoveStructureCommand(generationHandler));
		SpawnCustomCommand spawnCmd = new SpawnCustomCommand(entityHandler, CustomSpawnsHandler.getInstance());
		getCommand(SpawnCustomCommand.COMMAND_NAME).setExecutor(spawnCmd);
		getCommand(SpawnCustomCommand.COMMAND_NAME).setTabCompleter(spawnCmd);
		BuildingCommand buildingCmd = new BuildingCommand(new File(getDataFolder(), "buildings"));
		getCommand(BuildingCommand.COMMAND_NAME).setExecutor(buildingCmd);
		getCommand(BuildingCommand.COMMAND_NAME).setTabCompleter(buildingCmd);
		
		//Generator config
		FileConfiguration config = getConfig();
		saveDefaultConfig();
		ConfigurationSection section = config.getConfigurationSection("generator.worlds");
		if (section != null) {
			for (String key : section.getKeys(false)) {
				GeneratorWorldConfig cfg = new GeneratorWorldConfig();
				//Config
				cfg.setGenerateStructures(section.getBoolean(key + ".generateStructures", true));
				for (String dont : section.getStringList(key + ".dontGenerate")) {
					cfg.getDontGenerate().add(dont);
				}
				//Add
				generationHandler.setWorldConfig(key, cfg);
			}
		}		
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		generationHandler.onDisable();
	}

	public DimensionHandler getDimensionHandler() {
		return dimensionHandler;
	}

	public RemovalHandler getRemovalHandler() {
		return removalHandler;
	}

	public GenerationHandler getGenerationHandler() {
		return generationHandler;
	}

	public InventoryGUIHandler getGuiHandler() {
		return guiHandler;
	}

	public MusicHandler getMusicHandler() {
		return musicHandler;
	}

	public EntityHandler getEntityHandler() {
		return entityHandler;
	}

	public CustomEntitySpawnerHandler getSpawnerHandler() {
		return spawnerHandler;
	}

	public CustomEntitySpawner getEntitySpawner() {
		return entitySpawner;
	}
	
}
