package org.rsmod.api.repo.player

import jakarta.inject.Inject
import org.rsmod.api.registry.player.PlayerRegistry
import org.rsmod.game.entity.Player
import org.rsmod.map.CoordGrid
import org.rsmod.map.zone.ZoneKey

public class PlayerRepository @Inject constructor(private val registry: PlayerRegistry) {
    public fun hide(player: Player) {
        registry.hide(player)
    }

    public fun reveal(player: Player) {
        registry.reveal(player)
    }

	@JvmName("findAllZone")
    public fun findAll(zone: ZoneKey): Sequence<Player> = registry.findAll(zone)

	@JvmName("findAllCoord")
    public fun findAll(coords: CoordGrid): Sequence<Player> =
        findAll(ZoneKey.from(coords)).filter { it.coords == coords }

	@JvmName("findAllZoneEntries")
	public fun findAllEntries(zone: ZoneKey): List<Player>? = registry.findAllEntries(zone)

	@JvmName("findAllCoordEntries")
	public fun findAllEntries(coords: CoordGrid): List<Player>? =
		findAllEntries(ZoneKey.from(coords))/*?.filter { it.coords == coords }*/

	@JvmName("findAllZone")
    public fun findAll(zone: ZoneKey, zoneRadius: Int): Sequence<Player> {
        return sequence {
            for (x in -zoneRadius..zoneRadius) {
                for (z in -zoneRadius..zoneRadius) {
                    val translate = zone.translate(x, z)
                    val players = findAll(translate)
                    yieldAll(players)
                }
            }
        }
    }
}
