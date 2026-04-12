package com.reasonps.dominion.bosses.sol_heredit.attacks;

import com.reasonps.dominion.bosses.Attack;
import com.reasonps.dominion.bosses.sol_heredit.EchoSolHeredit;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;

import java.util.ArrayList;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-29
 */
public class ShieldAttack implements Attack {

	private final int safeTileCount;
	private final Bounds bossBounds;

	public ShieldAttack(int safeTileCount, Bounds bossBounds) {
		this.safeTileCount = safeTileCount;
		this.bossBounds = bossBounds;
	}

	@Override
	public void invoke(Player target, NPCCombat boss) {
		var startTile = boss.getNpc().getCentrePosition().copy();
		var safeTiles = new ArrayList<Position>();
		var unsafeTiles = new ArrayList<Position>();

		for (var x = startTile.getX() - safeTileCount; x <= startTile.getX() + safeTileCount; x++) {
			for (var y = startTile.getY() - safeTileCount; y <= startTile.getY() + safeTileCount; y++) {
				var pos = Position.of(x, y, 0);
				if (startTile.distance(pos) == safeTileCount)
					safeTiles.add(pos);
			}
		}
		bossBounds.forEachPos(tile -> {
			boolean safe = safeTiles.stream().anyMatch(pos -> tile.distance(pos) < 1);
			if (!safe) {
				unsafeTiles.add(tile);
			}
		});
		boss.getNpc().animate(10885);
		World.startEvent(event -> {
			event.setCancelCondition(boss::targetIsNotInBossRegion);
			event.delay(3);
			for (var pos : unsafeTiles) {
				World.sendGraphics(2670, 0, pos.distance(startTile), pos);
				if (target.getPosition().distance(pos) < 1 && boss.getNpc().getHp() > 0) {
					target.hit(new Hit().randDamage(35, 45));
					((EchoSolHeredit) boss).damagedPlayer = true;
				}
			}
		});
	}
}
