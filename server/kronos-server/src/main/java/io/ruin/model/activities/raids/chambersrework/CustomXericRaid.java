package io.ruin.model.activities.raids.chambersrework;

import discord.webhooks.notifications.RareDropHook;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.PerkTaskHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.RaidingRestorations;
import io.ruin.model.activities.raids.xeric.chamber.combat.GreatOlm;
import io.ruin.model.activities.raids.xeric.party.Party;
import io.ruin.model.activities.tempevents.summerevent.SummerEvent;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.inter.utils.Unlock;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.dynamic.DynamicChunk;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.services.Loggers;
import io.ruin.utility.Broadcast;
import io.ruin.utility.Utils;
import lombok.Getter;
import org.json.JSONObject;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomXericRaid {
	DynamicMap olmRoom;
	@Getter
	DynamicMap floorOne;
	@Getter
	DynamicMap floorTwo;
	List<XericInvocations> invocations = new ArrayList<>();
	ActivityTimer timer;
	int invocationLevel = 0;
	public XericParty currentParty = null;
	int fakeScale = 4;

	NPC olm;

	List<NPC> npcs = new ArrayList<>();
	List<GameObject> objs = new ArrayList<>();

	int teamPoints = 0;

	public void addInvocation(XericInvocations invocation) {
		invocations.add(invocation);
		invocationLevel += invocation.invocationLevel;
	}

	public NPC spawnNPC(NPC npc, Position pos) {
		npc.spawn(pos);
		npc.setIgnoreMulti(true);
		scaleNPC(npc, currentParty.getMembers().size() + fakeScale);
		npcs.add(npc);
		return npc;
	}

	public GameObject spawnObject(int obj, Position pos, int dir) {
		GameObject object = GameObject.spawn(obj, pos.getX(), pos.getY(), 0, 10, dir);
		objs.add(object);
		return object;
	}

	public List<XericInvocations> getInvocations() {
		return invocations;
	}

	public void buildRaid(Player player, XericParty xericParty) {
		if (!player.getName().equalsIgnoreCase(xericParty.getLeader())) {
			player.sendMessage("Only the leader can start the raid.");
			return;
		}

		try {
			olmRoom = new DynamicMap().build(12889, 0);
		} catch (DynamicMap.DynamicMapBuildException e) {
			player.sendMessage("Unable to build dynamic map.");
			return;
		}

		teamPoints = 0;
		startTime = Instant.now();
		currentParty = xericParty;
		combatPointsFactor = 1.5 * (currentParty.getMembers().size() + fakeScale);

		if (!getInvocations().contains(XericInvocations.TEKTON) &&
			!getInvocations().contains(XericInvocations.VASA) && !getInvocations().contains(XericInvocations.SHAMANS)) {
			try {
				floorOne = getDefaultFloorOne();
				floorTwo = getDefaultFloorTwo();
			} catch (DynamicMap.DynamicMapBuildException e) {
				player.sendMessage("Unable to build dynamic map.");
				return;
			}
		}

		spawnNPCs();
		for (String name : currentParty.getMembers()) {
			Player p = World.getPlayer(name);
			if (p != null) {
				if (player.getPosition().getRegion().id != p.getPosition().regionId()) {
					p.sendMessage("Your raid leader has started the raid without you.");
					currentParty.removeMember(p.getName());
				} else {
					p.setCustomXericRaid(this);
					p.inCox = true;
					p.insideRaid = true;
					p.deathEndListener = (entity, killer, killHit) -> handleDeath(p);
					p.openInterface(ToplevelComponent.WILDERNESS_OVERLAY, Interface.RAID_POINTS);
					VarPlayerRepository.RAIDS_TIMER.set(p, 1);
					p.getMovement().teleport(floorOne.swRegion.baseX + 3, floorOne.swRegion.baseY + 4, 0);
					p.teleportListener = CustomXericRaid::allowTeleport;
					VarPlayerRepository.RAIDS_PERSONAL_POINTS.set(p, 0);
					VarPlayerRepository.RAIDS_PARTY_POINTS.set(p, 0);
				}
			}

		}
	}

	private static void confiscateItems(Player player) {
		for (Item item : player.getInventory().getItems()) {
			if (item != null && item.getDef() != null && item.getDef().coxItem && item.getId() > 20000) {
				item.remove();

			}
		}
		for (Item item : player.getBank().getItems()) {
			if (item != null && item.getDef() != null && item.getDef().coxItem && item.getId() > 20000) {
				item.remove();

			}
		}
		for (Item item : player.getPrivateRaidStorage().getItems()) {
			if (item != null && item.getDef() != null && item.getDef().coxItem && item.getId() > 20000) {
				item.remove();
			}
		}
	}

	public void sendTotalPoints() {
		if (!anyAliveSkeletalMages() && getMysticBlock() != null && !getMysticBlock().isRemoved()) {
			getMysticBlock().remove();
		}
		if (currentParty == null)
			return;
		currentParty.getMembers().forEach(name -> {
			Player p = World.getPlayer(name);
			if (p != null) {
				VarPlayerRepository.RAIDS_PARTY_POINTS.set(p, teamPoints);
				VarPlayerRepository.RAIDS_TIMER.set(p, (int) (Duration.between(startTime, Instant.now()).toMillis() / 600L));
			}
		});
	}

	public static boolean isRaiding(Player player) {
		return player.party != null;
	}

	public static void addPoints(Player player, int points) {
		if (!isRaiding(player))
			return;
		points /= 2.5;
		if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.DOUBLE_RAID_POINTS)) {
			points *= 2;
		}
		if (!player.getCustomXericRaid().olmRaid)
			points /= 8;
		int personalPoints = VarPlayerRepository.RAIDS_PERSONAL_POINTS.get(player);
		if (personalPoints + points > 130000)
			personalPoints = 130000;
		else
			personalPoints += points;
		VarPlayerRepository.RAIDS_PERSONAL_POINTS.set(player, personalPoints);
		VarPlayerRepository.RAIDS_PARTY_POINTS.set(player, VarPlayerRepository.RAIDS_PARTY_POINTS.get(player) + points);
		player.getCustomXericRaid().teamPoints += points;
	}

	private double combatPointsFactor = 1;

	public static void addDamagePoints(Player player, NPC target, int points) {
		if (!isRaiding(player))
			return;
		if (target.get("RAID_NO_POINTS") != null)
			return;
		// points /= 2;
		float multiplier = 2;
		points *= player.getCustomXericRaid().combatPointsFactor;
		multiplier = switch (player.getDifficulty()) {
			case EASY -> 1.0f;
			case INTERMEDIATE -> 1.3f;
			case HARD -> 1.5F;
			case EXTREME -> 2.5f;
			default -> multiplier;
		};
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.RAIDING_RESTORATIONS)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.RAIDING_RESTORATIONS);
			RaidingRestorations c = (RaidingRestorations) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
				.getPerk(player);
			multiplier += c.getLootChance();
		}
		multiplier += (player.calculateDropRate() / 100.0f);
		points *= multiplier;
		if (player.getCustomXericRaid().olmRaid) {
			points /= 3;
			if (player.getCustomXericRaid().currentParty != null
				&& player.getCustomXericRaid().currentParty.getMembers().size() > 1) {
				points /= (int) (player.getCustomXericRaid().currentParty.getMembers().size() * 0.5);
			}
		}
		addPoints(player, points);
	}

	private Instant startTime;

	private void handleDeath(Player player) {
		deathCount++;
		if (olmRaid && currentParty.getMembers().size() == 1) {
			failRaid(player);
		} else {
			for (Item item : player.getInventory().getItems()) {
				if (item != null && item.getDef() != null && item.getDef().coxItem) {
					item.remove();
					new GroundItem(item).position(player.getPosition()).spawnPublic();
				}
			}
			player.getCombat().restore();
			player.getMovement().teleport(getRespawnPosition(player));
			player.getPacketSender().resetCamera();
			int pointsLost = (int) (VarPlayerRepository.RAIDS_PERSONAL_POINTS.get(player) * 0.4);
			if (pointsLost > 0)
				addPoints(player, -pointsLost);
			player.openInterface(ToplevelComponent.WILDERNESS_OVERLAY, Interface.RAID_POINTS);
			if (startTime != null)
				VarPlayerRepository.RAIDS_TIMER.set(player,
					(int) (Duration.between(startTime, Instant.now()).toMillis() / 600L));
		}
	}

	private Position getRespawnPosition(Player player) {
		if (olmRaid) {
			return new Position(olmRoom.swRegion.baseX + 33, olmRoom.swRegion.baseY + 24, 0);
		}
		if (player.getPosition().getRegion().id == floorOne.swRegion.id
			|| player.getPosition().getRegion().id == floorOne.nwRegion.id) {
			return new Position(floorOne.swRegion.baseX + 3, floorOne.swRegion.baseY + 4, 0);
		} else if (player.getPosition().getRegion().id == floorTwo.swRegion.id
			|| player.getPosition().getRegion().id == floorTwo.seRegion.id) {
			return new Position(player.getCustomXericRaid().floorTwo.swRegion.baseX + 15,
				player.getCustomXericRaid().floorTwo.swRegion.baseY + 8, 0);
		} else {
			return new Position(olmRoom.swRegion.baseX + 33, olmRoom.swRegion.baseY + 24, 0);
		}
	}

	void spawnNPCs() {
		int scaledFor = currentParty.getMembers().size() + fakeScale;
		spawnMuttadiles(scaledFor);
		spawnTighropeMonsters(scaledFor);
		spawnFloorOneScavs(scaledFor);
		spawnVanguards();
		spawnFloorTwoScavs(scaledFor);
		spawnFloorTwoScavs2(scaledFor);
		spawnSkeletalMages(scaledFor);
		spawnGuardians();
		NPC olm = spawnNPC(new NPC(7554), new Position(olmRoom.swRegion.baseX + 38, olmRoom.swRegion.baseY + 42, 0));
		GreatOlm greatOlm = (GreatOlm) olm.getCombat();
		greatOlm.scaledFor = scaledFor;

	}

	void spawnGuardians() {
		NPC guardianOne = spawnNPC(new NPC(7569),
			new Position(floorTwo.seRegion.baseX + 21, floorTwo.seRegion.baseY + 38, 0));
		NPC guardianTwo = spawnNPC(new NPC(7570),
			new Position(floorTwo.seRegion.baseX + 24, floorTwo.seRegion.baseY + 38, 0));

		guardianTwo.face(Direction.NORTH);
		guardianOne.face(Direction.NORTH);
		GameObject passage = Tile.getObject(29789, floorTwo.seRegion.baseX + 22, floorTwo.seRegion.baseY + 34, 0);
		ObjectAction.register(passage, 1, (player, obj) -> {
			if (player.localNpcs().stream().anyMatch(npc -> npc.getId() == 7569 || npc.getId() == 7570)) {
				player.sendMessage("The guardians prevent you from passing.");
				player.hit(new Hit().randDamage(5, 10));
			} else {
				obj.getDef().defaultActions[0].handle(player, obj);
			}
		});
	}

	private static int getMysticsCount(int partySize) {
		if (partySize <= 3)
			return 3;
		else if (partySize < 7)
			return 4;
		else
			return 5;
	}

	private GameObject getMysticBlock() {
		for (GameObject obj : objs) {
			if (obj.getId() == 29796) {
				return obj;
			}
		}
		return null;
	}

	private boolean anyAliveSkeletalMages() {
		for (NPC npc : npcs) {
			if (npc.getId() >= 7604 && npc.getId() <= 7606 && npc.getHp() > 0) {
				return true;
			}
		}
		return false;
	}

	void spawnSkeletalMages(int scaledFor) {
		GameObject block = spawnObject(29796, new Position(floorTwo.swRegion.baseX + 57, floorTwo.swRegion.baseY + 41), 0);
		AtomicInteger mystics = new AtomicInteger(getMysticsCount(scaledFor));
		List<Position> mysticSpawns = new ArrayList<>(getSkeletalMageSpawns()); // Create a modifiable list
		for (int i = 0; i < mystics.get(); i++) {
			Position spawn = mysticSpawns.remove(0); // Remove first element
			NPC mystic = spawnNPC(new NPC(7604 + (i % 3)), spawn);
			mystic.deathEndListener = (entity, killer, killHit) -> {
				if (mystics.decrementAndGet() <= 0) {
					block.remove(); // Complete room
				}
				mystic.remove();
			};
		}
	}

	void spawnFloorTwoScavs(int scaledFor) {
		List<Position> skavSpawns = new ArrayList<>(getSkavFloorTwoSpawns()); // Create a modifiable list
		int toSpawn;
		if (scaledFor < 5)
			toSpawn = 2;
		else if (scaledFor < 8)
			toSpawn = 3;
		else
			toSpawn = 4;

		for (int i = 0; i < toSpawn; i++) {
			Position pos = skavSpawns.remove(0); // Remove first element
			spawnSkav(pos);
		}
	}

	void spawnFloorTwoScavs2(int scaledFor) {
		List<Position> skavSpawns = new ArrayList<>(getSkavFloorTwoSpawns2()); // Create a modifiable list
		int toSpawn;
		if (scaledFor < 5)
			toSpawn = 2;
		else if (scaledFor < 8)
			toSpawn = 3;
		else
			toSpawn = 4;

		for (int i = 0; i < toSpawn; i++) {
			Position pos = skavSpawns.remove(0); // Remove first element
			spawnSkav(pos);
		}
	}

	void spawnFloorOneScavs(int scaledFor) {
		List<Position> skavSpawns = new ArrayList<>(getSkavFloorOneSpawns()); // Create a modifiable list
		int toSpawn;
		if (scaledFor < 5)
			toSpawn = 2;
		else if (scaledFor < 8)
			toSpawn = 3;
		else
			toSpawn = 4;

		for (int i = 0; i < toSpawn; i++) {
			Position pos = skavSpawns.removeFirst(); // Remove first element
			spawnSkav(pos);
		}
	}

	private static final int[][] waypoints = {
		{ 47, 22 },
		{ 53, 19 },
		{ 48, 14 },
	};

	private int[][] instancedWaypoints = new int[3][];
	private int[] lastWaypoints = new int[3];
	private boolean activated = false;
	private NPC[] vanguards = new NPC[3];

	private double getLargestDifference() {
		return Math.max(Math.abs(getHealthRatio(0) - getHealthRatio(1)),
			Math.max(Math.abs(getHealthRatio(0) - getHealthRatio(2)), Math.abs(getHealthRatio(1) - getHealthRatio(2))));
	}

	private double getHealthRatio(int index) {
		return vanguards[index].getCombat().isDead() ? 0 : (double) vanguards[index].getHp() / vanguards[index].getMaxHp();
	}

	private static final double HEAL_THRESHOLD = 0.4;

	boolean vanguardsHealed = false;

	void spawnVanguards() {
		GameObject crystal = spawnObject(30017, new Position(floorOne.nwRegion.baseX + 48, floorOne.nwRegion.baseY + 2, 0),
			0);
		for (int i = 0; i < waypoints.length; i++) {
			int x = waypoints[i][0];
			int y = waypoints[i][1];
			NPC vanguard = vanguards[i] = spawnNPC(new NPC(7525),
				new Position(floorOne.nwRegion.baseX + x, floorOne.nwRegion.baseY + y, 0));
			lastWaypoints[i] = i;
			instancedWaypoints[i] = new int[] { floorOne.nwRegion.baseX + waypoints[i][0], waypoints[i][1],
				floorOne.nwRegion.baseY + waypoints[i][0], waypoints[i][1] };

			vanguard.deathEndListener = (entity, killer, killHit) -> {
				var allDead = Arrays.stream(vanguards).allMatch(n -> n.getCombat().isDead());
				if (allDead) {
					if (!vanguardsHealed) {
						vanguard.getPosition().getRegion().players.forEach(p -> {
							if (p != null) {
								Objects.requireNonNull(p.combatAchievementsList
									.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECTLY_BALANCED.ordinal()))
									.getCombatAchievement()).check(p);
							}
						});
					}

					World.startEvent(event -> {
						crystal.animate(7506);
						event.delay(3);
						if (!crystal.isRemoved())
							crystal.remove();
					});
				}
				vanguard.remove();
			};
		}
		World.startEvent(event -> {
			while (!activated) {
				if (vanguards[0].localPlayers().stream()
					.anyMatch(p -> p.getPosition().isWithinDistance(vanguards[0].getPosition(), 8))) {
					activated = true;
					for (int i = 0; i < vanguards.length; i++) {
						activated = true;
						vanguards[i].animate(7428);
						vanguards[i].transform(7527 + i);
						vanguards[i].getCombat().getStat(StatType.Hitpoints).restore();
					}
					event.delay(2);
					for (NPC vanguard : vanguards) {
						vanguard.animate(vanguard.getCombat().getInfo().spawn_animation);
					}
				} else {
					event.delay(1);
				}
			}
			event.delay(Random.get(50, 70));
			while (!crystal.isRemoved()) {
				// shuffle
				boolean heal = getLargestDifference() > HEAL_THRESHOLD;
				for (NPC vanguard : vanguards) {
					if (vanguard.isRemoved() || vanguard.getCombat().isDead())
						continue;
					vanguard.set("VANGUARD_ORIGINAL_ID", vanguard.getId());
					vanguard.getCombat().reset();
					vanguard.lock(LockType.FULL_NULLIFY_DAMAGE);
					vanguard.animate(vanguard.getCombat().getInfo().death_animation);
					vanguard.transform(7526);
					if (heal) {
						vanguard.hit(new Hit(HitType.HEAL).fixedDamage(vanguard.getMaxHp()));
						vanguardsHealed = true;
					}
				}
				event.delay(2);
				for (int i = 0; i < vanguards.length; i++) {
					if (vanguards[i].isRemoved() || vanguards[i].getCombat().isDead())
						continue;
					lastWaypoints[i] = (lastWaypoints[i] + 1) % 3;
					int[] waypoint = instancedWaypoints[lastWaypoints[i]];
					vanguards[i].getRouteFinder().routeAbsolute(waypoint[0], waypoint[1]);
				}
				while (Arrays.stream(vanguards).anyMatch(n -> !n.getMovement().isAtDestination()))
					event.delay(1);
				event.delay(1);
				for (NPC vanguard : vanguards) {
					if (vanguard.isRemoved() || vanguard.getCombat().isDead())
						continue;
					vanguard.transform(vanguard.get("VANGUARD_ORIGINAL_ID", 0));
					vanguard.animate(vanguard.getCombat().getInfo().spawn_animation);
					vanguard.unlock();
				}
				event.delay(Random.get(60, 80));
			}
		});
	}

	private static final StatType[] SCALED_STATS = { StatType.Hitpoints, StatType.Attack, StatType.Strength,
		StatType.Magic, StatType.Ranged };

	public void scaleNPC(NPC npc, int scaledFor) {
		if (npc.getCombat() == null) {
			return;
		}
		double factor;
		factor = olmRaid ? 1.4 + (0.55 * scaledFor) : 1.2 + (0.40 * scaledFor);
		if (factor != 0 & scaledFor > 1) {
			for (StatType type : SCALED_STATS) {
				npc.getCombat().getStat(type).fixedLevel *= factor;
				npc.getCombat().getStat(type).restore();
			}
		}

	}

	void spawnSkav(Position position) {
		spawnNPC(new NPC(Random.rollPercent(50) ? 7548 : 7549), position);
	}

	private String getTimeSinceStart() {
		Duration d = Duration.between(startTime, Instant.now());
		return String.format("%02d:%02d", d.toMinutes(), d.getSeconds() % 60);
	}

	private void handleTimers(Player plr) {
		if (timer == null || olmRaid)
			return;
		switch (currentParty.getMembers().size()) {
			case 1:
				plr.chambersOfXericSoloBestTime = timer.stop(plr, plr.chambersOfXericSoloBestTime);
				break;
			case 2:
				plr.chambersOfXericDuoBestTime = timer.stop(plr, plr.chambersOfXericDuoBestTime);
				break;
			case 3:
				plr.chambersOfXericTrioBestTime = timer.stop(plr, plr.chambersOfXericTrioBestTime);
				break;
			case 4:
				plr.chambersOfXericFourManBestTime = timer.stop(plr, plr.chambersOfXericFourManBestTime);
				break;
			case 5:
				plr.chambersOfXericFiveManBestTime = timer.stop(plr, plr.chambersOfXericFiveManBestTime);
				break;
		}
	}

	public void completeRaid() {
		String time = getTimeSinceStart();
		currentParty.getMembers().forEach(p -> {
			Player plr = World.getPlayer(p);
			if (plr != null) {
				handleTimers(plr);
				if (!olmRaid && !deathlyKilled) {
					Objects.requireNonNull(plr.combatAchievementsList
						.get(plr.getCombatAchievementIndexByOrdinal(CombatAchievements.NO_TIME_FOR_DEATH.ordinal()))
						.getCombatAchievement()).check(plr);
				}
				if (currentParty.getMembers().size() == 5
					&& ActivityTimer.timeInSeconds(plr.chambersOfXericFiveManBestTime) < 480 && !olmRaid) {
					Objects
						.requireNonNull(
							plr.combatAchievementsList
								.get(plr.getCombatAchievementIndexByOrdinal(
									CombatAchievements.CHAMBERS_OF_XERIC_FIVE_SCALE_SPEED_CHASER.ordinal()))
								.getCombatAchievement())
						.check(plr);
				}
				if (currentParty.getMembers().size() == 5
					&& ActivityTimer.timeInSeconds(plr.chambersOfXericFiveManBestTime) < 420 && !olmRaid) {
					Objects
						.requireNonNull(
							plr.combatAchievementsList
								.get(plr.getCombatAchievementIndexByOrdinal(
									CombatAchievements.CHAMBERS_OF_XERIC_FIVE_SCALE_SPEED_RUNNER.ordinal()))
								.getCombatAchievement())
						.check(plr);
				}
				if (olm != null && currentParty.getMembers().size() == 3) {
					GreatOlm npc = (GreatOlm) olm.getCombat();
					if (!npc.damagedPlayer) {
						Objects.requireNonNull(plr.combatAchievementsList
							.get(plr.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_OLM_TRIO.ordinal()))
							.getCombatAchievement()).check(plr);
					}
				}
				if (olm != null && currentParty.getMembers().size() == 1) {
					GreatOlm npc = (GreatOlm) olm.getCombat();
					if (!npc.damagedPlayer) {
						Objects.requireNonNull(plr.combatAchievementsList
							.get(plr.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_OLM_SOLO.ordinal()))
							.getCombatAchievement()).check(plr);
					}
				}
				if (currentParty.getMembers().size() == 1 && VarPlayerRepository.RAIDS_PERSONAL_POINTS.get(plr) >= 80000) {
					Objects.requireNonNull(plr.combatAchievementsList
						.get(plr.getCombatAchievementIndexByOrdinal(CombatAchievements.PUTTING_IT_OLM_THE_LINE.ordinal()))
						.getCombatAchievement()).check(plr);
				}
				if (currentParty.getMembers().size() == 3 && ActivityTimer.timeInSeconds(plr.chambersOfXericTrioBestTime) < 600
					&& !olmRaid) {
					Objects.requireNonNull(plr.combatAchievementsList
						.get(plr
							.getCombatAchievementIndexByOrdinal(CombatAchievements.CHAMBERS_OF_XERIC_TRIO_SPEED_CHASER.ordinal()))
						.getCombatAchievement()).check(plr);
				}
				if (currentParty.getMembers().size() == 3 && ActivityTimer.timeInSeconds(plr.chambersOfXericTrioBestTime) < 510
					&& !olmRaid) {
					Objects.requireNonNull(plr.combatAchievementsList
						.get(plr
							.getCombatAchievementIndexByOrdinal(CombatAchievements.CHAMBERS_OF_XERIC_TRIO_SPEED_RUNNER.ordinal()))
						.getCombatAchievement()).check(plr);
				}
				if (currentParty.getMembers().size() == 1 && ActivityTimer.timeInSeconds(plr.chambersOfXericSoloBestTime) < 720
					&& !olmRaid) {
					Objects.requireNonNull(plr.combatAchievementsList
						.get(plr
							.getCombatAchievementIndexByOrdinal(CombatAchievements.CHAMBERS_OF_XERIC_SOLO_SPEED_CHASER.ordinal()))
						.getCombatAchievement()).check(plr);
				}
				if (currentParty.getMembers().size() == 1 && ActivityTimer.timeInSeconds(plr.chambersOfXericSoloBestTime) < 600
					&& !olmRaid) {
					Objects.requireNonNull(plr.combatAchievementsList
						.get(plr
							.getCombatAchievementIndexByOrdinal(CombatAchievements.CHAMBERS_OF_XERIC_SOLO_SPEED_RUNNER.ordinal()))
						.getCombatAchievement()).check(plr);
				}
				if (currentParty.getMembers().size() == 1 && deathCount == 0) {
					Objects.requireNonNull(plr.combatAchievementsList
						.get(plr.getCombatAchievementIndexByOrdinal(CombatAchievements.UNDYING_RAIDER.ordinal()))
						.getCombatAchievement()).check(plr);
				}
				giveRewards(plr);
				if (VarPlayerRepository.RAIDS_PERSONAL_POINTS.get(plr) > 0) {
					plr.sendMessage(Color.RAID_PURPLE
						.wrap("Congratulations - your raid is complete! Duration: " + Color.RED.wrap(time) + "."));
					if (olmRaid)
						plr.olmOnlyKills.increment(plr);
					else
						plr.chambersofXericKills.increment(plr);
					DailyTasks.handleTaskDecrement(plr, "cox");
					PerkTaskHandler.handleMonsterKill(plr, "cox");
					Objects.requireNonNull(plr.combatAchievementsList
						.get(plr.getCombatAchievementIndexByOrdinal(CombatAchievements.CHAMBERS_OF_XERIC_ADEPT.ordinal()))
						.getCombatAchievement()).check(plr);
					Objects.requireNonNull(plr.combatAchievementsList
						.get(plr.getCombatAchievementIndexByOrdinal(CombatAchievements.CHAMBERS_OF_XERIC_VETERAN.ordinal()))
						.getCombatAchievement()).check(plr);
					Objects.requireNonNull(plr.combatAchievementsList
						.get(plr.getCombatAchievementIndexByOrdinal(CombatAchievements.CHAMBERS_OF_XERIC_MASTER.ordinal()))
						.getCombatAchievement()).check(plr);
					Objects.requireNonNull(plr.combatAchievementsList
						.get(plr.getCombatAchievementIndexByOrdinal(CombatAchievements.CHAMBERS_OF_XERIC_GRANDMASTER.ordinal()))
						.getCombatAchievement()).check(plr);
					if (plr.activePerksList.isEmpty() && currentParty.getMembers().size() == 1) {
						Objects.requireNonNull(plr.combatAchievementsList
							.get(plr.getCombatAchievementIndexByOrdinal(CombatAchievements.PERKLESS_CHAMBERS.ordinal()))
							.getCombatAchievement()).check(plr);
					}
				}
				Party.updatePartyStage(plr, Party.GET_OUT);
				plr.inCox = false;
			}
		});
		currentParty.getMembers().forEach(name -> {
			Player p = World.getPlayer(name);
			if (p != null) {
				p.sendMessage(String.format(Color.RAID_PURPLE.wrap("Take your rewards from the chest or they will vanish!")));
			}
		});
	}

	private static Item rollRegular() {
		return regularTable.rollItem();
	}

	private static Item rollUnique() {
		return uniqueTable.rollItem();
	}

	private static Item rollUniqueOlmOnly() {
		return uniqueOlmOnlyTable.rollItem();
	}

	private static void withdrawReward(Player p, int slot) {
		if (slot < 0 || slot >= p.getRaidRewards().getItems().length)
			return;
		Item item = p.getRaidRewards().get(slot);
		if (item == null)
			return;
		if (item.move(item.getId(), item.getAmount(), p.getInventory()) > 0) {
			p.addToCollectionLog(item);
			p.getRaidRewards().sendUpdates();
		} else {
			p.sendMessage("Not enough space in your inventory.");
		}
	}

	private static void openRewards(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, Interface.RAID_REWARDS);
		player.getRaidRewards().sendUpdates();
		new Unlock(Interface.RAID_REWARDS, 5, 0, 2).unlockMultiple(player, 0, 9);
	}

	private static LootTable uniqueTable = new LootTable()
		.addTable(1,
			new LootItem(21034, 1, 10), // dexterous scroll
			new LootItem(21079, 1, 10), // arcane scroll
			new LootItem(24466, 1, 10), // Twisted Horns
			new LootItem(21000, 1, 7), // twisted buckler
			new LootItem(21012, 1, 7), // dragon hunter crossbow
			new LootItem(21015, 1, 5), // dinh's bulwark
			new LootItem(21018, 1, 5), // ancestral hat
			new LootItem(21021, 1, 5), // ancestral top
			new LootItem(21024, 1, 5), // ancestral bottom
			new LootItem(13652, 1, 5), // dragon claws
			new LootItem(21003, 1, 2), // elder maul
			new LootItem(21043, 1, 2), // kodai insignia
			new LootItem(20997, 1, 1) // twisted bow
		);

	private static LootTable uniqueOlmOnlyTable = new LootTable()
		.addTable(1,
			new LootItem(21034, 1, 20), // dexterous scroll
			new LootItem(21079, 1, 20), // arcane scroll
			new LootItem(24466, 1, 20), // Twisted Horns
			new LootItem(21000, 1, 14), // twisted buckler
			new LootItem(21012, 1, 14), // dragon hunter crossbow
			new LootItem(21015, 1, 10), // dinh's bulwark
			new LootItem(21018, 1, 10), // ancestral hat
			new LootItem(21021, 1, 10), // ancestral top
			new LootItem(21024, 1, 10), // ancestral bottom
			new LootItem(13652, 1, 10), // dragon claws
			new LootItem(21003, 1, 4), // elder maul
			new LootItem(21043, 1, 4), // kodai insignia
			new LootItem(20997, 1, 1) // twisted bow
		);

	private static LootTable uniqueKalTable = new LootTable()
		.addTable(1,
			new LootItem(21000, 1, 1), // twisted buckler
			new LootItem(21012, 1, 1), // dragon hunter crossbow
			new LootItem(21015, 1, 1), // dinh's bulwark
			new LootItem(21018, 1, 1), // ancestral hat
			new LootItem(21021, 1, 1), // ancestral top
			new LootItem(21024, 1, 1), // ancestral bottom
			new LootItem(13652, 1, 1), // dragon claws
			new LootItem(21003, 1, 1), // elder maul
			new LootItem(21043, 1, 1), // kodai insignia
			new LootItem(20997, 1, 1) // twisted bow
		);

	private static LootTable regularTable = new LootTable() // regular table. the "amount" here is the number used to
		// determine the amount given to players based on how many
		// points they have, for example 1 soul rune per 20 points
		.addTable(1,
			new LootItem(560, 20, 1), // death rune
			new LootItem(565, 16, 1), // blood rune
			new LootItem(566, 10, 1), // soul rune
			new LootItem(892, 7, 1), // rune arrow
			new LootItem(11212, 70, 1), // dragon arrow

			new LootItem(3050, 185, 1), // grimy toadflax
			new LootItem(208, 400, 1), // grimy ranarr weed
			new LootItem(210, 98, 1), // grimy irit
			new LootItem(212, 185, 1), // grimy avantoe
			new LootItem(214, 205, 1), // grimy kwuarm
			new LootItem(3052, 500, 1), // grimy snapdragon
			new LootItem(216, 200, 1), // grimy cadantine
			new LootItem(2486, 150, 1), // grimy lantadyme
			new LootItem(218, 106, 1), // grimy dwarf weed
			new LootItem(220, 428, 1), // grimy torstol

			new LootItem(443, 20, 1), // silver ore
			new LootItem(454, 20, 1), // coal
			new LootItem(445, 45, 1), // gold ore
			new LootItem(448, 45, 1), // mithril ore
			new LootItem(450, 100, 1), // adamantite ore
			new LootItem(452, 750, 1), // runite ore

			new LootItem(1624, 100, 1), // uncut sapphire
			new LootItem(1622, 170, 1), // uncut emerald
			new LootItem(1620, 125, 1), // uncut ruby
			new LootItem(1618, 260, 1), // uncut diamond

			// new LootItem(13391, 25, 1), // lizardman fang
			new LootItem(7937, 200, 1), // pure essence
			// new LootItem(13422, 24, 1), // saltpetre
			new LootItem(8781, 100, 1), // teak plank
			new LootItem(8783, 240, 1), // mahogany plank
			// new LootItem(13574, 55, 1), // dynamite

			new LootItem(21047, 131071, 1), // torn prayer scroll
			new LootItem(21027, 131071, 1) // dark relic

		);

	private static double getPetDonatorBoost(Player player) {
		switch (player.getSecondaryGroup()) {
			case SUPER_DONATOR -> {
				return 0.98;
			}
			case ELITE_DONATOR -> {
				return 0.96;
			}
			case NOBLE_DONATOR -> {
				return 0.94;
			}
			case GOLD_DONATOR -> {
				return 0.93;
			}
			case PLATINUM_DONATOR -> {
				return 0.92;
			}
			case LEGENDARY_DONATOR -> {
				return 0.91;
			}
			case SUPREME_DONATOR -> {
				return 0.90;
			}
		}
		return 1;
	}

	public static void giveRewards(Player p) {
		p.getRaidRewards().clear();
		int points = VarPlayerRepository.RAIDS_PERSONAL_POINTS.get(p);
		float purpleChance = p.getCustomXericRaid().olmRaid ? points / 3800.0f : points / 1800.0f;
		if (p.getCustomXericRaid().olmRaid) {
			if (p.soloOlmTimer != null)
				p.soloOlmBestTime = p.soloOlmTimer.stop(p, p.soloOlmBestTime);
		}
		if (points > 1000) {
			SummerEvent.handleKill(p, "Chambers of Xeric");
			if (p.getPlayerPerkHandler().getActivePerks(p).contains(Perks.RAIDING_RESTORATIONS)) {
				int perkIndex = p.getPlayerPerkHandler().getActivePerkIndex(p, Perks.RAIDING_RESTORATIONS);
				RaidingRestorations c = (RaidingRestorations) p.getPlayerPerkHandler().getActivePerks(p).get(perkIndex)
					.getPerk(p);
				double multiplier = 1.0 + c.getLootChance();
				purpleChance *= multiplier;
			}
			if (p.getEquipment().get(Equipment.SLOT_RING) != null && AttributeExtensions
				.hasAttribute(p.getEquipment().get(Equipment.SLOT_RING), AttributeTypes.RAID_UNIQUE_CHARM)) {
				int level = AttributeExtensions.getCharges(AttributeTypes.RAID_UNIQUE_CHARM,
					p.getEquipment().get(Equipment.SLOT_RING));
				double multiplier = 1.0 + (level * 0.05);
				purpleChance *= multiplier;
			}
			if (purpleChance > 45)
				purpleChance = 45;
			if (p.getCustomXericRaid().olmRaid && purpleChance > 20)
				purpleChance = 20;
			if (Random.get(100) < purpleChance) {
				String message = p.getName() + " just received ";
				int basePetRate = 40;
				if (p.petDropBonus.isDelayed())
					basePetRate = 30;

				basePetRate *= getPetDonatorBoost(p);

				if (Random.get(1, basePetRate) == 1) {
					Pet.OLMLET.unlock(p, 0);
				}
				p.getPacketSender().sendCreateObject(30030, p.getPosition().getRegion().baseX + 33,
					p.getPosition().getRegion().baseY + 55, p.getHeight(), 3,
					0);
				p.getPacketSender().sendCreateObject(30030, p.getPosition().getRegion().baseX + 33,
					p.getPosition().getRegion().baseY + 56, p.getHeight(), 10,
					0);
				World.startEvent(e -> {
					e.delay(1);
					p.getPacketSender().sendCreateObject(-1, p.getPosition().getRegion().baseX + 33,
						p.getPosition().getRegion().baseY + 56, p.getHeight(), 10,
						0);
				});
				Item item = p.getCustomXericRaid().olmRaid ? rollUniqueOlmOnly() : rollUnique();
				p.addToCollectionLog(item);
				p.getRaidRewards().add(item);
				Loggers.logRaidsUnique(p.getName(), item.getDef().name, p.chambersofXericKills.getKills());
				if (p.getCustomXericRaid().olmRaid) {
					int kc = p.olmOnlyKills.getKills() + 1;
					Broadcast.GLOBAL.sendNewsDropMessage(p, Icon.ADMINISTRATOR, p.getName(),
						" has just received " + Color.DARK_RED.wrap(item.getDef().name) + " from CoX [Olm Only] " + "(KC: "
							+ Color.RED.wrap("" + kc) + ")");

					RareDropHook.sendDiscordMessage(() -> {
						var jsonObject = new JSONObject();
						jsonObject.put("player", p.getName());
						jsonObject.put("game_mode", p.getGameMode());
						jsonObject.put("item_id", item.getId());
						jsonObject.put("item_name", item.getDef().name);
						jsonObject.put("source", "CoX [Olm Only]");
						jsonObject.put("total_attempts", Utils.formatMoneyString(p.olmOnlyKills.getKills() + 1));
						return jsonObject;
					});

				}
				else {
					Broadcast.GLOBAL.sendNewsDropMessage(p, Icon.ADMINISTRATOR, p.getName(),
						" has just received " + Color.DARK_RED.wrap(item.getDef().name) + " from Chambers of Xeric!");

					RareDropHook.sendDiscordMessage(() -> {
						var jsonObject = new JSONObject();
						jsonObject.put("player", p.getName());
						jsonObject.put("game_mode", p.getGameMode());
						jsonObject.put("item_id", item.getId());
						jsonObject.put("item_name", item.getDef().name);
						jsonObject.put("source", "Chambers of Xeric");
						jsonObject.put("total_attempts", Utils.formatMoneyString(p.chambersofXericKills.getKills() + 1));
						return jsonObject;
					});
				}
			} else {
				int playerPoints = Math.max(131071, VarPlayerRepository.RAIDS_PERSONAL_POINTS.get(p));
				if (playerPoints == 0)
					return;
				boolean bookRolled = false;
				p.getPacketSender().sendCreateObject(30029, p.getPosition().getRegion().baseX + 33,
					p.getPosition().getRegion().baseY + 55, p.getHeight(), 3,
					0);
				p.getPacketSender().sendCreateObject(30029, p.getPosition().getRegion().baseX + 33,
					p.getPosition().getRegion().baseY + 56, p.getHeight(), 10,
					0);
				World.startEvent(e -> {
					e.delay(1);
					p.getPacketSender().sendCreateObject(-1, p.getPosition().getRegion().baseX + 33,
						p.getPosition().getRegion().baseY + 56, p.getHeight(), 10,
						0);
				});
				for (int i = 0; i < 2; i++) {
					if (Random.get(100) == 0 && !bookRolled) {
						Item item = new Item(30548, 1);
						String message = p.getName() + " just received ";
						Broadcast.GLOBAL.sendNewsDropMessage(p, Icon.ADMINISTRATOR, p.getName(), " has just received "
							+ Color.DARK_RED.wrap(item.getDef().name) + " from Chambers of Xeric!");

						RareDropHook.sendDiscordMessage(() -> {
							var jsonObject = new JSONObject();
							jsonObject.put("player", p.getName());
							jsonObject.put("game_mode", p.getGameMode());
							jsonObject.put("item_id", item.getId());
							jsonObject.put("item_name", item.getDef().name);
							jsonObject.put("source", "Chambers of Xeric");
							jsonObject.put("total_attempts", Utils.formatMoneyString(p.chambersofXericKills.getKills()));
							return jsonObject;
						});

						p.addToCollectionLog(item);
						p.getRaidRewards().add(item);
						bookRolled = true;
					}
					Item rolled = rollRegular();
					double pointsPerItem = rolled.getAmount();
					int amount = (int) Math.ceil(playerPoints / pointsPerItem);
					rolled.setAmount(amount);
					if (amount > 1 && !rolled.getDef().stackable && !rolled.getDef().isNote())
						rolled.setId(rolled.getDef().notedId);
					p.addToCollectionLog(rolled);
					p.getRaidRewards().add(rolled);
				}
			}
		}
	}

	void spawnMuttadiles(int scaledFor) {
		NPC swimmer = spawnNPC(new NPC(7561), new Position(floorOne.nwRegion.baseX + 20, floorOne.nwRegion.baseY + 19, 0));
		swimmer.attackNpcListener = (player, npc, message) -> {
			if (message)
				player.sendMessage("The Muttadile is underwater, your attacks can't reach it!");
			return false;
		};
		NPC smallMuttadile = spawnNPC(new NPC(7562),
			new Position(floorOne.nwRegion.baseX + 13, floorOne.nwRegion.baseY + 11, 0));
		smallMuttadile.deathEndListener = (entity, killer, killHit) -> {
			smallMuttadile.remove();
			swimmer.startEvent(event -> {
				swimmer.lock();
				Direction dir = Direction.WEST;
				swimmer.transform(7563);
				swimmer.animate(7423);
				swimmer.step(dir.deltaX * 6, dir.deltaY * 6, StepType.WALK);
				event.delay(7);
				swimmer.unlock();
				double factor = 1.1 + (0.20 * scaledFor);
				swimmer.getCombat().getStat(StatType.Hitpoints).fixedLevel *= factor;
				swimmer.getCombat().getStat(StatType.Hitpoints).restore();
				swimmer.attackNpcListener = null;
				if (killHit != null && killHit.attacker != null) {
					swimmer.getCombat().setTarget(killHit.attacker);
					swimmer.face(killHit.attacker);
				}
			});
		};
		GameObject blocking = spawnObject(30018,
			new Position(floorOne.nwRegion.baseX + 12, floorOne.nwRegion.baseY + 24, 0), 0);
		swimmer.deathEndListener = (entity, killer, killHit) -> {
			swimmer.remove();
			World.startEvent(event -> {
				blocking.animate(7506);
				event.delay(3);
				blocking.remove();
			});
		};
	}

	void spawnTighropeMonsters(int scaledFor) {
		spawnObject(29749, new Position(floorOne.nwRegion.baseX + 27, floorOne.nwRegion.baseY + 47, 0), 2);
		int amountToSpawn;
		if (scaledFor < 4)
			amountToSpawn = 2;
		else if (scaledFor < 8)
			amountToSpawn = 3;
		else
			amountToSpawn = 4;
		List<Position> rangerSpawns = new ArrayList<>(getTightRopeRangerSpawns());
		List<Position> mageSpawns = new ArrayList<>(getTightRopeMagerSpawns());

		Iterator<Position> rangerIterator = rangerSpawns.iterator();
		Iterator<Position> mageIterator = mageSpawns.iterator();
		for (int i = 0; i < amountToSpawn; i++) {
			if (rangerIterator.hasNext() && mageIterator.hasNext()) {
				spawnDeathlyRanger(rangerIterator.next());
				rangerIterator.remove();
				spawnDeathlyMage(mageIterator.next());
				mageIterator.remove();
			}
		}

	}

	boolean deathlyKilled = false;

	void spawnDeathlyMage(Position position) {
		NPC npc = spawnNPC(new NPC(7560), position);
		npc.deathEndListener = (entity, killer, killHit) -> {
			deathlyKilled = true;
			npc.remove();
		};
	}

	void spawnDeathlyRanger(Position position) {
		NPC npc = spawnNPC(new NPC(7559), position);
		npc.deathEndListener = (entity, killer, killHit) -> {
			deathlyKilled = true;
			npc.remove();
		};
	}

	boolean olmRaid = false;
	int deathCount = 0;

	public void buildOlmOnlyRaid(Player player, XericParty party) {
		if (!player.getName().equalsIgnoreCase(party.getLeader())) {
			player.sendMessage("Only the leader can start the raid.");
			return;
		}

		try {
			olmRoom = new DynamicMap().build(12889, 0);
		}
		catch (DynamicMap.DynamicMapBuildException e) {
			player.sendMessage("Unable to build dynamic map");
			return;
		}

		deathCount = 0;
		deathlyKilled = false;
		teamPoints = 0;
		currentParty = party;
		startTime = Instant.now();
		olmRaid = true;
		timer = new ActivityTimer();
		for (String name : List.copyOf(currentParty.getMembers())) {
			Player p = World.getPlayer(name);
			if (p != null) {
				if (player.getPosition().getRegion().id != p.getPosition().regionId()) {
					p.sendMessage("Your raid leader has started the raid without you.");
					currentParty.removeMember(p.getName());
				} else {
					p.setCustomXericRaid(this);
					p.inCox = true;
					p.insideRaid = true;
					p.deathEndListener = (entity, killer, killHit) -> handleDeath(p);
					p.openInterface(ToplevelComponent.WILDERNESS_OVERLAY, Interface.RAID_POINTS);
					VarPlayerRepository.RAIDS_TIMER.set(p, 1);
					p.getMovement().teleport(olmRoom.swRegion.baseX + 33, olmRoom.swRegion.baseY + 24, 0);
					p.teleportListener = CustomXericRaid::allowTeleport;
					VarPlayerRepository.RAIDS_PERSONAL_POINTS.set(p, 0);
					VarPlayerRepository.RAIDS_PARTY_POINTS.set(p, 0);
				}
			}

		}
		olm = spawnNPC(new NPC(7554), new Position(olmRoom.swRegion.baseX + 38, olmRoom.swRegion.baseY + 42, 0));
		GreatOlm greatOlm = (GreatOlm) olm.getCombat();
		greatOlm.scaledFor = party.getMembers().size() + 4;
		combatPointsFactor = 1.5 * (party.getMembers().size() + 4);
	}

	List<Position> getTightRopeRangerSpawns() {
		return Arrays.asList(
			new Position(floorOne.nwRegion.baseX + 6, floorOne.nwRegion.baseY + 52, 0),
			new Position(floorOne.nwRegion.baseX + 5, floorOne.nwRegion.baseY + 52, 0),
			new Position(floorOne.nwRegion.baseX + 6, floorOne.nwRegion.baseY + 53, 0),
			new Position(floorOne.nwRegion.baseX + 5, floorOne.nwRegion.baseY + 53, 0),
			new Position(floorOne.nwRegion.baseX + 6, floorOne.nwRegion.baseY + 54, 0),
			new Position(floorOne.nwRegion.baseX + 5, floorOne.nwRegion.baseY + 54, 0));
	}

	List<Position> getTightRopeMagerSpawns() {
		return Arrays.asList(
			new Position(floorOne.nwRegion.baseX + 12, floorOne.nwRegion.baseY + 53, 0),
			new Position(floorOne.nwRegion.baseX + 11, floorOne.nwRegion.baseY + 53, 0),
			new Position(floorOne.nwRegion.baseX + 12, floorOne.nwRegion.baseY + 54, 0),
			new Position(floorOne.nwRegion.baseX + 11, floorOne.nwRegion.baseY + 54, 0),
			new Position(floorOne.nwRegion.baseX + 12, floorOne.nwRegion.baseY + 55, 0),
			new Position(floorOne.nwRegion.baseX + 11, floorOne.nwRegion.baseY + 55, 0));
	}

	List<Position> getSkeletalMageSpawns() {
		return Arrays.asList(
			new Position(floorTwo.swRegion.baseX + 45, floorTwo.swRegion.baseY + 33, 0),
			new Position(floorTwo.swRegion.baseX + 42, floorTwo.swRegion.baseY + 44, 0),
			new Position(floorTwo.swRegion.baseX + 43, floorTwo.swRegion.baseY + 39, 0),
			new Position(floorTwo.swRegion.baseX + 46, floorTwo.swRegion.baseY + 44, 0),
			new Position(floorTwo.swRegion.baseX + 50, floorTwo.swRegion.baseY + 38, 0));
	}

	List<Position> getSkavFloorOneSpawns() {
		return Arrays.asList(
			new Position(floorOne.nwRegion.baseX + 39, floorOne.nwRegion.baseY + 45, 0),
			new Position(floorOne.nwRegion.baseX + 41, floorOne.nwRegion.baseY + 51, 0),
			new Position(floorOne.nwRegion.baseX + 53, floorOne.nwRegion.baseY + 54, 0),
			new Position(floorOne.nwRegion.baseX + 53, floorOne.nwRegion.baseY + 54, 0),
			new Position(floorOne.nwRegion.baseX + 56, floorOne.nwRegion.baseY + 47, 0),
			new Position(floorOne.nwRegion.baseX + 47, floorOne.nwRegion.baseY + 40, 0));
	}

	List<Position> getSkavFloorTwoSpawns() {
		return Arrays.asList(
			new Position(floorTwo.swRegion.baseX + 13, floorTwo.swRegion.baseY + 34, 0),
			new Position(floorTwo.swRegion.baseX + 20, floorTwo.swRegion.baseY + 31, 0),
			new Position(floorTwo.swRegion.baseX + 8, floorTwo.swRegion.baseY + 45, 0),
			new Position(floorTwo.swRegion.baseX + 15, floorTwo.swRegion.baseY + 47, 0),
			new Position(floorTwo.swRegion.baseX + 19, floorTwo.swRegion.baseY + 47, 0),
			new Position(floorTwo.swRegion.baseX + 23, floorTwo.swRegion.baseY + 38, 0),
			new Position(floorTwo.swRegion.baseX + 25, floorTwo.swRegion.baseY + 40, 0));
	}

	List<Position> getSkavFloorTwoSpawns2() {
		return Arrays.asList(
			new Position(floorTwo.seRegion.baseX + 9, floorTwo.seRegion.baseY + 10, 0),
			new Position(floorTwo.seRegion.baseX + 14, floorTwo.seRegion.baseY + 7, 0),
			new Position(floorTwo.seRegion.baseX + 16, floorTwo.seRegion.baseY + 4, 0),
			new Position(floorTwo.seRegion.baseX + 21, floorTwo.seRegion.baseY + 10, 0),
			new Position(floorTwo.seRegion.baseX + 16, floorTwo.seRegion.baseY + 15, 0));
	}

	DynamicMap getDefaultFloorTwo() throws DynamicMap.DynamicMapBuildException {
		List<DynamicChunk> swRegionFloorTwo = Arrays.asList(
			new DynamicChunk(412, 713, 0).pos(0, 0, 0),
			new DynamicChunk(413, 713, 0).pos(1, 0, 0),
			new DynamicChunk(414, 713, 0).pos(2, 0, 0),
			new DynamicChunk(412, 714, 0).pos(0, 1, 0),
			new DynamicChunk(413, 714, 0).pos(1, 1, 0),
			new DynamicChunk(414, 714, 0).pos(2, 1, 0),
			new DynamicChunk(412, 715, 0).pos(0, 2, 0),
			new DynamicChunk(413, 715, 0).pos(1, 2, 0),
			new DynamicChunk(414, 715, 0).pos(2, 2, 0),

			new DynamicChunk(416, 652, 1).pos(0, 3, 0),
			new DynamicChunk(417, 652, 1).pos(1, 3, 0),
			new DynamicChunk(418, 652, 1).pos(2, 3, 0),
			new DynamicChunk(419, 652, 1).pos(3, 3, 0),
			new DynamicChunk(416, 653, 1).pos(0, 4, 0),
			new DynamicChunk(417, 653, 1).pos(1, 4, 0),
			new DynamicChunk(418, 653, 1).pos(2, 4, 0),
			new DynamicChunk(419, 653, 1).pos(3, 4, 0),
			new DynamicChunk(416, 654, 1).pos(0, 5, 0),
			new DynamicChunk(417, 654, 1).pos(1, 5, 0),
			new DynamicChunk(418, 654, 1).pos(2, 5, 0),
			new DynamicChunk(419, 654, 1).pos(3, 5, 0),
			new DynamicChunk(416, 655, 1).pos(0, 6, 0),
			new DynamicChunk(417, 655, 1).pos(1, 6, 0),
			new DynamicChunk(418, 655, 1).pos(2, 6, 0),
			new DynamicChunk(419, 655, 1).pos(3, 6, 0),

			new DynamicChunk(415, 656, 1).pos(4, 3, 0).rotate(1),
			new DynamicChunk(415, 657, 1).pos(5, 3, 0).rotate(1),
			new DynamicChunk(415, 658, 1).pos(6, 3, 0).rotate(1),
			new DynamicChunk(415, 659, 1).pos(7, 3, 0).rotate(1),
			new DynamicChunk(414, 656, 1).pos(4, 4, 0).rotate(1),
			new DynamicChunk(414, 657, 1).pos(5, 4, 0).rotate(1),
			new DynamicChunk(414, 658, 1).pos(6, 4, 0).rotate(1),
			new DynamicChunk(414, 659, 1).pos(7, 4, 0).rotate(1),
			new DynamicChunk(413, 656, 1).pos(4, 5, 0).rotate(1),
			new DynamicChunk(413, 657, 1).pos(5, 5, 0).rotate(1),
			new DynamicChunk(413, 658, 1).pos(6, 5, 0).rotate(1),
			new DynamicChunk(413, 659, 1).pos(7, 5, 0).rotate(1),
			new DynamicChunk(412, 656, 1).pos(4, 6, 0).rotate(1),
			new DynamicChunk(412, 657, 1).pos(5, 6, 0).rotate(1),
			new DynamicChunk(412, 658, 1).pos(6, 6, 0).rotate(1),
			new DynamicChunk(412, 659, 1).pos(7, 6, 0).rotate(1),

			// new DynamicChunk(412, 647, 0).rotate(3).pos(4, 0, 0),
			// new DynamicChunk(412, 646, 0).rotate(3).pos(5, 0, 0),
			// new DynamicChunk(412, 645, 0).rotate(3).pos(6, 0, 0),
			// new DynamicChunk(412, 644, 0).rotate(3).pos(7, 0, 0),
			new DynamicChunk(413, 647, 0).rotate(3).pos(4, 0, 0),
			new DynamicChunk(413, 646, 0).rotate(3).pos(5, 0, 0),
			new DynamicChunk(413, 645, 0).rotate(3).pos(6, 0, 0),
			new DynamicChunk(413, 644, 0).rotate(3).pos(7, 0, 0),
			new DynamicChunk(414, 647, 0).rotate(3).pos(4, 1, 0),
			new DynamicChunk(414, 646, 0).rotate(3).pos(5, 1, 0),
			new DynamicChunk(414, 645, 0).rotate(3).pos(6, 1, 0),
			new DynamicChunk(414, 644, 0).rotate(3).pos(7, 1, 0),
			new DynamicChunk(415, 647, 0).rotate(3).pos(4, 2, 0),
			new DynamicChunk(415, 646, 0).rotate(3).pos(5, 2, 0),
			new DynamicChunk(415, 645, 0).rotate(3).pos(6, 2, 0),
			new DynamicChunk(415, 644, 0).rotate(3).pos(7, 2, 0));

		List<DynamicChunk> seRegionFloorTwo = Arrays.asList(
			new DynamicChunk(419, 654, 0).pos(0, 0, 0).rotate(2),
			new DynamicChunk(418, 654, 0).pos(1, 0, 0).rotate(2),
			new DynamicChunk(417, 654, 0).pos(2, 0, 0).rotate(2),
			new DynamicChunk(416, 654, 0).pos(3, 0, 0).rotate(2),
			new DynamicChunk(419, 653, 0).pos(0, 1, 0).rotate(2),
			new DynamicChunk(418, 653, 0).pos(1, 1, 0).rotate(2),
			new DynamicChunk(417, 653, 0).pos(2, 1, 0).rotate(2),
			new DynamicChunk(416, 653, 0).pos(3, 1, 0).rotate(2),
			new DynamicChunk(419, 652, 0).pos(0, 2, 0).rotate(2),
			new DynamicChunk(418, 652, 0).pos(1, 2, 0).rotate(2),
			new DynamicChunk(417, 652, 0).pos(2, 2, 0).rotate(2),
			new DynamicChunk(416, 652, 0).pos(3, 2, 0).rotate(2),

			new DynamicChunk(419, 656, 2).pos(0, 3, 0).rotate(1),
			new DynamicChunk(419, 657, 2).pos(1, 3, 0).rotate(1),
			new DynamicChunk(419, 658, 2).pos(2, 3, 0).rotate(1),
			new DynamicChunk(419, 659, 2).pos(3, 3, 0).rotate(1),
			new DynamicChunk(418, 656, 2).pos(0, 4, 0).rotate(1),
			new DynamicChunk(418, 657, 2).pos(1, 4, 0).rotate(1),
			new DynamicChunk(418, 658, 2).pos(2, 4, 0).rotate(1),
			new DynamicChunk(418, 659, 2).pos(3, 4, 0).rotate(1),
			new DynamicChunk(417, 656, 2).pos(0, 5, 0).rotate(1),
			new DynamicChunk(417, 657, 2).pos(1, 5, 0).rotate(1),
			new DynamicChunk(417, 658, 2).pos(2, 5, 0).rotate(1),
			new DynamicChunk(417, 659, 2).pos(3, 5, 0).rotate(1),
			new DynamicChunk(416, 656, 2).pos(0, 6, 0).rotate(1),
			new DynamicChunk(416, 657, 2).pos(1, 6, 0).rotate(1),
			new DynamicChunk(416, 658, 2).pos(2, 6, 0).rotate(1),
			new DynamicChunk(416, 659, 2).pos(3, 6, 0).rotate(1)

		);
		return new DynamicMap().build(swRegionFloorTwo).buildSe(seRegionFloorTwo);
	}

	DynamicMap getDefaultFloorOne() throws DynamicMap.DynamicMapBuildException {
		List<DynamicChunk> swRegionFloorOne = Arrays.asList(
			new DynamicChunk(412, 648, 0).pos(0, 0, 0),
			new DynamicChunk(413, 648, 0).pos(1, 0, 0),
			new DynamicChunk(414, 648, 0).pos(2, 0, 0),
			new DynamicChunk(412, 649, 0).pos(0, 1, 0),
			new DynamicChunk(413, 649, 0).pos(1, 1, 0),
			new DynamicChunk(414, 649, 0).pos(2, 1, 0),
			new DynamicChunk(412, 650, 0).pos(0, 2, 0),
			new DynamicChunk(413, 650, 0).pos(1, 2, 0),
			new DynamicChunk(414, 650, 0).pos(2, 2, 0),

			new DynamicChunk(412, 651, 0).pos(0, 3, 0),
			new DynamicChunk(413, 651, 0).pos(1, 3, 0),
			new DynamicChunk(414, 651, 0).pos(2, 3, 0),
			new DynamicChunk(412, 652, 0).pos(0, 4, 0),
			new DynamicChunk(413, 652, 0).pos(1, 4, 0),
			new DynamicChunk(414, 652, 0).pos(2, 4, 0),
			new DynamicChunk(412, 653, 0).pos(0, 5, 0),
			new DynamicChunk(413, 653, 0).pos(1, 5, 0),
			new DynamicChunk(414, 653, 0).pos(2, 5, 0),
			new DynamicChunk(412, 654, 0).pos(0, 6, 0),
			new DynamicChunk(413, 654, 0).pos(1, 6, 0),
			new DynamicChunk(414, 654, 0).pos(2, 6, 0),
			new DynamicChunk(412, 655, 0).pos(0, 7, 0),
			new DynamicChunk(413, 655, 0).pos(1, 7, 0),
			new DynamicChunk(414, 655, 0).pos(2, 7, 0),

			new DynamicChunk(411, 644, 0).pos(4, 7, 0).rotate(2),
			new DynamicChunk(410, 644, 0).pos(5, 7, 0).rotate(2),
			new DynamicChunk(409, 644, 0).pos(6, 7, 0).rotate(2),
			new DynamicChunk(408, 644, 0).pos(7, 7, 0).rotate(2),
			new DynamicChunk(411, 645, 0).pos(4, 6, 0).rotate(2),
			new DynamicChunk(410, 645, 0).pos(5, 6, 0).rotate(2),
			new DynamicChunk(409, 645, 0).pos(6, 6, 0).rotate(2),
			new DynamicChunk(408, 645, 0).pos(7, 6, 0).rotate(2),
			new DynamicChunk(411, 646, 0).pos(4, 5, 0).rotate(2),
			new DynamicChunk(410, 646, 0).pos(5, 5, 0).rotate(2),
			new DynamicChunk(409, 646, 0).pos(6, 5, 0).rotate(2),
			new DynamicChunk(408, 646, 0).pos(7, 5, 0).rotate(2),
			new DynamicChunk(411, 647, 0).pos(4, 4, 0).rotate(2),
			new DynamicChunk(410, 647, 0).pos(5, 4, 0).rotate(2),
			new DynamicChunk(409, 647, 0).pos(6, 4, 0).rotate(2),
			new DynamicChunk(408, 647, 0).pos(7, 4, 0).rotate(2)

		);
		List<DynamicChunk> nwRegionFloorOne = Arrays.asList(
			new DynamicChunk(412, 664, 1).pos(0, 0, 0),
			new DynamicChunk(413, 664, 1).pos(1, 0, 0),
			new DynamicChunk(414, 664, 1).pos(2, 0, 0),
			new DynamicChunk(415, 664, 1).pos(3, 0, 0),
			new DynamicChunk(412, 665, 1).pos(0, 1, 0),
			new DynamicChunk(413, 665, 1).pos(1, 1, 0),
			new DynamicChunk(414, 665, 1).pos(2, 1, 0),
			new DynamicChunk(415, 665, 1).pos(3, 1, 0),
			new DynamicChunk(412, 666, 1).pos(0, 2, 0),
			new DynamicChunk(413, 666, 1).pos(1, 2, 0),
			new DynamicChunk(414, 666, 1).pos(2, 2, 0),
			new DynamicChunk(415, 666, 1).pos(3, 2, 0),
			new DynamicChunk(412, 667, 1).pos(0, 3, 0),
			new DynamicChunk(413, 667, 1).pos(1, 3, 0),
			new DynamicChunk(414, 667, 1).pos(2, 3, 0),
			new DynamicChunk(415, 667, 1).pos(3, 3, 0),

			new DynamicChunk(416, 668, 1).pos(0, 4, 0),
			new DynamicChunk(417, 668, 1).pos(1, 4, 0),
			new DynamicChunk(418, 668, 1).pos(2, 4, 0),
			new DynamicChunk(419, 668, 1).pos(3, 4, 0),
			new DynamicChunk(416, 669, 1).pos(0, 5, 0),
			new DynamicChunk(417, 669, 1).pos(1, 5, 0),
			new DynamicChunk(418, 669, 1).pos(2, 5, 0),
			new DynamicChunk(419, 669, 1).pos(3, 5, 0),
			new DynamicChunk(416, 670, 1).pos(0, 6, 0),
			new DynamicChunk(417, 670, 1).pos(1, 6, 0),
			new DynamicChunk(418, 670, 1).pos(2, 6, 0),
			new DynamicChunk(419, 670, 1).pos(3, 6, 0),
			new DynamicChunk(416, 671, 1).pos(0, 7, 0),
			new DynamicChunk(417, 671, 1).pos(1, 7, 0),
			new DynamicChunk(418, 671, 1).pos(2, 7, 0),
			new DynamicChunk(419, 671, 1).pos(3, 7, 0),

			new DynamicChunk(408, 655, 1).pos(4, 7, 0),
			new DynamicChunk(409, 655, 1).pos(5, 7, 0),
			new DynamicChunk(410, 655, 1).pos(6, 7, 0),
			new DynamicChunk(411, 655, 1).pos(7, 7, 0),
			new DynamicChunk(408, 654, 1).pos(4, 6, 0),
			new DynamicChunk(409, 654, 1).pos(5, 6, 0),
			new DynamicChunk(410, 654, 1).pos(6, 6, 0),
			new DynamicChunk(411, 654, 1).pos(7, 6, 0),
			new DynamicChunk(408, 653, 1).pos(4, 5, 0),
			new DynamicChunk(409, 653, 1).pos(5, 5, 0),
			new DynamicChunk(410, 653, 1).pos(6, 5, 0),
			new DynamicChunk(411, 653, 1).pos(7, 5, 0),
			new DynamicChunk(408, 652, 1).pos(4, 4, 0),
			new DynamicChunk(409, 652, 1).pos(5, 4, 0),
			new DynamicChunk(410, 652, 1).pos(6, 4, 0),
			new DynamicChunk(411, 652, 1).pos(7, 4, 0),

			new DynamicChunk(412, 667, 0).pos(4, 3, 0),
			new DynamicChunk(413, 667, 0).pos(5, 3, 0),
			new DynamicChunk(414, 667, 0).pos(6, 3, 0),
			new DynamicChunk(415, 667, 0).pos(7, 3, 0),
			new DynamicChunk(412, 666, 0).pos(4, 2, 0),
			new DynamicChunk(413, 666, 0).pos(5, 2, 0),
			new DynamicChunk(414, 666, 0).pos(6, 2, 0),
			new DynamicChunk(415, 666, 0).pos(7, 2, 0),
			new DynamicChunk(412, 665, 0).pos(4, 1, 0),
			new DynamicChunk(413, 665, 0).pos(5, 1, 0),
			new DynamicChunk(414, 665, 0).pos(6, 1, 0),
			new DynamicChunk(415, 665, 0).pos(7, 1, 0),
			new DynamicChunk(412, 664, 0).pos(4, 0, 0),
			new DynamicChunk(413, 664, 0).pos(5, 0, 0),
			new DynamicChunk(414, 664, 0).pos(6, 0, 0),
			new DynamicChunk(415, 664, 0).pos(7, 0, 0));
		return new DynamicMap().build(swRegionFloorOne).buildNw(nwRegionFloorOne);
	}

	private void failRaid(Player player) {
		player.sendMessage("You have died in an olm only mode and have failed the raid.");
		exited(player, false);
	}

	public void exited(Player player, boolean logout) {
		if (currentParty != null) {
			currentParty.removeMember(player.getName());
		}
		player.party = null;
		player.getMovement().teleport(1233, 3566, 0);
		player.deathEndListener = null;
		player.teleportListener = null;
		confiscateItems(player);
		player.inCox = false;
		player.insideRaid = false;
		player.closeInterface(ToplevelComponent.WILDERNESS_OVERLAY);
		player.curePoison(1);
		player.getCombat().restore();
		VarPlayerRepository.RAIDS_PERSONAL_POINTS.set(player, 0);
		VarPlayerRepository.RAIDS_PARTY_POINTS.set(player, 0);
		VarPlayerRepository.RAIDS_TIMER.set(player, 0);
		VarPlayerRepository.RAIDS_PARTY.set(player, -1);
		if (currentParty != null && currentParty.getMembers().isEmpty()) {
			destroy();
		}
	}

	public static boolean allowTeleport(Player player) {
		player.sendMessage("You can't teleport away from the Chambers of Xeric.");
		return false;
	}

	private void destroy() {
		npcs.forEach(NPC::remove);
		objs.forEach(GameObject::remove);

		if (floorOne != null) {
			floorOne.destroy();
		}

		if (floorTwo != null) {
			floorTwo.destroy();
		}

		if (olmRoom != null) {
			olmRoom.destroy();
		}
	}

	public static void register() {
		NPCAction.register(7601, "loot-chest", (player, npc) -> {
			openRewards(player);
		});
		NPCAction.register(7601, "talk-to", (player, npc) -> {
			player.dialogue(
				new NPCDialogue(npc, "Hello there, use my right click options to loot the chest or leave the raid."));
		});
		NPCAction.register(7601, "teleport", (player, npc) -> {
			player.dialogue(new OptionsDialogue(
				"Are you sure you want to leave? Any loot that isn't taken from the chest will be lost.",
				new Option("Leave.", () -> player.getCustomXericRaid().exited(player, false)),
				new Option("Stay.", player::closeDialogue)));
		});
		ObjectAction.register(30028, "search", (player, obj) -> { // reward chest
			openRewards(player);
		});
		LoginListener.register(player -> {
			// Get player's current position
			Position pos = player.getPosition();
			player.setInvincible(false);
			player.getAppearance().setNpcId(-1);
			player.getAppearance().update();

			// Check if either coordinate is in dynamic range
			if (pos.getX() >= 6400) { // Only check X since you mentioned 7750, 150
				System.out.println("[CustomXericRaid] Player " + player.getName() + " logged in at dynamic coordinates: " +
					pos.getX() + ", " + pos.getY() + ". Moving to home.");

				// Clear raid states
				player.inCox = false;
				player.insideRaid = false;
				player.setCustomXericRaid(null);

				// Clear interfaces and variables
				player.closeInterface(ToplevelComponent.WILDERNESS_OVERLAY);
				VarPlayerRepository.RAIDS_PERSONAL_POINTS.set(player, 0);
				VarPlayerRepository.RAIDS_PARTY_POINTS.set(player, 0);
				VarPlayerRepository.RAIDS_TIMER.set(player, 0);
				VarPlayerRepository.RAIDS_PARTY.set(player, -1);

				// Remove raid items
				confiscateItems(player);

				// Teleport them home
				player.getMovement().teleport(World.HOME);

				System.out.println("[CustomXericRaid] Successfully moved player from dynamic map");
			}
		});
		InterfaceHandler.register(Interface.RAID_REWARDS, h -> {
			h.actions[5] = (DefaultAction) (p, option, slot, itemId) -> {
				if (slot < 0 || slot >= p.getRaidRewards().getItems().length)
					return;
				if (option == 1)
					withdrawReward(p, slot);
				else {
					Item item = p.getRaidRewards().get(slot);
					if (item != null)
						item.examine(p);
				}
			};
		});
		ObjectAction.register(49999, 1, (player, obj) -> {
			if (player.getPosition().getRegion().id == player.getCustomXericRaid().olmRoom.swRegion.id) {
				player.dialogue(new OptionsDialogue(
					"Are you sure you want to leave? Any loot that isn't taken from the chest will be lost.",
					new Option("Leave.", () -> player.getCustomXericRaid().exited(player, false)),
					new Option("Stay.", player::closeDialogue)));
			} else {
				player.dialogue(new OptionsDialogue(
					"The raid has already begun, you will not be able to re-enter.",
					new Option("Leave.", () -> player.getCustomXericRaid().exited(player, false)),
					new Option("Stay.", player::closeDialogue)));
			}
		});
		ObjectAction.register(29778, 1, (player, obj) -> {
			player.dialogue(new OptionsDialogue(
				"Are you sure you want to leave? Any loot that isn't taken from the chest will be lost.",
				new Option("Leave.", () -> player.getCustomXericRaid().exited(player, false)),
				new Option("Stay.", player::closeDialogue)));
		});
		ObjectAction.register(29996, 1, (player, obj) -> {
			if (player.getCustomXericRaid().olmRaid) {
				player.dialogue(new OptionsDialogue(
					"Are you sure you want to leave? Any loot that isn't taken from the chest will be lost.",
					new Option("Leave.", () -> player.getCustomXericRaid().exited(player, false)),
					new Option("Stay.", player::closeDialogue)));
			}
		});
		LoginListener.register(CustomXericRaid::confiscateItems);
		ObjectAction.register(41754, 1, (player, obj) -> {
			player.getInventory().add(new Item(ItemID.IRON_PICKAXE, 1));
			player.sendMessage("You take an iron pickaxe.");
		});
		ObjectAction.register(29734, 1, (player, obj) -> {
			player.getMovement().teleport(player.getCustomXericRaid().floorTwo.swRegion.baseX + 15,
				player.getCustomXericRaid().floorTwo.swRegion.baseY + 8, 0);
		});
		ObjectAction.register(29735, 1, (player, obj) -> {
			player.getMovement().teleport(player.getCustomXericRaid().olmRoom.swRegion.baseX + 32,
				player.getCustomXericRaid().olmRoom.swRegion.baseY + 25, 0);
		});
	}
}
