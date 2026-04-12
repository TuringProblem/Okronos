package mokhaiotl.combat.attacks.impl;

import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;
import mokhaiotl.combat.attacks.Attack;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-30
 */
public class ChargeBlastAttack implements Attack {

	private final Projectile PROJECTILE = new Projectile(
		3409,
		64,
		30,
		45,
		50,
		10,
		20,
		240
	).regionBased();

	@Override
	public void invoke(Player target, NPCCombat mokhaiotl) {
		mokhaiotl.getNpc().animate(12410);
		mokhaiotl.getNpc().graphics(3413, 36, 0);

		var delay = PROJECTILE.send(mokhaiotl.getNpc().getCentrePosition(), target);

		var hit = new Hit(mokhaiotl.getNpc());
			hit.delay(World.getTicks(delay));
			hit.randDamage(getMaxHitForDelve(target.get("MOKHAIOTL_DELVE_LEVEL")));

		target.hit(hit);
	}

	private int getMaxHitForDelve(int delve) {
		return switch(delve) {
			case 1, 2, 3 -> 60;
			case 4, 5 -> 80;
			default -> 99;
		};
	}
}
