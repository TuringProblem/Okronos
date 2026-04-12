package io.ruin.model.activities.bosses.balanceelemental;
import io.ruin.model.activities.bosses.balanceelemental.attacks.MagicFormAttack;
import io.ruin.model.activities.bosses.balanceelemental.attacks.MeleeFormAttack;
import io.ruin.model.activities.bosses.balanceelemental.attacks.RangeFormAttack;
import io.ruin.model.activities.bosses.balanceelemental.attacks.specialattacks.IceSpikeSpecial;
import io.ruin.model.activities.bosses.balanceelemental.attacks.specialattacks.PrayerDrainSpecial;
import io.ruin.model.activities.bosses.balanceelemental.attacks.specialattacks.RockFallSpecial;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.utility.Random;
import io.ruin.utility.TickDelay;


public class BalanceElemental extends NPCCombat {
	TickDelay changeFormDelay = new TickDelay();
	TickDelay specialDelay = new TickDelay();
	int damageSincePhaseChange = 0;
	@Override
	public void init() {
		npc.hitListener = new HitListener().preDamage(this::preDamage).postDamage(this::postDamage);
	}

	private void postDamage(Hit hit) {
		damageSincePhaseChange += hit.damage;
		if(damageSincePhaseChange >= 400) {
			damageSincePhaseChange = 0;
			if(changeFormDelay.remaining() < 1) {
				changeFormDelay.delay(18);
				Phase.changeForm(npc, getPhase());
			}
		}
	}

	private void preDamage(Hit hit) {
		if(hit.attackStyle != null) {
			if(getPhase() == Phase.MELEE && !hit.attackStyle.isMagic())
				hit.block();
			else if(getPhase() == Phase.RANGED && !hit.attackStyle.isMelee())
				hit.block();
			else if(getPhase() == Phase.MAGIC && !hit.attackStyle.isRanged())
				hit.block();
		}
	}

	@Override
	public void follow() {
		follow(npc.getId() == Phase.MELEE.npcId ? 2 : 7);
	}


	private Phase getPhase() {
		for(Phase phase : Phase.values()) {
			if(npc.getId() == phase.npcId)
				return phase;
		}
		return null;
	}

	@Override
	public boolean attack() {
		if(Random.get(4) == 0) {
			if(changeFormDelay.remaining() < 1) {
				changeFormDelay.delay(18);
				damageSincePhaseChange = 0;
				Phase.changeForm(npc, getPhase());
			}
		}
		if(Random.get(6) == 0) {
			performRandomSpecial();
		}
		if(npc.getId() == Phase.MELEE.npcId) {
			new MeleeFormAttack().attack(npc, getPhase());
		} else if(npc.getId() == Phase.MAGIC.npcId) {
			new MagicFormAttack().attack(npc, getPhase());
		} else {
			new RangeFormAttack().attack(npc, getPhase());
		}
		return true;
	}

	@Override
	public void process() {

	}

	private void performRandomSpecial() {
		if(specialDelay.remaining() > 0)
			return;
		specialDelay.delay(8);
		int random = Random.get(2);
		if(random == 0) {
			new RockFallSpecial().start(npc);
		} else if(random == 1) {
			new IceSpikeSpecial().start(npc);
		} else {
			new PrayerDrainSpecial().start(npc);
		}
	}
	@Override
	public int getAggressionRange() {
		return 20;
	}

	@Override
	public int getAttackBoundsRange() {
		return 20;
	}

}
