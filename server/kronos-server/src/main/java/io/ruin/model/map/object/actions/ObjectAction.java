package io.ruin.model.map.object.actions;

import io.ruin.cache.LocType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public interface ObjectAction {

	static final Logger log = LoggerFactory.getLogger(ObjectAction.class);

	void handle(Player player, GameObject obj);

	/**
	 * Default - (Registers the given action for every object in the game with the given objectId)
	 */

	static void register(int objectId, int option, ObjectAction action) {
		LocType def = LocType.get(objectId);
		if (def.defaultActions == null)
			def.defaultActions = new ObjectAction[5];
		def.defaultActions[option - 1] = action;
	}

	static boolean register(int objectId, String optionName, ObjectAction action) {
		LocType def = LocType.get(objectId);
		if (def == null)
			return false;
		int option = def.getOption(optionName);
		if (option == -1)
			return false;
		register(objectId, option, action);
		return true;
	}

	static void register(int objectId, Consumer<ObjectAction[]> actionsConsumer) {
		ObjectAction[] actions = new ObjectAction[6];
		actionsConsumer.accept(actions);
		LocType.get(objectId).defaultActions = Arrays.copyOfRange(actions, 1, actions.length);
	}

	/**
	 * Global - (Registers the given action for several objects in the game with the given name)
	 */

	static void register(String objectName, int option, ObjectAction action) {
		List<LocType> types = LocType.getByName(objectName);
		if (types == null) return;
		for (LocType def : types) {
			register(def.id, option, action);
		}
	}

	static void register(String objectName, String optionName, ObjectAction action) {
		List<LocType> types = LocType.getByName(objectName);
		if (types == null) return;
		for (LocType def : types) {
			register(def.id, optionName, action);
		}
	}

	static void register(String objectName, Consumer<ObjectAction[]> actionsConsumer) {
		List<LocType> types = LocType.getByName(objectName);
		if (types == null) return;
		for (LocType def : types) {
			register(def.id, actionsConsumer);
		}
	}

	/**
	 * Specific - (Registers the given action for an object in the game with the given objectId, x, y, and z)
	 */

	static void register(int objectId, int x, int y, int z, int option, ObjectAction action) {
		GameObject object = Tile.getObject(objectId, x, y, z);
		if (object == null) {
			log.error("Error loading object " + objectId + ", at " + "(" + x + ", " + y + ", " + z + ")");
			return;
		}
		register(object, option, action);
	}

	static boolean register(int objectId, int x, int y, int z, String optionName, ObjectAction action) {
		return register(Tile.getObject(objectId, x, y, z), optionName, action);
	}

	static void register(int objectId, int x, int y, int z, Consumer<ObjectAction[]> actionsConsumer) {
		register(Tile.getObject(objectId, x, y, z), actionsConsumer);
	}

	/**
	 * Specific - (Registers the given action for the given object)
	 */

	static void register(GameObject obj, int option, ObjectAction action) {

		if (obj.actions == null)
			obj.actions = new ObjectAction[5];
		obj.actions[option - 1] = action;
	}

	static boolean register(GameObject obj, String optionName, ObjectAction action) {
		if (obj == null)
			return false;
		LocType def = obj.getDef();
		if (def == null)
			return false;
		int option = def.getOption(optionName);
		if (option == -1)
			return false;
		register(obj, option, action);
		return true;
	}

	static void register(GameObject obj, Consumer<ObjectAction[]> actionsConsumer) {
		ObjectAction[] actions = new ObjectAction[5 + 1];
		actionsConsumer.accept(actions);
		obj.actions = Arrays.copyOfRange(actions, 1, actions.length);
	}

}