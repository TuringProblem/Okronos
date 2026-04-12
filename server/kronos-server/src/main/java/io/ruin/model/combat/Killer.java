package io.ruin.model.combat;

import io.ruin.api.utils.Random;
import io.ruin.api.utils.TimeUtils;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.model.World;
import io.ruin.model.activities.tasks.DailyTask;
import io.ruin.model.activities.wilderness.Hotspot;
import io.ruin.model.activities.wilderness.ResourceArea;
import io.ruin.model.activities.wilderness.StaffBounty;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.SecondaryGroup;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.DonatorLamp;
import io.ruin.model.item.actions.impl.WildernessDeadmanKey;
import io.ruin.model.item.actions.impl.boxes.WildernessRewardBox;
import io.ruin.model.map.Bounds;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.services.Loggers;
import io.ruin.services.discord.old.impl.KillingSpreeEmbedMessage;
import io.ruin.services.discord.old.impl.ShutdownEmbedMessage;
import io.ruin.utility.Broadcast;

public class Killer {
	public static final Bounds EDGEVILLE_FARM_SKIP_BOUNDS = new Bounds(3034, 3523, 3125, 3574, 0);

	public static int BASE_BM_REWARD = 50;

	public Player player;

	public int damage;

	public void reward(Player pKilled, boolean bhTarget) {
		/*
		 * Farming checks
		 */
		long ms = System.currentTimeMillis();
		if (player.getIpInt() == pKilled.getIpInt()) //don't reward bm for killing someone on your same ip
			return;
		if (!pKilled.getPosition().inBounds(EDGEVILLE_FARM_SKIP_BOUNDS)) {
			boolean logged = player.bmIpLogs.contains(pKilled.getIpInt(), ms, 5L)
				|| player.bmUserLogs.contains(pKilled.getUserId(), ms, 5L);
			player.bmIpLogs.add(pKilled.getIpInt(), ms);
			player.bmUserLogs.add(pKilled.getUserId(), ms);
			if (logged)
				return;
		}


		VarPlayerRepository.SPECIAL_ENERGY.set(player, 1000);
		int pkpReward = 4;
		if (player.isRegularDonator())
			pkpReward = 5;
		else if (player.isSuperDonator())
			pkpReward = 6;
		else if (player.isEliteDonator())
			pkpReward = 7;
		else if (player.isPlatinumDonator())
			pkpReward = 8;
		else if (player.isLegendaryDonator())
			pkpReward = 9;
		if (player.getIpInt() != pKilled.getIpInt()) {
			player.updatePKPoints(pkpReward);
			player.sendMessage("<shad=000000><col=BA0000>[PK Points]</shad> <col=0C2AC1>You earn <col=C10CB9>(+" + pkpReward + ") pk points <col=0C2AC1>after killing <col=0C2AC1>"
				+ pKilled.getName() + "<col=0C2AC1>! You now have <col=0C2AC1>" + player.getPKPoints() + " PKP<col=0C2AC1>!");
		} else {
			player.sendMessage("You earn no PKP for killing someone with the same IP as you.");
		}

		/*
		 * Increment killer kills & spree.
		 */
		VarPlayerRepository.PVP_KILLS.increment(player, 1);
		int killerSpree = ++player.currentKillSpree;
		if (killerSpree > player.highestKillSpree)
			player.highestKillSpree = killerSpree;
		if (killerSpree > 1) {
			player.sendMessage("You are currently on a killing spree of " + killerSpree + "!");
			if (killerSpree % 5 == 0 || killerSpree > 15) {
				String spreeMessage = player.getName() + " is on a killing spree of " + killerSpree + ". Kill "
					+ (player.getAppearance().isMale() ? "him" : "her") + " for a bounty reward of " + (BASE_BM_REWARD + bountyValue(killerSpree)) + " Blood money!";
				Broadcast.WORLD.sendPlain(KillingSpree.imgTag(killerSpree) + Color.PVP_RED.tag() + " " + spreeMessage);
				KillingSpreeEmbedMessage.sendDiscordMessage(spreeMessage);
			}
			if (player.getCombat().isSkulled()) //Overheads start at sprees of 2, so this fits here.
				player.getAppearance().setSkullIcon(KillingSpree.overheadId(player));
		}
		/*
		 * Check for shutdown
		 */
		int targetSpree = pKilled.currentKillSpree; //Spree is set to 0 in death method. (After this method)
		if (targetSpree >= 5) {
			String shutdownMessage = KillingSpree.shutdownMessage(player.getName(), pKilled.getName(), targetSpree);
			Broadcast.WORLD.sendPlain("<img=36> " + Color.PVP_RED.tag() + shutdownMessage);
			ShutdownEmbedMessage.sendDiscordMessage(shutdownMessage);
			if (targetSpree > player.highestShutdown)
				player.highestShutdown = targetSpree;
		}





		/*
		 * Finally reward the player the blood money and refill their special attack
		 */
		if (player.getPosition().isWithinDistance(pKilled.getPosition(), 32)) {
			// only restore special if they're within a 32 tile radius
			// this was orig being abused to refill special in different places of the world by getting alt accounts low
			// health and then killing them with an alt to receive the special restore
			player.getCombat().restoreSpecial(100);
		}


		/*
		 * Roll for the wilderness reward box
		 */
		int wildernessRewardBox = WildernessRewardBox.rollForDrop(player);
		if (wildernessRewardBox != -1) {
			player.getInventory().addOrDrop(new Item(wildernessRewardBox, 1));
		}

		/*
		 * Roll for a wilderness key which can be exchanged for OSRS gold
		 */
//        if(World.wildernessKeyEvent) {
//            WildernessKey.rollForPlayerKill(player, pKilled);
//        }


		if (player.insideWildernessAgilityCourse) {
			player.getStats().addXp(StatType.Agility, 50000, false);
			player.getInventory().addOrDrop(11849, 10);
			player.sendFilteredMessage("<col=6f0000> You receive 50,000 agility experience and 10 marks of grace for killing a player inside the Agility course.");
		}


		/*
		 * Adjust wilderness elo
		 */
		WildernessRating.adjustEloRatings(pKilled, player);

		/*
		 * Check daily
		 */
		DailyTask.checkPlayerKill(player, pKilled);
	}

	private static double donatorBonus(Player player) {
		if (player.isSecondaryGroup(SecondaryGroup.LEGENDARY_DONATOR)) {
			return 1.25;
		} else if (player.isSecondaryGroup(SecondaryGroup.PLATINUM_DONATOR)) {
			return 1.20;
		} else if (player.isSecondaryGroup(SecondaryGroup.GOLD_DONATOR)) {
			return 1.15;
		} else if (player.isSecondaryGroup(SecondaryGroup.NOBLE_DONATOR)) {
			return 1.10;
		} else if (player.isSecondaryGroup(SecondaryGroup.ELITE_DONATOR)) {
			return 1.05;
		} else if (player.isSecondaryGroup(SecondaryGroup.DONATOR)) {
			return 1.00;
		} else if (player.isSecondaryGroup(SecondaryGroup.SUPER_DONATOR)) {
			return 1.00;
		} else {
			return 1.00;
		}
	}

	private static int bountyValue(int spree) {
		return (10 * spree + 50 * (spree / 10));
	}

	private static int streakValue(int spree) {
		return 5 * spree;
	}


}