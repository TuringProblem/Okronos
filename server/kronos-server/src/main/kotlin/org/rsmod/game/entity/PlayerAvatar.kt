package org.rsmod.game.entity

import io.ruin.model.entity.player.Player

public class PlayerAvatar(kronos: Player) : PathingEntityAvatar(kronos, size = 1) {
    public var name: String = ""
}
