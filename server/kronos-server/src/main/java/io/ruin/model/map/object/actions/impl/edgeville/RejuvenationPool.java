package io.ruin.model.map.object.actions.impl.edgeville;

import io.ruin.cache.LocType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;

public class RejuvenationPool {

	private static void drink(Player player, GameObject obj) {
		boolean delayCheck = obj.getId() != 40004;
		if (delayCheck && System.currentTimeMillis() - player.rejuvenationPool < 1000 * 60) {
			player.dialogue(new MessageDialogue("You can only drink from the " + LocType.get(obj.getId()).name + " once every minute."));
			return;
		}
		player.startEvent(event -> {
			player.lock();
			player.animate(833);
			player.graphics(1039);
			player.getStats().restore(true);
			player.getMovement().restoreEnergy(100);
			player.curePoison(1);
			player.cureVenom(1);
			player.magicImbueHeartCooldown.delay(0);
			player.overloadImbueHeartCooldown.delay(0);
			player.strengthImbueHeartCooldown.delay(0);
			player.rangedImbueHeartCooldown.delay(0);
			player.combatantHeartCooldown.delay(0);
			player.rangersHeartCooldown.delay(0);
			player.overloadHeartCooldown.delay(0);
			if (player.storeAmountSpent > 99)
				player.getCombat().restore();
			event.delay(1);
			if (delayCheck)
				player.rejuvenationPool = System.currentTimeMillis();
			player.unlock();
		});
	}

	public static void register() {
		ObjectAction.register(40848, "Drink", RejuvenationPool::drink);
		ObjectAction.register(2654, "Drink", RejuvenationPool::drink);
	}

}
