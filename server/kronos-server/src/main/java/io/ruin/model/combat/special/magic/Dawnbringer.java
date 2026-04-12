package io.ruin.model.combat.special.magic;

import io.ruin.cache.ObjType;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;

public class Dawnbringer implements Special {


	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 35;
	}

	@Override
	public boolean accept(ObjType def, String name) {
		return name.contains("dawnbringer");
	}
}
