package io.ruin.model.activities.tempevents.summerevent;

import io.ruin.model.activities.newshop.NewShopHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;

public class SummerEventNPC {

	public static void register() {
		NPCAction.register(1805, "talk-to", SummerEventNPC::talk);
		NPCAction.register(1805, "teleport", ((player, npc) -> SummerEvent.teleportToBoss(player)));
		NPCAction.register(1805, "open-store", ((player, npc) -> NewShopHandler.openShop(player, NewShopHandler.summerStore)));
		NPCAction.register(1805, 5, (((player, npc) -> {
			SummerBossesInterface s = new SummerBossesInterface();
			s.open(player);
		})));
	}

	public static void talk(Player player, NPC npc) {
		player.startEvent(e -> {
			player.dialogue(new PlayerDialogue("What is the event?"));
			e.waitForDialogue(player);
			player.dialogue(new NPCDialogue(npc.getId(), "This event has two parts, one being a summer boss to spawn and the other are random summer creature spawns."));
			e.waitForDialogue(player);
			player.dialogue(new NPCDialogue(npc.getId(), "For the boss to spawn, 8 random bosses will be randomly assigned with a random amount of them to kill as a server."));
			e.waitForDialogue(player);
			player.dialogue(new NPCDialogue(npc.getId(), "Once you kill them all as a server the boss will spawn!"));
			e.waitForDialogue(player);
			player.dialogue(new NPCDialogue(npc.getId(), "The summer creatures will spawn randomly when killing any npc, so keep an eye out for them!"));
		});
	}
}
