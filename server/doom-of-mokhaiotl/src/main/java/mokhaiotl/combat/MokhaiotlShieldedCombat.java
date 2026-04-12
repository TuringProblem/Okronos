package mokhaiotl.combat;

import io.ruin.model.entity.npc.NPCCombat;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-29
 */
@Slf4j
public class MokhaiotlShieldedCombat extends NPCCombat {


	@Override
	public void init() {
		log.info("Shielded Mokhaiotl Initiate");
	}

	@Override
	public void follow() {}

	@Override
	public boolean attack() {
		log.info("Shielded Mokhaiotl Attack");
		return false;
	}

	@Override
	public void process() {
		log.info("Shielded Mokhaiotl Process");
	}
}
