package io.ruin.model.skills.farming.patch.impl;


import io.ruin.api.utils.Random;
import io.ruin.cache.ObjType;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.PerkTaskHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.DancingInTheYields;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.skills.CapePerks;
import io.ruin.model.skills.farming.crop.Crop;
import io.ruin.model.skills.farming.crop.impl.HerbCrop;
import io.ruin.model.skills.farming.patch.Patch;
import io.ruin.model.stat.StatType;

public class HerbPatch extends Patch {

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
		if (stage < getPlantedCrop().getTotalStages())
			stage = getPlantedCrop().getTotalStages();
		return getDiseaseStage() << 6 | stage + getPlantedCrop().getContainerIndex();
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
				if (Random.get(50000) == 0)
					Pet.TANGLEROOT.unlock(player, 0);
				PerkTaskHandler.handleGatherResource(player, getPlantedCrop().getProduceId(), 1);
				DailyTasks.handleItemObtained(player, getPlantedCrop().getProduceId(), StatType.Farming);
				player.collectResource(new Item(getPlantedCrop().getProduceId(), 1));
				player.getInventory().addOrDrop(getPlantedCrop().getProduceId(), 1);
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
		return crop instanceof HerbCrop;
	}

	@Override
	public boolean isDiseaseImmune() {
		return getObjectId() == 18816; // trollheim patch no disease
	}

	@Override
	public int calculateProduceAmount() {
		int amount = Random.get(8, 10);
		if (getCompost() == 2) { // supercompost bonus
			amount += Random.get(3, 5);
		}
		if (CapePerks.wearsFarmingCape(player)) {
			amount += Random.get(1, 2);
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.DANCING_IN_THE_YIELDS)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.DANCING_IN_THE_YIELDS);
			DancingInTheYields c = (DancingInTheYields) player.getPlayerPerkHandler().
				getActivePerks(player).get(perkIndex).getPerk(player);
			float multiplier = 1;
			multiplier += c.getYieldBonus();
			amount *= multiplier;
		}
		amount *= yieldBoost(player);
		return amount;
	}

	@Override
	public boolean requiresCure() {
		return true;
	}

	@Override
	public String getPatchName() {
		return "a herb";
	}

}

