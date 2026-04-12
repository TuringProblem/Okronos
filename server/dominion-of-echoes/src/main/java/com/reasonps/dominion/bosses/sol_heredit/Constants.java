package com.reasonps.dominion.bosses.sol_heredit;

import io.ruin.model.entity.player.Player;
import io.ruin.model.var.VarPlayerRepository;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-29
 */
public class Constants {

	protected static final int COMBO_ATTACK_CHANCE = 9; // 1 in 10 chance (0-9)
	protected static final int COMBO_COOLDOWN_TICKS = 30;

	protected static final double SHIELD_ATTACK_PROBABILITY = 0.5;

	/**
	 * Determines if the player is using a spear-type weapon based on their weapon type in the game.
	 *
	 * @param player the player whose weapon type is being checked
	 * @return true if the player's weapon type corresponds to a spear, false otherwise
	 */
	protected static boolean usingSpear(Player player) {
		return VarPlayerRepository.WEAPON_TYPE.get(player) == 24 ||
			VarPlayerRepository.WEAPON_TYPE.get(player) == 15 ||
			VarPlayerRepository.WEAPON_TYPE.get(player) == 12;
	}
}
