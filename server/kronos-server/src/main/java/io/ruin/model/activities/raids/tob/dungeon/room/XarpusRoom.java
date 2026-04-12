package io.ruin.model.activities.raids.tob.dungeon.room;

import com.google.common.collect.Lists;
import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.activities.raids.tob.dungeon.RoomType;
import io.ruin.model.activities.raids.tob.dungeon.boss.xarpus.XarpusCombat;
import io.ruin.model.activities.raids.tob.party.TheatreParty;
import io.ruin.model.activities.raids.tob.party.TheatrePartyManager;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.actions.ObjectAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author NuLL on 28/12/2021
 *         https://www.rune-server.ee/members/null1001/
 * @project Drako
 */
public class XarpusRoom extends TheatreRoom {

	public NPC Xarpus;

	public XarpusRoom(TheatreParty party) throws DynamicMapBuildException {
		super(party);
	}

	@Override
	public void onLoad() {
		try {
			buildSw(12612, 1);
		} catch (DynamicMap.DynamicMapBuildException e) {
			throw new RuntimeException(e);
		}

		Xarpus = new NPC(8340).spawn(convertX(3170), convertY(4387), 1, Direction.EAST, 0);
		// System.out.println("Xarpus spawned at " +Xarpus.getPosition());
		Xarpus.setIgnoreMulti(true);
		addNpc(Xarpus);
		party.scaleNPC(Xarpus, false);
		Xarpus.deathStartListener = (DeathListener.SimpleKiller) killer -> {
			XarpusCombat xarpus = (XarpusCombat) Xarpus.getCombat();
			for (String user : party.getUsers()) {
				TheatrePartyManager.instance().forUsername(user).ifPresent(p -> {
					p.xarpusBestTime = party.xarpusTimer.stop(p, p.xarpusBestTime);
					p.theatreOfBloodStage = 5;
					if (!xarpus.wearingBarrows)
						p.currentParty.wearingBarrows = false;
					if (!xarpus.damagedPlayer && !party.deadPlayers.contains(p)) {
						p.currentParty.perfectXarpus = true;
						Objects.requireNonNull(p.combatAchievementsList
								.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_XARPUS.ordinal()))
								.getCombatAchievement()).check(p.player);
					}
					if (!xarpus.usedRangeOrMage && !party.deadPlayers.contains(p)) {
						Objects.requireNonNull(p.combatAchievementsList
								.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.CAN_YOU_DANCE.ordinal()))
								.getCombatAchievement()).check(p.player);
					}
					p.sendFilteredMessage(
							"You have completed the final fucking stage, grab a dawnbringer and get ready for Verzik.");
					p.getCombat().restore();
				});
			}
			Xarpus.getCombat().setAllowRespawn(false);
			World.startEvent(e -> {
				party.deadPlayers.forEach(p -> {
					p.sendMessage("You will be moved soon..");
				});
				e.delay(12);
				party.deadPlayers.forEach(p -> {
					p.getMovement().teleport(convertX(3170), convertY(4387), 1);
					p.unlock();
					p.getCombat().restore();
					p.setInvincible(false);
				});
				party.deadPlayers.clear();
			});
		};
	}

	@Override
	public void registerObjects() {
		ObjectAction.register(32751, "enter", ((player, obj) -> {
			if (player.theatreOfBloodStage < 5)
				return;
			TheatrePartyManager.instance().getPartyForPlayer(player.getName())
					.ifPresent(party -> party.getDungeon().enterRoom(player, RoomType.VERZIK));
		}));
		ObjectAction.register(32741, "search", ((player, obj) -> {
			if (!party.dawnbringerClaimed) {
				if (player.getInventory().getFreeSlots() > 0) {
					party.dawnbringerClaimed = true;
					player.getInventory().add(ItemID.DAWNBRINGER, 1);
					return;
				} else {
					player.sendMessage("You don't have any inventory spaces free!");
				}
			} else {
				player.sendMessage("I wonder what was once here.");
			}
		}));
	}

	@Override
	public void registerNpcs() {

	}

	@Override
	public List<Position> getSpectatorSpots() {
		return Lists.newArrayList(
				Position.of(convertX(3157), convertY(4387), 1),
				Position.of(convertX(3183), convertY(4387), 1));
	}

	@Override
	public Position getEntrance() {
		return Position.of(3170, 4377, 1);
	}

}
