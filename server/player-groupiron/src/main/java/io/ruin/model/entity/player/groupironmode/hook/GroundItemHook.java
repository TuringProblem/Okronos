package io.ruin.model.entity.player.groupironmode.hook;

import io.ruin.HooksV2.Result;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.map.ground.GroundItem;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(Attributes.class)
public class GroundItemHook {

	public static void register() {
		GroundItem.hooks.register(GroundItem.Hook.Pickup.class, GroundItemHook::handle);
	}

	private static Result handle(GroundItem.Hook.Pickup ctx) {
		var item = ctx.item();
		var player = ctx.player();

		var originalOwner = item.originalOwner;
		var pGameMode = player.getGameMode();
		var pGroupIron = player.getGroupIron();

		if (pGroupIron == null) {
			return Result.Pass;
		}

		if (!pGameMode.isAnyOf(GameMode.GROUP_IRONMAN, GameMode.HARDCORE_GROUP_IRONMAN)) {
			return Result.Pass;
		}

		if (originalOwner == null || originalOwner.isEmpty()) {
			return Result.Pass;
		}

		if (originalOwner.equalsIgnoreCase(player.getName())) {
			return Result.Pass;
		}

		if (!pGroupIron.playerInGroup(originalOwner)) {
			player.sendMessage("You can't pick up items dropped by or for other players.");
			return Result.Return;
		}

		return Result.Pass;
	}

}
