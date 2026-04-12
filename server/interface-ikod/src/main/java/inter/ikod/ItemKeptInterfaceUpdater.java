package inter.ikod;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;

import java.util.ArrayList;
import java.util.Arrays;

class ItemKeptInterfaceUpdater {
	private final ItemsKeptOnDeath kept;
	private final ItemsKeptOnDeathInterfaceFlags flags;
	private final int[] keptIds;

	ItemKeptInterfaceUpdater(ItemsKeptOnDeath kept, ItemsKeptOnDeathInterfaceFlags flags) {
		this.kept = kept;
		this.flags = flags;
		this.keptIds = new int[4];
		Arrays.fill(this.keptIds, -1);
		int idx = 0;
		for (Item keptItem : kept.kept) {
			this.keptIds[idx] = keptItem.getId();
			idx++;
		}
	}

	public static ItemKeptInterfaceUpdater create(Player player, ItemsKeptOnDeathInterfaceFlags flags) {
		ItemsKeptOnDeath itemsKeptOnDeath = ItemsKeptOnDeath.create(player, flags);
		return new ItemKeptInterfaceUpdater(itemsKeptOnDeath, flags);
	}

	public void update(Player player) {
		ArrayList<Item> items = new ArrayList<>();
		ArrayList<Item> itemConfigs = new ArrayList<>();

		for (KeptOnDeathItem item : this.kept.otherItems) {
			items.add(item.item);
			itemConfigs.add(new Item(item.kind.configItem, item.item.getAmount()));
		}

		player.getPacketSender().sendItems(584, items);
		player.getPacketSender().sendItems(468, itemConfigs);

		player.getPacketSender().sendClientScript(972, "iiiisioooo",
			flags.skull ? 1 : 0,
			this.flags.protect ? 1 : 0,
			this.flags.wilderness ? 21 : 0,
			this.flags.killedByAPlayer ? 1 : 0,
			"",
			this.kept.kept.size(), this.keptIds[0], this.keptIds[1], this.keptIds[2], this.keptIds[3]);

	}

	@Override
	public String toString() {
		return "kept: " + this.kept;
	}
}
