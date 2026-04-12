package yama.combat.specials;

import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Projectile;
import yama.combat.Yama;

import java.util.concurrent.atomic.AtomicInteger;

public class ShadowStomp {

	Projectile SHADOW_PROJECTILE = new Projectile(3260, 2, 2, 0, 0, 22, 0, 64).regionBased();
	public void send(NPC npc) {
		Yama yama = (Yama) npc.getCombat();
		World.startEvent(e -> {
			npc.forceText("Umbra, proriumpse");
			npc.animate(12148);
			npc.graphics(3259);
			e.delay(2);
			AtomicInteger delay = new AtomicInteger();
			npc.getPosition().getRegion().players.forEach(p -> delay.set(SHADOW_PROJECTILE.send(npc, p)));
			e.delay(World.getTicks(delay.get()) + 1);
			npc.getPosition().getRegion().players.forEach(p -> p.graphics(3261));
			if(yama.getShadowImmunityTicks() < 1) {
				npc.getPosition().getRegion().players.forEach(p -> {
					p.freeze(2, npc);
					p.getPrayer().drain(19);
					p.hit(new Hit(npc).randDamage(35, 60));
				});
			} else {
				npc.getPosition().getRegion().players.forEach(p -> p.sendMessage(Color.PURPLE2.wrap("The Glyph of Shadow absorbs" +
					" the damage from the attack.")));
			}
		});
	}
}
