package io.ruin.model.inter.questtab;

import io.ruin.Server;
import io.ruin.api.utils.TimeUtils;
import io.ruin.cache.Color;
import io.ruin.model.activities.duelarena.Duel;
import io.ruin.model.activities.wilderness.Wilderness;
import io.ruin.model.content.drop_rate.DropRateBonusManager;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.actions.SlotAction;
import io.ruin.model.inter.questtab.bestiary.Bestiary;
import io.ruin.model.inter.questtab.toggles.*;
import io.ruin.model.var.VarPlayerRepository;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

import static io.ruin.model.World.*;

@Getter
public enum JournalTabComponent {
	/**
	 * Journal tab
	 */
	SUMMARY(JournalTabTab.SUMMARY, player -> {
		player.getPacketSender().sendIfEvents(712, 3, 3, 6, 2);
	}) {
		@Override
		public void init() {
			InterfaceHandler.register(getTab().getId(), h -> h.actions[3] = (SlotAction) (player, slot) -> JournalTab.handleSummaryClick(player, slot));
		}
	},
	SUMMARY_ACHIEVEMENTS(JournalTabTab.SUMMARY, player -> {
	}),
	SUMMARY_TASKS(JournalTabTab.SUMMARY, player -> {
	}),
	SUMMARY_COLLECTION(JournalTabTab.SUMMARY, player -> {
		//VarPlayerRepository.COLLECTION_PROGRESS.set(player, player.getCollectionLog().getCollected().size());
		//VarPlayerRepository.COLLECTION_COUNT.set(player, CollectionLogInfo.TOTAL_COLLECTABLES);
	}),
	SUMMARY_TIME_PLAYED(JournalTabTab.SUMMARY, player -> {
		player.getPacketSender().sendClientScript(3970, "iii", 0, 0, (int) TimeUnit.MILLISECONDS.toMinutes(player.playTime * Server.tickMs()));
	}),

	PLAYERS(JournalTabTab.QUEST, JournalTab.t1++, player -> "Players Online: " + Color.GREEN.wrap(String.valueOf(JournalTab.getOnlineCount())), (SimpleAction) PlayersOnline::open),
	STAFF(JournalTabTab.QUEST, JournalTab.t1++, player -> "Staff Online: " + Color.GREEN.wrap(String.valueOf(JournalTab.getStaffOnlineCount())), (SimpleAction) player -> JournalTab.sendStaffOnline(player)),
	UPTIME(JournalTabTab.QUEST, JournalTab.t1++, player -> "Uptime: " + Color.GREEN.wrap(TimeUtils.fromMs(Server.currentTick() * Server.tickMs(), false))),
	SERVER_TIME(JournalTabTab.QUEST, JournalTab.t1++, player -> "Time: " + Color.GREEN.wrap(JournalTab.getServerTime())),

	PLAYER_INFORMATION(JournalTabTab.QUEST, JournalTab.t1++),
	RANK(JournalTabTab.QUEST, JournalTab.t1++, player -> {
		if (player.isStaff() && player.isDonator()) {
			return "Rights: " + Color.GREEN.wrap(player.getPrimaryGroup().Name);
		}
		return "Rights: " + Color.GREEN.wrap(player.getPrimaryGroup().Name);
	}),
	DONATOR(JournalTabTab.QUEST, JournalTab.t1++, player -> {
		if (player.isDonator() && !player.isYoutuber()) {
			return "Rank: " + Color.GREEN.wrap(player.getSecondaryGroup().Name);
		}
		return "Rank: " + Color.GREEN.wrap(player.getSecondaryGroup().Name);
	}),
	DROP_RATE(JournalTabTab.QUEST, JournalTab.t1++, player -> "Drop Rate: " + Color.GREEN.wrap(DropRateBonusManager.getInstance().getTotalBonusDropRate(player) + "%")),
	TOTAL_PURCHASE(JournalTabTab.QUEST, JournalTab.t1++, player -> "Total Donated: " + Color.GREEN.wrap("$" + player.storeAmountSpent)),
	GAME_MODE(JournalTabTab.QUEST, JournalTab.t1++, player -> "Game Mode: " + Color.GREEN.wrap(player.getGameMode().Name)),
	XP_BONUS(JournalTabTab.QUEST, JournalTab.t1++, player -> "Difficulty: " + Color.GREEN.wrap(player.getDifficulty().Name)),
	//SKILL_XP_BONUS(Tab.QUEST, t1++, player -> "Your SkillXP Bonus: " + Color.GREEN.wrap(player.xpMode.getSkillRate() + "X")),
	//PLAYTIME(Tab.QUEST, t1++, player -> "Time Played: " + Color.GREEN.wrap(TimeUtils.fromMs(player.playTime * Server.tickMs(), false))),
	KDR(JournalTabTab.QUEST, JournalTab.t1++, player -> "KDR: " + Color.GREEN.wrap(JournalTab.getKdr(player))),
	BOSS_POINTS(JournalTabTab.QUEST, JournalTab.t1++, player -> "Boss Points: " + Color.GREEN.wrap(Integer.toString(player.bossPoints))),
	CREDITS(JournalTabTab.QUEST, JournalTab.t1++, player -> "PVM Points: " + Color.GREEN.wrap(Integer.toString(player.PvmPoints))),

	AFK_POINTS(JournalTabTab.QUEST, JournalTab.t1++, player -> "AFK Points: " + Color.GREEN.wrap(Integer.toString(player.afkPoints))),
	PEST_POINTS(JournalTabTab.QUEST, JournalTab.t1++, player -> "Pest Points: " + Color.GREEN.wrap(Integer.toString(player.pestPoints))),
	SLAYER_POINTS(JournalTabTab.QUEST, JournalTab.t1++, player -> "Slayer Points: " + Color.GREEN.wrap(Integer.toString(VarPlayerRepository.SLAYER_POINTS.get(player)))),
//        EMPTY_5(Tab.QUEST, t1++),

	/**
	 * Activity tab
	 */
	PLAYERS_WILD(JournalTabTab.QUEST, JournalTab.t1++, player -> "Wilderness: " + Color.GREEN.wrap(String.valueOf(Wilderness.players.size())) + " players."),
	PLAYERS_DUEL(JournalTabTab.QUEST, JournalTab.t1++, player -> "Duel Arena: " + Color.GREEN.wrap(String.valueOf(Duel.players.size())) + " players."),
	PK_BONUS(JournalTabTab.QUEST, JournalTab.t1++, player -> "PK Bonus: " + Color.GREEN.wrap((doublePkp ? "Enabled" : "Disabled"))),
	XP_BOOST(JournalTabTab.QUEST, JournalTab.t1++, player -> "XP Bonus: " + Color.GREEN.wrap(weekendExpBoost ? "Enabled" : "Disabled")),
//        EMPTY_6(Tab.QUEST, t1++),

//        SLAYER_BONUS(Tab.QUEST, t1++, player -> "Slayer Bonus: " + Color.GREEN.wrap(Misc.stateOf(WellOfGoodwill.isDoubleSlayer()))),
//        PC_BONUS(Tab.QUEST, t1++, player -> "Pest Bonus: " + Color.GREEN.wrap(Misc.stateOf(WellOfGoodwill.isDoublePest()))),

	/**
	 * Misc tab.
	 */
	DROP_TABLES(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, player -> "View drop tables", (SimpleAction) Bestiary::open),
	EMPTY(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, player -> "View forums", (SimpleAction) player -> player.sendMessage("Disabled.")),
	EMPTY2(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, player -> "View store", (SimpleAction) player -> player.openUrl("https://reasonps.com/store")),
	EMPTY3(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, player -> "Join discord", (SimpleAction) player -> player.openUrl("https://discord.gg/WeYBAV7S3G")),
	//EMPTY3(Tab.MISCELLANEOUS, t4++),

	COMBAT(JournalTabTab.MISCELLANEOUS, JournalTab.t4++),
	TARGET_OVERLAY(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, player -> "Target Overlay", (SimpleAction) player -> player.sendMessage("This feature is currently disabled.")),
	DRAG_SETTING(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, new DragSetting()),
	TIMER_TOGGLE(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, new ShowTimers()),
	BH_OVERLAY(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, new BountyOverlay()),
	KD_OVERLAY(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, new KDOverlay()),

	BOUNTY_HUNTER(JournalTabTab.MISCELLANEOUS, JournalTab.t4++),
	BH_TARGETING(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, new BountyHunterTargeting()),
	BH_STREAKS(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, new BountyHunterStreaks()),
	EMPTY4(JournalTabTab.MISCELLANEOUS, JournalTab.t4++),

	BROADCASTS(JournalTabTab.MISCELLANEOUS, JournalTab.t4++),
	BC_BOSS_EVENTS(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, new BroadcastBossEvent()),
	BC_VOLCANO(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, new BroadcastActiveVolcano()),
	BC_HOTSPOT(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, new BroadcastHotspot()),
	BC_SUPPLY_CHEST(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, new BroadcastSupplyChest()),
	BC_ANNOUNCEMENTS(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, new BroadcastAnnouncements()),
	BC_TOURNAMENTS(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, new BroadcastTournaments()),

	OTHER(JournalTabTab.MISCELLANEOUS, JournalTab.t4++),
	BREAK_VIALS(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, new BreakVials()),
	DISCARD_BUCKETS(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, new DiscardBuckets()),
	HIDE_ICON(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, new HideIcon()),
	RISK_PROTECTION(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, RiskProtection.INSTANCE),
	HIDE_YELLS(JournalTabTab.MISCELLANEOUS, JournalTab.t4++, new HideYells()),
	EMPTY_7(JournalTabTab.MISCELLANEOUS, JournalTab.t4++),
//        EXP_LOCK(Tab.MISCELLANEOUS, t4++, player -> "Experience lock", (SimpleAction) p -> ExperienceLock.open(p)),
	;

	private JournalTabTab tab;
	private int componentId;
	public JournalTab.TextField text;
	private InterfaceAction action;
	public SimpleAction send;

	JournalTabComponent(JournalTabTab tab) {
		this(tab, 0, null, null);
	}

	JournalTabComponent(JournalTabTab tab, SimpleAction send) {
		this(tab, 0, null, null);
		this.send = send;
	}

	JournalTabComponent(JournalTabTab tab, int componentId) {
		this(tab, componentId, null, null);
	}

	JournalTabComponent(JournalTabTab tab, int componentId, JournalTab.TextField text) {
		this(tab, componentId, text, null);
	}

	JournalTabComponent(JournalTabTab tab, int componentId, JournalTab.TextField text, InterfaceAction action) {
		this.tab = tab;
		this.componentId = componentId;
		this.text = text;
		this.action = action;
		init();
	}

	JournalTabComponent(JournalTabTab tab, int componentId, JournalToggle toggle) {
		this.tab = tab;
		this.componentId = componentId;
		this.text = toggle.getText();
		this.action = (SimpleAction) toggle::handle;
		init();
	}

	public static final JournalTabComponent[] VALUES = values();

	public void init() {

	}

	public void send(Player player) {
		if (send != null) {
			send.handle(player);
			return;
		}

		if (getTab().equals(JournalTabTab.MISCELLANEOUS)) {
			player.getPacketSender().sendString(getTab().getId(), getComponentId(), getText().send(player));
		} else {
			player.getPacketSender().sendString(getTab().getId(), getComponentId(), " - " + getText().send(player));
		}
	}
}
