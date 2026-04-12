package io.ruin.model.activities.pvminstances;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;

public class DonatorPortal {

	private static void enter(Player player, GameObject object) {
		if (!player.isSapphire() && !player.isAdmin()) {
			player.getMovement().teleport(3092, 3497, 0);
			player.sendMessage("You have no donator rank, so you will be sent to home.");

		} else
			player.getMovement().teleport(3804, 2844, 0);


	}

	public static void register() {
		ObjectAction.register(4390, "Exit", DonatorPortal::enter);
	}

}
