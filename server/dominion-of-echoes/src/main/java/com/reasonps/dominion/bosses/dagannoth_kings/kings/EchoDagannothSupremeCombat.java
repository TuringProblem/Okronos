package com.reasonps.dominion.bosses.dagannoth_kings.kings;

import com.reasonps.dominion.bosses.dagannoth_kings.EchoDagannothKingCombatCombat;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-28
 */
public class EchoDagannothSupremeCombat extends EchoDagannothKingCombatCombat {

	private final Projectile PROJECTILE =
		new Projectile(3167, 65, 31, 15, 56, 10, 15, 64).regionBased();

	@Override
	public boolean attack() {
		if (!withinDistance(getAttackBoundsRange())) return false;

		npc.animate(info.attack_animation);
		int delay = PROJECTILE.send(npc, target);
		var hit = new Hit(npc, AttackStyle.RANGED).randDamage(info.max_damage).clientDelay(delay);
			hit.preDamage(entity -> {
				if (entity instanceof Player t && (t.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)))
					hit.damage = (int) (hit.damage / 0.33);
			});

		target.hit(hit);
		return true;
	}

	@Override
	public void togglePrayAgainstAll() {
		if (getNpc().overheadPrayer() != null)
			getNpc().removeHeadIcon();
		else
			getNpc().setHeadIcon(NPC.DefaultHeadIconIndex.MageRangeMelee);
	}
}
