package io.ruin.model.skills.farming.patch.impl;

import io.ruin.api.utils.Random;
import io.ruin.api.utils.TimeUtils;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.skills.farming.crop.Crop;
import io.ruin.model.skills.farming.crop.impl.FruitTreeCrop;
import io.ruin.model.skills.farming.patch.RegrowPatch;
import io.ruin.model.skills.woodcutting.Hatchet;
import io.ruin.model.stat.StatType;

public class FruitTreePatch extends RegrowPatch {

	@Override
	public int getCropVarpbitValue() {
		return getDiseaseStage() << 6 | (getPlantedCrop().getTotalStages()) + getPlantedCrop().getContainerIndex();
	}

	@Override
	public long getRegrowDelay() {
		return TimeUtils.getMinutesToMillis(2);
	}

	@Override
	public int getMaxProduce() {
		return 6;
	}

	@Override
	public void tick() {
		super.tick();
		if (getPlantedCrop() != null && getStage() == getPlantedCrop().getTotalStages() + 2) { // regrow tree
			setStage(getStage() - 1);
			update();
		}
	}

	@Override
	public void cropInteract() {
		if (getStage() == getPlantedCrop().getTotalStages()) {
			checkHealth();
		} else if (getStage() == getPlantedCrop().getTotalStages() + 1) {
			if (getProduceCount() == 0) {
				chop();
			} else {
				pick();
			}
		} else if (getStage() == getPlantedCrop().getTotalStages() + 2) {
			clear();
		}
	}

	@Override
	public boolean canPlant(Crop crop) {
		return crop instanceof FruitTreeCrop;
	}

	@Override
	public boolean isDiseaseImmune() {
		return false;
	}

	@Override
	public int calculateProduceAmount() {
		return 6; // fixed
	}

	@Override
	public boolean requiresCure() {
		return false;
	}

	@Override
	public FruitTreeCrop getPlantedCrop() {
		return (FruitTreeCrop) super.getPlantedCrop();
	}

	@Override
	public String getPatchName() {
		return "a fruit tree";
	}

	private void checkHealth() {
		player.sendMessage("You examine the tree and find that it is in perfect health.");
		getPlantedCrop().getCounter().increment(player, 1);
		player.getStats().addXp(StatType.Farming, getPlantedCrop().getCheckHealthXP(), true);
		int petChance = Random.get(getPlantedCrop().getPetOdds());
		if (petChance == 0) {
			Pet.TANGLEROOT.unlock(player, 0);
		}
		advanceStage();
		update();
	}

	private void chop() {
		Hatchet axe = Hatchet.find(player);
		if (axe == null) {
			player.sendMessage("You'll need an axe to chop down this tree.");
			return;
		}
		player.startEvent(event -> {
			player.animate(axe.animationId);
			event.delay(Random.get(4, 7));
			advanceStage();
			update();
			player.getStats().addXp(StatType.Woodcutting, 1, false);
			player.sendMessage("You chop down the tree.");
		});
	}

}
