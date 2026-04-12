package io.ruin.model.entity.npc.actions.edgeville;

import io.ruin.model.content.combatachievements.CombatAchievementSystem;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.var.VarPlayerRepository;

import java.util.ArrayList;
import java.util.List;

public class Probita {

	public List<Integer> getPetsToReturn(Player player) {
		List<Integer> pets = new ArrayList<>();
		for (Integer petItemId : player.petItemIdsObtained) {
			if (petItemId == 33008 || petItemId == 33009 || petItemId == 33010 || petItemId == 30596 || pets.contains(petItemId))
				continue;
			if (player.getInventory().contains(petItemId) || player.getBank().contains(petItemId) || (player.pet != null && player.pet.itemId == petItemId))
				continue;
			pets.add(petItemId);
		}
		return pets;
	}

	public static void register() {
		for (Pet pet : Pet.VALUES) {
			ItemNPCAction.register(pet.itemId, 5906, (player, item, npc) -> {
				player.dialogue(
					new NPCDialogue(npc, "That's a cute pet! Can I pet them?"),
					new NPCDialogue(pet.npcId, "Grr!"),
					new NPCDialogue(npc, "Oh, I guess not!")
				);
			});
		}
		NPCAction.register(5906, "talk-to", (player, npc) -> {
			player.dialogue(
				new NPCDialogue(npc, "Hello, how can I help you?"),
				new OptionsDialogue(
					new Option("What do you do?", () -> {
						player.dialogue(
							new PlayerDialogue("What do you do?"),
							new NPCDialogue(npc, "I return any pets that have been lost through dying or other means."),
							new PlayerDialogue("Thanks!")
						);
					}),
					new Option("Can I have my lost pets returned?", () -> {
						List<Integer> pets = new Probita().getPetsToReturn(player);
						if (pets.size() > 0) {
							if (player.getInventory().getFreeSlots() < pets.size()) {
								player.dialogue(new NPCDialogue(npc, "You don't have enough inventory space to hold all of your pets!"));
								return;
							}
							for (Integer petItemId : pets) {
								player.getInventory().add(petItemId, 1);
								player.dialogue(new NPCDialogue(npc, "Sure, there you go!"));
							}
						} else {
							player.dialogue(new NPCDialogue(npc, "Sorry, I don't have any pets to return to you!"));
						}
					}),
					new Option("Nevermind")
				)
			);
		});
		NPCAction.register(5906, 3, (player, npc) -> {
			List<Integer> pets = new Probita().getPetsToReturn(player);
			if (pets.isEmpty()) {
				player.dialogue(new NPCDialogue(npc, "Sorry, I don't have any pets to return to you!"));
				return;
			}
			// try to add all the pets, one by one
			for (Integer petItemId : pets) {
				// if we cannot fit anymore, then break free
				if (player.getInventory().getFreeSlots() < 1) break;
				// add the pet to the player's inventory
				player.getInventory().add(petItemId, 1);
				player.dialogue(new NPCDialogue(npc, "Sure, there you go!"));
			}
		});
		LoginListener.register(player -> {
			player.getPlayerPerkHandler().handleLogin(player);
			CombatAchievementSystem.onLogin(player);
			VarPlayerRepository.SHIFT_DROP.set(player, 1);
			VarPlayerRepository.HIDE_ROOF.set(player, 1);
			VarPlayerRepository.FOLLOWER_PRIORITY.set(player, 1);

			for (Pet pet : Pet.VALUES) {
				if (player.uniqueDrops.containsKey(pet.itemId) && player.uniqueDrops.get(pet.itemId) > 0) {
					if (!player.petItemIdsObtained.contains(pet.itemId))
						player.petItemIdsObtained.add(pet.itemId);
				}
			}
		});
	}


}
