package discord.webhooks.logs;

import discord.comp.impl.Embed;
import discord.comp.impl.Field;
import discord.comp.impl.Message;
import discord.webhooks.Webhook;
import org.json.JSONObject;
import properties.ServerProperties;

import java.awt.*;

import static discord.webhooks.Util.getMapLocation;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
public interface GroundItemHook {

	static void sendPickupLogsToDiscord(JSONObject object, boolean manuallyDropped) {
		var noted = object.getBoolean("item_noted");

		var embedMessage = new Embed();
			embedMessage.setTitle("Item Pickup Event");
			embedMessage.setDescription("`%s` has just picked up `%s`"
				.formatted(object.getString("player"), object.getString("item_name")));
			embedMessage.setColor(new Color(15, 255, 15));
			embedMessage.setFields(
				new Field("Stack", noted ? "Noted" : "Item", true),
				new Field("Amount", object.getString("item_amount"), true),
				new Field("Did someone drop this?", String.valueOf(manuallyDropped),false),
				new Field("Location", getMapLocation(
					object.getInt("pickup_x"),
					object.getInt("pickup_y"),
					object.getInt("pickup_z")
				), false)
			);

		var message = new Message();
			message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("discord_hook_item_pickup", ""), message);
	}

	static void sendDropItemToDiscord(JSONObject object) {
		var noted = object.getBoolean("item_noted");

		var embedMessage = new Embed();
			embedMessage.setTitle("Item Drop Event");
			embedMessage.setDescription("`%s` has just dropped `%s`"
				.formatted(
					object.getString("player"),
					object.getString("item_name")
				)
			);
			embedMessage.setColor(new Color(15, 255, 15));
			embedMessage.setFields(
				new Field("Stack", noted ? "Noted" : "Item", true),
				new Field("Amount", object.getString("item_amount"), true),
				new Field("Location", getMapLocation(
					object.getInt("drop_x"),
					object.getInt("drop_y"),
					object.getInt("drop_z")
				), false)
			);

		var message = new Message();
			message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("discord_hook_item_drop", ""), message);
	}
}
