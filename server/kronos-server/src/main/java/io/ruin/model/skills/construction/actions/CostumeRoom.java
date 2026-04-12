package io.ruin.model.skills.construction.actions;

import io.ruin.cache.EnumMap;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.actions.SlotAction;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.skills.construction.Buildable;
import io.ruin.model.skills.construction.Hotspot;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.ruin.model.skills.construction.actions.CostumeStorage.handleClueScrollTiers;

@Slf4j
public class CostumeRoom {
	// new interface needs configuring for store and retrieving items
	public static void register() {
		InterfaceHandler.register(Interface.CONSTRUCTION_COSTUME_STORAGE, h -> {
			h.actions[2] = (SlotAction) CostumeRoom::withdrawCostume;
			h.actions[7] = (DefaultAction) (player, option, s, i) ->
				handleClueScrollTiers(player, option);
			h.actions[10] = CostumeRoom.depositInventory();
		});
		InterfaceHandler.register(Interface.COSTUME_INVENTORY, h -> {
			h.actions[0] = (DefaultAction) (player, o, slot, i) -> {
				Item item = player.getInventory().get(slot);
				depositCostume(player, item);
			};
		});

		for (Buildable b : Hotspot.FANCY_DRESS_BOX.getBuildables()) {
			int closed = b.getBuiltObjects()[0];
			int open = closed + 1;
			ObjectAction.register(closed, "open", (player, obj) -> open(player, obj, open));
			ObjectAction.register(open, "close", (player, obj) -> close(player, obj, closed));
			ObjectAction.register(open, "search", (player, obj) -> CostumeStorage.FANCY_DRESS_BOX.openFancyDress(player));
			ItemObjectAction.register(open, (player, item, obj) -> depositCostume(player, item, b, CostumeStorage.FANCY_DRESS_BOX));
			b.setRemoveTest((player, room) -> checkStorageEmpty(player, CostumeStorage.FANCY_DRESS_BOX));
		}

		for (Buildable b : Hotspot.TOY_BOX.getBuildables()) {
			int closed = b.getBuiltObjects()[0];
			int open = closed + 1;
			ObjectAction.register(closed, "open", (player, obj) -> open(player, obj, open));
			ObjectAction.register(open, "close", (player, obj) -> close(player, obj, closed));
			ObjectAction.register(open, "search", (player, obj) -> CostumeStorage.TOY_BOX_1.openToyBox(player));
			ItemObjectAction.register(open, (player, item, obj) -> depositCostume(player, item, b, CostumeStorage.TOY_BOX_1, CostumeStorage.TOY_BOX_2));
			b.setRemoveTest((player, room) -> checkStorageEmpty(player, CostumeStorage.TOY_BOX_1));
		}

		for (Buildable b : Hotspot.ARMOUR_CASE.getBuildables()) {
			int closed = b.getBuiltObjects()[0];
			int open = closed + 1;
			ObjectAction.register(closed, "open", (player, obj) -> open(player, obj, open));
			ObjectAction.register(open, "close", (player, obj) -> close(player, obj, closed));
			ObjectAction.register(open, "search", (player, obj) -> CostumeStorage.ARMOUR_CASE.openArmourCase(player));
			ItemObjectAction.register(open, (player, item, obj) -> depositCostume(player, item, b, CostumeStorage.ARMOUR_CASE));
			b.setRemoveTest((player, room) -> checkStorageEmpty(player, CostumeStorage.ARMOUR_CASE));
		}

		for (Buildable b : Hotspot.MAGIC_WARDROBE.getBuildables()) {
			int closed = b.getBuiltObjects()[0];
			int open = closed + 1;
			ObjectAction.register(closed, "open", (player, obj) -> open(player, obj, open));
			ObjectAction.register(open, "close", (player, obj) -> close(player, obj, closed));
			ObjectAction.register(open, "search", (player, obj) -> CostumeStorage.MAGIC_WARDROBE.openMagicCase(player));
			ItemObjectAction.register(open, (player, item, obj) -> depositCostume(player, item, b, CostumeStorage.MAGIC_WARDROBE));
			b.setRemoveTest((player, room) -> checkStorageEmpty(player, CostumeStorage.MAGIC_WARDROBE));
		}

		for (Buildable b : Hotspot.CAPE_RACK.getBuildables()) {
			int open = b.getBuiltObjects()[0];
			ObjectAction.register(open, "search", (player, obj) -> CostumeStorage.CAPE_RACK.openCapeRack(player));
			ItemObjectAction.register(open, (player, item, obj) -> depositCostume(player, item, b, CostumeStorage.CAPE_RACK));
			b.setRemoveTest((player, room) -> checkStorageEmpty(player, CostumeStorage.CAPE_RACK));
		}

		for (Buildable b : Hotspot.TREASURE_CHEST.getBuildables()) {
			int closed = b.getBuiltObjects()[0];
			int open = closed + 1;
			ObjectAction.register(closed, "open", (player, obj) -> open(player, obj, open));
			ObjectAction.register(open, "close", (player, obj) -> close(player, obj, closed));
			ItemObjectAction.register(open, (player, item, obj) -> depositCostume(player, item, b, CostumeStorage.BEGINNER_TREASURE_TRAILS, CostumeStorage.EASY_TREASURE_TRAILS, CostumeStorage.MEDIUM_TREASURE_TRAILS, CostumeStorage.HARD_TREASURE_TRAILS_1, CostumeStorage.ELITE_TREASURE_TRAILS, CostumeStorage.MASTER_TREASURE_TRAILS));

			b.setRemoveTest((player, room) -> checkStorageEmpty(player, CostumeStorage.BEGINNER_TREASURE_TRAILS, CostumeStorage.EASY_TREASURE_TRAILS, CostumeStorage.MEDIUM_TREASURE_TRAILS, CostumeStorage.HARD_TREASURE_TRAILS_1, CostumeStorage.ELITE_TREASURE_TRAILS, CostumeStorage.MASTER_TREASURE_TRAILS));
		}

		ObjectAction.register(18807, "search", (player, obj) ->
			player.dialogue(new OptionsDialogue(
				new Option("Beginner treasure trails", () ->
					CostumeStorage.BEGINNER_TREASURE_TRAILS.openTreasureChestBeginner(player)),
				new Option("Easy treasure trails", () ->
					CostumeStorage.EASY_TREASURE_TRAILS.openTreasureChestEasy(player)),
				new Option("Medium treasure trails", () ->
					CostumeStorage.MEDIUM_TREASURE_TRAILS.openTreasureChestMedium(player))
			)));

		ObjectAction.register(18809, "search", (player, obj) ->
			player.dialogue(new OptionsDialogue(
				new Option("Beginner treasure trails", () ->
					CostumeStorage.BEGINNER_TREASURE_TRAILS.openTreasureChestBeginner(player)),
				new Option("Easy treasure trails", () ->
					CostumeStorage.EASY_TREASURE_TRAILS.openTreasureChestEasy(player)),
				new Option("Medium treasure trails", () ->
					CostumeStorage.MEDIUM_TREASURE_TRAILS.openTreasureChestMedium(player)),
				new Option("Hard treasure trails", () ->
					CostumeStorage.HARD_TREASURE_TRAILS_1.openTreasureChestHard(player)),
				new Option("Elite treasure trails", () ->
					CostumeStorage.ELITE_TREASURE_TRAILS.openTreasureChestElite(player)),
				new Option("Master treasure trails", () ->
					CostumeStorage.MASTER_TREASURE_TRAILS.openTreasureChestMaster(player))
			)));

		ObjectAction.register(18805, "search", (player, obj) ->
			CostumeStorage.EASY_TREASURE_TRAILS.openTreasureChestBeginner(player));

		ItemObjectAction.register(18805, (player, item, obj) ->
			depositCostume(player, item,
				Buildable.OAK_TREASURE_CHEST,
				CostumeStorage.EASY_TREASURE_TRAILS));

		ItemObjectAction.register(18807, (player, item, obj) ->
			depositCostume(player, item,
				Buildable.TEAK_TREASURE_CHEST,
				CostumeStorage.EASY_TREASURE_TRAILS,
				CostumeStorage.MEDIUM_TREASURE_TRAILS));

		ItemObjectAction.register(18809, (player, item, obj) ->
			depositCostume(player, item,
				Buildable.MAHOGANY_TREASURE_CHEST,
				CostumeStorage.EASY_TREASURE_TRAILS,
				CostumeStorage.MEDIUM_TREASURE_TRAILS,
				CostumeStorage.HARD_TREASURE_TRAILS_1,
				CostumeStorage.HARD_TREASURE_TRAILS_2,
				CostumeStorage.ELITE_TREASURE_TRAILS,
				CostumeStorage.MASTER_TREASURE_TRAILS));

	}

	private static SimpleAction depositInventory() {
		return p -> {
			CostumeStorage type = p.get("COSTUME_STORAGE");
			log.debug("Depositing inventory into costume storage: {}", type);
			var backpackItems = Arrays.asList(p.getInventory().getItems());
			backpackItems.stream()
				.filter(Objects::nonNull)
				.forEach(item -> depositCostume(p, item));
		};
	}

	public static List<Integer> getCostumeEnumValueIds(CostumeStorage type) {
		return switch (type) {
			case MAGIC_WARDROBE -> Arrays.stream(EnumMap.get(3289).intValues).boxed().toList();
			case ARMOUR_CASE -> Arrays.stream(EnumMap.get(3290).intValues).boxed().toList();
			case FANCY_DRESS_BOX -> Arrays.stream(EnumMap.get(3291).intValues).boxed().toList();
			case CAPE_RACK -> Arrays.stream(EnumMap.get(3292).intValues).boxed().toList();
			case BEGINNER_TREASURE_TRAILS -> Arrays.stream(EnumMap.get(3293).intValues).boxed().toList();
			case EASY_TREASURE_TRAILS -> Arrays.stream(EnumMap.get(3294).intValues).boxed().toList();
			case MEDIUM_TREASURE_TRAILS -> Arrays.stream(EnumMap.get(3295).intValues).boxed().toList();
			case HARD_TREASURE_TRAILS_1,
				 HARD_TREASURE_TRAILS_2 -> Arrays.stream(EnumMap.get(3296).intValues).boxed().toList();
			case ELITE_TREASURE_TRAILS -> Arrays.stream(EnumMap.get(3297).intValues).boxed().toList();
			case MASTER_TREASURE_TRAILS -> Arrays.stream(EnumMap.get(3298).intValues).boxed().toList();
			case TOY_BOX_1,
				 TOY_BOX_2 -> Arrays.stream(EnumMap.get(3299).intValues).boxed().toList();
		};
	}

	private static void open(Player player, GameObject obj, int open) {
		player.animate(536);
		obj.setId(open);
	}

	private static void close(Player player, GameObject obj, int closed) {
		player.animate(535);
		obj.setId(closed);
	}

	private static boolean checkStorageEmpty(Player player, CostumeStorage... types) {
		for (CostumeStorage type : types) {
			Map<Costume, int[]> stored = type.getSets(player);
			if (!stored.isEmpty()) {
				player.sendMessage("You must take all the items from inside before you can remove it.");
				return false;
			}
		}
		return true;
	}


	private static void withdrawCostume(Player player, int slot) {
		CostumeStorage type = player.get("COSTUME_STORAGE");
		if (type == null)
			return;
		slot /= 4;
		if (slot < 0 || slot > type.getCostumes().length) {
			throw new IllegalArgumentException("" + slot);
		}
		if (type.display[slot].getId() == 10165) { // more...
			if (type == CostumeStorage.HARD_TREASURE_TRAILS_1) {
				CostumeStorage.HARD_TREASURE_TRAILS_2.openTreasureChestHard(player);
			} else if (type == CostumeStorage.TOY_BOX_1) {
				CostumeStorage.TOY_BOX_2.openToyBox(player);
			}
			return;
		} else if (type.display[slot].getId() == 10166) {
			if (type == CostumeStorage.HARD_TREASURE_TRAILS_2) {
				CostumeStorage.HARD_TREASURE_TRAILS_1.openTreasureChestHard(player);
			} else if (type == CostumeStorage.TOY_BOX_2) {
				CostumeStorage.TOY_BOX_1.openToyBox(player);
			}
			return;
		}
		if (type.display[0].getId() == 10166) // back...
			slot--;
		Map<Costume, int[]> storedSets = type.getSets(player);
		if (storedSets == null)
			throw new IllegalArgumentException();
		Costume costume = type.getCostumes()[slot];
		int[] stored = storedSets.get(costume);
		if (stored == null) {
			return;
		}
		if (!player.getInventory().hasFreeSlots(stored.length)) {
			player.sendMessage("You'll need at least " + stored.length + " free inventory slots to withdraw that costume.");
			return;
		}
		for (int id : stored) player.getInventory().add(id, 1);
		storedSets.remove(costume);
		player.closeInterfaces();
	}

	@Deprecated
	private static void depositCostume(Player player, Item item, Buildable b, CostumeStorage... validTypes) {
		CostumeStorage type = player.get("COSTUME_STORAGE");
		Costume costume = null;
		for (CostumeStorage cs : CostumeStorage.VALUES) {
			costume = cs.getByItem(item.getId());
			if (costume != null) {
				type = cs;
				break;
			}
		}
		if (!Arrays.asList(validTypes).contains(type)) {
			player.sendMessage("You can't store that item there.");
			System.out.println("costume = " + costume);
			System.out.println("type = " + type);
			System.out.println("validTypes = " + Arrays.toString(validTypes));
			return;
		}
//        for (int[] piece : costume.pieces) {
//            if (Arrays.stream(piece).anyMatch(option -> player.getInventory().contains(option, 1))) {
//                costume.sendRequiredItems(player);
//                return;
//            }
//        }
		int maxStored = getMaxStorage(b);
		int currentStored = type.countSpaceUsed(player);
		if (currentStored >= maxStored) {
			if (type == CostumeStorage.CAPE_RACK)
				player.sendMessage("That cape rack can only hold up to " + maxStored + " capes of accomplishment.");
			else
				player.sendMessage("There's no more space in there.");
			return;
		}
		depositCostume(player, item);
	}

	public static int getMaxStorage(Buildable b) {
		return switch (b) {
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

	private static void depositCostume(Player player, Item item) {
		CostumeStorage type = player.get("COSTUME_STORAGE");
		var keys = getCostumeEnumValueIds(type);
		keys.forEach(key -> {
			var cached = CostumeStorage.getValidCostumeIdsForCostumePiece(key);
			var index = 0;
			// Quick check if the item is a single entry
			if (key == item.getId()) {
				if (player.getInventory().remove(item.getId(), 1) > 0)
					depositCostumeIntoCachedItems(player, item, 0);
				return;
			}
			// we have other possible items in the display viewport, loop them
			if (cached.length > 0) {
				log.info("cached = {}", cached);
				for (int costumeItemId : cached) {
					if (item.getId() == costumeItemId) {
						if (player.getInventory().remove(item.getId(), 1) > 0)
							depositCostumeIntoCachedItems(player, item, index);
						return;
					}
					index++;
				}
			}
		});
		player.sendFilteredMessage("You can't store that item here.");
	}

	private static void depositCostumeIntoCachedItems(Player player, Item item, int index) {
		CostumeStorage type = player.get("COSTUME_STORAGE");
		type.display[index].setAmount(1);
		log.debug("Depositing item {} into costume storage {} at an displayIndex og {}", item.getId(), type, index);
		player.sendMessage("You place the costume in the %s.".formatted(type.getFormattedName()));
	}
}
