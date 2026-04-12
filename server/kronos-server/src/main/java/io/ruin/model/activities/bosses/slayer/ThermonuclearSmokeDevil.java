package io.ruin.model.activities.bosses.slayer;

import io.ruin.model.activities.miscpvm.slayer.SmokeDevil;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.stat.StatType;

public class ThermonuclearSmokeDevil extends SmokeDevil {
	public boolean playerAboveZeroPrayerPoints = false;
	public boolean damagedPlayer = false;
	public boolean damagedByNonSpecial = false;

	@Override
	public void init() {
		npc.hitListener = new HitListener().preDefend(this::preDefend);
	}

	private void preDefend(Hit hit) {
		if (hit.attacker != null && hit.attacker.isPlayer() && hit.attacker.player.getCombat().specialActive() == null)
			damagedByNonSpecial = true;
	}

	@Override
	public void process() {
		if (target != null && target.player != null && target.player.getStats().get(StatType.Prayer).currentLevel > 0) {
			playerAboveZeroPrayerPoints = true;
		}

	}

	public void resetCombatAchievementVariables() {
		playerAboveZeroPrayerPoints = false;
		damagedPlayer = false;
		damagedByNonSpecial = false;
	}


	@Override
	public boolean attack() {
		if (!withinDistance(8))
			return false;
		if (smokeAttack()) {
			damagedPlayer = true;
			return true;
		}
		npc.animate(info.attack_animation);
		int delay = SmokeDevil.PROJECTILE.send(npc, target);
		Hit hit = new Hit(npc, info.attack_style).randDamage(info.max_damage).ignorePrayer().clientDelay(delay);
		target.hit(hit);
		hit.postDamage(t -> {
			if (hit.damage > 0)
				damagedPlayer = true;
		});
		return true;
	}


}
