package io.ruin.model.activities.wellofgoodwill;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.object.actions.ObjectAction;

public class DonateToWell {

	public static final int WELL = 50000;

	public static void register() {
		ObjectAction.register(WELL, "Donate", (player, obj) -> {

		});
//        ObjectAction.register(WELL, "Check", (player, obj) -> {
////            WellofGoodwill.lookDownWell(player);
//        });
	}


	private static void donate(Player player) {
//        if (WellofGoodwill.isActive()) {
//            WellofGoodwill.checkFull(player);
//        } else {
//            player.integerInput("Enter amount to donate:", amt -> WellofGoodwill.donate(player, amt));
//        }
	}

}

