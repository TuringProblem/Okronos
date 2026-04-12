package io.ruin.model.item.actions.impl.boxes;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;

public class BrokenZombieAxe {

	private static void inspect(Player player, Item item) {
		player.sendMessage("The axe isn't in a great state, but it should be possible to repair it. You reckon you have the skills needed to do so yourself. You'll just need a hammer and an anvil.");
	}

	private static final int BROKEN_ZOMBIE_AXE = 28813;

	public static void register() {
		ItemAction.registerInventory(BROKEN_ZOMBIE_AXE, "inspect", BrokenZombieAxe::inspect);
	}
}
