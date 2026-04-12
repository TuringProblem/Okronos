package npc.nex.attacks.impl.spec;

import io.ruin.model.World;
import npc.nex.attacks.Attack;
import npc.nex.attacks.SpecialAttack;
import npc.nex.modes.Forms;
import npc.nex.scripts.NexCombat;
import io.ruin.model.entity.player.Player;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-04
 */
public record SoulSplitAttack(boolean deflectAfter) implements Attack {

	@Override
	public void invoke(Player target, NexCombat combat) {
		if (combat.getNpc().getId() != Forms.DEFAULT_NEX.getNpcId())
			return;

		combat.getNpc().transform(Forms.SOUL_SPLIT_NEX.getNpcId());
		World.startEvent(event -> {
			event.delay(18);
			if (combat.getNpc().getId() != Forms.DEFAULT_NEX.getNpcId() ||
				combat.isDead() ||
				combat.getNpc() == null ||
				combat.getNpc().isRemoved()
			) return;

			combat.getNpc().transform(Forms.DEFAULT_NEX.getNpcId());
			if (deflectAfter)
				SpecialAttack.deflectMeleeAttack(combat);
		});
	}
}
