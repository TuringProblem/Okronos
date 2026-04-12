package io.ruin.model.activities.raids.toa.bosses.kephri;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.raids.toa.Invocations;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;

import java.util.ArrayList;
import java.util.List;

public class ScarabMager extends NPCCombat {
	int locationChanged = 0;
	Position southEastCorner;
	Position southWestCorner;
	Position northEastCorner;
	Position northWestCorner;
	int timeAlive = 0;
	List<Position> locations = new ArrayList<>();

	public boolean damagedPlayer = false;

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDefend(this::postDefend);
		northWestCorner = new Position(npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 37, 0);
		northEastCorner = new Position(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 37, 0);
		southEastCorner = new Position(npc.getPosition().getRegion().baseX + 36, npc.getPosition().getRegion().baseY + 27, 0);
		southWestCorner = new Position(npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 27, 0);
		locations.add(northWestCorner);
		locations.add(northEastCorner);
		locations.add(southEastCorner);
		locations.add(southWestCorner);
	}

	private void postDefend(Hit hit) {
		if (npc.getHp() < npc.getMaxHp() / 2 && locationChanged == 0) {
			changeLocation();
		}
	}

	private void changeLocation() {
		locationChanged++;
		Position newPosition = null;
		while (newPosition == null) {
			Position randomPosition = Random.get(locations);
			if (npc.getPosition().distance(randomPosition) > 4) {
				newPosition = randomPosition;
			}
		}
		Position finalNewPosition = newPosition;
		World.startEvent(e -> {
			npc.animate(9597);
			e.delay(1);
			npc.getMovement().teleport(finalNewPosition);
			npc.animate(9596);
		});
	}

	Projectile projectile = new Projectile(2168, 30, 31, 0, 100, 0, 16, 64);

	private void sendBurst() {
		timeAlive = 0;
		npc.getPosition().getRegion().players.forEach(p -> {
			int minDamage = 50;
			int maxDamage = 100;
			if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
				minDamage /= 3;
				maxDamage /= 3;

				if (p.getCurrentToARaid() != null && p.getCurrentToARaid().getInvocations().contains(Invocations.QUIET_PRAYERS)) {
					minDamage *= 1.1;
					maxDamage *= 1.1;
				}
			} else damagedPlayer = true;
			int delay = projectile.send(npc, p);
			p.hit(new Hit(npc).randDamage(minDamage, maxDamage).clientDelay(delay).ignorePrayer());
			p.graphics(2169, 0, delay);
		});
	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		npc.face(target);
		return true;
	}

	@Override
	public void process() {
		if (target == null) {
			if (!npc.getPosition().getRegion().players.isEmpty()) {
				target = Random.get(npc.getPosition().getRegion().players);
			}
		}
		if (npc.getHp() > 0) {
			timeAlive++;
			if (timeAlive >= 60) {
				sendBurst();
			}
		}
	}
}
