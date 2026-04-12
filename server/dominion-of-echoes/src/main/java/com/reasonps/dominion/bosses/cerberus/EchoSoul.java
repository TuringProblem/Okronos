package com.reasonps.dominion.bosses.cerberus;

import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.Misc;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

import static com.reasonps.dominion.bosses.cerberus.Constants.*;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
@Getter
@RequiredArgsConstructor
public enum EchoSoul {

	RANGED(17003, AttackStyle.RANGED, (npc, p) -> {
		npc.animate(8530);
		SOUL_RANGED_PROJECTILE.send(npc, p);
	}),
	MAGIC(17002, AttackStyle.MAGIC, (npc, p) -> {
		npc.animate(8529);
		SOUL_MAGIC_PROJECTILE.send(npc, p);
	}),
	MELEE(17001, AttackStyle.SLASH, SOUL_MELEE_PROJECTILE::send);

	final int npcId;
	final AttackStyle style;
	final BiConsumer<NPC, Player> animate;

	public void attack(NPC npc, @Nonnull Player player, NPC cerb) {
		if (Misc.getDistance(npc.getPosition(), player.getPosition()) > 20)
			return;
		npc.face(player);
		animate.accept(npc, player);
		boolean block = (style.isRanged() && player.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
			|| (style.isMagic() && player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
			|| (style.isMelee() && player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE));
		var hit = new Hit(null, style, null).delay(2);
		if (block) {
			((EchoCerberus) cerb.getCombat()).ghostsBlocked++;
			hit.block();
		}
		else {
			((EchoCerberus) cerb.getCombat()).damagedByGhosts = true;
			hit.fixedDamage(30);
			hit.ignoreDefence();
			hit.ignorePrayer();
		}
		player.hit(hit);
	}

}
