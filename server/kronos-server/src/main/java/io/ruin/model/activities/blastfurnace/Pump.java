package io.ruin.model.activities.blastfurnace;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.player.Player;
import io.ruin.model.stat.StatType;

public class Pump {

	public static void operatePump(Player player, int objectId) {
/*        if(objectId != BlastFurnace.PUMP_ID) {
            return false;
        }
        if(player.isOperatingPump() == true) {
            return false;
        } else {
            player.setOperatingPump(true);
        }*/

		player.getMovement().teleport(1950, 4961, 0);
		player.face(1949, 4961);
		player.sendMessage("You start operating the pump...");
		player.animate(3850);

 /*       BlastFurnace.cycleEvent = new CycleEvent() {

            @Override
            public void execute(CycleEventContainer container) {
                if(player.getMovement().hasMoved()) {
                    player.setOperatingPump(false);
                    container.stop();
                    return;
                }
                if(!(player.getPosition().getX() == 1950) && !(player.getPosition().getY() == 4961)) {
                    player.setOperatingPump(false);
                    container.stop();
                    return;
                }
                if(Random.get(4) == 1 && objectId == BlastFurnace.PUMP_ID) {
                    player.getStats().addXp(StatType.Strength, 4, true);
                    player.setOperatingPump(true);
                } else if(Random.get(64) == 1 && objectId == BlastFurnace.PUMP_ID) {
                    player.sendMessage("Your hands slip causing you to jam your arms in the pump.");
                    player.hit(new Hit().randDamage(1, 2));
                    player.forceText("Ouch!");
                    player.resetAnimation();
                    player.setOperatingPump(false);
                    player.getMovement().teleport(1951, 4961, 0);
                    container.stop();
                    return;
                } else if(Random.get(48) == 1 && objectId == BlastFurnace.PUMP_ID) {
                    player.getStats().addXp(StatType.Strength, 4, true);
                    player.sendMessage("+15 Fremennik Reputation");
                    player.setOperatingPump(true);
                } else if(Random.get(100) == 1 && objectId == BlastFurnace.PUMP_ID) {
                    DriveBelt.breakDriveBelt(player);
                }
            }
        };*/
	}
}
