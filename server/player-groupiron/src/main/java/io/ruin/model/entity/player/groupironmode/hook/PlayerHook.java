package io.ruin.model.entity.player.groupironmode.hook;

import io.ruin.HooksV2.Result;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.entity.player.Player;
import io.ruin.model.var.VarPlayerRepository;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(Attributes.class)
public class PlayerHook {

	public static void register() {
		Player.hooks.register(Player.Hook.OnStart.class, PlayerHook::handle);
	}

	private static Result handle(Player.Hook.OnStart ctx) {
		var player = ctx.player();

		VarPlayerRepository.JOURNAL_TAB_ADVENTURE_PATHS_ENABLED.set(player, player.isGroupIronman() ? 1 : 0);
		loadGroupData(player);
		return Result.Pass;
	}

	public static void loadGroupData(Player player) {
		if (player.getGameMode() != GameMode.GROUP_IRONMAN && player.getGameMode() != GameMode.HARDCORE_GROUP_IRONMAN) {
			return;
		}

		int groupId = player.newGroupId;
		if (groupId <= 0) {
			return;
		}

		if (player.getGameMode() == GameMode.HARDCORE_GROUP_IRONMAN) {
			if (player.getGroupIron() != null && player.getGroupIron().getLivesRemaining() < 1) {
				player.sendMessage("Your group has lost all of their lives and your hardcore group status has been revoked.");
				VarPlayerRepository.IRONMAN_MODE.set(player, 4);
			}
		}
	}
}
