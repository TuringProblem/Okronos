package io.ruin.model.skills.cooking;

import io.ruin.api.utils.Random;
import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.PerkTaskHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.DancingInTheYields;
import io.ruin.model.activities.perktree.perks.KitchenExpert;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerCounter;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.skill.SkillDialogue;
import io.ruin.model.inter.dialogue.skill.SkillItem;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.skills.CapePerks;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;

import java.util.ArrayList;
import java.util.List;

public class Cooking {

	public static final int COOKING_GAUNLETS = 775;

	private static void cook(Player player, Food food, GameObject obj, int anim, boolean fire) {
		if (!player.getStats().check(StatType.Cooking, food.levelRequirement, "cook " + food.descriptiveName))
			return;
		SkillItem i = new SkillItem(food.rawID).name(food.rawName)
				.addAction((p, amount, event) -> startCooking(p, food, obj, amount, anim, fire));
		// System.out.println(food.name());
		if (food.equals(Food.RAW_MEAT) || food.equals(Food.SINEW)) {
			SkillItem sinew = new SkillItem(Food.SINEW.cookedID).name(Food.SINEW.itemName)
					.addAction((p, amount, event) -> startCooking(p, Food.SINEW, obj, amount, anim, fire));
			SkillDialogue.make(player, i, sinew);
		} else {
			if (player.getInventory().hasMultiple(food.rawID))
				SkillDialogue.cook(player, i);
			else
				startCooking(player, food, obj, 1, anim, fire);
		}
	}

	private static void cook(Player player, GameObject obj) {

		List<Food> cookableFood = new ArrayList<>();
		for (Food food : Food.VALUES) {
			Stat stat = player.getStats().get(StatType.Cooking);
			if (player.getInventory().hasId(food.rawID) && stat.currentLevel >= food.levelRequirement) {
				cookableFood.add(food);
			}
		}
		if (cookableFood.isEmpty()) {
			player.dialogue(new MessageDialogue("You don't have any raw fish to cook or the level requirement."));
			return;
		}
		SkillItem[] food = new SkillItem[cookableFood.size()];
		cookableFood.forEach(rawFood -> {

			Item rawItem = player.getInventory().findItem(rawFood.rawID);
			if (rawItem != null) {
				food[cookableFood.indexOf(rawFood)] = new SkillItem(rawFood.cookedID)
						.addAction((p, amount, event) -> startCooking(p, rawFood, obj, amount, 896, false));
			}
		});

		SkillDialogue.cook(player, food);
	}

	private static void startCooking(Player player, Food food, GameObject obj, int amountToCook, int anim, boolean fire) {
		player.startEvent(e -> {
			int amount = amountToCook;
			while (amount-- > 0) {
				Item rawFood = player.getInventory().findItem(food.rawID);
				if (rawFood == null) {
					player.sendMessage("You don't have any more " + food.itemNamePlural + " to cook.");
					break;
				}

				if (obj == null) {
					break;
				}

				if (rawFood.getAmount() > 1) {
					rawFood.setAmount(rawFood.getAmount() - 1);
				} else {
					rawFood.remove();
				}

				if (!player.getInventory().hasFreeSlots(1)) {
					player.sendMessage("Not enough space too cook.");
					break;
				}

				player.animate(anim);
				if (cookedFood(player, food, fire)) {
					player.cookedFishCounter++;
					if (player.cookedFishCounter == Achievements.JUST_FOR_THE_HALIBUT_I.getCompletionAmount())
						player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
								+ Achievements.JUST_FOR_THE_HALIBUT_I.getAchievementName());

					if (food.rawID == 371) {
						player.swordfishCooked++;
						if (player.swordfishCooked == Achievements.JUST_FOR_THE_HALIBUT_II.getCompletionAmount())
							player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
									+ Achievements.JUST_FOR_THE_HALIBUT_II.getAchievementName());
					}

					if (food.rawID == 13439 || food.rawID == 383) {
						player.anglerFishAndSharksCookedCounter++;
						if (player.anglerFishAndSharksCookedCounter == Achievements.JUST_FOR_THE_HALIBUT_III.getCompletionAmount())
							player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
									+ Achievements.JUST_FOR_THE_HALIBUT_III.getAchievementName());
					}

					if (food.rawID == 11934) {
						player.darkCrabsCooked++;
						if (player.darkCrabsCooked == Achievements.JUST_FOR_THE_HALIBUT_IV.getCompletionAmount())
							player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
									+ Achievements.JUST_FOR_THE_HALIBUT_IV.getAchievementName());
					}

					player.getInventory().add(food.cookedID);
					PerkTaskHandler.handleGatherResource(player, food.cookedID, 1);
					DailyTasks.handleItemObtained(player, food.cookedID, StatType.Cooking);
					player.getStats().addXp(StatType.Cooking, food.experience * bonus(player, fire), true);
					player.sendFilteredMessage(cookingMessage(food));
					PlayerCounter.COOKED_FOOD.increment(player, 1);
				} else {
					player.getInventory().add(food.burntID);
					player.sendFilteredMessage("You accidentally burn the " + food.itemName + ".");
					PlayerCounter.BURNT_FOOD.increment(player, 1);
				}

				if (fire)
					PlayerCounter.COOKED_ON_FIRE.increment(player, 1);
				int delay = 4;
				if (food.rawID == ItemID.RAW_PADDLEFISH)
					delay = 1;

				e.delay(delay);
			}
		});
	}

	private static double bonus(Player player, boolean fire) {
		double bonus = 1.0;
		return bonus;
	}


	private static String cookingMessage(Food food) {
		if (food == Food.RAW_LOBSTER)
			return "You roast a lobster.";
		else if (food == Food.PIE_MEAT)
			return "You successfully bake a tasty meat pie.";
		else if (food == Food.REDBERRY_PIE)
			return "You successfully bake a delicious redberry pie.";
		else if (food == Food.SEAWEED)
			return "You burn the seaweed to soda ash.";
		else
			return "You successfully cook " + food.descriptiveName + ".";
	}

	private static boolean cookedFood(Player player, Food food, Boolean fire) {
		if (food.burntID == -1)
			return true;
		if (CapePerks.wearsCookingCape(player))
			return true;
		double burnBonus = 0.0;
		int levelReq = food.levelRequirement;
		int burnStop = getBurnStop(player, food, fire);
		if (!fire)
			burnBonus = 3.0;
		double burnChance = (55.0 - burnBonus);
		double cookingLevel = player.getStats().get(StatType.Cooking).currentLevel;
		double randNum = Random.get() * 100.0;

		burnChance -= ((cookingLevel - levelReq) * (burnChance / (burnStop - levelReq)));
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.KITCHEN_EXPERT)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.KITCHEN_EXPERT);
			KitchenExpert c =
					(KitchenExpert) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
			float multiplier = 1;
			multiplier -= c.successFullyCookBoost();
			burnChance *= multiplier;
		}
		return burnChance <= randNum;
	}

	private static int getBurnStop(Player player, Food food, Boolean cookingOnRange) {
		Item gloves = player.getEquipment().get(Equipment.SLOT_HANDS);
		if (gloves != null && gloves.getId() == COOKING_GAUNLETS)
			return food.burnLevelCookingGauntlets;
		return cookingOnRange ? food.burnLevelRange : food.burnLevelFire;
	}

	public static void register() {
		// lmao, it loops every object in the game, and for each object in the game loops every object in the game again 3x
		// l0l
		for (Food food : Food.VALUES) {
			ItemObjectAction.register(food.rawID, "range",
					(player, item, obj) -> Cooking.cook(player, food, obj, 896, false));
			ItemObjectAction.register(food.rawID, "cooking range",
					(player, item, obj) -> Cooking.cook(player, food, obj, 896, false));
			ItemObjectAction.register(food.rawID, "fire", (player, item, obj) -> Cooking.cook(player, food, obj, 897, true));
			ItemObjectAction.register(food.rawID, "clay oven",
					(player, item, obj) -> Cooking.cook(player, food, obj, 897, true));
			ItemObjectAction.register(food.rawID, "stove",
					(player, item, obj) -> Cooking.cook(player, food, obj, 896, false));
			ItemObjectAction.register(food.rawID, "sulphur vent",
					(player, item, obj) -> Cooking.cook(player, food, obj, 896, false));
			ItemObjectAction.register(food.rawID, "cooking pot",
					(player, item, obj) -> Cooking.cook(player, food, obj, 897, true));
			ItemObjectAction.register(food.rawID, 5249, (player, item, obj) -> Cooking.cook(player, food, obj, 897, true));
		}
		ObjType.forEach(objDef -> {
			ObjectAction.register("range", 1, (player, obj) -> cook(player, obj));
			ObjectAction.register("cooking range", 1, (player, obj) -> cook(player, obj));
			ObjectAction.register("clay oven", 1, (player, obj) -> cook(player, obj));
		});
	}
}
