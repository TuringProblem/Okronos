package io.ruin.model.item.actions.impl.storage;

import com.google.gson.annotations.Expose;
import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;

public class SoulBearer {
	public static void register() {
		ItemAction.registerInventory(ItemID.SOUL_BEARER, "Fill", (player, item) -> player.getSoulBearer().chargeSoulBearer(player));
		ItemAction.registerInventory(ItemID.SOUL_BEARER, "Check", (player, item) -> player.sendMessage("You have " + player.getSoulBearer().charges + " charges in your soul bearer."));
		ItemAction.registerInventory(ItemID.SOUL_BEARER, "Uncharge", ((player, item) -> player.getSoulBearer().unchargeSoulbearer(player, item)));
	}

	public int charges = 0;

	public void chargeSoulBearer(Player player) {
		player.integerInput("Enter the amount of charges you want to fill.", s -> {
			if (s > 0) {
				int bloodRunes = player.getInventory().getAmount(ItemID.BLOOD_RUNE);
				int soulRunes = player.getInventory().getAmount(ItemID.SOUL_RUNE);
				if (bloodRunes >= s && soulRunes >= s) {
					player.getSoulBearer().charges += s;
					player.getInventory().remove(ItemID.SOUL_RUNE, s);
					player.getInventory().remove(ItemID.BLOOD_RUNE, s);
					player.sendMessage("You add " + s + " charges to your soul bearer, you now have " + player.getSoulBearer().charges + " charges.");
				} else {
					int amount;
					if (bloodRunes > soulRunes)
						amount = soulRunes;
					else if (soulRunes > bloodRunes)
						amount = bloodRunes;
					else amount = bloodRunes;
					if (amount < 1)
						player.sendMessage("You need at least 1 blood rune and 1 soul rune to charge your soul bearer.");
					else {
						player.getSoulBearer().charges += amount;
						player.getInventory().remove(ItemID.SOUL_RUNE, amount);
						player.getInventory().remove(ItemID.BLOOD_RUNE, amount);
						player.sendMessage("You add " + amount + " charges to your soul bearer, you now have " + player.getSoulBearer().charges + " charges.");
					}
				}
			}
		});
	}

	public void unchargeSoulbearer(Player player, Item item) {
		if (player.getSoulBearer().charges > 0) {
			player.dialogue(new YesNoDialogue("Are you sure you want to uncharge it?", "You will only get 80% of the runes back.", item, () -> {
				player.getInventory().add(ItemID.DEATH_RUNE, (int) (player.getSoulBearer().charges * 0.8));
				player.getInventory().add(ItemID.SOUL_RUNE, (int) (player.getSoulBearer().charges * 0.8));
				player.getSoulBearer().charges = 0;
				player.sendMessage("You uncharge your soul bearer.");
			}));
		} else player.sendMessage("Your soul bearer already has no charges.");
	}

	public void sendEnsouledHeadToBank(Player player, Item item) {
		if (player.getSoulBearer().charges > 0) {
			player.getBank().add(item.getId(), item.getAmount());
			player.getSoulBearer().charges--;
			player.sendFilteredMessage("Your soul bearer sends an " + item.getDef().name + " to your bank consuming a charge.");
		}
	}
}
