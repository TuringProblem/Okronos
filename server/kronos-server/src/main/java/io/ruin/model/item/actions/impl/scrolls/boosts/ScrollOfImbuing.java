package io.ruin.model.item.actions.impl.scrolls.boosts;

import io.ruin.cache.ItemID;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;

import java.util.HashMap;
import java.util.Map;

public class ScrollOfImbuing {


	enum IMBUE {
		SALVE_AMULET_E(10588, 12018),
		SALVE_AMULET(4081, 12017),
		BERSEKER_RING(ItemID.BERSERKER_RING, ItemID.BERSERKER_RING_I),
		ARCHER_RING(ItemID.ARCHERS_RING, ItemID.ARCHERS_RING_I),
		SEERS_RING(ItemID.SEERS_RING, ItemID.SEERS_RING_I),
		WARRIOR_RING(ItemID.WARRIOR_RING, ItemID.WARRIOR_RING_I),
		TREASONOUS_RING(ItemID.TREASONOUS_RING, ItemID.TREASONOUS_RING_I),
		TYRANNICAL_RING(ItemID.TYRANNICAL_RING, ItemID.TYRANNICAL_RING_I),
		RING_OF_THE_GODS(ItemID.RING_OF_THE_GODS, ItemID.RING_OF_THE_GODS_I),
		RING_OF_SUFFERING(ItemID.RING_OF_SUFFERING, ItemID.RING_OF_SUFFERING_I),
		RING_OF_SUFFERING_R(ItemID.RING_OF_SUFFERING_R, ItemID.RING_OF_SUFFERING_RI),
		BLACK_MASK(ItemID.BLACK_MASK, ItemID.BLACK_MASK_I),
		BLACK_MASK_10(ItemID.BLACK_MASK_10, ItemID.BLACK_MASK_I),
		SLAYER_HELMET(ItemID.SLAYER_HELMET, ItemID.SLAYER_HELMET_I),
		MAGIC_SHORTBOW(ItemID.MAGIC_SHORTBOW, ItemID.MAGIC_SHORTBOW_I),
		GRANITE_RING(ItemID.GRANITE_RING, ItemID.GRANITE_RING_I);
		private final int originalId, imbuedId;

		IMBUE(int originalId, int imbueId) {
			this.originalId = originalId;
			this.imbuedId = imbueId;
		}

		public static final IMBUE[] VALUES = values();
	}

	public static void register() {
		for (IMBUE imbue : IMBUE.VALUES) {
			ItemAction.registerInventory(imbue.imbuedId, "uncharge", (player, item) -> {
					if (AttributeExtensions.getCharges(item) > 0) {
						player.sendMessage("You must remove the charges first.");
						return;
					}
					player.dialogue(new YesNoDialogue("Are you sure you want to uncharge this item?", "Uncharge", item, () -> {
						Map<String, String> attributes = new HashMap<>();
						attributes.putAll(item.attributes);
						Item primary = new Item(imbue.originalId);
						primary.attributes.putAll(attributes);
						item.remove();
						player.getInventory().add(primary);
						player.sendMessage("You have uncharged your " + new Item(imbue.imbuedId).getDef().name + ".");
					}));
				}
			);
			ItemItemAction.register(imbue.originalId, 26706, (player, primary, secondary) -> {
				if (AttributeExtensions.getCharges(primary) > 0) {
					player.sendMessage("You can't use this item on a charged item.");
					return;
				}
				if (AttributeExtensions.getCharges(secondary) > 0) {
					player.sendMessage("You can't use this item on a charged item.");
					return;
				}
				if (primary.getId() != imbue.originalId && secondary.getId() != imbue.originalId)
					return;
				if (primary.getId() != 26706 && secondary.getId() != 26706) {
					return;
				}
				Item imbuedItem = new Item(imbue.imbuedId);
				Map<String, String> attributes = new HashMap<>();
				if (primary.getId() != 26706) {
					attributes.putAll(primary.attributes);
				}
				if (secondary.getId() != 26706) {
					attributes.putAll(secondary.attributes);
				}
				if (primary.getId() == 26706) {
					primary.setAmount(primary.getAmount() - 1);
					if (primary.getAmount() == 0)
						primary.remove();
					secondary.remove();
				}
				if (secondary.getId() == 26706) {
					secondary.setAmount(secondary.getAmount() - 1);
					if (secondary.getAmount() == 0)
						secondary.remove();
					primary.remove();
				}
				imbuedItem.attributes.putAll(attributes);
				player.getInventory().add(imbuedItem.getId(), 1);
				player.sendMessage("You have imbued your " + new Item(imbue.originalId).getDef().name + ".");
			});
			ItemItemAction.register(26706, imbue.originalId, (player, primary, secondary) -> {
				if (AttributeExtensions.getCharges(primary) > 0) {
					player.sendMessage("You can't use this item on a charged item.");
					return;
				}
				if (AttributeExtensions.getCharges(secondary) > 0) {
					player.sendMessage("You can't use this item on a charged item.");
					return;
				}
				if (primary.getId() != imbue.originalId && secondary.getId() != imbue.originalId)
					return;
				if (primary.getId() != 26706 && secondary.getId() != 26706) {
					player.sendMessage("You already have an imbued " + primary.getDef().name + ".");
					return;
				}
				Item imbuedItem = new Item(imbue.imbuedId);
				Map<String, String> attributes = new HashMap<>();
				if (primary.getId() != 26706) {
					attributes.putAll(primary.attributes);
				}
				if (secondary.getId() != 26706) {
					attributes.putAll(secondary.attributes);
				}
				if (primary.getId() == 26706) {
					primary.setAmount(primary.getAmount() - 1);
					if (primary.getAmount() == 0)
						primary.remove();
					secondary.remove();
				}
				if (secondary.getId() == 26706) {
					secondary.setAmount(secondary.getAmount() - 1);
					if (secondary.getAmount() == 0)
						secondary.remove();
					primary.remove();
				}
				imbuedItem.attributes.putAll(attributes);
				player.getInventory().add(imbuedItem);
				player.sendMessage("You have imbued your " + new Item(imbue.originalId).getDef().name + ".");
			});
		}
	}
}
