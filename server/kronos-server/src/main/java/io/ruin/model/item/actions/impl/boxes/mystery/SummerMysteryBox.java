package io.ruin.model.item.actions.impl.boxes.mystery;

import io.ruin.cache.Icon;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.utility.Broadcast;

public class SummerMysteryBox {

	private static final int SUMMER_MYSTERY_BOX = 30185;


	public static final LootTable summerMboxLoot = new LootTable().addTable(1,

		new LootItem(30138, 1, 5).broadcast(Broadcast.GLOBAL),//good
		new LootItem(30444, 1, 5).broadcast(Broadcast.GLOBAL),//bad
		new LootItem(30447, 1, 5).broadcast(Broadcast.GLOBAL),//bad
		new LootItem(30454, 1, 5).broadcast(Broadcast.GLOBAL),//bad
		new LootItem(30446, 1, 5).broadcast(Broadcast.GLOBAL),//bad
		new LootItem(30425, 1, 5).broadcast(Broadcast.GLOBAL),//bad
		new LootItem(12653, 1, 3).broadcast(Broadcast.GLOBAL),//good
		new LootItem(30140, 1, 3).broadcast(Broadcast.GLOBAL),//good
		new LootItem(30138, 1, 5).broadcast(Broadcast.GLOBAL),//good
		new LootItem(30444, 1, 5).broadcast(Broadcast.GLOBAL),//bad
		new LootItem(30447, 1, 5).broadcast(Broadcast.GLOBAL),//bad
		new LootItem(30454, 1, 5).broadcast(Broadcast.GLOBAL),//bad
		new LootItem(30446, 1, 5).broadcast(Broadcast.GLOBAL),//bad
		new LootItem(30425, 1, 5).broadcast(Broadcast.GLOBAL),//bad
		new LootItem(12653, 1, 3).broadcast(Broadcast.GLOBAL),//good
		new LootItem(30139, 1, 3).broadcast(Broadcast.GLOBAL),//good
		new LootItem(25348, 1, 1).broadcast(Broadcast.GLOBAL),//good
		new LootItem(24491, 1, 1).broadcast(Broadcast.GLOBAL),//good
		new LootItem(25350, 1, 1).broadcast(Broadcast.GLOBAL),//good
		new LootItem(30016, 1, 1).broadcast(Broadcast.GLOBAL),//good
		new LootItem(12703, 1, 1).broadcast(Broadcast.GLOBAL),//good
		new LootItem(30446, 1, 1).broadcast(Broadcast.GLOBAL)//bad


	);

	public static void register() {
		ItemAction.registerInventory(SUMMER_MYSTERY_BOX, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward;
			if (player.firstMysteryBoxReward) {
				reward = summerMboxLoot.rollItem();
				player.firstMysteryBoxReward = false;
			} else if (player.guaranteedMysteryBoxLoot >= 5) {
				reward = summerMboxLoot.rollItem();
				player.guaranteedMysteryBoxLoot = 1;
			} else {
				reward = summerMboxLoot.rollItem();
				player.guaranteedMysteryBoxLoot++;
			}
			item.remove();
			player.getInventory().add(reward);
			if (reward.lootBroadcast != null)
				Broadcast.GLOBAL.sendNews(Icon.MYSTERY_BOX, "Legendary Pet Box", "" + player.getName() + " just received " + reward.getDef().descriptiveName + "!");
			player.unlock();
		});
	}
}

//    public static void open(Player player, Item item) {
//        InterfaceHandler.register(Interface.SUMMER_MYSTER_BOX, h -> {
//            h.actions[7] = (SimpleAction) SummerMysteryBox::spin;
//            h.actions[19] = (SimpleAction) SummerMysteryBox::claimReward;
//        });
//
//        if(player.isVisibleInterface(Interface.SUMMER_MYSTER_BOX)) {
//            player.sendMessage("You need to claim or discard your reward before doing this!");
//            return;
//        }
//
//        if(player.claimedBox) {
//            player.getBox().clear();
//            generateReward(player);
//            player.getBox().sendUpdates();
//            player.openInterface(ToplevelComponent.MAINMODAL, 702);
//            player.getPacketSender().sendClientScript(10034, "ssii", "Legendary Pet Mystery Box", "Thank you for checking out our Legendary Pet Mystery Box!<br><br>" +
//                    "Click the \"Spin\" button when you're ready to test your luck!<br><br><br>" +
//                    "Good Luck!", 15, 0);
//        } else {
//            player.getBox().sendUpdates();
//            player.openInterface(ToplevelComponent.MAINMODAL, 702);
//            player.getPacketSender().sendClientScript(10034, "ssii", "Legendary Pet Mystery Box", "Thank you for checking out our Legendary Pet Mystery Box!<br><br>" +
//                    "Click the \"Spin\" button when you're ready to test your luck!<br><br><br>" +
//                    "Good Luck!", 15, 0);
//        }
//    }
//
//    private static void generateReward(Player player) {
//        LootTable table = summerMboxLoot;
//        for(int i = 0; i < 24; i ++)
//            player.getBox().add(table.rollItem());
//        Item reward = player.getBox().get(15);
//        if(reward == null)
//            generateReward(player);
//    }
//
//    private static void spin(Player player) {
//
//        Item superMysteryBox = player.getInventory().findItem(SUMMER_MYSTERY_BOX);
//        if (superMysteryBox == null) {
//            player.closeInterface(ToplevelComponent.MAINMODAL);
//            return;
//        }
//        player.claimedBox = false;
//        superMysteryBox.remove(1);
//
//    }
//
//    private static void claimReward(Player player) {
//        Item reward = player.getBox().get(15);
//        if (player.getInventory().getFreeSlots() < 1) {
//            player.sendMessage("You'll need at least 1 free inventory spaces to do this.");
//            return;
//        }
//        player.getInventory().add(reward);
//        player.claimedBox = true;
//        player.sendMessage("You get " + reward.getDef().descriptiveName + " from the Legendary Pet Mystery Box.");
//        if (summerMboxLoot.getWeight(reward) < 7) {
//            Broadcast.WORLD.sendNews(Icon.MYSTERY_BOX, "Legendary Pet Mystery Box", "" + player.getName() + " just received " + reward.getDef().descriptiveName + "!");
//        }
//        player.getBox().clear();
//        if(player.isVisibleInterface(Interface.SUPER_MYSTERY_BOX))
//            player.closeInterface(ToplevelComponent.MAINMODAL);
//    }
//
//    private static void discardReward(Player player) {
//        player.closeInterface(ToplevelComponent.MAINMODAL);
//        player.claimedBox = true;
//        player.sendMessage("You discard your Legendary Pet Mystery reward.");
//        player.getBox().clear();
//    }
//
//    public static void openRewards(Player player) {
//        player.openInterface(ToplevelComponent.MAINMODAL, VIEW_REWARDS_WIDGET);
//        player.closeInterface(ToplevelComponent.INVENTORY_TAB_AREA);
//        updateRewards(player, summerMboxLoot);
//    }
//
//    public static void register() {
//        ItemAction.registerInventory(SUMMER_MYSTERY_BOX, "open", SummerMysteryBox::open);
//    }
//}
