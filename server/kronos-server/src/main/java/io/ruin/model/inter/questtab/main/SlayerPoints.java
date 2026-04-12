package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;
import io.ruin.model.var.VarPlayerRepository;

/**
 * @author Danny
 */
public class SlayerPoints extends QuestTabEntry {

	public static final SlayerPoints INSTANCE = new SlayerPoints();

	@Override
	public void send(Player player) {
		send(player, "Points", VarPlayerRepository.SLAYER_POINTS.get(player), Color.GREEN);
	}

	@Override
	public void select(Player player) {

	}
}
