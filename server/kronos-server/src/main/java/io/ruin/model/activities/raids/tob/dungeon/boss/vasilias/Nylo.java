package io.ruin.model.activities.raids.tob.dungeon.boss.vasilias;

import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;

import java.util.List;

public abstract class Nylo {

	public abstract NPCCombat getCombat();

	public abstract NPC getNPC();

	public abstract void process();

	public abstract List<NPC> getTargets();

	public abstract boolean beenToMiddle();

	public abstract void setBeenToMiddle(boolean beenToMiddle);

}
