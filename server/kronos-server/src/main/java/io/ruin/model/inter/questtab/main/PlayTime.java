package io.ruin.model.inter.questtab.main;

import io.ruin.Server;
import io.ruin.api.utils.TimeUtils;
import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

public class PlayTime extends QuestTabEntry {

	public static final PlayTime INSTANCE = new PlayTime();

	@Override
	public void send(Player player) {
		send(player, "Play Time", toTime(player), Color.GREEN);
	}

	@Override
	public void select(Player player) {
		//player.forceText("!" + Color.BABY_BLUE.wrap("PLAY TIME:") + " " + toTime(player));
	}

	private static String toTime(Player player) {
		return TimeUtils.fromMs(player.playTime * Server.tickMs(), false);
	}


	public static void main(String[] args) {
		System.out.println("Ari playtime: " + TimeUtils.fromMs(45032400 * Server.tickMs(), false));
	}

}