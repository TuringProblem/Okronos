package io.ruin.model.skills.magic.spells.lunar;

import io.ruin.model.combat.Hit;
import io.ruin.model.item.Item;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;

public class EnergyTransfer extends Spell {

	public EnergyTransfer() {
		Item[] runes = {
			Rune.LAW.toItem(2),
			Rune.ASTRAL.toItem(3),
			Rune.NATURE.toItem(1)
		};
		registerEntity(91, runes, ((player, entity) -> {
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
			if (player.getHp() < 10) {
				player.sendMessage("You don't have enough HP to cast this spell.");
				return false;
			}
			if (player.eneryTransferCooldown.remaining() > 0) {
				player.sendMessage("You can only cast this spell once every 30 seconds.");
				return false;
			}
			if (entity.player.eneryTransferCooldown.remaining() > 0) {
				player.sendMessage("This can only be casted once on the same player every 30 seconds.");
				return false;
			}
			if (!player.getCombat().multiCheck(entity.player, false) && player.wildernessLevel > 0) {
				player.sendMessage("You can only cast this spell on players in multi areas when using it in the wilderness.");
				return false;
			}
			if (VarPlayerRepository.SPECIAL_ENERGY.get(player) < 100) {
				player.sendMessage("You need to have full special attack to cast this spell.");
				return false;
			}
			player.startEvent(event -> {
				player.animate(4411);
				player.eneryTransferCooldown.delay(50);
				entity.player.graphics(738, 95, 0);
				player.hit(new Hit().fixedDamage(10));
				VarPlayerRepository.SPECIAL_ENERGY.set(player, 0);
				player.getMovement().drainEnergy(100);
				entity.player.getMovement().restoreEnergy(100);
				entity.player.eneryTransferCooldown.delay(50);
				entity.player.getCombat().restoreSpecial(100);
				entity.player.sendMessage(player.getName() + " has transferred special energy to you.");
				player.getStats().addXp(StatType.Magic, 100, true);
			});

			return true;
		}));
	}
}
