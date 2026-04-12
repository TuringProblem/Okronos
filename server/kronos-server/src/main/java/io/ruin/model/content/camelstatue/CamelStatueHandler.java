package io.ruin.model.content.camelstatue;

import discord.webhooks.notifications.GlobalBroadcastHook;
import io.ruin.api.utils.ExecutorUtils;
import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.map.object.GameObject;
import io.ruin.utility.Broadcast;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class CamelStatueHandler {
	public static final AtomicLong totalDonated = new AtomicLong(0);
	public static final AtomicLong amountSinceLastUpdate = new AtomicLong(0);
	static CamelStatueRewards lastReward = null;

	@Getter
	static List<CamelStatueRewards> activeRewards = new ArrayList<>();

	public static boolean isRewardActive(CamelStatueRewards reward) {
		return activeRewards.contains(reward);
	}

	public static final ScheduledExecutorService scheduler =
			Executors.newSingleThreadScheduledExecutor(r -> {
				Thread thread = new Thread(r, "CamelStatue-Scheduler");
				thread.setDaemon(true);
				return thread;
			});

	public static void donateToWell(Player player) {
		player.integerInput("How much would you like to donate?", amount -> {
			try {
				if (amount < 1) {
					player.sendMessage("You must donate at least 1 coin.");
					return;
				}
				if (!player.getInventory().contains(995)) {
					player.sendMessage("You do not have any coins to donate.");
					return;
				}
				if (amount > player.getInventory().getAmount(995)) {
					amount = player.getInventory().getAmount(995);
				}
				player.getInventory().remove(995, amount);
				addGoldPieceValueToStatue(player, amount);
				player.getCamelStatueInterface().update(player);
				updateWell();
			} catch (Exception e) {
				log.error("Error processing donation from player: %s".formatted(player.getName()), e);
				player.sendMessage("There was an error processing your donation. Please try again.");
			}
		});
	}

	public static void donatePlatinumTokens(Player player, Item platinumToken, GameObject statue) {
		player.integerInput("How much would you like to donate?", amount -> {
			try {
				if (amount < 1) {
					player.sendMessage("You must donate at least 1 token.");
					return;
				}
				if (!player.getInventory().contains(ItemID.PLATINUM_TOKEN)) {
					player.sendMessage("You do not have any tokens to donate.");
					return;
				}
				if (amount > player.getInventory().getAmount(ItemID.PLATINUM_TOKEN)) {
					amount = player.getInventory().getAmount(ItemID.PLATINUM_TOKEN);
				}
				player.getInventory().remove(ItemID.PLATINUM_TOKEN, amount);

				var goldValue = amount * 1_000L;

				addGoldPieceValueToStatue(player, goldValue);
				player.getCamelStatueInterface().update(player);
				updateWell();
			} catch (Exception e) {
				log.error("Error processing donation from player: %s".formatted(player.getName()), e);
				player.sendMessage("There was an error processing your donation. Please try again.");
			}
		});
	}

	private static void addGoldPieceValueToStatue(Player player, long amount) {
		totalDonated.addAndGet(amount);
		amountSinceLastUpdate.addAndGet(amount);

		player.sendMessage("You have donated %s coins to the statue."
				.formatted(NumberUtils.formatNumber(amount)));
		player.sendMessage("There's been a total of %s coins donated to the statue."
				.formatted(NumberUtils.formatNumber(totalDonated.get())));

		if (amount > 50_000_000)
			Broadcast.GLOBAL.sendNews(Icon.ADMINISTRATOR, "Camel Statue",
					"%s has donated %s coins to the Camel Statue at home!"
							.formatted(player.getName(), NumberUtils.formatNumber(amount)));
	}

	public static void adminDonateToWell(Player player, int amount) {
		try {
			player.sendMessage("You have donated " + NumberUtils.formatNumber(amount) + " coins to the statue.");
			player.sendMessage(
					"There's been a total of " + NumberUtils.formatNumber(totalDonated.get()) + " coins donated to the statue.");

			totalDonated.addAndGet(amount);
			amountSinceLastUpdate.addAndGet(amount);

			if (amount > 50000000)
				Broadcast.GLOBAL.sendNews(Icon.ADMINISTRATOR, "Camel Statue", "[Admin Fill] " + player.getName() +
						" has donated " + NumberUtils.formatNumber(amount) + " coins to the Camel Statue at home!");

			player.getCamelStatueInterface().update(player);
			updateWell();
		} catch (Exception e) {
			log.error("Error processing admin donation from player: " + player.getName(), e);
			player.sendMessage("There was an error processing your donation.");
		}
	}

	public static void scheduleWellClearing() {
		scheduler.scheduleAtFixedRate(() -> {
			try {
				totalDonated.set(0);
				amountSinceLastUpdate.set(0);
				activeRewards.clear();
				updateWell();
				log.info("Well cleared at midnight.");
				Broadcast.GLOBAL.sendNews(Icon.ADMINISTRATOR, "Camel Statue",
						"A new day has started and the camel statue has been wiped!");
			} catch (Exception e) {
				log.error("Error clearing well", e);
			}
		}, getDelayUntilMidnight(), 24, TimeUnit.HOURS);
	}

	private static long getDelayUntilMidnight() {
		long now = System.currentTimeMillis();
		long midnight = now - (now % 86400000) + 86400000; // Next midnight in milliseconds
		return midnight - now;
	}

	private static void updateWell() {
		try {
			if (amountSinceLastUpdate.get() >= 250000000) {
				amountSinceLastUpdate.set(0);
				Broadcast.GLOBAL.sendNews(Icon.ADMINISTRATOR, "Camel Statue",
						"Another 250,000,000 coins have been donated to the Camel Statue at home!");
			}

			var dtoArray = new JSONArray();

			for (int i = 0; i < CamelStatueRewards.values().length; i++) {
				if (totalDonated.get() < getNextRewardCost(i)) {
					break;
				}
				if (!activeRewards.contains(CamelStatueRewards.values()[i])) {
					lastReward = CamelStatueRewards.values()[i];
					Broadcast.GLOBAL.sendNews(Icon.ADMINISTRATOR, "Camel Statue",
						"The Camel Statue at home has been upgraded and is now giving " +
							lastReward.name().toLowerCase().replace("_", " ") + "!");
//					RareDropEmbedMessage.sendWellMessage(
//						lastReward.name().toLowerCase().replace("_", " ") + " has been globally activated!");
					activeRewards.add(lastReward);

					dtoArray.put(new JSONObject()
						.put("perk_name", lastReward.getName())
						.put("perk_description", lastReward.getDescription())
					);
				}
			}
			var dto = new JSONObject();
				dto.put("enabled_perks", dtoArray);

			GlobalBroadcastHook.sendWellMessage(dto);

			if (activeRewards.contains(CamelStatueRewards.DOUBLE_SLAYER_POINTS) && !World.doubleSlayer)
				World.doubleSlayer = true;
		} catch (Exception e) {
			log.error("Error updating well", e);
		}
	}

	private static long getNextRewardCost(int ordinal) {
		return CamelStatueRewards.values()[ordinal].getUnlockAmount();
	}

	public static void shutdown() {
		ExecutorUtils.shutdown(scheduler);
	}
}
