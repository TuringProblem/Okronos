package io.ruin.model.skills.smithing;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.PerkTaskHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.DancingInTheYields;
import io.ruin.model.activities.perktree.perks.SmithingDynasty;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.item.Item;
import io.ruin.model.stat.StatType;

import java.util.ArrayList;

public class SmithItem {

	protected static final SmithItem NONE = null;

	public final int level;

	public final double xp;

	public final int makeId, makeAmount;

	public final int barReq;

	public SmithItem(int level, double xp, int makeId, int makeAmount, int barReq) {
		this.level = level;
		this.xp = xp;
		this.makeId = makeId;
		this.makeAmount = makeAmount;
		this.barReq = barReq;
	}

	public void make(Player player, int amount) {
		player.closeInterface(ToplevelComponent.MAINMODAL);
		player.getPacketSender().sendClientScript(6123);
		if (!player.getStats().check(StatType.Smithing, level, "make that"))
			return;
		player.startEvent(e -> {
			int made = 0;
			while (true) {
				ArrayList<Item> bars = player.getInventory().findItems(player.smithBar.itemId, barReq);
				if (bars == null) {
					if (made == 0)
						player.sendMessage("You don't have enough bars to make that.");
					else
						player.sendMessage("You don't have enough bars to make anymore.");
					return;
				}
				if (made % 2 == 0)
					player.animate(898);
				boolean keepBars = false;
				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SMITHING_DYNASTY)) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.SMITHING_DYNASTY);
					SmithingDynasty c = (SmithingDynasty) player.getPlayerPerkHandler().
						getActivePerks(player).get(perkIndex).getPerk(player);
					assert c != null;
					if (Random.rollPercent(c.chanceToKeepMaterials()))
						keepBars = true;
				}
				if (!keepBars) {
					for (Item bar : bars)
						bar.remove();
				}
				DailyTasks.handleItemObtained(player, makeId, StatType.Smithing, makeAmount);
				player.getInventory().addOrDrop(makeId, makeAmount);
				PerkTaskHandler.handleGatherResource(player, makeId, makeAmount);
				RingOfForging.onSmith(player, player.smithBar, this);
				player.getStats().addXp(StatType.Smithing, xp, true);
				if (++made >= amount)
					return;
				e.delay(2);
			}
		});
	}

	@Override
	public String toString() {
		return "SmithItem{" +
			"makeId=" + makeId +
			", makeAmount=" + makeAmount +
			'}';
	}
}
