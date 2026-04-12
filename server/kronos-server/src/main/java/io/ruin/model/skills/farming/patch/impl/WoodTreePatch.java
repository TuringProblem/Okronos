package io.ruin.model.skills.farming.patch.impl;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.skills.farming.crop.Crop;
import io.ruin.model.skills.farming.crop.impl.WoodTreeCrop;
import io.ruin.model.skills.farming.patch.Patch;
import io.ruin.model.skills.woodcutting.Woodcutting;
import io.ruin.model.stat.StatType;

public class WoodTreePatch extends Patch {

	@Override
	public int getCropVarpbitValue() {
		if (stage < getPlantedCrop().getTotalStages())
			stage = getPlantedCrop().getTotalStages();
		return getDiseaseStage() << 6 | stage + getPlantedCrop().getContainerIndex();
	}

	@Override
	public void cropInteract() {
		if (getStage() <= getPlantedCrop().getTotalStages()) {
			setStage(getPlantedCrop().getTotalStages());
			checkHealth();
		} else if (getStage() == getPlantedCrop().getTotalStages() + 1) {
			chop();
		} else if (getStage() == getPlantedCrop().getTotalStages() + 2) {
			clear();
		}
	}

	@Override
	public void clear() {
		if (getPlantedCrop() == null) {
			player.sendMessage("This patch doesn't have anything planted on it.");
			return;
		}
		if (!player.getInventory().contains(952, 1)) {
			player.sendMessage("You will need a spade to clear this patch.");
			return;
		}
		player.startEvent(event -> {
			player.animate(831);
			event.delay(Random.get(2, 4));
			player.resetAnimation();
			reset(false);
			if (getPlantedCrop() != null && getPlantedCrop().getRoots() != -1
				&& getStage() >= getPlantedCrop().getTotalStages()) {
				player.getInventory().addOrDrop(getPlantedCrop().getRoots(), getRootAmount());
			}
			player.sendMessage("You clear the patch for new crops.");
		});
	}

	public int getRootAmount() {
		if (getPlantedCrop() == null)
			return 0;
		return 1 + Math.max(
			Math.min(3, (player.getStats().get(StatType.Farming).currentLevel - getPlantedCrop().getLevelReq()) / 8), 0);
	}

	@Override
	public boolean canPlant(Crop crop) {
		return crop instanceof WoodTreeCrop;
	}

	@Override
	public boolean isDiseaseImmune() {
		return false;
	}

	@Override
	public int calculateProduceAmount() {
		return 0; // we dont use this
	}

	@Override
	public boolean requiresCure() {
		return false;
	}

	@Override
	public WoodTreeCrop getPlantedCrop() {
		return super.getPlantedCrop() == null ? null : (WoodTreeCrop) super.getPlantedCrop();
	}

	@Override
	public String getPatchName() {
		return "a tree";
	}

	private void chop() {
		Woodcutting.chop(getPlantedCrop().getTreeType(), player, () -> false, (worldEvent) -> {
			advanceStage();
			player.sendFilteredMessage("You chop down the tree.");
			update();
			worldEvent.delay(getPlantedCrop().getTreeType().respawnTime);
			if (getPlantedCrop() == null || getStage() < getPlantedCrop().getTotalStages())
				return; // player cleared tree
			setStage(getPlantedCrop().getTotalStages() + 1);
			update();
		});
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

	private void checkHealth() {
		player.sendMessage("You examine the tree and find that it is in perfect health.");
		getPlantedCrop().getCounter().increment(player, 1);
		player.getStats().addXp(StatType.Farming, getPlantedCrop().getHarvestXP(), true);
		player.treesGrown++;
		int petChance = Random.get(getPlantedCrop().getPetOdds());
		petChance /= 2;
		if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
			petChance *= 0.85F;
		if (player.petDropBonus.isDelayed())
			petChance *= 0.8F;
		petChance *= getPetDonatorBoost(player);
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
			ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
				.getPerk(player);
			assert c != null;
			petChance *= (float) c.getPetChanceBoost();
		}
		if (petChance == 0) {
			Pet.TANGLEROOT.unlock(player, 0);
		}
		DailyTasks.handleItemObtained(player, getObjectId(), StatType.Farming);
		if (player.treesGrown == Achievements.IN_BLOOM.getCompletionAmount())
			player.sendMessage(
				"<col=000080>You have completed the achievement: <col=800000>" + Achievements.IN_BLOOM.getAchievementName());
		advanceStage();
		update();
		data.getConfig().set(player, getCropVarpbitValue());
	}

}
