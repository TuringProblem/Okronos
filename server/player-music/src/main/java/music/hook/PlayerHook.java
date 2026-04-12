package music.hook;

import io.ruin.HooksV2.Result;
import io.ruin.HooksV2.Hook;
import io.ruin.model.entity.player.Player;
import music.inter.MusicPlayer;

public class PlayerHook implements Hook<Player.Hook.OnStart> {

	public static void register() {
		Player.hooks.register(Player.Hook.OnStart.class, new PlayerHook());
	}

	@Override
	public Result handle(Player.Hook.OnStart ctx) {
		for (var track : MusicPlayer.tracks()) {
			if (!track.isUnlocked(ctx.player())) {
				track.unlock(ctx.player());
			}
		}
		return Result.Pass;
	}
}
