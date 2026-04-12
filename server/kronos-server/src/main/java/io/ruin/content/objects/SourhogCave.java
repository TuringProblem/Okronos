package io.ruin.content.objects;

import io.ruin.model.entity.shared.LockType;
import io.ruin.model.map.Direction;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-12
 */
public class SourhogCave {

	public static final int BLOCKAGE = 40331;

	public static void register() {
		ObjectAction.register(BLOCKAGE, "Climb-over", (player, blockage) -> {
			player.startEvent(event -> {
				player.lock(LockType.FULL_DELAY_DAMAGE);
				player.animate(839);
				if (player.getPosition().getY() >= 9705)
					player.getMovement().force(0, -2, 0, 0, 0, 60, Direction.SOUTH);
				else
					player.getMovement().force(0, 2, 0, 0, 0, 60, Direction.NORTH);
				event.delay(1);
				player.getStats().addXp(StatType.Agility, 13.7, true);

				if (player.lastAgilityObjId == 23547)
					player.lastAgilityObjId = blockage.getId();
				player.unlock();
			});
		});
	}
}
