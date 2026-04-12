package io.ruin.model.skills.agility.courses;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.DancingInTheYields;
import io.ruin.model.activities.perktree.perks.RunningPays;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.actions.impl.pet.Pet;

public class AgilityPet {

	public static void rollForPet(Player player, int chance) {
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.RUNNING_PAYS)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.RUNNING_PAYS);
			RunningPays c = (RunningPays) player.getPlayerPerkHandler().
				getActivePerks(player).get(perkIndex).getPerk(player);
			if (player.getInventory().hasRoomFor(995, c.getReward())) {
				player.getInventory().add(995, c.getReward());
				player.sendFilteredMessage(NumberUtils.formatNumber(c.getReward()) + " coins has been added to your inventory for completing the agility course.");
			} else if (player.getBank().hasRoomFor(995, c.getReward())) {
				player.getInventory().add(995, c.getReward());
				player.sendFilteredMessage(NumberUtils.formatNumber(c.getReward()) + " coins has been sent to your bank for completing the agility course.");
			}
		}
		player.agilityLapsRan++;
		if (player.agilityLapsRan == Achievements.ANOTHER_ONE_BITES_THE_DUST.getCompletionAmount())
			player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.ANOTHER_ONE_BITES_THE_DUST.getAchievementName());
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
			ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().
				getActivePerks(player).get(perkIndex).getPerk(player);
			chance *= c.getPetChanceBoost();
		}
		if (player.petDropBonus.isDelayed())
			chance *= 0.8;
		if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
			chance *= 0.85F;

		chance *= getPetDonatorBoost(player);
		if (Random.get(chance) == 0)
			Pet.GIANT_SQUIRREL.unlock(player, 0);
	}

	private static double getPetDonatorBoost(Player player) {
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

}
