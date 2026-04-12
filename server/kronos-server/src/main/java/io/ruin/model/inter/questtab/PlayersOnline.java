package io.ruin.model.inter.questtab;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;

import java.util.LinkedList;
import java.util.List;

public class PlayersOnline {

	public static void open(Player player) {
		List<String> text = new LinkedList<>();
		List<String> players = new LinkedList<>();
		World.players().forEach(p -> players.add(p.getName()));
		text.add("<col=00FFFF><shad=0000000>Players Online</col></shad>");
		if (players.size() == 0) text.add("None online!");
		else text.addAll(players);
		//player.sendScroll("Players Online", text.toArray(new String[0]));
		return;

	}
}
