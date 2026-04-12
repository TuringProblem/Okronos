package io.ruin.model.item.actions.impl;

import io.ruin.cache.EnumType;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.network.incoming.handlers.InterfaceOnEntityHandler;

import java.util.ArrayList;

public class ItemSet {

	private final int setId;

	private final int[] itemIds;

	private final String contentsMessage;

	private ItemSet(EnumType map) {
		this.setId = map.getIntValues().get(-1);
		this.itemIds = new int[map.length - 1];
		int index = 0;
		for (int key : map.getIntValues().keySet()) {
			if (key != -1) {
				this.itemIds[index++] = map.getIntValues().get(key);
			}
		}
		StringBuilder message = new StringBuilder();
		for (int i = 0; i < itemIds.length; i++) {
			message.append("<col=ef1020>").append(ObjType.get(itemIds[i]).name);
			if (i == itemIds.length - 1)
				message.append("</col>.");
			else
				message.append("</col>, ");
		}
		this.contentsMessage = message.toString();
		ObjType.get(setId).itemSet = this;
	}

	private ItemSet(int setId, int... itemIds) {
		this.setId = setId;
		this.itemIds = itemIds;
		this.contentsMessage = null;
	}

	private void select(Player player) {
		ArrayList<Item> items = player.getInventory().collectOneOfEach(itemIds);
		if (items == null) {
			player.sendMessage("This set consists of:");
			player.sendMessage(contentsMessage);
			return;
		}
		for (Item item : items)
			item.remove();
		player.getInventory().add(setId, 1);
	}

	private void unpack(Player player, Item item) {
		int freeSlots = player.getInventory().getFreeSlots() + 1; //plus 1 because the actual set will be removed
		if (freeSlots < itemIds.length) {
			player.sendMessage("You need " + (itemIds.length - 1) + " free inventory spaces to unpack this set.");
			return;
		}
		item.remove();
		for (int id : itemIds)
			player.getInventory().add(id, 1);
	}

	public static void register() {
		/**
		 * Sets on interface
		 */
		EnumType geSetsList = EnumType.get(1033);
		ArrayList<ItemSet> setList = new ArrayList<>();
		for (int key : geSetsList.getIntValues().keySet()) {
			EnumType itemsMap = EnumType.get(geSetsList.getIntValues().get(key));
			setList.add(new ItemSet(itemsMap));
		}
		ItemSet[] sets = setList.toArray(new ItemSet[setList.size()]);
		InterfaceHandler.register(Interface.ITEM_SETS, h -> {
			h.closedAction = (p, i) -> {
				p.closeInterface(ToplevelComponent.SIDEMODAL);
			};
			h.actions[2] = (DefaultAction) (p, option, slot, itemId) -> {
				if (slot < 0 || slot >= sets.length)
					return;
				if (option == 1) {
					sets[slot].select(p);
					return;
				}
				Item.examine(p, itemId);
			};
		});
		InterfaceHandler.register(Interface.ITEM_SETS_INV, h -> {
			h.actions[0] = (DefaultAction) (p, option, slot, itemId) -> {
				Item item = p.getInventory().get(slot, itemId);

				if (item == null)
					return;
				if (option == 1) {
					ItemSet set = item.getDef().itemSet;
					if (set != null)
						set.unpack(p, item);
					return;
				}
				Item.examine(p, itemId);
			};
		});
		// setList.clear();
		/**
		 * Sets not on interface
		 */
		setList.add(new ItemSet(9668, 5574, 5575, 5576));   //initiate
		setList.add(new ItemSet(9670, 9672, 9674, 9678));   //proselyte m
		setList.add(new ItemSet(9666, 9672, 9674, 9676));   //proselyte f
		for (ItemSet set : setList) {
			for (int i : set.itemIds)
				ItemNPCAction.register(i, "Banker", (player, item, npc) -> set.select(player));
			ItemNPCAction.register(set.setId, "Banker", (player, item, npc) -> set.unpack(player, item));
		}

		//ItemNPCAction.register(12877, "Banker", (player, item, npc) -> item.getDef().itemSet.unpack(player, item));
	}

	public static void open(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, 451);
		player.openInterface(ToplevelComponent.SIDEMODAL, 430);
		player.getPacketSender().sendIfEvents(Interface.ITEM_SETS, 2, 0, 101, 1026);
		player.getPacketSender().sendIfEvents(Interface.ITEM_SETS_INV, 0, 0, 27, 1026);
	}

}
