package io.ruin.services.http.hiscores;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.ruin.model.entity.player.Player;
import io.ruin.model.stat.StatType;
import io.ruin.process.event.EventWorker;
import io.ruin.services.http.HttpRequestBuilder;
import io.ruin.services.http.HttpRequestEvent;
import io.ruin.services.http.HttpRequestMethod;

import java.util.stream.IntStream;

public class Hiscores {

	public static void postHiscores(Player player) {
		if (player.isAdmin() || player.getDifficulty() == null)
			return;
		String username = player.getName();
		int difficultyMode = player.getDifficulty().ordinal();
		int gameMode = switch (player.getGameMode()) {
			case IRONMAN -> 1;
			case ULTIMATE_IRONMAN -> 5;
			case HARDCORE_IRONMAN -> 3;
			case GROUP_IRONMAN -> 2;
			case HARDCORE_GROUP_IRONMAN -> 4;
			default -> 0;
		};
		int donatorRank = switch (player.getSecondaryGroup()) {
			case NONE -> 0;
			case DONATOR -> 1;
			case SUPER_DONATOR -> 2;
			case ELITE_DONATOR -> 3;
			case NOBLE_DONATOR -> 4;
			case GOLD_DONATOR -> 5;
			case PLATINUM_DONATOR -> 6;
			case LEGENDARY_DONATOR -> 7;
			case SUPREME_DONATOR -> 8;
			default -> 0;
		};

		JsonArray skillsArray = new JsonArray();
		JsonArray bossKillsArray = new JsonArray();

		// Adding the entry with skillId of -1 (total level and total experience)
		skillsArray.add(createTotalSkillJson(player));

		IntStream.range(0, StatType.VALUES.length)
			.mapToObj(skillId -> createSkillJson(skillId, player))
			.forEach(skillsArray::add);

		bossKillsArray.add(createBossKillJson(1, player, player.abyssalSireKills.getKills()));
		bossKillsArray.add(createBossKillJson(2, player, player.alchemicalHydraKills.getKills()));
		bossKillsArray.add(createBossKillJson(3, player, player.argentavisKills.getKills()));
		bossKillsArray.add(createBossKillJson(4, player, player.barrowsChestsLooted.getKills()));
		bossKillsArray.add(createBossKillJson(5, player, player.callistoKills.getKills()));
		bossKillsArray.add(createBossKillJson(6, player, player.cerberusKills.getKills()));
		bossKillsArray.add(createBossKillJson(7, player, player.chambersofXericKills.getKills()));
		bossKillsArray.add(createBossKillJson(8, player, player.olmOnlyKills.getKills()));
		bossKillsArray.add(createBossKillJson(9, player, player.chaosElementalKills.getKills()));
		bossKillsArray.add(createBossKillJson(10, player, player.chaosFanaticKills.getKills()));
		int allClues = player.beginnerClueCount + player.easyClueCount + player.medClueCount + player.hardClueCount + player.eliteClueCount + player.masterClueCount;
		bossKillsArray.add(createBossKillJson(11, player, allClues));
		bossKillsArray.add(createBossKillJson(12, player, player.beginnerClueCount));
		bossKillsArray.add(createBossKillJson(13, player, player.easyClueCount));
		bossKillsArray.add(createBossKillJson(14, player, player.medClueCount));
		bossKillsArray.add(createBossKillJson(15, player, player.hardClueCount));
		bossKillsArray.add(createBossKillJson(16, player, player.eliteClueCount));
		bossKillsArray.add(createBossKillJson(17, player, player.masterClueCount));
		// TODO(polish) - collectionlog
		//bossKillsArray.add(createBossKillJson(18, player, CollectionLogUpdated.getTotalObtained(player)));
		bossKillsArray.add(createBossKillJson(19, player, player.commanderZilyanaKills.getKills()));
		bossKillsArray.add(createBossKillJson(20, player, player.corporealBeastKills.getKills()));
		bossKillsArray.add(createBossKillJson(21, player, player.crazyArchaeologistKills.getKills()));
		bossKillsArray.add(createBossKillJson(22, player, player.dagannothPrimeKills.getKills()));
		bossKillsArray.add(createBossKillJson(23, player, player.dagannothRexKills.getKills()));
		bossKillsArray.add(createBossKillJson(24, player, player.dagannothSupremeKills.getKills()));
		bossKillsArray.add(createBossKillJson(25, player, player.demonicGorillaKills.getKills()));
		bossKillsArray.add(createBossKillJson(26, player, player.dukeKills.getKills()));
		bossKillsArray.add(createBossKillJson(27, player, player.theFightCaveKills.getKills()));
		bossKillsArray.add(createBossKillJson(28, player, player.galvekKills.getKills()));
		bossKillsArray.add(createBossKillJson(29, player, player.generalGraardorKills.getKills()));
		bossKillsArray.add(createBossKillJson(30, player, player.giantMoleKills.getKills()));
		bossKillsArray.add(createBossKillJson(31, player, player.grotesqueGuardianKills.getKills()));
		bossKillsArray.add(createBossKillJson(32, player, player.zukKills.getKills()));
		bossKillsArray.add(createBossKillJson(33, player, player.kalphiteQueenKills.getKills()));
		bossKillsArray.add(createBossKillJson(34, player, player.kingBlackDragonKills.getKills()));
		bossKillsArray.add(createBossKillJson(35, player, player.krakenKills.getKills()));
		bossKillsArray.add(createBossKillJson(36, player, player.kreeArraKills.getKills()));
		bossKillsArray.add(createBossKillJson(37, player, player.krilTsutsarothKills.getKills()));
		bossKillsArray.add(createBossKillJson(38, player, player.nightmareKills.getKills()));
		bossKillsArray.add(createBossKillJson(39, player, player.leviathanKills.getKills()));
		bossKillsArray.add(createBossKillJson(40, player, player.nexKills.getKills()));
		bossKillsArray.add(createBossKillJson(41, player, player.ophidiaKills.getKills()));
		bossKillsArray.add(createBossKillJson(42, player, player.phantomMuspahKills.getKills()));
		bossKillsArray.add(createBossKillJson(43, player, player.sarachnisKills.getKills()));
		bossKillsArray.add(createBossKillJson(44, player, player.scorpiaKills.getKills()));
		bossKillsArray.add(createBossKillJson(45, player, player.skotizoKills.getKills()));
		bossKillsArray.add(createBossKillJson(46, player, player.theGauntletKills.getKills()));
		bossKillsArray.add(createBossKillJson(47, player, player.theCorruptedGauntletKills.getKills()));
		bossKillsArray.add(createBossKillJson(48, player, player.theatreOfBloodKills.getKills()));
		bossKillsArray.add(createBossKillJson(49, player, player.thermonuclearSmokeDevilKills.getKills()));
		bossKillsArray.add(createBossKillJson(50, player, player.tombsOfAmascutKills.getKills()));
		bossKillsArray.add(createBossKillJson(51, player, player.vardorvisKills.getKills()));
		bossKillsArray.add(createBossKillJson(52, player, player.venenatisKills.getKills()));
		bossKillsArray.add(createBossKillJson(53, player, player.vetionKills.getKills()));
		bossKillsArray.add(createBossKillJson(54, player, player.vorkathKills.getKills()));
		bossKillsArray.add(createBossKillJson(55, player, player.whispererKills.getKills()));
		bossKillsArray.add(createBossKillJson(56, player, player.wintertodtKills.getKills()));
		bossKillsArray.add(createBossKillJson(57, player, player.zulrahKills.getKills()));
		bossKillsArray.add(createBossKillJson(58, player, player.dominionOfEchoesKills.getKills()));


		JsonObject ownerObject = new JsonObject();
		ownerObject.addProperty("username", username);
		ownerObject.addProperty("gameMode", gameMode);
		ownerObject.addProperty("gameRank", donatorRank);
		ownerObject.addProperty("gameExperienceMode", difficultyMode);

		JsonObject hiscoresObject = new JsonObject();
		hiscoresObject.add("owner", ownerObject);
		hiscoresObject.add("entries", skillsArray);
		hiscoresObject.add("bossEntries", bossKillsArray);

		HttpRequestEvent builder = new HttpRequestBuilder("hiscore", HttpRequestMethod.POST)
			.body(hiscoresObject)
			.onSuccess(response -> {
				//System.out.println("posted");
			})
			.onFailure(response -> {
				// System.out.println("failed to post");
			})
			.build();

		EventWorker.submitHttpRequest(builder);
	}

	public static void sendBossDetails() {
		JsonArray bossDetailsArray = getBossDetailsJsonArray();
		JsonObject bossDetailsObject = new JsonObject();
		bossDetailsObject.add("bossDetails", bossDetailsArray);
		HttpRequestEvent builder = new HttpRequestBuilder("hiscore/boss-details", HttpRequestMethod.POST)
			.body(bossDetailsObject)
			.onSuccess(response -> {
				//System.out.println("posted");
			})
			.onFailure(response -> {
				// System.out.println("failed to post");
			})
			.build();
		EventWorker.submitHttpRequest(builder);
		//TODO: find correct images for some bosses
	}

	private static JsonObject createTotalSkillJson(Player player) {
		int totalLevel = player.getStats().totalLevel;
		long totalExperience = player.getStats().getTotalXp();

		var totalSkillObject = new JsonObject();
			totalSkillObject.addProperty("skillId", -1);
			totalSkillObject.addProperty("level", totalLevel);
			totalSkillObject.addProperty("experience", totalExperience);

		return totalSkillObject;
	}

	private static JsonObject createSkillJson(int skillId, Player player) {
		int level = player.getStats().get(skillId).fixedLevel;
		double experience = player.getStats().get(skillId).experience;

		long roundedExperience = Math.round(experience);

		var skillObject = new JsonObject();
			skillObject.addProperty("skillId", skillId);
			skillObject.addProperty("level", level);
			skillObject.addProperty("experience", roundedExperience);
		return skillObject;
	}

	public static JsonObject createBossKillJson(int bossId, Player player, int killCount) {
		var bossKillObject = new JsonObject();
			bossKillObject.addProperty("bossId", bossId);
			bossKillObject.addProperty("ownerUsername", player.getName());
			bossKillObject.addProperty("killCount", killCount);
		return bossKillObject;
	}

	private static JsonObject createBossDetailsJson(int bossId, String bossName, String url, int order, String columnName) {
		var bossDetailsObject = new JsonObject();
			bossDetailsObject.addProperty("bossId", bossId);
			bossDetailsObject.addProperty("name", bossName);
			bossDetailsObject.addProperty("image", url);
			bossDetailsObject.addProperty("order", order);
			bossDetailsObject.addProperty("columnName", columnName);
		return bossDetailsObject;
	}

	public static JsonArray getBossDetailsJsonArray() {
		var bossDetailsArray = new JsonArray();
			bossDetailsArray.add(createBossDetailsJson(1, "Abyssal Sire", "/static/img/hiscore/boss/abyssalsire.png", 1, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(2, "Alchemical Hydra", "/static/img/hiscore/boss/alchemicalhydra.png", 2, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(3, "Argentavis", "/static/img/hiscore/boss/pvparenarank.png", 3, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(4, "Barrows Chests", "/static/img/hiscore/boss/barrowschests.png", 4, "Chests Opened"));
			bossDetailsArray.add(createBossDetailsJson(5, "Callisto", "/static/img/hiscore/boss/callisto.png", 5, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(6, "Cerberus", "/static/img/hiscore/boss/cerberus.png", 6, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(7, "Chambers of Xeric", "/static/img/hiscore/boss/chambersofxeric.png", 7, "Raid Completions"));
			bossDetailsArray.add(createBossDetailsJson(8, "Chambers of Xeric (Olm Only)", "/static/img/hiscore/boss/chambersofxericchallengemode.png", 8, "Raid Completions"));
			bossDetailsArray.add(createBossDetailsJson(9, "Chaos Elemental", "/static/img/hiscore/boss/chaoselemental.png", 9, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(10, "Chaos Fanatic", "/static/img/hiscore/boss/chaosfanatic.png", 10, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(11, "Clue Scrolls (All)", "/static/img/hiscore/boss/cluescrollsall.png", 11, "Completions"));
			bossDetailsArray.add(createBossDetailsJson(12, "Clue Scrolls (Beginner)", "/static/img/hiscore/boss/cluescrollsbeginner.png", 12, "Completions"));
			bossDetailsArray.add(createBossDetailsJson(13, "Clue Scrolls (Easy)", "/static/img/hiscore/boss/cluescrollseasy.png", 13, "Completions"));
			bossDetailsArray.add(createBossDetailsJson(14, "Clue Scrolls (Medium)", "/static/img/hiscore/boss/cluescrollsmedium.png", 14, "Completions"));
			bossDetailsArray.add(createBossDetailsJson(15, "Clue Scrolls (Hard)", "/static/img/hiscore/boss/cluescrollshard.png", 15, "Completions"));
			bossDetailsArray.add(createBossDetailsJson(16, "Clue Scrolls (Elite)", "/static/img/hiscore/boss/cluescrollselite.png", 16, "Completions"));
			bossDetailsArray.add(createBossDetailsJson(17, "Clue Scrolls (Master)", "/static/img/hiscore/boss/cluescrollsmaster.png", 17, "Completions"));
			bossDetailsArray.add(createBossDetailsJson(18, "Collections Logged", "/static/img/hiscore/boss/collectionslogged.png", 18, "Collections"));
			bossDetailsArray.add(createBossDetailsJson(19, "Commander Zilyana", "/static/img/hiscore/boss/commanderzilyana.png", 19, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(20, "Corporeal Beast", "/static/img/hiscore/boss/corporealbeast.png", 20, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(21, "Crazy Archaeologist", "/static/img/hiscore/boss/crazyarchaeologist.png", 21, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(22, "Dagannoth Prime", "/static/img/hiscore/boss/dagannothprime.png", 22, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(23, "Dagannoth Rex", "/static/img/hiscore/boss/dagannothrex.png", 23, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(24, "Dagannoth Supreme", "/static/img/hiscore/boss/dagannothsupreme.png", 24, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(25, "Demonic Gorilla", "/static/img/hiscore/boss/pvparenarank.p", 25, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(26, "Duke Sucellus", "/static/img/hiscore/boss/dukesucellus.png", 26, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(27, "Fight Caves", "/static/img/hiscore/boss/tztokjad.png", 27, "Completions"));
			bossDetailsArray.add(createBossDetailsJson(28, "Galvek", "/static/img/hiscore/boss/pvparenarank.png", 28, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(29, "General Graardor", "/static/img/hiscore/boss/generalgraardor.png", 29, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(30, "Giant Mole", "/static/img/hiscore/boss/giantmole.png", 30, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(31, "Grotesque Guardians", "/static/img/hiscore/boss/grotesqueguardians.png", 31, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(32, "Inferno", "/static/img/hiscore/boss/tzkalzuk.png", 32, "Completions"));
			bossDetailsArray.add(createBossDetailsJson(33, "Kalphite Queen", "/static/img/hiscore/boss/kalphitequeen.png", 33, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(34, "King Black Dragon", "/static/img/hiscore/boss/kingblackdragon.png", 34, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(35, "Kraken", "/static/img/hiscore/boss/kraken.png", 35, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(36, "Kree'Arra", "/static/img/hiscore/boss/kreearra.png", 36, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(37, "K'ril Tsutsaroth", "/static/img/hiscore/boss/kriltsutsaroth.png", 37, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(38, "The Nightmare", "/static/img/hiscore/boss/nightmare.png", 38, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(39, "Leviathan", "/static/img/hiscore/boss/theleviathan.png", 39, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(40, "Nex", "/static/img/hiscore/boss/nex.png", 40, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(41, "Ophidia", "/static/img/hiscore/boss/pvparenarank.png", 41, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(42, "Phantom Muspah", "/static/img/hiscore/boss/phantommuspah.png", 42, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(43, "Sarachnis", "/static/img/hiscore/boss/sarachnis.png", 43, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(44, "Scorpia", "/static/img/hiscore/boss/scorpia.png", 44, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(45, "Skotizo", "/static/img/hiscore/boss/skotizo.png", 45, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(46, "The Gauntlet", "/static/img/hiscore/boss/thegauntlet.png", 46, "Completions"));
			bossDetailsArray.add(createBossDetailsJson(47, "The Corrupted Gauntlet", "/static/img/hiscore/boss/thecorruptedgauntlet.png", 47, "Completions"));
			bossDetailsArray.add(createBossDetailsJson(48, "Theatre of Blood", "/static/img/hiscore/boss/theatreofblood.png", 48, "Raid Completions"));
			bossDetailsArray.add(createBossDetailsJson(49, "Thermonuclear Smoke Devil", "/static/img/hiscore/boss/thermonuclearsmokedevil.png", 49, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(50, "Tombs of Amascut", "/static/img/hiscore/boss/tombsofamascut.png", 50, "Raid Completions"));
			bossDetailsArray.add(createBossDetailsJson(51, "Vardorvis", "/static/img/hiscore/boss/vardorvis.png", 51, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(52, "Venenatis", "/static/img/hiscore/boss/venenatis.png", 52, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(53, "Vet'ion", "/static/img/hiscore/boss/vetion.png", 53, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(54, "Vorkath", "/static/img/hiscore/boss/vorkath.png", 54, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(55, "The Whisperer", "/static/img/hiscore/boss/therwhisperer.png", 55, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(56, "Wintertodt", "/static/img/hiscore/boss/wintertodt.png", 56, "Kills"));
			bossDetailsArray.add(createBossDetailsJson(57, "Zulrah", "/static/img/hiscore/boss/zulrah.png", 57, "Kills"));
		return bossDetailsArray;
	}


}
