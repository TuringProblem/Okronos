package npc.nex.loc;

import npc.nex.utils.ZarosUtils;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;

import static npc.nex.modes.Forms.UNATTACKABLE_NEX;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
public class AncientBarrier {

	private static final int ANCIENT_BARRIER_ID = 42967;

	public static void register() {
		ObjectAction.register(ANCIENT_BARRIER_ID, 1, AncientBarrier::passAction);
		ObjectAction.register(ANCIENT_BARRIER_ID, 2, AncientBarrier::peekAction);
	}

	public static void passAction(Player player, GameObject barrier) {
		Direction dir;
		player.face(barrier);
		Direction playerFacingDirection = Direction.getDirection(player.getPosition(), barrier.getPosition());

		int diffX, diffY = 0;
		//    System.out.println("Player facing direction: "+playerFacingDirection);
		if (playerFacingDirection == Direction.SOUTH_EAST) {
			dir = Direction.EAST;
			diffX = 2;
		}
		else {
			diffX = 0;
			player.sendMessage("You have to leave using the altar on the East side of the room.");
			return;
		}

		player.face(barrier);
		player.lock();
		player.startEvent(event -> {
			player.animate(4282);
			player.privateSound(3997, 1, 25);
			player.getMovement().force(diffX, diffY, 0, 0, 33, 60, dir);
			event.delay(1);
			player.unlock();
			if (ZarosUtils.shouldSpawnNex(player)) {
				new NPC(UNATTACKABLE_NEX.getNpcId())
					.spawn(
						player.getPosition().getRegion().baseX + 44,
						player.getPosition().getRegion().baseY + 18,
						player.getPosition().getZ(),
						40
					);
				player.sendMessage("You're not alone in there...");
			}
		});

	}

	public static void peekAction(Player player, GameObject barrier) {

	}

	public static void createPrivateChamber() {}
}
