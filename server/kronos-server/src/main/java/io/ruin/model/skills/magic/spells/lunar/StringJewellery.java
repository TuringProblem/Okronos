package io.ruin.model.skills.magic.spells.lunar;

import io.ruin.cache.ItemID;
import io.ruin.model.item.Item;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.skills.magic.rune.RuneRemoval;
import io.ruin.model.stat.StatType;
import lombok.RequiredArgsConstructor;

public class StringJewellery extends Spell {

	@RequiredArgsConstructor
	enum Recipes {
		HOLY(ItemID.UNSTRUNG_SYMBOL, ItemID.UNBLESSED_SYMBOL),
		UNHOLY(ItemID.UNSTRUNG_EMBLEM, ItemID.UNPOWERED_SYMBOL),
		GOLD(ItemID.GOLD_AMULET_U, ItemID.GOLD_AMULET),
		OPAL(ItemID.OPAL_AMULET_U, ItemID.OPAL_AMULET),
		JADE(ItemID.JADE_AMULET_U, ItemID.JADE_AMULET),
		TOPAZ(ItemID.TOPAZ_AMULET_U, ItemID.TOPAZ_AMULET),
		SAPPHIRE(ItemID.SAPPHIRE_AMULET_U, ItemID.SAPPHIRE_AMULET),
		EMERALD(ItemID.EMERALD_AMULET_U, ItemID.EMERALD_AMULET),
		RUBY(ItemID.RUBY_AMULET_U, ItemID.RUBY_AMULET),
		DIAMOND(ItemID.DIAMOND_AMULET_U, ItemID.DIAMOND_AMULET),
		DRAGONSTONE(ItemID.DRAGONSTONE_AMULET_U, ItemID.DRAGONSTONE_AMULET),
		;
		public final int unstrung;
		public final int strung;

		public static final Recipes[] VALUES = values();
	}


	public StringJewellery() {
		Item[] runes = {
			Rune.EARTH.toItem(10),
			Rune.WATER.toItem(5),
			Rune.ASTRAL.toItem(2),
		};
		clickAction = (player, i) -> {
			if (!player.getStats().check(StatType.Magic, 86, "cast this spell"))
				return;
			player.startEvent(event -> {
				for (Item item : player.getInventory().getItems()) {
					for (Recipes recipe : Recipes.VALUES) {
						if (item != null && item.getId() == recipe.unstrung) {
							player.sendMessage("You don't have any unstrung jewellery to string.");
							return;
						}
						RuneRemoval r = null;
						if (runes != null && (r = RuneRemoval.get(player, runes)) == null) {
							player.sendMessage("You don't have enough runes to cast this spell.");
							return;
						}
						int amount = player.getInventory().getAmount(recipe.unstrung);
						while (amount-- > 0) {
							if (runes != null && (r = RuneRemoval.get(player, runes)) == null) {
								player.sendMessage("You don't have enough runes to cast this spell.");
								return;
							}
							player.animate(4412);
							player.graphics(729, 96, 0);
							player.getInventory().remove(recipe.unstrung, 1);
							player.getInventory().add(recipe.strung, 1);
							r.remove();
							player.getStats().addXp(StatType.Crafting, 4, true);
							player.getStats().addXp(StatType.Magic, 83, true);
							event.delay(6);
						}
					}
				}
			});
			return;
		};
	}
}
