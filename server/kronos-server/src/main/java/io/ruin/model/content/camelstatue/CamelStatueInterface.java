package io.ruin.model.content.camelstatue;

import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.map.object.actions.ObjectAction;

public class CamelStatueInterface {
	CamelStatueRewards lastSection = null;

	public void open(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, 1113);
		if (lastSection == null)
			lastSection = CamelStatueRewards.DOUBLE_REASON_POINTS;
		update(player);
	}

	public void changeSection(Player player, CamelStatueRewards section) {
		lastSection = section;
		update(player);
	}

	public void update(Player player) {
		if (lastSection == null)
			lastSection = CamelStatueRewards.DOUBLE_REASON_POINTS;

		player.getPacketSender().sendString(1113, 53, lastSection.getName());
		player.getPacketSender().sendString(1113, 54, lastSection.getDescription());
		player.getPacketSender().sendString(1113, 65, "<col=31ff1f>" + NumberUtils.formatNumber(CamelStatueHandler.totalDonated.get()) + "</col> Gold sacrificed");
		player.getPacketSender().sendString(1113, 62, NumberUtils.formatNumber(CamelStatueHandler.totalDonated.get()) + "/" + NumberUtils.formatNumber(lastSection.getUnlockAmount()));

		int barInterfaceHash = 1113 << 16 | 60;
		int barBackgroundInterfaceHash = 1113 << 16 | 61;
		float percentageCompleted = (float) CamelStatueHandler.totalDonated.get() / lastSection.getUnlockAmount();
		float barWidth = percentageCompleted * 308;

		player.getPacketSender().sendClientScript(10606, "Ii", barInterfaceHash, (int) barWidth);
		player.getPacketSender().sendClientScript(10606, "Ii", barBackgroundInterfaceHash, (int) barWidth);
	}

	public static void register() {
		InterfaceHandler.register(1113, h -> {
			h.actions[24] = (SimpleAction) p -> p.getCamelStatueInterface().changeSection(p, CamelStatueRewards.DOUBLE_REASON_POINTS);
			h.actions[27] = (SimpleAction) p -> p.getCamelStatueInterface().changeSection(p, CamelStatueRewards.DOUBLE_PERK_EXPERIENCE);
			h.actions[30] = (SimpleAction) p -> p.getCamelStatueInterface().changeSection(p, CamelStatueRewards.DOUBLE_SLAYER_POINTS);
			h.actions[33] = (SimpleAction) p -> p.getCamelStatueInterface().changeSection(p, CamelStatueRewards.BOOSTED_PET_RATES);
			h.actions[36] = (SimpleAction) p -> p.getCamelStatueInterface().changeSection(p, CamelStatueRewards.ADDITIONAL_ROLL_CHANCE);
			h.actions[39] = (SimpleAction) p -> p.getCamelStatueInterface().changeSection(p, CamelStatueRewards.DOUBLE_RAID_POINTS);
			h.actions[42] = (SimpleAction) p -> p.getCamelStatueInterface().changeSection(p, CamelStatueRewards.DROP_RATE_BOOST);
			h.actions[45] = (SimpleAction) p -> p.getCamelStatueInterface().changeSection(p, CamelStatueRewards.DOUBLE_EXPERIENCE);
			h.actions[63] = (SimpleAction) CamelStatueHandler::donateToWell;
			h.actions[66] = (SimpleAction) p -> p.getCamelStatueInterface().update(p);
		});

		ObjectAction.register(10961, 1, (p, obj) -> CamelStatueHandler.donateToWell(p));
		ObjectAction.register(10961, 2, (p, obj) -> p.getCamelStatueInterface().open(p));

		ItemObjectAction.register(ItemID.PLATINUM_TOKEN, 10961, CamelStatueHandler::donatePlatinumTokens);
	}
}
