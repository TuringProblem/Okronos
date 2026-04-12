package io.ruin.model.activities.raids.tob.dungeon.boss.maiden.attacks;


import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Projectile;

public class TornadoAttack {

   /* private final int TORNADO_ANIM = 8092;
    private final Projectile tornadoProjectile = new Projectile(1577, 20, 31, 20, 15, 12, 15, 10);

    public TornadoAttack(Entity target, NPC npc) {
        tornadoAttack(target, npc);
    }

    private void tornadoAttack(Entity target, NPC npc) {
        npc.animate(TORNADO_ANIM);
        int maxDamage = 36;

        Player closest = null;
        double closestDistance = Double.MAX_VALUE;
        for (Player p : npc.getPosition().getRegion().players) {
            double distance = npc.getPosition().center(npc.getSize()).distance(p.getPosition());
            if (distance < closestDistance) {
                closest = p;
                closestDistance = distance;
            }
        }
        if (closest != null) {
            int delay = tornadoProjectile.send(npc, target);
            npc.startEvent(event -> event.delay(3));
            Hit hit = new Hit(npc, AttackStyle.MAGIC).randDamage(maxDamage).clientDelay(delay).ignorePrayer();
            target.hit(hit);
        }
    }*/
}
