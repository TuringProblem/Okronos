package npc.nex.scripts;

import io.ruin.cache.NPCType;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;

import static core.task.api.API.queue;
import static core.task.api.API.sleep;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
public class GlaciesCombat extends NPCCombat {

	public static final int ID = 11286;

	private static final Projectile PROJECTILE = new Projectile(
			368,
			43,
			0,
			51,
			56,
			10,
			16,
			64).skipTravel();

	public static void register() {
		NPCType.registerCombat(GlaciesCombat.class, 11286);
	}

	@Override
	public void init() {
		npc.hitListener = new HitListener().preDefend(hit -> {
			if (npc.isInvincible())
				hit.nullify();
		});
	}

	@Override
	public boolean attack() {
		if (npc.isInvincible())
			return false;
		if (!withinDistance(10))
			return false;
		npc.publicSound(171, 1, 0);
		npc.animate(info.attack_animation);
		int delay = PROJECTILE.send(npc, target);
		queue(() -> {
			sleep(World.getTicks(delay));
			if (getNpc().isRemoved() || isDead())
				return;
			var hit = new Hit(npc, AttackStyle.MAGIC, null)
					.randDamage(target.isPlayer() && target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC) ? 0 : 29)
					.clientDelay(delay);

			hit.postDamage(t -> {
				if (hit.damage > 0) {
					npc.publicSound(168, 1, 0);
					t.graphics(369, 0, 0);
					if (!t.hasFreezeImmunity() && !t.isFrozen()) {
						t.freeze(20, npc);
						t.player.sendMessage("You have been frozen.");
					}
				} else {
					t.publicSound(227, 1, 0);
					t.graphics(85, 124, 0);
					hit.hide();
				}
			});
			target.hit(hit);
		});
		return true;
	}

	@Override
	public boolean allowRespawn() {
		return false;
	}

	@Override
	public void follow() {}

	@Override
	public void process() {}
}
