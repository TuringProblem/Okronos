package discord.webhooks.notifications;

import discord.comp.impl.Embed;
import discord.comp.impl.Field;
import discord.comp.impl.Message;
import discord.comp.impl.Thumbnail;
import discord.webhooks.Webhook;
import org.json.JSONObject;
import properties.ServerProperties;

import java.util.ArrayList;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
public interface GlobalBroadcastHook {

	static void sendGlobalSpawnedMessage(JSONObject dto) {
		var thumbnail = new Thumbnail();
			thumbnail.setUrl("https://oldschool.runescape.wiki/images/Last_Stand_detail.png?72827");

		var embedMessage = new Embed();
			embedMessage.setTitle("World boss Spawn!");
			embedMessage.setDescription("`%s` %s"
				.formatted(
					dto.getString("boss"),
					dto.getString("description")
				));
			embedMessage.setColor(8917522);
			embedMessage.setThumbnail(thumbnail);

		var message = new Message();
			message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("discord_hook_global_broadcast", ""), message);
	}

	static void sendWellMessage(JSONObject object) {
		var thumbnail = new Thumbnail();
			thumbnail.setUrl("https://oldschool.runescape.wiki/images/Treasure_Arbiter_detail.png?fb1b2");

		var perkArray = object.getJSONArray("enabled_perks");
		var fields = new ArrayList<Field>();
		for (int i = 0; i < perkArray.length(); i++) {
			var perk = perkArray.getJSONObject(i);
			fields.add(
				new Field(
					perk.getString("perk_name"),
					perk.getString("perk_description"),
					false
				)
			);
		}

		var embedMessage = new Embed();
			embedMessage.setTitle("Camel Statue Activated!");
			embedMessage.setColor(8917522);
			embedMessage.setFields(fields.toArray(Field[]::new));
			embedMessage.setThumbnail(thumbnail);

		var message = new Message();
			message.setEmbeds(embedMessage);

		Webhook.send(ServerProperties.get("discord_hook_global_broadcast", ""), message);
	}
}
