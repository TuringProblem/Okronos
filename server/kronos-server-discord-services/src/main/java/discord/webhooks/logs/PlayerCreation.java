package discord.webhooks.logs;

import discord.comp.impl.Embed;
import discord.comp.impl.Field;
import discord.comp.impl.Message;
import discord.webhooks.Webhook;
import org.json.JSONObject;
import properties.ServerProperties;

import java.awt.*;
import java.time.LocalDateTime;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
public interface PlayerCreation {

	static void createAnsSendWebhookMessageWithEmbed(JSONObject object) {
		var message = new Message();
		var embedMessage = new Embed();
			embedMessage.setTitle("Newcomer Event");
			embedMessage.setDescription("`%s` has just joined Reason"
				.formatted(object.getString("player")));
			embedMessage.setColor(new Color(255, 0, 255));
			embedMessage.setFields(
				new Field("HWID", object.getString("hwid"), true),
				new Field("Created on", LocalDateTime.now().toString(),true)
			);
		message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("account_creation_discord_hook", ""), message);
	}
}
