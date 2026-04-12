package io.ruin.model;

import io.ruin.model.entity.player.Player;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteHandler {
	static Map<String, Long> votedHwids = new HashMap<>();

	public static void addHwid(String hwid) {
		//System.out.println("Adding hwid: " + hwid);
		votedHwids.put(hwid, Instant.now().getEpochSecond());
		// System.out.println("value is: " + votedHwids.get(hwid));
	}

	public static void getAllHwids() {
		for (Map.Entry<String, Long> entry : votedHwids.entrySet()) {
			// System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
	}

	public static boolean playerVotedRecently(Player player) {
		Instant now = Instant.now();
		Instant instant = Instant.ofEpochSecond(player.lastVoteClaimInEpoch);
		if (now.isAfter(instant.plusSeconds(84000))) {
			return false;
		}
		return true;
	}

	public static boolean playerDonatedRecently(Player player) {
		Instant now = Instant.now();
		Instant instant = Instant.ofEpochSecond(player.lastDonationClaimInEpoch);
		if (now.isAfter(instant.plusSeconds(84000))) {
			return false;
		}
		return true;
	}

	public static boolean hasVotedRecently(String hwid) {
		// System.out.println("the hwid is: " + hwid);
		if (hwid.equalsIgnoreCase("unknown"))
			return false;
		if (votedHwids.get(hwid) == null) {
			// System.out.println("the hwid is null");
			return false;
		}
		long lastVoteEpoch = votedHwids.get(hwid);
		Instant now = Instant.now();
		Instant instant = Instant.ofEpochSecond(lastVoteEpoch);
		if (now.isAfter(instant.plusSeconds(36000))) {
			return false;
		}
		//System.out.println("the hwid: " + hwid + " has voted recently");
		return true;
	}
}
