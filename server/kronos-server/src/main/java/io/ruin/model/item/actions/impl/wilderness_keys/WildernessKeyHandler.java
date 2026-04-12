package io.ruin.model.item.actions.impl.wilderness_keys;

import discord.webhooks.logs.WildernessKeyHook;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.map.object.actions.ObjectAction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WildernessKeyHandler {

	static final int KEY_ID = 26651;

	public static void register() {
		ObjectAction.register(43486, 1, (player, obj) -> {
			open(player);
		});

		ItemAction.registerInventory(KEY_ID, "Check", WildernessKeyHandler::check);
		ItemAction.registerInventory(KEY_ID, "Destroy", WildernessKeyHandler::destroyKey);
		ItemObjectAction.register(43484, KEY_ID, (player, item, obj) -> open(player, item));
		LoginListener.register(WildernessKeyHandler::setSkull);
	}

	public static boolean handlePlayerKill(Player player, List<Item> lostItems) {
		if (lostItems.isEmpty() || player.getGameMode() != GameMode.STANDARD)
			return false;
		int keyIndex = player.getInventory().findItem(KEY_ID) == null ? 0 : getFreeKeyIndex(player);
		if (keyIndex == -1 || !player.getInventory().hasRoomFor(KEY_ID, 1)) {
			player.sendMessage("You can't carry anymore keys.");
			return false;
		}
		player.wildernessKeyItems[keyIndex] = lostItems.toArray(new Item[0]);
		Item key = new Item(KEY_ID, 1);
		key.getDef().neverProtect = true;
		AttributeExtensions.setCharges(AttributeTypes.WILDERNESS_KEY, key, keyIndex);
		player.getInventory().add(key);
		player.sendMessage("You have received a wilderness key.");
		setSkull(player);
		return true;
	}

	private static void destroyKey(Player player, Item key) {
		player.dialogue(new OptionsDialogue("Are you sure you want to destroy the key and its contents?",
			new Option("Yes, destroy the key.", () -> {
				key.remove();
				int keyIndex = AttributeExtensions.getCharges(AttributeTypes.WILDERNESS_KEY, key);
				List<Item> itemsToLose = List.of(player.wildernessKeyItems[keyIndex]);
				player.wildernessKeyItems[keyIndex] = null;
				setSkull(player);

//				RareDropEmbedMessage.sendReceivedKeyLog(player, player, itemsToLose, true);

				var items = new JSONArray();
				for (Item item : itemsToLose) {
					var itemJson = new JSONObject();
					itemJson.put("id", item.getId());
					itemJson.put("name", item.getDef().getName());
					itemJson.put("amount", item.getAmount());
					items.put(itemJson);
				}

				var jsonObject = new JSONObject();
					jsonObject.put("player", player.getName());
					jsonObject.put("dead_player", player.getName());
					jsonObject.put("key_action", "destroyed");
					jsonObject.put("lost_items", items);

				WildernessKeyHook.sendDestroyKeyLog(jsonObject);

			}),
			new Option("Nevermind.")));
	}

	private static void check(Player player, Item key) {
		int keyIndex = AttributeExtensions.getCharges(AttributeTypes.WILDERNESS_KEY, key);
		if (keyIndex == -1)
			return;
		List<Item> items = List.of(player.wildernessKeyItems[keyIndex]);
		for (Item item : items) {
			player.sendMessage(item.getAmount() + "x " + item.getDef().name);
		}
	}

	public static boolean handleDyingWithKey(Player player, Player dead, Item key) {
		if (key.getId() != KEY_ID)
			return false;
		key.remove();
		if (getFreeKeyIndex(player) == -1) {
			int keyIndex = AttributeExtensions.getCharges(AttributeTypes.WILDERNESS_KEY, key);
			for (Item item : dead.wildernessKeyItems[keyIndex]) {
				new GroundItem(item).owner(player).position(dead.getPosition()).spawn();
			}
			dead.wildernessKeyItems[keyIndex] = null;
			return true;
		}
		int keyIndex = AttributeExtensions.getCharges(AttributeTypes.WILDERNESS_KEY, key);
		List<Item> lostItems = new ArrayList<>();
		for (Item item : dead.wildernessKeyItems[keyIndex]) {
			if (item == null)
				continue;
			lostItems.add(item);
		}
		player.wildernessKeyItems[keyIndex] = lostItems.toArray(new Item[0]);

		Item k = new Item(KEY_ID, 1);
		k.getDef().neverProtect = true;
		AttributeExtensions.setCharges(AttributeTypes.WILDERNESS_KEY, k, keyIndex);
		player.getInventory().add(k);
		player.sendMessage("You have received a wilderness key.");
		setSkull(player);

//		RareDropEmbedMessage.sendReceivedKeyLog(player, dead, lostItems, false);

		var items = new JSONArray();
		for (Item item : lostItems) {
			var itemJson = new JSONObject();
				itemJson.put("id", item.getId());
				itemJson.put("name", item.getDef().getName());
				itemJson.put("amount", item.getAmount());
			items.put(itemJson);
		}

		var jsonObject = new JSONObject();
			jsonObject.put("player", player.getName());
			jsonObject.put("death_x", dead.getPosition().getX());
			jsonObject.put("death_y", dead.getPosition().getY());
			jsonObject.put("death_z", dead.getPosition().getZ());
			jsonObject.put("dead_player", dead.getName());
			jsonObject.put("key_action", "received");
			jsonObject.put("lost_items", items);

		WildernessKeyHook.sendReceivedKeyLog(jsonObject);
		return true;
	}

	private static int countKeys(Player player) {
		int count = 0;
		for (int i = 0; i < 28; i++) {
			if (player.getInventory().get(i) == null || player.getInventory().get(i).getId() != KEY_ID)
				continue;
			count++;
		}
		return count;
	}

	private static int getFreeKeyIndex(Player player) {
		List<Integer> usedIndexes = new ArrayList<>();
		for (int i = 0; i < 28; i++) {
			if (player.getInventory().get(i) == null || player.getInventory().get(i).getId() != KEY_ID)
				continue;
			usedIndexes.add(AttributeExtensions.getCharges(AttributeTypes.WILDERNESS_KEY, player.getInventory().get(i)));
		}
		for (int i = 0; i < 5; i++)
			if (!usedIndexes.contains(i))
				return i;
		return -1;
	}

	public static boolean open(Player player, Item key) {
		if (key == null)
			return false;
		if (player.getGameMode() != GameMode.STANDARD) {
			player.sendMessage("You can't open wilderness keys on this game mode.");
			return false;
		}
		int keyIndex = AttributeExtensions.getCharges(AttributeTypes.WILDERNESS_KEY, key);
		List<Item> lootedItems = List.of(player.wildernessKeyItems[keyIndex]);
//		RareDropEmbedMessage.openWildernessKeyLog(player, lootedItems);

		var items = new JSONArray();
		for (Item item : lootedItems) {
			var itemJson = new JSONObject();
				itemJson.put("id", item.getId());
				itemJson.put("name", item.getDef().getName());
				itemJson.put("amount", item.getAmount());
			items.put(itemJson);
		}

		var jsonObject = new JSONObject();
			jsonObject.put("player", player.getName());
			jsonObject.put("looted_items", items);

		WildernessKeyHook.openWildernessKeyLog(jsonObject);

		for (Item item : player.wildernessKeyItems[keyIndex]) {
			int added = player.getBank().add(item.getId(), item.getAmount());
			if (added <= 0) {
				new GroundItem(item).owner(player).position(player.getPosition()).spawn();
				player.sendMessage("Dropped " + item.getAmount() + "x " + item.getDef().name + " to the ground as you don't have space.");
			} else {
				player.sendMessage("Added " + item.getAmount() + "x " + item.getDef().name + " to your bank.");
			}
		}
		key.remove();
		player.wildernessKeyItems[keyIndex] = null;
		setSkull(player);
		return true;
	}

	private static Item getKey(Player player) {
		for (int i = 0; i < 28; i++) {
			Item item = player.getInventory().get(i);
			if (item == null || item.getId() != KEY_ID)
				continue;
			return item;
		}
		return null;
	}

	public static void setSkull(Player player) {
		int keyCount = countKeys(player);
		if (keyCount > 5)
			keyCount = 5;
		if (keyCount > 0) {
			player.getAppearance().setSkullIcon(14 + keyCount);
		} else player.getAppearance().setSkullIcon(player.getCombat().isSkulled() ? 0 : -1);
	}


	public static boolean open(Player player) {
		Item key = getKey(player);
		if (key == null)
			return false;
		int keyIndex = AttributeExtensions.getCharges(AttributeTypes.WILDERNESS_KEY, key);
		List<Item> lootedItems = List.of(player.wildernessKeyItems[keyIndex]);
		for (Item item : player.wildernessKeyItems[keyIndex]) {
			int added = player.getBank().add(item.getId(), item.getAmount());
			if (added <= 0) {
				new GroundItem(item).owner(player).position(player.getPosition()).spawn();
				player.sendMessage("Dropped " + item.getAmount() + "x " + item.getDef().name + " to the ground as you don't have space.");
			} else {
				player.sendMessage("Added " + item.getAmount() + "x " + item.getDef().name + " to your bank.");
			}
		}
//		RareDropEmbedMessage.openWildernessKeyLog(player, lootedItems);

		var items = new JSONArray();
		for (Item item : lootedItems) {
			var itemJson = new JSONObject();
				itemJson.put("id", item.getId());
				itemJson.put("name", item.getDef().getName());
				itemJson.put("amount", item.getAmount());
			items.put(itemJson);
		}

		var jsonObject = new JSONObject();
			jsonObject.put("player", player.getName());
			jsonObject.put("looted_items", items);

		WildernessKeyHook.openWildernessKeyLog(jsonObject);

		key.remove();
		player.wildernessKeyItems[keyIndex] = null;
		setSkull(player);
		return true;
	}
}
