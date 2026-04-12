package org.rsmod.util

import io.ruin.cache.NPCType
import org.rsmod.api.cache.util.TextUtil
import org.rsmod.game.type.npc.NpcTypeBuilder
import org.rsmod.game.type.npc.UnpackedNpcType
import org.rsmod.game.type.util.CompactableIntArray

val NPCType.rsmod: UnpackedNpcType
	get() = NpcTypeBuilder(TextUtil.NULL).apply {
		if (this@rsmod.models != null) models = CompactableIntArray(this@rsmod.models)
		name = this@rsmod.name
		size = this@rsmod.size
		readyAnim = this@rsmod.standAnimation
		walkAnim = this@rsmod.walkAnimation
		turnLeftAnim = this@rsmod.turnLeftAnim
		turnRightAnim = this@rsmod.turnRightAnim
		category = this@rsmod.category
		this@rsmod.options.copyInto(op)
		recolS = this@rsmod.recolorToFind.compactableIntArray
		recolD = this@rsmod.recolorToReplace.compactableIntArray
	}.build(this.id)