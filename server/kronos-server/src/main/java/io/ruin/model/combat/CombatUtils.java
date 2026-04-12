package io.ruin.model.combat;

import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.handlers.EquipmentStats;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.chargable.Blowpipe;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.stat.StatList;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;

public class CombatUtils {

	private static int NO_CAP = 999;
	// {weapon_id, max_hit, max_hit_special}
	private static int[][] WEAPONS_MAX_HIT_CAPS = new int[][] {
			{ ItemID.HEAVY_BALLISTA, 61, 76 },
			{ ItemID.LIGHT_BALLISTA, 57, 71 },
			{ ItemID.DARK_BOW, NO_CAP, 48 },
			// voidwaker
			{ 12765, 87, NO_CAP },
			{ ItemID.DRAGON_CLAWS, NO_CAP, 89 },

			{ ItemID.DRAGON_DAGGER, NO_CAP, 50 },
			{ ItemID.DRAGON_DAGGER_20407, NO_CAP, 50 },
			{ ItemID.DRAGON_DAGGERP, NO_CAP, 50 },

			{ ItemID.ARMADYL_GODSWORD, NO_CAP, 87 },
			{ ItemID.ARMADYL_GODSWORD_20593, NO_CAP, 87 },
			{ ItemID.ARMADYL_GODSWORD_22665, NO_CAP, 87 },
			{ ItemID.ARMADYL_GODSWORD_OR, NO_CAP, 87 },
	};

	/**
	 * Magic calcs
	 */

	private static final int[] MAGIC_CALC_SLOTS = { Equipment.SLOT_CHEST, Equipment.SLOT_LEGS };

	private static double getMagicInterference(Player player) {
		int interferenceCount = 0;
		for (int slot : MAGIC_CALC_SLOTS) {
			ObjType def = player.getEquipment().getDef(slot);
			if (def == null || def.equipBonuses == null)
				continue;
			/**
			 * Things like Rune armor, D'hide, etc.
			 */
			if (def.equipBonuses[EquipmentStats.MAGIC_ATTACK] < 0)
				interferenceCount++;
		}
		return interferenceCount * 0.45;
	}

	/**
	 * Attack calcs
	 */

	private static double getEffectiveAttack(Entity entity, StatType statType, AttackType attackType) {
		double effectiveAttack = entity.getCombat().getLevel(statType);
		if (statType == StatType.Magic) {
			if (entity.player != null) {
				effectiveAttack *= (1D + entity.player.getPrayer().magicAttackBoost);
				double interference = getMagicInterference(entity.player);
				if (interference > 0)
					effectiveAttack *= (1D - interference);
			}
			if (attackType != null)
				effectiveAttack += (attackType == AttackType.ACCURATE ? 3 : 1);
		} else if (statType == StatType.Ranged) {
			if (entity.player != null)
				effectiveAttack *= (1D + entity.player.getPrayer().rangedAttackBoost);
			if (attackType == AttackType.ACCURATE)
				effectiveAttack += 3;
			else if (attackType == AttackType.LONG_RANGED)
				effectiveAttack += 1;
		} else {
			if (entity.player != null)
				effectiveAttack *= (1D + entity.player.getPrayer().attackBoost);
			if (attackType == AttackType.ACCURATE)
				effectiveAttack += 3;
			else if (attackType == AttackType.CONTROLLED)
				effectiveAttack += 1;
		}
		effectiveAttack += 8;

		if (entity.player != null && entity.player.isInvincible()) {
			effectiveAttack += 1000;
		}

		return effectiveAttack;
	}

	public static double getAttackBonus(Entity entity, AttackStyle attackStyle, AttackType attackType) {
		if (entity == null || entity.getCombat() == null)
			return 0;
		double effectiveAttack, bonus = 0;
		if (attackStyle == AttackStyle.MAGIC) {
			effectiveAttack = getEffectiveAttack(entity, StatType.Magic, attackType);
			bonus = entity.getCombat().getBonus(EquipmentStats.MAGIC_ATTACK);
		} else if (attackStyle == AttackStyle.RANGED || attackStyle == AttackStyle.MAGICAL_RANGED) {
			effectiveAttack = getEffectiveAttack(entity, StatType.Ranged, attackType);
			bonus = entity.getCombat().getBonus(EquipmentStats.RANGE_ATTACK);
		} else {
			effectiveAttack = getEffectiveAttack(entity, StatType.Attack, attackType);
			if (attackStyle == AttackStyle.STAB)
				bonus = entity.getCombat().getBonus(EquipmentStats.STAB_ATTACK);
			else if (attackStyle == AttackStyle.SLASH)
				bonus = entity.getCombat().getBonus(EquipmentStats.SLASH_ATTACK);
			else if (attackStyle == AttackStyle.CRUSH)
				bonus = entity.getCombat().getBonus(EquipmentStats.CRUSH_ATTACK);
			else if (attackStyle == AttackStyle.MAGICAL_MELEE)
				bonus = 0; // i know this was implied before but im leaving it explicit just to make it
										// clear
		}
		return effectiveAttack * (bonus + 64D);
	}

	/**
	 * Defence calcs
	 */

	private static double getEffectiveDefence(Entity entity) {
		AttackType type = entity.getCombat().getAttackType() == null ? null : entity.getCombat().getAttackType();
		double effectiveDefence = entity.getCombat().getLevel(StatType.Defence);
		if (entity.player != null)
			effectiveDefence *= (1D + entity.player.getPrayer().defenceBoost);
		if (type != null) {
			if (type == AttackType.DEFENSIVE || type == AttackType.LONG_RANGED)
				effectiveDefence += 3;
			else if (type == AttackType.CONTROLLED)
				effectiveDefence += 1;
		}
		effectiveDefence += 8;
		return effectiveDefence;
	}

	private static double getEffectiveMagicDefence(Entity entity) {
		double effectiveDefence = entity.getCombat().getLevel(StatType.Magic);
		if (entity.player != null)
			effectiveDefence *= (1D + entity.player.getPrayer().magicAttackBoost);
		return effectiveDefence;
	}

	public static double getDefenceBonus(Entity entity, AttackStyle attackStyle) {
		double effectiveDefence = getEffectiveDefence(entity);
		double bonus = 0;
		if (attackStyle == AttackStyle.MAGIC || attackStyle == AttackStyle.MAGICAL_RANGED
				|| attackStyle == AttackStyle.MAGICAL_MELEE) {
			effectiveDefence *= 0.30;
			effectiveDefence += getEffectiveMagicDefence(entity) * 0.70;
			bonus = entity.getCombat().getBonus(EquipmentStats.MAGIC_DEFENCE);
		} else {
			if (attackStyle == AttackStyle.STAB)
				bonus = entity.getCombat().getBonus(EquipmentStats.STAB_DEFENCE);
			else if (attackStyle == AttackStyle.SLASH)
				bonus = entity.getCombat().getBonus(EquipmentStats.SLASH_DEFENCE);
			else if (attackStyle == AttackStyle.CRUSH)
				bonus = entity.getCombat().getBonus(EquipmentStats.CRUSH_DEFENCE);
			else if (attackStyle == AttackStyle.RANGED)
				bonus = entity.getCombat().getBonus(EquipmentStats.RANGE_DEFENCE);
		}
		return effectiveDefence * (bonus + 64D);
	}

	/**
	 * Max damage (Melee & Ranged only!)
	 */

	private static int getEffectiveStrength(Entity entity, AttackType attackType) {
		int strengthLevel = (int) entity.getCombat().getLevel(StatType.Strength);

		double prayerBonus = 1;
		int styleBonus = 0;

		final Player player = entity.player;
		if (player != null) {
			prayerBonus += player.getPrayer().strengthBoost;

			if (AttackType.AGGRESSIVE.equals(attackType)) {
				styleBonus += 3;
			} else if (AttackType.CONTROLLED.equals(attackType)) {
				styleBonus += 1;
			}
		}

		int base = (int) Math.floor(
			Math.floor(
				strengthLevel * prayerBonus
			) + styleBonus + 8 + entity.getStrAdder()
		);

		return (int) Math.floor(base);
	}

	private static int getEffectiveRangedStrength(Entity entity, AttackType attackType) {
		int rangedLevel = (int) entity.getCombat().getLevel(StatType.Ranged);
		double prayerBonus = 0;
		int attackStyle = 0;

		final Player player = entity.player;
		if (player != null) {
			if (AttackType.ACCURATE.equals(attackType)) {
				attackStyle += 3;
			}

			prayerBonus = (1D + player.getPrayer().rangedStrengthBoost);
		}

		int innerFloor = (int) Math.floor(rangedLevel * prayerBonus);

		return innerFloor + attackStyle + 8 + entity.getStrAdder();
	}

	public static int getMaxMeleeDamage(Entity entity, AttackType attackType) {
		int effectiveStrength = getEffectiveStrength(entity, attackType);

		int strengthBonus = (int) entity.getCombat().getBonus(EquipmentStats.MELEE_STRENGTH);

		// Check for Crystal Blessing
		if (entity.isPlayer() && entity.player.getEquipment().contains(30384)) {
			if (entity.player.getEquipment().contains(ItemID.CRYSTAL_HELM)
				&& entity.player.getEquipment().contains(ItemID.CRYSTAL_BODY)
				&& entity.player.getEquipment().contains(ItemID.CRYSTAL_LEGS)) {
				strengthBonus += 31;
			}
		}

		double baseDamage = 0.5 + effectiveStrength * ( (strengthBonus + 64D) / 640D);
		return (int) Math.floor(baseDamage);
	}

	public static int getMaxRangedDamage(Entity entity, AttackType attackType) {
		int effectiveRangedStrength = getEffectiveRangedStrength(entity, attackType);

		int equipmentRangedStrength = (int) entity.getCombat().getBonus(EquipmentStats.RANGED_STRENGTH);

		if(entity.isPlayer() && entity.player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
			entity.player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 30374) {
			Item blowpipe = entity.player.getEquipment().get(Equipment.SLOT_WEAPON);
			var dartOrdinal = blowpipe.getAttributeInt(AttributeTypes.AMMO_ID, 0);
			var dart = Blowpipe.Dart.values()[dartOrdinal];
			equipmentRangedStrength += dart.getDartRangedStrengthBonus();
		}

		final Player player = entity.player;
		if (player != null && player.getEquipment().contains(ItemID.CRYSTAL_HELM)
			&& player.getEquipment().contains(ItemID.CRYSTAL_BODY)
			&& player.getEquipment().contains(ItemID.CRYSTAL_LEGS)) {

			var equipment = player.getEquipment();

			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
				if (equipment.isWearing(Equipment.SLOT_WEAPON,
					ItemID.CRYSTAL_HALBERD,
					ItemID.BLADE_OF_SAELDOR,
					24551, 24553, 25870, 25872, 25874, 25876, 25878, 25880)) {
					equipmentRangedStrength += 31;
				}

				if (equipment.isWearing(Equipment.SLOT_WEAPON, 23983, 25888, 25886, 25884, 25867, 25892, 25896, 25894, 25865)) {
					equipmentRangedStrength += 55;
				}
			}
		}

		double baseDamage = 0.5 + ( (effectiveRangedStrength * (equipmentRangedStrength + 64D)) / 640D);
		return (int) Math.floor(baseDamage);
	}

	public static int getMaxDamage(Entity entity, AttackStyle attackStyle, AttackType attackType) {
		return attackStyle.isRanged()
			? getMaxRangedDamage(entity, attackType)
			: getMaxMeleeDamage(entity, attackType);
	}

	public static int normalizeMaxHit(Player attacker, Player victim, int damage) {
		var eq = attacker.getEquipment();
		var isSpecialAttack = attacker.getCombat().attackedWithSpecial;

		for (var row : WEAPONS_MAX_HIT_CAPS) {
			if (!eq.isWearing(Equipment.SLOT_WEAPON, row[0])) {
				continue;
			}

			if (!isSpecialAttack) {
				return Math.min(damage, row[1]);
			}

			return Math.min(damage, row[2]);
		}

		return damage;
	}

	/**
	 * Experience
	 */

	public static void addXp(Player player, Entity victim, AttackStyle attackStyle, AttackType attackType,
			int damageDealt) {
		if (attackType == null)
			return;
		boolean multiplier = victim.npc != null;
		double xp = damageDealt * 4D;
		double monsterMod = 1.0;
		if (multiplier && victim.npc.getId() == 10507) {
			xp = 0;
		}
		if (multiplier && victim.npc.getId() == 7221) {
			monsterMod *= 1.3;
		}
		if (multiplier && victim.npc.getId() == 2668)
			monsterMod = victim.npc.getCombat().getInfo().combat_xp_modifier;
		xp *= monsterMod;
		if (xp <= 0)
			return;
		if (victim.isPlayer()) {
			xp = damageDealt;
			multiplier = false;
		}
		if (attackStyle.isMagic()) {// this can pretty much only happen from trident!!
			xp /= 2;
			if (player.showHitAsExperience()) {
				player.getStats().addXp(StatType.Magic, damageDealt, false);
				return;
			}
			switch (attackType) {
				case ACCURATE:
					player.getStats().addXp(StatType.Magic, xp, multiplier);
					break;
				case DEFENSIVE:
					xp /= 2;
					player.getStats().addXp(StatType.Magic, xp, multiplier);
					player.getStats().addXp(StatType.Defence, xp, multiplier);
					break;
			}
		} else if (attackStyle.isRanged()) {
			if (player.showHitAsExperience()) {
				player.getStats().addXp(StatType.Ranged, damageDealt, false);
				return;
			}
			switch (attackType) {
				case LONG_RANGED:
					xp /= 2;
					player.getStats().addXp(StatType.Ranged, xp, multiplier);
					player.getStats().addXp(StatType.Defence, xp, multiplier);
					break;
				default:
					player.getStats().addXp(StatType.Ranged, xp, multiplier);
					break;
			}
		} else {
			switch (attackType) {
				case ACCURATE:
					if (player.showHitAsExperience()) {
						player.getStats().addXp(StatType.Attack, damageDealt, false);
						return;
					}
					player.getStats().addXp(StatType.Attack, xp, multiplier);
					break;
				case AGGRESSIVE:
					if (player.showHitAsExperience()) {
						player.getStats().addXp(StatType.Strength, damageDealt, false);
						return;
					}
					player.getStats().addXp(StatType.Strength, xp, multiplier);
					break;
				case CONTROLLED:
					if (player.showHitAsExperience()) {
						player.getStats().addXp(StatType.Attack, damageDealt, false);
						return;
					}
					xp /= 3;
					player.getStats().addXp(StatType.Attack, xp, multiplier);
					player.getStats().addXp(StatType.Strength, xp, multiplier);
					player.getStats().addXp(StatType.Defence, xp, multiplier);
					break;
				case DEFENSIVE:
					if (player.showHitAsExperience()) {
						player.getStats().addXp(StatType.Defence, damageDealt, false);
						return;
					}
					player.getStats().addXp(StatType.Defence, xp, multiplier);
					break;
			}
		}
		player.getStats().addXp(StatType.Hitpoints, victim.isPlayer() ? damageDealt : damageDealt * 1.33 * monsterMod,
				multiplier);
	}

	public static void addMagicXp(Player player, double baseXp, int damage, boolean multiplier) {
		double xp = baseXp + (damage * 2D);
		if (player.showHitAsExperience) {
			player.getStats().addXp(StatType.Magic, damage, false);
			return;
		}
		if (VarPlayerRepository.DEFENSIVE_CAST.get(player) == 1) {
			xp /= 2;
			player.getStats().addXp(StatType.Defence, xp, multiplier);
			player.getStats().addXp(StatType.Magic, xp, multiplier);
		} else {
			player.getStats().addXp(StatType.Magic, xp, multiplier);
		}
		if (damage > 0)
			player.getStats().addXp(StatType.Hitpoints, damage * 1.33, multiplier);
	}

	/**
	 * Combat level
	 */

	public static int getCombatLevel(Player player) {
		StatList stats = player.getStats();
		double attack = stats.get(StatType.Attack).fixedLevel;
		double defence = stats.get(StatType.Defence).fixedLevel;
		double strength = stats.get(StatType.Strength).fixedLevel;
		double hitpoints = stats.get(StatType.Hitpoints).fixedLevel;
		double ranged = stats.get(StatType.Ranged).fixedLevel;
		double prayer = stats.get(StatType.Prayer).fixedLevel;
		double magic = stats.get(StatType.Magic).fixedLevel;
		double coreBase = (defence + hitpoints + (int) (prayer / 2D)) * 0.25;
		double meleeBase = (attack + strength) * 0.325;
		double rangedBase = ((int) (ranged / 2D) + ranged) * 0.325;
		double magicBase = ((int) (magic / 2D) + magic) * 0.325;
		return (int) (coreBase + Math.max(meleeBase, Math.max(rangedBase, magicBase)));
	}

}
