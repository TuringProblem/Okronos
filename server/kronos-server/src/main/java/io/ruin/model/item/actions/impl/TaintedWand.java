package io.ruin.model.item.actions.impl;

import io.ruin.cache.Color;
import io.ruin.model.activities.duelarena.DuelRule;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;

public class TaintedWand {

	public static void register() {
		ItemAction.registerInventory(30598, 2, (player, item) -> init(player));
	}

	public static void init(Player player) {
		if (DuelRule.NO_DRINKS.isToggled(player)) {
			player.sendMessage("You cannot use your Tainted wand's power up with no drinks enabled.");
			return;
		}
		if (player.magicTaintedWandCooldown.isDelayed()) {
			int delay = player.magicTaintedWandCooldown.remaining();
			if (delay >= 100) {
				int minutes = delay / 100;
				player.sendMessage(Color.DARK_RED, "The wand isn't ready for you to siphon any more energy. Judging by how it feels, it will be ready in around " + minutes + " minutes.");
			} else {
				int seconds = delay / 10 * 6;
				player.sendMessage(Color.DARK_RED, "The wand isn't ready for you to siphon any more energy. Judging by how it feels, it will be ready in around " + seconds + " seconds.");
			}
		} else {
			player.graphics(1398);
			player.animate(533);
			VarPlayerRepository.TAINTED_WAND_COOLDOWN.set(player, 70);
			player.magicTaintedWandCooldown.delay(700);
			player.sendMessage(Color.DARK_RED, "While the power of the magi knocks you down, it smiles upon you.");
			player.getStats().get(StatType.Magic).boost(1, 0.16);
			player.getStats().get(StatType.Defence).boost(1, 0.08);
			finished(player);
		}
	}

	public static void finished(Player player) {
		player.addEvent(event -> {
			event.delay(700);
			player.sendMessage(Color.DARK_RED, "Your tainted wand's aura speaks to you. The cooldown is up.");
			VarPlayerRepository.TAINTED_WAND_COOLDOWN.set(player, 0);

		});
	}
}
