package io.ruin.model.item.actions.impl;

import io.ruin.api.utils.ListUtils;
import io.ruin.cache.Icon;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.SecondaryGroup;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.services.Loggers;
import io.ruin.utility.Broadcast;

public class DonatorBond {

	public static void redeem(Player player, Item bond) {
		int amount = 0;
		switch (bond.getId()) {
			case 30464:
				amount = 500;
				player.updateTotalDonated(5);
				break;
			case 30497:
				amount = 1000;
				player.updateTotalDonated(10);
				break;
			case 30466:
				amount = 3000;
				player.updateTotalDonated(25);
				break;
			case 30467:
				amount = 6000;
				player.updateTotalDonated(50);
				break;
			case 30468:
				amount = 12500;
				player.updateTotalDonated(100);
				break;
		}
		bond.remove();
		Loggers.logBond(player.getUserId(), player.getName(), player.getIp(), "Donator points before bond: " + player.donatorPoints);
		player.donatorPoints += amount;
		// Loggers.logBond(player.getUserId(), player.getName(), player.getIp(), "Donator points after bond: " + player.donatorPoints);
		// Loggers.logBond(player.getUserId(), player.getName(), player.getIp(), "has claimed " + amount + " donator points from a bond.");
		player.sendMessage("You have redeemed " + amount + " donator points!");
		checkDonatorStatus(player);
	}

	public static void register() {
		LoginListener.register(DonatorBond::checkDonatorStatus);
		LoginListener.register(p -> {

			p.getDailyVote().voteRewardRefresh();

			checkDonatorStatus(p);
			if (p.inDynamicMap && p.currentDynamicMap == null) {
				p.getMovement().teleport(3087, 3496, 0);
				p.inDynamicMap = false;
			}
		});
		ItemAction.registerInventory(30497, "redeem", (player, item) -> {
			player.dialogue(new OptionsDialogue("Are you sure you want to redeem the bond?",
				new Option("Yes, redeem my bond.", () -> {
					redeem(player, item);
				}),
				new Option("Nevermind.")));
		});
		for (int i = 30464; i < 30469; i++) {
			ItemAction.registerInventory(i, "redeem", (player, item) -> {
				player.dialogue(new OptionsDialogue("Are you sure you want to redeem the bond?",
					new Option("Yes, redeem my bond.", () -> {
						redeem(player, item);
					}),
					new Option("Nevermind.")));
			});
		}
	}

	public static void checkDonatorStatus(Player player) {
		if (player.totalDonated >= 10 && player.totalDonated < 50 && !player.isGroups(SecondaryGroup.DONATOR)) {
			player.setSecondaryGroups(ListUtils.toList(SecondaryGroup.DONATOR.id));
			Broadcast.WORLD.sendNewsDropMessage(player, Icon.DONATOR_ICON, "<col=db5b58><shad=383737>" + player.getName(), "<col=db5b58><shad=383737> has just become a donator!");
		} else if (player.totalDonated >= 50 && player.totalDonated < 100 && !player.isGroups(SecondaryGroup.SUPER_DONATOR)) {
			player.setSecondaryGroups(ListUtils.toList(SecondaryGroup.SUPER_DONATOR.id));
			Broadcast.WORLD.sendNewsDropMessage(player, Icon.SUPER_DONATOR, "<col=1E90FF><shad=383737>" + player.getName(), "<col=1E90FF><shad=383737> has just become a super donator!");
		} else if (player.totalDonated >= 100 && player.totalDonated < 250 && !player.isGroups(SecondaryGroup.ELITE_DONATOR)) {
			player.setSecondaryGroups(ListUtils.toList(SecondaryGroup.ELITE_DONATOR.id));
			Broadcast.WORLD.sendNewsDropMessage(player, Icon.ELITE_DONATOR, "<col=27ae60><shad=383737>" + player.getName(), "<col=27ae60><shad=383737> has just become an elite donator!");
		} else if (player.totalDonated >= 250 && player.totalDonated < 500 && !player.isGroups(SecondaryGroup.NOBLE_DONATOR)) {
			player.setSecondaryGroups(ListUtils.toList(SecondaryGroup.NOBLE_DONATOR.id));
			Broadcast.WORLD.sendNewsDropMessage(player, Icon.NOBLE_DONATOR, "<col=8D501B><shad=383737>" + player.getName(), "<col=8D501B><shad=383737> has just become a noble donator!");
		} else if (player.totalDonated >= 500 && player.totalDonated < 1000 && !player.isGroups(SecondaryGroup.GOLD_DONATOR)) {
			player.setSecondaryGroups(ListUtils.toList(SecondaryGroup.GOLD_DONATOR.id));
			Broadcast.WORLD.sendNewsDropMessage(player, Icon.GOLD_DONATOR, "<col=F7E521><shad=383737>" + player.getName(), "<col=F7E521><shad=383737> has just become a gold donator!");
		} else if (player.totalDonated >= 1000 && player.totalDonated < 2500 && !player.isGroups(SecondaryGroup.PLATINUM_DONATOR)) {
			player.setSecondaryGroups(ListUtils.toList(SecondaryGroup.PLATINUM_DONATOR.id));
			Broadcast.WORLD.sendNewsDropMessage(player, Icon.PLATINUM_DONATOR, "<col=78757A><shad=383737>" + player.getName(), "<col=78757A><shad=383737> has just become a platinum donator!");
		} else if (player.totalDonated >= 2500 && player.totalDonated < 5000 && !player.isGroups(SecondaryGroup.LEGENDARY_DONATOR)) {
			player.setSecondaryGroups(ListUtils.toList(SecondaryGroup.LEGENDARY_DONATOR.id));
			Broadcast.WORLD.sendNewsDropMessage(player, Icon.LEGENDARY_DONATOR, "<col=E121F7><shad=383737>" + player.getName(), "<col=E121F7><shad=383737> has just become a legendary donator!");
		} else if (player.totalDonated >= 5000 && !player.isGroups(SecondaryGroup.SUPREME_DONATOR)) {
			player.setSecondaryGroups(ListUtils.toList(SecondaryGroup.SUPREME_DONATOR.id));
			Broadcast.WORLD.sendNewsDropMessage(player, Icon.SUPREME_DONATOR, "<col=FC7306><shad=383737>" + player.getName(), "<col=FC7306><shad=383737> has just become a supreme donator!");
		}
	}
}
