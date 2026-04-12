package io.ruin.kotlin.content.event.impl.eventboss.tabel

import io.ruin.model.item.loot.LootItem
import io.ruin.model.item.loot.LootTable
import io.ruin.utility.Broadcast


class AvatarOfCreationLoot : LootTable() {

	init {
		guaranteedItems(
			LootItem(13204, 100, 200, 100), //coins
			LootItem(4834, 1, 2, 100) //Bones
		)

		addTable(
			14,
			LootItem(2364, 100, 250, 100), //rune bars
			LootItem(19670, 100, 100, 100), //Redwood logs
			LootItem(1618, 50, 100, 100), //Uncut Diamonds
			LootItem(1632, 20, 40, 100), //Uncut Dragonstone
			LootItem(6572, 1, 3, 2), //Uncut Onyx
			LootItem(1514, 150, 300, 100) //Magic Logs
		)

		addTable(
			10,
			LootItem(11230, 50, 250, 100), //d dart
			LootItem(11212, 100, 300, 100), //d arrows
			LootItem(4088, 5, 10, 100), //dplatelegs
			LootItem(4586, 5, 10, 90), //dskirt
			LootItem(11959, 100, 200, 100), //black chin
			LootItem(13442, 100, 250, 90) //anglers

		)

		addTable(
			1,
			LootItem(19496, 1, 1, 1).broadcast(Broadcast.WORLD), //Zenyte
			LootItem(6758, 1, 3, 1).broadcast(Broadcast.WORLD), //Bonus XP Scroll
			LootItem(224, 250, 500, 1).broadcast(Broadcast.WORLD), //Red Spider Eggs
			LootItem(6694, 250, 500, 1).broadcast(Broadcast.WORLD), //Crushed nest
			LootItem(242, 250, 500, 1).broadcast(Broadcast.WORLD), //Dragon Scale Dust
			LootItem(3001, 250, 500, 1).broadcast(Broadcast.WORLD), //Snap Dragon Herb
			LootItem(2999, 250, 500, 1).broadcast(Broadcast.WORLD), //Toadflax Herb
			LootItem(23951, 1, 10, 1).broadcast(Broadcast.WORLD), //Enhanced Crystal Key
			LootItem(11920, 1, 1, 1).broadcast(Broadcast.WORLD), //dragon pickaxe
			LootItem(6739, 1, 1, 1).broadcast(Broadcast.WORLD), //dragon axe
			LootItem(21028, 1, 1, 1).broadcast(Broadcast.WORLD), //dragon harpoon
			LootItem(12800, 1, 1, 1).broadcast(Broadcast.WORLD), //dragon pickaxe upgrade kit
			LootItem(2528, 1, 3, 1).broadcast(Broadcast.WORLD) //XP Lamp
		)
	}
}
