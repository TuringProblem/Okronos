package io.ruin.model.entity.npc;

import io.ruin.api.utils.Random;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.map.ground.GroundItem;

public class NPCRareDropTable {

	public void HandleDrop(Player player, int id, NPC npc) {
		GroundItem loot = null;
		switch (id) {
			case 0:
				loot = new GroundItem(new Item(30452, 1)).position(player.getPosition());
				loot.owner(player);
				loot.spawn();
				player.sendMessage("<shad=000000><col=ff0000>A slayer mystery box has been dropped at your feet.");
				break;
			case 1:
				loot = Random.get(1) == 0 ? new GroundItem(new Item(30450, 1)).position(player.getPosition()) : new GroundItem(new Item(30577, 1)).position(player.getPosition());
				loot.owner(player);
				loot.spawn();
				player.sendMessage("<shad=000000><col=ff0000>A " + new Item(loot.id).getDef().name + " has been dropped at your feet.");
				break;
			case 2:
				loot = Random.get(1) == 0 ? new GroundItem(new Item(30451, 1)).position(player.getPosition()) : new GroundItem(new Item(30577, 1)).position(player.getPosition());
				loot.owner(player);
				loot.spawn();
				player.sendMessage("<shad=000000><col=ff0000>A " + new Item(loot.id).getDef().name + " has been dropped at your feet.");
				break;
		}
	}
}
