package io.ruin.model.map.object.actions.impl;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.skills.herblore.Potion;

/*
 * @project Kronos
 * @author Patrity - https://github.com/Patrity
 * Created on - 6/16/2020
 */
public class KronosRejuvinationPool {

	private static Potion potion;

	public static void register() {
		ObjectAction.register(50004, "Drink", ((player, obj) -> heal(player)));
		ObjectAction.register(29241, "Drink", ((player, obj) -> heal(player)));
	}

	private static void heal(Player player) {
		player.animate(833);
		player.graphics(1039);
		player.getStats().restore(true);
		player.getMovement().restoreEnergy(100);
		player.curePoison(1);
		player.cureVenom(1);
		player.magicImbueHeartCooldown.delay(0);
		player.combatantHeartCooldown.delay(0);
		player.rangersHeartCooldown.delay(0);
		player.overloadHeartCooldown.delay(0);
		if (player.storeAmountSpent > 99) {
			player.getCombat().restore();
			player.sendMessage("Your stats and special attack have been restored!");
		} else {
			player.sendMessage("Your stats have been restored!");
		}

	}

}
