package io.ruin.model.skills.agility.shortcut;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.Renders;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.stat.StatType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LogBalance {

	KARAMJA_LOG1(1, 1, Position.of(2906, 3049), Position.of(2910, 3049)),
	CAMELOT_LOG20(20, 1, Position.of(2598, 3477), Position.of(2603, 3477)),
	BRIMHAVEN_LOG30(30, 1, Position.of(2682, 9506), Position.of(2687, 9506)),
	ARDY_LOG33(33, 1, Position.of(2602, 3336), Position.of(2598, 3336)),
	ISAFDAR_1_LOG45(45, 1, Position.of(2202, 3237), Position.of(2196, 3237)),
	ISAFDAR_2_LOG45(45, 1, Position.of(2258, 3250), Position.of(2264, 3250)),
	ISAFDAR_3_LOG45(45, 1, Position.of(2290, 3232), Position.of(2290, 3239)),
	CAMELOT_LOG48(48, 1, Position.of(2722, 3592), Position.of(2722, 3596));

	private int levelReq, xp;
	private Position startPosition, endPosition;

	public void traverse(Player player, GameObject obj) {
		player.startEvent(e -> {
			player.lock(LockType.FULL_DELAY_DAMAGE);
			player.getAppearance().setCustomRenders(Renders.AGILITY_BALANCE);
			Position target = player.getPosition().equals(startPosition) ? endPosition : startPosition;
			int distance = startPosition.distance(endPosition);
			player.stepAbs(target.getX(), target.getY(), StepType.WALK);
			e.delay(distance + 1);
			if (World.isEco())
				player.getStats().addXp(StatType.Agility, xp, true);
			player.getAppearance().removeCustomRenders();
			player.unlock();
		});
	}
}