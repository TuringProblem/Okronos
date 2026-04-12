package io.ruin.model.content.combatachievements;

import io.ruin.cache.Color;
import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.item.actions.impl.scrolls.KeyTeleports;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CombatAchievementSystem {
	private static final Map<CombatAchievement.Tier, Integer> achievementsOfTypeCache = new HashMap<>();

	public static CombatAchievement.Tier getTier(int combatAchievementPoints) {
		if (combatAchievementPoints >= getPointsForTier(CombatAchievement.Tier.GRANDMASTER))
			return CombatAchievement.Tier.GRANDMASTER;
		if (combatAchievementPoints >= getPointsForTier(CombatAchievement.Tier.MASTER))
			return CombatAchievement.Tier.MASTER;
		if (combatAchievementPoints >= getPointsForTier(CombatAchievement.Tier.ELITE))
			return CombatAchievement.Tier.ELITE;
		if (combatAchievementPoints >= getPointsForTier(CombatAchievement.Tier.HARD))
			return CombatAchievement.Tier.HARD;
		if (combatAchievementPoints >= getPointsForTier(CombatAchievement.Tier.MEDIUM))
			return CombatAchievement.Tier.MEDIUM;
		if (combatAchievementPoints >= getPointsForTier(CombatAchievement.Tier.EASY))
			return CombatAchievement.Tier.EASY;
		return CombatAchievement.Tier.NONE;
	}

	public static CombatAchievement.Tier getTierByOrdinal(int ordinal) {
		return switch (ordinal) {
			case 1 -> CombatAchievement.Tier.EASY;
			case 2 -> CombatAchievement.Tier.MEDIUM;
			case 3 -> CombatAchievement.Tier.HARD;
			case 4 -> CombatAchievement.Tier.ELITE;
			case 5 -> CombatAchievement.Tier.MASTER;
			case 6 -> CombatAchievement.Tier.GRANDMASTER;
			default -> CombatAchievement.Tier.NONE;
		};
	}

	public static int getOrdinalByCombatAchievement(CombatAchievement ach) {
		for (CombatAchievements achievementEnum : CombatAchievements.values()) {
			if (achievementEnum.getCombatAchievement().getClass() == ach.getClass()) {
				return achievementEnum.ordinal();
			}
		}
		return -1;
	}

	public boolean hasEnoughPointsForTier(Player player, CombatAchievement.Tier type) {
		return player.combatAchievementPoints >= getPointsForTier(type);
	}

	public static int getCombatAchievementsOfType(CombatAchievement.Tier tier) {
		return achievementsOfTypeCache.computeIfAbsent(tier, (_k) -> {
			int count = 0;
			for (CombatAchievements achievement : CombatAchievements.values()) {
				if (achievement.getCombatAchievement().getTier() == tier)
					count++;
			}
			return count;
		});
	}

	public static int getAchievementsCompletedByTier(Player player, CombatAchievement.Tier type) {
		int count = 0;
		for (CombatAchievements achievement : CombatAchievements.values()) {
			if (achievement.getCombatAchievement().getTier() == type
					&& player.combatAchievementStore.get(achievement.ordinal()))
				count++;
		}
		return count;
	}

	public static int getPointsForTier(CombatAchievement.Tier type) {
		return switch (type) {
			case EASY -> getCombatAchievementsOfType(CombatAchievement.Tier.EASY);
			case MEDIUM ->
				getPointsForTier(CombatAchievement.Tier.EASY)
						+ (getCombatAchievementsOfType(CombatAchievement.Tier.MEDIUM) * 2);
			case HARD ->
				getPointsForTier(CombatAchievement.Tier.MEDIUM)
						+ (getCombatAchievementsOfType(CombatAchievement.Tier.HARD) * 3);
			case ELITE ->
				getPointsForTier(CombatAchievement.Tier.HARD) + (getCombatAchievementsOfType(CombatAchievement.Tier.ELITE) * 4);
			case MASTER ->
				getPointsForTier(CombatAchievement.Tier.ELITE)
						+ (getCombatAchievementsOfType(CombatAchievement.Tier.MASTER) * 5);
			case GRANDMASTER ->
				getPointsForTier(CombatAchievement.Tier.MASTER)
						+ (getCombatAchievementsOfType(CombatAchievement.Tier.GRANDMASTER) * 6);
			default -> 0;
		};
	}

	public static int getPointsForNextTier(int combatAchievementsClaimed) {
		CombatAchievement.Tier tier = getTierByOrdinal(combatAchievementsClaimed);
		if (tier == CombatAchievement.Tier.GRANDMASTER)
			return 0;
		return getPointsForTier(getTierByOrdinal(tier.ordinal() + 1));
	}

	public static int pointCompletionReward(CombatAchievement.Tier type) {
		return type.ordinal() + 1;
	}

	public static void completeAchievement(Player player, CombatAchievement.Tier type, CombatAchievements achieve) {
		for (int i = 0; i < 6; i++) {
			player.combatAchievementRewardsClaimed.putIfAbsent(i, false);
		}
		if (Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(achieve.ordinal())).getCombatAchievement()).completed)
			return;

		Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(achieve.ordinal())).getCombatAchievement()).complete(player);
		player.combatAchievementStore.put(achieve.ordinal(), true);
		player.sendMessage("You have received " + Color.DARK_RED.wrap("" + pointCompletionReward(type))
				+ " combat achievement points and now have: " + player.combatAchievementPoints + " points.");
	}

	public static boolean claimSectionCompletionReward(Player player, CombatAchievement.Tier type) {
		if (player.completedAchievementTier(type))
			return false;
		if(CombatAchievementSystem.getPointsForTier(type) > player.combatAchievementPoints)
			return false;
		player.combatAchievementsClaimed++;
		getCompletionRewardItems(player, type);
		player.dialogue(
			new NPCDialogue(13613, "You have claimed your " + type.name().toLowerCase() + " achievement reward!"));
		return true;
	}

	public static void claimNextReward(Player player) {
		boolean claimed = false;
		for (int i = 1; i < 7; i++) {
			if (player.combatAchievementsClaimed >= i)
				continue;
			if(claimSectionCompletionReward(player, CombatAchievement.Tier.values()[i])) {
				claimed = true;
				break;
			}
		}
		if (!claimed) {
			if(getPointsForNextTier(player.combatAchievementsClaimed) == 0) {
				player.dialogue(
					new NPCDialogue(13613, "You've already claimed all your combat achievement rewards.'"));
			} else {
				player.dialogue(
					new NPCDialogue(13613, "You don't have any rewards to claim at this time. You need " +
						Color.DARK_RED.wrap("" + getPointsForNextTier(player.combatAchievementsClaimed)) + " combat achievement points to claim a reward."));
			}
		}
	}

	private static void getCompletionRewardItems(Player player, CombatAchievement.Tier type) {
		switch (type) {
			case EASY -> {
				player.getInventory().addOrSendToBank(ItemID.INSTANCE_TOKEN, 10);
				player.getInventory().addOrSendToBank(ItemID.PERK_POINT_SCROLL, 6);
				player.getInventory().addOrSendToBank(ItemID.SLAYER_TASK_PICK_SCROLL, 10);
			}
			case MEDIUM -> {
				player.getInventory().addOrSendToBank(ItemID.DAMAGE_BOOST_SCROLL, 2);
				player.getInventory().addOrSendToBank(ItemID.DAMAGE_REDUCTION_SCROLL, 2);
				player.getInventory().addOrSendToBank(ItemID.INSTANCE_TOKEN, 15);
			}
			case HARD -> {
				player.getInventory().addOrSendToBank(ItemID.SLAYER_TASK_PICK_SCROLL, 10);
				player.getInventory().addOrSendToBank(ItemID.DAMAGE_BOOST_SCROLL, 3);
				player.getInventory().addOrSendToBank(ItemID.INSTANCE_TOKEN, 15);
			}
			case ELITE -> {
				player.getInventory().addOrSendToBank(ItemID.DONATOR_MYSTERY_BOX, 2);
				player.getInventory().addOrSendToBank(ItemID.INSTANCE_TOKEN, 15);
				player.getInventory().addOrSendToBank(ItemID.SLAYER_TASK_PICK_SCROLL, 15);
				player.getInventory().addOrSendToBank(ItemID.LARGE_EXP_LAMP, 1);
				player.getInventory().addOrSendToBank(ItemID.DAMAGE_BOOST_SCROLL, 4);
				player.getInventory().addOrSendToBank(ItemID.DOUBLE_DROP_SCROLL, 6);
			}
			case MASTER -> {
				player.getInventory().addOrSendToBank(ItemID.LARGE_EXP_LAMP, 3);
				player.getInventory().addOrSendToBank(ItemID.DONATOR_MYSTERY_BAG, 1);
				player.getInventory().addOrSendToBank(ItemID.DOUBLE_DROP_SCROLL, 8);
				player.getInventory().addOrSendToBank(ItemID.DROP_RATE_SCROLL, 4);
				player.getInventory().addOrSendToBank(ItemID.INSTANCE_TOKEN, 20);
				player.getInventory().addOrSendToBank(ItemID.SLAYER_TASK_PICK_SCROLL, 20);
				player.getInventory().addOrSendToBank(KeyTeleports.SLAYER_KEY4.id, 25);
			}
			case GRANDMASTER -> {
				player.getInventory().addOrSendToBank(ItemID.ADVANCED_DONATOR_MYSTERY_CHEST, 2);
				player.getInventory().addOrSendToBank(ItemID.LAMP, 2);
				player.getInventory().addOrSendToBank(ItemID.INSTANCE_TOKEN, 25);
				player.getInventory().addOrSendToBank(ItemID.DAMAGE_BOOST_SCROLL, 10);
				player.getInventory().addOrSendToBank(ItemID.DAMAGE_REDUCTION_SCROLL, 10);
				player.getInventory().addOrSendToBank(ItemID.SLAYER_TASK_PICK_SCROLL, 25);
				player.getInventory().addOrSendToBank(ItemID.SLAYER_SKIP_SCROLL, 25);
				player.getInventory().addOrSendToBank(ItemID.DOUBLE_DROP_SCROLL, 10);
				player.getInventory().addOrSendToBank(ItemID.DROP_RATE_SCROLL, 10);
				player.getInventory().addOrSendToBank(KeyTeleports.SLAYER_KEY4.id, 30);
			}
		}
	}

	protected static void getCompletionRewardByTier(Player player, CombatAchievement.Tier type) {
		switch (type) {
			case EASY -> player.updateCombatAchievementPoints(1);
			case MEDIUM -> player.updateCombatAchievementPoints(2);
			case HARD -> player.updateCombatAchievementPoints(3);
			case ELITE -> player.updateCombatAchievementPoints(4);
			case MASTER -> player.updateCombatAchievementPoints(5);
			case GRANDMASTER -> player.updateCombatAchievementPoints(6);
		}
	}

	public static void onLogin(Player player) {
		player.combatAchievementsList.clear();
		for (CombatAchievements achievement : CombatAchievements.values()) {
			if (player.combatAchievementStore.containsKey(achievement.ordinal()))
				continue;
			player.combatAchievementStore.put(achievement.ordinal(), false);
		}
		for (Map.Entry<Integer, Boolean> entry : player.combatAchievementStore.entrySet()) {
			CombatAchievements achievement = CombatAchievements.values()[entry.getKey()];
			Objects.requireNonNull(achievement.getCombatAchievement()).completed = entry.getValue();
			player.combatAchievementsList.add(achievement);
		}
		if(player.combatAchievementRewardsClaimed.isEmpty()) {
			for (int i = 0; i < 7; i++) {
				player.combatAchievementRewardsClaimed.put(i, false);
			}
		}
		int combatAchievementPoints = 0;
		for (CombatAchievements achievement : player.combatAchievementsList) {
			if (player.combatAchievementStore.get(achievement.ordinal())) {
				combatAchievementPoints += pointCompletionReward(achievement.getCombatAchievement().getTier());
			}
		}
		player.combatAchievementPoints = combatAchievementPoints;
	}

}
