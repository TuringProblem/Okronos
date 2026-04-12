package io.ruin.model.activities.tempevents;

import io.ruin.cache.Icon;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.utility.Broadcast;

public abstract class TemporaryEvent {

	protected int eventDuration = 6000; //Ticks

	protected void rewardWinner(Player player, Item reward) {
		if (player != null) {
			if (reward != null) {
				if (player.getInventory().getFreeSlots() < 1) {
					if (!player.getBank().contains(reward) && player.getBank().freeSlot() < 1)
						new GroundItem(reward).owner(player).position(player.getPosition()).spawn();
					else
						player.getBank().add(reward.getId(), reward.getAmount());
				} else
					player.getInventory().add(reward);
			}
		}
	}

	protected abstract void endEvent();

	protected abstract void process();//In case future events require a process method

	public abstract void start();
}
