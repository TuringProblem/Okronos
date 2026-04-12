package io.ruin.model.item.actions.impl.storage;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.*;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.var.VarPlayerRepository;

import static io.ruin.model.inter.AccessMasks.*;

public class DivineRunePouch extends ItemContainer {

	public static final int RUNE_POUCH = 27281;

	public static void register() {
		ItemAction.registerInventory(RUNE_POUCH, "open", (player, item) -> player.DivinerunePouch.open());
		ItemAction.registerInventory(RUNE_POUCH, "empty", (player, item) -> player.DivinerunePouch.empty(false));
		ItemAction.registerInventory(RUNE_POUCH, "destroy", DivineRunePouch::destroy);
		for (Rune rune : Rune.values())
			ItemItemAction.register(RUNE_POUCH, rune.getId(),
					(player, pouchItem, runeItem) -> player.DivinerunePouch.deposit(runeItem, runeItem.getAmount()));
		InterfaceHandler.register(1107, h -> {
			h.actions[8] = (DefaultAction) (p, option, slot, itemId) -> {
				Item item = p.getInventory().get(slot, itemId);
				if (item == null)
					return;
				if (option == 1)
					p.DivinerunePouch.deposit(item, 1);
				else if (option == 2)
					p.DivinerunePouch.deposit(item, 5);
				else if (option == 3)
					p.DivinerunePouch.deposit(item, item.getAmount());
				else if (option == 4)
					p.integerInput("Enter amount:", amt -> p.DivinerunePouch.deposit(item, amt));
				else
					item.examine(p);
			};
			h.actions[4] = new InterfaceAction() {

				@Override
				public void handleClick(Player p, int option, int slot, int itemId) {
					Item item = p.DivinerunePouch.get(slot, itemId);
					if (item == null)
						return;
					if (option == 1)
						p.DivinerunePouch.withdraw(item, 1);
					else if (option == 2)
						p.DivinerunePouch.withdraw(item, 5);
					else if (option == 3)
						p.DivinerunePouch.withdraw(item, item.getAmount());
					else if (option == 4)
						p.integerInput("Enter amount:", amt -> p.DivinerunePouch.withdraw(item, amt));
					else
						item.examine(p);
				}

				@Override
				public void handleDrag(Player player, int fromSlot, int fromItemId, int toInterfaceId, int toChildId,
						int toSlot, int toItemId) {
					DivineRunePouch pouch = player.DivinerunePouch;

					Item from = pouch.get(fromSlot);
					Item to = pouch.get(toSlot);

					if (from == null || to == null) {
						return;
					}

					pouch.set(fromSlot, to);
					pouch.set(toSlot, from);
				}

			};
		});
	}

	public static void destroy(Player player, Item item) {
		player.dialogue(
				new YesNoDialogue("Are you sure you want to do this?",
						"This Rune Pouch will be destroyed and all stored items will be dropped.", item, () -> {
							item.remove();
							player.sendMessage("You destroy your Rune Pouch!");
							for (Item i : player.DivinerunePouch.getItems()) {
								if (i != null) {
									new GroundItem(i).owner(player).droppedBy(player).position(player.getPosition()).spawnPrivate();
									i.remove();
								}
							}
						}));
	}

	public void deposit(Item item, int amount) {
		if (amount <= 0)
			return;
		if (amount > item.getAmount())
			amount = item.getAmount();
		Rune rune = item.getDef().rune;
		if (rune == null) {
			player.sendMessage("You can't store this item in your pouch.");
			return;
		}
		int freeSlot = -1;
		Item stack = null;
		for (int i = 0; i < items.length; i++) {
			Item stored = items[i];
			if (stored == null) {
				if (freeSlot == -1)
					freeSlot = i;
				continue;
			}
			if (item.getId() == stored.getId()) {
				stack = stored;
				break;
			}
		}
		if (stack != null) {
			amount = Math.min(amount, 30000 - stack.getAmount());
			stack.incrementAmount(amount);
			item.incrementAmount(-amount);
			return;
		}
		if (freeSlot != -1) {
			amount = Math.min(amount, 30000);
			set(freeSlot, new Item(item.getId(), amount));
			item.incrementAmount(-amount);
			return;
		}
		player.sendMessage("Not enough space in your pouch.");
	}

	public void empty(boolean bank) {
		var count = this.getCount();
		if (count == 0) {
			if (!bank)
				player.sendMessage("Your rune pouch is already empty.");
			return;
		}
		for (Item item : getItems()) {
			if (item != null)
				item.move(item.getId(), item.getAmount(), bank ? player.getBank() : player.getInventory());
		}
		if (count == 0) {
			if (!bank)
				player.sendMessage("You empty your rune pouch.");
			return;
		}
		player.sendMessage("Not enough space in your inventory.");
	}

	@Override
	public boolean sendUpdates() {
		if (updatedCount == 0) {
			return false;
		}

		if (updatedSlots[0]) {
			update(0, VarPlayerRepository.RUNE_POUCH_RUNE1, VarPlayerRepository.RUNE_POUCH_AMOUNT1);
		}

		if (updatedSlots[1]) {
			update(1, VarPlayerRepository.RUNE_POUCH_RUNE2, VarPlayerRepository.RUNE_POUCH_AMOUNT2);
		}

		if (updatedSlots[2]) {
			update(2, VarPlayerRepository.RUNE_POUCH_RUNE3, VarPlayerRepository.RUNE_POUCH_AMOUNT3);
		}

		if (updatedSlots[3]) {
			update(3, VarPlayerRepository.RUNE_POUCH_RUNE4, VarPlayerRepository.RUNE_POUCH_AMOUNT4);
		}

		updatedCount = 0;
		return true;
	}

	private void open() {
		if (player.getCombat().isDefending(6) || player.getCombat().isAttacking(6)) {
			player.sendFilteredMessage("You can't do this in combat.");
			return;
		}
		if (player.isVisibleInterface(1107)) {
			VarPlayerRepository.VARP_261.set(player, 4);
			player.getPacketSender().sendClientScript(2524, -1, -1);
			player.closeInterface(ToplevelComponent.INVENTORY_TAB_AREA);
		}

		player.getPacketSender().sendIfEvents(1107, 4, 0, 4, 1054 | 1 << 17 | 1 << 20);
		player.getPacketSender().sendIfEvents(1107, 8, 0, 27, 1054);
		player.openInterface(ToplevelComponent.MAINMODAL, 1107);
		player.getPacketSender().sendItems(982);

	}

	private void withdraw(Item item, int amount) {
		if (item.move(item.getId(), amount, player.getInventory()) > 0)
			return;
		player.sendMessage("Not enough space in your inventory.");
	}

	private void update(int slot, VarPlayerRepository typeConfig, VarPlayerRepository amountConfig) {
		Item item = items[slot];
		if (item == null) {
			typeConfig.set(player, 0);
			amountConfig.set(player, 0);
		} else {
			typeConfig.set(player, item.getDef().rune.ordinal() + 1);
			amountConfig.set(player, item.getAmount());
		}
		updatedSlots[slot] = false;
	}

}
