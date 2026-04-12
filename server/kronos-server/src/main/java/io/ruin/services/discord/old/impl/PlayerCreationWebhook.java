package io.ruin.services.discord.old.impl;

import io.ruin.api.utils.ServerWrapper;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.offline_player.PlayerProfileService;
import io.ruin.services.discord.old.Webhook;
import io.ruin.services.discord.old.util.Embed;
import io.ruin.services.discord.old.util.Field;
import io.ruin.services.discord.old.util.Message;
import properties.ServerProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-06-19
 */
@Deprecated(forRemoval = true)
public class PlayerCreationWebhook {

	private static final Webhook WEBHOOK = new Webhook(ServerProperties.get("account_creation_discord_hook", ""));

	public static void sendAccountCreationHook(Player player) {
		if (!World.isLive())
			return;
		try {
			var message = new Message();
			var embedMessage = new Embed();
				embedMessage.setTitle("Newcomer Event");
				embedMessage.setDescription("`%s` has just joined Reason".formatted(player.getName()));
				embedMessage.setColor(8917522);
				embedMessage.setFields(
					new Field("HWID", player.hwid, true),
					new Field("Created on", LocalDateTime.now().toString(),true)
				);
				message.setEmbeds(embedMessage);
			WEBHOOK.sendMessage(message.toJson());
		}
		catch (Exception e) {
			ServerWrapper.logError("Failed to send discord embed", e);
		}
	}
}
