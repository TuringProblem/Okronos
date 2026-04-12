package com.reasonps.dominion.bosses.cerberus;

import io.ruin.model.map.Projectile;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class Constants {

	public static final Projectile MAGIC_PROJECTILE = new Projectile(4236, 65, 31, 25, 64, 20, 15, 220).regionBased();
	public static final Projectile RANGED_PROJECTILE = new Projectile(4237, 65, 31, 25, 64, 20, 15, 220).regionBased();

	public static final Projectile SOUL_MAGIC_PROJECTILE = new Projectile(100, 31, 31, 15, 80, 0, 24, 11).regionBased();
	public static final Projectile SOUL_RANGED_PROJECTILE = new Projectile(1120, 40, 36, 41, 80, 0, 30, 11).regionBased();
	public static final Projectile SOUL_MELEE_PROJECTILE = new Projectile(1248, 31, 31, 15, 80, 0, 24, 11).regionBased();

}
