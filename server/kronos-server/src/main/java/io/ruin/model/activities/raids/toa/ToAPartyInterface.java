package io.ruin.model.activities.raids.toa;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;

import java.util.HashMap;
import java.util.Map;

public class ToAPartyInterface {
	Map<Integer, ToAParty> partyMap = new HashMap<>();

	public void open(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, 1120);
		refresh(player);
	}

	public void refresh(Player player) {
		partyMap.clear();
		for (int i = 32; i <= 92; i += 10) {
			player.getPacketSender().setHidden(1120, i, true);
		}
		TombsOfAmascutManager.getActiveRaidParties()
			.removeIf(party -> party.getMembers().isEmpty());

		if (TombsOfAmascutManager.getActiveRaidParties().isEmpty())
			return;

		int startingComponentId = 32;
		int startingMemberComponentId = 35;
		int partyLeaderNameStringComponentId = 36;
		int numberOfInvocationsComponentId = 38;
		int raidLevelComponentId = 39;
		int raidLevelTextStringComponentId = 40;
		int timerTextStringComponentId = 41;
		player.getPacketSender().setHidden(1120, timerTextStringComponentId, true);
		for (ToAParty party :
			TombsOfAmascutManager.getActiveRaidParties()) {
			if (startingComponentId > 92)
				break;
			player.getPacketSender().setHidden(1120, startingComponentId, false);
			player.getPacketSender().sendString(1120, startingMemberComponentId, "%d/6".formatted(party.getMembers().size()));
			player.getPacketSender().sendString(1120, partyLeaderNameStringComponentId, party.getLeader());
			player.getPacketSender().sendString(1120, numberOfInvocationsComponentId, String.valueOf(party.getActiveInvocations().size()));
			player.getPacketSender().sendString(1120, raidLevelComponentId, String.valueOf(party.getCurrentInvocationValue()));
			String raidLevelText;
			if (party.getCurrentInvocationValue() < 150)
				raidLevelText = "Entry";
			else if (party.getCurrentInvocationValue() < 300)
				raidLevelText = "Normal";
			else
				raidLevelText = "Expert";

			player.getPacketSender().sendString(1120, raidLevelTextStringComponentId, raidLevelText);
			player.getPacketSender().sendString(1120, timerTextStringComponentId, party.convertTimeInLongToText());
			partyMap.put(startingComponentId, party);

			startingComponentId += 10;
			startingMemberComponentId += 10;
			partyLeaderNameStringComponentId += 10;
			numberOfInvocationsComponentId += 10;
			raidLevelComponentId += 10;
			raidLevelTextStringComponentId += 10;
			timerTextStringComponentId += 10;
		}
	}

	public ToAParty getPartyFromComponentId(int componentId) {
		return partyMap.get(componentId);
	}

	public static void register() {
		InterfaceHandler.register(1120, h -> {
			h.actions[18] = (SimpleAction) p -> p.getToaPartyInterface().refresh(p);
			h.actions[20] = (SimpleAction) p -> p.getMembersInterface().open(p);
			for (int i = 32; i <= 92; i += 10) {
				int finalI = i;
				h.actions[i] = (SimpleAction) p -> {
					ToAParty party = p.getToaPartyInterface().getPartyFromComponentId(finalI);
					if (party == null)
						return;
					p.getToaPartyViewInterface().open(p, party);
				};
			}
		});
	}
}
