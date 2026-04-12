package io.ruin.model.map.object.actions.impl.halloween;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.questtab.bestiary.Bestiary;
import io.ruin.model.item.Item;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;

public class PumpkinChest {

	private static final Item[] COMMON_LOOT = {
		new Item(1632, 25), //Uncut dragon stones
		new Item(21905, 50), //Dragon bolts
		new Item(11230, 35), //Dragon dart
		new Item(811, 500), //Rune dart
		new Item(23962, 10), //Crystal shards
		new Item(990, 2), //Crystal keys
		new Item(1128, 5), //Rune platebody
		new Item(1080, 5), //Rune platelegs
		new Item(5316, 3), //Magic seed
		new Item(537, 50), //Dragon bones
		new Item(3025, 25), //Super restores
		new Item(6686, 25), //Saradomin Brews
		new Item(1392, 50), //Battlestaff
		new Item(570, 50), //Fire orb
		new Item(572, 50), //Water orb
		new Item(392, 100), //Manta ray
		new Item(3145, 150), //Cooked Karambwan
		new Item(2362, 100), //Adamant bars
		new Item(2364, 50), //Runite bars
		new Item(1988, 500), //Grapes
		new Item(5300, 10), //Snapdragon seed
		new Item(5295, 15), //Ranarr seed
		new Item(220, 10), //Grimy Torstol
		new Item(21880, 500), //Wrath rune
		new Item(20545, 1) //Medium clue reward casket
	};
	private static final Item[][] RUNE_LOOTS = {
		new Item[]{
			new Item(560, 1000),
			new Item(565, 1000),
			new Item(566, 1000)
		}
	};
	private static final Item[] UNCOMMON_LOOT = {
		new Item(11840, 1), //Dragon boots
		new Item(8783, 100), //Mahogany planks
		new Item(11944, 50), //Lava dragon bones
		new Item(6739, 1), //Dragon axe
		new Item(12914, 10), //Anti venom+
		new Item(13442, 50), //Crystal shards
		new Item(4207, 1), //Dragon platelegs
		new Item(23962, 50), //Dragon chainbody
		new Item(3205, 3), //Dragon Haberd
		new Item(9193, 500), //Dragonstone bolt tips
		new Item(21905, 200), //Dragon bolts
		new Item(11230, 150), //Dragon darts
		new Item(19484, 100), //Dragon javelins
		new Item(20544, 1), //Hard clue reward casket
		new Item(12831, 1), //Blessed spirit shield
		new Item(13307, 500), //Blood money
		new Item(2510, 150), //Black dragon leather
		new Item(21880, 1000), //Wrath runes
	};
	private static final Item[] RARE_LOOT = {
		new Item(6571, 1),//Uncut onyx
		new Item(11335, 1),//Dragon full helm
		new Item(2577, 1),//Ranger boots
		new Item(12598, 1),//Holy sandals
		new Item(6735, 1),//Warrior ring
		new Item(6737, 1),//Berserker ring
		new Item(6731, 1),//Seers ring
		new Item(6733, 1),//Archers ring
		new Item(12004, 1),//Kraken tentacle
		new Item(23962, 100),//Crystal shards
		new Item(20543, 1),//Elite clue reward casket
		new Item(22125, 25),//Superior dragon bones
		new Item(23956, 1),//Crystal armour seed
		new Item(11230, 500),//Dragon dart
		new Item(21905, 500)//Dragon bolts
	};

	private static final Item[] VERY_RARE_LOOT = {
		new Item(19836, 1),//Master clue reward casket
		new Item(24034, 1),//Dragonstone full helm
		new Item(24037, 1),//Dragonstone platebody
		new Item(24040, 1),//Dragonstone platelegs
		new Item(24043, 1),//Dragonstone boots
		new Item(24046, 1),//Dragonstone gauntlets
		new Item(22330, 1),//PVP armour mystery box
		new Item(30370, 1)//Enhanced crystal weapon seed
	};

	private static final LootTable DAILY_REWARDS_CHEST_TABLE = new LootTable().addTable(1,
            /*
            Rares 1/20 chance
             */
		new LootItem(995, 250000, 500000, 4),
		new LootItem(11808, 1, 4),
		new LootItem(11806, 1, 4),
		new LootItem(11804, 1, 4),
		new LootItem(11802, 1, 4),
		new LootItem(2577, 1, 4),
		new LootItem(2581, 1, 4),
		new LootItem(6585, 1, 4),
		new LootItem(4151, 1, 4),
		new LootItem(4151, 1, 4),
		new LootItem(2528, 1, 4),
		new LootItem(23071, 1, 4),
		new LootItem(6737, 1, 4),
		new LootItem(6585, 1, 4),
		new LootItem(4151, 1, 4),
		new LootItem(2528, 1, 4),
		new LootItem(23071, 1, 4),
		new LootItem(23071, 1, 4),

            /*
            Ultra Rares 1/147 chance
             */
		new LootItem(995, 500000, 750000, 3),
		new LootItem(2528, 1, 3),
		new LootItem(23071, 1, 3),
		new LootItem(2528, 1, 3),
		new LootItem(2581, 1, 3),
		new LootItem(12785, 1, 3),
		new LootItem(23071, 1, 3),
		new LootItem(2528, 1, 3),
		new LootItem(995, 3000000, 3),
		new LootItem(23071, 1, 3),
		new LootItem(6739, 1, 3),


            /*
            Amazing Loots 1/388 chance
             */
		new LootItem(995, 750000, 1500000, 2),
		new LootItem(2528, 1, 2),
		new LootItem(23071, 1, 2),
		new LootItem(2528, 1, 2),
		new LootItem(2801, 1, 2),
		new LootItem(12785, 1, 2),
		new LootItem(2801, 1, 2),
		new LootItem(995, 1000000, 10),
		new LootItem(23071, 1, 2),
		new LootItem(2528, 1, 2),



            /*
            Common Loots
             */
		new LootItem(995, 125000, 250000, 10),
		new LootItem(2677, 1, 10),
		new LootItem(995, 500000, 10),
		new LootItem(12785, 1, 10),
		new LootItem(23071, 1, 10)
	);

	private static void showLoot(Player player) {
		//player.sendMessage(Color.DARK_GREEN, "<img=15> Showing Daily Login Chest rewards...");
		/**
		 * sends the drop table interface
		 */
//        player.openInterface(ToplevelComponent.MAINMODAL, Bestiary.DROP_TABLE_INTERFACE);

		/**
		 * sends clientscript #id 10004, handles the popular searches on the left hand side
		 * params are int, string "is", amount of text, text seperated by a divider |.
		 */
		player.getPacketSender().sendClientScript(10004, "is", 0, "");
		//   player.getPacketSender().sendClientScript(10004, "is", 10, "Abyssal Demon|Green Dragon|Cow|Goblin|Elvarg|General Graa'dor|Kree'arra|Vorkath|Zulrah|Great olm");

		/**
		 * gets the npc's loot table and checks / verifies the items to then display onto the interface.
		 */
		LootTable table = DAILY_REWARDS_CHEST_TABLE;

		table.calculateWeight();

		int index = 0;
		for (LootTable.ItemsTable it : table.tables) {
			for (LootItem tableItem : it.items) {
				/**
				 * drop weight percentage
				 */
				double percentage = (it.weight / (double) table.totalWeight) * (tableItem.weight / (double) it.totalWeight) * 100;
				/**
				 * sends clientscript #id 10006, paramTypes: int int int string string string, gets the container index, item ids + drops and formating them.
				 */
				player.getPacketSender().sendClientScript(10006, "iiisss", index, tableItem.id, tableItem.max, tableItem.max + "", String.format("%.2f", percentage) + "%", "");
				index++;
			}
		}

		/**
		 * sends clientscript 10085, handling of component 25 & 26 for interface 801.
		 */
		//player.getPacketSender().sendClientScript(10085, "iii", (Bestiary.DROP_TABLE_INTERFACE << 16) | 25, (Bestiary.DROP_TABLE_INTERFACE << 16) | 26, index * 40);

	}

//    public static void register() {
//        ObjectAction.register(41462, "examine", (player, obj) -> showLoot(player));
//        ObjectAction.register(41462, 1, (player, obj) -> {
//            Item pumpkinKey = player.getInventory().findItem(23951); // Replace KEY ID
//            if (pumpkinKey == null) {
//                player.sendFilteredMessage("You need a Pumpkin key to open this chest.");
//                return;
//            }
//            player.startEvent(event -> {
//                player.lock();
//                player.sendFilteredMessage("You unlock the chest with your key.");
//                pumpkinKey.remove();
//                player.privateSound(51);
//                player.animate(536);
//                World.startEvent(e -> {
//                    obj.setId(41462);// Open Model
//                    e.delay(2);
//                    obj.setId(41463);// Closed Model
//                });
//                int amount = Random.get(25000, 75000);
//                player.getInventory().add(995, amount);
//                for(int i = 0; i <= 1; i++) {
//                    if (Random.rollDie(350, 1)) {
//                        /**
//                         * Very rare loot
//                         */
//                        Item loot = VERY_RARE_LOOT[Random.get(VERY_RARE_LOOT.length - 1)];
//                        player.getInventory().addOrDrop(loot.getId(), loot.getAmount());
//                        Broadcast.GLOBAL.sendNews(player.getName() + " just received " + loot.getDef().descriptiveName + " from the Elven Crystal Chest!");
//
//                    } else if (Random.rollDie(200, 1)) {
//                        /**
//                         * Rare loot
//                         */
//                        Item loot = RARE_LOOT[Random.get(RARE_LOOT.length - 1)];
//                        player.getInventory().addOrDrop(loot.getId(), loot.getAmount());
//
//                    } else if (Random.rollDie(100, 1)) {
//                        /**
//                         * Uncommon loot
//                         */
//                        Item loot = UNCOMMON_LOOT[Random.get(UNCOMMON_LOOT.length - 1)];
//                        player.getInventory().addOrDrop(loot.getId(), loot.getAmount());
//
//                    } else if (Random.rollDie(20, 1)) {
//                        /**
//                         * Rune loot
//                         */
//                        Item[] loot = RUNE_LOOTS[0];
//                        for (Item item : loot)
//                            player.getInventory().addOrDrop(item.getId(), item.getAmount());
//
//                    } else {
//                        /**
//                         * Common loot
//                         */
//                        Item loot = COMMON_LOOT[Random.get(COMMON_LOOT.length - 1)];
//                        player.getInventory().addOrDrop(loot.getId(), loot.getAmount());
//
//                    }
//                }
//                event.delay(1);
//                player.unlock();
//            });
//        });
//    }
}
