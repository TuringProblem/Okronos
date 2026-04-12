package io.ruin.model.combat.special.magic;

import io.ruin.cache.ObjType;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.skills.slayer.Slayer;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Utils;

import java.util.Objects;
import java.util.Optional;

import static io.ruin.model.entity.player.PlayerCombat.UNDEAD_NPCS;

public class VolatileNightmareStaff implements Special {


	@Override
	public boolean accept(ObjType def, String name) {
		return def.id == 24424;
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(8532);
		player.graphics(1760);
		target.graphics(1759, 0, 0);
		maxDamage = calculateMaxDamage(player, target);
		target.hit(new Hit(player, attackStyle, attackType).randDamage(maxDamage));
		player.getCombat().updateLastAttack(5);
		return true;
	}

	private int calculateMaxDamage(Player player, Entity target) {
		int magicLevel = player.getStats().get(StatType.Magic).currentLevel;
		int maxDamage = (int) ((magicLevel < 75 ? 0 : 50 + ((magicLevel - 75) / 1.5)) *
			player.getCombat().getRegularMagicDamageBoost(true));

		if (maxDamage > 89) {
			maxDamage = 89;
		}
		if (maxDamage > 80) {
			maxDamage = 80;
		}
		return maxDamage;
	}

	@Override
	public int getDrainAmount() {
		return 55;
	}

}
