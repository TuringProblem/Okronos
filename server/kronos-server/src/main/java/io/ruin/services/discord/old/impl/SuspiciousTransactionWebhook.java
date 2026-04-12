package io.ruin.services.discord.old.impl;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.services.discord.old.Webhook;
import io.ruin.services.discord.old.util.Embed;
import io.ruin.services.discord.old.util.Field;
import io.ruin.services.discord.old.util.Footer;
import io.ruin.services.discord.old.util.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-06-21
 */
@Deprecated(forRemoval = true)
public class SuspiciousTransactionWebhook {

	public static void sendHookToSusLogsOnDiscord(Player suspect, String susReason, Map<String, String> additionalInfo) {
		if (!World.isLive()) {
			return;
		}
		var webhook = new Webhook(
			"https://discord.com/api/webhooks/1386167749545427006/sky_gjpXx0ZWVFXjqEqCQOBBooE71_wwTSUjZfK_fa41haCz70svU2fNKTsGLdGHtNjK");
		// time stamp
		var footer = new Footer();
		footer.setText(LocalDateTime.now().toString());
		// The embed
		var embed = new Embed();
		embed.setColor(8917522);
		embed.setTitle("Suspicious activity detected!");
		embed.setDescription(suspect.getName().concat(" has been flagged for ").concat(susReason));
		embed.setFooter(footer);
		if (!additionalInfo.isEmpty()) {
			var fields = new ArrayList<Field>();
			additionalInfo.forEach((key, value) ->
				fields.add(new Field(key, value, false)));
			embed.setFields(fields.toArray(Field[]::new));
		}

		var message = new Message();
		message.setEmbeds(embed);
		webhook.sendMessage(message.toJson());
	}
}
