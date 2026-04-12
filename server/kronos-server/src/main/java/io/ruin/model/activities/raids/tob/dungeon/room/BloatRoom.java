package io.ruin.model.activities.raids.tob.dungeon.room;

import com.google.common.collect.Lists;
import io.ruin.api.utils.Random;
import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.activities.raids.tob.dungeon.boss.bloat.BloatCombat;
import io.ruin.model.activities.raids.tob.party.TheatreParty;
import io.ruin.model.activities.raids.tob.party.TheatrePartyManager;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.GameObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author NuLL on 28/12/2021
 *         https://www.rune-server.ee/members/null1001/
 * @project Drako
 */
public class BloatRoom extends TheatreRoom {

	public NPC bloat;

	public BloatRoom(TheatreParty party) throws DynamicMapBuildException {
		super(party);
	}

	@Override
	public void onLoad() {
		try {
			build(13125, 1);
		} catch (DynamicMap.DynamicMapBuildException e) {
			throw new RuntimeException(e);
		}

		GameObject chest = GameObject.spawn(32758, convertX(3278), convertY(4449), 0, 10, 0);

		bloat = new NPC(8359).spawn(convertX(3299), convertY(4448), 0, Direction.SOUTH, 0);
		bloat.setIgnoreMulti(true);
		bloat.getCombat().setAllowRetaliate(false);
		party.scaleNPC(bloat, false);
		addNpc(bloat);

		bloat.deathStartListener = (DeathListener.SimpleKiller) killer -> {
			party.bloatTimerEnd = party.bloatTimer.stop(killer.player, 0);
			AtomicBoolean allWearingSalve = new AtomicBoolean(true);
			for (String user : party.getUsers()) {
				TheatrePartyManager.instance().forUsername(user).ifPresent(p -> {
					if (p.getEquipment().get(Equipment.SLOT_AMULET) != null
							&& p.getEquipment().get(Equipment.SLOT_AMULET).getId() != ItemID.SALVE_AMULET
							&& p.getEquipment().get(Equipment.SLOT_AMULET).getId() != ItemID.SALVE_AMULET_E
							&& p.getEquipment().get(Equipment.SLOT_AMULET).getId() != ItemID.SALVE_AMULETEI
							&& p.getEquipment().get(Equipment.SLOT_AMULET).getId() != ItemID.SALVE_AMULETI) {
						allWearingSalve.set(false);
					}
				});
			}
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
					if (allWearingSalve.get()) {
						Objects.requireNonNull(p.combatAchievementsList
								.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.APPROPRIATE_TOOLS.ordinal()))
								.getCombatAchievement()).check(p);
					}
					BloatCombat bloatCombat = (BloatCombat) bloat.getCombat();
					if (!bloatCombat.wearingBarrows)
						p.currentParty.wearingBarrows = false;
					if (!bloatCombat.damagedPlayer && !party.deadPlayers.contains(p)) {
						p.currentParty.perfectBloat = true;
						Objects.requireNonNull(p.combatAchievementsList
								.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_BLOAT.ordinal()))
								.getCombatAchievement()).check(p);
					}
					if (bloatCombat.downs < 3 && !party.deadPlayers.contains(p)) {
						if (!party.deadPlayers.contains(p)) {
							Objects.requireNonNull(p.combatAchievementsList
									.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.TWO_DOWN.ordinal()))
									.getCombatAchievement()).check(p);
						}
					}
					p.theatreOfBloodStage = 2;
					p.sendFilteredMessage("You have completed stage 2!");
					p.getCombat().restore();
				});
			}
			bloat.getCombat().setAllowRespawn(false);
			World.startEvent(e -> {
				party.deadPlayers.forEach(p -> {
					p.sendMessage("You will be moved soon..");
				});
				e.delay(12);
				party.deadPlayers.forEach(p -> {
					p.getMovement().teleport(convertX(3303), convertY(4447), 0);
					p.setInvincible(false);
					p.unlock();
					p.getCombat().restore();
					p.supplyChestPoints = 0;
				});
				party.deadPlayers.clear();
			});
			for (String user : party.getUsers()) {
				TheatrePartyManager.instance().forUsername(user).ifPresent(player -> {
					if (party.getSupplyChestDeath().get(player))
						player.supplyChestPoints = 0;
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

	@Override
	public List<Position> getSpectatorSpots() {
		var region = bloat.getPosition().getRegion();
		return Lists.newArrayList(
				Position.of(region.baseX + 31, region.baseY + 20),
				Position.of(region.baseX + 32, region.baseY + 20),
				Position.of(region.baseX + 31, region.baseY + 43),
				Position.of(region.baseX + 32, region.baseY + 43)

		);
	}

	@Override
	public Position getEntrance() {
		return Position.of(3321, 4448, 0);
	}

}
