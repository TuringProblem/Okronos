package io.ruin.model.entity.player.groupironmode.hook;

import io.ruin.HooksV2.Result;
import io.ruin.model.entity.player.PlayerAction;
import io.ruin.model.entity.player.groupironmode.GroupIronInvitation;
import io.ruin.model.map.route.routes.TargetRoute;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(Attributes.class)
public class PlayerActionHook {

	public static void register() {
		PlayerAction.hooks.register(PlayerAction.Hook.Handle.class, PlayerActionHook::handle);
	}

	private static Result handle(PlayerAction.Hook.Handle ctx) {
		var action = ctx.action();
		var player = ctx.player();
		var target = ctx.target();

		switch (action) {
			case PlayerAction.GIM_Invite -> {
				player.face(target);
				TargetRoute.set(player, target, () -> {
					GroupIronInvitation.invite(player, target);
				});
				player.faceNone(true);
			}
			default -> {
			}
		}
		return Result.Pass;
	}

}
