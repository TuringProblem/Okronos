package io.ruin.model.activities.miscpvm.slayer;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.model.map.Projectile;
import io.ruin.model.var.VarPlayerRepository;

import java.util.Arrays;


public class Zygomite extends NPCCombat {
	public enum FungicideSpray {

		CHARGE_1(7430, null),
		CHARGE_2(7429, CHARGE_1),
		CHARGE_3(7428, CHARGE_2),
		CHARGE_4(7427, CHARGE_3),
		CHARGE_5(7426, CHARGE_4),
		CHARGE_6(7425, CHARGE_5),
		CHARGE_7(7424, CHARGE_6),
		CHARGE_8(7423, CHARGE_7),
		CHARGE_9(7422, CHARGE_8),
		CHARGE_10(7421, CHARGE_9);

		private final int id;
		private final FungicideSpray nextCharge;

		FungicideSpray(int id, FungicideSpray nextCharge) {
			this.id = id;
			this.nextCharge = nextCharge;
		}

		public static final FungicideSpray[] VALUES = values();
	}

	private static final int IDLE = 533;
	private static final int ACTIVE = 537;
	private boolean sprayed = false;

	private int attackCounter = 0;

	private static final Projectile PROJECTILE = new Projectile(576, 31, 31, 30, 50, 8, 0, 96);

	public static void register() {
		for (int i : Arrays.asList(537, 1024))
			for (FungicideSpray spray : FungicideSpray.values())
				ItemNPCAction.register(spray.id, i, (player, item, npc) -> {
					if (npc.getHp() <= 8) {
						((Zygomite) npc.getCombat()).sprayed = true;
						if (player.getInventory().contains(spray.id))

							player.getInventory().remove(spray.id, 1);
						if (spray.nextCharge != null)
							player.getInventory().add(spray.nextCharge.id, 1);
						npc.getCombat().startDeath(null);
					} else
						player.sendMessage("You use the fungicide spray, but the Fungi is not weak enough.");
				});

		NPCAction.register(533, "Pick", (player, npc) -> {
			if (npc.isLocked() || npc.getId() != 533)
				return;
			player.animate(3335);
			npc.lock();
			npc.addEvent(event -> {
				npc.animate(3329);
				npc.graphics(577);
				event.delay(2);
				npc.transform(ACTIVE);
				npc.face(player);
				event.delay(2);
				npc.unlock();
				npc.getCombat().setTarget(player);
				npc.face(player);
			});
		});
		NPCAction.register(535, "Pick", (player, npc) -> {
			if (npc.isLocked() || npc.getId() != 535)
				return;
			player.animate(3335);
			npc.lock();
			npc.addEvent(event -> {
				npc.animate(3329);
				npc.graphics(577);
				event.delay(2);
				npc.transform(ACTIVE);
				npc.face(player);
				event.delay(2);
				npc.unlock();
				npc.getCombat().setTarget(player);
				npc.face(player);
			});
		});
		NPCAction.register(1023, 1, (player, npc) -> {
			if (npc.isLocked() || npc.getId() != 1023)
				return;
			player.animate(3335);
			npc.lock();
			npc.addEvent(event -> {
				npc.animate(3329);
				npc.graphics(577);
				event.delay(2);
				npc.transform(ACTIVE);
				npc.face(player);
				event.delay(2);
				npc.unlock();
				npc.getCombat().setTarget(player);
				npc.face(player);
			});
		});
	}


	@Override
	public void init() {
		npc.deathEndListener = (entity, killer, killHit) -> {
			npc.transform(IDLE);
			sprayed = false;
			attackCounter = 0;
			npc.getCombat().reset();

		};
	}

	@Override
	public void reset() {
	}


	@Override
	public void follow() {
		follow(3);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(10)) {
			return false;
		}
		if (npc.getId() == IDLE) {
			return true; // whirlpool, dont attack
		}
		if (target.getPosition().isWithinDistance(npc.getPosition(), 1)) {
			basicAttack();
			return true;
		} else {
			if (npc.getPosition().isWithinDistance(target.getPosition()) && Random.rollDie(3)) {
				rangeAttack();
				return true;
			}
			return false;
		}
	}

	@Override
	public void process() {

	}

	private void rangeAttack() {
		int delay = PROJECTILE.send(npc, target);
		target.hit(new Hit(npc, AttackStyle.MAGICAL_RANGED, null).randDamage(info.max_damage).ignoreDefence().clientDelay(delay));
	}

	@Override
	public void startDeath(Hit killHit) {
		if (sprayed) {
			super.startDeath(killHit);
		}
		for (FungicideSpray spray : FungicideSpray.VALUES) {
			if (killHit != null && killHit.attacker != null && killHit.attacker.player != null && VarPlayerRepository.SHROOM_SPRAYER.get(killHit.attacker.player) == 1 && killHit.attacker.player.getInventory().contains(spray.id)) { // autosalt unlock
				if (spray.id == 7431) {
					player.sendMessage("You need to charge the fungicide spray first.");
					return;
				} else
					killHit.attacker.player.getInventory().remove(spray.id, 1);
				if (spray.nextCharge != null)
					killHit.attacker.player.getInventory().add(spray.nextCharge.id, 1);
				super.startDeath(killHit);
			} else {
				npc.setHp(1);
				npc.getCombat().reset();
			}
		}
	}


	@Override
	public boolean isAggressive() {
		return npc.getId() != IDLE;
	}
}
