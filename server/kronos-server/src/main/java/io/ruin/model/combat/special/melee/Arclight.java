package io.ruin.model.combat.special.melee;


import io.ruin.cache.ObjType;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;

/**
 * @Author - MSD
 * @project - Gauntlet
 */
// Weaken, which uses 50% of the player's special attack energy and lowers the opponent's Strength, Attack,
// and Defence levels by 10% on demons and 5% on other opponents. Weaken's effect is only applied if it is a successful hit;
public class Arclight implements Special {
	@Override
	public boolean accept(ObjType def, String name) {
		return name.contains("arclight");
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 25;
	}
}
