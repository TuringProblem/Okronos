package io.ruin.model.inter.questtab.main;

import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;
import io.ruin.model.var.VarPlayerRepository;

public class KillDeathRatio extends QuestTabEntry {

	@Override
	public void send(Player player) {
		int kills = VarPlayerRepository.PVP_KILLS.get(player);
		int deaths = VarPlayerRepository.PVP_DEATHS.get(player);
		send(player, "Kill/Death Ratio", toRatio(kills, deaths), Color.GREEN);
	}

	@Override
	public void select(Player player) {
		shout(player);
	}

	public static void shout(Player player) {
		int kills = VarPlayerRepository.PVP_KILLS.get(player);
		int deaths = VarPlayerRepository.PVP_DEATHS.get(player);
		//player.forceText("!" + Color.ORANGE_RED.wrap("KILLS:") + " " + kills + "  " + Color.ORANGE_RED.wrap("DEATHS:") + " " + deaths + "  " + Color.ORANGE_RED.wrap("RATIO:") + " " + toRatio(kills, deaths));
	}

	private static String toRatio(int kills, int deaths) {
		return NumberUtils.formatTwoPlaces(kills / Math.max(1D, deaths));
	}

}