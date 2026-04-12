package io.ruin.model.item.actions.impl.storage;

import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;

import java.util.Map;

public class MineralBag {
	public void open(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, 1137);
		int containerId = 5064;
		int oldEnhancerCompId = 17;
		int modernEnhancerCompId = 21;
		int innovativeEnhancerCompId = 25;

		int dullMineralsCompId = 29;
		int shinyMineralsCompId = 33;
		int glisteningMineralsCompId = 37;

		sendItemToContainer(player, new Item(ItemID.OLD_ENHANCER, player.mineralBagItems.getOrDefault(ItemID.OLD_ENHANCER, 0)), containerId++, oldEnhancerCompId);
		sendItemToContainer(player, new Item(ItemID.MODERN_ENHANCER, player.mineralBagItems.getOrDefault(ItemID.MODERN_ENHANCER, 0)), containerId++, modernEnhancerCompId);
		sendItemToContainer(player, new Item(ItemID.INNOVATIVE_ENHANCER, player.mineralBagItems.getOrDefault(ItemID.INNOVATIVE_ENHANCER, 0)), containerId++, innovativeEnhancerCompId);

		sendItemToContainer(player, new Item(ItemID.DULL_MINERALS, player.mineralBagItems.getOrDefault(ItemID.DULL_MINERALS, 0)), containerId++, dullMineralsCompId);
		sendItemToContainer(player, new Item(ItemID.SHINY_MINERALS, player.mineralBagItems.getOrDefault(ItemID.SHINY_MINERALS, 0)), containerId++, shinyMineralsCompId);
		sendItemToContainer(player, new Item(ItemID.GLISTENING_MINERALS, player.mineralBagItems.getOrDefault(ItemID.GLISTENING_MINERALS, 0)), containerId, glisteningMineralsCompId);

	}

	private void sendItemToContainer(Player player, Item item, int containerId, int componentId) {
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			1137 << 16 | componentId, containerId,
			4, 7, 1, -1, "Remove-1", "Remove-5", "Remove-10", "Remove-X", "Remove-All");
		player.getPacketSender().sendIfEvents(1137, componentId, 0, 27, 1086);
		player.getPacketSender().sendItems(
			-1,
			componentId,
			containerId,
			item);
	}

	private void remove(Player player, Item item, int amount) {
		if(amount < 1)
			return;
		if(player.mineralBagItems.get(item.getId()) == null) {
			player.sendMessage("You don't have any " + item.getDef().name + " in your mineral bag to remove.");
			return;
		}
		if(amount > player.mineralBagItems.get(item.getId()))
			amount = player.mineralBagItems.get(item.getId());
		if(!player.getInventory().hasRoomFor(item.getId())) {
			player.sendMessage("You do not have enough inventory space.");
			return;
		}
		player.mineralBagItems.put(item.getId(), player.mineralBagItems.get(item.getId()) - amount);
		player.getInventory().add(item.getId(), amount);
		open(player);
	}

	private void removeAll(Player player) {
		player.closeInterface(ToplevelComponent.MAINMODAL);
		for(Map.Entry<Integer, Integer> entry : player.mineralBagItems.entrySet()) {
			int itemId = entry.getKey();
			int amount = entry.getValue();
			if(amount < 1)
				continue;
			if(player.getInventory().hasRoomFor(itemId)) {
				player.getInventory().add(itemId, amount);
				player.mineralBagItems.put(itemId, 0);
			} else {
				player.sendMessage("You do not have enough inventory space.");
				return;
			}
		}
	}

	private void add(Player player, Item item) {
		player.closeInterface(ToplevelComponent.MAINMODAL);
		if(item.getAmount() > 1) {
			player.integerInput("How many would you like to deposit?", s -> {
				if(s > item.getAmount())
					s = item.getAmount();

				player.mineralBagItems.put(item.getId(), player.mineralBagItems.getOrDefault(item.getId(), 0) + s);
				player.sendMessage("You store " + s + " " + item.getDef().name + (s > 1 ? "s" : "") + " into your mineral bag.");
				player.getInventory().remove(item.getId(), s);
			});
		} else {
			player.mineralBagItems.put(item.getId(), player.mineralBagItems.getOrDefault(item.getId(), 0) + 1);
			player.sendMessage("You store a " + item.getDef().name + " into your mineral bag.");
			item.remove();
		}
	}

	private void check(Player player) {
		StringBuilder itemList = new StringBuilder("Your mineral bag contains:");
		boolean empty = true;
		for(Map.Entry<Integer, Integer> entry : player.mineralBagItems.entrySet()) {
			int itemId = entry.getKey();
			int amount = entry.getValue();
			if(amount < 1)
				continue;
			empty = false;
			itemList.append("<br>").append(amount).append(" x ").append(new Item(itemId).getDef().name);
		}
		if(empty) {
			player.sendMessage("Your mineral bag is empty.");
		} else {
			player.sendMessage(itemList.toString());
		}
	}




	public static void register() {
		InterfaceHandler.register(1137, h -> {
			for(int i = 17; i <= 37; i += 4) {
				h.actions[i] = (DefaultAction) (player, option, slot, itemId) -> {
					switch (option) {
						case 1:
							player.getMineralBag().remove(player, new Item(itemId), 1);
							break;
						case 2:
							player.getMineralBag().remove(player, new Item(itemId), 5);
							break;
						case 3:
							player.getMineralBag().remove(player, new Item(itemId), 10);
							break;
						case 4:
							player.integerInput("How many would you like to remove?", s -> player.getMineralBag().remove(player, new Item(itemId), s));
							break;
						case 5:
							player.getMineralBag().remove(player, new Item(itemId), Integer.MAX_VALUE);
							break;
					}
				};
			}
		});
		ItemAction.registerInventory(26986, "open", (player, item) -> player.getMineralBag().open(player));
		ItemAction.registerInventory(26986, "check", (player, item) -> player.getMineralBag().check(player));
		ItemAction.registerInventory(26986, "withdraw-all", (player, item) -> player.getMineralBag().removeAll(player));
		ItemItemAction.register(ItemID.MODERN_ENHANCER, 26986, (player, item, used) -> player.getMineralBag().add(player, item));
		ItemItemAction.register(ItemID.OLD_ENHANCER, 26986, (player, item, used) -> player.getMineralBag().add(player, item));
		ItemItemAction.register(ItemID.INNOVATIVE_ENHANCER, 26986, (player, item, used) -> player.getMineralBag().add(player, item));
		ItemItemAction.register(ItemID.DULL_MINERALS, 26986, (player, item, used) -> player.getMineralBag().add(player, item));
		ItemItemAction.register(ItemID.SHINY_MINERALS, 26986, (player, item, used) -> player.getMineralBag().add(player, item));
		ItemItemAction.register(ItemID.GLISTENING_MINERALS, 26986, (player, item, used) -> player.getMineralBag().add(player, item));
	}
}
