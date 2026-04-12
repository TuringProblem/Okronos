package io.ruin.model.item.actions.impl.combine;


import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.stat.StatType;

public class SmoulderingStone {

	private static final int SMOULDERING_STONE = 13233;
	private static final int INFERNAL_PICKAXE = 13243;
	private static final int INFERNAL_AXE = 13241;
	private static final int INFERNAL_HARPOON = 21031;

	private static final int DRAGON_PICKAXE = 11920;
	private static final int DRAGON_AXE = 6739;
	private static final int DRAGON_HARPOON = 21028;

	private static final int DRAGON_BOOTS = 11840;
	private static final int RANGER_BOOTS = 2577;
	private static final int INFINITY_BOOTS = 6920;

	private static final int PRIMORDIAL_CRYSTAL = 13231;
	private static final int PRIMORDIAL_BOOTS = 13239;

	private static final int PEGASIAN_CRYSTAL = 13229;
	private static final int PEGASIAN_BOOTS = 13237;

	private static final int ETERNAL_CRYSTAL = 13227;
	private static final int ETERNAL_BOOTS = 13235;

	private static final int TOURMALINE_CORE = 21730;
	private static final int BANDOS_BOOTS = 11836;
	private static final int GUARDIAN_BOOTS = 21733;

	private static void makePickaxe(Player player, Item primary, Item secondary, int result) { //TODO: Item charges..
		if (player.getStats().get(StatType.Mining).currentLevel < 61 || player.getStats().get(StatType.Smithing).currentLevel < 85) {
			player.dialogue(new ItemDialogue().two(SMOULDERING_STONE, DRAGON_PICKAXE, "You need level 61 Mining and level 85 Smithing to make an infernal pickaxe."));
			return;
		}
		player.dialogue(
			new YesNoDialogue("Are you sure you want to do this?", "Convert your dragon pickaxe into an infernal pickaxe", result, 1, () -> {
				primary.remove();
				secondary.remove();
				player.getInventory().add(result, 1);
				player.animate(4513);
				player.graphics(1240);
				player.infernalAxesMade++;
				if (player.infernalAxesMade == Achievements.THERES_LAYERS_TO_IT.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.THERES_LAYERS_TO_IT.getAchievementName());
				player.dialogue(new ItemDialogue().one(INFERNAL_PICKAXE, "You infuse the smouldering stone into the pickaxe to make an infernal pickaxe."));
			})
		);
	}

	private static void makeAxe(Player player, Item primary, Item secondary, int result) { //TODO: Item charges..
		if (player.getStats().get(StatType.Woodcutting).currentLevel < 61 || player.getStats().get(StatType.Firemaking).currentLevel < 85) {
			player.dialogue(new ItemDialogue().two(SMOULDERING_STONE, DRAGON_PICKAXE, "You need level 61 Woodcutting and level 85 Firemaking to make an infernal axe."));
			return;
		}
		player.dialogue(
			new YesNoDialogue("Are you sure you want to do this?", "Convert your dragon axe into an infernal axe", result, 1, () -> {
				primary.remove();
				secondary.remove();
				player.getInventory().add(result, 1);
				player.animate(4513);
				player.graphics(1240);
				player.dialogue(new ItemDialogue().one(INFERNAL_AXE, "You infuse the smouldering stone into the pickaxe to make an infernal axe."));
			})
		);
	}

	private static void makeHarpoon(Player player, Item primary, Item secondary, int result) { //TODO: Item charges..
		if (player.getStats().get(StatType.Fishing).currentLevel < 75 || player.getStats().get(StatType.Cooking).currentLevel < 85) {
			player.dialogue(new ItemDialogue().two(SMOULDERING_STONE, DRAGON_HARPOON, "You need level 75 Fishing and level 85 Cooking to make an infernal harpoon."));
			return;
		}
		player.dialogue(
			new YesNoDialogue("Are you sure you want to do this?", "Convert your dragon harpoon into an infernal harpoon", result, 1, () -> {
				primary.remove();
				secondary.remove();
				player.getInventory().add(result, 1);
				player.animate(4513);
				player.graphics(1240);
				player.dialogue(new ItemDialogue().one(INFERNAL_HARPOON, "You infuse the smouldering stone into the harpoon to make an infernal harpoon."));
			})
		);
	}

	private static void makeBoots(Player player, Item primary, Item secondary, int result) {
		if (player.getStats().get(StatType.Runecrafting).currentLevel < 60 || player.getStats().get(StatType.Magic).currentLevel < 60) {
			player.dialogue(new ItemDialogue().two(primary.getId(), secondary.getId(), "You need level 60 Magic and level 60 Runecrafting to use the crystal."));
			return;
		}

		Item item = new Item(result, 1);
		player.dialogue(
			new YesNoDialogue("Are you sure you want to do this?", "Infuse the " + primary.getDef().name + " and " + secondary.getDef().name + " into the " + item.getDef().name + "?", item, () -> {
				if (primary.getId() == PRIMORDIAL_CRYSTAL && secondary.getId() == DRAGON_BOOTS) {
					player.animate(4513);
					player.graphics(1240);
				} else {
					player.animate(4462);
					player.graphics(759);
				}

				primary.remove();
				secondary.remove();
				player.getInventory().add(result, 1);
				player.getStats().addXp(StatType.Magic, 200.0, false);
				player.getStats().addXp(StatType.Runecrafting, 200.0, false);
				player.dialogue(new ItemDialogue().one(result, "You successfully infuse the " + primary.getDef().name + " and " + secondary.getDef().name + " to create " + item.getDef().name));
			})
		);
	}

	private static void makeGuardianBoots(Player player, Item primary, Item secondary, int result) {
		Item item = new Item(result, 1);
		player.dialogue(
			new YesNoDialogue("Are you sure you want to do this?", "Infuse the " + primary.getDef().name + " and " + secondary.getDef().name + " into the " + item.getDef().name + "?", item, () -> {
				if (primary.getId() == TOURMALINE_CORE && secondary.getId() == BANDOS_BOOTS) {
					player.animate(4462);
					player.graphics(759);
				} else {
					player.animate(4462);
					player.graphics(759);
				}
				primary.remove();
				secondary.remove();
				player.getInventory().add(result, 1);
				player.dialogue(new ItemDialogue().one(result, "The second you hold your tourmaline core by the Bandos boots, the core is absorbed rapidly. Perhaps there is some link between the metal in Bandos and tourmaline..."));
			})
		);
	}

	public static void register() {
		ItemItemAction.register(SMOULDERING_STONE, DRAGON_PICKAXE, (player, primary, secondary) -> makePickaxe(player, primary, secondary, INFERNAL_PICKAXE));
		ItemItemAction.register(SMOULDERING_STONE, DRAGON_AXE, (player, primary, secondary) -> makeAxe(player, primary, secondary, INFERNAL_AXE));
		ItemItemAction.register(SMOULDERING_STONE, DRAGON_HARPOON, ((player, primary, secondary) -> makeHarpoon(player, primary, secondary, INFERNAL_HARPOON)));
		ItemItemAction.register(PRIMORDIAL_CRYSTAL, DRAGON_BOOTS, (player, primary, secondary) -> makeBoots(player, primary, secondary, PRIMORDIAL_BOOTS));
		ItemItemAction.register(PEGASIAN_CRYSTAL, RANGER_BOOTS, (player, primary, secondary) -> makeBoots(player, primary, secondary, PEGASIAN_BOOTS));
		ItemItemAction.register(ETERNAL_CRYSTAL, INFINITY_BOOTS, (player, primary, secondary) -> makeBoots(player, primary, secondary, ETERNAL_BOOTS));
		ItemItemAction.register(TOURMALINE_CORE, BANDOS_BOOTS, (player, primary, secondary) -> makeGuardianBoots(player, primary, secondary, GUARDIAN_BOOTS));
	}

}
