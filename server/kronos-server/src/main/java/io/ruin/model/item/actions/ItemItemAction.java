package io.ruin.model.item.actions;

import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.itemeffects.ItemEffects;
import io.ruin.model.item.containers.Equipment;

import java.util.ArrayList;

public interface ItemItemAction {

	void handle(Player player, Item primary, Item secondary);

	/**
	 * Default register method - For private use only.
	 */

	default void register(int id, boolean primary) {
		ObjType def = ObjType.get(id);
		ArrayList<ItemItemAction> list;
		if (primary) {
			if (def.primaryItemItemActions == null)
				def.primaryItemItemActions = new ArrayList<>();
			list = def.primaryItemItemActions;
		} else {
			if (def.secondaryItemItemActions == null)
				def.secondaryItemItemActions = new ArrayList<>();
			list = def.secondaryItemItemActions;
		}
		list.add(this);
		list.trimToSize(); //only okay because it's on startup ;)
	}

	/**
	 * Registers actions without a specific secondary item.
	 */

	static void register(int primaryId, ItemItemAction action) {
		ObjType def = ObjType.get(primaryId);
		if (def == null) {
			System.err.println("Error loading objtype " + primaryId);
			return;
		}
		def.defaultPrimaryItemItemAction = action;
	}

	/**
	 * Registers actions with a specific primary & secondary item.
	 */

	static void register(int primaryId, int secondaryId, ItemItemAction action) {
		action.register(primaryId, true);
		action.register(secondaryId, false);
	}

	/**
	 * Handle actions
	 */

	static void handleAction(Player player, Item from, Item to) {
		ObjType fromDef = from.getDef();
		ObjType toDef = to.getDef();
		ItemItemAction action;
		if ((action = match(fromDef.primaryItemItemActions, toDef.secondaryItemItemActions)) != null) {
			action.handle(player, from, to);
			return;
		}
		if ((action = match(toDef.primaryItemItemActions, fromDef.secondaryItemItemActions)) != null) {
			action.handle(player, to, from);
			return;
		}
		if ((action = fromDef.defaultPrimaryItemItemAction) != null) {
			action.handle(player, from, to);
			return;
		}
		if ((action = toDef.defaultPrimaryItemItemAction) != null) {
			action.handle(player, to, from);
			return;
		}
		for (ItemEffects effect : ItemEffects.VALUES) {

			if (to.getId() == effect.getSigilItem().getId() && from.getDef().equipSlot == Equipment.SLOT_WEAPON) {
				effect.useOnWeapon(player, from);
				return;
			}
			if (from.getId() == effect.getSigilItem().getId() && to.getDef().equipSlot == Equipment.SLOT_WEAPON) {
				effect.useOnWeapon(player, to);
				return;
			}
		}
		player.sendMessage("Nothing interesting happens.");
	}

	static ItemItemAction match(ArrayList<ItemItemAction> primaryActions, ArrayList<ItemItemAction> secondaryActions) {
		if (primaryActions == null || secondaryActions == null)
			return null;
		for (ItemItemAction primary : primaryActions) {
			for (ItemItemAction secondary : secondaryActions) {
				if (primary == secondary)
					return primary;
			}
		}
		return null;
	}

}
