package io.ruin.model.content.itembreaking;

import discord.webhooks.logs.ItemPerkHook;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.utility.Broadcast;
import lombok.Getter;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

@Getter
public enum ItemBreakPerks {
	QUICK_STEP("Quick Step", AttributeTypes.QUICK_STEP, 1, 10, 25, 45, 75, true, Arrays.asList(new Item(ItemBreakingHandler.DULL_MINERAL, 65), new Item(ItemBreakingHandler.OLD_ENHANCER, 3)), Equipment.SLOT_FEET),
	PRECISION_STRIKE("Precision Strike", AttributeTypes.PRECISION_STRIKE, 2, 5, 12, 32, 50, true, Arrays.asList(new Item(ItemBreakingHandler.DULL_MINERAL, 105), new Item(ItemBreakingHandler.SHINY_MINERAL, 25), new Item(ItemBreakingHandler.MODERN_ENHANCER, 2)), Equipment.SLOT_WEAPON),
	FULLY_LOADED("Fully Loaded", AttributeTypes.FULLY_LOADED, 1, 10, 18, 33, 60, false, Arrays.asList(new Item(ItemBreakingHandler.DULL_MINERAL, 75), new Item(ItemBreakingHandler.OLD_ENHANCER, 1)), Equipment.SLOT_AMMO),
	CLUE_HUNTER("Clue Hunter", AttributeTypes.CLUE_HUNTER, 2, 10, 20, 40, 70, true, Arrays.asList(new Item(ItemBreakingHandler.DULL_MINERAL, 95), new Item(ItemBreakingHandler.SHINY_MINERAL, 25), new Item(ItemBreakingHandler.OLD_ENHANCER, 3)), Equipment.SLOT_RING),
	ELEMENTAL_FURY("Elemental Fury", AttributeTypes.ELEMENTAL_FURY, 1, 10, 25, 45, 70, true, Arrays.asList(new Item(ItemBreakingHandler.DULL_MINERAL, 75), new Item(ItemBreakingHandler.SHINY_MINERAL, 15), new Item(ItemBreakingHandler.MODERN_ENHANCER, 3)), Equipment.SLOT_WEAPON),
	BURNING_WRATH("Burning Wrath", AttributeTypes.BURNING_WRATH, 3, 8, 18, 36, 60, true, Arrays.asList(new Item(ItemBreakingHandler.DULL_MINERAL, 100), new Item(ItemBreakingHandler.SHINY_MINERAL, 60), new Item(ItemBreakingHandler.MODERN_ENHANCER, 5)), Equipment.SLOT_WEAPON),
	ELEMENTAL_CONVERGENCE("Elemental Convergence", AttributeTypes.ELEMENTAL_CONVERGENCE, 2, 10, 25, 45, 70, true, Arrays.asList(new Item(ItemBreakingHandler.DULL_MINERAL, 100), new Item(ItemBreakingHandler.SHINY_MINERAL, 50), new Item(ItemBreakingHandler.OLD_ENHANCER, 4)), Equipment.SLOT_WEAPON),
	SOUL_REAVER("Soul Reaver", AttributeTypes.SOUL_REAVER, 4, 5, 12, 25, 50, true, Arrays.asList(new Item(ItemBreakingHandler.SHINY_MINERAL, 100), new Item(ItemBreakingHandler.MODERN_ENHANCER, 3)), Equipment.SLOT_WEAPON),
	IMMORTAL_FORTITUDE("Immortal Fortitude", AttributeTypes.IMMORTAL_FORTITUDE, 3, 8, 18, 35, 65, true, Arrays.asList(new Item(ItemBreakingHandler.SHINY_MINERAL, 40), new Item(ItemBreakingHandler.MODERN_ENHANCER, 2)), Equipment.SLOT_HAT, Equipment.SLOT_CHEST, Equipment.SLOT_LEGS),
	ETERNAL_REGENERATION("Eternal Regeneration", AttributeTypes.ETERNAL_REGENERATION, 2, 10, 25, 50, 80, true, Arrays.asList(new Item(ItemBreakingHandler.DULL_MINERAL, 80), new Item(ItemBreakingHandler.SHINY_MINERAL, 50), new Item(ItemBreakingHandler.MODERN_ENHANCER, 2)), Equipment.SLOT_RING),
	BERSERKER_RAGE("Berserker Rage", AttributeTypes.BERSERKER_RAGE, 4, 3, 9, 18, 40, true, Arrays.asList(new Item(ItemBreakingHandler.SHINY_MINERAL, 50), new Item(ItemBreakingHandler.GLISTENING_MINERAL, 30), new Item(ItemBreakingHandler.MODERN_ENHANCER, 5)), Equipment.SLOT_WEAPON),
	ETERNAL_RESILIENCE("Eternal Resilience", AttributeTypes.ETERNAL_RESILIENCE, 3, 8, 18, 35, 70, true, Arrays.asList(new Item(ItemBreakingHandler.DULL_MINERAL, 150), new Item(ItemBreakingHandler.SHINY_MINERAL, 45), new Item(ItemBreakingHandler.MODERN_ENHANCER, 2), new Item(ItemBreakingHandler.MODERN_ENHANCER, 1)), Equipment.SLOT_SHIELD),
	TIME_MANIPULATOR("Time Manipulator", AttributeTypes.TIME_MANIPULATOR, 5, 3, 8, 18, 45, true, Arrays.asList(new Item(ItemBreakingHandler.SHINY_MINERAL, 150), new Item(ItemBreakingHandler.GLISTENING_MINERAL, 25), new Item(ItemBreakingHandler.MODERN_ENHANCER, 3), new Item(ItemBreakingHandler.INNOVATIVE_ENHANCER, 1)), Equipment.SLOT_AMULET, Equipment.SLOT_RING),
	JELLIFIED("Jellified", AttributeTypes.JELLIFIED, 5, 5, 12, 25, 45, true, Arrays.asList(new Item(ItemBreakingHandler.SHINY_MINERAL, 80), new Item(ItemBreakingHandler.GLISTENING_MINERAL, 45), new Item(ItemBreakingHandler.MODERN_ENHANCER, 5), new Item(ItemBreakingHandler.INNOVATIVE_ENHANCER, 1)), Equipment.SLOT_LEGS, Equipment.SLOT_CHEST, Equipment.SLOT_HAT),
	BERSERKER("Berserker", AttributeTypes.BERSERKER, 5, 3, 9, 20, 50, true, Arrays.asList(new Item(ItemBreakingHandler.SHINY_MINERAL, 50), new Item(ItemBreakingHandler.GLISTENING_MINERAL, 60), new Item(ItemBreakingHandler.INNOVATIVE_ENHANCER, 3)), Equipment.SLOT_WEAPON),
	SPECTRAL_GUARDIAN("Spectral Guardian", AttributeTypes.SPECTRAL_GUARDIAN, 5, 2, 7, 18, 40, true, Arrays.asList(new Item(ItemBreakingHandler.SHINY_MINERAL, 80), new Item(ItemBreakingHandler.GLISTENING_MINERAL, 60), new Item(ItemBreakingHandler.INNOVATIVE_ENHANCER, 2)), Equipment.SLOT_AMULET),
	ENHANCED_SOAKING("Enhanced Soak", AttributeTypes.ENHANCED_SOAK, 5, 3, 11, 25, 40, true, Arrays.asList(new Item(ItemBreakingHandler.GLISTENING_MINERAL, 70), new Item(ItemBreakingHandler.INNOVATIVE_ENHANCER, 2)), Equipment.SLOT_HAT, Equipment.SLOT_CHEST, Equipment.SLOT_LEGS),
	TREASURE_HUNTER("Treasure Hunter", AttributeTypes.TREASURE_HUNTER, 5, 3, 12, 35, 55, true, Arrays.asList(new Item(ItemBreakingHandler.SHINY_MINERAL, 150), new Item(ItemBreakingHandler.GLISTENING_MINERAL, 50), new Item(ItemBreakingHandler.INNOVATIVE_ENHANCER, 2)), Equipment.SLOT_RING),
	RAID_UNIQUE_CHARM("Raid Unique Charm", AttributeTypes.RAID_UNIQUE_CHARM, 5, 3, 13, 32, 55, true, Arrays.asList(new Item(ItemBreakingHandler.GLISTENING_MINERAL, 95), new Item(ItemBreakingHandler.INNOVATIVE_ENHANCER, 5)), Equipment.SLOT_RING),
	//WEATHER_WIZARD("Weather Wizard", AttributeTypes.WEATHER_WIZARD, 6, 2, 8, 35, 50, true, Arrays.asList(new Item(ItemBreakingHandler.GLISTENING_MINERAL, 250), new Item(ItemBreakingHandler.INNOVATIVE_ENHANCER, 5)), Equipment.SLOT_WEAPON),
	//DRAGON_WRATH("Dragon Wrath", AttributeTypes.DRAGON_WRATH, 6, 2, 8, 35, 50, true, Arrays.asList(new Item(ItemBreakingHandler.GLISTENING_MINERAL, 250), new Item(ItemBreakingHandler.INNOVATIVE_ENHANCER, 5)), Equipment.SLOT_WEAPON);
	;
	final String name;
	final public AttributeTypes perk;
	final int tier;
	final int level5Chance;
	final int level4Chance;
	final int level3Chance;
	final int level2Chance;
	final boolean specificSlot;
	final int[] slots;
	final List<Item> cost;

	ItemBreakPerks(String name, AttributeTypes perk, int tier, int level5Chance, int level4Chance, int level3Chance, int level2Chance, boolean specificSlot, List<Item> cost, int... slot) {
		this.name = name;
		this.perk = perk;
		this.tier = tier;
		this.level5Chance = level5Chance;
		this.level4Chance = level4Chance;
		this.level3Chance = level3Chance;
		this.level2Chance = level2Chance;
		this.specificSlot = specificSlot;
		this.slots = slot;
		this.cost = cost;
	}

	public static final ItemBreakPerks[] VALUES = values();

	public static String getDescription(ItemBreakPerks perk) {
		return switch (perk) {
			case QUICK_STEP -> """
				    When this perk is active in your feet slot, you have a chance to gain Agility experience whenever you gain experience in any skill. For example, you could be training Combat and receive Agility experience.
				    The chance of gaining Agility experience is based on the level of the perk: 1/20 - (level * 3) so level 5 is 1/5.
				    The Agility experience earned is (0.1 + (level * 0.1)) times the experience gained.
				""";
			case PRECISION_STRIKE -> """
				    When this perk is active on your weapon, you have a chance to strike your target, ignoring their defense and dealing a minimum damage based on your max hit and the level of the perk.
				    The chance of ignoring defense is calculated as: 1/20 - (level * 2) so level 5 is 1/10.
				    The minimum hit value is (0.02 * level) times your max hit.
				""";
			case FULLY_LOADED -> """
				    When this perk is active on an item that uses charges, you have a chance to save the charge from being used.
				    The chance to save a charge is 5% - (level * 1%). At level 5, you will have a 100% chance to save the charge, making the item effectively unlimited.
				""";
			case CLUE_HUNTER -> """
				    When this perk is active in your ring slot, you have an increased chance to receive clue scrolls from monsters. Additionally, you will automatically pick up clue scrolls if you have inventory space.
				    Each level provides a 10% boost to the chance of receiving a clue scroll.
				""";
			case ELEMENTAL_FURY -> """
				    When this perk is active on your weapon, you deal extra damage with elemental spells. The extra damage is increased by 5% per level.
				""";
			case BURNING_WRATH -> """
				    When this perk is active on your weapon, you have a chance to activate a burn effect that deals damage over time to the target. The duration and damage depend on the perk level.
				    The chance to activate is calculated as: 15% - (level * 1.5%).
				    The target will take burn damage for 3 times the level, with each burn dealing (1-4) times the level in damage.
				""";
			case ELEMENTAL_CONVERGENCE -> """
				    When this perk is active on your weapon and you use elemental spells, you have a chance to cast an additional spell at the target.
				    The chance of this occurring is: 10% - level. At level 5, there is a 1 in 5 chance of a double cast.
				""";
			case SOUL_REAVER -> """
				    This perk, when attached to a weapon, gives you a chance to deal a hit based on the target’s current HP. The hit deals 2% multiplied by the perk level, so at level 5, it deals 10% of the target's current HP.
				    The chance of this hit occurring is: 25% - (level * 2%).
				""";
			case IMMORTAL_FORTITUDE -> """
				    When this perk is active and your health drops below 25%, you will absorb a portion of all incoming damage. The damage absorbed is 10% multiplied by the perk level, so at level 5, you absorb 50% of incoming damage.
				""";
			case ETERNAL_REGENERATION -> """
				    When this perk is active, your hitpoints regeneration rate is divided by the perk level. At level 5, you regenerate health 5 times faster than usual.
				""";
			case BERSERKER_RAGE -> """
				    When this perk is active on a weapon and your health drops below 50%, you enter a berserker rage for (16 * level) ticks, so 80 ticks at level 5.
				    During this time, you attack twice as fast and gain a 200% accuracy boost and a damage boost of 10% multiplied by the perk level (50% at level 5).
				    After activation, there is a cooldown of (500 - (50 * level)) ticks.
				""";
			case ETERNAL_RESILIENCE -> """
				    When this perk is active, you have a chance to activate a resilience effect that absorbs 50% of all incoming damage. The duration of the effect is (8 * level) ticks, so 40 ticks at level 5.
				    After activation, there is a cooldown of (500 / level) ticks, so 100 ticks at level 5.
				""";
			case TIME_MANIPULATOR -> """
				    When this perk is active, you have a chance to slow down time for your target. The target's attack speed is multiplied by (50% * level), so at level 5, the target is slowed by 250%.
				    The chance of this effect occurring is: 30% - (level * 1.5%).
				    Once activated, there is a cooldown of (200 - (level * 15)) ticks.
				""";
			case JELLIFIED -> """
				    When this perk is active, you have a chance to heal instead of taking damage. If a 70 damage hit was incoming and this effect triggered, you would be healed for 70 instead.
				    The chance of this effect occurring is: 1/20 - (level * 2) so level 5 is 1/10.
				""";
			case BERSERKER -> """
				    When this perk is active, you are guaranteed to hit your maximum hit on your target if the target is at full health. Additionally, you gain a damage boost of 0.1% per missing hitpoint multiplied by the perk level, so 1% per missing hitpoint at level 5.
				""";
			case SPECTRAL_GUARDIAN -> """
				    When this perk is active, you have a chance to summon a spectral guardian that heals players in the area. The guardian heals for (5 * level), so 25 at level 5.
				    The chance to summon the guardian is: 150% - (level * 10%).
				""";
			case ENHANCED_SOAKING -> """
				    When this perk is active, you receive a 1% damage soak per level, so 5% per level 5.
				""";
			case TREASURE_HUNTER -> """
				    When this perk is active, you receive a 1% increase in drop rate per level of the perk.
				""";
			case RAID_UNIQUE_CHARM -> """
				    When this perk is active, you receive a 5% boost to the chance of getting a unique item in raids multiplied by the level. At level 5, you have a 25% boost.
				""";
			/*case WEATHER_WIZARD -> """
				    When this perk is active, you have a chance to summon a tornado that deals damage to any NPC it contacts. The tornado's duration and damage depend on the perk level.
				    The chance to summon the tornado is: 60% - (level * 10%). The tornado stays for (8 + (level * 4)) ticks, with a cooldown of (500 - (level * 50)) ticks.
				""";
			case DRAGON_WRATH -> """
				    When this perk is active, you have a chance to activate a dragon breath attack that hits in a cone shape towards your target. The size and damage of the attack depend on the perk level.
				    Any target in the breath’s path takes damage between (level * 22) and (level * 45). There is a cooldown of (500 - (level * 50)) ticks.
				""";

			 */
		};
	}

	public static int getChance(Player player, int baseChance, List<Item> minimumCost, List<Item> totalPaid) {
		for (int i = 0; i < minimumCost.size(); i++) {
			if (totalPaid.get(i).getAmount() < minimumCost.get(i).getAmount()) {
				return 0;
			}
		}
		var minimumMinerals = minimumCost.stream()
			.filter(minItem -> minItem.getDef().name.toLowerCase().contains("mineral"))
			.mapToInt(Item::getAmount)
			.sum();

		var paidMinerals = totalPaid.stream()
			.filter(paidItem -> paidItem.getDef().name.toLowerCase().contains("mineral"))
			.mapToInt(Item::getAmount)
			.sum();

		double mineralContribution = (double) paidMinerals / minimumMinerals;
		int chance = baseChance *= mineralContribution;

		int finalChance = Math.min(baseChance * 3, (int) chance);
		finalChance *= 0.9;

		finalChance *= donatorBoost(player);


		return finalChance;
	}

	private static double donatorBoost(Player player) {
		return switch (player.getSecondaryGroup()) {
			case DONATOR -> 1.01;
			case SUPER_DONATOR -> 1.02;
			case ELITE_DONATOR -> 1.03;
			case NOBLE_DONATOR -> 1.04;
			case GOLD_DONATOR -> 1.05;
			case PLATINUM_DONATOR -> 1.06;
			case LEGENDARY_DONATOR -> 1.08;
			case SUPREME_DONATOR -> 1.1;
			default -> 1;
		};
	}


	public static List<ItemBreakPerks> getAvailablePerks(Item item) {
		return Arrays.stream(VALUES)
			.filter(perk -> {
				if (perk.specificSlot) {
					for (int slot : perk.slots) {
						if (slot == item.getDef().equipSlot) {
							return true;
						}
					}
					return false;
				}
				return true;
			})
			.toList();
	}



	public static void upgradeItem(Player player, Item selectedItem, ItemBreakPerks perk, int level5Chance, int level4Chance, int level3Chance, int level2Chance) {
		int level;
		int roll = Random.get(1, 100);
		if (roll <= (World.isLive() ? level5Chance : 150)) {
			level = 5;
		} else if (roll <= level4Chance) {
			level = 4;
		} else if (roll <= level3Chance) {
			level = 3;
		} else if (roll <= level2Chance) {
			level = 2;
		} else {
			level = 1;
		}
		if (level == 5) {
			Broadcast.GLOBAL.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " has upgraded their " + perk.name + " to level 5 on their " + selectedItem.getDef().name + "!");
//			ItemPerkWebhook.sendTierFiveUpgrade(player, selectedItem, perk);

			ItemPerkHook.sendTierFiveUpgrade(
				new JSONObject()
					.put("player", player.getName())
					.put("perk_name", perk.name)
					.put("item_name", selectedItem.getDef().name)
			);
		}
		if (AttributeExtensions.hasAttribute(selectedItem, perk.perk)) {
			if (AttributeExtensions.getCharges(perk.perk, selectedItem) >= level) {
				player.sendMessage("You rolled a level lower than your current level so you kept your current level.");
				return;
			}
		}
		player.sendMessage(Color.DARK_GREEN.wrap("You have upgraded your " + selectedItem.getDef().name + " to level " + level + " " + perk.name + "!"));
		AttributeExtensions.setCharges(perk.perk, selectedItem, level);
	}

	public static void upgradeItem(Player player, Item selectedItem, ItemBreakPerks perk, int level) {
		player.sendMessage(Color.DARK_GREEN.wrap("You have upgraded your " + selectedItem.getDef().name + " to level " + level + " " + perk.name + "!"));
		AttributeExtensions.setCharges(perk.perk, selectedItem, level);
	}

	public static boolean canUpgrade(Player player, ItemBreakPerks perk) {
		for (Item costItem : perk.cost) {
			int costAmount = costItem.getAmount();
			if(player.getInventory().contains(26986)) {
				if(player.mineralBagItems.containsKey(costItem.getId())) {
					int amount = player.mineralBagItems.get(costItem.getId());
					if(amount >= costAmount)
						continue;
					else costAmount -= amount;
				}
			}
			if (!player.getInventory().contains2(costItem.getId(), costAmount)) {
				player.sendMessage("You need " + costItem.getAmount() + " " + costItem.getDef().name + " to upgrade this item.");
				return false;
			}
		}
		return true;
	}
}

