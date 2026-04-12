package discord.webhooks.notifications;

import discord.comp.impl.*;
import discord.webhooks.Webhook;
import java.util.function.Supplier;
import org.json.JSONObject;
import properties.ServerProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-21
 */
@Slf4j
public class RareDropHook {

	public static void sendDiscordMessage(Supplier<JSONObject> objectSupplier) {
		try {
			var object = objectSupplier.get();
			var mode = switch (object.get("game_mode").toString()) {
				case "IRONMAN" -> "[Ironman] ";
				case "HARDCORE_GROUP_IRONMAN" -> "[Hardcore Group Ironman] ";
				case "GROUP_IRONMAN" -> "[Group Ironman] ";
				case "HARDCORE_IRONMAN" -> "[Hardcore Ironman] ";
				case "ULTIMATE_IRONMAN" -> "[Ultimate Ironman] ";
				default -> "";
			};

			var thumbnail = new Thumbnail();
			thumbnail.setUrl("https://static.runelite.net/cache/item/icon/" + object.get("item_id") + ".png");

			var footer = new Footer();
			if (object.has("total_attempts")) {
				footer.setText("Kill count: " + object.get("total_attempts").toString());
			} else {
				footer.setText("Kill count: n/a");
			}

			var embedMessage = new Embed();
			embedMessage.setTitle(mode + "Rare drop received!");
			embedMessage.setDescription("`%s` has just received `%s`!"
					.formatted(object.get("player"), object.get("item_name")));
			embedMessage.setColor(8917522);
			embedMessage.setThumbnail(thumbnail);
			embedMessage.setFooter(footer);
			if (object.has("source"))
				embedMessage.setFields(new Field("Source:", object.get("source").toString(), false));

			var message = new Message();
			message.setEmbeds(embedMessage);

			Webhook.send(ServerProperties.get("discord_hook_rare_drop", ""), message);
		} catch (Exception e) {
			log.error("Webhook error: ", e);
		}
	}
}
