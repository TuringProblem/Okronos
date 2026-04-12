package io.ruin.content.objects;

import io.ruin.model.map.Position;
import io.ruin.model.map.object.actions.ObjectAction;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-13
 */
public class GiantsDenCave {

	private static final int ENTRANCE = 42248;
	private static final int EXIT = 42247;

	public static void register() {
		ObjectAction.register(ENTRANCE, "enter", (player, entrance) ->
			player.getMovement().teleport(Position.of(1432, 9913, 0)));
		ObjectAction.register(EXIT, "exit", (player, exit) ->
			player.getMovement().teleport(Position.of(1420, 3588, 0)));
	}
}
