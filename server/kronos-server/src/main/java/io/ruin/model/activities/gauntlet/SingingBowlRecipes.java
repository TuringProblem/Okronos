package io.ruin.model.activities.gauntlet;

import io.ruin.cache.ItemID;
import io.ruin.model.item.Item;

public enum SingingBowlRecipes {
	CORRUPTED_BASIC_BOW(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 20), new Item(ItemID.WEAPON_FRAME, 1)}, new Item(ItemID.CORRUPTED_BOW_BASIC, 1), null),
	CORRUPTED_ATTUNED_BOW(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 60), new Item(ItemID.CORRUPTED_BOW_BASIC, 1)}, new Item(ItemID.CORRUPTED_BOW_ATTUNED, 1),
		new Item(ItemID.CORRUPTED_BOW_BASIC, 1)),
	CORRUPTED_PERFECTED_BOW(new Item[]{new Item(ItemID.CORRUPTED_BOW_ATTUNED, 1), new Item(ItemID.CORRUPTED_BOWSTRING, 1)}, new Item(ItemID.CORRUPTED_BOW_PERFECTED, 1),
		new Item(ItemID.CORRUPTED_BOW_ATTUNED, 1)),
	CORRUPTED_BASIC_STAFF(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 20), new Item(ItemID.WEAPON_FRAME, 1)}, new Item(ItemID.CORRUPTED_STAFF_BASIC, 1), null),
	CORRUPTED_ATTUNED_STAFF(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 60), new Item(ItemID.CORRUPTED_STAFF_BASIC, 1)}, new Item(ItemID.CORRUPTED_STAFF_ATTUNED, 1),
		new Item(ItemID.CORRUPTED_STAFF_BASIC, 1)),
	CORRUPTED_PERFECTED_STAFF(new Item[]{new Item(ItemID.CORRUPTED_STAFF_ATTUNED, 1), new Item(ItemID.CORRUPTED_ORB, 1)}, new Item(ItemID.CORRUPTED_STAFF_PERFECTED, 1),
		new Item(ItemID.CORRUPTED_STAFF_ATTUNED, 1)),
	CORRUPTED_BASIC_HALBERD(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 20), new Item(ItemID.WEAPON_FRAME, 1)}, new Item(ItemID.CORRUPTED_HALBERD_BASIC, 1), null),
	CORRUPTED_ATTUNED_HALBERD(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 60), new Item(ItemID.CORRUPTED_HALBERD_BASIC, 1)}, new Item(ItemID.CORRUPTED_HALBERD_ATTUNED, 1),
		new Item(ItemID.CORRUPTED_HALBERD_BASIC, 1)),
	CORRUPTED_PERFECTED_HALBERD(new Item[]{new Item(ItemID.CORRUPTED_HALBERD_ATTUNED, 1), new Item(ItemID.CORRUPTED_SPIKE, 1)}, new Item(ItemID.CORRUPTED_HALBERD_PERFECTED, 1),
		new Item(ItemID.CORRUPTED_HALBERD_ATTUNED, 1)),
	CORRUPTED_BASIC_CRYSTAL_BODY(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 40), new Item(ItemID.CORRUPTED_ORE, 1), new Item(ItemID.PHREN_BARK, 1),
		new Item(ItemID.LINUM_TIRINUM, 1)}, new Item(ItemID.CORRUPTED_BODY_BASIC, 1), null),
	CORRUPTED_ATTUNED_CRYSTAL_BODY(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 60), new Item(ItemID.CORRUPTED_BODY_BASIC, 1), new Item(ItemID.CORRUPTED_ORE, 2),
		new Item(ItemID.PHREN_BARK, 2), new Item(ItemID.LINUM_TIRINUM, 2)}, new Item(ItemID.CORRUPTED_BODY_ATTUNED, 1),
		new Item(ItemID.CORRUPTED_BODY_BASIC, 1)),
	CORRUPTED_PERFECTED_CRYSTAL_BODY(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 80), new Item(ItemID.CORRUPTED_BODY_ATTUNED, 1), new Item(ItemID.CORRUPTED_ORE, 2),
		new Item(ItemID.PHREN_BARK, 2), new Item(ItemID.LINUM_TIRINUM, 2)}, new Item(ItemID.CORRUPTED_BODY_PERFECTED, 1),
		new Item(ItemID.CORRUPTED_BODY_ATTUNED, 1)),
	CORRUPTED_BASIC_CRYSTAL_HELM(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 40), new Item(ItemID.CORRUPTED_ORE, 1), new Item(ItemID.PHREN_BARK, 1),
		new Item(ItemID.LINUM_TIRINUM, 1)}, new Item(ItemID.CORRUPTED_HELM_BASIC, 1), null),
	CORRUPTED_ATTUNED_CRYSTAL_HELM(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 60), new Item(ItemID.CORRUPTED_HELM_BASIC, 1), new Item(ItemID.CORRUPTED_ORE, 2),
		new Item(ItemID.PHREN_BARK, 2), new Item(ItemID.LINUM_TIRINUM, 2)}, new Item(ItemID.CORRUPTED_HELM_ATTUNED, 1),
		new Item(ItemID.CORRUPTED_HELM_BASIC, 1)),
	CORRUPTED_PERFECTED_CRYSTAL_HELM(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 80), new Item(ItemID.CORRUPTED_HELM_ATTUNED, 1), new Item(ItemID.CORRUPTED_ORE, 2),
		new Item(ItemID.PHREN_BARK, 2), new Item(ItemID.LINUM_TIRINUM, 2)}, new Item(ItemID.CORRUPTED_HELM_PERFECTED, 1),
		new Item(ItemID.CORRUPTED_HELM_ATTUNED, 1)),
	CORRUPTED_BASIC_CRYSTAL_LEGS(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 40), new Item(ItemID.CORRUPTED_ORE, 1), new Item(ItemID.PHREN_BARK, 1),
		new Item(ItemID.LINUM_TIRINUM, 1)}, new Item(ItemID.CORRUPTED_LEGS_BASIC, 1), null),
	CORRUPTED_ATTUNED_CRYSTAL_LEGS(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 60), new Item(ItemID.CORRUPTED_LEGS_BASIC, 1), new Item(ItemID.CORRUPTED_ORE, 1),
		new Item(ItemID.PHREN_BARK, 2), new Item(ItemID.LINUM_TIRINUM, 2)}, new Item(ItemID.CORRUPTED_LEGS_ATTUNED, 1),
		new Item(ItemID.CORRUPTED_LEGS_BASIC, 1)),
	CORRUPTED_PERFECTED_CRYSTAL_LEGS(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 80), new Item(ItemID.CORRUPTED_LEGS_ATTUNED, 1), new Item(ItemID.CORRUPTED_ORE, 2),
		new Item(ItemID.PHREN_BARK, 2), new Item(ItemID.LINUM_TIRINUM, 2)}, new Item(ItemID.CORRUPTED_LEGS_PERFECTED, 1),
		new Item(ItemID.CORRUPTED_LEGS_ATTUNED, 1)),
	CORRUPTED_PADDLEFISH(new Item[]{new Item(ItemID.PADDLEFISH, 1), new Item(ItemID.CORRUPTED_SHARDS, 10)}, new Item(25958, 1), null),
	CORRUPTED_ESCAPE_CRYSTAL(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 200)}, new Item(25959, 1), null),
	CORRUPTED_TELEPORT_CRYSTAL(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 40)}, new Item(23858, 1), null),

	CRYSTAL_BASIC_BOW(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 20), new Item(ItemID.WEAPON_FRAME_23871, 1)}, new Item(ItemID.CRYSTAL_BOW_BASIC, 1), null),
	CRYSTAL_ATTUNED_BOW(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 60), new Item(ItemID.CRYSTAL_BOW_BASIC, 1)}, new Item(ItemID.CRYSTAL_BOW_ATTUNED, 1),
		new Item(ItemID.CRYSTAL_BOW_BASIC, 1)),
	CRYSTAL_PERFECTED_BOW(new Item[]{new Item(ItemID.CRYSTAL_BOW_ATTUNED, 1), new Item(ItemID.CRYSTALLINE_BOWSTRING, 1)}, new Item(ItemID.CRYSTAL_BOW_PERFECTED, 1),
		new Item(ItemID.CRYSTAL_BOW_ATTUNED, 1)),
	CRYSTAL_BASIC_STAFF(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 20), new Item(ItemID.WEAPON_FRAME_23871, 1)}, new Item(ItemID.CRYSTAL_STAFF_BASIC, 1), null),
	CRYSTAL_ATTUNED_STAFF(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 60), new Item(ItemID.CRYSTAL_STAFF_BASIC, 1)}, new Item(ItemID.CRYSTAL_STAFF_ATTUNED, 1),
		new Item(ItemID.CRYSTAL_STAFF_BASIC, 1)),
	CRYSTAL_PERFECTED_STAFF(new Item[]{new Item(ItemID.CRYSTAL_STAFF_ATTUNED, 1), new Item(ItemID.CRYSTAL_ORB, 1)}, new Item(ItemID.CRYSTAL_STAFF_PERFECTED, 1),
		new Item(ItemID.CRYSTAL_STAFF_ATTUNED, 1)),
	CRYSTAL_BASIC_HALBERD(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 20), new Item(ItemID.WEAPON_FRAME_23871, 1)}, new Item(ItemID.CRYSTAL_HALBERD_BASIC, 1), null),
	CRYSTAL_ATTUNED_HALBERD(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 60), new Item(ItemID.CRYSTAL_HALBERD_BASIC, 1)}, new Item(ItemID.CRYSTAL_HALBERD_ATTUNED, 1),
		new Item(ItemID.CRYSTAL_HALBERD_BASIC, 1)),
	CRYSTAL_PERFECTED_HALBERD(new Item[]{new Item(ItemID.CRYSTAL_HALBERD_ATTUNED, 1), new Item(ItemID.CRYSTAL_SPIKE, 1)}, new Item(ItemID.CRYSTAL_HALBERD_PERFECTED, 1),
		new Item(ItemID.CRYSTAL_HALBERD_ATTUNED, 1)),
	CRYSTAL_BASIC_CRYSTAL_BODY(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 40), new Item(ItemID.CRYSTAL_ORE, 1), new Item(ItemID.PHREN_BARK_23878, 1),
		new Item(ItemID.LINUM_TIRINUM, 1)}, new Item(ItemID.CRYSTAL_BODY_BASIC, 1), null),
	CRYSTAL_ATTUNED_CRYSTAL_BODY(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 60), new Item(ItemID.CRYSTAL_BODY_BASIC, 1), new Item(ItemID.CRYSTAL_ORE, 2),
		new Item(ItemID.PHREN_BARK_23878, 2), new Item(ItemID.LINUM_TIRINUM, 2)}, new Item(ItemID.CRYSTAL_BODY_ATTUNED, 1),
		new Item(ItemID.CRYSTAL_BODY_BASIC, 1)),
	CRYSTAL_PERFECTED_CRYSTAL_BODY(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 80), new Item(ItemID.CRYSTAL_BODY_ATTUNED, 1), new Item(ItemID.CRYSTAL_ORE, 2),
		new Item(ItemID.PHREN_BARK_23878, 2), new Item(ItemID.LINUM_TIRINUM, 2)}, new Item(ItemID.CRYSTAL_BODY_PERFECTED, 1),
		new Item(ItemID.CRYSTAL_BODY_ATTUNED, 1)),
	CRYSTAL_BASIC_CRYSTAL_HELM(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 40), new Item(ItemID.CRYSTAL_ORE, 1), new Item(ItemID.PHREN_BARK_23878, 1),
		new Item(ItemID.LINUM_TIRINUM, 1)}, new Item(ItemID.CRYSTAL_HELM_BASIC, 1), null),
	CRYSTAL_ATTUNED_CRYSTAL_HELM(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 60), new Item(ItemID.CRYSTAL_HELM_BASIC, 1), new Item(ItemID.CRYSTAL_ORE, 2),
		new Item(ItemID.PHREN_BARK_23878, 2), new Item(ItemID.LINUM_TIRINUM, 2)}, new Item(ItemID.CRYSTAL_HELM_ATTUNED, 1),
		new Item(ItemID.CRYSTAL_HELM_BASIC, 1)),
	CRYSTAL_PERFECTED_CRYSTAL_HELM(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 80), new Item(ItemID.CRYSTAL_HELM_ATTUNED, 1), new Item(ItemID.CRYSTAL_ORE, 2),
		new Item(ItemID.PHREN_BARK_23878, 2), new Item(ItemID.LINUM_TIRINUM, 2)}, new Item(ItemID.CRYSTAL_HELM_PERFECTED, 1),
		new Item(ItemID.CRYSTAL_HELM_ATTUNED, 1)),
	CRYSTAL_BASIC_CRYSTAL_LEGS(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 40), new Item(ItemID.CRYSTAL_ORE, 1), new Item(ItemID.PHREN_BARK_23878, 1),
		new Item(ItemID.LINUM_TIRINUM, 1)}, new Item(ItemID.CRYSTAL_LEGS_BASIC, 1), null),
	CRYSTAL_ATTUNED_CRYSTAL_LEGS(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 60), new Item(ItemID.CRYSTAL_LEGS_BASIC, 1), new Item(ItemID.CRYSTAL_ORE, 2),
		new Item(ItemID.PHREN_BARK_23878, 2), new Item(ItemID.LINUM_TIRINUM, 2)}, new Item(ItemID.CRYSTAL_LEGS_ATTUNED, 1),
		new Item(ItemID.CRYSTAL_LEGS_BASIC, 1)),
	CRYSTAL_PERFECTED_CRYSTAL_LEGS(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 80), new Item(ItemID.CRYSTAL_LEGS_ATTUNED, 1), new Item(ItemID.CRYSTAL_ORE, 2),
		new Item(ItemID.PHREN_BARK_23878, 2), new Item(ItemID.LINUM_TIRINUM, 2)}, new Item(ItemID.CRYSTAL_LEGS_PERFECTED, 1),
		new Item(ItemID.CRYSTAL_LEGS_ATTUNED, 1)),
	CRYSTAL_PADDLEFISH(new Item[]{new Item(ItemID.PADDLEFISH, 1), new Item(ItemID.CRYSTAL_SHARDS, 10)}, new Item(25960, 1), null),
	CRYSTAL_ESCAPE_CRYSTAL(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 200)}, new Item(25961, 1), null),
	CRYSTAL_TELEPORT_CRYSTAL(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 40)}, new Item(23904, 1), null),
	VIAL(new Item[]{new Item(ItemID.CORRUPTED_SHARDS, 10)}, new Item(23839, 1), null),
	VIAL2(new Item[]{new Item(ItemID.CRYSTAL_SHARDS, 10)}, new Item(23839, 1), null),

	;

	private Item[] ingredients;
	private Item finishedProduct;
	private Item itemToUpgrade;

	SingingBowlRecipes(Item[] ingredients, Item finishedProduct, Item itemToUpgrade) {
		this.ingredients = ingredients;
		this.finishedProduct = finishedProduct;
		this.itemToUpgrade = itemToUpgrade;
	}

	public Item[] getIngredients() {
		return ingredients;
	}

	public Item getItemToMake() {
		return finishedProduct;
	}

	public Item getItemToUpgrade() {
		return itemToUpgrade;
	}

	public static final SingingBowlRecipes[] VALUES = values();
}
