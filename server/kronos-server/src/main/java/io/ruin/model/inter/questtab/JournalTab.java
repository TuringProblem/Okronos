package io.ruin.model.inter.questtab;

import io.ruin.HooksV2;
import io.ruin.Server;
import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.TimeUtils;
import io.ruin.cache.Color;
import io.ruin.model.ScrollbarClientScript;
import io.ruin.model.World;
import io.ruin.model.activities.dailytasks.DailyTasksInterface;
import io.ruin.model.activities.duelarena.Duel;
import io.ruin.model.activities.wilderness.Wilderness;
import io.ruin.model.content.drop_rate.DropRateBonusManager;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.inter.*;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.actions.SlotAction;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.handlers.AchievementInterface;
import io.ruin.model.inter.handlers.LootsTables;
import io.ruin.model.inter.questtab.bestiary.Bestiary;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.inter.questtab.toggles.*;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.Position;
import io.ruin.model.skills.magic.spells.modern.ModernTeleport;
import io.ruin.model.skills.slayer.SlayerUnlock;
import io.ruin.model.var.VarPlayerRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.ruin.model.World.*;

public class JournalTab {

	public static interface Hook {
		record OnSummaryClick(Player player, int slot) implements Hook {
		}

		record OnSend(Player player, JournalTab.TabComponent component) implements Hook {
		}

		record UpdateCurrentSection(Player player, JournalTab.Section section) implements Hook {
		}
	}

	public static HooksV2<Hook> hooks = new HooksV2<>(Hook.class);
	public static Section section;

	public enum Section {
		PLAYER_INFO,
		SERVER_INFO,
		ACHIEVEMENT,
		GROUP_IRON
	}

	@Getter
	public enum Tab {
		SUMMARY(Interface.QUEST),
		QUEST(Interface.SERVER_JOURNAL),
		ACHIEVEMENT(Interface.ACHIEVEMENT),
		ACTIVITIES(Interface.ACTIVITIES),
		MISCELLANEOUS(Interface.MISCELLANEOUS);

		private final int id;

		Tab(int id) {
			this.id = id;
		}

		/**
		 * Returns the components of the specified {@link Tab}.
		 *
		 * @return A list of components.
		 */
		public List<TabComponent> getComponents() {
			List<TabComponent> list = new ArrayList<>();

			for (TabComponent component : TabComponent.VALUES) {
				if (!component.getTab().equals(this)) {
					continue;
				}

				list.add(component);
			}

			return list;
		}

		public static void init() {
			try {
				for (Tab tab : values()) {
					Map<Integer, InterfaceAction> actions = new HashMap<>();

					for (TabComponent c : tab.getComponents()) {
						if (c.getAction() == null) {
							continue;
						}

						actions.put(c.getComponentId(), c.getAction());
					}

					if (actions.isEmpty()) {
						continue;
					}

					InterfaceHandler.register(tab.getId(), interfaceHandler -> {
						for (int component : actions.keySet()) {
							interfaceHandler.actions[component] = actions.get(component);
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static int t1 = 7; // 10 - 30
	public static int t2;
	public static int t3 = 9; // 9 - 20
	public static int t4 = 7;

	@Getter
	public enum TabComponent {
		/**
		 * Journal tab
		 */
		SUMMARY(Tab.SUMMARY, player -> {
			player.getPacketSender().sendIfEvents(712, 3, 3, 6, 2);
		}) {
			@Override
			public void init() {
				InterfaceHandler.register(getTab().getId(),
						h -> h.actions[3] = (SlotAction) (player, slot) -> JournalTab.handleSummaryClick(player, slot));
			}
		},
		SUMMARY_ACHIEVEMENTS(Tab.SUMMARY, player -> {
		}),
		SUMMARY_TASKS(Tab.SUMMARY, player -> {
		}),
		SUMMARY_COLLECTION(Tab.SUMMARY, player -> {
		}),
		SUMMARY_TIME_PLAYED(Tab.SUMMARY, player -> {
			player.getPacketSender().sendClientScript(3970, "iii", 0, 0,
					(int) TimeUnit.MILLISECONDS.toMinutes(player.playTime * Server.tickMs()));
		}),

		PLAYERS(Tab.QUEST, t1++,
				player -> "Players Online: " + Color.GREEN.wrap(String.valueOf(JournalTab.getOnlineCount())),
				(SimpleAction) PlayersOnline::open),
		STAFF(Tab.QUEST, t1++,
				player -> "Staff Online: " + Color.GREEN.wrap(String.valueOf(JournalTab.getStaffOnlineCount())),
				(SimpleAction) player -> JournalTab.sendStaffOnline(player)),
		UPTIME(Tab.QUEST, t1++,
				player -> "Uptime: " + Color.GREEN.wrap(TimeUtils.fromMs(Server.currentTick() * Server.tickMs(), false))),
		SERVER_TIME(Tab.QUEST, t1++, player -> "Time: " + Color.GREEN.wrap(JournalTab.getServerTime())),

		PLAYER_INFORMATION(Tab.QUEST, t1++),
		RANK(Tab.QUEST, t1++, player -> {
			if (player.isStaff() && player.isDonator()) {
				return "Rights: " + Color.GREEN.wrap(player.getPrimaryGroup().Name);
			}
			return "Rights: " + Color.GREEN.wrap(player.getPrimaryGroup().Name);
		}),
		DONATOR(Tab.QUEST, t1++, player -> {
			if (player.isDonator() && !player.isYoutuber()) {
				return "Rank: " + Color.GREEN.wrap(player.getSecondaryGroup().Name);
			}
			return "Rank: " + Color.GREEN.wrap(player.getSecondaryGroup().Name);
		}),
		DROP_RATE(Tab.QUEST, t1++,
				player -> "Drop Rate: "
						+ Color.GREEN.wrap(DropRateBonusManager.getInstance().getTotalBonusDropRate(player) + "%")),
		TOTAL_PURCHASE(Tab.QUEST, t1++, player -> "Total Donated: " + Color.GREEN.wrap("$" + player.storeAmountSpent)),
		GAME_MODE(Tab.QUEST, t1++, player -> "Game Mode: " + Color.GREEN.wrap(player.getGameMode().Name)),
		// XP_BONUS(Tab.QUEST, t1++, player -> "Difficulty: " +
		// Color.GREEN.wrap(player.getDifficulty().Name)),
		KDR(Tab.QUEST, t1++, player -> "KDR: " + Color.GREEN.wrap(JournalTab.getKdr(player))),
		BOSS_POINTS(Tab.QUEST, t1++, player -> "Boss Points: " + Color.GREEN.wrap(Integer.toString(player.bossPoints))),
		CREDITS(Tab.QUEST, t1++, player -> "PVM Points: " + Color.GREEN.wrap(Integer.toString(player.PvmPoints))),
		AFK_POINTS(Tab.QUEST, t1++, player -> "AFK Points: " + Color.GREEN.wrap(Integer.toString(player.afkPoints))),
		PEST_POINTS(Tab.QUEST, t1++, player -> "Pest Points: " + Color.GREEN.wrap(Integer.toString(player.pestPoints))),
		SLAYER_POINTS(Tab.QUEST, t1++,
				player -> "Slayer Points: "
						+ Color.GREEN.wrap(Integer.toString(VarPlayerRepository.SLAYER_POINTS.get(player)))),
		/**
		 * Activity tab
		 */
		PLAYERS_WILD(Tab.QUEST, t1++,
				player -> "Wilderness: " + Color.GREEN.wrap(String.valueOf(Wilderness.players.size())) + " players."),
		PLAYERS_DUEL(Tab.QUEST, t1++,
				player -> "Duel Arena: " + Color.GREEN.wrap(String.valueOf(Duel.players.size())) + " players."),
		PK_BONUS(Tab.QUEST, t1++, player -> "PK Bonus: " + Color.GREEN.wrap((doublePkp ? "Enabled" : "Disabled"))),
		XP_BOOST(Tab.QUEST, t1++, player -> "XP Bonus: " + Color.GREEN.wrap(weekendExpBoost ? "Enabled" : "Disabled")),
		/**
		 * Misc tab.
		 */
		DROP_TABLES(Tab.MISCELLANEOUS, t4++, player -> "View drop tables", (SimpleAction) Bestiary::open),
		// EMPTY(Tab.MISCELLANEOUS, t4++, player -> "View forums", (SimpleAction) player
		// -> player.openUrl("https://prifddinas.io/community/")),
		// EMPTY2(Tab.MISCELLANEOUS, t4++, player -> "View store", (SimpleAction) player
		// -> player.openUrl("https://prifddinas.io/community/pages/Store/")),
		// EMPTY3(Tab.MISCELLANEOUS, t4++, player -> "Join discord", (SimpleAction)
		// player -> player.openUrl("https://discord.gg/prifddinas")),
		COMBAT(Tab.MISCELLANEOUS, t4++),
		TARGET_OVERLAY(Tab.MISCELLANEOUS, t4++, player -> "Target Overlay",
				(SimpleAction) player -> player.sendMessage("This feature is currently disabled.")),
		DRAG_SETTING(Tab.MISCELLANEOUS, t4++, new DragSetting()),
		TIMER_TOGGLE(Tab.MISCELLANEOUS, t4++, new ShowTimers()),
		BH_OVERLAY(Tab.MISCELLANEOUS, t4++, new BountyOverlay()),
		KD_OVERLAY(Tab.MISCELLANEOUS, t4++, new KDOverlay()),
		BOUNTY_HUNTER(Tab.MISCELLANEOUS, t4++),
		BH_TARGETING(Tab.MISCELLANEOUS, t4++, new BountyHunterTargeting()),
		BH_STREAKS(Tab.MISCELLANEOUS, t4++, new BountyHunterStreaks()),
		EMPTY4(Tab.MISCELLANEOUS, t4++),
		BROADCASTS(Tab.MISCELLANEOUS, t4++),
		BC_BOSS_EVENTS(Tab.MISCELLANEOUS, t4++, new BroadcastBossEvent()),
		BC_VOLCANO(Tab.MISCELLANEOUS, t4++, new BroadcastActiveVolcano()),
		BC_HOTSPOT(Tab.MISCELLANEOUS, t4++, new BroadcastHotspot()),
		BC_SUPPLY_CHEST(Tab.MISCELLANEOUS, t4++, new BroadcastSupplyChest()),
		BC_ANNOUNCEMENTS(Tab.MISCELLANEOUS, t4++, new BroadcastAnnouncements()),
		BC_TOURNAMENTS(Tab.MISCELLANEOUS, t4++, new BroadcastTournaments()),
		OTHER(Tab.MISCELLANEOUS, t4++),
		BREAK_VIALS(Tab.MISCELLANEOUS, t4++, new BreakVials()),
		DISCARD_BUCKETS(Tab.MISCELLANEOUS, t4++, new DiscardBuckets()),
		HIDE_ICON(Tab.MISCELLANEOUS, t4++, new HideIcon()),
		RISK_PROTECTION(Tab.MISCELLANEOUS, t4++, RiskProtection.INSTANCE),
		HIDE_YELLS(Tab.MISCELLANEOUS, t4++, new HideYells()),
		EMPTY_7(Tab.MISCELLANEOUS, t4++),
		;

		private Tab tab;
		private int componentId;
		private TextField text;
		private InterfaceAction action;
		private SimpleAction send;
		public static final TabComponent[] VALUES = values();

		TabComponent(Tab tab) {
			this(tab, 0, null, null);
		}

		TabComponent(Tab tab, SimpleAction send) {
			this(tab, 0, null, null);
			this.send = send;
		}

		TabComponent(Tab tab, int componentId) {
			this(tab, componentId, null, null);
		}

		TabComponent(Tab tab, int componentId, TextField text) {
			this(tab, componentId, text, null);
		}

		TabComponent(Tab tab, int componentId, TextField text, InterfaceAction action) {
			this.tab = tab;
			this.componentId = componentId;
			this.text = text;
			this.action = action;
			init();
		}

		TabComponent(Tab tab, int componentId, JournalToggle toggle) {
			this.tab = tab;
			this.componentId = componentId;
			this.text = toggle.getText();
			this.action = (SimpleAction) toggle::handle;
			init();
		}

		public void init() {

		}

		public void send(Player player) {
			if (hooks.handle(new Hook.OnSend(player, this))) {
				return;
			}

			if (send != null) {
				send.handle(player);
				return;
			}

			if (getTab().equals(Tab.MISCELLANEOUS)) {
				player.getPacketSender().sendString(getTab().getId(), getComponentId(), getText().send(player));
			} else {
				player.getPacketSender().sendString(getTab().getId(), getComponentId(), " - " + getText().send(player));
			}
		}
	}

	public static void setTab(Player player, Tab tab) {
		int index = tab.ordinal() != 0 ? tab.ordinal() + 1 : tab.ordinal();
		if (tab == Tab.MISCELLANEOUS) {
			index = tab.ordinal();
		} else if (tab == Tab.ACTIVITIES) {
			index = 0;
		} else if (tab == Tab.QUEST) {
			index = 1;
		}
		if (tab == Tab.SUMMARY) {
			VarPlayerRepository.QUEST_ACTIVE_TAB.set(player, 0);
		} else {
			if (tab == Tab.MISCELLANEOUS) {
				VarPlayerRepository.QUEST_ACTIVE_TAB.set(player, 3);
			} else
				VarPlayerRepository.QUEST_ACTIVE_TAB.set(player, tab.ordinal());
		}
		player.getPacketSender().openSubInterface(tab.getId(), 629, 28, ClientInterfaceType.OVERLAY);
		updateTab(player, tab);
	}

	public static void updateTab(Player player, Tab tab) {
		if (tab == Tab.QUEST) {
			player.getPacketSender().sendString(tab.getId(), 1, "Journal");
		} else if (tab == Tab.ACHIEVEMENT) {
			player.getPacketSender().sendIfEvents(259, 2, 0, 11, AccessMasks.ClickOp1);
		} else if (tab == Tab.ACTIVITIES) {
			player.getPacketSender().sendString(tab.getId(), 1, "Activities");
		} else if (tab == Tab.MISCELLANEOUS) {
			player.getPacketSender().sendString(tab.getId(), 1, "Miscellaneous");

			int count = 0;

			for (QuestTabEntry entry : QuestTab.TOGGLES.getEntries()) {
				if (count++ == 2) {
					break;
				}

			}
		}

		for (TabComponent c : tab.getComponents()) {
			if (c.text == null && c.send == null) {
				continue;
			}

			c.send(player);
		}
	}

	public static void send(Player player, TabComponent c) {
		player.getPacketSender().sendString(c.getTab().getId(), c.getComponentId(), " - " + c.getText().send(player));
	}

	public static void handleSummaryClick(Player player, int slot) {
		if (hooks.handle(new Hook.OnSummaryClick(player, slot))) {
			return;
		}
		if (slot == 3) {
			player.sendMessage("Coming soon! :)");
		} else if (slot == 4) {
			setTab(player, Tab.ACHIEVEMENT);
		} else if (slot == 5) {
			// AchievementInterface.openTasks(player);
		}
	}

	/**
	 * Sends an interface filtered with staff that are currently online.
	 *
	 * @param player The player.
	 */
	public static void sendStaffOnline(Player player) {
		int interId = 119;
		player.openInterface(ToplevelComponent.MAINMODAL, interId);
		player.getPacketSender().sendString(interId, 4, "Online Staff");
		List<Player> staffList = World.getPlayerStream().filter(Player::isStaff).collect(Collectors.toList());
		int component = 6;
		for (Player p1 : staffList) {
			player.getPacketSender().sendString(interId, component++, p1.getPrimaryGroup().tag() + " " + p1.getName());
		}
	}

	public static int getStaffOnlineCount() {
		List<Player> staffList = World.getPlayerStream().filter(Player::isStaff).collect(Collectors.toList());
		return staffList.size();
	}

	public static int getOnlineCount() {
		List<Player> staffList = World.getPlayerStream().filter(Player::isPlayer).collect(Collectors.toList());
		return staffList.size();
	}

	public static String getKdr(Player player) {
		int kills = VarPlayerRepository.PVP_KILLS.get(player);
		int deaths = VarPlayerRepository.PVP_DEATHS.get(player);

		return (kills + "/" + deaths + "/" + (deaths == 0 ? "0,00" : String.format("%.2f", ((double) kills / deaths))));
	}

	private static String getDoublePkp() {
		if (doublePkp) {
			return Color.GREEN.wrap("Enabled");
		} else {
			return Color.RED.wrap("Disabled");
		}
	}

	private static String getDoubleSlayerPoints() {
		if (World.doubleSlayer) {
			return Color.GREEN.wrap("Enabled");
		} else {
			return Color.RED.wrap("Disabled");
		}
	}

	private static String getDoublePcPoints() {
		if (World.doublePest) {
			return Color.GREEN.wrap("Enabled");
		} else {
			return Color.RED.wrap("Disabled");
		}
	}

	public static String getServerTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date date = new Date(System.currentTimeMillis());
		return sdf.format(date);
	}

	private static double getXpBonus(Player player) {
		double xp = 1;

		if (World.xpMultiplier > 0)
			xp += World.xpMultiplier - 1;

		if (player.expBonus.isDelayed()) // 50% xp scrolls
			xp += 0.5;

		if (player.wildernessLevel > 1) // wilderness bonus
			xp += .25;

		if (World.weekendExpBoost)
			xp += .25;

		return xp;
	}

	public interface TextField {
		String send(Player player);
	}

	public static void swap(Player player, int interfaceId) {
		player.openInterface(ToplevelComponent.QUEST_TAB_AREA, interfaceId);
	}

	public static void openAchievementInterface(Player player) {
		if (player.currentAchievement == null)
			player.currentAchievement = Achievements.ARE_YOU_SHORE_ABOUT_THIS;
		AchievementInterface achievementInterface = new AchievementInterface();
		achievementInterface.Init(player, player.currentAchievement.getSkillName());
	}

	private static void sendAchievementSection(Player player, Achievements.AchievementTypes tier) {
		AchievementInterface achievementInterface = new AchievementInterface();
		switch (tier) {
			case EASY:
				player.currentAchievement = Achievements.ARE_YOU_SHORE_ABOUT_THIS;
				achievementInterface.Init(player, player.currentAchievement.getSkillName());
				for (Achievements ach : Achievements.VALUES) {
					player.getPacketSender().setHidden(849, ach.getHiddenButtonID(), true);
				}
				player.getPacketSender().setHidden(849, player.currentAchievement.getHiddenButtonID(), false);
				achievementInterface.GetAchievementTypeButtonDown(player);
				break;
			case MEDIUM:
				player.currentAchievement = Achievements.IBANT_BELIEVE_THIS;
				achievementInterface.Init(player, player.currentAchievement.getSkillName());
				achievementInterface.GetAchievementTypeButtonDown(player);
				break;
			case HARD:
				player.currentAchievement = Achievements.PROTECTIVE_HEADGEAR;
				achievementInterface.Init(player, player.currentAchievement.getSkillName());
				achievementInterface.GetAchievementTypeButtonDown(player);
				break;
			case ELITE:
				player.currentAchievement = Achievements.LUMBERJACK_IV;
				achievementInterface.Init(player, player.currentAchievement.getSkillName());
				achievementInterface.GetAchievementTypeButtonDown(player);
				break;
		}
	}

	private static int getNumberOfAchievementsByTier(Achievements.AchievementTypes tier) {
		int amount = 0;
		for (Achievements ach : Achievements.VALUES) {
			if (ach.getAchievementType() == tier)
				amount++;
		}
		return amount;
	}

	private static int getNumberOfAchievementsCompletedByTier(Player player, Achievements.AchievementTypes tier) {
		int completed = 0;
		for (Achievements ach : Achievements.VALUES) {
			if (ach.getAchievementType() != tier)
				continue;
			if (player.getAchievementCurrentProgress(ach) >= ach.getCompletionAmount())
				completed++;
		}
		return completed;
	}

	private static void sendAchievementProgressBar(Player player, int interfaceHash, Achievements.AchievementTypes tier) {
		float completed = getNumberOfAchievementsCompletedByTier(player, tier);
		float total = getNumberOfAchievementsByTier(tier);
		float progress = (completed / total) * 153;
		player.getPacketSender().sendClientScript(10533, "Ii", interfaceHash, (int) progress);
	}

	private static void sendAchievementTab(Player player) {
		player.currentSection = Section.ACHIEVEMENT;

		player.getPacketSender().openSubInterface(1086, 629, 28, ClientInterfaceType.OVERLAY);
		player.getPacketSender().setHidden(1086, 6, true);
		player.getPacketSender().setHidden(1086, 101, true);
		player.getPacketSender().setHidden(1086, 19, true);
		player.getPacketSender().setHidden(1086, 27, true);
		player.getPacketSender().setHidden(1086, 39, false);
		player.getPacketSender().setHidden(1086, 47, false);

		int easyBarInterfaceHash = 1086 << 16 | 55;
		int medBarInterfaceHash = 1086 << 16 | 67;
		int hardBarInterfaceHash = 1086 << 16 | 79;
		int eliteBarInterfaceHash = 1086 << 16 | 91;

		player.getPacketSender().sendString(1086, 60,
				getNumberOfAchievementsCompletedByTier(player, Achievements.AchievementTypes.EASY) + "/"
						+ getNumberOfAchievementsByTier(Achievements.AchievementTypes.EASY));
		player.getPacketSender().sendString(1086, 72,
				getNumberOfAchievementsCompletedByTier(player, Achievements.AchievementTypes.MEDIUM) + "/"
						+ getNumberOfAchievementsByTier(Achievements.AchievementTypes.MEDIUM));
		player.getPacketSender().sendString(1086, 84,
				getNumberOfAchievementsCompletedByTier(player, Achievements.AchievementTypes.HARD) + "/"
						+ getNumberOfAchievementsByTier(Achievements.AchievementTypes.HARD));
		player.getPacketSender().sendString(1086, 96,
				getNumberOfAchievementsCompletedByTier(player, Achievements.AchievementTypes.ELITE) + "/"
						+ getNumberOfAchievementsByTier(Achievements.AchievementTypes.ELITE));

		int easyProgress = getNumberOfAchievementsCompletedByTier(player, Achievements.AchievementTypes.EASY)
				/ getNumberOfAchievementsByTier(Achievements.AchievementTypes.EASY) * 100;
		int medProgress = getNumberOfAchievementsCompletedByTier(player, Achievements.AchievementTypes.MEDIUM)
				/ getNumberOfAchievementsByTier(Achievements.AchievementTypes.MEDIUM) * 100;
		int hardProgress = getNumberOfAchievementsCompletedByTier(player, Achievements.AchievementTypes.HARD)
				/ getNumberOfAchievementsByTier(Achievements.AchievementTypes.HARD) * 100;
		int eliteProgress = getNumberOfAchievementsCompletedByTier(player, Achievements.AchievementTypes.ELITE)
				/ getNumberOfAchievementsByTier(Achievements.AchievementTypes.ELITE) * 100;
		sendAchievementBarColour(player, easyBarInterfaceHash, easyProgress);
		sendAchievementBarColour(player, easyBarInterfaceHash, medProgress);
		sendAchievementBarColour(player, easyBarInterfaceHash, hardProgress);
		sendAchievementBarColour(player, easyBarInterfaceHash, eliteProgress);

		sendAchievementProgressBar(player, easyBarInterfaceHash, Achievements.AchievementTypes.EASY);
		sendAchievementProgressBar(player, medBarInterfaceHash, Achievements.AchievementTypes.MEDIUM);
		sendAchievementProgressBar(player, hardBarInterfaceHash, Achievements.AchievementTypes.HARD);
		sendAchievementProgressBar(player, eliteBarInterfaceHash, Achievements.AchievementTypes.ELITE);
	}

	private static void sendServerInformationTab(Player player) {
		ScrollbarClientScript.create()
				.interfaceId(848)
				.containerId(2)
				.scrollbarChildId(31)
				.childrenCount(23)
				.withDarkGraphics()
				.build()
				.send(player);
		player.currentSection = Section.SERVER_INFO;
		player.getPacketSender().openSubInterface(848, 629, 28, ClientInterfaceType.OVERLAY);
		for (ComponentText componentText : ComponentText.VALUES) {
			Object value = componentText.value.apply(player);
			if (value != null) {
				player.getPacketSender().sendString(848, componentText.childId, componentText.preText + value);
			}
		}
	}

	private static void sendAchievementBarColour(Player player, int interfaceHash, int progress) {
		if (progress < 25)
			player.getPacketSender().sendClientScript(10536, "I", interfaceHash);
		else if (progress < 50)
			player.getPacketSender().sendClientScript(10537, "I", interfaceHash);
		else if (progress < 75)
			player.getPacketSender().sendClientScript(10535, "I", interfaceHash);
		else if (progress >= 75)
			player.getPacketSender().sendClientScript(10534, "I", interfaceHash);
	}

	public static void updateCurrentTab(Player player) {
		if (player.newPlayer) {
			return;
		}

		switch (player.currentSection) {
			case ACHIEVEMENT:
				sendAchievementTab(player);
				break;
			case PLAYER_INFO:
				sendPlayerInfoTab(player);
				break;
			case SERVER_INFO:
				sendServerInformationTab(player);
				break;
			default:
				break;
		}

		if (hooks.handle(new Hook.UpdateCurrentSection(player, section))) {
			return;
		}
	}

	public static void sendPlayerInfoTab(Player player) {
		player.currentSection = JournalTab.Section.PLAYER_INFO;
		player.getPacketSender().openSubInterface(1086, 629, 28, ClientInterfaceType.OVERLAY);
		player.getPacketSender().setHidden(1086, 6, false);
		player.getPacketSender().setHidden(1086, 19, false);
		player.getPacketSender().setHidden(1086, 27, false);
		player.getPacketSender().setHidden(1086, 39, true);
		player.getPacketSender().setHidden(1086, 47, true);
		player.getPacketSender().setHidden(1086, 101, false);

		player.getPacketSender().sendString(1086, 11,
				"<col=FFFFFF>Total playtime: <col=00ff00>" + TimeToText(player));
		String difficulty = player.getDifficulty().toString().toLowerCase();
		String diff = difficulty.substring(0, 1).toUpperCase() + difficulty.substring(1);
		player.getPacketSender().sendString(1086, 12, "<col=FFFFFF>Difficulty mode: <col=00ff00>" + diff);
		player.getPacketSender().sendString(1086, 13,
				"<col=FFFFFF>Reason points: <col=00ff00>" + NumberUtils.formatNumber(player.getReasonPoints()));
		player.getPacketSender().sendString(1086, 14,
				"<col=FFFFFF>Achievement points: <col=00ff00>" + NumberUtils.formatNumber(player.getAchievementPoints()));
		player.getPacketSender().sendString(1086, 15,
				"<col=FFFFFF>Perk points: <col=00ff00>" + NumberUtils.formatNumber(player.perkPoints));
		player.getPacketSender().sendString(1086, 16,
				"<col=FFFFFF>Vote points: <col=00ff00>" + NumberUtils.formatNumber(player.getVotePoints()));
		player.getPacketSender().sendString(1086, 17,
				"<col=FFFFFF>Donator points: <col=00ff00>" + NumberUtils.formatNumber(player.getDonatorPoints()));
		player.getPacketSender().sendString(1086, 18, "<col=FFFFFF>Total donated: <col=59CB04>$<col=00ff00>"
				+ NumberUtils.formatNumber(player.getTotalDonated()) + ".00");
		player.getPacketSender().sendString(1086, 102,
				"<col=FFFFFF>Drop rate: <col=59CB04><col=00ff00>" + NumberUtils.formatNumber(player.calculateDropRate()) + "%");

		String dialogue = player.currentPerkTask != null
				? player.currentPerkTask.description.toLowerCase() + player.perkTaskCurrentAmount
						+ player.currentPerkTask.description_2 + "."
				: "No Task.";
		player.getPacketSender().sendString(1086, 103, "<col=FFFFFF>Perk Task: <col=59CB04><col=00ff00>" + dialogue);

		player.getPacketSender().sendString(1086, 24, "<col=FFFFFF>Slayer points: <col=00ff00>"
				+ NumberUtils.formatNumber(VarPlayerRepository.SLAYER_POINTS.get(player)));
		String slayerTask;
		if (VarPlayerRepository.SLAYER_TASK_AMOUNT.get(player) > 0)
			slayerTask = "<col=FFFFFF>Task: <col=00ff00>" + VarPlayerRepository.SLAYER_TASK_AMOUNT.get(player) + " "
					+ SlayerUnlock.taskName(player, VarPlayerRepository.SLAYER_TASK.get(player));
		else
			slayerTask = "<col=FFFFFF>Task: <col=00ff00>No task assigned.";

		String bossSlayerTask;
		if (player.bossSlayerName != null)
			bossSlayerTask = "<col=FFFFFF>Boss Task: <col=00ff00>" + player.currentBossSlayerAmount + " "
					+ player.bossSlayerName;
		else
			bossSlayerTask = "<col=FFFFFF>Boss Task: <col=00ff00>No task assigned.";

		player.getPacketSender().sendString(1086, 25, slayerTask);
		player.getPacketSender().sendString(1086, 26, bossSlayerTask);

		int doubleExpMinutes = (int) (player.getDoubleExpRemaining() / 100);
		if (doubleExpMinutes < 0)
			doubleExpMinutes = 0;
		player.getPacketSender().sendString(1086, 32,
				"<col=FFFFFF>Double exp timer: <col=00ff00>" + NumberUtils.formatNumber(doubleExpMinutes) + "m");

		int doubleDropsMinutes = (int) (player.getDoubleDropsRemaining() / 100);

		int delay = player.doubleDropBonus.remaining();
		int minutes = delay / 100;
		if (minutes < 1)
			minutes = 0;
		player.getPacketSender().sendString(1086, 33,
				"<col=FFFFFF>Double drops timer: <col=00ff00>" + NumberUtils.formatNumber(minutes) + "m");

		int damageBoostMinutes = (int) (player.getDamageBoostRemaining() / 100);
		if (damageBoostMinutes < 0)
			damageBoostMinutes = 0;
		player.getPacketSender().sendString(1086, 34,
				"<col=FFFFFF>Damage boost timer: <col=00ff00>" + NumberUtils.formatNumber(damageBoostMinutes) + "m");

		int damageReductionMinutes = (int) (player.getDamageReductionBoostRemaining() / 100);
		if (damageReductionMinutes < 0)
			damageReductionMinutes = 0;
		player.getPacketSender().sendString(1086, 35,
				"<col=FFFFFF>Damage reduction timer: <col=00ff00>" + NumberUtils.formatNumber(damageReductionMinutes) + "m");
		int brewImmunityMinutes = (int) (player.getBrewImmunityRemaining() / 100);
		if (brewImmunityMinutes < 0)
			brewImmunityMinutes = 0;

		player.getPacketSender().sendString(1086, 36,
				"<col=FFFFFF>Brew immunity timer: <col=00ff00>" + NumberUtils.formatNumber(brewImmunityMinutes) + "m");
		int prayerBoostMinutes = (int) (player.getPrayerBoostBonusRemaining() / 100);
		if (prayerBoostMinutes < 0)
			prayerBoostMinutes = 0;
		player.getPacketSender().sendString(1086, 37,
				"<col=FFFFFF>Prayer drain reduction: <col=00ff00>" + NumberUtils.formatNumber(prayerBoostMinutes) + "m");
		int dropRateBoostMinutes = (int) (player.getDropRateBoostRemaining() / 100);
		if (dropRateBoostMinutes < 0)
			dropRateBoostMinutes = 0;
		player.getPacketSender().sendString(1086, 38,
				"<col=FFFFFF>5% Drop rate timer: <col=00ff00>" + NumberUtils.formatNumber(dropRateBoostMinutes) + "m");
	}

	@RequiredArgsConstructor
	enum ComponentText {
		SERVER_TIME("Server time: ", 16, plr -> "<col=00FF00>" + Server.getTime()),
		SERVER_UPTIME("Server uptime: ", 15, plr -> "<col=00FF00>" + Server.getUptime()),
		STAFF_COUNT("Staff online: ", 18, plr -> "<col=00FF00>" + World.staffCount()),
		PLAYER_COUNT("Players online: ", 17, plr -> "<col=00FF00>" + World.playerCount()),
		WIKI_FORUM_OVERWRITE("ReasonPS Wiki ", 30, plr -> ""),
		;

		final String preText;
		final int childId;
		final Function<Player, Object> value;
		public static final ComponentText[] VALUES = values();
	}

	private static String TimeToText(Player player) {
		return TimeUtils.fromMsShort(player.playTime * Server.tickMs());
	}

	private static String TimeToText(long time) {
		String text;
		long days = time / (24 * 3600);
		time %= (24 * 3600);
		long hours = time / 3600;
		time %= 3600;
		long minutes = time / 60;
		time %= 60;
		long seconds = time;
		text = days + "D " + hours + "H";
		if (days > 0)
			text = days + "D";
		return text;
	}

	public static void restore(Player player) {
		swap(player, 1086);
	}

	private static void openDailyTasks(Player player) {
		DailyTasksInterface dailyTasksInterface = new DailyTasksInterface();
		dailyTasksInterface.open(player);
	}

	public static void init() {
		try {
			InterfaceHandler.register(Interface.QUEST_TAB, interfaceHandler -> {
				interfaceHandler.actions[3] = (SimpleAction) JournalTab::sendPlayerInfoTab;
				interfaceHandler.actions[8] = (SimpleAction) JournalTab::sendServerInformationTab;
				interfaceHandler.actions[13] = (SimpleAction) JournalTab::sendAchievementTab;
			});
			InterfaceHandler.register(848, interfaceHandler -> {
				interfaceHandler.actions[22] = (SimpleAction) p -> p.getDropViewer().open(p);
				interfaceHandler.actions[34] = (SimpleAction) JournalTab::openDailyTasks;
				interfaceHandler.actions[23] = (SimpleAction) p -> p.getLootsViewer().updateInterface(p,
						LootsTables.ADVANCED_MYSTERY_BOX);
				interfaceHandler.actions[28] = (SimpleAction) player -> player.openUrl("Donation",
						"https://reasonps.com/store");
				interfaceHandler.actions[29] = (SimpleAction) player -> player.openUrl("Vote", "https://reasonps.com/vote");
				interfaceHandler.actions[30] = (SimpleAction) player -> player.openUrl("Wiki",
						"https://reasonps.fandom.com/wiki/ReasonPS_Wiki");
				interfaceHandler.actions[31] = (SimpleAction) player -> player.openUrl("Discord",
						"https://discord.gg/WeYBAV7S3G");
			});
			InterfaceHandler.register(1086, interfaceHandler -> {
				interfaceHandler.actions[11] = (SimpleAction) player -> {
					player.forceText("My current playtime is " + TimeToText(player));
				};
				interfaceHandler.actions[12] = (SimpleAction) player -> {
					player.forceText("I am playing on " + player.getDifficulty().Name + " mode!");
				};
				interfaceHandler.actions[13] = (SimpleAction) player -> {
					player
							.forceText("I currently have " + NumberUtils.formatNumber(player.getReasonPoints()) + " reason points.");
				};
				interfaceHandler.actions[14] = (SimpleAction) player -> {
					player.forceText(
							"I currently have " + NumberUtils.formatNumber(player.getAchievementPoints()) + " achievement points.");
				};
				interfaceHandler.actions[15] = (SimpleAction) player -> {
					player.forceText("I currently have " + NumberUtils.formatNumber(player.perkPoints) + " perk points.");
				};
				interfaceHandler.actions[16] = (SimpleAction) player -> {
					player.forceText("I currently have " + NumberUtils.formatNumber(player.votePoints) + " vote points.");
				};
				interfaceHandler.actions[17] = (SimpleAction) player -> {
					player.forceText("I currently have " + NumberUtils.formatNumber(player.donatorPoints) + " donator points.");
				};
				interfaceHandler.actions[24] = (SimpleAction) player -> {
					player.forceText("I currently have " + NumberUtils.formatNumber(VarPlayerRepository.SLAYER_POINTS.get(player))
							+ " slayer points.");
				};
				interfaceHandler.actions[32] = (SimpleAction) player -> {
					if (player.getDoubleExpRemaining() <= 0)
						player.forceText("I currently have 0 minutes left on my Double exp timer.");
					else
						player.forceText("I currently have " + NumberUtils.formatNumber(player.getDoubleExpRemaining() / 100)
								+ " minutes left on my Double exp timer.");
				};
				interfaceHandler.actions[33] = (SimpleAction) player -> {

					int delay = player.doubleDropBonus.remaining();
					int minutes = delay / 100;
					if (minutes < 0)
						minutes = 0;
					player.forceText(
							"I currently have " + NumberUtils.formatNumber(minutes) + " minutes left on my Double drops timer.");

				};
				interfaceHandler.actions[34] = (SimpleAction) player -> {
					if (player.getDropRateBoostRemaining() <= 0)
						player.forceText("I currently have 0 minutes left on damage boost scroll.");
					else
						player.forceText("I currently have " + NumberUtils.formatNumber(player.getDamageBoostRemaining() / 100)
								+ " minutes left on my damage boost scroll.");
				};
				interfaceHandler.actions[35] = (SimpleAction) player -> {
					if (player.getDamageReductionBoostRemaining() <= 0)
						player.forceText("I currently have 0 minutes left on my Damage reduction timer.");
					else
						player
								.forceText(
										"I currently have " + NumberUtils.formatNumber(player.getDamageReductionBoostRemaining() / 100)
												+ " minutes left on my Damage reduction timer.");
				};
				interfaceHandler.actions[18] = (SimpleAction) player -> {
					player.forceText(
							"I currently have a total of $" + NumberUtils.formatNumber(player.totalDonated) + ".00 donated.");
				};
				interfaceHandler.actions[36] = (SimpleAction) player -> {
					if (player.getBrewImmunityRemaining() <= 0)
						player.forceText("I currently have 0 minutes left on my Brew immunity timer.");
					else
						player.forceText("I currently have " + NumberUtils.formatNumber(player.getBrewImmunityRemaining() / 100)
								+ " minutes left on my Brew immunity timer.");
				};
				interfaceHandler.actions[37] = (SimpleAction) player -> {
					if (player.getPrayerBoostBonusRemaining() <= 0)
						player.forceText("I currently have 0 minutes left on my Prayer drain reduction timer.");
					else
						player.forceText("I currently have " + NumberUtils.formatNumber(player.getPrayerBoostBonusRemaining() / 100)
								+ " minutes left on my Prayer drain reduction timer.");
				};
				interfaceHandler.actions[102] = (SimpleAction) player -> {
					player.forceText("I have a drop rate bonus of " + NumberUtils.formatNumber(player.calculateDropRate()) + "%.");
				};
				interfaceHandler.actions[38] = (SimpleAction) player -> {
					if (player.getDropRateBoostRemaining() <= 0)
						player.forceText("I currently have 0 minutes left on my 5% Drop rate timer.");
					else
						player.forceText("I currently have " + NumberUtils.formatNumber(player.getDropRateBoostRemaining() / 100)
								+ " minutes left on my 10% Drop rate timer.");
				};
				interfaceHandler.actions[25] = (SimpleAction) player -> {
					int left = VarPlayerRepository.SLAYER_TASK_AMOUNT.get(player);
					if (player.gauntlet != null && player.gauntlet.inGauntlet) {
						player.sendMessage("You cannot use this while in the Gauntlet.");
						return;
					}
					if (player.wildernessLevel > 20) {
						player.sendMessage("This cannot be used above 20 wilderness!");
						return;
					}
					if (left < 1) {
						ModernTeleport.teleport(player, new Position(3097, 3511, 0));
						return;
					}
					if (player.slayerTaskPosition == null) {
						player.sendMessage("This task does not support this.");
						return;
					}
					if (player.taskInWilderness) {
						player.dialogue(new OptionsDialogue("Your task is in the wilderness, are you sure?",
								new Option("Yes, teleport me.", () -> {
									player.getMovement().teleport(player.slayerTaskPosition);
								}),
								new Option("Nevermind.")));
					} else {
						ModernTeleport.teleport(player, player.slayerTaskPosition);
					}
				};
				interfaceHandler.actions[26] = (SimpleAction) player -> {
					if (player.gauntlet != null && player.gauntlet.inGauntlet) {
						player.sendMessage("You cannot use this while in the Gauntlet.");
						return;
					}
					if (player.wildernessLevel > 20) {
						player.sendMessage("This cannot be used above 20 wilderness!");
						return;
					}
					if (player.bossSlayerName == null) {
						ModernTeleport.teleport(player, new Position(3099, 3508, 0));
						return;
					}
					if (player.bossSlayerPosition == null) {
						player.sendMessage("This task does not support this.");
						return;
					}
					if (player.bossTaskInWildy) {
						player.dialogue(new OptionsDialogue("Your task is in the wilderness, are you sure?",
								new Option("Yes, teleport me.", () -> {
									player.getMovement().teleport(player.bossSlayerPosition);
								}),
								new Option("Nevermind.")));
					} else {
						ModernTeleport.teleport(player, player.bossSlayerPosition);
					}
				};
				interfaceHandler.actions[44] = (SimpleAction) JournalTab::openAchievementInterface;
				interfaceHandler.actions[85] = (SimpleAction) player -> sendAchievementSection(player,
						Achievements.AchievementTypes.ELITE);
				interfaceHandler.actions[73] = (SimpleAction) player -> sendAchievementSection(player,
						Achievements.AchievementTypes.HARD);
				interfaceHandler.actions[61] = (SimpleAction) player -> sendAchievementSection(player,
						Achievements.AchievementTypes.MEDIUM);
				interfaceHandler.actions[49] = (SimpleAction) player -> sendAchievementSection(player,
						Achievements.AchievementTypes.EASY);
			});
			LoginListener.register(player -> {
				setTab(player, Tab.SUMMARY);
				player.addEvent(event -> {
					while (true) {
						updateCurrentTab(player);
						event.delay(30);
					}
				});
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
