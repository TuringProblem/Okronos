package mokhaiotl.combat.attacks;

import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;

import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-30
 */
@FunctionalInterface
public interface Attack {
	void invoke(Player target, NPCCombat mokhaiotl);

	default List<Position> getArenaPositions(NPCCombat mokhaiotl) {
		var mokhaiotlPos = mokhaiotl.getNpc().getPosition();
		var region = mokhaiotlPos.getRegion();
		var arena = new Bounds(
			region.baseX + 20,
			region.baseY + 26,
			region.baseX + 42,
			region.baseY + 48,
			0
		);
		return arena.getAllPositions();
	}
}
