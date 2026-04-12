package io.ruin.model.content;

import discord.webhooks.logs.ReferralHook;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeExtensions;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RefSystem {
	private static final String filePath = "data/referral.txt";

	public static boolean hasUsedReferral(Player player) {
		String playerHwid = player.hwid;

		try {
			List<String> lines = Files.readAllLines(Paths.get(filePath));
			return lines.stream().map(String::trim).anyMatch(line -> line.equals(playerHwid));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static void claimReferral(Player player, String code) {
		if (hasUsedReferral(player) && !player.hasRef || player.refClaimed) {
			player.sendMessage("You have already claimed a ref code.");
			return;
		}
		if (player.getInventory().getFreeSlots() < 5) {
			player.sendMessage("You need at least 5 free inventory slots to claim a ref code.");
			return;
		}
		player.hasRef = false;
		player.refClaimed = true;
//		RareDropEmbedMessage.sendRefClaimLogToDiscord(player);

		ReferralHook.sendRefClaimLogToDiscord(player.getName(), code);

		handleRewardFromCode(player, code);
	}

	private static void handleRewardFromCode(Player player, String code) {
		switch (code) {
			case "returning": {
				player.getInventory().add(30460, 2);//double exp scroll
				player.getInventory().add(608, 2); //5% dr scroll 1 hour
				player.getInventory().add(30570, 10); //10 perk point scrolls
				addHwidToFile(player);
				break;
			}
			case "newplayer": {
				Item bond = new Item(30464, 1);
				AttributeExtensions.addCharges(bond, 5);
				player.getInventory().add(bond); //$5 bond
				player.getInventory().add(30460, 2); //double exp scroll
				player.getInventory().add(30570, 10); //10 perk point scrolls
				addHwidToFile(player);
				break;
			}
			default: {
				player.sendMessage("Invalid ref code.");
				break;
			}
		}
	}

	public static void addHwidToFile(Player player) {
		String playerHwid = player.hwid;

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
			bw.write(playerHwid);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
