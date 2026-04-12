package io.ruin.model.activities.wilderness;

import io.ruin.model.activities.pvminstances.InstanceDialogue;
import io.ruin.model.activities.pvminstances.InstanceType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.object.actions.ObjectAction;

public class CorporealBeastCave {

	private static void goThroughPassage(Player player, GameObject cave) {
		/**
		 * TODO - corp beast interface overlay
		 * 	public static final int CORP_BEAST_DAMAGE = 1142;
		 * 	Widget ID = 13
		 */
		int x = player.getAbsX();
		if (x < cave.getPosition().getX()) {
			player.getMovement().teleport(x + 4, player.getPosition().getY(), player.getHeight());
		} else {
			player.getMovement().teleport(x - 4, player.getPosition().getY(), player.getHeight());
		}
	}

	private static void enterCave(Player player) {
		if (player.getCombat().tbTicks >= 1) {
			player.sendMessage("You are currently teleblocked.");
			return;
		}
		if (player.getCombat().isDefending(5)) {
			player.sendMessage("You cannot enter the cave whilst in combat.");
			return;
		}

		player.getMovement().teleport(2964, 4382, 2);
	}

	private static void exitCave(Player player) {
		player.dialogue(
			new MessageDialogue("The exit leads to the Wilderness, are you sure?"),
			new OptionsDialogue(new Option("Yes", () -> player.getMovement().teleport(3206, 3681, 0)), new Option("No", player::closeDialogue))
		);
	}

	public static void register() {
		ObjectAction.register(677, "go-through", CorporealBeastCave::goThroughPassage);
		ObjectAction.register(678, "enter", (player, obj) -> enterCave(player));
		ObjectAction.register(679, "exit", (player, obj) -> exitCave(player));
		MapListener.registerRegion(11844)
			.onExit((p, logout) -> {
				p.closeInterface(ToplevelComponent.OVERLAY);
				VarPlayerRepository.CORPOREAL_BEAST_DAMAGE.set(p, 0);
			}).onEnter(p -> {
				p.openInterface(ToplevelComponent.OVERLAY, 13);
				VarPlayerRepository.CORPOREAL_BEAST_DAMAGE.set(p, 0);
			});
		ObjectAction.register(9370, 2966, 4379, 2, 1, (player, obj) -> {
			InstanceDialogue.open(player, InstanceType.CORP);
		});
	}
}
