package discord.webhooks.logs;

import discord.comp.impl.Embed;
import discord.comp.impl.Field;
import discord.comp.impl.Message;
import discord.webhooks.Webhook;
import io.ruin.api.discord.dto.CommandNotificationType;
import org.json.JSONObject;
import properties.ServerProperties;

import java.awt.*;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
public interface AdministrationHook {

	static void sendAdminDBLogsToDiscord(JSONObject object) {
		var embedMessage = new Embed();
			embedMessage.setTitle("Admin Action Event");
			embedMessage.setDescription("`%s` has spawned a Donation Boss"
				.formatted(object.getString("user")));
			embedMessage.setColor(new Color(255, 96, 8));

		var message = new Message();
			message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("discord_hook_admin_actions", ""), message);
	}

	static void sendAdminDonorDealAmountLogsToDiscord(JSONObject object) {
		var embedMessage = new Embed();
			embedMessage.setTitle("Store Credit Event");
			embedMessage.setDescription("`%s` has given credit to `%s` "
				.formatted(
					object.getString("player"),
					object.getString("player_receiving")
				)
			);
			embedMessage.setColor(new Color(255, 96, 8));
			embedMessage.setFields(new Field("Amount", object.getString("amount"), true));

		var message = new Message();
			message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("discord_hook_admin_actions", ""), message);
	}

	static void sendAdminShopLogsToDiscord(JSONObject object) {
		var embedMessage = new Embed();
			embedMessage.setTitle("Admin Store Event");
			embedMessage.setDescription("`%s` has just %s an item. "
				.formatted(object.getString("player"), object.getString("exchange")));
			embedMessage.setColor(new Color(255, 96, 8));
			embedMessage.setFields(
				new Field("Cost", object.getString("cost"), false),
				new Field("Item", object.getString("item_name"), false),
				new Field("Item ID", object.get("item_id").toString(), true),
				new Field("Item Amount", object.get("item_amount").toString(), true)
			);

		var message = new Message();
			message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("discord_hook_admin_actions", ""), message);
	}

	static void sendCommandLogsToDiscordForOwner(CommandNotificationType cal, JSONObject object) {
		var embedMessage = new Embed();
			embedMessage.setTitle("Admin Command Event");
			embedMessage.setDescription("`%s` has just used a command. "
				.formatted(object.getString("player")));
			embedMessage.setColor(new Color(255, 96, 8));
			embedMessage.setFields(
				new Field("Command:", object.getString("command_name"), false),
				new Field("Arguments:", object.getString("command_args"), false)
			);

		var message = new Message();
			message.setEmbeds(embedMessage);

		Webhook.send(cal.getWebhook(), message);
	}
}
