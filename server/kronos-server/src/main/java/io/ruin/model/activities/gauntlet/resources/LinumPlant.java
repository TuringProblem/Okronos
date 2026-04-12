package io.ruin.model.activities.gauntlet.resources;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.gauntlet.GauntletResource;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.stat.StatType;

public class LinumPlant extends GauntletResource {
	public LinumPlant(GameObject object, boolean corrupted) {
		super(object, corrupted);
	}

	@Override
	public void harvest(Player player, GameObject object) {
		player.startEvent(event -> {
			while (true) {
				if (player.getInventory().getFreeSlots() < 1) {
					player.sendMessage("You need at least 1 inventory space to mine this.");
					break;
				}
				player.animate(2282);
				event.delay(Random.get(1, 3));
				player.getStats().addXp(StatType.Farming, 1, true);
				player.getInventory().add(new Item(23836, 1));
				health--;
				event.delay(1);
				if (health <= 0) {
					deplete(object);
					break;
				}
			}
		});
	}

	@Override
	public void deplete(GameObject object) {
		object.setId(corrupted ? 35976 : 36073);
	}
}
