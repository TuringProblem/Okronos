package com.reasonps.dominion.bosses.cerberus.attacks;

import com.reasonps.dominion.bosses.Attack;
import com.reasonps.dominion.bosses.cerberus.EchoCerberus;
import com.reasonps.dominion.bosses.cerberus.EchoSoul;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class SoulSummonAttack implements Attack {

	private final List<EchoSoul> soulList = new ArrayList<>(List.of(EchoSoul.RANGED, EchoSoul.MAGIC, EchoSoul.MELEE));
	private final Position[] soulSpawns = new Position[9];

	@Override
	public void invoke(Player target, NPCCombat boss) {
		var cerberus = ((EchoCerberus) boss);
		var spawnPoint = cerberus.getNpc().spawnPosition;

		loadSoulSpawns(spawnPoint);

		cerberus.summonSoulsCooldown.delay(50);
		cerberus.getNpc().forceText("Aaarrrooooooo");
		cerberus.ghostsSpawned = true;
		cerberus.getNpc().animate(4494);
		var soulIndex = 0;
		for (int i = 0; i < 3; i++) {
			Collections.shuffle(soulList);
			for (int j = 0; j < 3; j++) {
				var echoSoul = soulList.get(j);
				int finalSoulIndex = soulIndex;
				var position = soulSpawns[finalSoulIndex];
				var soul = new NPC(echoSoul.getNpcId()).spawn(position);

				soul.addEvent(event -> {
					soul.step(0, -12, StepType.WALK);
					event.waitForMovement(soul);
					event.delay(1 + (finalSoulIndex * 2));
					if (target == null || cerberus.isDead()) {
						soul.remove();
						return;
					}
					echoSoul.attack(soul, target.player, cerberus.getNpc());
					event.delay(3);
					soul.faceNone(false);
					soul.step(0, 10, StepType.WALK);
					event.waitForMovement(soul);
					soul.remove();
				});
				soulIndex++;
			}
		}
	}

	/**
	 * Initializes the positions for soul spawns relative to the given spawn point.
	 * The positions are organized into a 3x3 grid structure, with rows and columns
	 * offset from the provided base point.
	 *
	 * @param spawnPoint the base position from which the soul spawn positions are calculated
	 */
	private void loadSoulSpawns(Position spawnPoint) {
		// Row 1
		soulSpawns[0] = Position.of(spawnPoint.getX() + 1, spawnPoint.getY() + 16, spawnPoint.getZ());
		soulSpawns[1] = Position.of(spawnPoint.getX() + 2, spawnPoint.getY() + 16, spawnPoint.getZ());
		soulSpawns[2] = Position.of(spawnPoint.getX() + 3, spawnPoint.getY() + 16, spawnPoint.getZ());
		// Row 2
		soulSpawns[3] = Position.of(spawnPoint.getX() + 1, spawnPoint.getY() + 17, spawnPoint.getZ());
		soulSpawns[4] = Position.of(spawnPoint.getX() + 2, spawnPoint.getY() + 17, spawnPoint.getZ());
		soulSpawns[5] = Position.of(spawnPoint.getX() + 3, spawnPoint.getY() + 17, spawnPoint.getZ());
		// Row 3
		soulSpawns[6] = Position.of(spawnPoint.getX() + 1, spawnPoint.getY() + 18, spawnPoint.getZ());
		soulSpawns[7] = Position.of(spawnPoint.getX() + 2, spawnPoint.getY() + 18, spawnPoint.getZ());
		soulSpawns[8] = Position.of(spawnPoint.getX() + 3, spawnPoint.getY() + 18, spawnPoint.getZ());
	}
}
