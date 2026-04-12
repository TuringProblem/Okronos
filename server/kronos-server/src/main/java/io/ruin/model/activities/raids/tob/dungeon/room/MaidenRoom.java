package io.ruin.model.activities.raids.tob.dungeon.room;

import com.google.common.collect.Lists;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.raids.tob.dungeon.RoomType;
import io.ruin.model.activities.raids.tob.dungeon.boss.maiden.MaidenCombat;
import io.ruin.model.activities.raids.tob.party.TheatreParty;
import io.ruin.model.activities.raids.tob.party.TheatrePartyManager;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author NuLL on 28/12/2021
 *         https://www.rune-server.ee/members/null1001/
 * @project Drako
 */
public class MaidenRoom extends TheatreRoom {

	public NPC Maiden;
	public static boolean complete = false;

	boolean maidenTimerStarted = false;
	boolean bloatTimerStarted = false;
	boolean vasillasTimerStarted = false;
	boolean sotetsegTimerStarted = false;
	boolean xarpusTimerStarted = false;

	public MaidenRoom(TheatreParty party) throws DynamicMapBuildException {
		super(party);
	}

	@Override
	public void onLoad() {
		try {
			buildSw(12613, 1);
			buildSe(12869, 1);
		} catch (DynamicMap.DynamicMapBuildException e) {
			throw new RuntimeException(e);
		}

		GameObject.spawn(32756, convertX(3192), convertY(4448), 0, 11, 4);
		List<Position> blockPositions = new ArrayList<>();

		Tile.get(convertX(3167), convertY(4449), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3167), convertY(4449), 0));
		Tile.get(convertX(3167), convertY(4448), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3167), convertY(4448), 0));
		Tile.get(convertX(3167), convertY(4447), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3167), convertY(4447), 0));
		Tile.get(convertX(3167), convertY(4446), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3167), convertY(4446), 0));
		Tile.get(convertX(3167), convertY(4445), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3167), convertY(4445), 0));
		Tile.get(convertX(3167), convertY(4444), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3167), convertY(4444), 0));

		Tile.get(convertX(3166), convertY(4444), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3166), convertY(4444), 0));
		Tile.get(convertX(3165), convertY(4444), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3165), convertY(4444), 0));
		Tile.get(convertX(3164), convertY(4444), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3164), convertY(4444), 0));
		Tile.get(convertX(3163), convertY(4444), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3163), convertY(4444), 0));
		Tile.get(convertX(3162), convertY(4444), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3162), convertY(4444), 0));

		Tile.get(convertX(3162), convertY(4445), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3162), convertY(4445), 0));
		Tile.get(convertX(3162), convertY(4446), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3162), convertY(4446), 0));
		Tile.get(convertX(3162), convertY(4447), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3162), convertY(4447), 0));
		Tile.get(convertX(3162), convertY(4448), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3162), convertY(4448), 0));
		Tile.get(convertX(3162), convertY(4449), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3162), convertY(4449), 0));

		Tile.get(convertX(3166), convertY(4449), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3166), convertY(4449), 0));
		Tile.get(convertX(3165), convertY(4449), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3165), convertY(4449), 0));
		Tile.get(convertX(3164), convertY(4449), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3164), convertY(4449), 0));
		Tile.get(convertX(3163), convertY(4449), 0, true).flagUnmovable(); // block floor
		blockPositions.add(Position.of(convertX(3163), convertY(4449), 0));

		Maiden = new NPC(8360).spawn(convertX(3162), convertY(4444), 0, Direction.EAST, 0);
		Maiden.setIgnoreMulti(true);
		Maiden.getCombat().isAggressive();
		party.scaleNPC(Maiden, false);
		addNpc(Maiden);
		party.deaths = 0;
		party.getPlayerDeaths().clear();
		AtomicInteger tobChest = new AtomicInteger(33086);
		for (String user : party.getUsers()) {
			TheatrePartyManager.instance().forUsername(user).ifPresent(player -> {
				party.getPlayerDeaths().put(player, 0);
				party.getSupplyChestDeath().put(player, false);
				player.tobreward = false;
				player.theatreOfBloodStage = 0;
				player.tobDamageDealt = 0;
				player.supplyChestDamage = 0;
				player.supplyChestPoints = 0;
				player.theatreReward.clear();
				player.tobChestId = tobChest.get();
				tobChest.getAndIncrement();
			});
		}

		Maiden.deathStartListener = (DeathListener.SimpleKiller) killer -> {
			party.maidenTimerEnd = party.maidenTimer.stop(killer.player, 0);
			blockPositions.forEach(pos -> {
				Tile.get(pos, true).unflagUnmovable();
			});
			MaidenCombat maidenCombat = (MaidenCombat) Maiden.getCombat();
			for (String user : party.getUsers()) {
				TheatrePartyManager.instance().forUsername(user).ifPresent(p -> {
					if(!maidenCombat.cantDrainThisFailed) {
						Objects.requireNonNull(p.combatAchievementsList
							.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.CANT_DRAIN_THIS.ordinal()))
							.getCombatAchievement()).check(p);
					}
					if(!maidenCombat.anticoagulants) {
						Objects.requireNonNull(p.combatAchievementsList
							.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.ANTICOAGULANTS.ordinal()))
							.getCombatAchievement()).check(p);
					}
					if(!maidenCombat.perfectMaidenFailed) {
						p.currentParty.perfectMaiden = true;
						Objects.requireNonNull(p.combatAchievementsList
							.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_MAIDEN.ordinal()))
							.getCombatAchievement()).check(p);
					}
					p.theatreOfBloodStage = 1;
					p.sendFilteredMessage("You have completed stage 1!");
					p.getCombat().restore();
				});
			}


			Maiden.getCombat().setAllowRespawn(false);
			World.startEvent(e -> {
				party.deadPlayers.forEach(p -> {
					p.sendMessage("You will be moved soon..");
				});
				e.delay(12);
				party.deadPlayers.forEach(p -> {
					p.getMovement().teleport(convertX(3176), convertY(4428), 0);
					p.unlock();
					p.setInvincible(false);
					p.getCombat().restore();
				});
				party.deadPlayers.clear();
			});
		};
	}

	@Override
	public void registerObjects() {
		ObjectAction.register(32755, "pass", (player, obj) -> {
			if (player.getPosition().getX() > obj.getPosition().getX()) {
				if (!maidenTimerStarted) {
					maidenTimerStarted = true;
					party.maidenTimer = new ActivityTimer();
				}
				if (!bloatTimerStarted && player.theatreOfBloodStage == 1) {
					bloatTimerStarted = true;
					party.bloatTimer = new ActivityTimer();
				}
				if (!vasillasTimerStarted && player.theatreOfBloodStage == 2) {
					vasillasTimerStarted = true;
					party.vasillasTimer = new ActivityTimer();
				}
				if (!sotetsegTimerStarted && player.theatreOfBloodStage == 3) {
					sotetsegTimerStarted = true;
					party.sotetsegTimer = new ActivityTimer();
				}
				if (!xarpusTimerStarted && player.theatreOfBloodStage == 4) {
					xarpusTimerStarted = true;
					party.xarpusTimer = new ActivityTimer();
				}
				player.getMovement().teleport(obj.getPosition().getX() - 2, obj.getPosition().getY(), obj.getPosition().getZ()); // Teleport because it will not walk correctly:S wtf
			} else if (player.getPosition().getX() < obj.getPosition().getX()) {
				player.getMovement().teleport(obj.getPosition().getX() + 2, obj.getPosition().getY(), obj.getPosition().getZ());
			} else if (player.getPosition().getY() > obj.getPosition().getY()) {
				if (!maidenTimerStarted) {
					maidenTimerStarted = true;
					party.maidenTimer = new ActivityTimer();
				}
				if (!bloatTimerStarted && player.theatreOfBloodStage == 1) {
					bloatTimerStarted = true;
					party.bloatTimer = new ActivityTimer();
				}
				if (!vasillasTimerStarted && player.theatreOfBloodStage == 2) {
					vasillasTimerStarted = true;
					party.vasillasTimer = new ActivityTimer();
				}
				if (!sotetsegTimerStarted && player.theatreOfBloodStage == 3) {
					sotetsegTimerStarted = true;
					party.sotetsegTimer = new ActivityTimer();
				}
				if (!xarpusTimerStarted && player.theatreOfBloodStage == 4) {
					xarpusTimerStarted = true;
					party.xarpusTimer = new ActivityTimer();
				}
				player.getMovement().teleport(obj.getPosition().getX(), obj.getPosition().getY() - 2, obj.getPosition().getZ());
			} else if (player.getPosition().getY() < obj.getPosition().getY()) {
				player.getMovement().teleport(obj.getPosition().getX(), obj.getPosition().getY() + 2, obj.getPosition().getZ());
			}
		});
		ObjectAction.register(33113, "enter", ((player, obj) -> {
			if (player.theatreOfBloodStage == 1) {
				TheatrePartyManager.instance().getPartyForPlayer(player.getName())
						.ifPresent(party -> party.getDungeon().enterRoom(player, RoomType.BLOAT));
				player.theatreroom = "bloat";
			} else if (player.theatreOfBloodStage == 2) {
				TheatrePartyManager.instance().getPartyForPlayer(player.getName())
						.ifPresent(party -> party.getDungeon().enterRoom(player, RoomType.VASILIAS));
				player.theatreroom = "vasilias";
			} else if (player.theatreOfBloodStage == 3) {
				TheatrePartyManager.instance().getPartyForPlayer(player.getName())
						.ifPresent(party -> party.getDungeon().enterRoom(player, RoomType.SOTETSEG));
				player.theatreroom = "sotetseg";
			} else if (player.theatreOfBloodStage == 4) {
				TheatrePartyManager.instance().getPartyForPlayer(player.getName())
						.ifPresent(party -> party.getDungeon().enterRoom(player, RoomType.XARPUS));
				player.theatreroom = "xarpus";
			}
		}));

	}

	@Override
	public void registerNpcs() {

	}

	@Override
	public List<Position> getSpectatorSpots() {
		return Lists.newArrayList(
				Position.of(convertX(3167), convertY(4460), 0),
				Position.of(convertX(3166), convertY(4460), 0),
				Position.of(convertX(3166), convertY(4433), 0),
				Position.of(convertX(3167), convertY(4433), 0));
	}

	@Override
	public Position getEntrance() {
		return Position.of(3186, 4446);
	}

	// public void addBloodPit(Position pitLocation, boolean object) {
	// if (!getBloodPitLocations().contains(pitLocation)) {
	// getBloodPitLocations().add(pitLocation);
	// if (object) {
	// final GameObject pitObject = new GameObject(32984, pitLocation, 10, 0);
	//
	// ObjectBuilder.add(pitObject);
	// GameWorld.submit(30, pitObject, () -> {
	// getBloodPitLocations().remove(pitLocation);
	// ObjectBuilder.remove(pitObject);
	// });
	// } else {
	// Graphic.send(1579, pitLocation);
	// GameObject.spawn(pitObject);
	// GameWorld.submit(10, () -> getBloodPitLocations().remove(pitLocation));
	// }
	// }
	// }

}
