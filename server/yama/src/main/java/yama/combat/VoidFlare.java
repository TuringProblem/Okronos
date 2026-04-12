package yama.combat;

import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;

import java.util.ArrayList;
import java.util.List;

public class VoidFlare extends NPCCombat {

	private int timeAlive = 0;
	NPC yama;
	Yama yamaCombat;
	@Override
	public void init() {
		for(NPC npc : getNpc().localNpcs()) {
			if(npc.getName().equalsIgnoreCase("Yama")) {
				yama = npc;
				yamaCombat = (Yama) yama.getCombat();
			}
		}
		npc.hitListener = new HitListener().preDamage(this::preDamage);
	}

	private void preDamage(Hit hit) {
		if(npc.getHp() == npc.getMaxHp()) {
			if(hit.attackStyle.isMagic() || hit.attackStyle.isRanged()) {
				hit.damage = hit.maxDamage;
			}
		}
	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		return false;
	}

	@Override
	public void process() {
		if(yama == null || yama.getHp() < 1 || yamaCombat.inIntermission) {
			npc.remove();
			return;
		}
		npc.hitsUpdate.add(31, timeAlive++, 20);
		if(timeAlive >= 20 && npc.getHp() > 0) {
			int hp = npc.getHp();
			yama.hit(new Hit(HitType.HEAL).fixedDamage(hp));
			Yama yc = (Yama) yama.getCombat();
			if(yc.isInIntermission()) {
				npc.remove();
				return;
			}
			for(Player player : npc.getPosition().getRegion().players) {
				if(npc.getId() == 17028 && yc.getShadowImmunityTicks() < 1) {
					player.hit(new Hit().fixedDamage(hp / npc.getPosition().getRegion().players.size() > 1 ? 14 : 8));
				}
				else if(npc.getId() == 14179 && yc.getFireImmunityTicks() < 1) {
					player.hit(new Hit().fixedDamage(hp / npc.getPosition().getRegion().players.size() > 1 ? 14 : 8));
				}
			}
			npc.remove();
		}
	}
}
