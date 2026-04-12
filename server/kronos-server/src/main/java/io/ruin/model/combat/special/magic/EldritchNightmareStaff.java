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

public class EldritchNightmareStaff implements Special {

	@Override
	public boolean accept(ObjType def, String name) {
		return def.id == 24425;
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(8532);
		player.graphics(1762);
		target.graphics(1761, 0, 0);
		maxDamage = calculateMaxDamage(player, target);
		int damage = target.hit(new Hit(player, attackStyle, attackType).randDamage(maxDamage));
		if (damage > 0) {
			int currentPrayerLevel = player.getStats().get(StatType.Prayer).currentLevel;
			int boost = damage / 2;
			if (currentPrayerLevel + boost > 120) {
				boost = 120 - currentPrayerLevel;
			}
			player.getStats().get(StatType.Prayer).boost(boost, 0);
		}
		return true;
	}

	private int calculateMaxDamage(Player player, Entity target) {
		int magicLevel = player.getStats().get(StatType.Magic).currentLevel;
		int maxDamage = (int) ((magicLevel < 75 ? 0 : 39 + ((magicLevel - 75) / 2)) * player.getCombat().getRegularMagicDamageBoost(true));

//        if(player.slayerTask != null && target.isNpc() && Slayer.isTask(player, target.npc)) {
		if (player.getEquipment().wearsSlayerHelm() || salveAmulatBoost(player, target)) {
			maxDamage *= 0.15;
		}
		if (maxDamage > 67) {
			maxDamage = 67;
		}
//        } else {
		if (maxDamage > 60) {
			maxDamage = 60;
		}
//        }
		return maxDamage;
	}

	private boolean salveAmulatBoost(Player player, Entity entity) {
		final int salveAmuletImbued = 12017;

		Item amulet = player.getEquipment().get(Equipment.SLOT_AMULET);
		if (amulet == null) {
			return false;
		}

		Optional<String> npc = UNDEAD_NPCS.stream().filter(Objects::isNull)
			.filter(s -> s.equalsIgnoreCase(entity.npc.getDef().name)
				|| Utils.containsIgnoreCase(entity.npc.getDef().name, s))
			.findAny();

		if (!npc.isPresent()) {
			return false;
		}

		if (amulet.getId() == salveAmuletImbued) {
			return true;
		}
		return false;
	}

	@Override
	public int getDrainAmount() {
		return 55;
	}
}
