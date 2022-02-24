package at.jojokobi.mcutil.entity.ai;

import java.util.List;
import java.util.Map;

import at.jojokobi.mcutil.Handler;
import at.jojokobi.mcutil.NamespacedEntry;
import at.jojokobi.mcutil.entity.CustomEntity;

/**
 * 
 * A static handler for <code>EntityAI</code>s. All AIs need to before
 * registered before any custom emntity is spawned or loaded from the save file.
 * 
 * @author jojokobi
 *
 */

@Deprecated
public final class AIHandler extends Handler<EntityAI> {

	private static AIHandler instance = new AIHandler();

	private AIHandler() {

	}

	public static Map<NamespacedEntry, EntityAI> getAIMap() {
		return instance.getItems();
	}

	public static EntityAI getAIController(String namespace, String identifier) {
		return instance.getItem(namespace, identifier);
	}

	public static void addAIController(EntityAI ai) {
		instance.addItem(ai);
	}

	public static List<EntityAI> getAIList() {
		return instance.getItemList();
	}

	@Override
	protected EntityAI getStandardInstance(String namespace, String identifier) {
		return new EntityAI() {
			@Override
			public String getNamespace() {
				return namespace;
			}

			@Override
			public String getIdentifier() {
				return identifier;
			}

			@Override
			public void changeAI(CustomEntity<?> entity) {

			}
		};
	}

}
