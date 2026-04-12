package io.ruin.model.activities.wilderness;

import io.ruin.model.map.object.actions.ObjectAction;

public class SlayerCave {
	public static void register() {
		/**
		 * North entrance/exit
		 */
		ObjectAction.register(40391, 3405, 10146, 0, "exit", (player, obj) -> player.startEvent(event -> {
			player.lock();
			player.animate(2796);
			event.delay(2);
			player.resetAnimation();
			player.getMovement().teleport(3294, 3749, 0);
			player.sendFilteredMessage("You exit the cave.");
			player.unlock();
		}));
		ObjectAction.register(40390, 3293, 3746, 0, "walk-down", (player, obj) -> player.startEvent(event -> {
			player.lock();
			player.animate(2796);
			event.delay(2);
			player.resetAnimation();
			player.getMovement().teleport(3406, 10145, 0);
			player.sendFilteredMessage("You enter the cave.");
			player.unlock();
		}));

		/**
		 * South entrance/exit
		 */
		ObjectAction.register(40389, 3384, 10050, 0, "exit", (player, obj) -> player.startEvent(event -> {
			player.lock();
			player.animate(2796);
			event.delay(2);
			player.resetAnimation();
			player.getMovement().teleport(3260, 3663, 0);
			player.sendFilteredMessage("You exit the cave.");
			player.unlock();
		}));
		ObjectAction.register(40388, 3259, 3664, 0, "walk-down", (player, obj) -> player.startEvent(event -> {
			player.lock();
			player.animate(2796);
			event.delay(2);
			player.resetAnimation();
			player.getMovement().teleport(3385, 10052, 0);
			player.sendFilteredMessage("You enter the cave.");
			player.unlock();
		}));
	}
}
