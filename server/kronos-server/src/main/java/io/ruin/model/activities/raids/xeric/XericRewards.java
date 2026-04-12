package io.ruin.model.activities.raids.xeric;

import discord.webhooks.notifications.RareDropHook;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.RaidingRestorations;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.inter.utils.Unlock;
import io.ruin.model.item.Item;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.services.Loggers;
import io.ruin.utility.Broadcast;
import io.ruin.utility.Utils;
import org.json.JSONObject;

import java.util.List;

public class XericRewards {

	public static void register() {
		/*
		 * ObjectAction.register(30028, "search", (player, obj) -> { // reward chest
		 * openRewards(player);
		 * });
		 * InterfaceHandler.register(Interface.RAID_REWARDS, h -> {
		 * h.actions[5] = (DefaultAction) (p, option, slot, itemId) -> {
		 * if (slot < 0 || slot >= p.getRaidRewards().getItems().length)
		 * return;
		 * if (option == 1)
		 * withdrawReward(p, slot);
		 * else {
		 * Item item = p.getRaidRewards().get(slot);
		 * if (item != null)
		 * item.examine(p);
		 * }
		 * };
		 * });
		 * 
		 */
	}

	private static void withdrawReward(Player p, int slot) {
		if (slot < 0 || slot >= p.getRaidRewards().getItems().length)
			return;
		Item item = p.getRaidRewards().get(slot);
		if (item == null)
			return;
		if (item.move(item.getId(), item.getAmount(), p.getInventory()) > 0) {
			p.getRaidRewards().sendUpdates();
			p.addToCollectionLog(item);
		} else {
			p.sendMessage("Not enough space in your inventory.");
		}
	}

	private static void openRewards(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, Interface.RAID_REWARDS);
		player.getRaidRewards().sendUpdates();
		new Unlock(Interface.RAID_REWARDS, 5, 0, 2).unlockMultiple(player, 0, 9);
	}

	private static LootTable uniqueTable = new LootTable()
			.addTable(1,
					new LootItem(21034, 1, 10), // dexterous scroll
					new LootItem(21079, 1, 10), // arcane scroll
					new LootItem(24466, 1, 10), // Twisted Horns
					new LootItem(21000, 1, 7), // twisted buckler
					new LootItem(21012, 1, 7), // dragon hunter crossbow
					new LootItem(21015, 1, 5), // dinh's bulwark
					new LootItem(21018, 1, 5), // ancestral hat
					new LootItem(21021, 1, 5), // ancestral top
					new LootItem(21024, 1, 5), // ancestral bottom
					new LootItem(13652, 1, 5), // dragon claws
					new LootItem(21003, 1, 2), // elder maul
					new LootItem(21043, 1, 2), // kodai insignia
					new LootItem(20997, 1, 1) // twisted bow
			);

	private static LootTable uniqueKalTable = new LootTable()
			.addTable(1,
					new LootItem(21000, 1, 1), // twisted buckler
					new LootItem(21012, 1, 1), // dragon hunter crossbow
					new LootItem(21015, 1, 1), // dinh's bulwark
					new LootItem(21018, 1, 1), // ancestral hat
					new LootItem(21021, 1, 1), // ancestral top
					new LootItem(21024, 1, 1), // ancestral bottom
					new LootItem(13652, 1, 1), // dragon claws
					new LootItem(21003, 1, 1), // elder maul
					new LootItem(21043, 1, 1), // kodai insignia
					new LootItem(20997, 1, 1) // twisted bow
			);

	private static LootTable regularTable = new LootTable() // regular table. the "amount" here is the number used to
																													// determine the amount given to players based on how many
																													// points they have, for example 1 soul rune per 20 points
			.addTable(1,
					new LootItem(560, 20, 1), // death rune
					new LootItem(565, 16, 1), // blood rune
					new LootItem(566, 10, 1), // soul rune
					new LootItem(892, 7, 1), // rune arrow
					new LootItem(11212, 70, 1), // dragon arrow

					new LootItem(3050, 185, 1), // grimy toadflax
					new LootItem(208, 400, 1), // grimy ranarr weed
					new LootItem(210, 98, 1), // grimy irit
					new LootItem(212, 185, 1), // grimy avantoe
					new LootItem(214, 205, 1), // grimy kwuarm
					new LootItem(3052, 500, 1), // grimy snapdragon
					new LootItem(216, 200, 1), // grimy cadantine
					new LootItem(2486, 150, 1), // grimy lantadyme
					new LootItem(218, 106, 1), // grimy dwarf weed
					new LootItem(220, 428, 1), // grimy torstol

					new LootItem(443, 20, 1), // silver ore
					new LootItem(454, 20, 1), // coal
					new LootItem(445, 45, 1), // gold ore
					new LootItem(448, 45, 1), // mithril ore
					new LootItem(450, 100, 1), // adamantite ore
					new LootItem(452, 750, 1), // runite ore

					new LootItem(1624, 100, 1), // uncut sapphire
					new LootItem(1622, 170, 1), // uncut emerald
					new LootItem(1620, 125, 1), // uncut ruby
					new LootItem(1618, 260, 1), // uncut diamond

					// new LootItem(13391, 25, 1), // lizardman fang
					new LootItem(7937, 200, 1), // pure essence
					// new LootItem(13422, 24, 1), // saltpetre
					new LootItem(8781, 100, 1), // teak plank
					new LootItem(8783, 240, 1), // mahogany plank
					// new LootItem(13574, 55, 1), // dynamite

					new LootItem(21047, 131071, 1), // torn prayer scroll
					new LootItem(21027, 131071, 1) // dark relic

			);

	private static double getPetDonatorBoost(Player player) {
		switch (player.getSecondaryGroup()) {
			case SUPER_DONATOR -> {
				return 0.98;
			}
			case ELITE_DONATOR -> {
				return 0.96;
			}
			case NOBLE_DONATOR -> {
				return 0.94;
			}
			case GOLD_DONATOR -> {
				return 0.93;
			}
			case PLATINUM_DONATOR -> {
				return 0.92;
			}
			case LEGENDARY_DONATOR -> {
				return 0.91;
			}
			case SUPREME_DONATOR -> {
				return 0.90;
			}
		}
		return 1;
	}

	public static void giveRewards(ChambersOfXeric raid) {
		raid.getParty().forPlayers(p -> {
			p.getRaidRewards().clear();
			int points = VarPlayerRepository.RAIDS_PERSONAL_POINTS.get(p);
			float purpleChance = points / 1800.0f;
			if (points > 1000) {
				if (p.getPlayerPerkHandler().getActivePerks(p).contains(Perks.RAIDING_RESTORATIONS)) {
					int perkIndex = p.getPlayerPerkHandler().getActivePerkIndex(p, Perks.RAIDING_RESTORATIONS);
					RaidingRestorations c = (RaidingRestorations) p.getPlayerPerkHandler().getActivePerks(p).get(perkIndex)
							.getPerk(p);
					double multiplier = 1.0 + c.getLootChance();
					purpleChance *= multiplier;
				}
				if (p.getEquipment().get(Equipment.SLOT_RING) != null && AttributeExtensions
						.hasAttribute(p.getEquipment().get(Equipment.SLOT_RING), AttributeTypes.RAID_UNIQUE_CHARM)) {
					int level = AttributeExtensions.getCharges(AttributeTypes.RAID_UNIQUE_CHARM,
							p.getEquipment().get(Equipment.SLOT_RING));
					double multiplier = 1.0 + (level * 0.05);
					purpleChance *= multiplier;
				}
				if (purpleChance > 45)
					purpleChance = 45;

				if (Random.get(100) < purpleChance) {
					String message = p.getName() + " just received ";
					int basePetRate = 40;
					if (p.petDropBonus.isDelayed())
						basePetRate = 30;

					basePetRate *= getPetDonatorBoost(p);

					if (Random.get(1, basePetRate) == 1) {
						Pet.OLMLET.unlock(p, 0);
					}

					Item item = rollUnique();
					p.addToCollectionLog(item);
					p.getRaidRewards().add(item);
					Loggers.logRaidsUnique(p.getName(), item.getDef().name, p.chambersofXericKills.getKills());

					Broadcast.GLOBAL.sendNewsDropMessage(p, Icon.ADMINISTRATOR, p.getName(),
							" has just received " + Color.DARK_RED.wrap(item.getDef().name) + " from Chambers of Xeric!");
//					RareDropEmbedMessage.sendDiscordMessage(p, message, item.getDef().name + " from Chambers of Xeric",
//							item.getId(), p.chambersofXericKills.getKills());


					RareDropHook.sendDiscordMessage(() -> {
						var jsonObject = new JSONObject();
						jsonObject.put("player", p.getName());
						jsonObject.put("game_mode", p.getGameMode());
						jsonObject.put("item_id", item.getId());
						jsonObject.put("item_name", item.getDef().name);
						jsonObject.put("source", "Chambers of Xeric");
						jsonObject.put("total_attempts", Utils.formatMoneyString(p.chambersofXericKills.getKills()));
						return jsonObject;
					});

				} else {
					int playerPoints = Math.max(131071, VarPlayerRepository.RAIDS_PERSONAL_POINTS.get(p));
					if (playerPoints == 0)
						return;
					boolean bookRolled = false;
					for (int i = 0; i < 2; i++) {
						if (Random.get(100) == 0 && !bookRolled) {
							Item item = new Item(30548, 1);
							String message = p.getName() + " just received ";
							Broadcast.GLOBAL.sendNewsDropMessage(p, Icon.ADMINISTRATOR, p.getName(), " has just received "
									+ Color.DARK_RED.wrap(new Item(30548, 1).getDef().name) + " from Chambers of Xeric!");

							RareDropHook.sendDiscordMessage(() -> {
								var jsonObject = new JSONObject();
								jsonObject.put("player", p.getName());
								jsonObject.put("game_mode", p.getGameMode());
								jsonObject.put("item_id", item.getId());
								jsonObject.put("item_name", item.getDef().name);
								jsonObject.put("source", "Chambers of Xeric");
								jsonObject.put("total_attempts", Utils.formatMoneyString(p.chambersofXericKills.getKills()));
								return jsonObject;
							});

							p.addToCollectionLog(item);
							p.getRaidRewards().add(item);
							bookRolled = true;
						}
						Item rolled = rollRegular();
						double pointsPerItem = rolled.getAmount();
						int amount = (int) Math.ceil(playerPoints / pointsPerItem);
						rolled.setAmount(amount);
						if (amount > 1 && !rolled.getDef().stackable && !rolled.getDef().isNote())
							rolled.setId(rolled.getDef().notedId);
						p.addToCollectionLog(rolled);
						p.getRaidRewards().add(rolled);
					}
				}
			}
		});
	}

	private static Item rollRegular() {
		return regularTable.rollItem();
	}

	private static Item rollUnique() {
		return uniqueTable.rollItem();
	}

	private static Item rollKalUnique() {
		return uniqueKalTable.rollItem();
	}

	private static Player getPlayerToReceiveUnique(ChambersOfXeric raid, List<String> purpleRewarders) {
		int roll = Random.get(raid.getParty().getPoints());

		for (Player player : raid.getParty().getMembers()) {
			if (purpleRewarders.contains(player.getName()))
				continue;
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.RAIDING_RESTORATIONS)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.RAIDING_RESTORATIONS);
				RaidingRestorations c = (RaidingRestorations) player.getPlayerPerkHandler()
						.getActivePerks(player).get(perkIndex).getPerk(player);
				double chance = 1 - c.getLootChance();
				double modifiedRoll = roll * chance;
				modifiedRoll -= VarPlayerRepository.RAIDS_PERSONAL_POINTS.get(player);
				if (modifiedRoll <= 0) {
					return player;
				}
			}
		}

		return Random.get(raid.getParty().getMembers()); // shouldn't happen, but just in case
	}

}
