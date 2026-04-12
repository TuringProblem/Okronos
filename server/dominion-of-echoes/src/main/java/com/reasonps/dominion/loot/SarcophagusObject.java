package com.reasonps.dominion.loot;

import discord.webhooks.notifications.RareDropHook;
import io.ruin.cache.Icon;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.item.Item;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.utility.Broadcast;
import io.ruin.utility.Utils;
import org.json.JSONObject;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-06-05
 */
public class SarcophagusObject {

	private static final int RAID_REWARD_INTERFACE = 1122;
	private static final int RAID_REWARD_INTERFACE_CHILD = RAID_REWARD_INTERFACE << 16;

	public static void register() {
		ObjectAction.register(60000, "Search", SarcophagusObject::openSarcophagus);
	}

	private static void openSarcophagus(Player player, GameObject casket) {
		if (player.get("dominion_of_echos_rewarded").equals(true)) {
			player.doeRewardItems.forEach(reward -> {
				player.getInventory().addOrDrop(reward);
				if (reward.lootBroadcast != null) {
					Broadcast.WORLD.sendNewsDropMessage(
							player,
							Icon.ADMINISTRATOR,
							"<col=000000>".concat(player.getName()),
							" has just received <shad>%s</shad> from Dominion of Echoes! (Chest: %s)"
									.formatted(reward.getDef().name, player.dominionOfEchoesKills.getKills()));


					RareDropHook.sendDiscordMessage(() -> {
						var jsonObject = new JSONObject();
						jsonObject.put("player", player.getName());
						jsonObject.put("game_mode", player.getGameMode());
						jsonObject.put("item_id", reward.getId());
						jsonObject.put("item_name", reward.getDef().name);
						jsonObject.put("source", "Dominion of Echoes");
						jsonObject.put("total_attempts", Utils.formatMoneyString(player.dominionOfEchoesKills.getKills()));
						return jsonObject;
					});
				}
			});
			openRewardInterface(player);
			player.doeRewardItems.clear();
			player.set("dominion_of_echos_rewarded", false);
		}
	}

	private static void openRewardInterface(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, RAID_REWARD_INTERFACE);
		int startingItemComponent = 18;
		int startingContainerId = 566;
		player.getPacketSender().sendString(RAID_REWARD_INTERFACE, 15, "Dominion of Echoes");
		for (int i = 0; i < 6; i++) {
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					RAID_REWARD_INTERFACE_CHILD | startingItemComponent + i, startingContainerId + i,
					4, 7, 1, -1, "", "", "", "", "");
			player.getPacketSender().sendItems(
					-1,
					startingItemComponent + i,
					startingContainerId + i,
					new Item(-1));
		}
		for (Item item : player.doeRewardItems) {
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					RAID_REWARD_INTERFACE_CHILD | startingItemComponent, startingContainerId,
					4, 7, 1, -1, "", "", "", "", "");
			player.getPacketSender().sendItems(-1, startingItemComponent, startingContainerId, item);

			startingItemComponent++;
			startingContainerId++;
			player.addToCollectionLog(item);
		}
	}

}
