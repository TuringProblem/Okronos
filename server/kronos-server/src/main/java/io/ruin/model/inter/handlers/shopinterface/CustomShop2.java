package io.ruin.model.inter.handlers.shopinterface;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.item.Item;
import io.ruin.model.shop.Currency;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum CustomShop2 {
	MELEE_STORE(
		1,
		Currency.COINS,
		new ShopItem[]{
			new ShopItem(1323, 44),
			new ShopItem(1363, 72),
			new ShopItem(1153, 61),
			new ShopItem(1115, 224),
			new ShopItem(1067, 112),
			new ShopItem(4121, 33),
			new ShopItem(1191, 95),
			new ShopItem(1175, 67),
			new ShopItem(1101, 84),
			new ShopItem(1081, 112),
			new ShopItem(1325, 160),
			new ShopItem(1365, 260),
			new ShopItem(1157, 220),
			new ShopItem(1119, 800),
			new ShopItem(1069, 400),
			new ShopItem(4123, 120),
			new ShopItem(1193, 340),
			new ShopItem(1177, 240),
			new ShopItem(1105, 300),
			new ShopItem(1083, 400),
			new ShopItem(1329, 416),
			new ShopItem(1369, 676),
			new ShopItem(1159, 572),
			new ShopItem(1121, 2080),
			new ShopItem(1071, 1040),
			new ShopItem(4127, 312),
			new ShopItem(1197, 884),
			new ShopItem(1181, 624),
			new ShopItem(1109, 780),
			new ShopItem(1085, 1040),
			new ShopItem(1331, 1024),
			new ShopItem(1371, 1664),
			new ShopItem(1161, 1408),
			new ShopItem(1123, 6656),
			new ShopItem(1073, 2560),
			new ShopItem(4129, 768),
			new ShopItem(1199, 2176),
			new ShopItem(1183, 1536),
			new ShopItem(1111, 1920),
			new ShopItem(1091, 2560),
			new ShopItem(1540, 1000)
		}
	),
	RANGED_STORE(
		2,
		Currency.COINS,
		new ShopItem[]{
			new ShopItem(884, 3),
			new ShopItem(886, 14),
			new ShopItem(888, 37),
			new ShopItem(890, 82),
			new ShopItem(841, 50),
			new ShopItem(843, 120),
			new ShopItem(849, 200),
			new ShopItem(853, 400),
			new ShopItem(9140, 50),
			new ShopItem(9141, 100),
			new ShopItem(9142, 150),
			new ShopItem(9177, 100),
			new ShopItem(9179, 300),
			new ShopItem(9181, 700),
			new ShopItem(9183, 2100),
			new ShopItem(863, 55),
			new ShopItem(865, 110),
			new ShopItem(807, 150),
			new ShopItem(808, 750),
			new ShopItem(809, 1500),
			new ShopItem(1167, 9),
			new ShopItem(1129, 8),
			new ShopItem(1095, 8),
			new ShopItem(1063, 7),
			new ShopItem(1169, 80),
			new ShopItem(1133, 340),
			new ShopItem(1097, 300),
			new ShopItem(1135, 3120),
			new ShopItem(1099, 1560),
			new ShopItem(1065, 1000)
		}
	),
	MAGIC_STORE(
		3,
		Currency.COINS,
		new ShopItem[]{
			new ShopItem(558, 85),
			new ShopItem(556, 40),
			new ShopItem(557, 40),
			new ShopItem(559, 180),
			new ShopItem(555, 40),
			new ShopItem(554, 40),
			new ShopItem(562, 350),
			new ShopItem(560, 540),
			new ShopItem(565, 850),
			new ShopItem(561, 770),
			new ShopItem(563, 500),
			new ShopItem(564, 400),
			new ShopItem(9075, 500),
			new ShopItem(566, 300),
			new ShopItem(1381, 1500),
			new ShopItem(1383, 1500),
			new ShopItem(1385, 1500),
			new ShopItem(1387, 1500),
			new ShopItem(1391, 6820),
			new ShopItem(4675, 78000),
			new ShopItem(2415, 75000),
			new ShopItem(2416, 75000),
			new ShopItem(2417, 75000),
			new ShopItem(6109, 750),
			new ShopItem(6107, 750),
			new ShopItem(6108, 750),
			new ShopItem(6110, 750),
			new ShopItem(6106, 750),
			new ShopItem(6111, 450)
		}
	),
	IRONMAN_SHOP(
		4,
		Currency.COINS,
		new ShopItem[]{
			new ShopItem(314, 1),
			new ShopItem(590, 1),
			new ShopItem(2347, 1),
			new ShopItem(946, 1),
			new ShopItem(1755, 1),
			new ShopItem(1735, 1),
			new ShopItem(1733, 1),
			new ShopItem(1734, 3),
			new ShopItem(1785, 5),
			new ShopItem(1592, 10),
			new ShopItem(1595, 10),
			new ShopItem(1597, 10),
			new ShopItem(11065, 10),
			new ShopItem(5523, 175),
			new ShopItem(1267, 245),
			new ShopItem(1269, 2275),
			new ShopItem(1271, 5050),
			new ShopItem(1275, 32000),
			new ShopItem(1349, 98),
			new ShopItem(1353, 325),
			new ShopItem(1355, 900),
			new ShopItem(1357, 2260),
			new ShopItem(1359, 21000),

			new ShopItem(303, 1),
			new ShopItem(309, 1),
			new ShopItem(307, 1),
			new ShopItem(301, 1),
			new ShopItem(311, 1),

			new ShopItem(1540, 1000),
			new ShopItem(952, 1),
			new ShopItem(5341, 3),
			new ShopItem(5343, 3),
			new ShopItem(5325, 3),
			new ShopItem(5329, 3),

			new ShopItem(10006, 1),
			new ShopItem(10008, 1),
			new ShopItem(10012, 0),
			new ShopItem(11260, 0),
			new ShopItem(10010, 12),
			new ShopItem(954, 10),
			new ShopItem(227, 1),
			new ShopItem(233, 2),
			new ShopItem(2446, 216),
			new ShopItem(9419, 1),
			new ShopItem(10499, 600),
			new ShopItem(544, 52),
			new ShopItem(542, 39),
			new ShopItem(3105, 7),
			new ShopItem(8880, 1200),
			new ShopItem(8882, 1),
			new ShopItem(9672, 4800),
			new ShopItem(9674, 7200),
			new ShopItem(9676, 6000)
		}
	),
	PVM_POINT_SHOP(
		5,
		Currency.PVM_POINTS,
		new ShopItem[]{
			new ShopItem(10548, 275),
			new ShopItem(10547, 275),
			new ShopItem(10549, 275),
			new ShopItem(10550, 275),
			new ShopItem(10551, 375),
			new ShopItem(10555, 375),
			new ShopItem(10552, 100),
			new ShopItem(10553, 150),
			new ShopItem(7453, 25),
			new ShopItem(7454, 50),
			new ShopItem(7455, 75),
			new ShopItem(7456, 100),
			new ShopItem(7457, 125),
			new ShopItem(7458, 150),
			new ShopItem(7459, 175),
			new ShopItem(7460, 200),
			new ShopItem(7461, 225),
			new ShopItem(7462, 250),
			new ShopItem(3840, 250),
			new ShopItem(3842, 250),
			new ShopItem(3844, 250),
			new ShopItem(12608, 500),
			new ShopItem(12610, 500),
			new ShopItem(12612, 500),
			new ShopItem(12863, 1250),
			new ShopItem(1409, 500),
			new ShopItem(4081, 20),
			new ShopItem(30427, 10),
			new ShopItem(30104, 25),
			new ShopItem(23071, 75),
			new ShopItem(22114, 500),
			new ShopItem(7510, 500),
			new ShopItem(6758, 750),
			new ShopItem(11738, 5)
		}
	),
	BLOOD_MONEY_STORE(
		6,
		Currency.BLOOD_MONEY,
		new ShopItem[]{
			new ShopItem(6758, 1500),
			new ShopItem(608, 2000),
			new ShopItem(607, 2000),
			new ShopItem(12791, 5000),
			new ShopItem(12783, 10000),
			new ShopItem(11738, 100),
			new ShopItem(23071, 250),
			new ShopItem(20703, 150),
			new ShopItem(21791, 3000),
			new ShopItem(21795, 3000),
			new ShopItem(21793, 3000)
		}
	),
	MISC_STORE(
		7,
		Currency.COINS,
		new ShopItem[]{
			new ShopItem(2446, 340),
			new ShopItem(2428, 100),
			new ShopItem(113, 100),
			new ShopItem(2432, 100),
			new ShopItem(4417, 2250),
			new ShopItem(333, 49),
			new ShopItem(365, 414),
			new ShopItem(379, 287),
			new ShopItem(373, 596),
			new ShopItem(227, 8),
			new ShopItem(8013, 1237)

		}
	),
	SKILLING_SUPPLIES(
		8,
		Currency.COINS,
		new ShopItem[]{
			new ShopItem(1931, 1),
			new ShopItem(1935, 1),
			new ShopItem(20742, 210),
			new ShopItem(1925, 3),
			new ShopItem(1923, 6),
			new ShopItem(1887, 15),
			new ShopItem(314, 36),
			new ShopItem(590, 213),
			new ShopItem(2347, 110),
			new ShopItem(946, 34),
			new ShopItem(1755, 86),
			new ShopItem(1735, 27),
			new ShopItem(1733, 100),
			new ShopItem(1734, 9),
			new ShopItem(1785, 42),
			new ShopItem(1592, 10),
			new ShopItem(1595, 10),
			new ShopItem(1597, 10),
			new ShopItem(11065, 511),
			new ShopItem(5523, 175),
			new ShopItem(1267, 245),
			new ShopItem(1269, 2275),
			new ShopItem(1271, 5050),
			new ShopItem(1349, 98),
			new ShopItem(1353, 325),
			new ShopItem(1355, 900),
			new ShopItem(1357, 2260),
			new ShopItem(952, 1)
		}
	),
	DONATOR_STORE(
		9,
		Currency.DTICKETS,
		new ShopItem[]{
			new ShopItem(6199, 5),
			new ShopItem(290, 25),
			new ShopItem(30448, 40),
			new ShopItem(30185, 100),
			new ShopItem(10600, 1),
			new ShopItem(12791, 15),
			new ShopItem(30389, 100),
			new ShopItem(20724, 10),
			new ShopItem(6806, 25),
			new ShopItem(6807, 25),
			new ShopItem(6808, 25),
			new ShopItem(12877, 5),
			new ShopItem(12875, 5),
			new ShopItem(12881, 5),
			new ShopItem(12883, 5),
			new ShopItem(12873, 5),
			new ShopItem(12004, 5),
			new ShopItem(21034, 65),
			new ShopItem(21079, 65),
//                    new ShopItem(25087,100),
			new ShopItem(21295, 150),
			new ShopItem(20405, 5),
			new ShopItem(10551, 10),
			new ShopItem(12954, 10),
			new ShopItem(6585, 10),
			new ShopItem(19707, 10),
			new ShopItem(20714, 25),
			new ShopItem(12849, 10),
			new ShopItem(12863, 20),
			new ShopItem(11663, 5),
			new ShopItem(11664, 5),
			new ShopItem(11665, 5),
			new ShopItem(8839, 15),
			new ShopItem(8840, 15),
			new ShopItem(8842, 5),
			new ShopItem(30016, 150)
//                    new ShopItem(24868,25),
//                    new ShopItem(24869,25),
//                    new ShopItem(24870,25),
//                    new ShopItem(24871,25)
		}
	),
	VOTE_STORE(
		10,
		Currency.VOTE_TICKETS,
		new ShopItem[]{
			new ShopItem(989, 2),
			new ShopItem(19564, 25),
			new ShopItem(13071, 100),
			new ShopItem(11738, 5),
			new ShopItem(19473, 5),
			new ShopItem(11941, 20),
			new ShopItem(12791, 50),
			new ShopItem(12846, 100),
			new ShopItem(1505, 50),
			new ShopItem(6758, 5),
			new ShopItem(608, 8),
			new ShopItem(607, 8),
			new ShopItem(1564, 50),
			new ShopItem(1561, 50),
			new ShopItem(1562, 50),
			new ShopItem(1565, 50),
			new ShopItem(1566, 50),
			new ShopItem(1563, 50),
			new ShopItem(7582, 100),
			new ShopItem(13256, 150),
			new ShopItem(12703, 250),
			new ShopItem(20220, 10),
			new ShopItem(20223, 10),
			new ShopItem(20226, 10),
			new ShopItem(20229, 10),
			new ShopItem(20232, 10),
			new ShopItem(20235, 10)
		}
	),

	IDK_SHOP(
		12,
		Currency.COINS,
		new ShopItem[]{
			new ShopItem(314, 1),
			new ShopItem(590, 1),
			new ShopItem(2347, 1),
			new ShopItem(946, 1),
			new ShopItem(1755, 1),
			new ShopItem(1735, 1),
			new ShopItem(1733, 1),
			new ShopItem(1734, 3),
			new ShopItem(1785, 5),
			new ShopItem(1592, 10),
			new ShopItem(1595, 10),
			new ShopItem(1597, 10),
			new ShopItem(11065, 10),
			new ShopItem(5523, 175),
			new ShopItem(1267, 245),
			new ShopItem(1269, 2275),
			new ShopItem(1271, 5050),
			new ShopItem(1275, 32000),
			new ShopItem(1349, 98),
			new ShopItem(1353, 325),
			new ShopItem(1355, 900),
			new ShopItem(1357, 2260),
			new ShopItem(1359, 21000),

			new ShopItem(303, 1),
			new ShopItem(309, 1),
			new ShopItem(307, 1),
			new ShopItem(301, 1),
			new ShopItem(311, 1),

			new ShopItem(1540, 1000),
			new ShopItem(952, 1),
			new ShopItem(5341, 3),
			new ShopItem(5343, 3),
			new ShopItem(5325, 3),
			new ShopItem(5329, 3),

			new ShopItem(10006, 1),
			new ShopItem(10008, 1),
			new ShopItem(10012, 0),
			new ShopItem(11260, 0),
			new ShopItem(10010, 12),
			new ShopItem(954, 10),
			new ShopItem(227, 1),
			new ShopItem(233, 2),
			new ShopItem(2446, 216),
			new ShopItem(9419, 1),
			new ShopItem(10499, 600),
			new ShopItem(544, 52),
			new ShopItem(542, 39),
			new ShopItem(3105, 7),
			new ShopItem(8880, 1200),
			new ShopItem(8882, 1),
			new ShopItem(9672, 4800),
			new ShopItem(9674, 7200),
			new ShopItem(9676, 6000)
		}
	);

	private ShopItem[] shopItems;
	private Currency currency;
	private int shopId;
	@Getter
	private List<Player> playersInShop;

	CustomShop2(final int SHOP_ID, final Currency currency, ShopItem[] shopItems) {
		this.shopId = SHOP_ID;
		this.currency = currency;
		this.shopItems = shopItems;
		playersInShop = new ArrayList<>();
	}

	public static Item[] getItemsFromShop(Player player) {
		int shopId = player.getShopIdentifier();
		if (shopId < 0) {
			player.sendMessage("Something is wrong with this shop. Please contact a staff member.");
			return new Item[0];
		}
		CustomShop2 shop = Arrays.stream(CustomShop2.values())
			.filter(s -> s.shopId == shopId)
			.findFirst()
			.orElse(null);

		if (shop != null) {
			return toItemArray(shop.shopItems);
		}
		return new Item[0];
	}

	public ShopItem[] getShopItems() {
		return shopItems;
	}

	private static Item[] toItemArray(ShopItem[] shopItems) {
		Item[] items = new Item[shopItems.length];
		for (int index = 0; index < shopItems.length; index++) {
			ShopItem shopItem = shopItems[index];
			if (shopItem != null) {
				items[index] = new Item(shopItem.getItemId(), shopItem.getQuantity());
			}
		}
		return items;
	}

	public Item[] getItems() {
		Item[] items = new Item[shopItems.length];
		for (int index = 0; index < shopItems.length; index++) {
			ShopItem shopItem = shopItems[index];
			if (shopItem != null) {
				items[index] = new Item(shopItem.getItemId(), shopItem.getQuantity());
			}
		}
		return items;
	}

	public static CustomShop2 get(int shopId) {
		return Arrays.stream(CustomShop2.values())
			.filter(s -> s.shopId == shopId)
			.findFirst()
			.orElse(null);
	}

	public void addPlayerToShop(Player player) {
		if (!playersInShop.contains(player)) {
			playersInShop.add(player);
		}
	}

	public void removePlayerFromShop(Player player) {
		playersInShop.remove(player);
	}

	public Currency getCurrency() {
		return currency;
	}

	public ShopItem getShopItem(int itemId) {
		return Arrays.stream(shopItems)
			.filter(i -> i.getItemId() == itemId)
			.findFirst()
			.orElse(null);
	}

	public void refreshShop() {
		List<Player> playersToRemove = new ArrayList<>();
		for (Player player : playersInShop) {
			if (player != null) {
				if (!player.hasInterfaceOpen(Interface.CUSTOM_SHOP2, ToplevelComponent.MAINMODAL)) {
					playersToRemove.add(player);
					continue;
				}
				player.getPacketSender().sendItems(10005, getItems());
				player.setShopIdentifier(player.getShopIdentifier());
				player.getPacketSender().sendClientScript(917, "ii", -1, -1);
				player.getPacketSender().sendClientScript(10207);
				player.getPacketSender().sendIfEvents(836, 39, 0,
					127, 1150);
			}
		}
		playersToRemove.stream().filter(Objects::nonNull).forEach(this::removePlayerFromShop);
	}

	public static void openCustomShopViaCmd(Player player) {
		player.getPacketSender().sendString(891, 14, "Melee Store");
		CustomShopInterface2.open(player, CustomShop2.get(1).getItems());
		CustomShopInterface2.handleEnteringShop(player, CustomShop2.MELEE_STORE);
		player.setShopIdentifier(1);
	}


	public static void openIronmanShop(Player player) {
		player.getPacketSender().sendString(891, 14, "Ironman Store");
		CustomShopInterface2.open(player, CustomShop2.get(12).getItems());
		CustomShopInterface2.handleEnteringShop(player, CustomShop2.IRONMAN_SHOP);
		player.setShopIdentifier(12);
	}

//    public static void register() {
//        NPCAction.register(2108, 1, (player, npc) -> {
//            player.getPacketSender().sendString(891, 14, "Vote Store");
//            CustomShopInterface2.open(player, CustomShop2.get(10).getItems());
//            CustomShopInterface2.handleEnteringShop(player, CustomShop2.VOTE_STORE);
//            player.setShopIdentifier(10);
//        });
	//}
}
