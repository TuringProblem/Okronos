package io.ruin.model.entity.npc.actions.edgeville;

import io.ruin.Server;
import io.ruin.api.utils.StringUtils;
import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.utility.Broadcast;

import java.util.*;

public class VoteGambler {

	private static boolean enabled = true;
	private static boolean isFirstRaffle = true; // Added variable to track the first raffle

	private static final int VOTE_TICKET_ID = 30602;
	private static final int NPC_ID = 4058;
	private static final int REWARD_ID = 30461;

	private static Set<String> entries = new HashSet<>();
	private static List<Winner> unclaimedRewards = new ArrayList<>();

	public static Set<String> getEntries() {
		return entries;
	}

	private static Map<String, Long> entryTimes = new HashMap<>();

	public static void addEntryTime(String username, long startTime) {
		entryTimes.put(username.toLowerCase(), startTime);
	}

	public static long getEntryStartTime(String user) {
		return entryTimes.getOrDefault(user.toLowerCase(), 0L);
	}

	public static List<Winner> getUnclaimedRewards() {
		return unclaimedRewards;
	}

	public static void register() {
		NPCAction.register(NPC_ID, "Lottery-info", VoteGambler::lotteryInfo);

		World.startEvent(event -> {
			while (true) {
				event.delay(Server.toTicks(432 * 100));
				handleWinnerEntry();
			}
		});
		LoginListener.register(player -> {
			if (!unclaimedRewards.isEmpty()) {
				unclaimedRewards.stream()
					.filter(win -> win.getUsername().equalsIgnoreCase(player.getName()))
					.findAny()
					.ifPresent(winner -> {
						if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
							player.getInventory().addOrDrop(REWARD_ID, 2);
							player.sendMessage("2 Donator boxes were added to your inventory for winning the vote lottery.");
						} else {
							player.getBank().add(REWARD_ID, 2);
							player.sendMessage("2 Donator boxes were added to your bank for winning the vote lottery.");
						}
						unclaimedRewards.remove(winner);
					});
			}
		});
	}

	private static void lotteryInfo(Player player, NPC npc) {
		int totalEntries = entries.size();
		String type = totalEntries == 1 ? "entry" : "entries";
		String message = totalEntries == 0 ? "There are no entries" : "There is currently " + Color.RED.wrap(totalEntries + " ") + type;
		player.dialogue(new NPCDialogue(npc, message));
	}

	public static void convertTicket(Player player, Item item) {
		player.votePoints++;
		player.getInventory().remove(620, 1);
		player.sendMessage("You have converted a vote ticket to a vote point. Your total is now: " + player.getVotePoints());
	}

	public static void handleEntry(Player player, Item item) {
		if (!enabled) {
			sendMessage(player, "Vote lottery is currently disabled.");
			return;
		}

       /* if (alreadyEntered(player)) {
            long timeUntilRaffle = calculateTimeUntilNextDrawing();
            if (timeUntilRaffle > 0) {
                long hours = timeUntilRaffle / (60 * 60 * 1000);
                long minutes = (timeUntilRaffle % (60 * 60 * 1000)) / (60 * 1000);
                long seconds = (timeUntilRaffle % (60 * 1000)) / 1000;
                sendMessage(player, "You have already been entered into the raffle. Time until the next drawing: " + hours + "h " + minutes + "m " + seconds + "s");
            } else {
                sendMessage(player, "You have already entered into the raffle. Time until next drawing: 0h 0m 0s");
            }
            return;
        }

        */

		player.getInventory().remove(30602, 1);
		addEntry(player);
		addEntryTime(player.getName().toLowerCase(), System.currentTimeMillis());
		sendMessage(player, "You've entered the vote raffle. Good luck!");

		if (isFirstRaffle) {
			// Set isFirstRaffle to false after the first entry
			isFirstRaffle = false;
		}
	}

	public static boolean isFirstRaffle() {
		return isFirstRaffle;
	}

	public static long calculateTimeUntilNextDrawing() {
		int totalEntries = entries.size();
		if (entries.isEmpty() || isFirstRaffle) {
			// No entries or first raffle, return the full raffle duration
			return 12 * 60 * 60 * 1000;
		} else {
			// Calculate time until next drawing based on the first entry time
			long entryTime = VoteGambler.getEntryStartTime(entries.iterator().next());
			return 12 * 60 * 60 * 1000 - (System.currentTimeMillis() - entryTime);
		}
	}

	private static void handleWinnerEntry() {
		int totalEntries = entries.size();

		if (entries.isEmpty()) {
			sendBroadcast("As there were no participants, no prizes have been awarded this vote raffle. Enter now!");
			return;
		}

		Optional<String> entryWinnerOptional = entries.stream().skip(new Random().nextInt(totalEntries)).findFirst();

		if (!entryWinnerOptional.isPresent()) {
			System.err.println("This should not be empty...");
			return;
		}

		String winnerName = entryWinnerOptional.get();

		Optional<Player> playerOptional = Optional.ofNullable(World.getPlayer(winnerName));
		if (playerOptional.isPresent()) {
			Player player = playerOptional.get();
			rewardAndReset(player);
		} else {
			unclaimedRewards.add(new Winner(winnerName));
			sendBroadcast("The winner has been picked for the Vote lottery! Vote now for a chance to win the prize of 2 Mystery boxes.");
			clearAllEntries();
		}
	}

	private static void rewardAndReset(Player player) {
		if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
			player.getInventory().addOrDrop(REWARD_ID, 2);
			sendMessage(player, "You have won the vote raffle draw!");
			sendBroadcast(StringUtils.capitalizeFirst(player.getName()) + " has won the Vote Lottery!");
			sendBroadcast("Vote now for a chance to win 2 Mystery boxes!");
			clearAllEntries();
			return;
		}
		player.getBank().add(REWARD_ID, 2);
		sendMessage(player, "You have won the vote raffle draw!");
		sendBroadcast(StringUtils.capitalizeFirst(player.getName()) + " has won the Vote Lottery!");
		sendBroadcast("Vote now for a chance to win 2 Mystery boxes!");
		clearAllEntries();
	}

	private static void addEntry(Player player) {
		entries.add(player.getName().toLowerCase());
	}

	private static boolean alreadyEntered(Player player) {
		return entries.contains(player.getName().toLowerCase());
	}

	private static void clearAllEntries() {
		entries.clear();
	}

	private static void sendBroadcast(String message) {
		Broadcast.WORLD.sendNews("[Vote Lottery] " + message);
	}

	private static void sendMessage(Player player, String message) {
		player.sendFilteredMessage(Color.DARK_RED.wrap("[Vote Lottery]") + " " + message);
	}

	public static class Winner {
		String username;

		public Winner(String username) {
			this.username = username;
		}

		public String getUsername() {
			return username;
		}
	}
}
