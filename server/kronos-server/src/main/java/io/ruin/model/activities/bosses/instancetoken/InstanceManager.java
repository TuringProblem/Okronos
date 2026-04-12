package io.ruin.model.activities.bosses.instancetoken;

import io.ruin.model.entity.player.Player;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InstanceManager {
	@Getter
	public static final Map<String, InstanceHandler> instances = new ConcurrentHashMap<>();

	public static void init(Player player) {
		// Initialize the instance for the player with the player's name
		addInstance(player.getName().toLowerCase(), new InstanceHandler(player.getName()));
	}

	public static void addInstance(String key, InstanceHandler instance) {
		instances.put(key, instance);
	}

	public static InstanceHandler getInstance(String key) {
		return instances.get(key);
	}

	public static void removeInstance(String key) {
		instances.remove(key);
	}

	public static boolean hasInstance(String key) {
		return instances.containsKey(key);
	}
}