package io.ruin.model.activities.miscpvm.slayer;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Projectile;
import io.ruin.model.stat.StatType;

public class BasiliskKnight extends NPCCombat {

	private static StatType[] DRAIN = {StatType.Attack, StatType.Strength, StatType.Defence, StatType.Ranged, StatType.Magic};
	private static final Projectile PROJECTILE = new Projectile(1735, 31, 31, 50, 56, 10, 16, 127);

	@Override
	public void init() {

	}

	@Override
	public void follow() {
		follow(1);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(8))
			return false;
		if (Random.rollDie(4, 1))
			magicAttack();
		else if (withinDistance(1))
			basicAttack();
		if (target.player != null) {
			if (target.player.getEquipment().getId(Equipment.SLOT_SHIELD) != 4156 &&
				target.player.getEquipment().getId(Equipment.SLOT_SHIELD) != 24266) {
				for (StatType statType : DRAIN) {
					target.player.getStats().get(statType).drain(4);
				}
				target.hit(new Hit(npc, null).randDamage(2, 5).ignoreDefence().ignorePrayer());
				target.player.sendMessage("<col=ff0000>The basilisk's piercing gaze drains your stats!");
				target.player.sendMessage("<col=ff0000>A mirror shield can protect you from this attack.");
			}
		}
		return true;
	}

	@Override
	public void process() {

	}

	private void magicAttack() {
		npc.animate(8500);
		int delay = PROJECTILE.send(npc, target);
		int maxDamage = 4;
		Hit hit = new Hit(npc, null).randDamage(maxDamage).clientDelay(delay);
		target.hit(hit);
	}
}
