package io.ruin.model.item.actions.impl.boxes.mystery;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;

import static io.ruin.model.item.actions.impl.boxes.mystery.VoteMysteryBox.VIEW_REWARDS_WIDGET;
import static io.ruin.model.item.actions.impl.boxes.mystery.VoteMysteryBox.updateRewards;

public class CrystalChestBox {

	public static final LootTable CRYSTAL_TABLE = new LootTable().addTable(1,


		new LootItem(1632, 25, 1), //Uncut dragon stones
		new LootItem(21905, 50, 1), //Dragon bolts
		new LootItem(11230, 35, 1), //Dragon dart
		new LootItem(811, 500, 1), //Rune dart
		new LootItem(990, 2, 1), //Crystal keys
		new LootItem(1128, 5, 1), //Rune platebody
		new LootItem(1080, 5, 1), //Rune platelegs
		new LootItem(5316, 3, 1), //Magic seed
		new LootItem(537, 50, 1), //Dragon bones
		new LootItem(3025, 25, 1), //Super restores
		new LootItem(392, 100, 1), //Manta ray
		new LootItem(3145, 150, 1), //Cooked Karambwan
		new LootItem(2362, 100, 1), //Adamant bars
		new LootItem(2364, 50, 1), //Runite bars
		new LootItem(5300, 10, 1), //Snapdragon seed
		new LootItem(5295, 15, 1), //Ranarr seed
		new LootItem(220, 10, 1), //Grimy Torstol
		new LootItem(20545, 1, 1),//Medium clue reward casket


		new LootItem(11840, 1, 1), //Dragon boots
		new LootItem(8783, 100, 1), //Mahogany planks
		new LootItem(11944, 50, 1), //Lava dragon bones
		new LootItem(6739, 1, 1), //Dragon axe
		new LootItem(12914, 10, 1), //Anti venom+
		new LootItem(13442, 50, 1), //Crystal shards
		new LootItem(4207, 1, 1), //Dragon platelegs
		new LootItem(23962, 50, 1), //Dragon chainbody
		new LootItem(3205, 3, 1), //Dragon Haberd
		new LootItem(9193, 500, 1), //Dragonstone bolt tip
		new LootItem(20544, 1, 1), //Hard clue reward casket
		new LootItem(12831, 1, 1), //Blessed spirit shield
		new LootItem(2510, 150, 1), //Black dragon leather


		new LootItem(6571, 1, 1),//Uncut onyx
		new LootItem(11335, 1, 1),//Dragon full helm
		new LootItem(2577, 1, 1),//Ranger boots
		new LootItem(12598, 1, 1),//Holy sandals
		new LootItem(6735, 1, 1),//Warrior ring
		new LootItem(6737, 1, 1),//Berserker ring
		new LootItem(6731, 1, 1),//Seers ring
		new LootItem(6733, 1, 1),//Archers ring
		new LootItem(12004, 1, 1),//Kraken tentacle
		new LootItem(23962, 100, 1),//Crystal shards
		new LootItem(20543, 1, 1),//Elite clue reward casket
		new LootItem(22125, 25, 1),//Superior dragon bones


		new LootItem(19836, 1, 1),//Master clue reward casket
		new LootItem(24034, 1, 1),//Dragonstone full helm
		new LootItem(24037, 1, 1),//Dragonstone platebody
		new LootItem(24040, 1, 1),//Dragonstone platelegs
		new LootItem(24043, 1, 1),//Dragonstone boots
		new LootItem(24046, 1, 1),//Dragonstone gauntlets
		new LootItem(22330, 1, 1)//PVP armour mystery box
	);

	private static final LootTable INCENTIVE_TABLE = new LootTable().addTable(1
//            new LootItem(6585, 1, 100), //Amulet of fury
//            new LootItem(11284, 1, 100), //Dragonfire shield
//            new LootItem(6737, 1, 50), //Berserker ring
//            new LootItem(6731, 1, 50), //Seers ring
//            new LootItem(2581, 1, 1), //Robin hood hat
//            new LootItem(12596, 1, 1), //Rangers' tunic
//            new LootItem(1038, 1, 1).broadcast(Broadcast.GLOBAL), //Red party hat
//            new LootItem(1040, 1, 1).broadcast(Broadcast.GLOBAL), //Yellow party hat
//            new LootItem(1042, 1, 1).broadcast(Broadcast.GLOBAL), //Blue party hat
//            new LootItem(1044, 1, 1).broadcast(Broadcast.GLOBAL), //Green party hat
//            new LootItem(1046, 1, 1).broadcast(Broadcast.GLOBAL), //Purple party hat
//            new LootItem(1048, 1, 1).broadcast(Broadcast.GLOBAL), //White  party hat
//            new LootItem(12437, 1, 1).broadcast(Broadcast.GLOBAL), // 3rd age cloak
//            new LootItem(1053, 1, 1).broadcast(Broadcast.GLOBAL), //Green halloween mask
//            new LootItem(1055, 1, 1).broadcast(Broadcast.GLOBAL), //Blue halloween mask
//            new LootItem(1057, 1, 1).broadcast(Broadcast.GLOBAL), //Red halloween mask
//            new LootItem(12696, 4000, 5000, 1), //5000 Super combat potions
//            new LootItem(13442, 4000, 5000, 1) //5000 Anglerfish
	);


	public static void openRewards(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, VIEW_REWARDS_WIDGET);
		player.closeInterface(ToplevelComponent.INVENTORY_TAB_AREA);
		updateRewards(player, CRYSTAL_TABLE);
	}
}






