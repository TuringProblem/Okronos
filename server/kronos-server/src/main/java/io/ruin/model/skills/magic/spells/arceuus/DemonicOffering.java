package io.ruin.model.skills.magic.spells.arceuus;

import io.ruin.model.item.Item;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.skills.magic.rune.RuneRemoval;
import io.ruin.model.stat.StatType;
import lombok.RequiredArgsConstructor;

public class DemonicOffering extends Spell {

	@RequiredArgsConstructor
	enum Recipes {
		HOLY(25766, 30),
		UNHOLY(25769, 75),
		GOLD(25772, 195),
		OPAL(25775, 255),
		JADE(25778, 330);
		public final int unstrung;
		public final int prayer;

		public static final Recipes[] VALUES = values();
	}


	public DemonicOffering() {
		Item[] runes = {
			Rune.SOUL.toItem(1),
			Rune.WRATH.toItem(1),
		};
		clickAction = (player, i) -> {
			if (!player.getStats().check(StatType.Magic, 84, "cast this spell"))
				return;

			player.startEvent(event -> {
				for (Item item : player.getInventory().getItems()) {
					for (Recipes recipe : Recipes.VALUES) {
						if (item != null && item.getId() == recipe.unstrung) {
							player.sendMessage("You don't have any ash's to scatter.");
							return;
						}
						int amount = player.getInventory().getAmount(recipe.unstrung);
						RuneRemoval r = null;
						while (amount-- > 0) {
							if (runes != null && (r = RuneRemoval.get(player, runes)) == null) {
								player.sendMessage("You don't have enough runes to cast this spell.");
								return;
							}
							if (player.getInventory().getAmount(recipe.unstrung) < 3) {
								player.sendMessage("You don't have any ashes to scatter.");
								return;
							}
//                            if (runes != null && (r = RuneRemoval.get(player, runes)) == null) {
//                                player.sendMessage("You don't have enough runes to cast this spell.");
//                                return;
//                            }
							if (amount < 3) {
								player.sendMessage("You don't have enough ash's to scatter.");
								break;
							}
							player.animate(8975);
							player.graphics(1871, 96, 0);
							player.getInventory().remove(recipe.unstrung, 3);
							r.remove();
							player.getStats().addXp(StatType.Prayer, recipe.prayer * 3, true);
							player.getStats().addXp(StatType.Magic, 84, true);
							event.delay(8);
						}
					}
				}
			});
		};
		return;
	}
}

