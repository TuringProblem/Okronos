package io.ruin.model.activities.raids.tob.dungeon.boss.maiden.attacks;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Projectile;

public class BloodSpotAttack {

	private final int bloodPitGFX = 1579;
	private final int bloodPitAnim = 8091;
	private final Projectile bloodProjectile = new Projectile(1578, 20, 31, 20, 15, 12, 15, 10);
	private final int bloodSplash = 1576;

	public BloodSpotAttack(NPC npc) {
		bloodSpatAttack(npc);
	}

	private void bloodSpatAttack(NPC npc) {
		npc.animate(bloodPitAnim);

		Player furthest = null;
		double furthestDistance = Double.MIN_VALUE;
		for (Player p : npc.getPosition().getRegion().players) {
			double distance = npc.getPosition().distance(p.getPosition());
			if (distance > furthestDistance) {
				furthest = p;
				furthestDistance = distance;
			}
		}
		if (furthest == null) return;
		npc.face(furthest);
		for (Player p : npc.getPosition().getRegion().players) {
			sendBloodPit(p);
			if (furthest.equals(p)) {
				npc.startEvent(event -> event.delay(1));
				sendBloodPit(p);
				npc.startEvent(event -> event.delay(1));
				sendBloodPit(p);
			}
		}
	}

	private void sendBloodPit(Player target) {
	}
}
