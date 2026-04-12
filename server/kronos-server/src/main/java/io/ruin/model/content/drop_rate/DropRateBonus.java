package io.ruin.model.content.drop_rate;

import io.ruin.model.entity.player.Player;

@FunctionalInterface
public interface DropRateBonus {

	public float accept(Player player);

}
