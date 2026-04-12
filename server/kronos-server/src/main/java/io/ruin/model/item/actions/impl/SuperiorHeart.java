package io.ruin.model.item.actions.impl;

import io.ruin.model.World;
import io.ruin.model.activities.duelarena.DuelRule;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.stat.StatType;

public class SuperiorHeart {
	public static void register() {
		ItemAction.registerInventory(30380, 1, (player, item) -> init(player));
	}

	public static void init(Player player) {
		if (player.wildernessLevel > 0) {
			player.sendMessage("You can't use this here.");
			return;
		}
		if (DuelRule.NO_DRINKS.isToggled(player)) {
			player.sendMessage("You cannot use an Imbued Heart with drinks disabled.");
			return;
		}
		if (player.superiorHeartCooldown.isDelayed()) {
			player.superiorPotionCooldown.reset();
		}
		player.graphics(1793);
		player.superiorHeartCooldown.delay(500);
		World.startEvent(e -> {
			e.setCancelCondition(() -> player.wildernessLevel > 0 || !player.isOnline());
			for (int i = 0; i < 20; i++) {
				player.getStats().get(StatType.Attack).boost(7, 0.25);
				player.getStats().get(StatType.Strength).boost(7, 0.25);
				player.getStats().get(StatType.Defence).boost(7, 0.25);
				player.getStats().get(StatType.Ranged).boost(7, 0.25);
				player.getStats().get(StatType.Magic).boost(7, 0.25);
				e.delay(25);
				if (i == 19)
					finished(player);
			}
		});
	}

	public static void finished(Player player) {
		if (player.superiorHeartCooldown.remaining() < 10) {
			player.sendMessage("<col=FF0000>Your superior overload heart has ran out and your stats return to normal.");
			player.getStats().get(StatType.Attack).restore();
			player.getStats().get(StatType.Strength).restore();
			player.getStats().get(StatType.Defence).restore();
			player.getStats().get(StatType.Ranged).restore();
			player.getStats().get(StatType.Magic).restore();
		}
	}
}
