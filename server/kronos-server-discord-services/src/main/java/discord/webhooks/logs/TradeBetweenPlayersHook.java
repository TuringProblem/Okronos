package discord.webhooks.logs;

import discord.comp.impl.Embed;
import discord.comp.impl.Field;
import discord.comp.impl.Footer;
import discord.comp.impl.Message;
import discord.webhooks.Util;
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
public interface TradeBetweenPlayersHook {

	static void sendTradeLogsToDiscord(JSONObject object) {
		Webhook.send(
			ServerProperties.get("discord_hook_trade_player", ""),
			buildEmbed(object)
		);
	}

	static void sendTradeLogsToDiscordForOwner(JSONObject object) {
		Webhook.send(
			ServerProperties.get("discord_hook_admin_actions", ""),
			buildEmbed(object)
		);
	}

	static Message buildEmbed(JSONObject object) {
		var sb = new StringBuilder();
		var primaryPlayerItems = object.getJSONArray("primary_trade_items");
		for (int i = 0; i < primaryPlayerItems.length(); i++) {
			var item = primaryPlayerItems.getJSONObject(i);
			sb.append(item.getInt("amount"))
				.append(" x ")
				.append(item.getString("name"))
				.append("\n");
		}
		var sb2 = new StringBuilder();
		var secondaryPlayerItems = object.getJSONArray("secondary_trade_items");
		for (int i = 0; i < secondaryPlayerItems.length(); i++) {
			var item = secondaryPlayerItems.getJSONObject(i);
			sb2.append(item.getInt("amount"))
				.append(" x ")
				.append(item.getString("name"))
				.append("\n");
		}
		var embedMessage = new Embed();
			embedMessage.setTitle("Trade Exchange Event");
			embedMessage.setDescription("`%s` has traded with `%s`"
				.formatted(
					object.getString("player"),
					object.getString("recipient")
				)
			);
			embedMessage.setColor(new Color(32, 96, 255));
			embedMessage.setFields(
				new Field(
				"Trade Location",
				Util.getMapLocation(
					object.getInt("trade_x"),
					object.getInt("trade_y"),
					object.getInt("trade_z")
				), false));


		var primaryTradeEmbed = new Embed();
			primaryTradeEmbed.setTitle("`%s`'s Items"
				.formatted(object.getString("player")));
			primaryTradeEmbed.setDescription(sb.toString());
			primaryTradeEmbed.setColor(new Color(119, 7, 104));

		var secondaryTradeEmbed = new Embed();
			secondaryTradeEmbed.setTitle("`%s`'s Items"
				.formatted(object.getString("recipient")));
			secondaryTradeEmbed.setDescription(sb2.toString());
			secondaryTradeEmbed.setColor(new Color(3, 98, 149));

		var message = new Message();
			message.setEmbeds(embedMessage, primaryTradeEmbed, secondaryTradeEmbed);

		return message;
	}
}
