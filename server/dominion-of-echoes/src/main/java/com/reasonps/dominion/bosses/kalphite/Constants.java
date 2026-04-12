package com.reasonps.dominion.bosses.kalphite;

import io.ruin.model.map.Projectile;

import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-16
 */
public class Constants {
	public static final Projectile MAGIC_PROJECTILE = new Projectile(3173, 65, 43, 45, 90, 0, 20, 240).regionBased();
	public static final Projectile MAGIC_CHAIN_PROJECTILE = new Projectile(4231, 43, 43, 30, 90, 0, 20, 11).regionBased();
	public static final Projectile RANGED_PROJECTILE = new Projectile(289, 25, 30, 45, 50, 10, 20, 240).regionBased();

	public static final int TRANSFORM_ANIM = 6270;
	public static final int TRANSFORM_GFX = 3179;

	protected static final List<Integer> FLAILS = List.of(4755, 4982, 4983, 4984, 4985);
}
