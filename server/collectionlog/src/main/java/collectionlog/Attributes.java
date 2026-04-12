package collectionlog;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import io.ruin.model.entity.player.Player;
import lombok.extern.slf4j.Slf4j;
import player.attributes.PlayerAttributeCodec.LoadContext;

import java.util.function.Function;

import static player.attributes.api.API.attrib;

/**
 * Container of all accessors to extensions of a player provided by this module.
 * Whenever a class that is about to use these extensions can use lombok's
 * extension method so that it doesnt need to do attrib(cls, player), instead,
 * just player.attribute();
 */
@Slf4j
public class Attributes {

	public static void register() {
		attrib().register().persistent(CollectionLog.class, CollectionLog::new);
		attrib().register().temporary(CollectionLogUpdated.class, CollectionLogUpdated::new);
	}

	public static CollectionLog collectionLog(Player player) {
		return attrib(CollectionLog.class, player);
	}

	public static CollectionLogUpdated collectionLogUpdated(Player player) {
		return attrib(CollectionLogUpdated.class, player);
	}

}
