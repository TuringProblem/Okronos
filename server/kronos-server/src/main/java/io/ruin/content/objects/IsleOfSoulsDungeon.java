package io.ruin.content.objects;

import io.ruin.model.map.Position;
import io.ruin.model.map.object.actions.ObjectAction;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-12
 */
public class IsleOfSoulsDungeon {

	private static final int OVERWORLD_ENTRANCE = 40736;
	private static final int DUNGEON_EXIT = 40737;

	public static void register() {
		ObjectAction.register(OVERWORLD_ENTRANCE, "enter", (player, entrance) -> {
			player.getMovement().teleport(Position.of(2167, 9308, 0));
		});
		ObjectAction.register(DUNGEON_EXIT, "exit", (player, exit) -> {
			player.getMovement().teleport(Position.of(2310, 2919, 0));
		});
	}
}
