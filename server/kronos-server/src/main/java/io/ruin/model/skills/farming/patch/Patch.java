package io.ruin.model.skills.farming.patch;

import com.google.gson.annotations.Expose;
import io.ruin.api.utils.Random;
import io.ruin.api.utils.TimeUtils;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.handlers.TabStats;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.skills.farming.crop.Crop;
import io.ruin.model.skills.farming.crop.TreeCrop;
import io.ruin.model.stat.StatType;

public abstract class Patch {

	protected transient Player player;
	private transient Crop plantedCrop;
	public transient PatchData data;
	private transient boolean update;

	protected int stage;
	private int compost;
	private int raked;
	private long timePlanted;
	private int diseaseStage;
	private int produceCount;
	private long lastWeedGrowth;
	private boolean farmerProtected;
	private int plantedSeed = -1;

	public Patch() {

	}

	public Patch set(PatchData data) {
		this.data = data;
		return this;
	}

	public Patch setPlayer(Player player) {
		this.player = player;
		return this;
	}

	public abstract int getCropVarpbitValue();

	public abstract void cropInteract();

	public abstract boolean canPlant(Crop crop);

	public abstract boolean isDiseaseImmune();

	public abstract int calculateProduceAmount();

	/**
	 * @return true if this patch is cured through plant cure, false if cured
	 * through pruning
	 */
	public abstract boolean requiresCure();

	public void update() {
		if (player.getFarming().getVisibleGroup() != null
			&& player.getFarming().getVisibleGroup().getPatches().contains(data))
			send();
	}

	public void send() {
		data.getConfig().set(player, getVarpbitValue());
	}

	public void interact() {
		if (!isRaked()) {
			if (!player.getInventory().contains(5341, 1)) {
				player.sendMessage("You need a rake to weed a farming patch.");
				return;
			}
			player.animate(2273);
			player.startEvent(event -> {
				while (true) {
					player.animate(2273);
					player.privateSound(2442);
					event.delay(4);
					if (Random.get() < 0.8) {
						raked++;
						player.getStats().addXp(StatType.Farming, 3, true);
						player.getInventory().addOrDrop(6055, 1);
						lastWeedGrowth = System.currentTimeMillis();
						send();
					}
					if (isRaked()) {
						if (getPatchName().contains("herb")) {
							player.herbPatchesRaked++;
							if (player.herbPatchesRaked == Achievements.THE_GRASS_IS_ALWAYS_GREENER.getCompletionAmount())
								player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
									+ Achievements.THE_GRASS_IS_ALWAYS_GREENER.getAchievementName());
						}
						player.resetAnimation();
						return;
					}
				}
			});
		} else if (plantedCrop == null) {
			player.sendMessage("The patch is clear for new crops. "
				+ (compost > 0 ? "It has been treated with " + (compost == 1 ? "regular" : "super") + " compost." : ""));
		} else if (isDead()) {
			clear();
		} else if (isDiseased()) {
			if (requiresCure()) {
				usePlantCure();
			} else {
				prune();
			}
		} else {
			cropInteract();
		}
	}

	public void tick() {
		if (plantedCrop == null && raked == 0)
			/* do nothing */
			return;
		if (plantedCrop == null && System.currentTimeMillis() - lastWeedGrowth >= TimeUtils.getMinutesToMillis(4)) {
			growWeeds();
			return;
		}
		if (isNextStageReady()) {
			diseaseRoll();
			if (!isDead()) {
				advanceStage();
				setNeedsUpdate(true);
			}
		}
	}

	public boolean isDiseased() {
		return diseaseStage == 1;
	}

	public boolean isDead() {
		return diseaseStage == 2;
	}

	public long getTimeElapsed() {
		return System.currentTimeMillis() - getTimePlanted();
	}

	public void clear() {
		if (getPlantedCrop() == null) {
			player.sendMessage("This patch doesn't have anything planted on it.");
			return;
		}
		if (!player.getInventory().contains(952, 1)) {
			player.sendMessage("You will need a spade to clear this patch.");
			return;
		}
		if (isDead()) {
		}
		// else if (getStage() != getPlantedCrop().getTotalStages()) {
		// player.sendMessage("You must wait for it to finish growing!");
		// return;
		// }
		player.startEvent(event -> {
			player.animate(831);
			event.delay(Random.get(2, 4));
			player.resetAnimation();
			reset(false);
			player.sendMessage("You clear the patch for new crops.");
		});
	}

	public void reset(boolean weeds) {
		setPlantedCrop(null);
		stage = 0;
		diseaseStage = 0;
		produceCount = 0;
		timePlanted = 0;
		compost = 0;
		raked = weeds ? 0 : 3;
		farmerProtected = false;
		update();
	}

	public void treat(Item item) {
		int compostType;
		if (item.getId() == 6032)
			compostType = 1;
		else if (item.getId() == 6034)
			compostType = 2;
		else
			throw new IllegalArgumentException("Invalid compost");

		if (!isRaked()) {
			player.sendMessage("You should clear the patch first.");
			return;
		}
		if (plantedCrop != null) {
			player.sendMessage("Something is already growing in this patch.");
			return;
		}
		if (getCompost() >= compostType) {
			player.sendMessage("This patch has already been treated.");
			return;
		}
		setCompost(compostType);
		player.animate(2283);
		if (!player.discardBuckets)
			item.setId(1925);
		else
			item.remove();
		player.getStats().addXp(StatType.Farming, 4, true);
		player.sendMessage("You treat the patch with " + (compostType == 2 ? "super" : "") + " compost.");
	}

	public void plant(Item item) {
		Crop crop = item.getDef().seedType;
		if (!canPlant(crop))
			return;
		boolean sapling = false;
		if (crop instanceof TreeCrop) {
			sapling = true;
		}
		String type = sapling ? "sapling" : "seed";
		if (!player.getStats().check(StatType.Farming, crop.getLevelReq(), "plant that " + type)) {
			return;
		}
		if (!player.getInventory().contains(sapling ? 952 : 5343, 1)) {
			player.sendMessage("You need a " + (sapling ? "spade" : "seed dibber") + " to plant " + type + "s.");
			return;
		}
		final boolean tree = sapling;
		player.animate(sapling ? 830 : 2291);
		player.startEvent(event -> {
			event.delay(2);
			player.sendFilteredMessage("You plant the " + type + " in the patch.");
			if (!tree)
				player.getInventory().remove(crop.getSeed(), 1);
			else if (item.getId() != crop.getSeed())
				item.setId(5350);
			else
				player.getInventory().remove(crop.getSeed(), 1);
			setPlantedCrop(crop);
			produceCount = calculateProduceAmount();
			player.getStats().addXp(StatType.Farming, crop.getPlantXP(), true);
			timePlanted = System.currentTimeMillis();
			send();
		});
	}

	public void handleItem(Item item) {
		if (item.getDef().seedType != null) {
			if (plantedCrop != null) {
				player.sendMessage("There is already something growing in this patch.");
			} else if (canPlant(item.getDef().seedType)) {
				plant(item);
			} else {
				player.sendMessage("You can't plant that seed on this type of patch.");
			}
		} else if (item.getId() == 6032 || item.getId() == 6034) { // compost / supercompost
			treat(item);
		} else if (item.getId() == 6036) { // plant cure
			if (isDead()) {
				player.sendMessage("Plant cure can't bring plants back from the dead! You should clear the patch.");
			} else if (!isDiseased()) {
				player.sendMessage("This patch is perfectly healthy, there is no need use plant cure on it.");
			} else {
				if (requiresCure())
					usePlantCure();
				else
					player.sendMessage("You should try pruning the patch instead.");
			}
		} else if (item.getId() == 952) { // spade, force clear
			clear();
		} else if (item.getId() == 5350) { // plant pot
			if (!isRaked()) {
				player.sendMessage("You should clear the patch first.");
			} else {
				fillPlantPots();
			}
		} else {
			player.sendMessage("Nothing interesting happens.");
		}
	}

	public void fillPlantPots() {
		if (!player.getInventory().contains(5325, 1)) {
			player.sendMessage("You'll need a gardening trowel to fill plant pots.");
			return;
		}
		player.startEvent(event -> {
			player.animate(2272);
			event.delay(4);
			int amount = player.getInventory().remove(5350, 28);
			if (amount > 0)
				player.getInventory().add(5356, amount);
		});
	}

	public int getCompost() {
		return compost;
	}

	public void setCompost(int compost) {
		this.compost = compost;
	}

	public int getVarpbitValue() {
		if (plantedCrop != null)
			return getCropVarpbitValue();
		else if (!isRaked())
			return getRaked();
		else if (plantedCrop == null)
			return 3;
		return 0;
	}

	public boolean isRaked() {
		return raked >= 3;
	}

	public int getRaked() {
		return raked;
	}

	public void setRaked(int raked) {
		this.raked = raked;
	}

	public Crop getPlantedCrop() {
		return plantedCrop;
	}

	public void setPlantedCrop(Crop plantedCrop) {
		this.plantedCrop = plantedCrop;
		this.plantedSeed = plantedCrop == null ? -1 : plantedCrop.getSeed();
		this.setStage(getStage());
		data.getConfig().set(player, getVarpbitValue());

	}

	public int getObjectId() {
		return data.getObjectId();
	}

	public int getDiseaseStage() {
		return diseaseStage;
	}

	public void setDiseaseStage(int diseaseStage) {
		this.diseaseStage = diseaseStage;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public long getTimePlanted() {
		return timePlanted;
	}

	public void setTimePlanted(long timePlanted) {
		this.timePlanted = timePlanted;
	}

	public int getProduceCount() {
		return produceCount;
	}

	public void setProduceCount(int produceCount) {
		this.produceCount = produceCount;
	}

	public boolean removeProduce() {

		if ((player.getEquipment().getId(Equipment.SLOT_WEAPON) == 7409 || player.getInventory().contains(7409, 1))
			&& Random.get() < (0.2)) { // magic secateurs, save a "life"
			player.sendFilteredMessage("<col=00ffff>Your magic secateurs allow you to efficiently harvest the crops.");
			return false;
		}
		if (--produceCount <= 0) {
			produceCount = 0;
			return true;
		}
		return false;
	}

	public boolean needsUpdate() {
		return update;
	}

	public void setNeedsUpdate(boolean update) {
		this.update = update;
	}

	public void onLoad() {
		if (plantedSeed > 0) {
			Crop crop = ObjType.get(plantedSeed).seedType;
			if (crop != null && canPlant(crop))
				plantedCrop = crop;
			else
				reset(true); // invalid seed planted, reset patch
		}
		while (isNextStageReady()) { // important to simulate natural growth so no cycles are skipped
			tick();
		}
	}

	public void objectAction(int option) {
		if (option == 1)
			interact();
		else if (option == 2)
			inspect();
		else if (option == 3)
			clear();
		else if (option == 4)
			TabStats.openGuide(player, StatType.Farming, data.getGuideChildId());
	}

	public abstract String getPatchName();

	public boolean isFarmerProtected() {
		return farmerProtected;
	}

	public void setFarmerProtected(boolean farmerProtected) {
		this.farmerProtected = farmerProtected;
	}

	protected boolean isNextStageReady() {
		if (getPlantedCrop() == null)
			return false;
		int actualStage = (int) Math.floor(getTimeElapsed() / (plantedCrop.getStageTime()));
		return stage < plantedCrop.getTotalStages() && actualStage > stage && !isDead();
	}

	protected void diseaseRoll() {
		double diseaseChance = plantedCrop.getDiseaseChance(compost);
		if (!isFarmerProtected() && !isDiseaseImmune() && diseaseStage == 0
			&& getStage() != (plantedCrop.getTotalStages() - 1) && Random.get() < diseaseChance) {
			diseaseStage = 1;
			setNeedsUpdate(true);
		} else if (isDiseased()) {
			diseaseStage = 2; // rip
			setNeedsUpdate(true);
		}
	}

	protected void advanceStage() {
		stage++;
	}

	void prune() {
		if (!player.getFarming().hasSecateurs()) {
			player.sendMessage("You'll need some secateurs to prune this tree.");
			return;
		}
		player.startEvent(event -> {
			player.animate(2274);
			event.delay(3);
			if (isDead() || getPlantedCrop() == null || !isDiseased())
				return;
			player.resetAnimation();
			player.sendMessage("You prune the calquat tree, curing it of its disease.");
			setDiseaseStage(0);
			update();
		});
	}

	void usePlantCure() {
		if (!player.getInventory().contains(6036, 1)) {
			player.sendMessage("You don't have any plant cure to use.");
			return;
		}
		player.animate(2288);
		player.getInventory().remove(6036, 1);
		setDiseaseStage(0);
		update();
		player.sendMessage("The plant cure completely cures the disease. Your crops are now perfectly healthy.");
	}

	void growWeeds() {
		raked -= 1;
		setNeedsUpdate(true);
		lastWeedGrowth += TimeUtils.getMinutesToMillis(4);
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

	private void inspect() {
		String msg = "";
		msg += "This is " + getPatchName() + " patch. ";
		if (getCompost() == 0) {
			msg += "The soil has not been treated. ";
		} else {
			msg += "The soil has been trated with ";
			msg += getCompost() == 1 ? "compost" : "supercompost";
			msg += ". ";
		}
		if (getPlantedCrop() == null) {
			if (isRaked())
				msg += "The patch is clear for new crops. ";
			else
				msg += "The patch neeeds weeding.";
		} else {
			msg += "The patch has something growing in it.";
		}
		player.sendMessage(msg);
	}
}
