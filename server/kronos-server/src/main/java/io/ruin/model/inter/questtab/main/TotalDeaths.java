package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;
import io.ruin.model.var.VarPlayerRepository;

public class TotalDeaths extends QuestTabEntry {

	@Override
	public void send(Player player) {
		send(player, "Total Deaths", VarPlayerRepository.PVP_DEATHS.get(player), Color.GREEN);
	}

	@Override
	public void select(Player player) {
		//KillDeathRatio.shout(player);
	}

}