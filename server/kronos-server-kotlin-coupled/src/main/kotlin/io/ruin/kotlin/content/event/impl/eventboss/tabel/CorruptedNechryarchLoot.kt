package io.ruin.kotlin.content.event.impl.eventboss.tabel

import io.ruin.model.item.attributes.AttributeExtensions
import io.ruin.model.item.attributes.AttributeTypes
import io.ruin.model.item.loot.LootItem
import io.ruin.model.item.loot.LootTable
import io.ruin.utility.AttributePair
import io.ruin.utility.Broadcast

/**
 *
 * @project Kronos
 * @author ReverendDread on 5/12/2020
 * https://www.rune-server.ee/members/reverenddread/
 */

class CorruptedNechryarchLoot : LootTable() {

	init {
		guaranteedItems(
			LootItem(995, 100_000, 200_000, 100), //coins
			LootItem(532, 1, 1, 100) //coins
		)
		addTable(
			25,
			LootItem(392, 50, 100, 100), //manta ray
			LootItem(811, 500, 2000, 100), //rune dart
			LootItem(11230, 100, 250, 100), //dragon dart
			LootItem(560, 500, 2500, 100), //death rune
			LootItem(13307, 100, 100, 100), //blood money
			LootItem(26018, 1, 1, 100) //dragon scim (e)
		)
		addTable(
			10,
			LootItem(4151, 1, 1, 100), //dboots
			LootItem(11840, 1, 1, 100), //whip
			LootItem(6585, 1, 1, 100), //fury
			LootItem(11931, 1, 1, 90), //maled 1
			LootItem(11932, 1, 1, 100), //maled 2
			LootItem(11933, 1, 1, 90), //maled 3
			LootItem(11928, 1, 1, 100), //odium 1
			LootItem(11929, 1, 1, 100), //odium 2
			LootItem(11930, 1, 1, 90) //odium 3
		)
		addTable(
			1,
			LootItem(22610, 1, 1, 1).broadcast(Broadcast.GLOBAL), //vesta spear
			LootItem(22613, 1, 1, 1).broadcast(Broadcast.GLOBAL), //vesta sword
			LootItem(22616, 1, 1, 1).broadcast(Broadcast.GLOBAL), //vesta chest
			LootItem(22619, 1, 1, 1).broadcast(Broadcast.GLOBAL), //vesta skirt
			LootItem(22625, 1, 1, 1).broadcast(Broadcast.GLOBAL), //statius helm
			LootItem(22628, 1, 1, 1).broadcast(Broadcast.GLOBAL), //statius plate
			LootItem(22631, 1, 1, 1).broadcast(Broadcast.GLOBAL), //statius legs
			LootItem(22634, 1, 1, 1).broadcast(Broadcast.GLOBAL), //Morrigans axe
			LootItem(22636, 1, 1, 1).broadcast(Broadcast.GLOBAL), //morrigans javelin
			LootItem(22638, 1, 1, 1).broadcast(Broadcast.GLOBAL), //morrigans coif
			LootItem(22641, 1, 1, 1).broadcast(Broadcast.GLOBAL), //morrigans body
			LootItem(22644, 1, 1, 1).broadcast(Broadcast.GLOBAL), //morrigans chaps
			LootItem(22647, 1, 1, 1).broadcast(Broadcast.GLOBAL), //zuriels staff
			LootItem(22650, 1, 1, 1).broadcast(Broadcast.GLOBAL), //zuriels hood
			LootItem(22653, 1, 1, 1).broadcast(Broadcast.GLOBAL), //zuriels top
			LootItem(22656, 1, 1, 1).broadcast(Broadcast.GLOBAL) //zuriels bottom
		)
	}

}
