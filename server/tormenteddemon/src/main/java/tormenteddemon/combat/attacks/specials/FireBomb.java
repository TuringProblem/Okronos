package tormenteddemon.combat.attacks.specials;

import core.api.Random;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.var.VarPlayerRepository;
import tormenteddemon.combat.TormentedDemon;

import java.util.ArrayList;
import java.util.List;

public class FireBomb {

	Projectile BOMB_PROJECTILE = new Projectile(2855, 100, 0, 0, 135, 0, 0, 0);
	public void send(NPC npc, Player target) {
		npc.animate(11387);
		Position targetPosition = target.getPosition().copy();
		List<Position> positions = new ArrayList<>();
		for(int x = -1; x <= 1; x++) {
			for(int y = -1; y <= 1; y++) {
				if(x == 0 && y == 0)
					continue;
				positions.add(new Position(targetPosition.x() + x, targetPosition.y() + y, targetPosition.getZ()));
			}
		}
		Position targetPositionTwo = Random.get(positions);

		World.startEvent(1, e -> {
			int delay = BOMB_PROJECTILE.send(npc, targetPosition);
			BOMB_PROJECTILE.send(npc, targetPositionTwo);
			npc.graphics(2852);
			target.freeze(2, npc);
			target.sendMessage("The demon's spell binds you!");
			VarPlayerRepository.RUNNING.set(target, 0);
			target.graphics(179, 100, 0);
			e.delay(World.getTicks(delay) + 1);
			World.sendGraphics(2856, 0, 0, targetPosition);
			World.sendGraphics(2856, 0, 0, targetPositionTwo);
			if(target.getPosition().distance(targetPosition) < 1 || target.getPosition().distance(targetPositionTwo) < 1) {
				target.hit(new Hit(npc).randDamage(55, 80).ignoreDefence());
			}
			npc.step(2, Random.get(2), StepType.WALK);
			e.delay(2);
			TormentedDemon tormentedDemon = (TormentedDemon) npc.getCombat();
			tormentedDemon.setCanAttack(true);
		});
	}
}
