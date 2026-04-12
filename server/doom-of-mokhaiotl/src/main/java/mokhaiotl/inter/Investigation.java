package mokhaiotl.inter;


import io.ruin.api.utils.NumberUtils;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import io.ruin.utility.CS2Script;
import mokhaiotl.loc.BurrowHole;
import mokhaiotl.loot.Loot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-09
 */
public class Investigation {

	private final static int INVESTIGATION_INTERFACE = 919;

	public static void register() {
		InterfaceHandler.register(INVESTIGATION_INTERFACE, handler -> {
			handler.actions[14] = (SimpleAction) Investigation::confirmation;
			handler.actions[23] = (SimpleAction) player -> {
				player.closeInterface(ToplevelComponent.MAINMODAL);
				BurrowHole.jumpIntoBurrowHole(player, null);
			};
		});
	}

	public static void openInterfaceForPlayer(Player player) {
		var delveLevel = player.get("MOKHAIOTL_DELVE_LEVEL", 1);
		player.getPacketSender().sendClientScript(2889, 19857413, 19857416, 19857414, 19857415, 19857417, 19857419, 19857421, 19857422, 19857423, 19857428, 19857426, 19857427, 19857424, 19857425, 0);
		player.getPacketSender().sendClientScript(2524, -1, -1);
		player.openInterface(ToplevelComponent.MAINMODAL, INVESTIGATION_INTERFACE);
		player.getPacketSender().sendClientScript(7927, (delveLevel - 1), 0, 0);
		player.getPacketSender().sendIfEvents(INVESTIGATION_INTERFACE, 19, 0, 27, 1086);
		player.getPacketSender().sendString(INVESTIGATION_INTERFACE, 20, calculateValue(player));
		player.getPacketSender().sendItems(-1, 19, 0, Loot.getSortedItems(player).toArray(new Item[0]));
		CS2Script.DOOM_OF_MOKHAIOTL_REWARDS.sendScript(player, INVESTIGATION_INTERFACE << 16 | 19, 0, 8, 2, 1, -1, "", "", "", "", "");
	}

	/**
	 * Calculates the total value of a player's Mokhaiotl reward items
	 * and returns it as a formatted string.
	 *
	 * @param player the player whose reward item values are to be calculated
	 * @return a formatted string representing the total value of the items in GP
	 */
	public static String calculateValue(Player player) {
		var cashValue = new AtomicInteger(0);
		player.mokhaiotlRewardItems.forEach(item -> {
			var value = item.getDef().value;
			var amt = item.getAmount();
			cashValue.addAndGet(value * amt);
		});
		return "Value: %s GP".formatted(NumberUtils.formatNumber(cashValue.get()));
	}

	private static void confirmation(Player player) {
		player.dialogue(
			new YesNoDialogue(
				"Are you sure you want to claim your loot?",
				"Claiming your loot early will <col=ffff00>forfeit your run</col>, " +
					"not allowing you to proceed any further",
				new Item(31099, 5),
				() -> {
					player.getMovement().teleport(1311, 9554, 0);
					player.mokhaiotlRewardItems.forEach(item -> player.getInventory().addOrDrop(item));
					player.mokhaiotlRewardItems.clear();
					player.currentDynamicMap = null;
				}
			)
		);
	}
}
