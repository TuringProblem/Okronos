package org.rsmod.game.movement

import org.rsmod.map.CoordGrid
import java.util.*

public data class RouteDestination(
    public val waypoints: Deque<CoordGrid> = ArrayDeque<CoordGrid>(),
    public var recalcRequest: RouteRequest? = null,
) : Deque<CoordGrid> by waypoints {
    internal fun abort() {
        waypoints.clear()
        recalcRequest = null
    }

    internal fun clearRecalc() {
        recalcRequest = null
    }
}
