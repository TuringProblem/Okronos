package com.reasonps.dominion.rooms;

import io.ruin.model.activities.bosses.instancetoken.MapHandler;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-19
 */
public abstract class Dominion extends MapHandler {

	@Override
	public boolean isCannonRestricted() {
		return true;
	}
}
