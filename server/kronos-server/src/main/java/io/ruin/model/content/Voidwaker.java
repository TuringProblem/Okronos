package io.ruin.model.content;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.actions.impl.chargable.ThammaronsSceptre;

public class Voidwaker {

	private static void makeVoidwaker(Player player, Item itemOne, Item itemTwo) {
		if (player.getInventory().containsAll(false, new Item(30558, 1),
			new Item(30557, 1), new Item(30556))) {
			player.dialogue(new OptionsDialogue(
				Color.DARK_RED.wrap("Create voidwaker?"),
				new Option("Proceed.", () -> {
					player.getInventory().remove(new Item(30557, 1));
					player.getInventory().remove(new Item(30556));
					player.getInventory().remove(new Item(30558, 1));
					player.getInventory().add(new Item(12765, 1));
				}),
				new Option("Cancel.", Player::closeDialogue)
			));
		} else {
			player.sendMessage("You don't have all the pieces to make this.");
		}

	}

	private static void inspect(Player player, Item item) {
		player.dialogue(new ItemDialogue().one(12765, "One of three items needed to create the voidwaker."));

	}

	public static void register() {
		ItemAction.registerInventory(30556, "inspect", Voidwaker::inspect);
		ItemAction.registerInventory(30557, "inspect", Voidwaker::inspect);
		ItemAction.registerInventory(30558, "inspect", Voidwaker::inspect);
		ItemItemAction.register(30556, 30558, Voidwaker::makeVoidwaker);
		ItemItemAction.register(30556, 30557, Voidwaker::makeVoidwaker);
		ItemItemAction.register(30558, 30556, Voidwaker::makeVoidwaker);
		ItemItemAction.register(30558, 30557, Voidwaker::makeVoidwaker);
		ItemItemAction.register(30557, 30558, Voidwaker::makeVoidwaker);
		ItemItemAction.register(30557, 30556, Voidwaker::makeVoidwaker);
		ItemItemAction.register(30558, 30556, Voidwaker::makeVoidwaker);
		ItemItemAction.register(30557, 30556, Voidwaker::makeVoidwaker);
		ItemItemAction.register(30556, 30558, Voidwaker::makeVoidwaker);
		ItemItemAction.register(30557, 30558, Voidwaker::makeVoidwaker);
		ItemItemAction.register(30558, 30557, Voidwaker::makeVoidwaker);
		ItemItemAction.register(30556, 30557, Voidwaker::makeVoidwaker);
	}
}
