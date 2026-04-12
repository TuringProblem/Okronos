package com.reasonps.dominion.npc;

import com.reasonps.dominion.DominionOfEchoes;
import core.api.Random;
import io.ruin.cache.ItemID;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.Pet;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-30
 */
@Slf4j
public class KeeperOfEchos {

	public static void register() {
		// Keeper in the Lobby
		NPCAction.register(17012, "Talk-to", KeeperOfEchos::handleTalkTo);
		NPCAction.register(17012, "Sacrifice", KeeperOfEchos::handleSacrificingOrb);

		// Keeper within the realm, with no store option
		NPCAction.register(17013, "Proceed", (player, npc) -> handleRoomProgression(player));

		// Keeper within the realm, with a store option
		NPCAction.register(17014, "Proceed", (player, npc) -> handleRoomProgression(player));
	}

	private static void handleSacrificingOrb(Player player, NPC keeper) {
		var orbs = List.of(
			new Item(ItemID.ASGARNIA_ECHO_ORB),
			new Item(ItemID.DESERT_ECHO_ORB),
			new Item(ItemID.FREMENNIK_ECHO_ORB),
			new Item(ItemID.KANDARIN_ECHO_ORB),
			new Item(ItemID.MORYTANIA_ECHO_ORB),
			new Item(ItemID.TIRANNWN_ECHO_ORB),
			new Item(ItemID.WILDERNESS_ECHO_ORB),
			new Item(ItemID.KOUREND_ECHO_ORB),
			new Item(ItemID.VARLAMORE_ECHO_ORB)
		);

		if (player.getInventory().containsAny(orbs)) {
			player.dialogue(
				new NPCDialogue(keeper.getId(), "I see you have an orb in your inventory.<br>Would you like to sacrifice it?"),
				new OptionsDialogue("Would you like to sacrifice your orb(s)?",
					new Option("Yes.", () -> {
						for (Item orb : orbs) {
							if (player.getInventory().contains(orb)) {
								player.sendMessage("You sacrifice the orb.");
								player.getInventory().remove(orb);
								if (Random.get(100) == 1) {
									Pet.KEEP_OF_ECHOES.unlock(player, 17012);
									player.dialogue(new MessageDialogue("You have been awarded a pet!"));
								}
								// We break here to only take one orb at a time
								break;
							}
						}
					}),
					new Option("Nevermind")
				)
			);
		}
		else
			player.dialogue(new NPCDialogue(keeper.getId(), "You don't have any orbs in your inventory."));
	}


	private static void handleRoomProgression(Player player) {
		var raid = DominionOfEchoes.getActiveRaidForPlayer(player);
		if (raid == null) {
			log.error("Player {} is not in a raid.", player.getName());
			return;
		}
		raid.movePlayerToNextInstance(player);
	}

	private static void handleTalkTo(Player player, NPC npc) {
		player.dialogue(
			new NPCDialogue(npc.getId(), "Hello mortal. What do you seek?"),
			new OptionsDialogue("What do you seek?",
				new Option("What is this place?", () -> whatIsThisPlaceDialogue(player, npc)),
				new Option("What are the rewards?", () -> whatAreTheRewardsDialogue(player, npc)),
				new Option("Nevermind.", Player::closeDialogue)
			)
		);
	}

	private static void whatIsThisPlaceDialogue(Player player, NPC npc) {
		player.dialogue(
			new PlayerDialogue("What is this place?"),
			new NPCDialogue(npc.getId(), "You stand at the threshold of time's dissonance; The Dominion of Echoes."),
			new NPCDialogue(npc.getId(), "Here, memories of Gielinor’s fiercest foes have fractured from reality, twisted by ancient energies and bound together in eternal unrest."),
			new NPCDialogue(npc.getId(), "This place is not meant to exist… and yet it endures. A test. A prison. A graveyard of champions."),
			new NPCDialogue(npc.getId(), "Enter, if you dare. Conquer each echo… and perhaps the past will reward you.”")
		);
	}

	private static void whatAreTheRewardsDialogue(Player player, NPC npc) {
		player.dialogue(
			new PlayerDialogue("What are the rewards?"),
			new NPCDialogue(npc.getId(), "The echoes do not fall quietly, adventurer. Each one clings to a relic; fragments of power too great for the waking world."),
			new NPCDialogue(npc.getId(), "Weapons forged in storms, shields bathed in dragonfire, trinkets that hum with divine fury..."),
			new NPCDialogue(npc.getId(), "these are not mere treasures, they are remnants of warped champions."),
			new NPCDialogue(npc.getId(), "Defeat them, and their strength may become your own. But know this; such power is never claimed without cost.")
		);
	}
}
