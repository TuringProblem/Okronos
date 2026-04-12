package io.ruin.model.map.object.actions.impl.dungeons;

import io.ruin.model.activities.bosses.instancetoken.InstanceMaps;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.map.object.actions.impl.Ladder;

import java.time.Instant;

public class Varrock {

	public static void register() {
		/**
		 * Ladders
		 */
		ObjectAction.register(11806, 3237, 9858, 0, "climb-up", (player, obj) -> Ladder.climb(player, 3236, 3458, 0, true, true, false));
		ObjectAction.register(17385, 3230, 9904, 0, "climb-up", (player, obj) -> Ladder.climb(player, 3230, 3505, 0, true, true, false));

		/**
		 * Hole
		 */
		ObjectAction.register(7142, 3230, 3504, 0, "climb-down", (player, obj) -> Ladder.climb(player, 3229, 9904, 0, false, true, false));

		/**
		 * Manhole
		 */
		ObjectAction.register(881, "open", (player, obj) -> {
			obj.setId(882);
			player.sendFilteredMessage("You pull back the cover from over the manhole.");
		});
		ObjectAction.register(882, "close", (player, obj) -> {
			obj.setId(obj.originalId);
			player.sendFilteredMessage("You place the cover back over the manhole.");
		});
		ObjectAction.register(882, "climb-down", (player, obj) -> player.startEvent(event -> {
			 player.lock();
			 player.getMovement().teleport(3237, 9858, 0);
			player.unlock();
		}));
		ObjectAction.register(14203, 1, (player, obj) -> {
			player.sendMessage("Use private for now.");
		});
		ObjectAction.register(14203, 2, (player, obj) -> {
			player.getInstanceTokenInterface().selectedBoss = InstanceMaps.SCURRIUS;
			player.getInstanceTokenInterface().startInstance(player, true);
		});
		NPCAction.register(7615, "trade", (player, npc) -> {
			int spines = player.getInventory().getAmount(28798);
			if(spines < 1) {
				player.dialogue(new NPCDialogue(7615, "Come talk to me when you have Scurrius' spines to trade in for antique lamps."));
				return;
			}
			player.dialogue(new OptionsDialogue("Hand in " + spines + " Scurrius' spines for " + spines + " antique lamps?",
				new Option("Yes.", () -> {
					if(player.getInventory().getAmount(28798) < spines) {
						return;
					}
					player.getInventory().remove(28798, spines);
					player.getInventory().add(28800, spines);
					player.dialogue(new NPCDialogue(7615, "Thank you."));
				}),
				new Option("No.")));

		});
		ObjectAction.register(14204, "cross", (player, obj) -> {
			player.dialogue(new OptionsDialogue("Are you sure you want to leave the instance?",
				new Option("Yes.", () -> {
					player.getMovement().teleport(3281, 9868, 0);
				}),
				new Option("No.")));
		});
		ObjectAction.register(14204, "quick-escape", (player, obj) -> {
			player.getMovement().teleport(3281, 9868, 0);
		});
	}
}
