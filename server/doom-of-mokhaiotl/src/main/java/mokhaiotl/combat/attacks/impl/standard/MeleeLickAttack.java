package mokhaiotl.combat.attacks.impl.standard;

import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import mokhaiotl.combat.attacks.Attack;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-30
 */
public class MeleeLickAttack implements Attack {

	@Override
	public void invoke(Player target, NPCCombat mokhaiotl) {
		mokhaiotl.getNpc().animate(12416);

		var hit = new Hit(mokhaiotl.getNpc(), AttackStyle.CRUSH);
			hit.randDamage(40);

		target.hit(hit);
	}
}
