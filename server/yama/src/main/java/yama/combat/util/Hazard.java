package yama.combat.util;

import io.ruin.model.entity.npc.NPC;

public abstract class Hazard {

	public abstract void sendHazard(NPC npc, Phase phase);
}
