package io.ruin.model.activities.deadman;

import io.ruin.model.entity.player.Player;

public class DeadmanKeyReward {

	public static void handle_deadman_death(Player killer, Player dead) {

		boolean safeDeath;

	}

	public static void handleSafeDeath(Player killer, Player dead, boolean safeDeath) {
		if (!safeDeath) {
			// TODO
		}

	}

	public static void handle_dangerous_zone_death(Player killer, Player dead) {

		//    var dead_carried = dead.varps().varbit(Varbit.DEADMAN_KEYS_CARRIED)
		//    val carried = killer.varps().varbit(Varbit.DEADMAN_KEYS_CARRIED)
		//   val possible_total = carried + dead_carried + 1// Plus one for target's personal bank key.

	}

	public static int giveOrDropDeadBankKey(Player killer, Player dead, int carried) {
		int itemId = carried;
		switch (carried) {
			case 0:// Key 1
				return 13302;
			case 1://Key 2
				return 13303;
			case 2://Key 3
				return 13304;
			case 3://Key 4
				return 13305;
			case 4://Key 5
				return 13306;
		}
		return 13302;// Key 1 default
	}

	public static void handle_safezone_death(Player killer, Player dead) {
		killer.sendMessage("" + dead.getName() + " has died and you have been awarded a key to their bank.");
		killer.sendMessage("The key remains where your victim died.");
		// TODO filter and destory shitter ones than the 5 you're carrying and drop others
        /*val gkey = GroundItem(killer.world(), Item(13302), dead.tile(), killer.id()).pkedFrom(dead.id()).hidden().linkDmmBankKey(createBankKey(dead, deadinfo))
        killer.world().spawnGroundItem(gkey)*/
	}

	public static void createBankKey(Player dead, BankKey bankKey) {
       /* val rawItemsKey = deadinfo.bankItemsKey.copy() // This is the key killer will get
        for (loot in rawItemsKey.lootcontainer) {
            loot ?: continue
                    //dead.bank().remove(loot, true) // TODO enable on real game
                    dead.message("You would have lost ${loot.amount()} x ${loot.definition(dead.world()).name} from your bank on real DMM.")
        }
        deadinfo.bankItemsKey = BankKey(dead.world())
        deadinfo.updateBankKey() // Update dead player's info so their bank key has the items they'll lose when they die next time.
        // Create an empty key
        val rewardedBankKey = BankKey(dead.world())
        // Cycle the lost items, add them to the empty key which the Killer will get as noted items.
        for (loot in rawItemsKey.lootcontainer) {
            loot ?: continue
                    rewardedBankKey.lootcontainer.add(loot.note(dead.world()), false)
            rewardedBankKey.value += loot.realPrice(dead.world()) * loot.amount()
        }
        return rewardedBankKey*/

	}


	/**
	 * Is the player in a Dangerous location? This is established every game cycle in WildernessLevelIndicator.
	 */
/*    @JvmStatic fun inDangerous(player: Player): Boolean {
        return player.attribOr<Int>(AttributeKey.GENERAL_VARBIT1, 0) as Int shr 22 and 3 == 1
    }*/
}
