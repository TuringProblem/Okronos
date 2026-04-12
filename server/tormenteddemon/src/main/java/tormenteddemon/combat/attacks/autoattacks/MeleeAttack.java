package tormenteddemon.combat.attacks.autoattacks;

import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.skills.prayer.Prayer;

public class MeleeAttack extends AutoAttack {
	@Override
	public void send(NPC npc, Player target) {
		npc.face(target);
		npc.animate(11392);
		npc.graphics(2851);
		int maxDamage = 55;
		if(target.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE))
			maxDamage = 10;
		target.hit(new Hit(npc).randDamage(maxDamage).ignorePrayer());
	}
}
