package io.ruin.model.item.containers;


import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.map.object.actions.ObjectAction;

public class TournamentSuppliesInterface {

	public static int INTERFACE_ID = 100;

	public static int ITEM_CONTAINER = 4;

	public static int MAX_CHILD = 491;

	public static int ACCESS_MASK = 1181694;

	public static final int ONE = 1;
	public static final int FIVE = 2;
	public static final int TEN = 3;
	public static final int X = 4;
	public static final int EXAMINE = 5;

	public static void openTournamentSupplies(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, INTERFACE_ID);
		player.getPacketSender().sendIfEvents(INTERFACE_ID, ITEM_CONTAINER, 0, MAX_CHILD, ACCESS_MASK);
	}

	public static void registerTournamentSupplies() {

		InterfaceHandler.register(INTERFACE_ID, h -> {
			h.actions[4] = (DefaultAction) (player, option, slot, itemId) -> {
				switch (option) {
					case ONE:
						player.getInventory().add(itemId, 1);
						break;
					case FIVE:
						player.getInventory().add(itemId, 5);
						break;
					case TEN:
						player.getInventory().add(itemId, 10);
						break;
					case X:
						player.integerInput("Enter amount to take:", amt -> player.getInventory().add(itemId, amt));
						break;
					case EXAMINE:
						player.sendMessage("");
						break;

				}


			};
		});
	}

	public static void register() {
		ObjectAction.register(40434, actions -> {
			actions[1] = (player, obj) -> openTournamentSupplies(player);
		});
	}
}
