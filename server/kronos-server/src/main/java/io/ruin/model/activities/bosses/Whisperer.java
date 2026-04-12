package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;

import java.util.ArrayList;
import java.util.List;

public class Whisperer extends NPCCombat {

	private final Projectile rangedProjectile = new Projectile(2444, 80, 33, 0, 100, 8, 255, 0).regionBased();
	private final Projectile magicProjectile = new Projectile(2445, 80, 33, 0, 100, 8, 255, 0).regionBased();

	@Override
	public void init() {}

	@Override
	public void follow() {
		follow(10);
	}

	@Override
	public int getAggressionRange() {
		return 40;
	}

	@Override
	public int getAttackBoundsRange() {
		return 40;
	}

	boolean canAttack = true;
	int attackDelay = 0;
	int attacks = 0;

	@Override
	public boolean attack() {
		int attackStyle = Random.get(1);
		if (attackStyle == 0) {
			sendRangeAttack((Player) target);
		} else {
			sendMagicAttack((Player) target);
		}
		if (getHpPercentage() > 25) {
			if (attacks++ == 3) {
				if (Random.get(1) == 0) {
					fourDiagonalTentacleAttack((Player) target);
				} else {
					fourPlusTentancleAttack((Player) target);
				}
			}
		} else {
			World.startEvent(e -> {
				e.delay(1);
				spawnSoloTentacle((Player) target);
			});
		}
		return true;
	}

	public void resetVariables() {
		canAttack = true;
		attackDelay = 0;
		attacks = 0;
		mageStyle = true;
		damagedPlayer = false;
	}

	public boolean damagedPlayer = false;

	boolean mageStyle = true;

	private void enrageAttackEvent(Player target) {
		if (!canAttack)
			return;

		// Set canAttack to false before initiating the enrage attack event
		canAttack = false;
		npc.animate(10235);

		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getPosition().getRegion().id != target.getPosition().getRegion().id);

			// Perform attack based on current style
			if (mageStyle) {
				sendMagicAttack(target);
			} else {
				sendRangeAttack(target);
			}

			// Delay between actions
			e.delay(1);

			// Spawn a solo tentacle attack after the main attack


			// Switch between magic and range after every two attacks
			if (++attacks == 2) {
				attacks = 0;
				mageStyle = !mageStyle;
			}

			// Allow the NPC to attack again after this event
			canAttack = true;
		});
	}

	private void spawnSoloTentacle(Player target) {
		if (target == null)
			return;
		Position tentaclePosition = getTentaclePosition(target);
		if (tentaclePosition == null) {
			return;
		}

		Position targetPosition = target.getPosition().copy();
		NPC tentacle = new NPC(12208).spawn(
			tentaclePosition.getX(),
			tentaclePosition.getY(),
			tentaclePosition.getZ(),
			getTentacleFaceDirection(tentaclePosition, target),
			0
		);

		World.startEvent(event -> {
			tentacle.animate(10263);
			event.delay(2);
			tentacle.animate(10266);
			event.delay(1);
			tentacle.remove();

			Position startPosition = tentaclePosition.copy();
			int loops = 0;

			while (startPosition.distance(targetPosition) > 0) {
				int xStep = Integer.compare(targetPosition.getX(), startPosition.getX());
				int yStep = Integer.compare(targetPosition.getY(), startPosition.getY());

				startPosition.translate(xStep, yStep);

				World.sendGraphics(2449, 0, loops * 3, startPosition);
				loops++;

				if (target.getPosition().distance(startPosition) < 1 && npc.getHp() > 0) {
					target.hit(new Hit().randDamage(22, 40));
					damagedPlayer = true;
				}
				event.delay(1);
			}
		});
	}


	private Position getTentaclePosition(Player target) {
		if (target == null)
			return npc.getPosition();
		Position targetPosition = target.getPosition().copy();
		List<Position> positions = new ArrayList<>();
		Position tentacleOne = new Position(targetPosition.getX() - 4, targetPosition.getY() - 4, targetPosition.getZ());
		Position tentacleTwo = new Position(targetPosition.getX() + 4, targetPosition.getY() + 4, targetPosition.getZ());
		Position tentacleThree = new Position(targetPosition.getX() - 4, targetPosition.getY() + 4, targetPosition.getZ());
		Position tentacleFour = new Position(targetPosition.getX() + 4, targetPosition.getY() - 4, targetPosition.getZ());
		Position tentacleFive = new Position(targetPosition.getX() - 4, targetPosition.getY(), targetPosition.getZ());
		Position tentacleSix = new Position(targetPosition.getX() + 4, targetPosition.getY(), targetPosition.getZ());
		Position tentacleSeven = new Position(targetPosition.getX(), targetPosition.getY() + 4, targetPosition.getZ());
		Position tentacleEight = new Position(targetPosition.getX(), targetPosition.getY() - 4, targetPosition.getZ());

		if (tentacleOne.distance(npc.getPosition()) >= 2)
			positions.add(tentacleOne);
		if (tentacleTwo.distance(npc.getPosition()) >= 2)
			positions.add(tentacleTwo);
		if (tentacleThree.distance(npc.getPosition()) >= 2)
			positions.add(tentacleThree);
		if (tentacleFour.distance(npc.getPosition()) >= 2)
			positions.add(tentacleFour);
		if (tentacleFive.distance(npc.getPosition()) >= 2)
			positions.add(tentacleFive);
		if (tentacleSix.distance(npc.getPosition()) >= 2)
			positions.add(tentacleSix);
		if (tentacleSeven.distance(npc.getPosition()) >= 2)
			positions.add(tentacleSeven);
		if (tentacleEight.distance(npc.getPosition()) >= 2)
			positions.add(tentacleEight);
		if (positions.isEmpty())
			return null;
		return Random.get(positions);
	}

	private Direction getTentacleFaceDirection(Position pos, Player target) {
		Position targetPos = target.getPosition();
		int dx = pos.getX() - targetPos.getX();
		int dy = pos.getY() - targetPos.getY();

		if (dx == 0) {
			if (dy < 0) return Direction.NORTH;
			if (dy > 0) return Direction.SOUTH;
		} else if (dy == 0) {
			if (dx < 0) return Direction.EAST;
			if (dx > 0) return Direction.WEST;
		} else if (dx < 0 && dy < 0) {
			return Direction.NORTH_EAST;
		} else if (dx > 0 && dy < 0) {
			return Direction.NORTH_WEST;
		} else if (dx < 0 && dy > 0) {
			return Direction.SOUTH_EAST;
		} else if (dx > 0 && dy > 0) {
			return Direction.SOUTH_WEST;
		}
		return Direction.NORTH;
	}

	private void attackEvent(Player target, int delay) {
		if (!canAttack)
			return;

		// Immediately set canAttack to false to prevent multiple attack events

		World.startEvent(event -> {
			event.setCancelCondition(() -> npc.getHp() < 1 || target.getHp() < 1 || target.getPosition().regionId() != npc.getPosition().regionId());

			// Delay before starting the attack sequence
			event.delay(delay);

			for (int i = 0; i < 3; i++) {
				// Check again in case canAttack has been canceled mid-event
				if (!canAttack) return;

				// Randomly decide between ranged and magic attack


				if (i == 2) { // After third attack, reset canAttack and adjust delay
					canAttack = true;

					// Adjust attackDelay based on NPC's HP percentage
					if (getHpPercentage() < 20) {
						attackDelay = 1;
					} else if (getHpPercentage() < 40) {
						attackDelay = 2;
					} else if (getHpPercentage() < 60) {
						attackDelay = 3;
					} else if (getHpPercentage() < 80) {
						attackDelay = 4;
					} else {
						attackDelay = 5;
					}

					// Random tentacle attack
					if (Random.get(1) == 0) {
						fourDiagonalTentacleAttack(target);
					} else {
						fourPlusTentancleAttack(target);
					}
				}

				// Delay between attacks within the same event
				event.delay(4);
			}
		});
	}

	@Override
	public void process() {

	}

	private int getHpPercentage() {
		return (npc.getHp() * 100) / npc.getMaxHp();
	}

	private void sendRangeAttack(Player target) {
		npc.animate(10235);
		World.startEvent(e -> {
			int delay = rangedProjectile.send(npc, target);
			int maxHit = 66;
			int minHit = 30;
			e.delay(World.getTicks(delay) + 1);
			if (target.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
				maxHit = 9;
				minHit = 2;
			} else damagedPlayer = true;
			if (npc.getHp() > 0) {
				target.hit(new Hit().randDamage(minHit, maxHit));
			}
		});
	}

	private void sendMagicAttack(Player target) {
		npc.animate(10235);
		World.startEvent(e -> {
			int delay = magicProjectile.send(npc, target);
			int maxHit = 66;
			int minHit = 30;
			e.delay(World.getTicks(delay) + 1);
			if (target.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
				maxHit = 9;
				minHit = 2;
			} else damagedPlayer = true;
			if (npc.getHp() > 0) {
				target.hit(new Hit().randDamage(minHit, maxHit));
			}
		});
	}

	private void fourDiagonalTentacleAttack(Player target) {
		Position targetPosition = target.getPosition().copy();
		Position tentacleOne = new Position(targetPosition.getX() - 5, targetPosition.getY() - 5, targetPosition.getZ());
		Position tentacleTwo = new Position(targetPosition.getX() + 5, targetPosition.getY() + 5, targetPosition.getZ());
		Position tentacleThree = new Position(targetPosition.getX() - 5, targetPosition.getY() + 5, targetPosition.getZ());
		Position tentacleFour = new Position(targetPosition.getX() + 5, targetPosition.getY() - 5, targetPosition.getZ());
		NPC tentacleOneNPC = new NPC(12208).spawn(tentacleOne.getX(), tentacleOne.getY(), tentacleOne.getZ(), Direction.NORTH_EAST, 0);
		NPC tentacleTwoNPC = new NPC(12208).spawn(tentacleTwo.getX(), tentacleTwo.getY(), tentacleTwo.getZ(), Direction.NORTH_WEST, 0);
		NPC tentacleThreeNPC = new NPC(12208).spawn(tentacleThree.getX(), tentacleThree.getY(), tentacleThree.getZ(), Direction.SOUTH_EAST, 0);
		NPC tentacleFourNPC = new NPC(12208).spawn(tentacleFour.getX(), tentacleFour.getY(), tentacleFour.getZ(), Direction.SOUTH_WEST, 0);
		List<NPC> tentacles = new ArrayList<>();
		tentacles.add(tentacleOneNPC);
		tentacles.add(tentacleTwoNPC);
		tentacles.add(tentacleThreeNPC);
		tentacles.add(tentacleFourNPC);
		tentacles.add(tentacleFourNPC);
		tentacles.forEach(tentacle -> {
			World.startEvent(e -> {
				tentacle.animate(10263);
				e.delay(2);
				tentacle.animate(10266);
				e.delay(1);
			});
		});
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getHp() < 1 || target.getHp() < 1 || target.getPosition().regionId() != npc.getPosition().regionId());
			e.delay(2);
			tentacles.forEach(NPC::remove);
			List<Position> impactPositions = new ArrayList<>();
			World.sendGraphics(2450, 0, 9, targetPosition);
			impactPositions.add(targetPosition);
			for (int i = 0; i < 5; i++) {
				Position positionOne = new Position(tentacleOne.getX() + i, tentacleOne.getY() + i, tentacleOne.getZ());
				Position positionTwo = new Position(tentacleTwo.getX() - i, tentacleTwo.getY() - i, tentacleTwo.getZ());
				Position positionThree = new Position(tentacleThree.getX() + i, tentacleThree.getY() - i, tentacleThree.getZ());
				Position positionFour = new Position(tentacleFour.getX() - i, tentacleFour.getY() + i, tentacleFour.getZ());
				World.sendGraphics(2449, 0, i * 3, positionOne);
				World.sendGraphics(2449, 0, i * 3, positionTwo);
				World.sendGraphics(2449, 0, i * 3, positionThree);
				World.sendGraphics(2449, 0, i * 3, positionFour);
				if (!impactPositions.contains(positionOne))
					impactPositions.add(positionOne);
				if (!impactPositions.contains(positionTwo))
					impactPositions.add(positionTwo);
				if (!impactPositions.contains(positionThree))
					impactPositions.add(positionThree);
				if (!impactPositions.contains(positionFour))
					impactPositions.add(positionFour);


			}
			for (Position position : impactPositions) {
				if (target.getPosition().distance(position) < 1 && npc.getHp() > 0) {
					target.hit(new Hit().randDamage(22, 40));
					damagedPlayer = true;
				}
			}
		});
	}

	private void fourPlusTentancleAttack(Player target) {
		Position targetPosition = target.getPosition().copy();
		Position tentacleOne = new Position(targetPosition.getX() - 6, targetPosition.getY(), targetPosition.getZ());
		Position tentacleTwo = new Position(targetPosition.getX() + 6, targetPosition.getY(), targetPosition.getZ());
		Position tentacleThree = new Position(targetPosition.getX(), targetPosition.getY() + 6, targetPosition.getZ());
		Position tentacleFour = new Position(targetPosition.getX(), targetPosition.getY() - 6, targetPosition.getZ());
		NPC tentacleOneNPC = new NPC(12208).spawn(tentacleOne.getX(), tentacleOne.getY(), tentacleOne.getZ(), Direction.NORTH, 0);
		NPC tentacleTwoNPC = new NPC(12208).spawn(tentacleTwo.getX(), tentacleTwo.getY(), tentacleTwo.getZ(), Direction.SOUTH, 0);
		NPC tentacleThreeNPC = new NPC(12208).spawn(tentacleThree.getX(), tentacleThree.getY(), tentacleThree.getZ(), Direction.EAST, 0);
		NPC tentacleFourNPC = new NPC(12208).spawn(tentacleFour.getX(), tentacleFour.getY(), tentacleFour.getZ(), Direction.WEST, 0);
		List<NPC> tentacles = new ArrayList<>();
		tentacles.add(tentacleOneNPC);
		tentacles.add(tentacleTwoNPC);
		tentacles.add(tentacleThreeNPC);
		tentacles.add(tentacleFourNPC);
		tentacles.forEach(tentacle -> {
			World.startEvent(e -> {
				tentacle.animate(10263);
				e.delay(2);
				tentacle.animate(10266);
				e.delay(1);
			});
		});
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getHp() < 1 || target.getHp() < 1 || target.getPosition().regionId() != npc.getPosition().regionId());
			e.delay(2);
			tentacles.forEach(NPC::remove);
			List<Position> impactPositions = new ArrayList<>();
			World.sendGraphics(2450, 0, 9, targetPosition);
			impactPositions.add(targetPosition);
			for (int i = 0; i < 6; i++) {
				Position positionOne = new Position(tentacleOne.getX() + i, tentacleOne.getY(), tentacleOne.getZ());
				Position positionTwo = new Position(tentacleTwo.getX() - i, tentacleTwo.getY(), tentacleTwo.getZ());
				Position positionThree = new Position(tentacleThree.getX(), tentacleThree.getY() - i, tentacleThree.getZ());
				Position positionFour = new Position(tentacleFour.getX(), tentacleFour.getY() + i, tentacleFour.getZ());
				World.sendGraphics(2449, 0, i * 3, positionOne);
				World.sendGraphics(2449, 0, i * 3, positionTwo);
				World.sendGraphics(2449, 0, i * 3, positionThree);
				World.sendGraphics(2449, 0, i * 3, positionFour);
				if (!impactPositions.contains(positionOne))
					impactPositions.add(positionOne);
				if (!impactPositions.contains(positionTwo))
					impactPositions.add(positionTwo);
				if (!impactPositions.contains(positionThree))
					impactPositions.add(positionThree);
				if (!impactPositions.contains(positionFour))
					impactPositions.add(positionFour);
			}
			for (Position position : impactPositions) {
				if (target.getPosition().distance(position) < 1 && npc.getHp() > 0) {
					target.hit(new Hit().randDamage(22, 40));
					damagedPlayer = true;
				}
			}
		});
	}
}