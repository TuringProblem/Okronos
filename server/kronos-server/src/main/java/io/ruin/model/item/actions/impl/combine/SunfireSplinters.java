package io.ruin.model.item.actions.impl.combine;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;

import java.util.HashMap;
import java.util.Map;

public class SunfireSplinters {
	private static final int SUNFIRE_SPLINTERS = 28924;
	private static final int TONALZTICS_OF_RALOS = 28922;
	private static final int TONALZTICS_OF_RALOS_UNCHARGED = 28919;
	private static final int QUIVER_UNCHARGED = 28947;
	private static final int QUIVER_CHARGED = 28951;

	private static void chargeTonalzticsOfRalos(Player player, Item primary, Item secondary, int result) {
		player.integerInput("How many would you like to charge it with?", amount -> {
			if (amount < 1) {
				player.dialogue(new MessageDialogue("You must charge it with at least 1 Sunfire Splinter."));
				return;
			}
			if (amount > secondary.getAmount()) {
				player.dialogue(new MessageDialogue("You do not have that many Sunfire Splinters."));
				return;
			}
			player.dialogue(
				new MessageDialogue("<col=7f0000>Warning!</col><br>Charging the Tonalztics of Ralos will consume your Sunfire Splinters."),
				new YesNoDialogue("Are you sure you want to do this?", "", result, amount, () -> {
					Item imbuedItem = new Item(primary.getId());
					if (imbuedItem.getId() == TONALZTICS_OF_RALOS_UNCHARGED) {
						imbuedItem.setId(TONALZTICS_OF_RALOS);
					}
					Map<String, String> attributes = new HashMap<>();
					attributes.putAll(primary.attributes);
					imbuedItem.attributes.putAll(attributes);
					primary.remove();
					AttributeExtensions.setCharges(imbuedItem, amount + AttributeExtensions.getCharges(imbuedItem));
					secondary.remove(amount);
					player.sendMessage("Your Tonalztics of Ralos now has " + AttributeExtensions.getCharges(imbuedItem) + " charges.");
					player.getInventory().add(imbuedItem);
				})
			);
		});
	}

	private static void chargeQuiver(Player player, Item primary, Item secondary, int result) {
		player.integerInput("How many would you like to charge it with?", amount -> {
			if (amount < 1) {
				player.dialogue(new MessageDialogue("You must charge it with at least 1 Sunfire Splinter."));
				return;
			}
			if (amount > secondary.getAmount()) {
				player.dialogue(new MessageDialogue("You do not have that many Sunfire Splinters."));
				return;
			}
			player.dialogue(
				new MessageDialogue("<col=7f0000>Warning!</col><br>Charging the Quiver will consume your Sunfire Splinters."),
				new YesNoDialogue("Are you sure you want to do this?", "", result, amount, () -> {
					Item imbuedItem = new Item(primary.getId());
					if (imbuedItem.getId() == QUIVER_UNCHARGED) {
						imbuedItem.setId(QUIVER_CHARGED);
					}
					Map<String, String> attributes = new HashMap<>();
					attributes.putAll(primary.attributes);
					imbuedItem.attributes.putAll(attributes);
					primary.remove();
					player.getInventory().add(imbuedItem);
					AttributeExtensions.setCharges(imbuedItem, amount + AttributeExtensions.getCharges(imbuedItem));
					secondary.remove(amount);
					player.sendMessage("Your Quiver now has " + AttributeExtensions.getCharges(imbuedItem) + " charges.");
				})
			);
		});
	}

	public static void register() {
		ItemItemAction.register(TONALZTICS_OF_RALOS_UNCHARGED, SUNFIRE_SPLINTERS, (player, primary, secondary) -> chargeTonalzticsOfRalos(player, primary, secondary, TONALZTICS_OF_RALOS));
		ItemItemAction.register(QUIVER_UNCHARGED, SUNFIRE_SPLINTERS, (player, primary, secondary) -> chargeQuiver(player, primary, secondary, QUIVER_CHARGED));
		ItemItemAction.register(SUNFIRE_SPLINTERS, TONALZTICS_OF_RALOS_UNCHARGED, (player, primary, secondary) -> chargeTonalzticsOfRalos(player, secondary, primary, TONALZTICS_OF_RALOS));
		ItemItemAction.register(SUNFIRE_SPLINTERS, QUIVER_UNCHARGED, (player, primary, secondary) -> chargeQuiver(player, secondary, primary, QUIVER_CHARGED));
		ItemItemAction.register(TONALZTICS_OF_RALOS, SUNFIRE_SPLINTERS, (player, primary, secondary) -> chargeTonalzticsOfRalos(player, primary, secondary, TONALZTICS_OF_RALOS));
		ItemItemAction.register(QUIVER_CHARGED, SUNFIRE_SPLINTERS, (player, primary, secondary) -> chargeQuiver(player, primary, secondary, QUIVER_CHARGED));


	}
}
