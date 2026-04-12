package npc.nex.utils;

import npc.nex.scripts.NexCombat;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.player.Player;

import java.util.Objects;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
public interface NexCombatAchievements {

	static void onNexDeath(Player player,
						   NexCombat nexCombat,
						   int playersKilled,
						   boolean healedFromSiphon,
						   boolean damagedByShadow,
						   boolean damagedByIce,
						   boolean playerCoughing
	) {
		if (!nexCombat.overTwoPlayers) {
			Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.NEX_DUO.ordinal()))
				.getCombatAchievement()).check(player);
		}
		if (!nexCombat.overOnePlayers) {
			Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.NEX_SOLO.ordinal()))
				.getCombatAchievement()).check(player);
		}
		if (!nexCombat.overThreePlayers) {
			Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.NEX_TRIO.ordinal()))
				.getCombatAchievement()).check(player);
		}

		if (playersKilled == 0) {
			Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.NEX_SURVIVORS.ordinal()))
				.getCombatAchievement()).check(player);
		}
		if (playerCoughing) {
			Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.I_SHOULD_SEE_A_DOCTOR.ordinal()))
				.getCombatAchievement()).check(player);
		}
		Objects.requireNonNull(player.combatAchievementsList
			.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.NEX_MASTER.ordinal()))
			.getCombatAchievement()).check(player);
		Objects.requireNonNull(player.combatAchievementsList
			.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.NEX_ADEPT.ordinal()))
			.getCombatAchievement()).check(player);
		if (!healedFromSiphon) {
			Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.A_SIPHON_WILL_SOLVE_THIS.ordinal()))
				.getCombatAchievement()).check(player);
		}
		if (!damagedByShadow) {
			Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.SHADOWS_MOVE.ordinal()))
				.getCombatAchievement()).check(player);
		}
		if (!damagedByIce) {
			Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.CONTAIN_THIS.ordinal()))
				.getCombatAchievement()).check(player);
		}
		if (!damagedByIce && !damagedByShadow && !healedFromSiphon) {
			Objects.requireNonNull(player.combatAchievementsList
				.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_NEX.ordinal()))
				.getCombatAchievement()).check(player);
		}
	}
}
