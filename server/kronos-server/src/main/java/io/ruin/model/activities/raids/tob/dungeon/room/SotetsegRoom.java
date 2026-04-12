package io.ruin.model.activities.raids.tob.dungeon.room;

import com.google.common.collect.Lists;
import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.raids.tob.dungeon.boss.sotetseg.SotetsegCombat;
import io.ruin.model.activities.raids.tob.party.TheatreParty;
import io.ruin.model.activities.raids.tob.party.TheatrePartyManager;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.GameObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author NuLL on 28/12/2021
 *         https://www.rune-server.ee/members/null1001/
 * @project Drako
 */
public class SotetsegRoom extends TheatreRoom {

	public NPC Sotetseg;

	public SotetsegRoom(TheatreParty party) throws DynamicMapBuildException {
		super(party);
	}

	@Override
	public void onLoad() {
		try {
			buildSw(13123, 1);
		} catch (DynamicMap.DynamicMapBuildException e) {
			throw new RuntimeException(e);
		}
		GameObject chest = GameObject.spawn(32758, convertX(3281), convertY(4293), 0, 10, 2).spawn();
		Sotetseg = new NPC(8388).spawn(convertX(3277), convertY(4328), 0, Direction.SOUTH, 0);
		Sotetseg.setIgnoreMulti(true);
		Sotetseg.getCombat().isAggressive();
		Sotetseg.getCombat().setAllowRetaliate(true);
		party.scaleNPC(Sotetseg, false);
		addNpc(Sotetseg);
		Sotetseg.deathStartListener = (DeathListener.SimpleKiller) killer -> {
			party.sotetsegTimerEnd = party.sotetsegTimer.stop(killer.player, 0);
			for (String user : party.getUsers()) {
				TheatrePartyManager.instance().forUsername(user).ifPresent(p -> {
					for (int i = 0; i < getPlayersByPerformance().size(); i++) {
						if (i == 0)
							p.supplyChestPoints = Random.get(10, 14);
						else if (i > 0 && i < 3)
							p.supplyChestPoints = Random.get(8, 11);
						else
							p.supplyChestPoints = Random.get(6, 9);
					}
					SotetsegCombat sotetsegCombat = (SotetsegCombat) Sotetseg.getCombat();
					if (!sotetsegCombat.wearingBarrows)
						p.currentParty.wearingBarrows = false;
					if (!party.deadPlayers.contains(p) && !sotetsegCombat.damagedPlayer) {
						p.currentParty.perfectSotetseg = true;
						Objects.requireNonNull(p.combatAchievementsList
								.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_SOTETSEG.ordinal()))
								.getCombatAchievement()).check(p);
					}
					p.theatreOfBloodStage = 4;
					p.sendFilteredMessage("You have completed stage 4!");
					p.getCombat().restore();
				});
			}
			Sotetseg.getCombat().setAllowRespawn(false);
			World.startEvent(e -> {
				party.deadPlayers.forEach(p -> {
					p.sendMessage("You will be moved soon..");
				});
				e.delay(12);
				party.deadPlayers.forEach(p -> {
					p.getMovement().teleport(convertX(3279), convertY(4325), 0);
					p.unlock();
					p.getCombat().restore();
					p.setInvincible(false);
					p.supplyChestPoints = 0;
				});
				party.deadPlayers.clear();
			});
			for (String user : party.getUsers()) {
				TheatrePartyManager.instance().forUsername(user).ifPresent(player -> {
					if (party.getSupplyChestDeath().getOrDefault(player, false)) {
						player.supplyChestPoints = 0;
					}
					party.getSupplyChestDeath().put(player, false);
				});
			}
		};
	}

	private List<Player> getPlayersByPerformance() {
		List<Player> playerPerformanceOrder = new ArrayList<>();
		for (String user : party.getUsers()) {
			TheatrePartyManager.instance().forUsername(user).ifPresent(player -> {
				playerPerformanceOrder.add(player);
			});
		}

		// Sort the list based on supplyChestDamage in descending order
		playerPerformanceOrder.sort(Comparator.comparingInt(Player::getSupplyChestDamage).reversed());

		return playerPerformanceOrder;
	}

	@Override
	public void registerObjects() {

	}

	@Override
	public void registerNpcs() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Position> getSpectatorSpots() {
		final ArrayList<Position>[] positions = new ArrayList[] { new ArrayList<>() };
		Optional<String> userId = party.getUsers().stream().findAny();
		TheatrePartyManager.instance().forUsername(userId.get()).ifPresent(player -> {
			switch (player.theatreOfBloodStage) {
				case 0:
					positions[0] = Lists.newArrayList(
							Position.of(convertX(3167), convertY(4460), 0),
							Position.of(convertX(3166), convertY(4460), 0),
							Position.of(convertX(3166), convertY(4433), 0),
							Position.of(convertX(3167), convertY(4433), 0));
					break;
				case 1:
					positions[0] = Lists.newArrayList(
							Position.of(convertX(3297), convertY(4435), 0),
							Position.of(convertX(3298), convertY(4435), 0),
							Position.of(convertX(3296), convertY(4435), 0),
							Position.of(convertX(3295), convertY(4435), 0));
					break;
				case 2:
					positions[0] = Lists.newArrayList(
							Position.of(convertX(3301), convertY(4240), 0),
							Position.of(convertX(3290), convertY(4240), 0));
					break;
				case 3:
					positions[0] = Lists.newArrayList(
							Position.of(convertX(3270), convertY(4314), 0),
							Position.of(convertX(3270), convertY(4313), 0),
							Position.of(convertX(3289), convertY(4313), 0),
							Position.of(convertX(3289), convertY(4314), 0));
					break;
				case 4:
					positions[0] = Lists.newArrayList(
							Position.of(convertX(3157), convertY(4387), 1),
							Position.of(convertX(3183), convertY(4387), 1));
					break;
				case 5:
					positions[0] = Lists.newArrayList(
							Position.of(convertX(3157), convertY(4325), 0),
							Position.of(convertX(3159), convertY(4325), 0),
							Position.of(convertX(3161), convertY(4325), 0),
							Position.of(convertX(3175), convertY(4325), 0),
							Position.of(convertX(3177), convertY(4325), 0),
							Position.of(convertX(3179), convertY(4325), 0));
					break;
			}
		});
		return Lists.newArrayList(
				Position.of(convertX(3270), convertY(4314), 0),
				Position.of(convertX(3270), convertY(4313), 0),
				Position.of(convertX(3289), convertY(4313), 0),
				Position.of(convertX(3289), convertY(4314), 0));
	}

	@Override
	public Position getEntrance() {
		return Position.of(3279, 4293);
	}

}
