package yama.combat.attacks;

import core.api.Random;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.skills.prayer.Prayer;
import yama.combat.Yama;
import yama.combat.util.Phase;

public class MeleeAttack extends AutoAttack{
	@Override
	public void attack(NPC npc) {
		Yama yama = (Yama) npc.getCombat();
		Player target = (Player) npc.getCombat().getTarget();
		npc.face(target);
		npc.animate(12146);
		int maxDamage = 69;
		if(target.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE))
			maxDamage = yama.phase == Phase.THREE ? 31 : 16;
		target.hit(new Hit(npc).randDamage(maxDamage).ignorePrayer());
		if(Random.get(10) == 0)
			target.poison(20);
	}
}
