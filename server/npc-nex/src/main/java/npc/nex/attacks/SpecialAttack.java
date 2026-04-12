package npc.nex.attacks;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import npc.nex.attacks.impl.spec.*;
import npc.nex.attacks.impl.spec.*;
import npc.nex.attacks.impl.std.DragAttack;
import npc.nex.attacks.impl.std.ShadowAttack;
import npc.nex.modes.Forms;
import npc.nex.modes.Phase;
import npc.nex.scripts.NexCombat;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
public class SpecialAttack {

	public boolean forceShadowSmash = false;
	public boolean forceBloodSiphonAttack = false;
	public boolean forceBloodSiphonHeal = false;
	public boolean forceContainmentAttack = false;
	public boolean forceSoulSplitAttackWithReflect = false;
	public boolean forceSoulSplitAttackOnly = false;
	public boolean forceChokeAttack = false;
	public boolean forceDeflectMelee = false;
	public boolean forceDragAttack = false;
	public boolean forceBloodSacrificeAttack = false;
	public boolean forceIcePrisonAttack = false;

	public SpecialAttack decideSpecialAttack(NexCombat nexCombat) {
		switch (nexCombat.getPhase()) {
			case SMOKE_LOCKED:
			case SMOKE:
				int roll = Random.get(1, 2);
				if (roll == 1) {
					forceChokeAttack = true;
				} else {
					forceDragAttack = true;
				}
				//smokeDash();
				break;
			case SHADOW_LOCKED:
			case SHADOW:
				forceShadowSmash = true;
				break;
			case BLOOD_LOCKED:
			case BLOOD:
				int theySeeMeRolling = Random.get(1, 2);
				// if (theySeeMeRolling == 1) {
				// forceBloodSiphonAttack = true;
				// } else {
				// forceBloodSacrificeAttack = true;
				// }
				break;
			case ICE_LOCKED:
			case ICE:
				int iceRoll = Random.get(1, 2);
				if (iceRoll == 1) {
					// forceContainmentAttack = true;
				} else {
					// forceIcePrisonAttack = true;
				}
				break;
			case ZAROS:
				int rng = Random.get(1, 2);
				if (rng == 1) {
					forceSoulSplitAttackOnly = true;
				} else if (rng == 2) {
					forceDeflectMelee = true;
				}
				break;
		}
		return this;
	}

	public boolean invoked(NexCombat nexCombat) {
		var phase = nexCombat.getPhase();
		if (forceShadowSmash && (phase == Phase.SHADOW || phase == Phase.SHADOW_LOCKED)) {
			new ShadowAttack().invoke(nexCombat.getTarget().player, nexCombat);
			forceShadowSmash = false;
			return true;
		}
		if (forceBloodSiphonAttack && (phase == Phase.BLOOD || phase == Phase.BLOOD_LOCKED)) {
			new BloodSiphonAttack().invoke(nexCombat.getTarget().player, nexCombat);
			forceBloodSiphonAttack = false;
			return true;
		}
		if (forceBloodSiphonHeal && (phase == Phase.BLOOD || phase == Phase.BLOOD_LOCKED)) {
			BloodSiphonAttack.bloodSiphonHeal(nexCombat);
			forceBloodSiphonHeal = false;
			return true;
		}
		if (forceBloodSacrificeAttack && (phase == Phase.BLOOD || phase == Phase.BLOOD_LOCKED)) {
			new BloodSscrificeAttack().invoke(nexCombat.getTarget().player, nexCombat);
			forceBloodSacrificeAttack = false;
			return true;
		}
		if (forceContainmentAttack && (phase == Phase.ICE || phase == Phase.ICE_LOCKED)) {
			new ContainmentAttack().invoke(nexCombat.getTarget().player, nexCombat);
			forceContainmentAttack = false;
			return true;
		}
		if (forceIcePrisonAttack && (phase == Phase.ICE || phase == Phase.ICE_LOCKED)) {
			new IcePrisonAttack().invoke(nexCombat.getTarget().player, nexCombat);
			forceIcePrisonAttack = false;
			return true;
		}
		if (forceSoulSplitAttackWithReflect && phase == Phase.ZAROS) {
			new SoulSplitAttack(true).invoke(nexCombat.getTarget().player, nexCombat);
			forceSoulSplitAttackWithReflect = false;
			return true;
		}
		if (forceSoulSplitAttackOnly && phase == Phase.ZAROS) {
			new SoulSplitAttack(false).invoke(nexCombat.getTarget().player, nexCombat);
			forceSoulSplitAttackOnly = false;
			return true;
		}
		if (forceChokeAttack && (phase == Phase.SMOKE || phase == Phase.SMOKE_LOCKED)) {
			new ChokeAttack().invoke(nexCombat.getTarget().player, nexCombat);
			forceChokeAttack = false;
			return true;
		}
		if (forceDragAttack && (phase == Phase.SMOKE || phase == Phase.SMOKE_LOCKED)) {
			new DragAttack().invoke(nexCombat.getTarget().player, nexCombat);
			forceDragAttack = false;
			return true;
		}
		if (forceDeflectMelee && phase == Phase.ZAROS) {
			deflectMeleeAttack(nexCombat);
			forceDeflectMelee = false;
			return true;
		}
		return false;
	}

	public static void deflectMeleeAttack(NexCombat combat) {
		if (combat.getNpc().getId() != Forms.DEFAULT_NEX.getNpcId())
			return;

		combat.getNpc().transform(Forms.REFLECT_MELEE_NEX.getNpcId());
		World.startEvent(event -> {
			event.delay(18);
			combat.getNpc().transform(Forms.DEFAULT_NEX.getNpcId());
		});
	}
}
