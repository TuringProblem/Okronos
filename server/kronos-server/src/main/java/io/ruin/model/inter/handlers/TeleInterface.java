package io.ruin.model.inter.handlers;

import io.ruin.model.activities.DonationBossHandler;
import io.ruin.model.activities.bosses.instancetoken.InstanceMaps;
import io.ruin.model.activities.newcomertasks.NewcomerTasks;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.AccessMasks;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;
import io.ruin.model.map.Position;
import io.ruin.model.skills.magic.spells.modern.ModernTeleport;

import java.util.ArrayList;

public class TeleInterface extends ItemContainer {
	public static final int INTERFACE_ID = 851;
	public transient Categories currentCategory;
	public transient SkillingSubSections currentSkillingSection;
	public transient ServerTeleports currentTeleport;
	transient int pageNumber = 1;
	transient int maxPageNumber = 3;
	transient boolean opened = false;
	transient int currentComponentId = 0;

	public void init() {
	}

	public void open(Player player) {
		if (opened) {
			player.openInterface(ToplevelComponent.MAINMODAL, INTERFACE_ID);
			sendFavourites(player);
			if (currentCategory != null) {
				switch (currentCategory) {
					case SKILLING:
						sendSkillingSection(player);
						break;
					case BOSSES:
						sendBossTeleports(player);
						break;
					case MINIGAMES:
						sendMiningTeleports(player);
						break;
					case WILDERNESS:
						sendWildernessTeleports(player);
						break;
					case TRAINING:
						sendTrainingTeleports(player);
						break;
					case CITIES:
						sendCityTeleports(player);
						break;
					default:
						break;
				}
			}
			if (currentTeleport != null)
				sendTeleport(player, currentComponentId);
			return;
		}
		opened = true;

		this.init(player, 12, -1, 64161, 141, false);
		player.openInterface(ToplevelComponent.MAINMODAL, INTERFACE_ID);
		player.getPacketSender().setHidden(851, 152, true);
		player.getPacketSender().setHidden(851, 153, true);
		player.getPacketSender().setHidden(851, 60, true);
		player.getPacketSender().setHidden(851, 61, true);
		player.getPacketSender().setHidden(851, 62, true);
		player.getPacketSender().setHidden(851, 63, true);
		player.getPacketSender().setHidden(851, 65, true);
		player.getPacketSender().setHidden(851, 66, true);
		player.getPacketSender().setHidden(851, 155, true);
		this.clear();
		this.sendAll = true;
		this.sendUpdates();
		sendFavourites(player);
		player.set("teleportInterfacePageNumber", 1);
		player.getPacketSender().setHidden(851, 105, true);
		// currentCategory = null;
		// currentTeleport = null;
		// currentSkillingSection = null;
		int interfaceHash = 851 << 16 | 59;
		int interfaceHash2 = 851 << 16 | 60;
		player.getPacketSender().sendClientScript(10609, interfaceHash, 2215);
		// player.getPacketSender().sendClientScript(10609, interfaceHash2, 2215);
		player.getPacketSender().sendNpcHead(851, 60, 2215);
		player.getPacketSender().sendNpcHead(851, 59, 1);

	}

	public void search(Player player, String input) {
		player.searchTeles = new ArrayList<>();
		currentCategory = Categories.SEARCH;
		for (int i = 22; i < 34; i++) {
			player.getPacketSender().setHidden(851, i, true);
		}
		for (ServerTeleports t : ServerTeleports.VALUES) {
			if (t.name.toLowerCase().contains(input.toLowerCase())) {
				player.searchTeles.add(t);
				continue;
			}
		}
		if (player.searchTeles.isEmpty()) {
			player.sendMessage("Your search found no results.");
		}
		else {
			for (int i = 0; i < player.searchTeles.size(); i++) {
				int currentCompId = 22;
				player.getPacketSender().setHidden(851, currentCompId + i, false);
			}
			int t = 22;
			for (ServerTeleports teles : player.searchTeles) {
				player.getPacketSender().sendString(851, t, teles.name + "");
				if (t == 33)
					break;
				t++;
			}
			player.getPacketSender().sendString(851, 20, "Search");
			player.getPacketSender().setHidden(851, 38, true);
			player.getPacketSender().setHidden(851, 152, false);
			player.getPacketSender().setHidden(851, 153, false);
		}
	}

	public void sendBossTeleports(Player player) {
		activateHiddenTeleportNames(player);
		player.getPacketSender().sendString(851, 20, "Bosses");
		player.getPacketSender().setHidden(851, 38, true);
		player.getPacketSender().setHidden(851, 152, false);
		player.getPacketSender().setHidden(851, 153, false);
		player.getPacketSender().setHidden(851, 28, false);
		player.getPacketSender().setHidden(851, 29, false);
		player.getPacketSender().setHidden(851, 30, false);
		player.getPacketSender().setHidden(851, 31, false);
		player.getPacketSender().setHidden(851, 32, false);
		player.getPacketSender().setHidden(851, 33, false);
		player.getPacketSender().sendString(851, 22, ServerTeleports.GWD.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.VORKATH.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.ZULRAH.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.GALVEK.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.CORP.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.KQ.name);
		player.getPacketSender().sendString(851, 28, ServerTeleports.DKS.name);
		player.getPacketSender().sendString(851, 29, ServerTeleports.KRAKEN.name);
		player.getPacketSender().sendString(851, 30, ServerTeleports.HYDRA.name);
		player.getPacketSender().sendString(851, 31, ServerTeleports.CERBERUS.name);
		player.getPacketSender().sendString(851, 32, ServerTeleports.SMOKE_DEVIL.name);
		player.getPacketSender().sendString(851, 33, ServerTeleports.SIRE.name);
		currentCategory = Categories.BOSSES;
		pageNumber = 1;
	}

	public void sendBossTeleportsPageTwo(Player player) {
		player.getPacketSender().setHidden(851, 30, false);
		player.getPacketSender().setHidden(851, 31, false);
		player.getPacketSender().setHidden(851, 32, false);
		player.getPacketSender().setHidden(851, 33, false);
		player.getPacketSender().sendString(851, 22, ServerTeleports.VENENATIS.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.CALLISTO.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.VETION.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.KBD.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.GIANT_MOLE.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.SKOTIZO.name);
		player.getPacketSender().sendString(851, 28, ServerTeleports.DEMONIC_GORILLA.name);
		player.getPacketSender().sendString(851, 29, ServerTeleports.ARGENTAVIS.name);
		player.getPacketSender().sendString(851, 30, ServerTeleports.NEX.name);
		player.getPacketSender().sendString(851, 31, ServerTeleports.NIGHTMARE.name);
		player.getPacketSender().sendString(851, 32, ServerTeleports.SARACHNIS.name);
		player.getPacketSender().sendString(851, 33, ServerTeleports.OPHIDIA.name);
		pageNumber = 2;
	}

	public void sendBossTeleportsPageThree(Player player) {
		pageNumber = 3;
		activateHiddenTeleportNames(player);
		player.getPacketSender().sendString(851, 22, ServerTeleports.PHANTOM_MUSPAH.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.LEVIATHAN.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.DUKE_SUCELLUS.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.VARDORVIS.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.WHISPERER.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.MALAKAR.name);
		player.getPacketSender().sendString(851, 28, ServerTeleports.SOL_HEREDIT.name);
		player.getPacketSender().sendString(851, 29, ServerTeleports.GROTESQUE_GUARDIANS.name);
		player.getPacketSender().sendString(851, 30, ServerTeleports.ARAXXOR.name);
		player.getPacketSender().sendString(851, 31, ServerTeleports.SCURRIUS.name);
		player.getPacketSender().sendString(851, 32, ServerTeleports.BALANCE_ELEMENTAL.name);
		player.getPacketSender().setHidden(851, 33, true);
	}

	public void removeFromFavourites(Player player, int componentId) {
		String teleportRemoved = "";
		switch (componentId) {
			case 91:
				teleportRemoved = "" + player.teleportFavourites.get(0).name;
				player.teleportFavourites.remove(0);
				break;
			case 92:
				teleportRemoved = "" + player.teleportFavourites.get(1).name;
				player.teleportFavourites.remove(1);
				break;
			case 93:
				teleportRemoved = "" + player.teleportFavourites.get(2).name;
				player.teleportFavourites.remove(2);
				break;
			case 94:
				teleportRemoved = "" + player.teleportFavourites.get(3).name;
				player.teleportFavourites.remove(3);
				break;
			case 95:
				teleportRemoved = "" + player.teleportFavourites.get(4).name;
				player.teleportFavourites.remove(4);
				break;
		}
		player.sendMessage("You have removed " + teleportRemoved + " from your favourites.");
		sendFavourites(player);
	}

	public void addToFavourites(Player player) {
		player.sendMessage("we activated");
		if (currentTeleport == null) {
			player.sendMessage("You need to select a teleport before favouriting it!");
			return;
		}
		if (player.teleportFavourites.contains(currentTeleport)) {
			player.sendMessage("You already have this teleport favourited!");
			return;
		}
		if (player.teleportFavourites.size() >= 5) {
			player.sendMessage("You already have the maximum amount of favourites.");
			return;
		}
		player.teleportFavourites.add(currentTeleport);
		sendFavourites(player);
		player.sendMessage("You have added " + currentTeleport.name + " to your favourites.");
	}

	private void sendFavourites(Player player) {
		if (player.teleportFavourites == null) {
			player.getPacketSender().setHidden(851, 88, true);
			player.getPacketSender().setHidden(851, 89, true);
			player.getPacketSender().setHidden(851, 90, true);
			player.getPacketSender().setHidden(851, 91, true);
			player.getPacketSender().setHidden(851, 92, true);
			player.getPacketSender().setHidden(851, 93, true);
			player.getPacketSender().setHidden(851, 94, true);
			player.getPacketSender().setHidden(851, 95, true);
			player.getPacketSender().setHidden(851, 96, true);
			player.getPacketSender().setHidden(851, 97, true);
		}
		else {
			switch (player.teleportFavourites.size()) {
				case 0:
					player.getPacketSender().setHidden(851, 88, true);
					player.getPacketSender().setHidden(851, 89, true);
					player.getPacketSender().setHidden(851, 90, true);
					player.getPacketSender().setHidden(851, 91, true);
					player.getPacketSender().setHidden(851, 92, true);
					player.getPacketSender().setHidden(851, 93, true);
					player.getPacketSender().setHidden(851, 94, true);
					player.getPacketSender().setHidden(851, 95, true);
					player.getPacketSender().setHidden(851, 96, true);
					player.getPacketSender().setHidden(851, 97, true);
					break;
				case 1:
					player.getPacketSender().sendString(851, 88, player.teleportFavourites.get(0).name);
					player.getPacketSender().setHidden(851, 88, false);
					player.getPacketSender().setHidden(851, 92, true);
					player.getPacketSender().setHidden(851, 91, true);
					player.getPacketSender().setHidden(851, 90, true);
					player.getPacketSender().setHidden(851, 89, true);
					player.getPacketSender().setHidden(851, 93, false);
					player.getPacketSender().setHidden(851, 94, true);
					player.getPacketSender().setHidden(851, 95, true);
					player.getPacketSender().setHidden(851, 96, true);
					player.getPacketSender().setHidden(851, 97, true);
					break;
				case 2:
					player.getPacketSender().sendString(851, 88, player.teleportFavourites.get(0).name);
					player.getPacketSender().sendString(851, 89, player.teleportFavourites.get(1).name);
					player.getPacketSender().setHidden(851, 88, false);
					player.getPacketSender().setHidden(851, 89, false);
					player.getPacketSender().setHidden(851, 92, true);
					player.getPacketSender().setHidden(851, 91, true);
					player.getPacketSender().setHidden(851, 90, true);
					player.getPacketSender().setHidden(851, 93, false);
					player.getPacketSender().setHidden(851, 94, false);
					player.getPacketSender().setHidden(851, 95, true);
					player.getPacketSender().setHidden(851, 96, true);
					player.getPacketSender().setHidden(851, 97, true);
					break;
				case 3:
					player.getPacketSender().sendString(851, 88, player.teleportFavourites.get(0).name);
					player.getPacketSender().sendString(851, 89, player.teleportFavourites.get(1).name);
					player.getPacketSender().sendString(851, 90, player.teleportFavourites.get(2).name);
					player.getPacketSender().setHidden(851, 88, false);
					player.getPacketSender().setHidden(851, 89, false);
					player.getPacketSender().setHidden(851, 90, false);
					player.getPacketSender().setHidden(851, 92, true);
					player.getPacketSender().setHidden(851, 91, true);
					player.getPacketSender().setHidden(851, 93, false);
					player.getPacketSender().setHidden(851, 94, false);
					player.getPacketSender().setHidden(851, 95, false);
					player.getPacketSender().setHidden(851, 96, true);
					player.getPacketSender().setHidden(851, 97, true);
					break;
				case 4:
					player.getPacketSender().sendString(851, 88, player.teleportFavourites.get(0).name);
					player.getPacketSender().sendString(851, 89, player.teleportFavourites.get(1).name);
					player.getPacketSender().sendString(851, 90, player.teleportFavourites.get(2).name);
					player.getPacketSender().sendString(851, 91, player.teleportFavourites.get(3).name);
					player.getPacketSender().setHidden(851, 88, false);
					player.getPacketSender().setHidden(851, 89, false);
					player.getPacketSender().setHidden(851, 90, false);
					player.getPacketSender().setHidden(851, 91, false);
					player.getPacketSender().setHidden(851, 92, true);
					player.getPacketSender().setHidden(851, 93, false);
					player.getPacketSender().setHidden(851, 94, false);
					player.getPacketSender().setHidden(851, 95, false);
					player.getPacketSender().setHidden(851, 96, false);
					player.getPacketSender().setHidden(851, 97, true);
					break;
				case 5:
					player.getPacketSender().sendString(851, 88, player.teleportFavourites.get(0).name);
					player.getPacketSender().sendString(851, 89, player.teleportFavourites.get(1).name);
					player.getPacketSender().sendString(851, 90, player.teleportFavourites.get(2).name);
					player.getPacketSender().sendString(851, 91, player.teleportFavourites.get(3).name);
					player.getPacketSender().sendString(851, 92, player.teleportFavourites.get(4).name);
					player.getPacketSender().setHidden(851, 88, false);
					player.getPacketSender().setHidden(851, 89, false);
					player.getPacketSender().setHidden(851, 90, false);
					player.getPacketSender().setHidden(851, 91, false);
					player.getPacketSender().setHidden(851, 92, false);
					player.getPacketSender().setHidden(851, 93, false);
					player.getPacketSender().setHidden(851, 94, false);
					player.getPacketSender().setHidden(851, 95, false);
					player.getPacketSender().setHidden(851, 96, false);
					player.getPacketSender().setHidden(851, 97, false);
					break;
			}
		}
	}

	public void sendTeleport(Player player, ServerTeleports teleport) {
		// player.getPacketSender().sendNpcHead(851, 59, 2215);
		currentTeleport = teleport;
		this.clear();

		final Item[] teleportItems = teleport.getItems();
		final int teleportItemsLength = teleportItems == null ? 0 : teleportItems.length;
		init(player, teleportItemsLength, -1, 64161, 141, false);
		for (int i = 0; i < teleportItemsLength; i++) {
			final Item teleportItem = teleportItems[i];
			add(teleportItem.getId(), teleportItem.getAmount());
		}

		this.sendAll = true;
		this.sendUpdates();
		player.getPacketSender().sendClientScript(
			149, "IviiiIsssss",
			851 << 16 | 77, 141,
			4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendIfEvents(851, 141, 0, 27, 1024);
		player.getPacketSender().sendIfEvents(851, 141, 0, 27, 1086);
		player.getPacketSender().sendIfEvents(851, 141, 0, 5, AccessMasks.ClickOp1);
		player.getPacketSender().sendIfEvents(851, 141, 0, 27, 1086);
		player.getPacketSender().sendIfEvents(851, 77, 0, 27, 1024);
		player.getPacketSender().sendIfEvents(851, 77, 0, 27, 1086);
		player.getPacketSender().sendIfEvents(851, 77, 0, 5, AccessMasks.ClickOp1);
		player.getPacketSender().sendIfEvents(851, 77, 0, 27, 1086);

		player.getPacketSender().sendString(851, 58, teleport.name);
	}

	public void sendTeleport(Player player, int componentId) {
		currentComponentId = componentId;
		for (int i = 156; i < 171; i++) {
			player.getPacketSender().setHidden(851, i, true);
		}

		this.clear();
		teleportCheck(componentId);

		final ServerTeleports currentTeleport = this.currentTeleport;
		if (currentTeleport == null) {
			return;
		}

		int itemContainer = 156;
		int itemContainerId = 1000;

		final Item[] teleportItems = currentTeleport.getItems();
		if (teleportItems != null) {
			for (final Item teleportItem : teleportItems) {
				player.getPacketSender().setHidden(851, itemContainer, false);
				player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					851 << 16 | itemContainer, itemContainerId,
					4, 7, 1, -1, "", "", "", "", "");
				player.getPacketSender().sendItems(
					-1,
					itemContainer,
					itemContainerId,
					teleportItem);
				player.getPacketSender().sendIfEvents(851, itemContainer, 0, 27, 1024);
				player.getPacketSender().sendIfEvents(851, itemContainer, 0, 27, 1086);
				player.getPacketSender().sendIfEvents(851, itemContainer, 0, 5, AccessMasks.ClickOp1);
				player.getPacketSender().sendIfEvents(851, itemContainer, 0, 27, 1086);
				itemContainerId++;
				itemContainer++;
				if (itemContainer == 170) {
					break;
				}
			}
		}
		player.getPacketSender().setHidden(851, 108, false);
		player.getPacketSender().setHidden(851, 109, false);
		player.getPacketSender().setHidden(851, 110, false);
		player.getPacketSender().setHidden(851, 111, false);
		if (currentTeleport.difficulty == "") {
			player.getPacketSender().setHidden(851, 108, true);
			player.getPacketSender().setHidden(851, 109, true);
		}
		if (currentTeleport.requirements == "") {
			player.getPacketSender().setHidden(851, 110, true);
			player.getPacketSender().setHidden(851, 111, true);
		}
		player.getPacketSender().setHidden(851, 105, false);
		int compId = 62;
		switch (currentTeleport) {
			case ROCK_CRABS:
			case SAND_CRABS:
			case AMMONITE_CRABS:
			case EXPERIMENTS:
			case CATALYSTS:
			case GWD:
			case CORP:
			case VENENATIS:
			case SARACHNIS:
			case BARROWS:
			case ZULRAH:
			case REVENANTS:
			case SCORPIA:
			case PEST_CONTROL:
			case YAKS:
				compId = 61;
				break;
			case SMOKE_DEVIL:
			case CERBERUS:
			case BASILISK_KNIGHTS:
			case ARMOURED_ZOMBIES:
			case KRAKEN:
			case THE_GAUNTLET:
			case CALLISTO:
			case DEMONIC_GORILLA:
			case CHAOS_ELE:
			case VETION:
			case NEX:
			case KQ:
			case OPHIDIA:
			case VYREWATCH_SENTINELS:
				compId = 60;
				break;
			case GIANT_MOLE:
				compId = 63;
				break;
			case DKS:
			case CHICKENS:
			case WINTERTODT:
			case CHAMBERS_OF_XERIC:
			case BALANCE_ELEMENTAL:
				compId = 65;
				break;
			case SKOTIZO:
			case HYDRA:
				compId = 66;
				break;

		}

		player.getPacketSender().setHidden(851, 60, true);
		player.getPacketSender().setHidden(851, 61, true);
		player.getPacketSender().setHidden(851, 62, true);
		player.getPacketSender().setHidden(851, 63, true);
		player.getPacketSender().setHidden(851, 65, true);
		player.getPacketSender().setHidden(851, 66, true);
		player.getPacketSender().setHidden(851, 155, true);

		player.getPacketSender().setHidden(851, compId, false);

		int interfaceHash = 851 << 16 | compId;
		if (currentTeleport.animId > 0 && currentTeleport.modelId > 0) {
			player.getPacketSender().sendClientScript(10623, "Ii", interfaceHash, currentTeleport.modelId);
			player.getPacketSender().animateInterface(851, compId, currentTeleport.animId);
			player.getPacketSender().setHidden(851, compId, false);
		}
		else
			player.getPacketSender().setHidden(851, compId, true);
		if (currentTeleport == ServerTeleports.CHAOS_DRUID) {
			player.getPacketSender().sendNpcHead(851, 60, 520);
			player.getPacketSender().setHidden(851, 60, false);
		}
		this.sendAll = true;
		this.sendUpdates();
		player.getPacketSender().sendString(851, 109, currentTeleport.description);
		player.getPacketSender().sendString(851, 113, currentTeleport.requirements);
		player.getPacketSender().sendString(851, 111, currentTeleport.difficulty);
		player.getPacketSender().sendString(851, 58, currentTeleport.name);
	}

	private void teleportCheck(int componentId) {
		switch (componentId) {
			case 22:
				if (currentCategory == Categories.BOSSES && pageNumber == 1) {
					currentTeleport = ServerTeleports.GWD;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 2) {
					currentTeleport = ServerTeleports.VENENATIS;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 3) {
					currentTeleport = ServerTeleports.PHANTOM_MUSPAH;
				}
				else if (currentCategory == Categories.SKILLING
					&& currentSkillingSection == SkillingSubSections.WOODCUTTING) {
					currentTeleport = ServerTeleports.WOODCUTTING_GUILD;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.FARMING_GUILD;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.LUMBRIDGE_TREE_PATCH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 3) {
					currentTeleport = ServerTeleports.BRIMHAVEN_SPIRIT_TREE_PATCH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FISHING) {
					currentTeleport = ServerTeleports.CATHTERBY;
				}
				else if (currentCategory == Categories.SKILLING
					&& currentSkillingSection == SkillingSubSections.RUNECRAFTING) {
					currentTeleport = ServerTeleports.ABYSS;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.MINING) {
					currentTeleport = ServerTeleports.MINING_GUILD;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.HUNTER) {
					currentTeleport = ServerTeleports.CRIMSON_SWIFTS;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.AGILITY) {
					currentTeleport = ServerTeleports.GNOME_STRONGHOLD_COURSE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.THIEVING) {
					currentTeleport = ServerTeleports.EDGEVILLE_MARKETPLACE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.TAVERLEY_DUNGEON;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.DARK_BEAST;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 1) {
					currentTeleport = ServerTeleports.VARROCK;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 2) {
					currentTeleport = ServerTeleports.POLLNIVNEACH;
				}
				else if (currentCategory == Categories.MINIGAMES) {
					currentTeleport = ServerTeleports.CHAMBERS_OF_XERIC;
				}
				else if (currentCategory == Categories.WILDERNESS) {
					currentTeleport = ServerTeleports.EAST_DRAGONS;
				}
				else if (currentCategory == Categories.TRAINING) {
					currentTeleport = ServerTeleports.ROCK_CRABS;
				}
				else if (currentCategory == Categories.SEARCH) {
					if (player.searchTeles.isEmpty()) {
						return;
					}
					currentTeleport = player.searchTeles.getFirst();
				}
				break;
			case 23:
				if (currentCategory == Categories.BOSSES && pageNumber == 1) {
					currentTeleport = ServerTeleports.VORKATH;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 2) {
					currentTeleport = ServerTeleports.CALLISTO;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 3) {
					currentTeleport = ServerTeleports.LEVIATHAN;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.LUMMY_DUNGEON;
				}
				else if (currentCategory == Categories.SKILLING
					&& currentSkillingSection == SkillingSubSections.WOODCUTTING) {
					currentTeleport = ServerTeleports.HARDWOOD_GROVE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.KOUREND_ALLOTMENT;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.VARROCK_TREE_PATCH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 3) {
					currentTeleport = ServerTeleports.AL_KHARID_CACTUS_PATCH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FISHING) {
					currentTeleport = ServerTeleports.FISHING_GUILD;
				}
				else if (currentCategory == Categories.SKILLING
					&& currentSkillingSection == SkillingSubSections.RUNECRAFTING) {
					currentTeleport = ServerTeleports.ZMI;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.MINING) {
					currentTeleport = ServerTeleports.MOTHERLOAD_MINE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.HUNTER) {
					currentTeleport = ServerTeleports.DESERT_HUNTING_GROUND;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.AGILITY) {
					currentTeleport = ServerTeleports.DRAYNOR_ROOFTOP_COURSE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.SLAYER_TOWER;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 1) {
					currentTeleport = ServerTeleports.LUMBRIDGE;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 2) {
					currentTeleport = ServerTeleports.GNOME_STRONGHOLD;
				}
				else if (currentCategory == Categories.MINIGAMES) {
					currentTeleport = ServerTeleports.WARRIORS_GUILD;
				}
				else if (currentCategory == Categories.WILDERNESS) {
					currentTeleport = ServerTeleports.WEST_DRAGONS;
				}
				else if (currentCategory == Categories.TRAINING) {
					currentTeleport = ServerTeleports.SAND_CRABS;
				}
				else if (currentCategory == Categories.SEARCH) {
					if (player.searchTeles.size() < 2) {
						return;
					}
					currentTeleport = player.searchTeles.get(1);
				}
				break;
			case 24:
				if (currentCategory == Categories.BOSSES && pageNumber == 1) {
					currentTeleport = ServerTeleports.ZULRAH;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 2) {
					currentTeleport = ServerTeleports.VETION;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 3) {
					currentTeleport = ServerTeleports.DUKE_SUCELLUS;
				}
				else if (currentCategory == Categories.SKILLING
					&& currentSkillingSection == SkillingSubSections.WOODCUTTING) {
					currentTeleport = ServerTeleports.SEERS;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.ARDOUGNE_ALLOTMENT;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.ARDY_DUNGEON;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.FALADOR_TREE_PATCH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 3) {
					currentTeleport = ServerTeleports.HARDWOOD_TREE_PATCHES;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FISHING) {
					currentTeleport = ServerTeleports.OTTOS_GROTTO;
				}
				else if (currentCategory == Categories.SKILLING
					&& currentSkillingSection == SkillingSubSections.RUNECRAFTING) {
					currentTeleport = ServerTeleports.ARCEUUS_ESSENCE_MINE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.MINING) {
					currentTeleport = ServerTeleports.AMETHYST_MINE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.HUNTER) {
					currentTeleport = ServerTeleports.TROPICAL_WAGTAIL;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.AGILITY) {
					currentTeleport = ServerTeleports.AL_KHARID_ROOFTOP_COURSE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.STRONGHOLD_CAVE;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 1) {
					currentTeleport = ServerTeleports.FALADOR;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 2) {
					currentTeleport = ServerTeleports.YANILLE;
				}
				else if (currentCategory == Categories.MINIGAMES) {
					currentTeleport = ServerTeleports.BARROWS;
				}
				else if (currentCategory == Categories.WILDERNESS) {
					currentTeleport = ServerTeleports.MAGE_BANK;
				}
				else if (currentCategory == Categories.TRAINING) {
					currentTeleport = ServerTeleports.AMMONITE_CRABS;
				}
				else if (currentCategory == Categories.SEARCH) {
					if (player.searchTeles.size() < 3) {
						return;
					}
					currentTeleport = player.searchTeles.get(2);
				}
				break;
			case 25:
				if (currentCategory == Categories.BOSSES && pageNumber == 1) {
					currentTeleport = ServerTeleports.GALVEK;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.LITHKREN;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 2) {
					currentTeleport = ServerTeleports.KBD;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 3) {
					currentTeleport = ServerTeleports.VARDORVIS;
				}
				else if (currentCategory == Categories.SKILLING
					&& currentSkillingSection == SkillingSubSections.WOODCUTTING) {
					currentTeleport = ServerTeleports.PLEASANT_PARK;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.CATHERBY_ALLOTMENT;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.TAVERLEY_TREE_PATCH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 3) {
					currentTeleport = ServerTeleports.MUSHROOM_PATCH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FISHING) {
					currentTeleport = ServerTeleports.PISCATORIS;
				}
				else if (currentCategory == Categories.SKILLING
					&& currentSkillingSection == SkillingSubSections.RUNECRAFTING) {
					currentTeleport = ServerTeleports.BLOOD_ALTAR;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.MINING) {
					currentTeleport = ServerTeleports.AL_KHARID_MINE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.HUNTER) {
					currentTeleport = ServerTeleports.SWAMP_LIZARDS;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.AGILITY) {
					currentTeleport = ServerTeleports.VARROCK_ROOFTOP_COURSE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.KOUREND_CATACOMBS;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 1) {
					currentTeleport = ServerTeleports.SEERS;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 2) {
					currentTeleport = ServerTeleports.AL_KHARID;
				}
				else if (currentCategory == Categories.MINIGAMES) {
					currentTeleport = ServerTeleports.FIGHT_CAVES;
				}
				else if (currentCategory == Categories.WILDERNESS) {
					currentTeleport = ServerTeleports.REVENANTS;
				}
				else if (currentCategory == Categories.TRAINING) {
					currentTeleport = ServerTeleports.YAKS;
				}
				else if (currentCategory == Categories.SEARCH) {
					if (player.searchTeles.size() < 4) {
						return;
					}
					currentTeleport = player.searchTeles.get(3);
				}
				break;
			case 26:
				if (currentCategory == Categories.BOSSES && pageNumber == 1) {
					currentTeleport = ServerTeleports.CORP;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.ISLE_OF_SOULS;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 2) {
					currentTeleport = ServerTeleports.GIANT_MOLE;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 3) {
					currentTeleport = ServerTeleports.WHISPERER;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.MINING) {
					currentTeleport = ServerTeleports.VARROCK_EAST_MINE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.FALADOR_ALLOTMENT;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.GNOME_STRONGHOLD_TREE_PATCH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 3) {
					currentTeleport = ServerTeleports.LUMBRIDGE_HOPS;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FISHING) {
					currentTeleport = ServerTeleports.ANGLERFISH_SPOT;
				}
				else if (currentCategory == Categories.SKILLING
					&& currentSkillingSection == SkillingSubSections.RUNECRAFTING) {
					currentTeleport = ServerTeleports.SOUL_ALTAR;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.HUNTER) {
					currentTeleport = ServerTeleports.FALCONRY;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.AGILITY) {
					currentTeleport = ServerTeleports.CANIFIS_ROOFTOP_COURSE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.CAVE_HORRORS;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 1) {
					currentTeleport = ServerTeleports.DRAYNOR;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 2) {
					currentTeleport = ServerTeleports.PORT_SARIM;
				}
				else if (currentCategory == Categories.MINIGAMES) {
					currentTeleport = ServerTeleports.INFERNO;
				}
				else if (currentCategory == Categories.WILDERNESS) {
					currentTeleport = ServerTeleports.SCORPIA;
				}
				else if (currentCategory == Categories.TRAINING) {
					currentTeleport = ServerTeleports.CHICKENS;
				}
				else if (currentCategory == Categories.SEARCH) {
					if (player.searchTeles.size() < 5) {
						return;
					}
					currentTeleport = player.searchTeles.get(4);
				}
				break;
			case 27:
				if (currentCategory == Categories.BOSSES && pageNumber == 1) {
					currentTeleport = ServerTeleports.KQ;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.GIANTS_DEN;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 2) {
					currentTeleport = ServerTeleports.SKOTIZO;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 3) {
					currentTeleport = ServerTeleports.MALAKAR;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.PHASMATYS_ALLOTMENT;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.MINING) {
					currentTeleport = ServerTeleports.VARROCK_WEST_MINE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.GNOME_STRONGHOLD_FRUIT_TREE_PATCH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 3) {
					currentTeleport = ServerTeleports.SEERS_HOPS;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FISHING) {
					currentTeleport = ServerTeleports.KARAMBWAN_SPOT;
				}
				else if (currentCategory == Categories.SKILLING
					&& currentSkillingSection == SkillingSubSections.RUNECRAFTING) {
					currentTeleport = ServerTeleports.WRATH_ALTAR;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.HUNTER) {
					currentTeleport = ServerTeleports.RED_CHINCHOMPAS;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.AGILITY) {
					currentTeleport = ServerTeleports.FALADOR_ROOFTOP_COURSE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.FREMENNIK_SLAYER_CAVE;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 1) {
					currentTeleport = ServerTeleports.ARDOUGNE;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 2) {
					currentTeleport = ServerTeleports.BRIMHAVEN;
				}
				else if (currentCategory == Categories.MINIGAMES) {
					currentTeleport = ServerTeleports.PEST_CONTROL;
				}
				else if (currentCategory == Categories.TRAINING) {
					currentTeleport = ServerTeleports.CHAOS_DRUID;
				}
				else if (currentCategory == Categories.WILDERNESS) {
					currentTeleport = ServerTeleports.CHAOS_FANATIC;
				}
				else if (currentCategory == Categories.SEARCH) {
					if (player.searchTeles.size() < 6) {
						return;
					}
					currentTeleport = player.searchTeles.get(5);
				}
				break;
			case 28:
				if (currentCategory == Categories.BOSSES && pageNumber == 1) {
					currentTeleport = ServerTeleports.DKS;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 2) {
					currentTeleport = ServerTeleports.APE_ATOLL;
				}
				if (currentCategory == Categories.BOSSES && pageNumber == 2) {
					currentTeleport = ServerTeleports.DEMONIC_GORILLA;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.SOURHOG;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 3) {
					currentTeleport = ServerTeleports.SOL_HEREDIT;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.CHAMPIONS_GUILD_BUSH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.CATHERBY_FRUIT_TREE_PATCH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 3) {
					currentTeleport = ServerTeleports.YANILLE_HOPS;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.HUNTER) {
					currentTeleport = ServerTeleports.BLACK_CHINCHOMPA;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.AGILITY) {
					currentTeleport = ServerTeleports.SEERS_ROOFTOP_COURSE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.MOUNT_KARUULM;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 1) {
					currentTeleport = ServerTeleports.CANIFIS;
				}
				else if (currentCategory == Categories.MINIGAMES) {
					currentTeleport = ServerTeleports.DOMINION_OF_ECHOES;
				}
				else if (currentCategory == Categories.TRAINING) {
					currentTeleport = ServerTeleports.EXPERIMENTS;
				}
				else if (currentCategory == Categories.WILDERNESS) {
					currentTeleport = ServerTeleports.CRAZY_ARCH;
				}
				else if (currentCategory == Categories.SEARCH) {
					if (player.searchTeles.size() < 7) {
						return;
					}
					currentTeleport = player.searchTeles.get(6);
				}
				break;
			case 29:
				if (currentCategory == Categories.BOSSES && pageNumber == 1) {
					currentTeleport = ServerTeleports.KRAKEN;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 2) {
					currentTeleport = ServerTeleports.PRIFDDINAS;
				}
				if (currentCategory == Categories.BOSSES && pageNumber == 2) {
					currentTeleport = ServerTeleports.ARGENTAVIS;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 3) {
					currentTeleport = ServerTeleports.GROTESQUE_GUARDIANS;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 2) {
					currentTeleport = ServerTeleports.PRIFDDINAS;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.RIMMINGTON_BUSH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.TREE_GNOME_MAZE_FRUIT_TREE_PATCH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 3) {
					currentTeleport = ServerTeleports.ENTRANA_HOPS;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.HUNTER) {
					currentTeleport = ServerTeleports.RED_SALAMANDER;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.AGILITY) {
					currentTeleport = ServerTeleports.POLLNIVNEACH_ROOFTOP_COURSE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.ANCIENT_CAVERN;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 1) {
					currentTeleport = ServerTeleports.MOS_LE_HARMLESS;
				}
				else if (currentCategory == Categories.MINIGAMES) {
					currentTeleport = ServerTeleports.WINTERTODT;
				}
				else if (currentCategory == Categories.TRAINING) {
					currentTeleport = ServerTeleports.CATALYSTS;
				}
				else if (currentCategory == Categories.WILDERNESS) {
					currentTeleport = ServerTeleports.CHAOS_ELE;
				}
				else if (currentCategory == Categories.SEARCH) {
					if (player.searchTeles.size() < 8) {
						return;
					}
					currentTeleport = player.searchTeles.get(7);
				}
				break;
			case 30:
				if (currentCategory == Categories.BOSSES && pageNumber == 1) {
					currentTeleport = ServerTeleports.HYDRA;
				}
				if (currentCategory == Categories.BOSSES && pageNumber == 2) {
					currentTeleport = ServerTeleports.NEX;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 3) {
					currentTeleport = ServerTeleports.ARAXXOR;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.ARDOUGNE_BUSH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.BRIMHAVEN_FRUIT_TREE_PATCH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.HUNTER) {
					currentTeleport = ServerTeleports.BLACK_SALAMANDER;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.AGILITY) {
					currentTeleport = ServerTeleports.RELLEKA_ROOFTOP_COURSE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.FOSSIL_ISLAND_WYVERN_CAVE;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 1) {
					currentTeleport = ServerTeleports.SHILO_VILLAGE;
				}
				else if (currentCategory == Categories.MINIGAMES) {
					currentTeleport = ServerTeleports.DUEL_ARENA;
				}
				else if (currentCategory == Categories.TRAINING) {
					currentTeleport = ServerTeleports.SHAMANS;
				}
				else if (currentCategory == Categories.WILDERNESS) {
					currentTeleport = ServerTeleports.WILDY_RESOURCE_AREA;
				}
				else if (currentCategory == Categories.SEARCH) {
					if (player.searchTeles.size() < 9) {
						return;
					}
					currentTeleport = player.searchTeles.get(8);
				}
				break;
			case 31:
				if (currentCategory == Categories.BOSSES && pageNumber == 1) {
					currentTeleport = ServerTeleports.CERBERUS;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 2) {
					currentTeleport = ServerTeleports.NIGHTMARE;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 3) {
					currentTeleport = ServerTeleports.SCURRIUS;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.ETCETERIA_BUSH;
				}
				else if (currentCategory == Categories.WILDERNESS) {
					currentTeleport = ServerTeleports.CHAOS_ALTAR;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.LLETYA_FRUIT_TREE_PATCH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.HUNTER) {
					currentTeleport = ServerTeleports.PURO_PURO;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.AGILITY) {
					currentTeleport = ServerTeleports.ARDOUGNE_ROOFTOP_COURSE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.SKELETAL_WYVERN;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 1) {
					currentTeleport = ServerTeleports.RELLEKA;
				}
				else if (currentCategory == Categories.MINIGAMES) {
					currentTeleport = ServerTeleports.THE_GAUNTLET;
				}
				else if (currentCategory == Categories.SEARCH) {
					if (player.searchTeles.size() < 10) {
						return;
					}
					currentTeleport = player.searchTeles.get(9);
				}
				else if (currentCategory == Categories.TRAINING) {
					currentTeleport = ServerTeleports.VYREWATCH_SENTINELS;
				}
				break;
			case 32:
				if (currentCategory == Categories.BOSSES && pageNumber == 1) {
					currentTeleport = ServerTeleports.SMOKE_DEVIL;
				}
				if (currentCategory == Categories.BOSSES && pageNumber == 2) {
					currentTeleport = ServerTeleports.SARACHNIS;
				}
				else if (currentCategory == Categories.BOSSES && pageNumber == 3) {
					currentTeleport = ServerTeleports.BALANCE_ELEMENTAL;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.SMOKE_DEVILS_CAVE;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.HOSIDIUS_VINERY;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.ETCETERIA_SPIRIT_TREE_PATCH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.HUNTER) {
					currentTeleport = ServerTeleports.BIRDHOUSES;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.AGILITY) {
					currentTeleport = ServerTeleports.WILDERNESS_AGILITY_COURSE;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 1) {
					currentTeleport = ServerTeleports.BURTHORPE;
				}
				else if (currentCategory == Categories.SEARCH) {
					if (player.searchTeles.size() < 11) {
						return;
					}
					currentTeleport = player.searchTeles.get(10);
				}
				else if (currentCategory == Categories.MINIGAMES) {
					currentTeleport = ServerTeleports.THEATRE_OF_BLOOD;
				}
				else if (currentCategory == Categories.TRAINING) {
					currentTeleport = ServerTeleports.BASILISK_KNIGHTS;
				}
				else if (currentCategory == Categories.TRAINING) {
					currentTeleport = ServerTeleports.ARMOURED_ZOMBIES;
				}
				break;
			case 33:

				if (currentCategory == Categories.BOSSES && pageNumber == 1) {
					currentTeleport = ServerTeleports.SIRE;
				}
				if (currentCategory == Categories.BOSSES && pageNumber == 2) {
					currentTeleport = ServerTeleports.OPHIDIA;
				}
				else if (currentCategory == Categories.TRAINING) {
					currentTeleport = ServerTeleports.ARMOURED_ZOMBIES;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.SEAWEED_PATCH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.FARMING
					&& pageNumber == 2) {
					currentTeleport = ServerTeleports.PORT_SARIM_SPIRIT_TREE_PATCH;
				}
				else if (currentCategory == Categories.SKILLING && currentSkillingSection == SkillingSubSections.SLAYER
					&& pageNumber == 1) {
					currentTeleport = ServerTeleports.BRIMHAVEN_DUNGEON;
				}
				else if (currentCategory == Categories.CITIES && pageNumber == 1) {
					currentTeleport = ServerTeleports.LUNAR_ISLE;
				}
				else if (currentCategory == Categories.SEARCH) {
					if (player.searchTeles.size() < 12) {
						return;
					}
					currentTeleport = player.searchTeles.get(11);
				}
				else if (currentCategory == Categories.MINIGAMES) {
					currentTeleport = ServerTeleports.TOMBS_OF_AMASCUT;
				}
				break;
		}
	}

	public void sendSkillingSection(Player player) {
		player.getPacketSender().setHidden(851, 117, false);
		player.getPacketSender().setHidden(851, 38, true);
		player.getPacketSender().setHidden(851, 22, true);
		player.getPacketSender().setHidden(851, 23, true);
		player.getPacketSender().setHidden(851, 24, true);
		player.getPacketSender().setHidden(851, 25, true);
		player.getPacketSender().setHidden(851, 26, true);
		player.getPacketSender().setHidden(851, 27, true);
		player.getPacketSender().setHidden(851, 28, true);
		player.getPacketSender().setHidden(851, 29, true);
		player.getPacketSender().setHidden(851, 30, true);
		player.getPacketSender().setHidden(851, 31, true);
		player.getPacketSender().setHidden(851, 32, true);
		player.getPacketSender().setHidden(851, 33, true);
		player.getPacketSender().sendString(851, 20, "");
		player.getPacketSender().setHidden(851, 152, false);
		player.getPacketSender().setHidden(851, 153, false);
		currentSkillingSection = null;
		currentTeleport = null;
		currentCategory = Categories.SKILLING;
		pageNumber = 1;
	}

	public void activateHiddenTeleportNames(Player player) {
		player.getPacketSender().setHidden(851, 22, false);
		player.getPacketSender().setHidden(851, 23, false);
		player.getPacketSender().setHidden(851, 24, false);
		player.getPacketSender().setHidden(851, 25, false);
		player.getPacketSender().setHidden(851, 26, false);
		player.getPacketSender().setHidden(851, 27, false);
		player.getPacketSender().setHidden(851, 28, false);
		player.getPacketSender().setHidden(851, 29, false);
		player.getPacketSender().setHidden(851, 30, false);
		player.getPacketSender().setHidden(851, 31, false);
		player.getPacketSender().setHidden(851, 32, false);
		player.getPacketSender().setHidden(851, 33, false);
	}

	public void sendMiningTeleports(Player player) {
		pageNumber = 1;
		activateHiddenTeleportNames(player);
		currentSkillingSection = SkillingSubSections.MINING;
		player.getPacketSender().sendString(851, 20, "Mining");
		player.getPacketSender().sendString(851, 22, ServerTeleports.MINING_GUILD.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.MOTHERLOAD_MINE.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.AMETHYST_MINE.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.AL_KHARID_MINE.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.VARROCK_EAST_MINE.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.VARROCK_WEST_MINE.name);
		for (int i = 28; i < 34; i++) {
			player.getPacketSender().setHidden(851, i, true);
		}
	}

	public void sendThievingTeleports(Player player) {
		pageNumber = 1;
		player.getPacketSender().sendString(851, 20, "Thieving");
		currentSkillingSection = SkillingSubSections.THIEVING;
		activateHiddenTeleportNames(player);
		player.getPacketSender().sendString(851, 22, ServerTeleports.EDGEVILLE_MARKETPLACE.name);
		for (int i = 23; i < 34; i++) {
			player.getPacketSender().setHidden(851, i, true);
		}
	}

	public void sendWoodcuttingTeleports(Player player) {
		pageNumber = 1;
		player.getPacketSender().sendString(851, 20, "Woodcutting");
		activateHiddenTeleportNames(player);
		currentSkillingSection = SkillingSubSections.WOODCUTTING;
		player.getPacketSender().sendString(851, 22, ServerTeleports.WOODCUTTING_GUILD.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.HARDWOOD_GROVE.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.SEERS.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.PLEASANT_PARK.name);
		for (int i = 26; i < 34; i++) {
			player.getPacketSender().setHidden(851, i, true);
		}
	}

	public void sendRunecraftingTeleports(Player player) {
		pageNumber = 1;
		player.getPacketSender().sendString(851, 20, "Runecrafting");
		currentSkillingSection = SkillingSubSections.RUNECRAFTING;
		activateHiddenTeleportNames(player);
		player.getPacketSender().sendString(851, 22, ServerTeleports.ABYSS.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.ZMI.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.ARCEUUS_ESSENCE_MINE.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.BLOOD_ALTAR.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.SOUL_ALTAR.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.WRATH_ALTAR.name);
		for (int i = 26; i < 34; i++) {
			player.getPacketSender().setHidden(851, i, true);
		}
	}

	public void sendAgilityTeleports(Player player) {
		pageNumber = 1;
		activateHiddenTeleportNames(player);
		currentSkillingSection = SkillingSubSections.AGILITY;
		player.getPacketSender().setHidden(851, 33, true);
		player.getPacketSender().sendString(851, 20, "Agility");
		player.getPacketSender().sendString(851, 22, ServerTeleports.GNOME_STRONGHOLD_COURSE.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.DRAYNOR_ROOFTOP_COURSE.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.AL_KHARID_ROOFTOP_COURSE.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.VARROCK_ROOFTOP_COURSE.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.CANIFIS_ROOFTOP_COURSE.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.FALADOR_ROOFTOP_COURSE.name);
		player.getPacketSender().sendString(851, 28, ServerTeleports.SEERS_ROOFTOP_COURSE.name);
		player.getPacketSender().sendString(851, 29, ServerTeleports.POLLNIVNEACH_ROOFTOP_COURSE.name);
		player.getPacketSender().sendString(851, 30, ServerTeleports.RELLEKA_ROOFTOP_COURSE.name);
		player.getPacketSender().sendString(851, 31, ServerTeleports.ARDOUGNE_ROOFTOP_COURSE.name);
		player.getPacketSender().sendString(851, 32, ServerTeleports.WILDERNESS_AGILITY_COURSE.name);
	}

	public void sendFishingTeleports(Player player) {
		pageNumber = 1;
		activateHiddenTeleportNames(player);
		currentSkillingSection = SkillingSubSections.FISHING;
		player.getPacketSender().sendString(851, 20, "Fishing");
		player.getPacketSender().sendString(851, 22, ServerTeleports.CATHTERBY.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.FISHING_GUILD.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.OTTOS_GROTTO.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.PISCATORIS.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.ANGLERFISH_SPOT.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.KARAMBWAN_SPOT.name);
		for (int i = 28; i < 34; i++) {
			player.getPacketSender().setHidden(851, i, true);
		}
	}

	public void sendSlayerTeleports(Player player) {
		pageNumber = 1;
		activateHiddenTeleportNames(player);
		currentSkillingSection = SkillingSubSections.SLAYER;
		player.getPacketSender().sendString(851, 20, "Slayer Teleports");
		player.getPacketSender().sendString(851, 22, ServerTeleports.TAVERLEY_DUNGEON.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.SLAYER_TOWER.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.STRONGHOLD_CAVE.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.KOUREND_CATACOMBS.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.CAVE_HORRORS.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.FREMENNIK_SLAYER_CAVE.name);
		player.getPacketSender().sendString(851, 28, ServerTeleports.MOUNT_KARUULM.name);
		player.getPacketSender().sendString(851, 29, ServerTeleports.ANCIENT_CAVERN.name);
		player.getPacketSender().sendString(851, 30, ServerTeleports.FOSSIL_ISLAND_WYVERN_CAVE.name);
		player.getPacketSender().sendString(851, 31, ServerTeleports.SKELETAL_WYVERN.name);
		player.getPacketSender().sendString(851, 32, ServerTeleports.SMOKE_DEVILS_CAVE.name);
		player.getPacketSender().sendString(851, 33, ServerTeleports.BRIMHAVEN_DUNGEON.name);
	}

	public void sendSlayerTeleportsPageTwo(Player player) {
		pageNumber = 2;
		player.getPacketSender().sendString(851, 22, ServerTeleports.DARK_BEAST.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.LUMMY_DUNGEON.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.ARDY_DUNGEON.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.LITHKREN.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.ISLE_OF_SOULS.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.GIANTS_DEN.name);
		player.getPacketSender().sendString(851, 28, ServerTeleports.SOURHOG.name);

		for (int i = 29; i < 34; i++) {
			player.getPacketSender().setHidden(851, i, true);
		}
	}

	public void sendHunterTeleports(Player player) {
		pageNumber = 1;
		player.getPacketSender().sendString(851, 20, "Hunter");
		currentSkillingSection = SkillingSubSections.HUNTER;
		activateHiddenTeleportNames(player);
		player.getPacketSender().sendString(851, 22, ServerTeleports.CRIMSON_SWIFTS.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.DESERT_HUNTING_GROUND.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.TROPICAL_WAGTAIL.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.SWAMP_LIZARDS.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.FALCONRY.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.RED_CHINCHOMPAS.name);
		player.getPacketSender().sendString(851, 28, ServerTeleports.BLACK_CHINCHOMPA.name);
		player.getPacketSender().sendString(851, 29, ServerTeleports.RED_SALAMANDER.name);
		player.getPacketSender().sendString(851, 30, ServerTeleports.BLACK_SALAMANDER.name);
		player.getPacketSender().sendString(851, 31, ServerTeleports.PURO_PURO.name);
		player.getPacketSender().sendString(851, 32, ServerTeleports.BIRDHOUSES.name);
		player.getPacketSender().setHidden(851, 33, true);
	}

	public void sendFarmingTeleports(Player player) {
		pageNumber = 1;
		currentSkillingSection = SkillingSubSections.FARMING;
		player.getPacketSender().sendString(851, 20, "Farming");
		activateHiddenTeleportNames(player);
		player.getPacketSender().sendString(851, 22, ServerTeleports.FARMING_GUILD.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.KOUREND_ALLOTMENT.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.ARDOUGNE_ALLOTMENT.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.CATHERBY_ALLOTMENT.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.FALADOR_ALLOTMENT.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.PHASMATYS_ALLOTMENT.name);
		player.getPacketSender().sendString(851, 28, ServerTeleports.CHAMPIONS_GUILD_BUSH.name);
		player.getPacketSender().sendString(851, 29, ServerTeleports.RIMMINGTON_BUSH.name);
		player.getPacketSender().sendString(851, 30, ServerTeleports.ARDOUGNE_BUSH.name);
		player.getPacketSender().sendString(851, 31, ServerTeleports.ETCETERIA_BUSH.name);
		player.getPacketSender().sendString(851, 32, ServerTeleports.HOSIDIUS_VINERY.name);
		player.getPacketSender().sendString(851, 33, ServerTeleports.SEAWEED_PATCH.name);
	}

	public void sendFarmingTeleportsPageTwo(Player player) {
		pageNumber = 2;
		activateHiddenTeleportNames(player);
		player.getPacketSender().sendString(851, 22, ServerTeleports.LUMBRIDGE_TREE_PATCH.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.VARROCK_TREE_PATCH.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.FALADOR_TREE_PATCH.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.TAVERLEY_TREE_PATCH.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.GNOME_STRONGHOLD_TREE_PATCH.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.GNOME_STRONGHOLD_FRUIT_TREE_PATCH.name);
		player.getPacketSender().sendString(851, 28, ServerTeleports.CATHERBY_FRUIT_TREE_PATCH.name);
		player.getPacketSender().sendString(851, 29, ServerTeleports.TREE_GNOME_MAZE_FRUIT_TREE_PATCH.name);
		player.getPacketSender().sendString(851, 30, ServerTeleports.BRIMHAVEN_FRUIT_TREE_PATCH.name);
		player.getPacketSender().sendString(851, 31, ServerTeleports.LLETYA_FRUIT_TREE_PATCH.name);
		player.getPacketSender().sendString(851, 32, ServerTeleports.ETCETERIA_SPIRIT_TREE_PATCH.name);
		player.getPacketSender().sendString(851, 33, ServerTeleports.PORT_SARIM_SPIRIT_TREE_PATCH.name);
	}

	public void sendFarmingTeleportsPageThree(Player player) {
		pageNumber = 3;
		activateHiddenTeleportNames(player);
		player.getPacketSender().sendString(851, 22, ServerTeleports.BRIMHAVEN_SPIRIT_TREE_PATCH.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.AL_KHARID_CACTUS_PATCH.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.HARDWOOD_TREE_PATCHES.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.MUSHROOM_PATCH.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.LUMBRIDGE_HOPS.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.SEERS_HOPS.name);
		player.getPacketSender().sendString(851, 28, ServerTeleports.YANILLE_HOPS.name);
		player.getPacketSender().sendString(851, 29, ServerTeleports.ENTRANA_HOPS.name);
		player.getPacketSender().setHidden(851, 30, true);
		player.getPacketSender().setHidden(851, 31, true);
		player.getPacketSender().setHidden(851, 32, true);
		player.getPacketSender().setHidden(851, 33, true);
	}

	public void sendCityTeleports(Player player) {
		pageNumber = 1;
		currentCategory = Categories.CITIES;
		activateHiddenTeleportNames(player);
		player.getPacketSender().sendString(851, 20, "Cities");
		player.getPacketSender().setHidden(851, 38, true);
		player.getPacketSender().setHidden(851, 152, false);
		player.getPacketSender().setHidden(851, 153, false);
		player.getPacketSender().sendString(851, 22, ServerTeleports.VARROCK.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.LUMBRIDGE.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.FALADOR.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.SEERS.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.DRAYNOR.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.ARDOUGNE.name);
		player.getPacketSender().sendString(851, 28, ServerTeleports.CANIFIS.name);
		player.getPacketSender().sendString(851, 29, ServerTeleports.MOS_LE_HARMLESS.name);
		player.getPacketSender().sendString(851, 30, ServerTeleports.SHILO_VILLAGE.name);
		player.getPacketSender().sendString(851, 31, ServerTeleports.RELLEKA.name);
		player.getPacketSender().sendString(851, 32, ServerTeleports.BURTHORPE.name);
		player.getPacketSender().sendString(851, 33, ServerTeleports.LUNAR_ISLE.name);
	}

	public void sendCityTeleportsPageTwo(Player player) {
		pageNumber = 2;
		player.getPacketSender().sendString(851, 20, "Cities");
		player.getPacketSender().sendString(851, 22, ServerTeleports.POLLNIVNEACH.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.GNOME_STRONGHOLD.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.YANILLE.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.AL_KHARID.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.PORT_SARIM.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.BRIMHAVEN.name);
		player.getPacketSender().sendString(851, 28, ServerTeleports.APE_ATOLL.name);
		player.getPacketSender().sendString(851, 29, ServerTeleports.PRIFDDINAS.name);
		for (int i = 30; i < 34; i++) {
			player.getPacketSender().setHidden(851, i, true);
		}
	}

	public void sendTrainingTeleports(Player player) {
		currentCategory = Categories.TRAINING;
		activateHiddenTeleportNames(player);
		player.getPacketSender().sendString(851, 20, "Training");
		player.getPacketSender().setHidden(851, 38, true);
		player.getPacketSender().setHidden(851, 152, false);
		player.getPacketSender().setHidden(851, 153, false);
		player.getPacketSender().sendString(851, 22, ServerTeleports.ROCK_CRABS.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.SAND_CRABS.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.AMMONITE_CRABS.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.YAKS.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.CHICKENS.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.CHAOS_DRUID.name);
		player.getPacketSender().sendString(851, 28, ServerTeleports.EXPERIMENTS.name);
		player.getPacketSender().sendString(851, 29, ServerTeleports.CATALYSTS.name);
		player.getPacketSender().sendString(851, 30, ServerTeleports.SHAMANS.name);
		player.getPacketSender().sendString(851, 31, ServerTeleports.VYREWATCH_SENTINELS.name);
		player.getPacketSender().sendString(851, 32, ServerTeleports.BASILISK_KNIGHTS.name);
		player.getPacketSender().sendString(851, 33, ServerTeleports.ARMOURED_ZOMBIES.name);

	}

	public void sendMinigamesTeleports(Player player) {
		currentCategory = Categories.MINIGAMES;
		activateHiddenTeleportNames(player);
		player.getPacketSender().sendString(851, 20, "Minigames");
		player.getPacketSender().setHidden(851, 38, true);
		player.getPacketSender().setHidden(851, 152, false);
		player.getPacketSender().setHidden(851, 153, false);
		player.getPacketSender().sendString(851, 22, ServerTeleports.CHAMBERS_OF_XERIC.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.WARRIORS_GUILD.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.BARROWS.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.FIGHT_CAVES.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.INFERNO.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.PEST_CONTROL.name);
		player.getPacketSender().sendString(851, 28, ServerTeleports.DOMINION_OF_ECHOES.name);
		player.getPacketSender().sendString(851, 29, ServerTeleports.WINTERTODT.name);
		player.getPacketSender().sendString(851, 30, ServerTeleports.DUEL_ARENA.name);
		player.getPacketSender().sendString(851, 31, ServerTeleports.THE_GAUNTLET.name);
		player.getPacketSender().sendString(851, 32, ServerTeleports.THEATRE_OF_BLOOD.name);
		player.getPacketSender().sendString(851, 33, ServerTeleports.TOMBS_OF_AMASCUT.name);
	}

	public void sendWildernessTeleports(Player player) {
		currentCategory = Categories.WILDERNESS;
		player.getPacketSender().sendString(851, 20, "Wilderness");
		activateHiddenTeleportNames(player);
		player.getPacketSender().setHidden(851, 38, true);
		player.getPacketSender().setHidden(851, 152, false);
		player.getPacketSender().setHidden(851, 153, false);
		player.getPacketSender().sendString(851, 22, ServerTeleports.EAST_DRAGONS.name);
		player.getPacketSender().sendString(851, 23, ServerTeleports.WEST_DRAGONS.name);
		player.getPacketSender().sendString(851, 24, ServerTeleports.MAGE_BANK.name);
		player.getPacketSender().sendString(851, 25, ServerTeleports.REVENANTS.name);
		player.getPacketSender().sendString(851, 26, ServerTeleports.SCORPIA.name);
		player.getPacketSender().sendString(851, 27, ServerTeleports.CHAOS_FANATIC.name);
		player.getPacketSender().sendString(851, 28, ServerTeleports.CRAZY_ARCH.name);
		player.getPacketSender().sendString(851, 29, ServerTeleports.CHAOS_ELE.name);
		player.getPacketSender().sendString(851, 30, ServerTeleports.WILDY_RESOURCE_AREA.name);
		player.getPacketSender().sendString(851, 31, ServerTeleports.CHAOS_ALTAR.name);
		for (int i = 32; i < 34; i++) {
			player.getPacketSender().setHidden(851, i, true);
		}
	}

	public void startTeleport(Player player) {
		if (currentTeleport == null) {
			player.sendMessage("You need to select a teleport before you can teleport somewhere.");
			return;
		}
		if (currentTeleport == ServerTeleports.BALANCE_ELEMENTAL) {
			player.getInstanceTokenInterface().selectedBoss = InstanceMaps.BALANCE_ELEMENTAL;
			player.getInstanceTokenInterface().startInstance(player, true);
			player.lastServerTeleport = currentTeleport;
			return;
		}
		else if (currentTeleport == ServerTeleports.VARDORVIS) {
			player.getInstanceTokenInterface().selectedBoss = InstanceMaps.VARDORVIS;
			player.getInstanceTokenInterface().startInstance(player, true);
			player.lastServerTeleport = currentTeleport;
			return;
		}
		else if (currentTeleport == ServerTeleports.LEVIATHAN) {
			player.getInstanceTokenInterface().selectedBoss = InstanceMaps.LEVIATHAN;
			player.getInstanceTokenInterface().startInstance(player, true);
			player.lastServerTeleport = currentTeleport;
			return;
		}
		else if (currentTeleport == ServerTeleports.DUKE_SUCELLUS) {
			player.getInstanceTokenInterface().selectedBoss = InstanceMaps.DUKE;
			player.getInstanceTokenInterface().startInstance(player, true);
			player.lastServerTeleport = currentTeleport;
			return;
		}
		else if (currentTeleport == ServerTeleports.PHANTOM_MUSPAH) {
			player.getInstanceTokenInterface().selectedBoss = InstanceMaps.PHANTOM_MUSPAH;
			player.getInstanceTokenInterface().startInstance(player, true);
			player.lastServerTeleport = currentTeleport;
			return;
		}
		else if (currentTeleport == ServerTeleports.SOL_HEREDIT) {
			player.getInstanceTokenInterface().selectedBoss = InstanceMaps.SOL_HEREDIT;
			player.getInstanceTokenInterface().startInstance(player, true);
			player.lastServerTeleport = currentTeleport;
			return;
		}
		else if (currentTeleport == ServerTeleports.WHISPERER) {
			player.getInstanceTokenInterface().selectedBoss = InstanceMaps.WHISPERER;
			player.getInstanceTokenInterface().startInstance(player, true);
			player.lastServerTeleport = currentTeleport;
			return;
		}
		else if (currentTeleport == ServerTeleports.OPHIDIA) {
			player.dialogue(new OptionsDialogue("Which Ophidia's Lair would you like to enter?",
				new Option("Global Ophidia's Lair", () -> {
					Position teleportPosition = new Position(3149, 4658, 0);
					ModernTeleport.teleport(player, teleportPosition);
					player.lastServerTeleport = currentTeleport;
				}),
				new Option("Instanced Ophidia's Lair", () -> {
					player.getInstanceTokenInterface().selectedBoss = InstanceMaps.OPHIDIA;
					player.getInstanceTokenInterface().startInstance(player, true);
					player.lastServerTeleport = currentTeleport;
				})));
			return;
		}
		else if (currentTeleport == ServerTeleports.GALVEK) {
			player.dialogue(new OptionsDialogue("Which Galvek Lair would you like to enter?",
				new Option("Global Galvek Lair", () -> {
					ModernTeleport.teleport(player, currentTeleport.teleportPos);
					player.lastServerTeleport = currentTeleport;
				}),
				new Option("Instanced Galvek Lair", () -> {
					player.getInstanceTokenInterface().selectedBoss = InstanceMaps.GALVEK;
					player.getInstanceTokenInterface().startInstance(player, true);
					player.lastServerTeleport = currentTeleport;
				})));
			return;
		}
		else if (currentTeleport == ServerTeleports.GROTESQUE_GUARDIANS) {
			player.getInstanceTokenInterface().selectedBoss = InstanceMaps.GROTESQUE_GUARDIANS;
			player.getInstanceTokenInterface().startInstance(player, true);
			player.lastServerTeleport = currentTeleport;
			return;
		}
		if (currentTeleport == ServerTeleports.MALAKAR)
			currentTeleport.teleportPos = DonationBossHandler.malakarTeleportPosition;
		if (currentTeleport == ServerTeleports.PURO_PURO) {
			player.puropuroTravelledCounter++;
			if (player.puropuroTravelledCounter == Achievements.MIGHT_NEED_A_JAR_OR_TWO.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
					+ Achievements.MIGHT_NEED_A_JAR_OR_TWO.getAchievementName());

		}
		if (currentTeleport == ServerTeleports.SAND_CRABS) {
			player.clanChatSpeeches++;
			if (player.clanChatSpeeches == 1)
				player.sendMessage("<col=000080>You have completed the newcomer task: <col=800000>"
					+ NewcomerTasks.MISSING_HOME.getFormattedName() + "!");
		}
		player.homeTeleportsUsed++;
		if (player.homeTeleportsUsed == Achievements.NEXWORKING.getCompletionAmount())
			player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
				+ Achievements.NEXWORKING.getAchievementName());
		player.lastServerTeleport = currentTeleport;
		ModernTeleport.teleport(player, currentTeleport.teleportPos);
		player.closeInterfaces();
	}

	public void switchPage(Player player, boolean increasePage) {
		switch (currentCategory) {
			case BOSSES:
				maxPageNumber = 3;
				break;
			case SKILLING:
				if (currentSkillingSection == null)
					break;
				switch (currentSkillingSection) {
					case FARMING:
						maxPageNumber = 3;
						break;
					case SLAYER:
						maxPageNumber = 2;
						break;
					case HUNTER:
					case MINING:
					case AGILITY:
					case FISHING:
					case THIEVING:
					case WOODCUTTING:
					case RUNECRAFTING:
						maxPageNumber = 1;
						break;
				}
				break;
		}
		if (!increasePage) {
			if (pageNumber == 1) {
				player.sendMessage("You're already on the first page!");
				return;
			}
			if (currentCategory == Categories.BOSSES) {
				if (pageNumber == 3)
					sendBossTeleportsPageTwo(player);
				else if (pageNumber == 2)
					sendBossTeleports(player);
			}
			else if (currentCategory == Categories.SKILLING) {
				switch (currentSkillingSection) {
					case SLAYER:
						sendSlayerTeleports(player);
						break;
					case FARMING:
						if (pageNumber == 2)
							sendFarmingTeleports(player);
						else if (pageNumber == 3)
							sendFarmingTeleportsPageTwo(player);
						break;
				}
			}
			else if (currentCategory == Categories.CITIES)
				sendCityTeleports(player);

		}
		else {
			if (pageNumber == maxPageNumber) {
				player.sendMessage("You're already on the last page!");
				return;
			}
			if (currentCategory == Categories.BOSSES) {
				if (pageNumber == 1)
					sendBossTeleportsPageTwo(player);
				else if (pageNumber == 2)
					sendBossTeleportsPageThree(player);
			}
			else if (currentCategory == Categories.CITIES)
				sendCityTeleportsPageTwo(player);
			else if (currentCategory == Categories.SKILLING && currentSkillingSection != null) {
				switch (currentSkillingSection) {
					case SLAYER:
						sendSlayerTeleportsPageTwo(player);
						break;
					case FARMING:
						if (pageNumber == 1)
							sendFarmingTeleportsPageTwo(player);
						else if (pageNumber == 2)
							sendFarmingTeleportsPageThree(player);
						break;
				}
			}
		}

	}

	public void backToMenu(Player player) {
		player.getPacketSender().setHidden(851, 38, false);
		player.getPacketSender().setHidden(851, 117, true);
		player.getPacketSender().setHidden(851, 60, true);
		player.getPacketSender().setHidden(851, 61, true);
		player.getPacketSender().setHidden(851, 62, true);
		player.getPacketSender().setHidden(851, 63, true);
		player.getPacketSender().setHidden(851, 65, true);
		player.getPacketSender().setHidden(851, 66, true);
		player.getPacketSender().setHidden(851, 155, true);
		currentCategory = null;
		currentTeleport = null;
		currentSkillingSection = null;
		this.clear();
		this.sendAll = true;
		this.sendUpdates();
		player.getPacketSender().sendString(851, 109, "");
		player.getPacketSender().sendString(851, 113, "");
		player.getPacketSender().sendString(851, 111, "");
		player.getPacketSender().sendString(851, 58, "");
		player.getPacketSender().setHidden(851, 152, true);
		player.getPacketSender().setHidden(851, 153, true);
		player.getPacketSender().setHidden(851, 112, true);
		player.getPacketSender().setHidden(851, 110, true);
		for (int i = 156; i < 171; i++) {
			player.getPacketSender().setHidden(851, i, true);
		}

	}

	enum Categories {
		SKILLING,
		BOSSES,
		TRAINING,
		WILDERNESS,
		MINIGAMES,
		CITIES,
		SEARCH
	}

	enum SkillingSubSections {
		WOODCUTTING,
		FARMING,
		FISHING,
		RUNECRAFTING,
		MINING,
		HUNTER,
		AGILITY,
		THIEVING,
		SLAYER
	}

	public static void register() {
		InterfaceHandler.register(INTERFACE_ID, h -> {
			h.actions[116] = (SimpleAction) (p) -> {
				p.stringInput("Search teleports (name):", s -> {
					p.teleportInterface.search(p, s);
				});
			};
			for (int i = 156; i < 171; i++) {
				h.actions[i] = (DefaultAction) (player, option, slot, itemId) -> {
					switch (option) {
						case 10:
							player.sendMessage("" + new Item(itemId).getDef().examine);
							break;
					}
				};
			}
			h.actions[39] = (SimpleAction) (player) -> player.teleportInterface.sendSkillingSection(player);
			h.actions[40] = (SimpleAction) (player) -> player.teleportInterface.sendBossTeleports(player);
			h.actions[44] = (SimpleAction) (player) -> player.teleportInterface.sendCityTeleports(player);
			h.actions[41] = (SimpleAction) (player) -> player.teleportInterface.sendTrainingTeleports(player);
			h.actions[43] = (SimpleAction) (player) -> player.teleportInterface.sendMinigamesTeleports(player);
			h.actions[42] = (SimpleAction) (player) -> player.teleportInterface.sendWildernessTeleports(player);
			h.actions[152] = (SimpleAction) (player) -> player.teleportInterface.backToMenu(player);
			h.actions[22] = (SimpleAction) (player) -> player.teleportInterface.sendTeleport(player, 22);
			h.actions[23] = (SimpleAction) (player) -> player.teleportInterface.sendTeleport(player, 23);
			h.actions[24] = (SimpleAction) (player) -> player.teleportInterface.sendTeleport(player, 24);
			h.actions[25] = (SimpleAction) (player) -> player.teleportInterface.sendTeleport(player, 25);
			h.actions[26] = (SimpleAction) (player) -> player.teleportInterface.sendTeleport(player, 26);
			h.actions[27] = (SimpleAction) (player) -> player.teleportInterface.sendTeleport(player, 27);
			h.actions[28] = (SimpleAction) (player) -> player.teleportInterface.sendTeleport(player, 28);
			h.actions[29] = (SimpleAction) (player) -> player.teleportInterface.sendTeleport(player, 29);
			h.actions[30] = (SimpleAction) (player) -> player.teleportInterface.sendTeleport(player, 30);
			h.actions[31] = (SimpleAction) (player) -> player.teleportInterface.sendTeleport(player, 31);
			h.actions[32] = (SimpleAction) (player) -> player.teleportInterface.sendTeleport(player, 32);
			h.actions[33] = (SimpleAction) (player) -> player.teleportInterface.sendTeleport(player, 33);
			h.actions[35] = (SimpleAction) (player) -> player.teleportInterface.switchPage(player, false);
			h.actions[37] = (SimpleAction) (player) -> player.teleportInterface.switchPage(player, true);
			h.actions[64] = (SimpleAction) (player) -> player.teleportInterface.addToFavourites(player);
			h.actions[88] = (SimpleAction) (player) -> player.teleportInterface.sendTeleport(player,
				player.teleportFavourites.get(0));
			h.actions[89] = (SimpleAction) (player) -> player.teleportInterface.sendTeleport(player,
				player.teleportFavourites.get(1));
			h.actions[90] = (SimpleAction) (player) -> player.teleportInterface.sendTeleport(player,
				player.teleportFavourites.get(2));
			h.actions[91] = (SimpleAction) (player) -> player.teleportInterface.sendTeleport(player,
				player.teleportFavourites.get(3));
			h.actions[92] = (SimpleAction) (player) -> player.teleportInterface.sendTeleport(player,
				player.teleportFavourites.get(4));
			h.actions[93] = (SimpleAction) (player) -> player.teleportInterface.removeFromFavourites(player, 91);
			h.actions[94] = (SimpleAction) (player) -> player.teleportInterface.removeFromFavourites(player, 92);
			h.actions[95] = (SimpleAction) (player) -> player.teleportInterface.removeFromFavourites(player, 93);
			h.actions[96] = (SimpleAction) (player) -> player.teleportInterface.removeFromFavourites(player, 94);
			h.actions[97] = (SimpleAction) (player) -> player.teleportInterface.removeFromFavourites(player, 95);
			h.actions[114] = (SimpleAction) (player) -> player.teleportInterface.startTeleport(player);
			h.actions[124] = (SimpleAction) (player) -> player.teleportInterface.sendWoodcuttingTeleports(player);
			h.actions[125] = (SimpleAction) (player) -> player.teleportInterface.sendFarmingTeleports(player);
			h.actions[126] = (SimpleAction) (player) -> player.teleportInterface.sendFishingTeleports(player);
			h.actions[131] = (SimpleAction) (player) -> player.teleportInterface.sendRunecraftingTeleports(player);
			h.actions[132] = (SimpleAction) (player) -> player.teleportInterface.sendMiningTeleports(player);
			h.actions[133] = (SimpleAction) (player) -> player.teleportInterface.sendHunterTeleports(player);
			h.actions[138] = (SimpleAction) (player) -> player.teleportInterface.sendAgilityTeleports(player);
			h.actions[139] = (SimpleAction) (player) -> player.teleportInterface.sendThievingTeleports(player);
			h.actions[140] = (SimpleAction) (player) -> player.teleportInterface.sendSlayerTeleports(player);
		});
	}
}
