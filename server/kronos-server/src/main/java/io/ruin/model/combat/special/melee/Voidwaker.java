package io.ruin.model.combat.special.melee;

import io.ruin.cache.ObjType;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.skills.prayer.Prayer;


public class Voidwaker implements Special {

	@Override
	public boolean accept(ObjType def, String name) {
		return name.contains("voidwaker");
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(1378, 0);
		target.graphics(2363, 0, 0);
		var min = maxDamage / 2;
		var max = (int) (maxDamage * 1.5);
		var hit = new Hit(player, AttackStyle.MAGIC, attackType);
			hit.randDamage(min, max);
			hit.ignoreDefence();
			hit.capDamagePvP(81, target);

		if (target.isPlayer()) {
			// if the player has prayer active, reduce the damage by 50%
			if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
				hit.damage *= 0.5D;
		}
		target.hit(hit);
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 50;
	}

}