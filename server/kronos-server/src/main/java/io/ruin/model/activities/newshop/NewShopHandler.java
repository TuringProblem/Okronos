package io.ruin.model.activities.newshop;

import io.ruin.model.activities.newshop.shops.*;
import io.ruin.model.activities.tempevents.hweenevent.HalloweenEventStore;
import io.ruin.model.activities.tempevents.summerevent.SummerStore;
import io.ruin.model.entity.player.Player;

public class NewShopHandler {
	public static ReasonPointStore reasonPointStore = new ReasonPointStore();
	public static AchievementPointStore achievementPointStore = new AchievementPointStore();
	public static VotePointStore votePointStore = new VotePointStore();
	public static DonatorPointStore donatorPointStore = new DonatorPointStore();
	public static IronManStore ironManStore = new IronManStore();
	public static AdminStore adminStore = new AdminStore();
	public static HalloweenEventStore halloweenEventStore = new HalloweenEventStore();
	public static SkillcapeShop skillcapeShop = new SkillcapeShop();
	public static SlayerPointStore slayerStore = new SlayerPointStore();
	public static SummerStore summerStore = new SummerStore();

	public static void openShop(Player player, NewShop shop) {
		player.getNewShopInterface().openShop(player, shop);
		shop.openMessage(player);
	}
}
