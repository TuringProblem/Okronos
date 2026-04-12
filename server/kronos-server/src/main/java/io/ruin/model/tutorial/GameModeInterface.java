package io.ruin.model.tutorial;

import io.ruin.api.utils.ListUtils;
import io.ruin.cache.Color;
import io.ruin.cache.ItemID;
import io.ruin.db.PlayerDatabase;
import io.ruin.model.entity.npc.actions.edgeville.StarterGuide;
import io.ruin.model.entity.player.Difficulty;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.SecondaryGroup;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.network.PacketSender;

import java.util.ArrayList;
import java.util.List;

public class GameModeInterface {
	//@formatter:off
	enum AccountType {
		REGULAR("Regular Account", AccountMode.REGULAR, 10545, new Item(ItemID.IRON_FULL_HELM), new Item(ItemID.IRON_PLATEBODY), new Item(ItemID.IRON_PLATELEGS)),
		IRONMAN("Standard IM", AccountMode.IRONMAN, 10544, new Item(ItemID.IRONMAN_HELM), new Item(ItemID.IRONMAN_PLATEBODY), new Item(ItemID.IRONMAN_PLATELEGS)),
		ULTIMATE_IRONMAN("Ultimate IM", AccountMode.ULTIMATE_IRONMAN, 10543, new Item(ItemID.ULTIMATE_IRONMAN_HELM), new Item(ItemID.ULTIMATE_IRONMAN_PLATEBODY), new Item(ItemID.ULTIMATE_IRONMAN_PLATELEGS)),
		HARDCORE_IRONMAN("Hardcore IM", AccountMode.HARDCORE_IRONMAN, 10542, new Item(ItemID.HARDCORE_IRONMAN_HELM), new Item(ItemID.HARDCORE_IRONMAN_PLATEBODY), new Item(ItemID.HARDCORE_IRONMAN_PLATELEGS)),
		GROUP_IRONMAN("Group IM", AccountMode.GROUP_IRONMAN, 10541, new Item(26156), new Item(26158),	new Item(26166)),
		HARDCORE_GROUP_IRONMAN("Hardcore Group IM", AccountMode.HARDCORE_GROUP_IRONMAN, 10540, new Item(26170), new Item(26172), new Item(26180));

		private String name;
		private AccountMode accountMode;
		private int scriptId;
		Item helmet;
		Item platebody;
		Item platelegs;

		AccountType(String name, AccountMode accountMode, int scriptId, Item helmet, Item platebody, Item platelegs) {
			this.accountMode = accountMode;
			this.name = name;
			this.scriptId = scriptId;
			this.helmet = helmet;
			this.platebody = platebody;
			this.platelegs = platelegs;
		}
	}
	//@formatter:on

	enum DifficultyType {
		EASY(Color.GREEN.wrap("Easy"), Difficulty.EASY),
		INTERMEDIATE(Color.YELLOW.wrap("Intermediate"), Difficulty.INTERMEDIATE),
		HARD(Color.ORANGE.wrap("Hard"), Difficulty.HARD),
		EXTREME(Color.RED.wrap("Extreme"), Difficulty.EXTREME);

		private String name;
		private Difficulty difficulty;

		DifficultyType(String name, Difficulty difficulty) {
			this.name = name;
			this.difficulty = difficulty;
		}
	}

	enum AccountMode {
		REGULAR,
		IRONMAN,
		ULTIMATE_IRONMAN,
		HARDCORE_IRONMAN,
		GROUP_IRONMAN,
		HARDCORE_GROUP_IRONMAN
	}

	static final int IRONMAN_MODE_INTERFACE = 1100;
	static final int DIFFICULTY_MODE_INTERFACE = 1101;
	static final int PRESSED_BUTTON = 699;
	static final int UNPRESSED_BUTTON = 697;
	static int ironmanSettings = 0;
	static int difficultySettings = 0;

	public static void handleButtonClick(Player player, int componentID, int scriptID) {
		PacketSender ps = player.getPacketSender();
		ps.sendClientScript(scriptID, UNPRESSED_BUTTON, PRESSED_BUTTON, componentID);

		if (scriptID == 10206)
			difficultySettings = componentID;
		else if (scriptID == 10205)
			ironmanSettings = componentID;

		switch (difficultySettings) {
			case 28:
				hideInfoText(player, false);
				ps.sendString(883, 55, "Fast experience rates.");
				// ps.sendString(883, 54, "Can reach end game<br>content quicker than that <br>
				// of the harder modes.");
				break;
			case 29:
				hideInfoText(player, false);
				ps.sendString(883, 55, "Slight increase in <br>        Drop Rate<br>.");
				// ps.sendString(883, 54, "Drop rates increased slightly.");
				break;
			case 30:
				hideInfoText(player, false);
				ps.sendString(883, 55, "Noticeable increase in <br>           Drop Rate<br>.");
				// ps.sendString(883, 54, "Drop rates increased noticeably.");
				break;
			case 31:
				hideInfoText(player, false);
				ps.sendString(883, 55, "Significant increase in <br>        Drop Rate");
				// ps.sendString(883, 54, "Drop rates increased<br>" + "SIGNIFICANTLY");
				break;
		}
		switch (ironmanSettings) {
			case 16: // regular acc
				ps.setHidden(883, 52, false);
				ps.sendString(883, 53, "No ironman restrictions.");
				break;
			case 17:// regular iron
				ps.setHidden(883, 53, false);
				ps.sendString(883, 53, "Restrictions:<br>You Can't Trade");
				break;
			case 18: // ult iron
				ps.setHidden(883, 53, false);
				ps.sendString(883, 53, "Restrictions:<br>Can't Bank.<br>Can't Trade.");
				break;
			case 19:// regular group iron
				ps.setHidden(883, 53, false);
				ps.sendString(883, 53, "Perks:<br>Can Trade GIM.<br>(only in group)<br>GIM Storage.");
				break;
			case 20: // hardcore group iron
				ps.setHidden(883, 53, false);
				ps.sendString(883, 53,
					"Perks:<br>Can Trade GIM.<br>(only in group)<br>GIM Storage<br>Restrictions:<br>Can lost lives");
				break;
			case 58: // hardcore ironman
				ps.setHidden(883, 53, false);
				ps.sendString(883, 53, "Restrictions.<br>Only 1 Life.");
				break;

		}
	}

	public static void hideInfoText(Player player, boolean hidden) {
		PacketSender ps = player.getPacketSender();
		ps.setHidden(883, 55, hidden);
		ps.setHidden(883, 54, hidden);
		ps.setHidden(883, 53, hidden);
	}

	public static void confirmChoice(Player player) {
		if (ironmanSettings == 0 || difficultySettings == 0) {
			player.sendMessage("You must pick a difficulty mode and an ironman setting to confirm!");
			return;
		}
		player.closeInterfaces();
		StarterGuide.continueTutorial(player);
		switch (difficultySettings) {
			case 28:
				player.setDifficulty(Difficulty.EASY);
				break;
			case 29:
				player.setDifficulty(Difficulty.INTERMEDIATE);
				break;
			case 30:
				player.setDifficulty(Difficulty.HARD);
				break;
			case 31:
				player.setDifficulty(Difficulty.EXTREME);
				break;
		}
		switch (ironmanSettings) {
			case 16:
				GameMode.changeForumsGroup(player, GameMode.STANDARD.groupId);
				break;
			case 17:
				VarPlayerRepository.IRONMAN_MODE.set(player, 1);
				GameMode.changeForumsGroup(player, GameMode.IRONMAN.groupId);
				break;
			case 18:
				VarPlayerRepository.IRONMAN_MODE.set(player, 2);
				GameMode.changeForumsGroup(player, GameMode.ULTIMATE_IRONMAN.groupId);
				break;
			case 58:
				VarPlayerRepository.IRONMAN_MODE.set(player, 3);
				GameMode.changeForumsGroup(player, GameMode.HARDCORE_IRONMAN.groupId);
				break;
			case 19:
				VarPlayerRepository.IRONMAN_MODE.set(player, 4);
				GameMode.changeForumsGroup(player, GameMode.GROUP_IRONMAN.groupId);
				break;
			case 20:
				VarPlayerRepository.IRONMAN_MODE.set(player, 5);
				GameMode.changeForumsGroup(player, GameMode.HARDCORE_GROUP_IRONMAN.groupId);
				break;
		}
		PlayerDatabase.insertQueue(player);
	}

	public static void register() {
		InterfaceHandler.register(IRONMAN_MODE_INTERFACE, h -> {
			h.actions[125] = (SimpleAction) p -> p.getGameModeInterface().confirmIronmanSettings(p);
			h.actions[118] = (SimpleAction) p -> p.getGameModeInterface().dismissContinueIronmanWindow(p);
			h.actions[28] = (SimpleAction) p -> {
				p.sendMessage("pressed");
				p.getGameModeInterface().changeIronType(p, true);
			};
			h.actions[27] = (SimpleAction) p -> p.getGameModeInterface().changeIronType(p, false);
			h.actions[33] = (SimpleAction) p -> p.getGameModeInterface().continueIronmanSettings(p);

		});
		InterfaceHandler.register(DIFFICULTY_MODE_INTERFACE, h -> {
			h.actions[124] = (SimpleAction) p -> p.getGameModeInterface().confirmAccountCreation(p);
			h.actions[117] = (SimpleAction) p -> p.getGameModeInterface().closeDifficultyPopupWarning(p);
			h.actions[28] = (SimpleAction) p -> p.getGameModeInterface().changeDifficultyType(p, true);
			h.actions[27] = (SimpleAction) p -> p.getGameModeInterface().changeDifficultyType(p, false);
			h.actions[32] = (SimpleAction) p -> p.getGameModeInterface().sendDifficultyPopupWarning(p);

		});

		LoginListener.register(p -> {
			if (p.currentDifficulty == null) {
				if (!p.newPlayer) {
					if (p.xpMode != null) {
						switch (p.xpMode) {
							case EASY:
								p.setDifficulty(Difficulty.EASY);
								break;
							case MEDIUM:
							case IRON:
								p.setDifficulty(Difficulty.INTERMEDIATE);
								break;
							case HARD:
								p.setDifficulty(Difficulty.HARD);
								break;
							case OSRS:
								p.setDifficulty(Difficulty.OSRS);
								break;
						}
					}
				}
			}
		});
	}

	private static String getModeDescription(AccountType currentAccountType) {
		switch (currentAccountType) {
			case REGULAR:
				return "You will have no account restrictions on this mode, and you can trade with other players.";
			case IRONMAN:
				return "Ironman Mode in Reason is a self-sufficient game" +
					" mode where players cannot trade with others" +
					" or have any in-game assistance." +
					"<br>The ultimate goal is to complete all game content without outside help.";
			case ULTIMATE_IRONMAN:
				return "Ultimate Ironman Mode in Reason is a self-sufficient game" +
					" mode where players cannot trade with others, bank" +
					" or have any in-game assistance." +
					"<br>The lack of banking makes this mode quite the challenge.";
			case HARDCORE_IRONMAN:
				return "Hardcore Ironman Mode in Reason is a self-sufficient game" +
					" mode where players cannot trade with others" +
					" or have any in-game assistance." +
					"<br>What makes this mode different is you have only one life. If you die, your account will be reverted to a regular IM.";
			case GROUP_IRONMAN:
				return "Group Ironman Mode in Reason" +
					" is a cooperative game mode for up to five players." +
					" Players can trade with each other, gather resources together," +
					" and progress through the game as a team.<br>The goal is to complete all" +
					" content available in the game without outside help, while enjoying the" +
					" social aspects of playing with others.";
			case HARDCORE_GROUP_IRONMAN:
				return "Hardcore Group Ironman Mode in Reason is similar to Group Ironman Mode," +
					" but with an added challenge." +
					" Your group only has 5 lives, if you lose them all you become a regular iron man group." +
					" This mode requires careful planning and coordination among group members." +
					"<br>The goal is still to complete all game content without outside help," +
					" but with the added risk of losing your hardcore status.";
		}
		return null;
	}

	transient ItemContainer container;
	transient List<Item> items = new ArrayList<>();
	transient AccountType currentAccountType;
	transient DifficultyType currentDifficultyType;
	public transient int stage = 1;

	public void openIronmanSettingsInterface(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, IRONMAN_MODE_INTERFACE);
		player.getPacketSender().sendString(IRONMAN_MODE_INTERFACE, 46, player.getName());
		currentAccountType = AccountType.REGULAR;
		updateIronmanSettings(player);
		stage = 1;
	}

	public void continueIronmanSettings(Player player) {
		player.getPacketSender().setHidden(IRONMAN_MODE_INTERFACE, 106, false);
		int interfaceHash = 1100 << 16 | 123;
		player.getPacketSender().sendClientScript(currentAccountType.scriptId, "I", interfaceHash);
		player.getPacketSender().sendString(IRONMAN_MODE_INTERFACE, 119, currentAccountType.name);
	}

	public void confirmIronmanSettings(Player player) {
		player.getEquipment().clear();
		player.closeInterface(ToplevelComponent.MAINMODAL);
		player.startEvent(e -> {
			e.delay(2);
			openDifficultyModeInterface(player);
		});
	}

	public void changeDifficultyType(Player player, boolean right) {
		if (right) {
			if (currentDifficultyType.ordinal() == DifficultyType.values().length - 1) {
				currentDifficultyType = DifficultyType.values()[0];
			} else {
				currentDifficultyType = DifficultyType.values()[currentDifficultyType.ordinal() + 1];
			}
		} else {
			if (currentDifficultyType.ordinal() == 0) {
				currentDifficultyType = DifficultyType.values()[DifficultyType.values().length - 1];
			} else {
				currentDifficultyType = DifficultyType.values()[currentDifficultyType.ordinal() - 1];
			}
		}
		updateDifficultyModeInterface(player);
	}

	public void sendDifficultyPopupWarning(Player player) {
		player.getPacketSender().setHidden(DIFFICULTY_MODE_INTERFACE, 105, false);
		int interfaceHash = 1101 << 16 | 122;
		player.getPacketSender().sendClientScript(currentAccountType.scriptId, "I", interfaceHash);
		player.getPacketSender().sendString(DIFFICULTY_MODE_INTERFACE, 118, sendDifficultyTitle(currentDifficultyType));
	}

	public void closeDifficultyPopupWarning(Player player) {
		player.getPacketSender().setHidden(DIFFICULTY_MODE_INTERFACE, 105, true);
	}

	public void confirmAccountCreation(Player player) {
		player.closeInterface(ToplevelComponent.MAINMODAL);
		player.getEquipment().clear();
		player.setDifficulty(currentDifficultyType.difficulty);
		switch (currentAccountType) {
			case REGULAR:
				GameMode.changeForumsGroup(player, GameMode.STANDARD.groupId);
				break;
			case IRONMAN:
				VarPlayerRepository.IRONMAN_MODE.set(player, 1);
				GameMode.changeForumsGroup(player, GameMode.IRONMAN.groupId);
				break;
			case ULTIMATE_IRONMAN:
				VarPlayerRepository.IRONMAN_MODE.set(player, 2);
				GameMode.changeForumsGroup(player, GameMode.ULTIMATE_IRONMAN.groupId);
				break;
			case HARDCORE_IRONMAN:
				VarPlayerRepository.IRONMAN_MODE.set(player, 3);
				GameMode.changeForumsGroup(player, GameMode.HARDCORE_IRONMAN.groupId);
				break;
			case GROUP_IRONMAN:
				VarPlayerRepository.IRONMAN_MODE.set(player, 4);
				GameMode.changeForumsGroup(player, GameMode.GROUP_IRONMAN.groupId);
				break;
			case HARDCORE_GROUP_IRONMAN:
				VarPlayerRepository.IRONMAN_MODE.set(player, 5);
				GameMode.changeForumsGroup(player, GameMode.HARDCORE_GROUP_IRONMAN.groupId);
				break;
		}
		player.setSecondaryGroups(ListUtils.toList(SecondaryGroup.NONE.id));
		StarterGuide.continueTutorial(player);

	}

	public void dismissContinueIronmanWindow(Player player) {
		player.getPacketSender().setHidden(IRONMAN_MODE_INTERFACE, 106, true);
	}

	public void changeIronType(Player player, boolean right) {
		if (right) {
			if (currentAccountType.ordinal() == AccountType.values().length - 1) {
				currentAccountType = AccountType.values()[0];
			} else {
				currentAccountType = AccountType.values()[currentAccountType.ordinal() + 1];
			}
		} else {
			if (currentAccountType.ordinal() == 0) {
				currentAccountType = AccountType.values()[AccountType.values().length - 1];
			} else {
				currentAccountType = AccountType.values()[currentAccountType.ordinal() - 1];
			}
		}
		updateIronmanSettings(player);
	}

	public void open(Player player) {

		PacketSender ps = player.getPacketSender();
		// player.openInterface(ToplevelComponent.MAINMODAL, INTERFACE_ID);

		// for(int i = 52; i < 56; i++)
		// ps.setHidden(INTERFACE_ID, i, true);

	}

	private void updateIronmanSettings(Player player) {
		player.getPacketSender().sendString(IRONMAN_MODE_INTERFACE, 47, currentAccountType.name);
		player.getPacketSender().sendString(IRONMAN_MODE_INTERFACE, 26, currentAccountType.name);
		int interfaceHash = 1100 << 16 | 30;
		player.getPacketSender().sendClientScript(currentAccountType.scriptId, "I", interfaceHash);
		String modeDescription = getModeDescription(currentAccountType);
		player.getPacketSender().sendString(IRONMAN_MODE_INTERFACE, 29, modeDescription);
		updateIronModeInventory(player);
		updateIronModeEquipment(player);

	}

	private void updateIronModeInventory(Player player) {
		int startingComponent = 57;
		int startingContainer = 1000;
		for (Item item : items) {
			player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1100 << 16 | startingComponent, startingContainer,
				4, 7, 1, -1, "", "", "", "", "");
			player.getPacketSender().sendItems(
				-1,
				startingComponent,
				startingContainer,
				new Item(-1));
			startingComponent++;
			startingContainer++;
		}
		getModeItems();
		startingComponent = 57;
		startingContainer = 1000;
		for (Item item : items) {
			player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1100 << 16 | startingComponent, startingContainer,
				4, 7, 1, -1, "", "", "", "", "");
			player.getPacketSender().sendItems(
				-1,
				startingComponent,
				startingContainer,
				item);
			startingComponent++;
			startingContainer++;
		}

	}

	private void updateIronModeEquipment(Player player) {
		player.getEquipment().set(0, currentAccountType.helmet);
		player.getEquipment().set(4, currentAccountType.platebody);
		player.getEquipment().set(7, currentAccountType.platelegs);
		player.getEquipment().set(2, new Item(1704));
		player.getEquipment().set(3, new Item(ItemID.IRON_SCIMITAR));
		player.getEquipment().set(10, new Item(ItemID.CLIMBING_BOOTS));
	}

	private void openDifficultyModeInterface(Player player) {
		stage = 2;
		player.openInterface(ToplevelComponent.MAINMODAL, DIFFICULTY_MODE_INTERFACE);
		player.getPacketSender().sendString(DIFFICULTY_MODE_INTERFACE, 45, player.getName());
		currentDifficultyType = DifficultyType.EASY;
		updateDifficultyModeInterface(player);

	}

	private void updateDifficultyModeInterface(Player player) {
		player.getPacketSender().sendString(DIFFICULTY_MODE_INTERFACE, 46,
			currentAccountType.name + "<br>" + currentDifficultyType.name);
		player.getPacketSender().sendString(DIFFICULTY_MODE_INTERFACE, 29, getDifficultyDescription(currentDifficultyType));
		player.getPacketSender().sendString(DIFFICULTY_MODE_INTERFACE, 26, sendDifficultyTitle(currentDifficultyType));
		updateDifficultyEquipment(player);
		sendDifficultyModeItems(player);
	}

	private void updateDifficultyEquipment(Player player) {
		player.getEquipment().set(0, currentAccountType.helmet);
		player.getEquipment().set(4, currentAccountType.platebody);
		player.getEquipment().set(7, currentAccountType.platelegs);
		player.getEquipment().set(10, new Item(ItemID.CLIMBING_BOOTS));
		switch (currentDifficultyType) {
			case EXTREME:
				player.getEquipment().set(2, new Item(30477));
				player.getEquipment().set(3, new Item(30479));
				break;
			case HARD:
				player.getEquipment().set(3, new Item(ItemID.IRON_SCIMITAR));
				player.getEquipment().set(2, new Item(30477));
				break;
			default:
				player.getEquipment().set(2, new Item(30477));
				player.getEquipment().set(3, new Item(ItemID.IRON_SCIMITAR));
				break;
		}

	}

	private void sendDifficultyModeItems(Player player) {
		int startingComponent = 56;
		int startingContainer = 1000;
		for (Item item : items) {
			player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1101 << 16 | startingComponent, startingContainer,
				4, 7, 1, -1, "", "", "", "", "");
			player.getPacketSender().sendItems(
				-1,
				startingComponent,
				startingContainer,
				new Item(-1));
			startingComponent++;
			startingContainer++;
		}
		getModeItems();
		items.add(new Item(30477));
		items.add(new Item(30588));
		items.add(new Item(30589));
		switch (currentDifficultyType) {
			case EXTREME:
				items.add(new Item(30478));
				items.add(new Item(30479));
				items.add(new Item(30480));
				break;
		}
		startingComponent = 56;
		startingContainer = 1000;
		for (Item item : items) {
			player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1101 << 16 | startingComponent, startingContainer,
				4, 7, 1, -1, "", "", "", "", "");
			player.getPacketSender().sendItems(
				-1,
				startingComponent,
				startingContainer,
				item);
			startingComponent++;
			startingContainer++;
		}
	}

	private String sendDifficultyTitle(DifficultyType difficultyType) {
		switch (difficultyType) {
			case EASY:
				return "<col=47b489>Easy Mode <col=16E625>(100x)";
			case INTERMEDIATE:
				return "<col=47b489>Intermediate <col=C9C80E>(25x)";
			case HARD:
				return "<col=47b489>Hard Mode <col=FFA500>(10x)";
			case EXTREME:
				return "<col=47b489>Extreme Mode <col=FF0000>(3x)";
		}
		return null;
	}

	private void getModeItems() {
		items.clear();
		items.add(new Item(995, 500000));
		items.add(new Item(562, 500));
		items.add(new Item(558, 1000));
		items.add(new Item(554, 10000));
		items.add(new Item(557, 10000));
		items.add(new Item(555, 10000));
		items.add(new Item(556, 10000));
		items.add(new Item(884, 10000));
		items.add(new Item(6108, 1));
		items.add(new Item(ItemID.STAFF_OF_AIR, 1));
		items.add(new Item(6107, 1));
		items.add(new Item(6109, 1));
		items.add(new Item(841, 1));
		items.add(new Item(1095, 1));
		items.add(new Item(1129, 1));
		items.add(new Item(1167, 1));
		items.add(new Item(1704, 1));

		switch (currentAccountType) {
			case REGULAR:
				items.add(new Item(1265, 1));
				items.add(new Item(1323, 1));
				items.add(new Item(ItemID.LOBSTER + 1, 300));
				break;
			case IRONMAN:
				items.add(new Item(ItemID.IRONMAN_HELM, 1));
				items.add(new Item(ItemID.IRONMAN_PLATEBODY, 1));
				items.add(new Item(ItemID.IRONMAN_PLATELEGS, 1));
				items.add(new Item(ItemID.LOBSTER + 1, 300));
				break;
			case ULTIMATE_IRONMAN:
				items.add(new Item(ItemID.ULTIMATE_IRONMAN_HELM, 1));
				items.add(new Item(ItemID.ULTIMATE_IRONMAN_PLATEBODY, 1));
				items.add(new Item(ItemID.ULTIMATE_IRONMAN_PLATELEGS, 1));
				items.add(new Item(ItemID.LOBSTER + 1, 300));
				break;
			case HARDCORE_IRONMAN:
				items.add(new Item(ItemID.HARDCORE_IRONMAN_HELM, 1));
				items.add(new Item(ItemID.HARDCORE_IRONMAN_PLATEBODY, 1));
				items.add(new Item(ItemID.HARDCORE_IRONMAN_PLATELEGS, 1));
				items.add(new Item(ItemID.LOBSTER + 1, 300));
				break;
			case GROUP_IRONMAN:
				items.add(new Item(26156, 1));
				items.add(new Item(26158, 1));
				items.add(new Item(26166, 1));
				items.add(new Item(ItemID.LOBSTER + 1, 300));
				break;
			case HARDCORE_GROUP_IRONMAN:
				items.add(new Item(26170, 1));
				items.add(new Item(26172, 1));
				items.add(new Item(26180, 1));
				items.add(new Item(ItemID.LOBSTER + 1, 300));
				break;

		}
	}

	private String getDifficultyDescription(DifficultyType currentDifficultyType) {
		switch (currentDifficultyType) {
			case EASY:
				return "This is reasons easiest difficulty rate," +
					" at a staggering 100x experience rate you" +
					" will be able to access end game content faster" +
					" than other modes, but your drop rates will be penalised.";
			case INTERMEDIATE:
				return "On intermediate mode your experience rates will be 25x, on this" +
					" mode you will have slightly better drop rates than easy mode and" +
					" you will also receive more reason points.";
			case HARD:
				return "On hard mode your experience rates will be 10x, on this" +
					" mode you will have better drop rates than intermediate mode," +
					" you will also receive more reason points and you will get access to the training amulet.";
			case EXTREME:
				return "On extreme mode your experience rates will be 3x, on this" +
					" mode you will have significantly better drop rates than hard mode," +
					" you will also receive more reason points and you will get access to the training amulet," +
					" training sword, training staff and training bow.";
		}
		return null;
	}
}
