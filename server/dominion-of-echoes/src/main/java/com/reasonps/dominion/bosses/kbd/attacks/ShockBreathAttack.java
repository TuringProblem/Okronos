package com.reasonps.dominion.bosses.kbd.attacks;

import com.reasonps.dominion.bosses.Attack;
import com.reasonps.dominion.bosses.IDirectional;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.stat.StatType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class ShockBreathAttack implements Attack, IDirectional {

	private final Projectile SHOCK_PROJECTILE =
		new Projectile(3164, 43, 31, 51, 120, 5, 15, 250).regionBased();

	private final StatType[] SHOCK_STATS = {
		StatType.Attack, StatType.Strength, StatType.Defence,
		StatType.Ranged, StatType.Magic
	};

	@Override
	public void invoke(Player target, NPCCombat boss) {
		var destination = target.getPosition().copy();
		var enraged = boss.getNpc().getHp() <= (boss.getNpc().getMaxHp() / 2);
		boss.getNpc().animate(81);
		int delay = SHOCK_PROJECTILE.send(boss.getNpc(), destination);

		var size = enraged ? 5 : 3;
		var offset = enraged ? 2 : 1;
		// explodes on impact in a 3x3 X-shaped pattern, dealing 15-damage.
		var northSouthPath = getVerticalTiles(Direction.SOUTH, destination.translated(0, offset), size);
		var westEastPath = getHorizontalTiles(Direction.EAST, destination.translated(-offset, 0), size);

		// Collect the tiles to light up
		var hotSpots = new ArrayList<Position>();
			hotSpots.addAll(northSouthPath);
			hotSpots.addAll(westEastPath);

		var hit = new Hit(boss.getNpc(), AttackStyle.DRAGONFIRE)
			.randDamage(15)
			.ignoreDefence()
			.ignorePrayer();

		// prepare the delay
		boss.getNpc().addEvent(event -> {
			event.setCancelCondition(() -> boss.isDead() || boss.getNpc().isRemoved() || target == null);
			event.delay(World.getTicks(delay) + 1);
			var pulses = enraged ? 12 : 3;
			for (int i = 0; i < pulses; i++) {
				event.delay(1);
				pulseElectricShock(hotSpots, target, hit);
			}
		});
	}

	private void pulseElectricShock(List<Position> hotSpots, Entity target, Hit hit) {
		hotSpots.forEach(pos -> {
			World.sendGraphics(3165, 0, 0, pos);
			if (target.getPosition().isAtPosition(pos)) {
				target.hit(hit);
				Arrays.stream(SHOCK_STATS).forEach(s ->
					target.player.getStats().get(s).drain(7));
			}
		});
	}
}
