package io.ruin.model.activities.perktree;

import io.ruin.model.activities.perktree.perks.*;
import io.ruin.model.entity.player.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public enum Perks {
	DAMAGE_REFLECT(DamageReflectPerk.class, 10555, 12),
	SPEEDY_STRIKES(SpeedyStrikes.class, 10585, 11),
	DOUBLE_TAP(DoubleTap.class, 10557, 12),
	ARCANE_ENHANCEMENT(ArcaneEnhancement.class, 10548, 12),
	THE_SOULSPLIT(TheSoulsplit.class, 10593, 12),
	RECURRENT_DAMAGE(RecurrentDamage.class, 10577, 11),
	BLESSED_BOLTS(BlessedBolts.class, 10550, 9),
	BLOOD_SACRIFICE(BloodSacrifice.class, 10551, 9),
	ACCURATE_BLOWS(AccurateBlows.class, 10547, 8),
	TAKE_YOUR_TIME(TakeYourTime.class, 10586, 7),
	THE_CRUSHER(TheCrusher.class, 10591, 7),
	THE_STABBER(TheStabber.class, 10594, 7),
	THE_SLASHER(TheSlasher.class, 10592, 7),
	ENHANCED_MAGICAL_WEAPONS(EnhancedMagicalWeapons.class, 10560, 11),
	SKILLED_RANGER(SkilledRanger.class, 10580, 11),
	FINEST_WIZARDRY(FinestWizardry.class, 10563, 11),
	THE_EFFICIENT_CHOP(TheEfficientChop.class, 10559, 7),
	SPEED_FISHER(SpeedFisher.class, 10584, 6),
	THE_ART_OF_MINING(TheArtOfMining.class, 10590, 6),
	HUNTING_IN_ITS_TRUE_FORM(HuntingInItsTrueForm.class, 10568, 6),
	TAKING_IT_ON_THE_CHIN(TakingItOnTheChin.class, 10587, 6),
	MASTER_THIEF(MasterThief.class, 10571, 6),
	A_SLAYING_EXPERIENCE(ASlayingExperience.class, 10581, 6),
	ONE_WITH_THE_RUNES(OneWithTheRunes.class, 10572, 6),
	DANCING_IN_THE_YIELDS(DancingInTheYields.class, 10556, 6),
	KITCHEN_EXPERT(KitchenExpert.class, 10569, 6),
	SMITHING_DYNASTY(SmithingDynasty.class, 10582, 6),
	THE_ARSONIST(TheArsonist.class, 10589, 6),
	RUNNING_PAYS(RunningPays.class, 10578, 6),
	PACKS_N_POTIONS(PacksnPotions.class, 10573, 6),
	ARTS_N_CRAFTS(ArtsnCrafts.class, 10549, 6),
	FLETCHING_DREAMS(FletchingDreams.class, 10564, 6),
	BOB_THE_BUILDER(BobTheBuilder.class, 10598, 5),
	EXPERIENCE_ENHANCER(ExperienceEnhancer.class, 10561, 7),
	CONSERVATIVE_PRAYERS(ConservativePrayers.class, 10553, 9),
	GOD_WARS_VETERAN(GodWarsVeteran.class, 10567, 8),
	POTIONS_MASTER(PotionsMaster.class, 10575, 7),
	FOOD_CONNOISSEUR(FoodConnoisseur.class, 10565, 7),
	LAYING_TO_REST(LayingToRest.class, 10570, 6),
	RAIDING_RESTORATIONS(RaidingRestorations.class, 10576, 7),
	FAST_EATER(FastEater.class, 10562, 7),
	THE_DRAGON_SLAYER(TheDragonSlayer.class, 10558, 7),
	SNAKE_CHARMER(SnakeCharmer.class, 10583, 5),
	THE_GOLDEN_GAUNTLET(TheGoldenGauntlet.class, 10566, 5),
	VANQUISHED_FOE(VanquishedFoe.class, 10596, 8),
	WILDERNESS_HUNTER(WildernessHunter.class, 10595, 6),
	THE_SPECIAL_ATTACK(TheSpecialAttacker.class, 10597, 5),
	THE_ALCHEMIST(TheAlchemist.class, 10588, 5),
	SECURING_THE_BAG(SecuringTheBag.class, 10579, 8),
	THE_PET_HUNTER(ThePetHunter.class, 10574, 7),
	CRIMSON_CHIN(CrimsonChin.class, 10554, 7),
	;
	private final Class<? extends PlayerPerk> perkClass;
	int scriptId;
	int upgradePrice;
	public Map<Integer, PlayerPerk> cachedPerks = new HashMap<>();

	Perks(Class<? extends PlayerPerk> perkClass, int scriptId, int upgradePrice) {
		this.perkClass = perkClass;
		this.scriptId = scriptId;
		this.upgradePrice = upgradePrice;
	}

	public static final Perks[] VALUES = values();

	public PlayerPerk getPerk(Player player) {
		try {
			PlayerPerk perkInstance = getPlayerCachedPerkInstance(player);
			if (perkInstance == null) {
				perkInstance = perkClass.getDeclaredConstructor().newInstance();
				perkInstance.setPerkLevel(player.ownedPerks.get(this.ordinal()));
				player.ownedPlayerPerks.put(this.ordinal(), perkInstance);
			}
			perkInstance.setPerkLevel(player.ownedPerks.get(this.ordinal()));
			return perkInstance;
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

	public PlayerPerk getInterfacePerk() {
		PlayerPerk perkInstance = cachedPerks.get(this.ordinal());
		if (perkInstance == null) {
			try {
				perkInstance = perkClass.getDeclaredConstructor().newInstance();
				cachedPerks.put(this.ordinal(), perkInstance);
			} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}
		}
		return perkInstance;
	}

	private PlayerPerk getPlayerCachedPerkInstance(Player player) {
		return player.ownedPlayerPerks.get(this.ordinal());
	}


	public int getUpgradePrice() {
		return upgradePrice;
	}
}
