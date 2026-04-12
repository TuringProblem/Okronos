package io.ruin.model.activities.bosses.instancetoken;

import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.dynamic.DynamicMap;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class MapHandler {
	@Setter public DynamicMap map;
	@Setter public Player host;

	protected String password;
	protected boolean passwordActive = false;
	@Getter protected List<Player> players = new ArrayList<>();
	@Getter protected List<NPC> npcs = new ArrayList<>();

	public abstract void init();

	/**
	 * Handles a player's request to join the current map instance. If password protection
	 * is enabled, it prompts the player to enter the password. If the correct password is provided,
	 * the player is moved to the instance. Otherwise, they are notified of the incorrect password.
	 * If password protection is not active, the player is moved to the instance directly.
	 *
	 * @param player The player requesting to join the current map instance.
	 */
	public void requestJoin(Player player) {
		if (passwordActive) {
			player.stringInput("Enter the password for the instance.", s -> {
				if (s.equals(password))
					movePlayerToInstance(player);
				else {
					player.sendMessage("The password you entered was incorrect.");
				}
			});
		}
		else
			movePlayerToInstance(player);
	}

	/**
	 * Moves the specified player to the current instance.
	 * <p>
	 * This method adds the player to the instance's players list, sends
	 * specific warning messages to the player depending on whether they are
	 * the host, removes a required token from the player's inventory, and
	 * assigns a listener to handle the player's exit from the map.
	 *
	 * @param player The player to be moved to the current instance. The player
	 *               will receive a notification regarding the potential consequences
	 *               of leaving the instance.
	 */
	public void movePlayerToInstance(Player player) {
		players.add(player);
		if (!player.getName().equals(host.getName())) {
			player.sendMessage(Color.RED.wrap("Warning")
				.concat(": If you leave you will have to use another instance token to return."));
			player.getInventory().remove(7478, 1);
		}
		else
			player.sendMessage(Color.RED.wrap("Warning")
				.concat(": If you leave your instance will be destroyed."));

		map.assignListener(player)
			.onExit(this::defaultMapExit);
	}

	/**
	 * Moves the specified player to a solo instance.
	 * <p>
	 * This method adds the player to the players list, sends a warning message
	 * to the player about the consequences of leaving the instance, and assigns
	 * a listener for the player to handle their exit from the map.
	 *
	 * @param player The player to move to the solo instance.
	 */
	public void movePlayerToSoloInstance(Player player) {
		players.add(player);
		player.sendMessage(Color.RED.wrap("Warning")
			.concat(": If you leave your instance will be destroyed."));
		map.assignListener(player)
			.onExit(this::defaultMapExit);
	}

	/**
	 * Sets the password for the current instance and activates the password protection.
	 *
	 * @param password The password to be set for the instance. This will be required
	 *                 for players to join the instance if password protection is enabled.
	 */
	public void setPassword(String password) {
		this.password = password;
		passwordActive = true;
	}


	/**
	 * Destroys the current instance and performs the necessary cleanup tasks.
	 * <p>
	 * This method is responsible for:
	 * <l>
	 * 	<li> Removing all NPCs from the instance.</li>
	 * 	<li> Teleporting all players back to their home location, notifying them that the instance is terminated.</li>
	 * 	<li> Destroying the associated dynamic map.</li>
	 * 	<li> Deregistering and removing the instance from the global instance manager.</li>
	 * </l>
	 * <p>
	 * This method should be called when the host leaves the instance, either intentionally
	 * or due to other circumstances that require the instance to be shut down.
	 */
	public void destroy() {
		log.debug("Destroying instance for host: {}.", host.getName());

		log.debug("Removing {} NPCs from the instance.", npcs.size());
		npcs.forEach(NPC::remove);
		npcs.clear();

		log.debug("Removing {} players from the instance.", players.size());
		players.forEach(plr -> {
			plr.getMovement().teleport(World.HOME);
			plr.sendMessage("The host has left the instance, you have been sent home.");
			World.checkCannon(plr);
		});
		players.clear();

		if (map != null) {
			log.debug("Destroying the associated dynamic map.");
			map.destroy();
			map = null;
		}

		InstanceManager.removeInstance(host.getName().toLowerCase());
		log.debug("Total remaining free regions: {}", DynamicMap.FREE_REGIONS_SIZE());
	}

	/**
	 * Handles the default behavior when a player exits the dynamic map, either by logging out
	 * or otherwise leaving the instance. Ensures the player is removed from the instance
	 * and performs cleanup if the host leaves.
	 *
	 * @param player The player who is exiting the map.
	 * @param logout Indicates whether the exit is due to a logout.
	 */
	protected void defaultMapExit(Player player, boolean logout) {
		if (logout)
			player.getMovement().teleport(World.HOME);
		players.remove(player);
		player.currentMapHandler = null;
		if (player.getName().equals(host.getName()))
			destroy();
		player.currentDynamicMap = null;
		player.inDynamicMap = false;
	}

	public boolean isCannonRestricted() {
		return false;
	}
}
