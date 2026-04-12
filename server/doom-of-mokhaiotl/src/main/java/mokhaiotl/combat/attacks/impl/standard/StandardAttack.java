package mokhaiotl.combat.attacks.impl.standard;

import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;
import mokhaiotl.combat.MokhaiotlCombat;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-15
 */
public class StandardAttack {

	private final int MAX_DAMAGE = 54;

	protected void attemptToAnimate(MokhaiotlCombat boss) {
		if (!boss.isBurrowed()) {
			var shieldUp = boss.getDoomOfMokhaiotl().getId() == 14708;
			boss.getDoomOfMokhaiotl().animate(shieldUp ? 12407 : 12406);
		}
	}

	protected void fireProjectile(Player target, AttackStyle style, Projectile projectile, NPC mokhaiotl) {
		fireProjectile(target, style, projectile, mokhaiotl.getCentrePosition(), mokhaiotl);
	}

	protected void fireProjectile(Player target, AttackStyle style, Projectile projectile, Position sourcePos) {
		var delay = projectile.send(sourcePos, target);
		var hit = projectileHit(null, target, style, delay);
		target.hit(hit);
	}

	protected void fireProjectile(Player target, AttackStyle style, Projectile projectile, Position sourcePos,
			NPC attacker) {
		var delay = projectile.send(sourcePos, target);
		var hit = projectileHit(attacker, target, style, delay);
		target.hit(hit);
	}

	private Hit projectileHit(NPC attacker, Player target, AttackStyle style, int delay) {
		var hit = new Hit(attacker, style);
		hit.randDamage(MAX_DAMAGE);
		hit.delay(World.getTicks(delay) + 1);
		hit.preDamage(_ -> {
			var protection = getProtectionPrayerForAttackStyle(style);
			if (protection != null && target.getPrayer().isActive(protection))
				hit.block();
		});
		return hit;
	}

	private Prayer getProtectionPrayerForAttackStyle(AttackStyle style) {
		return switch (style) {
			case CRUSH -> Prayer.PROTECT_FROM_MELEE;
			case MAGIC -> Prayer.PROTECT_FROM_MAGIC;
			case RANGED -> Prayer.PROTECT_FROM_MISSILES;
			default -> null;
		};
	}
}
