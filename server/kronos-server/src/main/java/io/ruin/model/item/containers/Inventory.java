package io.ruin.model.item.containers;

import io.ruin.cache.ObjType;
import io.ruin.model.entity.shared.Movement;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;
import io.ruin.model.map.ground.GroundItem;

import java.math.BigInteger;
import java.util.Map;

public class Inventory extends ItemContainer {

	public transient double weight;

	public void addOrSendToBank(int id, BigInteger amount) {
		addOrSendToBank(id, amount.intValueExact());
	}

	public void addOrSendToBank(int id, int amount) {
		Item item = new Item(id, amount);
		var remaining = amount - add(id, amount);

		if (remaining == 0) {
			return;
		}

		if (player.getDuel().stage >= 4) {
			return;
		}

		var banked = player.getBank().deposit(id, remaining);
		player.sendFilteredMessage(banked + "x " + item.getDef().name + " has been sent to your bank.");
		remaining -= banked;
		if (remaining != 0) {
			player.getInventory().addOrDrop(id, remaining, null);
			return;
		}
	}

	public void addOrDrop(Item item) {
		addOrDrop(item.getId(), item.getAmount(), item.copyOfAttributes());
	}

	public void addOrDrop(int id, int amount) {
		addOrDrop(id, amount, null);
	}

	@Override
	public Inventory clone() {
		Inventory container = new Inventory();
		Item[] items = new Item[this.items.length];
		for (int id = 0; id < items.length; id++) {
			items[id] = this.items[id] == null ? null
					: new Item(this.items[id].getId(), this.items[id].getAmount());
		}
		container.weight = weight;
		container.items = items;
		container.updatedCount = updatedCount;
		container.updatedSlots = updatedSlots;
		container.sendAll = sendAll;
		container.player = player;
		container.interfaceHash = interfaceHash;
		container.containerId = containerId;
		container.forceStack = forceStack;
		return container;
	}

	public void addOrDrop(int id, int amount, Map<String, String> attributes) {
		if (add(id, amount, attributes) == amount) {
			/* added normally */
			return;
		}
		if (player.isAdmin()) {
			player.sendMessage("Not enough space to spawn item (" + id + ", " + amount + ")");
			// return;
		}
		if (player.getDuel().stage >= 4) {
			// player.sendMessage("You can't drop items in a duel.");
			return;
		}
		if (player.joinedTournament) {
			// player.sendMessage("You can't drop items while you're signed up for a
			// tournament.");
			return;
		}
		Movement movement = player.getMovement();
		int x, y, z;
		if (movement.isTeleportQueued()) {
			x = movement.teleportX;
			y = movement.teleportY;
			z = movement.teleportZ;
		} else {
			x = player.getAbsX();
			y = player.getAbsY();
			z = player.getHeight();
		}
		new GroundItem(id, amount, attributes).owner(player).position(x, y, z).spawn();
	}

	@Override
	public boolean sendUpdates() {
		if (!super.sendUpdates())
			return false;
		weight = 0;
		for (Item item : items) {
			if (item == null)
				continue;
			ObjType def = item.getDef();
			if (def == null)
				continue;
			weight += def.weightInventory;
		}
		return true;
	}

	public boolean removeCoinsOrPlatTokens(BigInteger amount) {
		var totalCoinsInInv = BigInteger.valueOf(getAmount(995))
				.add(BigInteger.valueOf(getAmount(13204)).multiply(BigInteger.valueOf(1000)));
		if (totalCoinsInInv.compareTo(amount) < 0) {
			return false;
		}
		var coins = BigInteger.valueOf(getAmount(995));
		if (coins.compareTo(amount) > 0) {
			remove(995, amount.intValueExact());
		} else {
			remove(995, coins.intValueExact());
			var remaining = amount.subtract(coins);
			var tokensToRemove = remaining.divide(BigInteger.valueOf(1000));
			remove(13204, tokensToRemove.intValueExact());
		}
		return true;
	}

	public boolean removeCoinsOrPlatTokens(long amount) {
		long totalCoinsInInv = getAmount(995) + (getAmount(13204) * 1000L);
		if (totalCoinsInInv < amount) {
			return false;
		}
		int coins = getAmount(995);
		if (coins >= amount) {
			remove(995, (int) amount);
		} else {
			remove(995, coins);
			long remaining = amount - coins;
			int tokensToRemove = (int) (remaining / 1000);
			remove(13204, tokensToRemove);
		}
		return true;
	}

	public void addCoinsOrPlatTokens(BigInteger amount) {
		if (amount.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) <= 0
				&& hasRoomFor(995, amount.intValueExact())) {
			add(995, amount.intValueExact());
		} else {
			var tokenAmount = (amount.divide(BigInteger.valueOf(1000)));
			addOrSendToBank(13204, tokenAmount.intValueExact());
			var coinsRemainder = (amount.remainder(BigInteger.valueOf(1000))).intValueExact();
			if (coinsRemainder != 0) {
				addOrSendToBank(995, coinsRemainder);
			}
		}
	}

	public void addCoinsOrPlatTokens(long amount) {
		if (amount <= Integer.MAX_VALUE && hasRoomFor(995, (int) amount)) {
			add(995, (int) amount);
		} else {
			int tokenAmount = (int) (amount / 1000);
			addOrSendToBank(13204, tokenAmount);
			var coinsRemainder = (int) (amount % 1000);
			addOrSendToBank(995, coinsRemainder);
		}
	}
}
