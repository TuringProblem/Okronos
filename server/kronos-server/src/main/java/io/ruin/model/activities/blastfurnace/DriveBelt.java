package io.ruin.model.activities.blastfurnace;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.player.Player;
import io.ruin.model.stat.StatType;

import java.util.concurrent.TimeUnit;

public class DriveBelt {

	public static boolean fixDriveBelt(Player player, int objectId) {

		if (objectId != BlastFurnace.DRIVE_BELTS[1])
			return false;

		player.sendMessage("You attempt to fix the drive belt...");

       /* BlastFurnace.cycleEvent = new CycleEvent() {

            @Override
            public void execute(CycleEventContainer container) {
                if(!player.getInventory().contains(2347)) {
                    player.sendMessage("You need a hammer to do this!");
                    player.resetAnimation();
                    container.stop();
                    return;
                }

                player.animate(1755);

                if(objectId == BlastFurnace.DRIVE_BELTS[0]) {
                    player.getPA().object(BlastFurnace.DRIVE_BELTS[0], 1944, 4967, 0, 10);
                    player.sendMessage("The drive belt has already been fixed!");
                    player.resetAnimation();
                    container.stop();
                    return;
                } else if(Random.get(3) == 1 && objectId == BlastFurnace.DRIVE_BELTS[1]) {
                    fixedDriveBelt(player, objectId);
                    player.sendMessage("+25 Fremennik Reputation");
                    player.fremennikRep += 25;
                    player.resetAnimation();
                    container.stop();
                    return;
                } else if(Random.get(10) == 1 && objectId == BlastFurnace.DRIVE_BELTS[1]) {
                    player.sendMessage("Your finger slips and you fail to fix the drive belt, injuring yourself in the process.");
                    player.hit(new Hit().randDamage(1, 2));
                    player.forceText("Ouch!");
                    player.resetAnimation();
                    container.stop();
                    return;
                }

            }
        };*/
		// BlastFurnace.cycleEventContainer = CycleEventHandler.getSingleton().addEvent(BlastFurnace.class, BlastFurnace.cycleEvent, Misc.toCycles(1, TimeUnit.SECONDS));

		return true;
	}

	public static void breakDriveBelt(Player player) {
		// World.getWorld().getGlobalObjects().add(new GlobalObject(BlastFurnace.DRIVE_BELTS[1], 1944, 4967, 0, 0, 10, -1, BlastFurnace.DRIVE_BELTS[0]));
		player.setDriveBeltBroken(true);
		player.sendMessage("The drive belt has snapped and needs repairing!");
	}

	public static void fixedDriveBelt(Player player, int objectId) {
		if (objectId == BlastFurnace.DRIVE_BELTS[1]) {
			player.getStats().addXp(StatType.Smithing, 150, true);
			player.sendMessage("You have successfully fixed the drive belt.");
			//World.getWorld().getGlobalObjects().remove(BlastFurnace.DRIVE_BELTS[1], 1944, 4967, 0);
			// World.getWorld().getGlobalObjects().add(new GlobalObject(BlastFurnace.DRIVE_BELTS[0], 1944, 4967, 0, 0, 10, -1, BlastFurnace.DRIVE_BELTS[1]));
		} else if (objectId == BlastFurnace.DRIVE_BELTS[0]) {
			player.sendMessage("The drive belt has already been fixed! fixedDriveBelt");
		}
	}
}
