package io.ruin.model.skills.construction.actions.impl.costume_room;

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

import java.util.Arrays;
import java.util.Objects;

import static io.ruin.model.skills.construction.actions.impl.costume_room.CostumeStorage.*;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-08
 */
public class CostumeRoomAction {

	public static void register() {
		InterfaceHandler.register(Interface.CONSTRUCTION_COSTUME_STORAGE, h -> {
			h.actions[2] = (SlotAction) (player, slot) -> {
				var costumeStorage = (CostumeStorage) player.get("COSTUME_STORAGE");
				if (costumeStorage == null) {
					player.sendMessage("You must be in a costume storage to withdraw items.");
					return;
				}
				costumeStorage.handleWithdrawFromSlot(player, slot);
			};
			h.actions[7] = (DefaultAction) (player, option, s, i) ->
				CostumeStorage.handleClueScrollTiers(player, option);
			// action: 8 -> toggles the 'Deposit Mode' button
			h.actions[10] = depositInventory();
		});
		InterfaceHandler.register(Interface.COSTUME_INVENTORY, h -> {
			h.actions[0] = (DefaultAction) (player, o, slot, i) -> {
				Item item = player.getInventory().get(slot);
				player.costumeStorage.depositCostume(player, item);
			};
		});

		ObjectAction.register(18807, "search", (player, obj) ->
			player.dialogue(new OptionsDialogue(
				new Option("Beginner treasure trails", () -> CostumeStorage.openCostumeStorage(player, BEGINNER_TREASURE_TRAILS)),
				new Option("Easy treasure trails", () -> CostumeStorage.openCostumeStorage(player, EASY_TREASURE_TRAILS)),
				new Option("Medium treasure trails", () -> CostumeStorage.openCostumeStorage(player, MEDIUM_TREASURE_TRAILS))
			)));

		ObjectAction.register(18809, "search", (player, obj) ->
			player.dialogue(new OptionsDialogue(
				new Option("Beginner treasure trails", () -> CostumeStorage.openCostumeStorage(player, BEGINNER_TREASURE_TRAILS)),
				new Option("Easy treasure trails", () -> CostumeStorage.openCostumeStorage(player, EASY_TREASURE_TRAILS)),
				new Option("Medium treasure trails", () -> CostumeStorage.openCostumeStorage(player, MEDIUM_TREASURE_TRAILS)),
				new Option("Hard treasure trails", () -> CostumeStorage.openCostumeStorage(player, HARD_TREASURE_TRAILS)),
				new Option("Elite treasure trails", () ->	CostumeStorage.openCostumeStorage(player, ELITE_TREASURE_TRAILS)),
				new Option("Master treasure trails", () -> CostumeStorage.openCostumeStorage(player, MASTER_TREASURE_TRAILS))
			)));

		ObjectAction.register(18805, "search", (player, o) ->
			CostumeStorage.openCostumeStorage(player, FANCY_DRESS_BOX));

		ItemObjectAction.register(18805, (player, item, o) ->
			player.costumeStorage.depositCostume(player, item));
		ItemObjectAction.register(18807, (player, item, o) ->
			player.costumeStorage.depositCostume(player, item));
		ItemObjectAction.register(18809, (player, item, o) ->
			player.costumeStorage.depositCostume(player, item));

		for (Buildable b : Hotspot.FANCY_DRESS_BOX.getBuildables()) {
			int closed = b.getBuiltObjects()[0];
			int open = closed + 1;
			ObjectAction.register(closed, "open", (player, obj) -> open(player, obj, open));
			ObjectAction.register(open, "close", (player, obj) -> close(player, obj, closed));
			ObjectAction.register(open, "search", (player, obj) -> CostumeStorage.openCostumeStorage(player, FANCY_DRESS_BOX));
			ItemObjectAction.register(open, (player, item, obj) -> player.costumeStorage.depositCostume(player, item));
			b.setRemoveTest((player, room) -> checkStorageEmpty(player, FANCY_DRESS_BOX));
		}

		for (Buildable b : Hotspot.TOY_BOX.getBuildables()) {
			int closed = b.getBuiltObjects()[0];
			int open = closed + 1;
			ObjectAction.register(closed, "open", (player, obj) -> open(player, obj, open));
			ObjectAction.register(open, "close", (player, obj) -> close(player, obj, closed));
			ObjectAction.register(open, "search", (player, obj) -> CostumeStorage.openCostumeStorage(player, TOY_BOX));
			ItemObjectAction.register(open, (player, item, obj) -> player.costumeStorage.depositCostume(player, item));
			b.setRemoveTest((player, room) -> checkStorageEmpty(player, TOY_BOX));
		}

		for (Buildable b : Hotspot.ARMOUR_CASE.getBuildables()) {
			int closed = b.getBuiltObjects()[0];
			int open = closed + 1;
			ObjectAction.register(closed, "open", (player, obj) -> open(player, obj, open));
			ObjectAction.register(open, "close", (player, obj) -> close(player, obj, closed));
			ObjectAction.register(open, "search", (player, obj) -> CostumeStorage.openCostumeStorage(player, ARMOUR_CASE));
			ItemObjectAction.register(open, (player, item, obj) -> player.costumeStorage.depositCostume(player, item));
			b.setRemoveTest((player, room) -> checkStorageEmpty(player, ARMOUR_CASE));
		}

		for (Buildable b : Hotspot.MAGIC_WARDROBE.getBuildables()) {
			int closed = b.getBuiltObjects()[0];
			int open = closed + 1;
			ObjectAction.register(closed, "open", (player, obj) -> open(player, obj, open));
			ObjectAction.register(open, "close", (player, obj) -> close(player, obj, closed));
			ObjectAction.register(open, "search", (player, obj) -> CostumeStorage.openCostumeStorage(player, MAGIC_WARDROBE));
			ItemObjectAction.register(open, (player, item, obj) -> player.costumeStorage.depositCostume(player, item));
			b.setRemoveTest((player, room) -> checkStorageEmpty(player, MAGIC_WARDROBE));
		}

		for (Buildable b : Hotspot.CAPE_RACK.getBuildables()) {
			int open = b.getBuiltObjects()[0];
			ObjectAction.register(open, "search", (player, obj) -> CostumeStorage.openCostumeStorage(player, CAPE_RACK));
			ItemObjectAction.register(open, (player, item, obj) -> player.costumeStorage.depositCostume(player, item));
			b.setRemoveTest((player, room) -> checkStorageEmpty(player, CAPE_RACK));
		}

		for (Buildable b : Hotspot.TREASURE_CHEST.getBuildables()) {
			int closed = b.getBuiltObjects()[0];
			int open = closed + 1;
			ObjectAction.register(closed, "open", (player, obj) -> open(player, obj, open));
			ObjectAction.register(open, "close", (player, obj) -> close(player, obj, closed));
			ItemObjectAction.register(open, (player, item, obj) -> player.costumeStorage.depositCostume(player, item));

			b.setRemoveTest((player, room) -> checkStorageEmpty(player, BEGINNER_TREASURE_TRAILS, EASY_TREASURE_TRAILS, MEDIUM_TREASURE_TRAILS, HARD_TREASURE_TRAILS, ELITE_TREASURE_TRAILS, MASTER_TREASURE_TRAILS));
		}

	}

	private static boolean checkStorageEmpty(Player player, CostumeStorage... types) {
		for (CostumeStorage type : types) {
			var stored = type.getOutfits();
			if (!stored.isEmpty()) {
				player.sendMessage("You must take all the items from inside before you can remove it.");
				return false;
			}
		}
		return true;
	}

	private static SimpleAction depositInventory() {
		return p -> {
			var backpackItems = Arrays.asList(p.getInventory().getItems());
			backpackItems.stream()
				.filter(Objects::nonNull)
				.forEach(item -> p.costumeStorage.depositCostume(p, item));
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
}
