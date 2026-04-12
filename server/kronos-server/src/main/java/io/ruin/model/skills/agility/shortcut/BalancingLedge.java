package io.ruin.model.skills.agility.shortcut;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.Renders;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.stat.StatType;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum BalancingLedge {

	YANILLE_DUNGEON_ENTRANCE_1(40, 1, Position.of(2580, 9512)),
	YANILLE_DUNGEON_ENTRANCE_2(40, 1, Position.of(2580, 9520)),

	GOBLIN_VILLAGE_WALL(40, 1, Position.of(2928, 3520)),
	GOBLIN_VILLAGE_WALL_1(40, 1, Position.of(2926, 3522)),
	;

	private int level;
	private double exp;
	private Position to;

	public void traverse(Player player, GameObject object) {
		if (!player.getStats().check(StatType.Agility, level, "walk-across"))
			return;
		player.startEvent((event) -> {
			player.lock(LockType.FULL_DELAY_DAMAGE);
			Direction dir = Direction.getDirection(object.getPosition(), to);
			boolean reverse = dir.equals(Direction.NORTH) || dir.equals(Direction.WEST);
			player.animate(reverse ? 758 : 753);
			player.stepAbs(object.getPosition().getX(), object.getPosition().getY(), StepType.WALK);
			player.getAppearance().setCustomRenders(reverse ? Renders.AGILITY_WALL : Renders.AGILITY_JUMP);
			event.delay(2);
			int stepsNeeded = object.getPosition().distance(to);
			for (int step = 0; step < stepsNeeded; step++) {
				player.stepAbs(to.getX(), to.getY(), StepType.WALK);
				player.privateSound(2451, 2, 0);
				event.delay(1);
			}
			player.animate(758);
			player.getAppearance().removeCustomRenders();
			player.getStats().addXp(StatType.Agility, exp, true);
			player.unlock();
		});
	}

	public void goblinwall(Player player, GameObject object) {
		if (!player.getStats().check(StatType.Agility, level, "walk-across"))
			return;
		player.startEvent((event) -> {
			player.lock(LockType.FULL_DELAY_DAMAGE);
			Direction dir = Direction.getDirection(object.getPosition(), to);
			boolean reverse = dir.equals(Direction.NORTH) || dir.equals(Direction.WEST);
			player.animate(758);
			player.stepAbs(object.getPosition().getX(), object.getPosition().getY(), StepType.WALK);
			player.getAppearance().setCustomRenders(reverse ? Renders.AGILITY_WALL : Renders.AGILITY_JUMP);
			event.delay(2);
			int stepsNeeded = object.getPosition().distance(to);
			for (int step = 0; step < stepsNeeded; step++) {
				player.stepAbs(to.getX(), to.getY(), StepType.WALK);
				player.privateSound(2451, 2, 0);
				event.delay(1);
			}
			player.animate(758);
			player.getAppearance().removeCustomRenders();
			player.getStats().addXp(StatType.Agility, exp, true);
			player.unlock();
		});
	}

}
