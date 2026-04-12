package io.ruin.model.combat.special.magic;

import io.ruin.cache.ObjType;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.stat.StatType;

public class CorruptedNightmareStaff implements Special {

	private static final int SPECIAL_RANGE = 10;

	@Override
	public boolean accept(ObjType def, String name) {
		return def.id == 25932;
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(3299);
//        player.graphics(1762); Would like to find a better animation for this.
		target.graphics(1813, 0, 0);

		// TODO add its special attack
		int damage = target.hit(new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostDamage(2.20));
		player.getCombat().reset();
		if (damage > 0 && target.player != null) {
			target.player.getPrayer().drain(damage);
			player.getStats().get(StatType.Prayer).restore(damage, 0);
		}
		return true;
	}

	@Override
	public boolean handleActivation(Player player) {
		player.getCombat().setSpecialDistance(SPECIAL_RANGE);
		return false;
	}

	@Override
	public int getDrainAmount() {
		return 55;
	}

}
