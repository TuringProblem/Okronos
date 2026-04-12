package io.ruin.model.activities.barrows.brothers;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.stat.StatType;

public class Guthan extends NPCCombat {

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
		follow(1);
	}


	@Override
	public boolean attack() {
		if (!withinDistance(1))
			return false;
		npc.animate(info.attack_animation);
		Hit hit = new Hit(npc, info.attack_style).randDamage(info.max_damage);
		Player player = target.player;
		player.killedAllMeleeBarrowsWithoutBeingHit = false;
		hit.postDamage(t -> {
			if (hit.damage > 0)
				player.killedAllBarrowsWithoutDamaged = false;
			if (hit.damage > 0 && Random.rollDie(4, 1)) {
				t.graphics(398);
				npc.incrementHp(hit.damage);
			}
		});
		target.hit(hit);
		return true;
	}

	@Override
	public void process() {
		if (target != null && target.player.getStats().get(StatType.Prayer).currentLevel > 0)
			target.player.barrowsRunWithZeroPrayer = false;
	}


}