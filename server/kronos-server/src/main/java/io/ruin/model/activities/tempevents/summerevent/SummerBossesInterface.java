package io.ruin.model.activities.tempevents.summerevent;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.map.Position;

import java.util.Map;

public class SummerBossesInterface {

	public static final int INTERFACE_ID = 1135;

	/*
	Displays the active bosses to kill and their progress, server wide kill counts.
	 */
	public void open(Player player) {
		if (SummerEvent.disabled) {
			return;
		}
		player.openInterface(ToplevelComponent.MAINMODAL, INTERFACE_ID);
		for(int i = 0; i < 8; i++) {
			player.getPacketSender().setHidden(INTERFACE_ID, 17 + (i * 12), true);
		}
		int i = 0;
		for (Map.Entry<String, Integer> entry : SummerEvent.activeBossNames.entrySet()) {
			String name = entry.getKey();
			int amount = entry.getValue();
			int startingAmount = SummerEvent.startingAmountOfBosses.getOrDefault(name, 0);
			int amountKilled = startingAmount - amount;
			player.getPacketSender().setHidden(INTERFACE_ID, 17 + (i * 12), false);
			player.getPacketSender().sendString(INTERFACE_ID, 21 + (i * 12), name);
			player.getPacketSender().sendString(INTERFACE_ID, 27 + (i * 12), amountKilled + "/" + startingAmount);
			int barTextureInterfaceHash = INTERFACE_ID << 16 | 26 + (i * 12);
			int barInterfaceHash = INTERFACE_ID << 16 | 25 + (i * 12);
			float barPercentage = ((float) amountKilled / startingAmount) * 441;

			if(amountKilled == 0)
				barPercentage = 0;

			player.getPacketSender().sendClientScript(10607, "Ii", barInterfaceHash, (int) barPercentage);
			player.getPacketSender().sendClientScript(10608, "Ii", barTextureInterfaceHash, (int) barPercentage);
			i++;
		}
	}
}
