package tormenteddemon.combat.attacks.autoattacks;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;

public abstract class AutoAttack {
	public abstract void send(NPC npc, Player target);
}
