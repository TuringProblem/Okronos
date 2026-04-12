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
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.stat.StatType;

public class Darklight implements Special {

	public static final StatType[] DRAIN_STATS = {StatType.Strength, StatType.Attack, StatType.Defence};

	@Override
	public boolean accept(ObjType def, String name) {
		return def.id == 6746 || def.id == 19675 || def.id == 29589;
	}

	@Override
	public boolean handle(Player player, Entity victim, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		boolean demon = victim.npc != null && victim.npc.getDef().demon;
		player.animate(2890);
		player.graphics(483);
		double boostAttack = 0.5;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (victim.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				boostAttack += c.getAccuracyBoost();
			}
		}
		Hit hit = new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostAttack(boostAttack);
		victim.hit(hit);
		double drain = 0.05;
		if(demon) {
			drain = 0.1;
			if(player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 29589)
				drain = 0.15;
		}
		if (!hit.isBlocked()) {
			for (StatType stat : DRAIN_STATS) {
				if (victim.player != null)
					victim.player.getStats().get(stat).drain(drain);
				else
					victim.npc.getCombat().getStat(stat).drain(drain);
			}
		}
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 50;
	}
}
