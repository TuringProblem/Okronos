package io.ruin.model.skills.magic.spells.lunar;

import io.ruin.model.activities.duelarena.DuelRule;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Widget;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.item.Item;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.stat.StatType;

public class VengeanceOther extends Spell {

	public VengeanceOther() {
		Item[] runes = {
			Rune.DEATH.toItem(2),
			Rune.ASTRAL.toItem(3),
			Rune.EARTH.toItem(10)
		};
		registerEntity(93, runes, (player, entity) -> {
			setBlightedSack(24621);

			if (entity.npc != null) {
				player.sendMessage("You can only use this spell on players.");
				return false;
			}
			if (entity.player.getDuel().stage >= 4) {
				player.sendMessage("This player can't be cured right now.");
				return false;
			}
			if (VarPlayerRepository.ACCEPT_AID.get(entity.player) == 0) {
				player.sendMessage("This player is not accepting aid right now.");
				return false;
			}
			if (VarPlayerRepository.VENG_COOLDOWN.get(player) == 1) {
				player.sendMessage("Vengeance spells may only be cast every 30 seconds.");
				return false;
			}
			if (player.wildernessLevel > 0 && !player.getCombat().multiCheck(entity.player, false)) {
				player.sendMessage("You can only cast this spell on players in multi areas when using it in the wilderness.");
				return false;
			}
			if (VarPlayerRepository.VENG_COOLDOWN.get(entity.player) == 1) {
				player.sendMessage("Vengeance spells may only be cast every 30 seconds.");
				return false;
			}
			player.startEvent(event -> {
				player.animate(4411);
				player.publicSound(2886);
				player.getStats().addXp(StatType.Magic, 61, true);
				event.delay(4);
				entity.graphics(725, 95, 0);
				entity.player.vengeanceActive = true;
				entity.player.sendMessage(player.getName() + " has casted Vengeance on you!");
				VarPlayerRepository.VENG_COOLDOWN.set(entity.player, 1);
				VarPlayerRepository.VENG_COOLDOWN.set(player, 1);
				entity.player.getPacketSender().sendWidgetTimerCustom(Widget.VENGEANCE, 30);
				entity.player.addEvent(e -> {
					e.delay(50);
					VarPlayerRepository.VENG_COOLDOWN.set(entity.player, 0);
				});
				return;
			});
			return true;
		});
	}

	public static void check(Player target, Hit hit) {
		if (target.vengeanceActive)
			return;
		if (hit.attacker == null || hit.attackStyle == null)
			return;
		if (target.getStats().get(StatType.Defence).fixedLevel < 40) {
			return;
		}
		int vengDamage = (int) Math.ceil(hit.damage * 0.75);
		if (vengDamage <= 0)
			return;
		target.vengeanceActive = false;
		target.forceText("Taste Vengeance!");
		hit.attacker.hit(new Hit(target).fixedDamage(vengDamage));
	}

}