package io.ruin.model.activities.gauntlet;

import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;

public class Dragon extends NPCCombat {

	/*
	Duration Start is speed of projectile
	 */
	private static final Projectile CRYSTAL_PROJECTILE = new Projectile(1701, 30, 30, 35, 60, 1, 16, 192);
	private static final Projectile CORRUPTED_PROJECTILE = new Projectile(1702, 30, 30, 35, 60, 1, 16, 192);

	private static final int CRYSTAL_END_GFX = 1703;
	private static final int CORRUPTED_END_GFX = 1704;

	@Override
	public void init() {

	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		int delay = (npc.getId() == 9047 ? CORRUPTED_PROJECTILE : CRYSTAL_PROJECTILE).send(npc, target);
		target.player.graphics(npc.getId() == 9047 ? CORRUPTED_END_GFX : CRYSTAL_END_GFX, 20, delay);
		npc.animate(83);
		int maxDamage = info.max_damage;
		if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
			maxDamage = 0;
		target.player.hit(new Hit(npc, AttackStyle.MAGIC).randDamage(maxDamage).clientDelay(delay));
		return true;
	}

	@Override
	public void process() {

	}
}
