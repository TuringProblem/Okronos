package io.ruin.kotlin.content.event.impl.eventboss.tabel

import io.ruin.model.item.loot.LootItem
import io.ruin.model.item.loot.LootTable
import io.ruin.utility.Broadcast


class AvatarOfDestructionLoot : LootTable() {

	init {
		guaranteedItems(
			LootItem(13204, 100, 200, 100), //coins
			LootItem(4834, 1, 5, 100) //Bones
		)

		addTable(
			14,
			LootItem(2364, 100, 250, 100), //rune bars
			LootItem(565, 500, 1000, 100), //blood runes
			LootItem(560, 500, 1000, 100), //death rune
			LootItem(12002, 1, 1, 5), //Occult Necklace
			LootItem(11785, 1, 1, 5) //Armadyl Crossbow
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
			LootItem(21295, 1, 1, 1).broadcast(Broadcast.WORLD), //Infernal Cape
			LootItem(20374, 1, 1, 1).broadcast(Broadcast.WORLD), //Zamorak Godsword
			LootItem(20372, 1, 1, 1).broadcast(Broadcast.WORLD), //Saradomin Godsword
			LootItem(20370, 1, 1, 1).broadcast(Broadcast.WORLD), //Bandos Godsword
			LootItem(20368, 1, 1, 1).broadcast(Broadcast.WORLD), //Armadyl Godsword
			LootItem(2528, 1, 3, 1).broadcast(Broadcast.WORLD) //XP Lamp
		)
	}
}
