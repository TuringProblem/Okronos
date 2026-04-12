package core.api;

import io.ruin.data.impl.npcs.npc_combat;
import io.ruin.model.entity.npc.NPCCombat;

// Wrapper for shitty core
public class BaseCombat extends NPCCombat {

	public npc_combat.Info info = new npc_combat.Info();

	public BaseCombat() {
		this.update();
	}

	public void update() {
		this.update(this.info);
	}

	@Override
	public void init() {
	}

	@Override
	public void follow() {
	}

	@Override
	public boolean attack() {
		return true;
	}

	@Override
	public void process() {
	}

}
