package io.ruin.services.discord.old.impl;

import io.ruin.api.utils.ServerWrapper;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.services.discord.old.Webhook;
import io.ruin.services.discord.old.util.Embed;
import io.ruin.services.discord.old.util.Field;
import io.ruin.services.discord.old.util.Message;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-06-19
 */
@Deprecated(forRemoval = true)
public class StaffCommandWebhook {

	private static final Webhook WEBHOOK = new Webhook("https://discord.com/api/webhooks/1385261003192926228/jnBuoXGcrPXA-BoWdmkHXanDLawheYSBv9rJCprcc5DlKl6ce9IypbsleRm39ttgmDRf");

	public static void sendCommandHook(Player player, String command, String... args) {
		if (!World.isLive()) return;
		try {
			var message = new Message();
			var embedMessage = new Embed();
				embedMessage.setTitle("Command executed");
				embedMessage.setDescription("`%s` has just executed command".formatted(player.getName()));
				embedMessage.setColor(8917522);
				embedMessage.setFields(
					new Field("Command", command, true),
					new Field("Arguments", String.join(" ", args), true)
				);

			message.setEmbeds(embedMessage);
			WEBHOOK.sendMessage(message.toJson());
		}
		catch (Exception e) {
			ServerWrapper.logError("Failed to send discord embed", e);
		}
	}
}
