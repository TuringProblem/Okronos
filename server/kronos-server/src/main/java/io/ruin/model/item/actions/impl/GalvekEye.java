package io.ruin.model.item.actions.impl;

import io.ruin.model.World;
import io.ruin.model.activities.duelarena.DuelRule;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;

public class GalvekEye {
	public static void register() {
		ItemAction.registerInventory(30591, "look-into", (player, item) -> init(player));
	}

	public static void init(Player player) {
		if (player.wildernessLevel > 0) {
			player.sendMessage("You can't use this here.");
			return;
		}
		if (DuelRule.NO_DRINKS.isToggled(player)) {
			player.sendMessage("You cannot use a Galvek's eye with drinks disabled.");
			return;
		}
		if (player.galvekEyeCooldown.isDelayed()) {
			int delay = player.galvekEyeCooldown.remaining();
			if (delay >= 100) {
				int minutes = delay / 100;
				player.sendMessage("The eye is still drained of its power. Judging by how it feels, it will be ready in around " + minutes + " minutes.");
			} else {
				int seconds = delay / 10 * 6;
				player.sendMessage("The eye is still drained of its power. Judging by how it feels, it will be ready in around " + seconds + " seconds.");
			}
		} else {
			player.graphics(1940);
			player.galvekEyeCooldown.delay(250);
			finished(player);
			World.startEvent(e -> {
				e.setCancelCondition(() -> player.wildernessLevel > 0);
				for (int i = 0; i < 15; i++) {
					player.hit(new Hit(HitType.HEAL).fixedDamage(5));
					Stat stat = player.getStats().get(StatType.Prayer);
					stat.restore(7, 0.05);
					if (stat.currentLevel > player.getStats().get(StatType.Prayer).fixedLevel)
						stat.currentLevel = player.getStats().get(StatType.Prayer).fixedLevel;
					player.getStats().get(StatType.Prayer).alter(stat.currentLevel);
					e.delay(4);
				}
			});
		}
	}

	public static void finished(Player player) {
		player.addEvent(event -> {
			event.delay(250);
			player.sendMessage("<col=FF0000>Your galvek eye has regained its magical power.");
		});
	}
}
