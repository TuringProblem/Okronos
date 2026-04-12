package io.ruin.services.discord.old.impl;

import io.ruin.api.utils.ServerWrapper;
import io.ruin.model.World;
import io.ruin.services.discord.old.Webhook;
import io.ruin.services.discord.old.util.Embed;
import io.ruin.services.discord.old.util.Footer;
import io.ruin.services.discord.old.util.Message;
import io.ruin.services.discord.old.util.Thumbnail;

public class ShutdownEmbedMessage {

	public static void sendDiscordMessage(String shutdownMessage) {
        /*
        if (!World.isLive()){
            return;
        }
        try {
            Webhook webhook = new Webhook("");
            Message message = new Message();

            Embed embedMessage = new Embed();
            embedMessage.setTitle("Shutdown Announcement");
            embedMessage.setDescription(shutdownMessage);
            embedMessage.setColor(8917522);


            Thumbnail thumbnail = new Thumbnail();
            thumbnail.setUrl("https://static.runelite.net/cache/item/icon/" + 13307 + ".png");
            embedMessage.setThumbnail(thumbnail);


            Footer footer = new Footer();
            footer.setText(World.type.getWorldName() + " - Wasting Your Life One Skill At A Time!");
            embedMessage.setFooter(footer);


            message.setEmbeds(embedMessage);
            webhook.sendMessage(message.toJson());
        } catch (Exception e) {
            ServerWrapper.logError("Failed to send discord embed", e);
        }

         */

	}

}
