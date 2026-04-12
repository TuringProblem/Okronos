package io.ruin.model.activities.raids.tob.dungeon.boss.vasilias;

import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Projectile;

public class NylocasPrinkipas extends NPCCombat {

	private static final int START_HEIGHT = 25;
	private static final int END_HEIGHT = 30;
	private static final int DELAY = 30;
	private static final int DURATION_START = 46;
	private static final int DURATION_INCREMENT = 8;
	private static final int CURVE = 15;
	private static final int OFFSET = 255;
	private static final int TILE_OFFSET = 1;

	private static final Projectile RANGED_PROJECTILE = new Projectile(1560, START_HEIGHT, END_HEIGHT, DELAY, DURATION_START, DURATION_INCREMENT, CURVE, OFFSET).tileOffset(TILE_OFFSET).regionBased();
	private static final Projectile MAGIC_PROJECTILE = new Projectile(1580, START_HEIGHT, END_HEIGHT, DELAY, DURATION_START, DURATION_INCREMENT, CURVE, OFFSET).tileOffset(TILE_OFFSET).regionBased();

	public static final int ANIM = 7989;
	public static final int ANIM_RANGED = 7999;

	private static final int MELEE = 10800;
	private static final int MAGIC = 10802;
	private static final int RANGED = 10801;

	@Override
	public void init() {
		npc.setIgnoreMulti(true);
		npc.setMaxHp((int) ((double) npc.getMaxHp() * 0.75));
		npc.startEvent(e -> {
			e.delay(50);
			if (npc.getHp() > 0) {
				npc.hit(new Hit().fixedDamage(npc.getHp()));
				for (Player p : npc.localPlayers()) {
					if (p.getPosition().isWithinDistance(npc.getPosition(), 2)) {
						p.hit(new Hit().randDamage(5, 10));
					}
				}
			}
		});
	}


	@Override
	public void follow() {
		follow(2);
	}

	@Override
	public boolean attack() {
		if (npc.getId() == MELEE) {
			basicAttack(8004, AttackStyle.CRUSH, info.max_damage);
		} else if (npc.getId() == RANGED) {
			rangedAttack();
		} else if (npc.getId() == MAGIC) {
			magicAttack();
		}
		return true;
	}

	@Override
	public void process() {

	}

	private void magicAttack() {
		npc.animate(ANIM);
		int delay = MAGIC_PROJECTILE.send(npc, target);
		target.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(82).clientDelay(delay));
	}

	private void rangedAttack() {
		npc.animate(ANIM_RANGED);
		int delay = RANGED_PROJECTILE.send(npc, target);
		target.hit(new Hit(npc, AttackStyle.RANGED).randDamage(47).clientDelay(delay));
	}
}
