package io.ruin.model.activities.npc;

import io.ruin.cache.ObjType;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.npc.actions.edgeville.Decanter;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.skills.herblore.Herb;
import io.ruin.model.skills.herblore.Potion;

public class herbCleaner {

	public static void HerbCleaning(Player player, int herbcounter, int coincounter) {
		if (player.getInventory().count(995) >= coincounter) {
			player.getInventory().remove(995, coincounter);
			for (Herb herb : Herb.VALUES) {
				if (player.getInventory().contains(herb.grimyId)) {
					int amount = player.getInventory().getAmount(herb.grimyId);
					player.getInventory().remove(herb.grimyId, amount);
					player.getInventory().add(herb.cleanId, amount);
				} else if (player.getInventory().contains(ObjType.get(herb.grimyId).notedId)) {
					int amount = player.getInventory().getAmount(ObjType.get(herb.grimyId).notedId);
					player.getInventory().remove(ObjType.get(herb.grimyId).notedId, amount);
					player.getInventory().add(ObjType.get(herb.cleanId).notedId, amount);
				}
			}
		} else {
			player.sendMessage("Sorry you don't have enough coins!");
		}
	}

	public static void MakeUnfPotions(Player player, int herbcounter, int vialcounter) {
		int coincounter = herbcounter * herbCleanPrice(player);

		if (player.getInventory().count(995) < coincounter) {
			player.sendMessage("Sorry you don't have enough coins!");
			return;
		}


		for (Herb herb : Herb.VALUES) {
			if (player.getInventory().contains(ObjType.get(herb.cleanId).notedId) && player.getInventory().contains(ObjType.get(227).notedId)) {
				vialcounter = player.getInventory().count(ObjType.get(227).notedId);
				herbcounter = player.getInventory().count(ObjType.get(herb.cleanId).notedId);

				int countToRemove = Math.min(vialcounter, herbcounter);
				player.getInventory().remove(ObjType.get(herb.cleanId).notedId, countToRemove);
				player.getInventory().remove(ObjType.get(227).notedId, countToRemove);
				player.getInventory().remove(995, countToRemove * herbCleanPrice(player));
				player.getInventory().add(ObjType.get(herb.unfId).notedId, countToRemove);

			} else if (player.getInventory().contains(herb.cleanId) && player.getInventory().contains(227)) {
				vialcounter = player.getInventory().count(227);
				herbcounter = player.getInventory().count(herb.cleanId);

				int countToRemove = Math.min(vialcounter, herbcounter);
				player.getInventory().remove(herb.cleanId, countToRemove);
				player.getInventory().remove(227, countToRemove);
				player.getInventory().add(herb.unfId, countToRemove);
				player.getInventory().remove(995, countToRemove * herbCleanPrice(player));
			}
		}
	}

	public static int vialcounter;
	public static int coincounter;
	public static int herbcounter;

	private static int herbCleanPrice(Player player) {
		switch (player.getSecondaryGroup()) {
			case DONATOR -> {
				return 4800;
			}
			case SUPER_DONATOR -> {
				return 4600;
			}
			case ELITE_DONATOR -> {
				return 4400;
			}
			case NOBLE_DONATOR -> {
				return 4200;
			}
			case GOLD_DONATOR -> {
				return 4000;
			}
			case PLATINUM_DONATOR -> {
				return 3500;
			}
			case LEGENDARY_DONATOR -> {
				return 2800;
			}
			case SUPREME_DONATOR -> {
				return 2500;
			}
			default -> {
				return 5000;
			}
		}
	}

	public static void register() {
		NPCAction.register(4753, "clean", (player, npc) -> {
			int herbcounter = 0;
			int coincounter = 0;

			for (Herb herb : Herb.VALUES) {
				int tempHerbCounter = 0;
				int tempCoinCounter = 0;
				if (player.getInventory().contains(herb.grimyId)) {
					tempHerbCounter = player.getInventory().count(herb.grimyId);
					tempCoinCounter = tempHerbCounter * herbCleanPrice(player);
				} else if (player.getInventory().contains(ObjType.get(herb.grimyId).notedId)) {
					tempHerbCounter = player.getInventory().count(ObjType.get(herb.grimyId).notedId);
					tempCoinCounter = tempHerbCounter * herbCleanPrice(player);
				}
				herbcounter += tempHerbCounter;
				coincounter += tempCoinCounter;
			}

			int finalHerbcounter = herbcounter;
			int finalCoincounter = coincounter;
			player.dialogue(new OptionsDialogue("Are you sure you want to spend " + finalCoincounter + " Coins! to clean " + herbcounter + " herbs!",
				new Option("yes", () -> HerbCleaning(player, finalHerbcounter, finalCoincounter)),
				new Option("no"))
			);
		});
		NPCAction.register(4753, "make unfinished potion(s)", (player, npc) -> {
			int coincounter = 0;
			int herbcounter = 0;
			int vialcounter = 0;

			for (Herb herb : Herb.VALUES) {
				if (player.getInventory().contains(herb.cleanId)) {
					herbcounter += player.getInventory().count(herb.cleanId);
					coincounter += herbCleanPrice(player);
				} else if (player.getInventory().contains(ObjType.get(herb.cleanId).notedId)) {
					herbcounter += player.getInventory().count(ObjType.get(herb.cleanId).notedId);
					coincounter += herbCleanPrice(player);
				}
			}
			vialcounter = player.getInventory().count(ObjType.get(227).notedId);
			vialcounter += player.getInventory().count(227);

			int finalHerbcounter = herbcounter;
			int finalVialcounter = vialcounter;
			player.dialogue(new OptionsDialogue("Are you sure you want to spend " + coincounter + " Coins! to make " + herbcounter + " unf potions!",
				new Option("yes", () -> MakeUnfPotions(player, finalHerbcounter, finalVialcounter)),
				new Option("no"))
			);
		});
		NPCAction.register(4753, "decant", Decanter::decantPotions);
		InterfaceHandler.register(Interface.POTION_DECANTING, h -> {
			h.actions[3] = (SimpleAction) p -> Potion.decant(p, 1);
			h.actions[4] = (SimpleAction) p -> Potion.decant(p, 2);
			h.actions[5] = (SimpleAction) p -> Potion.decant(p, 3);
			h.actions[6] = (SimpleAction) p -> Potion.decant(p, 4);
		});
	}
}
