package io.ruin.model.skills.magic.spells.lunar;

import io.ruin.cache.ItemID;
import io.ruin.model.entity.Entity;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;

import java.util.ArrayList;


public class Dream extends Spell {


	public Dream() {
		Item[] runes = {
			Rune.COSMIC.toItem(1),
			Rune.ASTRAL.toItem(2),
			Rune.BODY.toItem(5)
		};
		registerClick(79, 0, true, runes, (player, item) -> {
			Stat HP = player.getStats().get(StatType.Hitpoints);
			if (HP.currentLevel == HP.fixedLevel) {
				player.sendMessage("You already have full hitpoints.");
				return false;
			}
			player.startEvent(event -> {
				while (true) {
					for (int i = 0; i < 3; i++) {
						player.animate(7627);
						event.delay(2);
						player.graphics(1056, 10, 9);
						event.delay(11);
					}
					player.incrementHp(1);
				}
			});
			return true;
		});
	}

}
