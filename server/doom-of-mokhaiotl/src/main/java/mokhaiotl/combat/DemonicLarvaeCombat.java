package mokhaiotl.combat;

import io.ruin.model.entity.npc.NPCCombat;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-02
 */
public class DemonicLarvaeCombat extends NPCCombat {

	@Override
	public void init() {

	}

	@Override
	public void follow() {}

	@Override
	public boolean attack() {
		return false;
	}

	@Override
	public void process() {}

	@Override
	public boolean allowRespawn() {
		return false;
	}
}
