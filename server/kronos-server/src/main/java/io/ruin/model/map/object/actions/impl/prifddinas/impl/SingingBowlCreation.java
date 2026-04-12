package io.ruin.model.map.object.actions.impl.prifddinas.impl;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.map.object.actions.ObjectAction;

public class SingingBowlCreation {

	public static void register() {
		ObjectAction.register(37344, 1, (player, obj) -> readRecipe(player));
		ObjectAction.register(36075, 1, (player, obj) -> readRecipe(player));
	}

	public static void readRecipe(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, Interface.SINGING_BOWL_RECIPE);
	}

}
