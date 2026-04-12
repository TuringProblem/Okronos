package io.ruin.model.map;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.dynamic.DynamicMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class MapListener {

	public interface EnteredAction {
		void entered(Player player);
	}

	public interface ExitAction {
		void exited(Player player, boolean logout);
	}

	public static List<MapListener> LISTENERS = new ArrayList<>();

	public static MapListener register(Predicate<Player> predicate) {
		return register(new MapListener(predicate));
	}

	public static MapListener registerPosition(Position position) {
		return register(new MapListener(p -> p.getPosition().equals(position)));
	}

	public static MapListener registerBounds(Bounds bounds) {
		return register(new MapListener(p -> p.getPosition().inBounds(bounds)));
	}

	public static MapListener registerBounds(Bounds... bounds) {
		return register(new MapListener(p -> {
			for (Bounds b : bounds) {
				if (p.getPosition().inBounds(b))
					return true;
			}
			return false;
		}));
	}

	public static MapListener registerRegion(int regionId) {
		return register(new MapListener(p -> p.lastRegion != null && p.lastRegion.id == regionId));
	}

	public static MapListener registerRegions(int... regionIds) {
		return register(new MapListener(p -> {
			if (p.lastRegion != null) {
				for (int id : regionIds) {
					if (p.lastRegion.id == id)
						return true;
				}
			}
			return false;
		}));
	}

	public static MapListener registerRegions(List<Integer> regionIds) {
		return register(new MapListener(p -> p.lastRegion != null && regionIds.contains(p.lastRegion.id)));
	}

	public static MapListener registerMap(DynamicMap map) {
		return register(map.toListener());
	}

	public static void process(Player player) {
		for (var listener : MapListener.LISTENERS) {
			var wasActive = player.activeStaticMapListeners.contains(listener.index);
			var isActive = listener.isActive(player);

			if (wasActive && isActive) {
				continue;
			}

			if (!wasActive && !isActive) {
				continue;
			}

			if (wasActive && !isActive) {
				listener.notifyExited(player);
				player.activeStaticMapListeners.remove(listener.index);
			} else if (!wasActive && isActive) {
				listener.notifyEntered(player);
				player.activeStaticMapListeners.add(listener.index);
			}
		}

		player.activeMapListeners.removeIf(it -> {
			var active = it.activeCheck.test(player);
			if (!active) {
				it.notifyExited(player);
			}
			return !active;
		});
	}

	public static void onLogout(Player player) {
		for (var listener : MapListener.LISTENERS) {
			var wasActive = player.activeStaticMapListeners.contains(listener.index);

			if (!wasActive) {
				continue;
			}

			listener.notifyExited(player, true);
			player.activeStaticMapListeners.remove(listener.index);
		}

		for (var listener : player.activeMapListeners) {
			listener.notifyExited(player, true);
		}
		player.activeMapListeners.clear();
	}

	private static MapListener register(MapListener listener) {
		listener.index = LISTENERS.size();
		LISTENERS.add(listener);
		return listener;
	}

	private Predicate<Player> activeCheck;
	public EnteredAction enterAction;
	public ExitAction exitAction;
	public int index = -1;

	public MapListener(Predicate<Player> activeCheck) {
		this.activeCheck = activeCheck;
	}

	public MapListener onEnter(EnteredAction enterAction) {
		this.enterAction = enterAction;
		return this;
	}

	public MapListener onExit(ExitAction exitAction) {
		this.exitAction = exitAction;
		return this;
	}

	public boolean isActive(Player player) {
		return activeCheck.test(player);
	}

	public void notifyEntered(Player player) {
		if (this.enterAction == null) {
			return;
		}
		this.enterAction.entered(player);
	}

	public void notifyExited(Player player, boolean logout) {
		if (this.exitAction == null) {
			return;
		}
		this.exitAction.exited(player, logout);
	}

	public void notifyExited(Player player) {
		this.notifyExited(player, false);
	}

}
