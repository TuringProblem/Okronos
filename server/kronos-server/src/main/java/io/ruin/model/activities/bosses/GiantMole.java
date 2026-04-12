package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.skills.prayer.Prayer;

import java.util.Arrays;

public class GiantMole extends NPCCombat {

	public boolean damagedPlayer = false;

	public int hitsTaken = 0;

	private static final int[][] BURROW_POINTS = {
		{-1, 1},
	};


	private static int BURROW_DOWN_ANIM = 3314;
	private static int BURROW_SURFACE_ANIM = 3315;

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDamage(this::postDamage);
		npc.attackBounds = new Bounds(npc.getSpawnPosition(), 64);
	}


	private void postDamage(Hit hit) {
		if (!isDead() && npc.getHp() < npc.getMaxHp() / 2 && Random.get(30) == 0) {
			//burrow();
		}
		hitsTaken++;
	}


	private void burrow() {
		int[] offsets = Random.get(BURROW_POINTS);
//        System.out.println(Arrays.toString(offsets));
		Position burrowDestination = npc.getSpawnPosition().relative(offsets[0], offsets[1]);
		final Player player = target == null ? null : target.player;
		if (player != null)
			player.getCombat().reset();
		npc.lock(LockType.FULL_NULLIFY_DAMAGE);
		npc.attackNpcListener = (player1, npc1, message) -> false; // so the player can't spam click the mole to follow it wherever it teleports
		npc.addEvent(event -> {
			reset();
			npc.animate(BURROW_DOWN_ANIM);
			event.delay(3);
			npc.getMovement().teleport(burrowDestination);
			npc.animate(BURROW_SURFACE_ANIM);
			event.delay(2);
			if (player != null) {
				player.getPacketSender().sendHintIcon(npc.getAbsX(), npc.getAbsY());
				player.addEvent(e -> {
					e.waitForCondition(() -> player.getCombat().getTarget() == npc || !player.getPosition().isWithinDistance(npc.getPosition(), 64), 200);
					player.getPacketSender().resetHintIcon(false);
				});
			}
			npc.attackNpcListener = null;
			npc.unlock();
		});
	}

	@Override
	public void follow() {
		follow(2);
	}


	@Override
	public boolean attack() {
		if (!withinDistance(1))
			return false;
		meleeAttack();
		return true;
	}

	private void meleeAttack() {
		npc.animate(info.attack_animation);
		int maxDamage = info.max_damage;
		if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE))
			maxDamage = 0;
		Hit hit = new Hit(npc, info.attack_style).randDamage(maxDamage);
		if (hit.damage > 0)
			damagedPlayer = true;
		target.hit(hit);
	}

	@Override
	public void process() {


	}
}
