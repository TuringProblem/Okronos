package io.ruin.model.activities.bosses;

import io.ruin.Server;
import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;

import java.util.List;

public class RevenantMaledictus extends NPCCombat {

	private static final Projectile AOE_PROJECTILE = new Projectile(159, 43, 31, 30, 150, 0, 16, 192);
	private static final Projectile ICE_PROJECTILE = new Projectile(2006, 15, 0, 1, 25, 8, 0, 255);
	private static final Projectile BLOOD_PROJECTILE = new Projectile(2002, 15, 0, 1, 25, 8, 0, 255);
	private static final Projectile RANGE_PROJECTILE = new Projectile(2010, 0, 5, 1, 46, 8, 0, 255);

	private static final int AOE_SMALL_EXPLOSION_GFX = 2014;
	private static final int AOE_BIG_EXPLOSION_GFX = 160;

	@Override
	public void init() {
		npc.setIgnoreMulti(true);
	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		if (Random.get(0, 1) == 0)
			standardAttack();
		else if (Random.get(0, 1) == 0) {
			if (Random.get(0, 1) == 0) iceAttack();
			else bloodAttack();
		} else
			AOEAttack(Random.get(npc.localPlayers()).getPosition());

		return true;
	}

	@Override
	public void process() {

	}

	private void standardAttack() {
		npc.startEvent(e -> {
			npc.animate(9282);
			e.delay(2);
			npc.localPlayers().forEach(p -> {
				int delay = RANGE_PROJECTILE.send(npc, p);
				p.hit(new Hit(npc, AttackStyle.RANGED).randDamage(info.max_damage).clientDelay(delay));
			});
		});
	}

	private void AOEAttack(Position impactPosition) {
		Position impact = new Position(impactPosition.getX(), impactPosition.getY(), impactPosition.getZ());
		npc.startEvent(event -> {
			npc.animate(9278);
			int delay = AOE_PROJECTILE.send(npc, impact);
			Position southwestCorner = new Position(impact.getX() - 3, impact.getY() - 3, 0);
			Position northwestCorner = new Position(impact.getX() + 2, impact.getY() + 2, 0);
			Bounds areaOfImpact = new Bounds(southwestCorner, northwestCorner, 0);
			event.delay(getTicks(delay));
			World.sendGraphics(AOE_BIG_EXPLOSION_GFX, 0, 0, impact);
			for (int x = 0; x < 5; x++) {
				if (x == 0 || x == 4) {
					for (int y = 0; y < 5; y++) {
						World.sendGraphics(AOE_SMALL_EXPLOSION_GFX, 0, 0, areaOfImpact.swX + x, areaOfImpact.swY + y, 0);
					}
				} else {
					World.sendGraphics(AOE_SMALL_EXPLOSION_GFX, 0, 0, areaOfImpact.swX + x, areaOfImpact.swY, 0);
					World.sendGraphics(AOE_SMALL_EXPLOSION_GFX, 0, 0, areaOfImpact.swX + x, areaOfImpact.swY + 4, 0);
				}
			}
			for (Player localPlayer : npc.localPlayers()) {
				if (localPlayer.getPosition().inBounds(areaOfImpact)) {
					localPlayer.hit(new Hit(npc).randDamage(18, 25));
				}
			}
		});
	}

	private void bloodAttack() {
		npc.startEvent(event -> {
			npc.animate(9279);
			event.delay(2);
			npc.localPlayers().forEach(p -> {
				p.startEvent(e -> {
					Position targetPosition = new Position(p.getPosition().getX(), p.getPosition().getY(), p.getPosition().getZ());
					int delay = BLOOD_PROJECTILE.send(npc, targetPosition);
					e.delay(getTicks(delay));
					if (p.getPosition().distance(targetPosition) < 1) {
						int damage = Random.get(info.max_damage);
						p.hit(new Hit(npc).fixedDamage(damage));
						npc.hit(new Hit(HitType.HEAL).fixedDamage((int) (damage * 0.6)));
					}
				});
			});
		});
	}

	private void iceAttack() {
		npc.startEvent(event -> {
			npc.animate(9278);
			event.delay(1);
			npc.localPlayers().forEach(p -> {
				p.startEvent(e -> {
					Position targetPosition = new Position(p.getPosition().getX(), p.getPosition().getY(), p.getPosition().getZ());
					int delay = ICE_PROJECTILE.send(npc, targetPosition);
					e.delay(getTicks(delay));
					if (p.getPosition().distance(targetPosition) < 1) {
						p.graphics(2005);
						int freezeTime = Random.get(3, 6);
						p.freeze(freezeTime, npc);
						p.hit(new Hit(npc).randDamage(info.max_damage));
						p.getCombat().reset();
					}
				});
			});
		});
	}

	private int getTicks(int delay) {
		return Math.max(1, (delay * 16) / Server.tickMs());
	}
}
