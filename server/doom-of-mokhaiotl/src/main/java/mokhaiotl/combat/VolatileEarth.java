package mokhaiotl.combat;

import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.entity.shared.listeners.HitListener;
import mokhaiotl.npc.EarthShield;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-14
 */
public class VolatileEarth extends NPCCombat {

	@Override
	public void init() {
		getNpc().deathEndListener = handleDeathEnd();
	}

	@Override
	public void follow() {}

	@Override
	public boolean attack() {
		return false;
	}

	@Override
	public void process() {
	}

	private DeathListener handleDeathEnd() {
		return (entity, killer, hit) -> {
			if (killer.player != null) {
				// if we already tagged a location,
				// then we want to spawn in the EarthShield and path it to the other position
				if (killer.player.taggedVolatileEarth != null) {
					entity.npc.remove();
					var earthShield = new EarthShield().spawn(entity.getPosition().translated(-2, -2));
					var delveLevel = killer.player.get("MOKHAIOTL_DELVE_LEVEL", 1);
					var destination = killer.player.taggedVolatileEarth.translated(-2, -2);
					var travelSpeed = delveLevel < 3 ? StepType.CRAWL : StepType.WALK;
					// Move the NPC
					earthShield.walkTo = destination;
					earthShield.stepAbs(destination.getX(), destination.getY(), travelSpeed);
					// now that we spawned the earthShield and have it pathing, we can remove the tag
					killer.player.taggedVolatileEarth = null;
					return;
				}
				// we're setting the tag to the position of the entity
				killer.player.taggedVolatileEarth = entity.getPosition();
			}
		};
	}
}
