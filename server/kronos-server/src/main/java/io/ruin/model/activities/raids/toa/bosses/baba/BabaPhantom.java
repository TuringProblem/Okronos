package io.ruin.model.activities.raids.toa.bosses.baba;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;

public class BabaPhantom extends NPCCombat {

	//anim 9744
	//gfx 2250

	@Override
	public void init() {
	}

	public boolean attack() {
		;
		return false;
	}

	int ticksTilAttack = 0;

	@Override
	public void process() {
		if (ticksTilAttack-- <= 0) {
			ticksTilAttack = 5;
			rangedAttack();
		}
	}

	private void rangedAttack() {
		npc.animate(9744);
		for (Player player : npc.getPosition().getRegion().players) {
			if (player == null)
				continue;
			World.startEvent(e -> {
				Position targetPos = player.getPosition().copy();
				World.sendGraphics(2250, 0, 0, targetPos);
				e.delay(5);
				if (player.getPosition().distance(targetPos) < 1 && !npc.isRemoved()) {
					player.hit(new Hit().randDamage(20, 35));
				}
			});
		}
	}

	@Override
	public void follow() {
	}

	@Override
	public int getAggressionRange() {
		return 100;
	}
}
