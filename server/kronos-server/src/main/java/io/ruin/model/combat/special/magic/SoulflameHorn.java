package io.ruin.model.combat.special.magic;

import io.ruin.cache.Color;
import io.ruin.cache.ObjType;
import io.ruin.model.activities.duelarena.DuelRule;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.containers.Equipment;

import java.util.ArrayList;
import java.util.List;

public class SoulflameHorn implements Special {
	@Override
	public boolean accept(ObjType def, String name) {
		return def.id == 30759;
	}

	@Override
	public boolean handleActivation(Player player) {
		List<Player> targets = new ArrayList<>();
		if(player.soulflameHornBoost.remaining() < 1)
			targets.add(player);
		for (Player p : player.localPlayers()) {
			if(p.getPosition().distance(player.getPosition()) > 3)
				continue;
			if(p.soulflameHornBoost.remaining() >= 1)
				continue;
			if(targets.size() < 3)
				targets.add(p);
			if(targets.size() >= 3)
				break;
		}
		if(targets.isEmpty()) {
			player.sendMessage("Found no targets to use this on right now.");
			return false;
		}
		player.animate(12158);
		targets.forEach(p -> {
			p.graphics(3283, 40, 0);
			p.soulflameHornBoost.delay(30);
			p.sendMessage(Color.PURPLE2.wrap(player.getName() + " has enticed you to deal 50% more damage for the next 30 ticks."));
		});
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 25;
	}
}
