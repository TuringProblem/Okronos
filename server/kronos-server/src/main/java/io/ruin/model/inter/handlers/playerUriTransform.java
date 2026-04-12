package io.ruin.model.inter.handlers;

import io.ruin.model.entity.player.Player;

public class playerUriTransform {
	public playerUriTransform(Player player) {
		player.startEvent(event -> {
			player.lock();
			player.graphics(86, 96, 0);
			player.getAppearance().setNpcId(7311);
			event.delay(1);
			player.graphics(1306);
			player.animate(7278);
			player.getAppearance().setNpcId(7313);
			event.delay(9);
			player.animate(4069);
			event.delay(1);
			player.graphics(678);
			player.animate(4071);
			event.delay(1);
			player.graphics(86);
			player.resetAnimation();
			player.getAppearance().setNpcId(-1);
			player.unlock();
		});
	}

}
