package player.mongo.hook;

import io.ruin.model.entity.player.Player;
import player.mongo.SaveScheduler;

public class HookPlayer {

	public static void register() {
		Player.hooks.registerSilent(Player.Hook.OnRemoved.class, ctx -> {
			SaveScheduler.queue(ctx.player());
		});
	}
}
