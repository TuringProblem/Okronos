package yama.npc.judgeofyama.attacks;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;

public class FireballAttack {
	Projectile FIRE_BALL_PROJECTILE = new Projectile(2855, 150, 0, 0, 135, 0, 0, 0);
	public void send(Player player, NPC npc) {
		World.startEvent(e -> {
			npc.animate(69);
			Position targetPosition = player.getPosition().copy();
			int delay = FIRE_BALL_PROJECTILE.send(npc, targetPosition);
			e.delay(World.getTicks(delay) + 1);
			if(player.getPosition().distance(targetPosition) < 1) {
				player.hit(new Hit(npc).randDamage(35, 50));
			}
			World.sendGraphics(2856, 0, 0, targetPosition);
		});
	}
}
