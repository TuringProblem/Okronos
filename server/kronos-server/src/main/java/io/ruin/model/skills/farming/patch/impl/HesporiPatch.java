package io.ruin.model.skills.farming.patch.impl;

import io.ruin.cache.ObjType;
import io.ruin.model.item.Item;
import io.ruin.model.skills.farming.crop.Crop;
import io.ruin.model.skills.farming.crop.impl.HesporiCrop;
import io.ruin.model.skills.farming.patch.Patch;
import io.ruin.model.stat.StatType;

public class HesporiPatch extends Patch {

	@Override
	public int getCropVarpbitValue() {
		return getDiseaseStage() << 6 | (getPlantedCrop().getTotalStages()) + getPlantedCrop().getContainerIndex();
	}

	@Override
	public void cropInteract() {
		if (isDead()) {
			clear();
			return;
		}
		if (getProduceCount() == 0) {
			reset(false);
			return;
		}
		player.startEvent(event -> {
			while (true) {
				if (player.getInventory().getFreeSlots() == 0) {
					player.sendMessage("Not enough space in your inventory.");
					return;
				}
				if (getProduceCount() == 0) {
					reset(false);
					player.sendMessage("You've picked all the herbs from this patch.");
					return;
				}
				player.animate(2282);
				event.delay(2);
				player.collectResource(new Item(getPlantedCrop().getProduceId(), 1));
				player.getInventory().add(getPlantedCrop().getProduceId(), 1);
				player.getStats().addXp(StatType.Farming, getPlantedCrop().getHarvestXP(), true);
				player.sendFilteredMessage("You pick a " + ObjType.get(getPlantedCrop().getProduceId()).name + ".");
				getPlantedCrop().getCounter().increment(player, 1);
				removeProduce();
				event.delay(1);
			}
		});
	}

	@Override
	public boolean canPlant(Crop crop) {
		return crop instanceof HesporiCrop;
	}

	@Override
	public boolean isDiseaseImmune() {
		return true;
	}

	@Override
	public int calculateProduceAmount() {
		return 0;
	}

	@Override
	public boolean requiresCure() {
		return false;
	}

	@Override
	public String getPatchName() {
		return "a Hespori";
	}
}
