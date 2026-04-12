package com.reasonps.dominion.bosses.sol_heredit;

import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.utility.TickDelay;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-11
 */
public class HealingTotem extends NPC {

	private final NPCCombat boss;
	private final TickDelay delay = new TickDelay();

	public HealingTotem(NPCCombat boss) {
		super(12825);
		this.boss = boss;
		delay.delay(17);
		getDef().occupyTiles = false;
	}

	@Override
	public void process() {
		super.process();
		if (boss.isDead()) {
			this.remove();
		}

		// after a short delay, start healing the boss for 17 HP
		if (delay.finished()) {
			delay.delay(3);

			if (boss.getNpc().queuedHitsCount() > 100) {
				return;
			}

			boss.getNpc().hit(new Hit().type(HitType.HEAL_TOTEM_OTHER).fixedDamage(17));
		}
	}
}
