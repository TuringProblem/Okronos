package io.ruin.model.skills.magic.spells.lunar;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.item.Item;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.stat.StatType;

public class StatSpy extends Spell {

	public StatSpy() {
		Item[] runes = {
			Rune.BODY.toItem(5),
			Rune.ASTRAL.toItem(2),
			Rune.COSMIC.toItem(2)
		};
		registerEntity(75, runes, (player, entity) -> {
			if (entity.npc != null) {
				player.sendMessage("You can only use this spell on players.");
				return false;
			}
			if (entity.player == player) {
				player.sendMessage("You can't use this spell on yourself.");
				return false;
			}
			player.startEvent(event -> {
				player.animate(6293);
				player.graphics(1060, 96, 0);
				player.getStats().addXp(StatType.Magic, 76, true);
				player.examinePlayer = entity.player;
				overview(player);
				player.openInterface(ToplevelComponent.SIDEMODAL, 523);
				player.getPacketSender().sendString(523, 94, "Stats for " + entity.player.getName());
				entity.player.graphics(736, 96, 0);
				entity.player.sendMessage(player.getName() + " has just spied on your stats.");
			});


			return true;
		});
	}

	private static void overview(Player player) {
		Player examined = player.examinePlayer;
		player.getPacketSender().sendString(523, 1, "" + examined.getStats().get(StatType.Attack).currentLevel);
		player.getPacketSender().sendString(523, 2, "" + examined.getStats().get(StatType.Attack).fixedLevel);
		player.getPacketSender().sendString(523, 5, "" + examined.getStats().get(StatType.Hitpoints).currentLevel);
		player.getPacketSender().sendString(523, 6, "" + examined.getStats().get(StatType.Hitpoints).fixedLevel);
		player.getPacketSender().sendString(523, 9, "" + examined.getStats().get(StatType.Mining).currentLevel);
		player.getPacketSender().sendString(523, 10, "" + examined.getStats().get(StatType.Mining).fixedLevel);
		player.getPacketSender().sendString(523, 13, "" + examined.getStats().get(StatType.Strength).currentLevel);
		player.getPacketSender().sendString(523, 14, "" + examined.getStats().get(StatType.Strength).fixedLevel);
		player.getPacketSender().sendString(523, 17, "" + examined.getStats().get(StatType.Agility).currentLevel);
		player.getPacketSender().sendString(523, 18, "" + examined.getStats().get(StatType.Agility).fixedLevel);
		player.getPacketSender().sendString(523, 21, "" + examined.getStats().get(StatType.Smithing).currentLevel);
		player.getPacketSender().sendString(523, 22, "" + examined.getStats().get(StatType.Smithing).fixedLevel);
		player.getPacketSender().sendString(523, 25, "" + examined.getStats().get(StatType.Defence).currentLevel);
		player.getPacketSender().sendString(523, 26, "" + examined.getStats().get(StatType.Defence).fixedLevel);
		player.getPacketSender().sendString(523, 29, "" + examined.getStats().get(StatType.Herblore).currentLevel);
		player.getPacketSender().sendString(523, 30, "" + examined.getStats().get(StatType.Herblore).fixedLevel);
		player.getPacketSender().sendString(523, 33, "" + examined.getStats().get(StatType.Fishing).currentLevel);
		player.getPacketSender().sendString(523, 34, "" + examined.getStats().get(StatType.Fishing).fixedLevel);
		player.getPacketSender().sendString(523, 37, "" + examined.getStats().get(StatType.Ranged).currentLevel);
		player.getPacketSender().sendString(523, 38, "" + examined.getStats().get(StatType.Ranged).fixedLevel);
		player.getPacketSender().sendString(523, 41, "" + examined.getStats().get(StatType.Thieving).currentLevel);
		player.getPacketSender().sendString(523, 42, "" + examined.getStats().get(StatType.Thieving).fixedLevel);
		player.getPacketSender().sendString(523, 45, "" + examined.getStats().get(StatType.Cooking).currentLevel);
		player.getPacketSender().sendString(523, 46, "" + examined.getStats().get(StatType.Cooking).fixedLevel);
		player.getPacketSender().sendString(523, 49, "" + examined.getStats().get(StatType.Prayer).currentLevel);
		player.getPacketSender().sendString(523, 50, "" + examined.getStats().get(StatType.Prayer).fixedLevel);
		player.getPacketSender().sendString(523, 53, "" + examined.getStats().get(StatType.Crafting).currentLevel);
		player.getPacketSender().sendString(523, 54, "" + examined.getStats().get(StatType.Crafting).fixedLevel);
		player.getPacketSender().sendString(523, 57, "" + examined.getStats().get(StatType.Firemaking).currentLevel);
		player.getPacketSender().sendString(523, 58, "" + examined.getStats().get(StatType.Firemaking).fixedLevel);
		player.getPacketSender().sendString(523, 61, "" + examined.getStats().get(StatType.Magic).currentLevel);
		player.getPacketSender().sendString(523, 62, "" + examined.getStats().get(StatType.Magic).fixedLevel);
		player.getPacketSender().sendString(523, 65, "" + examined.getStats().get(StatType.Fletching).currentLevel);
		player.getPacketSender().sendString(523, 66, "" + examined.getStats().get(StatType.Fletching).fixedLevel);
		player.getPacketSender().sendString(523, 69, "" + examined.getStats().get(StatType.Woodcutting).currentLevel);
		player.getPacketSender().sendString(523, 70, "" + examined.getStats().get(StatType.Woodcutting).fixedLevel);
		player.getPacketSender().sendString(523, 73, "" + examined.getStats().get(StatType.Runecrafting).currentLevel);
		player.getPacketSender().sendString(523, 74, "" + examined.getStats().get(StatType.Runecrafting).fixedLevel);
		player.getPacketSender().sendString(523, 77, "" + examined.getStats().get(StatType.Slayer).currentLevel);
		player.getPacketSender().sendString(523, 78, "" + examined.getStats().get(StatType.Slayer).fixedLevel);
		player.getPacketSender().sendString(523, 81, "" + examined.getStats().get(StatType.Farming).currentLevel);
		player.getPacketSender().sendString(523, 82, "" + examined.getStats().get(StatType.Farming).fixedLevel);
		player.getPacketSender().sendString(523, 85, "" + examined.getStats().get(StatType.Construction).currentLevel);
		player.getPacketSender().sendString(523, 86, "" + examined.getStats().get(StatType.Construction).fixedLevel);
		player.getPacketSender().sendString(523, 89, "" + examined.getStats().get(StatType.Hunter).currentLevel);
		player.getPacketSender().sendString(523, 90, "" + examined.getStats().get(StatType.Hunter).fixedLevel);
	}

	;
}