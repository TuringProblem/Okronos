package io.ruin.model.item.actions.impl;

import io.ruin.cache.ItemID;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.dialogue.skill.SkillDialogue;
import io.ruin.model.inter.dialogue.skill.SkillItem;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.model.item.containers.Inventory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Amrod {

	@RequiredArgsConstructor
	enum Recipes {

		CRYSTAL_WEAPON_SEED(ItemID.CRYSTAL_WEAPON_SEED, 10),
		CRYSTAL_TOOL_SEED(ItemID.CRYSTAL_TOOL_SEED, 100),
		ENHANCED_CRYSTAL_TELEPORT_SEED(ItemID.ENHANCED_CRYSTAL_TELEPORT_SEED, 150),
		CRYSTAL_ARMOUR_SEED(ItemID.CRYSTAL_ARMOUR_SEED, 250),
		ENHANCED_CRYSTAL_WEAPON_SEED(25859, 1500),
		BOFA_REG(25862, 1250),
		BOS_REG(23997, 1250),
		BOFA_CORRUPT(25867, 1500),
		BOS_CORRUPT(24551, 1500),

		;
		public static final Recipes[] VALUES = values();

		public final int itemToTrade;
		public final int shards;
	}

	private static void open(Player player) {
		List<SkillItem> items = getMakeableItems(player);

		if (items.size() == 0) {
			player.dialogue(new NPCDialogue(9053, "You do not have anything to exchange. I take stronger Crystal weapons, weapon/tool/armour seeds, Enhanced crystal weapon seeds, and Enhanced crystal teleport seeds."));
			return;
		}
		SkillDialogue.make(player, Amrod::exchangeitem, items.toArray(new SkillItem[0]));
	}

	private static List<SkillItem> getMakeableItems(Player player) {
		List<SkillItem> items = new ArrayList<>();
		for (Recipes value : Recipes.VALUES) {
			if (player.getInventory().contains(value.itemToTrade)) {
				items.add(new SkillItem(value.itemToTrade));
			}
		}
		return items;
	}

	private static void exchangeitem(Player player, Item item) {
		player.dialogue(new YesNoDialogue("Crystal Item Breakdown",
			"This will consume the weapon seed and return shards.<br>Are you sure you want to proceed?",
			new Item(item.getId(), 1),
			() -> {
				int shards = Arrays.stream(Recipes.VALUES)
					.filter(r -> r.itemToTrade == item.getId())
					.findFirst()
					.get().shards;

				if (shards > 0) {
					Inventory inventory = player.getInventory();
					int amount = inventory.getAmount(item.getId());
					inventory.remove(item.getId(), amount);
					inventory.add(ItemID.CRYSTAL_SHARD, shards * amount);
					player.sendMessageFormat("You exchange your %s for %s crystal shards.", item.getDef().name, shards * amount);
					player.closeInterfaces();
				}
			})
		);
	}

	public static void register() {

		NPCAction.register(9053, "talk-to", (player, npc) -> {
			player.dialogue(
				new NPCDialogue(npc, "Greeting, adventurer! I'm a trader of sorts, but only to those who don't ask too many questions..."),
				new PlayerDialogue("Interesting, what do you offer?"),
				new NPCDialogue(npc, "I can give you crystal shards in return for crystal seeds."),
				new OptionsDialogue(
					new Option("Yes, that sounds fair.", () -> open(player)),
					new Option("I'll pass, maybe another time.", () -> player.closeDialogue())
				));

		});
		ItemNPCAction.register(9053, (player, item, npc) -> {
			Amrod.open(player);
		});


	}
}
