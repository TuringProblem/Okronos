package io.ruin.model.activities.raids.toa.bosses.akkha;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;

public class AkkhasPhantom extends NPCCombat {

	private static final Projectile RANGED_PROJECTILE = new Projectile(475, 65, 31, 15, 56, 10, 15, 64);

	private static final Projectile MAGIC_PROJECTILE = new Projectile(475, 65, 31, 15, 56, 10, 15, 64);

	//Attack - 9774

	@Override
	public void init() {
	}

	int attackStyle = 0;

	@Override
	public boolean attack() {
		if (Random.get(7) == 0) {
			if (attackStyle == 0)
				attackStyle = 1;
			else
				attackStyle = 0;
		}
		if (attackStyle == 0)
			rangedAttack();
		else
			magicattack();
		return true;
	}

	@Override
	public void process() {

	}

	private void rangedAttack() {
		npc.animate(9774);
		Position[] targets = new Position[5];
		for (Player player : npc.localPlayers()) {
			if (!canAttack(player))
				continue;
			for (Position pos : targets) {
				if (player.isAt(pos.getX(), pos.getY())) {
					int delay = RANGED_PROJECTILE.send(npc, player);
					player.hit(new Hit(npc, AttackStyle.MAGIC, null).randDamage(15, 35).clientDelay(delay).ignoreDefence());
				}
			}
		}
	}

	private void magicattack() {
		Position[] targets = new Position[5];
		for (Player player : npc.localPlayers()) {
			if (!canAttack(player))
				continue;
			for (Position pos : targets) {
				if (player.isAt(pos.getX(), pos.getY())) {
					int delay = MAGIC_PROJECTILE.send(npc, player);
					player.hit(new Hit(npc, AttackStyle.MAGIC, null).randDamage(15, 35).clientDelay(delay).ignoreDefence());
				}
			}
		}
	}


	@Override
	public void follow() {
	}

	@Override
	public int getAggressionRange() {
		return 100;
	}
}
