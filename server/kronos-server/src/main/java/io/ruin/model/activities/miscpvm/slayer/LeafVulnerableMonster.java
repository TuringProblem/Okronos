package io.ruin.model.activities.miscpvm.slayer;

import io.ruin.cache.ObjType;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.skills.magic.spells.modern.MagicDart;

import java.util.Arrays;

public class LeafVulnerableMonster extends NPCCombat {// Turoths and Kurasks

	public static void register() {
		for (int i : Arrays.asList(4158, 11902, 20727, 4160, 11875, 21316)) // leaf-bladed/broad items
			ObjType.get(i).leafBladed = true;
	}

	@Override
	public void init() {
		npc.hitListener = new HitListener().preDefend(this::preDefend);
	}

	private void preDefend(Hit hit) {
		boolean block = false;
		if (hit.attacker != null && hit.attacker.player != null && hit.attackStyle != null) {
			if (hit.attackStyle.isMelee() && (hit.attackWeapon == null || !hit.attackWeapon.leafBladed)) {
				block = true;
			} else if (hit.attackStyle.isRanged() && (hit.rangedAmmo == null || !hit.rangedAmmo.leafBladed)) {
				block = true;
			} else if (hit.attackStyle.isMagic() || hit.attackSpell == MagicDart.INSTANCE) {                              // TODO slayer dart spell!!!!
				block = false;
			}
			if (block) {
				hit.block();
				hit.attacker.player.sendMessage("This monster is only vulnerable to leaf-bladed melee weapons and broad ammunition.");
			}
		}
	}

	@Override
	public void follow() {
		follow(1);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(1))
			return false;
		basicAttack();
		return true;

	}

	@Override
	public void process() {

	}
}
