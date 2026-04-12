package discord.webhooks.logs;

import discord.webhooks.Webhook;
import org.json.JSONObject;
import properties.ServerProperties;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
public interface SuspiciousTransactionHook {

	static void sendHookToSusLogsOnDiscord(JSONObject dto) {
		Webhook.send(
			ServerProperties.get("discord_hook_sus_transaction", ""),
			TradeBetweenPlayersHook.buildEmbed(dto)
		);
	}
}
