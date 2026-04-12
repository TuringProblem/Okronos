package io.ruin.model.activities.raids.tob.dungeon.room;

import com.google.common.collect.Lists;
import io.ruin.model.World;
import io.ruin.model.activities.raids.tob.dungeon.boss.vasilias.VasiliasCombat;
import io.ruin.model.activities.raids.tob.party.TheatreParty;
import io.ruin.model.activities.raids.tob.party.TheatrePartyManager;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.dynamic.DynamicMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author NuLL on 28/12/2021
 *         https://www.rune-server.ee/members/null1001/
 * @project Drako
 */
public class VasiliasRoom extends TheatreRoom {

	public NPC Vasilias;

	public VasiliasRoom(TheatreParty party) throws DynamicMapBuildException {
		super(party);
	}

	@Override
	public void onLoad() {
		try {
			buildSw(13122, 1);
		} catch (DynamicMap.DynamicMapBuildException e) {
			throw new RuntimeException(e);
		}

		Vasilias = new NPC(8356).spawn(convertX(3294), convertY(4247), 0, Direction.EAST, 0);
		Vasilias.setHidden(true);

		Tile.get(convertX(3289), convertY(4248), 0, true).destroy(); // unblock exit
		Tile.get(convertX(3289), convertY(4249), 0, true).destroy(); // unblock exit
		Tile.get(convertX(3302), convertY(4248), 0, true).destroy(); // unblock exit
		Tile.get(convertX(3302), convertY(4249), 0, true).destroy(); // unblock exit
		Vasilias.getCombat().isAggressive();
		Vasilias.setIgnoreMulti(true);
		addNpc(Vasilias);
		party.scaleNPC(Vasilias, false);

		for (String user : party.getUsers()) {
			TheatrePartyManager.instance().forUsername(user).ifPresent(p -> p.currentParty = party);
		}
		Vasilias.deathStartListener = (DeathListener.SimpleKiller) killer -> {
			party.vasillasTimerEnd = party.vasillasTimer.stop(killer.player, 0);
			VasiliasCombat vasiliasCombat = (VasiliasCombat) Vasilias.getCombat();
			for (String user : party.getUsers()) {
				TheatrePartyManager.instance().forUsername(user).ifPresent(p -> {
					if (!vasiliasCombat.wearingBarrows)
						p.currentParty.wearingBarrows = false;
					if (!vasiliasCombat.perfectNylocasFailed && !party.deadPlayers.contains(p)) {
						party.perfectVasillas = true;
						Objects.requireNonNull(p.combatAchievementsList
								.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_NYLOCAS.ordinal()))
								.getCombatAchievement()).check(p);
					}
					p.theatreOfBloodStage = 3;
					p.sendFilteredMessage("You have completed stage 3!");
					p.getCombat().restore();
				});
			}
			Vasilias.getCombat().setAllowRespawn(false);

			Vasilias.remove();
			World.startEvent(e -> {
				party.deadPlayers.forEach(p -> p.sendMessage("You will be moved soon.."));
				e.delay(12);
				party.deadPlayers.forEach(p -> {
					p.getMovement().teleport(convertX(3296), convertY(4249), 0);
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

	}

	@Override
	public void registerNpcs() {

	}

	@Override
	public List<Position> getSpectatorSpots() {
		return Lists.newArrayList(
				Position.of(convertX(3301), convertY(4240), 0),
				Position.of(convertX(3290), convertY(4240), 0));
	}

	@Override
	public Position getEntrance() {
		return Position.of(3296, 4283);
	}

	public void moveIntoRoom(Player player, Direction direction) {
		int x = player.getAbsX();
		player.stepAbs(direction == Direction.SOUTH ? x - 2 : x + 2, player.getAbsY(), StepType.WALK);
	}

}
