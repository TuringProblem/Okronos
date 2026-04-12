package io.ruin.model.inter.handlers;


import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.AccessMasks;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;


public class AchievementInterface extends ItemContainer {

	static ItemContainer container;

	public static void register() {
		InterfaceHandler.register(849, h -> {
			for (int i = 145; i < 151; i++) {
				h.actions[i] = (DefaultAction) (player, option, slot, itemId) -> {
					switch (option) {
						case 10:
							player.sendMessage("" + new Item(itemId).getDef().examine);
							break;
					}
				};
			}
		});
	}

	private static String insertCommas(String str) {
		if (str.length() < 4) {
			return str;
		}
		return insertCommas(str.substring(0, str.length() - 3)) + "," + str.substring(str.length() - 3, str.length());
	}

	transient Achievements currentAchievement;

	public void Init(Player player, String skill) {
		currentAchievement = player.currentAchievement;
		if (currentAchievement == null)
			currentAchievement = Achievements.ARE_YOU_SHORE_ABOUT_THIS;
		player.currentAchievement = currentAchievement;
		for (Achievements ach :
			Achievements.VALUES) {
			player.getPacketSender().setHidden(849, ach.getHiddenButtonID(), true);
		}
		player.getPacketSender().setHidden(849, currentAchievement.getHiddenButtonID(), false);
		AchievementsSwitcher(player);
		Open(currentAchievement.getItems(), player, currentAchievement.getItems().length, currentAchievement, 849, skill);
	}


	public void GetAchievementTypeButtonDown(Player player) {
		player.getPacketSender().setHidden(849, 133, true);
		player.getPacketSender().setHidden(849, 134, true);
		player.getPacketSender().setHidden(849, 135, true);
		player.getPacketSender().setHidden(849, 136, true);
		switch (player.currentAchievement.getAchievementType()) {
			case EASY:
				player.getPacketSender().setHidden(849, 133, false);
				break;
			case MEDIUM:
				player.getPacketSender().setHidden(849, 134, false);
				break;
			case HARD:
				player.getPacketSender().setHidden(849, 135, false);
				break;
			case ELITE:
				player.getPacketSender().setHidden(849, 136, false);
				break;
		}
	}

	private void Open(Item[] item, Player player, int containerSize, Achievements achievementID, int interfaceID, String skill) {

		for (int i = 145; i < 151; i++) {
			player.getPacketSender().setHidden(interfaceID, i, true);
		}
		player.openInterface(ToplevelComponent.MAINMODAL, 849);
		int startingContainerId = 2500;
		int startingContainer = 145;
		for (int i = 0; i < item.length; i++) {
			int amount = item[i].getAmount();
			if (player.getAchievementRewardClaimed(achievementID))
				amount = 0;
			player.getPacketSender().setHidden(849, startingContainer, false);
			player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				849 << 16 | startingContainer, startingContainerId,
				4, 7, 1, -1, "", "", "", "", ""
			);
			player.getPacketSender().sendItems(
				-1,
				startingContainer,
				startingContainerId,
				new Item(item[i].getId(), amount)
			);
			player.getPacketSender().sendIfEvents(849, startingContainer, 0, 27, 1024);
			player.getPacketSender().sendIfEvents(849, startingContainer, 0, 27, 1086);
			player.getPacketSender().sendIfEvents(849, startingContainer, 0, 5, AccessMasks.ClickOp1);
			player.getPacketSender().sendIfEvents(849, startingContainer, 0, 27, 1086);
			startingContainerId++;
			startingContainer++;
			if (startingContainer == 150)
				break;
		}
		TextUpdater(achievementID, player, skill);

		GetAchievementTypeButtonDown(player);
	}

	private void TextUpdater(Achievements achievement, Player player, String skill) {
		String reasonPointString = "<col=ffffff>-<col=ffff00> " + insertCommas(Integer.toString(achievement.getReasonPointReward())) + "<col=ffa31a> reason points.";
		String pkPointString = "<col=ffffff>-<col=ffff00> " + insertCommas(Integer.toString(achievement.getPKPointReward())) + "<col=ffa31a> PK points.";
		String achievementPointString = "<col=ffffff>-<col=ffff00> " + insertCommas(Integer.toString(achievement.getAchievementPointReward())) + "<col=ffa31a> achievement points.";
		String expRewardString = "<col=ffffff>-<col=ffff00> " + insertCommas(Integer.toString(achievement.getExperienceReward())) + " " + skill + "<col=ffa31a> exp.";
		int progress = player.getAchievementCurrentProgress(achievement) > achievement.completionAmount ? achievement.completionAmount : player.getAchievementCurrentProgress(achievement);
		String progressString = "<col=ffffff>Progress: <col=00ff00> (" + progress + "/" + achievement.completionAmount + ")";
		if (achievement.getExperienceReward() == 0)
			expRewardString = "<col=ffffff>-<col=ffa31a> No experience reward.";
		if (achievement.getReasonPointReward() == 0)
			reasonPointString = "";
		if (achievement.getPKPointReward() == 0)
			pkPointString = "";
		ChangeAchievement(player, achievement.getInterfaceID(), achievement.getAchievementName(), reasonPointString, progressString, achievementPointString, expRewardString, achievement.achievementDesc);
	}

	private void ChangeAchievement(Player player, int interfaceID, String achievementName, String reasonPointString, String progressString, String pointRewardString, String xpRewardString, String achievementDescString) {
		player.getPacketSender().sendString(interfaceID, 144, reasonPointString);
		player.getPacketSender().sendString(interfaceID, 25, achievementName);
		player.getPacketSender().sendString(interfaceID, 26, achievementDescString);
		player.getPacketSender().sendString(interfaceID, 27, progressString);
		player.getPacketSender().sendString(interfaceID, 142, xpRewardString);
		player.getPacketSender().sendString(interfaceID, 143, pointRewardString);
	}

	private void AchievementsSwitcher(Player player) {
		String crabKiller = "";
		if (player.crabKills > 0 && player.crabKills < Achievements.ARE_YOU_SHORE_ABOUT_THIS.completionAmount)
			crabKiller = "<col=ffff00>";
		else if (player.crabKills >= Achievements.ARE_YOU_SHORE_ABOUT_THIS.completionAmount && !player.getAchievementRewardClaimed(Achievements.ARE_YOU_SHORE_ABOUT_THIS))
			crabKiller = "<col=6495ed>";
		else if (player.crabKills >= Achievements.ARE_YOU_SHORE_ABOUT_THIS.completionAmount)
			crabKiller = "<col=00ff00>";

		String mindGames = "";
		if (player.strikeSpellCounter > 0 && player.strikeSpellCounter < Achievements.MIND_GAMES.completionAmount)
			mindGames = "<col=ffff00>";
		else if (player.strikeSpellCounter >= Achievements.MIND_GAMES.completionAmount && !player.getAchievementRewardClaimed(Achievements.MIND_GAMES))
			mindGames = "<col=6495ed>";
		else if (player.strikeSpellCounter >= Achievements.MIND_GAMES.completionAmount)
			mindGames = "<col=00ff00>";

		String divineSensesI = "";
		if (player.regularBonesBuriedOrSarcrificed > 0 && player.regularBonesBuriedOrSarcrificed < Achievements.DIVINE_SENSES_I.completionAmount)
			divineSensesI = "<col=ffff00>";
		else if (player.regularBonesBuriedOrSarcrificed >= Achievements.DIVINE_SENSES_I.completionAmount && !player.getAchievementRewardClaimed(Achievements.DIVINE_SENSES_I))
			divineSensesI = "<col=6495ed>";
		else if (player.regularBonesBuriedOrSarcrificed >= Achievements.DIVINE_SENSES_I.completionAmount)
			divineSensesI = "<col=00ff00>";

		String powerWithin = "";
		if (player.runecraftedRunesCounter > 0 && player.runecraftedRunesCounter < Achievements.THE_POWER_WITHIN.completionAmount)
			powerWithin = "<col=ffff00>";
		else if (player.runecraftedRunesCounter >= Achievements.THE_POWER_WITHIN.completionAmount && !player.getAchievementRewardClaimed(Achievements.THE_POWER_WITHIN))
			powerWithin = "<col=6495ed>";
		else if (player.runecraftedRunesCounter >= Achievements.THE_POWER_WITHIN.completionAmount)
			powerWithin = "<col=00ff00>";

		String leatherCrafted = "";
		if (player.leatherItemsCraftedCounter > 0 && player.leatherItemsCraftedCounter < Achievements.SKIN_IS_THE_GAME.completionAmount)
			leatherCrafted = "<col=ffff00>";
		else if (player.leatherItemsCraftedCounter >= Achievements.SKIN_IS_THE_GAME.completionAmount && !player.getAchievementRewardClaimed(Achievements.SKIN_IS_THE_GAME))
			leatherCrafted = "<col=6495ed>";
		else if (player.leatherItemsCraftedCounter >= Achievements.SKIN_IS_THE_GAME.completionAmount)
			leatherCrafted = "<col=00ff00>";

		String ironBarsSmelted = "";
		if (player.smeltedIronBars > 0 && player.smeltedIronBars < Achievements.IRON_INTELLECT.completionAmount)
			ironBarsSmelted = "<col=ffff00>";
		else if (player.smeltedIronBars >= Achievements.IRON_INTELLECT.completionAmount && !player.getAchievementRewardClaimed(Achievements.IRON_INTELLECT))
			ironBarsSmelted = "<col=6495ed>";
		else if (player.smeltedIronBars >= Achievements.IRON_INTELLECT.completionAmount)
			ironBarsSmelted = "<col=00ff00>";

		String fishFlyFished = "";
		if (player.flyFishCaughtCounter > 0 && player.flyFishCaughtCounter < Achievements.SOMETHING_IS_FISHY_HERE_I.completionAmount)
			fishFlyFished = "<col=ffff00>";
		else if (player.flyFishCaughtCounter >= Achievements.SOMETHING_IS_FISHY_HERE_I.completionAmount && !player.getAchievementRewardClaimed(Achievements.SOMETHING_IS_FISHY_HERE_I))
			fishFlyFished = "<col=6495ed>";
		else if (player.flyFishCaughtCounter >= Achievements.SOMETHING_IS_FISHY_HERE_I.completionAmount)
			fishFlyFished = "<col=00ff00>";

		String fishCooked = "";
		if (player.cookedFishCounter > 0 && player.cookedFishCounter < Achievements.JUST_FOR_THE_HALIBUT_I.completionAmount)
			fishCooked = "<col=ffff00>";
		else if (player.cookedFishCounter >= Achievements.JUST_FOR_THE_HALIBUT_I.completionAmount && !player.getAchievementRewardClaimed(Achievements.JUST_FOR_THE_HALIBUT_I))
			fishCooked = "<col=6495ed>";
		else if (player.cookedFishCounter >= Achievements.JUST_FOR_THE_HALIBUT_I.completionAmount)
			fishCooked = "<col=00ff00>";

		String oaksCut = "";
		if (player.choppedOak > 0 && player.choppedOak < Achievements.LUMBERJACK_I.completionAmount)
			oaksCut = "<col=ffff00>";
		else if (player.choppedOak >= Achievements.LUMBERJACK_I.completionAmount && !player.getAchievementRewardClaimed(Achievements.LUMBERJACK_I))
			oaksCut = "<col=6495ed>";
		else if (player.choppedOak >= Achievements.LUMBERJACK_I.completionAmount)
			oaksCut = "<col=00ff00>";

		String oaksBurned = "";
		if (player.oakLogsBurnt > 0 && player.oakLogsBurnt < Achievements.PYROMANCER_I.completionAmount)
			oaksBurned = "<col=ffff00>";
		else if (player.oakLogsBurnt >= Achievements.PYROMANCER_I.completionAmount && !player.getAchievementRewardClaimed(Achievements.PYROMANCER_I))
			oaksBurned = "<col=6495ed>";
		else if (player.oakLogsBurnt >= Achievements.PYROMANCER_I.completionAmount)
			oaksBurned = "<col=00ff00>";

		String shortbowsFletched = "";
		if (player.shortbowsFletchedCounter > 0 && player.shortbowsFletchedCounter < Achievements.THESE_SHOULDNT_TAKE_LONG_I.completionAmount)
			shortbowsFletched = "<col=ffff00>";
		else if (player.shortbowsFletchedCounter >= Achievements.THESE_SHOULDNT_TAKE_LONG_I.completionAmount && !player.getAchievementRewardClaimed(Achievements.THESE_SHOULDNT_TAKE_LONG_I))
			shortbowsFletched = "<col=6495ed>";
		else if (player.shortbowsFletchedCounter >= Achievements.THESE_SHOULDNT_TAKE_LONG_I.completionAmount)
			shortbowsFletched = "<col=00ff00>";

		String agilityLaps = "";
		if (player.agilityLapsRan > 0 && player.agilityLapsRan < Achievements.ANOTHER_ONE_BITES_THE_DUST.completionAmount)
			agilityLaps = "<col=ffff00>";
		else if (player.agilityLapsRan >= Achievements.ANOTHER_ONE_BITES_THE_DUST.completionAmount && !player.getAchievementRewardClaimed(Achievements.ANOTHER_ONE_BITES_THE_DUST))
			agilityLaps = "<col=6495ed>";
		else if (player.agilityLapsRan >= Achievements.ANOTHER_ONE_BITES_THE_DUST.completionAmount)
			agilityLaps = "<col=00ff00>";

		String slayerTasks = "";
		if (player.totalSlayerTasksCompleted > 0 && player.totalSlayerTasksCompleted < Achievements.RAISING_THE_BAR.completionAmount)
			slayerTasks = "<col=ffff00>";
		else if (player.totalSlayerTasksCompleted >= Achievements.RAISING_THE_BAR.completionAmount && !player.getAchievementRewardClaimed(Achievements.RAISING_THE_BAR))
			slayerTasks = "<col=6495ed>";
		else if (player.totalSlayerTasksCompleted >= Achievements.RAISING_THE_BAR.completionAmount)
			slayerTasks = "<col=00ff00>";

		String rakeHerbPatch = "";
		if (player.herbPatchesRaked > 0 && player.herbPatchesRaked < Achievements.THE_GRASS_IS_ALWAYS_GREENER.completionAmount)
			rakeHerbPatch = "<col=ffff00>";
		else if (player.herbPatchesRaked >= Achievements.THE_GRASS_IS_ALWAYS_GREENER.completionAmount && !player.getAchievementRewardClaimed(Achievements.THE_GRASS_IS_ALWAYS_GREENER))
			rakeHerbPatch = "<col=6495ed>";
		else if (player.herbPatchesRaked >= Achievements.THE_GRASS_IS_ALWAYS_GREENER.completionAmount)
			rakeHerbPatch = "<col=00ff00>";

		String firstTimeHomeOwner = "";
		if (player.housesBought > 0 && player.housesBought < Achievements.FIRST_TIME_HOME_OWNER.completionAmount)
			firstTimeHomeOwner = "<col=ffff00>";
		else if (player.housesBought >= Achievements.FIRST_TIME_HOME_OWNER.completionAmount && !player.getAchievementRewardClaimed(Achievements.FIRST_TIME_HOME_OWNER))
			firstTimeHomeOwner = "<col=6495ed>";
		else if (player.housesBought >= Achievements.FIRST_TIME_HOME_OWNER.completionAmount)
			firstTimeHomeOwner = "<col=00ff00>";

		String huntIsOn1 = "";
		if (player.copperLongtailCatches > 0 && player.copperLongtailCatches < Achievements.THE_HUNT_IS_ON_I.completionAmount)
			huntIsOn1 = "<col=ffff00>";
		else if (player.copperLongtailCatches >= Achievements.THE_HUNT_IS_ON_I.completionAmount && !player.getAchievementRewardClaimed(Achievements.THE_HUNT_IS_ON_I))
			huntIsOn1 = "<col=6495ed>";
		else if (player.copperLongtailCatches >= Achievements.THE_HUNT_IS_ON_I.completionAmount)
			huntIsOn1 = "<col=00ff00>";

		String dontBeGrimy = "";
		if (player.guamsCleanedCounter > 0 && player.guamsCleanedCounter < Achievements.DONT_BE_SO_GRIMY.completionAmount)
			dontBeGrimy = "<col=ffff00>";
		else if (player.guamsCleanedCounter >= Achievements.DONT_BE_SO_GRIMY.completionAmount && !player.getAchievementRewardClaimed(Achievements.DONT_BE_SO_GRIMY))
			dontBeGrimy = "<col=6495ed>";
		else if (player.guamsCleanedCounter >= Achievements.DONT_BE_SO_GRIMY.completionAmount)
			dontBeGrimy = "<col=00ff00>";

		String thankGodForThat = "";
		if (player.magicCapesBought > 0 && player.magicCapesBought < Achievements.THANK_GOD_FOR_THAT.completionAmount)
			thankGodForThat = "<col=ffff00>";
		else if (player.magicCapesBought >= Achievements.THANK_GOD_FOR_THAT.completionAmount && !player.getAchievementRewardClaimed(Achievements.THANK_GOD_FOR_THAT))
			thankGodForThat = "<col=6495ed>";
		else if (player.magicCapesBought >= Achievements.THANK_GOD_FOR_THAT.completionAmount)
			thankGodForThat = "<col=00ff00>";

		String justALittleBoost = "";
		if (player.homePoolsUsed > 0 && player.homePoolsUsed < Achievements.JUST_A_LITTLE_BOOST.completionAmount)
			justALittleBoost = "<col=ffff00>";
		else if (player.homePoolsUsed >= Achievements.JUST_A_LITTLE_BOOST.completionAmount && !player.getAchievementRewardClaimed(Achievements.JUST_A_LITTLE_BOOST))
			justALittleBoost = "<col=6495ed>";
		else if (player.homePoolsUsed >= Achievements.JUST_A_LITTLE_BOOST.completionAmount)
			justALittleBoost = "<col=00ff00>";

		String nexworking = "";
		if (player.homeTeleportsUsed > 0 && player.homeTeleportsUsed < Achievements.NEXWORKING.completionAmount)
			nexworking = "<col=ffff00>";
		else if (player.homeTeleportsUsed >= Achievements.NEXWORKING.completionAmount && !player.getAchievementRewardClaimed(Achievements.NEXWORKING))
			nexworking = "<col=6495ed>";
		else if (player.homeTeleportsUsed >= Achievements.NEXWORKING.completionAmount)
			nexworking = "<col=00ff00>";

		String ohCul = "";
		if (player.homeSpellbookAltarsUsed > 0 && player.homeSpellbookAltarsUsed < Achievements.OH_CUL.completionAmount)
			ohCul = "<col=ffff00>";
		else if (player.homeSpellbookAltarsUsed >= Achievements.OH_CUL.completionAmount && !player.getAchievementRewardClaimed(Achievements.OH_CUL))
			ohCul = "<col=6495ed>";
		else if (player.homeSpellbookAltarsUsed >= Achievements.OH_CUL.completionAmount)
			ohCul = "<col=00ff00>";

		String openYourMine = "";
		if (player.enteredMotherloadMineCounter > 0 && player.enteredMotherloadMineCounter < Achievements.OPEN_YOUR_MINE.completionAmount)
			openYourMine = "<col=ffff00>";
		else if (player.enteredMotherloadMineCounter >= Achievements.OPEN_YOUR_MINE.completionAmount && !player.getAchievementRewardClaimed(Achievements.OPEN_YOUR_MINE))
			openYourMine = "<col=6495ed>";
		else if (player.enteredMotherloadMineCounter >= Achievements.OPEN_YOUR_MINE.completionAmount)
			openYourMine = "<col=00ff00>";

		String quitePuzzled = "";
		if (player.easyClueCount > 0 && player.easyClueCount < Achievements.QUITE_PUZZLED.completionAmount)
			quitePuzzled = "<col=ffff00>";
		else if (player.easyClueCount >= Achievements.QUITE_PUZZLED.completionAmount && !player.getAchievementRewardClaimed(Achievements.QUITE_PUZZLED))
			quitePuzzled = "<col=6495ed>";
		else if (player.easyClueCount >= Achievements.QUITE_PUZZLED.completionAmount)
			quitePuzzled = "<col=00ff00>";

		String thirdPartyCandidate = "";
		if (player.claimedVotes > 0 && player.claimedVotes < Achievements.THIRD_PART_CANDIDATE.completionAmount)
			thirdPartyCandidate = "<col=ffff00>";
		else if (player.claimedVotes >= Achievements.THIRD_PART_CANDIDATE.completionAmount && !player.getAchievementRewardClaimed(Achievements.THIRD_PART_CANDIDATE))
			thirdPartyCandidate = "<col=6495ed>";
		else if (player.claimedVotes >= Achievements.THIRD_PART_CANDIDATE.completionAmount)
			thirdPartyCandidate = "<col=00ff00>";

		String feelingLikeANewMan = "";
		if (player.makeOverMageCounter > 0 && player.makeOverMageCounter < Achievements.FEELING_LIKE_A_NEW_MAN.completionAmount)
			feelingLikeANewMan = "<col=ffff00>";
		else if (player.makeOverMageCounter >= Achievements.FEELING_LIKE_A_NEW_MAN.completionAmount && !player.getAchievementRewardClaimed(Achievements.FEELING_LIKE_A_NEW_MAN))
			feelingLikeANewMan = "<col=6495ed>";
		else if (player.makeOverMageCounter >= Achievements.FEELING_LIKE_A_NEW_MAN.completionAmount)
			feelingLikeANewMan = "<col=00ff00>";

		String ibantBelieveThis = "";
		if (player.ibanBlastsCast > 0 && player.ibanBlastsCast < Achievements.IBANT_BELIEVE_THIS.completionAmount)
			ibantBelieveThis = "<col=ffff00>";
		else if (player.ibanBlastsCast >= Achievements.IBANT_BELIEVE_THIS.completionAmount && !player.getAchievementRewardClaimed(Achievements.IBANT_BELIEVE_THIS))
			ibantBelieveThis = "<col=6495ed>";
		else if (player.ibanBlastsCast >= Achievements.IBANT_BELIEVE_THIS.completionAmount)
			ibantBelieveThis = "<col=00ff00>";

		String itsMagicYouKnow = "";
		if (player.magicShortbowAttacks > 0 && player.magicShortbowAttacks < Achievements.ITS_MAGIC_YOU_KNOW.completionAmount)
			itsMagicYouKnow = "<col=ffff00>";
		else if (player.magicShortbowAttacks >= Achievements.ITS_MAGIC_YOU_KNOW.completionAmount && !player.getAchievementRewardClaimed(Achievements.ITS_MAGIC_YOU_KNOW))
			itsMagicYouKnow = "<col=6495ed>";
		else if (player.magicShortbowAttacks >= Achievements.ITS_MAGIC_YOU_KNOW.completionAmount)
			itsMagicYouKnow = "<col=00ff00>";

		String passedTheBarr = "";
		if (player.barrowsCompletedKillingAllBrothers > 0 && player.barrowsCompletedKillingAllBrothers < Achievements.PASSED_THE_BARR.completionAmount)
			passedTheBarr = "<col=ffff00>";
		else if (player.barrowsCompletedKillingAllBrothers >= Achievements.PASSED_THE_BARR.completionAmount && !player.getAchievementRewardClaimed(Achievements.PASSED_THE_BARR))
			passedTheBarr = "<col=6495ed>";
		else if (player.barrowsCompletedKillingAllBrothers >= Achievements.PASSED_THE_BARR.completionAmount)
			passedTheBarr = "<col=00ff00>";

		String divineSensesII = "";
		if (player.dragonBonesBuriedOrSacrificed > 0 && player.dragonBonesBuriedOrSacrificed < Achievements.DIVINE_SENSES_II.completionAmount)
			divineSensesII = "<col=ffff00>";
		else if (player.dragonBonesBuriedOrSacrificed >= Achievements.DIVINE_SENSES_II.completionAmount && !player.getAchievementRewardClaimed(Achievements.DIVINE_SENSES_II))
			divineSensesII = "<col=6495ed>";
		else if (player.dragonBonesBuriedOrSacrificed >= Achievements.DIVINE_SENSES_II.completionAmount)
			divineSensesII = "<col=00ff00>";

		String astralWorld = "";
		if (player.astralsCrafted > 0 && player.astralsCrafted < Achievements.ASTRALWORLD.completionAmount)
			astralWorld = "<col=ffff00>";
		else if (player.astralsCrafted >= Achievements.ASTRALWORLD.completionAmount && !player.getAchievementRewardClaimed(Achievements.ASTRALWORLD))
			astralWorld = "<col=6495ed>";
		else if (player.astralsCrafted >= Achievements.ASTRALWORLD.completionAmount)
			astralWorld = "<col=00ff00>";

		String howManyRingsDoYouNeed = "";
		if (player.diamondsCut > 0 && player.diamondsCut < Achievements.HOW_MANY_RINGS_DO_YOU_NEED.completionAmount)
			howManyRingsDoYouNeed = "<col=ffff00>";
		else if (player.diamondsCut >= Achievements.HOW_MANY_RINGS_DO_YOU_NEED.completionAmount && !player.getAchievementRewardClaimed(Achievements.HOW_MANY_RINGS_DO_YOU_NEED))
			howManyRingsDoYouNeed = "<col=6495ed>";
		else if (player.diamondsCut >= Achievements.HOW_MANY_RINGS_DO_YOU_NEED.completionAmount)
			howManyRingsDoYouNeed = "<col=00ff00>";

		String didYouMithOne = "";
		if (player.smeltedMithrilBars > 0 && player.smeltedMithrilBars < Achievements.DID_YOU_MITH_ONE.completionAmount)
			didYouMithOne = "<col=ffff00>";
		else if (player.smeltedMithrilBars >= Achievements.DID_YOU_MITH_ONE.completionAmount && !player.getAchievementRewardClaimed(Achievements.DID_YOU_MITH_ONE))
			didYouMithOne = "<col=6495ed>";
		else if (player.smeltedMithrilBars >= Achievements.DID_YOU_MITH_ONE.completionAmount)
			didYouMithOne = "<col=00ff00>";

		String somethingIsFishyHereII = "";
		if (player.swordfishFished > 0 && player.swordfishFished < Achievements.SOMETHING_IS_FISHY_HERE_II.completionAmount)
			somethingIsFishyHereII = "<col=ffff00>";
		else if (player.swordfishFished >= Achievements.SOMETHING_IS_FISHY_HERE_II.completionAmount && !player.getAchievementRewardClaimed(Achievements.SOMETHING_IS_FISHY_HERE_II))
			somethingIsFishyHereII = "<col=6495ed>";
		else if (player.swordfishFished >= Achievements.SOMETHING_IS_FISHY_HERE_II.completionAmount)
			somethingIsFishyHereII = "<col=00ff00>";

		String justForTheHalibutII = "";
		if (player.swordfishCooked > 0 && player.swordfishCooked < Achievements.JUST_FOR_THE_HALIBUT_II.completionAmount)
			justForTheHalibutII = "<col=ffff00>";
		else if (player.swordfishCooked >= Achievements.JUST_FOR_THE_HALIBUT_II.completionAmount && !player.getAchievementRewardClaimed(Achievements.JUST_FOR_THE_HALIBUT_II))
			justForTheHalibutII = "<col=6495ed>";
		else if (player.swordfishCooked >= Achievements.JUST_FOR_THE_HALIBUT_II.completionAmount)
			justForTheHalibutII = "<col=00ff00>";

		String pyromancerII = "";
		if (player.wintertodtKills.getKills() > 0 && player.wintertodtKills.getKills() < Achievements.PYROMANCER_II.completionAmount)
			pyromancerII = "<col=ffff00>";
		else if (player.wintertodtKills.getKills() >= Achievements.PYROMANCER_II.completionAmount && !player.getAchievementRewardClaimed(Achievements.PYROMANCER_II))
			pyromancerII = "<col=6495ed>";
		else if (player.wintertodtKills.getKills() >= Achievements.PYROMANCER_II.completionAmount)
			pyromancerII = "<col=00ff00>";

		String lumberjackII = "";
		if (player.choppedYew > 0 && player.choppedYew < Achievements.LUMBERJACK_II.completionAmount)
			lumberjackII = "<col=ffff00>";
		else if (player.choppedYew >= Achievements.LUMBERJACK_II.completionAmount && !player.getAchievementRewardClaimed(Achievements.LUMBERJACK_II))
			lumberjackII = "<col=6495ed>";
		else if (player.choppedYew >= Achievements.LUMBERJACK_II.completionAmount)
			lumberjackII = "<col=00ff00>";

		String nowThatIsUnnecessary = "";
		if (player.mahoganyArmchairsMade > 0 && player.mahoganyArmchairsMade < Achievements.NOW_THAT_IS_JUST_UNNCECESSARY.completionAmount)
			nowThatIsUnnecessary = "<col=ffff00>";
		else if (player.mahoganyArmchairsMade >= Achievements.NOW_THAT_IS_JUST_UNNCECESSARY.completionAmount && !player.getAchievementRewardClaimed(Achievements.NOW_THAT_IS_JUST_UNNCECESSARY))
			nowThatIsUnnecessary = "<col=6495ed>";
		else if (player.mahoganyArmchairsMade >= Achievements.NOW_THAT_IS_JUST_UNNCECESSARY.completionAmount)
			nowThatIsUnnecessary = "<col=00ff00>";

		String limbsToLimbs = "";
		if (player.runiteCrossbowsFletched > 0 && player.runiteCrossbowsFletched < Achievements.LIMBS_TO_LIMBS.completionAmount)
			limbsToLimbs = "<col=ffff00>";
		else if (player.runiteCrossbowsFletched >= Achievements.LIMBS_TO_LIMBS.completionAmount && !player.getAchievementRewardClaimed(Achievements.LIMBS_TO_LIMBS))
			limbsToLimbs = "<col=6495ed>";
		else if (player.runiteCrossbowsFletched >= Achievements.LIMBS_TO_LIMBS.completionAmount)
			limbsToLimbs = "<col=00ff00>";

		String cantKeepMyHandsToMyselfII = "";
		if (player.knightsPickpocketed > 0 && player.knightsPickpocketed < Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_II.completionAmount)
			cantKeepMyHandsToMyselfII = "<col=ffff00>";
		else if (player.knightsPickpocketed >= Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_II.completionAmount && !player.getAchievementRewardClaimed(Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_II))
			cantKeepMyHandsToMyselfII = "<col=6495ed>";
		else if (player.knightsPickpocketed >= Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_II.completionAmount)
			cantKeepMyHandsToMyselfII = "<col=00ff00>";

		String cantKeepMyHandsToMyselfI = "";
		if (player.manPickpocketCounter > 0 && player.manPickpocketCounter < Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_I.completionAmount)
			cantKeepMyHandsToMyselfI = "<col=ffff00>";
		else if (player.manPickpocketCounter >= Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_I.completionAmount && !player.getAchievementRewardClaimed(Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_I))
			cantKeepMyHandsToMyselfI = "<col=6495ed>";
		else if (player.manPickpocketCounter >= Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_I.completionAmount)
			cantKeepMyHandsToMyselfI = "<col=00ff00>";

		String dontMindIfIDo = "";
		if (player.slayerTasksBlockedOrExtended > 0 && player.slayerTasksBlockedOrExtended < Achievements.DONT_MIND_IF_I_DO.completionAmount)
			dontMindIfIDo = "<col=ffff00>";
		else if (player.slayerTasksBlockedOrExtended >= Achievements.DONT_MIND_IF_I_DO.completionAmount && !player.getAchievementRewardClaimed(Achievements.DONT_MIND_IF_I_DO))
			dontMindIfIDo = "<col=6495ed>";
		else if (player.slayerTasksBlockedOrExtended >= Achievements.DONT_MIND_IF_I_DO.completionAmount)
			dontMindIfIDo = "<col=00ff00>";

		String inBloom = "";
		if (player.treesGrown > 0 && player.treesGrown < Achievements.IN_BLOOM.completionAmount)
			inBloom = "<col=ffff00>";
		else if (player.treesGrown >= Achievements.IN_BLOOM.completionAmount && !player.getAchievementRewardClaimed(Achievements.IN_BLOOM))
			inBloom = "<col=6495ed>";
		else if (player.treesGrown >= Achievements.IN_BLOOM.completionAmount)
			inBloom = "<col=00ff00>";

		String isThisEvenSafe = "";
		if (player.totalChinchompasCaught > 0 && player.totalChinchompasCaught < Achievements.IS_THIS_EVEN_SAFE.completionAmount)
			isThisEvenSafe = "<col=ffff00>";
		else if (player.totalChinchompasCaught >= Achievements.IS_THIS_EVEN_SAFE.completionAmount && !player.getAchievementRewardClaimed(Achievements.IS_THIS_EVEN_SAFE))
			isThisEvenSafe = "<col=6495ed>";
		else if (player.totalChinchompasCaught >= Achievements.IS_THIS_EVEN_SAFE.completionAmount)
			isThisEvenSafe = "<col=00ff00>";

		String mightNeedTheseLater = "";
		if (player.superRestoresMixed > 0 && player.superRestoresMixed < Achievements.MIGHT_NEED_THESE_LATER.completionAmount)
			mightNeedTheseLater = "<col=ffff00>";
		else if (player.superRestoresMixed >= Achievements.MIGHT_NEED_THESE_LATER.completionAmount && !player.getAchievementRewardClaimed(Achievements.MIGHT_NEED_THESE_LATER))
			mightNeedTheseLater = "<col=6495ed>";
		else if (player.superRestoresMixed >= Achievements.MIGHT_NEED_THESE_LATER.completionAmount)
			mightNeedTheseLater = "<col=00ff00>";

		String goneInTheBlinkOfAnEye = "";
		if (player.fairyRingsUsed > 0 && player.fairyRingsUsed < Achievements.GONE_IN_THE_BLINK_OF_AN_EYE.completionAmount)
			goneInTheBlinkOfAnEye = "<col=ffff00>";
		else if (player.fairyRingsUsed >= Achievements.GONE_IN_THE_BLINK_OF_AN_EYE.completionAmount && !player.getAchievementRewardClaimed(Achievements.GONE_IN_THE_BLINK_OF_AN_EYE))
			goneInTheBlinkOfAnEye = "<col=6495ed>";
		else if (player.fairyRingsUsed >= Achievements.GONE_IN_THE_BLINK_OF_AN_EYE.completionAmount)
			goneInTheBlinkOfAnEye = "<col=00ff00>";

		String whatDoWeHaveHereI = "";
		if (player.brimstoneChestsOpened > 0 && player.brimstoneChestsOpened < Achievements.WHAT_DO_WE_HAVE_HERE_I.completionAmount)
			whatDoWeHaveHereI = "<col=ffff00>";
		else if (player.brimstoneChestsOpened >= Achievements.WHAT_DO_WE_HAVE_HERE_I.completionAmount && !player.getAchievementRewardClaimed(Achievements.WHAT_DO_WE_HAVE_HERE_I))
			whatDoWeHaveHereI = "<col=6495ed>";
		else if (player.brimstoneChestsOpened >= Achievements.WHAT_DO_WE_HAVE_HERE_I.completionAmount)
			whatDoWeHaveHereI = "<col=00ff00>";

		String mightNeedAJarOrTwo = "";
		if (player.puropuroTravelledCounter > 0 && player.puropuroTravelledCounter < Achievements.MIGHT_NEED_A_JAR_OR_TWO.completionAmount)
			mightNeedAJarOrTwo = "<col=ffff00>";
		else if (player.puropuroTravelledCounter >= Achievements.MIGHT_NEED_A_JAR_OR_TWO.completionAmount && !player.getAchievementRewardClaimed(Achievements.MIGHT_NEED_A_JAR_OR_TWO))
			mightNeedAJarOrTwo = "<col=6495ed>";
		else if (player.puropuroTravelledCounter >= Achievements.MIGHT_NEED_A_JAR_OR_TWO.completionAmount)
			mightNeedAJarOrTwo = "<col=00ff00>";

		String relaxYoureAlmostDone = "";
		if (player.chaosElementalKills.getKills() > 0 && player.chaosElementalKills.getKills() < Achievements.RELAX_YOURE_ALMOST_DONE.completionAmount)
			relaxYoureAlmostDone = "<col=ffff00>";
		else if (player.chaosElementalKills.getKills() >= Achievements.RELAX_YOURE_ALMOST_DONE.completionAmount && !player.getAchievementRewardClaimed(Achievements.RELAX_YOURE_ALMOST_DONE))
			relaxYoureAlmostDone = "<col=6495ed>";
		else if (player.chaosElementalKills.getKills() >= Achievements.RELAX_YOURE_ALMOST_DONE.completionAmount)
			relaxYoureAlmostDone = "<col=00ff00>";

		String growingTheGreens = "";
		if (player.harvestedIrit > 0 && player.harvestedIrit < Achievements.GROWING_THE_GREEN.completionAmount)
			growingTheGreens = "<col=ffff00>";
		else if (player.harvestedIrit >= Achievements.GROWING_THE_GREEN.completionAmount && !player.getAchievementRewardClaimed(Achievements.GROWING_THE_GREEN))
			growingTheGreens = "<col=6495ed>";
		else if (player.harvestedIrit >= Achievements.GROWING_THE_GREEN.completionAmount)
			growingTheGreens = "<col=00ff00>";

		String overnightMuscles = "";
		if (player.fighterTorsosEquipped > 0 && player.fighterTorsosEquipped < Achievements.OVERNIGHT_MUSCLES.completionAmount)
			overnightMuscles = "<col=ffff00>";
		else if (player.fighterTorsosEquipped >= Achievements.OVERNIGHT_MUSCLES.completionAmount && !player.getAchievementRewardClaimed(Achievements.OVERNIGHT_MUSCLES))
			overnightMuscles = "<col=6495ed>";
		else if (player.fighterTorsosEquipped >= Achievements.OVERNIGHT_MUSCLES.completionAmount)
			overnightMuscles = "<col=00ff00>";

		String seemsDangerous = "";
		if (player.wildernessCourseLaps > 0 && player.wildernessCourseLaps < Achievements.THIS_SEEMS_A_LITTLE_DANGEROUS.completionAmount)
			seemsDangerous = "<col=ffff00>";
		else if (player.wildernessCourseLaps >= Achievements.THIS_SEEMS_A_LITTLE_DANGEROUS.completionAmount && !player.getAchievementRewardClaimed(Achievements.THIS_SEEMS_A_LITTLE_DANGEROUS))
			seemsDangerous = "<col=6495ed>";
		else if (player.wildernessCourseLaps >= Achievements.THIS_SEEMS_A_LITTLE_DANGEROUS.completionAmount)
			seemsDangerous = "<col=00ff00>";

		String gettingHangOfThis = "";
		if (player.medClueCount > 0 && player.medClueCount < Achievements.THINK_IM_GETTING_THE_HANG_OF_THIS.completionAmount)
			gettingHangOfThis = "<col=ffff00>";
		else if (player.medClueCount >= Achievements.THINK_IM_GETTING_THE_HANG_OF_THIS.completionAmount && !player.getAchievementRewardClaimed(Achievements.THINK_IM_GETTING_THE_HANG_OF_THIS))
			gettingHangOfThis = "<col=6495ed>";
		else if (player.medClueCount >= Achievements.THINK_IM_GETTING_THE_HANG_OF_THIS.completionAmount)
			gettingHangOfThis = "<col=00ff00>";

		String protectiveHeadgear = "";
		if (player.slayerHelmsCrafted > 0 && player.slayerHelmsCrafted < Achievements.PROTECTIVE_HEADGEAR.completionAmount)
			protectiveHeadgear = "<col=ffff00>";
		else if (player.slayerHelmsCrafted >= Achievements.PROTECTIVE_HEADGEAR.completionAmount && !player.getAchievementRewardClaimed(Achievements.PROTECTIVE_HEADGEAR))
			protectiveHeadgear = "<col=6495ed>";
		else if (player.slayerHelmsCrafted >= Achievements.PROTECTIVE_HEADGEAR.completionAmount)
			protectiveHeadgear = "<col=00ff00>";

		String watchYourSurroundings = "";
		if (player.lavaDragonsKilled > 0 && player.lavaDragonsKilled < Achievements.WATCH_YOUR_SURROUNDINGS.completionAmount)
			watchYourSurroundings = "<col=ffff00>";
		else if (player.lavaDragonsKilled >= Achievements.WATCH_YOUR_SURROUNDINGS.completionAmount && !player.getAchievementRewardClaimed(Achievements.WATCH_YOUR_SURROUNDINGS))
			watchYourSurroundings = "<col=6495ed>";
		else if (player.lavaDragonsKilled >= Achievements.WATCH_YOUR_SURROUNDINGS.completionAmount)
			watchYourSurroundings = "<col=00ff00>";

		String divineSensesIII = "";
		if (player.lavaDragonBonesBuriedOrSacrificed > 0 && player.lavaDragonBonesBuriedOrSacrificed < Achievements.DIVINE_SENSES_III.completionAmount)
			divineSensesIII = "<col=ffff00>";
		else if (player.lavaDragonBonesBuriedOrSacrificed >= Achievements.DIVINE_SENSES_III.completionAmount && !player.getAchievementRewardClaimed(Achievements.DIVINE_SENSES_III))
			divineSensesIII = "<col=6495ed>";
		else if (player.lavaDragonBonesBuriedOrSacrificed >= Achievements.DIVINE_SENSES_III.completionAmount)
			divineSensesIII = "<col=00ff00>";

		String allThisFromASeed = "";
		if (player.crystalEquipmentWorn > 0 && player.crystalEquipmentWorn < Achievements.ALL_THIS_FROM_A_SEED.completionAmount)
			allThisFromASeed = "<col=ffff00>";
		else if (player.crystalEquipmentWorn >= Achievements.ALL_THIS_FROM_A_SEED.completionAmount && !player.getAchievementRewardClaimed(Achievements.ALL_THIS_FROM_A_SEED))
			allThisFromASeed = "<col=6495ed>";
		else if (player.crystalEquipmentWorn >= Achievements.ALL_THIS_FROM_A_SEED.completionAmount)
			allThisFromASeed = "<col=00ff00>";

		String prayersAnswered = "";
		if (player.chargesCastWearingGodStaff > 0 && player.chargesCastWearingGodStaff < Achievements.PRAYERS_ANSWERED.completionAmount)
			prayersAnswered = "<col=ffff00>";
		else if (player.chargesCastWearingGodStaff >= Achievements.PRAYERS_ANSWERED.completionAmount && !player.getAchievementRewardClaimed(Achievements.PRAYERS_ANSWERED))
			prayersAnswered = "<col=6495ed>";
		else if (player.chargesCastWearingGodStaff >= Achievements.PRAYERS_ANSWERED.completionAmount)
			prayersAnswered = "<col=00ff00>";

		String whatDoWeHaveHereII = "";
		if (player.larranChestsOpened > 0 && player.larranChestsOpened < Achievements.WHAT_DO_WE_HAVE_HERE_II.completionAmount)
			whatDoWeHaveHereII = "<col=ffff00>";
		else if (player.larranChestsOpened >= Achievements.WHAT_DO_WE_HAVE_HERE_II.completionAmount && !player.getAchievementRewardClaimed(Achievements.WHAT_DO_WE_HAVE_HERE_II))
			whatDoWeHaveHereII = "<col=6495ed>";
		else if (player.larranChestsOpened >= Achievements.WHAT_DO_WE_HAVE_HERE_II.completionAmount)
			whatDoWeHaveHereII = "<col=00ff00>";

		String theseShouldntTakeLongII = "";
		if (player.magicShortbowsFletched > 0 && player.magicShortbowsFletched < Achievements.THESE_SHOULDNT_TAKE_LONG_II.completionAmount)
			theseShouldntTakeLongII = "<col=ffff00>";
		else if (player.magicShortbowsFletched >= Achievements.THESE_SHOULDNT_TAKE_LONG_II.completionAmount && !player.getAchievementRewardClaimed(Achievements.THESE_SHOULDNT_TAKE_LONG_II))
			theseShouldntTakeLongII = "<col=6495ed>";
		else if (player.magicShortbowsFletched >= Achievements.THESE_SHOULDNT_TAKE_LONG_II.completionAmount)
			theseShouldntTakeLongII = "<col=00ff00>";

		String prayToRnjesus = "";
		if (player.revenantKillcount > 0 && player.revenantKillcount < Achievements.PRAY_TO_RNJESUS.completionAmount)
			prayToRnjesus = "<col=ffff00>";
		else if (player.revenantKillcount >= Achievements.PRAY_TO_RNJESUS.completionAmount && !player.getAchievementRewardClaimed(Achievements.PRAY_TO_RNJESUS))
			prayToRnjesus = "<col=6495ed>";
		else if (player.revenantKillcount >= Achievements.PRAY_TO_RNJESUS.completionAmount)
			prayToRnjesus = "<col=00ff00>";

		String cantKeepMyHandsToMyselfIII = "";
		if (player.herosPickpocketed > 0 && player.herosPickpocketed < Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_III.completionAmount)
			cantKeepMyHandsToMyselfIII = "<col=ffff00>";
		else if (player.herosPickpocketed >= Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_III.completionAmount && !player.getAchievementRewardClaimed(Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_III))
			cantKeepMyHandsToMyselfIII = "<col=6495ed>";
		else if (player.herosPickpocketed >= Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_III.completionAmount)
			cantKeepMyHandsToMyselfIII = "<col=00ff00>";

		String lumberjackIII = "";
		if (player.choppedMagic > 0 && player.choppedMagic < Achievements.LUMBERJACK_III.completionAmount)
			lumberjackIII = "<col=ffff00>";
		else if (player.choppedMagic >= Achievements.LUMBERJACK_III.completionAmount && !player.getAchievementRewardClaimed(Achievements.LUMBERJACK_III))
			lumberjackIII = "<col=6495ed>";
		else if (player.choppedMagic >= Achievements.LUMBERJACK_III.completionAmount)
			lumberjackIII = "<col=00ff00>";

		String pyromancerIII = "";
		if (player.magicLogsBurnt > 0 && player.magicLogsBurnt < Achievements.PYROMANCER_III.completionAmount)
			pyromancerIII = "<col=ffff00>";
		else if (player.magicLogsBurnt >= Achievements.PYROMANCER_III.completionAmount && !player.getAchievementRewardClaimed(Achievements.PYROMANCER_III))
			pyromancerIII = "<col=6495ed>";
		else if (player.magicLogsBurnt >= Achievements.PYROMANCER_III.completionAmount)
			pyromancerIII = "<col=00ff00>";

		String thisMightTakeAwhile = "";
		if (player.minedAdamant > 0 && player.minedAdamant < Achievements.THIS_MIGHT_TAKE_AWHILE.completionAmount)
			thisMightTakeAwhile = "<col=ffff00>";
		else if (player.minedAdamant >= Achievements.THIS_MIGHT_TAKE_AWHILE.completionAmount && !player.getAchievementRewardClaimed(Achievements.THIS_MIGHT_TAKE_AWHILE))
			thisMightTakeAwhile = "<col=6495ed>";
		else if (player.minedAdamant >= Achievements.THIS_MIGHT_TAKE_AWHILE.completionAmount)
			thisMightTakeAwhile = "<col=00ff00>";

		String adamantAboutIt = "";
		if (player.smeltedAdamantBars > 0 && player.smeltedAdamantBars < Achievements.ADAMANT_ABOUT_IT.completionAmount)
			adamantAboutIt = "<col=ffff00>";
		else if (player.smeltedAdamantBars >= Achievements.ADAMANT_ABOUT_IT.completionAmount && !player.getAchievementRewardClaimed(Achievements.ADAMANT_ABOUT_IT))
			adamantAboutIt = "<col=6495ed>";
		else if (player.smeltedAdamantBars >= Achievements.ADAMANT_ABOUT_IT.completionAmount)
			adamantAboutIt = "<col=00ff00>";

		String somethingIsFishyHereIII = "";
		if (player.anglerFishAndSharksCaughtCounter > 0 && player.anglerFishAndSharksCaughtCounter < Achievements.SOMETHING_IS_FISHY_HERE_III.completionAmount)
			somethingIsFishyHereIII = "<col=ffff00>";
		else if (player.anglerFishAndSharksCaughtCounter >= Achievements.SOMETHING_IS_FISHY_HERE_III.completionAmount && !player.getAchievementRewardClaimed(Achievements.SOMETHING_IS_FISHY_HERE_III))
			somethingIsFishyHereIII = "<col=6495ed>";
		else if (player.anglerFishAndSharksCaughtCounter >= Achievements.SOMETHING_IS_FISHY_HERE_III.completionAmount)
			somethingIsFishyHereIII = "<col=00ff00>";

		String justForTheHalibutIII = "";
		if (player.anglerFishAndSharksCookedCounter > 0 && player.anglerFishAndSharksCookedCounter < Achievements.JUST_FOR_THE_HALIBUT_III.completionAmount)
			justForTheHalibutIII = "<col=ffff00>";
		else if (player.anglerFishAndSharksCookedCounter >= Achievements.JUST_FOR_THE_HALIBUT_III.completionAmount && !player.getAchievementRewardClaimed(Achievements.JUST_FOR_THE_HALIBUT_III))
			justForTheHalibutIII = "<col=6495ed>";
		else if (player.anglerFishAndSharksCookedCounter >= Achievements.JUST_FOR_THE_HALIBUT_III.completionAmount)
			justForTheHalibutIII = "<col=00ff00>";

		String reapingTheBenefits = "";
		if (player.bloodsCrafted > 0 && player.bloodsCrafted < Achievements.REAPING_THE_BENEFITS.completionAmount)
			reapingTheBenefits = "<col=ffff00>";
		else if (player.bloodsCrafted >= Achievements.REAPING_THE_BENEFITS.completionAmount && !player.getAchievementRewardClaimed(Achievements.REAPING_THE_BENEFITS))
			reapingTheBenefits = "<col=6495ed>";
		else if (player.bloodsCrafted >= Achievements.REAPING_THE_BENEFITS.completionAmount)
			reapingTheBenefits = "<col=00ff00>";

		String notSoHard = "";
		if (player.hardClueCount > 0 && player.hardClueCount < Achievements.NOT_SO_HARD_AFTER_ALL.completionAmount)
			notSoHard = "<col=ffff00>";
		else if (player.hardClueCount >= Achievements.NOT_SO_HARD_AFTER_ALL.completionAmount && !player.getAchievementRewardClaimed(Achievements.NOT_SO_HARD_AFTER_ALL))
			notSoHard = "<col=6495ed>";
		else if (player.hardClueCount >= Achievements.NOT_SO_HARD_AFTER_ALL.completionAmount)
			notSoHard = "<col=00ff00>";

		String thatSmall = "";
		if (player.motherloadMineUpperUnlockedCounter > 0 && player.motherloadMineUpperUnlockedCounter < Achievements.WAS_I_THAT_SMALL_ALL_THIS_TIME.completionAmount)
			thatSmall = "<col=ffff00>";
		else if (player.motherloadMineUpperUnlockedCounter >= Achievements.WAS_I_THAT_SMALL_ALL_THIS_TIME.completionAmount && !player.getAchievementRewardClaimed(Achievements.WAS_I_THAT_SMALL_ALL_THIS_TIME))
			thatSmall = "<col=6495ed>";
		else if (player.motherloadMineUpperUnlockedCounter >= Achievements.WAS_I_THAT_SMALL_ALL_THIS_TIME.completionAmount)
			thatSmall = "<col=00ff00>";

		String travellersLuck = "";
		if (player.dragonSqShieldsSmithed > 0 && player.dragonSqShieldsSmithed < Achievements.TRAVELLERS_LUCK.completionAmount)
			travellersLuck = "<col=ffff00>";
		else if (player.dragonSqShieldsSmithed >= Achievements.TRAVELLERS_LUCK.completionAmount && !player.getAchievementRewardClaimed(Achievements.TRAVELLERS_LUCK))
			travellersLuck = "<col=6495ed>";
		else if (player.dragonSqShieldsSmithed >= Achievements.TRAVELLERS_LUCK.completionAmount)
			travellersLuck = "<col=00ff00>";

		String armedNotLoaded = "";
		if (player.blowpipesFletched > 0 && player.blowpipesFletched < Achievements.ARMED_NOT_LOADED.completionAmount)
			armedNotLoaded = "<col=ffff00>";
		else if (player.blowpipesFletched >= Achievements.ARMED_NOT_LOADED.completionAmount && !player.getAchievementRewardClaimed(Achievements.ARMED_NOT_LOADED))
			armedNotLoaded = "<col=6495ed>";
		else if (player.blowpipesFletched >= Achievements.ARMED_NOT_LOADED.completionAmount)
			armedNotLoaded = "<col=00ff00>";

		String probablyShouldntBeDoingThis = "";
		if (player.rougesCastleChests > 0 && player.rougesCastleChests < Achievements.I_PROBABLY_SHOULDNT_BE_DOING_THIS.completionAmount)
			probablyShouldntBeDoingThis = "<col=ffff00>";
		else if (player.rougesCastleChests >= Achievements.I_PROBABLY_SHOULDNT_BE_DOING_THIS.completionAmount && !player.getAchievementRewardClaimed(Achievements.I_PROBABLY_SHOULDNT_BE_DOING_THIS))
			probablyShouldntBeDoingThis = "<col=6495ed>";
		else if (player.rougesCastleChests >= Achievements.I_PROBABLY_SHOULDNT_BE_DOING_THIS.completionAmount)
			probablyShouldntBeDoingThis = "<col=00ff00>";

		String sacreligous = "";
		int totalGWDKills = (player.krilTsutsarothKills.getKills() + player.commanderZilyanaKills.getKills() + player.generalGraardorKills.getKills() + player.kreeArraKills.getKills());
		if (totalGWDKills > 0 && totalGWDKills < Achievements.SACRELIGIOUS.completionAmount)
			sacreligous = "<col=ffff00>";
		else if (totalGWDKills >= Achievements.SACRELIGIOUS.completionAmount && !player.getAchievementRewardClaimed(Achievements.SACRELIGIOUS))
			sacreligous = "<col=6495ed>";
		else if (totalGWDKills >= Achievements.SACRELIGIOUS.completionAmount)
			sacreligous = "<col=00ff00>";

		String krakOfDawn = "";
		if (player.krakenKills.getKills() > 0 && player.krakenKills.getKills() < Achievements.FROM_THE_KRAK_OF_DAWN.completionAmount)
			krakOfDawn = "<col=ffff00>";
		else if (player.krakenKills.getKills() >= Achievements.FROM_THE_KRAK_OF_DAWN.completionAmount && !player.getAchievementRewardClaimed(Achievements.FROM_THE_KRAK_OF_DAWN))
			krakOfDawn = "<col=6495ed>";
		else if (player.krakenKills.getKills() >= Achievements.FROM_THE_KRAK_OF_DAWN.completionAmount)
			krakOfDawn = "<col=00ff00>";

		String empowered = "";
		if (player.voidEquippedCounter > 0 && player.voidEquippedCounter < Achievements.EMPOWERED.completionAmount)
			empowered = "<col=ffff00>";
		else if (player.voidEquippedCounter >= Achievements.EMPOWERED.completionAmount && !player.getAchievementRewardClaimed(Achievements.EMPOWERED))
			empowered = "<col=6495ed>";
		else if (player.voidEquippedCounter >= Achievements.EMPOWERED.completionAmount)
			empowered = "<col=00ff00>";

		String gotAnyChange = "";
		if (player.slayerMasterUsedCounter > 0 && player.slayerMasterUsedCounter < Achievements.GOT_ANY_CHANGE.completionAmount)
			gotAnyChange = "<col=ffff00>";
		else if (player.slayerMasterUsedCounter >= Achievements.GOT_ANY_CHANGE.completionAmount && !player.getAchievementRewardClaimed(Achievements.GOT_ANY_CHANGE))
			gotAnyChange = "<col=6495ed>";
		else if (player.slayerMasterUsedCounter >= Achievements.GOT_ANY_CHANGE.completionAmount)
			gotAnyChange = "<col=00ff00>";

		String allMine = "";
		if (player.prospectorEquippedCounter > 0 && player.prospectorEquippedCounter < Achievements.ALL_MINE.completionAmount)
			allMine = "<col=ffff00>";
		else if (player.prospectorEquippedCounter >= Achievements.ALL_MINE.completionAmount && !player.getAchievementRewardClaimed(Achievements.ALL_MINE))
			allMine = "<col=6495ed>";
		else if (player.prospectorEquippedCounter >= Achievements.ALL_MINE.completionAmount)
			allMine = "<col=00ff00>";

		String lumberjackIV = "";
		if (player.choppedRedwood > 0 && player.choppedRedwood < Achievements.LUMBERJACK_IV.completionAmount)
			lumberjackIV = "<col=ffff00>";
		else if (player.choppedRedwood >= Achievements.LUMBERJACK_IV.completionAmount && !player.getAchievementRewardClaimed(Achievements.LUMBERJACK_IV))
			lumberjackIV = "<col=6495ed>";
		else if (player.choppedRedwood >= Achievements.LUMBERJACK_IV.completionAmount)
			lumberjackIV = "<col=00ff00>";

		String pyromancerIV = "";
		if (player.redwoodLogsBurnt > 0 && player.redwoodLogsBurnt < Achievements.PYROMANCER_IV.completionAmount)
			pyromancerIV = "<col=ffff00>";
		else if (player.redwoodLogsBurnt >= Achievements.PYROMANCER_IV.completionAmount && !player.getAchievementRewardClaimed(Achievements.PYROMANCER_IV))
			pyromancerIV = "<col=6495ed>";
		else if (player.redwoodLogsBurnt >= Achievements.PYROMANCER_IV.completionAmount)
			pyromancerIV = "<col=00ff00>";

		String endOfTheMine = "";
		if (player.minedAmethyst > 0 && player.minedAmethyst < Achievements.END_OF_THE_MINE.completionAmount)
			endOfTheMine = "<col=ffff00>";
		else if (player.minedAmethyst >= Achievements.END_OF_THE_MINE.completionAmount && !player.getAchievementRewardClaimed(Achievements.END_OF_THE_MINE))
			endOfTheMine = "<col=6495ed>";
		else if (player.minedAmethyst >= Achievements.END_OF_THE_MINE.completionAmount)
			endOfTheMine = "<col=00ff00>";

		String thisWillTeachThem = "";
		if (player.dragonfireShieldsSmithed > 0 && player.dragonfireShieldsSmithed < Achievements.THIS_WILL_TEACH_THEM.completionAmount)
			thisWillTeachThem = "<col=ffff00>";
		else if (player.dragonfireShieldsSmithed >= Achievements.THIS_WILL_TEACH_THEM.completionAmount && !player.getAchievementRewardClaimed(Achievements.THIS_WILL_TEACH_THEM))
			thisWillTeachThem = "<col=6495ed>";
		else if (player.dragonfireShieldsSmithed >= Achievements.THIS_WILL_TEACH_THEM.completionAmount)
			thisWillTeachThem = "<col=00ff00>";

		String somethingIsFishyHereIV = "";
		if (player.darkCrabsFished > 0 && player.darkCrabsFished < Achievements.SOMETHING_IS_FISHY_HERE_IV.completionAmount)
			somethingIsFishyHereIV = "<col=ffff00>";
		else if (player.darkCrabsFished >= Achievements.SOMETHING_IS_FISHY_HERE_IV.completionAmount && !player.getAchievementRewardClaimed(Achievements.SOMETHING_IS_FISHY_HERE_IV))
			somethingIsFishyHereIV = "<col=6495ed>";
		else if (player.darkCrabsFished >= Achievements.SOMETHING_IS_FISHY_HERE_IV.completionAmount)
			somethingIsFishyHereIV = "<col=00ff00>";

		String justForTheHalibutIV = "";
		if (player.darkCrabsCooked > 0 && player.darkCrabsCooked < Achievements.JUST_FOR_THE_HALIBUT_IV.completionAmount)
			justForTheHalibutIV = "<col=ffff00>";
		else if (player.darkCrabsCooked >= Achievements.JUST_FOR_THE_HALIBUT_IV.completionAmount && !player.getAchievementRewardClaimed(Achievements.JUST_FOR_THE_HALIBUT_IV))
			justForTheHalibutIV = "<col=6495ed>";
		else if (player.darkCrabsCooked >= Achievements.JUST_FOR_THE_HALIBUT_IV.completionAmount)
			justForTheHalibutIV = "<col=00ff00>";

		String smallButDeadly = "";
		if (player.dragonDartsFletched > 0 && player.dragonDartsFletched < Achievements.SMALL_BUT_DEADLY.completionAmount)
			smallButDeadly = "<col=ffff00>";
		else if (player.dragonDartsFletched >= Achievements.SMALL_BUT_DEADLY.completionAmount && !player.getAchievementRewardClaimed(Achievements.SMALL_BUT_DEADLY))
			smallButDeadly = "<col=6495ed>";
		else if (player.dragonDartsFletched >= Achievements.SMALL_BUT_DEADLY.completionAmount)
			smallButDeadly = "<col=00ff00>";

		String colourfulTips = "";
		if (player.amethystArrowTipsCrafted > 0 && player.amethystArrowTipsCrafted < Achievements.COLOURFUL_TIPS.completionAmount)
			colourfulTips = "<col=ffff00>";
		else if (player.amethystArrowTipsCrafted >= Achievements.COLOURFUL_TIPS.completionAmount && !player.getAchievementRewardClaimed(Achievements.COLOURFUL_TIPS))
			colourfulTips = "<col=6495ed>";
		else if (player.amethystArrowTipsCrafted >= Achievements.COLOURFUL_TIPS.completionAmount)
			colourfulTips = "<col=00ff00>";

		String cantKeepMyHandsToMyselfIV = "";
		if (player.tzhaarPickpocketed > 0 && player.tzhaarPickpocketed < Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_IV.completionAmount)
			cantKeepMyHandsToMyselfIV = "<col=ffff00>";
		else if (player.tzhaarPickpocketed >= Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_IV.completionAmount && !player.getAchievementRewardClaimed(Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_IV))
			cantKeepMyHandsToMyselfIV = "<col=6495ed>";
		else if (player.tzhaarPickpocketed >= Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_IV.completionAmount)
			cantKeepMyHandsToMyselfIV = "<col=00ff00>";

		String slayersGettingWild = "";
		if (player.wildernessSlayerTasksCompleted > 0 && player.wildernessSlayerTasksCompleted < Achievements.SLAYERS_GETTING_WILD.completionAmount)
			slayersGettingWild = "<col=ffff00>";
		else if (player.wildernessSlayerTasksCompleted >= Achievements.SLAYERS_GETTING_WILD.completionAmount && !player.getAchievementRewardClaimed(Achievements.SLAYERS_GETTING_WILD))
			slayersGettingWild = "<col=6495ed>";
		else if (player.wildernessSlayerTasksCompleted >= Achievements.SLAYERS_GETTING_WILD.completionAmount)
			slayersGettingWild = "<col=00ff00>";

		String chamberOfSecrets = "";
		if (player.chambersofXericKills.getKills() > 0 && player.chambersofXericKills.getKills() < Achievements.CHAMBER_OF_SECRETS.completionAmount)
			chamberOfSecrets = "<col=ffff00>";
		else if (player.chambersofXericKills.getKills() >= Achievements.CHAMBER_OF_SECRETS.completionAmount && !player.getAchievementRewardClaimed(Achievements.CHAMBER_OF_SECRETS))
			chamberOfSecrets = "<col=6495ed>";
		else if (player.chambersofXericKills.getKills() >= Achievements.CHAMBER_OF_SECRETS.completionAmount)
			chamberOfSecrets = "<col=00ff00>";

		String pullTheVorkOut = "";
		if (player.vorkathKills.getKills() > 0 && player.vorkathKills.getKills() < Achievements.PULL_THE_VORK_OUT.completionAmount)
			pullTheVorkOut = "<col=ffff00>";
		else if (player.vorkathKills.getKills() >= Achievements.PULL_THE_VORK_OUT.completionAmount && !player.getAchievementRewardClaimed(Achievements.PULL_THE_VORK_OUT))
			pullTheVorkOut = "<col=6495ed>";
		else if (player.vorkathKills.getKills() >= Achievements.PULL_THE_VORK_OUT.completionAmount)
			pullTheVorkOut = "<col=00ff00>";

		String facingYourFears = "";
		if (player.galvekKills.getKills() > 0 && player.galvekKills.getKills() < Achievements.FACING_YOUR_FEARS.completionAmount)
			facingYourFears = "<col=ffff00>";
		else if (player.galvekKills.getKills() >= Achievements.FACING_YOUR_FEARS.completionAmount && !player.getAchievementRewardClaimed(Achievements.FACING_YOUR_FEARS))
			facingYourFears = "<col=6495ed>";
		else if (player.galvekKills.getKills() >= Achievements.FACING_YOUR_FEARS.completionAmount)
			facingYourFears = "<col=00ff00>";

		String itHasHowManyHeads = "";
		if (player.alchemicalHydraKills.getKills() > 0 && player.alchemicalHydraKills.getKills() < Achievements.IT_HAS_HOW_MANY_HEADS.completionAmount)
			itHasHowManyHeads = "<col=ffff00>";
		else if (player.alchemicalHydraKills.getKills() >= Achievements.IT_HAS_HOW_MANY_HEADS.completionAmount && !player.getAchievementRewardClaimed(Achievements.IT_HAS_HOW_MANY_HEADS))
			itHasHowManyHeads = "<col=6495ed>";
		else if (player.alchemicalHydraKills.getKills() >= Achievements.IT_HAS_HOW_MANY_HEADS.completionAmount)
			itHasHowManyHeads = "<col=00ff00>";

		String masteredIt = "";
		if (player.masterClueCount > 0 && player.masterClueCount < Achievements.IVE_MASTERED_IT.completionAmount)
			masteredIt = "<col=ffff00>";
		else if (player.masterClueCount >= Achievements.IVE_MASTERED_IT.completionAmount && !player.getAchievementRewardClaimed(Achievements.IVE_MASTERED_IT))
			masteredIt = "<col=6495ed>";
		else if (player.masterClueCount >= Achievements.IVE_MASTERED_IT.completionAmount)
			masteredIt = "<col=00ff00>";

		String cutTheHeadOffTheSnake = "";
		if (player.zulrahKills.getKills() > 0 && player.zulrahKills.getKills() < Achievements.CUT_THE_HEAD_OFF_THE_SNAKE.completionAmount)
			cutTheHeadOffTheSnake = "<col=ffff00>";
		else if (player.zulrahKills.getKills() >= Achievements.CUT_THE_HEAD_OFF_THE_SNAKE.completionAmount && !player.getAchievementRewardClaimed(Achievements.CUT_THE_HEAD_OFF_THE_SNAKE))
			cutTheHeadOffTheSnake = "<col=6495ed>";
		else if (player.zulrahKills.getKills() >= Achievements.CUT_THE_HEAD_OFF_THE_SNAKE.completionAmount)
			cutTheHeadOffTheSnake = "<col=00ff00>";

		String feelMyWrath = "";
		if (player.wrathsCrafted > 0 && player.wrathsCrafted < Achievements.FEEL_MY_WRATH.completionAmount)
			feelMyWrath = "<col=ffff00>";
		else if (player.wrathsCrafted >= Achievements.FEEL_MY_WRATH.completionAmount && !player.getAchievementRewardClaimed(Achievements.FEEL_MY_WRATH))
			feelMyWrath = "<col=6495ed>";
		else if (player.wrathsCrafted >= Achievements.FEEL_MY_WRATH.completionAmount)
			feelMyWrath = "<col=00ff00>";

		String thankfullyNotAllAtOnce = "";
		if (player.dragonsAnimated > 0 && player.dragonsAnimated < Achievements.THANKFULLY_NOT_ALL_AT_ONCE.completionAmount)
			thankfullyNotAllAtOnce = "<col=ffff00>";
		else if (player.dragonsAnimated >= Achievements.THANKFULLY_NOT_ALL_AT_ONCE.completionAmount && !player.getAchievementRewardClaimed(Achievements.THANKFULLY_NOT_ALL_AT_ONCE))
			thankfullyNotAllAtOnce = "<col=6495ed>";
		else if (player.dragonsAnimated >= Achievements.THANKFULLY_NOT_ALL_AT_ONCE.completionAmount)
			thankfullyNotAllAtOnce = "<col=00ff00>";

		String kingOfTheUnderground = "";
		if (player.demonicThronesCraftedAndSatOn > 0 && player.demonicThronesCraftedAndSatOn < Achievements.KING_OF_THE_UNDERGROUND.completionAmount)
			kingOfTheUnderground = "<col=ffff00>";
		else if (player.demonicThronesCraftedAndSatOn >= Achievements.KING_OF_THE_UNDERGROUND.completionAmount && !player.getAchievementRewardClaimed(Achievements.KING_OF_THE_UNDERGROUND))
			kingOfTheUnderground = "<col=6495ed>";
		else if (player.demonicThronesCraftedAndSatOn >= Achievements.KING_OF_THE_UNDERGROUND.completionAmount)
			kingOfTheUnderground = "<col=00ff00>";

		String noNetNeeded = "";
		if (player.dragonImplingOrLuckyImplingCaughtBarehandedCounter > 0 && player.dragonImplingOrLuckyImplingCaughtBarehandedCounter < Achievements.NO_NET_NEEDED.completionAmount)
			noNetNeeded = "<col=ffff00>";
		else if (player.dragonImplingOrLuckyImplingCaughtBarehandedCounter >= Achievements.NO_NET_NEEDED.completionAmount && !player.getAchievementRewardClaimed(Achievements.NO_NET_NEEDED))
			noNetNeeded = "<col=6495ed>";
		else if (player.dragonImplingOrLuckyImplingCaughtBarehandedCounter >= Achievements.NO_NET_NEEDED.completionAmount)
			noNetNeeded = "<col=00ff00>";

		String thisWasTorture = "";
		if (player.zenyteAmuletsCraftedAndEnchanted > 0 && player.zenyteAmuletsCraftedAndEnchanted < Achievements.THIS_WAS_TORTURE.completionAmount)
			thisWasTorture = "<col=ffff00>";
		else if (player.zenyteAmuletsCraftedAndEnchanted >= Achievements.THIS_WAS_TORTURE.completionAmount && !player.getAchievementRewardClaimed(Achievements.THIS_WAS_TORTURE))
			thisWasTorture = "<col=6495ed>";
		else if (player.zenyteAmuletsCraftedAndEnchanted >= Achievements.THIS_WAS_TORTURE.completionAmount)
			thisWasTorture = "<col=00ff00>";

		String divinityInTheMix = "";
		if (player.divineSuperCombatPotionsMixed > 0 && player.divineSuperCombatPotionsMixed < Achievements.LITTLE_BIT_OF_DIVINITY_IN_THE_MIX.completionAmount)
			divinityInTheMix = "<col=ffff00>";
		else if (player.divineSuperCombatPotionsMixed >= Achievements.LITTLE_BIT_OF_DIVINITY_IN_THE_MIX.completionAmount && !player.getAchievementRewardClaimed(Achievements.LITTLE_BIT_OF_DIVINITY_IN_THE_MIX))
			divinityInTheMix = "<col=6495ed>";
		else if (player.divineSuperCombatPotionsMixed >= Achievements.LITTLE_BIT_OF_DIVINITY_IN_THE_MIX.completionAmount)
			divinityInTheMix = "<col=00ff00>";

		String alldougne = "";
		if (player.ardougneRooftopLaps > 0 && player.ardougneRooftopLaps < Achievements.ALLDOUGNE.completionAmount)
			alldougne = "<col=ffff00>";
		else if (player.ardougneRooftopLaps >= Achievements.ALLDOUGNE.completionAmount && !player.getAchievementRewardClaimed(Achievements.ALLDOUGNE))
			alldougne = "<col=6495ed>";
		else if (player.ardougneRooftopLaps >= Achievements.ALLDOUGNE.completionAmount)
			alldougne = "<col=00ff00>";

		String wooxWouldBeProud = "";
		if (player.zukKills.getKills() > 0 && player.zukKills.getKills() < Achievements.WOOX_WOULD_BE_PROUD.completionAmount)
			wooxWouldBeProud = "<col=ffff00>";
		else if (player.zukKills.getKills() >= Achievements.WOOX_WOULD_BE_PROUD.completionAmount && !player.getAchievementRewardClaimed(Achievements.WOOX_WOULD_BE_PROUD))
			wooxWouldBeProud = "<col=6495ed>";
		else if (player.zukKills.getKills() >= Achievements.WOOX_WOULD_BE_PROUD.completionAmount)
			wooxWouldBeProud = "<col=00ff00>";

		String theresLayersToIt = "";
		if (player.infernalAxesMade > 0 && player.infernalAxesMade < Achievements.THERES_LAYERS_TO_IT.completionAmount)
			theresLayersToIt = "<col=ffff00>";
		else if (player.infernalAxesMade >= Achievements.THERES_LAYERS_TO_IT.completionAmount && !player.getAchievementRewardClaimed(Achievements.THERES_LAYERS_TO_IT))
			theresLayersToIt = "<col=6495ed>";
		else if (player.infernalAxesMade >= Achievements.THERES_LAYERS_TO_IT.completionAmount)
			theresLayersToIt = "<col=00ff00>";

		String tisTheSpirit = "";
		if (player.POHSpiritTreeTeleportsUed > 0 && player.POHSpiritTreeTeleportsUed < Achievements.TIS_THE_SPIRIT.completionAmount)
			tisTheSpirit = "<col=ffff00>";
		else if (player.POHSpiritTreeTeleportsUed >= Achievements.TIS_THE_SPIRIT.completionAmount && !player.getAchievementRewardClaimed(Achievements.TIS_THE_SPIRIT))
			tisTheSpirit = "<col=6495ed>";
		else if (player.POHSpiritTreeTeleportsUed >= Achievements.TIS_THE_SPIRIT.completionAmount)
			tisTheSpirit = "<col=00ff00>";


		if (player.currentAchievement.getAchievementType() == Achievements.AchievementTypes.EASY) {
			player.getPacketSender().sendString(849, 82, crabKiller + Achievements.ARE_YOU_SHORE_ABOUT_THIS.getAchievementName());
			player.getPacketSender().sendString(849, 83, mindGames + Achievements.MIND_GAMES.getAchievementName());
			player.getPacketSender().sendString(849, 84, divineSensesI + Achievements.DIVINE_SENSES_I.getAchievementName());
			player.getPacketSender().sendString(849, 85, powerWithin + Achievements.THE_POWER_WITHIN.getAchievementName());
			player.getPacketSender().sendString(849, 86, leatherCrafted + Achievements.SKIN_IS_THE_GAME.getAchievementName());
			player.getPacketSender().sendString(849, 87, ironBarsSmelted + Achievements.IRON_INTELLECT.getAchievementName());
			player.getPacketSender().sendString(849, 88, fishFlyFished + Achievements.SOMETHING_IS_FISHY_HERE_I.getAchievementName());
			player.getPacketSender().sendString(849, 89, fishCooked + Achievements.JUST_FOR_THE_HALIBUT_I.getAchievementName());
			player.getPacketSender().sendString(849, 90, oaksCut + Achievements.LUMBERJACK_I.getAchievementName());
			player.getPacketSender().sendString(849, 91, oaksBurned + Achievements.PYROMANCER_I.getAchievementName());
			player.getPacketSender().sendString(849, 92, shortbowsFletched + Achievements.THESE_SHOULDNT_TAKE_LONG_I.getAchievementName());
			player.getPacketSender().sendString(849, 93, agilityLaps + Achievements.ANOTHER_ONE_BITES_THE_DUST.getAchievementName());
			player.getPacketSender().sendString(849, 94, cantKeepMyHandsToMyselfI + Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_I.getAchievementName());
			player.getPacketSender().sendString(849, 95, slayerTasks + Achievements.RAISING_THE_BAR.getAchievementName());
			player.getPacketSender().sendString(849, 96, rakeHerbPatch + Achievements.THE_GRASS_IS_ALWAYS_GREENER.getAchievementName());
			player.getPacketSender().sendString(849, 97, firstTimeHomeOwner + Achievements.FIRST_TIME_HOME_OWNER.getAchievementName());
			player.getPacketSender().sendString(849, 98, huntIsOn1 + Achievements.THE_HUNT_IS_ON_I.getAchievementName());
			player.getPacketSender().sendString(849, 99, dontBeGrimy + Achievements.DONT_BE_SO_GRIMY.getAchievementName());
			player.getPacketSender().sendString(849, 100, thankGodForThat + Achievements.THANK_GOD_FOR_THAT.getAchievementName());
			player.getPacketSender().sendString(849, 101, justALittleBoost + Achievements.JUST_A_LITTLE_BOOST.getAchievementName());
			player.getPacketSender().sendString(849, 102, nexworking + Achievements.NEXWORKING.getAchievementName());
			player.getPacketSender().sendString(849, 103, ohCul + Achievements.OH_CUL.getAchievementName());
			player.getPacketSender().sendString(849, 104, openYourMine + Achievements.OPEN_YOUR_MINE.getAchievementName());
			player.getPacketSender().sendString(849, 105, quitePuzzled + Achievements.QUITE_PUZZLED.getAchievementName());
			player.getPacketSender().sendString(849, 106, thirdPartyCandidate + Achievements.THIRD_PART_CANDIDATE.getAchievementName());
			player.getPacketSender().sendString(849, 107, feelingLikeANewMan + Achievements.FEELING_LIKE_A_NEW_MAN.getAchievementName());
		} else if (player.currentAchievement.getAchievementType() == Achievements.AchievementTypes.MEDIUM) {
                /*
          Medium Achievements

                 */

			player.getPacketSender().sendString(849, 82, ibantBelieveThis + Achievements.IBANT_BELIEVE_THIS.getAchievementName());
			player.getPacketSender().sendString(849, 83, itsMagicYouKnow + Achievements.ITS_MAGIC_YOU_KNOW.getAchievementName());
			player.getPacketSender().sendString(849, 84, passedTheBarr + Achievements.PASSED_THE_BARR.getAchievementName());
			player.getPacketSender().sendString(849, 85, divineSensesII + Achievements.DIVINE_SENSES_II.getAchievementName());
			player.getPacketSender().sendString(849, 86, astralWorld + Achievements.ASTRALWORLD.getAchievementName());
			player.getPacketSender().sendString(849, 87, howManyRingsDoYouNeed + Achievements.HOW_MANY_RINGS_DO_YOU_NEED.getAchievementName());
			player.getPacketSender().sendString(849, 88, didYouMithOne + Achievements.DID_YOU_MITH_ONE.getAchievementName());
			player.getPacketSender().sendString(849, 89, somethingIsFishyHereII + Achievements.SOMETHING_IS_FISHY_HERE_II.getAchievementName());
			player.getPacketSender().sendString(849, 90, justForTheHalibutII + Achievements.JUST_FOR_THE_HALIBUT_II.getAchievementName());
			player.getPacketSender().sendString(849, 91, pyromancerII + Achievements.PYROMANCER_II.getAchievementName());
			player.getPacketSender().sendString(849, 92, lumberjackII + Achievements.LUMBERJACK_II.getAchievementName());
			player.getPacketSender().sendString(849, 93, nowThatIsUnnecessary + Achievements.NOW_THAT_IS_JUST_UNNCECESSARY.getAchievementName());
			player.getPacketSender().sendString(849, 94, limbsToLimbs + Achievements.LIMBS_TO_LIMBS.getAchievementName());
			player.getPacketSender().sendString(849, 95, cantKeepMyHandsToMyselfII + Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_II.getAchievementName());
			player.getPacketSender().sendString(849, 96, dontMindIfIDo + Achievements.DONT_MIND_IF_I_DO.getAchievementName());
			player.getPacketSender().sendString(849, 97, inBloom + Achievements.IN_BLOOM.getAchievementName());
			player.getPacketSender().sendString(849, 98, isThisEvenSafe + Achievements.IS_THIS_EVEN_SAFE.getAchievementName());
			player.getPacketSender().sendString(849, 99, mightNeedTheseLater + Achievements.MIGHT_NEED_THESE_LATER.getAchievementName());
			player.getPacketSender().sendString(849, 100, goneInTheBlinkOfAnEye + Achievements.GONE_IN_THE_BLINK_OF_AN_EYE.getAchievementName());
			player.getPacketSender().sendString(849, 101, whatDoWeHaveHereI + Achievements.WHAT_DO_WE_HAVE_HERE_I.getAchievementName());
			player.getPacketSender().sendString(849, 102, mightNeedAJarOrTwo + Achievements.MIGHT_NEED_A_JAR_OR_TWO.getAchievementName());
			player.getPacketSender().sendString(849, 103, relaxYoureAlmostDone + Achievements.RELAX_YOURE_ALMOST_DONE.getAchievementName());
			player.getPacketSender().sendString(849, 104, growingTheGreens + Achievements.GROWING_THE_GREEN.getAchievementName());
			player.getPacketSender().sendString(849, 105, overnightMuscles + Achievements.OVERNIGHT_MUSCLES.getAchievementName());
			player.getPacketSender().sendString(849, 106, seemsDangerous + Achievements.THIS_SEEMS_A_LITTLE_DANGEROUS.getAchievementName());
			player.getPacketSender().sendString(849, 107, gettingHangOfThis + Achievements.THINK_IM_GETTING_THE_HANG_OF_THIS.getAchievementName());
		} else if (player.currentAchievement.getAchievementType() == Achievements.AchievementTypes.HARD) {

          /*
          Hard Achievements

           */

			player.getPacketSender().sendString(849, 82, protectiveHeadgear + Achievements.PROTECTIVE_HEADGEAR.getAchievementName());
			player.getPacketSender().sendString(849, 83, watchYourSurroundings + Achievements.WATCH_YOUR_SURROUNDINGS.getAchievementName());
			player.getPacketSender().sendString(849, 84, divineSensesIII + Achievements.DIVINE_SENSES_III.getAchievementName());
			player.getPacketSender().sendString(849, 85, allThisFromASeed + Achievements.ALL_THIS_FROM_A_SEED.getAchievementName());
			player.getPacketSender().sendString(849, 86, prayersAnswered + Achievements.PRAYERS_ANSWERED.getAchievementName());
			player.getPacketSender().sendString(849, 87, whatDoWeHaveHereII + Achievements.WHAT_DO_WE_HAVE_HERE_II.getAchievementName());
			player.getPacketSender().sendString(849, 88, theseShouldntTakeLongII + Achievements.THESE_SHOULDNT_TAKE_LONG_II.getAchievementName());
			player.getPacketSender().sendString(849, 89, prayToRnjesus + Achievements.PRAY_TO_RNJESUS.getAchievementName());
			player.getPacketSender().sendString(849, 90, cantKeepMyHandsToMyselfIII + Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_III.getAchievementName());
			player.getPacketSender().sendString(849, 91, lumberjackIII + Achievements.LUMBERJACK_III.getAchievementName());
			player.getPacketSender().sendString(849, 92, pyromancerIII + Achievements.PYROMANCER_III.getAchievementName());
			player.getPacketSender().sendString(849, 93, thisMightTakeAwhile + Achievements.THIS_MIGHT_TAKE_AWHILE.getAchievementName());
			player.getPacketSender().sendString(849, 94, adamantAboutIt + Achievements.ADAMANT_ABOUT_IT.getAchievementName());
			player.getPacketSender().sendString(849, 95, somethingIsFishyHereIII + Achievements.SOMETHING_IS_FISHY_HERE_III.getAchievementName());
			player.getPacketSender().sendString(849, 96, justForTheHalibutIII + Achievements.JUST_FOR_THE_HALIBUT_III.getAchievementName());
			player.getPacketSender().sendString(849, 97, reapingTheBenefits + Achievements.REAPING_THE_BENEFITS.getAchievementName());
			player.getPacketSender().sendString(849, 98, notSoHard + Achievements.NOT_SO_HARD_AFTER_ALL.getAchievementName());
			player.getPacketSender().sendString(849, 99, thatSmall + Achievements.WAS_I_THAT_SMALL_ALL_THIS_TIME.getAchievementName());
			player.getPacketSender().sendString(849, 100, travellersLuck + Achievements.TRAVELLERS_LUCK.getAchievementName());
			player.getPacketSender().sendString(849, 101, armedNotLoaded + Achievements.ARMED_NOT_LOADED.getAchievementName());
			player.getPacketSender().sendString(849, 102, probablyShouldntBeDoingThis + Achievements.I_PROBABLY_SHOULDNT_BE_DOING_THIS.getAchievementName());
			player.getPacketSender().sendString(849, 103, sacreligous + Achievements.SACRELIGIOUS.getAchievementName());
			player.getPacketSender().sendString(849, 104, krakOfDawn + Achievements.FROM_THE_KRAK_OF_DAWN.getAchievementName());
			player.getPacketSender().sendString(849, 105, empowered + Achievements.EMPOWERED.getAchievementName());
			player.getPacketSender().sendString(849, 106, gotAnyChange + Achievements.GOT_ANY_CHANGE.getAchievementName());
			player.getPacketSender().sendString(849, 107, allMine + Achievements.ALL_MINE.getAchievementName());
		} else if (player.currentAchievement.getAchievementType() == Achievements.AchievementTypes.ELITE) {


          /*
          Elite achievements

           */

			player.getPacketSender().sendString(849, 82, lumberjackIV + Achievements.LUMBERJACK_IV.getAchievementName());
			player.getPacketSender().sendString(849, 83, pyromancerIV + Achievements.PYROMANCER_IV.getAchievementName());
			player.getPacketSender().sendString(849, 84, endOfTheMine + Achievements.END_OF_THE_MINE.getAchievementName());
			player.getPacketSender().sendString(849, 85, thisWillTeachThem + Achievements.THIS_WILL_TEACH_THEM.getAchievementName());
			player.getPacketSender().sendString(849, 86, somethingIsFishyHereIV + Achievements.SOMETHING_IS_FISHY_HERE_IV.getAchievementName());
			player.getPacketSender().sendString(849, 87, justForTheHalibutIV + Achievements.JUST_FOR_THE_HALIBUT_IV.getAchievementName());
			player.getPacketSender().sendString(849, 88, smallButDeadly + Achievements.SMALL_BUT_DEADLY.getAchievementName());
			player.getPacketSender().sendString(849, 89, colourfulTips + Achievements.COLOURFUL_TIPS.getAchievementName());
			player.getPacketSender().sendString(849, 90, cantKeepMyHandsToMyselfIV + Achievements.CANT_KEEP_MY_HANDS_TO_MYSELF_IV.getAchievementName());
			player.getPacketSender().sendString(849, 91, slayersGettingWild + Achievements.SLAYERS_GETTING_WILD.getAchievementName());
			player.getPacketSender().sendString(849, 92, chamberOfSecrets + Achievements.CHAMBER_OF_SECRETS.getAchievementName());
			player.getPacketSender().sendString(849, 93, pullTheVorkOut + Achievements.PULL_THE_VORK_OUT.getAchievementName());
			player.getPacketSender().sendString(849, 94, facingYourFears + Achievements.FACING_YOUR_FEARS.getAchievementName());
			player.getPacketSender().sendString(849, 95, itHasHowManyHeads + Achievements.IT_HAS_HOW_MANY_HEADS.getAchievementName());
			player.getPacketSender().sendString(849, 96, masteredIt + Achievements.IVE_MASTERED_IT.getAchievementName());
			player.getPacketSender().sendString(849, 97, cutTheHeadOffTheSnake + Achievements.CUT_THE_HEAD_OFF_THE_SNAKE.getAchievementName());
			player.getPacketSender().sendString(849, 98, feelMyWrath + Achievements.FEEL_MY_WRATH.getAchievementName());
			player.getPacketSender().sendString(849, 99, thankfullyNotAllAtOnce + Achievements.THANKFULLY_NOT_ALL_AT_ONCE.getAchievementName());
			player.getPacketSender().sendString(849, 100, kingOfTheUnderground + Achievements.KING_OF_THE_UNDERGROUND.getAchievementName());
			player.getPacketSender().sendString(849, 101, noNetNeeded + Achievements.NO_NET_NEEDED.getAchievementName());
			player.getPacketSender().sendString(849, 102, thisWasTorture + Achievements.THIS_WAS_TORTURE.getAchievementName());
			player.getPacketSender().sendString(849, 103, divinityInTheMix + Achievements.LITTLE_BIT_OF_DIVINITY_IN_THE_MIX.getAchievementName());
			player.getPacketSender().sendString(849, 104, alldougne + Achievements.ALLDOUGNE.getAchievementName());
			player.getPacketSender().sendString(849, 105, wooxWouldBeProud + Achievements.WOOX_WOULD_BE_PROUD.getAchievementName());
			player.getPacketSender().sendString(849, 106, theresLayersToIt + Achievements.THERES_LAYERS_TO_IT.getAchievementName());
			player.getPacketSender().sendString(849, 107, tisTheSpirit + Achievements.TIS_THE_SPIRIT.getAchievementName());
		}

		player.getPacketSender().sendString(player.currentAchievement.getInterfaceID(), player.currentAchievement.getComponentID(), "<col=ffffff>" + currentAchievement.getAchievementName());

	}


}
