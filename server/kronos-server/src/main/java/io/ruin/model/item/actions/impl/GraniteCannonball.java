package io.ruin.model.item.actions.impl;

import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemItemAction;

public class GraniteCannonball {

	public static void makeGraniteCannonball(Player player, Item item, Item itemTwo) {
		if (item.getId() != ItemID.CANNONBALL && itemTwo.getId() != ItemID.CANNONBALL) {
			return;
		}
		if (item.getId() != ItemID.GRANITE_DUST && itemTwo.getId() != ItemID.GRANITE_DUST) {
			return;
		}
		if (player.getInventory().hasMultiple(ItemID.CANNONBALL) && player.getInventory().hasMultiple(ItemID.GRANITE_DUST)) {
			player.integerInput("How many granite cannonballs do you want to make? ", amt -> {
				int amountToMake = amt;
				if (amountToMake < 1) {
					player.sendMessage("You must make at least one cannonball.");
					return;
				}
				if (amountToMake > player.getInventory().getAmount(ItemID.CANNONBALL)) {
					amountToMake = player.getInventory().getAmount(ItemID.CANNONBALL);
				}
				if (amountToMake > player.getInventory().getAmount(ItemID.GRANITE_DUST)) {
					amountToMake = player.getInventory().getAmount(ItemID.GRANITE_DUST);
				}
				int finalAmountToMake = amountToMake;
				player.startEvent(event -> {
					player.lock();
					player.animate(898);
					event.delay(2);
					player.getInventory().remove(ItemID.CANNONBALL, finalAmountToMake);
					player.getInventory().remove(ItemID.GRANITE_DUST, finalAmountToMake);
					player.getInventory().add(ItemID.GRANITE_CANNONBALL, finalAmountToMake);
					player.sendMessage("You make " + finalAmountToMake + " granite cannonballs.");
					player.unlock();
				});
			});
		} else {
			player.startEvent(event -> {
				player.lock();
				player.animate(898);
				event.delay(2);
				player.getInventory().remove(item.getId(), 1);
				player.getInventory().add(ItemID.GRANITE_CANNONBALL, 1);
				player.sendMessage("You make a granite cannonball.");
				player.unlock();
			});
		}
	}

	public static void register() {
		ItemItemAction.register(ItemID.CANNONBALL, ItemID.GRANITE_DUST, GraniteCannonball::makeGraniteCannonball);
		ItemItemAction.register(ItemID.GRANITE_DUST, ItemID.CANNONBALL, GraniteCannonball::makeGraniteCannonball);
	}
}
