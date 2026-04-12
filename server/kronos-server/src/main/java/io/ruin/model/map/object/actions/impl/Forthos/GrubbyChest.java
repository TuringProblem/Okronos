package io.ruin.model.map.object.actions.impl.Forthos;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.item.Item;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.utility.Broadcast;

public class GrubbyChest {

	private static final Item[][] LOOTS = {
		/**
		 * Food roll
		*/
		new Item[]{
			new Item(7056, 4),
			new Item(385, 4),
			new Item(6689, 1),
			new Item(3028, 1)
		},
		/**
		 * Potion roll
		*/
		new Item[]{
			new Item(147, 1),
			new Item(159, 1),
			new Item(165, 1),
			new Item(171, 1),
			new Item(3026, 2),
			new Item(139, 2)
		},
		/**
		 * Main roll
		*/
		new Item[]{
			new Item(3050, 10),
			new Item(989, 1),
			new Item(563, 200),
			new Item(560, 200),
			new Item(9075, 200),
			new Item(11232, 50),
			new Item(11237, 100),
			new Item(565, 200),
			new Item(537, 10),
			new Item(1750, 10),
			new Item(208, 10),
			new Item(3052, 10),
			new Item(220, 5),
			new Item(995, 10000)
		}

	};

	private static final Item[] RARE_LOOT = {
		/**
		 * New crystal bow
		*/
		new Item(4212, 1),
		new Item(4224, 1),
		new Item(13091, 1),

	};


	public static void register() {
		ObjectAction.register(34901, 1, (player, obj) -> {
			Item grubbyKey = player.getInventory().findItem(23499);
			if (grubbyKey == null) {
				player.sendFilteredMessage("You need a Grubby key to open this chest.");
				return;
			}
			player.startEvent(event -> {
				player.lock();
				player.sendFilteredMessage("You unlock the Grubby chest with your key.");
				grubbyKey.remove();
				player.privateSound(51);
				player.animate(536);
				World.startEvent(e -> {
					obj.setId(34901);
					e.delay(2);
					obj.setId(obj.originalId);
				});
				grubbyKey.setId(985);
				if (Random.get() <= 0.004) { //1/250
					/**
					 * Rare loot
					 */
					Item loot = RARE_LOOT[Random.get(RARE_LOOT.length - 1)];
					player.getInventory().add(loot.getId(), loot.getAmount());
					Broadcast.GLOBAL.sendNews(player.getName() + " just received " + loot.getDef().descriptiveName + " from the Grubby Chest!");
				} else {
					/**
					 * Regular loot
					 */
					Item[] loot = LOOTS[Random.get(LOOTS.length - 1)];
					for (Item item : loot)
						player.getInventory().addOrDrop(item.getId(), item.getAmount());
				}
				event.delay(1);
				player.unlock();
			});
		});
	}
}
