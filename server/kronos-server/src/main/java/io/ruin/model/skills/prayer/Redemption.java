package io.ruin.model.skills.prayer;

import io.ruin.model.activities.duelarena.DuelRule;
import io.ruin.model.entity.player.Player;
import io.ruin.model.stat.StatType;

public class Redemption {

	public static void check(Player player) {
		if (DuelRule.NO_PRAYER.isToggled(player)) //pointless, won't be able to turn on if no prayer is on lol
			return;
		// if the prayer is not on, stop
		if (!player.getPrayer().isActive(Prayer.REDEMPTION))
			return;
		// if the player was over damaged and is dead
		if (player.getCombat().isDead())
			return;
		// if the player is at or below 10% health, work
		if (player.getHp() <= player.getMaxHp() * 0.1) {
			// drain all the prayer
			player.getPrayer().drain(99);
			player.getPrayer().deactivateAll();
			player.graphics(436, 0, 0);
			var prayerLevel = player.getStats().get(StatType.Prayer).fixedLevel;

			// Restore 25% of the skill Level
			player.incrementHp((int) (prayerLevel * 0.25));
		}
	}
}
