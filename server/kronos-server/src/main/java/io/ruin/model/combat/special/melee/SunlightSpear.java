package io.ruin.model.combat.special.melee;

import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.AccurateBlows;
import io.ruin.model.combat.*;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.handlers.EquipmentStats;
import io.ruin.model.map.Position;
import io.ruin.model.var.VarPlayerRepository;

import java.util.ArrayList;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-06-03
 */
public class SunlightSpear implements Special {

	private static final int SUNLIGHT_SPEAR = 30369;

	private static final int SUNLIGHT_SPEAR_ANIMATION = 11933;

	// GFX
	private static final int SHIELD_SLAM_DUST_TRAVEL_NORTH = 3082;
	private static final int SHIELD_SLAM_DUST_TRAVEL_SOUTH = 3080;
	private static final int SHIELD_SLAM_DUST_TRAVEL_EAST = 3083;
	private static final int SHIELD_SLAM_DUST_TRAVEL_WEST = 3081;
	private static final int SHIELD_SLAM_DUST_TRAVEL_SOUTH_WEST = 3085;
	private static final int SHIELD_SLAM_DUST_TRAVEL_SOUTH_EAST = 3084;
	private static final int SHIELD_SLAM_DUST_TRAVEL_NORTH_WEST = 3086;
	private static final int SHIELD_SLAM_DUST_TRAVEL_NORTH_EAST = 3087;


	@Override
	public boolean accept(ObjType def, String name) {
		return def.getId() == SUNLIGHT_SPEAR;
	}

	@Override
	public boolean handleActivation(Player player) {
		if (VarPlayerRepository.SUNLIGHT_SPEAR_STACKS.get(player) < 7)
			player.sendMessage("You don't have enough sunlight to use this.");
		else {
			// Animate
			player.animate(SUNLIGHT_SPEAR_ANIMATION);
			// graphics
			shieldSlamGfx(player);
			// hit all targets in the area
			shieldSlamDamage(player);
			VarPlayerRepository.SUNLIGHT_SPEAR_STACKS.increment(player, -7);
		}
		return true;
	}

	/**
	 * Inflicts typeless area damage to all NPCs within a specified radius around the player.
	 * Damage is determined randomly up to a specified maximum value.
	 *
	 * @param player The player who initiates the shield slam, serving as the center of the area.
	 */
	private void shieldSlamDamage(Player player) {
		var maxDamage = CombatUtils.getMaxDamage(player, AttackStyle.STAB, AttackType.AGGRESSIVE);
		var centerPos = player.getPosition().copy();

		player.localNpcs().forEach(t -> {
			if (t != null) {
				if (t.getPosition().isWithinDistance(centerPos, 3)) {
					var prayerBonus = player.getEquipment().bonuses[EquipmentStats.PRAYER];
					var modifier = 0.03 * prayerBonus;
					// slap some typeless damage
					t.hit(new Hit(HitType.DAMAGE)
						.randDamage(maxDamage)
						.boostDamage(modifier));
				}
			}
		});
	}

	private void shieldSlamGfx(Player player) {
		var centerPos = player.getPosition().copy();
		// local ring
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_NORTH, 0, 0, Position.of(centerPos.getX(), centerPos.getY() + 1));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_SOUTH, 0, 0, Position.of(centerPos.getX(), centerPos.getY() - 1));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_EAST, 0, 0, Position.of(centerPos.getX() + 1, centerPos.getY()));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_WEST, 0, 0, Position.of(centerPos.getX() - 1, centerPos.getY()));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_SOUTH_WEST, 0, 0, Position.of(centerPos.getX() - 1, centerPos.getY() - 1));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_SOUTH_EAST, 0, 0, Position.of(centerPos.getX() + 1, centerPos.getY() - 1));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_NORTH_WEST, 0, 0, Position.of(centerPos.getX() - 1, centerPos.getY() + 1));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_NORTH_EAST, 0, 0, Position.of(centerPos.getX() + 1, centerPos.getY() + 1));
		// middle ring
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_NORTH, 0, 3, Position.of(centerPos.getX(), centerPos.getY() + 2));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_SOUTH, 0, 3, Position.of(centerPos.getX(), centerPos.getY() - 2));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_EAST, 0, 3, Position.of(centerPos.getX() + 2, centerPos.getY()));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_WEST, 0, 3, Position.of(centerPos.getX() - 2, centerPos.getY()));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_SOUTH_WEST, 0, 3, Position.of(centerPos.getX() - 2, centerPos.getY() - 2));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_SOUTH_EAST, 0, 3, Position.of(centerPos.getX() + 2, centerPos.getY() - 2));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_NORTH_WEST, 0, 3, Position.of(centerPos.getX() - 2, centerPos.getY() + 2));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_NORTH_EAST, 0, 3, Position.of(centerPos.getX() + 2, centerPos.getY() + 2));
		// outer ring
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_NORTH, 0, 6, Position.of(centerPos.getX(), centerPos.getY() + 3));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_SOUTH, 0, 6, Position.of(centerPos.getX(), centerPos.getY() - 3));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_EAST, 0, 6, Position.of(centerPos.getX() + 3, centerPos.getY()));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_WEST, 0, 6, Position.of(centerPos.getX() - 3, centerPos.getY()));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_SOUTH_WEST, 0, 6, Position.of(centerPos.getX() - 3, centerPos.getY() - 3));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_SOUTH_EAST, 0, 6, Position.of(centerPos.getX() + 3, centerPos.getY() - 3));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_NORTH_WEST, 0, 6, Position.of(centerPos.getX() - 3, centerPos.getY() + 3));
		World.sendGraphics(SHIELD_SLAM_DUST_TRAVEL_NORTH_EAST, 0, 6, Position.of(centerPos.getX() + 3, centerPos.getY() + 3));
	}

	public static void register() {
		ObjType.get(SUNLIGHT_SPEAR).sunlightSpear = true;
	}
}
