package discord.webhooks.logs;

import discord.comp.impl.Embed;
import discord.comp.impl.Field;
import discord.comp.impl.Message;
import discord.webhooks.Webhook;
import org.json.JSONObject;
import properties.ServerProperties;

import java.awt.*;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
public interface PrivateMessageHook {

	static void sendPMLogsToDiscord(JSONObject object) {
		var message = new Message();
		var embedMessage = new Embed();
			embedMessage.setTitle("Private Message Event");
			embedMessage.setDescription("`%s` has spoke to `%s`"
				.formatted(
					object.getString("player"),
					object.getString("recipient"))
			);
			embedMessage.setColor(new Color(255, 0, 96));
			embedMessage.setFields(new Field("Message", object.getString("message"),true));
		message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("discord_hook_private_message", ""), message);
	}
}
