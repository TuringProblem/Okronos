package io.ruin.model.skills.magic.spells.lunar;

import io.ruin.model.World;
import io.ruin.model.activities.duelarena.DuelRule;
import io.ruin.model.combat.Hit;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Widget;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.item.Item;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.stat.StatType;

import java.util.Objects;

public class Vengeance extends Spell {

	public Vengeance() {
		Item[] runes = {Rune.DEATH.toItem(2), Rune.ASTRAL.toItem(4), Rune.EARTH.toItem(10)};
		registerClick(94, 112.0, false, runes, Vengeance::cast);
		setBlightedSack(24621);
	}

	public static boolean cast(Player player, Integer i) {
		if (DuelRule.NO_MAGIC.isToggled(player)) {
			player.sendMessage("Melee has been disabled for this duel!");
			return false;
		}
		if (player.vengeanceActive) {
			player.sendMessage("You already have this spell casted.");
			return false;
		}
		if (VarPlayerRepository.VENG_COOLDOWN.get(player) == 1) {
			player.sendMessage("Vengeance spells may only be cast every 30 seconds.");
			return false;
		}
		if (player.getStats().get(StatType.Defence).currentLevel < 40) {
			player.sendMessage("You need at least 40 defence to cast Vengeance.");
			return false;
		}
		player.resetActions(true, true, false);
		player.animate(8316);
		player.graphics(726, 92, 0);
		player.publicSound(2907, 1, 0);
		player.vengeanceActive = true;
		VarPlayerRepository.VENG_COOLDOWN.set(player, 1);
		player.getPacketSender().sendWidgetTimerCustom(Widget.VENGEANCE, 30);
		World.startEvent(50, e -> VarPlayerRepository.VENG_COOLDOWN.set(player, 0));
		return true;
	}

	public static void check(Player player, Hit hit) {
		if (!player.vengeanceActive)
			return;
		if (hit.attacker == null || hit.attackStyle == null)
			return;
		if (player.getStats().get(StatType.Defence).fixedLevel < 40) {
			return;
		}
		int vengDamage = (int) Math.ceil(hit.damage * 0.75);
		if (vengDamage <= 0)
			return;
		player.vengeanceActive = false;
		player.forceText("Taste Vengeance!");
		if (hit.attacker.isNpc() && hit.attacker.npc.getDef().name.equalsIgnoreCase("Kalphite Queen")) {
			if (vengDamage >= hit.attacker.npc.getHp()) {
				Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.INSECT_DEFLECTION.ordinal())).
					getCombatAchievement()).check(player);
			}
		}
		if (hit.attacker.isNpc() && hit.attacker.npc.getDef().name.equalsIgnoreCase("Zulrah")) {
			if (vengDamage >= hit.attacker.npc.getHp()) {
				Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.SNAKE_REBOUND.ordinal())).
					getCombatAchievement()).check(player);
			}
		}
		hit.attacker.hit(new Hit(player).fixedDamage(vengDamage));
	}

}