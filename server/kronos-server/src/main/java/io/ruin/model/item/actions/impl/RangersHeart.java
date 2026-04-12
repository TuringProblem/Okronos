package io.ruin.model.item.actions.impl;

import io.ruin.model.activities.duelarena.DuelRule;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.TickDelay;
import kotlinx.coroutines.Delay;

public class RangersHeart {

	public static void register() {
		ItemAction.registerInventory(30502, 1, (player, item) -> init(player));

	}

	public static void init(Player player) {
		if (DuelRule.NO_DRINKS.isToggled(player)) {
			player.sendMessage("You cannot use an Rangers Heart with drinks disabled.");
			return;
		}
		if (player.rangersHeartCooldown.isDelayed()) {
			int delay = player.rangersHeartCooldown.remaining();
			if (delay >= 100) {
				int minutes = delay / 100;
				player.sendMessage("The heart is still drained of its power. Judging by how it feels, it will be ready in around " + minutes + " minutes.");
			} else {
				int seconds = delay / 10 * 6;
				player.sendMessage("The heart is still drained of its power. Judging by how it feels, it will be ready in around " + seconds + " seconds.");
			}
		} else {
			player.graphics(1316);
			VarPlayerRepository.IMBUED_HEART_COOLDOWN.set(player, 70);
			player.rangersHeartCooldown.delay(700);
			player.getStats().get(StatType.Ranged).boost(2, 0.18);
			player.getStats().get(StatType.Defence).boost(2, 0.18);
			if (player.rangersHeartCooldown.isDelayed()) {
				finished(player);
			}
		}
	}

	public static void finished(Player player) {
		player.addEvent(event -> {
			event.delay(700);
			player.sendMessage("<col=FF0000>Your rangers heart has regained its magical power.");
			VarPlayerRepository.IMBUED_HEART_COOLDOWN.set(player, 0);

		});
	}
}
