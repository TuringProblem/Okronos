package org.rsmod.game.entity

import io.ruin.model.entity.npc.NPC
import org.rsmod.game.entity.npc.NoopNpcInfo
import org.rsmod.game.entity.npc.NpcInfoProtocol

public class NpcAvatar(kronos: NPC, size: Int, public var infoProtocol: NpcInfoProtocol = NoopNpcInfo) :
    PathingEntityAvatar(kronos, size)
