package io.ruin.model.item.actions.impl;

import io.ruin.api.utils.Random;
import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.activities.duelarena.DuelRule;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.*;
import io.ruin.model.activities.raids.toa.Invocations;
import io.ruin.model.activities.raids.toa.TombsOfAmascutManager;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Widget;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.skills.herblore.Potion;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.Misc;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import static io.ruin.model.entity.shared.LockType.FULL_ALLOW_EAT;

public class Consumable {


	/**
	 * Eating
	 */

	public static void register() {
		registerPotion(PotionDrink.SUPER_RESTORE, p -> restore(p, true));
		registerPotion(PotionDrink.ATTACK, p -> p.getStats().get(StatType.Attack).boost(3, 0.10));
		registerPotion(PotionDrink.EGNIOL_POTION, Consumable::egniolPotion);
		registerPotion(PotionDrink.STRENGTH, p -> p.getStats().get(StatType.Strength).boost(3, 0.10));
		registerPotion(PotionDrink.DEFENCE, p -> p.getStats().get(StatType.Defence).boost(3, 0.10));
		registerPotion(PotionDrink.COMBAT, p -> {
			p.getStats().get(StatType.Attack).boost(3, 0.10);
			p.getStats().get(StatType.Strength).boost(3, 0.10);
		});

		registerPotion(PotionDrink.SUPER_ATTACK, p -> p.getStats().get(StatType.Attack).boost(5, 0.15));
		registerPotion(PotionDrink.SUPER_STRENGTH, p -> p.getStats().get(StatType.Strength).boost(5, 0.15));
		registerPotion(PotionDrink.SUPER_DEFENCE, p -> p.getStats().get(StatType.Defence).boost(5, 0.15));
		registerPotion(PotionDrink.SUPER_COMBAT, p -> {
			p.getStats().get(StatType.Attack).boost(5, 0.15);
			p.getStats().get(StatType.Strength).boost(5, 0.15);
			p.getStats().get(StatType.Defence).boost(5, 0.15);
		});

		registerPotion(PotionDrink.RANGING, p -> p.getStats().get(StatType.Ranged).boost(4, 0.10));
		registerPotion(PotionDrink.MAGIC, p -> p.getStats().get(StatType.Magic).boost(5, 0.0));

		registerPotion(PotionDrink.AGILITY, p -> p.getStats().get(StatType.Agility).boost(3, 0.0));
		registerPotion(PotionDrink.FISHING, p -> p.getStats().get(StatType.Fishing).boost(3, 0.0));
		registerPotion(PotionDrink.HUNTER, p -> p.getStats().get(StatType.Hunter).boost(3, 0.0));

		registerPotion(PotionDrink.ANTIPOISON, p -> p.curePoison((90 * 1000) / 600));
		registerPotion(PotionDrink.RESTORE, p -> restore(p, false));
		registerPotion(PotionDrink.ENERGY, p -> p.getMovement().restoreEnergy(10));
		registerPotion(PotionDrink.PRAYER, p -> {
			Stat stat = p.getStats().get(StatType.Prayer);
			if (p.getEquipment().getId(Equipment.SLOT_RING) == 13202)
				stat.restore(7, 0.27);
			else
				stat.restore(7, 0.25);
			p.getStats().get(StatType.Prayer).alter(stat.currentLevel);
		});

		registerPotion(PotionDrink.SUPER_ANTIPOISON, p -> p.curePoison((360 * 1000) / 600));
		registerPotion(PotionDrink.SUPER_ENERGY, p -> p.getMovement().restoreEnergy(20));

		registerPotion(PotionDrink.BLIGHTED_SUPER_RESTORE, p -> restore(p, true));
		registerPotion(PotionDrink.SANFEW_SERUM, p -> {
			restore(p, true);
			p.curePoison((90 * 1000) / 600);
		});
		registerPotion(PotionDrink.ANTIDOTE_PLUS, p -> p.curePoison((540 * 1000) / 600));
		registerPotion(PotionDrink.ANTIFIRE, p -> {
			p.antifireTicks = 600;
		});
		registerPotion(PotionDrink.SUPER_ANTIFIRE, p -> {
			p.superAntifireTicks = 300;
		});

		registerPotion(PotionDrink.STAMINA, p -> {
			p.getMovement().restoreEnergy(20);
			VarPlayerRepository.STAMINA_POTION.set(p, 1);
			p.staminaTicks = 200;
		});
		registerPotion(PotionDrink.ANTIDOTE_PLUS_PLUS, p -> {
			p.curePoison((730 * 1000) / 600);
			p.curePoison((730 * 1000) / 600);
		});
		registerPotion(PotionDrink.SARADOMIN_BREW, p -> {
			p.getStats().get(StatType.Hitpoints).boost(2, 0.15);
			p.getStats().get(StatType.Defence).boost(2, 0.20);
			if (!p.brewImmunityActive()) {
				p.getStats().get(StatType.Attack).drain(0.10);
				p.getStats().get(StatType.Strength).drain(0.10);
				p.getStats().get(StatType.Ranged).drain(0.10);
				p.getStats().get(StatType.Magic).drain(0.10);
			}
		});

		registerPotion(PotionDrink.ZAMORAK_BREW, p -> {
			p.getStats().get(StatType.Attack).boost(2, 0.2);
			p.getStats().get(StatType.Strength).boost(2, 0.12);
			p.getStats().get(StatType.Defence).drain((int) (2 + (p.getStats().get(StatType.Defence).fixedLevel * 0.1)));
			p.hit(new Hit().fixedDamage((int) (2 + (p.getMaxHp() * 0.1))));
		});

		registerPotion(PotionDrink.EXTENDED_ANTIFIRE, p -> {
			p.antifireTicks = 1200;
		});
		registerPotion(PotionDrink.EXTENDED_SUPER_ANTIFIRE, p -> {
			p.superAntifireTicks = 600;
		});
		registerPotion(PotionDrink.MAGIC_ESSENCE, p -> p.getStats().get(StatType.Magic).boost(3, 0));
		registerPotion(PotionDrink.ANTI_VENOM, p -> {
			p.cureVenom(300);
			p.curePoison(300);
		});
		registerPotion(PotionDrink.SUPER_ANTI_VENOM, p -> {
			p.cureVenom(1500);
			p.curePoison(1500);
		});


		/**
		 * Raids potions
		 */
		registerPotion(PotionDrink.ELDER_MINUS, p -> {
			p.getStats().get(StatType.Attack).boost(4, 0.10);
			p.getStats().get(StatType.Strength).boost(4, 0.10);
			p.getStats().get(StatType.Defence).boost(4, 0.10);
		});
		registerPotion(PotionDrink.ELDER_REGULAR, p -> {
			p.getStats().get(StatType.Attack).boost(5, 0.13);
			p.getStats().get(StatType.Strength).boost(5, 0.13);
			p.getStats().get(StatType.Defence).boost(5, 0.13);
		});
		registerPotion(PotionDrink.ELDER_PLUS, p -> {
			p.getStats().get(StatType.Attack).boost(6, 0.16);
			p.getStats().get(StatType.Strength).boost(6, 0.16);
			p.getStats().get(StatType.Defence).boost(6, 0.16);
		});

		registerPotion(PotionDrink.TWISTED_MINUS, p -> {
			p.getStats().get(StatType.Ranged).boost(4, 0.10);
		});
		registerPotion(PotionDrink.TWISTED_REGULAR, p -> {
			p.getStats().get(StatType.Ranged).boost(5, 0.13);
		});
		registerPotion(PotionDrink.TWISTED_PLUS, p -> {
			p.getStats().get(StatType.Ranged).boost(6, 0.16);
		});

		registerPotion(PotionDrink.KODAI_MINUS, p -> {
			p.getStats().get(StatType.Magic).boost(4, 0.10);
		});
		registerPotion(PotionDrink.KODAI_REGULAR, p -> {
			p.getStats().get(StatType.Magic).boost(5, 0.13);
		});
		registerPotion(PotionDrink.KODAI_PLUS, p -> {
			p.getStats().get(StatType.Magic).boost(6, 0.16);
		});

		registerPotion(PotionDrink.REVITALISATION_MINUS, p -> {
			restore(p, false);
		});
		registerPotion(PotionDrink.REVITALISATION_REGULAR, p -> {
			restore(p, false);
		});
		registerPotion(PotionDrink.REVITALISATION_PLUS, p -> {
			restore(p, true);
		});

		registerPotion(PotionDrink.PRAYER_ENHANCE_MINUS, p -> {
			Stat stat = p.getStats().get(StatType.Prayer);
			if (p.getEquipment().getId(Equipment.SLOT_RING) == 13202)
				stat.restore(3, 0.22);
			else
				stat.restore(3, 0.20);
			p.getStats().get(StatType.Prayer).alter(stat.currentLevel);
		});
		registerPotion(PotionDrink.PRAYER_ENHANCE_REGULAR, p -> {
			Stat stat = p.getStats().get(StatType.Prayer);
			if (p.getEquipment().getId(Equipment.SLOT_RING) == 13202)
				stat.restore(5, 0.25);
			else
				stat.restore(5, 0.23);
			p.getStats().get(StatType.Prayer).alter(stat.currentLevel);
		});
		registerPotion(PotionDrink.PRAYER_ENHANCE_PLUS, p -> {
			Stat stat = p.getStats().get(StatType.Prayer);
			if (p.getEquipment().getId(Equipment.SLOT_RING) == 13202)
				stat.restore(7, 0.27);
			else
				stat.restore(7, 0.25);
			p.getStats().get(StatType.Prayer).alter(stat.currentLevel);
		});

		registerPotion(PotionDrink.XERIC_AID_MINUS, p -> {
			p.getStats().get(StatType.Hitpoints).boost(2, 0.5);
			p.getStats().get(StatType.Defence).boost(2, 0.10);
			p.getStats().get(StatType.Attack).drain(0.3);
			p.getStats().get(StatType.Strength).drain(0.3);
			p.getStats().get(StatType.Ranged).drain(0.3);
			p.getStats().get(StatType.Magic).drain(0.3);
		});
		registerPotion(PotionDrink.XERIC_AID_REGULAR, p -> {
			p.getStats().get(StatType.Hitpoints).boost(2, 0.10);
			p.getStats().get(StatType.Defence).boost(2, 0.15);
			p.getStats().get(StatType.Attack).drain(0.6);
			p.getStats().get(StatType.Strength).drain(0.6);
			p.getStats().get(StatType.Ranged).drain(0.6);
			p.getStats().get(StatType.Magic).drain(0.6);
		});
		registerPotion(PotionDrink.XERIC_AID_PLUS, p -> {
			p.getStats().get(StatType.Hitpoints).boost(2, 0.15);
			p.getStats().get(StatType.Defence).boost(2, 0.20);
			p.getStats().get(StatType.Attack).drain(0.10);
			p.getStats().get(StatType.Strength).drain(0.10);
			p.getStats().get(StatType.Ranged).drain(0.10);
			p.getStats().get(StatType.Magic).drain(0.10);
		});

		registerPotion(PotionDrink.OVERLOAD_MINUS, p -> {
			p.getStats().get(StatType.Attack).boost(4, 0.10);
			p.getStats().get(StatType.Strength).boost(4, 0.10);
			p.getStats().get(StatType.Defence).boost(4, 0.10);
			p.getStats().get(StatType.Ranged).boost(4, 0.10);
			p.getStats().get(StatType.Magic).boost(4, 0.10);
		});
		registerPotion(PotionDrink.OVERLOAD_REGULAR, p -> {
			p.getStats().get(StatType.Attack).boost(5, 0.15);
			p.getStats().get(StatType.Strength).boost(5, 0.15);
			p.getStats().get(StatType.Defence).boost(5, 0.15);
			p.getStats().get(StatType.Ranged).boost(5, 0.15);
			p.getStats().get(StatType.Magic).boost(5, 0.15);
		});
		registerPotion(PotionDrink.OVERLOAD_PLUS, p -> {
			p.getStats().get(StatType.Attack).boost(6, 0.16);
			p.getStats().get(StatType.Strength).boost(6, 0.16);
			p.getStats().get(StatType.Defence).boost(6, 0.16);
			p.getStats().get(StatType.Ranged).boost(6, 0.16);
			p.getStats().get(StatType.Magic).boost(6, 0.16);
		});
		registerPotion(PotionDrink.DIVINE_SUPER_ATTACK, p -> {
			p.getStats().get(StatType.Attack).boost(5, 0.15);
		});
		registerPotion(PotionDrink.DIVINE_SUPER_STRENGTH, p -> {
			p.getStats().get(StatType.Strength).boost(5, 0.15);
		});
		registerPotion(PotionDrink.DIVINE_SUPER_DEFENCE, p -> {
			p.getStats().get(StatType.Defence).boost(5, 0.15);
		});
		registerPotion(PotionDrink.DIVINE_SUPER_COMBAT, p -> {
			p.getStats().get(StatType.Attack).boost(5, 0.15);
			p.getStats().get(StatType.Strength).boost(5, 0.15);
			p.getStats().get(StatType.Defence).boost(5, 0.15);
		});
		registerPotion(PotionDrink.DIVINE_RANGING, p -> {
			p.getStats().get(StatType.Ranged).boost(4, 0.10);
		});
		registerPotion(PotionDrink.DIVINE_BASTION, p -> {
			p.getStats().get(StatType.Ranged).boost(4, 0.10);
			p.getStats().get(StatType.Defence).boost(5, 0.15);
		});
		registerPotion(PotionDrink.MOONLIGHT_POTION, p -> {
			restore(p, true);
			p.getStats().get(StatType.Attack).boost(7, 0.21);
			p.getStats().get(StatType.Strength).boost(7, 0.21);
			p.getStats().get(StatType.Defence).boost(7, 0.21);
			p.getStats().get(StatType.Ranged).boost(6, 0.17);
			p.getStats().get(StatType.Magic).boost(6, 0.17);
		});
		registerPotion(PotionDrink.DIVINE_MAGIC, p -> {
			p.getStats().get(StatType.Magic).boost(4, 0);
		});
		registerPotion(PotionDrink.DIVINE_BATTLEMAGE, p -> {
			p.getStats().get(StatType.Magic).boost(4, 0);
			p.getStats().get(StatType.Defence).boost(5, 0.15);
		});

		registerEat(1942, 1, p -> p.sendFilteredMessage("You eat the potato. Yuck!"));
		registerEat(1965, 1, p -> p.sendFilteredMessage("You eat the cabbage. Yuck!"));
		registerEat(10476, Math.max(1, ThreadLocalRandom.current().nextInt(3)), 1, true, p -> p.getMovement().restoreEnergy(10));
		registerEat(1963, 2, "banana");
		registerEat(1969, 2, "spinach roll");
		registerEat(7056, 16, "egg potato");
		registerEat(22929, 12, "dragonfruit");
		registerEat(2162, 2, "king worm");
		registerEat(6883, 8, "peach");
		registerEat(1883, 19, "kebab");
		registerEat(2309, 5, "bread");
		registerEat(23874, 20, "paddlefish");
		registerEat(29217, 20, "cooked bream");
		registerEat(29077, 20, "cooked moss lizard");
		registerEat(712, 3, p -> p.getStats().get(StatType.Attack).boost(3, 0.0));
		registerCake(1891, 1893, 1895, 12, "cake");
		registerCake(1897, 1899, 1901, 15, "chocolate cake");

		registerPizza(2289, 2291, 14, "pizza");
		registerPizza(2293, 2295, 16, "meat pizza");
		registerPizza(2297, 2299, 18, "anchovy pizza");
		registerPizza(2301, 2303, 22, "pineapple pizza");

		registerPie(2325, 2333, 10, "redberry pie", null);
		registerPie(2327, 2331, 12, "meat pie", null);
		registerPie(2323, 2335, 14, "apple pie", null);
		registerPie(7178, 7180, 12, "garden pie", p -> p.getStats().get(StatType.Farming).boost(3, 0.0));
		registerPie(7188, 7190, 12, "fish pie", p -> p.getStats().get(StatType.Fishing).boost(3, 0.0));

		registerPie(7198, 7200, 16, "admiral pie", p -> p.getStats().get(StatType.Fishing).boost(5, 0.0));
		registerPie(7218, 7220, 22, "summer pie", p -> p.getStats().get(StatType.Agility).boost(5, 0.0));
		registerPie(7208, 7210, 22, "wild pie", p -> {
			p.getStats().get(StatType.Ranged).boost(4, 0.0);
			p.getStats().get(StatType.Slayer).boost(5, 0.0);
		});

		registerEat(7082, 1923, 5, "fried mushrooms");
		registerEat(2011, 1923, 19, "curry");

		registerEat(7054, 14, "chilli potato");
		registerEat(7058, 20, "mushroom potato");
		registerEat(6705, 16, "potato with cheese");
		registerEat(7060, 22, "tuna potato");

		registerEat(2140, 4, "chicken");
		registerEat(2142, 4, "meat");

		registerEat(315, 3, "shrimps");
		registerEat(325, 4, "sardine");
		registerEat(2152, 3, "toads legs");
		registerEat(319, 1, "anchovies");
		registerEat(347, 5, "herring");
		registerEat(355, 6, "mackerel");
		registerEat(333, 7, "trout");
		registerEat(339, 7, "cod");
		registerEat(351, 8, "pike");
		registerEat(329, 9, "salmon");
		registerEat(361, 10, "tuna");
		registerEat(379, 12, "lobster");
		registerEat(365, 13, "bass");
		registerEat(373, 14, "swordfish");
		registerEat(7946, 16, "monkfish");
		registerEat(385, 20, "shark");
		registerEat(397, 21, "sea turtle");
		registerEat(391, 22, "manta ray");
		registerEat(11936, 22, "dark crab");

		registerEat(20856, 4, "pysk");
		registerEat(20858, 8, "suphi");
		registerEat(20860, 8, "leckish");
		registerEat(20862, 12, "brawk");
		registerEat(20864, 17, "mycril");
		registerEat(20866, 20, "roqed");
		registerEat(20868, 23, "kyren");

		registerEat(20871, 4, "guanic");
		registerEat(20873, 8, "praeal");
		registerEat(20875, 8, "giral");
		registerEat(20877, 12, "phluxia");
		registerEat(20879, 17, "kryket");
		registerEat(20881, 20, "murng");
		registerEat(20883, 23, "psykk");

		registerEat(403, 4, "seaweed");

		ObjType.get(3144).consumable = true;
		ItemAction.registerInventory(3144, "eat", (player, item) -> {
			if (eatKaram(player, item, 18))
				player.sendFilteredMessage("You eat the karambwan.");
		});
		ObjType.get(1993).consumable = true;
		ObjType.get(25958).consumable = true;
		ObjType.get(25960).consumable = true;
		ItemAction.registerInventory(1993, "drink", (player, item) -> {
			if (eatKaram(player, item, 11))
				player.getStats().get(StatType.Attack).drain(2);
			player.sendFilteredMessage("You drink the wine.");
		});
		ItemAction.registerInventory(25958, "eat", (player, item) -> {
			if (eatKaram(player, item, 16))
				player.sendFilteredMessage("You eat the corrupted paddlefish.");
		});
		ItemAction.registerInventory(25960, "eat", (player, item) -> {
			if (eatKaram(player, item, 16))
				player.sendFilteredMessage("You eat the crystal paddlefish.");
		});

		ObjType.get(24595).consumable = true;
		ItemAction.registerInventory(24595, "eat", (player, item) -> {
			if (eatBlightedKaram(player, item))
				player.sendFilteredMessage("You eat the blighted karambwan.");
		});

		ObjType.get(24589).consumable = true;
		ItemAction.registerInventory(24589, "eat", (player, item) -> {
			if (eatBlightedManta(player, item))
				player.sendFilteredMessage("You eat the blighted manta ray.");
		});

		ObjType.get(24592).consumable = true;
		ItemAction.registerInventory(24592, "eat", (player, item) -> {
			if (eatBlightedAngler(player, item))
				player.sendFilteredMessage("You eat the blighted anglerfish.");
		});

		ObjType.get(13441).consumable = true;
		ItemAction.registerInventory(13441, "eat", (player, item) -> {
			if (eatAngler(player, item))
				player.sendFilteredMessage("You eat the anglerfish.");
		});
	}

	private static void registerEat(int id, int heal, String name) {
		registerEat(id, -1, heal, 3, false, p -> p.sendFilteredMessage("You eat the " + name + "."));
	}

	private static void registerEat(int id, int newId, int heal, String name) {
		registerEat(id, newId, heal, 3, false, p -> p.sendFilteredMessage("You eat the " + name + "."));
	}

	private static void registerEat(int id, int heal, Consumer<Player> eatAction) {
		registerEat(id, -1, heal, 3, false, eatAction);
	}

	private static void registerEat(int id, int heal, int ticks, boolean stackable, Consumer<Player> eatAction) {
		registerEat(id, -1, heal, ticks, stackable, eatAction);
	}

	private static void registerEat(int id, int newId, int heal, int ticks, boolean stackable, Consumer<Player> eatAction) {
		ObjType.get(id).consumable = true;
		ItemAction.registerInventory(id, "eat", (player, item) -> {
			if (player.getCurrentToARaid() != null &&
				player.getCurrentToARaid().getInvocations().contains(Invocations.ON_A_DIET) &&
				TombsOfAmascutManager.getRaidParty(player).isStarted()) {
				player.sendMessage("You can't eat food with your current invocations.");
				return;
			}
			if (eat(player, item, newId, heal, ticks, stackable))
				eatAction.accept(player);
		});
	}

	private static void registerCake(int firstId, int secondId, int thirdId, int heal, String name) {
		heal /= 3;
		registerEat(firstId, secondId, heal, 2, false, p -> p.sendFilteredMessage("You eat part of the " + name + "."));
		registerEat(secondId, thirdId, heal, 2, false, p -> p.sendFilteredMessage("You eat some more of the " + name + "."));
		registerEat(thirdId, -1, heal, 3, false, p -> p.sendFilteredMessage("You eat the slice of " + name + "."));
	}

	private static void registerPizza(int fullId, int halfId, int heal, String name) {
		heal /= 2;
		registerEat(fullId, halfId, heal, 1, false, p -> p.sendFilteredMessage("You eat half of the " + name + "."));
		registerEat(halfId, -1, heal, 2, false, p -> p.sendFilteredMessage("You eat the remaining " + name + "."));
	}

	private static void registerPie(int fullId, int halfId, int heal, String name, Consumer<Player> postEffect) {
		heal /= 2;
		registerEat(fullId, halfId, heal, 1, false, p -> {
			p.sendFilteredMessage("You eat half of the " + name + ".");
			if (postEffect != null)
				postEffect.accept(p);
		});
		registerEat(halfId, -1, heal, 2, false, p -> {
			p.sendFilteredMessage("You eat the remaining " + name + ".");
			if (postEffect != null)
				postEffect.accept(p);
		});
	}

	public static boolean eat(Player player, Item item, int newId, int heal, int ticks, boolean stackable) {
		if (player.isLockedExclude(FULL_ALLOW_EAT) || player.isStunned())
			return false;
		if (player.eatDelay.isDelayed() || player.karamDelay.isDelayed() || player.potDelay.isDelayed())
			return false;
		if (DuelRule.NO_FOOD.isToggled(player)) {
			player.sendMessage("Food has been disabled for this duel!");
			return false;
		}
		if (item.getId() == 29217) {
			heal = Math.min(player.getStats().get(StatType.Cooking).fixedLevel / 3, player.getStats().get(StatType.Fishing).fixedLevel / 3);
		} else if (item.getId() == 29077) {
			heal = Math.min(player.getStats().get(StatType.Cooking).fixedLevel / 3, player.getStats().get(StatType.Hunter).fixedLevel / 2);
		}
		boolean saveFood = false;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.FOOD_CONNOISSEUR)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.FOOD_CONNOISSEUR);
			FoodConnoisseur c = (FoodConnoisseur) player.getPlayerPerkHandler().
				getActivePerks(player).get(perkIndex).getPerk(player);
			if (Random.rollPercent(c.getSaveFoodChance())) {
				player.sendFilteredMessage("Your perk allows you to keep the food.");
				saveFood = true;
			}
			float multiplier = 1;
			multiplier += c.getHealMultiplier();
			heal *= multiplier;

		}
		if (!saveFood) {
			if (stackable)
				item.remove(1);
			else if (newId == -1)
				item.remove();
			else
				item.setId(newId);
		}
		animEat(player);
		player.incrementHp(heal);
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.FAST_EATER) && player.wildernessLevel < 1) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.FAST_EATER);
			FastEater c = (FastEater) player.getPlayerPerkHandler().
				getActivePerks(player).get(perkIndex).getPerk(player);
			double calc = 1 - c.getTickMultiplier();
			ticks *= calc;

		}
		player.eatDelay.delay(ticks);
		if (item.getId() == 10476) return true;
		player.getCombat().delayAttack(3);
		return true;
	}

	private static boolean eatKaram(Player player, Item item, int healAmount) {
		if (player.isLocked() || player.isStunned())
			return false;
		if (player.karamDelay.isDelayed())
			return false;
		if (DuelRule.NO_FOOD.isToggled(player)) {
			player.sendMessage("Food has been disabled for this duel!");
			return false;
		}
		if (player.getCurrentToARaid() != null && player.getCurrentToARaid().getInvocations().contains(Invocations.ON_A_DIET) && TombsOfAmascutManager.getRaidParty(player).isStarted()) {
			player.sendMessage("You can't eat food with your current invocations.");
			return false;
		}
		item.remove();
		animEat(player);
		player.incrementHp(healAmount);
		player.karamDelay.delay(3);
		player.getCombat().delayAttack(player.eatDelay.isDelayed() ? 1 : 2); //delays combat 1 tick less than other food on rs
		return true;
	}

	private static boolean eatWine(Player player, Item item) {
		item.remove();
		animEat(player);
		player.incrementHp(11);
		player.karamDelay.delay(3);
		player.getCombat().delayAttack(player.eatDelay.isDelayed() ? 1 : 2); //delays combat 1 tick less than other food on rs
		return true;
	}

	private static boolean eatAngler(Player player, Item item) {
		if (player.isLocked() || player.isStunned())
			return false;
		if (player.eatDelay.isDelayed() || player.karamDelay.isDelayed() || player.potDelay.isDelayed())
			return false;
		if (DuelRule.NO_FOOD.isToggled(player)) {
			player.sendMessage("Food has been disabled for this duel!");
			return false;
		}
		if (player.getCurrentToARaid() != null && player.getCurrentToARaid().getInvocations().contains(Invocations.ON_A_DIET) && TombsOfAmascutManager.getRaidParty(player).isStarted()) {
			player.sendMessage("You can't eat food with your current invocations.");
			return false;
		}
		item.remove();
		animEat(player);
		int hp = player.getHp();
		int maxHp = player.getMaxHp();
		int c;
		if (maxHp <= 24)
			c = 2;
		else if (maxHp <= 49)
			c = 4;
		else if (maxHp <= 74)
			c = 6;
		else if (maxHp <= 92)
			c = 8;
		else
			c = 13;
		int restore = (maxHp / 10) + c;
		int newHp = Math.min(hp + restore, maxHp + restore);
		player.setHp(newHp);
		player.eatDelay.delay(3);
		player.getCombat().delayAttack(3);
		return true;
	}

	private static boolean eatBlightedAngler(Player player, Item item) {
		if (player.wildernessLevel < 1) {
			player.sendMessage("You have to be in the wilderness to eat this.");
			return false;
		}
		if (player.isLocked() || player.isStunned())
			return false;
		if (player.eatDelay.isDelayed() || player.karamDelay.isDelayed() || player.potDelay.isDelayed())
			return false;
		if (DuelRule.NO_FOOD.isToggled(player)) {
			player.sendMessage("Food has been disabled for this duel!");
			return false;
		}
		item.remove();
		animEat(player);
		int hp = player.getHp();
		int maxHp = player.getMaxHp();
		int c;
		if (maxHp <= 24)
			c = 2;
		else if (maxHp <= 49)
			c = 4;
		else if (maxHp <= 74)
			c = 6;
		else if (maxHp <= 92)
			c = 8;
		else
			c = 13;
		int restore = (maxHp / 10) + c;
		int newHp = Math.min(hp + restore, maxHp + restore);
		player.setHp(newHp);
		player.eatDelay.delay(3);
		player.getCombat().delayAttack(3);
		return true;
	}

	private static boolean eatBlightedManta(Player player, Item item) {
		if (player.wildernessLevel < 1) {
			player.sendMessage("You have to be in the wilderness to eat this.");
			return false;
		}
		if (player.isLocked() || player.isStunned())
			return false;
		if (player.eatDelay.isDelayed() || player.karamDelay.isDelayed() || player.potDelay.isDelayed())
			return false;
		if (DuelRule.NO_FOOD.isToggled(player)) {
			player.sendMessage("Food has been disabled for this duel!");
			return false;
		}
		item.remove();
		animEat(player);
		player.setHp(22);
		player.eatDelay.delay(3);
		player.getCombat().delayAttack(3);
		return true;
	}

	private static boolean eatBlightedKaram(Player player, Item item) {
		if (player.wildernessLevel < 1) {
			player.sendMessage("You have to be in the wilderness to eat this.");
			return false;
		}
		if (player.isLocked() || player.isStunned())
			return false;
		if (player.karamDelay.isDelayed())
			return false;
		if (DuelRule.NO_FOOD.isToggled(player)) {
			player.sendMessage("Food has been disabled for this duel!");
			return false;
		}
		item.remove();
		animEat(player);
		player.incrementHp(18);
		player.karamDelay.delay(3);
		player.getCombat().delayAttack(player.eatDelay.isDelayed() ? 1 : 2); //delays combat 1 tick less than other food on rs
		return true;
	}

	public static void animEat(Player player) {
		if (player.getEquipment().getId(Equipment.SLOT_WEAPON) == 4084)
			player.animate(1469);
		else if (player.seat != null)
			player.animate(player.seat.getEatAnimation(player));
		else
			player.animate(829);
		player.privateSound(2393);
		player.resetActions(true, player.getMovement().following != null, true);
	}

	private static void egniolPotion(Player player) {
		Stat stat = player.getStats().get(StatType.Prayer);
		stat.restore(8, 0.25);
		player.getMovement().restoreEnergy(20);
		VarPlayerRepository.STAMINA_POTION.set(player, 1);
		player.staminaTicks = 200;
	}

	private static void restore(Player player, boolean superEffect) {
		for (StatType type : StatType.VALUES) {
			if (type == StatType.Hitpoints)
				continue;
			Stat stat = player.getStats().get(type);
			if (stat.currentLevel < stat.fixedLevel) {
				if (superEffect) {
					if (type == StatType.Prayer && player.getEquipment().getId(Equipment.SLOT_RING) == 13202) // ring of the gods
						stat.restore(8, 0.27);
					else
						stat.restore(8, 0.25);
					continue;
				}
				if (type != StatType.Prayer)
					stat.restore(10, 0.30);
			}
		}
	}

	private static void registerDrink(PotionDrink potion, int id, int newId, Consumer<Player> effect) {
		ObjType.get(id).consumable = true;
		ItemAction.registerInventory(id, "Drink", (player, item) -> {
			if (player.getCurrentToARaid() != null && player.getCurrentToARaid().getInvocations().contains(Invocations.DEHYDRATION) &&
				TombsOfAmascutManager.getRaidParty(player) != null && TombsOfAmascutManager.getRaidParty(player).isStarted()) {
				player.sendMessage("You can't drink potions with your current invocations.");
				return;
			}
			if (drink(player, potion, item, newId))
				effect.accept(player);
		});
	}

	private static void registerEmpty(int id) {
		ObjType def = ObjType.get(id);
		if (def == null)
			return;
		if (!def.hasOption("empty"))
			return;
		ItemAction.registerInventory(id, "empty", (player, item) -> {
			item.setId(229);
			player.sendMessage("You empty the contents of the vial on the floor.");
		});
	}

	private static void registerPotion(PotionDrink potion, Consumer<Player> effect) {

		registerDrink(potion, potion.vialIds[0], potion.vialIds[1], p -> {

			effect.accept(p);
			p.sendFilteredMessage("You drink some of your " + new Item(potion.vialIds[0]).getDef().name + ".");
			p.sendFilteredMessage("You have 3 doses of potion left.");
		});
		registerDrink(potion, potion.vialIds[1], potion.vialIds[2], p -> {

			effect.accept(p);
			p.sendFilteredMessage("You drink some of your " + new Item(potion.vialIds[1]).getDef().name + ".");
			p.sendFilteredMessage("You have 2 doses of potion left.");
		});
		registerDrink(potion, potion.vialIds[2], potion.vialIds[3], p -> {

			effect.accept(p);
			p.sendFilteredMessage("You drink some of your " + new Item(potion.vialIds[2]).getDef().name + ".");
			p.sendFilteredMessage("You have 1 dose of potion left.");
		});
		registerDrink(potion, potion.vialIds[3], 229, p -> {

			effect.accept(p);
			p.sendFilteredMessage("You drink some of your " + new Item(potion.vialIds[3]).getDef().name + ".");
			p.sendFilteredMessage("You have finished your potion.");
		});
		for (int vial : potion.vialIds) {
			registerEmpty(vial);
		}
	}

	private static boolean drink(Player player, PotionDrink potion, Item item, int newId) {
		if (potion == PotionDrink.BLIGHTED_SUPER_RESTORE && player.wildernessLevel < 1) {
			player.sendFilteredMessage("You can't use this potion outside the wilderness.");
			return false;
		}
		if (player.isLocked() || player.isStunned())
			return false;
		if (player.potDelay.isDelayed() || player.karamDelay.isDelayed())
			return false;
		if (DuelRule.NO_DRINKS.isToggled(player)) {
			player.sendMessage("Drinks have been disabled for this duel!");
			return false;
		}
		if (player.overloadBoostActive && (potion == PotionDrink.OVERLOAD_PLUS || potion == PotionDrink.OVERLOAD_REGULAR || potion == PotionDrink.OVERLOAD_MINUS)) {
			player.sendMessage("Your overload boost is still active.");
			return false;
		}
		if (player.getHp() <= 60 && (potion == PotionDrink.OVERLOAD_PLUS)) {
			player.sendMessage("Your health is to low!!");
			return false;
		}
		if (player.prayerEnhanceBoostActive && (potion == PotionDrink.PRAYER_ENHANCE_MINUS || potion == PotionDrink.PRAYER_ENHANCE_REGULAR || potion == PotionDrink.PRAYER_ENHANCE_PLUS)) {
			player.sendMessage("Your prayer enhance boost is still active.");
			return false;
		}

		if (newId == -1 || (newId == 229 && player.breakVials))
			item.remove();
		else if (potion.raidsPotion)
			item.setId(newId == 229 ? 20800 : newId);
		else if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.POTIONS_MASTER) && player.wildernessLevel < 1) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.POTIONS_MASTER);
			PotionsMaster c = (PotionsMaster) player.getPlayerPerkHandler().
				getActivePerks(player).get(perkIndex).getPerk(player);
			if (Random.rollPercent(c.getSavePotionDoseChance()))
				player.sendMessage("Your perk allows you to save a dose.");
			else item.setId(newId);
		} else
			item.setId(newId);
		animDrink(player);
		player.potDelay.delay(3);
		if (potion == PotionDrink.OVERLOAD_PLUS || potion == PotionDrink.OVERLOAD_REGULAR || potion == PotionDrink.OVERLOAD_MINUS)
			overload(player, potion);
		if (potion == PotionDrink.PRAYER_ENHANCE_MINUS || potion == PotionDrink.PRAYER_ENHANCE_REGULAR || potion == PotionDrink.PRAYER_ENHANCE_PLUS)
			prayerEnhance(player, potion);
		if (potion == PotionDrink.DIVINE_SUPER_ATTACK || potion == PotionDrink.DIVINE_SUPER_STRENGTH || potion == PotionDrink.DIVINE_SUPER_DEFENCE || potion == PotionDrink.DIVINE_SUPER_COMBAT)
			divineCombat(player, potion);
		if (potion == PotionDrink.DIVINE_RANGING || potion == PotionDrink.DIVINE_BASTION)
			divineRange(player, potion);
		if (potion == PotionDrink.DIVINE_MAGIC || potion == PotionDrink.DIVINE_BATTLEMAGE)
			divineMagic(player, potion);
		return true;
	}

	private static void divineCombat(Player player, PotionDrink potion) {
		if (player.getHp() <= 10) {
			player.sendMessage("You need over 10 Hitpoints before you can drink this.");
			return;
		}
		World.startEvent(event -> {
			player.graphics(560);
			player.hit(new Hit().fixedDamage(10));
			event.delay(1);
			if (potion == PotionDrink.DIVINE_SUPER_ATTACK) {
				for (int i = 0; i < 20; i++) {
					player.getStats().get(StatType.Attack).boost(5, 0.15);
					event.delay(25);
				}
			} else if (potion == PotionDrink.DIVINE_SUPER_STRENGTH) {
				for (int i = 0; i < 20; i++) {
					player.getStats().get(StatType.Strength).boost(5, 0.15);
					event.delay(25);
				}
			} else if (potion == PotionDrink.DIVINE_SUPER_DEFENCE) {
				for (int i = 0; i < 20; i++) {
					player.getStats().get(StatType.Defence).boost(5, 0.15);
					event.delay(25);
				}
			} else if (potion == PotionDrink.DIVINE_SUPER_COMBAT) {
				for (int i = 0; i < 20; i++) {
					player.getStats().get(StatType.Attack).boost(5, 0.15);
					player.getStats().get(StatType.Strength).boost(5, 0.15);
					player.getStats().get(StatType.Defence).boost(5, 0.15);
					event.delay(25);
				}
			}
		});
	}

	private static void divineRange(Player player, PotionDrink potion) {
		if (player.getHp() <= 10) {
			player.sendMessage("You need over 10 Hitpoints before you can drink this.");
			return;
		}
		World.startEvent(event -> {
			player.graphics(560);
			player.hit(new Hit().fixedDamage(10));
			event.delay(1);
			if (potion == PotionDrink.DIVINE_RANGING) {
				for (int i = 0; i < 20; i++) {
					player.getStats().get(StatType.Ranged).boost(4, 0.10);
					event.delay(25);
				}
			} else if (potion == PotionDrink.DIVINE_BASTION) {
				for (int i = 0; i < 20; i++) {
					player.getStats().get(StatType.Ranged).boost(4, 0.10);
					player.getStats().get(StatType.Defence).boost(5, 0.15);
					event.delay(25);
				}
			}
		});
	}

	private static void divineMagic(Player player, PotionDrink potion) {
		if (player.getHp() <= 10) {
			player.sendMessage("You need over 10 Hitpoints before you can drink this.");
			return;
		}
		World.startEvent(event -> {
			player.graphics(560);
			player.hit(new Hit().fixedDamage(10));
			event.delay(1);
			if (potion == PotionDrink.DIVINE_MAGIC) {
				for (int i = 0; i < 20; i++) {
					player.getStats().get(StatType.Magic).boost(4, 0);
					event.delay(25);
				}
			} else if (potion == PotionDrink.DIVINE_BATTLEMAGE) {
				for (int i = 0; i < 20; i++) {
					player.getStats().get(StatType.Magic).boost(4, 0);
					player.getStats().get(StatType.Magic).boost(5, 15);
					event.delay(25);
				}
			}
		});
	}

	private static void overload(Player player, PotionDrink potion) {
		if (player.getHp() <= 50) {
			player.sendMessage("You need over 50 Hitpoints before you can drink this.");
			return;
		}
		World.startEvent(event -> {
			player.overloadBoostActive = true;
			for (int i = 0; i < 5; i++) {
				player.animate(3170);
				player.graphics(560);
				player.hit(new Hit().fixedDamage(10));
				event.delay(2);
			}
			if (potion == PotionDrink.OVERLOAD_PLUS) {
				for (int i = 0; i < 20; i++) {
					if (player.raidsParty == null || player.raidsParty.getRaid() == null) {
						player.sendMessage("Your overload boost has worn off.");
						player.overloadBoostActive = false;
						return;
					}
					player.getStats().get(StatType.Attack).boost(6, 0.16);
					player.getStats().get(StatType.Strength).boost(6, 0.16);
					player.getStats().get(StatType.Defence).boost(6, 0.16);
					player.getStats().get(StatType.Ranged).boost(6, 0.16);
					player.getStats().get(StatType.Magic).boost(6, 0.16);
					event.delay(25);

					if (i == 19) {
						player.getStats().get(StatType.Attack).restore();
						player.getStats().get(StatType.Strength).restore();
						player.getStats().get(StatType.Defence).restore();
						player.getStats().get(StatType.Ranged).restore();
						player.getStats().get(StatType.Magic).restore();
						player.incrementHp(50);
					}
				}
			} else if (potion == PotionDrink.OVERLOAD_MINUS) {
				for (int i = 0; i < 20; i++) {
					if (player.raidsParty == null || player.raidsParty.getRaid() == null) {
						player.sendMessage("Your overload boost has worn off.");
						player.overloadBoostActive = false;
						return;
					}
					player.getStats().get(StatType.Attack).boost(4, 0.10);
					player.getStats().get(StatType.Strength).boost(4, 0.10);
					player.getStats().get(StatType.Defence).boost(4, 0.10);
					player.getStats().get(StatType.Ranged).boost(4, 0.10);
					player.getStats().get(StatType.Magic).boost(4, 0.10);
					event.delay(25);

					if (i == 19) {
						player.getStats().get(StatType.Attack).restore();
						player.getStats().get(StatType.Strength).restore();
						player.getStats().get(StatType.Defence).restore();
						player.getStats().get(StatType.Ranged).restore();
						player.getStats().get(StatType.Magic).restore();
						player.incrementHp(50);
					}
				}
			} else if (potion == PotionDrink.OVERLOAD_REGULAR) {
				for (int i = 0; i < 20; i++) {
					player.getStats().get(StatType.Attack).boost(5, 0.15);
					player.getStats().get(StatType.Strength).boost(5, 0.15);
					player.getStats().get(StatType.Defence).boost(5, 0.15);
					player.getStats().get(StatType.Ranged).boost(5, 0.15);
					player.getStats().get(StatType.Magic).boost(5, 0.15);
					event.delay(25);

					if (i == 19) {
						player.getStats().get(StatType.Attack).restore();
						player.getStats().get(StatType.Strength).restore();
						player.getStats().get(StatType.Defence).restore();
						player.getStats().get(StatType.Ranged).restore();
						player.getStats().get(StatType.Magic).restore();
						player.incrementHp(50);
					}
				}
			}
			player.overloadBoostActive = false;
			player.sendMessage("Your overload boost has worn off.");
		});
	}

	private static void prayerEnhance(Player player, PotionDrink potion) {
		World.startEvent(event -> {
			player.prayerEnhanceBoostActive = true;
			if (potion == PotionDrink.PRAYER_ENHANCE_PLUS) {
				int count = 500;
				while (true) {
					if (count == -1 || player.raidsParty == null || player.raidsParty.getRaid() == null) {
						player.sendMessage("Your prayer enhance boost has worn off.");
						player.prayerEnhanceBoostActive = false;
						break;
					}
					if (count % 6 == 0)
						player.getStats().get(StatType.Prayer).restore(1);
					count--;
					event.delay(1);
				}
			} else if (potion == PotionDrink.PRAYER_ENHANCE_REGULAR) {
				int count = 483;
				while (true) {
					if (count == -1 || player.raidsParty == null || player.raidsParty.getRaid() == null) {
						player.sendMessage("Your prayer enhance boost has worn off.");
						player.prayerEnhanceBoostActive = false;
						break;
					}
					if (count % 6 == 0)
						player.getStats().get(StatType.Prayer).restore(1);
					count--;
					event.delay(1);
				}
			} else if (potion == PotionDrink.PRAYER_ENHANCE_MINUS) {
				int count = 458;
				while (true) {
					if (count == -1 || player.raidsParty == null || player.raidsParty.getRaid() == null) {
						player.sendMessage("Your prayer enhance boost has worn off.");
						player.prayerEnhanceBoostActive = false;
						break;
					}
					if (count % 6 == 0)
						player.getStats().get(StatType.Prayer).restore(1);
					count--;
					event.delay(1);
				}
			}
		});
	}

	public static void animDrink(Player player) {
		if (player.getEquipment().getId(Equipment.SLOT_WEAPON) == 4084)
			player.animate(1469);
		else if (player.seat != null)
			player.animate(player.seat.getEatAnimation(player));
		else
			player.animate(829);
		player.privateSound(2401);
		player.resetActions(true, player.getMovement().following != null, true);
	}

}
