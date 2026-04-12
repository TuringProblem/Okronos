package io.ruin.model.activities.raids.tob.dungeon.boss.xarpus;


import io.ruin.Server;
import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.TheSoulsplit;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.AttackNpcListener;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.object.GameObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class XarpusCombat extends NPCCombat {

	private static final Projectile spitProjectile = new Projectile(1644, 35, 30, 30, 46, 8, 15, 255).tileOffset(1).regionBased();
	private static final Projectile healProjectile = new Projectile(1550, 0, 30, 30, 46, 8, 15, 255).tileOffset(1).regionBased();
	private Bounds roomBounds = null;

	List<Position> pools = new ArrayList<>();
	State currentState;
	List<Direction> directions = new ArrayList<>();
	Direction currentDirection;

	int poolsLaid = 0;
	boolean poolsStarted = false;

	public boolean usedRangeOrMage = false;
	public boolean damagedPlayer = false;

	boolean canAttack = true;
	boolean canBeAttacked = true;
	int maxHealth = 0;
	boolean screechStarted = false;
	boolean started = false;

	@Override
	public void init() {
		System.out.println("xarpus " + npc.getPosition());
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDefend(this::preHitDefend);
		npc.isMovementBlocked(true, false);
		// addToDirectionList();
		roomBounds = new Bounds(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 28,
			npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 42, npc.getPosition().getZ());
		// currentState = State.POISON;
		//  HandleNewState(currentState);

	}


	@Override
	public void follow() {
		//leave empty so npc does not walk
	}

	@Override
	public boolean attack() {
		sendPoisonSpit();
		return true;
	}

	private void sendPoisonSpit() {
		npc.face(target);
		npc.localPlayers().forEach(p -> poisonSpit(p.getPosition().copy().getX(), p.getPosition().copy().getY()));
	}

	public boolean wearingBarrows = true;

	@Override
	public void process() {
		if (!npc.getPosition().getRegion().players.isEmpty()) {
			for (Player p : npc.getPosition().getRegion().players) {
				if (!p.wearingBarrows())
					wearingBarrows = false;
			}
		}
		if (currentState == State.COUNTER) {
			if (!screechStarted)
				handleScreech();
		}

		for (Player player : npc.getPosition().getRegion().players) {
			// ignore spectators
			if (!roomBounds.inBounds(player)) continue;

			World.startEvent(e -> {
				for (int i = pools.size() - 1; i >= 0; i--) {
					e.delay(1);
					if (player.getHp() < 1)
						continue;
					if (i > pools.size() - 1)
						break;
					if (player.getPosition().isAtPosition(pools.get(i))) {
						if (npc.getHp() > 0) {
							damagedPlayer = true;
							player.hit(new Hit(HitType.POISON).randDamage(4, 8).ignoreDefence().ignorePrayer());
						}
					}
				}
			});
		}

	}

	private void preHitDefend(Hit hit) {
		if (hit.damage > 50)
			hit.damage = 50;
	}


	private void postDamage(Hit hit) {
		if (hit.attacker != null && hit.attacker.isPlayer()) {
			if (hit.attackStyle != null && !hit.attackStyle.isMelee())
				usedRangeOrMage = true;
			if (hit.attacker.isPlayer())
				hit.attacker.player.tobDamageDealt += hit.damage;
		}
		if (currentState == State.RECOVERY) return;
		//  if (npc.getHp() <= (npc.getMaxHp() / 2) && currentState != State.COUNTER) {
		// currentState = State.COUNTER;
		// HandleNewState(currentState);
		// }
		if (currentState == State.COUNTER && screechStarted) {
			for (Player player : npc.localPlayers()) {
				int x = player.getPosition().getX() - npc.getPosition().centrePosition(npc.getSize()).getX();
				int y = player.getPosition().getY() - npc.getPosition().centrePosition(npc.getSize()).getY();
				double mag = Math.sqrt((x * x) + (y * y));
				Position diffNorm = new Position(mag == 0 ? 0 : (int) (x / mag), mag == 0 ? 0 : (int) (y / mag), 0);
				double currentMag = Math.sqrt((currentDirection.deltaX * currentDirection.deltaX) + (currentDirection.deltaY * currentDirection.deltaY));
				Position currentDir = new Position(currentMag == 0 ? 0 : (int) (currentDirection.deltaX / currentMag), currentMag == 0 ? 0 : (int) (currentDirection.deltaY / currentMag), 0);
				double dot = dot(currentDir, diffNorm);
				if (dot > 0.8) {
					player.hit(new Hit(HitType.POISON).randDamage(50, 75).ignoreDefence().ignorePrayer());
					damagedPlayer = true;
				}
			}
		}
	}

	private void handleScreech() {
		npc.forceText("SCREEECH!");
		npc.getCombat().setAllowRetaliate(false);
		npc.getCombat().reset();
		canAttack = false;
		screechStarted = true;
		addToDirectionList();
		if (currentDirection != null && directions.contains(currentDirection))
			directions.remove(currentDirection);
		int randomIndex = Random.get(directions.size() - 1);
		currentDirection = directions.get(randomIndex);
		npc.face(currentDirection);

		World.startEvent(event -> {
			for (int i = 0; i < 15; i++) {
				event.delay(1);
				if (i == 14)
					handleScreech();
			}
		});
	}


	public double dot(Position pointA, Position pointB) {
		return pointA.getX() * pointB.getX() + pointA.getY() * pointB.getY();
	}

	private void addToDirectionList() {
		directions.clear();
		directions.add(Direction.EAST);
		directions.add(Direction.NORTH);
		directions.add(Direction.SOUTH);
		directions.add(Direction.WEST);
		directions.add(Direction.NORTH_EAST);
		directions.add(Direction.NORTH_WEST);
		directions.add(Direction.SOUTH_EAST);
		directions.add(Direction.SOUTH_WEST);
	}

	private void sendHealingPool() {
		poolsStarted = true;
		Position poolPosition = roomBounds.randomPosition();
		npc.startEvent(e -> {
			GameObject pool = GameObject.spawn(32743, poolPosition.getX(), poolPosition.getY(), poolPosition.getZ(), 22, 0);
			e.delay(1);
			pool.animate(8065);
			for (int i = 0; i < 5; i++) {
				e.delay(5);
				boolean canHeal = true;
				for (Player player : npc.localPlayers()) {
					if (player.getPosition().isWithinDistance(poolPosition, 0)) {
						canHeal = false;
					}
				}
				if (canHeal) {
					int delay = healProjectile.send(pool.getPosition().center(1).copy(), npc.getCentrePosition().copy());
					healXarpus();
				}
				if (i == 4) {
					pool.graphics(1549);
					e.delay(1);
					pool.remove();
					poolsLaid++;
					if (poolsLaid >= 5) {
						currentState = State.POISON;
						HandleNewState(currentState);
					} else {
						sendHealingPool();
					}
				}
			}
		});
	}

	private void healXarpus() {
		World.startEvent(3, e ->
			npc.hit(new Hit(HitType.HEAL).randDamage(9, 20)));
	}

	private void refreshPoisonPools() {
		World.startEvent(event -> {
			while (currentState == State.POISON) {
				for (Position pool : pools)
					World.sendGraphics(1654, 0, 0, pool);
				event.delay(10);
			}
		});
	}

	private void poisonSpit(int x, int y) {
		Position pos = new Position(x, y, npc.getHeight());
		npc.animate(8059);
		World.startEvent(e -> {
			Position npcPos = npc.getCentrePosition().copy();
			int delay = spitProjectile.send(npcPos, pos);
			int ticks = World.getTicks(delay);
			e.delay(ticks + 1);
			World.sendGraphics(1654, 0, 0, pos);
			// if there is a pool here, don't add another
			if (!pools.contains(pos)) {
				// Add the new pool pos
				pools.add(pos);
				// after the time it takes to remove itself graphically, we need to logically remove it
				World.startEvent(10, event -> pools.remove(pos));
			}
		});
	}


	private XarpusNPC asXarpus() {
		return (XarpusNPC) npc;
	}

	public void removePools() {
		for (Position pool : pools)
			World.sendGraphics(-1, 0, 0, pool);
		pools.clear();
	}

	public void HandleNewState(State state) {
		switch (state) {
			case COUNTER:
				npc.transform(8340);
				canBeAttacked = true;
				removePools();
				break;
			case POISON:
				refreshPoisonPools();
				npc.animate(8061);
				npc.getCombat().setAllowRetaliate(true);
				canBeAttacked = true;
				maxHealth = npc.getHp();
				break;
		}

	}

	enum State {
		RECOVERY,
		POISON,
		COUNTER
	}
}
