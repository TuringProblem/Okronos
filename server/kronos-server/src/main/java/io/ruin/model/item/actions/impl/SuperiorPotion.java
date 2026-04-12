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

public class SuperiorPotion {
	public static void register() {
		ItemAction.registerInventory(30594, 1, (player, item) -> init(player));
	}

	public static void init(Player player) {
		if (player.wildernessLevel > 0) {
			player.sendMessage("You can't use this here.");
			return;
		}
		if (DuelRule.NO_DRINKS.isToggled(player)) {
			player.sendMessage("You cannot use a superior potion with drinks disabled.");
			return;
		}
		if (player.superiorPotionCooldown.isDelayed()) {
			player.superiorPotionCooldown.reset();
		}
		player.graphics(1800);
		player.superiorPotionCooldown.delay(500);
		World.startEvent(e -> {
			e.setCancelCondition(() -> player.wildernessLevel > 0);
			for (int i = 0; i < 20; i++) {
				player.getStats().get(StatType.Attack).boost(6, 0.22);
				player.getStats().get(StatType.Strength).boost(6, 0.22);
				player.getStats().get(StatType.Defence).boost(6, 0.22);
				player.getStats().get(StatType.Ranged).boost(6, 0.22);
				player.getStats().get(StatType.Magic).boost(6, 0.22);
				e.delay(25);
				if (i == 19)
					finished(player);
			}
		});
	}

	public static void finished(Player player) {
		if (player.superiorPotionCooldown.remaining() < 1) {
			player.sendMessage("<col=FF0000>Your superior potion has ran out and your stats return to normal.");
			player.getStats().get(StatType.Attack).restore();
			player.getStats().get(StatType.Strength).restore();
			player.getStats().get(StatType.Defence).restore();
			player.getStats().get(StatType.Ranged).restore();
			player.getStats().get(StatType.Magic).restore();
		}
	}
}
