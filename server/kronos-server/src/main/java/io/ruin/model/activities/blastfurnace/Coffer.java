package io.ruin.model.activities.blastfurnace;


import io.ruin.api.utils.Random;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.object.actions.ObjectAction;

public class Coffer {

	public static void register() {
		ObjectAction.register(29330, 1, (player, obj) -> startDialogue(player));
	}

	private static void startDialogue(Player player) {
		player.dialogue(
				new OptionsDialogue(
						new Option("I'd like to add 1 hours worth to the coffer (75,000 coins).", () -> optOne(player)),
						new Option("I'd like to add 3 hours worth to the coffer (200,000 coins).", () -> optTwo(player)),
						new Option("I'd like to add 6 hours worth to the coffer (400,000 coins).", () -> optThree(player)),
						new Option("Nevermind.")));
	}

	private static void optOne(Player plr) {
		if (plr.getInventory().hasItem(995, 75000)) {
			plr.getInventory().remove(995, 75000);
			plr.sendMessage("You add 75,000 coins to the coffer for an hour of dwarf assistance.");
			plr.blastFurnaceCofferAmount += 75000;
			plr.sendMessage("The coffer now contains " + plr.blastFurnaceCofferAmount + " coins.");
		} else {
			plr.sendMessage("You need at least 75,000 coins to add an hour!");
		}
	}

	private static void optTwo(Player plr) {
		if (plr.getInventory().hasItem(995, 200000)) {
			plr.getInventory().remove(995, 200000);
			plr.sendMessage("You add 200,000 coins to the coffer for three hours of dwarf assistance.");
			plr.blastFurnaceCofferAmount += 200000;
			plr.sendMessage("The coffer now contains " + plr.blastFurnaceCofferAmount + " coins.");
		} else {
			plr.sendMessage("You need at least 200,000 coins to add 3 hours!");
		}
	}

	private static void optThree(Player plr) {
		if (plr.getInventory().hasItem(995, 400000)) {
			plr.getInventory().remove(995, 400000);
			plr.sendMessage("You add 400,000 coins to the coffer for six hours of dwarf assistance.");
			plr.blastFurnaceCofferAmount += 400000;
			plr.sendMessage("The coffer now contains " + plr.blastFurnaceCofferAmount + " coins.");
		} else {
			plr.sendMessage("You need at least 400,000 coins to add 6 hours!");
		}
	}

}
