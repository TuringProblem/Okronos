package io.ruin.model.activities.miscpvm.slayer;

import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.slayer.Slayer;
import io.ruin.model.stat.StatType;

public class AbhorrentSpectre extends NPCCombat {
	private static final Projectile PROJECTILE = new Projectile(336, 100, 38, 5, 30, 15, 16, 0);
	private static final StatType[] DRAIN = {StatType.Attack, StatType.Strength, StatType.Defence, StatType.Ranged, StatType.Magic};

	@Override
	public void init() {

	}

	@Override
	public void follow() {
		follow(8);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(8))
			return false;
		npc.animate(7550);
		int delay = PROJECTILE.send(npc, target);
		Hit hit = new Hit(npc, info.attack_style).clientDelay(delay);
		if (target.player != null && target.player.getEquipment().getId(Equipment.SLOT_HAT) != 4168 && !Slayer.hasSlayerHelmEquipped(target.player)) {
			hit.randDamage(info.max_damage + 3).ignoreDefence().ignorePrayer();
			for (StatType statType : DRAIN) {
				target.player.getStats().get(statType).drain(6);
			}
			target.player.sendMessage("<col=ff0000>The abhorrent spectre's stench disorients you!");
			target.player.sendMessage("<col=ff0000>A nose peg can protect you from this attack.");
		} else {
			hit.randDamage(info.max_damage);
		}
		target.hit(hit);
		return true;
	}

	@Override
	public void process() {

	}

}
