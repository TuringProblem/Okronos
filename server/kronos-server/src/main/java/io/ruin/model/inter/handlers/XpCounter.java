package io.ruin.model.inter.handlers;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SlotAction;
import io.ruin.model.var.VarPlayerRepository;

public class XpCounter {

	public static void select(Player player, int option) {
		if (option == 1) {
			/**
			 * Toggle
			 */
			boolean enabled = VarPlayerRepository.XP_COUNTER_SHOWN.toggle(player) == 1;
			//int childId = player.isFixedScreen() ? 16 : 7;
			if (enabled)
				player.getPacketSender().sendToplevelSubInterface(ToplevelComponent.XP_TRACKER);
			else
				player.getPacketSender().removeInterface(ToplevelComponent.XP_TRACKER);
		} else {
			/**
			 * Setup
			 */
			player.getPacketSender().sendVarp(638, 0); //selected stat << 23 | selected tracker << 28
			player.getPacketSender().sendVarp(261, 0); //start point
			player.getPacketSender().sendVarp(262, 0); //end point
			player.getPacketSender().sendClientScript(917, "ii", -1, -1);
			player.openInterface(ToplevelComponent.MAINMODAL, 137);
			player.getPacketSender().sendIfEvents(137, 51, 1, 3, 2);
			player.getPacketSender().sendIfEvents(137, 52, 1, 3, 2);
			player.getPacketSender().sendIfEvents(137, 53, 1, 4, 2);
			player.getPacketSender().sendIfEvents(137, 54, 1, 32, 2);
			player.getPacketSender().sendIfEvents(137, 55, 1, 32, 2);
			player.getPacketSender().sendIfEvents(137, 56, 1, 8, 2);
			player.getPacketSender().sendIfEvents(137, 57, 1, 2, 2);
			player.getPacketSender().sendIfEvents(137, 58, 1, 3, 2);
			player.getPacketSender().sendIfEvents(137, 17, 0, 24, 2);
		}
	}

	public static void register() {
		InterfaceHandler.register(137, h -> {
			h.actions[51] = (SlotAction) (player, slot) -> VarPlayerRepository.XP_COUNTER_POSITION.set(player, slot - 1);
			h.actions[52] = (SlotAction) (player, slot) -> VarPlayerRepository.XP_COUNTER_SIZE.set(player, slot - 1);
			h.actions[58] = (SlotAction) (player, slot) -> VarPlayerRepository.XP_COUNTER_SPEED.set(player, slot - 1);
			h.actions[53] = (SlotAction) (player, slot) -> VarPlayerRepository.XP_COUNTER_DURATION.set(player, slot - 1);
			h.actions[54] = (SlotAction) (player, slot) -> VarPlayerRepository.XP_COUNTER_COUNTER.set(player, slot - 1);
			h.actions[55] = (SlotAction) (player, slot) -> VarPlayerRepository.XP_COUNTER_PROGRESS_BAR.set(player, slot - 1);
			h.actions[56] = (SlotAction) (player, slot) -> VarPlayerRepository.XP_COUNTER_COLOUR.set(player, slot - 1);
			h.actions[57] = (SlotAction) (player, slot) -> VarPlayerRepository.XP_COUNTER_GROUP.set(player, slot - 1);
		});
	}

}
