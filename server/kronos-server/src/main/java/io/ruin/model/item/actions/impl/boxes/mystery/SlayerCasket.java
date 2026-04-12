package io.ruin.model.item.actions.impl.boxes.mystery;

import io.ruin.api.utils.Random;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.var.VarPlayerRepository;

public class SlayerCasket {

	private static final LootTable SLAYER_CASKET_LOOT = new LootTable().addTable(1,
		new LootItem(1163, 1, 50), //Rune full helm
		new LootItem(1127, 1, 50), //Rune platebody
		new LootItem(1079, 1, 50), //Rune platelegs
		new LootItem(1201, 1, 50), //Rune kiteshield
		new LootItem(1333, 1, 50), //Rune scimitar
		new LootItem(1319, 1, 50), //Rune scimitar
		new LootItem(11738, 1, 25), //Herb Box
		new LootItem(995, 25000, 75000, 15), //Coins
		new LootItem(995, 75000, 150000, 10), //Coins
		new LootItem(995, 150000, 250000, 5), //Coins
		new LootItem(1164, 4, 15), //Rune full helm
		new LootItem(1164, 4, 15), //Rune full helm
		new LootItem(1128, 4, 15), //Rune platebody
		new LootItem(1128, 4, 15), //Rune platebody
		new LootItem(1080, 4, 15), //Rune platelegs
		new LootItem(1080, 4, 15), //Rune platelegs
		new LootItem(1202, 4, 15), //Rune kiteshield
		new LootItem(1202, 4, 15), //Rune kiteshield
		new LootItem(1334, 4, 15), //Rune scimitar
		new LootItem(1334, 4, 15), //Rune scimitar
		new LootItem(1320, 4, 15), //Rune 2h sword
		new LootItem(1320, 4, 15), //Rune 2h sword
		new LootItem(11738, 1, 15), //Herb Box
		new LootItem(11738, 1, 12), //Herb Box
		new LootItem(11738, 1, 10), //Herb Box
		new LootItem(386, 10, 25, 25), //Sharks
		new LootItem(12696, 5, 10, 25), //Super Combat potion(4)
		new LootItem(2435, 5, 10, 25), //Prayer Potions(4)
		new LootItem(3025, 5, 15), //Super Restores(4)
		new LootItem(560, 250, 15), //Death Runes
		new LootItem(565, 250, 15), //Blood Runes
		new LootItem(21880, 250, 15), //Wrath Runes
		new LootItem(12639, 1, 5), //Guthix halo
		new LootItem(12638, 1, 5), //Zamorak halo
		new LootItem(12637, 1, 5), //Saraodmin halo
		new LootItem(24201, 1, 5) //Ancient Halo
	);

	public static void register() {
		ItemAction.registerInventory(23071, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward;
//            Item reward2;
			if (Random.rollDie(20, 1)) {
				VarPlayerRepository.SLAYER_POINTS.set(player, 25 + VarPlayerRepository.SLAYER_POINTS.get(player));
				player.sendMessage("You recieved 25 slayer points!");
			}
			reward = SLAYER_CASKET_LOOT.rollItem();
//            reward2 = SLAYER_CASKET_LOOT.rollItem();
			item.remove();
			player.getInventory().add(reward);
//            player.getInventory().add(reward2);
			player.unlock();
		});
		ItemAction.registerInventory(25590, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			item.remove(1);
			LootTable table = new LootTable().addTable(1,
				new LootItem(995, 500000, 4),
				new LootItem(995, 1000000, 3),
				new LootItem(995, 1500000, 2),
				new LootItem(995, 2000000, 1)
			);
			Item reward = table.rollItem();
			player.getInventory().addOrDrop(reward);
			player.sendMessage("You open the casket and find " + reward.getAmount() + " coins!");
			player.unlock();
		});
	}
}
