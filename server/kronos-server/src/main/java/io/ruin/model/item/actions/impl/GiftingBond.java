package io.ruin.model.item.actions.impl;

import discord.webhooks.logs.BondHook;
import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemPlayerAction;
import io.ruin.utility.Broadcast;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class GiftingBond {


	private static final List<Integer> BOND_IDS = Arrays.asList(30464, 30497, 30466, 30467, 30468);

	public static void register() {
		int[] bondIdsArray = BOND_IDS.stream().mapToInt(Integer::intValue).toArray();

		for (int bondId : bondIdsArray) {
			ItemPlayerAction.register(bondId, (player, item, other) -> {
				if (!item.copyOfAttributes().isEmpty()) {
					return;
				}
				if (other.getInventory().isFull()) {
					player.sendFilteredMessage("That player doesn't have enough inventory space.");
					return;
				}


				player.startEvent(event -> {
					player.lock();
					player.dialogue(new OptionsDialogue(Color.DARK_RED.wrap("Are you sure you want to gift this bond to " + other.getName() + "?"),
						new Option("Yes, I wish to gift " + other.getName() + " the bond", () -> handleGift(player, item, other)),
						new Option("No, I changed my mind", () -> {
							player.sendFilteredMessage("You did not gift your bond.");
							player.unlock();
						})));
				});
			});
		}
	}

	private static void handleGift(Player player, Item item, Player other) {
		if (!player.getInventory().contains(item.getId()))
			return;
		player.closeInterfaces();
		player.getInventory().remove(item.getId(), 1);
		other.getInventory().addOrDrop(item.getId(), 1);
		player.unlock();
		other.unlock();
//		RareDropEmbedMessage.sendBondGiftToDiscord(player.getName(), other.getName(), item);

		var jsonObject = new JSONObject();
			jsonObject.put("player", player.getName());
			jsonObject.put("player_receiving", other.getName());
			jsonObject.put("item_id", item.getId());
			jsonObject.put("item_name", item.getDef().name);
		BondHook.sendBondGiftToDiscord(jsonObject);

		Broadcast.GLOBAL.sendNews(Icon.BOND, "<col=52b36b><shad=112e19>[Bond Exchange]<shad=567185>", " " + other.getName() + " was just gifted a " + item.getDef().name + " from " + player.getName() + "!");
	}
}
