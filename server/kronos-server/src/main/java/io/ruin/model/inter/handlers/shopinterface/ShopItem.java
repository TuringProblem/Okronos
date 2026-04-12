package io.ruin.model.inter.handlers.shopinterface;

public class ShopItem {
	private int itemId;
	private int quantity;
	private int price;

	public ShopItem(int itemId, int quantity, int price) {
		this.itemId = itemId;
		this.quantity = quantity;
		this.price = price;
	}

	public ShopItem(int itemId, int price) {
		this(itemId, 1, price);
	}

	public int getItemId() {
		return itemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getPrice() {
		return price;
	}

	public void decreaseQuantity(int amount) {
		quantity -= amount;
	}

	public void increaseQuantity(int amount) {
		quantity += amount;
	}
}
