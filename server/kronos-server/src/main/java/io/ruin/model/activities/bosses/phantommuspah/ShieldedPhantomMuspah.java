package io.ruin.model.activities.bosses.phantommuspah;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;

public class ShieldedPhantomMuspah extends NPCCombat {
	private final Projectile MAGIC_PROJECTILE = new Projectile(0, 0, 0, 0, 0, 0, 0, 0);
	private final Projectile RANGED_PROJECTILE = new Projectile(0, 0, 0, 0, 0, 0, 0, 0);

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDefend(this::postDefend).preDefend(this::preDefend);
	}

	private void postDefend(Hit hit) {
	}

	private void preDefend(Hit hit) {
		if (hit.attacker != null && hit.attacker.isPlayer()) {
			Player player = hit.attacker.player;
			int damageDivisionAmount = 10;
			if (player.getPrayer().isActive(Prayer.SMITE))
				damageDivisionAmount = 5;
			hit.damage /= damageDivisionAmount;
		}
	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		return false;
	}

	@Override
	public void process() {

	}

	private void rangedAttack(Player target) {
		World.startEvent(e -> {
			npc.animate(9922);
			int delay = RANGED_PROJECTILE.send(npc, target);
			int damage = 43;
			e.delay(World.getTicks(delay));
			if (target == null || target.getHp() < 1 || target.getCombat().isDead())
				return;
			if (target.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
				damage = 9;
			}
			target.hit(new Hit(npc).randDamage(damage).ignorePrayer().clientDelay(delay));
		});
	}

	private void chargedMagicAttack(Player target) {
		World.startEvent(e -> {
			npc.animate(9918);
			e.delay(2);
			if (target == null || target.getHp() < 1 || target.getCombat().isDead())
				return;
			int delay = MAGIC_PROJECTILE.send(npc, target);
			int damage = 47;
			boolean isProtected = false;
			if (target.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
				damage = 9;
				isProtected = true;
			}
			target.hit(new Hit(npc).randDamage(damage).ignorePrayer().clientDelay(delay));
			if (!isProtected) {
				target.sendMessage("The Phantom Muspah's magic attack has corrupted you!");
				startCorruptionEvent(target);
			}
		});
	}

	private void startCorruptionEvent(Player player) {
		World.startEvent(e -> {
			for (int i = 0; i < 10; i++) {
				e.delay(6);
				if (player == null || player.getHp() < 1 || player.getCombat().isDead())
					break;
				player.getPrayer().drain(Random.get(3, 7));
			}
		});
	}
}
