package io.ruin.model.content;

import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.item.Item;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Data
public class ItemExchange {

	@RequiredArgsConstructor
	@Getter
	public enum Exchange {

		// 5 Shard Value
		ANCIENT_HALO(24201, 5),
		ARMADYL_HALO(24192, 5),
		BANDOS_HALO(24195, 5),
		BRASSICA_HALO(24204, 5),
		GUTHIX_HALO(12639, 5),
		SARADOMIN_HALO(12637, 5),
		SEREN_HALO(24198, 5),
		ZAMORAK_HALO(12638, 5),
		DRAGON_SWORD(21009, 5),
		DRAGON_SCIMITAR_E(26018, 5),

		// 15 Shard Value
		INFINITY_ROBE_TOP(6916, 15),
		INFINITY_ROBE_BOTTOM(6924, 15),
		INFINITY_HAT(6918, 15),
		INFINITY_BOOTS(6920, 15),
		INFINITY_GLOVES(6922, 15),

		// 20 Shard Value
		AMULET_OF_FURY(6585, 20),
		BERSERKER_NECKLACE(11128, 20),
		REGEN_BRACELET(11133, 20),

		// 30 Shard Value
		DRAGON_AXE(6739, 30),
		MAGE_BOOK(6889, 30),
		MASTER_WAND(6914, 30),
		MASTER_WAND1(20560, 30),

		// 50 Shard Value
		ABYSSAL_WHIP(4151, 50),
		DRAGON_BOOTS(11840, 50),
		ARCHER_RING(6733, 50),
		BERSERKER_RING(6737, 50),
		AHRIM_SET(12881, 50),
		DHAROK_SET(12877, 50),
		GUTHAN_SET(12873, 50),
		KARIL_SET(12883, 50),
		TORAG_SET(12879, 50),
		VERAC_SET(12875, 50),
		ABYSSAL_TENTACLE(12006, 50),
		DRAGONFIRE_SHIELD(11284, 50),
		SARADOMIN_SWORD(11838, 50),
		DRAGON_PICKAXE(11920, 50),
		RING_OF_THE_GODS(12601, 50),
		TREASONOUS_RING(12605, 50),
		TYRANNICAL_RING(12603, 50),
		ODIUM_WARD(11926, 50),
		MALEDICTION_WARD(11924, 50),
		SPIRIT_SHIELD(12829, 50),
		DRAGON_HARPOON(21028, 50),
		LIGHT_BALLISTA(19478, 50),
		DARK_BOW(11235, 50),
		BLACK_MASK(8901, 50),
		WARRIOR_RING(6735, 50),
		SEERS_RING(6731, 50),

		// 75 Shard Value
		BLOOD_WHIP(26019, 75),

		// 100 Shard Value
		KRAKEN_TENTACALE(12004, 100),
		ZAMORAK_GODSWORD(11808, 100),
		ETERNAL_CRYSTAL(13227, 100),
		ZURIEL_HOOD(22650, 100),
		MORRIGAN_COIF(22638, 100),
		STATIUS_FULL_HELM(22625, 100),
		HEAVY_BALLISTA(19481, 100),
		SMOULDERING_STONE(13233, 100),
		UNCHARGED_TRIDENT(11908, 100),
		BLACK_MASK_I(11774, 100),

		// 125 Shard Value
		BLESSED_SPIRIT_SHIELD(12831, 125),

		// 150 Shard Value
		MAGIC_FANG(12932, 150),
		STAFF_OF_THE_DEAD(11791, 150),
		ARMADYL_HELM(11826, 150),
		BANDOS_BOOTS(11836, 150),
		RANGER_BOOTS(2577, 150),
		DRAGON_CROSSBOW(21902, 150),
		ZURIEL_ROBE_TOP(22653, 150),
		ZURIEL_ROBE_BOTTOM(22656, 150),
		MORRIGAN_LEATHER_BODY(22641, 150),
		MORRIGAN_LEATHER_CHAPS(22644, 150),
		CORRUPTED_PICKAXE_I(30101, 150),
		OCCULT_NECKLACE(12002, 150),

		// 175 Shard Value
		TWISTED_BUCKLER(21000, 175),
		DINHS_BULWARK(21015, 175),
		JUSTICIAR_FACEGUARD(22326, 175),
		SERPENTINE_HELM(12929, 175),
		ZAMORAKIAN_SPEAR(11824, 175),
		PEGASIAN_CRYSTAL(13229, 175),

		// 250 Shard Value
		STAFF_OF_LIGHT(22296, 250),
		DRAGON_PLATEBODY(21892, 250),
		DRAGON_FULL_HELM(11335, 250),
		JAR_OF_DIRT(12007, 250),
		JAR_OF_SAND(12885, 250),
		JAR_OF_SWAMP(12936, 250),
		JAR_OF_SOULS(13245, 250),
		JAR_OF_MIASMA(13277, 250),
		JAR_OF_DARKNESS(19701, 250),
		JAR_OF_STONE(21745, 250),
		JAR_OF_DECAY(22106, 250),
		JAR_OF_CHEMICALS(23064, 250),
		JAR_OF_EYES(23525, 250),
		JAR_OF_DREAMS(24495, 250),
		JAR_OF_SPIRITS(25521, 250),
		JAR_OF_SMOKE(25524, 250),
		BRIMSTONE_RING(22975, 250),

		// 300 Shard Value
		BANDOS_CHESTPLATE(11832, 300),
		SARADOMIN_GODSWORD(11806, 300),
		ZURIEL_STAFF(22647, 300),
		STATIUS_PLATEBODY(22628, 300),
		STATIUS_PLATELEGS(22631, 300),
		FEROCIOUS_GLOVES(22981, 300),
		ARCANE_PRAYER_SCROLL(21079, 300),
		ANCESTRAL_HAT(21018, 300),
		JUSTICIAR_CHESTGUARD(22327, 300),
		JUSTICIAR_PLATELEGS(22328, 300),
		THIRD_AGE_VAMBRACES(10336, 300),

		// 350 Shard Value
		THIRD_AGE_RANGE_COIF(10334, 350),
		THIRD_AGE_MAGE_HAT(10342, 350),

		// 400 Shard Value
		BANDOS_TASSETS(11834, 400),
		PRIMORDIAL_CRYSTAL(13231, 400),
		BANDOS_GODSWORD(11804, 400),
		ARMADYL_CHAINSKIRT(11830, 400),
		AVERNIC_DEFENDER(22322, 400),
		THIRD_AGE_AMULET(10344, 400),
		ANCIENT_WYVERN_SHIELD(21634, 400),
		NECKLACE_OF_ANGUISH(19547, 400),
		AMULET_OF_TORTURE(19553, 400),
		TORMENTED_BRACELET(19544, 400),
		RING_OF_SUFFERING(19550, 400),

		// 450 Shard Value
		ARMADYL_CHESTPLATE(11828, 450),
		VESTA_PLATEBODY(22616, 450),
		VESTA_PLATESKIRT(22619, 450),
		THIRD_AGE_RANGE_CHAPS(10332, 450),
		THIRD_AGE_FULL_HELMET(10350, 450),

		// 500 Shard Value
		ARMADYL_GODSWORD(11802, 500),
		VESTA_SPEAR(22610, 500),
		ELDER_MAUL(21003, 500),
		DRAGONSTONE_FULL_HELM(24034, 500),
		DRAGONSTONE_PLATEBODY(24037, 500),
		DRAGONSTONE_PLATELEGS(24040, 500),
		DRAGONSTONE_BOOTS(24043, 500),
		DRAGONSTONE_GLOVES(24046, 500),
		DRAGONFIRE_WARD(22003, 500),
		VESTA_LONGSWORD(22613, 500),

		// 550 Shard Value
		Nightmare_STAFF(24422, 550),
		INQUISITOR_FACEGUARD(24419, 550),

		// 600 Shard Value
		DEXTEROUS_PRAYER_SCROLL(21034, 600),
		KODAI_INSIGNIA(21043, 600),
		THIRD_AGE_RANGE_TOP(10330, 600),
		THIRD_AGE_ROBE(10340, 600),
		THIRD_AGE_KITESHIELD(10352, 600),

		// 750 Shard Value
		ANCESTRAL_ROBE_BOTTOM(21024, 750),
		ELDRITCH_ORB(24517, 750),
		NEITIZNOT_FACEGUARD(24271, 750),
		THIRD_AGE_ROBE_TOP(10338, 750),
		THIRD_AGE_PLATELEGS(10346, 750),
		THIRD_AGE_PLATESKIRT(23242, 750),

		// 800 Shard Value
		DRAGON_CLAWS(13652, 800),
		DRAGON_HUNTER_CROSSBOW(21012, 800),
		ANCESTRAL_ROBE_TOP(21021, 850),

		// 850 Shard Value
		INQUISITORR_PLATESKIRT(24421, 850),
		THIRD_AGE_PLATEBODY(10348, 850),

		// 900 Shard Value
		INQUISITOR_HAUBERK(24420, 900),

		// 1000 Shard Value
		BLACK_HALLOWEEN_MASK(11847, 1000),
		GREEN_HALLOWEEN_MASK(1053, 1000),
		BLUE_HALLOWEEN_MASK(1055, 1000),
		RED_HALLOWEEN_MASK(1057, 1000),

		// 1250 Shard Value
		DRAGON_HUNTER_LANCE(22978, 1250),

		// 1500 Shard Value
		SPECTRAL_SPIRIT_SHIELD(12821, 1500),
		SANTA_HAT(1050, 1500),

		// 2000 Shard Value
		THIRD_AGE_LONGSWORD(12426, 2000),
		THIRD_AGE_WAND(12422, 2000),
		THIRD_AGE_BOW(12424, 2000),
		// CORRUPTED_STAFF(30023, 2000),
		// CORRUPTED_JAVELIN(30009, 2000),
		RED_PARTYHAT(1038, 2000),
		BLUE_PARTYHAT(1042, 2000),
		GREEN_PARTYHAT(1044, 2000),
		YELLOW_PARTYHAT(1040, 2000),
		PURPLE_PARTYHAT(1046, 2000),
		WHITE_PARTYHAT(1048, 2000),
		BLACK_PARTYHAT(11862, 2000),
		RAINBOW_PARTYHAT(11863, 2000),

		// 2500 Shard Value
		THIRD_AGE_CLOAK(12437, 2500),
		ARCANE_SPIRIT_SHIELD(12825, 2500),
		NECROMANCER_ROBE_TOP(30021, 2500),
		NECROMANCER_ROBE_BOTTOMS(30022, 2500),

		// 5000 Shard Value
		THIRD_AGE_PICKAXE(20014, 5000),
		THIRD_AGE_AXE(20011, 5000),

		// 7500 Shard Value
		ELYSIAN_SPIRIT_SHIELD(12817, 7500),
		THIRD_AGE_DRUIDIC_ROBE_TOP(23336, 7500),
		THIRD_AGE_DRUIDIC_ROBE_BOTTOMS(23339, 7500),
		THIRD_AGE_DRUIDIC_STAFF(23342, 7500),
		THIRD_AGE_DRUIDIC_CLOAK(23345, 7500),

		;

		public static Exchange forItemId(int itemId) {
			return Stream.of(Exchange.values())
				.filter(e -> e.getItem() == itemId)
				.findFirst().orElse(null);
		}

		private final int item;

		private final int value;

	}

	private static final int[] ITEM_COMPONENTS = {
		17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
		28, 29, 30, 31, 32, 33, 34, 35, 36
	};
	private static final int MAX_CONTAINER_SIZE = 20;
	private static final int SHARD_ID = 10506;
	private static final int SHARD_CONTAINER = 40;

	public static void register() {
		// ObjectAction.register(50040, "Sacrifice", (player, object) ->
		// player.getItemExchange().sendInterface());
		// ObjectAction.register(50040, "Shard shop", (player, object) ->
		// ShopManager.openIfExists(player, "VuTjIYLjUXYvOyWMjMATmNHyZg"));
		// InterfaceHandler.register(Interface.ITEM_EXCHANGE, interfaceHandler -> {
		// interfaceHandler.actions[37] = new InterfaceAction() {
		// @Override
		// public void handleClick(Player player, int option, int slot, int itemId) {
		// player.getItemExchange().confirmExchange();
		// }
		// };
		// });
		// InterfaceHandler.register(Interface.UPGRADE_MACHINE_INVENTORY,
		// (interfaceHandler -> {
		// interfaceHandler.actions[3] = new InterfaceAction() {
		// @Override
		// public void handleClick(Player player, int option, int slot, int itemId) {
		// player.getItemExchange().addItemToContainer(slot);
		// }
		//
		// @Override
		// public void handleDrag(Player player, int fromSlot, int fromItemId, int
		// toInterfaceId, int toChildId, int toSlot, int toItemId) {
		// TabInventory.drag(player, fromSlot, toSlot);
		// }
		// };
		// }));

	}

	private transient Player player;
	private transient List<Item> container = new ArrayList<>();

	public void sendInterface() {
		player.sendMessage("This feature has been temporarily disabled.");
		// container.clear();
		// player.openInterface(InterfaceType.MAIN, Interface.ITEM_EXCHANGE);
		// player.openInterface(InterfaceType.INVENTORY,
		// Interface.UPGRADE_MACHINE_INVENTORY);
		// player.getPacketSender().sendAccessMask(Interface.UPGRADE_MACHINE_INVENTORY,
		// 3, 0, 27, 1181694);
		//// player.getPacketSender().sendAccessMask(Interface.UPGRADE_MACHINE_INVENTORY,
		// 10, 0, 27, 1054);
		// updateInterface();
	}

	private void addItemToContainer(int slot) {
		if (container.size() >= MAX_CONTAINER_SIZE) {
			player.sendMessage("Container is full");
			return;
		}
		Item item = player.getInventory().get(slot);
		if (item == null)
			return;
		ObjType itemDef = item.getDef();
		if (itemDef == null)
			return;
		Exchange exchange = Exchange.forItemId(item.getId());
		if (exchange == null) {
			player.sendMessage("You can't sacrifice this item.");
			return;
		}
		Item containerItem = new Item(item.getId(), item.getAmount());
		if (!canAdd(containerItem)) {
			player.sendMessage("You don't have this amount.");
			return;
		}
		container.add(containerItem);
		updateInterface();
	}

	private boolean canAdd(Item item) {
		boolean isInContainer = container.stream()
			.anyMatch(c -> c.getId() == item.getId());
		if (isInContainer) {
			int itemAmount = container.stream()
				.filter(c -> c.getId() == item.getId())
				.mapToInt(c -> c.getAmount())
				.sum();
			if (player.getInventory().contains(item.getId(), itemAmount + item.getAmount())) {
				return true;
			} else {
				return false;
			}
		}
		return true;

	}

	private void updateInterface() {
		displayItemsInContainer();
		displayTotalValue();
	}

	private void confirmExchange() {
		container.stream()
			.forEach(c -> {
				if (!player.getInventory().hasItem(c.getId(), c.getAmount()))
					return;
				player.getInventory().remove(c);
				int amount = Exchange.forItemId(c.getId()).getValue();
				player.getInventory().add(SHARD_ID, amount);
				player.sendMessage("You have been given " + amount + " x Shards in exchange for " + c.getAmount() + " x "
					+ c.getDef().name + ".");
			});
		container.clear();
		updateInterface();
	}

	private void displayItemsInContainer() {
		for (int i = 0; i < ITEM_COMPONENTS.length; i++) {
			player.getPacketSender().sendItems(Interface.ITEM_EXCHANGE, ITEM_COMPONENTS[i], 0, new Item(-1));
		}
		for (int i = 0; i < container.size(); i++) {
			player.getPacketSender().sendItems(Interface.ITEM_EXCHANGE, ITEM_COMPONENTS[i], 0, container.get(i));
		}
	}

	private void displayTotalValue() {
		int totalValue = container.stream()
			.mapToInt(i -> Exchange.forItemId(i.getId()).getValue())
			.sum();
		player.getPacketSender().sendItems(Interface.ITEM_EXCHANGE, SHARD_CONTAINER, 0, new Item(SHARD_ID, totalValue));
	}

}
