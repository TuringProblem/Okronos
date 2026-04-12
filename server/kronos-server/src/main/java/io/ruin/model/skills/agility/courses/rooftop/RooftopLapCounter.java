package io.ruin.model.skills.agility.courses.rooftop;

import io.ruin.model.entity.player.Player;

public class RooftopLapCounter {

	public static void notify(Player player, String name, int count) {
		player.sendMessage("Your " + name + " lap count is now " + count);
	}
}
