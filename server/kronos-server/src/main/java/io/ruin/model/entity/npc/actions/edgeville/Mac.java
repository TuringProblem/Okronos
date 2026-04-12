package io.ruin.model.entity.npc.actions.edgeville;

import io.ruin.model.activities.newshop.NewShopHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.MaxCape;
import io.ruin.model.shop.*;
import io.ruin.model.stat.StatRequirement;
import io.ruin.model.stat.StatType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.ruin.cache.ItemID.COINS_995;

public class Mac {

	public static final String MAC_SHOP_UUID = "9cdf0778-af8d-49ca-8dad-dff2e4862583";
	public static final String MAC_SHOP2_UUID = "9cdf0778-af8d-49ca-8dad-dff2e4862584";
	private static final int ECO_PRICE = 99000;

	public static void register() {
		StatType[] types = StatType.VALUES;
		List<ShopItem> untrimmedCapes = Stream.of(types)
			.map(statType -> {
				ShopItem untrimmedItem = ShopItem.builder()
					.id(statType.regularCapeId)
					.amount(100)
					.price(ECO_PRICE)
					.requirementCheckType(RequirementCheckType.REQUIRED_TO_BUY)
					.requiredLevels(Arrays.asList(new StatRequirement(statType, 99)))
					.additionalItems(Arrays.asList(new Item(statType.hoodId, 1)))
					.build();
				return new ShopItem[]{untrimmedItem};
			}).flatMap(Stream::of).collect(Collectors.toList());

		List<ShopItem> trimmedCapes = Stream.of(types)
			.map(statType -> {
				ShopItem trimmedItem = ShopItem.builder()
					.id(statType.trimmedCapeId)
					.amount(100)
					.price(ECO_PRICE)
					.requirementCheckType(RequirementCheckType.REQUIRED_TO_BUY)
					.additionalItems(Arrays.asList(new Item(statType.hoodId, 1)))
					.requiredLevels(Arrays.asList(new StatRequirement(statType, 99)))
					.build();

				return new ShopItem[]{trimmedItem};
			})
			.flatMap(Stream::of)
			.collect(Collectors.toList());

		ShopItem maxCape = ShopItem.builder()
			.id(13280)
			.amount(100)
			.price(ECO_PRICE * 23)
			.additionalItems(Arrays.asList(new Item(13281, 1)))
			.requirementCheckType(RequirementCheckType.REQUIRED_TO_BUY)
			.additionalRequirements(player -> MaxCape.unlocked(player) ? "" : "You need to be maxed in all trainable stats to purchase this cape.")
			.build();
		trimmedCapes.add(maxCape);


		Shop trimmedCapeShop = Shop.builder()
			.identifier(MAC_SHOP_UUID)
			.title("Mac's Trimmed Cape Emporium")
			.currency(Currency.COINS)
			.canSellToStore(false)
			.defaultStock(trimmedCapes)
			.generatedByBuilder(true)
			.accessibleByIronMan(true)
			.accesibleByRegular(true)
			.build();

		ShopManager.registerShop(trimmedCapeShop);

		Shop untrimmedCapeShop = Shop.builder()
			.identifier(MAC_SHOP2_UUID)
			.title("Mac's Untrimmed Cape Emporium")
			.currency(Currency.COINS)
			.canSellToStore(false)
			.defaultStock(untrimmedCapes)
			.generatedByBuilder(true)
			.accessibleByIronMan(true)
			.accesibleByRegular(true)
			.build();

		ShopManager.registerShop(untrimmedCapeShop);

		NPCAction.register(6481, 1, (p, n) -> NewShopHandler.openShop(p, NewShopHandler.skillcapeShop));
		NPCAction.register(6481, "buy-capes 2", (p, n) -> ShopManager.openIfExists(p, MAC_SHOP2_UUID));
		NPCAction.register(6481, "reset-levels", Mac::resetLevels);

	}

	/**
	 * Resetting levels
	 */

	private static final int RESET_COST = 500000;

	private static void resetLevels(Player player, NPC npc) {
		player.dialogue(
			new OptionsDialogue(
				new Option("Reset Attack", () -> resetLevel(player, StatType.Attack)),
				new Option("Reset Strength", () -> resetLevel(player, StatType.Strength)),
				new Option("Reset Defence", () -> resetLevel(player, StatType.Defence)),
				new Option("Reset Ranged", () -> resetLevel(player, StatType.Ranged)),
				new Option("More...", () -> {
					player.dialogue(new OptionsDialogue(
						new Option("Reset Prayer", () -> resetLevel(player, StatType.Prayer)),
						new Option("Reset Magic", () -> resetLevel(player, StatType.Magic)),
						new Option("Reset Hitpoints", () -> resetLevel(player, StatType.Hitpoints)),
						new Option("Previous...", () -> resetLevels(player, npc))
					));
				})
			)
		);
	}

	private static void resetLevel(Player player, StatType stat) {
		player.dialogue(
			new YesNoDialogue("Reset your " + stat.name() + " level to 1?", "This action cannot be undone and will cost you 500,000 coins.", COINS_995, RESET_COST, () -> {
				Item coins = player.getInventory().findItem(COINS_995);
				if (coins == null || coins.getAmount() < RESET_COST) {
					player.sendMessage("You don't have enough coins to reset your " + stat.name() + " level.");
					return;
				}
				if (!player.getEquipment().isEmpty()) {
					player.sendMessage("You must remove all equipment before resetting levels.");
					return;
				}
				coins.remove(RESET_COST);
				player.getPrayer().deactivateAll();
				if (stat.equals(StatType.Hitpoints)) {
					player.getStats().set(stat, 10);
				} else {
					player.getStats().get(stat).resetTo1();
				}
				player.getCombat().updateLevel();
				player.sendMessage("Your " + stat.name() + " level has been reset.");
			})
		);
	}
}
