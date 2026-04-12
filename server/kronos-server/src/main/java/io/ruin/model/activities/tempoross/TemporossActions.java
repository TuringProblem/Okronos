package io.ruin.model.activities.tempoross;

import io.ruin.cache.ObjType;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.skills.Tool;
import io.ruin.model.stat.StatType;

import static io.ruin.model.activities.tempoross.Tempoross.CannonAttack;

public class TemporossActions {

	public static final TemporossBoat LADDER = new TemporossBoat(TemporossGameSettings.NOVICE);

	public static void register() {

//        NPCAction.register(10571, 1, (player, npc) -> {
//            player.startEvent(event -> {
//                if (player.getInventory().contains(311)) {
//                    player.animate(618); // Normal Harpoon Normal Damage
//                    Tempoross.HarpoonAttack(5,10);
//                } else if (player.getInventory().contains(21028) || player.getEquipment().hasId(21028)) {
//                    player.animate(7402);// Dragon Harpoon Boosted Damage
//                    Tempoross.HarpoonAttack(10,15);
//                } else if (player.getInventory().contains(23762) || player.getEquipment().hasId(23762)) {
//                    player.animate(618);// Crystal Harpoon Boosted+ Damage
//                    Tempoross.HarpoonAttack(15,25);
//                } else {
//                    player.animate(6703);// Barehand -Normal Damage
//                    Tempoross.HarpoonAttack(1,5);
//                }
//
//            });
//        });

		NPCAction.register(10576, 1, (player, npc) -> {
			player.startEvent(event -> {
				int amount = player.getInventory().count(25565);
				while (amount-- > 0) {
					for (Item i : player.getInventory().getItems()) {
						if (i.getId() != 25565) {
							player.sendMessage("You don't have anymore " + ObjType.get(i.getId()).name + " to fire!");
							return;
						}
						if (i.getId() == 25565) {
							player.animate(896);
							i.remove();
							CannonAttack();
							Tempoross.cannon1ammo += 1;
							event.delay(4);
						}
					}
				}
			});
		});

		NPCAction.register(10577, 1, (player, npc) -> {
			player.startEvent(event -> {
				int amount = player.getInventory().count(25565);
				while (amount-- > 0) {
					for (Item i : player.getInventory().getItems()) {
						if (i.getId() != 25565) {
							player.sendMessage("You don't have anymore " + ObjType.get(i.getId()).name + " to fire!");
							return;
						}
						if (i.getId() == 25565) {
							player.animate(896);
							i.remove();
							CannonAttack();
							Tempoross.cannon2ammo += 1;
							event.delay(4);
						}
					}
				}
			});
		});
		NPCAction.register(10578, 1, (player, npc) -> {
			player.startEvent(event -> {
				int amount = player.getInventory().count(25565);
				while (amount-- > 0) {
					for (Item i : player.getInventory().getItems()) {
						if (i.getId() != 25565) {
							player.sendMessage("You don't have anymore " + ObjType.get(i.getId()).name + " to fire!");
							return;
						}
						if (i.getId() == 25565) {
							player.animate(896);
							i.remove();
							CannonAttack();
							Tempoross.cannon3ammo += 1;
							event.delay(4);
						}
					}
				}
			});
		});
		NPCAction.register(10579, 1, (player, npc) -> {
			player.startEvent(event -> {
				int amount = player.getInventory().count(25565);
				while (amount-- > 0) {
					for (Item i : player.getInventory().getItems()) {
						if (i.getId() != 25565) {
							player.sendMessage("You don't have anymore " + ObjType.get(i.getId()).name + " to fire!");
							return;
						}
						if (i.getId() == 25565) {
							player.animate(896);
							i.remove();
							CannonAttack();
							Tempoross.cannon4ammo += 1;
							event.delay(4);
						}
					}
				}
			});
		});


		Tile.getObject(41305, 3135, 2840, 0).skipReachCheck = p -> p.equals(3137, 2840) || p.equals(3137, 2841);

		ObjectAction.register(41305, 1, (player, obj) -> {
			if (!player.joinedTempoross) {
				player.sendMessage("Tempoross is currently disabled!");
//                LADDER.join(player, obj);
			} else {
				LADDER.leave(player);
			}
		});

		ObjectAction.register(40966, 2, (player, obj) -> bucketCrate(player, 5));
		ObjectAction.register(40966, 3, (player, obj) -> bucketCrate(player, 10));
		ObjectAction.register(40966, 4, (player, obj) -> player.integerInput("Enter amount to take:", amt -> bucketCrate(player, amt)));

		ObjectAction.register(40966, 1, (player, obj) -> bucketCrate(player, 1));
		ObjectAction.register(40965, 1, (player, obj) -> ropeCrate(player, 1));
		ObjectAction.register(40964, 1, (player, obj) -> hammerCrate(player, 1));
		ObjectAction.register(40967, 1, (player, obj) -> harpoonCrate(player, 1));

		ObjectAction.register(41000, 1, (player, obj) -> player.startEvent(event -> {
			int amt = player.getInventory().count(Tool.EMPTY_BUCKET);
			while (amt-- > 0) {
				Item bucket = player.getInventory().findItem(Tool.EMPTY_BUCKET);
				if (bucket == null)
					break;
				bucket.setId(1929);
				player.animate(894);
				event.delay(2);
			}
		}));

		ObjectAction.register(41004, 1, (player, obj) -> player.startEvent(event -> {
			int amt = player.getInventory().count(Tool.EMPTY_BUCKET);
			while (amt-- > 0) {
				Item bucket = player.getInventory().findItem(Tool.EMPTY_BUCKET);
				if (bucket == null)
					break;
				bucket.setId(1929);
				player.animate(894);
				event.delay(2);
			}
		}));

		ObjectAction.register(41354, 1, (player, obj) -> {
			if (!player.temporossTether) {
				player.sendMessage("You tether yourself to protect from the storm!");
				player.temporossTether = true;
				player.root(5, true);
			} else {
				player.sendMessage("You un-tether yourself!");
				player.temporossTether = false;
				player.root(0, true);
			}
		});

		ObjectAction.register(41352, 1, (player, obj) -> {
			if (!player.temporossTether) {
				player.sendMessage("You tether yourself to protect from the storm!");
				player.temporossTether = true;
				player.root(5, true);
			} else {
				player.sendMessage("You un-tether yourself!");
				player.temporossTether = false;
				player.root(0, true);
			}
		});

		ObjectAction.register(41353, 1, (player, obj) -> {
			if (!player.temporossTether) {
				player.sendMessage("You tether yourself to protect from the storm!");
				player.temporossTether = true;
				player.isMovementBlocked(true, true);
			} else {
				player.sendMessage("You un-tether yourself!");
				player.temporossTether = false;
			}
		});

		ObjectAction.register(41355, 1, (player, obj) -> {
			if (!player.temporossTether) {
				player.sendMessage("You tether yourself to protect from the storm!");
				player.temporossTether = true;
				player.root(5, true);
			} else {
				player.sendMessage("You un-tether yourself!");
				player.temporossTether = false;
				player.root(0, true);
			}
		});

		ObjectAction.register(41236, 1, (player, obj) -> {
			if (!player.getInventory().contains(25564)) {
				player.sendMessage("You don't have any fish to cook!");
			}
			player.startEvent(event -> {
				int amount = player.getInventory().count(25564);
				while (amount-- > 0) {
					for (Item item : player.getInventory().getItems()) {
						if (item == null) {
							player.sendMessage("You don't have anymore " + ObjType.get(25564).name + " to cook.");
							return;
						}
						if (item.getId() == 25564) {
							player.animate(896);
							item.setId(25565);
							player.getStats().addXp(StatType.Cooking, 10, false);
							player.sendFilteredMessage("You cook the Harpoonfish, hardening it's shell!");
							event.delay(4);
						}
					}
				}
			});
		});
	}

	private static void bucketCrate(Player player, int amount) {
		if (player.getInventory().isFull()) {
			player.sendMessage("Not enough space in your inventory.");
			return;
		}
		amount = Math.min(player.getInventory().getFreeSlots(), amount);
		player.getInventory().add(1925, amount);
	}

	private static void ropeCrate(Player player, int amount) {
		if (player.getInventory().isFull()) {
			player.sendMessage("Not enough space in your inventory.");
			return;
		}
		amount = Math.min(player.getInventory().getFreeSlots(), amount);
		player.getInventory().add(954, amount);
	}

	private static void harpoonCrate(Player player, int amount) {
		if (player.getInventory().isFull()) {
			player.sendMessage("Not enough space in your inventory.");
			return;
		}
		amount = Math.min(player.getInventory().getFreeSlots(), amount);
		player.getInventory().add(311, amount);
	}

	private static void hammerCrate(Player player, int amount) {
		if (player.getInventory().isFull()) {
			player.sendMessage("Not enough space in your inventory.");
			return;
		}
		amount = Math.min(player.getInventory().getFreeSlots(), amount);
		player.getInventory().add(2347, amount);
	}


}
