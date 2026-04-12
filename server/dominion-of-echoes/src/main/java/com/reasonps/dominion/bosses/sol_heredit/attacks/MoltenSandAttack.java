package com.reasonps.dominion.bosses.sol_heredit.attacks;

import com.reasonps.dominion.bosses.Attack;
import com.reasonps.dominion.bosses.sol_heredit.EchoSolHeredit;
import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-29
 */
public class MoltenSandAttack implements Attack {

	private final Bounds bossBounds;

	public MoltenSandAttack(Bounds bossBounds) {
		this.bossBounds = bossBounds;
	}

	@Override
	public void invoke(Player target, NPCCombat boss) {
		if (target == null)
			return;
		Position startPosition = target.getPosition().copy();
		List<Position> spawnPositions = new ArrayList<>();
		List<Position> impactPositions = new ArrayList<>();
		for (int x = startPosition.getX() - 9; x < startPosition.getX() + 9; x++) {
			for (int y = startPosition.getY() - 9; y < startPosition.getY() + 9; y++) {
				Position pos = new Position(x, y, 0);
				if (pos.inBounds(bossBounds))
					spawnPositions.add(pos);
			}
		}
		for (int i = 0; i < 5; i++) {
			Position pos = Random.get(spawnPositions);
			spawnPositions.remove(pos);
			((EchoSolHeredit) boss).getCurrentMoltenPositions().add(pos);
			impactPositions.add(pos);
		}
		impactPositions.add(startPosition);
		((EchoSolHeredit) boss).getCurrentMoltenPositions().add(startPosition);
		World.startEvent(e -> {
			e.setCancelCondition(boss::targetIsNotInBossRegion);
			for (var pos : impactPositions) {
				if (Objects.isNull(pos)) continue;
				World.sendGraphics(3156, 0, 0, pos);
			}
			e.delay(3);
			for (var pos : impactPositions) {
				if (pos.distance(target.getPosition()) < 1 && boss.getNpc().getHp() > 0) {
					target.hit(new Hit().randDamage(35, 55));
					((EchoSolHeredit) boss).damagedPlayer = true;
				}
			}
			e.delay(2);
			((EchoSolHeredit) boss).getCurrentMoltenPositions().forEach(pos -> {
				World.sendGraphics(-1, 0, 0, pos);
				if (!((EchoSolHeredit) boss).getMoltenPositions().contains(pos))
					((EchoSolHeredit) boss).getMoltenPositions().add(pos);
			});
			((EchoSolHeredit) boss).getCurrentMoltenPositions().clear();
		});
	}
}
