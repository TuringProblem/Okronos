package io.ruin.model.item.actions.impl;

import io.ruin.model.activities.duelarena.DuelRule;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.TickDelay;
import kotlinx.coroutines.Delay;

public class CrystalSaw {

	public static void register() {
		ItemAction.registerInventory(9625, 1, (player, item) -> init(player));

	}

	public static void init(Player player) {
		if (player.crystalSawCooldown.isDelayed()) {
			int delay = player.crystalSawCooldown.remaining();
			if (delay >= 100) {
				int minutes = delay / 100;
				player.sendMessage("The saw is still drained of its power. Judging by how it feels, it will be ready in around " + minutes + " minutes.");
			} else {
				int seconds = delay / 10 * 6;
				player.sendMessage("The saw is still drained of its power. Judging by how it feels, it will be ready in around " + seconds + " seconds.");
			}
		} else {
			player.graphics(1799);
			VarPlayerRepository.CRYSTAL_SAW_COOLDOWN.set(player, 700);
			player.crystalSawCooldown.delay(7000);
			player.getStats().get(StatType.Construction).boost(3, 0.00);
			player.sendMessage("You have activated your crystal saw's powers and temporarily gain an additional 3 Construction levels.");
			if (player.crystalSawCooldown.isDelayed()) {
				finished(player);
			}
		}
	}

	public static void finished(Player player) {
		player.addEvent(event -> {
			event.delay(7000);
			player.sendMessage("<col=FF0000>Your crystal saw has regained its magical power.");
			VarPlayerRepository.CRYSTAL_SAW_COOLDOWN.set(player, 0);

		});
	}
}
