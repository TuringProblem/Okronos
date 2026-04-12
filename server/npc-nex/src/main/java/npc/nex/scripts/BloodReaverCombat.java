package npc.nex.scripts;

import npc.nex.old.Nex;
import io.ruin.cache.NPCType;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.map.Projectile;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
public class BloodReaverCombat extends NPCCombat {
	public static final int LEVEL_161 = 11294;
	private final Projectile PROJECTILE = new Projectile(
			374,
			15,
			30,
			51,
			56,
			10,
			16,
			64).regionBased();


	public static void register() {
		NPCType.registerCombat(BloodReaverCombat.class, 11294, 11293);
	}

	boolean NEX_SPAWN = false;
	private NPC nex;


	@Override
	public void init() {
		NEX_SPAWN = npc.getPosition().inBounds(Nex.NEX_BOUNDS);
		if (NEX_SPAWN) {
			for (NPC n : npc.localNpcs()) { // find reference to shield
				if (n.getDef().name.equalsIgnoreCase("nex")) {
					nex = n;
					break;
				}
			}
		}
	}

	@Override
	public void follow() {
		follow(8);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(10))
			return false;
		// If this reaver spawns in nex bounds make sure it only attacks other players in nex bounds.
		if (NEX_SPAWN && !target.getPosition().inBounds(Nex.NEX_BOUNDS)) {
			return false;
		}
		npc.animate(info.attack_animation);
		npc.publicSound(106, 1, 0);
		int delay = PROJECTILE.send(npc, target);
		Hit hit = new Hit(npc, AttackStyle.MAGIC, null)
				.randDamage(20)
				.clientDelay(delay);
		hit.postDamage(t -> {
			if (hit.damage > 0) {
				if (nex != null) {
					nex.incrementHp(hit.damage);
				}
				t.graphics(373, 124, 0);
				npc.incrementHp((int) (hit.damage * 0.25));
				t.publicSound(104, 1, 0);
			} else {
				t.graphics(85, 124, 0);
				t.publicSound(227, 1, 0);
				hit.hide();
			}
		});
		target.hit(hit);
		return true;
	}

	@Override
	public void process() {}

	@Override
	public int getAggressionRange() {
		return 40;
	}

	@Override
	public boolean allowRespawn() {
		return false;
	}
}
