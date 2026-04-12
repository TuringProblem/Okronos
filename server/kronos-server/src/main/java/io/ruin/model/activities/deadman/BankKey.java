package io.ruin.model.activities.deadman;

import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;

public class BankKey {

	public World world;
	public ItemContainer lootcontainer;
	public long value;
	public long lastGametickValueUpdate;

	public BankKey(World world) {
		this.world = world;
		lootcontainer = new ItemContainer();//todo check this method
	}

	public BankKey(World world, ItemContainer contents, long value) {
		this.world = world;
		this.lootcontainer = contents;
		this.value = value;
	}

	public BankKey(ItemContainer contents) {
		this.lootcontainer = contents;
		for (Item item : lootcontainer.getItems()) {
			if (item == null) continue;
			ObjType def = item.getDef();
			if (def == null) continue;
			value += item.getDef().value * item.count();
		}
	}

	public BankKey copy() {
		return new BankKey(world, lootcontainer, value);
	}
}