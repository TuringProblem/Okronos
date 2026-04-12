package io.ruin.model.combat.special.ranged.bolts;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.BlessedBolts;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.impl.ZaryteCrossbow;

import java.util.function.BiFunction;

public class SapphireBoltEffect implements BiFunction<Entity, Hit, Boolean> {

	@Override
	public Boolean apply(Entity target, Hit hit) {
		Player player = hit.attacker.player;
		final boolean ZaryteBow = ZaryteCrossbow.isWorn(player);
		double chance = 0.05;
		if (ZaryteBow && ZaryteCrossbow.forciblyProcBolt(player))
			chance = 1;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.BLESSED_BOLTS)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.BLESSED_BOLTS);
			BlessedBolts c = (BlessedBolts) player.getPlayerPerkHandler().
				getActivePerks(player).get(perkIndex).getPerk(player);
			chance += c.getBoltSpecialChance();

		}

		if (target.player == null || (!Random.rollPercent(chance) && chance < 1))
			return false;
		Stat prayer = hit.attacker.player.getStats().get(StatType.Prayer);
		int drain = prayer.currentLevel / 20;
		if (drain == 0)
			return false;
		if (ZaryteBow)
			drain *= 0.1;
		prayer.boost(drain / 2, 0.0);
		target.player.getPrayer().drain(drain);
		target.graphics(751);
		target.hit(hit);
		return true;
	}

}