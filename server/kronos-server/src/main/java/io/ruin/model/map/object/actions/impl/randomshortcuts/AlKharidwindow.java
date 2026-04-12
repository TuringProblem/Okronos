package io.ruin.model.map.object.actions.impl.randomshortcuts;

import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;

public class AlKharidwindow {

	public static void register() {

		ObjectAction.register(33348, 1, (player, obj) -> {
			if (!player.getStats().check(StatType.Agility, 75, "attempt this"))
				return;
			player.startEvent(event -> {
				player.lock();
				player.animate(2586);
				event.delay(1);

				player.getMovement().teleport(3295, 3157, 0);
				player.unlock();

			});
		});

	}
}

