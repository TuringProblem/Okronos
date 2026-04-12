package player.chat.filter.attribute;

import static player.attributes.api.API.*;

import io.ruin.model.entity.player.Player;

public class Attributes {

	public static void register() {
		attrib().register().persistent(ChatFilterAttribute.class, ChatFilterAttribute::new);
	}

	public static ChatFilterAttribute chatFilter(Player player) {
		return attrib(ChatFilterAttribute.class, player);
	}
}
