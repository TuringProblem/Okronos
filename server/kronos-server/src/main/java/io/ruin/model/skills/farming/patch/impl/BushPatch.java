package io.ruin.model.skills.farming.patch.impl;

import io.ruin.api.utils.Random;
import io.ruin.api.utils.TimeUtils;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.DancingInTheYields;
import io.ruin.model.entity.player.Player;
import io.ruin.model.skills.farming.crop.Crop;
import io.ruin.model.skills.farming.crop.impl.BushCrop;
import io.ruin.model.skills.farming.patch.RegrowPatch;
import io.ruin.model.stat.StatType;

public class BushPatch extends RegrowPatch {

	private static double yieldBoost(Player player) {
		switch (player.getSecondaryGroup()) {
			case DONATOR -> {
				return 1.02;
			}
			case SUPER_DONATOR -> {
				return 1.04;
			}
			case ELITE_DONATOR -> {
				return 1.06;
			}
			case NOBLE_DONATOR -> {
				return 1.08;
			}
			case GOLD_DONATOR -> {
				return 1.1;
			}
			case PLATINUM_DONATOR -> {
				return 1.13;
			}
			case LEGENDARY_DONATOR -> {
				return 1.17;
			}
			case SUPREME_DONATOR -> {
				return 1.25;
			}
			default -> {
				return 1;
			}
		}
	}

	@Override
	public int getCropVarpbitValue() {
		return getDiseaseStage() << 6 | (getPlantedCrop().getTotalStages()) + getPlantedCrop().getContainerIndex();
	}

	@Override
	public long getRegrowDelay() {
		return TimeUtils.getMinutesToMillis(3);
	}

	@Override
	public int getMaxProduce() {
		return 4;
	}

	@Override
	public void cropInteract() {
		if (getStage() == getPlantedCrop().getTotalStages()) {
			checkHealth();
		} else if (getStage() == getPlantedCrop().getTotalStages() + 1) {
			if (getProduceCount() == 0) {
				clear();
			} else {
				pick();
			}
		}
	}

	@Override
	public boolean canPlant(Crop crop) {
		return crop instanceof BushCrop;
	}

	@Override
	public boolean isDiseaseImmune() {
		return false;
	}

	@Override
	public int calculateProduceAmount() {
		int amount = Random.get(4, 9);
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.DANCING_IN_THE_YIELDS)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.DANCING_IN_THE_YIELDS);
			DancingInTheYields c = (DancingInTheYields) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
				.getPerk(player);
			float multiplier = 1;
			multiplier += c.getYieldBonus();
			amount *= multiplier;
		}
		amount *= yieldBoost(player);
		return amount;
	}

	@Override
	public boolean requiresCure() {
		return false;
	}

	@Override
	public String getPatchName() {
		return "a bush";
	}

	private void checkHealth() {
		player.sendMessage("You examine the bush and find that it is in perfect health.");
		getPlantedCrop().getCounter().increment(player, 1);
		player.getStats().addXp(StatType.Farming, ((BushCrop) getPlantedCrop()).getCheckHealthXP(), true);
		advanceStage();
		update();
	}
}
