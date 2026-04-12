package io.ruin.model.skills.agility.shortcut;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.Renders;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.stat.StatType;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum MonkeyBars {

	YANILLE_DUNGEON_BARS_1(57, 1, Position.of(2598, 9494), Position.of(2598, 9489)),
	YANILLE_DUNGEON_BARS_2(57, 1, Position.of(2599, 9489), Position.of(2599, 9494));

	private int level;
	private double exp;
	private Position from;
	private Position to;

	public void traverse(Player player, GameObject object) {
		if (!player.getStats().check(StatType.Agility, level, "Swing across"))
			return;
		player.startEvent((event) -> {
			player.lock(LockType.FULL_DELAY_DAMAGE);
			event.path(player, from);
			player.face(to.getX(), to.getY());
			player.animate(742);
			event.delay(2);
			player.getAppearance().setCustomRenders(Renders.MONKEY_BARS);
			int stepsNeeded = object.getPosition().distance(to);
			for (int step = 0; step < stepsNeeded; step++) {
				player.stepAbs(to.getX(), to.getY(), StepType.WALK);
				player.privateSound(2451, 2, 0);
				event.delay(1);
			}
			player.animate(743);
			player.getAppearance().removeCustomRenders();
			event.delay(2);
			player.getStats().addXp(StatType.Agility, exp, true);
			player.animate(-1);
			player.unlock();
		});
	}

}
