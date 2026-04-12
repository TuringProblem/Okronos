package io.ruin.model.activities.bosses.sarachnis;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.Region;
import io.ruin.model.map.object.GameObject;

public class Sarachnis extends NPCCombat {

	private static final Projectile RANGED_PROJECTILE = new Projectile(
		1686,
		20,
		31,
		35,
		35,
		10,
		0,
		32
	).regionBased();

	private static final int RANGED_ANIM = 4410;

	public boolean attackedWithRangeTwice = false;
	public boolean dealtDamage = false;
	int attacksInARowWithRange = 0;


	private enum newSpot {
		SOUTH_WEST(1834, 9894, 0),
		SOUTH_EAST(1846, 9894, 0),
		NORTH_WEST(1846, 9906, 0),
		NORTH_EAST(1834, 9906, 0);

		public final int x, y, z;

		newSpot(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	private static int attacks;
	boolean spawned = false;
	boolean spawnedSecond = false;

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDefend(hit -> {
//            if (npc.getHp() <= 266)
//                spawnFirstMinions();
//            if (npc.getHp() <= 133)
//                spawnSecondMinions();
		});
		npc.deathEndListener = (entity, killer, killHit) -> {
			for (NPC n : entity.localNpcs()) {
				//if (n.getDef().id == 8714 || n.getDef().id == 8715) {
				// n.hit(new Hit(entity).fixedDamage(n.getHp()).delay(0));
			}
			//   }
		};

	}

//    public void spawnFirstMinions() {
//        final Entity e = target;
//        List<Position> positions = e.getPosition().area(3, pos -> !pos.equals(e.getPosition()) && (pos.getTile() == null || pos.getTile().clipping == 0));
//        if (!spawned) {
//            NPC mage = new NPC(8715).spawn(Random.get(positions));
//            NPC melee = new NPC(8714).spawn(Random.get(positions));
//            Player p = Random.get(npc.localPlayers());
//            mage.face(p);
//            melee.face(p);
//            target = p;
//            spawned = true;
//        }
//    }

//
//    private void spawnSecondMinions() {
//        final Entity e = target;
//        List<Position> positions = e.getPosition().area(3, pos -> !pos.equals(e.getPosition()) && (pos.getTile() == null || pos.getTile().clipping == 0));
//        if (!spawnedSecond) {
//            NPC mage = new NPC(8715).spawn(Random.get(positions));
//            NPC melee = new NPC(8714).spawn(Random.get(positions));
//            Player p = Random.get(npc.localPlayers());
//            mage.face(p);
//            melee.face(p);
//            spawnedSecond = true;
//        }
//    }


	private void randomMove() {
		Position currentPos = npc.getPosition().copy();
		Position newPos;
		int roll = Random.get(1, 4);

		newPos = switch (roll) {
			case 1 -> new Position(newSpot.NORTH_EAST.x, newSpot.NORTH_EAST.y, newSpot.NORTH_EAST.z);
			case 2 -> new Position(newSpot.NORTH_WEST.x, newSpot.NORTH_WEST.y, newSpot.NORTH_WEST.z);
			case 3 -> new Position(newSpot.SOUTH_WEST.x, newSpot.SOUTH_WEST.y, newSpot.SOUTH_WEST.z);
			case 4 -> new Position(newSpot.SOUTH_EAST.x, newSpot.SOUTH_EAST.y, newSpot.SOUTH_EAST.z);
			default -> throw new IllegalStateException("Unexpected value: " + roll);
		};
//        target.player.sendMessage("CURRENT POS:" + currentPos);
//        target.player.sendMessage("NEW POS:" + newPos);

		if (currentPos != newPos) {
			npc.stepAbs(newPos.getX(), newPos.getY(), StepType.WALK);
			return;
		}
		randomMove();
	}


	@Override
	public void follow() {
		follow(16);
	}

	@Override
	public boolean attack() {
		if (!getNpc().getPosition().getRegion().players.contains(target.player)) {
			target = null;
			npc.faceNone(false);
			return false;
		}
		if (attacks == 4) {
			move();
			return false;
		}
		if (withinDistance(1)) {
			meleeAttack();
		} else {
			rangedAttack();
		}
		attacks++;
		return true;
	}

	@Override
	public void process() {

	}

	public void move() {
		npc.addEvent(event -> {
			event.setCancelCondition(this::isDead);
			npc.forceText("Hsss");
			npc.localPlayers().forEach(plr -> {
				plr.freeze(6, npc);
				RANGED_PROJECTILE.send(npc, plr);
				Position pos = plr.getPosition().copy();
				GameObject web = GameObject.spawn(34895, pos, 10, 0);
				plr.addEvent(e -> {
					e.delay(8);
					web.remove();
				});
			});
			npc.faceNone(false);
			npc.lock();
			randomMove();
			event.waitForMovement(npc);
			npc.localPlayers().forEach(plr -> plr.getCombat().reset());
			npc.faceNone(false);
			//event.delay(2);
			while (npc.localPlayers().size() < 1) event.delay(1);
			Player p = Random.get(npc.localPlayers());
			npc.face(p);
			target = p;
			npc.faceNone(false);
			attacks = 0;
			npc.unlock();
		});
	}


	public void meleeAttack() {
		attacksInARowWithRange = 0;
		npc.face(target);
		npc.animate(8147);
		Hit hit = new Hit(npc, AttackStyle.CRUSH)
			.randDamage(info.max_damage);
		hit.postDamage(t -> {
			if (hit.damage > 0) {
				dealtDamage = true;
				npc.hit(new Hit(HitType.HEAL).fixedDamage(10));
			}
		});
		target.hit(hit);
	}

	public void rangedAttack() {
		attacksInARowWithRange++;
		if (attacksInARowWithRange == 2)
			attackedWithRangeTwice = true;
		npc.face(target);
		npc.animate(RANGED_ANIM);
		npc.localPlayers().forEach(plr -> {
			int delay = RANGED_PROJECTILE.send(npc, target);
			Hit hit = new Hit(npc, AttackStyle.RANGED)
				.randDamage(info.max_damage)
				.clientDelay(delay);
			hit.postDamage(t -> {
				if (hit.damage > 0) {
					dealtDamage = true;
					npc.hit(new Hit(HitType.HEAL).fixedDamage(10));
				}
			});
			target.hit(hit);
		});

	}
}
