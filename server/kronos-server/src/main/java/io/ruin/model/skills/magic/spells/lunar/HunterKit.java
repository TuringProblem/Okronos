package io.ruin.model.skills.magic.spells.lunar;

import io.ruin.cache.ItemID;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;

import java.util.ArrayList;


public class HunterKit extends Spell {

	private static final int[] HUNTER_KIT_TOOLS = {ItemID.NOOSE_WAND, ItemID.BUTTERFLY_NET, ItemID.BIRD_SNARE, ItemID.RABBIT_SNARE, ItemID.TEASING_STICK, ItemID.UNLIT_TORCH, ItemID.IMPLING_JAR, ItemID.BOX_TRAP};

	public static void register() {
		ItemAction.registerInventory(11159, "open", (player, item) -> {
			player.getInventory().remove(11159, 1);
			for (int i = 0; i < HUNTER_KIT_TOOLS.length; i++) {
				player.getInventory().add(HUNTER_KIT_TOOLS[i], 1);
			}
		});
	}

	public HunterKit() {
		Item[] runes = {
			Rune.ASTRAL.toItem(2),
			Rune.EARTH.toItem(2)
		};
		registerClick(71, 70, true, runes, (player, i) -> {
			ArrayList<Item> hunterKit = player.getInventory().collectItems(HUNTER_KIT_TOOLS);
			if (hunterKit != null && hunterKit.size() == 8 || player.getInventory().contains(ItemID.HUNTER_KIT, 1)) {
				player.sendMessage("You already have a Hunter Kit or full set of its contents in your inventory.");
				return false;
			}

			player.startEvent(event -> {
				player.lock();
				player.animate(6303);
				player.graphics(1074, 96, 0);
				player.getInventory().add(ItemID.HUNTER_KIT, 1);
				event.delay(1);
				player.unlock();
			});
			return true;
		});
	}

}
