package io.ruin.model.map.object.actions.impl.dungeons;

import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;

public class GnomeStronghold {

	public static void register() {
		/**
		 * Entrance/exit
		 */


		ObjectAction.register(26709, 2428, 3423, 0, "enter", (player, obj) -> player.startEvent(event -> {
			player.lock();
			player.animate(2796);
			event.delay(2);
			player.resetAnimation();
			player.getMovement().teleport(2429, 9824, 0);
			player.unlock();
		}));
		ObjectAction.register(27257, 2430, 9824, 0, "use", (player, obj) -> player.startEvent(event -> {
			player.lock();
			player.animate(2796);
			event.delay(2);
			player.resetAnimation();
			player.getMovement().teleport(2430, 3424, 0);
			player.unlock();
		}));
		ObjectAction.register(30175, 2430, 9807, 0, "enter", (player, obj) -> player.startEvent(event -> {
			if (!player.getStats().check(StatType.Agility, 72, "use this shortcut"))
				return;
			player.lock();
			player.animate(2796);
			event.delay(2);
			player.resetAnimation();
			player.getMovement().teleport(2435, 9807, 0);
			player.unlock();
		}));
		ObjectAction.register(30174, 2430, 9806, 0, "enter", (player, obj) -> player.startEvent(event -> {
			if (!player.getStats().check(StatType.Agility, 72, "use this shortcut"))
				return;
			player.lock();
			player.animate(2796);
			event.delay(2);
			player.resetAnimation();
			player.getMovement().teleport(2435, 9806, 0);
			player.unlock();
		}));
		ObjectAction.register(30175, 2434, 9806, 0, "enter", (player, obj) -> player.startEvent(event -> {
			if (!player.getStats().check(StatType.Agility, 72, "use this shortcut"))
				return;
			player.lock();
			player.animate(2796);
			event.delay(2);
			player.resetAnimation();
			player.getMovement().teleport(2429, 9806, 0);
			player.unlock();
		}));
		ObjectAction.register(30174, 2434, 9807, 0, "enter", (player, obj) -> player.startEvent(event -> {
			if (!player.getStats().check(StatType.Agility, 72, "use this shortcut"))
				return;
			player.lock();
			player.animate(2796);
			event.delay(2);
			player.resetAnimation();
			player.getMovement().teleport(2429, 9807, 0);
			player.unlock();
		}));
	}

}

