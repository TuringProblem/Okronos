package io.ruin.model.item.actions.impl.boxes.mystery;

import io.ruin.cache.Icon;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.utility.Broadcast;

import static io.ruin.cache.ItemID.COINS_995;

public class SuperMysteryBox extends ItemContainer {

	private static final int SUPER_MYSTERY_BOX = 290;

	private static final int EASTER_EGG = 21227;

	private static final LootTable ECO_MYSTERY_BOX_TABLE = new LootTable().addTable(1,
		new LootItem(COINS_995, 4000000, 11000000, 20), // 6M coins
		new LootItem(COINS_995, 4000000, 14000000, 20), // 8M coins
		new LootItem(COINS_995, 4000000, 20000000, 15), // 20M coins

		new LootItem(12004, 1, 25), // tentacle

		new LootItem(20716, 1, 25), // tome of fire (empty)

		new LootItem(30035, 25, 25), // 15x donor ticket
		new LootItem(11284, 1, 25), // dfs
		new LootItem(21880, 7500, 25), // wrath runes
		new LootItem(12696, 750, 25), // super combat

		new LootItem(11824, 1, 20), // zamorakian spear

		new LootItem(12002, 1, 25), // Occult necklace
		new LootItem(6585, 1, 25), // Amulet of fury
		new LootItem(12902, 1, 20), // Toxic staff of the dead

		new LootItem(11926, 1, 22), // Odium ward
		new LootItem(11924, 1, 22), // Malediction ward
		new LootItem(11785, 1, 12).broadcast(Broadcast.WORLD), // Armadyl crossbow
		new LootItem(12373, 1, 12).broadcast(Broadcast.WORLD), // Serpentine helm

		new LootItem(6889, 1, 25), // Master's book
		new LootItem(12900, 1, 19), // Uncharged toxic trident
		new LootItem(20724, 1, 22), // Imbued heart
		new LootItem(11908, 1, 20), // Uncharged trident

		new LootItem(21634, 1, 22), // Ancient wyvern shield
		new LootItem(22003, 1, 22), // Dragonfire ward
		new LootItem(11284, 1, 22), // Dragonfire shield
		new LootItem(11808, 1, 23).broadcast(Broadcast.WORLD), // Zamorak godsword
		new LootItem(11806, 1, 23).broadcast(Broadcast.WORLD), // Saradomin godsword
		new LootItem(11804, 1, 23).broadcast(Broadcast.WORLD), // Bandos godsword
		new LootItem(11773, 1, 23).broadcast(Broadcast.WORLD), // Berserker ring (i)
		new LootItem(11771, 1, 23).broadcast(Broadcast.WORLD), // Archers ring (i)
		new LootItem(11770, 1, 23).broadcast(Broadcast.WORLD), // Seers ring (i))

		new LootItem(20017, 1, 20).broadcast(Broadcast.WORLD), // Ring of coins
		new LootItem(6583, 1, 20).broadcast(Broadcast.WORLD), // Ring of stone
		new LootItem(20005, 1, 20).broadcast(Broadcast.WORLD), // Ring of nature
		new LootItem(COINS_995, 5000000, 18000000, 20).broadcast(Broadcast.WORLD), // Coins
		new LootItem(COINS_995, 8000000, 25000000, 20).broadcast(Broadcast.WORLD), // Coins
		new LootItem(12821, 1, 2).broadcast(Broadcast.WORLD), // Spectral spirit shield
		new LootItem(13271, 1, 2).broadcast(Broadcast.WORLD), // Abyssal dagger
		new LootItem(2581, 1, 2).broadcast(Broadcast.WORLD), // Robin hood hat
		new LootItem(12596, 1, 10), // Rangers' tunic
		new LootItem(22975, 1, 20), // Brimstone ring
		new LootItem(1053, 1, 5).broadcast(Broadcast.WORLD),
		new LootItem(1055, 1, 5).broadcast(Broadcast.WORLD), // Blue halloween mask
		new LootItem(1057, 1, 5).broadcast(Broadcast.WORLD), // Red halloween mask
		new LootItem(11847, 1, 2).broadcast(Broadcast.WORLD), // Black halloween mask
		new LootItem(1050, 1, 2).broadcast(Broadcast.WORLD), // Santa hat
		new LootItem(13343, 1, 2).broadcast(Broadcast.WORLD), // Black santa hat
		new LootItem(13344, 1, 2).broadcast(Broadcast.WORLD), // Inverted santa hat
		new LootItem(1038, 1, 5).broadcast(Broadcast.WORLD), // Red party hat
		new LootItem(1040, 1, 5).broadcast(Broadcast.WORLD), // Yellow party hat
		new LootItem(1042, 1, 5).broadcast(Broadcast.WORLD), // Blue party hat
		new LootItem(1044, 1, 5).broadcast(Broadcast.WORLD), // Green party hat
		new LootItem(1046, 1, 5).broadcast(Broadcast.WORLD), // Purple party hat
		new LootItem(1048, 1, 5).broadcast(Broadcast.WORLD), // White party hat
		new LootItem(11862, 1, 3).broadcast(Broadcast.WORLD), // Black party hat
		new LootItem(11863, 1, 3).broadcast(Broadcast.WORLD), // Rainbow party hat
		new LootItem(12399, 1, 5).broadcast(Broadcast.WORLD), // Partyhat & specs
		new LootItem(962, 1, 5).broadcast(Broadcast.WORLD), // Xmas cracker
		new LootItem(1050, 1, 12).broadcast(Broadcast.WORLD), // Santa hat
		new LootItem(1419, 1, 12).broadcast(Broadcast.WORLD), // Scythe
		new LootItem(1037, 1, 12).broadcast(Broadcast.WORLD), // Bunny ears
		new LootItem(22613, 1, 18).broadcast(Broadcast.WORLD), // Vesta's longsword

		new LootItem(22610, 1, 18).broadcast(Broadcast.WORLD), // Vesta's spear
		new LootItem(22616, 1, 18).broadcast(Broadcast.WORLD), // Vesta's body
		new LootItem(22619, 1, 18).broadcast(Broadcast.WORLD), // Vesta's skirt

		new LootItem(22647, 1, 18).broadcast(Broadcast.WORLD), // Zuriel's staff
		new LootItem(22650, 1, 18).broadcast(Broadcast.WORLD), // Zuriel's hood
		new LootItem(22653, 1, 18).broadcast(Broadcast.WORLD), // Zuriel's top
		new LootItem(22656, 1, 18).broadcast(Broadcast.WORLD), // Zuriel's bottom

		new LootItem(22625, 1, 18).broadcast(Broadcast.WORLD), // Statius helm
		new LootItem(22628, 1, 18).broadcast(Broadcast.WORLD), // Statius body
		new LootItem(22631, 1, 18).broadcast(Broadcast.WORLD), // Statius legs

		new LootItem(22638, 1, 18).broadcast(Broadcast.WORLD), // Morrigan's coif
		new LootItem(22641, 1, 18).broadcast(Broadcast.WORLD), // Morrigan's body
		new LootItem(22644, 1, 18).broadcast(Broadcast.WORLD) // Morrigan's chaps
	);

	public static void register() {
		ItemAction.registerInventory(SUPER_MYSTERY_BOX, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward;
			if (player.firstMysteryBoxReward) {
				reward = ECO_MYSTERY_BOX_TABLE.rollItem();
				player.firstMysteryBoxReward = false;
			} else if (player.guaranteedMysteryBoxLoot >= 5) {
				reward = ECO_MYSTERY_BOX_TABLE.rollItem();
				player.guaranteedMysteryBoxLoot = 1;
			} else {
				reward = ECO_MYSTERY_BOX_TABLE.rollItem();
				player.guaranteedMysteryBoxLoot++;
			}
			item.remove();
			player.getInventory().add(reward);
			if (reward.lootBroadcast != null)
				Broadcast.GLOBAL.sendNews(Icon.MYSTERY_BOX, "Mystery Box",
					"" + player.getName() + " just received " + reward.getDef().descriptiveName + "!");
			player.unlock();
		});
	}
}
