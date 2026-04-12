package io.ruin.model.skills.construction.actions.impl.costume_room;

import io.ruin.api.utils.StringUtils;
import io.ruin.cache.EnumMap;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.handlers.OptionScroll;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.skills.construction.Buildable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static io.ruin.model.inter.AccessMasks.*;
import static io.ruin.model.inter.AccessMasks.DragTargetable;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-07
 */
@Slf4j
public enum CostumeStorage {
	FANCY_DRESS_BOX(3291),
	ARMOUR_CASE(3290),
	MAGIC_WARDROBE(3289),
	CAPE_RACK(3292),
	BEGINNER_TREASURE_TRAILS(3293),
	EASY_TREASURE_TRAILS(3294),
	MEDIUM_TREASURE_TRAILS(3295),
	HARD_TREASURE_TRAILS(3296),
	ELITE_TREASURE_TRAILS(3297),
	MASTER_TREASURE_TRAILS(3298),
	TOY_BOX(3299),
	;

	private final int containerId;
	private final int enumMapId;
	private final EnumMap enumMap;
	@Getter
	private final List<Outfit> outfits;

	CostumeStorage(int enumMabId) {
		this.containerId = 32768;
		this.enumMapId = enumMabId;
		this.enumMap = EnumMap.get(this.enumMapId);
		this.outfits = new ArrayList<>();
		Arrays.stream(getEnumMap().intValues)
			.mapToObj(Outfit::new)
			.forEach(outfits::add);
	}

	public static void openCostumeStorage(Player player, CostumeStorage storage) {
//		if (player.isVisibleInterface(Interface.COSTUME_INVENTORY))
//			return;
//
//		player.openInterface(ToplevelComponent.SIDEMODAL, Interface.COSTUME_INVENTORY);
//		player.getPacketSender().sendClientScript(149, "iiiiiisssss", 44171264, 93, 4, 7, 1, -1,
//			"Store<col=ff9040>", "", "", "", "");
//		player.getPacketSender().sendIfEvents(Interface.COSTUME_INVENTORY, 0, 0, 27,
//			ClickOp1,
//			ClickOp10,
//			DragDepth1,
//			DragTargetable
//		);
		long owned = 0;
		int slot = 0;
		for (int i = 0; i < storage.getOutfits().size(); i++) {
			int shift = slot;
			if (shift > 30)
				shift++;
			if (storage.getOutfits().get(i).contents() != null) {
				owned |= 1L << shift;
			}
			slot++;
		}
		player.getPacketSender().sendClientScript(417, "iii",
			(int) (owned & 0xffffffffL),
			(int) (owned >> 32)
		);
		player.getPacketSender().sendClientScript(3532, "iii",
			storage.enumMapId,
			1,
			1
		);

		player.openInterface(ToplevelComponent.MAINMODAL, Interface.CONSTRUCTION_COSTUME_STORAGE);
		//TODO : Talk with Polish about the packets for sending items
		player.getPacketSender().sendItems(storage.containerId, -1, storage.containerId, storage.getCostumeAsArray());

		player.getPacketSender().sendIfEvents(Interface.CONSTRUCTION_COSTUME_STORAGE,
			4,
			0,
			storage.getTotalCostumePiecesInStorage(), // was 287
			1026
		);
		player.set("COSTUME_STORAGE", storage);
		player.costumeStorage = storage;
		log.info("Opened costume storage: {}", storage.getFormattedName());
	}

	private int getTotalCostumePiecesInStorage() {
		var count = new AtomicInteger();
		outfits.forEach(outfit -> count.addAndGet(outfit.contents().size()));
		return count.get();
	}

	/**
	 * Retrieves all costume pieces stored within the storage as an array of {@code Item} objects.
	 *
	 * @return an array of {@code Item} objects representing all costume pieces currently in storage
	 */
	private Item[] getCostumeAsArray() {
		var count = getTotalCostumePiecesInStorage();
		var costumes = new Item[count];
		var ticker = new AtomicInteger(0);
		outfits.forEach(outfit ->
			outfit.contents().forEach((itemId, amount) ->
				costumes[ticker.getAndIncrement()] = new Item(itemId, amount)));
		return costumes;
	}

	private EnumMap getEnumMap() {
		return enumMap;
	}

	public void depositCostume(Player player, Item item) {
		outfits.forEach(outfit -> {
			outfit.contents().forEach((itemId, amount) -> {
				if (itemId == item.getId()) {
					outfit.contents().put(itemId, amount + 1);
					player.getInventory().remove(item.getId(), 1);
					player.sendMessage("You place the costume in the %s.".formatted(getFormattedName()));
				}
			});
		});
	}

	public void handleWithdrawFromSlot(Player player, int slot) {
		var costume = (CostumeStorage) player.get("COSTUME_STORAGE");
		slot /= 4;
		if (slot < 0 || slot > costume.getOutfits().size())
			throw  new IllegalArgumentException("Slot must be between 0 and %d".formatted(costume.getOutfits().size()));

		log.info("Handling withdraw from slot: {}", slot);
	}

	public void withdrawCostume(Player player, Item item) {
		var costume = (CostumeStorage) player.get("COSTUME_STORAGE");
		if (costume == null) {
			player.sendMessage("You don't have a costume storage open.");
			return;
		}
		getOutfits().forEach(outfit -> {
			if (outfit.contents().containsKey(item.getId())) {
				// we have a match, withdraw item
				var amountInStorage = outfit.contents().get(item.getId());
				// Make sure we have something to remove
				if (amountInStorage < 1) {
					player.sendMessage("You don't have any more of that costume in the %s.".formatted(getFormattedName()));
					return;
				}
				// remove it and add to the player
				if (!player.getInventory().hasRoomFor(item.getId())) {
					player.sendMessage("You don't have room for that item.");
					return;
				}
				// remove 1 item from the costume storage
				outfit.contents().put(item.getId(), amountInStorage - 1);
				// add it to the player's inventory
				player.getInventory().add(item.getId(), 1);
				// Inform the player
				player.sendMessage("You take the costume from the %s.".formatted(getFormattedName()));
			}
		});
	}

	public String getFormattedName() {
		return StringUtils.fixCaps(name().replace("_", " "));
	}

	public static void handleClueScrollTiers(Player player, int option) {
		switch (option) {
			case 1:
				OptionScroll.open(player, "Select a tier",
					new Option("Beginner", () -> CostumeStorage.openCostumeStorage(player, BEGINNER_TREASURE_TRAILS)),
					new Option("Easy", () -> CostumeStorage.openCostumeStorage(player, EASY_TREASURE_TRAILS)),
					new Option("Medium", () -> CostumeStorage.openCostumeStorage(player, MEDIUM_TREASURE_TRAILS)),
					new Option("Hard", () -> CostumeStorage.openCostumeStorage(player, HARD_TREASURE_TRAILS)),
					new Option("Elite", () -> CostumeStorage.openCostumeStorage(player, ELITE_TREASURE_TRAILS)),
					new Option("Master", () -> CostumeStorage.openCostumeStorage(player, MASTER_TREASURE_TRAILS))
				);
				break;
			case 2:
				CostumeStorage.openCostumeStorage(player, BEGINNER_TREASURE_TRAILS);
				break;
		}
	}

	public static int getMaxStorageForBuiltObject(Buildable storageUnit) {
		return switch (storageUnit) {
			case OAK_CAPE_RACK -> 0;
			case OAK_MAGIC_WARDROBE, TEAK_CAPE_RACK -> 1;
			case OAK_ARMOUR_CASE, CARVED_OAK_MAGIC_WARDROBE, OAK_FANCY_DRESS_BOX -> 2;
			case TEAK_MAGIC_WARDROBE -> 3;
			case TEAK_ARMOUR_CASE, CARVED_TEAK_MAGIC_WARDROBE, TEAK_FANCY_DRESS_BOX -> 4;
			case MAHOGANY_MAGIC_WARDROBE, MAHOGANY_CAPE_RACK -> 5;
			case GILDED_MAGIC_WARDROBE -> 6;
			case GILDED_CAPE_RACK -> 10;
			default -> Integer.MAX_VALUE;
		};
	}
}
