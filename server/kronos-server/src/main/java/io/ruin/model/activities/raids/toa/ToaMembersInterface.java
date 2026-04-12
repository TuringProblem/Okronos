package io.ruin.model.activities.raids.toa;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;

public class ToaMembersInterface {
	public void open(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, 1118);
		refresh(player);

	}

	public void refresh(Player player) {
		for (int i = 48; i <= 113; i += 13) {
			player.getPacketSender().setHidden(1118, i, true);

		}
		if (TombsOfAmascutManager.getRaidParty(player) == null) {
			player.getPacketSender().sendString(1118, 22, "Create Party");
		} else if (TombsOfAmascutManager.getRaidParty(player).getLeader().equals(player.getName())) {
			player.getPacketSender().sendString(1118, 22, "Disband");
		} else {
			player.getPacketSender().sendString(1118, 22, "Leave Party");
		}
		player.getPacketSender().setHidden(1118, 32, true);
		player.getPacketSender().setHidden(1118, 34, true);
		if (TombsOfAmascutManager.getRaidParty(player) == null)
			return;
		player.getPacketSender().sendString(1118, 14, "Party of " + TombsOfAmascutManager.getRaidParty(player).getLeader());
		String raidLevelText;
		if (TombsOfAmascutManager.getRaidParty(player).getCurrentInvocationValue() < 150)
			raidLevelText = "Entry";
		else if (TombsOfAmascutManager.getRaidParty(player).getCurrentInvocationValue() >= 150 && TombsOfAmascutManager.getRaidParty(player).getCurrentInvocationValue() < 300)
			raidLevelText = "Normal";
		else
			raidLevelText = "Expert";
		player.getPacketSender().sendString(1118, 32, raidLevelText);
		player.getPacketSender().sendString(1118, 34, TombsOfAmascutManager.getRaidParty(player).getCurrentInvocationValue() + " <col=ffffff>(" + TombsOfAmascutManager.getRaidParty(player).getActiveInvocations().size() + ")");

		player.getPacketSender().setHidden(1118, 32, false);
		player.getPacketSender().setHidden(1118, 34, false);
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

		for (String member : TombsOfAmascutManager.getRaidParty(player).getMembers()) {
			if (startingComponentId > 113)
				break;
			Player memberPlayer = World.getPlayer(member);
			if (memberPlayer == null) {
				continue;
			}
			player.getPacketSender().setHidden(1118, startingComponentId, false);
			player.getPacketSender().sendString(1118, startingMemberNameComponentId, memberPlayer.getName());
			player.getPacketSender().sendString(1118, startingCombatLevel, "" + memberPlayer.getCombat().getLevel());
			player.getPacketSender().sendString(1118, startingAttackLevel, "" + memberPlayer.getStats().get(0).fixedLevel);
			player.getPacketSender().sendString(1118, startingStrengthLevel, "" + memberPlayer.getStats().get(2).fixedLevel);
			player.getPacketSender().sendString(1118, startingRangeLevel, "" + memberPlayer.getStats().get(4).fixedLevel);
			player.getPacketSender().sendString(1118, startingMagicLevel, "" + memberPlayer.getStats().get(6).fixedLevel);
			player.getPacketSender().sendString(1118, startingDefenceLevel, "" + memberPlayer.getStats().get(1).fixedLevel);
			player.getPacketSender().sendString(1118, startingHitpointsLevel, "" + memberPlayer.getStats().get(3).fixedLevel);
			player.getPacketSender().sendString(1118, startingPrayerLevel, "" + memberPlayer.getStats().get(5).fixedLevel);

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
		player.getPacketSender().sendString(1118, 27, "Members (" + TombsOfAmascutManager.getRaidParty(player).getMembers().size() + ")");
	}

	public static void register() {
		InterfaceHandler.register(1118, h -> {
			h.actions[22] = (SimpleAction) p -> {
				if (TombsOfAmascutManager.getRaidParty(p) == null) {
					ToAParty party = new ToAParty(p.getName());
					party.createParty(p);
					p.getMembersInterface().refresh(p);
				} else if (TombsOfAmascutManager.getRaidParty(p).getLeader().equals(p.getName())) {
					TombsOfAmascutManager.getRaidParty(p).disbandParty(p);
					p.getMembersInterface().refresh(p);
				} else {
					TombsOfAmascutManager.getRaidParty(p).getMembers().remove(p.getName());
					p.getMembersInterface().refresh(p);
					TombsOfAmascutManager.getActiveRaidParties().forEach(party -> {
						party.getMembers().forEach(m -> {
							if (p.getName().equalsIgnoreCase(m)) {
								party.getMembers().remove(m);
							}
						});
					});
				}
			};
			h.actions[21] = (SimpleAction) p -> {
				if (TombsOfAmascutManager.getRaidParty(p) == null) {
					ToAParty party = new ToAParty(p.getName());
					party.createParty(p);
					p.getMembersInterface().refresh(p);
				} else if (TombsOfAmascutManager.getRaidParty(p).getLeader().equals(p.getName())) {
					TombsOfAmascutManager.getRaidParty(p).disbandParty(p);
					p.getMembersInterface().refresh(p);
				} else {
					TombsOfAmascutManager.getRaidParty(p).getMembers().remove(p.getName());
					p.getMembersInterface().refresh(p);
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
				p.getMembersInterface().refresh(p);
			};
			h.actions[24] = (SimpleAction) p -> {
				p.getToaApplicantsInterface().open(p);
			};
			h.actions[25] = (SimpleAction) p -> {
				p.getToaInvocationsInterface().open(p);
			};
		});
	}
}
