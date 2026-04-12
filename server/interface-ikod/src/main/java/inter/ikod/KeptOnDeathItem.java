package inter.ikod;

import io.ruin.model.item.Item;

class KeptOnDeathItem {
	public final ItemOnDeathItemKind kind;
	public final Item item;

	KeptOnDeathItem(ItemOnDeathItemKind kind, Item item) {
		this.kind = kind;
		this.item = item;
	}

	@Override
	public String toString() {
		return "KeptOnDeathItem{item=" + item + ", kind=" + kind + "}";
	}
}
