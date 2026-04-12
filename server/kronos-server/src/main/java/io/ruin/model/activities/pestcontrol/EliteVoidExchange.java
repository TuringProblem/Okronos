package io.ruin.model.activities.pestcontrol;

import io.ruin.model.activities.wilderness.ChaosAltar;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemNPCAction;

import static io.ruin.cache.ItemID.COINS_995;

public class EliteVoidExchange {

	public static final int VOID_KNIGHT = 5513;
	public static int TOP = 8839, ROBE = 8840;

	public static void register() {
		NPCAction.register(VOID_KNIGHT, "talk-to", (player, npc) -> player.dialogue(
			new NPCDialogue(npc, "Do you wish me to upgrade any Void Knight armor for you?"),
			new OptionsDialogue(
				new Option("What does that do?", () -> player.dialogue(
					new PlayerDialogue("What does that do?"),
					new NPCDialogue(npc, "The top and robes can be upgraded, separately, to give each piece a +3 prayer bonus. It costs 200 Commendation points to upgrade each item."))),
				new Option("Yes please.", () -> player.dialogue(
					new PlayerDialogue("Yes please."),
					new NPCDialogue(npc, "Very well. Hand me the top or robe you wish me to upgrade. It costs 200 Commendation points to upgrade each item."))),
				new Option("No thanks.", () -> player.dialogue(new PlayerDialogue("No thanks.")))
			)
		));
		ItemNPCAction.register(TOP, VOID_KNIGHT, EliteVoidExchange::exchangeTop);
		ItemNPCAction.register(ROBE, VOID_KNIGHT, EliteVoidExchange::exchangeRobe);
	}

	private static void exchangeTop(Player player, Item item, NPC npc) {
		if (player.pestPoints <= 200 || !player.getInventory().hasId(8839)) {
			player.sendMessage("You need at least 200 Pest control points and a Void knight top");
			return;
		}
		if (player.kandarinElite <= 1) {
			player.sendMessage("You need to complete the Kandarin elite diary to upgrade your void top.");
			return;
		}
		if (player.getInventory().hasItem(8839, 1) && player.pestPoints >= 200 && player.kandarinElite >= 2) {
			player.getInventory().remove(8839, 1);
			player.getInventory().add(13072, 1);
			player.pestPoints -= 200;
		}
	}

	private static void exchangeRobe(Player player, Item item, NPC npc) {
		if (player.pestPoints <= 200 || !player.getInventory().hasId(8840)) {
			player.sendMessage("You need at least 200 Pest control points and a Void knight robe");
			return;
		}
		if (player.kandarinElite <= 1) {
			player.sendMessage("You need to complete the Kandarin elite diary to upgrade your void robe.");
			return;
		}
		if (player.getInventory().hasItem(8840, 1) && player.pestPoints >= 200 && player.kandarinElite >= 2) {
			player.getInventory().remove(8840, 1);
			player.getInventory().add(13073, 1);
			player.pestPoints -= 200;
		}
	}
}
