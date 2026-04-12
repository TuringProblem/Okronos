package io.ruin.kotlin.content.event.impl.eventboss

import io.ruin.kotlin.content.event.impl.eventboss.tabel.AvatarOfCreationLoot
import io.ruin.kotlin.content.event.impl.eventboss.tabel.AvatarOfDestructionLoot
import io.ruin.model.item.actions.impl.pet.Pet
import io.ruin.model.item.loot.LootTable
import io.ruin.model.map.Position

enum class EventBossType(
	val id: Int,
	val positions: MutableList<Position>,
	val hitpoints: Int,
	val message: MutableList<String>,
	val rolls: Int,
	val lootTable: LootTable,
	val embedUrl: String,
	val pet: Pet,
	val petChance: Int
) {

	AVATAR_OF_CREATION(
		10531,
		mutableListOf(
			Position.of(3197, 4034, 0)
		),
		5000,
		mutableListOf("The Avatar Of Creation has spawned! At the wilderness boss pit! In level 65 wilderness!"),
		1,
		AvatarOfCreationLoot(),
		"https://prifddinas.io/img/avc.png",
		Pet.LIL_CREATOR,
		150
	),
	AVATAR_OF_DESTRUCTION(
		10532,
		mutableListOf(
			Position.of(3197, 4034, 0)
		),
		5000,
		mutableListOf("The Avatar Of Destruction has spawned! At the wilderness boss pit! In level 65 wilderness!"),
		1,
		AvatarOfDestructionLoot(),
		"https://prifddinas.io/img/avd.png",
		Pet.LIL_DESTRUCTOR,
		150
	),
}
