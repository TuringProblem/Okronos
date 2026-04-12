package io.ruin.model.activities.gamble;

import io.ruin.Server;
import io.ruin.api.utils.StringUtils;
import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;
import io.ruin.model.item.ItemContainerG;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.Position;
import io.ruin.network.PacketSender;

import java.util.concurrent.TimeUnit;

public class Gamble extends ItemContainer {

	public static Bounds GAMBLE_ZONE = new Bounds(1355, 2501, 1387, 2563, 1);
	public static final Bounds[] GAMBLEZONE_BOUNDS = {
		new Bounds(new Position(1359, 2510, 1), new Position(1365, 2522, 1), 1),
		new Bounds(new Position(1365, 2504, 1), new Position(1377, 2510, 1), 1),
		new Bounds(new Position(1377, 2510, 1), new Position(1383, 2522, 1), 1),
		new Bounds(new Position(1366, 2523, 1), new Position(1376, 2536, 1), 1),
		new Bounds(new Position(1359, 2538, 1), new Position(1365, 2549, 1), 1),
		new Bounds(new Position(1365, 2550, 1), new Position(1376, 2556, 1), 1),
		new Bounds(new Position(1377, 2537, 1), new Position(1383, 2550, 1), 1)
	};

	public static void register() {
		try {
			MapListener.registerBounds(GAMBLE_ZONE)
				.onEnter(Gamble::enteredZone)
				.onExit(Gamble::exitedZone);
		} catch (Throwable e) {
			System.out.println(e);
		}
		InterfaceHandler.register(876, h -> {
			h.actions[83] = (DefaultAction) (p, option, slot, itemId) -> {
				Item item = p.getGamble().get(slot, itemId);
				if (item == null)
					return;
				if (option == 1) {
					p.getGamble().remove(item, 1);
					return;
				}
				if (option == 2) {
					p.getGamble().remove(item, 5);
					return;
				}
				if (option == 3) {
					p.getGamble().remove(item, 10);
					return;
				}
				if (option == 4) {
					p.getGamble().remove(item, Integer.MAX_VALUE);
					return;
				}
				if (option == 5) {
					p.integerInput("How many would you like to remove?", amt -> p.getGamble().remove(item, amt));
					return;
				}
				item.examine(p);
			};
			h.actions[84] = (DefaultAction) (p, option, slot, itemId) -> {
				Item item = p.getGamble().targetGamble.get(slot, itemId);
				item.examine(p);
			};
			h.actions[94] = (SimpleAction) p -> p.getGamble().accept(true);
			h.actions[7] = (SimpleAction) p -> {
				p.getGamble().close();
				p.getGamble().closeInterfaces();
			};
			h.actions[103] = (SimpleAction) p -> {
				p.getGamble().close();
				p.getGamble().closeInterfaces();
			};
			h.actions[108] = (SimpleAction) p -> {
				p.getGamble().HandleButtonClick(108);
			};
			h.actions[109] = (SimpleAction) p -> {
				p.getGamble().HandleButtonClick(109);
			};
			h.actions[110] = (SimpleAction) p -> {
				p.getGamble().HandleButtonClick(110);
			};
			h.actions[111] = (SimpleAction) p -> {
				p.getGamble().HandleButtonClick(111);
			};
			h.actions[112] = (SimpleAction) p -> {
				p.getGamble().HandleButtonClick(112);
			};
			h.actions[116] = (SimpleAction) p -> {
				p.getGamble().HandleButtonClick(116);
			};
			h.actions[117] = (SimpleAction) p -> {
				p.getGamble().HandleButtonClick(117);
			};
		});
		InterfaceHandler.register(882, h -> {
			h.actions[66] = (SimpleAction) p -> {
				p.getGamble().accept(false);
			};
			h.actions[68] = (SimpleAction) p -> {
				p.getGamble().close();
				p.getGamble().closeInterfaces();
			};
		});
		InterfaceHandler.register(879, h -> {
			h.actions[0] = (DefaultAction) (p, option, slot, itemId) -> {
				Item item = p.getInventory().get(slot, itemId);
				if (item == null)
					return;
				if (option == 1)
					p.getGamble().offer(item, 1);
				else if (option == 2)
					p.getGamble().offer(item, 5);
				else if (option == 3)
					p.getGamble().offer(item, 10);
				else if (option == 4)
					p.getGamble().offer(item, Integer.MAX_VALUE);
				else if (option == 5)
					p.integerInput("Enter amount:", amt -> p.getGamble().offer(item, amt));
				else
					item.examine(p);
			};
		});
	}

	private static void enteredZone(Player player) {
		// player.setAction(1, PlayerAction.GAMBLE);
	}

	private static void exitedZone(Player player, boolean logout) {
		if (!logout) {
			// player.setAction(1, null);
		}
	}

	public transient Gamble targetGamble;
	private transient boolean accepted;
	private transient int mode = 0;
	public transient boolean started = false;
	// 1 = first screen, 2 = second screen, 3 countdown, 4 = gamble
	public transient int stage = 0;
	private transient int requestUserId = -1;

	public Gamble(Player player) {
		init(player, 28, -1, 64212, 90, false);
		mirror(-2, 60981, 90);
	}

	public GambleData getGambleData() {
		return GambleData.instance.get(player.gambleId);
	}

	public GambleGameHandler getGambleGameManager() {
		return GambleGameHandler.instance.get(getGambleData().playerOne.gambleId);
	}

	public void HandleButtonClick(int componentID) {
		PacketSender ps1 = player.getPacketSender();
		PacketSender ps2 = targetGamble.player.getPacketSender();

		ps1.sendClientScript(10203, 697);
		ps2.sendClientScript(10203, 697);
		ps1.sendClientScript(10204, 699, componentID);
		ps2.sendClientScript(10204, 699, componentID);

		for (int i = 127; i < 129; i++) {
			ps1.setHidden(876, i, false);
			ps2.setHidden(876, i, false);
		}

		switch (componentID) {
			case 108:
				if (mode != 1) {
					targetGamble.accepted = false;
					accepted = false;
					targetGamble.player.getPacketSender().setHidden(876, 130, true);
					player.getPacketSender().setHidden(876, 130, true);
				}
				mode = 1;
				targetGamble.mode = 1;
				send55x2Rules(getGambleData().playerOne, getGambleData().playerTwo);
				getGambleData().host = getGambleData().playerOne;
				getGambleData().participant = getGambleData().playerTwo;
				break;
			case 109:
				if (mode != 2) {
					targetGamble.accepted = false;
					accepted = false;
					targetGamble.player.getPacketSender().setHidden(876, 130, true);
					player.getPacketSender().setHidden(876, 130, true);
				}
				getGambleData().host = getGambleData().playerTwo;
				getGambleData().participant = getGambleData().playerOne;
				mode = 2;
				targetGamble.mode = 2;
				send55x2Rules(getGambleData().playerTwo, getGambleData().playerOne);
				break;
			case 110:
				if (mode != 3) {
					targetGamble.accepted = false;
					accepted = false;
					targetGamble.player.getPacketSender().setHidden(876, 130, true);
					player.getPacketSender().setHidden(876, 130, true);
				}
				getGambleData().host = getGambleData().playerOne;
				getGambleData().participant = getGambleData().playerTwo;
				mode = 3;
				targetGamble.mode = 3;
				sendBlackJackRules(getGambleData().host);
				break;
			case 111:
				if (mode != 4) {
					targetGamble.accepted = false;
					accepted = false;
					targetGamble.player.getPacketSender().setHidden(876, 130, true);
					player.getPacketSender().setHidden(876, 130, true);
				}
				getGambleData().host = getGambleData().playerTwo;
				getGambleData().participant = getGambleData().playerOne;
				mode = 4;
				targetGamble.mode = 4;
				sendBlackJackRules(getGambleData().host);
				break;
			case 112:
				if (mode != 5) {
					targetGamble.accepted = false;
					accepted = false;
					targetGamble.player.getPacketSender().setHidden(876, 130, true);
					player.getPacketSender().setHidden(876, 130, true);
				}
				targetGamble.mode = 5;
				mode = 5;
				sendAbcFlowerRules();
				break;
			case 117:
				if (mode != 7) {
					targetGamble.accepted = false;
					accepted = false;
					targetGamble.player.getPacketSender().setHidden(876, 130, true);
					player.getPacketSender().setHidden(876, 130, true);
				}
				mode = 7;
				targetGamble.mode = 7;
				sendFlowerPokerRules();
				break;
			case 116:
				if (mode != 6) {
					targetGamble.accepted = false;
					accepted = false;
					targetGamble.player.getPacketSender().setHidden(876, 130, true);
					player.getPacketSender().setHidden(876, 130, true);
				}
				targetGamble.mode = 6;
				mode = 6;
				sendDiceDuelRules();
				break;
		}
	}

	public void request(Player target) {
		if (player.getGameMode().isIronMan() && !target.isAdmin()) {
			player.sendMessage("Ironmen stand alone.");
			return;
		}
		if (player.getName().equalsIgnoreCase("99")) {
			player.sendMessage("Your better judgement stops you from doing this.");
			return;
		}
		long playtime = player.playTime * Server.tickMs();
		long days = TimeUnit.MILLISECONDS.toDays(playtime);
		if (days < 7) {
			player.sendMessage("You are not experienced enough to gamble.");
			return;
		}
		if (target.getGameMode().isIronMan() && !player.isAdmin()) {
			player.sendMessage(target.getName() + " is an ironman and so cannot gamble.");
			return;
		}
		if (target.isLocked()) {
			player.sendMessage("This player is currently busy.");
			return;
		}
		Gamble targetGamble = target.getGamble();
		if (stage >= 3 || targetGamble.stage >= 3) // already in a duel
			return;
		if (targetGamble.requestUserId == player.getUserId()) {
			if (!target.getPosition().inBounds(GAMBLE_ZONE)) {
				player.sendMessage("Your target isn't inside the gambling zone.");
				return;
			}
			if (!player.getPosition().inBounds(GAMBLE_ZONE)) {
				player.sendMessage("You're not inside the gambling zone.");
				return;
			}
			if (target.isLocked()) {
				player.sendMessage("This player is currently busy.");
				return;
			}
			init(targetGamble);
			targetGamble.init(this);
			return;
		}
		requestUserId = target.getUserId();
		target.getPacketSender().sendMessage(player.getName() + " wishes to gamble with you.", player.getName(), 103);
		player.getPacketSender().sendMessage("Sending gamble request to " + target.getName() + "...", null, 102);
	}

	public void returnItems() {
		for (Item item : getItems()) {
			if (item != null)
				player.getInventory().addOrDrop(item.getId(), item.getAmount());
		}
	}

	public Item[] getItems() {
		return items;
	}

	public void close() {
		if (targetGamble == null) {
			/* no gamble active */
			return;
		}
		if (stage >= 3) {
			/* in a duel */
			return;
		}
		if (started)
			return;

		targetGamble.player.sendMessage("The other player declined the gamble.");
		targetGamble.returnItems();
		targetGamble.closeInterfaces();
		targetGamble.destroy();
		targetGamble = null;
		player.sendMessage("You declined the gamble.");
		returnItems();
		closeInterfaces();
		destroy();
	}

	public void offer(Item item, int amount) {
		if (item.getId() == ItemID.BLOOD_MONEY) {
			player.sendMessage("You can't stake this item.");
			return;
		}
		if (!item.getDef().tradeable) {
			player.sendMessage("You can't stake this item.");
			return;
		} else if (item.getUniqueValue() != 0) {
			player.sendMessage("You can't stake this item.");
			return;
		}
		int moved = item.move(item.getId(), amount, this);
		if (moved <= 0) {
			player.sendMessage("Not enough space to offer this.");
			return;
		}
		if (getGambleData().items.containsKey(item.getId()))
			getGambleData().items.replace(item.getId(), getGambleData().items.get(item.getId()) + moved);
		else
			getGambleData().items.put(item.getId(), (long) moved);
		targetGamble.player.sendMessage("Gamble Stake addition: " + moved + "x " + item.getDef().name + " added!");
		update(targetGamble);
	}

	private void sendMessage(String message) {
		player.getPacketSender().sendMessage(message, null, 102);
	}

	private void destroy(boolean returnItems) {
		if (returnItems) {
			/**
			 * Return my items
			 */
			for (Item item : getItems()) {
				if (item != null)
					player.getInventory().add(item);
			}
		} else {
			getGambleGameManager().startGame(mode);
			started = true;
		}
		stage = 0;
		accepted = false;
		// targetGamble = null;
		player.getPacketSender().sendClientScript(209, "s", "");
		clear();
	}

	private void updateState() {
		String s = null;
		PacketSender ps1 = player.getPacketSender();
		if (accepted)
			s = "Waiting for other player...";
		else if (targetGamble.accepted)
			s = "Other player has accepted.";
		if (stage == 1) {
			ps1.setHidden(876, 130, false);
			ps1.sendString(876, 130, s);
		} else if (stage == 2) {
			ps1.setHidden(882, 75, false);
			ps1.sendString(882, 75, s);
		}
	}

	private void second(Player host, Player participant) {
		stage = 2;
		accepted = false;
		player.openInterface(ToplevelComponent.MAINMODAL, 882);
		String textOne = "Absolutely nothing.";
		String textTwo = "Absolutely nothing.";
		String gambleRules = "";
		PacketSender ps = player.getPacketSender();

		if (getItems().length > 0)
			textOne = "";
		if (targetGamble.getItems().length > 0)
			textTwo = "";

		for (Item item : getItems()) {
			if (item == null)
				continue;
			textOne += item.getAmount() + "x " + StringUtils.capitalizeFirst(item.getDef().name + "<br>");
		}

		for (Item item : targetGamble.getItems()) {
			if (item == null)
				continue;
			textTwo += item.getAmount() + "x " + StringUtils.capitalizeFirst(item.getDef().name + "<br>");
		}

		switch (mode) {
			case 1:
			case 2:
				gambleRules += "55x2 and " + host.getName() + " is the host.<br>";
				gambleRules += host.getName() + " will roll a 1-100 die and if it is below 55 " + host.getName()
					+ " will win and if it is higher than 55 " + participant.getName() + " will win.";
				break;

			case 3:
			case 4:
				gambleRules += "Black Jack and " + host.getName() + " is the host.<br>";
				gambleRules += "Closes to 100 without going over 100 wins.<br>";
				break;
			case 7:
				gambleRules += "Flower poker.<br>";
				gambleRules += "Each player will plant five flowers, the player with the best pairs of colours will win";
				break;
			case 6:
				gambleRules += "Dice duel.<br>";
				gambleRules += "Each round both players will roll a 1-100 die and the player who rolls the highest wins the point.<br>";
				gambleRules += "The first person to get 3 points wins";
				break;
			case 5:
				gambleRules += "ABC Flower";
				gambleRules += "Each round both players will plant a flower the colour that that comes alphabetically first wins the point.";
				break;

		}
		ps.setHidden(882, 75, true);
		ps.sendString(882, 7, gambleRules);
		ps.sendString(882, 73, textOne);
		ps.sendString(882, 74, textTwo);
	}

	private void accept(boolean firstScreen) {
		if (mode == 0) {
			player.sendMessage("You must select a mode first!");
			return;
		}
		if (accepted)
			return;
		if (firstScreen) {
			if (stage != 1)
				return;
			int slotsRequired = 0;
			for (Item item : targetGamble.getItems()) {
				if (item == null || (item.getDef().stackable && player.getInventory().hasId(item.getId())))
					continue;
				slotsRequired++;
			}
			if (player.getInventory().getFreeSlots() < slotsRequired) {
				sendMessage("You don't have enough inventory space to accept this trade.");
				return;
			}

			if (targetGamble.accepted) {
				switch (mode) {
					case 1:
					case 3:
					case 5:
					case 6:
					case 7:
						second(player, targetGamble.player);
						targetGamble.second(player, targetGamble.player);
						break;
					case 2:
					case 4:
						second(targetGamble.player, player);
						targetGamble.second(targetGamble.player, player);
						break;

				}
				return;
			}
		} else {
			if (stage != 2)
				return;

			if (targetGamble.accepted) {
				targetGamble.destroy(false);
				targetGamble.closeInterfaces();
				destroy(false);
				closeInterfaces();
				getGambleGameManager().startGame(mode);
				started = true;
				return;
			}
		}
		accepted = true;
		targetGamble.updateState();
		updateState();
	}

	private void send55x2Rules(Player host, Player participant) {
		PacketSender ps1 = player.getPacketSender();
		PacketSender ps2 = targetGamble.player.getPacketSender();

		ps1.setHidden(876, 127, false);
		ps1.setHidden(876, 128, false);
		ps2.setHidden(876, 127, false);
		ps2.setHidden(876, 128, false);

		ps1.sendString(876, 127, "55x2 with the host being: " + host.getName());
		ps2.sendString(876, 127, "55x2 with the host being: " + host.getName());

		ps1.sendString(876, 128,
			"The host will roll a 1-100 dice if the roll is 55 or above " + participant.getName() + " will win.");
		ps2.sendString(876, 128,
			"The host will roll a 1-100 dice if the roll is 55 or above " + participant.getName() + " will win.");

	}

	private void sendBlackJackRules(Player host) {
		PacketSender ps1 = player.getPacketSender();
		PacketSender ps2 = targetGamble.player.getPacketSender();

		ps1.setHidden(876, 127, false);
		ps1.setHidden(876, 128, false);
		ps2.setHidden(876, 127, false);
		ps2.setHidden(876, 128, false);

		ps1.sendString(876, 127, "Black Jack with the host being: " + host.getName());
		ps2.sendString(876, 127, "Black Jack with the host being: " + host.getName());

		ps1.sendString(876, 128,
			"Get 100, or as close to 100 as possible, without going over. If you go over, the game is automatically lost without the dealer ever having to roll."
				+
				" The dealer rolls next and is trying to beat the player's roll without going over 100 as well.");
		ps2.sendString(876, 128,
			"Get 100, or as close to 100 as possible, without going over. If you go over, the game is automatically lost without the dealer ever having to roll."
				+
				" The dealer rolls next and is trying to beat the player's roll without going over 100 as well.");

	}

	private void sendAbcFlowerRules() {
		PacketSender ps1 = player.getPacketSender();
		PacketSender ps2 = targetGamble.player.getPacketSender();

		ps1.setHidden(876, 127, false);
		ps1.setHidden(876, 128, false);
		ps2.setHidden(876, 127, false);
		ps2.setHidden(876, 128, false);

		ps1.sendString(876, 127, "Abc Flower - Earliest alphabetical colour wins in this ABC game of chance.");
		ps2.sendString(876, 127,
			"Abc Flower - Earliest alphabetical colour wins in this ABC game of chance.<br>First person to win three times wins the game.");

		ps1.sendString(876, 128,
			"The order of the flowers from best to worst:<br><br>Blue > Orange > Pastel > Purple > Rainbow > Red > Yellow.");
		ps2.sendString(876, 128,
			"The order of the flowers from best to worst:<br><br>Blue > Orange > Pastel > Purple > Rainbow > Red > Yellow.");
	}

	private void sendFlowerPokerRules() {
		PacketSender ps1 = player.getPacketSender();
		PacketSender ps2 = targetGamble.player.getPacketSender();

		ps1.setHidden(876, 127, false);
		ps2.setHidden(876, 127, false);
		ps1.setHidden(876, 128, true);
		ps2.setHidden(876, 128, true);

		ps1.sendString(876, 127,
			"Flower Poker - Poker in the traditional sense, but with flowers. <br><br>Each player will plant five flowers, the player with the best pairs of colours will win.");
		ps2.sendString(876, 127,
			"Flower Poker - Poker in the traditional sense, but with flowers. <br><br>Each player will plant five flowers, the player with the best pairs of colours will win.");

	}

	private void sendDiceDuelRules() {
		PacketSender ps1 = player.getPacketSender();
		PacketSender ps2 = targetGamble.player.getPacketSender();

		ps1.setHidden(876, 128, true);
		ps2.setHidden(876, 128, true);

		ps1.setHidden(876, 127, false);
		ps2.setHidden(876, 127, false);

		ps1.sendString(876, 127,
			"Dice duel - Each player will roll a 1-100 dice, the highest roll win a point<br><br>First player to reach 3 points wins the game.");
		ps2.sendString(876, 127,
			"Dice duel - Each player will roll a 1-100 dice, the highest roll win a point<br><br>First player to reach 3 points wins the game.");
	}

	private void closeInterfaces() {
		player.closeInterface(ToplevelComponent.MAINMODAL);
		player.closeInterface(ToplevelComponent.SIDEMODAL);
	}

	private void destroy() {
		stage = 0;
		accepted = false;
		clear();
	}

	private void update(ItemContainerG mirrorContainer) {
		if (mirrorContainer == null) {
			mirrorContainer = this;
			open();
		} else {
			accepted = false;
			targetGamble.accepted = false;
			targetGamble.player.getPacketSender().sendString(876, 74,
				"<col=ff0000> Stake has changed - check before accepting!");
			targetGamble.player.getPacketSender().setHidden(876, 130, true);
			player.getPacketSender().setHidden(876, 130, true);
		}
		sendUpdates(mirrorContainer);
		player.getPacketSender().sendClientScript(149, "IviiiIsssss", 876 << 16 | 83, 90, 4, 7, 0, 876 << 16 | 19, "Remove",
			"Remove-5", "Remove-10", "Remove-All", "Remove-X");
		targetGamble.player.getPacketSender().sendClientScript(158, "IviiiIsssssi", 876 << 16 | 84, 90, 4, 7, 0,
			876 << 16 | 27, "", "", "", "", "", 1);
		player.getPacketSender().sendClientScript(158, "IviiiIsssssi", 876 << 16 | 84, 90, 4, 7, 0, 876 << 16 | 27, "", "",
			"", "", "", 1);
		player.getPacketSender().sendIfEvents(876, 84, 0, 27, 1024);
	}

	private void open() {
		this.clear();
		for (int slot = 0; slot < 28; slot++) {
			update(slot);
		}
		sendUpdates(targetGamble);

		PacketSender ps = player.getPacketSender();
		stage = 1;
		mode = 0;
		sendAll = true;
		player.inGamble = true;
		player.openInterface(ToplevelComponent.MAINMODAL, 876);
		player.openInterface(ToplevelComponent.SIDEMODAL, 879);
		ps.sendClientScript(149, "IviiiIsssss", 879 << 16, 93, 4, 7, 0, -1,
			"Offer", "Offer-5", "Offer-10", "Offer-All", "Offer-X");
		ps.sendIfEvents(879, 0, 0, 27, 1086);
		ps.sendIfEvents(876, 84, 0, 27, 1024);
		ps.sendIfEvents(879, 0, 0, 27, 1086);
		player.getPacketSender().sendClientScript(149, "IviiiIsssss", 876 << 16 | 83, 90, 4, 7, 0, 876 << 16 | 19,
			"Remove 1", "Remove 5", "Remove 10", "Remove All", "Remove X");
		player.getPacketSender().sendIfEvents(876, 83, 0, 27, 1086);

		ps.sendString(876, 126, "Select a game mode to view the rules.");
		for (int i = 127; i < 131; i++)
			ps.setHidden(876, i, true);
		ps.sendString(876, 131, "Gambling with " + targetGamble.player.getName() + "...");
		if (getGambleData().playerOne == null && getGambleData().playerTwo == null) {
			getGambleData().setPlayers(player, targetGamble.player);
		}
		ps.sendString(876, 118, "55x2 (" + getGambleData().playerOne.getName() + " host)");
		ps.sendString(876, 119, "55x2 (" + getGambleData().playerTwo.getName() + " host)");
		ps.sendString(876, 120, "Black Jack (" + getGambleData().playerOne.getName() + " host)");
		ps.sendString(876, 121, "Black Jack (" + getGambleData().playerTwo.getName() + " host)");
	}

	private void remove(Item item, int amount) {
		if (stage != 1) {
			/* not on the first screen! */
			return;
		}
		long itemAmount = getGambleData().items.get(item.getId());

		int moved = item.move(item.getId(), amount, player.getInventory());
		if (moved <= 0) {
			Server.logWarning(player.getName() + " failed to remove item (" + item.getId() + ", " + item.getAmount()
				+ ") from duel, this should NEVER happen!");
			return;
		}

		if (itemAmount == moved) {
			getGambleData().items.remove(item.getId());
		} else {
			long newAmount = getGambleData().items.get(item.getId()) - moved;
			getGambleData().items.remove(item.getId());
			getGambleData().items.put(item.getId(), newAmount);
		}

		targetGamble.player.getPacketSender().sendClientScript(10060, "iii", 46137372, item.getSlot(), 46137417);
		targetGamble.player.acceptDelay.delaySeconds(3);

		targetGamble.player.sendMessage("Gamble Stake removal: " + moved + "x " + item.getDef().name + " removed!");
		update(targetGamble);
	}

	private void init(Gamble targetGamble) {
		close();
		this.targetGamble = targetGamble;
		this.requestUserId = -1;
		update(null);
	}

}
