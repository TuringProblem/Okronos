package io.ruin.model.activities.raids.toa.bosses.kephri;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;

public class KephriPhantom extends NPCCombat {
	@Override
	public void init() {

	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		npc.getPosition().getRegion().players.forEach(this::handleAutoAttack);
		return true;
	}

	private static final Projectile BOMB_PROJECTILE = new Projectile(2266, 210, 27, 35, 20, 20, 16, 192);

	private void handleAutoAttack(Player target) {
		Position targetPos = target.getPosition().copy();
		npc.animate(9576);
		World.startEvent(e -> {
			World.sendGraphics(1447, 0, 0, targetPos);
			e.delay(1);
			World.sendGraphics(1447, 0, 0, targetPos);
			int delay = BOMB_PROJECTILE.send(npc, targetPos);
			int ticks = World.getTicks(delay);
			ticks++;
			e.delay(ticks);
			World.sendGraphics(2156, 0, 0, targetPos);
			npc.localPlayers().forEach(p -> {
				if (p.getPosition().distance(targetPos) < 1) {
					p.hit(new Hit(npc).fixedDamage(30).ignoreDefence().ignorePrayer());
				}
			});
		});
	}

	@Override
	public void process() {

	}
}
