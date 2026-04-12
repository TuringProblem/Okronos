package io.ruin.model.content.itembreaking;

import discord.webhooks.logs.ItemBreakHook;
import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.ObjType;
import io.ruin.model.content.combatachievements.CombatAchievementSystem;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeExtensions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemBreakingHandler {
	public static final int DULL_MINERAL = 21341;
	public static final int OLD_ENHANCER = 21768;
	public static final int SHINY_MINERAL = 21533;
	public static final int MODERN_ENHANCER = 27607;
	public static final int GLISTENING_MINERAL = 21535;
	public static final int INNOVATIVE_ENHANCER = 6653;

	public static void handleItemBreaking(Player player, Item itemToBreak) {
		BreakItems breakItems = BreakItems.forId(itemToBreak.getId());
		if (breakItems == null) {
			return;
		}

		player.sendMessage("You successfully broke down your item" + (itemToBreak.getAmount() > 1 ? "s into minerals." : " into minerals."));
		List<Item> items = new ArrayList<>();
		for (int i = 0; i < itemToBreak.getAmount(); i++) {
			int tier = breakItems.tier;
			boolean dropShinyMineral = Random.get(100) <= getShinyMineralChance(tier);
			boolean dropModernEnhancer = Random.get(100) <= getModernEnhancerChance(tier);
			boolean dropGlisteningMineral = Random.get(100) <= getGlisteningMineralChance(tier);
			boolean dropInnovativeEnhancer = Random.get(100) <= getInnovativeEnhancerChance(tier);
			if (dropShinyMineral) {
				items.add(new Item(SHINY_MINERAL, Random.get(getShinyMineralMinAmount(player, tier), getShinyMineralMaxAmount(player, tier))));
			}
			if (dropModernEnhancer) {
				items.add(new Item(MODERN_ENHANCER, Random.get(getModernEnhancerMinAmount(player, tier), getModernEnhancerMaxAmount(player, tier))));
			}
			if (dropGlisteningMineral) {
				items.add(new Item(GLISTENING_MINERAL, Random.get(getGlisteningMineralMinAmount(player, tier), getGlisteningMineralMaxAmount(player, tier))));
			}
			if (dropInnovativeEnhancer) {
				items.add(new Item(INNOVATIVE_ENHANCER, Random.get(getInnovativeEnhancerMinAmount(player, tier), getInnovativeEnhancerMaxAmount(player, tier))));
			}
			items.add(new Item(DULL_MINERAL, Random.get(getDullMineralMinAmount(player, tier), getDullMineralMaxAmount(player, tier))));
			items.add(new Item(OLD_ENHANCER, Random.get(getOldEnhancerMinAmount(player, tier), getOldEnhancerMaxAmount(player, tier))));
			itemToBreak.remove();
		}

		var minerals = new JSONArray();

		for (Item item : items) {
			if (player.getInventory().hasRoomFor(item.getId(), item.getAmount())) {
				player.getInventory().add(item.getId(), item.getAmount());
			} else {
				player.getBank().add(item.getId(), item.getAmount());
				player.sendMessage(item.getAmount() + "x " + item.getDef().name + " has been sent to your bank.");
			}
			minerals.put(new JSONObject()
				.put("item_name", item.getDef().name)
				.put("item_id", item.getId())
				.put("item_amount", NumberUtils.formatNumber(item.getAmount()))
			);
		}
//		ItemPerkWebhook.sendItemBreakdown(player, itemToBreak, items);

		var dto = new JSONObject()
			.put("player", player.getName())
			.put("item_name", ObjType.get(breakItems.itemId).getName())
			.put("item_id", breakItems.itemId)
			.put("minerals", minerals);

		ItemBreakHook.sendItemBreakMessage(dto);
	}

	public static void removeItemPerk(Player player, Item item) {
		List<ItemBreakPerks> perks = getTotalAttachments(item);
		if (perks.isEmpty()) {
			player.sendMessage("This item does not have any perks to remove.");
			return;
		}
		List<Option> options = new ArrayList<>();
		for (int i = 0; i < perks.size(); i++) {
			final int index = i;
			options.add(new Option(perks.get(i).name, () -> {
				AttributeExtensions.clearCharges(perks.get(index).perk, item);
				player.sendMessage("You have successfully removed the " + perks.get(index).name + " perk from your " + item.getDef().name + ".");
			}));
		}
		player.dialogue(new OptionsDialogue("Which perk would you like to remove from your " + item.getDef().name + "?",
			options.toArray(new Option[0])
		));
	}

	public static List<ItemBreakPerks> getTotalAttachments(Item item) {
		List<ItemBreakPerks> total = new ArrayList<>();
		for (ItemBreakPerks perk : ItemBreakPerks.VALUES) {
			if (AttributeExtensions.hasAttribute(item, perk.perk)) {
				total.add(perk);
			}
		}
		return total;
	}

	public static int getShinyMineralChance(int tier) {
		if (tier < 1) return 0;
		if (tier < 2)
			return 2;
		double baseChance = 0.15;
		double growthRate = 2.8;
		int chance = (int) (baseChance * Math.pow(growthRate, tier - 1) * 100);
		return Math.min(chance, 100);
	}

	public static int getModernEnhancerChance(int tier) {
		double baseChance = 0.07;
		double growthRate = 2.2;
		return calculateChance(tier, baseChance, growthRate);
	}

	public static int getGlisteningMineralChance(int tier) {
		if (tier < 2) return 0;
		double baseChance = 0.05;
		double growthRate = 1.9;
		int chance = (int) (baseChance * Math.pow(growthRate, tier - 1) * 100);
		return Math.min(chance, 100);
	}

	private static int calculateChance(int tier, double baseChance, double growthRate) {
		if (tier < 2) return 0;
		double chance = baseChance * Math.pow(growthRate, tier - 1);
		return Math.min((int) (chance * 100), 100);
	}

	public static int getInnovativeEnhancerChance(int tier) {
		double baseChance = 0.07;
		double growthRate = 1.6;
		return calculateChance(tier, baseChance, growthRate);
	}

	static int getInnovativeEnhancerMaxAmount(Player player, int tier) {
		return calculateAmount(player, tier, 1, 1.4);
	}

	static int getInnovativeEnhancerMinAmount(Player player, int tier) {
		return calculateAmount(player, tier, 1, 1.2);
	}

	static int getGlisteningMineralMaxAmount(Player player, int tier) {
		return calculateAmount(player, tier, 6, 1.65);
	}

	static int getGlisteningMineralMinAmount(Player player, int tier) {
		return calculateAmount(player, tier, 5, 1.7);
	}

	static int getModernEnhancerMaxAmount(Player player, int tier) {
		return calculateAmount(player, tier, 1, 1.65);
	}

	static int getModernEnhancerMinAmount(Player player, int tier) {
		return calculateAmount(player, tier, 1, 1.2);
	}

	static int getShinyMineralMaxAmount(Player player, int tier) {
		return calculateAmount(player, tier, 7, 1.9);
	}

	static int getShinyMineralMinAmount(Player player, int tier) {
		return calculateAmount(player, tier, 6, 1.8);
	}

	static int getOldEnhancerMaxAmount(Player player, int tier) {
		return calculateAmount(player, tier, 2, 3.0);
	}

	static int getOldEnhancerMinAmount(Player player, int tier) {
		return calculateAmount(player, tier, 1, 2.8);
	}

	static int getDullMineralMaxAmount(Player player, int tier) {
		return calculateAmount(player, tier, 10, 2.2);
	}

	static int getDullMineralMinAmount(Player player, int tier) {
		return calculateAmount(player, tier, 11, 2.3);
	}

	private static int calculateAmount(Player player, int tier, double baseAmount, double growthFactor) {
		if (tier < 1) return 0;
		var growth = Math.pow(growthFactor, tier - 1);
		var base = Math.round(baseAmount * growth);
		var modifier = donatorAmountBoost(player);
		int amount = (int) (base * modifier);
		switch (CombatAchievementSystem.getTier(player.combatAchievementPoints)) {
			case ELITE -> amount = (int) (amount * 1.03);
			case MASTER -> amount = (int) (amount * 1.05);
			case GRANDMASTER -> amount = (int) (amount * 1.1);
		}
		return amount;
	}

	private static double donatorAmountBoost(Player player) {
		return switch (player.getSecondaryGroup()) {
			case DONATOR -> 1.02;
			case SUPER_DONATOR -> 1.05;
			case ELITE_DONATOR -> 1.08;
			case NOBLE_DONATOR -> 1.1;
			case GOLD_DONATOR -> 1.12;
			case PLATINUM_DONATOR -> 1.15;
			case LEGENDARY_DONATOR ->1.18;
			case SUPREME_DONATOR -> 1.2;
			default -> 1.0;
		};
	}

}
