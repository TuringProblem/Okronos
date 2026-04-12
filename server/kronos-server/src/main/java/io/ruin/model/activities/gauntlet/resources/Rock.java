package io.ruin.model.activities.gauntlet.resources;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.gauntlet.GauntletResource;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.stat.StatType;

public class Rock extends GauntletResource {
	public Rock(GameObject object, boolean corrupted) {
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
				if (corrupted) {
					if (!player.getInventory().contains(23822) &&
						player.getEquipment().getDef(Equipment.SLOT_WEAPON).id != 23822) {
						player.sendMessage("You need a corrupted pickaxe to mine this!");
						break;
					}
				} else {
					if (!player.getInventory().contains(23863) &&
						player.getEquipment().getDef(Equipment.SLOT_WEAPON).id != 23863) {
						player.sendMessage("You need a crystal pickaxe to mine this!");
						break;
					}
				}
				player.animate(8350);
				event.delay(Random.get(1, 3));
				player.getStats().addXp(StatType.Mining, 10, true);
				player.getInventory().add(corrupted ? new Item(23837) : new Item(23877));
				String oreType = corrupted ? "corrupted" : "crystal";
				player.sendFilteredMessage("You mine some " + oreType + " ore.");
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
		object.setId(corrupted ? 35968 : 36065);
	}
}
