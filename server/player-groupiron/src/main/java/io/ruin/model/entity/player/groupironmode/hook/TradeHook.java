package io.ruin.model.entity.player.groupironmode.hook;

import io.ruin.HooksV2.Result;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.item.containers.Trade;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(Attributes.class)
public class TradeHook implements Trade.Hook {

	public static void register() {
		Trade.hooks.register(Trade.Hook.OnRequest.class, TradeHook::handle);
	}

	private static Result handle(Trade.Hook.OnRequest ctx) {
		var player = ctx.player();
		var target = ctx.target();

		var admin = player.isManager() || target.isAdmin() || target.isManager();
		var pGameMode = player.getGameMode();
		if (admin) {
			return Result.Pass;
		}

		if (pGameMode.isAnyOf(GameMode.HARDCORE_GROUP_IRONMAN, GameMode.GROUP_IRONMAN)) {
			var pGroup = player.newGroupId;
			var targetGroup = target.newGroupId;

			if (pGroup == 0) {
				player.sendMessage("You are not in a group.");
				return Result.Return;
			}

			if (pGroup != targetGroup) {
				player.sendMessage("This player is not in your group.");
				return Result.Return;
			}
		}

		if (player.getGameMode().isIronMan() && !target.isManager() && !player.isManager()) {
			if (player.getGroupIron() != null && player.newGroupId != target.newGroupId) {
				player.sendMessage("Ironman stand alone.");
				return Result.Return;
			}
		}

		if (target.getGameMode().isIronMan() && !target.getGameMode().isGroupIronman()
				&& !target.getGameMode().isHardcoreGroupIronman()) {
			player.sendMessage(target.getName() + " is an ironman and so cannot trade.");
			return Result.Return;
		}

		return Result.Pass;
	}

}
