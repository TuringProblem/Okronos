package io.ruin.model.activities.raids.tob.dungeon.boss.bloat;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.TheSoulsplit;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BloatCombat extends NPCCombat {

	private static final int FLIES_GFX = 1568;

	List<Position> cornerPositions = new ArrayList<>(4);
	int waypointIndex = 1;
	int changeDirectionCount = 0;
	Position targetPos;

	Bounds roomBounds;


	public int downs = 0;
	public boolean damagedPlayer = false;

	@Override
	public void init() {
		cornerPositions.add(0, new Position(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 25, npc.getPosition().getZ()));//SW
		cornerPositions.add(1, new Position(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 35, npc.getPosition().getZ()));//NW
		cornerPositions.add(2, new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 35, npc.getPosition().getZ()));//NE
		cornerPositions.add(3, new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 25, npc.getPosition().getZ()));//SE
		roomBounds = new Bounds(cornerPositions.get(0).getX(), cornerPositions.get(0).getY(), cornerPositions.get(2).getX(), cornerPositions.get(2).getY(), npc.getPosition().getZ());
		flesh(0, 2500);
		bloatWalk();
		npc.hitListener = new HitListener().postDamage(this::postDamage)
			.preDefend(hit -> {
				if (sleepytime) {
					hit.boostDefence(0.75);
				} else if (hit.attacker != null && hit.attacker.isPlayer()) {
					hit.attacker.player.graphics(FLIES_GFX);
					for (int i = 0; i < 4; i++) {
						damagedPlayer = true;
						hit.attacker.player.hit(new Hit().randDamage(7, 8));
					}
				}
			});
		npc.deathStartListener = (DeathListener.Simple) () -> {
			npc.animate(8085);
		};
	}


	private int getPlayersInBounds() {
		AtomicInteger count = new AtomicInteger();
		npc.getPosition().getRegion().players.forEach(p -> {
			if (p.getPosition().inBounds(roomBounds))
				count.getAndIncrement();
		});
		return count.get();
	}


	private void postDamage(Hit hit) {
		if (hit.attacker != null && hit.attacker.isPlayer()) {
			if (hit.attacker.isPlayer())
				hit.attacker.player.tobDamageDealt += hit.damage;
		}
	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		return false;
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
		if (!sleepytime) {
			count--;
			if (count <= 0) {
				if (getPlayersInBounds() == 0) {
					count = 18;
					return;
				}
				sleepytime = true;
				downs++;
				npc.getMovement().reset();
				npc.animate(8082);
				npc.lock();
			} else
				bloatWalk();
		} else if (sleepytime && count < 30) {
			count++;
		} else {
			count = 18;
			npc.unlock();
			stomp();
			sleepytime = false;
			flesh(0, 3000);
			bloatWalk();
		}

		if (roomBounds != null) {
			if (!npc.getPosition().inBounds(roomBounds)) {
				// npc.getMovement().teleport(cornerPositions.get(0));
			}
		}
		if (!sleepytime) {
			bloatReachDestination();
			npc.animate(-1);
		}
		for (Player player : npc.getPosition().getRegion().players) {
			if (player.getPosition().isWithinDistance(npc.getPosition(), 1) && !sleepytime) {
				player.startEvent(event -> {
					if (player.getHp() > 0 && npc.getHp() > 0) {
						damagedPlayer = true;
						player.hit(new Hit(HitType.DAMAGE).randDamage(40, 60));
						player.forceText("OUCH");
					}
					event.delay(2);
				});
			}
		}

	}

	int direction = 0;

	private void bloatReachDestination() {
		if (npc.getPosition().isWithinDistance(targetPos, 3)) {
			changeDirectionCount++;
			if (Random.get(0, 3) == 0 && changeDirectionCount >= 6) {//Change direction
				if (direction == 0) direction = 1;
				else direction = 0;
				changeDirectionCount = 0;
			}

			if (direction == 0) {
				if (waypointIndex == 3)
					waypointIndex = 0;
				else waypointIndex++;
			} else if (direction == 1) {
				if (waypointIndex == 0)
					waypointIndex = 3;
				else waypointIndex--;
			}
			bloatWalk();
		}
	}

	public void bloatWalk() {
		if (sleepytime) {
			return;
		}
		StepType stepType;
		targetPos = cornerPositions.get(waypointIndex);
		if (npc.getHp() < (npc.getMaxHp() * 0.6))
			stepType = Random.get(0, 3) == 0 ? StepType.RUN : StepType.WALK;
		else
			stepType = StepType.WALK;
		npc.getMovement().stepType = stepType;
		npc.getRouteFinder().routeAbsolute(targetPos.getX(), targetPos.getY());

	}

	public boolean sleepytime = false;


	public void stomp() {
		npc.startEvent(e -> {
			for (Player p : npc.getPosition().getRegion().players) {
				if (p.getPosition().isWithinDistance(npc.getPosition(), 2) && !sleepytime) {
					p.hit(new Hit(npc).randDamage(40, 60));
					damagedPlayer = true;
				}
			}
		});
	}


	public Position getAbsolute(int localX, int localY) {
		return new Position(npc.getPosition().getRegion().baseX + localX, npc.getPosition().getRegion().baseY + localY, npc.getPosition().getZ());
	}

	public int count = 18;


	public void flesh(int delay, int duration) {
		World.startEvent(event -> {
			event.setCancelCondition(this::isDead);
			event.delay(delay);
			int ticks = 0;
			while (ticks < duration && !sleepytime) {
				if (npc.localPlayers().isEmpty()) {
					return;
				}
				for (int i = 0; i < 8; i++) {
					Position position;
					position = getAbsolute(Random.get(23, 40), Random.get(23, 40));


					if (position.getTile().isTileFreeCheckDecor() && position.inBounds(npc.getPosition()
						.getRegion()
						.getBounds())) {

						if (Random.get() <= 0.5)
							World.sendGraphics(1572, 0, 5, position);
						else
							World.sendGraphics(1573, 0, 5, position);


						event.delay(3);

						npc.localPlayers().forEach(p -> {
							if (p.getPosition().distance(position) < 1) {
								damagedPlayer = true;
								p.hit(new Hit(npc).randDamage(30, 60));
							}
						});
						// Flies
						damagePlayersInLos();
					}
				}
				ticks += 2;
				event.delay(2);
			}
		});
	}


	private void damagePlayersInLos() {
		if (sleepytime) return;
		Bounds psouth = new Bounds(npc.getPosition().getRegion().baseX + 23, npc.getPosition().getRegion().baseY + 23, npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().baseY + 28, 0);
		Bounds pnorth = new Bounds(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 23, npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 40, 0);
		// ^^ DONE South East Corner -> Span to North East and South West Corner

		Bounds psouth1 = new Bounds(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 24, npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 39, 0);
		Bounds pnorth1 = new Bounds(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 35, npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().baseY + 39, 0);
		// ^^ DONE North West Corner -> Span to North East Corner and South West corner

		Bounds psouth2 = new Bounds(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 24, npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 39, 0);
		Bounds pnorth2 = new Bounds(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 24, npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().baseY + 28, 0);
		// ^^ DONE South West Corner -> Span to North West Corner and South East Corner


		Bounds psouth3 = new Bounds(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 24, npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().baseY + 39, 0);
		Bounds pnorth3 = new Bounds(npc.getPosition().getRegion().baseX + 24, npc.getPosition().getRegion().baseY + 35, npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().baseY + 39, 0);
		// ^^ DONE North East Corner -> Span to South East Corner and North West Corner
		npc.localPlayers().forEach(plr -> {
			if (npc.getPosition().inBounds(psouth) && plr.getPosition().inBounds(psouth) || npc.getPosition().inBounds(pnorth) && plr.getPosition().inBounds(pnorth)) {
				plr.graphics(FLIES_GFX);
				plr.hit(new Hit().randDamage(1, 3));
				damagedPlayer = true;
			} else if (npc.getPosition().inBounds(psouth1) && plr.getPosition().inBounds(psouth1) || npc.getPosition().inBounds(pnorth1) && plr.getPosition().inBounds(pnorth1)) {
				plr.graphics(FLIES_GFX);
				plr.hit(new Hit().randDamage(9, 11));
				damagedPlayer = true;
			} else if (npc.getPosition().inBounds(psouth2) && plr.getPosition().inBounds(psouth2) || npc.getPosition().inBounds(pnorth2) && plr.getPosition().inBounds(pnorth2)) {
				plr.graphics(FLIES_GFX);
				plr.hit(new Hit().randDamage(1, 4));
				damagedPlayer = true;
			} else if (npc.getPosition().inBounds(psouth3) && plr.getPosition().inBounds(psouth3) || npc.getPosition().inBounds(pnorth3) && plr.getPosition().inBounds(pnorth3)) {
				plr.graphics(FLIES_GFX);
				if (plr.getHp() > 0) {
					plr.hit(new Hit().randDamage(1, 4));
					damagedPlayer = true;
				}
			}
		});

	}

}
