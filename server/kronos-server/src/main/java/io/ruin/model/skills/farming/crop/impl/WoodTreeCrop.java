package io.ruin.model.skills.farming.crop.impl;

import io.ruin.api.utils.TimeUtils;
import io.ruin.model.entity.player.PlayerCounter;
import io.ruin.model.item.Item;
import io.ruin.model.skills.farming.crop.TreeCrop;
import io.ruin.model.skills.woodcutting.Tree;
import lombok.Getter;

import static io.ruin.cache.ItemID.COINS_995;

public enum WoodTreeCrop implements TreeCrop {
	OAK(5312, 6043, 5370, 5358, 5364, Tree.OAK, 15, 14, 467.3, 4, new Item(COINS_995, 4000), 8, PlayerCounter.GROWN_OAK, 3500),
	WILLOW(5313, 6045, 5371, 5359, 5365, Tree.WILLOW, 30, 25, 1456.5, 6, new Item(COINS_995, 8000), 15, PlayerCounter.GROWN_WILLOW, 3000),
	MAPLE(5314, 6047, 5372, 5360, 5366, Tree.MAPLE, 45, 45, 3403.4, 8, new Item(COINS_995, 20000), 24, PlayerCounter.GROWN_MAPLE, 2500),
	YEW(5315, 6049, 5373, 5361, 5367, Tree.YEW, 60, 81, 7069, 10, new Item(COINS_995, 70000), 35, PlayerCounter.GROWN_YEW, 2000),
	MAGIC(5316, 6051, 5374, 5362, 5368, Tree.MAGIC, 75, 145.5, 13768.3, 12, new Item(COINS_995, 120000), 48, PlayerCounter.GROWN_MAGIC, 800),
	REDWOOD(22871, -1, 22859, 22850, 22854, Tree.REDWOOD, 90, 230, 22450, 11, new Item(COINS_995, 200000), 8, PlayerCounter.GROWN_REDWOOD, 500),
	CELASTRUS(22869, -1, 22856, 22848, 22852, Tree.CELASTRUS, 50, 500, 5555, 6, new Item(COINS_995, 2555555), 8, PlayerCounter.GROWN_CELASTRUS, 400);


	WoodTreeCrop(int seedId, int roots, int sapling, int seedling, int wateredSeedling, Tree treeType, int levelReq, double plantXP, double harvestXP, int totalStages, Item payment, int containerIndex, PlayerCounter counter, int petOdds) {
		this.plantXP = plantXP;
		this.checkHealthXP = harvestXP;
		this.seedId = seedId;
		this.treeType = treeType;
		this.levelReq = levelReq;
		this.containerIndex = containerIndex;
		this.totalStages = totalStages;
		this.roots = roots;
		this.counter = counter;
		this.payment = payment;
		this.sapling = sapling;
		this.seedling = seedling;
		this.wateredSeedling = wateredSeedling;
		this.petOdds = petOdds;
	}

	private final double plantXP, checkHealthXP;
	private final Tree treeType;
	private final int seedId, levelReq, containerIndex, sapling, seedling, wateredSeedling;
	private final int totalStages;
	private final int roots;
	private final PlayerCounter counter;
	@Getter
	private final int petOdds;

	@Override
	public int getSeed() {
		return seedId;
	}

	@Override
	public int getLevelReq() {
		return levelReq;
	}

	@Override
	public long getStageTime() {
		return 1;
	}

	@Override
	public int getTotalStages() {
		return totalStages;
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
				return 0.4 / getTotalStages();
		}
	}

	@Override
	public double getPlantXP() {
		return plantXP;
	}

	@Override
	public int getContainerIndex() {
		return containerIndex;
	}

	@Override
	public int getProduceId() {
		return treeType.log;
	}

	@Override
	public double getHarvestXP() {
		return checkHealthXP;
	}

	public Tree getTreeType() {
		return treeType;
	}

	public int getRoots() {
		return roots;
	}

	@Override
	public PlayerCounter getCounter() {
		return counter;
	}

	@Override
	public Item getPayment() {
		return payment;
	}

	private Item payment;

	@Override
	public int getSapling() {
		return sapling;
	}

	@Override
	public int getSeedling() {
		return seedling;
	}

	@Override
	public int getWateredSeedling() {
		return wateredSeedling;
	}


}
