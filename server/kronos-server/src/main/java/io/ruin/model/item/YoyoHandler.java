package io.ruin.model.item;

import io.ruin.model.item.actions.ItemAction;

public class YoyoHandler {
	final static int Yoyo = 4079;

	public static void register() {
		ItemAction.registerInventory(Yoyo, "play", (player, item) -> {
			player.startEvent(event -> {
				player.lock();
				player.animate(1457);
				event.delay(1);
				player.unlock();
			});
		});
		ItemAction.registerInventory(Yoyo, "loop", (player, item) -> {
			player.startEvent(event -> {
				player.lock();
				player.animate(1458);
				event.delay(1);
				player.unlock();
			});
		});
		ItemAction.registerInventory(Yoyo, "walk", (player, item) -> {
			player.startEvent(event -> {
				player.lock();
				player.animate(1459);
				event.delay(1);
				player.unlock();
			});
		});
		ItemAction.registerInventory(Yoyo, "crazy", (player, item) -> {
			player.startEvent(event -> {
				player.lock();
				player.animate(1460);
				event.delay(1);
				player.unlock();
			});
		});
	}
}
