package io.ruin.model.map.object.actions.impl.dungeons;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.actions.ObjectAction;

import static io.ruin.data.impl.teleports.teleport;

public class LizardmenLair {

	public static void handlePosition30383(Player player) {
		if (player.getPosition().getX() == 1305 && player.getPosition().getY() == 9957) {
			teleport(player, new Position(1305, 9953, 0));
		} else {
			if (player.getPosition().getX() == 1318 && player.getPosition().getY() == 9960) {
				teleport(player, new Position(1318, 9956, 0));
			}
		}
	}

	public static void handlePosition30382(Player player) {
		if (player.getPosition().getX() == 1305 && player.getPosition().getY() == 9953) {
			teleport(player, new Position(1305, 9957, 0));
		} else {
			if (player.getPosition().getX() == 1318 && player.getPosition().getY() == 9956) {
				teleport(player, new Position(1318, 9960, 0));
			}
		}
	}

	public static void handlePosition30384(Player player) {
		if (player.getPosition().getX() == 1295 && player.getPosition().getY() == 9959) {
			teleport(player, new Position(1299, 9959, 0));
		} else {
			if (player.getPosition().getX() == 1319 && player.getPosition().getY() == 9966) {
				teleport(player, new Position(1323, 9966, 0));
			}
		}
	}

	public static void handlePosition30385(Player player) {
		if (player.getPosition().getX() == 1299 && player.getPosition().getY() == 9959) {
			teleport(player, new Position(1295, 9959, 0));
		} else {
			if (player.getPosition().getX() == 1323 && player.getPosition().getY() == 9966) {
				teleport(player, new Position(1319, 9966, 0));
			}
		}
	}

	public static void register() {
		ObjectAction.register(30383, "squeeze-through", (player, obj) -> handlePosition30383(player));
		ObjectAction.register(30383, "squeeze-through", (player, obj) -> handlePosition30383(player));


		ObjectAction.register(30382, "squeeze-through", (player, obj) -> handlePosition30382(player));
		ObjectAction.register(30382, "squeeze-through", (player, obj) -> handlePosition30382(player));

		ObjectAction.register(30384, "squeeze-through", (player, obj) -> handlePosition30384(player));
		ObjectAction.register(30384, "squeeze-through", (player, obj) -> handlePosition30384(player));

		ObjectAction.register(30385, "squeeze-through", (player, obj) -> handlePosition30385(player));
		ObjectAction.register(30385, "squeeze-through", (player, obj) -> handlePosition30385(player));


	}
}
