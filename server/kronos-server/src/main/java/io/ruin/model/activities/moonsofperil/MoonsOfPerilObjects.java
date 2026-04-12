package io.ruin.model.activities.moonsofperil;

import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.handlers.LootsTables;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.object.actions.ObjectAction;

public class MoonsOfPerilObjects {
	public static void register() {
		ObjectAction.register(51376, "Pass-through", (player, obj) -> {
			int x = obj.x;
			int y = obj.y;
			if (x == 1421 && y == 9650) {
				player.getMovement().teleport(1403, 9717, 0);
			} else if (x == 1421 && y == 9613) {
				player.getMovement().teleport(1388, 9574, 0);
			} else if (x == 1458 && y == 9650) {
				player.getMovement().teleport(1481, 9669, 0);
			}
		});
		ObjectAction.register(51377, "Pass-through", (player, obj) -> {
			int x = obj.x;
			int y = obj.y;
			if (x == 1404 && y == 9703) {
				player.getMovement().teleport(1440, 9653, 0);
			} else if (x == 1439 && y == 9650) {
				player.getMovement().teleport(1403, 9704, 0);
			} else if (x == 1373 && y == 9664) {
				player.getMovement().teleport(1347, 9590, 0);
			} else if (x == 1419 && y == 9631) {
				player.getMovement().teleport(1386, 9591, 0);
			} else if (x == 1512 && y == 9560) {
				player.getMovement().teleport(1356, 9538, 0);
			} else if (x == 1512 && y == 9597) {
				player.getMovement().teleport(1526, 9671, 0);
			} else if (x == 1527 && y == 9670) {
				player.getMovement().teleport(1513, 9596, 0);
			} else if (x == 1458 && y == 9631) {
				player.getMovement().teleport(1510, 9675, 0);
			}
		});
		ObjectAction.register(51373, "Use", (player, obj) -> {
			if (player.getMoonsOfPerilHandler().blueMoonDead) {
				player.sendMessage("The Blue Moon has already been defeated.");
				return;
			}
			player.dialogue(new MessageDialogue("Would you like to start the blue moon boss fight?"),
				new OptionsDialogue(
					new Option("Yes.", () -> {
						player.getMoonsOfPerilHandler().enterBlueMoon(player);
					}),
					new Option("No.")));
		});
		ObjectAction.register(51372, "Use", (player, obj) -> {
			if (player.getMoonsOfPerilHandler().bloodMoonDead) {
				player.sendMessage("The Blood Moon has already been defeated.");
				return;
			}
			player.dialogue(new MessageDialogue("Would you like to start the blood moon boss fight?"),
				new OptionsDialogue(
					new Option("Yes.", () -> {
						player.getMoonsOfPerilHandler().enterBloodMoon(player);
					}),
					new Option("No.")));
		});
		ObjectAction.register(53003, "Escape", (player, obj) -> {
			if (player.getMoonsOfPerilHandler().eclipseMoonRoom.swRegion.id == player.getPosition().getRegionId()) {
				player.getMoonsOfPerilHandler().leaveEclipseMoon(player);
			} else {
				player.getMoonsOfPerilHandler().leaveBloodMoon(player);
			}
		});
		ObjectAction.register(53003, "Quick-escape", (player, obj) -> {
			if (player.getMoonsOfPerilHandler().eclipseMoonRoom.swRegion.id == player.getPosition().getRegionId()) {
				player.getMovement().teleport(1466, 9632, 0);
			} else {
				player.getMovement().teleport(1413, 9632, 0);
			}
		});
		ObjectAction.register(53004, "Escape", (player, obj) -> {
			player.getMoonsOfPerilHandler().leaveBlueMoon(player);
		});
		ObjectAction.register(53004, "Quick-escape", (player, obj) -> {
			player.getMovement().teleport(1440, 9658, 0);
		});
		ObjectAction.register(51359, "Trap", (player, obj) -> player.getMoonsOfPerilHandler().catchMossLizard(player));
		ObjectAction.register(51365, "Collect-from", (player, obj) -> player.getMoonsOfPerilHandler().pickMoonlightGrub(player));
		ObjectAction.register(51362, "Cook", (player, obj) -> {
			if (player.getInventory().hasId(29076)) {
				player.getMoonsOfPerilHandler().cookMossLizard(player);
			} else if (player.getInventory().hasId(29216)) {
				player.getMoonsOfPerilHandler().cookBream(player);
			} else {
				player.sendMessage("You have nothing to cook here.");
			}
		});
		ObjectAction.register(51346, "Claim", (player, obj) -> player.getMoonsOfPerilHandler().lootShrine(player));
		ObjectAction.register(51367, "Fish", (player, obj) -> player.getMoonsOfPerilHandler().fishBream(player));
		ObjectAction.register(51374, "Use", (player, obj) -> {
			if (player.getMoonsOfPerilHandler().eclipseMoonDead) {
				player.sendMessage("The Eclipse Moon has already been defeated.");
				return;
			}
			player.dialogue(new MessageDialogue("Would you like to start the eclipse moon boss fight?"),
				new OptionsDialogue(
					new Option("Yes.", () -> {
						player.getMoonsOfPerilHandler().enterEclipseMoon(player);
					}),
					new Option("No.")));
		});
		ObjectAction.register(51375, "Pass-through", (player, obj) -> {
			int x = obj.x;
			int y = obj.y;
			if (x == 1388 && y == 9576) {
				player.getMovement().teleport(1424, 9616, 1);
			} else if (x == 1404 && y == 9716) {
				player.getMovement().teleport(1424, 9648, 1);
			} else if (x == 1480 && y == 9667) {
				player.getMovement().teleport(1456, 9648, 1);
			} else if (x == 1345 && y == 9590) {
				player.getMovement().teleport(1374, 9668, 0);
			} else if (x == 1388 && y == 9589 || x == 1388 && y == 9591) {
				player.getMovement().teleport(1418, 9632, 0);
			} else if (x == 1354 && y == 9536 || x == 1356 && y == 9536) {
				player.getMovement().teleport(1513, 9563, 0);
			} else if (x == 1509 && y == 9673) {
				player.getMovement().teleport(1461, 9632, 0);
			}
		});
		ObjectAction.register(51378, "Pass-through", (player, obj) -> {
			int x = obj.x;
			int y = obj.y;
			if (x == 1389 && y == 9674) {
				player.getMovement().teleport(1522, 9719, 0);
			} else if (x == 1521 && y == 9720) {
				player.getMovement().teleport(1390, 9676, 0);
			}
		});
	}
}
