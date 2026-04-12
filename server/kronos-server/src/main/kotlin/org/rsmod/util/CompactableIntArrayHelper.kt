package org.rsmod.util

import org.rsmod.game.type.util.CompactableIntArray

val ShortArray?.compactableIntArray: CompactableIntArray
	get() = this?.let { array -> CompactableIntArray(array.map { it.toInt() }.toIntArray()) }
		?: CompactableIntArray(0)