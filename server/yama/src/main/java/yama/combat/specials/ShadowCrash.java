package yama.combat.specials;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import yama.combat.Yama;
import yama.combat.hazards.ShadowWaves;
import yama.combat.hazards.VoidFlares;

import java.util.HashSet;
import java.util.Set;

public class ShadowCrash {
	private static final int GFX_FIREBALL = 3262;
	private static final int GFX_SMOKE    = 3252;

	public enum Direction {
		EAST_WEST,
		NORTH_SOUTH,
		NE_SW,
		NW_SE
	}

	public void send(NPC npc, Player target) {
		int minX = npc.getPosition().getRegion().baseX + 17;
		int minY = npc.getPosition().getRegion().baseY + 20;
		int maxX = npc.getPosition().getRegion().baseX + 44;
		int maxY = npc.getPosition().getRegion().baseY + 45;

		int swX = minX + 1, swY = minY + 4;
		int seX = maxX - 1, seY = minY + 4;
		int nwX = minX + 1, nwY = maxY - 4;
		int neX = maxX - 1, neY = maxY - 4;

		int inset = 3;

		int nwX2 = nwX + inset;
		int nwY2 = nwY - inset;

		int neX2 = neX - inset;
		int neY2 = neY - inset;
		World.startEvent(e -> {
			sendCrash(npc, target, ShadowCrash.Direction.NORTH_SOUTH);
			e.delay(4);
			sendCrash(npc, target, ShadowCrash.Direction.NW_SE);
			e.delay(4);
			sendCrash(npc, target, ShadowCrash.Direction.EAST_WEST);
			e.delay(3);
			ShadowWaves.sendShadowWave(npc, minX, minY, maxX, maxY, seX, seY, -1, +1);
			ShadowWaves.sendShadowWave(npc, minX, minY, maxX, maxY, swX, swY, +1, +1);
			e.delay(6);
			new VoidFlares().spawnOneVoidflare(npc);
			new VoidFlares().spawnOneVoidflare(npc);
			e.delay(6);
			ShadowWaves.sendShadowWave(npc, minX, minY, maxX, maxY, nwX2, nwY2, +1, -1);
			ShadowWaves.sendShadowWave(npc, minX, minY, maxX, maxY, neX2, neY2, -1, -1);
			e.delay(15);
			ShadowWaves.sendShadowWave(npc, minX, minY, maxX, maxY, nwX2, nwY2, +1, -1);
			ShadowWaves.sendShadowWave(npc, minX, minY, maxX, maxY, neX2, neY2, -1, -1);
			ShadowWaves.sendShadowWave(npc, minX, minY, maxX, maxY, seX, seY, -1, +1);
			ShadowWaves.sendShadowWave(npc, minX, minY, maxX, maxY, swX, swY, +1, +1);
		});
	}

	public void sendCrash(NPC npc, Player target, Direction dir) {
		final Position mid = target.getPosition().copy();
		final int midX = mid.getX();
		final int midY = mid.getY();

		final Position sideOne;
		final Position sideTwo;
		sideTwo = switch (dir) {
			case EAST_WEST -> {
				sideOne = new Position(midX - 3, midY, 0);
				yield new Position(midX + 3, midY, 0);
			}
			case NORTH_SOUTH -> {
				sideOne = new Position(midX, midY - 3, 0);
				yield new Position(midX, midY + 3, 0);
			}
			case NE_SW -> {
				sideOne = new Position(midX - 3, midY - 3, 0);
				yield new Position(midX + 3, midY + 3, 0);
			}
			default -> {
				sideOne = new Position(midX - 3, midY + 3, 0);
				yield new Position(midX + 3, midY - 3, 0);
			}
		};

		World.startEvent(e -> {
			World.sendGraphics(GFX_FIREBALL, 0, 0, mid);
			World.sendGraphics(GFX_FIREBALL, 0, 0, sideOne);
			World.sendGraphics(GFX_FIREBALL, 0, 0, sideTwo);

			e.delay(3);

			int delay = 0;
			for (int i = 3; i >= 0; i--) {
				switch (dir) {
					case EAST_WEST: {
						int xW = midX - i, xE = midX + i;
						for (int j = -i; j <= i; j++) {
							World.sendGraphics(GFX_SMOKE, 0, delay, new Position(xW, midY + j, 0));
							if (i != 0) World.sendGraphics(GFX_SMOKE, 0, delay, new Position(xE, midY + j, 0));
						}
						break;
					}
					case NORTH_SOUTH: {
						int yS = midY - i, yN = midY + i;
						for (int j = -i; j <= i; j++) {
							World.sendGraphics(GFX_SMOKE, 0, delay, new Position(midX + j, yS, 0));
							if (i != 0) World.sendGraphics(GFX_SMOKE, 0, delay, new Position(midX + j, yN, 0));
						}
						break;
					}
					case NE_SW: {
						int lx1x = midX - i, lx1y = midY - i;
						int lx2x = midX + i, lx2y = midY + i;
						for (int j = -i; j <= i; j++) {
							World.sendGraphics(GFX_SMOKE, 0, delay, new Position(lx1x + j, lx1y - j, 0));
							if (i != 0) World.sendGraphics(GFX_SMOKE, 0, delay, new Position(lx2x + j, lx2y - j, 0));
						}
						break;
					}
					case NW_SE: {
						int lx1x = midX - i, lx1y = midY + i;
						int lx2x = midX + i, lx2y = midY - i;
						for (int j = -i; j <= i; j++) {
							World.sendGraphics(GFX_SMOKE, 0, delay, new Position(lx1x + j, lx1y + j, 0));
							if (i != 0) World.sendGraphics(GFX_SMOKE, 0, delay, new Position(lx2x + j, lx2y + j, 0));
						}
						break;
					}
				}
				delay += 8;
			}

			final Set<Position> danger = new HashSet<>();
			for (int i = 3; i >= 0; i--) {
				switch (dir) {
					case EAST_WEST: {
						int xW = midX - i, xE = midX + i;
						for (int j = -i; j <= i; j++) {
							danger.add(new Position(xW, midY + j, 0));
							if (i != 0) danger.add(new Position(xE, midY + j, 0));
						}
						break;
					}
					case NORTH_SOUTH: {
						int yS = midY - i, yN = midY + i;
						for (int j = -i; j <= i; j++) {
							danger.add(new Position(midX + j, yS, 0));
							if (i != 0) danger.add(new Position(midX + j, yN, 0));
						}
						break;
					}
					case NE_SW: {
						int lx1x = midX - i, lx1y = midY - i;
						int lx2x = midX + i, lx2y = midY + i;
						for (int j = -i; j <= i; j++) {
							danger.add(new Position(lx1x + j, lx1y - j, 0));
							if (i != 0) danger.add(new Position(lx2x + j, lx2y - j, 0));
						}
						break;
					}
					case NW_SE: {
						int lx1x = midX - i, lx1y = midY + i;
						int lx2x = midX + i, lx2y = midY - i;
						for (int j = -i; j <= i; j++) {
							danger.add(new Position(lx1x + j, lx1y + j, 0));
							if (i != 0) danger.add(new Position(lx2x + j, lx2y + j, 0));
						}
						break;
					}
				}
			}
			if (danger.contains(target.getPosition())) {
				target.hit(new Hit(npc).randDamage(20));
			}
		});
	}
}





