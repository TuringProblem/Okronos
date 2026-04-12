package mokhaiotl.npc;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Position;
import mokhaiotl.combat.DemonicLarvaeType;
import mokhaiotl.combat.MokhaiotlCombat;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-28
 */
public class DemonicLarvae extends NPC {

	private Position destination;
	private final MokhaiotlCombat mokhaiotl;
	private final Player bossesTarget;

	public DemonicLarvae(DemonicLarvaeType type, NPCCombat mokhaiotl, Player target) {
		super(type.getNpcId());
		this.mokhaiotl = (MokhaiotlCombat) mokhaiotl;
		this.bossesTarget = target;
		deathEndListener = (e, k, h) -> {
			var tiles = new ArrayList<Position>();
			for (int x = -1; x < 1; x++) {
				for (int y = -1; y < 1; y++) {
					tiles.add(getCentrePosition().translate(x, y));
				}
			}
			World.sendGraphics(3418, 0, 0, getPosition());
			for (Position tile : tiles) {
				World.sendGraphics(3405, 0, 0, tile);
				if (mokhaiotl.getNpc().getCentrePosition().isAtPosition(tile))
					mokhaiotl.getNpc().hit(new Hit(HitType.DAMAGE_OTHER).fixedDamage(getLarvaeHeal(type)));
				if (bossesTarget.getPosition().isAtPosition(tile))
					bossesTarget.hit(new Hit(HitType.DAMAGE_OTHER).fixedDamage(5));
			}
		};
	}

	private int getLarvaeHeal(DemonicLarvaeType type) {
		return switch (type) {
			case GIANT_MAGE, GIANT_RANGED -> 150;
			default -> 50;
		};
	}

	@Override
	public void process() {
		super.process();
		if (Objects.nonNull(walkTo) && getPosition().isAtPosition(walkTo)) {
			mokhaiotl.getNpc().hit(new Hit(HitType.HEAL).fixedDamage(50));
			remove();
		}
	}

	public void setPathToBoss(Position centrePosition) {
		this.destination = centrePosition;
		stepAbs(destination.getX(), destination.getY(), StepType.WALK);
		walkTo = destination;
	}
}
