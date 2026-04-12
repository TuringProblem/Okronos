package io.ruin.model.skills.farming.crop.impl;

import io.ruin.api.utils.TimeUtils;
import io.ruin.model.entity.player.PlayerCounter;
import io.ruin.model.skills.farming.crop.Crop;

public class HesporiCrop implements Crop {

	public static final HesporiCrop INSTANCE = new HesporiCrop();

	private HesporiCrop() {

	}

	@Override
	public int getSeed() {
		return 22875;
	}

	@Override
	public int getLevelReq() {
		return 68;
	}

	@Override
	public long getStageTime() {
		return 1;
	}

	@Override
	public int getTotalStages() {
		return 4;
	}

	@Override
	public double getDiseaseChance(int compostType) {
		switch (compostType) {
			case 2:
				return 0.2 / getTotalStages();
			case 1:
				return 0.3 / getTotalStages();
			case 0:
			default:
				return 0.35 / getTotalStages();
		}
	}

	@Override
	public double getPlantXP() {
		return 68;
	}

	@Override
	public int getContainerIndex() {
		return 4;
	}

	@Override
	public int getProduceId() {
		return -1;
	}

	@Override
	public double getHarvestXP() {
		return 0;
	}

	@Override
	public PlayerCounter getCounter() {
		return PlayerCounter.HARVESTED_HESPORI;
	}


}
