package io.ruin.model.map.object.actions.impl;

import io.ruin.data.impl.teleports;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.object.actions.ObjectAction;

/*
 * @project Kronos
 * @author Patrity - https://github.com/Patrity
 * Created on - 6/16/2020
 */
public class DonatorChinPortal {

	public static void register() {
		ObjectAction.register(30397, "Pass-Through", ((player, obj) -> teleport(player)));
	}

	private static void teleport(Player player) {
		if (player.storeAmountSpent > 49) {
			teleports.teleport(player, 3805, 2852, 0);
		} else {
			player.sendMessage("You need to be atleast a Emerald donator to pass this barier!");
		}
	}
}
