package io.ruin.model.activities.bosses.grotesqueguardians;

import io.ruin.api.utils.Random;
import io.ruin.cache.NpcID;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.object.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Dawn extends NPCCombat {
	private static final Projectile STUN_BALL_PROJECTILE = new Projectile(1477, 60, 31, 35, 35, 39, 0, 32);
	private static final Projectile RANGED_PROJECTILE = new Projectile(1437, 60, 31, 35, 35, 22, 0, 32);

	Bounds bossBounds;

	NPC dusk;


	@Override
	public void init() {
		npc.hitListener = new HitListener().preDefend(this::preDefend).postDamage(this::postDamage);
		bossBounds = new Bounds(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 23, npc.getPosition().getRegion().baseX + 40, npc.getPosition().getRegion().baseY + 38, 0);
		npc.localNpcs().forEach(npc -> {
			if (npc.getId() == NpcID.DUSK || npc.getId() == NpcID.DUSK_7851 || npc.getId() == NpcID.DUSK_7854 || npc.getId() == NpcID.DUSK_7882 || npc.getId() == NpcID.DUSK_7883) {
				dusk = npc;
			}
		});
	}

	int phase = 1;

	private void postDamage(Hit hit) {
		int hpPercent = npc.getHp() * 100 / npc.getMaxHp();
		if (phase == 1 && hpPercent < 55 && hit.attacker != null) {
			npc.animate(7773);
			World.startEvent(e -> {
				e.delay(2);
				startPhaseTwo(hit.attacker.player);
			});
		}
	}

	private void meleeAttack() {
		npc.animate(7769);
		defendAnim();
		Hit hit = new Hit(npc, AttackStyle.CRUSH, null).randDamage(35);
		target.hit(hit);
	}

	@Override
	public int getAggressionRange() {
		if (npc.getId() == 7853)
			return 0;
		return 600;
	}

	@Override
	public int getAttackBoundsRange() {
		if (npc.getId() == 7853)
			return 0;
		return 600;
	}

	private void startPhaseTwo(Player player) {
		npc.setHidden(true);
		phase = 2;
	}


	private void preDefend(Hit hit) {
		if (hit.attackStyle != null && hit.attackStyle.isMelee()) {
			hit.attacker.player.sendFilteredMessage("You can't reach that.");
			hit.block();
		}
	}

	@Override
	public void follow() {
		follow(5);
	}

	private void rockFall(Player player) {
		World.startEvent(e -> {
			List<Position> impactPositions = new ArrayList<>();
			impactPositions.add(player.getPosition().copy());
			for (int i = 0; i < 10; i++) {
				Position randomPosition = bossBounds.randomPosition();
				impactPositions.add(randomPosition);
			}
			for (Position impactPosition : impactPositions) {
				World.sendGraphics(1727, 0, 0, impactPosition);
				e.delay(4);
				if (player.getPosition().distance(impactPosition) < 2) {
					player.hit(new Hit().fixedDamage(22));
				}
			}
		});
	}

	int attacks = 0;

	@Override
	public boolean attack() {
		if (npc.isHidden() || npc.getId() == 7853)
			return false;
		npc.face(target);
		if (dusk != null && dusk.isHidden() && Random.get(6) == 0) {
			rockFall(target.player);
		}
		if (Random.get(5) == 0) {
			stunBallAttack(target.player);
		} else {
			if (withinDistance(2))
				meleeAttack();
			else
				rangedAttack(target.player);
		}
		if (phase == 2 && attacks++ == 4) {
			//sendPurpleHealOrbs();
		}
		return true;
	}

	@Override
	public void process() {
		if (dusk == null) {
			npc.localNpcs().forEach(npc -> {
				if (npc.getId() == NpcID.DUSK || npc.getId() == NpcID.DUSK_7851 || npc.getId() == NpcID.DUSK_7854 || npc.getId() == NpcID.DUSK_7882 || npc.getId() == NpcID.DUSK_7883) {
					dusk = npc;
				}
			});
		}
		if (target == null && !npc.localPlayers().isEmpty())
			target = Random.get(npc.localPlayers());


	}


	private void rangedAttack(Player player) {
		npc.animate(7770);
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getHp() < 1 || target == null);
			for (int i = 0; i < 2; i++) {
				int delay = RANGED_PROJECTILE.send(npc, target);
				Hit hit = new Hit(npc, AttackStyle.RANGED)
					.randDamage(9)
					.clientDelay(delay);
				player.hit(hit);
				e.delay(1);
			}
		});
	}

	private void stunBallAttack(Player player) {
		npc.animate(7771);
		Position targetPosition = player.getPosition().copy();
		List<Position> positions = new ArrayList<>();
		for (int x = -2; x <= 2; x++) {
			for (int y = -2; y <= 2; y++) {
				positions.add(new Position(targetPosition.getX() + x, targetPosition.getY() + y, targetPosition.getZ()));
			}
		}
		Position randomPosition = Random.get(positions);
		World.sendGraphics(2111, 0, 0, randomPosition);
		int delay = STUN_BALL_PROJECTILE.send(npc, randomPosition);
		World.startEvent(e -> {
			for (int i = 0; i <= World.getTicks(delay) + 1; i++) {
				World.sendGraphics(2111, 0, 0, randomPosition);
				e.delay(1);
			}
			World.sendGraphics(1478, 0, 0, randomPosition);
			if (player.getPosition().distance(randomPosition) < 1) {
				player.stun(5, true);
				player.getPrayer().slashPrayers();
				player.hit(new Hit().fixedDamage(45));
			}
		});

	}
}
