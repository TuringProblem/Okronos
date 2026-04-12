package io.ruin.model.item;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.SecondaryGroup;
import io.ruin.model.item.actions.ItemAction;

public class CrystalMemories {

	private static int getMemoryCooldown(Player player) {
		if (player.getSecondaryGroup() == SecondaryGroup.DONATOR) {
			return 150;
		} else if (player.getSecondaryGroup() == SecondaryGroup.SUPER_DONATOR) {
			return 120;
		} else if (player.getSecondaryGroup() == SecondaryGroup.ELITE_DONATOR) {
			return 100;
		} else if (player.getSecondaryGroup() == SecondaryGroup.NOBLE_DONATOR) {
			return 80;
		} else if (player.getSecondaryGroup() == SecondaryGroup.GOLD_DONATOR) {
			return 45;
		} else if (player.getSecondaryGroup() == SecondaryGroup.PLATINUM_DONATOR) {
			return 30;
		} else if (player.getSecondaryGroup() == SecondaryGroup.LEGENDARY_DONATOR) {
			return 15;
		} else if (player.getSecondaryGroup() == SecondaryGroup.SUPREME_DONATOR) {
			return 0;
		}
		return 180;
	}

	public static void register() {
		ItemAction.registerInventory(25104, "teleport-back", (player, item) -> {
			if (player.crystalMemoryPosition == null) {
				player.sendMessage("You have not stored a location in the crystal of memories.");
				return;
			}
			if (player.crystalMemoryDelay.remaining() > 0) {
				int delay = player.galvekEyeCooldown.remaining();
				if (delay >= 100) {
					int minutes = delay / 100;
					player.sendMessage("Your crystal memories is still drained of its power. It will be ready in around " + minutes + " minutes.");
				} else {
					int seconds = delay / 10 * 6;
					player.sendMessage("Your crystal memories is still drained of its power. It will be ready in around " + seconds + " seconds.");
				}
				return;
			}
			player.getMovement().startTeleport(e -> {
				player.animate(8805);
				player.sendMessage("The crystal of memories teleports you to your stored location.");
				player.graphics(1822, 92, 0);
				e.delay(5);
				player.animate(8807);
				player.graphics(1823, 92, 0);
				e.delay(3);
				player.getMovement().teleport(player.crystalMemoryPosition);
				player.crystalMemoryDelay.delaySeconds(getMemoryCooldown(player));
			});
		});
	}
}
