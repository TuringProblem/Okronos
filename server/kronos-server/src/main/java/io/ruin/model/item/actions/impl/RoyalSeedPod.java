package io.ruin.model.item.actions.impl;

import io.ruin.model.World;
import io.ruin.model.activities.raids.toa.TombsOfAmascutManager;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.map.Position;
import io.ruin.model.skills.magic.spells.HomeTeleport;

public class RoyalSeedPod {

	public static void register() {
		ItemAction.registerInventory(19564, "commune", (player, item) -> {
			if (player.teleportListener != null && !player.teleportListener.allow(player)) {
				return;
			}
			if (player.getCombat().tbTicks >= 1) {
				player.sendMessage("A mysterious force stops you from doing this.");
				return;
			}
			if (player.insideRaid || TombsOfAmascutManager.getRaid(player) != null) {
				player.sendMessage("You cannot use the seed pod while inside a raid.");
				return;
			}
			if (player.gauntlet != null) {
				player.sendMessage("You cannot use the seed pod while in gauntlet.");
				return;
			}
			if (player.inferno != null) {
				player.sendMessage("You cannot use the seed pod while in inferno.");
				return;
			}
			if (player.wildernessLevel > 30) {
				player.sendMessage("You cannot use the seed pod above level 30 wilderness.");
			}
			else {
				player.lock(LockType.FULL_NULLIFY_DAMAGE);
				player.graphics(767);
				player.animate(4544);
				World.startEvent(3, _ -> {
					if (player.insideRaid) {
						player.graphics(-1);
						player.animate(-1);
						player.unlock();
						return;
					}
					Position override = HomeTeleport.getHomeTeleportOverride(player);
					if (override != null)
						player.getMovement().teleport(override);
					else {
						if (!player.edgeHome)
							player.getMovement().teleport(World.HOME);
						else
							player.getMovement().teleport(World.EDGEHOME);
					}
					player.graphics(-1);
					player.animate(-1);
					player.unlock();
					//player.getAppearance().setNpcId(-1);
				});
			}
		});
	}

}
