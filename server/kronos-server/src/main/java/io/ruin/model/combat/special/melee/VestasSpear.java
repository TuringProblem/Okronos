package io.ruin.model.combat.special.melee;

import io.ruin.cache.ObjType;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.AccurateBlows;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.utility.Misc;

import java.util.LinkedList;

public class VestasSpear implements Special {
	@Override
	public boolean accept(ObjType def, String name) {
		return name.equalsIgnoreCase("vesta's spear") || name.equalsIgnoreCase("vesta's spear (deg)");
	}

	@Override
	public boolean handle(Player player, Entity victim, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(8184);
		player.graphics(1627);
		player.vestasSpearSpecial.delay(8);
		int targetsHit = 1;
		victim.hit(new Hit(player, attackStyle, attackType).randDamage(maxDamage / 2));
		if (player.inMulti()) {
			LinkedList<Entity> targets = new LinkedList<>();
			player.localPlayers().forEach(p -> {
				if (p != player)
					targets.add(p);
			});
			targets.addAll(player.localNpcs());
			targets.remove(victim);
			for (Entity e : targets) {
				double boostAttack = 0;
				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
					if (e.isNpc()) {
						int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
						AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
							getActivePerks(player).get(perkIndex).getPerk(player);
						boostAttack += c.getAccuracyBoost();
					}
				}
				if (player.getCombat().canAttack(e, false) && Misc.getEffectiveDistance(player, e) <= 8) {
					targetsHit++;
					e.hit(new Hit(player, attackStyle, attackType).randDamage(maxDamage / 2).boostAttack(boostAttack));
					if (targetsHit >= 16)
						break;
				}
			}
		}
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 50;
	}
}
