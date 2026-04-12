package io.ruin.model.combat.special.melee;

import io.ruin.Server;
import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.AccurateBlows;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;

public class UrsineChainmace implements Special {
	@Override
	public boolean accept(ObjType def, String name) {
		return def.id == 8872;
	}

	private int getTicks(int delay) {
		return Math.max(1, (delay * 16) / Server.tickMs());
	}

	@Override
	public boolean handle(Player player, Entity victim, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(245);
		player.graphics(1292);
		double boostAttack = 0.0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (victim.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				boostAttack += c.getAccuracyBoost();
			}
		}
		victim.hit(new Hit(player, attackStyle, attackType).randDamage((int) (maxDamage * 1.5)).boostDamage(0.15).boostAttack(boostAttack));
		World.startEvent(event -> {
			int hits = 5;
			while (hits > 0 && !victim.getCombat().isDead()) {
				hits--;
				event.delay(2);
				if (victim.getHp() > 0)
					victim.hit(new Hit(player, attackStyle, attackType).boostAttack(500).randDamage(4, 8));
			}
		});

		return true;
	}

	@Override
	public int getDrainAmount() {
		return 50;
	}

}
