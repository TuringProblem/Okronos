package io.ruin.model.activities.perktree;

import io.ruin.model.activities.perktree.perksets.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum PerkSets {
	SKILLED_MIND(new SkilledMind(), new Perks[][]{
		{Perks.EXPERIENCE_ENHANCER, Perks.FLETCHING_DREAMS, Perks.DANCING_IN_THE_YIELDS, Perks.RUNNING_PAYS, Perks.SMITHING_DYNASTY},
		{Perks.EXPERIENCE_ENHANCER, Perks.PACKS_N_POTIONS, Perks.THE_ARSONIST, Perks.KITCHEN_EXPERT, Perks.SPEED_FISHER},
		{Perks.EXPERIENCE_ENHANCER, Perks.ONE_WITH_THE_RUNES, Perks.A_SLAYING_EXPERIENCE, Perks.ARTS_N_CRAFTS, Perks.MASTER_THIEF},
		{Perks.EXPERIENCE_ENHANCER, Perks.HUNTING_IN_ITS_TRUE_FORM, Perks.TAKING_IT_ON_THE_CHIN, Perks.THE_ART_OF_MINING, Perks.THE_EFFICIENT_CHOP},
	}, 10600),
	BLEED_THEM_DRY(new BleedThemDry(), new Perks[][]{
		{Perks.BLOOD_SACRIFICE, Perks.TAKE_YOUR_TIME, Perks.FAST_EATER, Perks.VANQUISHED_FOE, Perks.CONSERVATIVE_PRAYERS},
		{Perks.SPEEDY_STRIKES, Perks.THE_SOULSPLIT, Perks.RECURRENT_DAMAGE, Perks.ARCANE_ENHANCEMENT, Perks.LAYING_TO_REST},
		{Perks.SKILLED_RANGER, Perks.DOUBLE_TAP, Perks.BLESSED_BOLTS, Perks.FOOD_CONNOISSEUR, Perks.FAST_EATER},

	}, 10605),
	GOLD_DIGGER(new GoldDigger(), new Perks[][]{
		{Perks.THE_GOLDEN_GAUNTLET, Perks.SECURING_THE_BAG, Perks.THE_PET_HUNTER, Perks.RAIDING_RESTORATIONS, Perks.GOD_WARS_VETERAN}
	}, 10604),
	MAGIC_RESISTANCE(new MagicResistance(), new Perks[][]{
		{Perks.DOUBLE_TAP, Perks.BLESSED_BOLTS, Perks.SKILLED_RANGER, Perks.CRIMSON_CHIN, Perks.THE_SPECIAL_ATTACK}
	}, 10599),
	MELEE_RESISTANCE(new MeleeResistance(), new Perks[][]{
		{Perks.ARCANE_ENHANCEMENT, Perks.ENHANCED_MAGICAL_WEAPONS, Perks.FINEST_WIZARDRY, Perks.THE_SOULSPLIT, Perks.TAKE_YOUR_TIME}
	}, 10603),
	RANGE_RESISTANCE(new RangeResistance(), new Perks[][]{
		{Perks.THE_STABBER, Perks.SPEEDY_STRIKES, Perks.POTIONS_MASTER, Perks.ACCURATE_BLOWS, Perks.DAMAGE_REFLECT}
	}, 10601),
	PRAYER_SIPHON(new PrayerSiphon(), new Perks[][]{
		{Perks.THE_DRAGON_SLAYER, Perks.THE_SPECIAL_ATTACK, Perks.CRIMSON_CHIN, Perks.SNAKE_CHARMER, Perks.THE_ALCHEMIST},
		{Perks.WILDERNESS_HUNTER, Perks.POTIONS_MASTER, Perks.TAKING_IT_ON_THE_CHIN, Perks.THE_CRUSHER, Perks.DAMAGE_REFLECT}
	}, 10602),
	;

	Perks[][] perkCombinations;
	public PlayerPerkSet perkSet;
	int scriptId;

	PerkSets(PlayerPerkSet perkSet, Perks[][] perkCombinations, int scriptId) {
		this.perkSet = perkSet;
		this.perkCombinations = perkCombinations;
		this.scriptId = scriptId;
	}

	public List<Perks> hasAnyPerksetCombination(List<Perks> activePerks) {
		List<Perks> perks = new ArrayList<>();
		if (perkCombinations != null) {
			for (Perks[] perkCombination : perkCombinations) {
				if (Arrays.stream(perkCombination).allMatch(activePerks::contains)) {
					Arrays.asList(perkCombination).forEach(perks::add);
					return perks;
				}
			}
		}
		return null;
	}

	public PlayerPerkSet perkSet() {
		return perkSet;
	}
}
