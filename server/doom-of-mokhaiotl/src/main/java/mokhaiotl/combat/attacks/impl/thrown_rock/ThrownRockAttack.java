package mokhaiotl.combat.attacks.impl.thrown_rock;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;
import mokhaiotl.combat.attacks.Attack;

import java.util.ArrayList;
import java.util.List;

import static core.task.api.API.queue;
import static core.task.api.API.sleep;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-30
 */
public abstract class ThrownRockAttack implements Attack {

	protected final Projectile RANGED_ROCK_PROJECTILE = new Projectile(
		3384,
		96,
		128,
		32,
		128,
		5,
		30,
		240
	).regionBased();

	protected final Projectile MAGIC_ROCK_PROJECTILE = new Projectile(
		3385,
		96,
		128,
		32,
		128,
		5,
		30,
		240
	).regionBased();

	protected final Projectile RANGED_PROJECTILE = new Projectile(
		3380,
		10,
		30,
		45,
		96,
		10,
		20,
		240
	).regionBased();

	protected final Projectile MAGIC_PROJECTILE = new Projectile(
		3379,
		10,
		30,
		45,
		96,
		10,
		20,
		240
	).regionBased();

	protected Projectile getDebrisProjectile(int projectileId) {
		return new Projectile(
			projectileId,
			128,
			0,
			32,
			96,
			5,
			20,
			240
		).regionBased();
	}

	protected void explodeRockMidAir(Projectile projectile,
								   Prayer protectionPrayer,
								   int delay,
								   Position destination,
								   Player target,
								   NPCCombat mokhaiotl
	) {
		var debris = new ArrayList<>(getDebrisProjectileIds());
		var dangerTiles = new ArrayList<>(getDangerTiles(destination));

		World.startEvent(World.getTicks(delay), event -> {
			var fireFrom = destination.copy();
			var d = 5;
			for (int i = 0; i < 6; i++) {
				var projectileId = Random.get(debris);
				var dest = Random.get(dangerTiles);
				debris.remove(projectileId);
				dangerTiles.remove(dest);
				d = getDebrisProjectile(projectileId).send(destination, dest);
				if (Random.get() < 0.5)
					fireFrom = dest;
				World.sendGraphics(3404, 0, d, dest);
			}

			event.delay(World.getTicks(d));

			var rangedDelay = projectile.send(fireFrom, target);

			event.delay(World.getTicks(rangedDelay));

			fireAtPlayer(fireFrom, projectile, protectionPrayer, target);
		}).setCancelCondition(mokhaiotl::isDead);
	}

	private void fireAtPlayer(Position fireFrom,
							  Projectile projectile,
							  Prayer protectionPrayer,
							  Player target
	) {
		var damage = target.getPrayer().isActive(protectionPrayer) ?
			0 : 47;
		target.hit(new Hit().randDamage(damage));
		var delveLevel = target.get("MOKHAIOTL_DELVE_LEVEL", 1);
		if (delveLevel > 7) {
			queue(() -> {
				sleep(2);

				var prj = projectile.getGfxId() == 3384 ? MAGIC_ROCK_PROJECTILE : RANGED_ROCK_PROJECTILE;
				var pray = projectile.getGfxId() == 3384 ? Prayer.PROTECT_FROM_MAGIC : Prayer.PROTECT_FROM_MISSILES;
				var delay2 = prj.send(fireFrom, target);

				sleep(World.getTicks(delay2));

				var dmg = target.getPrayer().isActive(pray) ?
					0 : 47;

				target.hit(new Hit().randDamage(dmg));
			});
		}
	}

	private List<Position> getDangerTiles(Position destination) {
		return new Bounds(destination, 5)
			.getAllPositions();
	}

	private List<Integer> getDebrisProjectileIds() {
		return List.of(
			3388, 3389, 3390,
			3391, 3392, 3393,
			3394, 3395, 3396,
			3397, 3398, 3399,
			3400, 3401, 3402,
			3403
		);
	}
}
