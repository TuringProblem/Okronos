package collectionlog;

import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.model.entity.player.KillCounter;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.Item;
import io.ruin.utility.Broadcast;
import lombok.experimental.ExtensionMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ExtensionMethod(Attributes.class)
public class CollectionLogUpdated {

	CollectionLogData currentLogView;
	CollectionLogData currentLogClaimRewards;
	Categories currentCategory;
	HashMap<Integer, CollectionLogData> componentDataStore = new HashMap<>();

	public void open(Player player) {
		if (player.inTutorial)
			return;
		player.openInterface(ToplevelComponent.MAINMODAL, 1134);
		player.getPacketSender().sendString(1134, 12,
				"Collection Log - " + getTotalObtained(player) + "/" + getTotalItems());
		currentLogView = null;
		currentLogClaimRewards = null;
		updateCollectionLog(player, null);
		currentCategory = Categories.BOSSES;
		for (int i = 58; i < 298; i += 3) {
			player.getPacketSender().setHidden(1134, i, true);
		}
		sendCategory(player, currentCategory);
	}

	private int getTotalItems() {
		int totalItems = 0;
		for (CollectionLogData log : CollectionLogData.VALUES) {
			totalItems += log.getUniqueItems().length;
		}
		return totalItems;
	}

	public static int getTotalObtained(Player player) {
		int totalObtained = 0;
		for (CollectionLogData log : CollectionLogData.VALUES) {
			for (Item item : log.getUniqueItems()) {
				if (player.uniqueDrops.get(item.getId()) != null && player.uniqueDrops.get(item.getId()) > 0)
					totalObtained++;
			}
		}
		return totalObtained;
	}

	private int getSectionComponentId() {
		switch (currentCategory) {
			case BOSSES:
				return 304;
			case RAIDS:
				return 307;
			case CLUES:
				return 310;
			case MINIGAMES:
				return 313;
			case OTHER:
				return 316;

		}
		return -1;
	}

	public void sendCategory(Player player, Categories category) {
		for (int i = 58; i < 298; i += 3) {
			player.getPacketSender().setHidden(1134, i, true);
		}
		for (int i = 304; i < 317; i += 3) {
			int interfaceHash = 1134 << 16 | i;
			player.getPacketSender().sendClientScript(10622, interfaceHash);
		}
		int interfaceHash = 1134 << 16 | getSectionComponentId();
		player.getPacketSender().sendClientScript(10621, interfaceHash);
		List<CollectionLogData> entriesInCategory = new ArrayList<>();
		for (CollectionLogData entries : CollectionLogData.VALUES) {
			if (entries.getCategories() != category)
				continue;
			entriesInCategory.add(entries);
		}
		currentLogView = entriesInCategory.get(0);
		updateCollectionLog(player, currentLogView);
		int i = 58;
		int componentStringId = 60;
		int componentButtonId = 59;
		for (CollectionLogData logs : entriesInCategory) {
			player.getPacketSender().setHidden(1134, i, false);
			String name = logs.name;
			if (finishedLog(player, logs))
				name = Color.GREEN.wrap(name);
			player.getPacketSender().sendString(1134, componentStringId, name);
			if (componentDataStore.get(componentStringId) == null)
				componentDataStore.put(componentButtonId, logs);
			else
				componentDataStore.replace(componentButtonId, logs);
			componentStringId += 3;
			componentButtonId += 3;
			i += 3;
		}
	}

	private boolean finishedLog(Player player, CollectionLogData log) {
		for (Item item : log.uniqueItems) {
			if (player.uniqueDrops.get(item.getId()) == null || player.uniqueDrops.get(item.getId()) < 1) {
				return false;
			}
		}
		return true;
	}

	public void handleSectionButtonClick(Player player, int componentID) {
		switch (componentID) {
			case 304:
				currentCategory = Categories.BOSSES;
				break;
			case 307:
				currentCategory = Categories.RAIDS;
				break;
			case 310:
				currentCategory = Categories.CLUES;
				break;
			case 313:
				currentCategory = Categories.MINIGAMES;
				break;
			case 316:
				currentCategory = Categories.OTHER;
				break;
		}
		sendCategory(player, currentCategory);
	}

	public void updateCollectionLog(Player player, CollectionLogData log) {
		currentLogView = log;
		int skipped = 0;
		int row = 212;
		int i;
		int obtainedAmount = 0;
		int startItemComponent = 327;
		if (log == null) {
			player.getPacketSender().setHidden(1134, 318, true);
			return;
		}
		player.getPacketSender().sendString(1134, 321, log.name);
		int killCount = 0;
		int totalItems = 0;
		player.getPacketSender().setHidden(1134, 318, false);
		switch (currentCategory) {
			case BOSSES:
				killCount = KillCounter.getKills(log.name, player);
				switch (log) {
					case BALANCE_ELEMENTAL -> killCount = player.balanceElementalKills.getKills();
					case NEX -> killCount = player.nexKills.getKills();
					case DAGANNOTH_KINGS -> killCount = player.dagannothRexKills.getKills()
							+ player.dagannothPrimeKills.getKills() + player.dagannothSupremeKills.getKills();
				}
				break;
			case CLUES:
				switch (log) {
					case BEGINNER_CLUES -> killCount = player.beginnerClueCount;
					case EASY_CLUES -> killCount = player.easyClueCount;
					case MEDIUM_CLUES -> killCount = player.medClueCount;
					case HARD_CLUES -> killCount = player.hardClueCount;
					case ELITE_CLUES -> killCount = player.eliteClueCount;
					case MASTER_CLUES -> killCount = player.masterClueCount;
				}
				break;
			case OTHER:
				if (log == CollectionLogData.GLOUGHS_EXPERIMENTS)
					killCount = player.demonicGorillaKills.getKills();
				break;
			case RAIDS:
				switch (log) {
					case CHAMBERS_OF_XERIC -> killCount = player.chambersofXericKills.getKills();
					case THEATRE_OF_BLOOD -> killCount = player.theatreOfBloodKills.getKills();
					case TOMBS_OF_AMASCUT -> killCount = player.tombsOfAmascutKills.getKills();
					case DOMINION_OF_ECHOS -> killCount = player.dominionOfEchoesKills.getKills();
				}
				break;
			case MINIGAMES:
				player.getPacketSender().setHidden(1134, 324, true);
				break;

		}
		player.getPacketSender().sendString(1134, 323, log.name + " kills: " + "<col=FFFFFF>" + killCount);
		player.getPacketSender().setHidden(1134, 324, true);

		for (int j = 326; j < 524; j++) {
			int a = j - 326;
			if (a % 6 == 0) {
				continue;
			}
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					1134 << 16 | j, 1000 + j,
					4, 7, 1, -1, "", "", "", "", "");

			player.getPacketSender().sendItems(
					-1,
					j,
					1000 + j,
					new Item(-1));
		}
		for (Item items : log.getUniqueItems()) {
			if (totalItems > 0 && totalItems % 5 == 0) {
				startItemComponent++;
			}
			totalItems++;

			i = startItemComponent;

			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					1134 << 16 | i, 1000 + i,
					4, 7, 1, -1, "", "", "", "", "");
			if (player.uniqueDrops.get(items.getId()) == null)
				player.uniqueDrops.put(items.getId(), 0);
			if (player.uniqueDrops.get(items.getId()) > 0)
				obtainedAmount += 1;

			player.getPacketSender().sendItems(
					-1,
					i,
					1000 + i,
					new Item(items.getId(), player.uniqueDrops.get(items.getId())));
			startItemComponent++;

		}
		player.getPacketSender().sendString(1134, 322, "Obtained " + obtainedAmount + "/" + totalItems);
	}

	private void viewRewards(Player player) {
		if (currentLogView == null) {
			player.sendMessage("You must select a collection log first.");
			return;
		}

		if (currentLogView.completionRewards == null || currentLogView.completionRewards.length == 0) {
			player.sendMessage("This collection log has no rewards.");
			return;
		}

		this.currentLogClaimRewards = currentLogView;

		for (int i = 490; i < 494; i++) {
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					1134 << 16 | i, 4300 + i,
					4, 7, 1, -1, "", "", "", "", "");

			player.getPacketSender().sendItems(
					-1,
					i,
					4300 + i,
					new Item(-1));
		}
		int itemContainerComponent = 544;
		int itemContainerId = 4300;
		for (Item item : currentLogView.completionRewards) {
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					1134 << 16 | itemContainerComponent, itemContainerId,
					4, 7, 1, -1, "", "", "", "", "");

			player.getPacketSender().sendItems(
					-1,
					itemContainerComponent,
					itemContainerId,
					item);
			itemContainerComponent++;
			itemContainerId++;
			if (itemContainerComponent == 548)
				break;
		}
		player.getPacketSender().setHidden(1134, 529, false);
	}

	private void closeViewRewards(Player player) {
		this.currentLogClaimRewards = null;
		player.getPacketSender().setHidden(1134, 529, true);
	}

	enum Categories {
		BOSSES, RAIDS, CLUES, MINIGAMES, OTHER;
	}

	private boolean claimReward(Player p) {
		var logData = this.currentLogClaimRewards;
		if (logData == null) {
			p.sendMessage("You must select proper category first.");
			return false;
		}
		if (p.claimedCollectionLogs.get(logData.name) != null && p.claimedCollectionLogs.get(logData.name)) {
			p.sendMessage("You have already claimed this reward.");
			return false;
		}

		for (Item item : logData.uniqueItems) {
			if (p.uniqueDrops.get(item.getId()) == null || p.uniqueDrops.get(item.getId()) < 1) {
				p.sendMessage("You haven't yet completed this completion log.");
				return false;
			}
		}

		if (p.getInventory().getFreeSlots() < logData.completionRewards.length) {
			p.sendMessage("You don't have enough inventory space to claim this reward.");
			return false;
		}

		if (p.wildernessLevel > 0) {
			p.sendMessage("You can't claim rewards in the wilderness.");
			return false;
		}

		for (Item item : logData.completionRewards) {
			if (p.getGameMode().isUltimateIronman()) {
				p.getInventory().addOrDrop(item.getId(), item.getAmount());
			} else {
				p.getInventory().addOrSendToBank(item.getId(), item.getAmount());
			}
		}
		p.sendMessage("You have claimed your reward.");
		Broadcast.WORLD.sendNewsDropMessage(p, Icon.ADMINISTRATOR, "<col=000000>" + p.getName(),
				" has completed the " + logData.name + " collection log!");
		p.claimedCollectionLogs.put(logData.name, true);
		return true;
	}

	public static void register() {
		//@formatter:off
		InterfaceHandler.register(1134, h -> {
			h.actions[524] = (SimpleAction) (p) -> p.collectionLogUpdated().viewRewards(p);
			h.actions[304] = (SimpleAction) (player) -> player.collectionLogUpdated().handleSectionButtonClick(player, 304);
			h.actions[307] = (SimpleAction) (player) -> player.collectionLogUpdated().handleSectionButtonClick(player, 307);
			h.actions[310] = (SimpleAction) (player) -> player.collectionLogUpdated().handleSectionButtonClick(player, 310);
			h.actions[313] = (SimpleAction) (player) -> player.collectionLogUpdated().handleSectionButtonClick(player, 313);
			h.actions[316] = (SimpleAction) (player) -> player.collectionLogUpdated().handleSectionButtonClick(player, 316);
			h.actions[548] = (SimpleAction) (p) -> p.collectionLogUpdated().claimReward(p);
			h.actions[541] = (SimpleAction) (p) -> {
				p.collectionLogUpdated().closeViewRewards(p);
			};
			for (int i = 59; i < 299; i += 3) {
				int finalI = i;
				h.actions[i] = (SimpleAction) (player) -> {
					var selectedLog = player.collectionLogUpdated().componentDataStore.get(finalI);
					player.collectionLogUpdated().updateCollectionLog(player, selectedLog);
				};
			}
		});
		//@formatter:on
	}
}
