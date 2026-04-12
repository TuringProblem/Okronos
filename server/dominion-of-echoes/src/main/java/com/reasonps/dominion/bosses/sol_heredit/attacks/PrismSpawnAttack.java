package com.reasonps.dominion.bosses.sol_heredit.attacks;

import com.reasonps.dominion.bosses.Attack;
import com.reasonps.dominion.bosses.sol_heredit.EchoSolHeredit;
import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-29
 */
public class PrismSpawnAttack implements Attack {

	private EchoSolHeredit solHeredit;

	@Override
	public void invoke(Player target, NPCCombat boss) {
		solHeredit = (EchoSolHeredit) boss;
		var prism = new NPC(12824)
			.spawn(getPrismSpawnPosition(boss));
		solHeredit.getPrisms().add(prism);
		var canMove = new AtomicBoolean(true);
		var cornerPositions = getPrismCornerPosition(boss);
		var lightDirection = getLightDirection(boss);
		var ticks = new AtomicInteger();

		final Position[] targetPosition = { Random.get(cornerPositions) };

		if (solHeredit.getPrismsSpawned() == 0)
			solHeredit.setPrismTicksUntilLight(30);

		solHeredit.setPrismsSpawned(solHeredit.getPrismsSpawned() + 1);

		World.startEvent(e -> {
			while (!solHeredit.isDead() || !solHeredit.getNpc().isRemoved()) {
				e.delay(1);
				assert targetPosition[0] != null: "Seems the targetPosition is null";
				if (prism.getPosition().distance(targetPosition[0]) < 2)
					targetPosition[0] = getNextCornerPosition(cornerPositions, prism);

				int xDistance = targetPosition[0].getX() - prism.getPosition().getX();
				int yDistance = targetPosition[0].getY() - prism.getPosition().getY();
				if (canMove.get())
					prism.step(xDistance, yDistance, StepType.WALK);
				else prism.resetSteps();
				ticks.getAndIncrement();
				if (solHeredit.getPrismTicksUntilLight() <= 0) {
					ticks.set(0);
					canMove.set(false);
					prism.resetSteps();
					prism.getMovement().reset();
					prism.lock();
					e.delay(1);
					prism.animate(10801);  // Play prism animation
					var stopPosition = prism.getPosition().copy();
					e.delay(7);
					drawLightBeam(stopPosition, lightDirection);
					e.delay(4);
					sendLight(target, stopPosition, lightDirection);

					canMove.set(true);
					prism.animate(-1);
					prism.unlock();

					solHeredit.setPrismTicksUntilLight(24);
				}
			}
			if (solHeredit.isDead() || solHeredit.getNpc().isRemoved())
				prism.remove();

		}).setCancelCondition(boss::targetIsNotInBossRegion);
	}

	private Position getPrismSpawnPosition(NPCCombat boss) {
		var region = boss.getNpc().getPosition().getRegion();
		return switch (((EchoSolHeredit) boss).getPrismsSpawned()) {
			case 1 -> Position.of(region.baseX + 33, region.baseY + 26);//south
			case 2 -> Position.of(region.baseX + 41, region.baseY + 35);//east
			case 3 -> Position.of(region.baseX + 31, region.baseY + 42);//north
			default -> Position.of(region.baseX + 24, region.baseY + 34);//west
		};
	}

	private List<Position> getPrismCornerPosition(NPCCombat boss) {
		var region = boss.getNpc().getPosition().getRegion();
		return switch (((EchoSolHeredit) boss).getPrismsSpawned()) {
			case 1 -> List.of(
				Position.of(region.baseX + 27, region.baseY + 26),
				Position.of(region.baseX + 38, region.baseY + 26)
			);
			case 2 -> List.of(
				Position.of(region.baseX + 41, region.baseY + 40),
				Position.of(region.baseX + 41, region.baseY + 29)
			);
			case 3 -> List.of(
				Position.of(region.baseX + 27, region.baseY + 43),
				Position.of(region.baseX + 38, region.baseY + 43)
			);
			default -> List.of(
				Position.of(region.baseX + 24, region.baseY + 29),
				Position.of(region.baseX + 24, region.baseY + 40)
			);
		};
	}

	private Position getNextCornerPosition(List<Position> positions, NPC prism) {
		Position position = null;
		for (var pos : positions) {
			if (position == null)
				position = pos;
			if (prism.getPosition().distance(pos) > prism.getPosition().distance(position))
				position = pos;
		}
		return position;
	}

	private Direction getLightDirection(NPCCombat boss) {
		return switch (((EchoSolHeredit) boss).getPrismsSpawned()) {
			case 1 -> Direction.NORTH;
			case 2 -> Direction.WEST;
			case 3 -> Direction.SOUTH;
			default -> Direction.EAST;
		};
	}

	private void drawLightBeam(Position prismPosition, Direction dir) {
		if (dir == Direction.EAST) {
			for (int i = 1; i < 17; i++) {
				Position pos = new Position(prismPosition.getX() + i, prismPosition.getY(), 0);
				World.sendGraphics(3150, 0, i, pos);
			}
		}
		if (dir == Direction.NORTH) {
			for (int i = 1; i < 17; i++) {
				Position pos = new Position(prismPosition.getX(), prismPosition.getY() + i, 0);
				World.sendGraphics(3149, 0, i, pos);
			}
		}
		if (dir == Direction.WEST) {
			for (int i = 1; i < 17; i++) {
				Position pos = new Position(prismPosition.getX() - i, prismPosition.getY(), 0);
				World.sendGraphics(3148, 0, i, pos);
			}
		}
		if (dir == Direction.SOUTH) {
			for (int i = 1; i < 17; i++) {
				Position pos = new Position(prismPosition.getX(), prismPosition.getY() - i, 0);
				World.sendGraphics(3147, 0, i, pos);
			}
		}
	}

	private void sendLight(Player player, Position prismPosition, Direction dir) {
		if (dir == Direction.EAST) {
			for (int i = 0; i < 17; i++) {
				Position pos = new Position(prismPosition.getX() + i, prismPosition.getY(), 0);
				World.sendGraphics(3154, 0, i * 2, pos);
				if (i == 16)
					World.sendGraphics(3154, 0, (i * 2) + 1, pos);
				sunRoastTile(player, pos);
			}
		}
		if (dir == Direction.NORTH) {
			for (int i = 0; i < 17; i++) {
				Position pos = new Position(prismPosition.getX(), prismPosition.getY() + i, 0);
				World.sendGraphics(3153, 0, i * 2, pos);
				if (i == 16)
					World.sendGraphics(3153, 0, (i * 2) + 1, pos);
				sunRoastTile(player, pos);
			}
		}
		if (dir == Direction.WEST) {
			for (int i = 0; i < 17; i++) {
				Position pos = new Position(prismPosition.getX() - i, prismPosition.getY(), 0);
				World.sendGraphics(3152, 0, i * 2, pos);
				if (i == 16)
					World.sendGraphics(3152, 0, (i * 2) + 1, pos);
				sunRoastTile(player, pos);
			}
		}
		if (dir == Direction.SOUTH) {
			for (int i = 0; i < 17; i++) {
				Position pos = new Position(prismPosition.getX(), prismPosition.getY() - i, 0);
				World.sendGraphics(3151, 0, i * 2, pos);
				if (i == 16)
					World.sendGraphics(3151, 0, (i * 2) + 1, pos);
				sunRoastTile(player, pos);
			}
		}
	}

	private void sunRoastTile(Player player, Position pos) {
		if (player.getPosition().distance(pos) < 1 && solHeredit.getNpc().getHp() > 0) {
			World.sendGraphics(3155, 0, 0, pos);
			player.hit(new Hit().randDamage(55, 70));
			solHeredit.damagedPlayer = true;
		}
	}

}
