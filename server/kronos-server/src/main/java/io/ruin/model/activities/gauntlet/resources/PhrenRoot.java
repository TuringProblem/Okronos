package io.ruin.model.activities.gauntlet.resources;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.gauntlet.GauntletResource;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.stat.StatType;

public class PhrenRoot extends GauntletResource {
	public PhrenRoot(GameObject object, boolean corrupted) {
		super(object, corrupted);
	}

	@Override
	public void harvest(Player player, GameObject object) {
		player.startEvent(event -> {
			while (true) {
				if (player.getInventory().getFreeSlots() < 1) {
					player.sendMessage("You need at least 1 inventory space to chop this.");
					break;
				}
				if (corrupted) {
					if (!player.getInventory().contains(23821) &&
						player.getEquipment().getDef(Equipment.SLOT_WEAPON).id != 23821) {
						player.sendMessage("You need a corrupted axe to chop this!");
						break;
					}
				} else {
					if (!player.getInventory().contains(23862) &&
						player.getEquipment().getDef(Equipment.SLOT_WEAPON).id != 23862) {
						player.sendMessage("You need a crystal axe to chop this!");
						break;
					}
				}
				player.animate(8323);
				event.delay(Random.get(1, 3));
				player.getStats().addXp(StatType.Woodcutting, 10, true);
				player.getInventory().add(corrupted ? new Item(23838) : new Item(23878));
				player.sendFilteredMessage("You chop some phren roots.");
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
		object.setId(corrupted ? 35970 : 36067);
	}
}
