package mokhaiotl.loc;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.map.Direction;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import mokhaiotl.area.impl.MokhaiotlDungeonOne;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-28
*/
public class Gap {

	private final static int GAP_OBJECT_ID = 57289;

	public static void registerGap() {
		ObjectAction.register(GAP_OBJECT_ID, "Jump-over", Gap::jumpGap);
	}

	private static void jumpGap(Player player, GameObject gap) {
		// if we're jumping into the instance
		if (player.getPosition().getY() < gap.getPosition().getY()) {
			// If we still have unclaimed loot, we can't jump
			if (!player.mokhaiotlRewardItems.isEmpty()) {
				player.sendMessage("You can't jump into the dungeon while you have unclaimed loot.");
				return;
			}
			var instance = new MokhaiotlDungeonOne(player);
			player.lock();
			player.animate(6132);
			player.privateSound(2462, 1, 25);
			player.getMovement().force(0, 3, 0, 0, 33, 60, Direction.NORTH);
			player.startEvent(event -> {
				event.delay(1);
				player.dialogue(new MessageDialogue("You jump the gap..."));
				player.getPacketSender().fadeOut();
				event.delay(2);
				player.set("MOKHAIOTL_DELVE_LEVEL", 1);
				instance.movePlayerToInstance(player);
				player.startDelveTimer();
				player.getPacketSender().fadeIn();
				player.deathEndListener = (_, _, _) -> {
					// move the player to the lobby
					player.getMovement().teleport(1311, 9553, 0);
					// if we die, clear the rewards
					player.mokhaiotlRewardItems.clear();
					// reset the listener
					player.deathEndListener = null;
				};
				player.unlock();
			});
		}
	}
}
