package io.ruin.model.skills.magic.spells.modern;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;

public class BonesDragonfruit {

	public static boolean cast(Player p, Integer i) {
		int count = 0;
		for (Item item : p.getInventory().getItems()) {
			if (item != null && item.getDef().allowFruit) {
				item.setId(22929);
				count++;
			}
		}
		if (count > 0) {
			p.animate(722);
			p.graphics(141, 92, 0);
			return true;
		}
		p.sendMessage("You don't have any bones in your inventory to turn to dragonfruit.");
		return false;
	}

	public static void register() {
//        ItemAction.registerInventory(30368, 1, (player, item) -> {
//            if (cast(player, 0)) {
//                item.incrementAmount(-1);
//            }
//        });
	}


}
