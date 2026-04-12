package io.ruin.model.activities.perktree.perksets;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.perktree.PlayerPerkSet;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;

public class BleedThemDry extends PlayerPerkSet {
	@Override
	public String getPerkSetName() {
		return "Bleed Them Dry";
	}

	@Override
	public String getPerkSetDescription() {
		return "Bleed Effect";
	}

	@Override
	public String getPerkSetEffect() {
		return "When this perk set is active you will have a chance to activate a bleed effect on your opponent, the damage of the bleed will be based on the perk set level.<br><br>" +
			"At level 1 you will have a " + getBleedChance(1) + "% chance to activate a bleed effect on your opponent.<br><br>" +
			"At level 2 you will have a " + getBleedChance(2) + "% chance to activate a bleed effect on your opponent.<br><br>" +
			"At level 3 you will have a " + getBleedChance(3) + "% chance to activate a bleed effect on your opponent.<br><br>" +
			"At level 4 you will have a " + getBleedChance(4) + "% chance to activate a bleed effect on your opponent.<br><br>" +
			"At level 5 you will have a " + getBleedChance(5) + "% chance to activate a bleed effect on your opponent.<br><br>"
			;
	}

	private double getBleedChance(int level) {
		return 2.5 + (1.5 * level);
	}

	public double getBleedChance() {
		return (2.5 + (1.5 * getLevel())) / 100;
	}

	public void bleedEffect(Player attacker, NPC target) {
		if (!attacker.isPlayer() || attacker.player.bleedActive)
			return;
		World.startEvent(e -> {
			int totalBleedHits = Random.get(5, 10 + (getLevel() * 2));
			attacker.player.bleedActive = true;
			e.delay(3);
			for (int i = 0; i < totalBleedHits; i++) {
				target.hit(new Hit().randDamage(getLevel(), getLevel() * 3));
				e.delay(2);
				if (i == totalBleedHits - 1)
					attacker.player.bleedActive = false;
			}
		});
	}
}
