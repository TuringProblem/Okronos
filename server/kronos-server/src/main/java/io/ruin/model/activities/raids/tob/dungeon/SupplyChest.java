package io.ruin.model.activities.raids.tob.dungeon;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;


public class SupplyChest {
	private static final int INTERFACE_ID = 405;

	public static void open(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, INTERFACE_ID);
		player.getPacketSender().sendString(INTERFACE_ID, 4, "Points Available: %s"
			.formatted(Color.WHITE.wrap(String.valueOf(player.supplyChestPoints))));
		player.startEvent(e -> {
			e.delay(1);
			player.getPacketSender().sendString(INTERFACE_ID, 4, "Points Available: %s"
				.formatted(Color.WHITE.wrap(String.valueOf(player.supplyChestPoints))));
			player.getPacketSender().sendIfEvents(INTERFACE_ID, 8, 0, 7, 1 << 2);
		});
	}

	private static void handleBuy(Player player, int price, int itemId) {
		if (player.getInventory().getFreeSlots() < 1) {
			player.sendMessage("You don't have enough inventory space for this.");
			return;
		}
		if (player.supplyChestPoints >= price) {
			player.getInventory().add(itemId, 1);
			player.supplyChestPoints -= price;
		}
		player.getPacketSender().sendString(INTERFACE_ID, 4, "Points Available: %s"
			.formatted(Color.WHITE.wrap(String.valueOf(player.supplyChestPoints))));
	}

	public static void register() {
		InterfaceHandler.register(INTERFACE_ID, h ->
			h.actions[8] = new InterfaceAction() {
				@Override
				public void handleClick(Player player, int option, int slot, int itemId) {
					switch (slot) {
						case 0, 4, 5 -> handleBuy(player, 1, itemId);
						case 1, 6, 7 -> handleBuy(player, 2, itemId);
						case 2, 3 -> handleBuy(player, 3, itemId);
					}
				}
			});
	}
}
