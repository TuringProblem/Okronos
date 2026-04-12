package io.ruin.model.activities.bosses.sarachnis;


import io.ruin.model.activities.bosses.instancetoken.InstanceMaps;
import io.ruin.model.entity.player.Player;

import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.object.actions.ObjectAction;

public class SarachnisLair {

	private static void goThroughPassage(Player player) {
		/**
		 * Sarachnis entrance
		 */
		int x = player.getAbsX();
		player.getMovement().teleport(1842, 9911, 0);
	}

	private static void enterCave(Player player) {
		if (player.getCombat().isDefending(5)) {
			player.sendMessage("You cannot enter the cave whilst in combat.");
			return;
		}
		player.dialogue(new OptionsDialogue("Which Ophidia's Lair would you like to enter?",
			new Option("Global Sarachnis Lair", () -> {
				player.getMovement().teleport(1842, 9911, 0);
			}),
			new Option("Instanced Sarachnis Lair", () -> {
				player.getInstanceTokenInterface().selectedBoss = InstanceMaps.SARACHNIS;
				player.getInstanceTokenInterface().startInstance(player, true);
			})));
	}


	public static void register() {
		ObjectAction.register(34858, 2, (player, obj) -> goThroughPassage(player));
		ObjectAction.register(34858, 1, (player, obj) -> enterCave(player));

	}
}
