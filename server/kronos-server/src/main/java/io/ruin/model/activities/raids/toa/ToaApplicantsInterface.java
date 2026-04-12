package io.ruin.model.activities.raids.toa;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;
import it.unimi.dsi.fastutil.Hash;

import java.util.HashMap;
import java.util.Map;

public class ToaApplicantsInterface {

	Map<Integer, String> applicantsMap = new HashMap<>();

	public void open(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, 1119);
		refresh(player);

	}

	public void refresh(Player player) {
		for (int i = 48; i <= 113; i += 13) {
			player.getPacketSender().setHidden(1119, i, true);

		}
		if (TombsOfAmascutManager.getRaidParty(player) == null) {
			player.getPacketSender().sendString(1119, 22, "Create Party");
		} else if (TombsOfAmascutManager.getRaidParty(player).getLeader().equals(player.getName())) {
			player.getPacketSender().sendString(1119, 22, "Disband");
		} else {
			player.getPacketSender().sendString(1119, 22, "Leave Party");
		}
		if (TombsOfAmascutManager.getRaidParty(player) == null)
			return;
		int startingComponentId = 48;
		int startingMemberNameComponentId = 51;
		int startingCombatLevel = 52;
		int startingAttackLevel = 53;
		int startingStrengthLevel = 54;
		int startingRangeLevel = 55;
		int startingMagicLevel = 56;
		int startingDefenceLevel = 57;
		int startingHitpointsLevel = 58;
		int startingPrayerLevel = 59;
		for (String member : TombsOfAmascutManager.getRaidParty(player).getApplications()) {
			if (startingComponentId > 113)
				break;
			Player memberPlayer = World.getPlayer(member);
			if (memberPlayer == null) {
				continue;
			}
			player.getPacketSender().setHidden(1119, startingComponentId, false);
			player.getPacketSender().sendString(1119, startingMemberNameComponentId, memberPlayer.getName());
			player.getPacketSender().sendString(1119, startingCombatLevel, "" + memberPlayer.getCombat().getLevel());
			player.getPacketSender().sendString(1119, startingAttackLevel, "" + memberPlayer.getStats().get(0).fixedLevel);
			player.getPacketSender().sendString(1119, startingStrengthLevel, "" + memberPlayer.getStats().get(2).fixedLevel);
			player.getPacketSender().sendString(1119, startingRangeLevel, "" + memberPlayer.getStats().get(4).fixedLevel);
			player.getPacketSender().sendString(1119, startingMagicLevel, "" + memberPlayer.getStats().get(6).fixedLevel);
			player.getPacketSender().sendString(1119, startingDefenceLevel, "" + memberPlayer.getStats().get(1).fixedLevel);
			player.getPacketSender().sendString(1119, startingHitpointsLevel, "" + memberPlayer.getStats().get(3).fixedLevel);
			player.getPacketSender().sendString(1119, startingPrayerLevel, "" + memberPlayer.getStats().get(5).fixedLevel);


			applicantsMap.put(startingComponentId, member);
			startingComponentId += 13;
			startingMemberNameComponentId += 13;
			startingCombatLevel += 13;
			startingAttackLevel += 13;
			startingStrengthLevel += 13;
			startingRangeLevel += 13;
			startingMagicLevel += 13;
			startingDefenceLevel += 13;
			startingHitpointsLevel += 13;
			startingPrayerLevel += 13;

		}
		player.getPacketSender().sendString(1119, 27, "Members (" + TombsOfAmascutManager.getRaidParty(player).getMembers().size() + ")");
	}

	public String getApplicantFromComponentId(int componentId) {
		return applicantsMap.get(componentId);
	}


	public static void register() {
		InterfaceHandler.register(1119, h -> {
			for (int i = 48; i <= 113; i += 13) {
				int finalI = i;
				h.actions[i] = (DefaultAction) (p, option, slot, id) -> {
					ToAParty party = TombsOfAmascutManager.getRaidParty(p);
					if (party == null)
						return;
					String applicant = p.getToaApplicantsInterface().getApplicantFromComponentId(finalI);
					if (!p.getName().equalsIgnoreCase(party.getLeader()))
						return;
					if (applicant == null)
						return;
					if (option == 1)
						party.acceptApplication(applicant);
					else if (option == 2)
						party.rejectApplication(applicant);
					p.getToaApplicantsInterface().refresh(p);
				};
			}
			h.actions[21] = (SimpleAction) p -> {
				if (TombsOfAmascutManager.getRaidParty(p) == null) {
					ToAParty party = new ToAParty(p.getName());
					party.createParty(p);
					p.getToaApplicantsInterface().refresh(p);
				} else if (TombsOfAmascutManager.getRaidParty(p).getLeader().equals(p.getName())) {
					TombsOfAmascutManager.getRaidParty(p).disbandParty(p);
					p.getMembersInterface().refresh(p);
					p.getToaApplicantsInterface().refresh(p);
				} else {
					TombsOfAmascutManager.getRaidParty(p).getMembers().remove(p.getName());
					p.getMembersInterface().refresh(p);
					p.getToaApplicantsInterface().refresh(p);
					TombsOfAmascutManager.getActiveRaidParties().forEach(party -> {
						party.getMembers().forEach(m -> {
							if (p.getName().equalsIgnoreCase(m)) {
								party.getMembers().remove(m);
							}
						});
					});
				}
			};
			h.actions[18] = (SimpleAction) p -> {
				p.getToaApplicantsInterface().refresh(p);
			};
			h.actions[23] = (SimpleAction) p -> {
				p.getMembersInterface().open(p);
			};
			h.actions[25] = (SimpleAction) p -> {
				p.getToaInvocationsInterface().open(p);
			};
		});
	}
}
