package io.ruin.model.skills.magic.spells.ancient;

import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.BlessedBolts;
import io.ruin.model.activities.perktree.perks.BloodSacrifice;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.skills.magic.spells.TargetSpell;

public abstract class BloodSpell extends TargetSpell {

	@Override
	protected void afterHit(Hit hit, Entity target) {
		if (hit.damage > 0) {
			int healAmount = hit.damage / 4;
			if (hit.attacker.player != null && hit.attacker.player.getEquipment().hasId(22647)) // zuriel's staff
				healAmount *= 1.5;
			if (hit.attacker.player.getPlayerPerkHandler().getActivePerks(hit.attacker.player).contains(Perks.BLOOD_SACRIFICE)) {
				int perkIndex = hit.attacker.player.getPlayerPerkHandler().getActivePerkIndex(hit.attacker.player, Perks.BLOOD_SACRIFICE);
				BloodSacrifice c = (BloodSacrifice) hit.attacker.player.getPlayerPerkHandler().
					getActivePerks(hit.attacker.player).get(perkIndex).getPerk(hit.attacker.player);
				double multiplier = 1 + c.getHealAmount();
				healAmount *= multiplier;

			}
			hit.attacker.incrementHp(healAmount);
		}
	}

}