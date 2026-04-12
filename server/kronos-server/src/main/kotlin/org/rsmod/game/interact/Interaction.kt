package org.rsmod.game.interact

import org.rsmod.game.entity.Npc
import org.rsmod.game.entity.PathingEntity
import org.rsmod.game.entity.npc.NpcUid

public sealed class Interaction(
    public val hasOpTrigger: Boolean,
    public val hasApTrigger: Boolean,
    public var apRange: Int,
    public var persistent: Boolean,
    public var apRangeCalled: Boolean = false,
    public var interacted: Boolean = false,
) {
    override fun toString(): String =
        "Interaction(" +
            "hasOpTrigger=$hasOpTrigger, " +
            "hasApTrigger=$hasApTrigger, " +
            "apRange=$apRange, " +
            "persistent=$persistent, " +
            "apRangeCalled=$apRangeCalled, " +
            "interacted=$interacted" +
            ")"
}

public sealed class InteractionNpc(
    public val target: Npc,
    hasOpTrigger: Boolean,
    hasApTrigger: Boolean,
    startApRange: Int,
    persistent: Boolean,
) : Interaction(hasOpTrigger, hasApTrigger, startApRange, persistent) {
    public val uid: NpcUid = target.uid

    init {
        check(uid != NpcUid.NULL) { "Npc does not have a proper uid: $target" }
    }

    override fun toString(): String =
        "InteractionNpc(" +
            "target=$target, " +
            "hasOpTrigger=$hasOpTrigger, " +
            "hasApTrigger=$hasApTrigger, " +
            "apRange=$apRange, " +
            "persistent=$persistent, " +
            "apRangeCalled=$apRangeCalled, " +
            "interacted=$interacted" +
            ")"
}

public class InteractionNpcOp(
    public val op: InteractionOp,
    target: Npc,
    hasOpTrigger: Boolean,
    hasApTrigger: Boolean,
    startApRange: Int = PathingEntity.DEFAULT_AP_RANGE,
    persistent: Boolean = false,
) : InteractionNpc(target, hasOpTrigger, hasApTrigger, startApRange, persistent)
