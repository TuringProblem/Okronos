package io.ruin.model.activities.bosses.balanceelemental;

import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.utility.Random;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Phase {
		MELEE(13529, 11348, 11343), //2872 can be special that makes player move
		RANGED(13530, 11354, 11355),//2941 2947 2976
		MAGIC(13528, 11342, 11349)
	;
	int npcId;
	@Getter int defaultAttackAnim;
	@Getter int secondaryAttackAnim;

	Phase(int npcId, int defaultAttackAnim, int secondaryAttackAnim) {
		this.npcId = npcId;
		this.defaultAttackAnim = defaultAttackAnim;
		this.secondaryAttackAnim = secondaryAttackAnim;
	}


	public static void changeForm(NPC npc, Phase phase) {
		List<Phase> phases = new ArrayList<>(Arrays.asList(Phase.MELEE, Phase.RANGED, Phase.MAGIC));
		if(phase != null)
			phases.remove(phase);
		Phase newPhase = Random.get(phases);
		if (newPhase == Phase.RANGED) {
			npc.setHeadIcon(NPC.DefaultHeadIconIndex.RangeAndMage);
		} else if (newPhase == Phase.MAGIC) {
			npc.setHeadIcon(NPC.DefaultHeadIconIndex.MageAndMelee);
		} else {
			npc.setHeadIcon(NPC.DefaultHeadIconIndex.RangeAndMelee);
		}
		World.startEvent(e -> {
			npc.resetAnimation();
			npc.animate(11344);
			e.delay(1);
			npc.transform(newPhase.npcId);
		});
	}

}
