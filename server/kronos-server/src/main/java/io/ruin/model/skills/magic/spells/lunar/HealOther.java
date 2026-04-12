package io.ruin.model.skills.magic.spells.lunar;

import io.ruin.model.combat.Hit;
import io.ruin.model.item.Item;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;

public class HealOther extends Spell {

	public HealOther() {
		Item[] runes = {
			Rune.LAW.toItem(3),
			Rune.ASTRAL.toItem(3),
			Rune.BLOOD.toItem(1)
		};
		registerEntity(92, runes, (player, entity) -> {
			if (entity.npc != null) {
				player.sendMessage("You can only use this spell on players.");
				return false;
			}
			if (entity.player == player) {
				player.sendMessage("You can't use this spell on yourself.");
				return false;
			}
			if (VarPlayerRepository.ACCEPT_AID.get(entity.player) == 0) {
				player.sendMessage("This player is not accepting aid right now.");
				return false;
			}
			if (player.getHp() < 11) {
				player.sendMessage("You don't have enough HP to cast this spell.");
				return false;
			}
			if (entity.player.getHp() >= 99) {
				player.sendMessage("This player doesn't need healing.");
				return false;
			}
			player.startEvent(event -> {
				player.animate(4411);
				entity.player.graphics(738, 95, 0);
				player.hit(new Hit().fixedDamage((int) (player.getStats().get(StatType.Hitpoints).currentLevel * 0.75)));
				entity.player.getStats().get(StatType.Hitpoints).restore(0, 0.75);
				entity.player.sendMessage(player.getName() + " has healed you.");
				player.getStats().addXp(StatType.Magic, 101, true);
			});
			return true;
		});


	}
}
