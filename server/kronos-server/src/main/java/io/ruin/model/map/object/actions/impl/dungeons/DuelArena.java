package io.ruin.model.map.object.actions.impl.dungeons;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.map.Direction;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.map.object.actions.impl.Ladder;

public class DuelArena {

	public static void register() {
		/**
		 * Duel arena shits
		 */
		//ObjectAction.register(43959, 3376, 3282, 0, 1, (player, obj) -> player.getMovement().teleport(3164, 5936, 0));
		//ObjectAction.register(35899, 3163, 5937, 0, 1, (player, obj) -> player.getMovement().teleport(3377, 3281, 0));

	}

	private static void crawlThroughTunnel(Player player, GameObject gap) {
		player.startEvent(e -> {
			player.lock(LockType.FULL_DELAY_DAMAGE);
			player.animate(2796);
			player.getMovement().teleport(3377, 3281, 0);
			e.delay(3);
			player.animate(-1);
			player.unlock();
		});
	}
}
