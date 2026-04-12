package io.ruin.services.http.models.store;

import discord.webhooks.logs.DonationClaimHook;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.model.activities.DonationBossHandler;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.process.event.EventWorker;
import io.ruin.services.http.HttpRequestBuilder;
import io.ruin.services.http.HttpRequestEvent;
import io.ruin.services.http.HttpRequestMethod;
import io.ruin.utility.Broadcast;
import io.ruin.utility.Utils;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DonationCommandHandler {

	public static void handleClaim(Player player) {
		var elapsedMS = System.currentTimeMillis() - player.lastClaimDonation;
		if (elapsedMS <= 30_000) {
			var reclaimUnlockedMS = 30_000 - elapsedMS;
			player.sendMessage("You can claim donation in " + (reclaimUnlockedMS / 1000) + "s");
			return;
		}

		player.lastClaimDonation = System.currentTimeMillis();

		// TODO implement a security method to stop user from using this command more than 30 seconds
		player.sendMessage("Attempting to claim your items.");

		HttpRequestEvent builder = new HttpRequestBuilder("store/claim?username=" + player.getName())
				.onSuccess(response -> {
					DonationRequestClaim respObject = response.getResponseObject(DonationRequestClaim.class);
					if (respObject == null)
						return;

					List<String> toMarkAsClaimed = new ArrayList<String>();

					float getTotalSpent = (float) (respObject.overallSpent) / 100;
					float totalSpentThisClaim = (float) respObject.totalSpentThisClaim / 100;
					DonationBossHandler.addDonationAmount((int) totalSpentThisClaim);
					Instant now = Instant.now();
					player.lastDonationClaimInEpoch = now.getEpochSecond();

					// TODO: handle what to do with these variables, set total donated or whatever

					if (respObject.viewModels.length == 0) {
						player.sendMessage(
								"You have nothing to claim! Try again later or if you feel this is a mistake reach out to management");
						return;
					} else {
						List<String> toRedeem = handleRequestBasket(respObject, player);
						markAsClaimed(toRedeem, player);
					}
				})
				.onFailure(respone -> {
					player.sendMessage("Something went wrong attempting to claim your items - try again later.");
				})
				.build();

		EventWorker.submitHttpRequest(builder);
	}

	private static List<String> handleRequestBasket(DonationRequestClaim basket, Player player) {
		List<String> successfulRequests = new ArrayList<String>();
		for (DonationViewModel viewModel : basket.viewModels) {
			successfulRequests.add(viewModel.basketModel.id);

			for (DonationIngameItems.IngameItem item : viewModel.inGameItems.items) {
				Item newItem = new Item(item.itemId, item.amount);
				if (player.getInventory().hasRoomFor(newItem)) {
					player.sendMessage("Added "
							+ Utils.formatMoneyString(item.amount) + "x "
							+ newItem.getDef().name + " to your inventory!");
					player.getInventory().add(newItem);
				} else {
					player.getBank().add(item.itemId, item.amount);
					player.sendMessage("Added "
							+ Utils.formatMoneyString(item.amount) + "x "
							+ newItem.getDef().name + " to your bank!");
				}
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(),
						" has just donated for " + newItem.getAmount() + "x " + newItem.getDef().getName() + "!");
				switch (newItem.getId()) {
					case ItemID.FIVE_BOND -> player.claimDonation(player, 5 * newItem.getAmount());
					case ItemID.TEN_BOND -> player.claimDonation(player, 10 * newItem.getAmount());
					case ItemID.TWENTY_FIVE_BOND -> player.claimDonation(player, 25 * newItem.getAmount());
					case ItemID.FIFTY_BOND -> player.claimDonation(player, 50 * newItem.getAmount());
					case ItemID.HUNDRED_BOND -> player.claimDonation(player, 100 * newItem.getAmount());
				}

				DonationClaimHook.sendDonatorLogsToDiscord(() -> {
					var object = new JSONObject();
					object.put("player", player.getName());
					object.put("player_hwid", player.hwid);
					object.put("item_amount", newItem.getAmount());
					object.put("item_name", newItem.getDef().getName());
					return object;
				});
			}
			player.sendMessage("Thank you for donating!");
		}

		return successfulRequests;
	}

	private static void markAsClaimed(List<String> markAsComplete, Player player) {
		HttpRequestEvent confirmTask =
				new HttpRequestBuilder("store/claim/confirm?ipAddress=" + player.getIp(), HttpRequestMethod.POST)
						.body(markAsComplete)
						.onSuccess(request -> {
						})
						.onFailure(r -> {
						})
						.build();
		EventWorker.submitHttpRequest(confirmTask);
	}

}
