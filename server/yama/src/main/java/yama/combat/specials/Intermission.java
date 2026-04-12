package yama.combat.specials;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Position;
import yama.combat.Yama;
import yama.npc.YamaThrone;
import yama.npc.judgeofyama.JudgeOfYama;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Intermission {

	public void start(NPC npc) {
		List<NPC> judges = new ArrayList<>();
		World.startEvent(e -> {
			npc.forceText("You bore me.");
			npc.animate(12147);
			e.delay(2);
			List<Position> teleportPositions = getTeleportPositions(npc);
			List<Position> judgeSpawnPositions = getJudgeSpawnPositions(npc);
			for(int i = 0; i < npc.getPosition().getRegion().players.size(); i++) {
				Player player = npc.getPosition().getRegion().players.get(i);
				player.getMovement().teleport(teleportPositions.get(i).x(), teleportPositions.get(i).y(), teleportPositions.get(i).z());
				NPC judge = new NPC(14180).spawn(judgeSpawnPositions.get(i).x(), judgeSpawnPositions.get(i).y(), judgeSpawnPositions.get(i).z());
				if(npc.getPosition().getRegion().players.size() > 1)
					YamaThrone.scaleNPC(judge);
				judge.setHeadIcon(NPC.DefaultHeadIconIndex.RangeAndMelee);
				player.getHealthHud().close();
				player.getHealthHud().open(true, judge.getId(), judge.getMaxHp());
				judges.add(judge);
				JudgeOfYama judgeOfYama = (JudgeOfYama) judge.getCombat();
				judgeOfYama.setTargetName(player.getName());
				player.getHealthHud().open(true, judge.getId(), judge.getMaxHp());
			}
			judges.getFirst().deathEndListener = (entity, killer, killHit) -> {
				Yama yama = (Yama) npc.getCombat();
				yama.endIntermission();
				judges.forEach(NPC::remove);
			};
			if(judges.size() > 1) {
				judges.get(0).hitListener = new HitListener().postDamage((hit) -> {
					judges.get(1).hit(new Hit().fixedDamage(hit.damage));
					if (hit.attacker != null && hit.attacker.isPlayer()) {
						if(!hit.attacker.player.getHealthHud().isOpened())
							hit.attacker.player.getHealthHud().open(true, judges.get(0).getId(), judges.get(0).getMaxHp());
						hit.attacker.player.getHealthHud().updateValue(judges.get(0).getHp());
					}
				}).preDamage((hit) -> {
					JudgeOfYama judgeOfYama = (JudgeOfYama) judges.get(0).getCombat();
					judgeOfYama.preDamage(hit);
				});
				judges.get(1).hitListener = new HitListener().postDamage((hit) -> {
					judges.get(0).hit(new Hit().fixedDamage(hit.damage));
					if (hit.attacker != null && hit.attacker.isPlayer()) {
						if(!hit.attacker.player.getHealthHud().isOpened())
							hit.attacker.player.getHealthHud().open(true, judges.get(1).getId(), judges.get(1).getMaxHp());
						hit.attacker.player.getHealthHud().updateValue(judges.get(1).getHp());
					}
				}).preDamage((hit) -> {
					JudgeOfYama judgeOfYama = (JudgeOfYama) judges.get(1).getCombat();
					judgeOfYama.preDamage(hit);
				});
			} else {
				judges.getFirst().hitListener = new HitListener().postDamage((hit) -> {
					if (hit.attacker != null && hit.attacker.isPlayer()) {
						if(!hit.attacker.player.getHealthHud().isOpened())
							hit.attacker.player.getHealthHud().open(true, judges.getFirst().getId(), judges.getFirst().getMaxHp());
						hit.attacker.player.getHealthHud().updateValue(judges.getFirst().getHp());
					}
				}).preDamage((hit) -> {
					JudgeOfYama judgeOfYama = (JudgeOfYama) judges.getFirst().getCombat();
					judgeOfYama.preDamage(hit);
				});
			}
			e.delay(3);
			npc.setHidden(true);
			npc.getMovement().teleport(npc.getPosition().getRegion().baseX + 28, npc.getPosition().getRegion().baseY + 28, 0);
		});
	}

	private List<Position> getTeleportPositions(NPC npc) {
		return Arrays.asList(
			new Position(npc.getPosition().getRegion().baseX + 7, npc.getPosition().getRegion().baseY + 29, npc.getPosition().getZ()),
			new Position(npc.getPosition().getRegion().baseX + 57, npc.getPosition().getRegion().baseY + 29, npc.getPosition().getZ())
		);
	}

	private List<Position> getJudgeSpawnPositions(NPC npc) {
		return Arrays.asList(
			new Position(npc.getPosition().getRegion().baseX + 6, npc.getPosition().getRegion().baseY + 49, npc.getPosition().getZ()),
			new Position(npc.getPosition().getRegion().baseX + 56, npc.getPosition().getRegion().baseY + 49, npc.getPosition().getZ())
		);
	}
}
