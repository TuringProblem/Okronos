package io.ruin.model.activities.bosses;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.*;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.TickDelay;

import java.util.ArrayList;
import java.util.List;

public class Vardorvis extends NPCCombat {
	private static final int headNPCId = 12226;
	private static final int axeNPCId = 12227;
	private static final int largeTendrilNPCId = 12225;

	Bounds headSpawnBounds;

	public boolean damagedPlayer = false;

	List<NPC> axes = new ArrayList<>();

	@Override
	public void init() {
		headSpawnBounds = new Bounds(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 23, npc.getPosition().getRegion().baseX + 44, npc.getPosition().getRegion().baseY + 29, 0);
		axeEvent.delay(14);
	}

	@Override
	public void follow() {
		if (npc.getId() == 12223)
			follow(1);
	}

	public boolean timerStarted = false;

	@Override
	public boolean attack() {
		if (!timerStarted) {
			timerStarted = true;
			target.player.vardorvisTimer = new ActivityTimer();
		}
		if (npc.getId() == 12228)
			return false;
		if (withinDistance(2)) {
			if (Random.get(6) == 0) {
				handleSpikeEvent(target);
			}
//            if(npc.getHp() < 1400 && Random.get(5) == 0 && headEvent.remaining() < 1) {
//                startHeadEvent();
//            }
			meleeAttack();
			return true;
		}
		return false;
	}

	protected Hit meleeAttack() {
		// System.out.println("melee attack");
		npc.animate(10341);
		int maxDamage = 55;
		if (target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE))
			maxDamage = 18;
		else damagedPlayer = true;
		Hit hit = new Hit(npc).randDamage(maxDamage);
		target.hit(hit);
		return hit;
	}

	@Override
	public void process() {
		if (npc.getId() == 12228)
			return;
		if (axeEvent.remaining() < 1 && target != null)
			handleAxeEvent();
		if (!axes.isEmpty()) {
			axes.forEach(axe -> {
				getPlayers().forEach(p -> {
					if (p.getPosition().distance(axe.getPosition()) < 1) {
						int maxDamage = 35;
						if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE))
							maxDamage = 17;
						damagedPlayer = true;
						p.hit(new Hit().randDamage(10, maxDamage));
						World.startEvent(e -> {
							e.setCancelCondition(() -> p == null || p.getHp() < 1 || p.getCombat().isDead());
							for (int i = 0; i < 6; i++) {
								e.delay(2);
								p.hit(new Hit().fixedDamage(6));
							}
						});

					}
				});
			});
		}
	}

	private List<Player> getPlayers() {
		return npc.getPosition().getRegion().players;
	}

	private void spawnSpike(Position position) {
		World.sendGraphics(2510, 0, 0, position);
		World.startEvent(event -> {
			event.delay(3);
			World.sendGraphics(2518, 0, 0, position);
			getPlayers().forEach(player -> {
				if (player.getPosition().distance(position) < 1) {
					damagedPlayer = true;
					player.hit(new Hit().randDamage(10, 22));
				}
			});
		});
	}

	TickDelay axeEvent = new TickDelay();
	TickDelay headEvent = new TickDelay();

	private void axeThrowEvent() {
		List<Direction> directionsToUse = getAxeDirections();
		for (Direction direction : directionsToUse) {
			sendAxeFromDirection(direction);
		}
	}

	private List<Direction> getAxeDirections() {
		int random = Random.get(0, 55);
		return switch (random) {
			case 0 -> List.of(Direction.WEST, Direction.EAST, Direction.SOUTH_WEST);
			case 1 -> List.of(Direction.NORTH_EAST, Direction.WEST, Direction.NORTH_WEST);
			case 2 -> List.of(Direction.WEST, Direction.NORTH, Direction.NORTH_EAST);
			case 3 -> List.of(Direction.NORTH, Direction.WEST, Direction.SOUTH_WEST);
			case 4 -> List.of(Direction.NORTH, Direction.SOUTH_WEST, Direction.SOUTH_EAST);
			case 5 -> List.of(Direction.SOUTH, Direction.WEST, Direction.NORTH_WEST);
			case 6 -> List.of(Direction.SOUTH, Direction.EAST, Direction.NORTH_EAST);
			case 7 -> List.of(Direction.SOUTH, Direction.NORTH_WEST, Direction.NORTH_EAST);
			case 8 -> List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH_EAST);
			case 9 -> List.of(Direction.NORTH, Direction.SOUTH, Direction.WEST);
			case 10 -> List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST);
			case 11 -> List.of(Direction.EAST, Direction.WEST, Direction.SOUTH_EAST);
			case 12 -> List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH);
			case 13 -> List.of(Direction.NORTH, Direction.WEST, Direction.SOUTH_EAST);
			case 14 -> List.of(Direction.NORTH_EAST, Direction.NORTH_WEST, Direction.SOUTH_WEST);
			case 15 -> List.of(Direction.NORTH, Direction.EAST, Direction.WEST);
			case 16 -> List.of(Direction.EAST, Direction.SOUTH, Direction.SOUTH_WEST);
			case 17 -> List.of(Direction.EAST, Direction.NORTH_EAST, Direction.SOUTH_WEST);
			case 18 -> List.of(Direction.WEST, Direction.SOUTH_WEST, Direction.SOUTH_EAST);
			case 19 -> List.of(Direction.NORTH, Direction.NORTH_EAST, Direction.NORTH_WEST);
			case 20 -> List.of(Direction.SOUTH, Direction.SOUTH_WEST, Direction.SOUTH_EAST);
			case 21 -> List.of(Direction.EAST, Direction.SOUTH, Direction.NORTH_WEST);
			case 22 -> List.of(Direction.EAST, Direction.WEST, Direction.SOUTH);
			case 23 -> List.of(Direction.NORTH_WEST, Direction.EAST, Direction.SOUTH_EAST);
			case 24 -> List.of(Direction.SOUTH, Direction.NORTH_WEST, Direction.SOUTH_EAST);
			case 25 -> List.of(Direction.WEST, Direction.NORTH_WEST, Direction.SOUTH_WEST);
			case 26 -> List.of(Direction.SOUTH, Direction.NORTH_WEST, Direction.SOUTH_WEST);
			case 27 -> List.of(Direction.NORTH, Direction.SOUTH, Direction.NORTH_WEST);
			case 28 -> List.of(Direction.NORTH, Direction.SOUTH, Direction.SOUTH_EAST);
			case 29 -> List.of(Direction.NORTH_EAST, Direction.WEST, Direction.SOUTH_WEST);
			case 30 -> List.of(Direction.NORTH_WEST, Direction.WEST, Direction.SOUTH_WEST);
			case 31 -> List.of(Direction.NORTH_EAST, Direction.NORTH_WEST, Direction.SOUTH);
			case 32 -> List.of(Direction.NORTH, Direction.SOUTH_EAST, Direction.SOUTH_WEST);
			case 33 -> List.of(Direction.NORTH_WEST, Direction.SOUTH_WEST, Direction.SOUTH_EAST);
			case 34 -> List.of(Direction.EAST, Direction.NORTH_EAST, Direction.SOUTH_EAST);
			case 35 -> List.of(Direction.NORTH_EAST, Direction.SOUTH_WEST, Direction.SOUTH_EAST);
			case 36 -> List.of(Direction.SOUTH, Direction.SOUTH_EAST, Direction.SOUTH_WEST);
			case 37 -> List.of(Direction.NORTH_WEST, Direction.SOUTH, Direction.SOUTH_WEST);
			case 38 -> List.of(Direction.NORTH_EAST, Direction.SOUTH, Direction.SOUTH_EAST);
			case 39 -> List.of(Direction.NORTH, Direction.SOUTH, Direction.NORTH_EAST);
			case 40 -> List.of(Direction.NORTH_WEST, Direction.SOUTH, Direction.SOUTH_EAST);
			case 41 -> List.of(Direction.NORTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST);
			case 42 -> List.of(Direction.NORTH, Direction.NORTH_EAST, Direction.SOUTH);
			case 43 -> List.of(Direction.NORTH, Direction.NORTH_WEST, Direction.SOUTH);
			case 44 -> List.of(Direction.NORTH_EAST, Direction.NORTH_WEST, Direction.SOUTH_EAST);
			case 45 -> List.of(Direction.EAST, Direction.SOUTH, Direction.SOUTH_EAST);
			case 46 -> List.of(Direction.WEST, Direction.SOUTH, Direction.SOUTH_WEST);
			case 47 -> List.of(Direction.NORTH, Direction.SOUTH_EAST, Direction.SOUTH);
			case 48 -> List.of(Direction.NORTH, Direction.SOUTH_WEST, Direction.SOUTH);
			case 49 -> List.of(Direction.NORTH_WEST, Direction.NORTH_EAST, Direction.WEST);
			case 50 -> List.of(Direction.NORTH_WEST, Direction.NORTH_EAST, Direction.EAST);
			case 51 -> List.of(Direction.SOUTH_WEST, Direction.SOUTH_EAST, Direction.EAST);
			case 52 -> List.of(Direction.SOUTH_WEST, Direction.SOUTH_EAST, Direction.WEST);
			case 53 -> List.of(Direction.NORTH, Direction.WEST, Direction.SOUTH);
			case 54 -> List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH);
			case 55 -> List.of(Direction.NORTH_WEST, Direction.NORTH_EAST, Direction.SOUTH_WEST);
			default -> throw new IllegalStateException("Unexpected value: " + random);
		};
	}

	private void handleAxeEvent() {
		axeEvent.delay(45);
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getHp() < 1);
			axeThrowEvent();
			e.delay(17);
			axeThrowEvent();
		});
	}

	private void sendAxeFromDirection(Direction dir) {
		switch (dir) {
			case NORTH:
				spawnNorthAxeThrowEvent();
				break;
			case SOUTH:
				spawnSouthAxeThrowEvent();
				break;
			case EAST:
				spawnEastAxeThrowEvent();
				break;
			case WEST:
				spawnWestAxeThrowEvent();
				break;
			case NORTH_EAST:
				spawnNorthEastAxeThrowEvent();
				break;
			case NORTH_WEST:
				spawnNorthWestAxeThrowEvent();
				break;
			case SOUTH_EAST:
				spawnSouthEastAxeThrowEvent();
				break;
			case SOUTH_WEST:
				spawnSouthWestAxeThrowEvent();
				break;
		}
	}

	private void spawnEastAxeThrowEvent() {
		NPC largeTendril = new NPC(largeTendrilNPCId).spawn(npc.getPosition().getRegion().baseX + 47, npc.getPosition().getRegion().baseY + 26, 0, Direction.WEST, 0);
		largeTendril.animate(10364);
		World.startEvent(event -> {
			event.delay(1);
			largeTendril.animate(10365);
			event.delay(2);
			NPC axe = new NPC(axeNPCId).spawn(npc.getPosition().getRegion().baseX + 46, npc.getPosition().getRegion().baseY + 26, 0, Direction.WEST, 0);
			axes.add(axe);
			axe.step(-9, 0, StepType.WALK);
			largeTendril.remove();
			event.delay(9);
			axes.remove(axe);
			axe.remove();
		});

	}

	private void spawnWestAxeThrowEvent() {
		NPC largeTendril = new NPC(largeTendrilNPCId).spawn(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 26, 0, Direction.EAST, 0);
		largeTendril.animate(10364);
		World.startEvent(event -> {
			event.delay(1);
			largeTendril.animate(10365);
			event.delay(2);
			NPC axe = new NPC(axeNPCId).spawn(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 26, 0, Direction.EAST, 0);
			axes.add(axe);
			axe.step(9, 0, StepType.WALK);
			largeTendril.remove();
			event.delay(9);
			axes.remove(axe);
			axe.remove();
		});
	}

	private void spawnNorthAxeThrowEvent() {
		NPC largeTendril = new NPC(largeTendrilNPCId).spawn(npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 32, 0, Direction.SOUTH, 0);
		largeTendril.animate(10364);
		World.startEvent(event -> {
			event.delay(1);
			largeTendril.animate(10365);
			event.delay(2);
			NPC axe = new NPC(axeNPCId).spawn(npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 31, 0, Direction.SOUTH, 0);
			axes.add(axe);
			axe.step(0, -9, StepType.WALK);
			largeTendril.remove();
			event.delay(9);
			axes.remove(axe);
			axe.remove();
		});
	}

	private void spawnSouthAxeThrowEvent() {
		NPC largeTendril = new NPC(largeTendrilNPCId).spawn(npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 20, 0, Direction.NORTH, 0);
		largeTendril.animate(10364);
		World.startEvent(event -> {
			event.delay(1);
			largeTendril.animate(10365);
			event.delay(2);
			NPC axe = new NPC(axeNPCId).spawn(npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 21, 0, Direction.NORTH, 0);
			axes.add(axe);
			axe.step(0, 9, StepType.WALK);
			largeTendril.remove();
			event.delay(9);
			axes.remove(axe);
			axe.remove();
		});
	}

	private void spawnSouthWestAxeThrowEvent() {
		NPC largeTendril = new NPC(largeTendrilNPCId).spawn(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 21, 0, Direction.NORTH_EAST, 0);
		largeTendril.animate(10364);
		World.startEvent(event -> {
			event.delay(1);
			largeTendril.animate(10365);
			event.delay(2);
			NPC axe = new NPC(axeNPCId).spawn(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 22, 0, Direction.NORTH_EAST, 0);
			axes.add(axe);
			axe.step(9, 9, StepType.WALK);
			largeTendril.remove();
			event.delay(9);
			axes.remove(axe);
			axe.remove();
		});
	}

	private void spawnSouthEastAxeThrowEvent() {
		NPC largeTendril = new NPC(largeTendrilNPCId).spawn(npc.getPosition().getRegion().baseX + 46, npc.getPosition().getRegion().baseY + 21, 0, Direction.NORTH_WEST, 0);
		largeTendril.animate(10364);
		World.startEvent(event -> {
			event.delay(1);
			largeTendril.animate(10365);
			event.delay(2);
			NPC axe = new NPC(axeNPCId).spawn(npc.getPosition().getRegion().baseX + 45, npc.getPosition().getRegion().baseY + 22, 0, Direction.NORTH_WEST, 0);
			axes.add(axe);
			axe.step(-9, 9, StepType.WALK);
			largeTendril.remove();
			event.delay(9);
			axes.remove(axe);
			axe.remove();
		});
	}

	private void spawnNorthWestAxeThrowEvent() {
		NPC largeTendril = new NPC(largeTendrilNPCId).spawn(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 31, 0, Direction.SOUTH_EAST, 0);
		largeTendril.animate(10364);
		World.startEvent(event -> {
			event.delay(1);
			largeTendril.animate(10365);
			event.delay(2);
			NPC axe = new NPC(axeNPCId).spawn(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 30, 0, Direction.SOUTH_EAST, 0);
			axes.add(axe);
			axe.step(9, -9, StepType.WALK);
			largeTendril.remove();
			event.delay(9);
			axes.remove(axe);
			axe.remove();
		});
	}

	private void spawnNorthEastAxeThrowEvent() {
		NPC largeTendril = new NPC(largeTendrilNPCId).spawn(npc.getPosition().getRegion().baseX + 46, npc.getPosition().getRegion().baseY + 31, 0, Direction.SOUTH_WEST, 0);
		largeTendril.animate(10364);
		World.startEvent(event -> {
			event.delay(1);
			largeTendril.animate(10365);
			event.delay(2);
			NPC axe = new NPC(axeNPCId).spawn(npc.getPosition().getRegion().baseX + 45, npc.getPosition().getRegion().baseY + 30, 0, Direction.SOUTH_WEST, 0);
			axes.add(axe);
			axe.step(-9, -9, StepType.WALK);
			largeTendril.remove();
			event.delay(9);
			axes.remove(axe);
			axe.remove();
		});
	}

	private void handleSpikeEvent(Entity target) {
		List<Position> spikePositions = new ArrayList<>();
		spikePositions.add(target.getPosition().copy());
		Bounds bounds = new Bounds(npc.getPosition().getX() - 2, npc.getPosition().getY() - 2, npc.getPosition().getX() + 2, npc.getPosition().getY() + 2, 0);
		while (spikePositions.size() < 7) {
			Position position = bounds.randomPosition();
			if (!spikePositions.contains(position) && !hasClipping(position.getX(), position.getY())) {
				spikePositions.add(position);
			}
		}
		spikePositions.forEach(this::spawnSpike);
	}

	private boolean hasClipping(int x, int y) {
		return Tile.get(x, y, npc.getPosition().getZ(), true).clipping != 0;
	}

	Projectile projectile = new Projectile(2521, 60, 31, 35, 35, 10, 0, 32);

	private void startHeadEvent() {
		headEvent.delay(20);
		NPC head = new NPC(headNPCId).spawn(headSpawnBounds.randomPosition());
		head.face(getPlayers().getFirst());
		World.startEvent(event -> {
			event.delay(1);
			Player target = getPlayers().getFirst();
			int delay = projectile.send(head, target);
			head.remove();
			event.delay(World.getTicks(delay) + 1);
			if (target != null) {
				if (!target.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
					target.hit(new Hit().randDamage(10, 22));
					damagedPlayer = true;
					target.getPrayer().slashPrayers();
				}
			}
		});
	}
}
