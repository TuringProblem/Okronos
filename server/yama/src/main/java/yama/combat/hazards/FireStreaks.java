package yama.combat.hazards;

import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.map.Position;
import io.ruin.utility.Random;
import yama.combat.Yama;
import yama.combat.util.Hazard;
import yama.combat.util.Phase;

import java.util.ArrayList;
import java.util.List;

public class FireStreaks extends Hazard {
	@Override
	public void sendHazard(NPC npc, Phase phase) {
		Yama yama = (Yama) npc.getCombat();
		int amountPerSide = 4 + Random.get(1);
		if(phase == Phase.TWO) {
			amountPerSide++;
		}

		List<Position> eastFireSpawns = new ArrayList<>();
		List<Position> westFireSpawns = new ArrayList<>();

		List<Integer> availableYPositions = new ArrayList<>();

		for(int i = 22; i < 36; i++) {
			availableYPositions.add(npc.getPosition().getRegion().baseY + i);
		}


		for(int i = 0; i < amountPerSide; i++) {
			Position eastSpawn = new Position(npc.getPosition().getRegion().baseX + 42, Random.get(availableYPositions), 0);
			availableYPositions.remove((Integer) eastSpawn.getY());
			Position westSpawn = new Position(npc.getPosition().getRegion().baseX + 18, Random.get(availableYPositions), 0);
			availableYPositions.remove((Integer) westSpawn.getY());

			eastFireSpawns.add(eastSpawn);
			westFireSpawns.add(westSpawn);
		}
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc == null || npc.getHp() < 1 || yama.isInIntermission());
			for(int i = 0; i < 12; i++) {
				for(Position pos : eastFireSpawns) {
					Position secondaryPos = new Position(pos.getX() - 1, pos.getY(), pos.getZ());
					World.sendGraphics(3267, 0, 0, pos);
					World.sendGraphics(3267, 0, 15, secondaryPos);
					npc.getPosition().getRegion().players.forEach(p -> {
						if(p.getPosition().distance(pos) < 1 || p.getPosition().distance(secondaryPos) < 1) {
							if(yama.getFireImmunityTicks() < 1) {
								p.hit(new Hit(npc).fixedDamage(25));
								p.getPrayer().slashPrayers();
							} else {
								p.sendMessage(Color.ORANGE2.wrap("The Glyph of Fire absorbs" +
									" the damage from the attack."));
							}
						}
					});
					pos.translate(-2, 0);
				}
				for(Position pos : westFireSpawns) {
					Position secondaryPos = new Position(pos.getX() + 1, pos.getY(), pos.getZ());
					World.sendGraphics(3267, 0, 0, pos);
					World.sendGraphics(3267, 0, 15, secondaryPos);
					npc.getPosition().getRegion().players.forEach(p -> {
						if(p.getPosition().distance(pos) < 1 || p.getPosition().distance(secondaryPos) < 1) {
							if(yama.getFireImmunityTicks() < 1) {
								p.hit(new Hit(npc).fixedDamage(25));
								p.getPrayer().slashPrayers();
							} else {
								p.sendMessage(Color.ORANGE2.wrap("The Glyph of Fire absorbs" +
									" the damage from the attack."));
							}
						}
					});
					pos.translate(2, 0);
				}
				e.delay(1);
			}
		});
	}
}
