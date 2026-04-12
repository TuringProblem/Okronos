package io.ruin.model.activities.tempevents.hweenevent;

import io.ruin.api.utils.NumberUtils;
import io.ruin.model.World;
import io.ruin.model.activities.newshop.NewShopHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.network.incoming.handlers.InterfaceOnEntityHandler;

public class HalloweenDeathNPC {

	private static final int DEATH = 9855;

	private static void displayWhichBossesActive(Player player) {
		player.dialogue(
			new NPCDialogue(DEATH, "The current bosses that are dropping pumpkins are: " + ((HweenEvent) World.hweenEvent).getActiveBosses()
			));
	}

	private static void turnInPumpkin(Player player, Item pumpkin, NPC death) {
		if (pumpkin.getId() == 1959) {
			pumpkin.remove();
			player.halloweenEventPoints += 100;
			player.dialogue(new MessageDialogue("You have turned in a pumpkin and received 100 points."));
		} else {
			int amount = pumpkin.getAmount();
			pumpkin.remove();
			player.halloweenEventPoints += amount * 100;
			player.dialogue(new MessageDialogue("You have turned in " + NumberUtils.formatNumber(amount) + " pumpkins and received " + NumberUtils.formatNumber((amount * 100L)) + " points."));
		}
	}

	private static void explainEvent(Player player) {
		player.startEvent((e) -> {
			player.dialogue(new NPCDialogue(DEATH,
				"During the halloween event, there will be 5 active bosses that you can kill to get pumpkins."));
			e.waitForDialogue(player);
			player.dialogue(new NPCDialogue(DEATH,
				"The bosses will be changed every 2 hours, so make sure to check back often to see which bosses are active."));
			e.waitForDialogue(player);
			player.dialogue(new NPCDialogue(DEATH,
				"When killing any of the active bosses you will have a chance to receive a pumpkin drop."));
			e.waitForDialogue(player);
			player.dialogue(new NPCDialogue(DEATH,
				"Return these pumpkins to me and I'll reward you with points to use in my store."));
		});
	}

	public static void register() {
		ItemNPCAction.register(1959, DEATH, HalloweenDeathNPC::turnInPumpkin);
		ItemNPCAction.register(1960, DEATH, HalloweenDeathNPC::turnInPumpkin);
		NPCAction.register(DEATH, 1, (player, npc) -> {
			player.dialogue(
				new OptionsDialogue("What would you like to do?",
					new Option("Open the store.", () -> NewShopHandler.openShop(player, NewShopHandler.halloweenEventStore))
				)
			);
		});
	}
}
