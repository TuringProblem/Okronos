package io.ruin.model.stat;

import com.google.gson.annotations.Expose;
import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.model.World;
import io.ruin.model.activities.perktree.PerkSets;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ExperienceEnhancer;
import io.ruin.model.activities.perktree.perksets.SkilledMind;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.content.itembreaking.ItemBreakPerkHandler;
import io.ruin.model.content.upgrade.ItemEffect;
import io.ruin.model.entity.player.BeginnerUpgradesHandler;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.skillcapes.HitpointsSkillCape;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.Broadcast;
import io.ruin.utility.Misc;

import java.util.Arrays;
import java.util.List;

public class StatList {

	public transient int total99s;
	public transient int totalLevel;
	public transient long totalXp;
	private transient Player player;

	private Stat[] stats;
	private StatCounter[] counters;

	public void init(Player player) {
		this.player = player;
		if (stats == null) {
			StatType[] types = StatType.VALUES;
			stats = new Stat[types.length];
			for (int id = 0; id < types.length; id++) {
				StatType type = types[id];
				if (type == StatType.Hitpoints)
					stats[id] = new Stat(10, 1154);
				else
					stats[id] = new Stat(1, 0);
				stats[id].updated = true;
			}
			return;
		}
		if (counters == null) {
			counters = new StatCounter[stats.length + 1];
			for (int i = 0; i < counters.length; i++)
				counters[i] = new StatCounter(i);
		} else {
			for (int i = 0; i < counters.length; i++) {
				StatCounter counter = counters[i];
				counter.index = i;
				counter.send(player); // i don't think we need to send if we have configs save ????
			}
		}
		for (Stat stat : stats) {
			stat.fixedLevel = Stat.levelForXp(stat.experience);
			stat.updated = true;
		}
	}

	public void set(StatType type, int level) {
		set(type, level, Stat.xpForLevel(level));
	}

	public void set(StatType type, int level, int experience) {
		Stat stat = get(type);
		stat.currentLevel = stat.fixedLevel = level;
		stat.experience = experience;
		stat.updated = true;
	}

	public boolean check(StatType type, int lvlReq) {
		return get(type).currentLevel >= lvlReq;
	}

	public boolean has200MExperience(StatType type) {
		return get(type).experience >= 200_000_000;
	}

	/**
	 * Checks the fixed (not boosted) level.
	 */
	public boolean checkFixed(StatType type, int levelReq, String action) {
		if (get(type).fixedLevel < levelReq) {
			player
				.sendMessage("You need " + type.descriptiveName + " level of " + levelReq + " or higher to " + action + ".");
			return false;
		}
		return true;
	}

	public boolean check(StatType type, int levelReq, String action) {
		if (!check(type, levelReq)) {
			player
				.sendMessage("You need " + type.descriptiveName + " level of " + levelReq + " or higher to " + action + ".");
			return false;
		}
		return true;
	}

	public boolean check(StatType type, int lvlReq, int itemId, String action) {
		if (!check(type, lvlReq)) {
			player.dialogue(new ItemDialogue().one(itemId,
				"You need " + type.descriptiveName + " level of " + lvlReq + " or higher to " + action + "."));
			return false;
		}
		return true;
	}

	public boolean check(StatType type, int lvlReq, int itemId1, int itemId2, String action) {
		if (!check(type, lvlReq)) {
			player.dialogue(new ItemDialogue().two(itemId1, itemId2,
				"You need " + type.descriptiveName + " level of " + lvlReq + " or higher to " + action + "."));
			return false;
		}
		return true;
	}

	public void restore(boolean restoreBoosted) {
		for (Stat stat : stats) {
			if (restoreBoosted && stat.currentLevel > stat.fixedLevel)
				continue;
			stat.alter(stat.fixedLevel);
		}
	}

	public void addXp(StatType type, double amount, boolean useMultiplier) {

		int statId = type.ordinal();
		Stat stat = stats[statId];
		double baseAmount = amount;

		for (Item item : player.getEquipment().getItems()) {
			if (item != null && item.getDef() != null) {
				List<String> upgrades = AttributeExtensions.getEffectUpgrades(item);
				boolean hasEffect = upgrades != null;
				if (hasEffect) {
					for (String s : upgrades) {
						try {
							if (s.equalsIgnoreCase("empty"))
								continue;
							ItemEffect effect = ItemEffect.valueOf(s);
							amount *= effect.getUpgrade().giveExperienceBoost(player, type);
						} catch (Exception ex) {
							System.err.println("Unknown upgrade { " + s + " } found!");
							ex.printStackTrace();
						}
					}
				}
			}
		}
		if (type == StatType.Agility || type == StatType.Prayer || type == StatType.Farming || type == StatType.Runecrafting
			|| type == StatType.Construction || type == StatType.Crafting
			|| type == StatType.Hunter || type == StatType.Thieving || type == StatType.Slayer || type == StatType.Fletching
			|| type == StatType.Cooking || type == StatType.Firemaking
			|| type == StatType.Fishing || type == StatType.Herblore || type == StatType.Mining || type == StatType.Smithing
			|| type == StatType.Woodcutting || type == StatType.Magic) {
			if (useMultiplier) {
				player.getSecurityGuard().rollGuardSpawnChance(player, 500);
			}
		}
		double chance = 50_000 - (amount * 15);

		// do pre exp drop here

		if (player.experienceLock) {
			return;
		}

		if (type.isCombat()) {
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && useMultiplier) {
				if (player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 30480 ||
					player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 30479 ||
					player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 30478) {
					amount *= 1.25;
				}
			}
			if (player.getEquipment().get(Equipment.SLOT_AMULET) != null && useMultiplier) {
				if (player.getEquipment().get(Equipment.SLOT_AMULET).getId() == 30477
					&& player.getStats().get(type).fixedLevel < 75) {
					amount *= 1.3;
				}

			}
		}

		// if(useMultiplier) {
		// if(World.xpMultiplier > 0)
		// amount += baseAmount * (World.xpMultiplier - 1);
		// amount += baseAmount * (Wilderness.getXPModifier(player, type));

		// }
		/*
		 * XP Modes
		 */
		if (useMultiplier) {
			amount *= player.getDifficulty().GetExperienceBoost();
			// if (stat.fixedLevel >= 99) {
			// amount *= player.xpMode.getAfter99Rate();
			// } else {
			// if (type.isCombat()) {
			// amount *= player.xpMode.getCombatRate();
			// } else {
			// amount *= player.xpMode.getSkillRate();
			// }
			// }

		}
		if (useMultiplier) {
			ItemBreakPerkHandler.handleQuickStepPerk(player, amount);
		}

		/**
		 * 50% experience boost from scroll
		 */
		if (player.doubleExpTimer.isDelayed() && useMultiplier)
			amount *= 2.00;
		if (player.expBonus.isDelayed() && useMultiplier)
			amount *= 2.00;
		/**
		 * 10% experience boost from first 3 days
		 */
		if (player.first3.isDelayed() && useMultiplier)
			amount *= 1.10;
		if (player.getPlayerPerkHandler().getActivePerkSets(player).contains(PerkSets.SKILLED_MIND) && useMultiplier) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkSetIndex(player, PerkSets.SKILLED_MIND);
			SkilledMind c = (SkilledMind) player.getPlayerPerkHandler().getActivePerkSets(player).get(perkIndex).perkSet();
			if (Random.rollPercent(c.getChanceToDoubleExperience()) && useMultiplier)
				amount *= 2.0;
		}
		/**
		 * 25% experience boost inside the wilderness
		 */
		if (player.wildernessLevel > 1 && useMultiplier)
			amount *= 1.25;

		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.EXPERIENCE_ENHANCER) && useMultiplier) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.EXPERIENCE_ENHANCER);
			ExperienceEnhancer c = (ExperienceEnhancer) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
				.getPerk(player);
			float multiplier = 1;
			assert c != null;
			multiplier += c.getExperienceBoost();
			amount *= multiplier;
		}

		/**
		 * 2x weekend experience boost
		 */

		if (World.doubleExpActive && useMultiplier)
			amount *= 2;
		if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.DOUBLE_EXPERIENCE) && useMultiplier)
			amount *= 2;
		double newXp = stat.experience + amount;
		if (newXp > Stat.MAX_XP)
			newXp = Stat.MAX_XP;

		if (newXp >= 200_000_000 && stat.experience < 200_000_000) {
			player.sendMessage(Color.ORANGE_RED.tag() + "Congratulations on achieving 200M experience in " + type.name()
				+ " on " + Misc.format_string(player.getDifficulty().name().toLowerCase()) + " mode");
			player.sendMessage(Color.ORANGE_RED.tag() + "You receive " + player.getDifficulty().GetMaxExpReward()
				+ " Reason points for achieving this!");
			player.updateReasonPoints(player.getDifficulty().GetMaxExpReward());
			Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(),
				" has just achieved 200M experience in " + type.name() + " on "
					+ Misc.format_string(player.getDifficulty().name().toLowerCase()) + " mode!");
		}
		if (newXp >= 500_000_000 && stat.experience < 500_000_000) {
			player.sendMessage(Color.ORANGE_RED.tag() + "Congratulations on achieving 500M experience in " + type.name()
				+ " on " + Misc.format_string(player.getDifficulty().name().toLowerCase()) + " mode");
			player.sendMessage(Color.ORANGE_RED.tag() + "You receive " + player.getDifficulty().GetMaxExpReward2()
				+ " Reason points for achieving this!");
			player.updateReasonPoints(player.getDifficulty().GetMaxExpReward2());
			Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(),
				" has just achieved 500M experience in " + type.name() + " on "
					+ Misc.format_string(player.getDifficulty().name().toLowerCase()) + " mode!");
		}
		if (newXp >= 1_000_000_000 && stat.experience < 1_000_000_000) {
			player.sendMessage(Color.ORANGE_RED.tag() + "Congratulations on achieving 1B experience in " + type.name()
				+ " on " + Misc.format_string(player.getDifficulty().name().toLowerCase()) + " mode");
			player.sendMessage(Color.ORANGE_RED.tag() + "You receive " + player.getDifficulty().GetMaxExpReward3()
				+ " Reason points for achieving this!");
			player.updateReasonPoints(player.getDifficulty().GetMaxExpReward3());
			Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(),
				" has just achieved 1B experience in " + type.name() + " on "
					+ Misc.format_string(player.getDifficulty().name().toLowerCase()) + " mode!");
		}

		stat.experience = newXp;
		stat.updated = true;

		int oldLevel = stat.fixedLevel;
		int newLevel = stat.fixedLevel = Stat.levelForXp(stat.experience);
		int gain = newLevel - oldLevel;
		if (gain == 0) {
			/* level did not change */
			return;
		}
		if (stat.currentLevel < stat.fixedLevel) {
			if (type == StatType.Hitpoints || type == StatType.Prayer) {
				if (stat.currentLevel == oldLevel)
					stat.currentLevel += gain;
			} else {
				stat.currentLevel += gain;
			}
		}
		player.graphics(199, 124, 0); // todo add the new gfx !!
		player.sendMessage(
			"You've just advanced " + type.descriptiveName + " level. You have reached level " + newLevel + ".");
		if (newLevel == 99) {
			player.sendMessage(Color.ORANGE_RED.tag() + "Congratulations on achieving level 99 in " + type.name() + " on "
				+ Misc.format_string(player.getDifficulty().name().toLowerCase()) + " mode");
			player.sendMessage(Color.ORANGE_RED.tag() + "You receive " + player.getDifficulty().GetMaxLevelRewardBonus()
				+ " reason points for achieving this!");
			player.updateReasonPoints(player.getDifficulty().GetMaxLevelRewardBonus());
			Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(),
				" has just achieved level 99 in " + type.name() + " on "
					+ Misc.format_string(player.getDifficulty().name().toLowerCase()) + " mode!");
			if (player.getStats().total99s >= 22) {
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(),
					" has just achieved level 99 in every skill on "
						+ Misc.format_string(player.getDifficulty().name().toLowerCase()) + " mode!");
				player.updateReasonPoints(player.getDifficulty().GetMaxLevelRewardBonus() * 5);
				player.sendMessage(
					"You have received " + NumberUtils.formatNumber(player.getDifficulty().GetMaxLevelRewardBonus() * 5L)
						+ " reason points for achieving this!");
			}
		}
		if (statId <= 6)
			player.getCombat().updateLevel();
		if (player.getEquipment().get(Equipment.SLOT_AMULET) != null) {
			if (player.getEquipment().get(Equipment.SLOT_AMULET).getId() == 30477) {
				switch (statId) {
					case 0:
						BeginnerUpgradesHandler.HandleMeleeWeaponUpgrade(player);
						break;
					case 1:
						BeginnerUpgradesHandler.HandleMageBottomsUpgrade(player);
						BeginnerUpgradesHandler.HandleMageHatUpgrade(player);
						BeginnerUpgradesHandler.HandleMageTopUpgrade(player);
						BeginnerUpgradesHandler.HandleMeleeBodyUpgrade(player);
						BeginnerUpgradesHandler.HandleMeleeHelmUpgrade(player);
						BeginnerUpgradesHandler.HandleMeleeLegsUpgrade(player);
						BeginnerUpgradesHandler.HandleRangeBodyUpgrade(player);
						break;
					case 4:
						BeginnerUpgradesHandler.HandleRangeWeaponUpgrade(player);
						BeginnerUpgradesHandler.HandleRangeBodyUpgrade(player);
						BeginnerUpgradesHandler.HandleRangeChapsUpgrade(player);
						BeginnerUpgradesHandler.HandleRangeHelmUpgrade(player);
						break;
					case 6:
						BeginnerUpgradesHandler.HandleMageTopUpgrade(player);
						BeginnerUpgradesHandler.HandleMageBottomsUpgrade(player);
						BeginnerUpgradesHandler.HandleMageHatUpgrade(player);
						break;
				}
			}

		}
	}

	public void process() {
		boolean rapidRestore = player.getPrayer().isActive(Prayer.RAPID_RESTORE);
		boolean rapidHeal = player.getPrayer().isActive(Prayer.RAPID_HEAL) || HitpointsSkillCape.wearsHitpointsCape(player);
		boolean preserve = player.getPrayer().isActive(Prayer.PRESERVE);
		StatType[] types = StatType.VALUES;
		totalLevel = 0;
		totalXp = 0;
		total99s = 0;
		for (int statId = 0; statId < types.length; statId++) {
			Stat stat = stats[statId];
			StatType type = types[statId];
			int eternalRegenLevel = 0;
			if (player.wildernessLevel < 1) {
				List<Integer> equipmentSlots = Arrays.asList(Equipment.SLOT_CHEST, Equipment.SLOT_LEGS, Equipment.SLOT_HAT,
					Equipment.SLOT_RING, Equipment.SLOT_AMULET, Equipment.SLOT_CAPE);
				for (Integer equipmentSlot : equipmentSlots) {
					if (player.getEquipment().get(equipmentSlot) != null) {
						if (AttributeExtensions.hasAttribute(player.getEquipment().get(equipmentSlot),
							AttributeTypes.ETERNAL_REGENERATION)) {
							eternalRegenLevel = AttributeExtensions.getCharges(AttributeTypes.ETERNAL_REGENERATION,
								player.getEquipment().get(equipmentSlot));
							break;
						}
					}
				}
			}
			if (type != StatType.Prayer)
				stat.process(type == StatType.Hitpoints, rapidRestore, rapidHeal, preserve, eternalRegenLevel);
			if (stat.updated) {
				stat.updated = false;
				player.getPacketSender().sendStat(statId, stat.currentLevel, (int) stat.experience);
			}
			if (stat.fixedLevel == 99)
				total99s++;
			totalLevel += stat.fixedLevel;
			totalXp += stat.experience;
		}
		player.getCombat().checkLevel();
	}

	public StatCounter getCounter(int slot) {
		return counters[slot];
	}

	public Stat get(int statId) {
		return stats[statId];
	}

	public Stat get(StatType type) {
		return stats[type.ordinal()];
	}

	public Stat[] get() {
		return stats;
	}

	public boolean check(StatRequirement statRequirement) {
		return statRequirement.hasRequirement(player);
	}

	public long getTotalXp() {
		long total = 0;
		for (int i = 0; i < stats.length; i++) {
			total += stats[i].experience;
		}
		return total;
	}
}
