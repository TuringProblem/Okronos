package yama.combat.attacks;

import core.api.Random;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.skills.prayer.Prayer;

public class MagicAttack extends AutoAttack{
	@Override
	public void attack(NPC npc) {
		npc.animate(12144);
		npc.graphics(3246);
		npc.face(npc.getCombat().getTarget());
		npc.getPosition().getRegion().players.forEach(p -> {
			World.startEvent(e -> {
				p.graphics(-1);
				p.graphics(3247);
				e.delay(3);
				if(!p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
					p.hit(new Hit(npc).randDamage(32, 65).ignorePrayer());
				} else {
					p.hit(new Hit(npc).randDamage(3, 11).ignorePrayer());
				}
				if(Random.get(10) == 0)
					p.poison(20);
			});
		});

	}
}
