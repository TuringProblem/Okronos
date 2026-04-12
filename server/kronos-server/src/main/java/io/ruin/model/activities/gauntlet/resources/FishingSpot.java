package io.ruin.model.activities.gauntlet.resources;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.gauntlet.GauntletResource;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.stat.StatType;

public class FishingSpot extends GauntletResource {
	public FishingSpot(GameObject object, boolean corrupted) {
		super(object, corrupted);
		this.health = 4;
	}

	@Override
	public void harvest(Player player, GameObject object) {
		player.startEvent(event -> {
			while (true) {
				if (player.getInventory().getFreeSlots() < 1) {
					player.sendMessage("You need at least 1 inventory space to fish here.");
					break;
				}
				if (corrupted) {
					if (!player.getInventory().contains(23823) &&
						player.getEquipment().getDef(Equipment.SLOT_WEAPON).id != 23823) {
						player.sendMessage("You need a crystal harpoon to fish here!");
						break;
					}
				} else {
					if (!player.getInventory().contains(23864) &&
						player.getEquipment().getDef(Equipment.SLOT_WEAPON).id != 23864) {
						player.sendMessage("You need a crystal harpoon to fish here!");
						break;
					}
				}

				// player.animate(8336);
				event.delay(Random.get(1, 3));
				player.getInventory().add(new Item(23872));
				player.getStats().addXp(StatType.Fishing, 10, true);
				player.sendFilteredMessage("You fish a raw paddlefish.");
				health--;
				if (health <= 0) {
					deplete(object);
					break;
				}
				event.delay(1);
			}
		});
	}

	@Override
	public void deplete(GameObject object) {
		object.setId(corrupted ? 35972 : 36069);
	}
}
