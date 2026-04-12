package io.ruin.model.entity.doors;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.item.actions.impl.BirdNest;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.skills.woodcutting.WoodcuttingGuild;
import io.ruin.model.stat.StatType;

public class MagicGuild {

	public static void register() {

		ObjectAction.register(15645, 2590, 3089, 0, "climb-up", (player, obj) -> player.getMovement().teleport(2591, 3092, 1));
		ObjectAction.register(15648, 2590, 3090, 1, "climb-down", (player, obj) -> player.getMovement().teleport(2591, 3088));

		ObjectAction.register(15645, 2590, 3084, 1, "climb-up", (player, obj) -> player.getMovement().teleport(2591, 3087, 2));
		ObjectAction.register(15648, 2590, 3085, 2, "climb-down", (player, obj) -> player.getMovement().teleport(2591, 3083, 1));

	}

}
