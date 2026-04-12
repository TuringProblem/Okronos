package io.ruin.model.inter.questtab.main;

import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.QuestTabEntry;

import java.util.ArrayList;
import java.util.List;

public class PlayersOnline extends QuestTabEntry {

	public static final PlayersOnline INSTANCE = new PlayersOnline();

	@Override
	public void send(Player player) {
		send(player, "Players Online", World.playerCount(), Color.GREEN);
	}

	@Override
	public void select(Player player) {
		List<String> playersOnline = new ArrayList<>();
//        if (player.isDev()) {
//            World.getPlayerStream().forEach(p -> {
//                if (p.getUserId() != -1) {
//                    playersOnline.add(p.getName() + " - Position: " + p.getPosition());
//                }
//            });
//            String[] players = playersOnline.toArray(new String[0]);
//            player.sendScroll("<col=800000>" + World.type.getWorldName() + "Players [" + realPlayers() + "]", players);
//        }
		player.sendMessage("There are currently " + World.playerCount() + " players online.");
	}


}
