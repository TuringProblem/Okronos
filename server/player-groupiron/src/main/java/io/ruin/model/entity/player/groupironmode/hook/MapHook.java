package io.ruin.model.entity.player.groupironmode.hook;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerAction;
import io.ruin.model.entity.player.groupironmode.GroupIronInvitation;
import io.ruin.model.map.MapListener;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(Attributes.class)
public class MapHook {

	public static void register() {
		MapListener.registerBounds(GroupIronInvitation.INVITE_ZONE)
				.onEnter(MapHook::enteredZone)
				.onExit(MapHook::exitedZone);
	}

	private static void enteredZone(Player player) {
		if (player.isGroupIronman()) {
			player.setAction(1, PlayerAction.GIM_Invite);
		}
	}

	private static void exitedZone(Player player, boolean logout) {
		if (player.isGroupIronman()) {
			if (player.getGroupIron() == null) {
				player.startEvent(e -> {
					e.delay(1);
					player.getMovement().teleport(3761, 3668, 0);
					player.sendMessage("You must join or form a group before leaving the island!");
				});
			}
			if (!logout) {
				player.setAction(1, null);
			}
		}
	}
}
