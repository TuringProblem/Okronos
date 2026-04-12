package io.ruin.model.item.containers.bank;

import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.SecondaryGroup;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.WidgetInfo;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.OptionAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.handlers.EquipmentStats;
import io.ruin.model.inter.handlers.TabInventory;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;
import io.ruin.model.item.ItemContainerG;
import io.ruin.model.item.actions.impl.storage.EssencePouch;
import io.ruin.model.item.actions.impl.storage.HerbSack;
import io.ruin.model.item.actions.impl.storage.LootingBag;
import io.ruin.model.item.actions.impl.storage.RunePouch;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.network.PacketSender;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static io.ruin.cache.ItemID.HERB_SACK;
import static io.ruin.model.entity.player.SecondaryGroup.*;
import static io.ruin.model.inter.AccessMasks.*;

@Slf4j
public class Bank extends ItemContainerG<BankItem> {

	public static final int BLANK_ID = -1, FILLER_ID = 20594;

	public static boolean hasItemAmountLimit(Player player, int itemId, int amount, Consumer<Player> failConsumer) {
		int inInv = player.getInventory().getAmount(itemId);
		long total = ((long) amount + (long) inInv);
		boolean limit = total < Integer.MAX_VALUE;
		if (limit) {
			failConsumer.accept(player);
			return true;
		} else {
			return false;
		}
	}

	static void unequip(Player player, int slot) {
		if (player.isLocked())
			return;
		Item item = player.getEquipment().get(slot);
		player.getEquipment().unequip(item);
	}

	transient boolean asNote;
	transient boolean sortSkip, sortRequired;

	public void open() {
		boolean presetsLocked = true;
		asNote = false;
		if (presetsLocked)
			player.getPacketSender().setHidden(12, 120, true);
		else {
			player.getPacketSender().setHidden(12, 120, false);
		}
		if (player.getGameMode().isUltimateIronman()) {
			player.sendMessage("Ultimate ironmen cannot access the bank.");
			return;
		}
		// if(player.getBankPin().requiresVerification(p -> open()))
		// return;
		player.getPacketSender().sendClientScript(917, "ii", -1, -2);
		player.setInterfaceUnderlay(-1, -2);
		player.openInterface(ToplevelComponent.MAINMODAL, Interface.BANK);
		player.openInterface(ToplevelComponent.SIDEMODAL, Interface.BANK_INVENTORY);
		PacketSender ps = player.getPacketSender();
		ps.sendIfEvents(WidgetInfo.BANK_ITEM_CONTAINER, 0, getItems().length, ClickOp1, ClickOp2, ClickOp3, ClickOp4,
				ClickOp5, ClickOp6,
				ClickOp7, ClickOp8, ClickOp9, ClickOp10, DragDepth2, DragDepth3, DragDepth6, DragDepth7, DragTargetable);
		ps.sendIfEvents(WidgetInfo.BANK_ITEM_CONTAINER, 1209, 1217, ClickOp1);
		ps.sendIfEvents(WidgetInfo.BANK_ITEM_CONTAINER, 1218, 1227, DragTargetable);
		ps.sendIfEvents(WidgetInfo.BANK_TAB_CONTAINER, 10, 10, ClickOp1, ClickOp7, DragTargetable);
		ps.sendIfEvents(WidgetInfo.BANK_TAB_CONTAINER, 11, 19, ClickOp1, ClickOp6, ClickOp7, DragDepth1,
				/* DragDepth3, DragDepth5, DragDepth7, */DragTargetable);
		ps.sendIfEvents(Interface.BANK_INVENTORY, 3, 0, 27, ClickOp1, ClickOp2, ClickOp3, ClickOp4, ClickOp5, ClickOp6,
				ClickOp7, ClickOp8, ClickOp9, ClickOp10, DragDepth1, DragTargetable);
		ps.sendIfEvents(Interface.BANK_INVENTORY, 19, 0, 27, ClickOp1, ClickOp2, ClickOp3, ClickOp4, ClickOp10);
		ps.sendIfEvents(Interface.BANK_INVENTORY, 13, 0, 27, ClickOp1, ClickOp2, ClickOp3, ClickOp4, ClickOp1);
		ps.sendIfEvents(Interface.BANK_INVENTORY, 4, 0, 27, ClickOp1, ClickOp10, DragDepth1, DragTargetable);
		ps.sendIfEvents(WidgetInfo.BANK_INCINERATOR_CONFIRM, 1, getItems().length + 1, ClickOp1);
		ps.sendIfEvents(Interface.BANK, 49, 0, 3, ClickOp1); // Tab display:
		ps.sendIfEvents(15, 13, 0, 27, ClickOp1);

		player.getPacketSender().sendString(Interface.BANK, 7, String.valueOf(getItems().length)); // used Slots
		player.getPacketSender().sendString(Interface.BANK, 9, String.valueOf(getBankCapacity(player))); // total Slots

		// this shit is actually per bit basis, each bit represents whether
		// the slot is 'active', 28 slots, 28 bits
		// bits are checked backwards
		int value = 0b00001111_11111111_11111111_11111111;
		VarPlayerRepository.varp(262, true).set(player, value);

		sendWornItemBonuses();
		sendAll = true;
	}

	// Extends the capacity by `amount`
	public void extendCapacity(Player player, int amount) {
		var currentExtra = player.totalExtraBankStorage;
		player.totalExtraBankStorage = currentExtra + amount;
		this.syncCapacity(player);
	}

	// When capacity changes, resize the container and send updates
	public void syncCapacity(Player player) {
		var capacity = this.getBankCapacity(player);
		this.resize(capacity);
		this.sendAll = true;
	}

	public int getBankCapacity(Player player) {
		var totalSlots = 800;
		totalSlots += player.totalExtraBankStorage;
		if (player.getSecondaryGroup().equalToOrGreaterThan(ELITE_DONATOR))
			totalSlots += 10;
		if (player.getSecondaryGroup().equalToOrGreaterThan(NOBLE_DONATOR))
			totalSlots += 20;
		if (player.getSecondaryGroup().equalToOrGreaterThan(GOLD_DONATOR))
			totalSlots += 20;
		if (player.getSecondaryGroup().equalToOrGreaterThan(PLATINUM_DONATOR))
			totalSlots += 30;
		if (player.getSecondaryGroup().equalToOrGreaterThan(LEGENDARY_DONATOR))
			totalSlots += 30;
		if (player.getSecondaryGroup().equalToOrGreaterThan(SUPREME_DONATOR))
			totalSlots += 40;
		return totalSlots;
	}

	public void openDepositBox() {
		if (player.getGameMode().isUltimateIronman()) {
			player.sendMessage("Ultimate ironmen cannot access the bank.");
			return;
		}
		player.getPacketSender().sendIfEvents(161, 47, -1, -1, 0);
		player.getPacketSender().sendIfEvents(161, 48, -1, -1, 0);
		player.getPacketSender().sendClientScript(915, "i", 3);
		player.getPacketSender().sendClientScript(917, "ii", -1, -1);
		player.openInterface(ToplevelComponent.MAINMODAL, Interface.DEPOSIT_BOX);
		player.openInterface(ToplevelComponent.SIDEMODAL, 78);
		player.getPacketSender().sendIfEvents(192, 2, 0, 27, 1180734);
	}

	@Override
	public int add(int id, int amount, Map<String, String> attributes) {
		if (player.getGameMode().isUltimateIronman()) {
			return 0;
		}
		ObjType def = ObjType.get(id);
		if (def == null || def.isPlaceholder())
			return -1;
		int tab = VarPlayerRepository.BANK_TAB.get(player);
		int freeSlot = -1;
		BankItem blankItem = null;
		if (getAmount(id) + (long) amount > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE - getAmount(id);
		}
		int hash = AttributeExtensions.hashAttributes(attributes);
		for (int slot = 0; slot < items.length; slot++) {
			BankItem item = items[slot];
			if (item == null) {
				if (freeSlot == -1)
					freeSlot = slot;
				continue;
			}
			if (item.getId() == BLANK_ID) {
				if (item.tab == tab && blankItem == null)
					blankItem = item;
				continue;
			}
			if (hash == 0) {
				if (item.getId() == id && !item.hasAttributes()) {
					item.incrementAmount(amount);
					return amount;
				}
				if (item.getId() == def.placeholderMainId) {
					item.setId(id);
					item.setAmount(amount);
					return amount;
				}
			}
		}
		if (blankItem != null) {
			blankItem.setId(id);
			blankItem.setAmount(amount);
			blankItem.putAttributes(attributes);
			return amount;
		}
		if (freeSlot != -1) {
			set(freeSlot, new BankItem(id, amount, tab, attributes));
			if (tab != 0)
				sort();
			return amount;
		}
		return 0;
	}

	/**
	 * Depositing
	 */

	public void deposit(ItemContainer container, boolean message, boolean forced) {
		if (player.isVisibleInterface(Interface.BANK) || player.isVisibleInterface(Interface.DEPOSIT_BOX) || forced) {
			if (container.isEmpty()) {
				if (message)
					player.sendMessage("You have nothing to deposit.");
				return;
			}
			sortAfter(() -> {
				boolean deposited = false;
				for (Item item : container.getItems()) {
					if (item != null && deposit(item, item.getAmount(), true, forced) > 0)
						deposited = true;
				}
				if (!deposited && message)
					player.sendMessage("Your bank cannot hold your items.");
			});
		}
	}

	public int deposit(Item item, int amount, boolean message, boolean forced) {
		if (player.isVisibleInterface(Interface.BANK) || player.isVisibleInterface(Interface.DEPOSIT_BOX) || forced) {
			ObjType def = item.getDef();
			int moved = item.move(def.isNote() ? def.notedId : def.id, amount, this);
			if (moved == 0 && message)
				player.sendMessage("You don't have enough space in your bank account.");
			return moved;
		}
		return 0;
	}

	public int deposit(int itemId, int amount) {
		return add(ObjType.unnotedId(itemId), amount);
	}

	public int deposit(int itemId, int amount, boolean message) {
		return add(ObjType.unnotedId(itemId), amount);
	}

	public int deposit(Item item, int amount, boolean message) {
		final ObjType def = item.getDef();
		if (def == null) {
			return 0;
		}

		if (item.getId() == 26651) {
			return 0;
		}

		int moved = item.move(def.unnotedId(), amount, this);
		if (moved == 0 && message)
			player.sendMessage("You don't have enough space in your bank account.");
		return moved;
	}

	public void deposit(ItemContainer container, boolean message) {
		if (container.isEmpty()) {
			if (message)
				player.sendMessage("You have nothing to deposit.");
			return;
		}
		sortAfter(() -> {
			boolean deposited = false;
			for (Item item : container.getItems()) {
				if (item != null && deposit(item, item.getAmount(), false) > 0)
					deposited = true;
			}
			if (!deposited && message)
				player.sendMessage("Your bank cannot hold your items.");
		});
	}

	public void removeBlankItems() {
		int removed = 0;
		for (BankItem item : items) {
			if (item == null)
				break;
			if (item.getId() == BLANK_ID) {
				set(item.getSlot(), null);
				removed++;
			}
		}
		if (removed > 0)
			sort();
	}

	public boolean toPlaceholder(BankItem item) {
		ObjType def = item.getDef();
		if (allowPlaceHolder(def)) {
			item.setId(def.placeholderMainId);
			item.setAmount(1);
			return true;
		}
		return false;
	}

	/**
	 * Sort
	 */

	public void sort() {
		if (sortSkip)
			sortRequired = true;
		else
			doSort();
	}

	public int remove(int id, int amount, Map<String, String> attributes) {
		return remove(id, amount, false, attributes);
	}

	@Override
	protected BankItem newItem(int id, int amount, Map<String, String> attributes) {
		return new BankItem(id, amount, 0, attributes);
	}

	@Override
	protected BankItem[] newArray(int size) {
		return new BankItem[size];
	}

	/**
	 * Withdrawing
	 */
	void withdraw(BankItem item, int amount) {
		int remaining = player.getInventory().getCapacityFor(item.getId());
		if (asNote) {
			if (amount >= Integer.MAX_VALUE)
				amount = Integer.MAX_VALUE - item.getAmount();
			// else
			// amount = item.getAmount();
		} else if (amount > remaining) {
			amount = remaining;
		}

		if (player.getInventory().getAmount(item.getId()) >= Integer.MAX_VALUE) {
			player.sendMessage("Your inventory is full of that item, please bank and try again.");
			return;
		}

		int amountInInv = player.getInventory().getAmount(item.getId());
		long total = ((long) amount + (long) amountInInv);

		if (total > Integer.MAX_VALUE) {
			amount -= amountInInv;
		}

		if (item.getId() == BLANK_ID) {
			return;
		}

		if (item.getId() == FILLER_ID) {
			if (amount == -1)
				setBlank(item);
			else
				clearFillers();
			return;
		}

		ObjType def = item.getDef();
		if (def.isPlaceholder()) {
			setBlank(item);
			return;
		}

		if (amount == -1) {
			amount = item.getAmount();
		}

		int itemId = item.getId();
		if (asNote) {
			if (def.notedId != -1) {
				itemId = def.notedId;
			} else {
				player.sendMessage("This item cannot be withdrawn as a note.");
			}
		}

		int moved = item.getDef().bankWithdrawListener(player, item, amount);
		moved += item.move(itemId, amount - moved, player.getInventory());

		if (moved <= 0) {
			player.sendMessage("You don't have enough inventory space.");
			return;
		}

		if (item.getAmount() > 0) {
			/* some of this item still remains in your bank */
			return;
		}

		var keepPlaceholder = VarPlayerRepository.BANK_ALWAYS_PLACEHOLDERS.get(player) == 1;
		if (item.placehold) {
			keepPlaceholder = true;
		}

		if (amount == -1) {
			keepPlaceholder = true;
		}

		if (keepPlaceholder && allowPlaceHolder(def)) {
			item.setId(def.placeholderMainId);
			item.setAmount(1);
			set(item.getSlot(), item);
			return;
		}

		setBlank(item);
	}

	/**
	 * Blank items
	 */

	private void setBlank(BankItem item) {
		boolean removed = item.getContainer() == null;
		BankItem prev = getSafe(item.getSlot() - 1);
		if (prev == null || prev.tab != item.tab) {
			/**
			 * First item in tab
			 */
			if (!removed)
				set(item.getSlot(), null);
			for (int slot = item.getSlot() + 1; slot < items.length; slot++) {
				BankItem next = items[slot];
				if (next == null || next.tab != item.tab || next.getId() != BLANK_ID)
					break;
				set(slot, null);
			}
			sort();
			return;
		}
		BankItem next = getSafe(item.getSlot() + 1);
		if (next == null || next.tab != item.tab) {
			/**
			 * Last item in tab
			 */
			if (!removed)
				set(item.getSlot(), null);
			for (int slot = item.getSlot() - 1; slot >= 0; slot--) {
				prev = items[slot];
				if (prev == null || prev.tab != item.tab || prev.getId() != BLANK_ID)
					break;
				set(slot, null);
			}
			sort();
			return;
		}
		item.clearAttributes();
		item.toBlank();
		if (removed)
			set(item.getSlot(), item);
	}

	private boolean allowPlaceHolder(ObjType def) {
		if (!def.hasPlaceholder()) {
			player.sendMessage("This item cannot leave a placeholder.");
			return false;
		}
		return findItem(def.placeholderMainId) == null;
	}

	// Mark an item to hold the place (slot) when all of it gets withdrawn.
	public void placehold(BankItem item) {
		item.placehold = true;
	}

	// Releases the held place (slot) of a placeholder.
	public void placeholderRelease(BankItem item) {
		sortAfter(() -> {
			for (var bItem : items) {
				if (bItem == null || bItem.getId() != item.getId()) {
					continue;
				}
				bItem.placehold = false;
				setBlank(item);
			}
		});

	}

	void releasePlaceholders() {
		sortAfter(() -> {
			int released = 0;
			for (BankItem item : items) {
				if (item == null || item.getId() == BLANK_ID || !item.getDef().isPlaceholder()) {
					continue;
				}
				setBlank(item);
				released++;
			}
			if (released == 0)
				player.sendMessage("You don't have any placeholders to release.");
			else if (released == 1)
				player.sendMessage("1 placeholder has been released.");
			else
				player.sendMessage(released + " placeholders have been released.");
		});
	}

	/**
	 * Fillers
	 */

	void fillBank() {
		sortAfter(() -> {
			int filled = 0;
			boolean hadFiller = false;
			int tab = VarPlayerRepository.BANK_TAB.get(player);
			for (int slot = items.length - 1; slot >= 0; slot--) {
				BankItem item = items[slot];
				if (item == null) {
					filled++;
					item = new BankItem(FILLER_ID, 1, tab, null);
					item.sortSlot = 65535;
					set(slot, item);
					sort();
				} else if (item.getId() == FILLER_ID) {
					hadFiller = true;
				} else if (item.getId() == BLANK_ID) {
					filled++;
					item.setId(FILLER_ID);
					item.setAmount(1);
					if (item.tab != tab || !hadFiller) {
						item.tab = tab;
						item.sortSlot = 65535;
						sort();
					}
				}
			}
			if (filled == 0)
				player.sendMessage("Your bank is already full.");
			else if (filled == 1)
				player.sendMessage("You fill up your bank with a bank filler.");
			else
				player.sendMessage("You fill up your bank with " + filled + " bank fillers.");
		});
	}

	private void clearFillers() {
		sortAfter(() -> {
			for (BankItem item : items) {
				if (item == null || item.getId() != FILLER_ID)
					continue;
				setBlank(item);
			}
		});
	}

	/**
	 * Incinerate
	 */

	void incinerate(int slot, int itemId) {
		BankItem item = get(slot - 1, itemId);
		if (item == null)
			return;
		setBlank(item);
	}

	/**
	 * Rearranging
	 */

	void rearrange(int fromSlot, int toSlot) {
		if (fromSlot == toSlot)
			return;
		BankItem fromItem = getSafe(fromSlot);
		if (fromItem == null)
			return;
		BankItem toItem = getSafe(toSlot);
		if (toItem == null)
			return;
		if (VarPlayerRepository.BANK_INSERT_MODE.get(player) == 1) {
			if (fromItem.getSlot() > toItem.getSlot() || toItem.tab == 0 && fromItem.tab != 0) {
				/* insert left */
				fromItem.sortSlot = toItem.getSlot();
				toItem.sortSlot = toItem.getSlot() + 1;
			} else {
				/* insert right */
				fromItem.sortSlot = toItem.getSlot();
				toItem.sortSlot = toItem.getSlot() - 1;
			}
			fromItem.tab = toItem.tab;
			sort();
		} else {
			int fromTab = fromItem.tab;
			fromItem.tab = toItem.tab;
			toItem.tab = fromTab;
			set(fromSlot, toItem);
			set(toSlot, fromItem);
		}
	}

	/**
	 * Tabs
	 */

	private void setTab(int newTab) {
		VarPlayerRepository.BANK_TAB.set(player, newTab);
		player.getPacketSender().sendClientScript(101, "i", 11);
	}

	void selectTab(int option, int slot) {
		int tab = slot - 10;
		if (tab < 0 || tab > 9)
			return;
		VarPlayerRepository.BANK_TAB_DISPLAY.set(player, slot);
		if (option == 1)
			setTab(tab);
		else if (option == 6) // todo fix this action button and add collapse placeholders
			collapseTab(tab);
	}

	void swapTabs(int fromSlot, int toSlot) {
		if (fromSlot == toSlot)
			return;
		int fromTab = fromSlot - 10;
		if (fromTab < 0 || fromTab > 9)
			return;
		int toTab = toSlot - 10;
		if (toTab < 0 || toTab > 9)
			return;
		for (BankItem item : items) {
			if (item == null)
				break;
			if (item.tab == fromTab)
				item.tab = toTab;
			else if (item.tab == toTab)
				item.tab = fromTab;
		}
		sort();
	}

	void changeTab(int fromSlot, int fromItemId, int toSlot) {
		BankItem item = get(fromSlot, fromItemId);
		if (item == null)
			return;
		int newTab = toSlot - 10;
		if (newTab < 0 || newTab > 9)
			return;
		int oldTab = item.tab;
		if (oldTab == newTab)
			return;
		for (int slot = getStartSlot(newTab); slot < items.length; slot++) {
			BankItem bankItem = items[slot];
			if (bankItem == null || bankItem.tab != newTab) {
				item.tab = newTab;
				item.sortSlot = Short.MAX_VALUE;
				sort();
				return;
			}
			if (bankItem.getId() == BLANK_ID) {
				set(item.getSlot(), null);
				bankItem.setId(item.getId());
				bankItem.setAmount(item.getAmount());
				sort();
				return;
			}
		}
	}

	private void collapseTab(int tab) {
		if (tab < 1 || tab > 9)
			return;
		for (int slot = getStartSlot(tab); slot < items.length; slot++) {
			BankItem bankItem = items[slot];
			if (bankItem == null)
				break;
			if (bankItem.tab == 0) {
				if (bankItem.getId() == BLANK_ID)
					set(slot, null);
			} else if (bankItem.tab == tab) {
				if (bankItem.getId() == BLANK_ID) {
					set(slot, null);
				} else {
					bankItem.tab = 0;
					bankItem.sortSlot = Short.MAX_VALUE + slot;
				}
			}
		}
		setTab(0);
		sort();
	}

	private int getStartSlot(int tab) {
		int tabStartSlot = 0;
		for (int t = 1; t < tab; t++) {
			int size = VarPlayerRepository.BANK_TAB_SIZES[t - 1].get(player);
			tabStartSlot += size;
		}
		return tabStartSlot;
	}

	private void sortAfter(Runnable runnable) {
		sortSkip = true;
		runnable.run();
		sortSkip = false;
		if (sortRequired) {
			sortRequired = false;
			doSort();
		}
	}

	private void doSort() {
		ArrayList<BankItem> list = new ArrayList<>();
		for (int slot = 0; slot < items.length; slot++) {
			BankItem item = items[slot];
			if (item != null) {
				set(slot, null);
				list.add(item);
			}
		}
		list.sort((item1, item2) -> {
			int tab1 = item1.tab == 0 ? 10 : item1.tab;
			int tab2 = item2.tab == 0 ? 10 : item2.tab;
			if (tab1 == tab2) {
				int slot1 = item1.sortSlot == -1 ? item1.getSlot() : item1.sortSlot;
				int slot2 = item2.sortSlot == -1 ? item2.getSlot() : item2.sortSlot;
				return Integer.compare(slot1, slot2);
			}
			return Integer.compare(tab1, tab2);
		});
		int slot = 0;
		int[] tabSizes = new int[9];
		int lastTab = -1, tabOffset = 0;
		for (BankItem item : list) {
			if (lastTab != item.tab) {
				lastTab = item.tab;
				tabOffset++;
			}
			if (item.tab != 0) {
				item.tab = tabOffset;
				tabSizes[item.tab - 1]++;
			}
			item.sortSlot = -1;
			set(slot++, item);
		}
		for (int i = 0; i < tabSizes.length; i++)
			VarPlayerRepository.BANK_TAB_SIZES[i].set(player, tabSizes[i]);
		sendAll = true;
	}

	private void sendWornItemBonuses() {
		EquipmentStats.update(player, Interface.BANK, 98);
	}

	public void addCoinsOrPlatTokens(long amount) {
		if (amount <= Integer.MAX_VALUE && hasRoomFor(995, (int) amount)) {
			add(995, (int) amount);
		} else {
			int tokenAmount = (int) (amount / 1000);
			var coinsRemainder = (int) (amount % 1000);
			add(13204, tokenAmount);
			if (coinsRemainder != 0) {
				add(995, 0);
			}
		}
	}

	public void addCoinsOrPlatTokens(BigInteger amount) {
		if (amount.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) <= 0 && hasRoomFor(995, amount.intValueExact())) {
			add(995, amount.intValueExact());
		} else {
			var tokenAmount = (amount.divide(BigInteger.valueOf(1000)));
			add(13204, tokenAmount.intValueExact());
			var coinsRemainder = (amount.remainder(BigInteger.valueOf(1000))).intValueExact();
			if (coinsRemainder != 0) {
				add(995, coinsRemainder);
			}
		}
	}
}
