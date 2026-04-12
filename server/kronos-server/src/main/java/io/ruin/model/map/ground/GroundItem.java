package io.ruin.model.map.ground;

import discord.webhooks.logs.GroundItemHook;
import io.ruin.HooksV2;
import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.services.Loggers;
import io.ruin.utility.Utils;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

import core.task.Continuations;
import org.json.JSONObject;

import static core.task.api.API.*;

import static io.ruin.model.item.actions.impl.storage.LootingBag.CLOSED_LOOTING_BAG;
import static io.ruin.model.item.actions.impl.storage.LootingBag.OPENED_LOOTING_BAG;

@ToString
@Slf4j
public class GroundItem extends Position {
	public static interface Hook {
		record Pickup(Player player, GroundItem item) implements Hook {
		};
	}

	public static HooksV2<Hook> hooks = new HooksV2<>(Hook.class);

	public String originalOwner;
	public String activeOwner;
	public String diedToIron;
	public int id;
	public int amount;
	public Tile tile;
	private int respawnMinutes;
	private long timeDropped;
	private String dropperName, dropperIp;
	private Map<String, String> attributes;

	public GroundItem(Item item) {
		this(item.getId(), item.getAmount(), item.copyOfAttributes());
	}

	public GroundItem(int id, int amount) {
		this(id, amount, null);
	}

	public GroundItem(int id, int amount, Map<String, String> attributes) {
		super(0, 0, 0);
		this.id = id;
		this.amount = amount;
		this.attributes = attributes;
	}

	public GroundItem owner(Player player) {
		return owner(player.getName());
	}

	public GroundItem owner(String ownerId) {
		this.originalOwner = ownerId;
		this.activeOwner = ownerId;
		return this;
	}

	public GroundItem diedToIron(Player player) {
		return diedToIron(player.getName());
	}

	public GroundItem diedToIron(String ownerId) {
		this.diedToIron = ownerId;
		return this;
	}

	public GroundItem position(Position pos) {
		return position(pos.getX(), pos.getY(), pos.getZ());
	}

	public GroundItem position(int x, int y, int z) {
		this.set(x, y, z);
		return this;
	}

	public boolean droppedByIronPlayer(Player player) {
		if (originalOwner != null && !originalOwner.isEmpty() && !originalOwner.equalsIgnoreCase(player.getName())) {
			return false;
		}
		return true;
	}

	/**
	 * Spawning - goes global after 60 seconds
	 */

	public GroundItem spawn() {
		return spawn(1);
	}

	/**
	 * wont appear globally, but will despawn as normal after eclapsed
	 * {@link #getDespawnTime()}
	 *
	 * @return
	 */
	public GroundItem spawnPrivate() {
		return spawn(-1);
	}

	public GroundItem spawn(int appearMinutes) {
		Tile.get(getX(), getY(), getZ(), true).addItem(this);
		if (appearMinutes != 0) {
			boolean allowAppear = appearMinutes > 0 && activeOwner != null && !activeOwner.isEmpty()
					&& ObjType.get(id).tradeable;

			queue(() -> {
				sleep((int) ((Math.abs(appearMinutes) * 60000L) / 600));
				if (allowAppear) {
					Continuations.schedule(this::appear);
				}
				sleep((int) ((getDespawnTime() * 60000L) / 600));
				queue(this::disappear);
			});
		}
		return this;
	}

	public GroundItem spawnPublic() {
		Tile.get(getX(), getY(), getZ(), true).addItem(this);
		queue(() -> {
			queue(this::appear);
			sleep((int) ((getDespawnTime() * 60000L) / 600));
			queue(this::disappear);
		});
		return this;
	}

	public GroundItem spawnWithRespawn(int respawnMinutes) {
		this.respawnMinutes = respawnMinutes;
		return spawn(0);
	}

	/**
	 * Appear
	 */

	private void appear() {
		if (isRemoved()) {
			/* this is possible because the task never gets stopped! */
			return;
		}
		sendRemove();
		activeOwner = null;
		sendAdd();
	}

	/**
	 * Disappear
	 */

	private void disappear() {
		if (isRemoved()) {
			/* this is possible because the task never gets stopped! */
			return;
		}
		remove();
	}

	private int getDespawnTime() {
		return Tile.get(getX(), getY(), getZ(), true).region.dynamicData != null ? 15 : 1; // ORIGINAL: 60 : 2
	}

	/**
	 * Remove
	 */

	public void remove() {
		// Warning: This MAY null if isRemoved isn't checked first!
		if (tile == null)
			throw new RuntimeException(
					"gitem null tile! " + this + " " + getClass().getName() + '@' + Integer.toHexString(hashCode()));
		tile.removeItem(this);
	}

	public boolean isRemoved() {
		return tile == null;
	}

	/**
	 * Pickup
	 */

	public void pickup(Player player, int distance) {
		if (isRemoved()) {
			player.sendMessage("Can't pick up item not spawned for you.");
			return;
		}

		if (activeOwner != null && !activeOwner.isEmpty() && !activeOwner.equalsIgnoreCase(player.getName())) {
			player.sendMessage("Can't pick up item not spawned for you.");
			return;
		}

		if (diedToIron != null && !diedToIron.isEmpty() && !diedToIron.equalsIgnoreCase(player.getName())) {
			return;
		}

		if (hooks.handle(new Hook.Pickup(player, this))) {
			return;
		}

		if (player.getGameMode().isIronMan()
				&& originalOwner != null
				&& !originalOwner.isEmpty()
				&& !originalOwner.equalsIgnoreCase(player.getName())) {
			player.sendMessage("Ironmen cannot pick up items dropped by or for other players.");
			return;
		}

		if (player.getDuel().stage >= 4) {
			player.sendMessage("You can't pickup items in a duel.");
			return;
		}

		if (player.joinedTournament) {
			player.sendMessage("You can't pickup items while you're signed up for a tournament.");
			return;
		}

		boolean hasLootingBag = player.getBank().hasId(CLOSED_LOOTING_BAG) || player.getInventory().hasId(CLOSED_LOOTING_BAG)
				|| player.getInventory().hasId(OPENED_LOOTING_BAG) || player.getBank().hasId(OPENED_LOOTING_BAG);
		boolean isLootingBag = id == CLOSED_LOOTING_BAG || id == OPENED_LOOTING_BAG;

		if (hasLootingBag && isLootingBag) {
			player.sendMessage("You already have a looting bag!");
			return;
		}
		// DTO for pickup logs
		var object = new JSONObject();
			object.put("player", player.getName());
			object.put("item_name", ObjType.get(id).name);
			object.put("item_amount", NumberUtils.formatNumber(amount));
			object.put("item_noted", ObjType.get(id).isNote());
			object.put("pickup_x", getX());
			object.put("pickup_y", getY());
			object.put("pickup_z", getZ());

		// looting bag logic
		if (player.getInventory().hasId(OPENED_LOOTING_BAG)) {
			if (!player.getLootingBag().isFull()) {
				if (player.wildernessLevel > 0) {

					if (player.getLootingBag().add(id, amount, attributes) == 0) {
						player.sendMessage("Not enough space in your looting bag, added item to your inventory instead.");
						player.getInventory().addOrDrop(id, amount, attributes);
						remove();
						// These logs are ONLY triggered if the item was NOT added to the bag
						Loggers.logPickup(player.getUserId(), player.getName(), player.getIp(), id, amount, getX(), getY(), getZ());
						if (getTimeDropped() > 0) { // this item was manually dropped by someone, log as trade
							Loggers.logDropTrade(player.getName(), originalOwner, player.getIp(), getDropperIp(), player.getName(),
								getDropperName(), id, amount, getX(), getY(), getZ(), getTimeDropped());
//							RareDropEmbedMessage.sendPickupLogsToDiscord(player, new Item(id, amount), true);
							GroundItemHook.sendPickupLogsToDiscord(object, true);
						}
						return;
					}
					else {
						log.debug("Added {} x {} to looting bag ", Utils.formatMoneyString(amount), ObjType.get(id).name);
						// Added Pickup log here
						Loggers.logPickup(player.getUserId(), player.getName(), player.getIp(), id, amount, getX(), getY(), getZ());
						if (getTimeDropped() > 0) { // someone manually dropped this item, log as trade
							Loggers.logDropTrade(player.getName(), originalOwner, player.getIp(), getDropperIp(), player.getName(),
								getDropperName(), id, amount, getX(), getY(), getZ(), getTimeDropped());
//							RareDropEmbedMessage.sendPickupLogsToDiscord(player, new Item(id, amount), true);
							GroundItemHook.sendPickupLogsToDiscord(object, true);
						}
					}
				}
				else {
					log.debug("{} x {} was not added as player is not in wildy ", Utils.formatMoneyString(amount), ObjType.get(id).name);
					player.getInventory().addOrDrop(id, amount, attributes);
				}
			}
			else {
				log.debug("{} x {} was not added as player loot bag is full ", Utils.formatMoneyString(amount), ObjType.get(id).name);
				player.getInventory().addOrDrop(id, amount, attributes);
			}
		}
		else if (player.getInventory().add(id, amount, attributes) == 0) {
			player.sendMessage("Not enough space in your inventory.");
			return;
		}
		remove();
		if (distance > 0)
			player.animate(832);
		player.privateSound(2582);
		if (respawnMinutes > 0) {
			queue(() -> {
				sleep((int) ((respawnMinutes * 60000L) / 600));
				queue(() -> this.spawnWithRespawn(respawnMinutes));
			});
		}
		Loggers.logPickup(player.getUserId(), player.getName(), player.getIp(), id, amount, getX(), getY(), getZ());
		if (getTimeDropped() > 0) { // this item was manually dropped by someone, log as trade
			Loggers.logDropTrade(player.getName(), originalOwner, player.getIp(), getDropperIp(), player.getName(),
					getDropperName(), id, amount, getX(), getY(), getZ(), getTimeDropped());
//			RareDropEmbedMessage.sendPickupLogsToDiscord(player, new Item(id, amount), true);
			GroundItemHook.sendPickupLogsToDiscord(object, true);
		}
	}

	/**
	 * Sending
	 */

	public void sendAdd() {
		for (Player player : tile.region.players) {
			if (activeOwner == null || activeOwner.isEmpty()) {
				player.getPacketSender().sendGroundItem(this);
				continue;
			}
			if (activeOwner.equalsIgnoreCase(player.getName())) {
				player.getPacketSender().sendGroundItem(this);
				return;
			}
		}
	}

	public void sendAdd(Player player) {
		player.getPacketSender().sendGroundItem(this);
	}

	public void sendRemove(Player player) {
		player.getPacketSender().sendRemoveGroundItem(this);
	}

	public void sendRemove() {
		for (Player player : tile.region.players) {
			if (activeOwner == null || activeOwner.isEmpty()) {
				player.getPacketSender().sendRemoveGroundItem(this);
				continue;
			}
			if (activeOwner.equalsIgnoreCase(player.getName())) {
				player.getPacketSender().sendRemoveGroundItem(this);
				return;
			}
		}
	}

	public void sendUpdate(int previousAmount) {
		if (tile == null || tile.region == null) {
			throw new RuntimeException("grounditem NPE " + this);
		}

		for (Player player : tile.region.players) {
			if (activeOwner == null || activeOwner.isEmpty()) {
				this.sendUpdate(player, previousAmount);
				continue;
			}

			if (activeOwner.equalsIgnoreCase(player.getName())) {
				this.sendUpdate(player, previousAmount);
				return;
			}
		}
	}

	private void sendUpdate(Player player, int previousAmount) {
		player.getPacketSender().sendGroundItemUpdate(this, previousAmount);
	}

	/**
	 * For logging
	 */
	public GroundItem droppedBy(Player player) {
		timeDropped = System.currentTimeMillis();
		dropperName = player.getName();
		dropperIp = player.getIp();
		return this;
	}

	public String getDropperName() {
		return dropperName;
	}

	public String getDropperIp() {
		return dropperIp;
	}

	public long getTimeDropped() {
		return timeDropped;
	}

	@Override
	public boolean equals(Object other) {
		// NOTE: the equality is instance based, no matter what changes to the state
		return other == this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), originalOwner, activeOwner, diedToIron, id, amount, tile, respawnMinutes,
				timeDropped, dropperName, dropperIp, attributes);
	}
}
