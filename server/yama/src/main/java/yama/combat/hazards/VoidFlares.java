package yama.combat.hazards;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.utility.Random;
import yama.combat.Yama;
import yama.combat.util.Direction;
import yama.combat.util.Hazard;
import yama.combat.util.Phase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VoidFlares extends Hazard {
	@Override
	public void sendHazard(NPC npc, Phase phase) {
		Bounds eastSpawnBounds = new Bounds(npc.getPosition().getRegion().baseX + 35,
			npc.getPosition().getRegion().baseY + 24, npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().
			baseY + 35, 0);

		Bounds westSpawnBounds = new Bounds(npc.getPosition().getRegion().baseX + 22,
			npc.getPosition().getRegion().baseY + 25, npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().
			baseY + 35, 0);

			Position firePosition = westSpawnBounds.randomPosition();
			Position shadowPosition = eastSpawnBounds.randomPosition();
			NPC fire = new NPC(14179).spawn(firePosition);
			fire.getCombat().setAllowRespawn(false);
			NPC shadow = new NPC(17028).spawn(shadowPosition);
			shadow.getCombat().setAllowRespawn(false);
	}

	public void spawnOneVoidflare(NPC npc) {
		Yama yama = (Yama) npc.getCombat();
		if(isFull(yama))
			return;
		List<Direction> free = new ArrayList<>();
		for (Direction slot : Direction.values()) {
			if (!yama.getOccupiedVoidflarePositions().containsKey(slot)) {
				free.add(slot);
			}
		}
		if (free.isEmpty()) return;
		Direction dir = Random.get(free);
		Bounds bounds = getBoundsFromDirection(dir, npc);
		Position spawnPos = bounds.randomPosition();
		if(spawnPos == null)
			return;

		int nextFlare = Random.get(1) == 0 ? 14179 : 17028;
		NPC flare = new NPC(nextFlare).spawn(spawnPos);
		flare.setHp(flare.getMaxHp() / 2);
		yama.getOccupiedVoidflarePositions().put(dir, flare.getId());
	}

	private boolean isFull(Yama yama) {
		return yama.getOccupiedVoidflarePositions().size() >= 4;
	}

	public static Bounds getBoundsFromDirection(Direction dir, NPC npc) {
		return switch (dir) {
			case EAST -> new Bounds(npc.getPosition().getRegion().baseX + 35,
				npc.getPosition().getRegion().baseY + 24, npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().
				baseY + 35, 0);
			case WEST -> new Bounds(npc.getPosition().getRegion().baseX + 22,
				npc.getPosition().getRegion().baseY + 25, npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().
				baseY + 35, 0);
			case SOUTH -> new Bounds(npc.getPosition().getRegion().baseX + 26,
				npc.getPosition().getRegion().baseY + 22, npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().
				baseY + 35, 0);
			case NORTH -> new Bounds(npc.getPosition().getRegion().baseX + 26,
				npc.getPosition().getRegion().baseY + 35, npc.getPosition().getRegion().baseX + 34, npc.getPosition().getRegion().
				baseY + 39, 0);
			default -> null;
		};
	}
}
