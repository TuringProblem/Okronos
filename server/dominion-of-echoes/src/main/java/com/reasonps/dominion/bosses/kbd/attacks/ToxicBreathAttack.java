package com.reasonps.dominion.bosses.kbd.attacks;

import com.reasonps.dominion.bosses.Attack;
import com.reasonps.dominion.bosses.IDirectional;
import com.reasonps.dominion.bosses.kbd.EchoKingBlackDragon;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.utility.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class ToxicBreathAttack implements Attack, IDirectional {

	private final Projectile POISON_PROJECTILE =
		new Projectile(394, 43, 31, 51, 120, 5, 15, 250).regionBased();

	private final Projectile ACID_POOL_PROJECTILE =
		new Projectile(1644, 5, 0, 51, 86, 1, 55, 64).regionBased();

	@Override
	public void invoke(Player target, NPCCombat boss) {
		var destination = target.getPosition().copy();
		var enraged = boss.getNpc().getHp() <= (boss.getNpc().getMaxHp() / 2);
		boss.getNpc().animate(81);
		int delay = POISON_PROJECTILE.send(boss.getNpc(), destination);

		var blobs = enraged ? 10 : 4;
		World.startEvent(event -> {
			event.delay(World.getTicks(delay));
			var hit = new Hit(boss.getNpc(), AttackStyle.DRAGONFIRE)
				.randDamage(15)
				.ignoreDefence()
				.ignorePrayer();

			((EchoKingBlackDragon) boss).getVenomPools().add(destination);

			if (target.getPosition().isAtPosition(destination))
				target.hit(hit);

			var positionPool = getPossiblePositions(destination);
			for (int i = 0; i < blobs; i++) {
				var randomPosition = Random.get(positionPool);
				positionPool.remove(randomPosition);
				var ricochetDelay = ACID_POOL_PROJECTILE.send(destination, randomPosition);
				World.startEvent(World.getTicks(ricochetDelay), e ->
					((EchoKingBlackDragon) boss).getVenomPools().add(randomPosition)).setCancelCondition(getCancelCondition(boss));
			}
		}).setCancelCondition(getCancelCondition(boss));

	}

	private List<Position> getPossiblePositions(Position origin) {
		var positions = new ArrayList<Position>();
		for (int x = -2; x < 2; x++)
			for (int y = -2; y < 2; y++)
				positions.add(Position.of(origin.getX() + x, origin.getY() + y, origin.getZ()));
		return positions;
	}

	private Supplier<Boolean> getCancelCondition(NPCCombat boss) {
		return () -> boss.isDead() || boss.getNpc().isRemoved() || boss.getNpc().getPosition().getRegion().players.isEmpty();
	}
}
