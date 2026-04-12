package io.ruin.model.combat.special.melee;

import io.ruin.cache.ObjType;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.containers.Equipment;
import io.ruin.utility.Misc;

import java.util.concurrent.atomic.AtomicInteger;

public class DinhsBulwark implements Special {
	@Override
	public boolean accept(ObjType def, String name) {
		return name.equalsIgnoreCase("dinh's bulwark");
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(player.getEquipment().getId(Equipment.SLOT_WEAPON) == 21015 ? 7511 : 7512);
		player.graphics(1336);
		player.publicSound(3869);
		AtomicInteger targetsHit = new AtomicInteger();
		final int maxTargets = 11;
		if (target.getSize() == 1) {
			if (target.inMulti()) {
				target.forLocalEntity(entity -> {
					if (targetsHit.get() >= maxTargets)
						return;
					if (Misc.getDistance(entity.getPosition(), target.getPosition()) > 5)
						return;
					if (!player.getCombat().canAttack(entity, false))
						return;
					if (target.isPlayer()) {
					} else {
						entity.hit(new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostDamage(0.10));
						targetsHit.getAndIncrement();
					}
				});
			}
			target.hit(new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostDamage(0.10));
		} else {
			target.hit(
				new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostDamage(0.10),
				new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostDamage(0.10).boostDefence(0.25)
			);
		}
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 50;
	}

}
