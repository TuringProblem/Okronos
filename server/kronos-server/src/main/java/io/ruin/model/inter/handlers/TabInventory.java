package io.ruin.model.inter.handlers;

import discord.webhooks.logs.GroundItemHook;
import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.EnumType;
import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.activities.DonationBossHandler;
import io.ruin.model.activities.wilderness.BloodyChest;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.*;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Tile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.map.object.GameObject;
import io.ruin.services.Loggers;
import io.ruin.model.var.VarPlayerRepository;
import org.json.JSONObject;

import static io.ruin.model.activities.raids.xeric.ChambersOfXeric.isRaiding;
import java.nio.channels.IllegalSelectorException;

public class TabInventory {

	public static void register() {
		InterfaceHandler.register(Interface.INVENTORY, h -> {
			h.actions[0] = new InterfaceAction() {
				@Override
				public void handleClick(Player player, int option, int slot, int itemId) {
					click(player, option, slot, itemId);
				}

				@Override
				public void handleDrag(Player player, int fromSlot, int fromItemId, int toInterfaceId, int toChildId,
						int toSlot, int toItemId) {
					drag(player, fromSlot, toSlot);
				}

				@Override
				public void handleOnInterface(Player player, int fromSlot, int fromItemId, int toInterfaceId, int toChildId,
						int toSlot, int toItemId) {
					onInterface(player, fromSlot, fromItemId, toSlot, toItemId);
				}

				@Override
				public void handleOnGroundItem(Player player, int slot, int itemId, GroundItem groundItem, int distance) {
					onGroundItem(player, slot, itemId, groundItem, distance);
				}

				@Override
				public void handleOnEntity(Player player, Entity entity, int slot, int itemId) {
					onEntity(player, entity, slot, itemId);
				}

				@Override
				public void handleOnObject(Player player, int slot, int itemId, GameObject obj) {
					onObject(player, slot, itemId, obj);
				}
			};
		});
	}

	public static Bounds TourneyBounds = new Bounds(3627, 9067, 3669, 9109, 0);
	public static Bounds TourneyFinal = new Bounds(3610, 8986, 3621, 8997, 0);

	private static void click(Player player, int ifbutton, int slot, int itemId) {
		// System.err.println(ifbutton + " s" + slot + " itm" + itemId);
		Item item = player.getInventory().get(slot, itemId);
		if (item == null)
			return;

		if (!item.getDef().consumable && player.isLockedExclude(LockType.FULL_ALLOW_EAT))
			return;

		if (ifbutton == 10) {
			item.examine(player);
			return;
		}
		ObjType def = item.getDef();
		ItemAction[] actions = def.inventoryActions;

		EnumType iop_to_ifbutton = EnumType.get(4303);

		int i = 0;
		int option = 0;
		if (ifbutton == 1) {
			option = item.getDef().shiftClickDropIndex + 1;
		} else {
			option = iop_to_ifbutton.intValuesReversed().get(ifbutton);
		}
		i = option - 1;

		if (i < 0 || i >= 5)
			return;
		if (actions != null) {
			ItemAction action = actions[i];
			if (action != null) {
				action.handle(player, item);
				return;
			}
		}
		if (player.debug)
			player.sendMessage("Option is: " + option + " " + def.dropOption);

		Bounds tournamentLobby = new Bounds(3106, 3510, 3112, 3518, 2);
		Bounds TourneyBounds = new Bounds(3627, 9067, 3669, 9109, 0);
		Bounds TourneyFinal = new Bounds(3610, 8986, 3621, 8997, 0);
		if (option == def.dropOption) {
			Tile tile = player.getPosition().getTile();
			if (tile != null /* && !tile.allowDrop */ && player.getPosition().inBounds(tournamentLobby)) {
				player.sendMessage("You can't drop items here.");
				return;
			}

			if (!item.copyOfAttributes().isEmpty() && item.getId() != 30464) {
				player.dialogue(new YesNoDialogue("You can't drop items with charges, would you like to clear them?",
						"You will receive nothing in return for doing this.", item, item::clearAttributes));
				return;
			}

			if (DonationBossHandler.map != null) {
				if (player.getPosition().getRegion().id == DonationBossHandler.map.swRegion.id) {
					player.sendMessage("You can't drop items here.");
					return;
				}
			}

			if (item.getId() == 30464 && !item.copyOfAttributes().isEmpty()) {
				player.sendMessage("You can't drop this item.");
				return;
			}

			if (player.getPosition().inBounds(TourneyFinal)) {
				player.sendMessage("You cannot drop items while inside a Tournament!");
				return;
			}
			if (player.getPosition().inBounds(TourneyBounds)) {
				player.sendMessage("You cannot drop items while inside a Tournament!");
				return;
			}
			if (player.jailOresAssigned > 0) {
				player.sendMessage("You cannot drop items while in jail.");
				return;
			}
			ObjType itemDef = ObjType.get(itemId);
			if (isRaiding(player) && !itemDef.tradeable && !item.getDef().coxItem) {
				player.sendMessage("You cannot drop untradable items while inside a raid!");
				return;
			}
			if (player.getDuel().stage >= 4) {
				player.sendMessage("You can't drop items in a duel.");
				return;
			}
			if (player.joinedTournament) {
				player.sendMessage("You can't drop items while you're signed up for a tournament.");
				return;
			}
			if (player.isInOwnHouse() && player.getCurrentHouse().isBuildingMode()) {
				player.sendMessage("You can't drop items while in building mode.");
				return;
			}
			if (player.supplyChestRestricted) {
				player.sendMessage("The power of the supply chest prevents you from dropping items!");
				return;
			}
			if (player.wildernessLevel > 0 && BloodyChest.hasBloodyKey(player)) {
				player.sendMessage("The power of your bloody key prevents you from dropping items!");
			}
			if (player.wildernessLevel > 0 && item.getId() == ItemID.LOOTING_BAG) {
				player.sendMessage("You cannot do this here.");
				return;
			}
			if (item.getId() == 30590) {
				player.sendMessage("You cannot drop this item.");
				return;
			}
			if (item.getId() == 30480 || item.getId() == 30479 || item.getId() == 30478) {
				player.sendMessage("You cannot drop this item.");
				return;
			}

			dropWithValueCheck(player, item, def);

			var object = new JSONObject();
				object.put("player", player.getName());
				object.put("item_name", item.getDef().name);
				object.put("item_amount", NumberUtils.formatNumber(item.getAmount()));
				object.put("item_noted", item.getDef().isNote());
				object.put("drop_x", player.getPosition().getX());
				object.put("drop_y", player.getPosition().getY());
				object.put("drop_z", player.getPosition().getZ());
			GroundItemHook.sendDropItemToDiscord(object);

			Loggers.logDrop(player.getUserId(), player.getName(), player.getIp(), item.getId(), item.getAmount(),
					player.getAbsX(), player.getAbsY(), player.getHeight());

			return;
		}
		if (option == def.equipOption) {
			player.getEquipment().equip(item);
			player.resetActions(false, player.getMovement().following != null, true);
			return;
		}
		item.examine(player);
	}

	private static void dropWithValueCheck(Player player, Item item, ObjType def) {
		var shouldWarn = VarPlayerRepository.DROP_ITEM_WARNING_ENABLED.get(player) == 1;
		var warnValue = VarPlayerRepository.DROP_NOTIFICATION_MIN_VALUE.get(player);
		if (shouldWarn && def.value >= warnValue) {
			dropWithWarning(player, item, def);
			return;
		}
		drop(player, item, def);
	}

	private static void dropWithWarning(Player player, Item item, ObjType def) {
		var dialogue = new ItemDialogue().one(
				item.getId(),
				"That item is considered valuable.<br> ?");
		Runnable onDialogueContinued = () -> {
			drop(player, item, def);
		};
		player.dialogue(dialogue, onDialogueContinued);
	}

	private static void drop(Player player, Item item, ObjType def) {
		item.remove();
		player.resetActions(true, false, true);
		player.privateSound(2739);
		if (player.wildernessLevel > 0) {
			dropWilderness(player, item, def);
		} else {
			dropRegular(player, item, def);
		}
	}

	private static void dropRegular(Player player, Item item, ObjType def) {
		/**
		 * Player is in a raid, so make items appear instantly.
		 */
		if (player.raidsParty != null) {
			new GroundItem(item).owner(player).droppedBy(player).position(player.getPosition()).spawnPublic();
		} else {
			/**
			 * Regular dropping
			 */
			new GroundItem(item).owner(player).droppedBy(player).position(player.getPosition()).spawn();
		}
	}

	private static void dropWilderness(Player player, Item item, ObjType def) {
		/**
		 * Dropping in the wilderness
		 */
		if (def.consumable || !def.tradeable)
			new GroundItem(item).owner(player).droppedBy(player).position(player.getPosition()).spawnPrivate();
		else
			new GroundItem(item).owner(player).droppedBy(player).position(player.getPosition()).spawnPublic();
	}

	public static void drag(Player player, int fromSlot, int toSlot) {
		if (fromSlot == toSlot)
			return;
		if (!player.getInventory().validateSlots(fromSlot, toSlot)) {
			return;
		}
		Item fromItem = player.getInventory().get(fromSlot);
		if (fromItem == null) {
			player.getInventory().update(toSlot);
			player.getInventory().update(fromSlot);
			return;
		}
		player.closeChatbox(false);
		Item toItem = player.getInventory().get(toSlot);
		if (toItem == null) {
			player.getInventory().set(fromSlot, null);
			player.getInventory().set(toSlot, fromItem);
		} else {
			player.getInventory().set(toSlot, fromItem);
			player.getInventory().set(fromSlot, toItem);
		}
	}

	private static void onInterface(Player player, int fromSlot, int fromItemId, int toSlot, int toItemId) {
		Item fromItem = player.getInventory().get(fromSlot, fromItemId);
		if (fromItem == null)
			return;
		Item toItem = player.getInventory().get(toSlot, toItemId);
		if (toItem == null)
			return;
		ItemItemAction.handleAction(player, fromItem, toItem);
	}

	private static void onGroundItem(Player player, int slot, int itemId, GroundItem groundItem, int distance) {
		Item item = player.getInventory().get(slot, itemId);
		if (item == null)
			return;
		ItemGroundItemAction.handleAction(player, item, groundItem, distance);
	}

	private static void onEntity(Player player, Entity entity, int slot, int itemId) {
		Item item = player.getInventory().get(slot, itemId);
		if (item == null)
			return;
		if (entity.npc != null)
			ItemNPCAction.handleAction(player, item, entity.npc);
		else
			ItemPlayerAction.handleAction(player, item, entity.player);
	}

	private static void onObject(Player player, int slot, int itemId, GameObject obj) {
		Item item = player.getInventory().get(slot, itemId);
		if (item == null)
			return;
		ItemObjectAction.handleAction(player, item, obj);
	}

}
