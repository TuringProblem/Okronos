package io.ruin.model.content.upgrademanager;

import io.ruin.model.content.UpgradeManager;
import io.ruin.model.item.Item;

public class UpgradeItem {
	private static int component;
	private final UpgradeManager.Category catagory;
	private final String name;
	private final int itemid;
	private final int chance;
	private final Item[] required;

	public UpgradeItem(int component, UpgradeManager.Category category, String name, int itemid, int chance, Item[] required) {
		this.component = component;
		this.catagory = category;
		this.name = name;
		this.itemid = itemid;
		this.chance = chance;
		this.required = required;
	}

//    public UpgradeItem(int component, UpgradeManager.Category category, String name, int itemid, int chance, Item[] required) {
//        this.component = component;
//        this.catagory = category;
//        this.name = name;
//        this.itemid = itemid;
//        this.chance = chance;
//        this.required = required;
//    }

	public static int getComponent() {
		return component;
	}

	public UpgradeManager.Category getCategory() {
		return catagory;
	}

	public String getName() {
		return name;
	}

	public int getItemid() {
		return itemid;
	}

	public int getChance() {
		return chance;
	}

	public Item[] getRequired() {
		return required;
	}
}
