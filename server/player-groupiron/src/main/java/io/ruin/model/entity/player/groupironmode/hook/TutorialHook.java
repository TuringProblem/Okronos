package io.ruin.model.entity.player.groupironmode.hook;

import io.ruin.HooksV2.Result;
import io.ruin.model.entity.npc.actions.edgeville.StarterGuide;
import io.ruin.model.var.VarPlayerRepository;

public class TutorialHook {

	public static void register() {
		StarterGuide.hooks.register(StarterGuide.Hook.Finished.class, TutorialHook::handle);
	}

	private static Result handle(StarterGuide.Hook.Finished ctx) {
		var player = ctx.player();
		VarPlayerRepository.JOURNAL_TAB_ADVENTURE_PATHS_ENABLED.set(player, player.isGroupIronman() ? 1 : 0);
		return Result.Pass;
	}
}
