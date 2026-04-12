package io.ruin.model.entity.player;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.content.upgrade.ItemEffect;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeExtensions;

import java.util.List;

/*
 * @project Kronos
 * @author Patrity - https://github.com/Patrity
 * Created on - 6/11/2020
 */
public class DoubleDrops {

	/*
	 * Math to retrieve loot rolls after a kill
	 */
	public static int getRolls(Player player) {
//        int doubleDropChance = getDoubleDropChance(player);
		int rolls = 1;
//        if (player.storeAmountSpent > 0) {
//            if (Random.get(1, 100) <= doubleDropChance) {
//                rolls++;
//            }
//        }


		if (player.getEquipment().contains(new Item(773))) { //5 rolls with pring
			rolls += 5;
		}


//        if (Random.get(1, 100) <= gearCount(player) * 2) {
//            rolls++;
//        }

		return rolls;
	}

	/*
	 * Method to display a visual chance at rolling multiple drops
	 */
	public static int getChance(Player player) {
		int chance = 0;


//        chance += getDoubleDropChance(player);;

		if (player.pet.npcId == 20000)  //20% chance to double roll with founders pet
			chance += 20;

		if (player.getEquipment().contains(new Item(12785)))  //20% chance to double roll with ROW i
			chance += 20;


//        chance += (gearCount(player) * 2);

		return chance;
	}

//    private static int gearCount(Player player) {
//        int gearRolls = 0;
//        for(Item item : player.getEquipment().getItems()) {
//            if(item != null && item.getDef() != null) {
//                List<String> upgrades = AttributeExtensions.getEffectUpgrades(item);
//                boolean hasEffect = upgrades != null;
//                if (hasEffect) {
//                    for (String s : upgrades) {
//                        try {
//                            if (s.equalsIgnoreCase("empty"))
//                                continue;
//                            ItemEffect effect = ItemEffect.valueOf(s);
//                            gearRolls += effect.getUpgrade().addDoubleDropRolls();
//                        } catch (Exception ex) {
//                            System.err.println("Unknown upgrade { " + s + " } found!");
//                            ex.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }
//        return gearRolls;
//    }

//    private static int getDoubleDropChance(Player player) {
//        int doubleDropChance = player.getSecondaryGroup().getDoubleDropChance();
//
//        if (player.isSecondaryGroup(SecondaryGroup.ZENYTE)) {
//            doubleDropChance = SecondaryGroup.ZENYTE.getDoubleDropChance();
//        } else if (player.isSecondaryGroup(SecondaryGroup.ONYX)) {
//            doubleDropChance = SecondaryGroup.ONYX.getDoubleDropChance();
//        } else if (player.isSecondaryGroup(SecondaryGroup.DRAGONSTONE)) {
//            doubleDropChance = SecondaryGroup.DRAGONSTONE.getDoubleDropChance();
//        } else if(player.isSecondaryGroup(SecondaryGroup.DIAMOND)) {
//            doubleDropChance = SecondaryGroup.DIAMOND.getDoubleDropChance();
//        }else if (player.isSecondaryGroup(SecondaryGroup.RUBY)) {
//            doubleDropChance = SecondaryGroup.RUBY.getDoubleDropChance();
//        } else if (player.isSecondaryGroup(SecondaryGroup.EMERALD)) {
//            doubleDropChance = SecondaryGroup.EMERALD.getDoubleDropChance();
//        } else if (player.isSecondaryGroup(SecondaryGroup.SAPPHIRE)) {
//            doubleDropChance = SecondaryGroup.SAPPHIRE.getDoubleDropChance();
//        }
//        return doubleDropChance;
//    }
}
