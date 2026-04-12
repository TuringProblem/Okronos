package io.ruin.model.skills.magic.spells.lunar;

import io.ruin.cache.ItemID;
import io.ruin.model.item.Item;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class RechargeDragonStone extends Spell {

	@RequiredArgsConstructor
	enum Recipes {
		GLORY(ItemID.AMULET_OF_GLORY, ItemID.AMULET_OF_GLORY4),
		COMBATBRACLET(ItemID.COMBAT_BRACELET, ItemID.COMBAT_BRACELET4),
		SKILLSNECKLACE(ItemID.SKILLS_NECKLACE, ItemID.SKILLS_NECKLACE4),

		;
		public final int uncharged;
		public final int charged;
		public static final Recipes[] VALUES = values();

	}

	public RechargeDragonStone() {
		Item[] runes = {
			Rune.WATER.toItem(4),
			Rune.ASTRAL.toItem(1),
			Rune.SOUL.toItem(1),
		};
		registerClick(89, 95, true, runes, (player, i) -> {
			List<Item> itemtoenchant = new ArrayList<>();
			for (Recipes recipe : Recipes.VALUES) {
				if (player.getInventory().contains(recipe.uncharged)) {
					int initialAmount = player.getInventory().getAmount(recipe.uncharged);
					itemtoenchant.add(new Item(recipe.charged, initialAmount));
					player.getInventory().remove(recipe.uncharged, initialAmount);
				}
			}
			if (itemtoenchant.isEmpty()) {
				player.sendMessage("You have no jewellery that this spell can recharge.");
				return false;
			}
			player.startEvent(event -> {
				player.lock();
				player.animate(712);
				player.graphics(282, 50, 0);
				itemtoenchant.forEach(item -> {
					player.getInventory().add(item);
				});
				event.delay(1);
				player.unlock();
			});
			return true;
		});

	}
}
