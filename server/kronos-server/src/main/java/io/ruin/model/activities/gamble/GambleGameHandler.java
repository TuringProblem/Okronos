package io.ruin.model.activities.gamble;

import io.ruin.api.utils.Random;
import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.network.PacketSender;
import io.ruin.utility.TickDelay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GambleGameHandler extends ItemContainer {
	private enum Flowers {
		ORANGE(2470, 2985, 1539, 2), // orange
		PURPLE(2468, 2984, 1485, 4), // purple
		YELLOW(2466, 2983, 1465, 7), // yellow
		BLUE(2464, 2982, 1530, 1), // blue
		RED(2462, 2981, 1408, 6), // red
		MIXED(2460, 2980, 1466, 3), // mixed
		ASSORTED(2472, 2986, 1078, 5); // assorted

		public static final Flowers[] VALUES = values();
		private int flowerId;
		private int objId;
		private int weight;

		private int flowerOrder;

		Flowers(int flowerId, int objId, int weight, int flowerOrder) {
			this.flowerId = flowerId;
			this.objId = objId;
			this.weight = weight;
			this.flowerOrder = flowerOrder;
		}
	}

	public static HashMap<Integer, GambleGameHandler> instance = new HashMap<>();
	private static int TOTAL_WEIGHT = Arrays.stream(Flowers.VALUES).mapToInt(f -> f.weight).sum();

	private static Flowers roll() {
		int roll = Random.get(TOTAL_WEIGHT);
		for (Flowers flower : Flowers.VALUES) {
			roll -= flower.weight;
			if (roll <= 0)
				return flower;
		}
		return null;
	}

	transient Player playerOne;
	transient Player playerTwo;
	transient int id;
	transient boolean started = false;
	transient TickDelay blackJackTimer = new TickDelay();
	transient boolean blackJackTimerStarted = false;
	transient boolean blackJackCanHitOrStick = false;

	public GambleGameHandler(int id, Player playerOne, Player playerTwo) {
		instance.put(id, this);
		this.id = id;
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
	}

	public void GamblerText(NPC npc, String string) {
		npc.startEvent(event -> {
			while (npc != null) {
				npc.forceText(string);
				event.delay(3);
			}
		});
	}

	public void startGame(int mode) {
		if (!started) {
			started = true;
			switch (mode) {
				case 1:
					HandleDice55x2(getGambleData().playerTwo, getGambleData().playerOne);
				case 2:
					HandleDice55x2(getGambleData().playerOne, getGambleData().playerTwo);
					break;
				case 3:
					HandleBlackJack(getGambleData().playerTwo, getGambleData().playerOne);
				case 4:
					HandleBlackJack(getGambleData().playerOne, getGambleData().playerTwo);
					break;
				case 5:
					startFlowerGame(1);
					break;
				case 6:
					HandleDiceDuel();
					break;
				case 7:
					startFlowerGame(0);
					break;
			}
		}
	}

	public void HandleDice55x2(Player participant, Player host) {
		getGambleData().host = host;
		getGambleData().participant = participant;
		int diceRoll = Random.get(1, 100);
		NPC npc = new NPC(3077).spawn(getGambleData().host.getPosition().getX() - 1,
			getGambleData().host.getPosition().getY(), getGambleData().host.getPosition().getZ());
		npc.face(getGambleData().host);
		getGambleData().host.startEvent(e -> {
			e.delay(1);
			npc.forceText("3..");
			e.delay(1);
			npc.forceText("2..");
			e.delay(1);
			npc.forceText("1..");
			e.delay(1);
			npc.forceText("GO!");
			// getGambleData().host.sendMessage("You just rolled <col=ef20ff><shad=000000>"
			// + diceRoll + "</shad> </col>on the percentile dice.");
			// getGambleData().participant.sendMessage("<col=ef20ff><shad=000000>" +
			// getGambleData().host.getName() + "</shad></col> just rolled
			// <col=ef20ff><shad=000000>" + diceRoll + " </shad></col>on the percentile
			// dice.");
			// getGambleData().host.forceText("[<col=0000FF>55x2 Dice</col>] I just rolled
			// <col=ef20ff><shad=000000>" + diceRoll + "</shad></col> on the percentile
			// dice");

			if (diceRoll < 55) {
				Winner(getGambleData().host, getGambleData().participant);
				npc.forceText("The roll was " + diceRoll + " the host wins!");
			} else {
				Winner(getGambleData().participant, getGambleData().host);
				npc.forceText("The roll was " + diceRoll + " " + getGambleData().participant.getName() + " wins!");
			}
			e.delay(2);
			npc.remove();
		});

	}

	public void HandleDiceDuel() {
		AtomicInteger playerOneScore = new AtomicInteger();
		AtomicInteger playerTwoScore = new AtomicInteger();
		NPC npc = new NPC(3077).spawn(playerOne.getPosition().getX() - 1, playerOne.getPosition().getY(),
			playerOne.getPosition().getZ());
		npc.face(getGambleData().host);
		World.startEvent(e -> {
			playerOne.lock();
			playerTwo.lock();
			e.delay(1);
			npc.forceText("3..");
			e.delay(1);
			npc.forceText("2..");
			e.delay(1);
			npc.forceText("1..");
			e.delay(1);
			npc.forceText("GO!");
			while (playerOneScore.get() != 3 && playerTwoScore.get() != 3) {
				GamblerText(npc,
					playerOne.getName() + " - " + playerOneScore + " : " + playerTwo.getName() + " - " + playerTwoScore);
				int diceRoll = Random.get(1, 100);
				int diceRoll2 = Random.get(1, 100);
				e.delay(2);
				playerOne.sendMessage(
					"You just rolled <col=ef20ff><shad=000000>" + diceRoll + "</shad> </col>on the percentile dice.");
				playerTwo.sendMessage(
					"<col=ef20ff><shad=000000>" + playerOne.getName() + "</shad></col> just rolled <col=ef20ff><shad=000000>"
						+ diceRoll + " </shad></col>on the percentile dice.");
				e.delay(4);
				playerTwo.sendMessage(
					"You just rolled <col=ef20ff><shad=000000>" + diceRoll2 + "</shad> </col>on the percentile dice.");
				playerOne.sendMessage(
					"<col=ef20ff><shad=000000>" + playerTwo.getName() + "</shad></col> just rolled <col=ef20ff><shad=000000>"
						+ diceRoll2 + " </shad></col>on the percentile dice.");
				if (diceRoll > diceRoll2) {
					playerOneScore.getAndIncrement();
					playerOne.forceText(playerOneScore.get() + " - " + playerTwoScore.get());
					playerTwo.forceText(playerTwoScore.get() + " - " + playerOneScore.get());
					if (playerOneScore.get() == 3) {
						Winner(playerOne, playerTwo);
						GamblerText(npc, playerOne.getName() + " wins the duel!");
						e.delay(5);
						npc.remove();
					}
				} else if (diceRoll2 > diceRoll) {
					playerTwoScore.getAndIncrement();
					playerOne.forceText(playerOneScore.get() + " - " + playerTwoScore.get());
					playerTwo.forceText(playerTwoScore.get() + " - " + playerOneScore.get());
					if (playerTwoScore.get() == 3) {
						Winner(playerTwo, playerOne);
						GamblerText(npc, playerTwo.getName() + " wins the duel!");
						e.delay(5);
						npc.remove();
					}
				} else {
					playerOne.sendMessage("REROLL!");
					playerTwo.sendMessage("REROLL!");
				}
			}
		});
	}

	public void refreshBlackJackInterface(Player player) {
		PacketSender ps = player.getPacketSender();
		ps.sendString(887, 24, "Turn: " + getGambleData().blackJackTurn.getName());
		ps.sendString(887, 12, "Black Jack (Host: " + getGambleData().host.getName() + ")");
		ps.sendString(887, 30,
			getGambleData().blackJackPlayerOneRolls == null ? "0" : getGambleData().blackJackPlayerOneRolls);
		ps.sendString(887, 36,
			getGambleData().blackJackPlayerTwoRolls == null ? "0" : getGambleData().blackJackPlayerTwoRolls);
		ps.sendString(887, 26, getGambleData().participant.getName() + " rolls:");
		ps.sendString(887, 32, getGambleData().host.getName() + " rolls:");
		ps.sendString(887, 27,
			"" + getGambleData().playerOneBlackJackValue == null ? "0" : "" + getGambleData().playerOneBlackJackValue);
		ps.sendString(887, 33,
			"" + getGambleData().playerTwoBlackJackValue == null ? "0" : "" + getGambleData().playerTwoBlackJackValue);
		player.openInterface(ToplevelComponent.MAINMODAL, 887);
	}

	public void HandleBlackJack(Player participant, Player host) {
		getGambleData().inBlackJack = true;
		getGambleData().host = host;
		getGambleData().participant = participant;
		PacketSender ps1 = playerOne.getPacketSender();
		PacketSender ps2 = playerTwo.getPacketSender();
		blackJackCanHitOrStick = false;
		World.startEvent(event -> {
			playerOne.lock();
			playerTwo.lock();
			event.delay(1);
			playerOne.openInterface(ToplevelComponent.MAINMODAL, 887);
			playerTwo.openInterface(ToplevelComponent.MAINMODAL, 887);
			ps1.sendString(887, 24, "Turn: " + participant.getName());
			ps2.sendString(887, 24, "Turn: " + participant.getName());
			ps1.sendString(887, 12, "Black Jack (Host: " + host.getName() + ")");
			ps2.sendString(887, 12, "Black Jack (Host: " + host.getName() + ")");
			ps2.sendString(887, 30, "");
			ps1.sendString(887, 30, "");
			ps2.sendString(887, 36, "");
			ps1.sendString(887, 36, "");
			ps2.sendString(887, 27, "0");
			ps1.sendString(887, 27, "0");
			ps2.sendString(887, 33, "0");
			ps1.sendString(887, 33, "0");
			ps1.sendString(887, 26, getGambleData().participant.getName() + " rolls:");
			ps1.sendString(887, 32, getGambleData().host.getName() + " rolls:");
			ps2.sendString(887, 26, getGambleData().participant.getName() + " rolls:");
			ps2.sendString(887, 32, getGambleData().host.getName() + " rolls:");
			getGambleData().blackJackTurn = playerOne;

			if (getGambleData().playerOneBlackJackValue == 0) {
				int roll = Random.get(1, 100);
				getGambleData().playerOneBlackJackValue += roll;
				getGambleData().blackJackPlayerOneRolls += "" + roll;
			}
			ps2.sendString(887, 30, "" + getGambleData().playerOneBlackJackValue);
			ps1.sendString(887, 30, "" + getGambleData().playerOneBlackJackValue);
			ps2.sendString(887, 27, "" + getGambleData().playerOneBlackJackValue);
			ps1.sendString(887, 27, "" + getGambleData().playerOneBlackJackValue);
			blackJackTimer.delaySeconds(30);
			blackJackTimerStarted = true;
			event.delay(4);
			blackJackCanHitOrStick = true;
		});
	}

	public void HandleBlackJackHit(Player player) {
		if (!getGambleData().inBlackJack)
			return;
		if (!player.getName().equalsIgnoreCase(getGambleData().blackJackTurn.getName())) {
			player.sendMessage("It isn't currently your turn!");
			return;
		}
		if (!blackJackCanHitOrStick) {
			player.sendMessage("Please wait a few seconds before trying to hit or stick again");
			return;
		}
		blackJackCanHitOrStick = false;
		PacketSender ps1 = playerOne.getPacketSender();
		PacketSender ps2 = playerTwo.getPacketSender();
		int diceValue = 0;
		blackJackTimer.delaySeconds(30);
		blackJackTimerStarted = true;
		player.startEvent(event -> {
			event.delay(5);
			blackJackCanHitOrStick = true;
		});
		if (getGambleData().participant.getName().equalsIgnoreCase(getGambleData().blackJackTurn.getName())) {
			diceValue = getGambleData().playerOneBlackJackValue;
			int roll = Random.get(1, 100);
			getGambleData().playerOneBlackJackValue += roll;
			getGambleData().blackJackPlayerOneRolls += "<br><br>" + roll;
			ps2.sendString(887, 30, getGambleData().blackJackPlayerOneRolls);
			ps1.sendString(887, 30, getGambleData().blackJackPlayerOneRolls);
			diceValue += roll;
			ps2.sendString(887, 27, "" + diceValue);
			ps1.sendString(887, 27, "" + diceValue);
			if (diceValue > 100) {
				World.startEvent(e -> {
					ps1.setHidden(887, 23, false);
					ps2.setHidden(887, 23, false);
					ps2.sendString(887, 23, getGambleData().host.getName() + " has won<br>the gamble!");
					ps1.sendString(887, 23, getGambleData().host.getName() + " has won<br>the gamble!");
					e.delay(5);
					Winner(getGambleData().host, getGambleData().participant);
				});
			}
		} else if (getGambleData().host.getName().equalsIgnoreCase(getGambleData().blackJackTurn.getName())) {
			diceValue = getGambleData().playerTwoBlackJackValue;
			int roll = Random.get(1, 100);
			getGambleData().playerTwoBlackJackValue += roll;
			getGambleData().blackJackPlayerTwoRolls += "<br><br>" + roll;
			ps2.sendString(887, 36, getGambleData().blackJackPlayerTwoRolls);
			ps1.sendString(887, 36, getGambleData().blackJackPlayerTwoRolls);
			diceValue += roll;
			ps2.sendString(887, 33, "" + diceValue);
			ps1.sendString(887, 33, "" + diceValue);
			blackJackCanHitOrStick = false;
			player.startEvent(event -> {
				event.delay(5);
				blackJackCanHitOrStick = true;
			});
			if (diceValue > 100) {
				World.startEvent(e -> {
					ps1.setHidden(887, 23, false);
					ps2.setHidden(887, 23, false);
					ps2.sendString(887, 23, getGambleData().host.getName() + " has won<br>the gamble!");
					ps1.sendString(887, 23, getGambleData().host.getName() + " has won<br>the gamble!");
					e.delay(5);
					Winner(getGambleData().participant, getGambleData().host);
				});
			}
		}
	}

	public void HandleBlackJackStick(Player player) {
		if (!getGambleData().inBlackJack)
			return;
		PacketSender ps1 = playerOne.getPacketSender();
		PacketSender ps2 = playerTwo.getPacketSender();
		if (!player.getName().equalsIgnoreCase(getGambleData().blackJackTurn.getName())) {
			player.sendMessage("It isn't currently your turn!");
			return;
		}
		if (!blackJackCanHitOrStick) {
			player.sendMessage("Please wait a few seconds before trying to hit or stick again");
			return;
		}
		blackJackTimer.delaySeconds(30);
		blackJackTimerStarted = true;
		if (getGambleData().blackJackTurn.getName().equalsIgnoreCase(getGambleData().participant.getName())) {
			getGambleData().blackJackTurn = getGambleData().host;
			ps1.sendString(887, 24, "Turn: " + getGambleData().host.getName());
			ps2.sendString(887, 24, "Turn: " + getGambleData().host.getName());
			int roll = Random.get(1, 100);
			ps2.sendString(887, 36, "" + roll);
			ps1.sendString(887, 36, "" + roll);
			ps2.sendString(887, 33, "" + roll);
			ps1.sendString(887, 33, "" + roll);
			getGambleData().playerTwoBlackJackValue += roll;
			getGambleData().blackJackPlayerTwoRolls += "" + roll;
		} else if (getGambleData().blackJackTurn.getName().equalsIgnoreCase(getGambleData().host.getName())) {
			if (getGambleData().playerOneBlackJackValue > getGambleData().playerTwoBlackJackValue) {
				World.startEvent(e -> {
					ps1.setHidden(887, 23, false);
					ps2.setHidden(887, 23, false);
					ps2.sendString(887, 23, getGambleData().host.getName() + " has won<br>the gamble!");
					ps1.sendString(887, 23, getGambleData().host.getName() + " has won<br>the gamble!");
					e.delay(5);
					Winner(getGambleData().participant, getGambleData().host);
				});
			} else if (getGambleData().playerTwoBlackJackValue >= getGambleData().playerOneBlackJackValue) {
				World.startEvent(e -> {
					ps1.setHidden(887, 23, false);
					ps2.setHidden(887, 23, false);
					ps2.sendString(887, 23, getGambleData().host.getName() + " has won<br>the gamble!");
					ps1.sendString(887, 23, getGambleData().host.getName() + " has won<br>the gamble!");
					e.delay(5);
					Winner(getGambleData().host, getGambleData().participant);
				});
			}
		}
	}

	public void HandleFlowerABCGame(NPC npc, boolean plantLeft) {
		List<GameObject> playerOneFlowers = new ArrayList<>();
		List<GameObject> playerTwoFlowers = new ArrayList<>();
		AtomicInteger playerOneScore = new AtomicInteger();
		AtomicInteger playerTwoScore = new AtomicInteger();

		World.startEvent(e -> {
			playerOne.lock();
			playerTwo.lock();
			e.delay(1);
			npc.forceText("3..");
			e.delay(1);
			npc.forceText("2..");
			e.delay(1);
			npc.forceText("1..");
			e.delay(1);
			npc.forceText("GO!");
			GamblerText(npc,
				playerOne.getName() + " - " + playerOneScore + " : " + playerTwo.getName() + " - " + playerTwoScore);

			while (playerOneScore.get() != 3 && playerTwoScore.get() != 3) {
				e.delay(2);
				Flowers playerOneFlower = roll();
				Flowers playerTwoFlower = roll();
				playerOneFlowers.add(0, GameObject.spawn(playerOneFlower.objId, playerOne.getAbsX(), playerOne.getAbsY(),
					playerOne.getHeight(), 10, 0));
				playerOne.getRouteFinder().routeAbsolute(
					plantLeft ? playerOne.getPosition().getX() - 1 : playerOne.getPosition().getX() + 1,
					playerOne.getPosition().getY());
				npc.getRouteFinder().routeAbsolute(plantLeft ? npc.getPosition().getX() - 1 : npc.getPosition().getX() + 1,
					npc.getPosition().getY());
				e.waitForMovement(playerOne);
				playerOne.face(playerOneFlowers.get(0));
				e.delay(3);
				playerTwoFlowers.add(0, GameObject.spawn(playerTwoFlower.objId, playerTwo.getAbsX(), playerTwo.getAbsY(),
					playerTwo.getHeight(), 10, 0));
				playerTwo.getRouteFinder().routeAbsolute(
					plantLeft ? playerTwo.getPosition().getX() - 1 : playerTwo.getPosition().getX() + 1,
					playerTwo.getPosition().getY());
				e.waitForMovement(playerTwo);
				playerTwo.face(playerTwoFlowers.get(0));
				npc.face(playerOne);

				playerTwo.face(playerTwoFlowers.get(0));
				e.delay(2);

				if (playerOneFlower.flowerOrder < playerTwoFlower.flowerOrder) {
					playerOneScore.getAndIncrement();
					playerOne.forceText("" + playerOneScore + " - " + playerTwoScore);
					playerTwo.forceText("" + playerTwoScore + " - " + playerOneScore);
					if (playerOneScore.get() == 3) {
						Winner(playerOne, playerTwo);
						GamblerText(npc, playerOne.getName() + " has won the game!");
						e.delay(3);
						npc.remove();
					}
				} else if (playerTwoFlower.flowerOrder < playerOneFlower.flowerOrder) {
					playerTwoScore.getAndIncrement();
					playerOne.forceText("" + playerOneScore + " - " + playerTwoScore);
					playerTwo.forceText("" + playerTwoScore + " - " + playerOneScore);
					if (playerTwoScore.get() == 3) {
						Winner(playerTwo, playerOne);
						GamblerText(npc, playerTwo.getName() + " has won the game!");
						e.delay(3);

						npc.remove();
					}
				} else {
					playerOne.forceText("TIE!");
					playerTwo.forceText("TIE!");
				}
				e.delay(2);
				playerOneFlowers.get(0).remove();
				playerTwoFlowers.get(0).remove();
				playerOneFlowers.clear();
				playerTwoFlowers.clear();
				playerTwo.getMovement().teleport(
					plantLeft ? playerTwo.getPosition().getX() + 1 : playerTwo.getPosition().getX() - 1,
					playerTwo.getPosition().getY(), 1);
				playerOne.getMovement().teleport(
					plantLeft ? playerOne.getPosition().getX() + 1 : playerOne.getPosition().getX() - 1,
					playerOne.getPosition().getY(), 1);
				npc.getMovement().teleport(plantLeft ? npc.getPosition().getX() + 1 : npc.getPosition().getX() - 1,
					npc.getPosition().getY(), 1);
				npc.face(playerOne);
				if (playerOneScore.get() != 3 && playerTwoScore.get() != 3) {
					GamblerText(npc,
						playerOne.getName() + " - " + playerOneScore + " : " + playerTwo.getName() + " - " + playerTwoScore);
				}

			}
		});
	}

	public void HandleFlowerPokerGame(NPC npc, boolean plantLeft) {
		List<GameObject> playerOneFlowers = new ArrayList<>();
		List<GameObject> playerTwoFlowers = new ArrayList<>();
		HashMap<Integer, Integer> playerOneHand = new HashMap<>();
		HashMap<Integer, Integer> playerTwoHand = new HashMap<>();
		AtomicInteger playerOnePairs = new AtomicInteger();
		AtomicBoolean playerOneHas3OAK = new AtomicBoolean(false);
		AtomicBoolean playerOneHas4OAK = new AtomicBoolean(false);
		AtomicBoolean playerOneHas5OAK = new AtomicBoolean(false);
		AtomicBoolean playerOneHasFH = new AtomicBoolean(false);
		AtomicInteger playerTwoPairs = new AtomicInteger();
		AtomicBoolean playerTwoHas3OAK = new AtomicBoolean(false);
		AtomicBoolean playerTwoHas4OAK = new AtomicBoolean(false);
		AtomicBoolean playerTwoHas5OAK = new AtomicBoolean(false);
		AtomicBoolean playerTwoHasFH = new AtomicBoolean(false);

		AtomicBoolean gameTied = new AtomicBoolean(false);

		World.startEvent(e -> {
			playerOne.lock();
			playerTwo.lock();
			e.delay(1);
			npc.forceText("3..");
			e.delay(1);
			npc.forceText("2..");
			e.delay(1);
			npc.forceText("1..");
			e.delay(1);
			npc.forceText("GO!");
			for (int i = 0; i < 5; i++) {
				Flowers playerOneFlower = roll();
				Flowers playerTwoFlower = roll();
				playerOneFlowers.add(GameObject.spawn(playerOneFlower.objId, playerOne.getAbsX(), playerOne.getAbsY(),
					playerOne.getHeight(), 10, 0));
				playerTwoFlowers.add(GameObject.spawn(playerTwoFlower.objId, playerTwo.getAbsX(), playerTwo.player.getAbsY(),
					playerTwo.player.getHeight(), 10, 0));
				playerOneHand.put(playerOneFlower.objId,
					playerOneHand.get(playerOneFlower.objId) == null ? 1 : playerOneHand.get(playerOneFlower.objId) + 1);
				playerTwoHand.put(playerTwoFlower.objId,
					playerTwoHand.get(playerTwoFlower.objId) == null ? 1 : playerTwoHand.get(playerTwoFlower.objId) + 1);
				playerOne.getRouteFinder().routeAbsolute(
					plantLeft ? playerOne.getPosition().getX() - 1 : playerOne.getPosition().getX() + 1,
					playerOne.getPosition().getY());
				playerTwo.getRouteFinder().routeAbsolute(
					plantLeft ? playerTwo.getPosition().getX() - 1 : playerTwo.getPosition().getX() + 1,
					playerTwo.getPosition().getY());
				npc.getRouteFinder().routeAbsolute(plantLeft ? npc.getPosition().getX() - 1 : npc.getPosition().getX() + 1,
					npc.getPosition().getY());
				e.waitForMovement(playerOne);
				npc.face(playerOne);
				playerOne.face(playerOneFlowers.get(i));
				playerTwo.face(playerTwoFlowers.get(i));
				e.delay(2);
			}

			playerOneHand.forEach((k, v) -> {
				if (v == 2)
					playerOnePairs.getAndIncrement();
				else if (v == 3)
					playerOneHas3OAK.set(true);
				else if (v == 4)
					playerOneHas4OAK.set(true);
				else if (v == 5)
					playerOneHas5OAK.set(true);
			});

			playerTwoHand.forEach((k, v) -> {
				if (v == 2)
					playerTwoPairs.getAndIncrement();
				else if (v == 3)
					playerTwoHas3OAK.set(true);
				else if (v == 4)
					playerTwoHas4OAK.set(true);
				else if (v == 5)
					playerTwoHas5OAK.set(true);
			});

			if (playerOneHas3OAK.get() && playerOnePairs.get() == 1)
				playerOneHasFH.set(true);

			if (playerTwoHas3OAK.get() && playerTwoPairs.get() == 1)
				playerTwoHasFH.set(true);

			if (playerOneHas5OAK.get())
				playerOne.forceText("5 OF A KIND!");
			else if (playerOneHas4OAK.get())
				playerOne.forceText("4 OF A KIND!");
			else if (playerOneHasFH.get())
				playerOne.forceText("FULL HOUSE!");
			else if (playerOneHas3OAK.get())
				playerOne.forceText("3 OF A KIND!");
			else if (playerOnePairs.get() > 0)
				playerOne.forceText(playerOnePairs + "P!");
			else
				playerOne.forceText("BUST!");

			if (playerTwoHas5OAK.get())
				playerTwo.forceText("5 OF A KIND!");
			else if (playerTwoHas4OAK.get())
				playerTwo.forceText("4 OF A KIND!");
			else if (playerTwoHasFH.get())
				playerTwo.forceText("FULL HOUSE!");
			else if (playerTwoHas3OAK.get())
				playerTwo.forceText("3 OF A KIND!");
			else if (playerTwoPairs.get() > 0)
				playerTwo.forceText(playerTwoPairs + "P!");
			else
				playerTwo.forceText("BUST!");

			e.delay(3);
			if (playerOneHas5OAK.get() && !playerTwoHas5OAK.get()) {
				Winner(playerOne, playerTwo);
				npc.forceText(playerOne.getName() + " has a better hand so they have won!");
				e.delay(3);
				npc.remove();
			} else if (!playerOneHas5OAK.get() && playerTwoHas5OAK.get()) {
				Winner(playerTwo, playerOne);
				npc.forceText(playerTwo.getName() + " has a better hand so they have won!");
				e.delay(3);
				npc.remove();
			} else if (playerOneHas4OAK.get() && !playerTwoHas4OAK.get()) {
				Winner(playerOne, playerTwo);
				npc.forceText(playerOne.getName() + " has a better hand so they have won!");
				e.delay(3);
				npc.remove();
			} else if (!playerOneHas4OAK.get() && playerTwoHas4OAK.get()) {
				Winner(playerTwo, playerOne);
				npc.forceText(playerTwo.getName() + " has a better hand so they have won!");
				e.delay(3);
				npc.remove();
			} else if (playerOneHasFH.get() && !playerTwoHasFH.get()) {
				Winner(playerOne, playerTwo);
				npc.forceText(playerOne.getName() + " has a better hand so they have won!");
				e.delay(3);
				npc.remove();
			} else if (!playerOneHasFH.get() && playerTwoHasFH.get()) {
				Winner(playerTwo, playerOne);
				npc.forceText(playerTwo.getName() + " has a better hand so they have won!");
				e.delay(3);
				npc.remove();
			} else if (playerOneHas3OAK.get() && !playerTwoHas3OAK.get()) {
				Winner(playerOne, playerTwo);
				npc.forceText(playerOne.getName() + " has a better hand so they have won!");
				e.delay(3);
				npc.remove();
			} else if (!playerOneHas3OAK.get() && playerTwoHas3OAK.get()) {
				Winner(playerTwo, playerOne);
				npc.forceText(playerTwo.getName() + " has a better hand so they have won!");
				e.delay(3);
				npc.remove();
			} else if (playerOnePairs.get() > playerTwoPairs.get()) {
				Winner(playerOne, playerTwo);
				npc.forceText(playerOne.getName() + " has a better hand so they have won!");
				e.delay(3);
				npc.remove();
			} else if (playerOnePairs.get() < playerTwoPairs.get()) {
				Winner(playerTwo, playerOne);
				npc.forceText(playerTwo.getName() + " has a better hand so they have won!");
				e.delay(3);
				npc.remove();
			} else
				gameTied.set(true);

			for (int i = playerOneFlowers.size() - 1; i >= 0; i--) {
				playerOneFlowers.get(i).remove();
			}
			for (int i = playerTwoFlowers.size() - 1; i >= 0; i--) {
				playerTwoFlowers.get(i).remove();
			}
			playerOneFlowers.clear();
			playerTwoFlowers.clear();
			if (gameTied.get()) {
				npc.forceText("Tie! The game will restart shortly!");
				playerTwo.unlock();
				playerOne.unlock();
				e.delay(2);
				npc.getMovement().teleport(plantLeft ? npc.getPosition().getX() + 5 : npc.getPosition().getX() - 5,
					npc.getPosition().getY(), 1);
				playerTwo.getMovement().teleport(
					plantLeft ? playerTwo.getPosition().getX() + 5 : playerTwo.getPosition().getX() - 5,
					playerTwo.getPosition().getY(), 1);
				playerOne.getMovement().teleport(
					plantLeft ? playerOne.getPosition().getX() + 5 : playerOne.getPosition().getX() - 5,
					playerOne.getPosition().getY(), 1);
				HandleFlowerPokerGame(npc, plantLeft);
			} else {
				playerTwo.unlock();
				playerOne.unlock();
				e.delay(1);
			}
		});
	}

	public void startFlowerGame(int flowerGame) {
		Bounds spawnBounds = Gamble.GAMBLEZONE_BOUNDS[Random.get(Gamble.GAMBLEZONE_BOUNDS.length - 1)];
		Position spawnPos = spawnBounds.randomPosition();
		;

		playerOne.getGamble().getGambleData().spawnPosition = spawnPos;

		// spawnPos.set(Random.get(0, 1) == 0 ? spawnBounds.bottomLeftX :
		// spawnBounds.topRightX, spawnPos.getY(), 1);
		boolean plantLeft = false;

		int y;
		int npcY;

		if (spawnPos.getY() == spawnBounds.neY) {
			y = spawnPos.getY() - 1;
			npcY = spawnPos.getY() + 1;
		} else {
			y = spawnPos.getY() + 1;
			npcY = spawnPos.getY() - 1;
		}

		Position playerTwoSpawnPos = new Position(spawnPos.getX(), y, 1);

		if (spawnPos.getX() - 5 < spawnBounds.swX)
			plantLeft = false;
		else if (spawnPos.getX() + 5 > spawnBounds.neX)
			plantLeft = true;
		else
			System.out.println("AN UNEXPECTED ERROR OCCURRED!");

		playerOne.getMovement().teleport(spawnPos.getX(), spawnPos.getY(), 1);
		playerTwo.getMovement().teleport(playerTwoSpawnPos.getX(), playerTwoSpawnPos.getY(), 1);
		NPC npc = new NPC(3105).spawn(spawnPos.getX(), npcY, spawnPos.getZ());
		npc.face(playerOne);
		switch (flowerGame) {
			case 0:
				HandleFlowerPokerGame(npc, plantLeft);
				break;
			case 1:
				HandleFlowerABCGame(npc, plantLeft);
				break;
		}

	}

	public boolean hasStarted() {
		return started;
	}

	public void process() {
		if (started) {
			if (blackJackTimer != null) {
				if (blackJackTimer.remaining() <= 0 && blackJackTimerStarted) {
					blackJackTimer.delaySeconds(30);
					HandleBlackJackHit(getGambleData().blackJackTurn);
				}
			}
		}
	}

	public void Winner(Player winner, Player loser) {
		if (getGambleData().winnerDetermined)
			return;
		getGambleData().winnerDetermined = true;
		getGambleData().inBlackJack = false;
		winner.closeInterfaces();
		loser.closeInterfaces();
		ItemContainer container = new ItemContainer();
		container.init(player, 28, -1, 63761, 90, false);
		container.sendAll = true;
		winner.sendMessage("Congratulations, you have won!");
		loser.sendMessage("Oh no, you have lost!");
		winner.unlock();
		loser.unlock();
		playerOne.getGamble().targetGamble = null;
		playerTwo.getGamble().targetGamble = null;
		playerOne.getGamble().started = false;
		playerTwo.getGamble().started = false;
		playerTwo.getGamble().stage = 0;
		playerOne.getGamble().stage = 0;
		StringBuilder items = new StringBuilder();

		getGambleData().items.forEach((key, value) -> {
			System.out.println("item id is " + key);
			if (key != null) {
				items.append(new Item(key).getDef().name).append(" x").append(value).append(", ");
				System.out.println("item id not null " + key);
				if (key == 995) {
					System.out.println("coins");
					if (value >= 1000) {
						convertGpToPlatinumToken(value, winner);
						System.out.println("converted to plat");
					}
				} else
					winner.getInventory().addOrSendToBank(key, value.intValue());
			}
		});
//		RareDropEmbedMessage.sendGambleLogsToDiscord(items.toString(), winner.getName(), loser.getName());

		getGambleData().items.clear();
		winner.getPacketSender().sendString(1085, 18, "The Spoils:");
		winner.getPacketSender().sendString(1085, 15, "You have won!");
		winner.getPacketSender().sendString(1085, 19, "The Defeated:");

		loser.getPacketSender().sendString(1085, 18, "You Lost:");
		loser.getPacketSender().sendString(1085, 15, "You lost!");
		loser.getPacketSender().sendString(1085, 19, "The Winner:");
	}

	GambleData getGambleData() {
		return GambleData.instance.get(id);
	}

	private void convertGpToPlatinumToken(long coinAmount, Player player) {
		if (coinAmount >= 1000) {
			long platinumAmount = coinAmount / 1000;
			long gpRemainder = coinAmount % 1000;
			player.getInventory().addOrSendToBank(ItemID.PLATINUM_TOKEN, (int) platinumAmount);
			player.getInventory().addOrSendToBank(ItemID.COINS_995, (int) gpRemainder);
		}
	}

}
