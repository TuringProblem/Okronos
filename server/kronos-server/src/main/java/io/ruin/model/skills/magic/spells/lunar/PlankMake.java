package io.ruin.model.skills.magic.spells.lunar;


import io.ruin.content.share.Plank;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;


import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.skills.magic.rune.RuneRemoval;
import io.ruin.model.stat.StatType;

import java.util.concurrent.atomic.AtomicInteger;

import static io.ruin.cache.ItemID.*;


public class PlankMake extends Spell {

	private static Item[] runes = {
		Rune.ASTRAL.toItem(0),
		Rune.EARTH.toItem(0),
		Rune.NATURE.toItem(0)
	};

	public PlankMake() {
		registerItem(86, 0, true, runes, (player, item) -> {
			if (player.alchDelay.isDelayed()) {
				player.startEvent(event -> {
					event.delay(player.alchDelay.remaining());
					itemAction.accept(player, item);
				});
				return false;
			}
			return make(player, item);
		});
	}


	private static boolean make(Player player, Item item) {
		Item[] runes = {
			Rune.ASTRAL.toItem(2),
			Rune.EARTH.toItem(15),
			Rune.NATURE.toItem(1)
		};
		player.startEvent(event -> {
			Plank plank = Plank.getFromLog(item.getId());
			int amount = player.getInventory().getAmount(plank.woodId);
			while (amount-- > 0) {
				if (plank == null) {
					player.sendMessage("You can't turn this item into a plank.");
					return;
				}
				RuneRemoval r = null;
				if (runes != null && (r = RuneRemoval.get(player, runes)) == null) {
					player.sendMessage("You don't have enough runes to cast this spell.");
					break;
				}
				if (!player.getInventory().contains(COINS_995, plank.cost)) {
					player.sendMessage(
						"You need at least " + plank.cost + COINS_995 + " to do this.");
					return;
				}
				player.getInventory().remove(plank.woodId, 1);
				player.getInventory().remove(COINS_995, plank.cost);
				r.remove();
				player.getInventory().add(plank.plankId, 1);
				player.getStats().addXp(StatType.Magic, 90, true);
				player.animate(6298);
				player.graphics(1063, 100, 0);
				event.delay(6);
			}
		});
		return true;
	}
}