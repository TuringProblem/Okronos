package io.ruin.model.activities.raids.tob.dungeon;

import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.raids.tob.dungeon.room.*;
import io.ruin.model.activities.raids.tob.party.TheatreParty;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap.DynamicMapBuildException;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.util.ListenedList;
import io.ruin.util.ListenedMap;
import io.ruin.utility.CS2Script;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ReverendDread on 7/24/2020 https://www.rune-server.ee/members/reverenddread/
 * @project Kronos
 */
@Getter
@Setter
@Slf4j
public class TheatreDungeon {

	/**
	 * List of rooms in the dungeon.
	 */
	public final ListenedMap<RoomType, TheatreRoom> rooms = new ListenedMap<>();

	/**
	 * List of rooms that have been started.
	 */
	private final ListenedList<RoomType> started = new ListenedList<>();

	/**
	 * The party assosiated with the dungeon.
	 */
	private final TheatreParty party;

	/**
	 * If the dungeon has been built.
	 */
	private boolean built;

	/**
	 * @param party
	 */
	public TheatreDungeon(TheatreParty party) {
		this.party = party;
		party.overallTimer = new ActivityTimer();
		rooms.postAdd(room -> {
			room.onLoad();
			room.registerObjects();
			room.registerNpcs();
			room.addMultiArea();
		});
		rooms.preRemove(room -> {
			room.removeMultiArea();
			room.destroy();
		});
	}

	public void destroy() {
		rooms.values().forEach(room -> {
			if (room != null) {
				room.removeMultiArea();
				room.destroy();
			}
		});
		rooms.clear();
		setBuilt(false);
	}

	/**
	 * Builds the dungeon.
	 */
	public void build() throws DynamicMapBuildException {
		if (!isBuilt()) {
			rooms.put(RoomType.MAIDEN, new MaidenRoom(party));
			rooms.put(RoomType.BLOAT, new BloatRoom(party));
			rooms.put(RoomType.VASILIAS, new VasiliasRoom(party));
			rooms.put(RoomType.SOTETSEG, new SotetsegRoom(party));
			rooms.put(RoomType.XARPUS, new XarpusRoom(party));
			rooms.put(RoomType.VERZIK, new VerzikRoom(party));
			rooms.put(RoomType.TREASURE, new TreasureRoom(party));
			setBuilt(true);
		}
	}

	/**
	 * Moves a player into the converted position for the dungeon.
	 *
	 * @param player
	 * @param type
	 * @param position
	 */
	public void move(Player player, RoomType type, Position position) {
		player.getMovement().teleport(
				rooms.get(type).convertX(position.getX()),
				rooms.get(type).convertY(position.getY()),
				position.getZ());
		player.teleportListener = TheatreDungeon::allowTeleport;
		rooms.get(type).assignMapListener(player);
		if (type == RoomType.TREASURE) {
			var chestVarp = getChestVarp(player.tobChestId);
			if (chestVarp == null) {
				log.error("Unknown chest varp: " + player.tobChestId);
				return;
			}
			chestVarp.set(player, party.getPurpleLootChests().contains(player.tobChestId) ? 3 : 2);
			player.getPosition().getRegion().players.forEach(p -> {
				if (!p.getName().equalsIgnoreCase(player.getName())) {
					chestVarp.set(p, party.getPurpleLootChests().contains(player.tobChestId) ? 1 : 0);
				}
			});
		}
	}

	VarPlayerRepository getChestVarp(int id) {
		return switch (id) {
			case 33086 -> VarPlayerRepository.THEATRE_CHEST_33086;
			case 33087 -> VarPlayerRepository.THEATRE_CHEST_33087;
			case 33088 -> VarPlayerRepository.THEATRE_CHEST_33088;
			case 33089 -> VarPlayerRepository.THEATRE_CHEST_33089;
			case 33090 -> VarPlayerRepository.THEATRE_CHEST_33090;
			case 33091 -> VarPlayerRepository.THEATRE_CHEST_33091;
			default -> null;
		};
	}

	public static boolean allowTeleport(Player player) {
		player.sendMessage("You can't teleport away from the Theatre.");
		return false;
	}

	/**
	 * Makes a player enter a certain room.
	 *
	 * @param player
	 * @param type
	 */


	public void enterRoom(Player player, RoomType type) {
		player.startEvent(e -> {
			// CS2Script.TOB_HUD_PORTAL.sendScript(player, "The Theatre awaits...");
			// CS2Script.TOB_HUD_FADE.sendScript(player, 1, 1, type.getPortalText());
			move(player, type, rooms.get(type).getEntrance());
		});
	}

	/**
	 * Checks if the desired room type has been started.
	 *
	 * @param type
	 * @return
	 */
	public boolean isStarted(RoomType type) {
		return started.contains(type);
	}

}
