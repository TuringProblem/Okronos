package io.ruin.model.entity.doors;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.item.actions.impl.BirdNest;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.skills.woodcutting.WoodcuttingGuild;
import io.ruin.model.stat.StatType;

public class PortPhasmatys {

	public static void register() {

		ObjectAction.register(16105, 3659, 3508, 0, 1, (player, obj) -> {
			player.startEvent(event -> {
				player.lock();
				if (player.getPosition().equals(3660, 3509) || player.getPosition().equals(3659, 3509)) {
					player.getMovement().teleport(3660, 3506);
				}
				if (player.getPosition().equals(3660, 3507) || player.getPosition().equals(3659, 3507)) {
					player.getMovement().teleport(3660, 3509);
				}
				player.unlock();
			});
		});


	}
}
