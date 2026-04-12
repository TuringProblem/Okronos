package io.ruin.model.content.combatachievements;

import io.ruin.model.content.combatachievements.achievements.medium.ArgentavisAdept;
import io.ruin.model.entity.player.Player;

public abstract class CombatAchievement {
	public boolean completed = false;

	public abstract void check(Player player);

	public void complete(Player player) {
		if(player.combatAchievementStore.get(CombatAchievementSystem.getOrdinalByCombatAchievement(this)))
			return;
		CombatAchievementSystem.getCompletionRewardByTier(player, getTier());
		player.sendMessage("Congratulations, you've completed a " + getTier().toString().toLowerCase() + " combat task: <col=319200>" + getName() + "</col> (" + CombatAchievementSystem.pointCompletionReward(getTier()) + " points).");
		player.sendMessage("You now have <col=319200>" + player.combatAchievementPoints + "</col> combat achievement points.");
		int ordinal = CombatAchievementSystem.getOrdinalByCombatAchievement(this);
		if (ordinal != -1)
			player.combatAchievementStore.put(ordinal, true);
	}

	public abstract String getName();

	public abstract String getDesc();

	public abstract Tier getTier();


	public enum Tier {
		NONE,
		EASY,
		MEDIUM,
		HARD,
		ELITE,
		MASTER,
		GRANDMASTER
	}
}
