package yama.combat.hazards;

import io.ruin.cache.Color;
import io.ruin.model.World;

import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.utility.Random;
import yama.combat.Yama;
import yama.combat.util.Hazard;
import yama.combat.util.Phase;

import java.util.*;

public class ShadowWaves extends Hazard {
	private static final int GFX_ID   = 3265;
	private static final int BASE     = 150;
	private static final int STEP     = 15;

	@Override
	public void sendHazard(NPC npc, Phase phase) {
		int minX = npc.getPosition().getRegion().baseX + 17;
		int minY = npc.getPosition().getRegion().baseY + 20;
		int maxX = npc.getPosition().getRegion().baseX + 44;
		int maxY = npc.getPosition().getRegion().baseY + 45;

		int swX = minX + 1, swY = minY + 4;
		int seX = maxX - 1, seY = minY + 4;
		int nwX = minX + 1, nwY = maxY - 4;
		int neX = maxX - 1, neY = maxY - 4;
		if(phase == Phase.ONE) {
			if(Random.get(1) == 0) {
				ShadowWaves.sendShadowWave(npc, minX, minY, maxX, maxY, swX, swY, +1, +1);
			} else {
				ShadowWaves.sendShadowWave(npc, minX, minY, maxX, maxY, neX, neY, -1, -1);
			}
		} else {
			if(Random.get(1) == 0) {
				ShadowWaves.sendShadowWave(npc, minX, minY, maxX, maxY, seX, seY, -1, +1);
				ShadowWaves.sendShadowWave(npc, minX, minY, maxX, maxY, swX, swY, +1, +1);
			} else {
				ShadowWaves.sendShadowWave(npc, minX, minY, maxX, maxY, nwX, nwY, +1, -1);
				ShadowWaves.sendShadowWave(npc, minX, minY, maxX, maxY, neX, neY, -1, -1);
			}
		}
	}

	public static void sendShadowWave(NPC npc, int minX, int minY, int maxX, int maxY,
									  int originX, int originY, int sx, int sy) {
		Yama yama = (Yama) npc.getCombat();
		TreeMap<Integer, List<Position>> rows = new TreeMap<>();
		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				Position pos = new Position(x, y, 0);
				if (Tile.get(pos, true).clipping != 0)
					continue;

				int idx = sx * (x - originX) + sy * (y - originY);
				if (idx < 0)
					continue;

				int delay = BASE + STEP * idx;
				World.sendGraphics(GFX_ID, 0, delay, pos);
				rows.computeIfAbsent(idx, k -> new ArrayList<>()).add(pos);
			}
		}

		if (rows.isEmpty()) return;
		final int maxRow = rows.lastKey();

		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.isRemoved() || npc.getHp() < 1);
			e.delay(5);
			int tick = 0;
			for (int i = 0; i < 20; i++) {
				int rowA = 2 * tick;
				int rowB = rowA + 1;

				if (rowA > maxRow) {
					break;
				}

				List<Position> bandA = rows.get(rowA);
				List<Position> bandB = rows.get(rowB);
				List<Player> players = new ArrayList<>(npc.getPosition().getRegion().players);

				for (Player pl : players) {
					Position pp = pl.getPosition();
					if (bandA != null) {
						for (Position pos : bandA) if (pp.equals(pos)) {
							if (yama.getShadowImmunityTicks() < 1) {
								pl.hit(new Hit(npc).fixedDamage(20));
								pl.getPrayer().slashPrayers();
							} else {
								pl.sendMessage(Color.PURPLE2.wrap("The Glyph of Shadow absorbs" +
									" the damage from the attack."));
							}
						}
					}
					if (bandB != null) {
						for (Position pos : bandB) if (pp.equals(pos)) {
							if (yama.getShadowImmunityTicks() < 1) {
								pl.hit(new Hit(npc).fixedDamage(20));
								pl.getPrayer().slashPrayers();
							} else {
								pl.sendMessage(Color.PURPLE2.wrap("The Glyph of Shadow absorbs" +
									" the damage from the attack."));
							}
						}
					}
				}
				e.delay(1);
				tick++;
			}
		});
	}
}
