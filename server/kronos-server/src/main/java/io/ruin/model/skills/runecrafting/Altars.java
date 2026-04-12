package io.ruin.model.skills.runecrafting;

import io.ruin.api.utils.Random;
import io.ruin.cache.LocType;
import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.PerkTaskHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ASlayingExperience;
import io.ruin.model.activities.perktree.perks.OneWithTheRunes;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerCounter;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.item.actions.impl.AchievementLamp;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.item.actions.impl.storage.EssencePouch;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
public enum Altars {
	AIR(1, 5.0, 1438, 5527, 556, 34760, 34813, new Position(2841, 4830, 0), 34748, new Position(2983, 3293, 0), Essence.REGULAR, 11, 22800, PlayerCounter.CRAFTED_AIR, 133, 134, Pet.RIFT_GUARDIAN_AIR),

	MIND(2, 5.5, 1448, 5529, 558, 34761, 34814, new Position(2716, 2908, 0), 34749, new Position(2984, 3512, 0), Essence.REGULAR, 14, 21000, PlayerCounter.CRAFTED_MIND, 133, 134, Pet.RIFT_GUARDIAN_MIND),

	WATER(5, 6.0, 1444, 5531, 555, 34762, 34815, new Position(2726, 4832, 0), 34750, new Position(3183, 3167, 0), Essence.REGULAR, 19, 17200, PlayerCounter.CRAFTED_WATER, 136, 137, Pet.RIFT_GUARDIAN_WATER),
	EARTH(9, 6.5, 1440, 5535, 557, 34763, 34816, new Position(2655, 4830, 0), 34751, new Position(3305, 3472, 0), Essence.REGULAR, 29, 15200, PlayerCounter.CRAFTED_EARTH, 139, 140, Pet.RIFT_GUARDIAN_EARTH),
	FIRE(14, 7.0, 1442, 5537, 554, 34764, 34817, new Position(2574, 4849, 0), 34752, new Position(3312, 3253, 0), Essence.REGULAR, 35, 13500, PlayerCounter.CRAFTED_FIRE, 130, 131, Pet.RIFT_GUARDIAN_FIRE),
	BODY(20, 7.5, 1446, 5533, 559, 34765, 34818, new Position(2521, 4834, 0), 34753, new Position(3054, 3443, 0), Essence.REGULAR, 46, 11800, PlayerCounter.CRAFTED_BODY, 130, 131, Pet.RIFT_GUARDIAN_BODY),
	COSMIC(27, 8.0, 1454, 5539, 564, 34766, 34819, new Position(2162, 4833, 0), 34754, new Position(2410, 4377, 0), Essence.PURE, 59, 12500, PlayerCounter.CRAFTED_COSMIC, 133, 134, Pet.RIFT_GUARDIAN_COSMIC),
	LAW(54, 9.5, 1458, 5545, 563, 34767, 34820, new Position(2464, 4818, 0), 34755, new Position(2860, 3381, 0), Essence.PURE, 200, 13200, PlayerCounter.CRAFTED_LAW, 133, 134, Pet.RIFT_GUARDIAN_LAW),
	NATURE(44, 9.0, 1462, 5541, 561, 34768, 34821, new Position(2400, 4835, 0), 34756, new Position(2868, 3017, 0), Essence.PURE, 91, 10570, PlayerCounter.CRAFTED_NATURE, 139, 140, Pet.RIFT_GUARDIAN_NATURE),
	CHAOS(35, 8.5, 1452, 5543, 562, 34769, 34822, new Position(2281, 4837, 0), 34757, new Position(3062, 3590, 0), Essence.PURE, 74, 11080, PlayerCounter.CRAFTED_CHAOS, 133, 134, Pet.RIFT_GUARDIAN_CHAOS),
	DEATH(65, 10.0, 1456, 5547, 560, 34770, 34823, new Position(2208, 4830, 0), 34758, new Position(1862, 4639, 0), Essence.PURE, 99, 11900, PlayerCounter.CRAFTED_DEATH, 130, 131, Pet.RIFT_GUARDIAN_DEATH),
	ASTRAL(40, 8.7, 7938, 7938, 9075, 34771, 27898, new Position(2156, 3863, 0), 27898, new Position(2156, 3863, 0), Essence.PURE, 82, 9990, PlayerCounter.CRAFTED_ASTRAL, -1, -1, Pet.RIFT_GUARDIAN_ASTRAL),
	BLOOD(77, 23.8, 7938, 7938, 565, 27978, 27898, new Position(1727, 3825, 0), 27898, new Position(1727, 3825, 0), Essence.DARK, -1, 8500, PlayerCounter.CRAFTED_BLOOD, -1, -1, Pet.RIFT_GUARDIAN_BLOOD),
	SOUL(90, 29.7, 7938, 7938, 566, 27980, 27898, new Position(1820, 3862, 0), 27898, new Position(1820, 3862, 0), Essence.DARK, -1, 8000, PlayerCounter.CRAFTED_SOUL, -1, -1, Pet.RIFT_GUARDIAN_SOUL),
	WRATH(95, 8, 22118, 22121, 21880, 34772, 34824, new Position(2335, 4827, 0), 34759, new Position(2448, 2825, 0), Essence.PURE, -1, 7500, PlayerCounter.CRAFTED_WRATH, -1, -1, Pet.RIFT_GUARDIAN_WRATH);

	public final int levelRequirement, talisman, tiara, runeID, altarObj, entranceObj, exitObj, multiplier, petOdds, imbueProjectile, imbueExplosion;
	public final double experience;
	public final Position entranceTile, exitTile;
	public final Essence essence;
	public final PlayerCounter counter;
	public final String talismanName;
	public final Pet pet;

	Altars(int levelRequirement, double experience, int talisman, int tiara, int runeID, int altarObj, int entranceObj, Position entranceTile, int exitObj,
	       Position exitTile, Essence essence, int multiplier, int petOdds, PlayerCounter counter, int imbueProjectile, int imbueExplosion, Pet pet) {
		this.levelRequirement = levelRequirement;
		this.experience = experience;
		this.talisman = talisman;
		this.talismanName = talisman != -1 ? ObjType.get(talisman).descriptiveName : "";
		this.tiara = tiara;
		this.runeID = runeID;
		this.altarObj = altarObj;
		this.entranceObj = entranceObj;
		this.entranceTile = entranceTile;
		this.exitObj = exitObj;
		this.exitTile = exitTile;
		this.essence = essence;
		this.multiplier = multiplier;
		this.petOdds = petOdds;
		this.counter = counter;
		this.imbueProjectile = imbueProjectile;
		this.imbueExplosion = imbueExplosion;
		this.pet = pet;
	}

	private enum RuneCombination {
		/* Mist runes */
		MIST_RUNES_AIR_ALTAR(4695, 34760, 555, 1444, 6, 8.0),
		MIST_RUNES_WATER_ALTAR(4695, 34762, 556, 1438, 8, 8.5),

		/* Dust runes */
		DUST_RUNES_AIR_ALTAR(4696, 34760, 557, 1440, 10, 8.3),
		DUST_RUNES_EARTH_ALTAR(4696, 34763, 556, 1438, 10, 9.0),

		/* Mud runes */
		MUD_RUNES_WATER_ALTAR(4698, 34762, 557, 1440, 13, 9.0),
		MUD_RUNES_EARTH_ALTAR(4698, 34763, 555, 1444, 13, 9.5),

		/* Smoke runes */
		SMOKE_RUNES_AIR_ALTAR(4697, 34760, 554, 1442, 15, 8.5),
		SMOKE_RUNES_FIRE_ALTAR(4697, 34764, 556, 1438, 15, 9.5),

		/* Steam runes */
		STEAM_RUNES_WATER_ALTAR(4694, 34762, 554, 1442, 19, 9.5),
		STEAM_RUNES_FIRE_ALTAR(4694, 34764, 555, 1444, 19, 10.0),

		/* Lava runes */
		LAVA_RUNES_EARTH_ALTAR(4699, 34763, 554, 1442, 23, 10.0),
		LAVA_RUNES_FIRE_ALTAR(4699, 34764, 557, 1440, 23, 10.5);

		public final int combinationRuneId, altar, requiredRuneId, requiredTalismanId, levelReq;
		public final double exp;
		public final String runeName, runeNameLowercase, requiredTalismanName, requiredRuneName;

		RuneCombination(int combinationRuneId, int altar, int requiredRuneId, int requiredTalismanId, int levelReq, double exp) {
			this.combinationRuneId = combinationRuneId;
			this.altar = altar;
			this.requiredRuneId = requiredRuneId;
			this.requiredTalismanId = requiredTalismanId;
			this.levelReq = levelReq;
			this.exp = exp;
			this.runeName = ObjType.get(combinationRuneId).name + "s";
			this.runeNameLowercase = ObjType.get(combinationRuneId).name.toLowerCase() + "s";
			this.requiredTalismanName = ObjType.get(requiredTalismanId).name.toLowerCase();
			this.requiredRuneName = ObjType.get(requiredRuneId).name.toLowerCase() + "s";
		}

		public static final RuneCombination[] VALUES = values();
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

	private void runeConversation(Player player, Altars altar) {
		if (!player.getStats().check(StatType.Runecrafting, altar.levelRequirement, "infuse these runes"))
			return;
		player.startEvent(e -> {
			int essenceCount = 0, fromPouches = 0;
			if (altar.essence == Essence.DARK) {
				Item fragments = player.getInventory().findItem(Essence.DARK.id);
				if (fragments != null) {
					essenceCount = player.darkEssFragments;
					player.darkEssFragments = 0;
					fragments.remove();
				}
			} else {
				ArrayList<Item> essences;
				if (altar.essence == Essence.PURE) {
					essences = player.getInventory().collectItems(Essence.PURE.id);
					essenceCount += (fromPouches = essenceFromPouches(player));
				} else
					essences = player.getInventory().collectItems(Essence.REGULAR.id, Essence.PURE.id);
				if (essences != null) {
					for (Item ess : essences)
						ess.remove();
					essenceCount += essences.size();
				}
				if (fromPouches > 0)
					clearPouches(player);
			}
			if (essenceCount == 0) {
				player.dialogue(new MessageDialogue("You do not have any " + altar.essence.name + " to bind."));
				return;
			}
			int runesPerEssence = 1;
			runesPerEssence *= player.getRunePercentageMultiplication();
			if (altar.multiplier != -1) {
				int addition = player.getStats().get(StatType.Runecrafting).currentLevel / altar.multiplier;
				if (addition > 0)
					runesPerEssence += addition;
			}
			player.lock(LockType.FULL_DELAY_DAMAGE);
			player.animate(791);
			player.graphics(186, 100, 0);
			e.delay(4);
			int amount = essenceCount * runesPerEssence;
			player.runecraftedRunesCounter += amount;
			DailyTasks.handleItemObtained(player, altar.runeID, StatType.Runecrafting, amount);
			PerkTaskHandler.handleGatherResource(player, altar.runeID, amount);
			if (player.runecraftedRunesCounter == Achievements.THE_POWER_WITHIN.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.THE_POWER_WITHIN.getAchievementName());

			if (altar.runeID == ASTRAL.runeID) {
				player.astralsCrafted += amount;
				if (player.astralsCrafted == Achievements.ASTRALWORLD.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.ASTRALWORLD.getAchievementName());
			}

			if (altar.runeID == BLOOD.runeID) {
				player.bloodsCrafted += amount;
				if (player.bloodsCrafted == Achievements.REAPING_THE_BENEFITS.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.REAPING_THE_BENEFITS.getAchievementName());
			}

			if (altar.runeID == WRATH.runeID) {
				player.wrathsCrafted += amount;
				if (player.wrathsCrafted == Achievements.FEEL_MY_WRATH.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.FEEL_MY_WRATH.getAchievementName());
			}
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ONE_WITH_THE_RUNES)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ONE_WITH_THE_RUNES);
				OneWithTheRunes c = (OneWithTheRunes) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				double multiplier = 1;
				multiplier += c.getRuneMultiplier();
				amount *= multiplier;
			}
			player.getInventory().add(altar.runeID, amount);
			double experience = essenceCount * altar.experience;
			if (player.wearingFullRaimentsOfTheEye())
				experience *= 1.3;
			player.getStats().addXp(StatType.Runecrafting, experience, true);
			counter.increment(player, amount);
			int petOdds = altar.petOdds / essenceCount;
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
				ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				petOdds *= c.getPetChanceBoost();
			}
			if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
				petOdds *= 0.85F;
			if (player.petDropBonus.isDelayed())
				petOdds *= 0.8;
			petOdds *= getPetDonatorBoost(player);
			if (Random.get(petOdds) == 0)
				altar.pet.unlock(player, 0);
			if (player.pet != null && player.pet.npcId >= 7354 && player.pet.npcId <= 7367 && VarPlayerRepository.PET_NPC_INDEX.get(player) != -1) {
				NPC npc = World.getNpc(VarPlayerRepository.PET_NPC_INDEX.get(player));
				if (npc != null && npc.ownerId == player.getUserId()) {
					// changes form based on altar you use
					npc.transform(altar.pet.npcId);
				}
			}
			player.unlock();
		});
	}

	protected static int essenceFromPouches(Player player) {
		return player.runeEssencePouches.entrySet().stream()
			.filter(e -> player.getInventory().contains(e.getKey().getItemId(), 1))
			.mapToInt(Map.Entry::getValue)
			.sum();
	}

	protected static void clearPouches(Player player) {
		player.runeEssencePouches.entrySet().stream()
			.filter(e -> player.getInventory().contains(e.getKey().getItemId(), 1))
			.forEach(e -> e.setValue(0));
	}

	private static int removeFromPouches(Player player, int amount) {
		int removed = 0;
		for (Map.Entry<EssencePouch, Integer> entry : player.runeEssencePouches.entrySet()) {
			if (!player.getInventory().contains(entry.getKey().getItemId(), 1) || entry.getValue() == 0)
				continue;
			int fromPouch = Math.min(amount - removed, entry.getValue());
			entry.setValue(entry.getValue() - fromPouch);
			removed += fromPouch;
			if (removed >= amount)
				return removed;
		}
		return removed;
	}

	private static void runeCombining(Player player, RuneCombination runeCombination) {
		boolean magicImbued = player.magicImbueEffect.isDelayed();
		if (!magicImbued) {
			Item requiredTalisman = player.getInventory().findItem(runeCombination.requiredTalismanId);
			if (requiredTalisman == null) {
				player.sendMessage("You need a " + runeCombination.requiredTalismanName + " to bind " + runeCombination.runeNameLowercase + ".");
				return;
			}
		}
		Item requiredRunes = player.getInventory().findItem(runeCombination.requiredRuneId);
		if (requiredRunes == null) {
			player.sendMessage("You need " + runeCombination.requiredRuneName + " to bind " + runeCombination.runeNameLowercase + ".");
			return;
		}
		Item pureEssence = player.getInventory().findItem(Essence.PURE.id);
		if (pureEssence == null) {
			player.sendMessage("You need pure essence to bind " + runeCombination.runeNameLowercase + ".");
			return;
		}

		int amountToCombine = Math.min(requiredRunes.getAmount(), pureEssence.count());
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ONE_WITH_THE_RUNES)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ONE_WITH_THE_RUNES);
			OneWithTheRunes c = (OneWithTheRunes) player.getPlayerPerkHandler().
				getActivePerks(player).get(perkIndex).getPerk(player);
			double multiplier = 1;
			multiplier += c.getRuneMultiplier();
			amountToCombine *= multiplier;
		}

		int finalAmountToCombine = amountToCombine;
		player.startEvent(event -> {
			player.lock();
			requiredRunes.remove(finalAmountToCombine);
			player.getInventory().remove(pureEssence.getId(), finalAmountToCombine);
			player.animate(791);
			player.graphics(186, 100, 0);
			player.getStats().addXp(StatType.Runecrafting, runeCombination.exp * finalAmountToCombine, true);

			/* 50% chance of success without binding necklace */
			if (Random.rollDie(2, 1) && !hasNecklace(player)) {
				player.sendMessage("You fail to combine the runes.");
			} else {
				player.getInventory().add(runeCombination.combinationRuneId, (int) (finalAmountToCombine * player.getRunePercentageMultiplication()));
				player.sendMessage("You bind the Temple's power into " + runeCombination.runeName + ".");
			}

			player.unlock();
		});
	}

	private static boolean hasNecklace(Player player) {
		Item necklace = player.getEquipment().get(Equipment.SLOT_AMULET);
		if (necklace == null)
			return false;

		if (necklace.getId() == BINDING_NECKLACE) {
			//TODO remove one charge
			return true;
		}
		return false;
	}

	private void createTiara(Player player, Altars altar) {
		Item talisman = player.getInventory().findItem(altar.talisman);
		if (talisman == null) {
			player.sendMessage("You need " + altar.talismanName + " to bind a Tiara here.");
			return;
		}
		Item tiara = player.getInventory().findItem(TIARA);
		if (tiara == null) {
			player.sendMessage("You need a Tiara to bind here.");
			return;
		}

		talisman.remove();
		tiara.setId(altar.tiara);
		player.getStats().addXp(StatType.Runecrafting, 25.0, true);
		player.sendMessage("You bind the power of the talisman into your Tiara.");
	}

	private void enterAltar(Player player, Item talisman, Altars altar) {
		player.startEvent(event -> {
			player.lock();
			player.animate(827);
			if (talisman != null)
				player.sendMessage("You hold the " + talisman.getDef().name.toLowerCase() + " talisman towards the mysterious ruins.");
			event.delay(2);
			player.sendMessage("You feel a powerful force take hold of you...");
			event.delay(1);
			player.getMovement().teleport(altar.entranceTile);
			player.unlock();
		});
	}

	private void exitAltar(Player player, Altars altar) {
		player.startEvent(event -> {
			player.lock();
			player.sendMessage("You step through the portal...");
			player.getMovement().teleport(altar.exitTile);
			event.delay(1);
			player.unlock();
		});
	}


	private static final int[] glowOffsetsX = {-3, 3, 3, -3};
	private static final int[] glowOffsetsY = {3, 3, -3, -3};

	private static final int[] deathOffsetsX = {-3, -1, 1, 3, 3, 1, -1, -3};
	private static final int[] deathOffsetsY = {1, 3, 3, 1, -1, -3, -3, 1};

	private static void imbueStaff(Player player, Altars type, GameObject altar) {
		if (!player.getStats().check(StatType.Runecrafting, 68, "create battlestaves")) {
			return;
		}
		int staves = player.getInventory().getAmount(1379);
		int invEss = player.getInventory().getAmount(7936);
		int pouchEss = essenceFromPouches(player);
		int toMake = Math.min(staves, (invEss + pouchEss) / 4);
		if (toMake == 0) {
			player.sendMessage("You'll need 4 pure essence for each staff you want to imbue.");
			return;
		}
		Projectile proj = new Projectile(type.imbueProjectile, 70, 55, 0, 150, 0, 16, 0);
		player.addEvent(event -> {
			player.lock();
			player.animate(832);
			player.getInventory().remove(1379, toMake);
			player.sendMessage("You place the " + (toMake > 1 ? "staves" : "staff") + " on the altar.");
			event.delay(2);
			player.animate(791);
			player.graphics(186, 100, 0);
			player.sendMessage("You start channeling the altar's energy through your essence...");
			int removed = removeFromPouches(player, toMake * 4);
			if (removed < toMake * 4) {
				player.getInventory().remove(7936, (toMake * 4) - removed);
			}
			int destX = altar.x + 1;
			int destY = altar.y + 1;
			int[] offsetsX = type == DEATH ? deathOffsetsX : glowOffsetsX;
			int[] offsetsY = type == DEATH ? deathOffsetsY : glowOffsetsY;
			for (int i = 0; i < offsetsX.length; i++) {
				proj.send(destX + offsetsX[i], destY + offsetsY[i], destX, destY);
			}
			event.delay(4);
			player.sendMessage("The energy improves the " + (toMake > 1 ? "staves" : "staff") + "!");
			World.sendGraphics(type.imbueExplosion, 200, 0, destX, destY, altar.z);
			player.getStats().addXp(StatType.Runecrafting, 6.0 * toMake, true);
			event.delay(1);
			player.animate(832);
			player.getInventory().add(1391, toMake);
			player.sendMessage("You take the imbued " + (toMake > 1 ? "staves" : "staff") + ".");
			player.unlock();
		});
	}

	private static final int TIARA = 5525;
	private static final int BINDING_NECKLACE = 5521;

	public static void register() {
		for (Altars altar : values()) {

			if (altar.tiara > 0 && altar.entranceObj > 0) {
				LocType objectDef = LocType.get(altar.entranceObj);
				if (objectDef.varpBitId > 0) {
					VarPlayerRepository altarVarp = VarPlayerRepository.varpbit(objectDef.varpBitId, true);
					ObjType tiaraDef = ObjType.get(altar.tiara);
					//  tiaraDef.registerEquip((player, item) -> {
					//  altarVarp.set(player, 1);
					//   player.sendMessage("You can see");
					//});
					//  tiaraDef.registerUnequip((player, item) -> {
					//     altarVarp.set(player, 0);
					//      player.sendMessage("You can't see");
					//   });
					//   }
				}
				/**
				 * Altar
				 */
				ObjectAction.register(altar.altarObj, "craft-rune", (player, obj) -> altar.runeConversation(player, altar));
				ObjectAction.register(altar.altarObj, "bind", (player, obj) -> altar.runeConversation(player, altar));

				/**
				 * Entrance & Exit
				 */
				ObjectAction.register(altar.exitObj, "use", (player, obj) -> altar.exitAltar(player, altar));
				ItemObjectAction.register(altar.talisman, altar.entranceObj, (player, item, obj) -> altar.enterAltar(player, item, altar));

				/**
				 * Tiara binding
				 */
				if (altar.talisman != -1 && altar.altarObj != -1) {
					ItemObjectAction.register(altar.talisman, altar.altarObj, (player, item, obj) -> altar.createTiara(player, altar));
					ItemObjectAction.register(TIARA, altar.altarObj, (player, item, obj) -> altar.createTiara(player, altar));
				}

				if (altar.entranceObj > 0 && altar.altarObj != -1) {
					ItemObjectAction.register(1379, altar.altarObj, (player, item, obj) -> imbueStaff(player, altar, obj));
					ObjectAction.register(altar.entranceObj, 1, (player, obj) -> altar.enterAltar(player, null, altar));
				}


				/**
				 * Tiara binding & Rune combining
				 */
				ItemObjectAction.register(altar.altarObj, (player, item, obj) -> {
					if (altar.talisman != -1 && altar.altarObj != -1 && item.getId() == TIARA)
						altar.createTiara(player, altar);


					for (RuneCombination runeCombination : RuneCombination.VALUES) {
						if (item.getId() == runeCombination.requiredRuneId || item.getId() == runeCombination.requiredTalismanId) {
							if (altar.altarObj == runeCombination.altar)
								runeCombining(player, runeCombination);
						}
					}

					if (item.getId() == altar.talisman) {
						altar.createTiara(player, altar);
					}
				});
			}
		}
	}
}
