package com.reasonps.dominion.bosses.sol_heredit.attacks;

import com.reasonps.dominion.bosses.Attack;
import com.reasonps.dominion.bosses.IDirectional;
import com.reasonps.dominion.bosses.sol_heredit.EchoSolHeredit;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-29
 */
public class SpearSecondaryAttack implements Attack, IDirectional {

	@Override
	public void invoke(Player target, NPCCombat boss) {
		var startTile = boss.getNpc().getCentrePosition().copy();
		var dangerTiles = new ArrayList<Position>();
		var direction = getDirection(boss.getNpc().getCentrePosition(), target.getPosition());

		int extendLength = 8; // Length of the extended part
		switch (direction) {
			case EAST:
				// Base 5x5 square
				for (int y = startTile.getY() - 2; y <= startTile.getY() + 2; y++) {
					for (int x = startTile.getX() - 2; x <= startTile.getX() + 2; x++)
						dangerTiles.add(new Position(x, y, 0));
					// Extend to the right
					for (int x = startTile.getX() + 3; x <= startTile.getX() + 3 + extendLength; x++)
						if (y == startTile.getY() - 2 || y == startTile.getY() + 2 || y == startTile.getY())
							dangerTiles.add(new Position(x, y, 0));
				}
				break;
			case NORTH:
				// Base 5x5 square
				for (int x = startTile.getX() - 2; x <= startTile.getX() + 2; x++) {
					for (int y = startTile.getY() - 2; y <= startTile.getY() + 2; y++)
						dangerTiles.add(new Position(x, y, 0));
					// Extend upwards
					for (int y = startTile.getY() + 3; y <= startTile.getY() + 3 + extendLength; y++)
						if (x == startTile.getX() - 2 || x == startTile.getX() + 2 || x == startTile.getX())
							dangerTiles.add(new Position(x, y, 0));
				}
				break;
			case WEST:
				// Base 5x5 square
				for (int y = startTile.getY() - 2; y <= startTile.getY() + 2; y++) {
					for (int x = startTile.getX() - 2; x <= startTile.getX() + 2; x++)
						dangerTiles.add(new Position(x, y, 0));
					// Extend to the left
					for (int x = startTile.getX() - 3; x >= startTile.getX() - 3 - extendLength; x--)
						if (y == startTile.getY() - 2 || y == startTile.getY() + 2 || y == startTile.getY())
							dangerTiles.add(new Position(x, y, 0));
				}
				break;
			case SOUTH:
				// Base 5x5 square
				for (int x = startTile.getX() - 2; x <= startTile.getX() + 2; x++) {
					for (int y = startTile.getY() - 2; y <= startTile.getY() + 2; y++)
						dangerTiles.add(new Position(x, y, 0));
					// Extend downwards
					for (int y = startTile.getY() - 3; y >= startTile.getY() - 3 - extendLength; y--)
						if (x == startTile.getX() - 2 || x == startTile.getX() + 2 || x == startTile.getX())
							dangerTiles.add(new Position(x, y, 0));
				}
				break;
			case NORTH_WEST:
				dangerTiles.addAll(mapPositionsToDangerList(startTile, new int[][]{
					{0, 0}, {1, 1}, {-1, 1}, {0, 2}, {1, -1},
					{-2, 0}, {-3, 1}, {-2, -2}, {-2, 2}, {-1, -3}, {0, -4}, {2, -2}, {3, -1},
					{2, 0}, {1, 1}, {0, 2}, {-1, 3}, {0, 4},
					{1, 3}, {2, 2}, {3, 1}, {4, 0}, {1, -3}, {-3, -1}, {-4, 0},

					{-5, 1},
					{-6, 2}, {-7, 3}, {-8, 4},
					{-3, 3},
					{-4, 4}, {-5, 5}, {-6, 6},
					{-1, 5},
					{-2, 6}, {-3, 7}, {-4, 8}
				}));
				break;
			case NORTH_EAST:
				dangerTiles.addAll(mapPositionsToDangerList(startTile, new int[][]{
					{0, 0}, {-1, -1}, {1, -1}, {0, -2}, {-1, 1},
					{-2, 0}, {-3, 1}, {-2, -2}, {-2, 2}, {-1, -3}, {0, -4}, {2, -2}, {3, -1},
					{2, 0}, {1, 1}, {0, 2}, {-1, 3}, {0, 4},
					{1, 3}, {2, 2}, {3, 1}, {4, 0}, {1, -3}, {-3, -1}, {-4, 0},

					{5, 1},
					{6, 2}, {7, 3}, {8, 4},
					{3, 3},
					{4, 4}, {5, 5}, {6, 6},
					{1, 5},
					{2, 6}, {3, 7}, {4, 8}
				}));
				break;
			case SOUTH_WEST:
				dangerTiles.addAll(mapPositionsToDangerList(startTile, new int[][]{
					{0, 0}, {1, 1}, {-1, 1}, {0, 2}, {1, -1},
					{2, 0}, {3, -1}, {2, 2}, {2, -2}, {1, 3}, {0, 4}, {-2, 2}, {-3, 1},
					{-2, 0}, {-1, -1}, {0, -2}, {1, -3}, {0, -4},
					{-1, -3}, {-2, -2}, {-3, -1}, {-4, 0}, {-1, 3}, {3, 1}, {4, 0},

					{-5, -1},
					{-6, -2}, {-7, -3}, {-8, -4},
					{-3, -3},
					{-4, -4}, {-5, -5}, {-6, -6},
					{-1, -5},
					{-2, -6}, {-3, -7}, {-4, -8}
				}));
				break;
			case SOUTH_EAST:
				dangerTiles.addAll(mapPositionsToDangerList(startTile, new int[][]{
					{0, 0}, {-1, 1}, {1, 1}, {0, 2}, {-1, -1},
					{2, 0}, {3, -1}, {2, 2}, {2, -2}, {1, 3}, {0, 4}, {-2, 2}, {-3, 1},
					{-2, 0}, {-1, -1}, {0, -2}, {1, -3}, {0, -4},
					{-1, -3}, {-2, -2}, {-3, -1}, {-4, 0}, {-1, 3}, {3, 1}, {4, 0},

					{5, -1},
					{6, -2}, {7, -3}, {8, -4},
					{3, -3},
					{4, -4}, {5, -5}, {6, -6},
					{1, -5},
					{2, -6}, {3, -7}, {4, -8}
				}));
				break;
			default:
				throw new IllegalStateException("Unexpected direction: " + direction);
		}

		boss.getNpc().animate(10883);
		World.startEvent(event -> {
			event.setCancelCondition(boss::targetIsNotInBossRegion);
			event.delay(3);
			for (Position pos : dangerTiles) {
				World.sendGraphics(2670, 0, pos.distance(startTile), pos);
				if (target.getPosition().distance(pos) < 1 && boss.getNpc().getHp() > 0) {
					((EchoSolHeredit) boss).damagedPlayer = true;
					target.hit(new Hit().randDamage(35, 45));
				}
			}
		});
	}

	private List<Position> mapPositionsToDangerList(Position startTile, int[][] relativePositions) {
		return Arrays.stream(relativePositions)
			.map(pos -> Position.of(startTile.getX() + pos[0], startTile.getY() + pos[1]))
			.collect(Collectors.toCollection(ArrayList::new));
	}
}
