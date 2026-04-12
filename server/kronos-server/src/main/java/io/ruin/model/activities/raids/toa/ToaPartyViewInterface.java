package io.ruin.model.activities.raids.toa;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;

public class ToaPartyViewInterface {

	ToAParty party;

	public void open(Player player, ToAParty party) {
		this.party = party;
		player.openInterface(ToplevelComponent.MAINMODAL, 1121);
		refresh(player);
	}

	public void back(Player player) {
		player.getToaPartyInterface().open(player);
	}

	public void apply(Player player) {
		party.applyForParty(player.getName());
		player.sendMessage("You have applied to join the party.");
	}

	public void refresh(Player player) {
		for (int i = 39; i <= 104; i += 13) {
			player.getPacketSender().setHidden(1121, i, true);
		}

		int startingComponentId = 39;
		int startingMemberNameComponentId = 42;
		int startingCombatLevel = 43;
		int startingAttackLevel = 44;
		int startingStrengthLevel = 45;
		int startingRangeLevel = 46;
		int startingMagicLevel = 47;
		int startingDefenceLevel = 48;
		int startingHitpointsLevel = 49;
		int startingPrayerLevel = 50;
		for (String member : party.getMembers()) {
			if (startingComponentId > 104)
				break;
			Player memberPlayer = World.getPlayer(member);
			if (memberPlayer == null) {
				continue;
			}
			player.getPacketSender().setHidden(1121, startingComponentId, false);
			player.getPacketSender().sendString(1121, startingMemberNameComponentId, memberPlayer.getName());
			player.getPacketSender().sendString(1121, startingCombatLevel, "" + memberPlayer.getCombat().getLevel());
			player.getPacketSender().sendString(1121, startingAttackLevel, "" + memberPlayer.getStats().get(0).fixedLevel);
			player.getPacketSender().sendString(1121, startingStrengthLevel, "" + memberPlayer.getStats().get(2).fixedLevel);
			player.getPacketSender().sendString(1121, startingRangeLevel, "" + memberPlayer.getStats().get(4).fixedLevel);
			player.getPacketSender().sendString(1121, startingMagicLevel, "" + memberPlayer.getStats().get(6).fixedLevel);
			player.getPacketSender().sendString(1121, startingDefenceLevel, "" + memberPlayer.getStats().get(1).fixedLevel);
			player.getPacketSender().sendString(1121, startingHitpointsLevel, "" + memberPlayer.getStats().get(3).fixedLevel);
			player.getPacketSender().sendString(1121, startingPrayerLevel, "" + memberPlayer.getStats().get(5).fixedLevel);

			String raidLevelText;
			if (party.getCurrentInvocationValue() < 150)
				raidLevelText = "Entry";
			else if (party.getCurrentInvocationValue() >= 150 && party.getCurrentInvocationValue() < 300)
				raidLevelText = "Normal";
			else
				raidLevelText = "Expert";
			player.getPacketSender().sendString(1121, 23, raidLevelText);
			player.getPacketSender().sendString(1121, 25, party.getCurrentInvocationValue() + " <col=ffffff>(" + party.getActiveInvocations().size() + ")");

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
	}

	public static void register() {
		InterfaceHandler.register(1121, h -> {
			h.actions[18] = (SimpleAction) p -> {
				p.getToaPartyViewInterface().back(p);
			};
			h.actions[20] = (SimpleAction) p -> {
				p.getToaPartyViewInterface().apply(p);
			};
		});
	}
}
