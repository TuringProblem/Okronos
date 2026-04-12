package io.ruin.model.activities.inferno.monsters;

import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class JalImKot extends NPCCombat { // Melee snake

	private int impatience;

	@Override
	public void init() {

	}

	@Override
	public void follow() {
		follow(1);
	}

	@Override
	public boolean attack() {
		if (npc.isRemoved() || target == null || target.getCombat().isDead()) {
			return false;
		}

		if (!withinDistance(1)) {
			impatience++;
			if (impatience >= 35) {
				impatience = 0;
				dig();
			}
			return false;
		}
		basicAttack();
		return true;
	}

	@Override
	public void process() {

	}

	private void dig() {
		if (target == null || npc.isRemoved() || target.getCombat().isDead() || !(target instanceof Player)) {
			return;
		}

		Player player = (Player) target;
		if (!player.inDynamicMap || player.currentDynamicMap == null) {
			return;
		}

		Position targetPos = target.getPosition().copy();
		Position destination = null;
		List<Position> positionList = new LinkedList<>();

		Bounds b = new Bounds(targetPos, 1);
		b.forEachPos(p -> {
			if (!p.equals(targetPos)) {
				positionList.add(p);
			}
		});
		Collections.shuffle(positionList);

		for (Position p : positionList) {
			int x = p.getX();
			int y = p.getY();
			if (x < targetPos.getX()) {
				x -= npc.getSize() - 1;
			}
			if (y < targetPos.getY()) {
				y -= npc.getSize() - 1;
			}
			if (canFit(x, y, p.getZ())) {
				destination = new Position(x, y, p.getZ());
				break;
			}
		}

		if (destination == null) {
			return;
		}

		npc.lock();
		Position finalDestination = destination;
		npc.addEvent(event -> {
			if (!npc.isRemoved()) {
				npc.animate(7600);
				event.delay(5);
				npc.getMovement().teleport(finalDestination);
				npc.animate(7601);
				event.delay(8);
				npc.unlock();
			}
		});
	}

	private boolean canFit(int dX, int dY, int z) {
		for (int x = dX; x < dX + npc.getSize(); x++) {
			for (int y = dY; y < dY + npc.getSize(); y++) {
				Tile tile = Tile.get(x, y, z);
				if (tile == null || tile.clipping != 0) {
					return false;
				}
			}
		}
		return true;
	}
}
