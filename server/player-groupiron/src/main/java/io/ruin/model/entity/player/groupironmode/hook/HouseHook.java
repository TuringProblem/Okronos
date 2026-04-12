package io.ruin.model.entity.player.groupironmode.hook;

import io.ruin.HooksV2.Result;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.skills.construction.HouseLocation;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(Attributes.class)
public class HouseHook {

	public static void register() {
		HouseLocation.hooks.register(HouseLocation.Hook.BeforeEnterFriendsHouse.class, HouseHook::handle);
	}

	private static Result handle(HouseLocation.Hook.BeforeEnterFriendsHouse ctx) {
		var player = ctx.player();
		var friend = ctx.friend();

		var pGroupIron = player.getGroupIron();
		if (player.getGameMode().isIronMan()
				&& (pGroupIron == null || !pGroupIron.playerInGroup(friend.getName()))) {
			player.dialogue(new MessageDialogue("Ironmen cannot enter other players's houses."));
			return Result.Return;
		}
		return Result.Pass;
	}

}
