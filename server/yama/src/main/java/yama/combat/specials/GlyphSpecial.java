package yama.combat.specials;

import io.ruin.cache.Color;
import io.ruin.model.World;

import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Projectile;
import io.ruin.utility.Random;
import yama.combat.Glyph;
import yama.combat.Yama;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GlyphSpecial {
	Projectile SHADOW_PROJECTILE = new Projectile(3257, 200, 0, 0, 30, 0, 0, 0).regionBased();
	Projectile FLAME_PROJECTILE = new Projectile(3254, 200, 0, 0, 30, 0, 0, 0).regionBased();

	private final NPC yama;
	public GlyphSpecial(NPC yama) {
		this.yama = yama;

	}

	public void sendRandomSpecial(int type) {
		if (type == 1) {
			sendShadowBasedAttack();
		} else if (type == 0) {
			sendFireBasedAttack();
		}
	}

	private void sendShadowBasedAttack() {
		Yama yamaCombat = (Yama) yama.getCombat();
		World.startEvent(e -> {
			e.setCancelCondition(() -> yama.getHp() < 1 || yamaCombat.isInIntermission());
			yama.graphics(3256);
			yama.animate(12145);
			yama.getPosition().getRegion().players.forEach(p -> {
				p.sendMessage(Color.PURPLE.wrap("Yama conjures a shadow-based attack at you."));
			});
			AtomicInteger delay = new AtomicInteger();
			e.delay(4);
			yama.getPosition().getRegion().players.forEach(p -> {
				delay.set(SHADOW_PROJECTILE.send(p, p));
			});
			e.delay(World.getTicks(delay.get() + 1));
			Yama yc = (Yama) yama.getCombat();
			yama.getPosition().getRegion().players.forEach(p -> {
				if(yc.getShadowImmunityTicks() < 1) {
					p.hit(new Hit().randDamage(45, 60));
					p.getPrayer().drain(Random.get(25, 45));
					p.sendMessage(Color.PURPLE.wrap("You are hit by Yama's shadow-based attack!"));
				} else {
					p.sendMessage(Color.PURPLE2.wrap("The Glyph of Shadow absorbs" +
						" the damage from the attack."));
				}
				yamaCombat.setCanAttack(true);
			});
		});
	}

	private void sendFireBasedAttack() {
		Yama yamaCombat = (Yama) yama.getCombat();
		World.startEvent(e -> {
			e.setCancelCondition(() -> yama.getHp() < 1 || yamaCombat.isInIntermission());
			yama.graphics(3253);
			yama.animate(12145);
			yama.getPosition().getRegion().players.forEach(p -> {
				p.sendMessage(Color.ORANGE2.wrap("Yama conjures a fire-based attack at you."));
			});
			AtomicInteger delay = new AtomicInteger();
			e.delay(4);
			yama.getPosition().getRegion().players.forEach(p -> {
				delay.set(FLAME_PROJECTILE.send(p, p));
			});
				e.delay(World.getTicks(delay.get() + 1));
				Yama yc = (Yama) yama.getCombat();
				yama.getPosition().getRegion().players.forEach(p -> {
					if(yc.getFireImmunityTicks() < 1) {
						p.hit(new Hit().randDamage(45, 60));
						p.getCombat().burnEvent(p, 8, 8, 14, 3);
						p.sendMessage(Color.ORANGE2.wrap("You are hit by Yama's fire-based attack!"));
					} else {
						p.sendMessage(Color.ORANGE2.wrap("The Glyph of Fire absorbs" +
							" the damage from the attack."));
					}
					yamaCombat.setCanAttack(true);
				});
		});
	}
}
