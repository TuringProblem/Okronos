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

/**
 * @author ReverendDread on 7/21/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project Kronos
 */
public class KaruulmLongsword implements Special {

	@Override
	public boolean accept(ObjType def, String name) {
		return name.contains("karuulm longsword");
	}

	@Override
	public boolean handle(Player player, Entity victim, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(6147);
		victim.graphics(5032);
		double boostAttack = 0.1;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (victim.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				boostAttack += c.getAccuracyBoost();
			}
		}
		victim.hit(new Hit(player, AttackStyle.MAGICAL_MELEE, attackType).randDamage((int) (maxDamage * .75)).boostAttack(boostAttack).boostDamage(.15));
		victim.hit(new Hit(player, AttackStyle.MAGICAL_MELEE, attackType).randDamage((int) (maxDamage * .5)).boostAttack(boostAttack).boostDamage(.15));
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 75;
	}
}
