package io.ruin.model.combat.special.melee;

import io.ruin.cache.ObjType;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.AccurateBlows;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;

//The Statius's warhammer special attack, Smash, costs 35% of the wielder's special attack energy.
//Upon a successful hit it will deal anywhere between 25% and 125% of the wielder's standard max hit, any successful hit will lower the opponents Defence by 30%.
public class StatiusWarhammer implements Special {
	private static StatType[] DRAIN_ORDER = {StatType.Defence, StatType.Strength, StatType.Attack, StatType.Prayer, StatType.Magic, StatType.Ranged};


	@Override
	public boolean accept(ObjType def, String name) {
		return name.contains("corrupted warhammer");
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(1378);
		player.graphics(1450);
		player.publicSound(2541);

		double boostAttack = 0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				boostAttack += c.getAccuracyBoost();
			}
		}

		int damage = target.hit(new Hit(player, attackStyle, attackType).randDamage((int) (maxDamage * 0.25), (int) (maxDamage * 1.25)).boostAttack(boostAttack));

		if (damage > 0) {
			if (target instanceof NPC) {
				target.npc.addWeakenedDamage(player, damage);
			}
			if (target.player != null)
				target.player.getStats().get(StatType.Defence).drain(0.30);
			else
				target.npc.getCombat().getStat(StatType.Defence).drain(0.30);
			if (target.player != null) {
				for (StatType t : DRAIN_ORDER) {
					if (t == StatType.Prayer)
						damage -= target.player.getPrayer().drain(damage);
					else
						damage -= target.player.getStats().get(t).drain(damage);
					if (damage <= 0)
						break;
				}
				target.player.sendMessage("You feel drained!");
			} else {
				for (StatType t : DRAIN_ORDER) {
					Stat stat = target.npc.getCombat().getStat(t);
					if (stat == null)
						continue;
					damage -= stat.drain(damage);
					if (damage <= 0)
						break;
				}
			}
		}
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 35;
	}

}