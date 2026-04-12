package io.ruin.model.activities.bosses.instancetoken;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemAction;

public class InstanceToken {
	private static final int TOKEN = 7478;

	private static void openInterface(Player player) {
		InstanceTokenInterface instanceTokenInterface = new InstanceTokenInterface();
		instanceTokenInterface.open(player);
	}

	private static void requestToJoinInstance(Player player) {
		if (player.teleportListener != null && !player.teleportListener.allow(player)) {
			return;
		}
		player.stringInput("Enter the players instance you wish to join", s -> {
			String name = s.toLowerCase();
			var instance = InstanceManager.getInstances().get(name);
			if (instance == null) {
				player.sendMessage("An instance hosted by that player couldn't be found.");
				return;
			}
			player.dialogue(
				new OptionsDialogue("Are you sure you want to join " + name + "'s instance?",
					new Option("Yes.", () -> instance.requestToJoinInstance(player)),
					new Option("No.", player::closeDialogue)
				)
			);
		});
	}

	public static void register() {
		ItemAction.registerInventory(TOKEN, 1, (player, item) -> openInterface(player));
		ItemAction.registerInventory(TOKEN, 2, (player, item) -> requestToJoinInstance(player));
	}
}
