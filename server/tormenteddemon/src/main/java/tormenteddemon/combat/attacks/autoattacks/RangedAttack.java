package tormenteddemon.combat.attacks.autoattacks;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;

public class RangedAttack extends AutoAttack {
	Projectile RANGE_PROJECTILE = new Projectile(2857, 80, 43, 0, 43, 20, 0, 0);
	@Override
	public void send(NPC npc, Player target) {
		npc.animate(11389);
		World.startEvent(e -> {
			int delay = RANGE_PROJECTILE.send(npc, target);
			int damage = 56;
			e.delay(World.getTicks(delay) + 1);
			if(target.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
				damage = 10;
			target.hit(new Hit(npc).randDamage(damage).ignorePrayer());
		});
	}
}
