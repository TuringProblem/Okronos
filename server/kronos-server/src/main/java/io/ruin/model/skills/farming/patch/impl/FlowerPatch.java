package io.ruin.model.skills.farming.patch.impl;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.DancingInTheYields;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.skills.farming.crop.Crop;
import io.ruin.model.skills.farming.crop.impl.FlowerCrop;
import io.ruin.model.skills.farming.patch.Patch;
import io.ruin.model.stat.StatType;

public class FlowerPatch extends Patch {

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

	private transient boolean watered;

	@Override
	public int getCropVarpbitValue() {
		return getDiseaseStage() << 6 | (getPlantedCrop().getTotalStages()) + getPlantedCrop().getContainerIndex();
	}

	@Override
	public void cropInteract() {
		if (getStage() >= getPlantedCrop().getTotalStages()) {
			player.startEvent(event -> {
				while (true) {
					if (player.getInventory().getFreeSlots() == 0) {
						player.sendMessage("Not enough space in your inventory.");
						return;
					}
					if (getProduceCount() == 0) {
						reset(false);
						player.sendMessage("You've picked all the flowers from this patch.");
						return;
					}
					player.animate(2292);
					event.delay(2);
					player.collectResource(new Item(getPlantedCrop().getProduceId(), 1));
					player.getInventory().add(getPlantedCrop().getProduceId(), 1);
					player.getStats().addXp(StatType.Farming, getPlantedCrop().getHarvestXP(), true);
					player.sendFilteredMessage("You pick a flower.");
					getPlantedCrop().getCounter().increment(player, 1);
					removeProduce();
					event.delay(1);
				}
			});
		}
	}

	@Override
	public void reset(boolean weeds) {
		watered = false;
		super.reset(weeds);
	}

	@Override
	public void handleItem(Item item) {
		int can = AllotmentPatch.WATERING_CAN_IDS.indexOf(item.getId());
		if (can != -1) {
			if (can == 0) {
				player.sendMessage("Your watering can has no water in it.");
			} else if (getPlantedCrop() == null) {
				player.sendMessage("There is nothing to water on this patch.");
			} else if (isDiseased() || isDead()) {
				player.sendMessage("Water won't cure your crops.");
			} else if (getStage() >= getPlantedCrop().getTotalStages()) {
				player.sendMessage("Your crops are already fully grown.");
			} else {
				water(item, can);
			}
			return;
		} else {
			super.handleItem(item);
		}
	}

	@Override
	public boolean canPlant(Crop crop) {
		return crop instanceof FlowerCrop;
	}

	@Override
	public boolean isDiseaseImmune() {
		return watered;
	}

	@Override
	public int calculateProduceAmount() {
		int amount = Random.get(3, 5);
		if (getCompost() == 2) { // supercompost bonus
			amount += Random.get(1, 3);
		}
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
		return true;
	}

	@Override
	public FlowerCrop getPlantedCrop() {
		return super.getPlantedCrop() == null ? null : (FlowerCrop) super.getPlantedCrop();
	}

	@Override
	public String getPatchName() {
		return "a flower";
	}

	@Override
	protected void advanceStage() {
		super.advanceStage();
		watered = false;
	}

	private void water(Item item, int index) {
		player.startEvent(event -> {
			player.animate(2293);
			event.delay(2);
			watered = true;
			item.setId(AllotmentPatch.WATERING_CAN_IDS.get(index - 1));
			if (index == 1)
				player.sendMessage("Your watering can is now empty.");
			player.getStats().addXp(StatType.Farming, 1, false);
		});
	}

}
