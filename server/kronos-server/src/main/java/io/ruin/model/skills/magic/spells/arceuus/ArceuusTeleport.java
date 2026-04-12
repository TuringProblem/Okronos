package io.ruin.model.skills.magic.spells.arceuus;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.content.HomeHandler;
import io.ruin.model.skills.magic.Spell;

public class ArceuusTeleport extends Spell {

	public ArceuusTeleport(int lvlReq, double xp, Bounds bounds, Item... runeItems) {
		registerClick(lvlReq, xp, true, runeItems, (p, i) -> teleport(p, bounds));
	}

	public ArceuusTeleport(int lvlReq, double xp, int x, int y, int z, Item... runeItems) {
		registerClick(lvlReq, xp, true, runeItems, (p, i) -> teleport(p, x, y, z));
	}

	public static boolean teleport(Player player, Bounds bounds) {
		return teleport(player, bounds.randomX(), bounds.randomY(), bounds.z);
	}

	public static boolean teleport(Player player, Position position) {
		return teleport(player, position.getX(), position.getY(), position.getZ());
	}

	public static boolean teleport(Player player, int x, int y, int z) {
		return player.getMovement().startTeleport(e -> {
			e.setCancelCondition(() -> player.teleportListener != null && !player.teleportListener.allow(player));
			if (player.getInventory().contains(25104)) {
				player.sendMessage("The crystal of memories stores your last location as an available teleport.");
				player.crystalMemoryPosition = player.getPosition().copy();
			}
			player.animate(8805);
			player.graphics(1822, 92, 0);
			//player.publicSound(200);
			e.delay(5);
			player.animate(8807);
			player.graphics(1823, 92, 0);
			e.delay(3);
			player.getMovement().teleport(x, y, z);
		});
	}
}