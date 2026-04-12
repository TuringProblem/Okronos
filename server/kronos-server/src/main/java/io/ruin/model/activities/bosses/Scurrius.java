package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.cache.ItemID;
import io.ruin.model.activities.ActivityTimer;
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
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;

import java.util.ArrayList;
import java.util.List;

public class Scurrius extends NPCCombat {
	int phase = 1;
	Bounds bossBounds;
	List<Position> foodPosition = new ArrayList<>();

	Projectile MAGIC_PROJECTILE = new Projectile(2640, 60, 0, 0, 100, 30, 16, 64);
	Projectile RANGED_PROJECTILE = new Projectile(2642, 44, 0, 0, 100, 30, 16, 64);






	@Override
	public void init() {
		npc.hitListener = new HitListener().postDamage(this::postDamage);
		bossBounds = new Bounds(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 4,
			npc.getPosition().getRegion().baseX + 42, npc.getPosition().getRegion().baseY + 18, 0);
		foodPosition.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 3, 0));
		foodPosition.add(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 3, 0));
		foodPosition.add(new Position(npc.getPosition().getRegion().baseX + 43, npc.getPosition().getRegion().baseY + 11, 0));
		foodPosition.add(new Position(npc.getPosition().getRegion().baseX + 43, npc.getPosition().getRegion().baseY + 12, 0));
		foodPosition.add(new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 20, 0));
		foodPosition.add(new Position(npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().baseY + 20, 0));
	}

	private void postDamage(Hit hit) {
		int hpPercent = npc.getHp() * 100 / npc.getMaxHp();
		if (phase == 1 && hpPercent < 80) {
			phase = 2;
			walkToTrough();
		} else if (phase == 2 && hpPercent < 31) {
			phase = 3;
			canAttack = false;
			canWalkToTrough = false;
			npc.getCombat().setAllowRetaliate(false);
			World.startEvent(e -> {
				Position pos = new Position(bossBounds.getCenterX(), bossBounds.getCenterY(), npc.getPosition().getZ());
				npc.stepAbs(pos.getX(), pos.getY(), StepType.WALK);
				e.waitForEntityToBeAtPos(npc, new Position(bossBounds.getCenterX(), bossBounds.getCenterY(), npc.getPosition().getZ()));
				canAttack = true;
				npc.getCombat().setAllowRetaliate(true);
			});
		}
	}

	private Position getFoodPositionStepTile(Position pos) {
		if (pos.getX() == npc.getPosition().getRegion().baseX + 35 && pos.getY() == npc.getPosition().getRegion().baseY + 3) {
			return new Position(pos.getX(), pos.getY() + 1, pos.getZ());
		} else if (pos.getX() == npc.getPosition().getRegion().baseX + 34 && pos.getY() == npc.getPosition().getRegion().baseY + 3) {
			return new Position(pos.getX(), pos.getY() + 1, pos.getZ());
		} else if (pos.getY() == npc.getPosition().getRegion().baseY + 11 && pos.getX() == npc.getPosition().getRegion().baseX + 43) {
			return new Position(pos.getX() - 1, pos.getY(), pos.getZ());
		} else if (pos.getY() == npc.getPosition().getRegion().baseY + 12 && pos.getX() == npc.getPosition().getRegion().baseX + 43) {
			return new Position(pos.getX() - 1, pos.getY(), pos.getZ());
		} else if (pos.getX() == npc.getPosition().getRegion().baseX + 35 && pos.getY() == npc.getPosition().getRegion().baseY + 20) {
			return new Position(pos.getX(), pos.getY() - 1, pos.getZ());
		} else if (pos.getX() == npc.getPosition().getRegion().baseX + 34 && pos.getY() == npc.getPosition().getRegion().baseY + 20) {
			return new Position(pos.getX(), pos.getY() - 1, pos.getZ());
		}
		return pos;
	}

	private void startHealing(Position troughPos) {
		canAttack = false;
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getHp() < 1 || npc.getPosition().getRegion().players.isEmpty());
			for (int i = 0; i < 20; i++) {
				npc.face(troughPos.getX(), troughPos.getY());
				e.delay(1);
				Player player = npc.getPosition().getRegion().players.getFirst();
				if (i % 4 == 0) {
					if (Random.get(1) == 0) {
						magicAttack(player, true);
					} else {
						rangedAttack(player, true);
					}
				} else if (i % 3 == 0) {
					npc.face(troughPos.getX(), troughPos.getY());
					npc.animate(10689);
					npc.hit(new Hit(HitType.HEAL).fixedDamage(50 * npc.localPlayers().size()));
				}
				if (i == 19) {
					Position pos = new Position(bossBounds.getCenterX(), bossBounds.getCenterY(), npc.getPosition().getZ());
					npc.unlock();
					npc.stepAbs(pos.getX(), pos.getY(), StepType.WALK);
					e.waitForEntityToBeAtPos(npc, new Position(bossBounds.getCenterX(), bossBounds.getCenterY(), npc.getPosition().getZ()));
					canAttack = true;
					canWalkToTrough = true;
					npc.getCombat().setAllowRetaliate(true);
				}
			}
		});
	}

	@Override
	public void follow() {
		follow(1);
	}

	@Override
	public boolean attack() {
		if (!timerStarted) {
			timerStarted = true;
			target.player.scurriusTimer = new ActivityTimer();
		}
		if (!canAttack)
			return false;
		if (phase == 1) {
			if (withinDistance(1)) {
				if (Random.get(6) == 0) {
					spawnRats();
				}
				if (Random.get(4) == 0) {
					fallingBrick(target.player);
				}
				tailSwipe();
			}
		} else if (phase == 2) {
			int random = Random.get(2);
			if (random == 0) {
				tailSwipe();
			} else if (random == 1) {
				magicAttack(target.player, false);
			} else if (random == 3) {
				rangedAttack(target.player, false);
			}
			if (Random.get(6) == 0) {
				spawnRats();
			}
			if (Random.get(6) == 0) {
				fallingBrick(target.player);
			}
		}
		if (phase == 3) {
			if (Random.get(1) == 0)
				rangedAttack(target.player, false);
			else magicAttack(target.player, false);

			if (Random.get(4) == 0)
				fallingBrick(target.player);
		}
		return true;
	}

	private void spawnRats() {
		int ratsInArea = npc.localNpcs().stream().filter(npc -> npc.getId() == 7223).toArray().length;
		if(ratsInArea >= 7)
			return;
		npc.animate(10706);
		for (int i = 0; i < 6; i++) {
			NPC rat = new NPC(7223).spawn(bossBounds.randomPosition());
			rat.getCombat().setTarget(target);
			rat.getDef().occupyTiles = false;
			rat.hitListener = new HitListener().preDamage(hit -> {
				hit.damage = rat.getHp();
			});
		}
	}

	private void tailSwipe() {
		npc.face(target);
		npc.animate(10692);
		int maxDamage = 55;
		if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE))
			maxDamage = 9;
		Hit hit = new Hit(npc).randDamage(maxDamage);
		target.hit(hit);
	}

	private void magicAttack(Player player, boolean healing) {
		npc.animate(healing ? 10697 : 10696);
		int delay = MAGIC_PROJECTILE.send(npc, player);
		World.startEvent(e -> {
			e.delay(World.getTicks(delay) + 1);
			if (player == null || player.getPosition().distance(npc.getPosition()) > 15)
				return;
			int maxDamage = 55;
			if (player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
				maxDamage = 5;
			Hit hit = new Hit(npc).randDamage(maxDamage);
			player.hit(hit);
		});
	}

	boolean canAttack = true;
	boolean canWalkToTrough = true;

	private void walkToTrough() {
		if (!canWalkToTrough)
			return;
		canAttack = false;
		canWalkToTrough = false;
		Position trough = Random.get(foodPosition);
		Position stepTile = getFoodPositionStepTile(trough);
		World.startEvent(e -> {
			npc.getCombat().setAllowRetaliate(false);
			npc.getCombat().setTarget(null);
			npc.getCombat().reset();
			npc.stepAbs(stepTile.getX(), stepTile.getY(), StepType.WALK);
			e.waitForEntityToBeAtPos(npc, stepTile);
			npc.resetSteps();
			npc.lock();
			startHealing(trough);
		});
	}

	private void fallingBrick(Player player) {
		npc.animate(10698);
		for (int i = 0; i < 10; i++) {
			Position randomPosition = i == 0 ? player.getPosition().copy() : bossBounds.randomPosition();
			World.sendGraphics(2644, 0, 0, randomPosition.getX(), randomPosition.getY(), randomPosition.getZ());
			World.startEvent(e -> {
				e.delay(2);
				npc.localPlayers().forEach(p -> {
					if (p.getPosition().distance(randomPosition) < 1)
						p.hit(new Hit(npc).randDamage(18, 25));
				});
			});
		}
	}

	private void rangedAttack(Player target, boolean healing) {
		npc.animate(healing ? 10695 : 10694);
		int delay = RANGED_PROJECTILE.send(npc, target);
		World.startEvent(e -> {
			e.delay(World.getTicks(delay) + 1);
			if (target == null || target.getPosition().distance(npc.getPosition()) > 15)
				return;
			int maxDamage = 55;
			if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
				maxDamage = 7;
			Hit hit = new Hit(npc).randDamage(maxDamage);
			target.hit(hit);
		});
	}

	@Override
	public int getAggressionRange() {
		return 25;
	}

	@Override
	public int getAttackBoundsRange() {
		return 25;
	}
	@Override
	public void process() {
		if(!npc.getPosition().getRegion().players.isEmpty()) {
			if (target == null && canAttack) {
				npc.getCombat().setTarget(npc.getPosition().getRegion().players.getFirst());
			}
		}
		int hpPercent = npc.getHp() * 100 / npc.getMaxHp();
		if(phase != 1 && hpPercent > 80) {
			phase = 1;
		} else if(phase != 2 && hpPercent < 80 && hpPercent > 30) {
			phase = 2;
		} else if(phase != 3 && hpPercent < 31) {
			phase = 3;
		}
	}
}
