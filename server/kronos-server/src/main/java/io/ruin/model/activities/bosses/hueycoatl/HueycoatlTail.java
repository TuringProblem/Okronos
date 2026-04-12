package io.ruin.model.activities.bosses.hueycoatl;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Position;

public class HueycoatlTail extends NPCCombat {

	Position westCaveSpawn;
	Position eastCaveSpawn;

	@Override
	public void init() {
		npc.hitListener = new HitListener().preDefend(this::preDefend);
		westCaveSpawn = new Position(0, 0, 0);//TODO:
		eastCaveSpawn = new Position(0, 0, 0);//TODO:
		shockwaveSpecial();
	}

	private void preDefend(Hit hit) {
		hit.boostDamage(0.95);
		hit.boostDefence(3);
	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		return false;
	}

	private void shockwaveSpecial() {
		if (side == 0) {
			Position startPos = new Position(npc.getCentrePosition().copy().getX() + 2, npc.getCentrePosition().copy().getY(), npc.getCentrePosition().copy().getZ());
			World.startEvent(e -> {
				int lines = 3;
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < lines; j++) {
						Position positionOne = new Position(startPos.getX() - j, startPos.getY() - (i - j), startPos.getZ());
						Position positionTwo = new Position(startPos.getX() - j, startPos.getY() + (i - j), startPos.getZ());
						World.sendGraphics(2235, 0, 0, positionOne);
						World.sendGraphics(2235, 0, 0, positionTwo);
						npc.localPlayers().forEach(player -> {
							if (player.getPosition().distance(positionOne) < 1 || player.getPosition().distance(positionTwo) < 1) {
								player.hit(new Hit(npc).randDamage(18, 25));
							}
						});
					}
					lines++;
					e.delay(2);
				}
			});
		} else {
			Position startPos = new Position(npc.getCentrePosition().copy().getX() - 2, npc.getCentrePosition().copy().getY(), npc.getCentrePosition().copy().getZ());
			World.startEvent(e -> {
				int lines = 3;
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < lines; j++) {
						Position positionOne = new Position(startPos.getX() + j, startPos.getY() - (i - j), startPos.getZ());
						Position positionTwo = new Position(startPos.getX() + j, startPos.getY() + (i - j), startPos.getZ());
						World.sendGraphics(2235, 0, 0, positionOne);
						World.sendGraphics(2235, 0, 0, positionTwo);
						npc.localPlayers().forEach(player -> {
							if (player.getPosition().distance(positionOne) < 1 || player.getPosition().distance(positionTwo) < 1) {
								player.hit(new Hit(npc).randDamage(18, 25));
							}
						});
					}
					lines++;
					e.delay(2);
				}
			});
		}
	}

	int ticksSinceSideChange = 0;
	int side = 0;

	@Override
	public void process() {
		if (ticksSinceSideChange > 20) {
			ticksSinceSideChange = 0;
			World.startEvent(e -> {
				npc.animate(1);
				npc.setHidden(true);
				e.delay(2);
				npc.getMovement().teleport(side == 0 ? eastCaveSpawn : westCaveSpawn);
				side = side == 0 ? 1 : 0;
				npc.setHidden(false);
				shockwaveSpecial();
			});
		}
	}
}
