package io.ruin.model.skills.crafting;

import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.PerkTaskHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ArtsnCrafts;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.skill.SkillDialogue;
import io.ruin.model.inter.dialogue.skill.SkillItem;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.stat.StatType;

public enum Battlestaff {

	WATER(571, 1395, 100.0, 7531, 1370, 54),
	EARTH(575, 1399, 112.5, 7531, 1371, 58),
	FIRE(569, 1393, 125.0, 7531, 1372, 62),
	AIR(573, 1397, 137.5, 7531, 306, 66);

	public final int orbId, staffId, animationId, gfxId, levelReq;
	public final double exp;

	Battlestaff(int orbId, int staffId, double exp, int animationId, int gfxId, int levelReq) {
		this.orbId = orbId;
		this.staffId = staffId;
		this.exp = exp;
		this.animationId = animationId;
		this.gfxId = gfxId;
		this.levelReq = levelReq;
	}

	private void make(Player player, Item staff, Item orb) {
		staff.setId(staffId);
		player.animate(animationId);
		player.graphics(gfxId);
		orb.remove();
		double xpToGain = exp;
		float multiplier = 1;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ARTS_N_CRAFTS)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ARTS_N_CRAFTS);
			ArtsnCrafts c = (ArtsnCrafts) player.getPlayerPerkHandler().
				getActivePerks(player).get(perkIndex).getPerk(player);
			multiplier += c.getExperienceBoost();
		}
		xpToGain *= multiplier;
		player.getStats().addXp(StatType.Crafting, xpToGain, true);
		PerkTaskHandler.handleGatherResource(player, staffId, 1);
		DailyTasks.handleItemObtained(player, staffId, StatType.Crafting);
	}

	private static final int BATTLE_STAFF = 1391;

	public static void register() {
		for (Battlestaff battlestaff : values()) {
			SkillItem item = new SkillItem(battlestaff.staffId).addAction((player, amount, event) -> {
				while (amount-- > 0) {
					Item staff = player.getInventory().findItem(BATTLE_STAFF);
					if (staff == null)
						return;
					Item orb = player.getInventory().findItem(battlestaff.orbId);
					if (orb == null)
						return;
					if (player.getInventory().hasMultiple(staff.getId(), orb.getId())) {
						battlestaff.make(player, staff, orb);
						event.delay(3);
						continue;
					}
					battlestaff.make(player, staff, orb);
					break;
				}
			});
			ItemItemAction.register(battlestaff.orbId, BATTLE_STAFF, (player, orb, staff) -> {
				if (!player.getStats().check(StatType.Crafting, battlestaff.levelReq, "do craft this"))
					return;
				if (player.getInventory().hasMultiple(orb.getId(), staff.getId())) {
					SkillDialogue.make(player, item);
					return;
				}
				battlestaff.make(player, orb, staff);
			});
		}
	}
}
