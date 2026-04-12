package org.rsmod.util

import io.ruin.model.entity.Entity
import io.ruin.model.map.Position
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.rsmod.map.CoordGrid
import org.rsmod.map.zone.ZoneKey
import io.ruin.model.entity.npc.NPC as KronosNPC
import io.ruin.model.entity.player.Player as KronosPlayer

val Position.coords: CoordGrid
	get() = CoordGrid(x, y, z)

val Entity.coords: CoordGrid
	get() = position.coords

fun Entity.zoneKey(): ZoneKey = ZoneKey.from(coords)

fun Entity.zoneKeyInt(): Int = zoneKey().packed

@JvmOverloads
fun Entity.zoneKeyTranslate(
	xOffset: Int = 0,
	zOffset: Int = 0,
	levelOffset: Int = 0,
): ZoneKey {
	val zoneKey = zoneKey()
	return zoneKey.translate(xOffset, zOffset, levelOffset)
}

@JvmOverloads
fun Entity.zoneKeyTranslateInt(
	xOffset: Int = 0,
	zOffset: Int = 0,
	levelOffset: Int = 0,
): Int = zoneKeyTranslate(xOffset, zOffset, levelOffset).packed

@JvmOverloads
fun searchZoneNpcs(
	entity: Entity,
	radius: Int = 1
): List<KronosNPC> {
	val npcs: MutableList<KronosNPC> = ObjectArrayList()
	val zoneKey = entity.zoneKey()
	for (xOffset in -radius..radius) {
		for (zOffset in -radius..radius) {
			val nx = zoneKey.x + xOffset
			val ny = zoneKey.z + zOffset

			if ((nx !in 0..2047) || (ny !in 0..2047)) {
				continue
			}

			val zone = zoneKey.translate(xOffset, zOffset)
			val entries = RsmodGlobal.npcRegistry.findAllEntries(zone) ?: continue
			npcs.addAll(entries.map { it.kronos })
		}
	}
	return npcs
}

@JvmOverloads
fun searchZoneNpcs(
	x: Int,
	y: Int,
	plane: Int,
	radius: Int = 1
): List<KronosNPC> {
	val npcs: MutableList<KronosNPC> = ObjectArrayList()
	val zoneKey = ZoneKey.fromAbsolute(x, y, plane)
	for (xOffset in -radius..radius) {
		for (zOffset in -radius..radius) {
			val nx = zoneKey.x + xOffset
			val ny = zoneKey.z + zOffset

			if ((nx !in 0..2047) || (ny !in 0..2047)) {
				continue
			}

			val zone = zoneKey.translate(xOffset, zOffset)
			val entries = RsmodGlobal.npcRegistry.findAllEntries(zone) ?: continue
			npcs.addAll(entries.map { it.kronos })
		}
	}
	return npcs
}

@JvmOverloads
fun searchZonePlayers(
	entity: Entity,
	radius: Int = 1
): List<KronosPlayer> {
	val players: MutableList<KronosPlayer> = ObjectArrayList()
	val zoneKey = entity.zoneKey()
	for (xOffset in -radius..radius) {
		for (zOffset in -radius..radius) {
			val nx = zoneKey.x + xOffset;
			val ny = zoneKey.z + zOffset;

			if ((nx !in 0..2047) || (ny !in 0..2047)) {
				continue
			}
			val zone = zoneKey.translate(xOffset, zOffset)
			val entries = RsmodGlobal.playerRegistry.findAllEntries(zone) ?: continue
			players.addAll(entries.map { it.kronos })
		}
	}
	return players
}