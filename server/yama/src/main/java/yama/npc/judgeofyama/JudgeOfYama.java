package yama.npc.judgeofyama;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import lombok.Getter;
import lombok.Setter;
import yama.npc.judgeofyama.attacks.FireSurgeAttack;
import yama.npc.judgeofyama.attacks.FireballAttack;

public class JudgeOfYama extends NPCCombat {
	@Setter String targetName;
	@Getter @Setter int protection = 1;
	@Override
	public void init() {

	}

	@Override
	public void follow() {

	}

	@Override
	public boolean attack() {
		Player player = World.getPlayer(targetName);
		if(player == null)
			return false;
		int baseY = player.getPosition().getRegion().baseY;
		int y = player.getPosition().getY() - baseY;
		if(y > 46) {
			new FireballAttack().send(player, npc);
		}
		return true;
	}

	int ticksTilSurge = 5;

	@Override
	public void process() {
		if(npc.isRemoved())
			return;
		Player player = World.getPlayer(targetName);
		if(player == null)
			return;
		int baseY = player.getPosition().getRegion().baseY;
		int y = player.getPosition().getY() - baseY;
		if(ticksTilSurge-- == 0) {
			ticksTilSurge = 5;
			if(y <= 46) {
				new FireSurgeAttack().send(player, npc);
			}
		}
	}

	public void preDamage(Hit hit) {
		if(protection == 0) {
			if(hit.attackStyle != null && (hit.attackStyle.isMagic() || hit.attackStyle.isRanged())) {
				hit.block();
			}
			if(hit.attackStyle != null && hit.attackStyle.isMelee()) {
				hit.damage = hit.maxDamage;
				protection = 1;
				npc.setHeadIcon(NPC.DefaultHeadIconIndex.RangeAndMelee);
			}
		}
		else if(protection == 1) {
			if(hit.attackStyle != null && (hit.attackStyle.isMelee() || hit.attackStyle.isRanged())) {
				hit.block();
			}
			if(hit.attackStyle != null && hit.attackStyle.isMagic()) {
				hit.damage = hit.maxDamage;
				protection = 0;
				npc.setHeadIcon(NPC.DefaultHeadIconIndex.RangeAndMage);
			}
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
