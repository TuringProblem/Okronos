package io.ruin.model.skills.fishing;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.PerkTaskHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.SpeedFisher;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerCounter;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.skills.cooking.Cooking;
import io.ruin.model.skills.cooking.Food;
import io.ruin.model.skills.firemaking.Burning;
import io.ruin.model.skills.woodcutting.Hatchet;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;

import java.util.ArrayList;
import java.util.List;

public class FishingSpot {

	private FishingTool tool;

	private FishingCatch[] regularCatches, barehandCatches;

	private FishingSpot(FishingTool tool) {
		this.tool = tool;
	}

	private FishingSpot regularCatches(FishingCatch... regularCatches) {
		this.regularCatches = regularCatches;
		return this;
	}

	private static int getDonatorNoteChance(Player player) {
		switch (player.getSecondaryGroup()) {
			case SUPER_DONATOR -> {
				return 5;
			}
			case ELITE_DONATOR -> {
				return 10;
			}
			case NOBLE_DONATOR -> {
				return 15;
			}
			case GOLD_DONATOR -> {
				return 20;
			}
			case PLATINUM_DONATOR -> {
				return 25;
			}
			case LEGENDARY_DONATOR -> {
				return 30;
			}
			case SUPREME_DONATOR -> {
				return 35;
			}
		}
		return 0;
	}

	private FishingSpot barehandCatches(FishingCatch... barehandCatches) {
		this.barehandCatches = barehandCatches;
		return this;
	}

	private FishingCatch randomCatch(int level, boolean barehand, FishingTool tool, Player player) {
		FishingCatch[] catches = barehand ? barehandCatches : regularCatches;
		double roll = Random.get();
		List<FishingCatch> possibleCatches = new ArrayList<>();
		for (int i = catches.length - 1; i >= 0; i--) {
			FishingCatch c = catches[i];
			int levelDifference = level - c.levelReq;
			if (levelDifference < 0) {
				/* not high enough level */
				continue;
			}
			if (tool.id == 309) {
				player.flyFishCaughtCounter++;
				if (player.flyFishCaughtCounter == Achievements.SOMETHING_IS_FISHY_HERE_I.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.SOMETHING_IS_FISHY_HERE_I.getAchievementName());
			}
			double chance = c.baseChance;
			if (chance >= 1.0) {
				/* always catch this bad boy */
				return c;
			}
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SPEED_FISHER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.SPEED_FISHER);
				SpeedFisher speedFisher = (SpeedFisher) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				chance += speedFisher.getFishChance();
			}
			if (tool == FishingTool.DRAGON_HARPOON)
				chance += 1.20;
			if (tool == FishingTool.CRYSTAL_HARPOON)
				chance += 1.50;
			if (tool == FishingTool.INFERNAL_HARPOON)
				chance += 1.25;


			chance += (double) levelDifference * 0.01;
			if (roll > Math.min(chance, 0.90)) {
				/* failed to catch */
				continue;
			}
			possibleCatches.add(c);
		}
		return possibleCatches.size() > 0 ? Random.get(possibleCatches) : null;
	}

	private double getPetDonatorBoost(Player player) {
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

	private void fish(Player player, NPC npc) {
		boolean barehand;
		Stat fishing = player.getStats().get(StatType.Fishing);
		if (player.getInventory().contains(new Item(tool.id)) || (tool == FishingTool.HARPOON && hasDragonHarpoon(player)) ||
			(tool == FishingTool.FISHING_ROD && hasPearlFishingRod(player)) || (tool == FishingTool.HARPOON && hasInfernalHarpoon(player)) || (tool == FishingTool.FLY_FISHING_ROD && hasPearlFlyFishingRod(player)) ||
			(tool == FishingTool.BARBARIAN_ROD && hasPearlBarbarianRod(player)) || (tool == FishingTool.CRYSTAL_HARPOON && hasCrystalHarpoon(player)) ||
			(tool == FishingTool.ANGLER_ROD && hasPearlFishingRod(player))) {
			FishingCatch lowestCatch = regularCatches[0];
			if (fishing.currentLevel < lowestCatch.levelReq) {
				player.sendMessage("You need a Fishing level of at least " + lowestCatch.levelReq + " to fish at this spot.");
				return;
			}

			if (tool == FishingTool.HARPOON && hasDragonHarpoon(player)) {
				if (fishing.currentLevel < 61) {
					player.sendMessage("You need a Fishing level of at least 61 to fish with a dragon harpoon.");
					return;
				}
			}

			barehand = false;
		} else {
			if (barehandCatches == null) {
				player.sendMessage("You need a " + tool.primaryName + " to fish at this spot.");
				return;
			}

			FishingCatch lowestCatch = barehandCatches[0];

			if (fishing.currentLevel < lowestCatch.levelReq) {
				player.sendMessage("You need a Fishing level of at least " + lowestCatch.levelReq + " to barehand fish at this spot.");
				player.sendMessage("To fish at this spot normally, you'll need a " + tool.primaryName + ".");
				return;
			}

			if (player.getStats().get(StatType.Agility).currentLevel < lowestCatch.agilityReq) {
				player.sendMessage("You need an Agility level of at least " + lowestCatch.agilityReq + " to barehand fish at this spot.");
				player.sendMessage("To fish at this spot normally, you'll need a " + tool.primaryName + ".");
				return;
			}

			if (player.getStats().get(StatType.Strength).currentLevel < lowestCatch.strengthReq) {
				player.sendMessage("You need a Strength level of at least " + lowestCatch.strengthReq + " to barehand fish at this spot.");
				player.sendMessage("To fish at this spot normally, you'll need a " + tool.primaryName + ".");
				return;
			}

			barehand = true;
		}

		Item secondary;
		if (barehand || tool.secondaryId == -1) {
			secondary = null;
		} else if ((secondary = player.getInventory().findItem(tool.secondaryId)) == null) {
			player.sendMessage("You need at least one " + tool.secondaryName + " to fish at this spot.");
			return;
		}

		if (player.getInventory().isFull()) {
			player.sendMessage("Your inventory is too full to hold any fish.");
			return;
		}

		if (npc.getId() == INFERNO_EEL) {
			if (!player.getEquipment().hasId(1580)) {
				player.sendMessage("You need to be wearing ice gloves to catch infernal eel.");
				return;
			}
		}

		/**
		 * Start event
		 */
		int animation = barehand ? 6704 : tool.startAnimationId;
		// if(tool == FishingTool.HARPOON && hasInfernalHarpoon(player))
		// animation = 7402;
		player.animate(animation);
		int finalAnimation = animation;
		player.startEvent(event -> {
			int animTicks = 2;
			boolean firstBarehandAnim = true;
			while (true) {
				if (animTicks > 0) { //we do this so we can check if the npc has moved every tick
					int diffX = Math.abs(player.getAbsX() - npc.getAbsX());
					int diffY = Math.abs(player.getAbsY() - npc.getAbsY());
					if (diffX + diffY > 1) {
						player.resetAnimation();
						return;
					}

					event.delay(1);
					animTicks--;
					continue;
				}

				FishingCatch c = randomCatch(fishing.currentLevel, barehand, tool, player);
				if (c != null) {
					if (npc.getId() == MINNOWS && (npc.minnowsFish || (npc.minnowsFish = Random.rollDie(100)))) {
						npc.graphics(1387);
						player.getInventory().remove(c.id, 26);
						player.sendFilteredMessage("A flying fish jumps up and eats some of your minnows!");
					} else {
						if (secondary != null)
							secondary.incrementAmount(-1);
						if (player.getInventory().contains(21031) || player.getEquipment().contains(21031) && (Random.rollDie(3, 1))) {
							Food burning = Food.get(c.id);
							if (burning != null && player.getStats().check(StatType.Cooking, burning.levelRequirement)) {
								player.sendFilteredMessage("The infernal harpoon incinerates a fish.");
								player.graphics(580, 50, 0);
								player.getStats().addXp(StatType.Cooking, burning.experience / 2, true);
								PerkTaskHandler.handleGatherResource(player, c.id, 1);
								DailyTasks.handleItemObtained(player, c.id, StatType.Fishing);
								FishingClueBottle.roll(player, c, barehand);
								int petOdds = c.petOdds;
								if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
									int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
									ThePetHunter d = (ThePetHunter) player.getPlayerPerkHandler().
										getActivePerks(player).get(perkIndex).getPerk(player);
									petOdds *= d.getPetChanceBoost();
								}
								if (player.petDropBonus.isDelayed())
									petOdds *= 0.8;
								petOdds *= getPetDonatorBoost(player);
								if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
									petOdds *= 0.85F;
								if (Random.get(petOdds) == 0)
									Pet.HERON.unlock(player, 0);
							}
						}

						if (npc.getId() == MINNOWS)
							player.sendFilteredMessage("You catch some minnows!");

						if (npc.getId() == INFERNO_EEL)
							player.sendFilteredMessage("You catch an infernal eel. It hardens as you handle it with your ice gloves.");

						int amount = npc.getId() == MINNOWS ? Random.get(10, 26) : 1;
						player.collectResource(new Item(c.id, amount));

						if (player.darkCrabBoost.isDelayed()) {
							if (Random.rollPercent(20))
								amount++;
						}
						PerkTaskHandler.handleGatherResource(player, c.id, amount);
						DailyTasks.handleItemObtained(player, c.id, StatType.Fishing);


						if (Random.rollPercent(getDonatorNoteChance(player)) && c.id != 13339)
							player.getInventory().add(c.id + 1, amount);
						else
							player.getInventory().add(c.id, amount);


						if (c.id == 371) {
							player.swordfishFished++;
							if (player.swordfishFished == Achievements.SOMETHING_IS_FISHY_HERE_II.getCompletionAmount())
								player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.SOMETHING_IS_FISHY_HERE_II.getAchievementName());
						}

						if (c.id == 383 || c.id == 13439) {
							player.anglerFishAndSharksCaughtCounter++;
							if (player.anglerFishAndSharksCaughtCounter == Achievements.SOMETHING_IS_FISHY_HERE_III.getCompletionAmount())
								player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.SOMETHING_IS_FISHY_HERE_III.getAchievementName());
						}

						if (c.id == 11934) {
							player.darkCrabsFished++;
							if (player.darkCrabsFished == Achievements.SOMETHING_IS_FISHY_HERE_IV.getCompletionAmount())
								player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.SOMETHING_IS_FISHY_HERE_IV.getAchievementName());

						}

						if (npc.getId() != MINNOWS)
							PlayerCounter.TOTAL_FISH.increment(player, 1);

						player.getStats().addXp(StatType.Fishing, c.xp * anglerBonus(player), true);

						FishingClueBottle.roll(player, c, barehand);
						int petOdds = c.petOdds;
						if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
							int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
							ThePetHunter d = (ThePetHunter) player.getPlayerPerkHandler().
								getActivePerks(player).get(perkIndex).getPerk(player);
							petOdds *= d.getPetChanceBoost();
						}
						if (player.petDropBonus.isDelayed())
							petOdds *= 0.8;
						petOdds *= getPetDonatorBoost(player);
						if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
							petOdds *= 0.85F;
						if (Random.get(petOdds) == 0)
							Pet.HERON.unlock(player, 0);

//                        if(npc.fishingArea != null && npc.fishingArea != FishingArea.RESOURCE_AREA && Random.rollDie(75))
//                            npc.fishingArea.move(npc);//TODO ADD ACTUAL TELEPORTING SUPPORT FOR MOVING SPOTS TO DIFF LOCS

						if (c.barbarianXp > 0) {
							player.getStats().addXp(StatType.Agility, c.barbarianXp, true);
							player.getStats().addXp(StatType.Strength, c.barbarianXp, true);

							if (barehand) {
								if (c == FishingCatch.BARBARIAN_TUNA)
									player.animate(firstBarehandAnim ? 6710 : 6711);
								else if (c == FishingCatch.BARBARIAN_SWORDFISH)
									player.animate(firstBarehandAnim ? 6707 : 6708);
								else if (c == FishingCatch.BARBARIAN_SHARK)
									player.animate(firstBarehandAnim ? 6705 : 6706);

								firstBarehandAnim = !firstBarehandAnim;
								animTicks = 8;
							}
						}

						if (player.getInventory().isFull()) {
							player.sendMessage("Your inventory is too full to hold any more fish.");
							player.resetAnimation();
							return;
						}
						if (!barehand && tool.secondaryId != -1) {
							Item requiredSecondary = player.getInventory().findItem(tool.secondaryId);

							if (requiredSecondary == null) {
								player.sendMessage("You need at least one " + tool.secondaryName + " to fish at this spot.");
								return;
							}
						}
					}
				}

				if (animTicks == 0) {
					player.animate(finalAnimation);
					animTicks = 3;
				}
			}
		});
	}

	private static boolean hasDragonHarpoon(Player player) {
		if (player.getInventory().hasId(FishingTool.DRAGON_HARPOON.id))
			return true;

		ObjType playerWeapon = player.getEquipment().getDef(Equipment.SLOT_WEAPON);
		if (playerWeapon != null && playerWeapon.id == FishingTool.DRAGON_HARPOON.id)
			return true;        // Shi's a prick...

		return false;
	}

	private static boolean hasInfernalHarpoon(Player player) {
		for (int i = 0; i < 28; i++) {
			Item item = player.getInventory().get(i);
			if (item == null || item.getId() != FishingTool.INFERNAL_HARPOON.id)
				continue;
			if (item.getId() == FishingTool.INFERNAL_HARPOON.id)
				return true;
		}
		ObjType playerWeapon = player.getEquipment().getDef(Equipment.SLOT_WEAPON);
		if (playerWeapon != null && playerWeapon.id == FishingTool.INFERNAL_HARPOON.id)
			return true;        // Shi's a prick...

		return false;
	}

	private static boolean hasCrystalHarpoon(Player player) {
		if (player.getInventory().hasId(FishingTool.CRYSTAL_HARPOON.id))
			return true;

		ObjType playerWeapon = player.getEquipment().getDef(Equipment.SLOT_WEAPON);
		if (playerWeapon != null && playerWeapon.id == FishingTool.CRYSTAL_HARPOON.id)
			return true;

		return false;
	}

	private static boolean hasPearlFishingRod(Player player) {
		if (player.getInventory().hasId(FishingTool.PEARL_FISHING_ROD.id))
			return true;

		ObjType playerWeapon = player.getEquipment().getDef(Equipment.SLOT_WEAPON);
		if (playerWeapon != null && playerWeapon.id == FishingTool.PEARL_FISHING_ROD.id)
			return true;

		return false;
	}

	private static boolean hasPearlFlyFishingRod(Player player) {
		if (player.getInventory().hasId(FishingTool.PEARL_FLY_FISHING_ROD.id))
			return true;

		ObjType playerWeapon = player.getEquipment().getDef(Equipment.SLOT_WEAPON);
		if (playerWeapon != null && playerWeapon.id == FishingTool.PEARL_FLY_FISHING_ROD.id)
			return true;

		return false;
	}

	private static boolean hasPearlBarbarianRod(Player player) {
		if (player.getInventory().hasId(FishingTool.PEARL_BARBARIAN_ROD.id))
			return true;

		ObjType playerWeapon = player.getEquipment().getDef(Equipment.SLOT_WEAPON);
		if (playerWeapon != null && playerWeapon.id == FishingTool.PEARL_BARBARIAN_ROD.id)
			return true;

		return false;
	}

	private void register(int npcId, String option) {
		NPCAction.register(npcId, option, this::fish);
	}

	/**
	 * :)
	 */

	public static final int NET_BAIT = 1518;            //shrimps,anchovies / sardine,herring

	public static final int LURE_BAIT = 1506;           //trout,salmon / pike

	public static final int CAGE_HARPOON = 1519;        //lobster / tuna,swordfish

	public static final int BIG_NET_HARPOON = 5233;     //shark / tuna,swordfish

	public static final int SMALL_NET_HARPOON = 4316;   //monkfish / swordfish

	public static final int USE_ROD = 1542;             //leaping

	public static final int CAGE = 1535;                //dark crab

	public static final int BAIT = 6825;                //angler

	private static final int MINNOWS = 7731;            //minnows

	public static final int INFERNO_EEL = 7676;        //infernal eel

	public static final int KARAMBWAN_SPOT = 4712;

	public static final int MOLTEN_EEL = 15018;

	public static final int SACRED_EEL = 6488;

	public static final int SLIMY_EEL = 2653;

	public static void register() {

		new FishingSpot(FishingTool.KARAMBWAN_VESSEL)
			.regularCatches(FishingCatch.KARAMBWAN)
			.register(KARAMBWAN_SPOT, "fish");

		/**
		 * Net / Bait
		 */
		new FishingSpot(FishingTool.SMALL_FISHING_NET)
			.regularCatches(FishingCatch.SHRIMPS, FishingCatch.ANCHOVIES)
			.register(NET_BAIT, "small net");
		new FishingSpot(FishingTool.FISHING_ROD)
			.regularCatches(FishingCatch.SARDINE, FishingCatch.HERRING)
			.register(NET_BAIT, "bait");

		/**
		 * Lure / Bait
		 */
		new FishingSpot(FishingTool.FLY_FISHING_ROD)
			.regularCatches(FishingCatch.TROUT, FishingCatch.SALMON)
			.register(LURE_BAIT, "lure");
		new FishingSpot(FishingTool.FISHING_ROD)
			.regularCatches(FishingCatch.PIKE)
			.register(LURE_BAIT, "bait");
		/**
		 * Cage / Harpoon
		 */
		new FishingSpot(FishingTool.LOBSTER_POT)
			.regularCatches(FishingCatch.LOBSTER)
			.register(CAGE_HARPOON, "cage");
		new FishingSpot(FishingTool.HARPOON)
			.regularCatches(FishingCatch.TUNA, FishingCatch.SWORDFISH)
			.barehandCatches(FishingCatch.BARBARIAN_TUNA, FishingCatch.BARBARIAN_SWORDFISH)
			.register(CAGE_HARPOON, "harpoon");
		/**
		 * Net (big) / Harpoon
		 */
		new FishingSpot(FishingTool.BIG_FISHING_NET)
			.regularCatches(FishingCatch.MACKEREL, FishingCatch.COD, FishingCatch.BASS)
			.register(BIG_NET_HARPOON, "big net");
		new FishingSpot(FishingTool.HARPOON)
			.regularCatches(FishingCatch.SHARK)
			.barehandCatches(FishingCatch.BARBARIAN_SHARK)
			.register(BIG_NET_HARPOON, "harpoon");
		/**
		 * Net (small) / Harpoon
		 */
		new FishingSpot(FishingTool.SMALL_FISHING_NET)
			.regularCatches(FishingCatch.MONKFISH)
			.register(SMALL_NET_HARPOON, "net");
		new FishingSpot(FishingTool.HARPOON)
			.regularCatches(FishingCatch.SHARK)
			.barehandCatches(FishingCatch.BARBARIAN_SHARK)
			.register(SMALL_NET_HARPOON, "harpoon");
		/**
		 * Use-rod (Leaping)
		 */
		new FishingSpot(FishingTool.BARBARIAN_ROD)
			.regularCatches(FishingCatch.LEAPING_TROUT, FishingCatch.LEAPING_SALMON, FishingCatch.LEAPING_STURGEON)
			.register(USE_ROD, "use-rod");
		/**
		 * Cage (Dark crab)
		 */
		new FishingSpot(FishingTool.DARK_CRAB_POT)
			.regularCatches(FishingCatch.DARK_CRAB)
			.register(CAGE, "cage");

		/**
		 * Bait (Angler)
		 */
		new FishingSpot(FishingTool.ANGLER_ROD)
			.regularCatches(FishingCatch.ANGLERFISH)
			.register(BAIT, "bait");
		/**
		 * Minnows
		 */
		new FishingSpot(FishingTool.SMALL_FISHING_NET)
			.regularCatches(FishingCatch.MINNOWS)
			.register(MINNOWS, "small net");
		NPC minnow1 = new NPC(MINNOWS).spawn(2611, 3443, 0);
		NPC minnow2 = new NPC(MINNOWS).spawn(2610, 3444, 0);
		NPC minnow3 = new NPC(MINNOWS).spawn(2618, 3443, 0);
		NPC minnow4 = new NPC(MINNOWS).spawn(2619, 3444, 0);
		World.startEvent(e -> {
			while (true) {
				e.delay(20);
				moveMinnow(minnow1, minnow3);
				e.delay(4);
				moveMinnow(minnow2, minnow4);
			}
		});
		/**
		 * Infernal eel
		 */
		new FishingSpot(FishingTool.OILY_FISHING_ROD)
			.regularCatches(FishingCatch.INFERNAL_EEL)
			.register(INFERNO_EEL, "bait");
		/**
		 * Molten eel
		 */
//        new FishingSpot(FishingTool.SMALL_FISHING_NET)
//                .regularCatches(FishingCatch.MOLTEN_EEL)
//                .register(MOLTEN_EEL, "bait");
		/**
		 * Slimy Eel
		 */
		new FishingSpot(FishingTool.FISHING_ROD)
			.regularCatches(FishingCatch.SLIMY_EEL)
			.register(SLIMY_EEL, "bait");

		/**
		 * Sacred eel
		 */
		new FishingSpot(FishingTool.FISHING_ROD)
			.regularCatches(FishingCatch.SACRED_EEL)
			.register(SACRED_EEL, "bait");
	}

	private static void moveMinnow(NPC... minnows) {
		for (NPC minnow : minnows) {
			int x = minnow.getAbsX();
			int y = minnow.getAbsY();

			if (y == 3443) {
				if (x == 2609 || x == 2617)
					minnow.step(0, 1, StepType.WALK);
				else
					minnow.step(-1, 0, StepType.WALK);
			} else {
				if (x == 2612 || x == 2620)
					minnow.step(0, -1, StepType.WALK);
				else
					minnow.step(1, 0, StepType.WALK);
			}

			minnow.minnowsFish = false;
		}
	}

	private static double anglerBonus(Player player) {
		double bonus = 1.0;
		Item hat = player.getEquipment().get(Equipment.SLOT_HAT);
		Item top = player.getEquipment().get(Equipment.SLOT_CHEST);
		Item waders = player.getEquipment().get(Equipment.SLOT_LEGS);
		Item boots = player.getEquipment().get(Equipment.SLOT_FEET);

		if (hat != null && hat.getId() == 13258)
			bonus += 0.04;
		if (top != null && top.getId() == 13259)
			bonus += 0.08;
		if (waders != null && waders.getId() == 13260)
			bonus += 0.06;
		if (boots != null && boots.getId() == 13261)
			bonus += 0.02;

		/* Whole set gives an additional 0.5% exp bonus */
		if (bonus >= 0.3)
			bonus += 0.05;

		return bonus;
	}

}
