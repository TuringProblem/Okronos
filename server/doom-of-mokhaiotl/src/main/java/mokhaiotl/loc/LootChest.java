package mokhaiotl.loc;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.item.Item;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.utility.CS2Script;
import mokhaiotl.loot.Loot;

import static mokhaiotl.inter.Investigation.calculateValue;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-20
 */
public class LootChest {

	private final static int INVESTIGATION_INTERFACE = 919;
	private final static int CHEST_ID = 50938;

	public static void registerLootChest() {
		ObjectAction.register(CHEST_ID, "look-inside", LootChest::lookInside);

		InterfaceHandler.register(INVESTIGATION_INTERFACE, 26, LootChest::bankLoot);
		InterfaceHandler.register(INVESTIGATION_INTERFACE, 17, LootChest::snagLootToInventory);
	}

	private static void bankLoot(Player player) {
		player.closeInterface(ToplevelComponent.MAINMODAL);
		player.mokhaiotlRewardItems.forEach(item ->
			player.getBank().deposit(item.getId(), item.getAmount()));
		player.mokhaiotlRewardItems.clear();
	}

	private static void snagLootToInventory(Player player) {
		player.closeInterface(ToplevelComponent.MAINMODAL);
		player.mokhaiotlRewardItems.forEach(item ->
			player.getInventory().addOrDrop(item));
		player.mokhaiotlRewardItems.clear();
	}

	private static void lookInside(Player player, GameObject chest) {
		player.openInterface(ToplevelComponent.MAINMODAL, INVESTIGATION_INTERFACE);
		player.getPacketSender().sendClientScript(7927, 0, 1, 1);
		player.getPacketSender().sendIfEvents(INVESTIGATION_INTERFACE, 19, 0, 27, 1086);
		player.getPacketSender().sendString(INVESTIGATION_INTERFACE, 20, calculateValue(player));
		player.getPacketSender().sendItems(-1, 19, 0, Loot.getSortedItems(player).toArray(new Item[0]));
		CS2Script.DOOM_OF_MOKHAIOTL_REWARDS.sendScript(player, INVESTIGATION_INTERFACE << 16 | 19, 0, 8, 2, 1, -1, "", "", "", "", "");
	}
}
