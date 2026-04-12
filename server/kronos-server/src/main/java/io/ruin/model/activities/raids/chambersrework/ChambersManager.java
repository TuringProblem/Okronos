package io.ruin.model.activities.raids.chambersrework;

import io.ruin.model.activities.raids.toa.ToAParty;
import io.ruin.model.activities.raids.toa.TombsOfAmascut;
import io.ruin.model.entity.player.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ChambersManager {
	@Getter
	static List<CustomXericRaid> activeRaids = new ArrayList<>();
	@Getter
	static List<XericParty> activeRaidParties = new ArrayList<>();

	public static void addRaid(CustomXericRaid raid) {
		activeRaids.add(raid);
	}

	public static int getRaidIdFromPlayer(Player player) {
		for (int i = 0; i < activeRaids.size(); i++) {
			if (activeRaids.get(i).currentParty.getMembers().contains(player.getName()))
				return i;
		}
		return -1;
	}

	public static CustomXericRaid getRaid(int index) {
		if (index < 0 || index >= activeRaids.size())
			return null;
		return activeRaids.get(index);
	}

	public static CustomXericRaid getRaid(Player player) {
		for (CustomXericRaid raid : activeRaids) {
			if (raid.currentParty.getMembers().contains(player.getName()))
				return raid;
		}
		return null;
	}

	public static void removeRaid(CustomXericRaid raid) {
		activeRaids.remove(raid);
	}

	public static void addRaidParty(XericParty party) {
		activeRaidParties.add(0, party);
	}

	public static XericParty getRaidParty(Player player) {
		return activeRaidParties.stream().filter(party -> party.getMembers().contains(player.getName())).findFirst().orElse(null);
	}

}
