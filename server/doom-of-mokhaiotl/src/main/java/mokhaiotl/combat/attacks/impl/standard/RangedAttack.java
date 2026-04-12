package mokhaiotl.combat.attacks.impl.standard;

import io.ruin.model.combat.AttackStyle;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Projectile;
import mokhaiotl.combat.MokhaiotlCombat;
import mokhaiotl.combat.attacks.Attack;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-30
 */
public class RangedAttack extends StandardAttack implements Attack {

	private final Projectile PROJECTILE = new Projectile(
		3380,
		96,
		30,
		45,
		50,
		10,
		30,
		240
	).regionBased();

	@Override
	public void invoke(Player target, NPCCombat mokhaiotl) {
		var boss = ((MokhaiotlCombat) mokhaiotl);
		attemptToAnimate(boss);
		fireProjectile(target, AttackStyle.RANGED, PROJECTILE, boss.getNpc());
	}
}
