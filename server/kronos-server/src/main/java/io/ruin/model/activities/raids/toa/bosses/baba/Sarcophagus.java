package io.ruin.model.activities.raids.toa.bosses.baba;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.object.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Sarcophagus {
	public NPC npc;
	public GameObject sarcophagus;
	Bounds bounds;

	public Sarcophagus(int npcId, int objectId, Position position, Bounds bounds) {
		this.npc = new NPC(npcId).spawn(position);
		this.sarcophagus = new GameObject(objectId, position.getX(), position.getY(), position.getZ(), 10, 0).spawn();
		this.bounds = bounds;
	}

	int timeSinceLastAttack = 0;
	Projectile projectile = new Projectile(1383, 0, 0, 0, 0, 0, 0, 0);

	public void process(List<Player> players, NPC boss, int damage) {
		if (boss.getHp() < 1) {
			return;
		}
		if (npc.getHp() <= 0) {
			if (!npc.isRemoved())
				npc.remove();
			if (timeSinceLastAttack++ >= 8) {
				timeSinceLastAttack = 0;
				List<Position> tiles = new ArrayList<>();
				while (tiles.size() < 5) {
					Position pos = bounds.randomPosition();
					if (!tiles.contains(pos))
						tiles.add(pos);
				}
				World.startEvent(event -> {
					for (Position pos : tiles) {
						int delay = projectile.send(sarcophagus.getPosition(), pos);
						event.delay(delay);
						for (Player player : players) {
							if (player.getPosition().distance(pos) < 1) {
								player.hit(new Hit().fixedDamage(damage));
							}
						}
					}

				});
			}
		}
	}
}
