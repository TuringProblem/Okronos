package io.ruin.model.activities.blastfurnace;

import io.ruin.api.utils.Random;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.activities.blastfurnace.BlastFurnace;

import java.util.concurrent.TimeUnit;

public class ConveyorBelt {

	public static boolean placeOresOnBelt(Player player, int objectId) {
		if (objectId != BlastFurnace.CONVEYOR_BELTS[0])
			return false;

		if (Random.get(100) == 1) {
			DriveBelt.breakDriveBelt(player);
			player.sendMessage("You cannot access the conveyor belt when the drive belt is broken!");
			return false;
		}

		if (player.isDriveBeltBroken()) {
			player.sendMessage("You cannot access the conveyor belt when the drive belt is broken!");
			return false;
		}

		return false;
	}
}
