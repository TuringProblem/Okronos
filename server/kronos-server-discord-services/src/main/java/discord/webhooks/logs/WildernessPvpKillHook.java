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
public interface WildernessPvpKillHook {

	static void sendPvpDeathWebhook(JSONObject object) {
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
			embedMessage.setTitle("Wilderness PvP Drop");
			embedMessage.setDescription(sb.toString());
			embedMessage.setColor(8917522);
			embedMessage.setFields(
				new Field("Killer", object.getString("player"), true),
				new Field("Killed Player", object.getString("dead_player"), true),
				new Field("Death Location", getMapLocation(object.getInt("death_x"), object.getInt("death_y"), object.getInt("death_z")), true)
			);

		var message = new Message();
			message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("discord_hook_pvp_death", ""), message);
	}
}
