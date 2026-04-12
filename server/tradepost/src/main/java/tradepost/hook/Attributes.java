package tradepost.hook;

import static player.attributes.api.API.*;

import io.ruin.model.entity.player.Player;
import tradepost.TradePostPlayer;

public class Attributes {

	public static void register() {
		attrib().register().temporary(TradePostPlayer.class, TradePostPlayer::new);
	}

	public static TradePostPlayer tradePost(Player player) {
		return attrib(TradePostPlayer.class, player);
	}
}
