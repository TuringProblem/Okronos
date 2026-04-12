package discord.webhooks.logs;

import discord.comp.impl.Embed;
import discord.comp.impl.Field;
import discord.comp.impl.Message;
import discord.webhooks.Webhook;
import org.json.JSONObject;
import properties.ServerProperties;

import static discord.webhooks.Util.getMapLocation;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
public interface WildernessKeyHook {

	static void openWildernessKeyLog(JSONObject object) {
		var sb = new StringBuilder();
		var itemArray = object.getJSONArray("looted_items");
		for (int i = 0; i < itemArray.length(); i++) {
			var item = itemArray.getJSONObject(i);
			sb.append(item.getInt("amount"))
				.append(" x ")
				.append(item.getString("name"))
				.append("\n");
		}

		var message = new Message();
		var embedMessage = new Embed();
			embedMessage.setTitle("`%s` just opened a Wilderness key!"
				.formatted(object.getString("player")));
			embedMessage.setDescription(sb.toString());
			embedMessage.setColor(8917522);

		message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("discord_hook_wilderness_log", ""), message);
	}

	static void sendReceivedKeyLog(JSONObject object) {
		var sb = new StringBuilder();
		var itemArray = object.getJSONArray("lost_items");
		for (int i = 0; i < itemArray.length(); i++) {
			var item = itemArray.getJSONObject(i);
			sb.append(item.getInt("amount"))
				.append(" x ")
				.append(item.getString("name"))
				.append("\n");
		}

		var embedMessage = new Embed();
			embedMessage.setTitle("Wilderness Key Action");
			embedMessage.setDescription("`%s` just %s a wilderness key containing:%n%s"
				.formatted(object.getString("player"), object.get("key_action"), sb));
			embedMessage.setColor(8917522);
			embedMessage.setFields(
				new Field("Killed Player", object.getString("dead_player"), true),
				new Field("Death Location", getMapLocation(
					object.getInt("death_x"),
					object.getInt("death_y"),
					object.getInt("death_z")),
					true)
			);

		var message = new Message();
			message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("discord_hook_wilderness_log", ""), message);
	}

	static void sendDestroyKeyLog(JSONObject object) {
		var sb = new StringBuilder();
		var itemArray = object.getJSONArray("lost_items");
		for (int i = 0; i < itemArray.length(); i++) {
			var item = itemArray.getJSONObject(i);
			sb.append(item.getInt("amount"))
				.append(" x ")
				.append(item.getString("name"))
				.append("\n");
		}

		var embedMessage = new Embed();
			embedMessage.setTitle("Wilderness Key Action");
			embedMessage.setDescription("`%s` just %s a wilderness key containing:%n%s"
				.formatted(object.getString("player"), object.get("key_action"), sb));
			embedMessage.setColor(8917522);
			embedMessage.setFields(new Field("Killed Player", object.getString("dead_player"), true));

		var message = new Message();
			message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("discord_hook_wilderness_log", ""), message);
	}
}
