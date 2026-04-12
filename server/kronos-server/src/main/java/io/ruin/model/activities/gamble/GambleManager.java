package io.ruin.model.activities.gamble;

import com.google.common.collect.Lists;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.Position;

import java.util.List;


public class GambleManager {
	public static int currentId = 0;


	public static void init(Player player, Player target) {
		currentId++;
		player.gambleId = currentId;
		target.gambleId = currentId;
		GambleData gambleData = new GambleData(currentId);
		GambleGameHandler gameHandler = new GambleGameHandler(currentId, player, target);

	}

}