package inter.ikod.hook;

import discord.webhooks.logs.WildernessKeyHook;
import discord.webhooks.logs.WildernessPvpKillHook;
import inter.ikod.IKOD;
import io.ruin.HooksV2.Result;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.PlayerCombat;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.wilderness_keys.WildernessKeyHandler;
import io.ruin.model.map.ground.GroundItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlayerCombatHook implements PlayerCombat.Hook {

	public static void register() {
		PlayerCombat.hooks.register(PlayerCombat.Hook.OnDeath.class, PlayerCombatHook::handle);
	}

	private static Result handle(PlayerCombat.Hook.OnDeath ctx) {
		var player = ctx.player();
		var killer = ctx.killer();
		var pKiller = ctx.pKiller();
		// TODO(polish) - split the module to interface/ikod logic
		// interface should handle just interface stuff, items kept on death is pure
		// logic

		var lostItems = new ArrayList<Item>();
		IKOD.forLostItem(player, killer, item -> {
			if(item.getId() == 30464) {
				if(item.hasAttributes()) {
					item.remove();
					return;
				}
			}
			item.clearAttributes();
			ObjType def = item.getDef();
			if (pKiller != null && pKiller.hideFreeItems && def.free) {
				return;
			}

			if (pKiller == null || !def.tradeable)
				new GroundItem(item).owner(player).position(player.getPosition()).spawn(60);
			else if (pKiller.getGameMode().isIronMan())
				new GroundItem(item).owner(player).position(player.getPosition()).diedToIron(player).spawn(1);
			else {
				// new GroundItem(item).owner(pKiller).position(player.getPosition()).spawn();
				lostItems.add(item);
			}
		});

		if (pKiller != null) {
			List<Item> itemsToLose = new ArrayList<>(lostItems);
			if (!lostItems.isEmpty()) {
				if (!WildernessKeyHandler.handlePlayerKill(pKiller, lostItems)) {
					for (Item item : lostItems) {
						if (WildernessKeyHandler.handleDyingWithKey(pKiller, player, item))
							continue;
						new GroundItem(item).owner(pKiller).position(player.getPosition()).spawn();
					}
//					RareDropEmbedMessage.sendPvpDeathWebhook(pKiller, player, itemsToLose);

					var items = new JSONArray();
					for (Item item : itemsToLose) {
						var itemJson = new JSONObject();
							itemJson.put("id", item.getId());
							itemJson.put("name", item.getDef().getName());
							itemJson.put("amount", item.getAmount());
						items.put(itemJson);
					}
					var jsonObject = new JSONObject();
						jsonObject.put("player", pKiller.getName());
						jsonObject.put("death_x", player.getPosition().getX());
						jsonObject.put("death_y", player.getPosition().getY());
						jsonObject.put("death_z", player.getPosition().getZ());
						jsonObject.put("dead_player", player.getName());
						jsonObject.put("lost_items", items);

					WildernessPvpKillHook.sendPvpDeathWebhook(jsonObject);
				}
				else {
//					RareDropEmbedMessage.sendReceivedKeyLog(pKiller, player, itemsToLose, false);

					var items = new JSONArray();
					for (Item item : lostItems) {
						var itemJson = new JSONObject();
							itemJson.put("id", item.getId());
							itemJson.put("name", item.getDef().getName());
							itemJson.put("amount", item.getAmount());
						items.put(itemJson);
					}

					var jsonObject = new JSONObject();
						jsonObject.put("player", pKiller.getName());
						jsonObject.put("death_x", player.getPosition().getX());
						jsonObject.put("death_y", player.getPosition().getY());
						jsonObject.put("death_z", player.getPosition().getZ());
						jsonObject.put("dead_player", player.getName());
						jsonObject.put("key_action", "received");
						jsonObject.put("lost_items", items);

					WildernessKeyHook.sendReceivedKeyLog(jsonObject);
				}
			}
		}
		return Result.Pass;
	}
}
