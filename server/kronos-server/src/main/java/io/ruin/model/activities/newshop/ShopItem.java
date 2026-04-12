package io.ruin.model.activities.newshop;

import io.ruin.model.item.Item;

public class ShopItem {
	Item item;
	int cost;
	ShopCategories category;
	String customDescription;
	boolean ironman;

	public ShopItem(Item item, int cost, ShopCategories category, String customDescription, boolean ironman) {
		this.item = item;
		this.cost = cost;
		this.category = category;
		this.customDescription = customDescription;
		this.ironman = ironman;
	}

	public Item getItem() {
		return item;
	}

	public int getCost() {
		return cost;
	}

	public ShopCategories getCategory() {
		return category;
	}

	public String getCustomDescription() {
		return customDescription;
	}

	public boolean isIronman() {
		return ironman;
	}

}
