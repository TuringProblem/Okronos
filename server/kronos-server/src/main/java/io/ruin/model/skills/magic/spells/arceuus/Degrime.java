package io.ruin.model.skills.magic.spells.arceuus;

import io.ruin.model.item.Item;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.stat.StatType;

import java.util.ArrayList;
import java.util.List;

public class Degrime extends Spell {

	public enum Herb {
		GUAM_LEAF(199, 249, 3, 1.2),
		MARRENTILL(201, 251, 5, 1.9),
		TARROMIN(203, 253, 11, 2.5),
		HARRALANDER(205, 255, 20, 3.1),
		RANARR_WEED(207, 257, 25, 3.7),
		TOADFLAX(3049, 2998, 30, 4.0),
		IRIT_LEAF(209, 259, 40, 4.4),
		AVANTOE(211, 261, 48, 5.0),
		KWUARM(213, 263, 54, 5.6),
		SNAPDRAGON(3051, 3000, 59, 5.9),
		CADANTINE(215, 265, 65, 6.2),
		LANTADYME(2485, 2481, 67, 6.5),
		DWARF_WEED(217, 267, 70, 6.9),
		TORSTOL(219, 269, 75, 7.5);

		public final int grimyherb, cleanherb, lvlreq, herbxp;

		Herb(int grimyherb, int cleanherb, int lvlreq, double herbxp) {
			this.grimyherb = grimyherb;
			this.cleanherb = cleanherb;
			this.lvlreq = lvlreq;
			this.herbxp = (int) herbxp;
		}

		public static final Herb[] VALUES = values();

	}

	public Degrime() {
		Item[] runes = {
			Rune.EARTH.toItem(4),
			Rune.NATURE.toItem(2),
		};
		registerClick(70, 83, true, runes, (player, i) -> {
			List<Item> itemtoenchant = new ArrayList<>();

			for (Herb herb : Herb.VALUES) {
				if (player.getStats().get(StatType.Herblore).currentLevel < herb.lvlreq) {
					player.sendMessage("You need a Herblore level of " + herb.lvlreq + " to clean this herb.");
					return false;
				}
				if (player.getInventory().contains(herb.grimyherb)) {
					int initialAmount = player.getInventory().getAmount(herb.grimyherb);
					itemtoenchant.add(new Item(herb.cleanherb, initialAmount));
					player.getInventory().remove(herb.grimyherb, initialAmount);
				}
			}

			if (itemtoenchant.isEmpty()) {
				player.sendMessage("You dont have any herbs need cleaning.");
				return false;
			}
			player.startEvent(event -> {
				player.lock();
				player.animate(8980);
				player.graphics(1885, 0, 0);
				event.delay(2);
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
