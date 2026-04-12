package yama.npc.judgeofyama.attacks;


import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import org.rsmod.events.SuspendEvent;

public class FireSurgeAttack {
	public void send(Player target, NPC npc) {
		npc.animate(69);
		Position targetPosition = target.getPosition().copy();
		NPC surge = new NPC(13507).spawn(targetPosition.getX(), npc.getPosition().getY(),
			npc.getPosition().getZ(), Direction.SOUTH, 0);
		surge.step(0, -18, StepType.WALK);
		World.startEvent(e -> {
			for(int i = 0; i < 18; i++) {
				if(surge.isRemoved()) {
					break;
				}
				int baseY = surge.getPosition().getRegion().baseY;
				int y = surge.getPosition().getY() - baseY;
				if(y <= 31) {
					surge.remove();
					break;
				}
				if(surge.getPosition().distance(target.getPosition()) < 1) {
					target.hit(new Hit().randDamage(35, 50));
					target.graphics(157);
					surge.remove();
					break;
				} else if(surge.getPosition().distance(targetPosition) < 1) {
					surge.remove();
				}
				e.delay(1);
			}
		});
	}
}
