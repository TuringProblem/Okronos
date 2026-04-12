package mokhaiotl.combat.attacks.impl;

import core.api.Random;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import mokhaiotl.combat.DemonicLarvaeType;
import mokhaiotl.combat.MokhaiotlCombat;
import mokhaiotl.combat.attacks.Attack;
import mokhaiotl.npc.DemonicLarvae;

import java.util.List;
import java.util.Objects;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-02
 */
public class DemonicLarvaeAttack implements Attack {

	@Override
	public void invoke(Player target, NPCCombat mokhaiotl) {
		var location = getValidLarvaePosition(mokhaiotl);
		if (location == null)
			return;
		var delve = target.get("MOKHAIOTL_DELVE_LEVEL", 1);
		switch (delve) {
			case 1, 2, 3 -> spawnLarvae(DemonicLarvaeType.NEUTRAL, location, target, mokhaiotl);
			case 4, 5, 6 -> spawnLarvae(
				Random.get(List.of(
					DemonicLarvaeType.MELEE,
					DemonicLarvaeType.MAGIC,
					DemonicLarvaeType.RANGED
				)),
				location,
				target,
				mokhaiotl
			);
			case 7 -> {
				var type = Random.get(List.of(
					DemonicLarvaeType.MELEE,
					DemonicLarvaeType.MAGIC,
					DemonicLarvaeType.RANGED
				));
				World.startEvent(event -> {
					spawnLarvae(type, location, target, mokhaiotl);
					event.delay(1);
					spawnLarvae(type, location, target, mokhaiotl);
				});
			}
			default -> spawnLarvae(
				Random.get(List.of(
					DemonicLarvaeType.GIANT_MAGE,
					DemonicLarvaeType.GIANT_RANGED
				)),
				location,
				target,
				mokhaiotl
			);
		}
	}

	private void spawnLarvae(DemonicLarvaeType type, Position location, Player target, NPCCombat mokhaiotl) {
		World.sendGraphics(3417, 0, 0, location);
		var larvae = spawnLarvae(type, location, mokhaiotl, target);
		if (Objects.nonNull(target.currentDynamicMap))
			target.currentDynamicMap.addNpc(larvae);
		larvae.setPathToBoss(mokhaiotl.getNpc().getCentrePosition());
	}

	private DemonicLarvae spawnLarvae(DemonicLarvaeType type,
									  Position location,
									  NPCCombat mokhaiotl,
									  Player target
	) {
		var larvae = new DemonicLarvae(type, mokhaiotl, target);
			larvae.spawn(location);
		if (Objects.nonNull(type.getOverheadPrayer()))
			larvae.setHeadIcon(type.getOverheadPrayer());
		return larvae;
	}

	private Position getValidLarvaePosition(NPCCombat mokhaiotl) {
		var options = getArenaPositions(mokhaiotl);
		return Random.get(options);
	}

}
