package io.ruin.model.item;

import java.util.Map;

public class ItemContainer extends ItemContainerG<Item> {

	@Override
	protected Item newItem(int id, int amount, Map<String, String> attributes) {
		return new Item(id, amount, attributes);
	}

	@Override
	protected Item[] newArray(int size) {
		return new Item[size];
	}

	@Override
	public ItemContainer clone() {
		ItemContainer container = new ItemContainer();
		Item[] items = new Item[this.items.length];
		for (int id = 0; id < items.length; id++) {
			items[id] = this.items[id] == null ? null : new Item(this.items[id].getId(), this.items[id].getAmount());
		}
		container.items = items;
		container.updatedCount = updatedCount;
		container.updatedSlots = updatedSlots;
		container.sendAll = sendAll;
		container.player = player;
		container.interfaceHash = interfaceHash;
		container.containerId = containerId;
		container.forceStack = forceStack;
		return container;
	}

}
