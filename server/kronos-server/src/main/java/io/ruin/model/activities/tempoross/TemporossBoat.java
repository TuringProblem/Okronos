package io.ruin.model.activities.tempoross;

import io.ruin.model.World;
import io.ruin.model.activities.pestcontrol.PestControlGame;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.LogoutListener;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.map.object.GameObject;
import io.ruin.utility.TickDelay;

import java.util.ArrayList;

public class TemporossBoat {

	/**
	 * The lowest amount of participants required to play this activity.
	 */
	private static final int MINIMUM_PARTY_SIZE = 1;

	/**
	 * The collection of players awaiting within this boat.
	 */
	private final ArrayList<Player> players = new ArrayList<>();

	public Tempoross game;

	private TemporossGameSettings settings;

	/**
	 * The delay until the next {@link PestControlGame} is attempted to dispatch.
	 */
	public TickDelay nextDeparture = new TickDelay();


	TemporossBoat(TemporossGameSettings settings) {
		World.startEvent(event -> {
			while (true) {
				event.delay(5);
				pulse();
			}
		});
	}

	private void pulse() {
		if (lobbySize() >= MINIMUM_PARTY_SIZE || nextDeparture.finished()) {
			if (lobbySize() < MINIMUM_PARTY_SIZE) {
				nextDeparture.delay(1000 * 60 * 2 / 600);
			} else {
				if (game == null || game.ended()) {
					startGame();
				}
			}
		}
	}


	private void startGame() {
		game = new Tempoross(settings);
		game.start(players);
		players.clear();
	}


	public void join(Player player, GameObject ladder) {
		if (players.stream().anyMatch(p -> p == player)) {
			return;
		}

		player.lock();
		players.add(player);
		player.getMovement().teleport(3133, 2840);
		player.joinedTempoross = true;
//        player.openInterface(InterfaceType.PRIMARY_OVERLAY, OVERLAY);
//        player.getPacketSender().sendString(OVERLAY, 21, settings.title());
//        player.getPacketSender().sendString(OVERLAY, 6, "Points: "+ player.pestPoints);
//        updateOverlay();
		player.teleportListener = p -> {
			leave(p);
			return true;
		};
		player.logoutListener = new LogoutListener().onLogout(p -> {
			leave(p);
			p.getMovement().teleport(settings.exitTile());
		});
		player.unlock();
	}

	/**
	 * Processes all actions upon the player leaving this boat.
	 *
	 * @param player
	 * @return
	 */
	public void leave(Player player) {
		player.lock();
		players.removeIf(p -> p == player);
		player.closeInterface(ToplevelComponent.WILDERNESS_OVERLAY);
		player.logoutListener = null;
		player.teleportListener = null;
		player.unlock();
		player.joinedTempoross = false;
		player.getMovement().teleport(3138, 2840, 0);
//        updateOverlay();
	}

	/**
	 * The current lobby size for this Pest Control boat.
	 *
	 * @return
	 */
	private int lobbySize() {
		return players.size();
	}

}
