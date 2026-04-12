package io.ruin.model.activities.raids.toa;

import io.ruin.model.entity.player.Player;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TombsOfAmascutManager {
	@Getter
	static List<TombsOfAmascut> activeRaids = new ArrayList<>();
	@Getter
	static List<ToAParty> activeRaidParties = new ArrayList<>();

	public static void addRaid(TombsOfAmascut raid) {
		if (raid == null) {
			return;
		}
		activeRaids.add(raid);
	}

	public static int getRaidIdFromPlayer(Player player) {
		for (int i = 0; i < activeRaids.size(); i++) {
			if (activeRaids.get(i).currentParty.getMembers().contains(player.getName()))
				return i;
		}
		return -1;
	}

	public static TombsOfAmascut getRaid(int index) {
		if (index < 0 || index >= activeRaids.size()) {
			return null;
		}
		return activeRaids.get(index);
	}

	public static TombsOfAmascut getRaid(Player player) {
		if (player == null) {
			return null;
		}

		if (activeRaids == null) {
			return null;
		}

		try {
			for (var raid : activeRaids) {
				if (raid != null &&
						raid.currentParty != null &&
						raid.currentParty.getMembers() != null &&
						raid.currentParty.getMembers().contains(player.getName())) {
					return raid;
				}
			}
		} catch (Exception e) {
			log.error("Error in getRaid for player: " + player.getName(), e);
		}
		return null;
	}

	public static void removeRaid(TombsOfAmascut raid) {
		if (raid == null) {
			return;
		}
		activeRaids.remove(raid);
	}

	public static void addRaidParty(ToAParty party) {
		activeRaidParties.add(0, party);
	}

	public static ToAParty getRaidParty(Player player) {
		return activeRaidParties.stream().filter(party -> party.getMembers().contains(player.getName())).findFirst()
				.orElse(null);
	}

}
