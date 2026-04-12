package io.ruin.model.inter.questtab.main;

//import io.ruin.content.areas.wilderness.DeadmanChest;
//import io.ruin.content.areas.wilderness.DeadmanChestEvent;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class DeadmanChestEntry extends QuestTabEntry {

	public static final DeadmanChestEntry INSTANCE = new DeadmanChestEntry();

	@Override
	public void send(Player player) {//TODO OLEM UNCOMMENT
		//String chestStatus = DeadmanChestEvent.INSTANCE.getCurrentChest() == null ? DeadmanChestEvent.INSTANCE.timeRemaining() : "Active";
		//send(player, "DMM Chest", chestStatus, chestStatus.equalsIgnoreCase("Active") ? Color.GREEN : Color.RED);
	}

	@Override
	public void select(Player player) {//TODO OLEM UNCOMMENT
		//DeadmanChest chest = DeadmanChestEvent.INSTANCE.getCurrentChest();
		//if (chest != null) {
		//		player.sendMessage("The Deadman Supply Chest is located "+ chest.getLocation().getHint());
		//} else {
		//	player.sendMessage("The Deadman Supply Chest will spawn in "+ DeadmanChestEvent.INSTANCE.timeRemaining() +".");
		//}
	}
}
