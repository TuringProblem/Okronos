package com.reasonps.dominion.bosses.sol_heredit.attacks;

import com.reasonps.dominion.bosses.Attack;
import com.reasonps.dominion.bosses.sol_heredit.EchoSolHeredit;
import com.reasonps.dominion.bosses.sol_heredit.HealingTotem;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.Region;

import java.security.cert.PolicyQualifierInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-11
 */
public class HealingTotemAttack implements Attack {

	private final List<Position> positions = new ArrayList<>();

	private int phase;

	public HealingTotemAttack setPhase(int phase) {
		this.phase = phase;
		return this;
	}

	@Override
	public void invoke(Player target, NPCCombat boss) {
		getPositions(boss.getNpc().getPosition().getRegion())
			.forEach(pos -> {
				removePositionFromMoltenSand(boss, pos);
				getHealingTotem(boss).spawn(pos);
			});
	}

	private void removePositionFromMoltenSand(NPCCombat boss, Position pos) {
		((EchoSolHeredit) boss).getMoltenPositions().remove(pos);
		((EchoSolHeredit) boss).getCurrentMoltenPositions().remove(pos);
		World.sendGraphics(-1, 0, 0, pos);
	}

	private List<Position> getPositions(Region region) {
		return switch (phase) {
			case 1 -> List.of(
				Position.of(region.baseX + 27, region.baseY + 40),
				Position.of(region.baseX + 38, region.baseY + 40),
				Position.of(region.baseX + 27, region.baseY + 28),
				Position.of(region.baseX + 38, region.baseY + 28)
			);
			case 2 -> List.of(
				Position.of(region.baseX + 27, region.baseY + 35),
				Position.of(region.baseX + 27, region.baseY + 34),
				Position.of(region.baseX + 38, region.baseY + 35),
				Position.of(region.baseX + 38, region.baseY + 34)
			);
			case 3 -> List.of(
				Position.of(region.baseX + 31, region.baseY + 40),
				Position.of(region.baseX + 34, region.baseY + 40),
				Position.of(region.baseX + 31, region.baseY + 29),
				Position.of(region.baseX + 34, region.baseY + 29),

				Position.of(region.baseX + 27, region.baseY + 36),
				Position.of(region.baseX + 27, region.baseY + 33),
				Position.of(region.baseX + 38, region.baseY + 36),
				Position.of(region.baseX + 38, region.baseY + 33)
			);
			case 4 -> List.of(
				Position.of(region.baseX + 32, region.baseY + 39),
				Position.of(region.baseX + 33, region.baseY + 39),
				Position.of(region.baseX + 32, region.baseY + 30),
				Position.of(region.baseX + 33, region.baseY + 30),

				Position.of(region.baseX + 28, region.baseY + 34),
				Position.of(region.baseX + 28, region.baseY + 35),
				Position.of(region.baseX + 37, region.baseY + 34),
				Position.of(region.baseX + 37, region.baseY + 35),

				Position.of(region.baseX + 29, region.baseY + 38),
				Position.of(region.baseX + 36, region.baseY + 38),
				Position.of(region.baseX + 36, region.baseY + 31),
				Position.of(region.baseX + 29, region.baseY + 31)
			);
			case 5 -> List.of(
				Position.of(region.baseX + 27, region.baseY + 41),
				Position.of(region.baseX + 27, region.baseY + 40),
				Position.of(region.baseX + 26, region.baseY + 40),

				Position.of(region.baseX + 26, region.baseY + 29),
				Position.of(region.baseX + 27, region.baseY + 29),
				Position.of(region.baseX + 27, region.baseY + 28),

				Position.of(region.baseX + 38, region.baseY + 29),
				Position.of(region.baseX + 39, region.baseY + 29),
				Position.of(region.baseX + 38, region.baseY + 28),

				Position.of(region.baseX + 38, region.baseY + 41),
				Position.of(region.baseX + 38, region.baseY + 40),
				Position.of(region.baseX + 39, region.baseY + 40)
			);
			default -> throw new IllegalStateException("Unexpected value: " + phase);
		};
	}

	private NPC getHealingTotem(NPCCombat boss) {
		return new HealingTotem(boss);
	}
}
