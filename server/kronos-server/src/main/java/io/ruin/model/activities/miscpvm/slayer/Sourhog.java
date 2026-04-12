package io.ruin.model.activities.miscpvm.slayer;

import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.slayer.Slayer;
import io.ruin.model.stat.StatType;

public class Sourhog extends NPCCombat {

	private static final StatType[] DRAIN = {
		StatType.Attack, StatType.Defence
	};

	@Override
	public void init() {}

	@Override
	public void follow() {
		follow(1);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(1))
			return false;
		// create the base hit
		var hit = new Hit(npc, info.attack_style);
		// Eye-Protection check
		if (target.player != null &&
			target.player.getEquipment().getId(Equipment.SLOT_HAT) != 24942 &&
			!Slayer.hasSlayerHelmEquipped(target.player)
		) {
			npc.animate(8770); // Spitting animation
			// No eye protection? lemme fuk you up
			hit.randDamage(20, 30).ignoreDefence().ignorePrayer();
			// ...and drain your stats
			for (StatType statType : DRAIN) {
				var stat = target.player.getStats().get(statType);
				var toDrain = stat.currentLevel *= 0.9; // Drain 90% of their stat
				stat.drain(toDrain);
			}
			// ... "Not the face!"
			target.player.forceText("Argh! My eyes!");
		}
		else{
			npc.animate(info.attack_animation); // Melee attack
			hit.randDamage(info.max_damage);
		}
		// finally, we hit the player
		target.hit(hit);
		return true;
	}

	@Override
	public void process() {}
}
