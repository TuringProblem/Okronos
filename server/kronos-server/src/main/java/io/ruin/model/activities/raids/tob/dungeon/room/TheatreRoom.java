package io.ruin.model.activities.raids.tob.dungeon.room;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.raids.tob.dungeon.RoomType;
import io.ruin.model.activities.raids.tob.dungeon.TheatreBoss;
import io.ruin.model.activities.raids.tob.party.TheatreParty;
import io.ruin.model.activities.raids.tob.party.TheatrePartyManager;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.map.MultiZone;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.var.VarPlayerRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author NuLL on 28/12/2021
 *         https://www.rune-server.ee/members/null1001/
 * @project Drako
 */
@Slf4j
public abstract class TheatreRoom extends DynamicMap {

	protected int lastSpectatorSpot;
	protected final TheatreParty party;
	protected TheatreBoss boss;

	protected TheatreRoom(TheatreParty party) throws DynamicMapBuildException {
		this.party = party;
	}

	// Returns the entrance spot for the room.
	public abstract Position getEntrance();

	// Called upon a room being loaded for the first time.
	public abstract void onLoad();

	// Used to register object listeners.
	public abstract void registerObjects();

	public abstract void registerNpcs();

	// Returns a list of spectator spots.
	public abstract List<Position> getSpectatorSpots();

	/**
	 * Assigns the map listener for this area.
	 *
	 * @param player
	 */
	public void assignMapListener(Player player) {
		assignListener(player)
				.onExit((p, logout) -> {
					p.closeInterface(ToplevelComponent.OVERLAY);
					p.getCombat().init(p);
				});
		player.openInterface(ToplevelComponent.OVERLAY, Interface.TOB_PARTY_MEMBERS_OVERLAY);
		player.theatreRoom = this;
	}

	void onEntry(Player player) {
	}

	private void failRaid(Player player) {
		player.getMovement().teleport(3671, 3219, 0);
		player.getCombat().restore();
		player.getCombat().setDead(false);
		player.setInvincible(false);
		player.getCombat().init(player);
		player.deathEndListener = null;
		player.deathStartListener = null;
		player.teleportListener = null;
		player.inTob = false;

		// Add finishRaid call
		TheatrePartyManager.instance()
			.getPartyForPlayer(player.getName())
			.ifPresent(p -> {
				p.deadPlayers.clear();
				p.finishRaid();
			});
		player.sendMessage("Your team has been wiped and you have been teleported back to the lobby.");
		player.unlock();
	}

	/**
	 * Adds multi to this area.
	 */
	public void addMultiArea() {
		Stream.of(getRegions()).filter(Objects::nonNull).forEach(region -> MultiZone.add(region.getBounds()));
	}

	/**
	 * Removes multi to this area.
	 */
	public void removeMultiArea() {
		Stream.of(getRegions()).filter(Objects::nonNull).forEach(region -> MultiZone.remove(region.getBounds()));
	}

	/**
	 * Handles a player's death while in this area.
	 *
	 * @param player
	 */
	public void handlePlayerDeath(Player player) {
		var treasureRoom = party.dungeon.rooms.get(RoomType.TREASURE);
		if (treasureRoom != null && player.getPosition().region() == treasureRoom.swRegion.id) {
			System.out.printf("treasure room id: %d", treasureRoom.swRegion.id);
			player.getCombat().restore();
			player.setInvincible(true);
			player.getCombat().setTruelyDead(false);
			player.getCombat().setDead(false);
			player.unlock();
		} else {
			int randomIndex = Random.get(getSpectatorSpots().size() - 1);
			player.getMovement().teleport(getSpectatorSpots().get(randomIndex));
			lastSpectatorSpot++;
		}
	}

	public void startDeathEvent(Player player) {
		if (party.deadPlayers.contains(player) || player.theatreOfBloodStage == 6) {
			player.getCombat().restore();
			return;
		}
		World.startEvent(e -> {
			player.getCombat().setDead(true);
			party.deaths++;
			party.getPlayerDeaths().put(player, party.getPlayerDeaths().get(player) + 1);
			party.getSupplyChestDeath().put(player, true);
			player.tobDamageDealt *= 0.8f;
			player.lock(LockType.FULL_NULLIFY_DAMAGE);
			e.delay(1);
			player.animate(836);
			e.delay(4);
			player.resetAnimation();
			if (!party.deadPlayers.contains(player))
				party.deadPlayers.add(player);
			player.getCombat().restore();
			player.setInvincible(true);
			player.getCombat().setTruelyDead(false);
			player.getCombat().setDead(false);
			player.unlock();
			player.sendMessage("Oh dear, you have died within the theatre.");
			// Check if we're finished the raid
			if (party.deadPlayers.size() >= party.playersInRaid.size()) {
				failRaid(player);
				return;
			}
			// else we spectate and pray the rest of the team is better than the dead
			// player...
			player.sendMessage("...it's up to your team now.");
			VarPlayerRepository.THEATRE_OF_BLOOD.set(player, 3); // set to spectator
			handlePlayerDeath(player);
		});
	}
}
