package io.ruin.model.activities.perktree;

import io.ruin.api.utils.NumberUtils;
import io.ruin.model.ScrollbarClientScript;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.impl.pet.perk.Perk;

import java.util.*;

public class PlayerPerkInterface {
	private static final int PLAYER_PERK_HOME_INTERFACE = 1094;
	private static final int PERK_SHOP_INTERFACE = 1095;
	private static final int PERK_SETS_INTERFACE = 1096;
	private static final int PERK_REPOSITORY = 1097;

	HashMap<Integer, Perks> perkRepository = new HashMap<>();
	HashMap<Integer, PerkSets> perkSetRepository = new HashMap<>();
	HashMap<Integer, PerkShop> perkShop = new HashMap<>();
	Perks currentPerk;
	PerkShop currentPurchase;

	RepositoryType repositoryType;

	enum RepositoryType {
		PERK,
		PERK_SET
	}

	public void openPlayerOwnedPerksSection(Player player) {
		perkRepository.clear();
		player.openInterface(ToplevelComponent.MAINMODAL, PERK_SETS_INTERFACE);
		player.getPacketSender().sendString(PERK_SETS_INTERFACE, 14, "Player Perks Management");
		player.getPacketSender().sendString(PERK_SETS_INTERFACE, 50, "Lvl<br>" + player.perkTreeLevel);
		for (int i = 0; i < 6; i++) {
			int component = 23 + (i * 5);
			if (i == 3) {
				component = 43;
			}
			if (i == 4) {
				component = 53;
			}
			if (i == 5) {
				component = 65;
			}
			player.getPacketSender().setHidden(PERK_SETS_INTERFACE, component, true);
		}
		for (int i = 0; i < 52; i++) {
			player.getPacketSender().setHidden(PERK_SETS_INTERFACE, 73 + (i * 7), true);
			player.getPacketSender().setHidden(PERK_SETS_INTERFACE, 78 + (i * 7), true);
			player.getPacketSender().setHidden(PERK_SETS_INTERFACE, 79 + (i * 7), true);
		}

		if (player.perkTreeLevel >= 50)
			player.getPacketSender().setHidden(PERK_SETS_INTERFACE, 38, true);
		if (player.perkTreeLevel >= 75)
			player.getPacketSender().setHidden(PERK_SETS_INTERFACE, 48, true);
		if (player.perkTreeLevel >= 100)
			player.getPacketSender().setHidden(PERK_SETS_INTERFACE, 60, true);

        /*
        Send active perks
         */
		int startingPerkIconComponentId = 22;
		int startingLevelComponent = 23;
		for (int i = 0; i < 6; i++) {
			if (i == 3) {
				startingLevelComponent = 43;
			}
			if (i == 4) {
				startingPerkIconComponentId = 47;
				startingLevelComponent = 53;
			}
			if (i == 5) {
				startingPerkIconComponentId = 59;
				startingLevelComponent = 65;
			}
			if (i >= player.getPlayerPerkHandler().getActivePerks(player).size())
				player.getPacketSender().setHidden(PERK_SETS_INTERFACE, startingPerkIconComponentId, true);
			else if (player.getPlayerPerkHandler().getActivePerks(player).get(i) == null) {
				player.getPacketSender().setHidden(PERK_SETS_INTERFACE, startingPerkIconComponentId, true);
			} else {
				int interfaceHash = PERK_SETS_INTERFACE << 16 | startingPerkIconComponentId;
				player.getPacketSender().setHidden(PERK_SETS_INTERFACE, startingLevelComponent, false);
				player.getPacketSender().sendString(PERK_SETS_INTERFACE, startingLevelComponent, "Lv. " + Objects.requireNonNull(player.getPlayerPerkHandler().getActivePerks(player).get(i).getPerk(player)).getPerkLevel());
				player.getPacketSender().sendClientScript(player.getPlayerPerkHandler().getActivePerks(player).get(i).scriptId, "I", interfaceHash);
			}
			startingPerkIconComponentId += 5;
			startingLevelComponent += 5;

		}


		int perkNameComponent = 76;
		int perkSmallInfoComponent = 77;
		int parentComponent = 73;
		int perkIconComponent = 75;
		player.ownedPerksList.sort(Comparator.comparing(perk -> Objects.requireNonNull(perk.getPerk(player)).getPerkName()));
		for (Perks perk :
			player.ownedPerksList) {
			perkRepository.put(parentComponent, perk);
			player.getPacketSender().sendString(PERK_SETS_INTERFACE, perkNameComponent, Objects.requireNonNull(perk.getPerk(player)).getPerkName());
			player.getPacketSender().sendString(PERK_SETS_INTERFACE, perkSmallInfoComponent, Objects.requireNonNull(perk.getPerk(player)).getPerkDescription());
			player.getPacketSender().setHidden(PERK_SETS_INTERFACE, parentComponent, false);
			int interfaceHash = PERK_SETS_INTERFACE << 16 | perkIconComponent;
			player.getPacketSender().sendClientScript(perk.scriptId, "I", interfaceHash);
			perkNameComponent += 7;
			perkSmallInfoComponent += 7;
			parentComponent += 7;
			perkIconComponent += 7;
		}
		if (player.getPlayerPerkHandler().getOwnedPerks(player).size() > 0) {
			sendOwnedPerkDetails(player, player.getPlayerPerkHandler().getOwnedPerks(player).get(0));
			currentPerk = player.getPlayerPerkHandler().getOwnedPerks(player).get(0);
		} else
			player.getPacketSender().setHidden(PERK_SETS_INTERFACE, 441, true);
	}


	public void sendOwnedPerkDetails(Player player, Perks perk) {
		currentPerk = perk;
		player.getPacketSender().setHidden(PERK_SETS_INTERFACE, 446, false);
		player.getPacketSender().sendString(PERK_SETS_INTERFACE, 444, Objects.requireNonNull(perk.getPerk(player)).getPerkName());
		player.getPacketSender().sendString(PERK_SETS_INTERFACE, 445, Objects.requireNonNull(perk.getPerk(player)).getPerkDescription());
		player.getPacketSender().sendString(PERK_SETS_INTERFACE, 448, Objects.requireNonNull(perk.getPerk(player)).getPerkEffect());
		player.getPacketSender().sendString(PERK_SETS_INTERFACE, 455, "Lv. " + Objects.requireNonNull(perk.getPerk(player)).getPerkLevel());
		int interfaceHash = PERK_SETS_INTERFACE << 16 | 443;
		player.getPacketSender().sendClientScript(perk.scriptId, "I", interfaceHash);

	}

	public void openPerkRepository(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, PERK_REPOSITORY);
		player.getPacketSender().sendString(PERK_REPOSITORY, 14, "Perk Repository");
		repositoryType = RepositoryType.PERK;
		perkRepository.clear();
		int parentComponent = 23;
		int perkIconComponent = 25;
		int perkNameComponent = 26;
		int perkDescriptionComponent = 27;
		List<Perks> perkList = Arrays.asList(Perks.VALUES);
		perkList.sort(Comparator.comparing(perk -> Objects.requireNonNull(perk.getInterfacePerk()).getPerkName()));
		sendPerkToRepository(player, perkList.get(0));
		for (Perks perk :
			perkList) {
			perkRepository.put(parentComponent, perk);
			int interfaceHash = PERK_REPOSITORY << 16 | perkIconComponent;
			player.getPacketSender().sendString(PERK_REPOSITORY, perkNameComponent, Objects.requireNonNull(perk.getInterfacePerk()).getPerkName());
			player.getPacketSender().sendString(PERK_REPOSITORY, perkDescriptionComponent, perk.getInterfacePerk().getPerkDescription());
			player.getPacketSender().sendClientScript(perk.scriptId, "I", interfaceHash);
			perkNameComponent += 5;
			perkDescriptionComponent += 5;
			parentComponent += 5;
			perkIconComponent += 5;
		}
	}

	public void openPerkSetRepository(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, PERK_REPOSITORY);
		player.getPacketSender().sendString(PERK_REPOSITORY, 14, "Perk Set Repository");
		repositoryType = RepositoryType.PERK_SET;
		perkRepository.clear();
		for (int i = 0; i < 51; i++)
			player.getPacketSender().setHidden(PERK_REPOSITORY, 23 + (i * 5), true);
		int parentComponent = 23;
		int perkIconComponent = 25;
		int perkNameComponent = 26;
		int perkDescriptionComponent = 27;

		List<PerkSets> perkSetList = Arrays.asList(PerkSets.values());
		perkSetList.sort(Comparator.comparing(Enum::name));
		sendPerkSetToRepository(player, perkSetList.get(0));
		for (PerkSets perk :
			perkSetList) {
			player.getPacketSender().setHidden(PERK_REPOSITORY, parentComponent, false);
			perkSetRepository.put(parentComponent, perk);
			player.getPacketSender().sendString(PERK_REPOSITORY, perkNameComponent, perk.perkSet.getPerkSetName());
			player.getPacketSender().sendString(PERK_REPOSITORY, perkDescriptionComponent, perk.perkSet.getPerkSetDescription());
			int interfaceHash = PERK_REPOSITORY << 16 | perkIconComponent;
			player.getPacketSender().sendClientScript(perk.scriptId, "I", interfaceHash);
			perkNameComponent += 5;
			perkDescriptionComponent += 5;
			parentComponent += 5;
			perkIconComponent += 5;
		}
	}

	private void sendPerkToRepository(Player player, Perks perk) {
		currentPerk = perk;
		player.getPacketSender().sendString(PERK_REPOSITORY, 285, Objects.requireNonNull(perk.getInterfacePerk()).getPerkName());
		player.getPacketSender().sendString(PERK_REPOSITORY, 286, Objects.requireNonNull(perk.getInterfacePerk()).getPerkDescription());
		player.getPacketSender().sendString(PERK_REPOSITORY, 287, Objects.requireNonNull(perk.getInterfacePerk()).getRepositoryDescription());
		player.getPacketSender().sendString(PERK_SETS_INTERFACE, 364, "Lv. " + Objects.requireNonNull(perk.getInterfacePerk()).getPerkLevel());
		int interfaceHash = PERK_REPOSITORY << 16 | 284;
		player.getPacketSender().sendClientScript(perk.scriptId, "I", interfaceHash);
		ScrollbarClientScript.create()
			.interfaceId(1097)
			.containerId(282)
			.scrollbarChildId(289)
			.childrenCount(56)
			.withDarkGraphics()
			.build()
			.send(player);

	}

	private void sendPerkSetToRepository(Player player, PerkSets perk) {
		player.getPacketSender().sendString(PERK_REPOSITORY, 285, perk.perkSet.getPerkSetName());
		player.getPacketSender().sendString(PERK_REPOSITORY, 286, perk.perkSet.getPerkSetDescription());
		int interfaceHash = PERK_REPOSITORY << 16 | 284;
		player.getPacketSender().sendClientScript(perk.scriptId, "I", interfaceHash);
		StringBuilder sb = new StringBuilder();
		sb.append(perk.perkSet.getPerkSetEffect());
		sb.append("<br>Combinations:<br><br>");
		for (Perks[] perkInComb : perk.perkCombinations) {
			for (int i = 0; i < perkInComb.length; i++) {
				Perks perkInComb2 = perkInComb[i];
				sb.append(Objects.requireNonNull(perkInComb2.getInterfacePerk()).getPerkName());
				if (i == perkInComb.length - 1) {
					sb.append(".<br>");
				} else {
					sb.append(", ");
				}
			}
			sb.append("<br>");
		}

		String perkSetEffect = sb.toString();


		player.getPacketSender().sendString(PERK_REPOSITORY, 287, perkSetEffect);
		ScrollbarClientScript.create()
			.interfaceId(1097)
			.containerId(282)
			.scrollbarChildId(289)
			.childrenCount(56)
			.withDarkGraphics()
			.build()
			.send(player);

	}

	public void openPlayerPerkHomeSection(Player player) {
		int nextLevel = player.perkTreeLevel + 1;
		player.getPacketSender().setHidden(PLAYER_PERK_HOME_INTERFACE, 95, true);
		player.openInterface(ToplevelComponent.MAINMODAL, PLAYER_PERK_HOME_INTERFACE);
		player.getPacketSender().sendString(PLAYER_PERK_HOME_INTERFACE, 42, "Perk points: " + NumberUtils.formatNumber(player.perkPoints));
		player.getPacketSender().sendString(PLAYER_PERK_HOME_INTERFACE, 49, "Perk Tree Level: <col=34ff00>" + player.perkTreeLevel);
		int experienceToNextLevel = player.getPlayerPerkHandler().calculateExperienceForLevel(player.perkTreeLevel) - player.perkTreeExperience;
		player.getPacketSender().sendString(PLAYER_PERK_HOME_INTERFACE, 48,
			"" + NumberUtils.formatNumber(experienceToNextLevel) + " XP until level " + nextLevel + "...");

		int barInterfaceHash = PLAYER_PERK_HOME_INTERFACE << 16 | 46;
		float barWidth = player.getPlayerPerkHandler().calculateNextPerkLevelPercentage(player) * 219;
		player.getPacketSender().sendClientScript(10606, "Ii", barInterfaceHash, (int) barWidth);

		int startingPerkIconComponentId = 63;
		int lvlComponent = 64;
		for (int i = 0; i < 6; i++) {
			if (i == 3) {
				lvlComponent = 84;
				startingPerkIconComponentId = 78;
			}
			if (i == 4) {
				startingPerkIconComponentId = 88;
				lvlComponent = 94;
			}
			if (i == 5) {
				startingPerkIconComponentId = 53;
				lvlComponent = 59;
			}
			player.getPacketSender().setHidden(PLAYER_PERK_HOME_INTERFACE, lvlComponent, true);
			if (i >= player.getPlayerPerkHandler().getActivePerks(player).size())
				player.getPacketSender().setHidden(PLAYER_PERK_HOME_INTERFACE, startingPerkIconComponentId, true);
			else if (player.getPlayerPerkHandler().getActivePerks(player).get(i) == null) {
				player.getPacketSender().setHidden(PLAYER_PERK_HOME_INTERFACE, startingPerkIconComponentId, true);
			} else {
				//  System.out.println("i is: " + i + " and startingPerkIconComponentId is: " + startingPerkIconComponentId + " and lvlComponent is: " + lvlComponent);
				int interfaceHash = PLAYER_PERK_HOME_INTERFACE << 16 | startingPerkIconComponentId;
				player.getPacketSender().setHidden(PLAYER_PERK_HOME_INTERFACE, lvlComponent, false);
				player.getPacketSender().sendString(PLAYER_PERK_HOME_INTERFACE, lvlComponent, "Lv. " + player.getPlayerPerkHandler().getActivePerks(player).get(i).getPerk(player).getPerkLevel());
				player.getPacketSender().sendClientScript(player.getPlayerPerkHandler().getActivePerks(player).get(i).scriptId, "I", interfaceHash);
			}
			lvlComponent += 5;
			startingPerkIconComponentId += 5;

		}
		String taskString;
		if (player.perkTreeLevel >= 50)
			player.getPacketSender().setHidden(1094, 79, true);
		if (player.perkTreeLevel >= 75)
			player.getPacketSender().setHidden(1094, 89, true);
		if (player.perkTreeLevel >= 100)
			player.getPacketSender().setHidden(1094, 54, true);

		if (player.currentPerkTask == null)
			taskString = "Current task: <br>You don't have a perk task, you can get one by talking to the perk master.";
		else {
			int taskAmountCompleted = player.perkTaskAssignedAmount - player.perkTaskCurrentAmount;
			taskString = "Current task:<br>" + player.currentPerkTask.description + player.currentPerkTask.description_2 + ".";
		}
		updatePresets(player);
		player.getPacketSender().sendString(PLAYER_PERK_HOME_INTERFACE, 20, taskString);
		player.getPacketSender().sendString(PLAYER_PERK_HOME_INTERFACE, 43, "Players can obtain fragments of perks which have decayed over time by complete various perk tasks. Individually, fragments contain a small portion of a relic's power, but they can be combined to create a powerful perk. The number of perks equipped is limited by the number of perk slots unlocked, up to a maximum of five. Perks can also be upgraded to level 5 within the owned perks section. Set effects become active once the player has equipped one of the correct perk combination from the perk set. Each set effect has a set level which will be the lowest perk level in the combination. You will level your perk tree up by obtaining experience from completing tasks set by The Perk Master, once you level up you will be rewarded with perk points, use these points to purchase new perks or upgrade the perks you already own.");
		player.getPacketSender().sendString(PLAYER_PERK_HOME_INTERFACE, 22, "Perk Info");
		player.getPacketSender().sendString(PLAYER_PERK_HOME_INTERFACE, 25, "Perk Sets<br>Info");
		player.getPacketSender().sendString(PLAYER_PERK_HOME_INTERFACE, 28, "Perk equip<br>or upgrade");
	}

	private void updatePresets(Player player) {
		player.getPacketSender().sendString(PLAYER_PERK_HOME_INTERFACE, 111, player.presetNameOne);
		player.getPacketSender().sendString(PLAYER_PERK_HOME_INTERFACE, 113, player.presetNameTwo);
		player.getPacketSender().sendString(PLAYER_PERK_HOME_INTERFACE, 115, player.presetNameThree);
		player.getPacketSender().sendString(PLAYER_PERK_HOME_INTERFACE, 117, player.presetNameFour);
		player.getPacketSender().sendString(PLAYER_PERK_HOME_INTERFACE, 119, player.presetNameFive);
	}

	public void openPerkShop(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, PERK_SHOP_INTERFACE);
		for (int i = 0; i < 52; i++)
			player.getPacketSender().setHidden(PERK_SHOP_INTERFACE, 37 + (i * 7), true);
		perkRepository.clear();
		perkShop.clear();

		List<PerkShop> perkShopList = Arrays.asList(PerkShop.values());
		perkShopList.sort(Comparator.comparingInt(PerkShop::getPrice));
		if (player.hasFreePerkUnlock)
			player.getPacketSender().setHidden(PERK_SHOP_INTERFACE, 20, false);
		else {
			player.getPacketSender().setHidden(PERK_SHOP_INTERFACE, 20, true);
		}

		int perkParentComponentId = 37;
		int perkIconComponentId = 39;
		int perkNameComponentId = 40;
		int perkDescriptionComponentId = 41;
		int perkPriceComponentId = 42;
		for (PerkShop perk :
			perkShopList) {
			String price = player.hasFreePerkUnlock ? "Free" : perk.getPrice() + " Perk points.";
			perkShop.put(perkParentComponentId, perk);
			if (player.getPlayerPerkHandler().getOwnedPerks(player).contains(perk.perk)) continue;
			player.getPacketSender().setHidden(PERK_SHOP_INTERFACE, perkParentComponentId, false);
			player.getPacketSender().sendString(PERK_SHOP_INTERFACE, perkNameComponentId, Objects.requireNonNull(perk.getPerk().getInterfacePerk()).getPerkName());
			player.getPacketSender().sendString(PERK_SHOP_INTERFACE, perkDescriptionComponentId, perk.getPerk().getInterfacePerk().getPerkDescription());
			player.getPacketSender().sendString(PERK_SHOP_INTERFACE, perkPriceComponentId, price);
			int interfaceHash = PERK_SHOP_INTERFACE << 16 | perkIconComponentId;
			player.getPacketSender().sendClientScript(perk.perk.scriptId, "I", interfaceHash);
			perkParentComponentId += 7;
			perkDescriptionComponentId += 7;
			perkNameComponentId += 7;
			perkIconComponentId += 7;
			perkPriceComponentId += 7;
		}
	}

	private void removePerkFromActivePerks(Player player, int index) {
		if (player.getPlayerPerkHandler().removeFromActivePerks(player, index)) {
			openPlayerOwnedPerksSection(player);
		}
	}

	private void addPerkToActivePerks(Player player, Perks perk) {
		if (player.getPlayerPerkHandler().addToActivePerks(player, perk)) {
			int size = player.activePerksList.size() - 1;
			int component;
			int lvlComponent = 0;
			switch (size) {
				case 0:
					component = 22;
					lvlComponent = 23;
					break;
				case 1:
					component = 27;
					lvlComponent = 28;
					break;
				case 2:
					component = 32;
					lvlComponent = 33;
					break;
				case 3:
					component = 37;
					lvlComponent = 43;
					break;
				case 4:
					component = 47;
					lvlComponent = 53;
					break;
				case 5:
					component = 59;
					lvlComponent = 65;
					break;
				default:
					component = 22 + (4 * size);
			}
			player.getPacketSender().setHidden(PERK_SETS_INTERFACE, lvlComponent, false);
			player.getPacketSender().sendString(PERK_SETS_INTERFACE, lvlComponent, "Lv. " + Objects.requireNonNull(perk.getPerk(player)).getPerkLevel());
			player.getPacketSender().setHidden(PERK_SETS_INTERFACE, component, false);
			int interfaceHash = PERK_SETS_INTERFACE << 16 | component;
			player.getPacketSender().sendClientScript(perk.scriptId, "I", interfaceHash);
		}
	}

	private boolean anyPresetsAvailable(Player player) {
		return player.perkPresetOne.isEmpty() || player.perkPresetTwo.isEmpty() || player.perkPresetThree.isEmpty() || player.perkPresetFour.isEmpty() || player.perkPresetFive.isEmpty();
	}

	private int presetsSaved(Player player) {
		int count = 0;
		if (!player.perkPresetOne.isEmpty()) count++;
		if (!player.perkPresetTwo.isEmpty()) count++;
		if (!player.perkPresetThree.isEmpty()) count++;
		if (!player.perkPresetFour.isEmpty()) count++;
		if (!player.perkPresetFive.isEmpty()) count++;
		return count;
	}

	private void savePreset(Player player, int slot) {
		player.stringInput("Enter a name for your preset:", name -> {
			if (name.length() > 12) {
				player.sendMessage("Your preset name must be 12 characters or less.");
				return;
			}
			if (slot == 0) {
				player.perkPresetOne.clear();
				player.perkPresetOne.putAll(player.activePerks);
				player.presetNameOne = name;
				player.sendMessage("Preset saved: " + name);
				updatePresets(player);
			}
			if (slot == 1) {
				player.perkPresetTwo.clear();
				player.perkPresetTwo.putAll(player.activePerks);
				player.presetNameTwo = name;
				player.sendMessage("Preset saved: " + name);
				updatePresets(player);
			}
			if (slot == 2) {
				player.perkPresetThree.clear();
				player.perkPresetThree.putAll(player.activePerks);
				player.presetNameThree = name;
				player.sendMessage("Preset saved: " + name);
				updatePresets(player);
			}
			if (slot == 3) {
				player.perkPresetFour.clear();
				player.perkPresetFour.putAll(player.activePerks);
				player.presetNameFour = name;
				player.sendMessage("Preset saved: " + name);
				updatePresets(player);
			}
			if (slot == 4) {
				player.perkPresetFive.clear();
				player.perkPresetFive.putAll(player.activePerks);
				player.presetNameFive = name;
				player.sendMessage("Preset saved: " + name);
				updatePresets(player);
			}
		});
	}

	public void savePerkPreset(Player player) {
		if (player.activePerks.isEmpty()) {
			player.sendMessage("You must have at least one perk equipped to save a preset.");
			return;
		}
		player.dialogue(new OptionsDialogue("Which preset slot would you like to save to?",
			new Option("Slot 1", () -> {
				savePreset(player, 0);
			}),
			new Option("Slot 2", () -> {
				savePreset(player, 1);
			}),
			new Option("Slot 3", () -> {
				savePreset(player, 2);
			}),
			new Option("Slot 4", () -> {
				savePreset(player, 3);
			}),
			new Option("Slot 5", () -> {
				savePreset(player, 4);
			}),
			new Option("Nevermind.", () -> {
			})));


	}


	public void loadPreset(Player player, int index) {
		player.activePerks.clear();

		if (index == 0) {
			if (player.perkPresetOne.isEmpty()) {
				player.sendMessage("You don't have a preset saved in this slot.");
				return;
			}
			player.activePerks.putAll(player.perkPresetOne);
			player.sendMessage("Loaded preset: " + player.presetNameOne);
		}
		if (index == 1) {
			if (player.perkPresetTwo.isEmpty()) {
				player.sendMessage("You don't have a preset saved in this slot.");
				return;
			}
			player.activePerks.putAll(player.perkPresetTwo);
			player.sendMessage("Loaded preset: " + player.presetNameTwo);
		}
		if (index == 2) {
			if (player.perkPresetThree.isEmpty()) {
				player.sendMessage("You don't have a preset saved in this slot.");
				return;
			}
			player.activePerks.putAll(player.perkPresetThree);
			player.sendMessage("Loaded preset: " + player.presetNameThree);
		}
		if (index == 3) {
			if (player.perkPresetFour.isEmpty()) {
				player.sendMessage("You don't have a preset saved in this slot.");
				return;
			}
			player.activePerks.putAll(player.perkPresetFour);
			player.sendMessage("Loaded preset: " + player.presetNameFour);
		}
		if (index == 4) {
			if (player.perkPresetFive.isEmpty()) {
				player.sendMessage("You don't have a preset saved in this slot.");
				return;
			}
			player.activePerks.putAll(player.perkPresetFive);
			player.sendMessage("Loaded preset: " + player.presetNameFive);
		}
		updatePresets(player);
		player.activePerksList.clear();
		for (Map.Entry<Integer, Integer> entry : player.activePerks.entrySet()) {
			int key = entry.getKey();
			int value = entry.getValue();
			Perks perks = getPerkByOrdinal(key);
			Objects.requireNonNull(perks.getPerk(player)).setPerkLevel(value);
			player.activePerksList.add(perks);
		}
		player.closeInterfaces();
		player.getPlayerPerkHandler().updateActivePerkSets(player);
		player.getPerkInterface().openPlayerPerkHomeSection(player);
	}

	public Perks getPerkByOrdinal(int ordinal) {
		for (Perks perk :
			Perks.VALUES) {
			if (perk.ordinal() == ordinal)
				return perk;
		}
		return null;
	}

	public void removePreset(Player player, int index) {
		if (index == 0) {
			player.perkPresetOne.clear();
			player.presetNameOne = "Empty";
		}
		if (index == 1) {
			player.perkPresetTwo.clear();
			player.presetNameTwo = "Empty";
		}
		if (index == 2) {
			player.perkPresetThree.clear();
			player.presetNameThree = "Empty";
		}
		if (index == 3) {
			player.perkPresetFour.clear();
			player.presetNameFour = "Empty";
		}
		if (index == 4) {
			player.perkPresetFive.clear();
			player.presetNameFive = "Empty";
		}

		updatePresets(player);
	}


	private void sendUpgradePerkWarning(Player player) {
		if (player.getName().equalsIgnoreCase("Dan Gleebles")) {

		} else {
			if (currentPerk.getPerk(player).perkLevel >= 5) {
				player.sendMessage("You can't upgrade this perk any further.");
				return;
			}
		}
		player.getPacketSender().setHidden(1096, 458, false);
		player.getPacketSender().sendString(1096, 476, "This will cost you " + currentPerk.getUpgradePrice() * Objects.requireNonNull(currentPerk.getPerk(player)).perkLevel + " perk points, are you sure?");
		player.getPacketSender().sendString(1096, 471, "Upgrade " + Objects.requireNonNull(currentPerk.getPerk(player)).getPerkName() + "?");
		player.getPacketSender().sendString(1096, 478, "Upgrade Perk");
		int interfaceHash = 1096 << 16 | 475;
		player.getPacketSender().sendClientScript(currentPerk.scriptId, "I", interfaceHash);

	}

	private void confirmUpgradePerk(Player player) {
		Perks perk = currentPerk;
		player.getPlayerPerkHandler().upgradePerk(player, currentPerk, currentPerk.getUpgradePrice() * Objects.requireNonNull(currentPerk.getPerk(player)).getPerkLevel());
		currentPerk = player.getPlayerPerkHandler().getPerkByOrdinal(perk.ordinal());
		openPlayerOwnedPerksSection(player);
		sendOwnedPerkDetails(player, perk);
		closeUpgradePerkWarning(player);
	}

	private void closeUpgradePerkWarning(Player player) {
		player.getPacketSender().setHidden(1096, 458, true);
	}

	private void sendShopBuyWarning(Player player, PerkShop perk) {
		currentPurchase = perk;
		player.getPacketSender().setHidden(1095, 403, false);
		if (player.hasFreePerkUnlock) {
			player.getPacketSender().sendString(1095, 421, "Do you wish to use your free perk level 5 unlock on this perk?");
			player.getPacketSender().sendString(1095, 423, "Confirm Free Perk");

		} else {
			player.getPacketSender().sendString(1095, 421, "This will cost you " + perk.getPrice() + " perk points, are you sure?");
			player.getPacketSender().sendString(1095, 423, "Confirm Purchase");
		}
		player.getPacketSender().sendString(1095, 416, Objects.requireNonNull(perk.getPerk().getInterfacePerk()).getPerkName());
		int interfaceHash = 1095 << 16 | 420;
		player.getPacketSender().sendClientScript(perk.getPerk().scriptId, "I", interfaceHash);
	}

	private void confirmPurchase(Player player, PerkShop perk) {
		player.getPlayerPerkHandler().purchasePerk(player, perk.getPerk(), perk.getPrice());
		closeShopBuyWarning(player);
		openPerkShop(player);
	}

	private void closeShopBuyWarning(Player player) {
		player.getPacketSender().setHidden(1095, 403, true);
	}

	public static void register() {
		InterfaceHandler.register(1097, h -> {
			for (int i = 23; i < 274; i += 5) {
				int finalI = i;
				h.actions[i] = (SimpleAction) (p) -> {
					if (p.getPerkInterface().repositoryType == RepositoryType.PERK)
						p.getPerkInterface().sendPerkToRepository(p, p.getPerkInterface().perkRepository.get(finalI));
					else p.getPerkInterface().sendPerkSetToRepository(p, p.getPerkInterface().perkSetRepository.get(finalI));
				};
			}
			h.actions[288] = (SimpleAction) (p) -> p.getPerkInterface().openPlayerPerkHomeSection(p);
		});
		InterfaceHandler.register(1096, h -> {
			for (int i = 73; i < 430; i += 7) {
				int finalI = i;
				h.actions[i] = (SimpleAction) (p) -> {
					p.getPerkInterface().sendOwnedPerkDetails(p, p.getPerkInterface().perkRepository.get(finalI));
				};
				h.actions[19] = (SimpleAction) (p) -> p.getPerkInterface().removePerkFromActivePerks(p, 0);
				h.actions[24] = (SimpleAction) (p) -> p.getPerkInterface().removePerkFromActivePerks(p, 1);
				h.actions[29] = (SimpleAction) (p) -> p.getPerkInterface().removePerkFromActivePerks(p, 2);
				h.actions[34] = (SimpleAction) (p) -> p.getPerkInterface().removePerkFromActivePerks(p, 3);
				h.actions[44] = (SimpleAction) (p) -> p.getPerkInterface().removePerkFromActivePerks(p, 4);
				h.actions[56] = (SimpleAction) (p) -> p.getPerkInterface().removePerkFromActivePerks(p, 5);
				h.actions[451] = (SimpleAction) (p) -> p.getPerkInterface().addPerkToActivePerks(p, p.getPerkInterface().currentPerk);
				h.actions[453] = (SimpleAction) (p) -> p.getPerkInterface().sendUpgradePerkWarning(p);
				h.actions[477] = (SimpleAction) (p) -> p.getPerkInterface().confirmUpgradePerk(p);
				h.actions[470] = (SimpleAction) (p) -> p.getPerkInterface().closeUpgradePerkWarning(p);
			}
			h.actions[457] = (SimpleAction) (p) -> p.getPerkInterface().openPlayerPerkHomeSection(p);
		});
		InterfaceHandler.register(1094, h -> {
			h.actions[21] = (SimpleAction) (p) -> p.getPerkInterface().openPerkRepository(p);
			h.actions[33] = (SimpleAction) (p) -> p.getPacketSender().setHidden(1094, 96, false);
			h.actions[108] = (SimpleAction) (p) -> p.getPacketSender().setHidden(1094, 96, true);
			h.actions[126] = (SimpleAction) (p) -> p.getPerkInterface().savePerkPreset(p);
			h.actions[110] = (SimpleAction) (p) -> p.getPerkInterface().loadPreset(p, 0);
			h.actions[112] = (SimpleAction) (p) -> p.getPerkInterface().loadPreset(p, 1);
			h.actions[114] = (SimpleAction) (p) -> p.getPerkInterface().loadPreset(p, 2);
			h.actions[116] = (SimpleAction) (p) -> p.getPerkInterface().loadPreset(p, 3);
			h.actions[118] = (SimpleAction) (p) -> p.getPerkInterface().loadPreset(p, 4);
			h.actions[120] = (SimpleAction) (p) -> p.getPerkInterface().removePreset(p, 0);
			h.actions[121] = (SimpleAction) (p) -> p.getPerkInterface().removePreset(p, 1);
			h.actions[122] = (SimpleAction) (p) -> p.getPerkInterface().removePreset(p, 2);
			h.actions[123] = (SimpleAction) (p) -> p.getPerkInterface().removePreset(p, 3);
			h.actions[124] = (SimpleAction) (p) -> p.getPerkInterface().removePreset(p, 4);
			h.actions[27] = (SimpleAction) (p) -> p.getPerkInterface().openPlayerOwnedPerksSection(p);
			h.actions[30] = (SimpleAction) (p) -> p.getPerkInterface().openPerkShop(p);
			h.actions[24] = (SimpleAction) (p) -> p.getPerkInterface().openPerkSetRepository(p);
		});
		InterfaceHandler.register(1095, h -> {
			for (int i = 37; i < 394; i += 7) {
				int finalI = i;
				h.actions[i] = (SimpleAction) (p) -> p.getPerkInterface().sendShopBuyWarning(p, p.getPerkInterface().perkShop.get(finalI));
			}
			h.actions[422] = (SimpleAction) (p) -> p.getPerkInterface().confirmPurchase(p, p.getPerkInterface().currentPurchase);
			h.actions[415] = (SimpleAction) (p) -> p.getPerkInterface().closeShopBuyWarning(p);
			h.actions[402] = (SimpleAction) (p) -> p.getPerkInterface().openPlayerPerkHomeSection(p);
		});
	}
}

