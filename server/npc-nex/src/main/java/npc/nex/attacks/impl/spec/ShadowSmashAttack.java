package npc.nex.attacks.impl.spec;

import io.ruin.model.World;
import npc.nex.attacks.Attack;
import npc.nex.scripts.NexCombat;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-04
 */
public class ShadowSmashAttack implements Attack {
	@Override
	public void invoke(Player target, NexCombat combat) {
		combat.getNpc().forceText(SHADOW_SMASH);
		combat.getNpc().getPosition().getRegion().players.forEach(p -> {
			if (!p.getPosition().inBounds(combat.ATTACK_BOUNDS))
				return;
			p.sendMessage("Nex: " + SHADOW_SMASH);
		});
		combat.getNpc().animate(9186);
		combat.getNpc().faceTemp(target);
		var targets = combat.getNpc().getPosition().getRegion().players;
		targets.forEach(p -> {
			if (!p.getPosition().inBounds(combat.ATTACK_BOUNDS))
				return;
			final var pos = p.getPosition().copy();
			final var shadow = new GameObject(SHADOW_SMASH_OBJECT_ID, p.getPosition().copy(), 10, 1).spawn();
			shadow.graphics(SHADOW_SMASH_GFX);
			var tile = Tile.get(pos.getX(), pos.getY(), pos.getZ(), true);
			World.startEvent(event -> {
				event.delay(SHADOW_SMASH_DELAY);
				if (tile.playerCount > 0) {
					targets.stream().filter(t -> t.getPosition().equals(pos)).forEach(noob -> {
						combat.damagedByShadow = true;
						Hit hit = new Hit(combat.getNpc(), AttackStyle.MAGIC)
							.fixedDamage(SHADOW_SMASH_MAX_HIT)
							.ignoreDefence()
							.ignorePrayer();
						noob.hit(hit);
					});
				}
				if (shadow.isSpawned()) {
					shadow.graphics(-1);
					shadow.remove();
				}
			});
		});
	}
}
