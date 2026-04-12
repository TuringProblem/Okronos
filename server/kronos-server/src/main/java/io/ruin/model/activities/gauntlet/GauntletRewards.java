package io.ruin.model.activities.gauntlet;

import discord.webhooks.notifications.RareDropHook;
import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.model.World;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.activities.tempevents.summerevent.SummerEvent;
import io.ruin.model.content.HomeHandler;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.Broadcast;
import io.ruin.utility.Utils;
import org.json.JSONObject;

public class GauntletRewards {
	boolean corrupted;

	public GauntletRewards(Boolean corrupted) {
		this.corrupted = corrupted;
	}

	private static final LootTable MINIMAL_DAMAGE = new LootTable().addTable(1,
		new LootItem(23670, 1, 1), //flyer
		new LootItem(195, 1, 1), //potion
		new LootItem(2518, 1, 1)); //rotten tomato

	private static final LootTable PARTIALLY_DAMAGED = new LootTable().addTable(1,
		new LootItem(1211, 1, 1), //adamant dagger
		new LootItem(1161, 1, 1), //adamant full helm
		new LootItem(1430, 2, 3, 1), //adamant mace
		new LootItem(1271, 1, 1), //adamant pickaxe
		new LootItem(1123, 1, 1), //adamant platebody
		new LootItem(1073, 1, 1), //adamant platelegs
		new LootItem(1091, 1, 1), //adamant plateskirt
		new LootItem(1331, 1, 1), //adamant scim
		new LootItem(852, 7, 13, 1), //maple long
		new LootItem(854, 8, 11, 1), //maple short
		new LootItem(1159, 1, 1), //mith full
		new LootItem(1429, 2, 5, 1), //mith mace
		new LootItem(1121, 1, 1), //mith platebody
		new LootItem(1071, 1, 1), //mith platelegs
		new LootItem(1085, 1, 1), //mith plateskirt
		new LootItem(556, 200, 300, 1), //air rune
		new LootItem(559, 250, 350, 1), //body rune
		new LootItem(557, 200, 300, 1), //earth rune
		new LootItem(554, 200, 300, 1), //fire rune
		new LootItem(558, 300, 400, 1), //mind rune
		new LootItem(555, 200, 300, 1), //water rune
		new LootItem(1892, 10, 20, 1), //cake
		new LootItem(340, 75, 125, 1), //cod
		new LootItem(334, 50, 100, 1), //trout
		new LootItem(222, 300, 500, 1), //eye of newt
		new LootItem(2356, 15, 30, 1), //silver bar
		new LootItem(1624, 1, 3, 1)); //uncut sapphire

	private final LootTable COMPLETED_COMMON_REWARDS = new LootTable().addTable(1,
		new LootItem(1164, corrupted ? 3 : 2, corrupted ? 5 : 4, 1), //rune full helm
		new LootItem(1114, corrupted ? 2 : 1, corrupted ? 3 : 2, 1), //rune chain
		new LootItem(1128, corrupted ? 2 : 1, 2, 1), //rune platebody
		new LootItem(1080, corrupted ? 2 : 1, corrupted ? 3 : 2, 1), //rune platelegs
		new LootItem(1094, corrupted ? 2 : 1, corrupted ? 3 : 2, 1), //rune plateskirt
		new LootItem(3203, corrupted ? 2 : 1, corrupted ? 3 : 2, 1), //rune halberd
		new LootItem(1276, corrupted ? 2 : 1, corrupted ? 3 : 2, 1), //rune pickaxe
		new LootItem(3205, corrupted ? 2 : 1, corrupted ? 2 : 1, 1), //dragon halberd
		new LootItem(564, corrupted ? 175 : 160, corrupted ? 250 : 240, 1), //cosmic rune
		new LootItem(561, corrupted ? 125 : 100, corrupted ? 150 : 140, 1), //nature rune
		new LootItem(563, corrupted ? 100 : 80, corrupted ? 150 : 140, 1), //law rune
		new LootItem(562, corrupted ? 200 : 180, corrupted ? 350 : 300, 1), //chaos rune
		new LootItem(560, corrupted ? 125 : 100, corrupted ? 175 : 160, 1), //death rune
		new LootItem(565, corrupted ? 100 : 80, corrupted ? 150 : 140, 1), //blood rune
		new LootItem(888, corrupted ? 1000 : 800, corrupted ? 1500 : 1200, 1), //mithril arrow
		new LootItem(890, corrupted ? 500 : 400, corrupted ? 725 : 600, 1), //adamant arrow
		new LootItem(892, corrupted ? 250 : 200, corrupted ? 450 : 300, 1), //rune arrow
		new LootItem(11212, corrupted ? 50 : 30, corrupted ? 100 : 80, 1), //dragon arrow
		new LootItem(1624, corrupted ? 25 : 20, corrupted ? 65 : 60, 1), //uncut sapphire
		new LootItem(1622, corrupted ? 15 : 10, corrupted ? 60 : 50, 1), //uncut emerald
		new LootItem(1620, corrupted ? 10 : 5, corrupted ? 40 : 30, 1), //uncut ruby
		new LootItem(1620, corrupted ? 5 : 3, corrupted ? 15 : 6, 1), //uncut diamond
		new LootItem(1392, corrupted ? 8 : 4, corrupted ? 12 : 8, 1), //battlestaff
		new LootItem(995, corrupted ? 75000 : 20000, corrupted ? 150000 : 80000, 1) //coins
	);

	private final LootTable COMPLETED_RARE_REWARDS = new LootTable().addTable(1,
		new LootItem(4207, 1, corrupted ? 10 : 17), //crystal weapon seed
		new LootItem(23956, 1, corrupted ? 10 : 17), //crystal armour seed
		new LootItem(23941, 1, corrupted ? 5 : 2), //ornament
		new LootItem(23937, 1, corrupted ? 5 : 2), //ornament
		new LootItem(23935, 1, corrupted ? 5 : 2), //ornament
		new LootItem(23933, 1, corrupted ? 5 : 2), //ornament
		new LootItem(23931, 1, corrupted ? 5 : 2), //ornament
		new LootItem(23929, 1, corrupted ? 5 : 2), //ornament
		new LootItem(23927, 1, corrupted ? 5 : 2), //ornament
		new LootItem(25859, 1, corrupted ? 2 : 1)); //enhanced crystal weapon seed

	public void openChest(Player player) {
		if (player.crystallineGauntletChestToBeLooted || player.corruptedGauntletChestToBeLooted) {
			Item reward;
			if (player.gauntlet.bossDamage < 200)
				reward = MINIMAL_DAMAGE.rollDrop(player);
			else
				reward = PARTIALLY_DAMAGED.rollDrop(player);

			player.corruptedGauntletChestToBeLooted = false;
			player.crystallineGauntletChestToBeLooted = false;
			VarPlayerRepository.GAUNTLET_REWARD.set(player, 0);
			int shardAmount = Random.get(corrupted ? 5 : 3, corrupted ? 9 : 7);
			player.getInventory().add(23962, shardAmount);
			player.getInventory().add(reward.getId(), reward.getAmount());
			player.addToCollectionLog(reward);
			player.sendMessage(Color.RED.wrap("Untradeable drop: " + shardAmount + "x Crystal shard"));
			player.corruptedGauntletBossChestToBeLooted = false;
			player.crystallineGauntletBossChestToBeLooted = false;
			player.localPlayers().forEach(p -> {
				p.sendFilteredMessage(Color.DARK_GREEN.wrap(player.getName() + " received a drop: " + NumberUtils.formatNumber(reward.getAmount()) + " x " + reward.getDef().name));
			});
		}


		if (player.corruptedGauntletBossChestToBeLooted || player.crystallineGauntletBossChestToBeLooted) {
			if (player.getInventory().getFreeSlots() < 4) {
				player.sendMessage("You need at least 4 slots free to loot this chest.");
				return;
			}
			SummerEvent.handleKill(player, "The Gauntlet");
			for (int i = 0; corrupted ? i < 3 : i < 2; i++) {
				float randomRoll = Random.get(0, corrupted ? 11 : 24);
				randomRoll *= (1 - (player.calculateDropRate() / 100.0));
				Item reward;
				if (randomRoll == 0) {
					reward = COMPLETED_RARE_REWARDS.rollDrop(player);

					if (reward.getId() == 23759 || reward.getId() == 23757 || reward.getId() == 25859
						|| reward.getId() == 23941 || reward.getId() == 23937 || reward.getId() == 23935 || reward.getId() == 23933
						|| reward.getId() == 23931 || reward.getId() == 23929 || reward.getId() == 23927) {
						String gauntlet = corrupted ? "Corrupted Gauntlet" : "Gauntlet";
						int kills = corrupted ? player.theCorruptedGauntletKills.getKills() : player.theGauntletKills.getKills();
						Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + reward.getAmount() + "x " + reward.getDef().name.toLowerCase() + "</shad> from The " + gauntlet + "! (<col=FC0101>" + kills + " KC<col=000000>)");

						RareDropHook.sendDiscordMessage(() -> {
							var jsonObject = new JSONObject();
							jsonObject.put("player", player.getName());
							jsonObject.put("game_mode", player.getGameMode());
							jsonObject.put("item_id", reward.getId());
							jsonObject.put("item_name", reward.getDef().name);
							jsonObject.put("source", gauntlet);
							jsonObject.put("total_attempts", Utils.formatMoneyString(kills));
							return jsonObject;
						});
					}
				} else
					reward = COMPLETED_COMMON_REWARDS.rollDrop(player);


				player.addToCollectionLog(reward);
				player.getInventory().add(reward.getId(), reward.getAmount());

				player.localPlayers().forEach(p -> {
					p.sendFilteredMessage(Color.DARK_GREEN.wrap(player.getName() + " received a drop: " + NumberUtils.formatNumber(reward.getAmount()) + " x " + reward.getDef().name));
				});

			}
			float basePetChance = 500;
			if (corrupted)
				basePetChance = 100;
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
				ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				basePetChance *= (float) c.getPetChanceBoost();
			}
			if (player.petDropBonus.isDelayed())
				basePetChance *= 0.8f;
			if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
				basePetChance *= 0.85F;

			basePetChance *= (float) HomeHandler.getPetDonatorBoost(player);
			if (Random.get((int) basePetChance) == 0) {
				World.startEvent(event -> {
					event.delay(1);
					int npcId = corrupted ? 9035 : 9021; // Proper Gauntlet boss IDs
					if (corrupted)
						Pet.CORRUPTED_YOUNGLLEF.unlock(player, npcId);
					else
						Pet.YOUNGLLEF.unlock(player, npcId);
				});
			}
			VarPlayerRepository.GAUNTLET_REWARD.set(player, 0);
			int shardAmount = Random.get(corrupted ? 10 : 5, corrupted ? 45 : 25);
			player.getInventory().add(23962, shardAmount);
			player.sendMessage(Color.RED.wrap("Untradeable drop: " + shardAmount + "x Crystal shard"));
			player.corruptedGauntletBossChestToBeLooted = false;
			player.crystallineGauntletBossChestToBeLooted = false;
		}
	}

	public void replaceChest(GameObject object, int replacementID, Player player) {
		if (player.getInventory().getFreeSlots() < 4) {
			player.sendMessage("You need at least 4 slots free to loot this chest.");
			return;
		}
		if (!player.corruptedGauntletBossChestToBeLooted && !player.crystallineGauntletBossChestToBeLooted
			&& !player.crystallineGauntletChestToBeLooted && !player.corruptedGauntletChestToBeLooted) {
			player.sendMessage("The chest is empty.");
			return;
		}
		World.startEvent(event -> {
			player.sendMessage("You open the chest.");
			player.animate(536);
			player.lock();
			event.delay(1);
			player.sendMessage("You find some treasure in the chest.");
			openChest(player);
			player.unlock();
			event.delay(1);
		});
	}
}







