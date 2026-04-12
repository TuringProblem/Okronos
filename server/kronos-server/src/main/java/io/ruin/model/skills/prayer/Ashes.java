package io.ruin.model.skills.prayer;

import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerCounter;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.stat.StatType;

public enum Ashes {

	FIENDISH_ASHES(ItemID.FIENDISH_ASHES, 5, PlayerCounter.FIENDISH_ASHES_SCATTERED),
	VILE_ASHES(ItemID.VILE_ASHES, 12.5, PlayerCounter.VILE_ASHES_SCATTERED),
	MALICIOUS_ASHES(ItemID.MALICIOUS_ASHES, 32.5, PlayerCounter.NALICIOUS_ASHES_SCATTERED),
	ABYSSAL_ASHES(ItemID.ABYSSAL_ASHES, 42.5, PlayerCounter.ABYSSAL_ASHES_SCATTERED),
	INFERNAL_ASHES(ItemID.INFERNAL_ASHES, 55, PlayerCounter.INFERNAL_ASHES_SCATTERED);

	public final int id;
	public final double exp;
	public final PlayerCounter scatteredCounter;

	Ashes(int id, double exp, PlayerCounter scatteredCounter) {
		this.id = id;
		this.exp = exp;
		this.scatteredCounter = scatteredCounter;
	}

	private void scatter(Player player, Item ash) {
		player.resetActions(true, false, true);
		player.startEvent(event -> {
			if (player.ashScatterDelay.isDelayed())
				return;
			ash.remove();
			player.animate(2295);
			player.getStats().addXp(StatType.Prayer, exp, true);
			player.privateSound(2444);
			scatteredCounter.increment(player, 1);
			player.karamDelay.delay(2);
			player.sendMessage("You scatter the ashes.");
		});
	}

	public static void register() {
		for (Ashes ash : values()) {
			ItemAction.registerInventory(ash.id, "scatter", ash::scatter);
		}
	}

	public static Ashes get(int ashId) {
		for (Ashes a : values()) {
			if (ashId == a.id)
				return a;
		}
		return null;
	}
}
