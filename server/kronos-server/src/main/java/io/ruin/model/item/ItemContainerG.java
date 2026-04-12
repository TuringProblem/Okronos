package io.ruin.model.item;

import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.attributes.AttributeExtensions;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public abstract class ItemContainerG<I extends Item> {

	protected I[] items;

	protected transient int updatedCount;
	protected transient boolean[] updatedSlots;
	public transient boolean sendAll;
	public transient Player player;
	public transient int interfaceHash;
	public transient int containerId;
	public transient boolean forceStack;
	private transient int mirrorInterfaceHash, mirrorContainerId;

	public void init(Player player, int size, int parentId, int childId, int containerId, boolean forceStack) {
		this.resize(size);
		this.player = player;
		this.interfaceHash = parentId << 16 | childId;
		this.containerId = containerId;
		this.forceStack = forceStack;
		this.mirrorInterfaceHash = 0;
		this.mirrorContainerId = 0;
	}

	protected final void resize(int size) {
		if (this.items == null) {
			this.items = newArray(size);
		} else if (items.length != size) {
			I[] newItems = newArray(size);
			System.arraycopy(items, 0, newItems, 0, Math.min(items.length, newItems.length));
			this.items = newItems;
		}
		this.updatedSlots = new boolean[size];
		for (int slot = 0; slot < size; slot++) {
			Item item = items[slot];
			if (item != null) {
				item.setSlot(slot);
				item.setContainer(this);
			}
		}
	}

	public void init(int size, boolean forceStack) {
		init(null, size, -1, -1, -1, forceStack);
	}

	public void clear() {
		for (int slot = 0; slot < items.length; slot++)
			set(slot, null);
	}

	public boolean validateSlots(int... slots) {
		for (int slot : slots) {
			if (slot < 0 || slot >= items.length)
				return false;
		}
		return true;
	}

	public void set(int slot, I item) {
		Item pre = items[slot];
		if (item == null) {
			if (pre == null)
				return;
			pre.setContainer(null);
			items[slot] = null;
		} else {
			item.setSlot(slot);
			item.setContainer(this);
			items[slot] = item;
		}
		update(slot);
	}

	public I get(int slot) {
		return items[slot];
	}

	public I getSafe(int slot) {
		if (slot < 0 || slot >= items.length)
			return null;
		return items[slot];
	}

	public I get(int slot, int itemId) {
		I item = getSafe(slot);
		if (item == null || item.getId() != itemId)
			return null;
		return item;
	}

	public I[] getItems() {
		return items;
	}

	public int getId(int slot) {
		Item item = items[slot];
		return item == null ? -1 : item.getId();
	}

	public ObjType getDef(int slot) {
		I item = get(slot);
		return item == null ? null : item.getDef();
	}

	public int getFreeSlots() {
		return items.length - getCount();
	}

	public int getCount() {
		return occupiedSlots();
	}

	public int occupiedSlots() {
		final Item[] items = this.items;
		if (items == null) {
			return 0;
		}

		int count = 0;
		for (final Item item : items) {
			if (item != null) {
				count++;
			}
		}
		return count;
	}

	public boolean isEmpty() {
		return getCount() == 0;
	}

	public boolean isNotEmpty() {
		return !isEmpty();
	}

	public boolean isFull() {
		return getFreeSlots() == 0;
	}

	/**
	 * Adding
	 */

	public int add(I item) {
		return add(item.getId(), item.getAmount(), item.copyOfAttributes());
	}

	public int add(I item, boolean neverStack) {

		if (item.getAmount() <= 0) {
			System.err
					.println("Invalid add amount: " + item.getAmount() + " | " + new Throwable().getStackTrace()[1].toString());
			return 0;
		}
		ObjType def = ObjType.get(item.getId());
		if (def == null) {
			System.err.println(
					"Failed to add non-existing item: " + item.getId() + " | " + new Throwable().getStackTrace()[1].toString());
			return 0;
		}

		boolean stack = forceStack;
		int attributeHash = AttributeExtensions.hashAttributes(item.copyOfAttributes());
		boolean hasAttributes = attributeHash != 0;

		if (hasAttributes) {
			stack = false;
		}

		int freeSlot = freeSlot();
		if (freeSlot == -1) {
			/**
			 * No free slot
			 */
			return 0;
		}
		set(freeSlot, newItem(item.getId(), item.getAmount(), item.copyOfAttributes()));
		return item.getAmount();

	}

	public int add(int id) {
		return add(id, 1, null);
	}

	public int add(int id, int amount) {
		return add(id, amount, null);
	}

	public int add(int id, int amount, Map<String, String> attributes) {

		if (amount <= 0) {
			System.err.println("Invalid add amount: " + amount + " | " + new Throwable().getStackTrace()[1].toString());
			return 0;
		}

		ObjType def = ObjType.get(id);
		if (def == null) {
			System.err
					.println("Failed to add non-existing item: " + id + " | " + new Throwable().getStackTrace()[1].toString());
			return 0;
		}

		boolean stack = forceStack;
		int attributeHash = AttributeExtensions.hashAttributes(attributes);
		boolean hasAttributes = attributeHash != 0;

		if (hasAttributes) {
			stack = false;
		}

		if (stack) {
			Item stackItem = findItem(id);
			if (stackItem != null) {
				return stackItem.incrementAmount(amount);
			}
		}

		if (def.stackable && !hasAttributes) {
			/**
			 * Regular stackable item
			 */
			Item stackItem = findItem(id);
			if (stackItem != null) {
				return stackItem.incrementAmount(amount);
			}
			stack = true;
		}

		if ((amount == 1 && !hasAttributes) || stack) {
			int freeSlot = freeSlot();
			if (freeSlot == -1) {
				/**
				 * No free slot
				 */
				return 0;
			}
			set(freeSlot, newItem(id, amount, attributes));
			return amount;
		}

		int added = 0;
		while (amount-- > 0) { // move one at a time
			int freeSlot = freeSlot();
			if (freeSlot == -1) {
				/**
				 * No free slot
				 */
				break;
			}
			set(freeSlot, newItem(id, 1, attributes));
			added++;
		}
		return added;
	}

	/**
	 * Removing
	 */

	public int remove(int id, int amount) {
		if (amount <= 0) {
			return 0;
		}
		return remove(id, amount, false, null);
	}

	public int remove(int id, int amount, Map<String, String> attributes) {
		return remove(id, amount, false, attributes);
	}

	/**
	 * Removing
	 */
	public int remove(Item item) {
		return remove(item.getId(), item.getAmount(), false, item.copyOfAttributes());
	}

	public int remove(int id, int amount, boolean acceptNoted) {
		return remove(id, amount, acceptNoted, new HashMap<>());
	}

	public int remove(int id, int amount, boolean acceptNoted, Map<String, String> attributes) {
		if (amount <= 0) {
			System.err.println("Invalid remove amount: " + amount + " | " + new Throwable().getStackTrace()[1].toString());
			return 0;
		}
		ObjType definition = ObjType.get(id);
		if (definition == null) {
			System.err
					.println("Failed to remove non-existing item: " + id + " | " + new Throwable().getStackTrace()[1].toString());
			return 0;
		}
		int removed = 0;
		while (amount > 0) {
			Item stack = findItem(id, attributes);
			if (stack == null && acceptNoted && definition.notedId > 0) {
				stack = findItem(definition.notedId, attributes);
			}
			if (stack == null)
				break;
			int fromThisStack = Math.min(stack.getAmount(), amount);
			stack.incrementAmount(-fromThisStack);
			removed += fromThisStack;
			amount -= fromThisStack;
		}
		return removed;
	}

	public void mirror(int parentId, int childId, int containerId) {
		mirrorInterfaceHash = parentId << 16 | childId;
		mirrorContainerId = containerId;
	}

	/**
	 * Updating
	 */

	public void update(int slot) {
		if (updatedSlots[slot])
			return;
		updatedSlots[slot] = true;
		updatedCount++;
	}

	public boolean sendUpdates() {
		return sendUpdates(null);
	}

	public boolean sendUpdates(ItemContainerG mirrorContainer) {
		if (updatedCount == 0 && !sendAll)
			return false;
		if (sendAll) {
			if (player != null)
				player.getPacketSender().sendItems(-1, containerId, items, items.length);
			if (mirrorContainer != null)
				mirrorContainer.player.getPacketSender().sendItems(mirrorContainer.mirrorInterfaceHash,
						mirrorContainer.mirrorContainerId, items, items.length);
		} else {
			if (player != null)
				player.getPacketSender().updateItems(-1, containerId, items, updatedSlots, updatedCount);
			if (mirrorContainer != null)
				mirrorContainer.player.getPacketSender().updateItems(mirrorContainer.mirrorInterfaceHash,
						mirrorContainer.mirrorContainerId, items, updatedSlots, updatedCount);
		}
		for (int i = 0; i < updatedSlots.length; i++)
			updatedSlots[i] = false;
		updatedCount = 0;
		sendAll = false;
		return true;
	}

	public void send(Player player) {
		player.getPacketSender().sendItems(-1, containerId, items, items.length);
	}

	public void send(Player player, int interfaceHash, int containerId) {
		player.getPacketSender().sendItems(-1, containerId, items, items.length);
	}

	/**
	 * Misc
	 */

	public ArrayList<I> collectItems(int... ids) {
		ArrayList<I> list = new ArrayList<>(ids.length);
		for (I item : items) {
			if (item == null)
				continue;
			for (int id : ids) {
				if (item.getId() == id)
					list.add(item);
			}
		}
		return list.isEmpty() ? null : list;
	}

	public ArrayList<I> collectOneOfEach(int... ids) {
		ArrayList<I> list = new ArrayList<>(ids.length);
		i: for (int id : ids) {
			for (I item : items) {
				if (item != null && item.getId() == id) {
					list.add(item);
					continue i;
				}
			}
		}
		return list.size() != ids.length ? null : list;
	}

	public ArrayList<I> findItems(int id, int amount) {
		ArrayList<I> list = new ArrayList<>(amount);
		for (I item : items) {
			if (item == null || item.getId() != id)
				continue;
			list.add(item);
			if (list.size() >= amount)
				break;
		}
		return list.size() < amount ? null : list;
	}

	public ArrayList<I> findItems(Predicate<Item> predicate) {
		ArrayList<I> list = new ArrayList<>();
		for (I item : items) {
			if (item != null && predicate.test(item))
				list.add(item);
		}
		return list;
	}

	public I findItem(int id) {
		return findItem(id, false);
	}

	public I findItemExact(int id) {
		return findItemExact(id, false);
	}

	public I findItem(int id, int attributeHash) {
		return findItem(id, false, attributeHash);
	}

	public I findItem(int id, Map<String, String> attributes) {
		return findItem(id, false, AttributeExtensions.hashAttributes(attributes));
	}

	public I findItem(int id, boolean acceptNoted) {
		for (I item : items) {
			if (item != null && item.getAttributeHash() == 0 && (item.getId() == id
					|| (acceptNoted && ObjType.get(id).notedId != -1 && item.getId() == ObjType.get(id).notedId)))
				return item;
		}
		return null;
	}

	public I findItemExact(int id, boolean acceptNoted) {
		for (I item : items) {
			if (item != null && (item.getId() == id
					|| (acceptNoted && ObjType.get(id).notedId != -1 && item.getId() == ObjType.get(id).notedId)))
				return item;
		}
		return null;
	}

	public I findItem(int id, boolean acceptNoted, int attributeHash) {
		for (I item : items) {
			if (item != null && item.getAttributeHash() == attributeHash && (item.getId() == id
					|| (acceptNoted && ObjType.get(id).notedId != -1 && item.getId() == ObjType.get(id).notedId)))
				return item;
		}
		return null;
	}

	public I findFirst(int... ids) {
		for (I item : items) {
			if (item == null)
				continue;
			for (int id : ids) {
				if (id == item.getId())
					return item;
			}
		}
		return null;
	}

	public int freeSlot() {
		if (items == null) {
			return -1;
		}
		for (int slot = 0; slot < items.length; slot++) {
			if (items[slot] == null)
				return slot;
		}
		return -1;
	}

	public int getSlot(int id) {
		return getSlot(id, false);
	}

	public int getSlot(int id, boolean acceptNoted) {
		Item item = findItem(id, acceptNoted);
		return item == null ? -1 : item.getSlot();
	}

	public int getAmount(int id) {
		ObjType definition = ObjType.get(id);
		if (definition == null) {
			System.err.println(
					"Failed to get amount for non-existing item: " + id + " | " + new Throwable().getStackTrace()[1].toString());
			return 0;
		}
		if (definition.stackable) {
			Item stacked = findItem(id);
			if (stacked != null)
				return stacked.getAmount();
			return 0;
		}
		return count(id);
	}

	public int count(int id) {
		return count(id, false);
	}

	public int count(int id, boolean acceptNoted) {
		int amount = 0;
		ObjType def = ObjType.get(id);
		for (Item item : items) {
			if (item == null || item.hasAttributes())
				continue;
			if (item.getId() == id)
				amount += item.getAmount();
			else if (acceptNoted && def.notedId != -1 && item.getId() == def.notedId)
				amount += item.getAmount();
		}
		return amount;
	}

	public int count2(int id, boolean acceptNoted) {
		int amount = 0;
		ObjType def = ObjType.get(id);
		for (Item item : items) {
			if (item == null)
				continue;
			if (item.getId() == id)
				amount += item.getAmount();
			else if (acceptNoted && def.notedId != -1 && item.getId() == def.notedId)
				amount += item.getAmount();
		}
		return amount;
	}

	public boolean containsAny(List<Item> items) {
		return items.stream().anyMatch(this::contains);
	}

	public boolean contains(Item item) {
		return contains(item, false);
	}

	public boolean contains(Item item, boolean acceptNoted) {
		return contains(item.getId(), item.getAmount(), acceptNoted);
	}

	public boolean contains(int id, int amount) {
		return contains(id, amount, false);
	}

	public boolean contains2(int id, int amount) {
		return contains2(id, amount, false);
	}

	public boolean contains(int id, boolean acceptNoted) {
		return count(id, acceptNoted) > 0;
	}

	public boolean contains(int id, int amount, boolean acceptNoted) {
		return count(id, acceptNoted) >= amount;
	}

	public boolean contains2(int id, int amount, boolean acceptNoted) {
		return count2(id, acceptNoted) >= amount;
	}

	public boolean contains(int id) {
		return contains(id, 1, false);
	}

	public boolean hasId(int id) {
		return findItem(id) != null;
	}

	public boolean hasItem(int id, int amount) {
		Item item = findItem(id);
		return item != null && item.getAmount() >= amount;
	}

	public boolean hasMultiple(int id) {
		int amount = 0;
		for (Item item : items) {
			if (item == null || item.getId() != id)
				continue;
			if ((amount += item.getAmount()) > 1)
				return true;
		}
		return false;
	}

	public boolean hasMultiple(int... itemIds) {
		int[] amounts = new int[itemIds.length];
		int multiples = 0;
		for (Item item : items) {
			if (item == null)
				continue;
			for (int i = 0; i < itemIds.length; i++) {
				if (itemIds[i] != item.getId())
					continue;
				if (amounts[i] > 1 || (amounts[i] += item.getAmount()) < 2)
					continue;
				if (++multiples >= itemIds.length)
					return true;
			}
		}
		return false;
	}

	public boolean hasRoomFor(int id) {
		ObjType def = ObjType.get(id);
		return getFreeSlots() > 0 || (def != null && def.stackable && hasId(id));
	}

	public boolean hasRoomFor(Item item) {
		return hasRoomFor(item.getId(), item.getAmount());
	}

	public boolean hasFreeSlots(int slots) {
		return getFreeSlots() >= slots;
	}

	@SafeVarargs
	public final boolean containsAll(boolean acceptNoted, I... items) {
		for (Item item : items) {
			if (!contains(item, acceptNoted))
				return false;
		}
		return true;
	}

	public boolean containsAll(boolean acceptNoted, List<Item> items) {
		for (Item item : items) {
			if (!contains(item, acceptNoted))
				return false;
		}
		return true;
	}

	@SafeVarargs
	public final int removeAll(boolean acceptNoted, I... items) {
		int removed = 0;
		for (Item item : items)
			removed += remove(item.getId(), item.getAmount(), acceptNoted, null);
		return removed;
	}

	public boolean hasRoomFor(int id, int amt) {
		ObjType def = ObjType.get(id);
		boolean stackable = forceStack || def.stackable;
		Item existing = findItem(id);
		if (stackable) {
			return existing != null ? ((long) existing.getAmount() + (long) amt) <= Integer.MAX_VALUE : getFreeSlots() >= 1;
		}
		return getFreeSlots() >= amt;
	}

	public int getCapacityFor(int id) {
		ObjType def = ObjType.get(id);
		boolean stackable = forceStack || def.stackable || def.isNote();
		Item existing = findItem(id);
		if (stackable) {
			return existing != null ? Integer.MAX_VALUE - existing.getAmount() : getFreeSlots() >= 1 ? Integer.MAX_VALUE : 0;
		}

		return getFreeSlots();
	}

	public boolean hasAtLeastOneOf(int... itemIds) {
		List<Integer> toFind = IntStream.of(itemIds).boxed().collect(Collectors.toList());
		return nonNullStream().anyMatch(item -> toFind.contains(item.getId()));
	}

	public Stream<I> nonNullStream() {
		return Stream.of(items).filter(Objects::nonNull);
	}

	public int size() {
		return items == null ? 0 : items.length;
	}

	public int removeAll(boolean acceptNoted, List<Item> ingredients) {
		int removed = 0;
		for (Item item : ingredients)
			removed += remove(item.getId(), item.getAmount(), acceptNoted, null);
		return removed;

	}

	protected abstract I newItem(int id, int amount, Map<String, String> attributes);

	protected abstract I[] newArray(int size);
}
