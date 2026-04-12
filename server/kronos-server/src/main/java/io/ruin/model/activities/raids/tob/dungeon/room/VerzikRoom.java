package io.ruin.model.activities.raids.tob.dungeon.room;

import com.google.common.collect.Lists;
import io.ruin.api.utils.Random;
import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.PerkTaskHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.RaidingRestorations;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.activities.raids.tob.dungeon.RoomType;
import io.ruin.model.activities.raids.tob.dungeon.boss.verzik.VerzikCombat;
import io.ruin.model.activities.raids.tob.dungeon.boss.verzik.VerzikPillar;
import io.ruin.model.activities.raids.tob.party.TheatreParty;
import io.ruin.model.activities.raids.tob.party.TheatrePartyManager;
import io.ruin.model.content.HomeHandler;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Broadcast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author NuLL on 28/12/2021
 *         https://www.rune-server.ee/members/null1001/
 * @project Drako
 */
public class VerzikRoom extends TheatreRoom {

	public NPC verzik;

	Object throne;

	boolean lootCalculated = false;
	int purpleChestNumber = 0;

	boolean verzikTimerStarted = false;

	public VerzikRoom(TheatreParty party) throws DynamicMapBuildException {
		super(party);
	}

	public static final LootTable UNIQUES = new LootTable().addTable(1,
			new LootItem(ItemID.AVERNIC_DEFENDER_HILT, 1, 11).broadcast(Broadcast.GLOBAL),
			new LootItem(ItemID.GHRAZI_RAPIER, 1, 6).broadcast(Broadcast.GLOBAL),
			new LootItem(ItemID.SANGUINESTI_STAFF_UNCHARGED, 1, 6).broadcast(Broadcast.GLOBAL),
			new LootItem(ItemID.JUSTICIAR_CHESTGUARD, 1, 6).broadcast(Broadcast.GLOBAL),
			new LootItem(ItemID.JUSTICIAR_FACEGUARD, 1, 6).broadcast(Broadcast.GLOBAL),
			new LootItem(ItemID.JUSTICIAR_LEGGUARDS, 1, 6).broadcast(Broadcast.GLOBAL),
			new LootItem(ItemID.SCYTHE_OF_VITUR_UNCHARGED, 1, 1).broadcast(Broadcast.GLOBAL));

	private static final LootTable LOOT_TABLE = new LootTable().addTable(1,
			new LootItem(ItemID.DEATH_RUNE, 500, 600, 1),
			new LootItem(ItemID.BLOOD_RUNE, 500, 600, 1),
			new LootItem(ItemID.SWAMP_TAR, 500, 600, 1),
			new LootItem(ItemID.COAL + 1, 500, 600, 1),
			new LootItem(ItemID.GOLD_ORE + 1, 300, 360, 1),
			new LootItem(ItemID.MOLTEN_GLASS + 1, 200, 240, 1),
			new LootItem(ItemID.ADAMANTITE_ORE + 1, 130, 156, 1),
			new LootItem(ItemID.RUNITE_ORE + 1, 60, 72, 1),
			new LootItem(ItemID.WINE_OF_ZAMORAK + 1, 50, 60, 1),
			new LootItem(ItemID.POTATO_CACTUS + 1, 50, 60, 1),
			new LootItem(ItemID.GRIMY_CADANTINE + 1, 50, 60, 1),
			new LootItem(ItemID.GRIMY_AVANTOE + 1, 40, 48, 1),
			new LootItem(ItemID.GRIMY_TOADFLAX + 1, 37, 44, 1),
			new LootItem(ItemID.GRIMY_KWUARM + 1, 36, 43, 1),
			new LootItem(ItemID.GRIMY_IRIT_LEAF + 1, 34, 40, 1),
			new LootItem(ItemID.GRIMY_RANARR_WEED + 1, 30, 36, 1),
			new LootItem(ItemID.GRIMY_SNAPDRAGON + 1, 27, 32, 1),
			new LootItem(ItemID.GRIMY_LANTADYME + 1, 26, 31, 1),
			new LootItem(ItemID.GRIMY_DWARF_WEED + 1, 24, 28, 1),
			new LootItem(ItemID.GRIMY_TORSTOL + 1, 20, 24, 1),
			new LootItem(ItemID.BATTLESTAFF + 1, 15, 18, 1),
			new LootItem(ItemID.MAHOGANY_SEED, 10, 12, 1),
			new LootItem(ItemID.RUNE_BATTLEAXE + 1, 4, 1),
			new LootItem(ItemID.RUNE_PLATEBODY + 1, 4, 1),
			new LootItem(ItemID.RUNE_CHAINBODY + 1, 4, 1),
			new LootItem(ItemID.PALM_TREE_SEED, 3, 1),
			new LootItem(ItemID.YEW_SEED, 3, 1),
			new LootItem(ItemID.MAGIC_SEED, 3, 1));

	@Override
	public void onLoad() {
		try {
			build(12611, 1);
		} catch (DynamicMap.DynamicMapBuildException e) {
			throw new RuntimeException(e);
		}

		verzik = new NPC(8369).spawn(convertX(3166), convertY(4323), 0, Direction.SOUTH, 0);
		System.out.println("Verzik spawned at " + convertX(3166) + ", " + convertY(4323));
		addNpc(verzik);
		// GameObject obj = GameObject.spawn(32737, convertX(3167), convertY(4324), 0,
		// 10, 0);

		var bossCombat = ((VerzikCombat) verzik.getCombat());
		bossCombat.bloodNylos.stream().filter(Objects::nonNull).forEach(this::addNpc);
		bossCombat.bombNylos.stream().filter(Objects::nonNull).forEach(this::addNpc);
		bossCombat.bombNyloList.stream().filter(Objects::nonNull)
				.map(npc -> npc.npc)
				.forEach(this::addNpc);
		bossCombat.purpleNylos.stream().filter(Objects::nonNull)
				.map(npc -> npc.npc)
				.forEach(this::addNpc);
		bossCombat.webList.stream().filter(Objects::nonNull)
				.map(npc -> npc.webNPC)
				.forEach(this::addNpc);
		bossCombat.tornados.stream().filter(Objects::nonNull)
				.map(npc -> npc.tornado)
				.forEach(this::addNpc);
		bossCombat.pillarList.stream().filter(Objects::nonNull)
				.map(VerzikPillar::getPillarNPC)
				.forEach(this::addNpc);

		verzik.deathEndListener = (DeathListener.SimpleKiller) killer -> {
			verzik.startEvent(event -> {
				if (verzik.getId() == 8374) {
					verzik.animate(8128);
					verzik.transform(8375);
					event.delay(6);
					verzik.remove();
					party.getUsers().forEach(user -> {
						TheatrePartyManager.instance().forUsername(user).ifPresent(p -> {
							p.insideRaid = false;
							p.theatreOfBloodStage = 6;
							PerkTaskHandler.handleMonsterKill(p, "tob");
							p.sendMessage("You beat Theatre of blood! Congratulations! go claim your treasure!");
							p.theatreOfBloodKills.increment(p);
							DailyTasks.handleTaskDecrement(p, "tob");
							p.tobreward = false;
						});
					});
					verzik.getCombat().setAllowRespawn(false);
				}
			});
		};
	}

	@Override
	public void registerObjects() {
		ObjectAction.register(32738, "enter", ((player, obj) -> {
			AtomicBoolean canEnter = new AtomicBoolean(true);
			player.localNpcs().forEach(npc -> {
				if (npc.getId() == 8369 || npc.getId() == 8370 || npc.getId() == 8371 || npc.getId() == 8372
						|| npc.getId() == 8373 || npc.getId() == 8374) {
					player.sendMessage("You must defeat Verzik Vitur before entering the treasure room.");
					canEnter.set(false);
				}
			});
			if (!canEnter.get()) {
				return;
			}
			if (party.verzikTimerEnd == 0)
				party.verzikTimerEnd = party.verzikTimer.stop(player, 0);
			if (party.overallTimerEnd == 0)
				party.overallTimerEnd = party.overallTimer.stop(player, 0);
			TheatrePartyManager.instance().getPartyForPlayer(player.getName()).ifPresent(party -> {
				party.getUsers().forEach(user -> {
					Player p = World.getPlayer(user);
					if (p != null) {
						switch (party.getUsers().size()) {
							case 1:
								System.out.println("party over time: " + party.overallTimerEnd);
								p.theatreSoloBestTime = ActivityTimer.set(p, p.theatreSoloBestTime, party.overallTimerEnd);
								break;
							case 2:
								p.theatreDuoBestTime = ActivityTimer.set(p, p.theatreDuoBestTime, party.overallTimerEnd);
								break;
							case 3:
								p.theatreTrioBestTime = ActivityTimer.set(p, p.theatreTrioBestTime, party.overallTimerEnd);
								break;
							case 4:
								p.theatreFourManBestTime = ActivityTimer.set(p, p.theatreFourManBestTime, party.overallTimerEnd);
								break;
							case 5:
								p.theatreFiveManBestTime = ActivityTimer.set(p, p.theatreFiveManBestTime, party.overallTimerEnd);
								break;
						}
						if (ActivityTimer.timeInSeconds(p.theatreFiveManBestTime) < 480 && party.getUsers().size() == 5) {
							Objects.requireNonNull(p.combatAchievementsList
									.get(p
											.getCombatAchievementIndexByOrdinal(CombatAchievements.THEATRE_FIVE_SCALE_SPEED_CHASER.ordinal()))
									.getCombatAchievement()).check(p.player);
						}
						if (ActivityTimer.timeInSeconds(p.theatreFiveManBestTime) < 420 && party.getUsers().size() == 5) {
							Objects.requireNonNull(p.combatAchievementsList
									.get(p
											.getCombatAchievementIndexByOrdinal(CombatAchievements.THEATRE_FIVE_SCALE_SPEED_RUNNER.ordinal()))
									.getCombatAchievement()).check(p.player);
						}
						if (ActivityTimer.timeInSeconds(p.theatreFourManBestTime) < 540 && party.getUsers().size() == 4) {
							Objects.requireNonNull(p.combatAchievementsList
									.get(p
											.getCombatAchievementIndexByOrdinal(CombatAchievements.THEATRE_FOUR_SCALE_SPEED_CHASER.ordinal()))
									.getCombatAchievement()).check(p.player);
						}
						if (ActivityTimer.timeInSeconds(p.theatreFourManBestTime) < 480 && party.getUsers().size() == 4) {
							Objects.requireNonNull(p.combatAchievementsList
									.get(p
											.getCombatAchievementIndexByOrdinal(CombatAchievements.THEATRE_FOUR_SCALE_SPEED_RUNNER.ordinal()))
									.getCombatAchievement()).check(p.player);
						}
						if (ActivityTimer.timeInSeconds(p.theatreTrioBestTime) < 600 && party.getUsers().size() == 3) {
							Objects.requireNonNull(p.combatAchievementsList
									.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.THEATRE_TRIO_SPEED_CHASER.ordinal()))
									.getCombatAchievement()).check(p.player);
						}
						if (ActivityTimer.timeInSeconds(p.theatreTrioBestTime) < 510 && party.getUsers().size() == 3) {
							Objects.requireNonNull(p.combatAchievementsList
									.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.THEATRE_TRIO_SPEED_RUNNER.ordinal()))
									.getCombatAchievement()).check(p.player);
						}
						if (ActivityTimer.timeInSeconds(p.theatreDuoBestTime) < 540 && party.getUsers().size() == 2) {
							Objects.requireNonNull(p.combatAchievementsList
									.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.THEATRE_DUO_SPEED_RUNNER.ordinal()))
									.getCombatAchievement()).check(p.player);
						}
					}
				});
				calculateLoot(player);
				party.getDungeon().enterRoom(player, RoomType.TREASURE);
			});
		}));
	}

	private static final StatType[] SCALED_STATS = { StatType.Defence, StatType.Attack, StatType.Strength, StatType.Magic,
			StatType.Ranged };

	private void scaleNPC(NPC npc) {
		int users = npc.getPosition().getRegion().players.size();
		if (npc.getCombat() == null) {
			return;
		}
		double factor;
		factor = 1.0 + (0.20 * users);
		if (factor != 0) {
			for (StatType type : SCALED_STATS) {
				if (type == StatType.Defence)
					factor = 1.1 + (0.20 * users);
				else {
					factor = 1.5 + (0.20 * users);
				}
				npc.getCombat().getStat(type).fixedLevel *= (int) factor;
				npc.getCombat().getStat(type).restore();
			}
		}
		scaleHP(npc);

	}

	public void scaleHP(NPC npc) {
		if (npc.getCombat() == null) {
			return;
		}
		double factor;

		factor = 1.0 + (0.6 * npc.getPosition().getRegion().players.size());
		if (factor != 0) {
			double newLevel = npc.getCombat().getStat(StatType.Hitpoints).fixedLevel * factor;
			npc.getCombat().getStat(StatType.Hitpoints).fixedLevel = (int) newLevel;
			npc.getCombat().getStat(StatType.Hitpoints).restore();
		}

	}

	@Override
	public void registerNpcs() {
		NPCAction.register(8369, "Quick-start", (player, npc) -> transformEvent(npc, player));
		NPCAction.register(8369, 1, (player, npc) -> {
			player.startEvent(e -> {
				player.dialogue(
						new NPCDialogue(8369, "Now that was quite the show! I haven't been that entertained in a long time."));
				e.waitForDialogue(player);
				player.dialogue(new NPCDialogue(8369,
						"Of course, you know I can't let you leave here alive. Time for your final performance..."));
				e.waitForDialogue(player);
				player.dialogue(
						new OptionsDialogue("Is your party ready to fight?",
								new Option("Yes", () -> transformEvent(npc, player)),
								new Option("No", player::closeDialogue)));
			});
		});
	}

	private void transformEvent(NPC npc, Player player) {
		World.startEvent(e -> {
			e.delay(4);
			if (!verzikTimerStarted && player.theatreOfBloodStage == 5) {
				verzikTimerStarted = true;
				party.verzikTimer = new ActivityTimer();
			}
			npc.transform(8370);
			scaleNPC(npc);
			npc.setIgnoreMulti(true);
			npc.getCombat().setAllowRespawn(false);
		});
	}

	private double getPetDonatorBoost(Player player) {
		return HomeHandler.getPetDonatorBoost(player);
	}

	private void calculateLoot(Player p) {
		p.inTob = false;
		p.theatreReward.clear();
		AtomicInteger purpleProbability = new AtomicInteger((party.deaths * 7) + 20);
		// boolean purpleDropping = Random.get(purpleProbability) == 0;
		int partySize = party.getUsers().size();
		purpleProbability.updateAndGet(v -> (int) (v + partySize * 2));
		AtomicBoolean purpleRewarded = new AtomicBoolean(false);
		AtomicInteger index = new AtomicInteger();
		if (p.tobDamageDealt == 0) {
			p.theatreReward.add(new Item(ItemID.CABBAGE));
			p.theatreReward.add(new Item(ItemID.MESSAGE_22475));
		} else {
			purpleProbability.updateAndGet(v -> (int) (v * p.getDifficulty().GetDropRate()));
			int chance = Random.get(purpleProbability.get());
			if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.DOUBLE_RAID_POINTS)) {
				chance /= 2;
			}
			if (p.getEquipment().get(Equipment.SLOT_RING) != null && AttributeExtensions
					.hasAttribute(p.getEquipment().get(Equipment.SLOT_RING), AttributeTypes.RAID_UNIQUE_CHARM)) {
				int level = AttributeExtensions.getCharges(AttributeTypes.RAID_UNIQUE_CHARM,
						p.getEquipment().get(Equipment.SLOT_RING));
				double multiplier = 1.0 - (level * 0.05);
				chance *= multiplier;
			}
			if (p.getPlayerPerkHandler().getActivePerks(p).contains(Perks.RAIDING_RESTORATIONS)) {
				int perkIndex = p.getPlayerPerkHandler().getActivePerkIndex(p, Perks.RAIDING_RESTORATIONS);
				RaidingRestorations c = (RaidingRestorations) p.getPlayerPerkHandler().getActivePerks(p).get(perkIndex)
						.getPerk(p);
				double multiplier = 1.0 + c.getLootChance();
				chance *= multiplier;
			}
			boolean purpleDropping = chance == 0;
			if (purpleDropping) {
				if (!purpleRewarded.get()) {
					TheatrePartyManager.instance().getPartyForPlayer(p.getName())
							.ifPresent(party -> party.getPurpleLootChests().add(p.tobChestId));
					p.theatreReward.add(UNIQUES.rollItem());
					purpleChestNumber = p.tobChestId;
					purpleRewarded.set(true);
				}
			}
		}
		index.getAndIncrement();
		int basePetChance = 200;
		if (p.getPlayerPerkHandler().getActivePerks(p).contains(Perks.THE_PET_HUNTER)) {
			int perkIndex = p.getPlayerPerkHandler().getActivePerkIndex(p, Perks.THE_PET_HUNTER);
			ThePetHunter c = (ThePetHunter) p.getPlayerPerkHandler().getActivePerks(p).get(perkIndex).getPerk(p);
			basePetChance *= c.getPetChanceBoost();
		}
		if (p.petDropBonus.isDelayed())
			basePetChance *= 0.8F;
		if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
			basePetChance *= 0.85F;

		basePetChance *= getPetDonatorBoost(p);

		if (Random.get(1, 25) == 1 && p.tobDamageDealt >= 100)
			p.theatreReward.add(new Item(ItemID.CLUE_SCROLL_ELITE));

		if (Random.get(1, basePetChance) == 1 && p.tobDamageDealt >= 100)
			p.theatreReward.add(new Item(ItemID.LIL_ZIK));

		boolean symbolRolled = false;
		if (!purpleRewarded.get() && p.tobDamageDealt != 0) {
			for (int i = 0; i < 2; i++) {
				if (Random.get(40) == 0 && !symbolRolled) {
					p.theatreReward.add(new Item(Random.get(1) == 0 ? 30545 : 30553, 1));
					symbolRolled = true;
				}
				p.theatreReward.add(LOOT_TABLE.rollItem());
			}
		}

	}

	private List<Player> getAllPlayers() {
		List<Player> players = new ArrayList<>();
		for (String user : party.getUsers()) {
			TheatrePartyManager.instance().forUsername(user).ifPresent(player -> {
				if (!players.contains(player))
					players.add(player);
			});
		}
		return players;
	}

	private List<Player> getPlayersByPerformance() {
		List<Player> playerPerformanceOrder = new ArrayList<>();
		party.getUsers()
				.forEach(user -> TheatrePartyManager.instance().forUsername(user)
						.ifPresent(playerPerformanceOrder::add));
		// Sort the list based on supplyChestDamage in descending order
		playerPerformanceOrder.sort(Comparator.comparingInt(Player::getSupplyChestDamage).reversed());

		return playerPerformanceOrder;
	}

	@Override
	public List<Position> getSpectatorSpots() {
		return Lists.newArrayList(
				Position.of(convertX(3157), convertY(4325), 0),
				Position.of(convertX(3159), convertY(4325), 0),
				Position.of(convertX(3161), convertY(4325), 0),
				Position.of(convertX(3175), convertY(4325), 0),
				Position.of(convertX(3177), convertY(4325), 0),
				Position.of(convertX(3179), convertY(4325), 0));
	}

	@Override
	public Position getEntrance() {
		return Position.of(3167, 4304, 0);
	}

	public static void register() {
		NPCAction.register(8390, "Teleport", (player, npc) -> {
			TheatrePartyManager.instance().getPartyForPlayer(player.getName()).ifPresent(party -> {
				if (party.verzikTimerEnd == 0)
					party.verzikTimerEnd = party.verzikTimer.stop(player, 0);
				if (party.overallTimerEnd == 0)
					party.overallTimerEnd = party.overallTimer.stop(player, 0);
				((VerzikRoom) party.dungeon.rooms.get(RoomType.VERZIK)).calculateLoot(player);
				party.getDungeon().enterRoom(player, RoomType.TREASURE);
			});
		});
	}

}
