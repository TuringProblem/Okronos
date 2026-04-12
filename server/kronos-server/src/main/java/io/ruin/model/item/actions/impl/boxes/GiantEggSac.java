package io.ruin.model.item.actions.impl.boxes;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;

public class GiantEggSac {

	private static void open(Player player, Item item) {
		item.remove();
		player.getInventory().add(224, 100);
		player.sendMessage("The noted red spiders' eggs from your Giant egg sac have been added to your inventory.");
	}

	private static void checkEggs(Player player, Item item) {
		player.sendMessage("There are 100 noted red spiders' eggs in the sac.");
	}

	private static final int GIANT_EGG_SAC = 23517;

	public static void register() {
		ItemAction.registerInventory(GIANT_EGG_SAC, "cut-open", GiantEggSac::open);
		ItemAction.registerInventory(GIANT_EGG_SAC, "check-eggs", GiantEggSac::checkEggs);
	}
}
