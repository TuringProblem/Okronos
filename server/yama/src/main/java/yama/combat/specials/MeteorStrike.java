package yama.combat.specials;

import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.var.VarPlayerRepository;
import yama.combat.Yama;

public class MeteorStrike {
	public void send(NPC npc, Player target) {
		World.startEvent(e -> {
			npc.forceText("Ven, estella infernus");
			npc.getPosition().getRegion().players.forEach(p ->  VarPlayerRepository.RUNNING.set(p, 0));
			Position targetPos = target.getPosition().copy();
			World.sendGraphics(3272, 0, 0, targetPos);
			e.delay(4);
			Yama yama = (Yama) npc.getCombat();
			npc.getPosition().getRegion().players.forEach(p -> {
				int distance = p.getPosition().getDistance(targetPos);
				if(yama.getFireImmunityTicks() < 1) {
					if (distance < 2) {
						p.hit(new Hit().fixedDamage(p.getHp()));
					} else if (distance < 3) {
						p.hit(new Hit().fixedDamage(80));
					} else if (distance < 4) {
						p.hit(new Hit().fixedDamage(40));
					}
				} else {
					p.sendMessage(Color.ORANGE2.wrap("The Glyph of Fire absorbs" +
						" the damage from the attack."));
				}
			});
		});
	}
}
