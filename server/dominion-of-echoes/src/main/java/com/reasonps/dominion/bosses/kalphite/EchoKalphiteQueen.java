package com.reasonps.dominion.bosses.kalphite;

import com.reasonps.dominion.bosses.kalphite.attacks.MagicAttack;
import com.reasonps.dominion.bosses.kalphite.attacks.MeleeAttack;
import com.reasonps.dominion.bosses.kalphite.attacks.RangedAttack;
import com.reasonps.dominion.bosses.kalphite.specials.PhaseOneSpecial;
import com.reasonps.dominion.bosses.kalphite.specials.PhaseTwoSpecial;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.stat.StatType;

import static com.reasonps.dominion.bosses.kalphite.Constants.*;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-16
 */
public class EchoKalphiteQueen extends NPCCombat {

	public boolean defenceLowered = false;
	public boolean usedNonFlail = false;
	public boolean performingSpecial = false;

	private int specialAttackProgression = 0;

	private final int THRESHOLD = 9;

	@Override
	public void startDeath(Hit killHit) {
		if (currentForm() == Form.FIRST) {
			npc.resetAnimation();
			World.startEvent(event -> {
				npc.lock();
				event.delay(1);
				npc.setHp(npc.getMaxHp());
				npc.transform(Form.SECOND.getNpcId());
				npc.lock(LockType.FULL_NULLIFY_DAMAGE);
				npc.animate(TRANSFORM_ANIM);
				npc.graphics(TRANSFORM_GFX);
				event.delay(11);
				npc.unlock();
				performingSpecial = false;
				npc.setHeadIcon(NPC.DefaultHeadIconIndex.ProtectFromMelee);

				var attacker = killHit.attacker;
				if (attacker != null && attacker.isPlayer()) {
					attacker.player.getHealthHud().updateMaxValue(npc.getMaxHp());
					attacker.player.getHealthHud().updateValue(npc.getMaxHp());
				}
			});
		} else if (currentForm() == Form.SECOND) {
			super.startDeath(killHit);
		}
	}

	@Override
	public void init() {
		npc.hitsUpdate.hpbarId = 1;
		npc.hitListener = new HitListener()
				.preDefend(this::preDefend)
				.postDamage(hit -> {
					if (hit.attacker != null && hit.attacker.isPlayer()) {
						if (!hit.attacker.player.getHealthHud().isOpened())
							hit.attacker.player.getHealthHud().open(true, npc.getId(), npc.getMaxHp());
						hit.attacker.player.getHealthHud().updateValue(npc.getHp());
					}
				});
		npc.attackNpcListener = (p, npc1, b) -> !npc1.isLockedExclude(LockType.MOVEMENT);
	}

	@Override
	public boolean allowRespawn() {
		return false;
	}

	private void preDefend(Hit hit) {
		if (hit.attackStyle == null)
			return;
		if (!(hit.attackStyle.isMelee() &&
				hit.attacker != null &&
				hit.attacker.isPlayer() &&
				hit.attacker.player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
				FLAILS.contains(hit.attacker.player.getEquipment().get(Equipment.SLOT_WEAPON).getId())))
			usedNonFlail = true;

		if (currentForm() == Form.FIRST && (hit.attackStyle.isRanged() || hit.attackStyle.isMagic()))
			hit.block();
		else if (currentForm() == Form.SECOND && (hit.attackStyle.isMelee()))
			hit.block();
	}

	@Override
	public void follow() {
		follow(currentForm().equals(Form.FIRST) ? 1 : 3);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(10))
			return false;
		if (performingSpecial)
			return true;
		// every attack, progress the "SpecialAttackProgression"
		specialAttackProgression++;
		// if the build-up is at/above the threshold, perform a special and reset the progress
		if (specialAttackProgression >= THRESHOLD) {
			specialAttackProgression = 0;
			performingSpecial = true;
			npc.forceText("Bzzzz!");
			if (currentForm().equals(Form.FIRST))
				new PhaseOneSpecial().invoke(target.player, this);
			else if (currentForm().equals(Form.SECOND))
				new PhaseTwoSpecial().invoke(target.player, this);
			return true;
		}
		if (currentForm().equals(Form.FIRST)) {
			if (withinDistance(1)) {
				var meleeAttack = new java.util.Random().nextBoolean();
				if (meleeAttack)
					new MeleeAttack().invoke(target.player, this);
				else
					new MagicAttack().invoke(target.player, this);
			} else
				new RangedAttack().invoke(target.player, this);
			return true;
		} else if (currentForm().equals(Form.SECOND)) {
			var ranged = new java.util.Random().nextBoolean();
			if (ranged)
				new RangedAttack().invoke(target.player, this);
			else
				new MagicAttack().invoke(target.player, this);
			return true;
		}
		return false; // something went wrong if we got here...
	}

	@Override
	public void process() {
		defenceLowered = npc.getCombat().getStat(StatType.Defence).currentLevel < 300;
	}

	public Form currentForm() {
		return npc.getId() == Form.FIRST.getNpcId() ? Form.FIRST : Form.SECOND;
	}

	@Override
	public int getAttackBoundsRange() {
		return 32;
	}
}
