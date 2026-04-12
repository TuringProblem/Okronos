package io.ruin.kotlin.content.event.impl.eventboss.tabel

import io.ruin.model.item.loot.LootItem
import io.ruin.model.item.loot.LootTable
import io.ruin.utility.Broadcast


/*
 * @project Kronos
 * @author Patrity - https://github.com/Patrity
 * Created on - 7/2/2020
 */
class BrutalLavaDragonLoot : LootTable() {

	init {
		guaranteedItems(
			LootItem(995, 1000000, 2000000, 100), //coins
			LootItem(11944, 5, 5, 100), //lava dragon bones
			LootItem(1748, 4, 8, 100) // black dhide
		)
		addTable(
			14,
			LootItem(2364, 100, 250, 100), //rune bars
			LootItem(565, 500, 1000, 100), //blood runes
			LootItem(560, 500, 1000, 100), //death rune
			LootItem(11994, 100, 250, 100), //death rune
			LootItem(3455, 1, 1, 2), //lava dhide
			LootItem(13307, 100, 500, 100) //blood money
		)
		addTable(
			10,
			LootItem(11230, 50, 250, 100), //d dart
			LootItem(11212, 100, 300, 100), //d arrows
			LootItem(4088, 5, 10, 100), //dplatelegs
			LootItem(4586, 5, 10, 90), //dskirt
			LootItem(11959, 100, 200, 100), //black chin
			LootItem(13442, 100, 250, 90), //anglers
			LootItem(30083, 2, 4, 100) //lava dhide

		)
		addTable(
			1,
			LootItem(21902, 1, 1, 1).broadcast(Broadcast.WORLD), //dragon crossbow
			LootItem(25965, 1, 1, 1).broadcast(Broadcast.WORLD), //bloodline shard
			LootItem(22103, 1, 1, 1).broadcast(Broadcast.WORLD), //dragon metal lump
			LootItem(11335, 1, 1, 1).broadcast(Broadcast.WORLD) //dragon full helm
		)
	}
}
