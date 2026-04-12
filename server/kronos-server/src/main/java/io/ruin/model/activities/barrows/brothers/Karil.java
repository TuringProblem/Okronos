package io.ruin.model.activities.barrows.brothers;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Projectile;
import io.ruin.model.stat.StatType;

public class Karil extends NPCCombat {

	public boolean attackWithNonMagic = false;

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDamage(this::postDamage);

	}

	private void postDamage(Hit hit) {
		if (hit.attackStyle != AttackStyle.MAGIC)
			attackWithNonMagic = true;
	}


	@Override
	public void follow() {
		follow(8);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(8))
			return false;
		Hit hit = projectileAttack(Projectile.BOLT, info.attack_animation, AttackStyle.RANGED, info.max_damage);
		hit.postDamage(t -> {
			Player player = t.player;
			if (hit.damage > 0)
				player.killedAllBarrowsWithoutDamaged = false;
		});
		if (Random.rollDie(4)) {
			target.graphics(401, 100, 0);
			target.player.getStats().get(StatType.Agility).drain(5);
		}
		return true;
	}

	@Override
	public void process() {
		if (target != null && target.player.getStats().get(StatType.Prayer).currentLevel > 0)
			target.player.barrowsRunWithZeroPrayer = false;
	}

}