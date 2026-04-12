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

import static core.task.api.API.queue;
import static core.task.api.API.sleep;

/**
 * @author ReverendDread on 7/21/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project Kronos
 */
public class ThunderKhopesh implements Special {

	@Override
	public boolean accept(ObjType def, String name) {
		return def.id == 30388;
	}

	@Override
	public boolean handle(Player player, Entity victim, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(11812);
		player.graphics(3030);
		victim.graphics(3033);
		double boostAttack = 1.0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (victim.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				var perk = (AccurateBlows) player.getPlayerPerkHandler()
					.getActivePerks(player)
					.get(perkIndex)
					.getPerk(player);
				if (perk != null)
					boostAttack += perk.getAccuracyBoost();
			}
		}

		var firstHit = new Hit(player, AttackStyle.SLASH, attackType)
			.randDamage((int) (maxDamage * 1.5))
			.boostAttack(boostAttack);

		var secondHit = new Hit(player, AttackStyle.SLASH, attackType)
			.randDamage((int) (maxDamage * 1.5))
			.boostAttack(boostAttack);

		var bonusHit = new Hit(player, AttackStyle.SLASH, attackType)
			.randDamage((int) (maxDamage * 2.25))
			.boostAttack(boostAttack)
			.clientDelay(2);


		victim.hit(firstHit, secondHit);
		if (firstHit.damage > 0 || secondHit.damage > 0) {
			queue(() -> {
				sleep(1);
				victim.graphics(3031);
				victim.hit(bonusHit);
			});
		}
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 50;
	}
}
