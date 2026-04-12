package io.ruin.model.activities.raids.toa.bosses.baba;

import io.ruin.api.utils.Random;
import io.ruin.model.entity.npc.NPC;

import java.util.List;

public class BaboonSpawn extends NPC {
	public BaboonSpawn(int id) {
		super(id);
	}

	public void process(NPC boss, List<Sarcophagus> activeSarcophagus) {
		if (boss.getHp() < 1) {
			this.remove();
			return;
		}
		if (this.getCombat().getTarget() == null) {
			if (activeSarcophagus.isEmpty()) {
				this.getCombat().setTarget(Random.get(this.localPlayers()));
				return;
			}
			this.getCombat().setTarget(Random.get(activeSarcophagus).npc);
		}

	}
}
