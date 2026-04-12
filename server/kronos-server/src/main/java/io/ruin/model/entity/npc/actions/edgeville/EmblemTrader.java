package io.ruin.model.entity.npc.actions.edgeville;


import io.ruin.cache.ObjType;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.SpawnListener;
import io.ruin.model.inter.dialogue.*;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.model.map.Bounds;
import io.ruin.model.shop.Shop;
import io.ruin.model.shop.ShopItem;
import io.ruin.model.shop.ShopManager;
import io.ruin.model.var.VarPlayerRepository;

import static io.ruin.cache.ItemID.*;

public class EmblemTrader {

	public static void register() {
		//Wilderness Emblem Trader
		NPCAction.register(308, "talk-to", (player, npc) -> {
			String currencyName = "coins";
			player.dialogue(new NPCDialogue(npc, "If you find an ancient emblem, totem, or statuette, use it on me in revenant caves and I'll exchange it for " + currencyName + "."));
		});
		NPCAction.register(308, "Coffer", (player, npc) -> {
			player.dialogue(new NPCDialogue(npc, "You have nothing in your coffer at this current time."));
		});
		int[][] ancientArtifacts = {
			{ ARCHAIC_EMBLEM_TIER_1, 500_000 },//Emblem
			{ ARCHAIC_EMBLEM_TIER_2, 500_000 },//Emblem
			{ ARCHAIC_EMBLEM_TIER_3, 500_000 },//Emblem
			{ ARCHAIC_EMBLEM_TIER_4, 500_000 },//Emblem
			{ ARCHAIC_EMBLEM_TIER_5, 500_000 },//Emblem
			{ ARCHAIC_EMBLEM_TIER_6, 500_000 },//Emblem
			{ ARCHAIC_EMBLEM_TIER_7, 500_000 },//Emblem
			{ ARCHAIC_EMBLEM_TIER_8, 500_000 },//Emblem
			{ ARCHAIC_EMBLEM_TIER_9, 500_000 },//Emblem
			{ ARCHAIC_EMBLEM_TIER_10, 500_000 },//Emblem

			{21807, 500_000},   //Emblem
			{21810, 1_000_000},  //Totem
			{21813, 2_000_000},  //Statuette
			{22299, 4_000_000},  //Medallion
			{22302, 8_000_000},  //Effigy
			{22305, 16_000_000}  //Relic
		};
		for (int[] artifact : ancientArtifacts) {
			int id = artifact[0];
			ObjType.get(id).sigmundBuyPrice = artifact[1];
			ItemNPCAction.register(id, 308, (player, item, npc) -> player.dialogue(new YesNoDialogue("Are you sure you want to do this?", "Sell your " + ObjType.get(id).name +
				" for " + artifact[1] + " coins?", item, () -> {
				item.remove();
				player.getInventory().add(COINS_995, artifact[1]);
				player.dialogue(new NPCDialogue(npc, "Excellent find, " + player.getName() + "! If you find anymore artifacts you know where to find me!"));
			})));
		}
		//Edgeville Emblem Trader
		ItemNPCAction.register(BLOOD_FRAGMENT, 308, (player, item, npc) -> player.dialogue(new MessageDialogue("<col=ff0000>Warning:</col> you are about to swap all your blood fragments for " +
				"blood money. Are you sure you want to continue?").lineHeight(25),
			new OptionsDialogue(
				new Option("Yes", () -> item.setId(BLOOD_MONEY)),
				new Option("No")
			)));
		ItemNPCAction.register(BLOOD_MONEY, 308, (player, item, npc) -> player.dialogue(new MessageDialogue("<col=ff0000>Warning:</col> you are about to swap all your blood money for " +
				"coins at a 1:10k ratio. Are you sure you want to continue?").lineHeight(25),
			new OptionsDialogue(
				new Option("Yes", () -> fromBloodMoneyToPlatinum(player, item)),
				new Option("No")
			)));
		ItemNPCAction.register(COINS_995, 308, (player, item, npc) -> player.dialogue(new MessageDialogue("<col=ff0000>Warning:</col> you are about to swap all your coins for " +
				"blood money at a 25k:1 ratio. Are you sure you want to continue?").lineHeight(25),
			new OptionsDialogue(
				new Option("Yes", () -> fromCoinsToBloodTokens(player, item)),
				new Option("No")
			)));
		NPCAction.register(308, "talk-to", (player, npc) -> {
			String currencyName = "coins";
			player.dialogue(new NPCDialogue(npc, "If you find an ancient emblem, totem, or statuette, use it on me in revenant caves and I'll exchange it for " + currencyName + "."));
		});
		NPCAction.register(308, "Skull", (player, npc) -> skull(player));
		NPCAction.register(308, "Rewards", (player, npc) -> ShopManager.openIfExists(player, "PkStore"));
		NPCAction.register(308, "reset-kdr", (player, npc) -> player.dialogue(
			new MessageDialogue("<col=ff0000>Warning:</col> You are about to reset your kills & deaths. All " +
				"statistics related to kills will also be reset. Are you sure you want to continue?").lineHeight(25),
			new OptionsDialogue(
				new Option("Yes", () -> {
					VarPlayerRepository.PVP_KILLS.set(player, 0);
					VarPlayerRepository.PVP_DEATHS.set(player, 0);
					player.currentKillSpree = 0;
					player.highestKillSpree = 0;
					player.highestShutdown = 0;
					player.dialogue(new NPCDialogue(npc, "Your kills, deaths, sprees and highest shutdown has been reset."));
				}),
				new Option("No")
			)
		));
		SpawnListener.forEach(npc -> {
			if (npc.getId() == 308 && npc.walkBounds != null)
				npc.walkBounds = new Bounds(3099, 3518, 3092, 3516, 0);
		});


	}


	public static final String SHOP_UUID = "";//TODO fill this out

	public static int getResellPrices(Item itemSelling) {
		if (itemSelling.getDef().id == 21034 || itemSelling.getDef().id == 21079)
			return 0;
		Shop shop = ShopManager.getByUUID(SHOP_UUID);
		if (shop == null)
			return 0;
		for (ShopItem item : shop.defaultStock) {
			int price;
			ObjType def = item.getDef();
			if (def.id == itemSelling.getDef().id) {
				price = +item.price;
				return price / 4;
			}
		}
		return 0;
	}

	public static void skull(Player player) {
		player.dialogue(new OptionsDialogue(
			new Option("<img=46> Regular <img=46>", () -> player.getCombat().skullNormal()),
			new Option("No Skull", () -> player.getCombat().resetSkull())
		));
	}

	private static void fromBloodMoneyToPlatinum(Player player, Item bloodMoney) {
		player.dialogue(
			new OptionsDialogue("Exchange your bloody money for platinum tokens?",
				new Option("Yes", () -> {
					if (bloodMoney.getAmount() > 100_000) {
						player.dialogue(new MessageDialogue("You can only convert 100k blood money at a time."));
						return;
					}
					long tokensAmount = bloodMoney.getAmount() * 10000 / 1000;
					Item tokens = player.getInventory().findItem(PLATINUM_TOKEN);
					if (tokens != null) {
						bloodMoney.incrementAmount(-tokensAmount);
						tokens.incrementAmount(tokensAmount);
					} else {
						int freeSlots = player.getInventory().getFreeSlots();
						if (tokensAmount == bloodMoney.getAmount())
							freeSlots++;
						if (freeSlots == 0) {
							player.dialogue(new MessageDialogue("You don't have enough inventory space."));
							return;
						}
						bloodMoney.incrementAmount(-tokensAmount);
						player.getInventory().add(PLATINUM_TOKEN, Math.toIntExact(tokensAmount));
					}
					player.dialogue(new ItemDialogue().two(BLOOD_MONEY, PLATINUM_TOKEN, "The bank exchanges your blood money for platinum tokens."));
				}),
				new Option("No", player::closeDialogue)
			)
		);
	}

	private static void fromCoinsToBloodTokens(Player player, Item coins) {
		player.dialogue(
			new OptionsDialogue("Exchange your coins for bloody tokens?",
				new Option("Yes", () -> {
					if (coins.getAmount() < 25_000_000) {
						player.dialogue(new MessageDialogue("You must be converting at least 25M coins."));
						return;
					}
					long tokensAmount = coins.getAmount() / 25000 / 1000;
					int removeAmount = coins.getAmount();
					Item tokens = player.getInventory().findItem(24719);
					if (tokens != null) {
						coins.incrementAmount(-removeAmount);
						tokens.incrementAmount(tokensAmount);
					} else {
						int freeSlots = player.getInventory().getFreeSlots();
						if (removeAmount == coins.getAmount())
							freeSlots++;
						if (freeSlots == 0) {
							player.dialogue(new MessageDialogue("You don't have enough inventory space."));
							return;
						}
						coins.incrementAmount(-removeAmount);
						player.getInventory().add(24719, Math.toIntExact(tokensAmount));
					}
					player.dialogue(new ItemDialogue().two(COINS_995, 24719, "The bank exchanges your coins for bloody tokens."));
				}),
				new Option("No", player::closeDialogue)
			)
		);
	}

}
