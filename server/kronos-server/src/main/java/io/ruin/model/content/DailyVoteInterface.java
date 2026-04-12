package io.ruin.model.content;

import discord.webhooks.logs.VotingHook;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.item.Item;
import org.json.JSONObject;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class DailyVoteInterface {
	public class DailyReward {
		transient boolean claimed;
		transient Item reward;

		public DailyReward(boolean claimed, Item reward) {
			this.claimed = claimed;
			this.reward = reward;
		}

		public void setClaimed(boolean claimed) {
			this.claimed = claimed;
		}
	}

	private static final int INTERFACE_ID = 1109;

	private static final List<Item> voteRewards = Arrays.asList(
			new Item(30570, 1),
			new Item(620, 1),
			new Item(7478, 2),
			new Item(995, 2500000),
			new Item(30596, 2),
			new Item(608, 1),
			new Item(30461, 1),
			new Item(620, 2),
			new Item(995, 10000000),
			new Item(30464, 1),
			new Item(30570, 2),
			new Item(620, 3),
			new Item(7478, 3),
			new Item(30596, 3),
			new Item(608, 2),
			new Item(995, 2500000),
			new Item(30461, 1),
			new Item(22092, 1),
			new Item(620, 4),
			new Item(30464, 2),
			new Item(30456, 2),
			new Item(7478, 4),
			new Item(995, 50000000),
			new Item(608, 3),
			new Item(30462, 1),
			new Item(7478, 5),
			new Item(22092, 1),
			new Item(30464, 3));

	public static void register() {
		InterfaceHandler.register(1109, h -> {
			h.actions[468] = (SimpleAction) (p) -> p.getDailyVote().claimReward(p);
		});

	}

	Player player;
	List<Item> rewardsLogged = new ArrayList<>();
	private int containerId = 2000;

	HashMap<Integer, DailyReward> entries = new HashMap<>();

	public DailyVoteInterface(Player player) {
		this.player = player;

	}

	public void open() {

		player.openInterface(ToplevelComponent.MAINMODAL, INTERFACE_ID);
		player.getPacketSender().sendString(INTERFACE_ID, 22, "Current Vote Streak: " + player.voteStreak);
		int startingLockComponent = 42;
		int startingClaimedComponent = 40;
		int startingContainerComponent = 41;
		int startingTextComponent = 39;

		List<Integer> itemContainerComponentIds = new ArrayList<>();

		int loops = 0;
		for (int i = 0; i < 28; i++) {
			if (loops == 4) {
				startingClaimedComponent++;
				startingContainerComponent++;
				startingTextComponent++;
				loops = 0;
			}
			player.getPacketSender().sendString(INTERFACE_ID, startingClaimedComponent, "<col=cc3333>Unclaimed");

			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					INTERFACE_ID << 16 | startingContainerComponent, 1000 + i,
					4, 7, 1, -1, "", "", "", "", "");

			player.getPacketSender().sendItems(
					-1,
					startingContainerComponent,
					1000 + i,
					voteRewards.get(i));
			itemContainerComponentIds.add(i, startingContainerComponent);
			player.getPacketSender().sendString(INTERFACE_ID, startingTextComponent, "Day " + (i + 1));

			loops++;
			startingClaimedComponent += 14;
			startingContainerComponent += 14;
			startingTextComponent += 14;
		}
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				INTERFACE_ID << 16 | 464, 2850,
				4, 7, 1, -1, "", "", "", "", "");

		var rewardIdx = player.votedToday ? player.voteStreak - 1 : player.voteStreak;
		var reward = rewardIdx >= voteRewards.size() ? voteRewards.getLast() : voteRewards.get(rewardIdx);
		player.getPacketSender().sendItems(
				-1,
				464,
				2850,
				reward);

		loops = 0;
		startingClaimedComponent = 40;

		for (int i = 0; i < player.voteStreak; i++) {
			if (loops == 4) {
				startingLockComponent++;
				startingClaimedComponent++;
				loops = 0;
			}
			player.getPacketSender().setHidden(INTERFACE_ID, startingLockComponent, true);

			if (i >= (player.voteStreak - 1) && i < player.voteStreak)
				player.getPacketSender().sendString(INTERFACE_ID, startingClaimedComponent, "<col=26ff1f>Claimed");
			else if (i == (player.voteStreak - 1) && player.claimedVoteToday)
				player.getPacketSender().sendString(INTERFACE_ID, startingClaimedComponent, "<col=26ff1f>Claimed");
			else
				player.getPacketSender().sendString(INTERFACE_ID, startingClaimedComponent, "<col=cc3333>Unclaimed");

			startingLockComponent += 14;
			startingClaimedComponent += 14;
			loops++;
		}
	}

	public void voteCheck() {
		Instant now = Instant.now();
		Instant lastVoteInstant = Instant.ofEpochSecond(player.lastVoteInEpoch);
		Instant lastVoteRewardInstant = Instant.ofEpochSecond(player.lastVoteRewardInEpoch);

		long hoursSinceLastVote = Duration.between(lastVoteInstant, now).toHours();
		long hoursSinceLastVoteReward = Duration.between(lastVoteRewardInstant, now).toHours();
		boolean canGetReward = hoursSinceLastVoteReward >= 18 && hoursSinceLastVote < 36;

		if (canGetReward || player.lastVoteInEpoch <= 0 || player.lastVoteRewardInEpoch <= 0) {
			player.canClaimVoteReward = true;
			player.claimedVoteToday = false;
			player.votedToday = true;
			player.lastVoteStreakRewardInEpoch = now.getEpochSecond();

			player.voteStreak++;
			if (player.voteStreak > 29) {
				player.voteStreak = 1;
				entries.clear();
				player.getDailyVote().voteRewardRefresh();
				rewardsLogged.clear();
			} else {
				player.sendMessage("You now have a daily vote streak of " + player.voteStreak + ".");
			}
		} else if (hoursSinceLastVote >= 36) {
			player.votedToday = true;
			player.canClaimVoteReward = true;
			player.claimedVoteToday = false;
			player.voteStreak = 1;
			entries.clear();
			player.getDailyVote().voteRewardRefresh();
			player.sendMessage("Your vote streak has been reset as you failed to vote within 36 hours.");
			rewardsLogged.clear();
		}

		player.lastVoteInEpoch = now.getEpochSecond();
	}

	public void voteRewardRefresh() {
		Instant now = Instant.now();
		Instant instant = Instant.ofEpochSecond(player.lastVoteRewardInEpoch);
		LocalDate dateFromTimestamp = instant.atZone(ZoneId.systemDefault()).toLocalDate();

		LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1);

		LocalDate lastVoteDate = Instant.ofEpochSecond(player.lastVoteRewardInEpoch)
				.atZone(ZoneId.systemDefault())
				.toLocalDate();

		if (player.voteStreak >= 28)
			player.voteStreak = 1;

		if (dateFromTimestamp.isEqual(today)) {
			player.todaysVoteReward = new DailyReward(false,
					voteRewards.get(player.votedToday ? player.voteStreak - 1 : player.voteStreak));
			entries.put(player.voteStreak, player.todaysVoteReward);
		} else {
			player.votedToday = false;
			player.lastVoteRewardInEpoch = today.toEpochDay();
			player.todaysVoteReward = new DailyReward(false, voteRewards.get(player.voteStreak));
			entries.put(player.voteStreak, player.todaysVoteReward);
		}

	}

	private void refreshInterface() {
		for (int i = 165; i < 195; i++)
			sendItemToContainer(player, i, new Item(-1));
		for (int i = 199; i < 315; i += 4)
			player.getPacketSender().setHidden(INTERFACE_ID, i, false);
		sendItemToContainer(player, 330, new Item(-1));
		player.getPacketSender().setHidden(INTERFACE_ID, 322, false);
	}

	private void sendItemToContainer(Player player, int containerComponentId, Item item) {
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				INTERFACE_ID << 16 | containerComponentId, containerId,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendItems(
				-1,
				containerComponentId,
				containerId,
				item);
		containerId++;
	}

	private void claimReward(Player player) {
		if (player.getInventory().getFreeSlots() < 3) {
			player.sendMessage("You need at least 3 free inventory slots to claim your reward.");
			return;
		}
		if (!player.claimedVoteToday) {
			if (player.canClaimVoteReward) {
				player.canClaimVoteReward = false;
				var rewardIdx = player.voteStreak - 1;

				Item reward;
				if (rewardIdx >= voteRewards.size()) {
					reward = voteRewards.getLast();
				} else {
					reward = voteRewards.get(rewardIdx);
				}

				player.getInventory().addOrDrop(reward);
				player.claimedVoteToday = true;
				player.sendMessage("You have received " + reward.getAmount() + "x " + reward.getDef().name + ".");
				player.getDailyVote().open();

				var dto = new JSONObject()
					.put("player", player.getName())
					.put("vote_streak", player.voteStreak)
					.put("hwid", player.hwid)
					.put("item_id", reward.getId())
					.put("item_name", reward.getDef().name)
					.put("item_amount", reward.getAmount());
				VotingHook.sendClaimedVoteStreak(dto);

			} else {
				player.sendMessage("You must vote today to claim this reward.");
			}
		} else {
			player.sendMessage("You have already claimed your reward for today.");
		}
	}
}
