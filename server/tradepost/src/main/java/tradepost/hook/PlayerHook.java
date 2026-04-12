package tradepost.hook;

import io.ruin.HooksV2.Result;
import io.ruin.model.entity.player.Player;
import tradepost.TradePost;
import tradepost.TradePostCoffer;
import tradepost.db.TradePostCoffersDb;
import tradepost.db.TradePostHistoriesDb;

public class PlayerHook {

	public static void register() {
		Player.hooks.register(Player.Hook.OnStart.class, PlayerHook::onStart);
	}

	public static Result onStart(Player.Hook.OnStart ctx) {
		var player = ctx.player();
		TradePost.loadSlots(player);
		TradePostCoffersDb.loadCoffer(player);
		TradePostHistoriesDb.load(player);
		TradePostCoffer.load(player);
		return Result.Pass;
	}
}
