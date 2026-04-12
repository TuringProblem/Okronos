package io.ruin.content.objects;

import io.ruin.cache.Color;
import io.ruin.cache.NPCType;
import io.ruin.data.impl.npcs.npc_combat;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-18
 */
@Slf4j
public class CombatDummy {

	public static void register() {
		NPCAction.register(2668, "Configure", CombatDummy::configureCombatDummyToMimicNPC);
		NPCAction.register(10507, "Configure", CombatDummy::configureCombatDummyToMimicNPC);
	}

	private static void configureCombatDummyToMimicNPC(Player player, NPC dummy) {
		var mapOfResults = new HashMap<Integer, npc_combat.Info>();
		// Prompt the player to input the name of the npc that want to mimic
		player.nameInput("What is the name of the NPC you want to mimic.", search -> {
			search = search.toLowerCase();
			log.info("Searching for NPC with name {}", search);
			// verify the NPC has combat definitions
			for (NPCType def : NPCType.cached.values()) {
				if (def == null || def.name == null)
					continue;

				if (def.name.toLowerCase().contains(search))
					mapOfResults.put(def.id, def.combatInfo);
			}
			if (mapOfResults.isEmpty()) {
				player.sendMessage("No NPCs found with that name.");
				return;
			}
			if (mapOfResults.size() > 1) {
				if (mapOfResults.size() > 5) {
					player.sendMessage("Too many results. Please narrow down your search.");
					return;
				}
				log.info("Found multiple results for NPC with name {}", search);
				var options = new ArrayList<Option>();
				mapOfResults.forEach((npcId, info) -> {
					options.add(
						new Option("%s (ID: %d)".formatted(NPCType.get(npcId).name, npcId),
							() -> {
								player.copyNpcToPlayerDummy(info);
								player.sendMessage(Color.CRIMSON, "Changed the Combat Dummy to mimic NPC %s (ID: %d)"
									.formatted(NPCType.get(npcId).name, npcId));
							}
						)
					);
				});
				var title = "Select the NPC you want to mimic.";
				player.dialogue(new OptionsDialogue(title, options));
				return;
			}
			// else we only have one result
			dummy.getCombat().updateInfo(mapOfResults.values().iterator().next());
			player.sendMessage(Color.CRIMSON, "Changed the Combat Dummy ");
		});

	}
}
