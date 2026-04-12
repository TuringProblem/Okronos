package tormenteddemon.combat.attacks.autoattacks;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;

public class MagicAttack extends AutoAttack {
	Projectile MAGIC_PROJECTILE = new Projectile(2853, 70, 43, 0, 43, 0, 0, 0);

	@Override
	public void send(NPC npc, Player target) {
		npc.animate(11388);
		World.startEvent(e -> {
			int delay = MAGIC_PROJECTILE.send(npc, target);
			int damage = 56;
			e.delay(World.getTicks(delay) + 1);
			if(target.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
				damage = 10;
			target.hit(new Hit(npc).randDamage(damage).ignorePrayer());
			target.graphics(2854);
		});
	}
}
