package io.ruin.model.activities.nightmarezone;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerCounter;
import io.ruin.model.map.object.actions.ObjectAction;

public class NightmareZoneObjects {

	/**
	 * Resources tab parent child ID
	 */
	public static int RESOURCES = 4;
	/**
	 * Upgrades tab parent child ID
	 */
	public static int UPGRADES = 5;
	/**
	 * Benefits tab parent child ID
	 */
	public static int BENEFITS = 6;

	/**
	 * increments (increases) the amount of nmz points specified
	 *
	 * @param player
	 * @param amount
	 */
	public static void incrementNMZPoints(Player player, int amount) {
		PlayerCounter.NMZ_REWARD_POINTS.increment(player, amount);
	}

	/**
	 * decrements (decreases) the amount of nmz points specified
	 *
	 * @param player
	 * @param amount
	 */
	public static void decrementNMZPoints(Player player, int amount) {
		PlayerCounter.NMZ_REWARD_POINTS.decrement(player, amount);
	}

	public static void register() {
		/**
		 * NMZ Rewards chest
		 *
		 */
		ObjectAction.register(26273, "search", (player, obj) -> {
			NightmareZoneRewards.open(player);
		});
	}
}
