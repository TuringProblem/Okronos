package io.ruin.model.map.object.actions.impl.randomshortcuts;

import io.ruin.model.entity.shared.LockType;
import io.ruin.model.map.Direction;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;

public class BarbagilPipe {

	public static void register() {
		/**
		 * Obstacle pipe
		 */
		ObjectAction.register(20210, 2552, 3559, 0, "squeeze-through", (player, obj) -> player.startEvent(event -> {
			if (!player.getStats().check(StatType.Agility, 32, "use this shortcut"))
				return;
			if (player.getPosition().equals(2552, 3561)) {
				player.lock(LockType.FULL_DELAY_DAMAGE);
				player.animate(749, 30);
				player.getMovement().force(0, -2, 0, 0, 33, 126, Direction.SOUTH);
				event.delay(2);
				player.getMovement().force(0, -1, 0, 0, 33, 126, Direction.SOUTH);
				event.delay(1);
				player.animate(749);
				event.delay(2);
				player.unlock();

			} else {
				if (player.getPosition().equals(2552, 3558)) {
					player.lock(LockType.FULL_DELAY_DAMAGE);
					player.animate(749, 30);
					player.getMovement().force(0, 2, 0, 0, 33, 126, Direction.NORTH);
					event.delay(2);
					player.getMovement().force(0, 1, 0, 0, 33, 126, Direction.NORTH);
					event.delay(1);
					player.animate(749);
					event.delay(2);
					player.unlock();
				}
			}
		}));
	}
}
