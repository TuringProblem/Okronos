package io.ruin.model.activities.gauntlet;

import io.ruin.Server;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.gauntlet.resources.FishingSpot;
import io.ruin.model.activities.gauntlet.resources.LinumPlant;
import io.ruin.model.activities.gauntlet.resources.PhrenRoot;
import io.ruin.model.activities.gauntlet.resources.Rock;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.TheGoldenGauntlet;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.entity.shared.listeners.LogoutListener;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.skill.SkillDialogue;
import io.ruin.model.inter.dialogue.skill.SkillItem;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.item.containers.Inventory;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicChunk;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static core.task.api.API.*;

@Slf4j
public class Gauntlet {

	final Player player;
	public boolean corrupted;

	boolean fiveMinuteWarning = false;
	boolean oneMinuteWarning = false;

	public int bossDamage = 0;

	boolean killedBoss = false;

	boolean bossFightStarted = false;

	public boolean inGauntlet = false;

	int timeTilNewTiles = 45;

	private boolean weaponFrameObtained = false;
	private int tier1MonstersKilled = 0;

	private ActivityTimer lootTimer;

	public DynamicMap map;

	public List<GauntletResource> gauntletResources = new ArrayList<>();

	NPC boss;

	int[][] corruptedConnectorChunksArray = new int[][] {
			{240, 704}, {240, 706}, {240, 708}, {240, 710}
	};
	int[][] corruptedEdgeChunksArraySW = new int[][] {
			{242, 704}, {242, 706}, {242, 708}, {242, 710}
	};

	int[][] corruptedCornerChunksArray = new int[][] {
			{244, 704}, {244, 706}, {244, 708}, {244, 710}
	};

	int[][] corruptedCornerChunksArrayNW = new int[][] {
			{245, 705}, {245, 707}, {245, 709}, {245, 711}
	};

	int[][] regularCornerChunksArrayNW = new int[][] {
			{237, 705}, {237, 707}, {237, 709}, {237, 711}
	};

	int[][] regularCornerChunksArray = new int[][] {
			{236, 704}, {236, 706}, {236, 708}, {236, 710}
	};

	int[][] regularEdgeChunksArraySW = new int[][] {
			{234, 704}, {234, 706}, {234, 708}, {234, 710}
	};

	int[][] regularConnectorChunksArray = new int[][] {
			{232, 704}, {232, 706}, {232, 708}, {232, 710}
	};

	int[][] swMonsterSpawns = new int[][] {
			{7, 10}, {9, 6}, {22, 56}, {24, 52}, {21, 40}, {24, 36}, {22, 25}, {24, 22}, {38, 25},
			{40, 22}, {54, 24},
			{56, 22}, {54, 41}, {57, 38}, {54, 57}, {56, 54}
	};

	int[][] swDemiBossSpawns = new int[][] {
			{7, 23}, {7, 40}, {7, 53}, {23, 7}, {40, 7}, {56, 7}
	};

	int[][] seMonsterSpawns = new int[][] {
			{6, 9}, {8, 6}
	};

	int[][] seDemiBossSpawns = new int[][] {
			{8, 24}, {8, 40}, {8, 56}
	};

	int[][] nwMonsterSpawns = new int[][] {
			{6, 8}, {8, 6}
	};

	int[][] nwDemiBossSpawns = new int[][] {
			{24, 7}, {39, 8}, {56, 8}
	};

	int[][] neMonsterSpawns = new int[][] {
			{6, 6}, {9, 8}
	};

	List<Integer[]> swFishingSpots = new ArrayList<>();
	List<Integer[]> seFishingSpots = new ArrayList<>();
	List<Integer[]> nwFishingSpots = new ArrayList<>();
	List<Integer[]> neResourceSpawns = new ArrayList<>();
	List<Integer[]> nwResourceSpawns = new ArrayList<>();
	List<Integer[]> seResourceSpawns = new ArrayList<>();
	List<Integer[]> swResourceSpawns = new ArrayList<>();
	List<Integer> weaponPieces = new ArrayList<>();

	List<Integer> resourceObjects = new ArrayList<>();
	List<Integer> monsterIds = new ArrayList<>();
	List<Integer> demiBossIds = new ArrayList<>();

	List<DynamicChunk> southWestChunks = new ArrayList<>();
	List<DynamicChunk> southEastChunks = new ArrayList<>();
	List<DynamicChunk> northEastChunks = new ArrayList<>();
	List<DynamicChunk> northWestChunks = new ArrayList<>();

	private final Position CHEST_LOCATION = new Position(3031, 6121, 1);

	public Gauntlet(Player player) {
		this.player = player;
		this.player.gauntlet = this;
	}

	private void setFakeContainers(Player player) {
		if (player.temporaryInventory == null)
			player.temporaryInventory = new Inventory();
		if (player.temporaryEquipment == null)
			player.temporaryEquipment = new Equipment();

		player.temporaryEquipment.init(player, 14, -1, 64208, 94, false);
		player.getEquipment().update(Equipment.SLOT_WEAPON);
		player.getEquipment().sendAll = true;
		player.temporaryInventory.init(player, 28, -1, 0, 93, false);
		player.temporaryInventory.sendAll = true;
	}

	void removeFakeContainer(Player player) {
		if (player.temporaryInventory != null) {
			player.temporaryInventory.clear();
			player.temporaryInventory = null;
		}
		if (player.temporaryEquipment != null) {
			player.temporaryEquipment.clear();
			player.temporaryEquipment = null;
		}
		player.getEquipment().update(Equipment.SLOT_WEAPON);
		player.getEquipment().sendAll = true;
		player.getInventory().sendAll = true;
	}

	public void start(boolean corrupted) {
		this.corrupted = corrupted;
		southWestChunks.clear();
		addWeaponPiecesToList();
		addResourceSpotsToList();
		addFishingSpotsToList();
		int gauntletPerkLevel = 0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_GOLDEN_GAUNTLET)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_GOLDEN_GAUNTLET);
			TheGoldenGauntlet c = (TheGoldenGauntlet) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			gauntletPerkLevel = c.getPerkLevel();
		}

		try {
			buildMap(gauntletPerkLevel >= 5, corrupted);
		} catch (DynamicMap.DynamicMapBuildException e) {
			player.sendMessage("Unable to build dynamic map.");
			return;
		}

		player.teleportListener = p -> {
			p.sendMessage("A mysterious force prevents you from teleporting.");
			return false;
		};
		setFakeContainers(player);
		player.getCombat().restore();
		this.corrupted = corrupted;

		if (gauntletPerkLevel >= 5) {
			player.getInventory().add(corrupted ? new Item(23854) : new Item(23900));
			player.getInventory().add(corrupted ? new Item(23857) : new Item(23903));
			player.getEquipment().set(Equipment.SLOT_WEAPON, corrupted ? new Item(23851) : new Item(23897));
			player.getEquipment().set(Equipment.SLOT_HAT, corrupted ? new Item(23842) : new Item(23888));
			player.getEquipment().set(Equipment.SLOT_CHEST, corrupted ? new Item(23845) : new Item(23891));
			player.getEquipment().set(Equipment.SLOT_LEGS, corrupted ? new Item(23848) : new Item(23894));
			player.getInventory().add(ItemID.EGNIOL_POTION_3, 2);
			player.getInventory().add(23874, 14);
			player.getInventory().add(corrupted ? 25958 : 25960, 5);
		} else {
			player.getInventory().add(corrupted ? new Item(23821) : new Item(23862));
			player.getInventory().add(corrupted ? new Item(23822) : new Item(23863));
			player.getInventory().add(corrupted ? new Item(23823) : new Item(23864));
			player.getInventory().add(new Item(233));
			player.getInventory().add(corrupted ? new Item(23858) : new Item(23904));
			player.getEquipment().set(Equipment.SLOT_WEAPON, corrupted ? new Item(23820) : new Item(23861));
		}

		inGauntlet = true;

		player.deathStartListener = (DeathListener.Simple) this::startDeathEvent;
		player.logoutListener = new LogoutListener().onLogout(p -> {
			onLogout(p);
		});

		if (gauntletPerkLevel < 5) {
			spawnResources();
			spawnMonsters();
		}
		int randomAddition = Random.get(0, 2);
		player.deathEndListener = (DeathListener.Simple) this::handleDeath;
		lootTimer = new ActivityTimer();
		player.gauntletBossTimer.delaySeconds(corrupted ? 800 : 1200);


		// once player leaves the assigned map by any means, it will be removed from the gauntlet
		player.queue(() -> {
			player.getMovement().teleport(map.swRegion.baseX + 43, map.swRegion.baseY + 44, 1);
			sleep(1);

			while (this.map.containsPlayer(player)) {
				sleep(1);
			}

			// already handled leaving by object action etc.
			if (!inGauntlet) {
				return;
			}

			onLeftMap(player);
		});

		boss = new NPC(corrupted ? 9035 + randomAddition : 9021 + randomAddition).spawn(map.swRegion.baseX + 39,
				map.swRegion.baseY + 56, 1, 1);
		if(boss.getId() == 9021 || boss.getId() == 9035)
			boss.setHeadIcon(NPC.DefaultHeadIconIndex.ProtectFromMelee);
		else if(boss.getId() == 9022 || boss.getId() == 9036)
			boss.setHeadIcon(NPC.DefaultHeadIconIndex.ProtectFromRanged);
		else if(boss.getId() == 9023 || boss.getId() == 9037)
			boss.setHeadIcon(NPC.DefaultHeadIconIndex.ProtectFromMagic);
		map.addNpc(boss);
	}

	public void enter(Player player) {
		player.dialogue(new NPCDialogue(9020, "Ah I see you want to take on the Gauntlet!"),
				new OptionsDialogue("Which Gauntlet would you like to enter?",
						new Option("I'd like to enter the corrupted Gauntlet.", () -> {
							start(true);
						}),
						new Option("I'd like to enter the normal Gauntlet.", () -> {
							start(false);
						}),
						new Option("Nevermind.", player::closeDialogue)));
	}

	public void onLogout(Player player) {
		this.leave(player, CHEST_LOCATION);
	}

	BossTileHandler bossTileHandler;

	void activateDangerousTiles(Player player) {
		if (bossTileHandler == null)
			bossTileHandler = new BossTileHandler(player, map, corrupted, boss);
		bossTileHandler.startDangerousTilesEvent(player, timeTilNewTiles);
		timeTilNewTiles = 45;
	}

	void addWeaponPiecesToList() {
		weaponPieces.add(corrupted ? 23831 : 23868);
		weaponPieces.add(corrupted ? 23832 : 23869);
		weaponPieces.add(corrupted ? 23833 : 23870);
	}

	void spawnResources() {
		spawnNEResources();
		spawnNWResources();
		spawnSEResources();
		spawnSWResources();
		spawnSWFishingSpots();
		spawnNWFishingSpots();
		spawnSEFishingSpots();
	}

	void spawnMonsters() {
		spawnSWMonsters();
		spawnSWDemiboss();
		spawnSEMonsters();
		spawnSEDemiboss();
		spawnNEMonsters();
		spawnNWMonsters();
		spawnNWDemiboss();
	}

	void addResourceObjectsToList() {
		resourceObjects.add(corrupted ? 35967 : 36064);
		resourceObjects.add(corrupted ? 35975 : 36072);
		resourceObjects.add(corrupted ? 35969 : 36066);
		resourceObjects.add(corrupted ? 35973 : 36070);
	}

	public void activateTeleportCrystal() {
		player.getMovement().teleport(map.swRegion.baseX + 43, map.swRegion.baseY + 44, 1);
		player.getInventory().remove(corrupted ? 23858 : 23904, 1);
		player.sendMessage("You teleport back to the start room consuming your teleport crystal.");
	}

	void addFishingSpotsToList() {
		swFishingSpots.add(new Integer[] {13, 9});
		swFishingSpots.add(new Integer[] {9, 13});
		swFishingSpots.add(new Integer[] {9, 18});
		swFishingSpots.add(new Integer[] {13, 22});
		swFishingSpots.add(new Integer[] {6, 29});
		swFishingSpots.add(new Integer[] {6, 34});
		swFishingSpots.add(new Integer[] {6, 45});
		swFishingSpots.add(new Integer[] {13, 41});
		swFishingSpots.add(new Integer[] {9, 50});
		swFishingSpots.add(new Integer[] {13, 54});
		swFishingSpots.add(new Integer[] {6, 61});
		swFishingSpots.add(new Integer[] {18, 5});
		swFishingSpots.add(new Integer[] {22, 13});
		swFishingSpots.add(new Integer[] {29, 9});
		swFishingSpots.add(new Integer[] {34, 9});
		swFishingSpots.add(new Integer[] {38, 13});
		swFishingSpots.add(new Integer[] {45, 9});
		swFishingSpots.add(new Integer[] {50, 6});
		swFishingSpots.add(new Integer[] {61, 9});
		swFishingSpots.add(new Integer[] {54, 13});
		swFishingSpots.add(new Integer[] {54, 61});
		swFishingSpots.add(new Integer[] {61, 57});
		swFishingSpots.add(new Integer[] {57, 50});
		swFishingSpots.add(new Integer[] {50, 54});
		swFishingSpots.add(new Integer[] {54, 45});
		swFishingSpots.add(new Integer[] {61, 41});
		swFishingSpots.add(new Integer[] {57, 34});
		swFishingSpots.add(new Integer[] {50, 38});
		swFishingSpots.add(new Integer[] {54, 29});
		swFishingSpots.add(new Integer[] {61, 22});
		swFishingSpots.add(new Integer[] {57, 18});
		swFishingSpots.add(new Integer[] {50, 22});
		swFishingSpots.add(new Integer[] {45, 25});
		swFishingSpots.add(new Integer[] {41, 18});
		swFishingSpots.add(new Integer[] {34, 22});
		swFishingSpots.add(new Integer[] {38, 29});
		swFishingSpots.add(new Integer[] {29, 25});
		swFishingSpots.add(new Integer[] {22, 29});
		swFishingSpots.add(new Integer[] {18, 22});
		swFishingSpots.add(new Integer[] {25, 18});
		swFishingSpots.add(new Integer[] {25, 34});
		swFishingSpots.add(new Integer[] {29, 41});
		swFishingSpots.add(new Integer[] {22, 45});
		swFishingSpots.add(new Integer[] {18, 54});
		swFishingSpots.add(new Integer[] {29, 57});
		seFishingSpots.add(new Integer[] {3, 9});
		seFishingSpots.add(new Integer[] {10, 12});
		seFishingSpots.add(new Integer[] {5, 9});
		seFishingSpots.add(new Integer[] {5, 28});
		seFishingSpots.add(new Integer[] {5, 35});
		seFishingSpots.add(new Integer[] {5, 44});
		seFishingSpots.add(new Integer[] {5, 51});
		seFishingSpots.add(new Integer[] {5, 60});
		nwFishingSpots.add(new Integer[] {4, 3});
		nwFishingSpots.add(new Integer[] {12, 9});
		nwFishingSpots.add(new Integer[] {19, 9});
		nwFishingSpots.add(new Integer[] {28, 10});
		nwFishingSpots.add(new Integer[] {35, 10});
		nwFishingSpots.add(new Integer[] {44, 10});
		nwFishingSpots.add(new Integer[] {51, 10});
		nwFishingSpots.add(new Integer[] {60, 10});
	}

	void addResourceSpotsToList() {
		swResourceSpawns.add(new Integer[] {2, 2});
		swResourceSpawns.add(new Integer[] {2, 13});
		swResourceSpawns.add(new Integer[] {13, 13});
		swResourceSpawns.add(new Integer[] {13, 2});
		swResourceSpawns.add(new Integer[] {2, 18});
		swResourceSpawns.add(new Integer[] {2, 27});
		swResourceSpawns.add(new Integer[] {13, 27});
		swResourceSpawns.add(new Integer[] {13, 18});
		swResourceSpawns.add(new Integer[] {2, 34});
		swResourceSpawns.add(new Integer[] {2, 45});
		swResourceSpawns.add(new Integer[] {13, 45});
		swResourceSpawns.add(new Integer[] {13, 34});
		swResourceSpawns.add(new Integer[] {2, 50});
		swResourceSpawns.add(new Integer[] {2, 61});
		swResourceSpawns.add(new Integer[] {13, 61});
		swResourceSpawns.add(new Integer[] {13, 50});
		swResourceSpawns.add(new Integer[] {18, 2});
		swResourceSpawns.add(new Integer[] {18, 13});
		swResourceSpawns.add(new Integer[] {29, 13});
		swResourceSpawns.add(new Integer[] {29, 2});
		swResourceSpawns.add(new Integer[] {34, 2});
		swResourceSpawns.add(new Integer[] {45, 2});
		swResourceSpawns.add(new Integer[] {34, 13});
		swResourceSpawns.add(new Integer[] {45, 13});
		swResourceSpawns.add(new Integer[] {50, 2});
		swResourceSpawns.add(new Integer[] {50, 13});
		swResourceSpawns.add(new Integer[] {61, 13});
		swResourceSpawns.add(new Integer[] {61, 2});
		swResourceSpawns.add(new Integer[] {50, 61});
		swResourceSpawns.add(new Integer[] {61, 61});
		swResourceSpawns.add(new Integer[] {50, 50});
		swResourceSpawns.add(new Integer[] {61, 50});
		swResourceSpawns.add(new Integer[] {50, 45});
		swResourceSpawns.add(new Integer[] {61, 45});
		swResourceSpawns.add(new Integer[] {50, 34});
		swResourceSpawns.add(new Integer[] {61, 34});
		swResourceSpawns.add(new Integer[] {50, 29});
		swResourceSpawns.add(new Integer[] {61, 29});
		swResourceSpawns.add(new Integer[] {50, 18});
		swResourceSpawns.add(new Integer[] {61, 18});
		swResourceSpawns.add(new Integer[] {34, 29});
		swResourceSpawns.add(new Integer[] {45, 29});
		swResourceSpawns.add(new Integer[] {34, 18});
		swResourceSpawns.add(new Integer[] {45, 18});
		swResourceSpawns.add(new Integer[] {18, 29});
		swResourceSpawns.add(new Integer[] {29, 29});
		swResourceSpawns.add(new Integer[] {18, 18});
		swResourceSpawns.add(new Integer[] {29, 18});
		swResourceSpawns.add(new Integer[] {18, 45});
		swResourceSpawns.add(new Integer[] {29, 45});
		swResourceSpawns.add(new Integer[] {18, 34});
		swResourceSpawns.add(new Integer[] {29, 34});
		swResourceSpawns.add(new Integer[] {18, 61});
		swResourceSpawns.add(new Integer[] {29, 61});
		swResourceSpawns.add(new Integer[] {18, 50});
		swResourceSpawns.add(new Integer[] {29, 50});
		seResourceSpawns.add(new Integer[] {3, 2});
		seResourceSpawns.add(new Integer[] {2, 12});
		seResourceSpawns.add(new Integer[] {13, 12});
		seResourceSpawns.add(new Integer[] {13, 18});
		seResourceSpawns.add(new Integer[] {13, 29});
		seResourceSpawns.add(new Integer[] {2, 29});
		seResourceSpawns.add(new Integer[] {2, 34});
		seResourceSpawns.add(new Integer[] {13, 34});
		seResourceSpawns.add(new Integer[] {13, 45});
		seResourceSpawns.add(new Integer[] {2, 50});
		seResourceSpawns.add(new Integer[] {2, 61});
		seResourceSpawns.add(new Integer[] {13, 61});
		nwResourceSpawns.add(new Integer[] {2, 2});
		nwResourceSpawns.add(new Integer[] {13, 2});
		nwResourceSpawns.add(new Integer[] {13, 13});
		nwResourceSpawns.add(new Integer[] {18, 13});
		nwResourceSpawns.add(new Integer[] {45, 13});
		nwResourceSpawns.add(new Integer[] {29, 2});
		nwResourceSpawns.add(new Integer[] {40, 13});
		nwResourceSpawns.add(new Integer[] {61, 2});
		nwResourceSpawns.add(new Integer[] {50, 13});
		neResourceSpawns.add(new Integer[] {2, 2});
		neResourceSpawns.add(new Integer[] {13, 13});
	}

	void addMonstersToList() {
		// System.out.println("? " + corrupted);
		monsterIds.add(corrupted ? 9040 : 9026);
		monsterIds.add(corrupted ? 9041 : 9027);
		monsterIds.add(corrupted ? 9042 : 9028);
		monsterIds.add(corrupted ? 9043 : 9029);
		monsterIds.add(corrupted ? 9044 : 9030);
		monsterIds.add(corrupted ? 9045 : 9031);
	}

	void addDemibossToList() {
		demiBossIds.add(corrupted ? 9046 : 9032);
		demiBossIds.add(corrupted ? 9047 : 9033);
		demiBossIds.add(corrupted ? 9048 : 9034);
	}

	void spawnSWFishingSpots() {
		int randomFishingSpotsAmount = Random.get(3, 6);
		for (int i = 0; i < randomFishingSpotsAmount; i++) {
			int randomSpawn = Random.get(0, swFishingSpots.size() - 1);

			GameObject fishingSpot = GameObject.spawn(corrupted ? 35971 : 36068,
					map.swRegion.baseX + swFishingSpots.get(randomSpawn)[0],
					map.swRegion.baseY + swFishingSpots.get(randomSpawn)[1], 1, 10, 0);
			swFishingSpots.remove(randomSpawn);
			spawnFishingResource(fishingSpot);
		}
	}

	void spawnSEFishingSpots() {
		int randomFishingSpotsAmount = Random.get(1, 3);
		for (int i = 0; i < randomFishingSpotsAmount; i++) {
			int randomSpawn = Random.get(0, seFishingSpots.size() - 1);
			GameObject fishingSpot = GameObject.spawn(corrupted ? 35971 : 36068,
					map.seRegion.baseX + seFishingSpots.get(randomSpawn)[0],
					map.seRegion.baseY + seFishingSpots.get(randomSpawn)[1], 1, 10, 0);
			seFishingSpots.remove(randomSpawn);
			spawnFishingResource(fishingSpot);
		}
	}

	void spawnNWFishingSpots() {
		int randomFishingSpotsAmount = Random.get(1, 3);
		for (int i = 0; i < randomFishingSpotsAmount; i++) {
			int randomSpawn = Random.get(0, nwFishingSpots.size() - 1);
			// if(map == null)
			// System.out.println("MAP NULL");
			GameObject fishingSpot = GameObject.spawn(corrupted ? 35971 : 36068,
					map.nwRegion.baseX + nwFishingSpots.get(randomSpawn)[0],
					map.nwRegion.baseY + nwFishingSpots.get(randomSpawn)[1], 1, 10, 0);
			nwFishingSpots.remove(randomSpawn);
			spawnFishingResource(fishingSpot);
		}
	}

	void spawnSWResources() {
		int randomResourceAmount = Random.get(20, 30);
		for (int i = 0; i < randomResourceAmount; i++) {
			if (resourceObjects.isEmpty())
				addResourceObjectsToList();
			int randomObject = Random.get(0, resourceObjects.size() - 1);
			int randomSpawn = Random.get(0, swResourceSpawns.size() - 1);
			GameObject spawnedObj = GameObject.spawn(resourceObjects.get(randomObject),
					map.swRegion.baseX + swResourceSpawns.get(randomSpawn)[0],
					map.swRegion.baseY + swResourceSpawns.get(randomSpawn)[1], 1, 10, 0);
			switch (spawnedObj.getId()) {
				case 35969:
				case 36066:
					spawnPhrenRootsResource(spawnedObj);
					break;
				case 35975:
				case 36072:
					spawnLinumResource(spawnedObj);
					break;
				case 35967:
				case 36064:
					spawnRockResource(spawnedObj);
					break;
			}
			resourceObjects.remove(randomObject);
			swResourceSpawns.remove(randomSpawn);
		}
	}

	void spawnSWMonsters() {
		for (int[] swMonsterSpawn : swMonsterSpawns) {
			if (monsterIds.isEmpty())
				addMonstersToList();
			int randomMonster = Random.get(0, monsterIds.size() - 1);
			var monster = new NPC(monsterIds.get(randomMonster))
					.spawn(map.swRegion.baseX + swMonsterSpawn[0], map.swRegion.baseY + swMonsterSpawn[1], 1, 1);
			map.addNpc(monster);
			monsterIds.remove(randomMonster);
		}
	}

	void spawnSWDemiboss() {
		for (int[] swDemiBossSpawn : swDemiBossSpawns) {
			if (demiBossIds.isEmpty())
				addDemibossToList();
			int randomDemiboss = Random.get(0, demiBossIds.size() - 1);
			var monster = new NPC(demiBossIds.get(randomDemiboss))
					.spawn(map.swRegion.baseX + swDemiBossSpawn[0], map.swRegion.baseY + swDemiBossSpawn[1], 1, 1);
			map.addNpc(monster);
			demiBossIds.remove(randomDemiboss);
		}
	}

	void spawnSEMonsters() {
		for (int[] seMonsterSpawn : seMonsterSpawns) {
			if (monsterIds.isEmpty())
				addMonstersToList();
			int randomMonster = Random.get(0, monsterIds.size() - 1);
			var monster = new NPC(monsterIds.get(randomMonster))
					.spawn(map.seRegion.baseX + seMonsterSpawn[0], map.seRegion.baseY + seMonsterSpawn[1], 1, 1);
			map.addNpc(monster);
			monsterIds.remove(randomMonster);
		}
	}

	void spawnSEDemiboss() {
		for (int[] seDemiBossSpawn : seDemiBossSpawns) {
			if (demiBossIds.isEmpty())
				addDemibossToList();
			int randomDemiboss = Random.get(0, demiBossIds.size() - 1);
			var monster = new NPC(demiBossIds.get(randomDemiboss))
					.spawn(map.seRegion.baseX + seDemiBossSpawn[0], map.seRegion.baseY + seDemiBossSpawn[1], 1, 1);
			map.addNpc(monster);
			demiBossIds.remove(randomDemiboss);
		}
	}

	void spawnNEMonsters() {
		for (int[] neMonsterSpawn : neMonsterSpawns) {
			if (monsterIds.isEmpty())
				addMonstersToList();
			int randomMonster = Random.get(0, monsterIds.size() - 1);
			var monster = new NPC(monsterIds.get(randomMonster))
					.spawn(map.neRegion.baseX + neMonsterSpawn[0], map.neRegion.baseY + neMonsterSpawn[1], 1, 1);
			map.addNpc(monster);
			monsterIds.remove(randomMonster);
		}
	}

	void spawnNWMonsters() {
		for (int[] nwMonsterSpawn : nwMonsterSpawns) {
			if (monsterIds.isEmpty())
				addMonstersToList();
			int randomMonster = Random.get(0, monsterIds.size() - 1);
			var monster = new NPC(monsterIds.get(randomMonster))
					.spawn(map.nwRegion.baseX + nwMonsterSpawn[0], map.nwRegion.baseY + nwMonsterSpawn[1], 1, 1);
			map.addNpc(monster);
			monsterIds.remove(randomMonster);
		}
	}

	void spawnNWDemiboss() {
		for (int[] nwDemiBossSpawn : nwDemiBossSpawns) {
			if (demiBossIds.isEmpty())
				addDemibossToList();
			int randomDemiboss = Random.get(0, demiBossIds.size() - 1);
			var monster = new NPC(demiBossIds.get(randomDemiboss))
					.spawn(map.nwRegion.baseX + nwDemiBossSpawn[0], map.nwRegion.baseY + nwDemiBossSpawn[1], 1, 1);
			map.addNpc(monster);
			demiBossIds.remove(randomDemiboss);
		}
	}

	void spawnSEResources() {
		int randomResourceAmount = Random.get(4, 9);
		for (int i = 0; i < randomResourceAmount; i++) {
			if (resourceObjects.isEmpty())
				addResourceObjectsToList();
			int randomObject = Random.get(0, resourceObjects.size() - 1);
			int randomSpawn = Random.get(0, seResourceSpawns.size() - 1);
			GameObject spawnedObj = GameObject.spawn(resourceObjects.get(randomObject),
					map.seRegion.baseX + seResourceSpawns.get(randomSpawn)[0],
					map.seRegion.baseY + seResourceSpawns.get(randomSpawn)[1], 1, 10, 0);
			switch (spawnedObj.getId()) {
				case 35973:
				case 36070:
					spawnFishingResource(spawnedObj);
					break;
				case 35969:
				case 36066:
					spawnPhrenRootsResource(spawnedObj);
					break;
				case 35975:
				case 36072:
					spawnLinumResource(spawnedObj);
					break;
				case 35967:
				case 36064:
					spawnRockResource(spawnedObj);
					break;
			}
			resourceObjects.remove(randomObject);
			seResourceSpawns.remove(randomSpawn);
		}
	}

	void spawnNEResources() {
		int randomResourceAmount = Random.get(0, 1);
		for (int i = 0; i < randomResourceAmount; i++) {
			if (resourceObjects.isEmpty())
				addResourceObjectsToList();
			int randomObject = Random.get(0, resourceObjects.size() - 1);
			int randomSpawn = Random.get(0, neResourceSpawns.size() - 1);
			GameObject spawnedObj = GameObject.spawn(resourceObjects.get(randomObject),
					map.neRegion.baseX + neResourceSpawns.get(randomSpawn)[0],
					map.neRegion.baseY + neResourceSpawns.get(randomSpawn)[1], 1, 10, 0);
			switch (spawnedObj.getId()) {
				case 35973:
				case 36070:
					spawnFishingResource(spawnedObj);
					break;
				case 35969:
				case 36066:
					spawnPhrenRootsResource(spawnedObj);
					break;
				case 35975:
				case 36072:
					spawnLinumResource(spawnedObj);
					break;
				case 35967:
				case 36064:
					spawnRockResource(spawnedObj);
					break;
			}
			resourceObjects.remove(randomObject);
			neResourceSpawns.remove(randomSpawn);
		}
	}

	void spawnFishingResource(GameObject obj) {
		// System.out.println("fishing spot spawned when list was size " +
		// gauntletResources.size());
		GauntletResource fishingSpot = new FishingSpot(obj, corrupted);
		gauntletResources.add(fishingSpot);
		// System.out.println("fishing spot id is " + fishingSpot.object.id);
		// System.out.println("fishing spot added size is now " +
		// gauntletResources.size());
	}

	void spawnPhrenRootsResource(GameObject obj) {
		GauntletResource phrenRoot = new PhrenRoot(obj, corrupted);
		gauntletResources.add(phrenRoot);
	}

	void spawnLinumResource(GameObject obj) {
		GauntletResource linumPlant = new LinumPlant(obj, corrupted);
		gauntletResources.add(linumPlant);
	}

	void spawnRockResource(GameObject obj) {
		GauntletResource rock = new Rock(obj, corrupted);
		gauntletResources.add(rock);
	}

	void spawnNWResources() {
		int randomResourceAmount = Random.get(3, 6);
		for (int i = 0; i < randomResourceAmount; i++) {
			if (resourceObjects.isEmpty())
				addResourceObjectsToList();
			int randomObject = Random.get(0, resourceObjects.size() - 1);
			int randomSpawn = Random.get(0, nwResourceSpawns.size() - 1);
			GameObject spawnedObj = GameObject.spawn(resourceObjects.get(randomObject),
					map.nwRegion.baseX + nwResourceSpawns.get(randomSpawn)[0],
					map.nwRegion.baseY + nwResourceSpawns.get(randomSpawn)[1], 1, 10, 0);
			switch (spawnedObj.getId()) {
				case 35973:
				case 36070:
					spawnFishingResource(spawnedObj);
					break;
				case 35969:
				case 36066:
					spawnPhrenRootsResource(spawnedObj);
					break;
				case 35975:
				case 36072:
					spawnLinumResource(spawnedObj);
					break;
				case 35967:
				case 36064:
					spawnRockResource(spawnedObj);
					break;
			}
			resourceObjects.remove(randomObject);
			nwResourceSpawns.remove(randomSpawn);
		}
	}

	public void createConnectorRoomSW(int x, int y) {
		int chunkIndex = Random.get(0, 3);
		int xChunk = corrupted ? corruptedConnectorChunksArray[chunkIndex][0] : regularConnectorChunksArray[chunkIndex][0];
		int yChunk = corrupted ? corruptedConnectorChunksArray[chunkIndex][1] : regularConnectorChunksArray[chunkIndex][1];
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 2; j++) {
				southWestChunks.add(new DynamicChunk(xChunk + i, yChunk + j, 1).pos(x + i, y + j, 1));
			}
	}

	public void createSpawnRoom(int x, int y) {
		int xChunk = corrupted ? 246 : 238;
		int yChunk = 708;
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 2; j++) {
				southWestChunks.add(new DynamicChunk(xChunk + i, yChunk + j, 1).pos(x + i, y + j, 1));
			}
	}

	public void createBossRoom(int x, int y) {
		int xChunk = corrupted ? 246 : 238;
		int yChunk = 710;
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 2; j++) {
				southWestChunks.add(new DynamicChunk(xChunk + i, yChunk + j, 1).pos(x + i, y + j, 1));
			}
	}

	public void buildSWCorner(int rotation, int posX, int posY) {
		int randomRoom = Random.get(0, 3);
		int xChunk = corrupted ? corruptedCornerChunksArrayNW[randomRoom][0] : regularCornerChunksArrayNW[randomRoom][0];
		int yChunk = corrupted ? corruptedCornerChunksArrayNW[randomRoom][1] : regularCornerChunksArrayNW[randomRoom][1];
		List<DynamicChunk> roomToRotatechunks = new ArrayList<>();
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 2; y++)
				roomToRotatechunks.add(new DynamicChunk(xChunk - x, yChunk - y, 1).pos(posX + x, posY + y, 1));
		}
		for (DynamicChunk chunks : roomToRotatechunks) {
			chunks.rotate(rotation);
			southWestChunks.add(chunks.pos(chunks.pointX, chunks.pointY, chunks.pointZ));
		}
		roomToRotatechunks.clear();
	}

	public void buildNWCorner(int rotation, int posX, int posY) {
		int randomRoom = Random.get(0, 3);
		int xChunk = corrupted ? corruptedCornerChunksArray[randomRoom][0] : regularCornerChunksArray[randomRoom][0];
		int yChunk = corrupted ? corruptedCornerChunksArray[randomRoom][1] : regularCornerChunksArray[randomRoom][1];
		List<DynamicChunk> roomToRotatechunks = new ArrayList<>();
		for (int x = 1; x < 3; x++) {
			for (int y = 1; y < 3; y++) {
				double degreesToRadiants = Math.PI / 180;
				double rotateAmount = 90 * degreesToRadiants;
				double _sin = Math.sin(rotateAmount);
				double _cos = Math.cos(rotateAmount);
				int newX = (int) (1 - Math.abs(Math.round(x * _cos - y * _sin) + 1));
				int newY = (int) (1 - (2 - Math.round(y * _cos + x * _sin)));
				roomToRotatechunks.add(new DynamicChunk(xChunk + x - 1, yChunk + y - 1, 1).pos(newX + posX, newY + posY, 1));
			}
		}
		for (DynamicChunk chunks : roomToRotatechunks) {
			chunks.rotate(rotation);
			northWestChunks.add(chunks.pos(chunks.pointX, chunks.pointY, chunks.pointZ));
		}
		roomToRotatechunks.clear();
	}

	public void buildNECorner(int rotation, int posX, int posY, int degrees) {
		for (int i = 0; i < 1; i++) {
			int randomRoom = Random.get(0, 3);
			int xChunk = corrupted ? corruptedCornerChunksArray[randomRoom][0] : regularCornerChunksArray[randomRoom][0];;
			int yChunk = corrupted ? corruptedCornerChunksArray[randomRoom][1] : regularCornerChunksArray[randomRoom][1];
			List<DynamicChunk> roomToRotatechunks = new ArrayList<>();
			for (int x = -1; x < 2; x += 2) {
				for (int y = -1; y < 2; y += 2) {
					double degreesToRadiants = Math.PI / 180;
					double rotateAmount = degrees * degreesToRadiants;
					double _sin = Math.sin(rotateAmount);
					double _cos = Math.cos(rotateAmount);

					int tempX = x == -1 ? 0 : x;
					int tempY = y == -1 ? 0 : y;

					int newX = (int) Math.round(x * _cos - y * _sin) == -1 ? 0 : 1;
					int newY = (int) Math.round(y * _cos + x * _sin) == -1 ? 0 : 1;

					roomToRotatechunks.add(new DynamicChunk(xChunk + tempX, yChunk + tempY, 1).pos(newX + posX, newY + posY, 1));
				}
			}
			for (DynamicChunk chunks : roomToRotatechunks) {
				chunks.rotate(rotation);
				northEastChunks.add(chunks.pos(chunks.pointX, chunks.pointY, chunks.pointZ));
			}
			roomToRotatechunks.clear();
		}
	}

	public void buildSECorner(int rotation, int posX, int posY, int degrees) {
		for (int i = 0; i < 1; i++) {
			int randomRoom = Random.get(0, 3);
			int xChunk = corrupted ? corruptedCornerChunksArray[randomRoom][0] : regularCornerChunksArray[randomRoom][0];
			int yChunk = corrupted ? corruptedCornerChunksArray[randomRoom][1] : regularCornerChunksArray[randomRoom][1];
			List<DynamicChunk> roomToRotatechunks = new ArrayList<>();
			for (int x = -1; x < 2; x += 2) {
				for (int y = -1; y < 2; y += 2) {
					double degreesToRadiants = Math.PI / 180;
					double rotateAmount = degrees * degreesToRadiants;
					double _sin = Math.sin(rotateAmount);
					double _cos = Math.cos(rotateAmount);

					int tempX = x == -1 ? 0 : x;
					int tempY = y == -1 ? 0 : y;

					int newX = (int) Math.round(x * _cos - y * _sin) == -1 ? 0 : 1;
					int newY = (int) Math.round(y * _cos + x * _sin) == -1 ? 0 : 1;

					roomToRotatechunks.add(new DynamicChunk(xChunk + tempX, yChunk + tempY, 1).pos(newX + posX, newY + posY, 1));
				}
			}
			for (DynamicChunk chunks : roomToRotatechunks) {
				chunks.rotate(rotation);
				southEastChunks.add(chunks.pos(chunks.pointX, chunks.pointY, chunks.pointZ));
			}
			roomToRotatechunks.clear();
		}
	}

	public void buildSWWalls(int rotation, int posX, int posY, int degrees) {
		for (int i = 0; i < 1; i++) {
			int randomRoom = Random.get(0, 3);
			int xChunk = corrupted ? corruptedEdgeChunksArraySW[randomRoom][0] : regularEdgeChunksArraySW[randomRoom][0];
			int yChunk = corrupted ? corruptedEdgeChunksArraySW[randomRoom][1] : regularEdgeChunksArraySW[randomRoom][1];
			List<DynamicChunk> roomToRotatechunks = new ArrayList<>();
			for (int x = -1; x < 2; x += 2) {
				for (int y = -1; y < 2; y += 2) {
					double degreesToRadiants = Math.PI / 180;
					double rotateAmount = degrees * degreesToRadiants;
					double _sin = Math.sin(rotateAmount);
					double _cos = Math.cos(rotateAmount);

					int tempX = x == -1 ? 0 : x;
					int tempY = y == -1 ? 0 : y;

					int newX = (int) Math.round(x * _cos - y * _sin) == -1 ? 0 : 1;
					int newY = (int) Math.round(y * _cos + x * _sin) == -1 ? 0 : 1;

					roomToRotatechunks.add(new DynamicChunk(xChunk + tempX, yChunk + tempY, 1).pos(newX + posX, newY + posY, 1));
				}
			}
			for (DynamicChunk chunks : roomToRotatechunks) {
				chunks.rotate(rotation);
				southWestChunks.add(chunks.pos(chunks.pointX, chunks.pointY, chunks.pointZ));
			}
			roomToRotatechunks.clear();
		}
	}

	public void buildNWWalls(int rotation, int posX, int posY, int degrees) {
		for (int i = 0; i < 1; i++) {
			int randomRoom = Random.get(0, 3);
			int xChunk = corrupted ? corruptedEdgeChunksArraySW[randomRoom][0] : regularEdgeChunksArraySW[randomRoom][0];
			int yChunk = corrupted ? corruptedEdgeChunksArraySW[randomRoom][1] : regularEdgeChunksArraySW[randomRoom][1];
			List<DynamicChunk> roomToRotatechunks = new ArrayList<>();
			for (int x = -1; x < 2; x += 2) {
				for (int y = -1; y < 2; y += 2) {
					double degreesToRadiants = Math.PI / 180;
					double rotateAmount = degrees * degreesToRadiants;
					double _sin = Math.sin(rotateAmount);
					double _cos = Math.cos(rotateAmount);

					int tempX = x == -1 ? 0 : x;
					int tempY = y == -1 ? 0 : y;

					int newX = (int) Math.round(x * _cos - y * _sin) == -1 ? 0 : 1;
					int newY = (int) Math.round(y * _cos + x * _sin) == -1 ? 0 : 1;

					roomToRotatechunks.add(new DynamicChunk(xChunk + tempX, yChunk + tempY, 1).pos(newX + posX, newY + posY, 1));
				}
			}
			for (DynamicChunk chunks : roomToRotatechunks) {
				chunks.rotate(rotation);
				northWestChunks.add(chunks.pos(chunks.pointX, chunks.pointY, chunks.pointZ));
			}
			roomToRotatechunks.clear();
		}
	}

	public void buildSEWalls(int rotation, int posX, int posY, int degrees) {
		for (int i = 0; i < 1; i++) {
			int randomRoom = Random.get(0, 3);
			int xChunk = corrupted ? corruptedEdgeChunksArraySW[randomRoom][0] : regularEdgeChunksArraySW[randomRoom][0];
			int yChunk = corrupted ? corruptedEdgeChunksArraySW[randomRoom][1] : regularEdgeChunksArraySW[randomRoom][1];
			List<DynamicChunk> roomToRotatechunks = new ArrayList<>();
			for (int x = -1; x < 2; x += 2) {
				for (int y = -1; y < 2; y += 2) {
					double degreesToRadiants = Math.PI / 180;
					double rotateAmount = degrees * degreesToRadiants;
					double _sin = Math.sin(rotateAmount);
					double _cos = Math.cos(rotateAmount);

					int tempX = x == -1 ? 0 : x;
					int tempY = y == -1 ? 0 : y;

					int newX = (int) Math.round(x * _cos - y * _sin) == -1 ? 0 : 1;
					int newY = (int) Math.round(y * _cos + x * _sin) == -1 ? 0 : 1;

					roomToRotatechunks.add(new DynamicChunk(xChunk + tempX, yChunk + tempY, 1).pos(newX + posX, newY + posY, 1));
				}
			}
			for (DynamicChunk chunks : roomToRotatechunks) {
				chunks.rotate(rotation);
				southEastChunks.add(chunks.pos(chunks.pointX, chunks.pointY, chunks.pointZ));
			}
			roomToRotatechunks.clear();
		}
	}

	public void buildMap(boolean leveFive, boolean corrupted) throws DynamicMap.DynamicMapBuildException {
		int pointX = 0;
		int pointY = 0;

		if (!leveFive) {
			for (int i = 0; i < 26; i++) {
				if (pointX == 0 && pointY == 0 && !leveFive) {
					buildSWCorner(2, pointX, pointY);
					buildNWCorner(3, pointX, pointY);
					buildSECorner(1, pointX, pointY, 270);
					buildNECorner(0, pointX, pointY, 0);
				} else if (pointX == 0 && pointY == 8 && !leveFive)// NW corner
					buildSWCorner(1, pointX, pointY);
				else if (pointX == 8 && pointY == 8 && !leveFive)// NE corner
					buildSWCorner(0, pointX, pointY);
				else if (pointX == 0 && pointY > 0 && pointY < 8 && !leveFive) {
					buildSWWalls(3, pointX, pointY, 90);
					buildSEWalls(1, pointX, pointY, 270);
				} else if (pointX > 0 && pointX < 8 && pointY == 0 && !leveFive) {
					buildSWWalls(2, pointX, pointY, 180);
					buildNWWalls(0, pointX, pointY, 0);
				} else if (pointX == 4 && pointY == 4)
					createSpawnRoom(pointX, pointY);
				else if (pointX == 4 && pointY == 6)
					createBossRoom(pointX, pointY);
				else if (!leveFive)
					createConnectorRoomSW(pointX, pointY);

				pointY += 2;
				if (pointY == 8) {
					pointY = 0;
					pointX += 2;
				}
			}
			map = new DynamicMap().build(southWestChunks).buildNw(northWestChunks).buildSe(southEastChunks)
					.buildNe(northEastChunks);
		} else {
			int xChunk = corrupted ? 246 : 238;
			int yChunk = 708;
			for (int i = 0; i < 2; i++)
				for (int j = 0; j < 2; j++) {
					southWestChunks.add(new DynamicChunk(xChunk + i, yChunk + j, 1).pos(4 + i, 4 + j, 1));
				}

			int xChunk2 = corrupted ? 246 : 238;
			int yChunk2 = 710;
			for (int i = 0; i < 2; i++)
				for (int j = 0; j < 2; j++) {
					southWestChunks.add(new DynamicChunk(xChunk2 + i, yChunk2 + j, 1).pos(4 + i, 6 + j, 1));
				}
			map = new DynamicMap().build(southWestChunks);
		}

	}

	public void rollTier1MonsterDrop(Player player, NPC npc) {
		GroundItem loot;
		tier1MonstersKilled++;
		int amountOfDrops = Random.get(1, 3);
		List<Integer[]> commonDrops = new ArrayList<>();
		commonDrops.add(new Integer[] {corrupted ? 23835 : 23875, 1});
		commonDrops.add(new Integer[] {corrupted ? 23834 : 23871, 1});
		commonDrops.add(new Integer[] {23872, Random.get(1, 3)});

		for (int i = 0; i < amountOfDrops; i++) {
			int lootIndex = Random.get(0, commonDrops.size() - 1);
			loot = new GroundItem(new Item(commonDrops.get(lootIndex)[0], commonDrops.get(lootIndex)[1]));
			loot.owner(player);
			loot.position(npc.getPosition());
			loot.spawn();
			if (loot.id == (corrupted ? 23834 : 23871))
				weaponFrameObtained = true;
			commonDrops.remove(lootIndex);
		}
		if (tier1MonstersKilled >= 3 && !weaponFrameObtained) {
			loot = new GroundItem(new Item(corrupted ? 23834 : 23871));
			loot.owner(player);
			loot.position(npc.getPosition());
			loot.spawn();
			weaponFrameObtained = true;
		}
		if (Random.get(0, 20) == 0) {
			loot = new GroundItem(new Item(corrupted ? 23858 : 23904));
			loot.owner(player);
			loot.spawn();
		}
		loot = new GroundItem(new Item(corrupted ? 23824 : 23866, Random.get(10, 37)));
		loot.owner(player);
		loot.position(npc.getPosition());
		loot.spawn();
	}

	public void rollTier2MonsterDrop(Player player, NPC npc) {
		GroundItem loot;
		int amountOfDrops = Random.get(1, 2);
		List<Integer[]> commonDrops = new ArrayList<>();
		commonDrops.add(new Integer[] {corrupted ? 23834 : 23871, 1});
		commonDrops.add(new Integer[] {23872, 4});

		for (int i = 0; i < amountOfDrops; i++) {
			int lootIndex = Random.get(0, commonDrops.size() - 1);
			loot = new GroundItem(new Item(commonDrops.get(lootIndex)[0], commonDrops.get(lootIndex)[1]));
			loot.owner(player);
			loot.position(npc.getPosition());
			loot.spawn();
			if (loot.id == (corrupted ? 23834 : 23871))
				weaponFrameObtained = true;
			commonDrops.remove(lootIndex);
		}
		if (!weaponFrameObtained) {
			loot = new GroundItem(new Item(corrupted ? 23834 : 23871));
			loot.owner(player);
			loot.position(npc.getPosition());
			loot.spawn();
			weaponFrameObtained = true;
		}
		if (Random.get(0, 20) == 0) {
			loot = new GroundItem(new Item(corrupted ? 23858 : 23904));
			loot.owner(player);
			loot.position(npc.getPosition());
			loot.spawn();
		}
		loot = new GroundItem(new Item(corrupted ? 23824 : 23866, Random.get(50, 105)));
		loot.owner(player);
		loot.position(npc.getPosition());
		loot.spawn();
	}

	public void rollDemiBossDrop(Player player, NPC npc) {
		GroundItem loot;
		tier1MonstersKilled++;

		loot = new GroundItem(new Item(corrupted ? 23834 : 23871));
		loot.owner(player);
		loot.position(npc.getPosition());
		loot.spawn();
		weaponFrameObtained = true;
		int bearId = corrupted ? 9046 : 9032;
		int dragonId = corrupted ? 9047 : 9033;
		int darkBeastId = corrupted ? 9048 : 9034;
		if (npc.getId() == bearId) {
			if (Random.get(0, 1) == 0) {
				loot = new GroundItem(new Item(23872, 5));
				loot.owner(player);
				loot.position(npc.getPosition());
				loot.spawn();
			}
			if (Random.get(0, 10) == 0) {
				loot = new GroundItem(new Item(corrupted ? 23858 : 23904));
				loot.owner(player);
				loot.position(npc.getPosition());
				loot.spawn();
			}
			loot = new GroundItem(new Item(corrupted ? 23824 : 23866, Random.get(80, 130)));
			loot.owner(player);
			loot.position(npc.getPosition());
			loot.spawn();
			if (weaponPieces.isEmpty())
				return;
			if (weaponPieces.contains(corrupted ? 23831 : 23868)) {
				int index = 0;
				for (int i = 0; i < weaponPieces.size(); i++) {
					if (weaponPieces.get(i) == (corrupted ? 23831 : 23868))
						index = i;
				}
				loot = new GroundItem(new Item(corrupted ? 23831 : 23868));
				loot.owner(player);
				loot.position(npc.getPosition());
				loot.spawn();
				weaponPieces.remove(index);
			} else {
				int randomIndex = Random.get(0, weaponPieces.size() - 1);
				loot = new GroundItem(new Item(weaponPieces.get(randomIndex)));
				weaponPieces.remove(randomIndex);
				loot.owner(player);
				loot.position(npc.getPosition());
				loot.spawn();
			}
		} else if (npc.getId() == dragonId) {
			if (Random.get(0, 1) == 0) {
				loot = new GroundItem(new Item(23872, 5));
				loot.owner(player);
				loot.position(npc.getPosition());
				loot.spawn();
			}
			if (Random.get(0, 10) == 0) {
				loot = new GroundItem(new Item(corrupted ? 23858 : 23904));
				loot.owner(player);
				loot.position(npc.getPosition());
				loot.spawn();
			}
			loot = new GroundItem(new Item(corrupted ? 23824 : 23866, Random.get(80, 130)));
			loot.owner(player);
			loot.position(npc.getPosition());
			loot.spawn();
			if (weaponPieces.isEmpty())
				return;
			if (weaponPieces.contains(corrupted ? 23833 : 23870)) {
				int index = 0;
				for (int i = 0; i < weaponPieces.size(); i++) {
					if (weaponPieces.get(i) == (corrupted ? 23833 : 23870))
						index = i;
				}
				loot = new GroundItem(new Item(corrupted ? 23833 : 23870));
				loot.owner(player);
				loot.position(npc.getPosition());
				loot.spawn();
				weaponPieces.remove(index);
			} else {
				int randomIndex = Random.get(0, weaponPieces.size() - 1);
				loot = new GroundItem(new Item(weaponPieces.get(randomIndex)));
				weaponPieces.remove(randomIndex);
				loot.owner(player);
				loot.position(npc.getPosition());
				loot.spawn();
			}
		} else if (npc.getId() == darkBeastId) {
			if (Random.get(0, 1) == 0) {
				loot = new GroundItem(new Item(23872, 5));
				loot.owner(player);
				loot.position(npc.getPosition());
				loot.spawn();
			}
			if (Random.get(0, 10) == 0) {
				loot = new GroundItem(new Item(corrupted ? 23858 : 23904));
				loot.owner(player);
				loot.position(npc.getPosition());
				loot.spawn();
			}
			loot = new GroundItem(new Item(corrupted ? 23824 : 23866, Random.get(80, 130)));
			loot.owner(player);
			loot.position(npc.getPosition());
			loot.spawn();
			if (weaponPieces.isEmpty())
				return;
			if (weaponPieces.contains(corrupted ? 23832 : 23869)) {
				int index = 0;
				for (int i = 0; i < weaponPieces.size(); i++) {
					if (weaponPieces.get(i) == (corrupted ? 23832 : 23869))
						index = i;
				}
				loot = new GroundItem(new Item(corrupted ? 23832 : 23869));
				loot.owner(player);
				loot.position(npc.getPosition());
				loot.spawn();
				weaponPieces.remove(index);
			} else {
				int randomIndex = Random.get(0, weaponPieces.size() - 1);
				loot = new GroundItem(new Item(weaponPieces.get(randomIndex)));
				weaponPieces.remove(randomIndex);
				loot.owner(player);
				loot.position(npc.getPosition());
				loot.spawn();
			}
		}

	}

	public void pickLinumTirinum(GameObject object) {
		player.startEvent(event -> {
			if (player.getInventory().getFreeSlots() < 3) {
				player.sendMessage("You need at least 3 inventory spaces to pick this plant.");
				return;
			}
			player.animate(2282, 2);
			event.delay(2);
			object.setId(corrupted ? 35976 : 36073);
			player.getInventory().add(new Item(23836, 3));
		});
	}

	public void pickGrymRoot(GameObject object) {
		player.startEvent(event -> {
			if (player.getInventory().getFreeSlots() < 1) {
				player.sendMessage("You need at least 1 inventory space to pick this herb.");
				return;
			}
			player.animate(2282, 2);
			player.getStats().addXp(StatType.Farming, 1, true);
			event.delay(2);
			object.setId(corrupted ? 35974 : 36071);
			player.getInventory().add(corrupted ? new Item(23835) : new Item(23875));
		});
	}

	public void mineCrystalRock(GameObject object) {
		player.startEvent(event -> {
			while (true) {
				int depleteChance = Random.get(0, 2);
				if (player.getInventory().getFreeSlots() < 1) {
					player.sendMessage("You need at least 1 inventory space to mine this.");
					return;
				}
				if (corrupted) {
					if (!player.getInventory().contains(23822) &&
							player.getEquipment().getDef(Equipment.SLOT_WEAPON).id != 23822) {
						player.sendMessage("You need a corrupted pickaxe to mine this!");
						return;
					}
				} else {
					if (!player.getInventory().contains(23863) &&
							player.getEquipment().getDef(Equipment.SLOT_WEAPON).id != 23863) {
						player.sendMessage("You need a crystal pickaxe to mine this!");
						return;
					}
				}
				player.animate(8350);
				event.delay(2);
				player.getInventory().add(corrupted ? new Item(23837) : new Item(23877));
				player.sendFilteredMessage("You mine some ore.");
				event.delay(1);
				if (depleteChance == 0) {
					object.setId(corrupted ? 35968 : 36065);
					break;
				}
			}
		});
	}

	public void chopPhrenBark(GameObject object) {
		player.startEvent(event -> {
			while (true) {
				int depleteChance = Random.get(0, 2);
				if (player.getInventory().getFreeSlots() < 1) {
					player.sendMessage("You need at least 1 inventory space to chop this.");
					return;
				}
				if (corrupted) {
					if (!player.getInventory().contains(23821) &&
							player.getEquipment().getDef(Equipment.SLOT_WEAPON).id != 23821) {
						player.sendMessage("You need a corrupted axe to chop this!");
						return;
					}
				} else {
					if (!player.getInventory().contains(23862) &&
							player.getEquipment().getDef(Equipment.SLOT_WEAPON).id != 23862) {
						player.sendMessage("You need a crystal axe to chop this!");
						return;
					}
				}
				player.animate(8323);
				event.delay(2);
				player.getInventory().add(corrupted ? new Item(23838) : new Item(23878));
				player.sendFilteredMessage("You chop some phren roots.");
				event.delay(1);
				if (depleteChance == 0) {
					object.setId(corrupted ? 35970 : 36067);
					break;
				}
			}
		});
	}

	public void fishPaddlefish(GameObject object) {
		player.startEvent(event -> {
			while (true) {
				int depleteChance = Random.get(0, 3);
				if (player.getInventory().getFreeSlots() < 1) {
					player.sendMessage("You need at least 1 inventory space to fish here.");
					return;
				}
				if (corrupted) {
					if (!player.getInventory().contains(23823) &&
							player.getEquipment().getDef(Equipment.SLOT_WEAPON).id != 23823) {
						player.sendMessage("You need a corrupted harpoon to fish here!");
						return;
					}
				} else {
					if (!player.getInventory().contains(23864) &&
							player.getEquipment().getDef(Equipment.SLOT_WEAPON).id != 23864) {
						player.sendMessage("You need a crystal harpoon to fish here!");
						return;
					}
				}

				player.animate(8336);
				event.delay(2);
				player.getInventory().add(new Item(23872));
				player.sendFilteredMessage("You fish a raw paddlefish.");
				event.delay(1);
				if (depleteChance == 0) {
					object.setId(corrupted ? 35972 : 36069);
					break;
				}
			}
		});
	}

	public void leaveDialogue(Player player) {
		displayTimer = 0;
		// player.closeInterface(ToplevelComponent.OVERLAY);
		player.dialogue(
				new OptionsDialogue("Are you sure you want to leave?",
						new Option("Yes.", this::leave),
						new Option("No.", player::closeDialogue)));
	}

	public void toolStorage(Player player) {
		displayTimer = 0;
		// player.closeInterface(ToplevelComponent.OVERLAY);
		String sceptre = corrupted ? "Corrupted sceptre." : "Crystal sceptre.";
		String axe = corrupted ? "Corrupted axe." : "Crystal axe.";
		String pickaxe = corrupted ? "Corrupted pickaxe." : "Crystal pickaxe.";
		String harpoon = corrupted ? "Corrupted harpoon." : "Crystal harpoon.";
		player.dialogue(new OptionsDialogue("Pick a tool to take.",
				new Option(sceptre, () -> player.getInventory().add(corrupted ? 23820 : 23861, 1)),
				new Option(axe, () -> player.getInventory().add(corrupted ? 23821 : 23862, 1)),
				new Option(pickaxe, () -> player.getInventory().add(corrupted ? 23822 : 23863, 1)),
				new Option(harpoon, () -> player.getInventory().add(corrupted ? 23823 : 23864, 1)),
				new Option("Pestle and mortar.", () -> player.getInventory().add(233, 1))));
	}

	public void singingBowlSkillDialogue(Player player, boolean corrupted) {
		displayTimer = 0;
		// player.closeInterface(ToplevelComponent.OVERLAY);
		int crystalSeed = corrupted ? 23858 : 23904;
		int vial = 23839;
		int crystalHelm = corrupted ? 23840 : 23886;
		int crystalBody = corrupted ? 23843 : 23889;
		int crystalLegs = corrupted ? 23846 : 23892;
		int crystalHalberd = corrupted ? 23849 : 23895;
		int crystalBow = corrupted ? 23855 : 23901;
		int crystalStaff = corrupted ? 23852 : 23898;
		int escapeCrystal = corrupted ? 25959 : 25961;
		int paddlefish = corrupted ? 25958 : 25960;

		if (player.getInventory().contains(crystalHalberd) || player.getEquipment().contains(crystalHalberd))
			crystalHalberd = corrupted ? 23850 : 23896;
		else if (player.getInventory().contains(corrupted ? 23850 : 23896)
				|| player.getEquipment().contains(corrupted ? 23850 : 23896))
			crystalHalberd = corrupted ? 23851 : 23897;

		if (player.getInventory().contains(crystalStaff) || player.getEquipment().contains(crystalStaff))
			crystalStaff = corrupted ? 23853 : 23899;
		else if (player.getInventory().contains(corrupted ? 23853 : 23899)
				|| player.getEquipment().contains(corrupted ? 23853 : 23899))
			crystalStaff = corrupted ? 23854 : 23900;

		if (player.getInventory().contains(crystalBow) || player.getEquipment().contains(crystalBow))
			crystalBow = corrupted ? 23856 : 23902;
		else if (player.getInventory().contains(corrupted ? 23856 : 23902)
				|| player.getEquipment().contains(corrupted ? 23856 : 23902))
			crystalBow = corrupted ? 23857 : 23903;

		if (player.getInventory().contains(crystalHelm) || player.getEquipment().contains(crystalHelm))
			crystalHelm = corrupted ? 23841 : 23887;
		else if (player.getInventory().contains(corrupted ? 23841 : 23887)
				|| player.getEquipment().contains(corrupted ? 23841 : 23887))
			crystalHelm = corrupted ? 23842 : 23888;

		if (player.getInventory().contains(crystalBody) || player.getEquipment().contains(crystalBody))
			crystalBody = corrupted ? 23844 : 23890;
		else if (player.getInventory().contains(corrupted ? 23844 : 23890)
				|| player.getEquipment().contains(corrupted ? 23844 : 23890))
			crystalBody = corrupted ? 23845 : 23891;

		if (player.getInventory().contains(crystalLegs) || player.getEquipment().contains(crystalLegs))
			crystalLegs = corrupted ? 23847 : 23893;
		else if (player.getInventory().contains(corrupted ? 23847 : 23893)
				|| player.getEquipment().contains(corrupted ? 23847 : 23893))
			crystalLegs = corrupted ? 23848 : 23894;

		int finalCrystalHelm = crystalHelm;
		int finalCrystalBody = crystalBody;
		int finalCrystalLegs = crystalLegs;
		int finalCrystalHalberd = crystalHalberd;
		int finalCrystalBow = crystalBow;
		int finalCrystalStaff = crystalStaff;
		SkillDialogue.make(player,
				new SkillItem(crystalSeed).addAction((p, amount, event) -> createItem(player,
						corrupted ? SingingBowlRecipes.CORRUPTED_TELEPORT_CRYSTAL
								: SingingBowlRecipes.CRYSTAL_TELEPORT_CRYSTAL)),
				new SkillItem(vial).addAction(
						(p, amount, event) -> createItem(player, corrupted ? SingingBowlRecipes.VIAL : SingingBowlRecipes.VIAL2)),
				new SkillItem(crystalHelm).addAction((p, amount, event) -> createItem(player, getItemRecipe(finalCrystalHelm))),
				new SkillItem(crystalBody).addAction((p, amount, event) -> createItem(player, getItemRecipe(finalCrystalBody))),
				new SkillItem(crystalLegs).addAction((p, amount, event) -> createItem(player, getItemRecipe(finalCrystalLegs))),
				new SkillItem(crystalHalberd)
						.addAction((p, amount, event) -> createItem(player, getItemRecipe(finalCrystalHalberd))),
				new SkillItem(crystalBow).addAction((p, amount, event) -> createItem(player, getItemRecipe(finalCrystalBow))),
				new SkillItem(crystalStaff)
						.addAction((p, amount, event) -> createItem(player, getItemRecipe(finalCrystalStaff))),
				new SkillItem(paddlefish).addAction((p, amount, event) -> createItem(player, getItemRecipe(paddlefish))),
				new SkillItem(escapeCrystal).addAction((p, amount, event) -> createItem(player, getItemRecipe(escapeCrystal))));
	}

	public static void createPotion(Player player, int amount, int dust) {
		Item unfinished = player.getInventory().findItem(23881);
		Item secondary = player.getInventory().findItem(dust);

		if (unfinished != null && secondary != null) {
			final int maxAmount = Math.min(amount, Math.min(player.getInventory().getAmount(23881), secondary.count()));
			player.startEvent(event -> {
				int made = 0;
				while (made++ < maxAmount) {
					if (!player.getInventory().hasId(23881))
						return;
					player.getInventory().remove(unfinished.getId(), 1);
					player.getInventory().remove(secondary.getId(), 10);
					player.getInventory().add(ItemID.EGNIOL_POTION_3, 1);
					player.animate(363);
					player.sendFilteredMessage("You mix an egniol potion.");
					player.getStats().addXp(StatType.Herblore, 1, false);
					event.delay(1);
				}
			});
		}
	}

	private SingingBowlRecipes getItemRecipe(int itemId) {

		for (SingingBowlRecipes recipe : SingingBowlRecipes.VALUES) {
			if (itemId == recipe.getItemToMake().getId())
				return recipe;
		}
		return null;
	}
	// TODO: REMOVE GAUNTLET ITEMS ON LOGIN TO PREVENT SMUGGLE, CHECK ON LEAVE TOO

	private int getIndexNumber(int id, List<Item> list) {
		for (int i = 0; i < list.size(); i++) {
			if (id == list.get(i).getId())
				return i;
		}
		return 0;
	}

	private void createItem(Player player, SingingBowlRecipes recipe) {
		List<Item> ingredients = new ArrayList<>(Arrays.asList(recipe.getIngredients()));
		if (recipe.getItemToUpgrade() != null) {
			if (player.getEquipment().contains(recipe.getItemToUpgrade())) {
				ingredients.remove(getIndexNumber(recipe.getItemToUpgrade().getId(), ingredients));
			}
		}

		if (player.getInventory().containsAll(false, ingredients)) {
			if (recipe.getItemToUpgrade() != null) {
				if (player.getEquipment().contains(recipe.getItemToUpgrade()))
					player.getEquipment().get(getEquipmentSlotByItem(player, recipe.getItemToUpgrade()))
							.setId(recipe.getItemToMake().getId());
				else
					player.getInventory().get(getInventorySlotByItem(player, recipe.getItemToUpgrade()))
							.setId(recipe.getItemToMake().getId());

				player.sendMessage("You have upgraded your " + recipe.getItemToUpgrade().getDef().name + ".");
				player.getInventory().removeAll(false, ingredients);
			} else {
				player.sendMessage("You have created a " + recipe.getItemToMake().getDef().name + ".");
				player.getInventory().removeAll(false, ingredients);
				player.getInventory().add(recipe.getItemToMake());
			}

		} else {
			player.sendMessage("You don't have all the ingredients to make this item.");
		}
	}

	private int getEquipmentSlotByItem(Player player, Item item) {
		for (Item item2 : player.getEquipment().getItems()) {
			if (item2 == null)
				continue;
			if (item.getId() == item2.getId()) {
				return item2.getSlot();
			}
		}
		return 0;
	}

	private int getInventorySlotByItem(Player player, Item item) {
		for (Item item2 : player.getInventory().getItems()) {
			if (item2 == null)
				continue;
			if (item.getId() == item2.getId()) {
				return item2.getSlot();
			}
		}
		return 0;
	}



	public void leave(Player player) {
		this.leave(player, new Position(3031, 6121, 1));
	}

	public void leave(Player player, Position target) {
		player.getMovement().teleport(target);
	}

	// whenever, by any means player leaves the main gauntlet map,
	// it should trigger the leaving logic, removing items and destroying the map
	public void onLeftMap(Player player) {
		if (!inGauntlet) {
			return;
		}
		onLeave(player);
	}

	public void onLeave(Player player) {
		this.destroy();

		// Clear player states
		player.teleportListener = null;
		player.logoutListener = null;
		removeFakeContainer(player);
		player.getCombat().restore();

		// Reset gauntlet state

		// Clean up gauntlet items from inventory and equipment
		List<Integer> gauntletItems = Arrays.asList(
				23820, 23821, 23822, 23823, 23824, 23830, 23831, 23832,
				23833, 23837, 23840, 23841, 23842, 23843, 23844, 23845,
				23846, 23847, 23848, 23849, 23850, 23851, 23852, 23853,
				23854, 23855, 23856, 23857, 23858, 25958, 25959, 23861,
				23862, 23863, 23864, 23866, 23867, 23868, 23869, 23870,
				23877, 23886, 23887, 23888, 23889, 23890, 23891, 23892,
				23893, 23894, 23895, 23896, 23897, 23898, 23899, 23900,
				23901, 23902, 23903, 23904);

		// Reset player state and position
		inGauntlet = false;

		player.gauntlet = null;
		player.deathStartListener = null;
		player.deathEndListener = null;

		// Clear gauntlet items from inventory
		for (int i = 27; i >= 0; i--) {
			Item item = player.getInventory().get(i);
			if (item == null)
				continue;
			if (gauntletItems.contains(item.getId()))
				player.getInventory().remove(item);
		}

		// Clear gauntlet items from equipment
		for (int i = 13; i >= 0; i--) {
			Item item = player.getEquipment().get(i);
			if (item == null)
				continue;
			if (gauntletItems.contains(item.getId()))
				player.getEquipment().remove(item);
		}

		// Send leave message
		player.sendMessage("You have left the gauntlet early and were not rewarded for your efforts.");
	}

	// TODO: LOOK AT TEMPORARY INVENTORY AND EQUIPMENT.
	private void destroy() {
		if (this.bossTileHandler != null) {
			this.bossTileHandler.deinit();
		}
		if (map != null) {
			if (map.getNpcs() != null) {
				map.getNpcs().forEach(NPC::remove);
			}
			map.destroy();
			map = null;
		}

		southWestChunks.clear();
		southEastChunks.clear();
		northEastChunks.clear();
		northWestChunks.clear();
		gauntletResources.clear();
	}


	public void sendUpdates() {
		if (inGauntlet) {
			if (player.gauntletBossTimer.remaining() <= 0 && !bossFightStarted) {
				startBossFight(true);
			}

			if (bossFightStarted) {
				timeTilNewTiles--;
				if (timeTilNewTiles < 1) {
					activateDangerousTiles(player);
				}
			}
		}
	}

	private int displayTimer = 0;

	public void displayTimer(Player player) {
		if (displayTimer == 50) {
			displayTimer = 0;
			// player.openInterface(ToplevelComponent.OVERLAY, 1092);
		}
		displayTimer++;
		float timeRemainingSeconds = Server.toSeconds(player.gauntletBossTimer.remaining());
		int minutes = (int) (timeRemainingSeconds / 60);
		int seconds = (int) (timeRemainingSeconds % 60);
		String secondsString = seconds < 10 ? "0" + seconds : "" + seconds;
		if (seconds == 300 && !fiveMinuteWarning) {
			fiveMinuteWarning = true;
			player.sendMessage(
					Color.PURPLE.wrap("You have five minutes before you'll be automatically teleported into the boss room."));
		}
		if (seconds == 60 && !oneMinuteWarning) {
			oneMinuteWarning = true;
			player.sendMessage(
					Color.PURPLE.wrap("You have one minute before you'll be automatically teleported into the boss room."));
		}
		player.getPacketSender().sendString(1092, 0, "<shad=000000>Time Left: " + minutes + ":" + secondsString);
	}

	private void startBossFight(boolean timerEnder) {
		// player.closeInterface(ToplevelComponent.OVERLAY);
		activateDangerousTiles(player);
		bossFightStarted = true;
		if (timerEnder)
			player.getMovement().teleport(map.swRegion.baseX + 40, map.swRegion.baseY + 50, 1);
		player.gauntletBossTimer.delaySeconds(0);
		// lootTimer.stop(player, -1);
		boss.getCombat().setTarget(player);
		if (corrupted)
			player.corruptedHunllefTimer = new ActivityTimer();
		else
			player.crystallineHunllefTimer = new ActivityTimer();

	}

	public void displayCrystalSingingRecipes() {
		String replacement = corrupted ? "Corrupted" : "Crystal";
		String replacement2 = corrupted ? "Corrupted" : "Crystalline";

		player.getPacketSender().sendString(1091, 11, "<shad=00000>10 " + replacement + " Shards</shad>");
		player.getPacketSender().sendString(1091, 15, "<shad=00000>40 " + replacement + " Shards</shad>");

		player.getPacketSender().sendString(1091, 18, "<shad=00000>Basic " + replacement + " Halberd</shad>");
		player.getPacketSender().sendString(1091, 19, "<shad=00000>20 " + replacement + " Shards & 1 Weapon Frame</shad>");
		player.getPacketSender().sendString(1091, 22, "<shad=00000>Basic " + replacement + " Bow</shad>");
		player.getPacketSender().sendString(1091, 23, "<shad=00000>20 " + replacement + " Shards & 1 Weapon Frame</shad>");
		player.getPacketSender().sendString(1091, 26, "<shad=00000>Basic " + replacement + " Staff</shad>");
		player.getPacketSender().sendString(1091, 27, "<shad=00000>20 " + replacement + " Shards & 1 Weapon Frame</shad>");
		player.getPacketSender().sendString(1091, 30, "<shad=00000>Basic " + replacement + " Helm</shad>");
		player.getPacketSender().sendString(1091, 31, "<shad=00000>40 " + replacement + " Shards, 1 " + replacement
				+ " Ore, 1 Phren Bark &<br><shad=00000> 1 Linum Tirinum</br></shad>");
		player.getPacketSender().sendString(1091, 34, "<shad=00000>Basic " + replacement + " Body</shad>");
		player.getPacketSender().sendString(1091, 35, "<shad=00000>40 " + replacement + " Shards, 1 " + replacement
				+ " Ore, 1 Phren Bark &<br><shad=00000> 1 Linum Tirinum</br></shad>");
		player.getPacketSender().sendString(1091, 38, "<shad=00000>Basic " + replacement + " Legs</shad>");
		player.getPacketSender().sendString(1091, 39, "<shad=00000>40 " + replacement + " Shards, 1 " + replacement
				+ " Ore, 1 Phren Bark &<br><shad=00000> 1 Linum Tirinum</br></shad>");

		player.getPacketSender().sendString(1091, 42, "<shad=00000>Attuned " + replacement + " Halberd</shad>");
		player.getPacketSender().sendString(1091, 43,
				"<shad=00000>60 " + replacement + " Shards & 1 Basic " + replacement + " Halberd</shad>");
		player.getPacketSender().sendString(1091, 46, "<shad=00000>Attuned " + replacement + " Bow</shad>");
		player.getPacketSender().sendString(1091, 47,
				"<shad=00000>60 " + replacement + " Shards & 1 Basic " + replacement + " Bow</shad>");
		player.getPacketSender().sendString(1091, 50, "<shad=00000>Attuned " + replacement + " Staff</shad>");
		player.getPacketSender().sendString(1091, 51,
				"<shad=00000>60 " + replacement + " Shards & 1 Basic " + replacement + " Staff</shad>");
		player.getPacketSender().sendString(1091, 54, "<shad=00000>Attuned " + replacement + " Helm</shad>");
		player.getPacketSender().sendString(1091, 55, "<shad=00000>60 " + replacement + " Shards, 1 Basic " + replacement
				+ " Helm, 2 " + replacement + " Ore,<br><shad=00000> 1 Phren Bark & 1 Linum Tirinum</br></shad>");
		player.getPacketSender().sendString(1091, 58, "<shad=00000>Attuned " + replacement + " Body</shad>");
		player.getPacketSender().sendString(1091, 59, "<shad=00000>60 " + replacement + " Shards, 1 Basic " + replacement
				+ " Body, 2 " + replacement + " Ore,<br><shad=00000> 1 Phren Bark & 1 Linum Tirinum</br></shad>");
		player.getPacketSender().sendString(1091, 62, "<shad=00000>Attuned " + replacement + " Legs</shad>");
		player.getPacketSender().sendString(1091, 63, "<shad=00000>60 " + replacement + " Shards, 1 Basic " + replacement
				+ " Legs, 2 " + replacement + " Ore,<br><shad=00000> 1 Phren Bark & 1 Linum Tirinum</br></shad>");

		player.getPacketSender().sendString(1091, 66, "<shad=00000>Perfected " + replacement + " Halberd</shad>");
		player.getPacketSender().sendString(1091, 67,
				"<shad=00000>1 Attuned " + replacement + " Halberd & 1 " + replacement + " Spike</shad>");
		player.getPacketSender().sendString(1091, 70, "<shad=00000>Perfected " + replacement + " Bow</shad>");
		player.getPacketSender().sendString(1091, 71,
				"<shad=00000>1 Attuned " + replacement + " Bow & 1 " + replacement2 + " Bowstring</shad>");
		player.getPacketSender().sendString(1091, 74, "<shad=00000>Perfected " + replacement + " Staff</shad>");
		player.getPacketSender().sendString(1091, 75,
				"<shad=00000>1 Attuned " + replacement + " Staff & 1 " + replacement + " Orb</shad>");
		player.getPacketSender().sendString(1091, 78, "<shad=00000>Perfected " + replacement + " Helm</shad>");
		player.getPacketSender().sendString(1091, 79, "<shad=00000>80 " + replacement + " Shards, 1 Attuned " + replacement
				+ " Helm, 2 " + replacement + " Ore,<br><shad=00000> 2 Phren Bark & 1 Linum Tirinum</br></shad>");
		player.getPacketSender().sendString(1091, 82, "<shad=00000>Perfected " + replacement + " Body</shad>");
		player.getPacketSender().sendString(1091, 83, "<shad=00000>80 " + replacement + " Shards, 1 Attuned " + replacement
				+ " Body, 2 " + replacement + " Ore,<br><shad=00000> 2 Phren Bark & 1 Linum Tirinum</br></shad>");
		player.getPacketSender().sendString(1091, 86, "<shad=00000>Perfected " + replacement + " Legs</shad>");
		player.getPacketSender().sendString(1091, 87, "<shad=00000>80 " + replacement + " Shards, 1 Attuned " + replacement
				+ " Legs, 2 " + replacement + " Ore,<br><shad=00000> 2 Phren Bark & 1 Linum Tirinum</br></shad>");

		player.openInterface(ToplevelComponent.MAINMODAL, 1091);
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 8, 1000,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				8,
				1000,
				new Item(23839));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 13, 1001,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				13,
				1001,
				new Item(corrupted ? 23858 : 23904));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 17, 1002,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				17,
				1002,
				new Item(corrupted ? 23849 : 23895));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 21, 1003,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				21,
				1003,
				new Item(corrupted ? 23855 : 23901));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 25, 1004,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				25,
				1004,
				new Item(corrupted ? 23852 : 23898));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 29, 1005,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				29,
				1005,
				new Item(corrupted ? 23840 : 23886));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 33, 1006,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				33,
				1006,
				new Item(corrupted ? 23843 : 23889));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 37, 1007,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				37,
				1007,
				new Item(corrupted ? 23846 : 23892));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 41, 1008,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				41,
				1008,
				new Item(corrupted ? 23850 : 23896));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 45, 1009,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				45,
				1009,
				new Item(corrupted ? 23856 : 23902));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 49, 1010,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				49,
				1010,
				new Item(corrupted ? 23853 : 23899));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 53, 1011,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				53,
				1011,
				new Item(corrupted ? 23841 : 23887));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 57, 1012,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				57,
				1012,
				new Item(corrupted ? 23844 : 23890));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 61, 1013,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				61,
				1013,
				new Item(corrupted ? 23847 : 23893));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 65, 1014,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				65,
				1014,
				new Item(corrupted ? 23851 : 23897));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 69, 1015,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				69,
				1015,
				new Item(corrupted ? 23857 : 23903));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 73, 1016,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				73,
				1016,
				new Item(corrupted ? 23854 : 23900));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 77, 1017,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				77,
				1017,
				new Item(corrupted ? 23842 : 23888));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 81, 1018,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				81,
				1018,
				new Item(corrupted ? 23845 : 23891));
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1091 << 16 | 85, 1019,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				85,
				1019,
				new Item(corrupted ? 23848 : 23894));
	}

	static List<Integer> gauntletItems = Arrays.asList(
			ItemID.CORRUPTED_BODY_ATTUNED,
			ItemID.CORRUPTED_BOW_ATTUNED,
			ItemID.CORRUPTED_HALBERD_ATTUNED,
			ItemID.CORRUPTED_HELM_ATTUNED,
			ItemID.CORRUPTED_LEGS_ATTUNED,
			ItemID.CORRUPTED_STAFF_ATTUNED,
			ItemID.CRYSTAL_BODY_ATTUNED,
			ItemID.CRYSTAL_BOW_ATTUNED,
			ItemID.CRYSTAL_HALBERD_ATTUNED,
			ItemID.CRYSTAL_HELM_ATTUNED,
			ItemID.CRYSTAL_LEGS_ATTUNED,
			ItemID.CRYSTAL_STAFF_ATTUNED,
			ItemID.CORRUPTED_BOW_ATTUNED,
			ItemID.CORRUPTED_HALBERD_ATTUNED,
			ItemID.CORRUPTED_STAFF_ATTUNED,
			ItemID.CORRUPTED_HELM_ATTUNED,
			ItemID.CORRUPTED_BODY_ATTUNED,
			ItemID.CORRUPTED_LEGS_ATTUNED,
			ItemID.CRYSTAL_BOW_ATTUNED,
			ItemID.CRYSTAL_HALBERD_ATTUNED,
			ItemID.CRYSTAL_STAFF_ATTUNED,
			ItemID.CRYSTAL_HELM_ATTUNED,
			ItemID.CRYSTAL_BODY_ATTUNED,
			ItemID.CRYSTAL_LEGS_ATTUNED,
			ItemID.CRYSTAL_BODY_BASIC,
			ItemID.CRYSTAL_BOW_BASIC,
			ItemID.CRYSTAL_HALBERD_BASIC,
			ItemID.CRYSTAL_HELM_BASIC,
			ItemID.CRYSTAL_LEGS_BASIC,
			ItemID.CRYSTAL_STAFF_BASIC,
			ItemID.CORRUPTED_BODY_BASIC,
			ItemID.CORRUPTED_BOW_BASIC,
			ItemID.CORRUPTED_HALBERD_BASIC,
			ItemID.CORRUPTED_HELM_BASIC,
			ItemID.CORRUPTED_LEGS_BASIC,
			ItemID.CORRUPTED_STAFF_BASIC,
			ItemID.CRYSTAL_ORE,
			ItemID.CORRUPTED_ORE,
			ItemID.CRYSTAL_SHARDS,
			ItemID.CORRUPTED_SHARDS,
			ItemID.CRYSTAL_BODY_PERFECTED,
			ItemID.CRYSTAL_BOW_PERFECTED,
			ItemID.CRYSTAL_HALBERD_PERFECTED,
			ItemID.CRYSTAL_HELM_PERFECTED,
			ItemID.CRYSTAL_LEGS_PERFECTED,
			ItemID.CRYSTAL_STAFF_PERFECTED,
			ItemID.CORRUPTED_BODY_PERFECTED,
			ItemID.CORRUPTED_BOW_PERFECTED,
			ItemID.CORRUPTED_HALBERD_PERFECTED,
			ItemID.CORRUPTED_HELM_PERFECTED,
			ItemID.CORRUPTED_LEGS_PERFECTED,
			ItemID.CORRUPTED_STAFF_PERFECTED,
			ItemID.LINUM_TIRINUM,
			ItemID.GRYM_LEAF,
			ItemID.PHREN_BARK,
			ItemID.PADDLEFISH,
			ItemID.EGNIOL_POTION_3,
			ItemID.EGNIOL_POTION_2,
			ItemID.EGNIOL_POTION_1,
			ItemID.GRYM_POTION_UNF,
			ItemID.LINUM_TIRINUM_23876,
			ItemID.GRYM_LEAF_23875,
			ItemID.PHREN_BARK_23878,
			ItemID.CORRUPTED_TELEPORT_CRYSTAL,
			ItemID.TELEPORT_CRYSTAL,
			ItemID.CORRUPTED_BOWSTRING,
			ItemID.CORRUPTED_ORB,
			ItemID.CORRUPTED_SCEPTRE,
			ItemID.CRYSTAL_SCEPTRE,
			ItemID.CRYSTAL_ORB,
			ItemID.CRYSTALLINE_BOWSTRING,
			ItemID.CRYSTAL_SPIKE,
			ItemID.CRYSTAL_SPIKE,
			ItemID.RAW_PADDLEFISH,
			ItemID.CORRUPTED_PICKAXE,
			ItemID.CORRUPTED_AXE,
			ItemID.CORRUPTED_HARPOON,
			ItemID.CRYSTAL_PICKAXE,
			ItemID.CRYSTAL_AXE,
			ItemID.CRYSTAL_HARPOON,
			ItemID.WEAPON_FRAME,
			ItemID.WEAPON_FRAME_23871

	);

	public static void confiscateItems(Player player) {
		for (Item invItem : player.getInventory().getItems()) {
			if (invItem != null && gauntletItems.contains(invItem.getId()))
				invItem.remove();
		}
		for (Item equipItem : player.getEquipment().getItems()) {
			if (equipItem != null && gauntletItems.contains(equipItem.getId()))
				equipItem.remove();
		}
		for (Item bankItem : player.getBank().getItems()) {
			if (bankItem != null && gauntletItems.contains(bankItem.getId()))
				bankItem.remove();
		}
	}

	private static void mix(Player player, boolean corrupted) {
		player.getInventory().remove(corrupted ? 23830 : 23867, 1);
		player.getInventory().remove(23881, 1);
		player.getInventory().add(23884, 1);
		player.getStats().addXp(StatType.Herblore, 10, true);
		player.animate(363);
	}

	static SkillItem filledVials = new SkillItem(23880).addAction((player, amount, event) -> {
		amount = player.getInventory().getAmount(23839);
		while (amount-- > 0) {
			player.getInventory().remove(23839, 1);
			player.getInventory().add(23880, 1);
			event.delay(1);
		}
	});

	static void fillVials(Player player) {
		if (!player.getInventory().contains(23839)) {
			player.sendMessage("You have no vials to fill.");
			return;
		}
		if (player.getInventory().hasMultiple(23839)) {
			SkillDialogue.make(player, filledVials);
			return;
		}
		player.getInventory().remove(23839, 1);
		player.getInventory().add(23880, 1);
	}

	private static void register(boolean corrupted) {
		SkillItem item = new SkillItem(23884).addAction((player, amount, event) -> {
			amount = player.getInventory().getAmount(23881);
			while (amount-- > 0) {
				Item primaryItem = player.getInventory().findItem(23881);
				if (primaryItem == null)
					return;
				List<Item> secondaryItems = player.getInventory().collectOneOfEach(corrupted ? 23830 : 23867);
				if (secondaryItems == null)
					return;
				mix(player, corrupted);
				event.delay(1);
			}
		});
		ItemItemAction.register(23881, corrupted ? 23830 : 23867, (player, primary, secondary) -> {
			if (player.getInventory().hasMultiple(corrupted ? 23830 : 23867)) {
				SkillDialogue.make(player, item);
				return;
			}
			List<Item> secondaries = player.getInventory().collectOneOfEach(corrupted ? 23830 : 23867);
			if (secondaries == null) {
				player.sendMessage("You need more ingredients to make this potion.");
				return;
			}
			mix(player, corrupted);
		});
	}

	// TODO: UPGRADE ARMOUR IF WEARING

	public void displayEgniolPotionsBook(Player player) {
		List<String> text = new LinkedList<>();
		String replace = corrupted ? "corrupted" : "crystal";
		text.add("Those hoping to survive the Gauntlet will need to");
		text.add("take advantage of the Grym roots found within the dungeon.");
		text.add("The leaves that grow on these roots can be used to create");
		text.add("Egniol potions, which are able to restore both energy and prayer.");
		text.add("");
		text.add("To create an Egniol potion, follow these steps:");
		text.add("Fill a vial with water.");
		text.add("Add a Grym leaf to the vial.");
		text.add("Crush ten " + replace + " shards.");
		text.add("Add the " + replace + " dust to the vial.");
		text.add("");

		player.sendScroll("Egniol Potions", text.toArray(new String[0]));
	}

	static void mixVial(Player player, boolean corrupted) {
		player.getInventory().remove(corrupted ? 23835 : 23875, 1);
		player.getInventory().remove(23880, 1);
		player.getInventory().add(23881, 1);
		player.animate(363);
	}

	public void cookFish() {
		if (!player.getInventory().contains(23872)) {
			player.sendMessage("You have no fish to cook.");
			return;
		}
		player.getInventory().remove(23872, 1);
		player.getInventory().add(23874, 1);
		player.animate(896);
	}

	public void startDeathEvent() {
		World.startEvent(e -> {
			inGauntlet = false;
			player.getCombat().setDead(true);
			player.clearHits();
			player.lock(LockType.FULL_NULLIFY_DAMAGE);
			e.delay(1);
			player.animate(836);
			e.delay(4);
			player.resetAnimation();
			// handleDeath();
		});
	}

	private void handleDeath() {
		World.startEvent(e -> {
			e.delay(2);
			player.getCombat().restore();
			// player.closeInterface(ToplevelComponent.OVERLAY);
			killedBoss = false;
			player.teleportListener = null;
			removeFakeContainer(player);
			player.getMovement().teleport(CHEST_LOCATION);
			player.unlock();
			player.gauntlet = null;
			player.deathStartListener = null;
			player.deathEndListener = null;
			player.sendMessage("You have been defeated!");
			if (corrupted)
				player.corruptedGauntletChestToBeLooted = true;
			else
				player.crystallineGauntletChestToBeLooted = true;
			VarPlayerRepository.GAUNTLET_REWARD.set(player, 1);
			player.getCombat().setDead(false);
		});

	}

	public void passBossBarrier() {
		if (player.getPosition().getX() == map.swRegion.baseX + 39 &&
				player.getPosition().getY() == map.swRegion.baseY + 48)
			player.getMovement().teleport(map.swRegion.baseX + 39, map.swRegion.baseY + 50, 1);
		else if (player.getPosition().getX() == map.swRegion.baseX + 40 &&
				player.getPosition().getY() == map.swRegion.baseY + 48)
			player.getMovement().teleport(map.swRegion.baseX + 40, map.swRegion.baseY + 50, 1);

		else if (player.getPosition().getX() == map.swRegion.baseX + 47 &&
				player.getPosition().getY() == map.swRegion.baseY + 55)
			player.getMovement().teleport(map.swRegion.baseX + 45, map.swRegion.baseY + 55, 1);
		else if (player.getPosition().getX() == map.swRegion.baseX + 47 &&
				player.getPosition().getY() == map.swRegion.baseY + 56)
			player.getMovement().teleport(map.swRegion.baseX + 45, map.swRegion.baseY + 56, 1);

		else if (player.getPosition().getX() == map.swRegion.baseX + 40 &&
				player.getPosition().getY() == map.swRegion.baseY + 63)
			player.getMovement().teleport(map.swRegion.baseX + 40, map.swRegion.baseY + 61, 1);
		else if (player.getPosition().getX() == map.swRegion.baseX + 39 &&
				player.getPosition().getY() == map.swRegion.baseY + 63)
			player.getMovement().teleport(map.swRegion.baseX + 39, map.swRegion.baseY + 61, 1);

		else if (player.getPosition().getX() == map.swRegion.baseX + 32 &&
				player.getPosition().getY() == map.swRegion.baseY + 56)
			player.getMovement().teleport(map.swRegion.baseX + 34, map.swRegion.baseY + 56, 1);
		else if (player.getPosition().getX() == map.swRegion.baseX + 32 &&
				player.getPosition().getY() == map.swRegion.baseY + 55)
			player.getMovement().teleport(map.swRegion.baseX + 34, map.swRegion.baseY + 55, 1);

		startBossFight(true);
	}

	{
		ObjectAction.register(35966, 1, (player, obj) -> {
			singingBowlSkillDialogue(player, true);
		});
		ObjectAction.register(36063, 1, (player, obj) -> {
			singingBowlSkillDialogue(player, false);
		});

	}

	public static void register() {

		ItemAction.registerInventory(23858, "activate", ((player1, item) -> {
			player1.gauntlet.activateTeleportCrystal();
		}));
		ItemAction.registerInventory(23904, "activate", ((player1, item) -> {
			player1.gauntlet.activateTeleportCrystal();
		}));
		ItemItemAction.register(23830, 23881, (player, before, pestleAndMortar) -> player.startEvent(event -> {
			register(true);
		}));
		ItemItemAction.register(23867, 23881, (player, before, pestleAndMortar) -> player.startEvent(event -> {
			register(false);
		}));
		ItemObjectAction.register(23839, 35981, ((player1, item1, obj) -> {
			fillVials(player1);
		}));
		ObjectAction.register(35981, "Fill-from", (player, obj) -> {
			fillVials(player);
		});
		ObjectAction.register(36078, "Fill-from", (player, obj) -> {
			fillVials(player);
		});
		SkillItem skillItem = new SkillItem(23881).addAction((player, amount, event) -> {
			amount = player.getInventory().getAmount(23835);
			while (amount-- > 0) {
				Item herbItem = player.getInventory().findItem(23835);
				if (herbItem == null)
					return;
				Item vialItem = player.getInventory().findItem(23880);
				if (vialItem == null)
					return;
				mixVial(player, true);
				event.delay(1);
			}
		});
		SkillItem skillItem2 = new SkillItem(23881).addAction((player, amount, event) -> {
			amount = player.getInventory().getAmount(23875);
			while (amount-- > 0) {
				Item herbItem = player.getInventory().findItem(23875);
				if (herbItem == null)
					return;
				Item vialItem = player.getInventory().findItem(23880);
				if (vialItem == null)
					return;
				mixVial(player, true);
				event.delay(1);
			}
		});
		SkillItem skillItem3 = new SkillItem(23884).addAction((player, amount, event) -> {
			amount = player.getInventory().getAmount(23881);
			while (amount-- > 0) {
				Item herbItem = player.getInventory().findItem(23881);
				if (herbItem == null)
					return;
				Item vialItem = player.getInventory().findItem(23830);
				if (vialItem == null)
					return;
				createPotion(player, amount, 23830);
				event.delay(1);
			}
		});
		SkillItem skillItem4 = new SkillItem(23884).addAction((player, amount, event) -> {
			amount = player.getInventory().getAmount(23881);
			while (amount-- > 0) {
				Item herbItem = player.getInventory().findItem(23881);
				if (herbItem == null)
					return;
				Item vialItem = player.getInventory().findItem(23867);
				if (vialItem == null)
					return;
				createPotion(player, amount, 23867);
				event.delay(1);
			}
		});
		ItemItemAction.register(23881, 23830, (player, unfinished, dust) -> {
			if (player.getInventory().hasMultiple(23881, 23830)) {
				SkillDialogue.make(player, skillItem3);
				return;
			}
			createPotion(player, 1, 23830);
		});
		ItemItemAction.register(23881, 23867, (player, unfinished, dust) -> {
			if (player.getInventory().hasMultiple(23881, 23867)) {
				SkillDialogue.make(player, skillItem4);
				return;
			}
			createPotion(player, 1, 23867);
		});
		ItemItemAction.register(23835, 23881, (player, herbItem, vialItem) -> {
			if (player.getInventory().hasMultiple(23835, 23881)) {
				SkillDialogue.make(player, skillItem);
				return;
			}
			mixVial(player, true);
		});
		ItemItemAction.register(23867, 23881, (player, unfinished, dust) -> {
			if (player.getInventory().hasMultiple(23881, 23867)) {
				SkillDialogue.make(player, skillItem4);
				return;
			}
			createPotion(player, 1, 23867);
		});
		ItemObjectAction.register(23839, 35981, (player, item, obj) -> fillVials(player));
		ItemItemAction.register(23880, 23835, (player, herbItem, vialItem) -> {
			if (player.getInventory().hasMultiple(23835, 23880)) {
				SkillDialogue.make(player, skillItem);
				return;
			}
			mixVial(player, true);
		});
		ItemItemAction.register(23875, 23880, (player, herbItem, vialItem) -> {
			if (player.getInventory().hasMultiple(23875, 23880)) {
				SkillDialogue.make(player, skillItem2);
				return;
			}
			mixVial(player, false);
		});
	}
}
