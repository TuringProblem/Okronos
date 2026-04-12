package npc.nex.attacks.impl.spec;

import npc.nex.attacks.Attack;
import npc.nex.scripts.BloodReaverCombat;
import npc.nex.scripts.NexCombat;
import npc.nex.utils.ZarosUtils;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-04
 */
public class BloodSiphonAttack implements Attack {
	@Override
	public void invoke(Player target, NexCombat combat) {
		bloodSiphonHeal(combat);
		combat.getNpc().forceText(SIPHON);
		combat.getNpc().getPosition().getRegion().players.forEach(p -> {
			if (!p.getPosition().inBounds(combat.ATTACK_BOUNDS))
				return;
			p.sendMessage("Nex: " + SIPHON);
		});
		combat.getNpc().animate(9183);
		combat.damageHealsDelay = SIPHON_DURATION;
		combat.getNpc().lock();

		combat.getNpc().addEvent(e -> {
			e.setCancelCondition(combat::targetIsNotInBossRegion);
			int reaverCount = Math.max(2, combat.getNpc().getPosition().getRegion().players.size() / 10);
			for (int i = 0; i < reaverCount; i++) {
				for (int tries = 0; tries < 64; tries++) {
					Bounds b = new Bounds(combat.getNpc().getSpawnPosition(), reaverCount * 2);
					Position p = b.randomPosition();
					if (p.getX() < 2910) {
						p = new Position(2910, p.getY(), p.getZ());
					}
					Tile t = Tile.get(p);
					if (t == null || t.clipping != 0)
						continue;

					NPC spawn = new NPC(BloodReaverCombat.LEVEL_161).spawn(p);
					combat.getBloodReavers().add(spawn);
					break;
				}
			}
			e.delay(SIPHON_DURATION);
			combat.getNpc().unlock();
		});
	}

	public static void bloodSiphonHeal(NexCombat combat) {
		if (combat.getBloodReavers().isEmpty())
			return;
		int remainingHitpoints = 0;
		for (var bloodReaver : combat.getBloodReavers()) {
			if (bloodReaver.isLocked())
				continue;
			remainingHitpoints += bloodReaver.getHp();
		}

		ZarosUtils.removeNpc(combat.getBloodReavers());
		combat.getBloodReavers().clear();

		if (remainingHitpoints == 0)
			return;

		combat.getNpc().hit(new Hit(HitType.HEAL)
			.fixedDamage(remainingHitpoints)
			.ignoreDefence()
			.ignorePrayer());
	}
}
