package io.ruin.content.plugins.npcs;

import io.ruin.api.plugin.PluginHandler;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.inter.dialogue.NPCDialogue;

/**
 * Flax Keeper — exchanges flax for bowstrings.
 * Migrated to io.ruin.content.plugins as a demonstration of the plugin pattern.
 */
@PluginHandler
public class FlaxKeeper {

    private static final int FLAX_KEEPER = 5522;

    static {
        NPCAction.register(FLAX_KEEPER, "talk-to", (player, npc) ->
                player.dialogue(new NPCDialogue(npc, "I will exchange your flax for bowstrings.").animate(557))
        );
        NPCAction.register(FLAX_KEEPER, "Exchange", (player, npc) -> {
            int flaxCount = player.getInventory().count(1779);
            player.getInventory().remove(1779, flaxCount);
            player.getInventory().add(1777, flaxCount);
            player.sendMessage("You have exchanged " + flaxCount + " flax for " + flaxCount + " bow strings.");
        });
    }
}
