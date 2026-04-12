package com.reasonps.dominion.bosses.dagannoth_kings;

import com.reasonps.dominion.bosses.dagannoth_kings.kings.EchoDagannothPrimeCombat;
import com.reasonps.dominion.bosses.dagannoth_kings.kings.EchoDagannothRexCombat;
import com.reasonps.dominion.bosses.dagannoth_kings.kings.EchoDagannothSupremeCombat;
import com.reasonps.dominion.rooms.EchoDagannothCave;
import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Projectile;

import java.util.Objects;

import static core.api.ReasonUtils.pos;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-28
 */
public abstract class EchoDagannothKingCombatCombat extends NPCCombat implements IEchoDagannothKingCombat {
	private static final Projectile spitProjectile =
		new Projectile(1644, 30, 0, 15, 56, 10, 15, 64)
			.regionBased();

	protected int damageTaken = 0;
	protected boolean isEnraged = false;

	private final int DAMAGE_THRESHOLD = 1_100;

	private EchoDagannothCave instance;

	public void assignInstance(EchoDagannothCave instance) {
		this.instance = instance;
	}

	/**
	 * Creates a {@link HitListener} for managing specific actions during the combat of an NPC boss.
	 * This method is used to track post-damage actions, such as deducting health, modifying NPC health,
	 * and switching prayers based on a damage threshold.
	 *
	 * @return A configured {@link HitListener} instance that executes the specified actions after damage is dealt.
	 */
	protected HitListener getHitListener() {
		return new HitListener()
			.preDamage(hit -> {
				if (getNpc().overheadPrayer() == NPC.DefaultHeadIconIndex.MageRangeMelee)
					hit.block();
			})
			.postDamage(hit -> {
				damageTaken += hit.damage;
				// deduct health from the instance's health pool
				instance.deductHealth(hit.damage);
				if (hit.attacker != null && hit.attacker.isPlayer()) {
					if (!hit.attacker.player.getHealthHud().isOpened())
						hit.attacker.player.getHealthHud().open(true, npc.getId(), npc.getMaxHp());
					hit.attacker.player.getHealthHud().updateValue(npc.getHp());
				}
				if (!isEnraged && damageTaken >= DAMAGE_THRESHOLD) {
					damageTaken -= DAMAGE_THRESHOLD;
					instance.getSpinolyps().stream().filter(Objects::nonNull).forEach(spinolyp -> {
						if (hit.attacker == null || !hit.attacker.isPlayer()) return;
						var targetLocation = instance.getRandomPosition(hit.attacker.player);
						spinolyp.face(targetLocation);
						spinolyp.animate(spinolyp.getCombat().getInfo().attack_animation);
						// shoot 2-3 projectiles at/around the targetLocation
						for (int i = 0; i < Random.get(1, 2); i++) {
							var poolLocation = targetLocation.translated(Random.get(-1, 1), Random.get(-1, 1));
							var delay = spitProjectile.send(spinolyp, poolLocation);
							World.startEvent(World.getTicks(delay)-1, event -> {
								if (!instance.getPoisonPools().contains(poolLocation))
									instance.getPoisonPools().add(poolLocation);
							}).setCancelCondition(() -> spinolyp.isRemoved() || instance.host == null);
						}
					});
					switchPrayers();
				}
			});
	}

	private void switchPrayers() {
		var kingToToggleOnOptional = getKingWithNoPrayers(instance);
		if (kingToToggleOnOptional.isEmpty()) return;
		var kingToToggleOn = kingToToggleOnOptional.get();
		if (kingToToggleOn.getId() == 17008) {
			((EchoDagannothSupremeCombat) instance.getEchoSupreme().getCombat()).togglePrayAgainstAll();
			((EchoDagannothRexCombat) instance.getEchoRex().getCombat()).togglePrayAgainstAll();
		}
		else if (kingToToggleOn.getId() == 17009) {
			((EchoDagannothPrimeCombat) instance.getEchoPrime().getCombat()).togglePrayAgainstAll();
			((EchoDagannothSupremeCombat) instance.getEchoSupreme().getCombat()).togglePrayAgainstAll();
		}
		else if (kingToToggleOn.getId() == 17010) {
			((EchoDagannothRexCombat) instance.getEchoRex().getCombat()).togglePrayAgainstAll();
			((EchoDagannothPrimeCombat) instance.getEchoPrime().getCombat()).togglePrayAgainstAll();
		}
	}

	protected DeathListener getDeathListener() {
		return (e, k, h) -> {
			k.player.getHealthHud().close();
			k.player.getCombat().reset();
			getNpc().localNpcs().stream().filter(Objects::nonNull).forEach(npc -> {
				npc.lock(LockType.FULL_NULLIFY_DAMAGE);
				npc.setHp(0);
				npc.animate(info.death_animation);

				if (!instance.getPoisonPools().isEmpty())
					instance.getPoisonPools().clear();

				instance.getSpinolyps().forEach(NPC::remove);

				npc.startEvent(event -> {
					event.delay(info.death_ticks);
					npc.remove();
				});
			});
			new NPC(17014).spawn(pos(instance.map, 32, 32), Direction.NORTH);
		};
	}

	@Override
	public void init() {
		npc.hitsUpdate.hpbarId = 1;
		getNpc().hitListener = getHitListener();
		getNpc().deathStartListener = getDeathListener();
		getNpc().deathEndListener = (e, k, h) -> getNpc().remove();
	}

	@Override
	public boolean allowRespawn() {
		return false;
	}

	@Override
	public void process() {
		if (instance != null)
			// Update everyone's health
			getNpc().setHp(Math.max(0, instance.getHealthPool()));

		// check if the Kings are enraged
		if (!isEnraged && getNpc().getHp() <= (getNpc().getMaxHp() * 0.2))
			isEnraged = true;

		// Kings will drop their prayers on enraged phase
		if (isEnraged && getNpc().overheadPrayer() != null)
			getNpc().removeHeadIcon();
	}

	@Override
	public void follow() {
		follow(getAttackBoundsRange());
	}

	@Override
	public int getAttackBoundsRange() {
		return 32;
	}

	@Override
	public int getAggressionRange() {
		return 32;
	}
}
