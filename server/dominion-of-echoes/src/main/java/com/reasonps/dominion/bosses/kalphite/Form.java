package com.reasonps.dominion.bosses.kalphite;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-16
 */
@Getter
@RequiredArgsConstructor
public enum Form {
	FIRST(6500, 6240, 6240, 3171),
	SECOND(6501, 6235, 6234, 3172);

	private final int npcId;
	private final int rangedAnimation;
	private final int magicAnimation;
	private final int magicGfx;
}
