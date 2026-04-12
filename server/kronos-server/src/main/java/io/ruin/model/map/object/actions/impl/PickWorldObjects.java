package io.ruin.model.map.object.actions.impl;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;

public class PickWorldObjects {

	public static void register() {
		ObjectAction.register(15506, "pick", PickWorldObjects::pickWheat);
		ObjectAction.register(15508, "pick", PickWorldObjects::pickWheat);
		ObjectAction.register(3366, "pick", PickWorldObjects::pickCabbage);
		ObjectAction.register(1161, "pick", PickWorldObjects::pickCabbage);
		ObjectAction.register(14896, "pick", PickWorldObjects::pickFlax);
		ObjectAction.register(3366, "pick", PickWorldObjects::pickOnion);
		ObjectAction.register(312, "pick", PickWorldObjects::pickPotato);
	}

	private static void pickWheat(Player player, GameObject wheat) {
		if (player.getInventory().isFull()) {
			player.sendMessage("You can't carry any more grain.");
			return;
		}
		player.startEvent(event -> {
			player.lock();
			player.animate(827);
			event.delay(1);
			player.getInventory().add(1947, 1);
			player.sendMessage("You pick some grain.");
			removeWheat(wheat);
			player.unlock();
		});
	}

	private static void removeWheat(GameObject wheat) {
		World.startEvent(event -> {
			wheat.remove();
			event.delay(60);
			wheat.restore();
		});
	}

	private static void pickCabbage(Player player, GameObject cabbage) {
		if (player.getInventory().isFull()) {
			player.sendMessage("You can't carry any more cabbage.");
			return;
		}
		player.startEvent(event -> {
			player.lock();
			player.animate(827);
			event.delay(1);
			player.getInventory().add(1965, 1);
			player.sendMessage("You pick some cabbage.");
			removeCabbage(cabbage);
			player.unlock();
		});
	}

	private static void removeCabbage(GameObject cabbage) {
		World.startEvent(event -> {
			cabbage.remove();
			event.delay(30);
			cabbage.restore();
		});
	}

	private static void pickFlax(Player player, GameObject flax) {
		if (player.getInventory().isFull()) {
			player.sendMessage("You can't carry any more flax.");
			return;
		}
		player.startEvent(event -> {
			player.lock();
			player.animate(827);
			event.delay(1);
			player.getInventory().add(1779, 1);
			player.sendMessage("You pick some flax.");
			if (Random.rollDie(6, 1))
				removeFlax(flax);
			player.unlock();
		});
	}

	private static void removeFlax(GameObject flax) {
		World.startEvent(event -> {
			flax.remove();
			event.delay(20);
			flax.restore();
		});
	}

	private static void pickOnion(Player player, GameObject onion) {
		if (player.getInventory().isFull()) {
			player.sendMessage("You can't carry any more onions.");
			return;
		}
		player.startEvent(event -> {
			player.lock();
			player.animate(827);
			event.delay(1);
			player.getInventory().add(1957, 1);
			player.sendMessage("You pick an onion.");
			removeOnion(onion);
			player.unlock();
		});
	}

	private static void removeOnion(GameObject onion) {
		World.startEvent(event -> {
			onion.remove();
			event.delay(30);
			onion.restore();
		});
	}

	private static void pickPotato(Player player, GameObject potato) {
		if (player.getInventory().isFull()) {
			player.sendMessage("You can't carry any more potatoes.");
			return;
		}
		player.startEvent(event -> {
			player.lock();
			player.animate(827);
			event.delay(1);
			player.getInventory().add(1942, 1);
			player.sendMessage("You pick a potato.");
			removePotato(potato);
			player.unlock();
		});
	}

	private static void removePotato(GameObject potato) {
		World.startEvent(event -> {
			potato.remove();
			event.delay(30);
			potato.restore();
		});
	}

}
