package io.ruin.model.activities.nightmarezone;

import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.MapListener;

public class NightmareZone {

	private static Bounds PREP_AREA = new Bounds(3118, 3481, 3127, 3487, 0);

	public static void register() {
		MapListener.registerBounds(PREP_AREA).onEnter(player -> {
				player.openInterface(ToplevelComponent.WILDERNESS_OVERLAY, 207);
			})
			.onExit((player, logout) -> {
				player.closeInterface(ToplevelComponent.WILDERNESS_OVERLAY);
			});
	}

}
