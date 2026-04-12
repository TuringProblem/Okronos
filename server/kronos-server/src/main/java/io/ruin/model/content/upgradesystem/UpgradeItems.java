package io.ruin.model.content.upgradesystem;

import io.ruin.api.utils.Random;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.model.content.itembreaking.ItemUpgradeInterface;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.utility.Broadcast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.ruin.cache.ItemID.KANDARIN_ECHO_ORB;
import static io.ruin.cache.ItemID.MORYTANIA_ECHO_ORB;

//@formatter:off
public enum UpgradeItems {
	TWISTED_ZARYTE_BOW(
			new Item(30515),
			new Ingredient[]{new Ingredient(20997), new Ingredient(26374)},
			80,
			new Item(30548),
			"This twisted bow is imbued with the power of a Zaryte crossbow allowing it to fire any arrow or bolt."
	),
	HARMONISED_SHADOW(new Item(33000),
		new Ingredient[]{new Ingredient(27275), new Ingredient(24423)},
		60, new Item(MORYTANIA_ECHO_ORB),
		"This harmonised shadow attacks faster than the tumeken's and also possesses more power."
	),

	THUNDER_KHOPESH(
		new Item(30388),
		new Ingredient[] { new Ingredient(12765), new Ingredient(6721), new Ingredient(ItemID.SCYTHE_OF_VITUR) },
		50,
		new Item(ItemID.WILDERNESS_ECHO_ORB),
		"The thunder khopesh is a one-handed slash weapon, it has a passive effect that grants standard, successful hits a 20% chance to summon a delayed lightning bolt at the target's location, damaging enemies in a 3x3 area for up to 50% of the player's max hit."
	),

	OSMUMTENS_SCYTHE(
			new Item(59527),
			new Ingredient[]{new Ingredient(27246), new Ingredient(22325)},
			80,
			new Item(28805),
			"This scythe will hit harder than the regular scythe as well as also hitting 4 times."
	),
	CORRUPTED_WARHAMMER(
			new Item(22622),
			new Ingredient[]{new Ingredient(ItemID.BANDOS_GODSWORD), new Ingredient(ItemID.DRAGON_WARHAMMER)},
			80,
			new Item(30587),
			"This warhammer is imbued with the power of Bandos godsword and the Dragon Warhammer. The special attack will reduce the opponents defence by 30% and then reduce the defence again by the damage of the hit."
	),

	CORRUPTED_SLAYER_HELM(
			new Item(25910),
			new Ingredient[]{new Ingredient(26382), new Ingredient(30521), new Ingredient(30618)},
			100,
			new Item(30625),
			"This helm is imbued with the powers of the Torva full helm, Virtus mask, Ophidian mask, and the Slayer Helm. It will provide the stats of these three helmet slots, along with the slayer helm boost for all combat styles."
	),

	MAGMA_BLOWPIPE(
			new Item(28688),
			new Ingredient[]{
				new Ingredient(ItemID.TOXIC_BLOWPIPE_EMPTY),
				// bow of faerdhinen
				new Ingredient(new int[] { 25867, 25886, 25890, 25894, 25896, 25884, 25892, 25869, 25867, 25888 })
			},
			80,
			new Item(ItemID.MAGMA_MUTAGEN),
			"This supercharged blowpipe fires three darts at once and also has a chance to set your target ablaze."
	),

	DRAGON_HUNTER_DARK_BOW(
			new Item(27853),
			new Ingredient[]{new Ingredient(ItemID.DRAGON_HUNTER_CROSSBOW), new Ingredient(ItemID.DARK_BOW)},
			80,
			new Item(28361),
			"This bow is imbued with the power of the Dragon hunter crossbow and the Dark bow."
	),
	RADIANT_OATHPLATE_HELM(
		new Item(30777),
		new Ingredient[]{new Ingredient(30510), new Ingredient(30750)},
		70,
		new Item(23508),
		"This item is imbued with the power of a Blood torva full helm and Oathplate helm, it will provide the best melee stats for this slot."
	),
	RADIANT_OATHPLATE_CHEST(
		new Item(30779),
		new Ingredient[]{new Ingredient(30511), new Ingredient(30753)},
		70,
		new Item(23508),
		"This item is imbued with the power of a Blood torva platebody and Oathplate chest, it will provide the best melee stats for this slot."
	),
	RADIANT_OATHPLATE_LEGS(
		new Item(30781),
		new Ingredient[]{new Ingredient(30512), new Ingredient(30756)},
		70,
		new Item(23508),
		"This item is imbued with the power of a Blood torva platelegs and Oathplate legs, it will provide the best melee stats for this slot."
	),

	BLOOD_TORVA_PLATEBODY(
		new Item(30511),
		new Ingredient[]{new Ingredient(26384), new Ingredient(ItemID.JUSTICIAR_CHESTGUARD)},
		80,
		new Item(30545),
		"This item is imbued with the power of Torva platebody and Justiciar chestguard, it will provide the second best melee stats for this slot."
	),

	BLOOD_TORVA_PLATELEGS(
		new Item(30512),
		new Ingredient[]{new Ingredient(26386), new Ingredient(ItemID.JUSTICIAR_LEGGUARDS)},
		80,
		new Item(30545),
		"This item is imbued with the power of Torva platelegs and Justiciar legguards, it will provide the second best melee stats for this slot."
	),
	BLOOD_TORVA_FULL_HELM(
			new Item(30510),
			new Ingredient[]{new Ingredient(26382), new Ingredient(ItemID.JUSTICIAR_FACEGUARD)},
			80,
			new Item(30545),
			"This item is imbued with the power of Torva full helm and Justiciar faceguard, it will provide the second best melee stats for this slot."
	),

	VIRELITH_HAT(new Item(33001),
		new Ingredient[]{new Ingredient(ItemID.ANCESTRAL_HAT), new Ingredient(ItemID.VIRTUS_MASK)},
		50,
		new Item(KANDARIN_ECHO_ORB),
		"This item is imbued with the power of Ancestral hat and Virtus mask, it will provide the best magic stats for this slot."
	),
	VIRELITH_ROBE_TOP(
		new Item(33002),
		new Ingredient[]{new Ingredient(ItemID.ANCESTRAL_ROBE_TOP), new Ingredient(ItemID.VIRTUS_ROBE_TOP)},
		50,
		new Item(KANDARIN_ECHO_ORB),
		"This item is imbued with the power of Ancestral robe top and Virtus robe top, it will provide the best magic stats for this slot."
	),
	VIRELITH_ROBE_BOTTOM(
		new Item(33003),
		new Ingredient[]{new Ingredient(ItemID.ANCESTRAL_ROBE_BOTTOM), new Ingredient(ItemID.VIRTUS_ROBE_BOTTOM)},
		50,
		new Item(KANDARIN_ECHO_ORB),
		"This item is imbued with the power of Ancestral robe bottom and Virtus robe bottom, it will provide the best magic stats for this slot."
	),
	AVERNIC_TREADS_MAX(
		new Item(31097),
		new Ingredient[]{new Ingredient(30520), new Ingredient(31088)},
		70,
		new Item(33032),
		"These boots are imbued with the power of Corrupted boots and Avernic treads. They will provide the best in slot stats for any style."
	),


	CORRUPTED_BOOTS(
			new Item(30520),
			new Ingredient[]{new Ingredient(ItemID.PRIMORDIAL_BOOTS), new Ingredient(ItemID.PEGASIAN_BOOTS), new Ingredient(ItemID.ETERNAL_BOOTS)},
			80,
			new Item(30550),
			"These boots are imbued with the power of Primordial boots, Pegasian boots and Eternal boots. They will provide the second best in slot stats for any style."
	),

	DEMONIC_AMULET(
			new Item(30507),
			new Ingredient[]{new Ingredient(ItemID.AMULET_OF_TORTURE), new Ingredient(ItemID.NECKLACE_OF_ANGUISH), new Ingredient(ItemID.OCCULT_NECKLACE)},
			80,
			new Item(30547),
			"This amulet is imbued with the power of Amulet of torture, Necklace of anguish and Occult necklace. It will provide the best in slot stats for any style."
	),

	DEMONIC_BRACELET(
			new Item(30506),
			new Ingredient[]{new Ingredient(ItemID.TORMENTED_BRACELET), new Ingredient(ItemID.RING_OF_SUFFERING)},
			80,
			new Item(30546),
			"This bracelet is imbued with the power of Tormented bracelet and Ring of suffering proving the stats of both items in one slot!"
	),

	DRAGON_SLAYER_BOOTS(
			new Item(30505),
			new Ingredient[]{new Ingredient(ItemID.DRAGON_BOOTS), new Ingredient(ItemID.DRACONIC_VISAGE)},
			80,
			new Item(30554),
			"These boots are imbued with the power of Dragon boots and Draconic visage. They will provide a 5% damage boost to dragons whilst providing dragonfire protection."
	),

	CORRUPTED_SPIRIT_SHIELD(
			new Item(30508),
			new Ingredient[]{new Ingredient(ItemID.ARCANE_SPIRIT_SHIELD), new Ingredient(ItemID.ELYSIAN_SPIRIT_SHIELD), new Ingredient(30191)},
			80,
			new Item(30551),
			"This spirit shield is imbued with the power of Arcane spirit shield, Elysian spirit shield and Divine spirit shield. This will provide the best defensive bonuses, reducing 35% of incoming damage."
	),

	HEROIC_GLOVES(
			new Item(30509),
			new Ingredient[]{new Ingredient(30506), new Ingredient(ItemID.FEROCIOUS_GLOVES), new Ingredient(26235)},
			80,
			new Item(30552),
			"These gloves are imbued with the power of Tormented bracelet, Ferocious gloves and Zartye vambraces. They will provide the best in slot stats for any style."
	),

	CORRUPTED_OFFHAND(
			new Item(30514),
			new Ingredient[]{new Ingredient(30516), new Ingredient(ItemID.AVERNIC_DEFENDER)},
			80,
			new Item(30553),
			"This offhand is imbued with the power of the Ultor ring morphed into a defender providing the best attack stats for melee in this slot."
	),

	OVERLOAD_IMBUED_HEART(
			new Item(30504),
			new Ingredient[]{new Ingredient(ItemID.IMBUED_HEART_MAGIC), new Ingredient(30502), new Ingredient(30503)},
			80,
			new Item(30554),
			"This item is morphs all 3 imbued heart variants together to create the ultimate overloaded heart. It will provide stat boosts to all combat styles."
	),

	SUPERIOR_OVERLOAD_HEART(
			new Item(30380),
			new Ingredient[]{new Ingredient(30504), new Ingredient(30594)},
			80,
			new Item(30622),
			"This heart will have the superior potion effect added to it."
	),

	MAGUS_RING(
			new Item(30518),
			new Ingredient[]{new Ingredient(6731), new Ingredient(30583)},
			80,
			new Item(30549),
			"This ring is imbued with the power of the Seers ring, providing it the best stats in this slot for magic."
	),

	VENATOR_RING(
			new Item(30517),
			new Ingredient[]{new Ingredient(6733), new Ingredient(30585)},
			80,
			new Item(30549),
			"This ring is imbued with the power of the Archers ring, providing it the best stats in this slot for ranged."
	),

	ULTOR_RING(
			new Item(30516),
			new Ingredient[]{new Ingredient(6737), new Ingredient(30584)},
			80,
			new Item(30549),
			"This ring is imbued with the power of the Berserker ring, providing it the best stats in this slot for melee."
	),

	BELLATOR_RING(
			new Item(30519),
			new Ingredient[]{new Ingredient(6735), new Ingredient(30586)},
			80,
			new Item(30549),
			"This ring is imbued with the power of the Warrior ring, providing it the best stats in this slot when using slash in melee."
	),


	DRYGORE_BLOWPIPE(
			new Item(30373),
			new Ingredient[] { new Ingredient(28688), new Ingredient(ItemID.TWISTED_BOW) },
			55,
			new Item(ItemID.DESERT_ECHO_ORB),
			"it has a passive effect which provides two independent accuracy rolls; if either is higher than the opponent's original defence roll, the attack is a successful hit."
	),

	EMPEROR_RING(
			new Item(30378),
			new Ingredient[] { new Ingredient(30519), new Ingredient(30517), new Ingredient(30518) },
			50,
			new Item(ItemID.FREMENNIK_ECHO_ORB),
			"A ring only worn by true kings, combining the powers of all it's predecessors."
	),

	AMULET_OF_THE_MONARCHS(
			new Item(30376),
			new Ingredient[] { new Ingredient(29801), new Ingredient(ItemID.DEMONIC_AMULET) },
			50,
			new Item(ItemID.FREMENNIK_ECHO_ORB),
			"An amulet that originated from the one true monarch of the northern province."
	),

	THE_DOGSWORD(
			new Item(30367),
			new Ingredient[] { new Ingredient(ItemID.BANDOS_GODSWORD), new Ingredient(ItemID.ANCIENT_GODSWORD), new Ingredient(ItemID.ZAMORAK_GODSWORD) },
			50,
			new Item(ItemID.ASGARNIA_ECHO_ORB),
			"The dogsword combines the effects of all 5 godswords."
	),

	SUNLIGHT_SPEAR(
			new Item(30369),
			new Ingredient[] { new Ingredient(21649), new Ingredient(28973) },
			50,
			new Item(ItemID.VARLAMORE_ECHO_ORB),
			"Every time the player attacks using the spear, they will gain a \"Sunlight Stack\", which can be stacked up to 20. Players will lose a Sunlight Stack if they have not attacked for 50 ticks (30 seconds)."
	),

	URSINE_CHAINMACE(
		new Item(30566),
		new Ingredient[]{new Ingredient(ItemID.VIGGORAS_CHAINMACE_U)},
		100,
		new Item(30563),
		"This item provides better stats than the Viggora's chainmace and has a powerful special attack"
	),

	ACCURSED_SCEPTRE(
		new Item(30565),
		new Ingredient[]{new Ingredient(ItemID.THAMMARONS_SCEPTRE_U)},
		100,
		new Item(30561),
		"This item provides better stats than the Thammaron's sceptre and has a powerful special attack"
	),

	WEBWEAVER_BOW(
		new Item(30568),
		new Ingredient[]{new Ingredient(ItemID.CRAWS_BOW_U)},
		100,
		new Item(30562),
		"This item provides better stats than the Craw's bow and has a powerful special attack"
	),

	;

	//@formatter:on

	final Item upgradeTo;
	final Ingredient[] ingredients;
	final int successRate;
	final Item bindingItem;
	final String description;

	UpgradeItems(Item upgradeTo, Ingredient[] ingredients, int successRate, Item bindingItem, String description) {
		this.upgradeTo = upgradeTo;
		this.ingredients = ingredients;
		this.successRate = successRate;
		this.bindingItem = bindingItem;
		this.description = description;
	}

	public static final UpgradeItems[] VALUES = values();

	public static boolean canUpgrade(Player player, Ingredient[] ingredients) {
		for (var ingredient : ingredients) {
			var ingridientItem = findIngredient(player, ingredient);

			if (ingridientItem == null) {
				if (ingredient.variants.length == 1) {
					var variant = ingredient.variants[0];
					var amt = variant.getAmount();
					var name = variant.getDef().name;
					player.sendMessage("You need " + amt + " " + name + " to upgrade this item.");
					return false;
				}

				player.sendMessage("You need any variant of to upgrade this item:");
				for (var variant : ingredient.variants) {
					var amt = variant.getAmount();
					var name = variant.getDef().name;
					player.sendMessage(amt + " x " + name + " (" + variant.getId() + ").");
				}

				return false;
			}
		}

		return true;
	}

	public double getSuccessRate(Player player) {
		// @formatter:off
		// START-OF Shitty-pity System...
		var shittyPityPoints = switch (this) {
			case DRYGORE_BLOWPIPE -> Math.min(20, player.desertOrbFails * 10);

			case EMPEROR_RING -> Math.min(20, player.fremenikOrbFails * 5);

			case AMULET_OF_THE_MONARCHS -> Math.min(20, player.fremenikOrbFails * 5);

			case THE_DOGSWORD -> Math.min(20, player.asgarniaOrbFails * 10);

			case THUNDER_KHOPESH -> Math.min(20, player.wildernessOrbFails * 10);

			case SUNLIGHT_SPEAR -> Math.min(20, player.varlamoreOrbFails * 5);

			case HARMONISED_SHADOW -> Math.min(20, player.shadowOrbFails * 10);

			case VIRELITH_HAT, VIRELITH_ROBE_TOP, VIRELITH_ROBE_BOTTOM -> Math.min(20, player.virelithOrbFails * 5);

			default -> 0;
		};
		var modifiedRate = successRate + shittyPityPoints;
		return modifiedRate + getOddsForPlayer(player);
		// @formatter:on
	}

	private int getOddsForPlayer(Player player) {
		if (player.isSupremeDonator())
			return 11;
		else if (player.isLegendaryDonator())
			return 9;
		else if (player.isPlatinumDonator())
			return 7;
		else if (player.isGoldDonator())
			return 5;
		else if (player.isNobleDonator())
			return 4;
		else if (player.isEliteDonator())
			return 3;
		else if (player.isSuperDonator())
			return 2;
		else if (player.isDonator())
			return 1;
		else
			return 0;
	}

	public void createItem(Player player) {
		if (canUpgrade(player, ingredients) && player.getInventory().contains(bindingItem)) {
			int random = Random.get(1, 100);
			if (random > getSuccessRate(player)) {
				player.sendMessage("You have failed to upgrade your item.");
				player.getInventory().remove(bindingItem);
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(),
						" has failed to craft a " + upgradeTo.getDef().name + " at the upgrade station!");
				hookCreateItemPittySystem(player, false);
				return;
			}
			removeItems(player, ingredients, bindingItem, upgradeTo);
			player.sendMessage("You have successfully upgraded your item.");
			Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(),
					" has successfully crafted a " + upgradeTo.getDef().name + " at the upgrade station!");
			hookCreateItemPittySystem(player, true);
		} else {
			player.sendMessage("You do not have the required ingredients to upgrade this item.");
		}
	}

	private void hookCreateItemPittySystem(Player player, boolean success) {
		if (!success) {
			// START-OF Shitty-pity System...
			switch (this) {
				case DRYGORE_BLOWPIPE -> player.desertOrbFails = (player.desertOrbFails + 1);
				case EMPEROR_RING -> player.fremenikOrbFails = (player.fremenikOrbFails + 1);
				case AMULET_OF_THE_MONARCHS -> player.fremenikOrbFails = (player.fremenikOrbFails + 1);
				case THUNDER_KHOPESH -> player.wildernessOrbFails = (player.wildernessOrbFails + 1);
				case THE_DOGSWORD -> player.asgarniaOrbFails = (player.asgarniaOrbFails + 1);
				case SUNLIGHT_SPEAR -> player.varlamoreOrbFails = (player.varlamoreOrbFails + 1);
				case HARMONISED_SHADOW -> player.shadowOrbFails = (player.shadowOrbFails + 1);
				case VIRELITH_HAT, VIRELITH_ROBE_TOP, VIRELITH_ROBE_BOTTOM -> player.virelithOrbFails = (player.virelithOrbFails + 1);
			}
			return;
		}
		// START-OF Shitty-pity System...
		switch (this) {
			case DRYGORE_BLOWPIPE -> player.desertOrbFails = (0);
			case EMPEROR_RING -> player.fremenikOrbFails = (0);
			case AMULET_OF_THE_MONARCHS -> player.fremenikOrbFails = (0);
			case THE_DOGSWORD -> player.asgarniaOrbFails = (0);
			case THUNDER_KHOPESH -> player.wildernessOrbFails = (0);
			case SUNLIGHT_SPEAR -> player.varlamoreOrbFails = (0);
			case HARMONISED_SHADOW -> player.shadowOrbFails = (0);
			case VIRELITH_HAT, VIRELITH_ROBE_TOP, VIRELITH_ROBE_BOTTOM -> player.virelithOrbFails = (0);
		}
	}

	private void removeItems(Player player, Ingredient[] ingredients, Item bindingItem, Item upgradeTo) {
		Map<String, String> attributes = new HashMap<>();
		for (var ingredient : ingredients) {
			var item = findIngredient(player, ingredient);
			assert item != null : "Item is null";
			for (var attr : item.attributes.entrySet()) {
				if (attributes.get(attr.getKey()) == null) {
					Item tempItem = new Item(item.getId());
					tempItem.attributes.putAll(attributes);
					if (ItemUpgradeInterface.getTotalAttachments(tempItem) >= ItemUpgradeInterface
							.getMaxAttachments(tempItem)) {
						continue;
					}

					if (attr.getKey().equalsIgnoreCase("Soul_Reaver")
							&& upgradeTo.getDef().name.toLowerCase().contains("scythe")) {
						continue;
					}

					attributes.put(attr.getKey(), attr.getValue());
				} else {
					int value = Integer.parseInt(attr.getValue());
					int attributesValue = Integer.parseInt(attributes.get(attr.getKey()));
					if (value > attributesValue) {
						attributes.put(attr.getKey(), attr.getValue());
					}
				}
			}
			item.remove();
		}

		player.getInventory().remove(bindingItem);
		Item item = new Item(upgradeTo.getId());
		item.attributes.putAll(attributes);
		player.getInventory().add(item);
	}

	private static Item findIngredient(Player player, Ingredient ingredient) {
		for (Item costItem : ingredient.variants) {
			for (Item item : player.getInventory().getItems()) {
				if (item == null) {
					continue;
				}

				if (item.getId() == costItem.getId() && item.getAmount() == costItem.getAmount()) {
					return item;
				}
			}
		}

		return null;
	}

	public record Ingredient(Item[] variants) {
		public Ingredient(int item) {
			this(new Item[] {new Item(item)});
		}

		public Ingredient(int[] variants) {
			this(Arrays.stream(variants)
					.mapToObj(Item::new)
					.toArray(Item[]::new));
		}
	}

}
