package io.ruin.model.entity.player.groupironmode.hook;

import io.ruin.HooksV2.Result;
import io.ruin.cache.Color;
import io.ruin.model.activities.DonationBossHandler;
import io.ruin.model.activities.VoteBossHandler;
import io.ruin.model.activities.tempevents.summerevent.SummerEvent;
import io.ruin.model.combat.Killer;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.entity.player.PlayerCombat;
import io.ruin.model.var.VarPlayerRepository;
import lombok.experimental.ExtensionMethod;

import static io.ruin.model.entity.player.GameMode.IRONMAN;

@ExtensionMethod(Attributes.class)
public class GroupDeathHook {

	public static void register() {
		PlayerCombat.hooks.register(PlayerCombat.Hook.OnDeath.class, GroupDeathHook::handle);
	}

	private static Result handle(PlayerCombat.Hook.OnDeath ctx) {
		var player = ctx.player();
		var killer = ctx.killer();
		var pKiller = ctx.pKiller();

		if (player.getGameMode() != GameMode.GROUP_IRONMAN && player.getGameMode() != GameMode.HARDCORE_GROUP_IRONMAN) {
			return Result.Break;
		}

		if (DonationBossHandler.map != null && DonationBossHandler.map.isIn(player)) {
			return Result.Break;
		}

		if (VoteBossHandler.map != null && VoteBossHandler.map.isIn(player)) {
			return Result.Break;
		}

		if (SummerEvent.map != null && SummerEvent.map.isIn(player)) {
			return Result.Break;
		}

		if (player.getPosition().regionId() == 11576) {
			return Result.Break;
		}

		if (player.getGameMode() == GameMode.HARDCORE_GROUP_IRONMAN) {
			var group = player.getGroupIron();
			if (group == null) {
				return Result.Break;
			}
			group.updateGroupLives(-1);
			player.sendMessage(
					Color.RED.wrap("You have fallen as a Hardcore Group Ironman, one of your group lives has been lost!"));

			if (group.getLivesRemaining() < 1) {
				player.sendMessage("Your group has lost all of their lives and your hardcore group status has been revoked.");
				VarPlayerRepository.IRONMAN_MODE.set(player, 4);
			}
		}
		return Result.Pass;
	}
}
