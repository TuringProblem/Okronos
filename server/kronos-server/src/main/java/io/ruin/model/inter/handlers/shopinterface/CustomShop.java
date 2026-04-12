package io.ruin.model.inter.handlers.shopinterface;

import io.ruin.model.entity.npc.NPCAction;
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

public enum CustomShop {
	MELEE_STORE(
		1,
		Currency.COINS,
		new ShopItem[]{ //not sending these to container
			new ShopItem(12960, 457),//bronze set (lg)
			new ShopItem(12972, 1601),//iron set (lg)
			new ShopItem(12984, 5720), //steel set (lg)
			new ShopItem(12988, 10981),//black set (lg)
			new ShopItem(13000, 14872),//mith set (lg)
			new ShopItem(13012, 41600),//addy set (lg)
			new ShopItem(13024, 284180),//rune set (lg)
			new ShopItem(5574, 5000),//initiate helm
			new ShopItem(9672, 6000),//proselyte helm
			new ShopItem(10828, 60000),//helm of nezzy
			new ShopItem(12962, 457),//bronze set (sk)
			new ShopItem(12974, 1601),//iron set (sk)
			new ShopItem(12986, 5720), //steel set (sk)
			new ShopItem(12990, 10981),//black set (sk)
			new ShopItem(13002, 14872),//mith set (sk)
			new ShopItem(13014, 41600),//addy set (sk)
			new ShopItem(13026, 284180),//rune set (sk)
			new ShopItem(5575, 10000),
			new ShopItem(9674, 12000),
			new ShopItem(544, 52),
			new ShopItem(4119, 25), //bronze boots
			new ShopItem(4121, 86), //iron boots
			new ShopItem(4123, 320), //steel boots
			new ShopItem(4125, 565), //black boots
			new ShopItem(4127, 790), //mithril boots
			new ShopItem(4129, 1910), //addy boots
			new ShopItem(4131, 12250), //rune boots
			new ShopItem(5576, 5000), //climbing boots
			new ShopItem(9676, 8000),
			new ShopItem(542, 39),
			//new ShopItem(1189,70),
			new ShopItem(1191, 235),
			new ShopItem(1193, 800),
			new ShopItem(1195, 1612),
			new ShopItem(1197, 2205),
			new ShopItem(1199, 5410),
			new ShopItem(1201, 52000),
			new ShopItem(10564, 65000),
			new ShopItem(6809, 50000),
			new ShopItem(3751, 45000),
//                    new ShopItem(4119,25),
//                    new ShopItem(4121,86),
//                    new ShopItem(4123,320),
//                    new ShopItem(4125,565),
//                    new ShopItem(4127,790),
//                    new ShopItem(4129,1910),
//                    new ShopItem(4131,12250),
			new ShopItem(3105, 5000),
			new ShopItem(1205, 10),
			new ShopItem(1203, 40),
			new ShopItem(1207, 125),
			new ShopItem(1217, 245),
			new ShopItem(1209, 330),
			new ShopItem(1211, 820),
			new ShopItem(1213, 8000),
			new ShopItem(1321, 33),
			new ShopItem(1323, 110),
			new ShopItem(1325, 400),
			new ShopItem(1327, 772),
			new ShopItem(1329, 1020),
			new ShopItem(1331, 2540),
			new ShopItem(1333, 25700),
			new ShopItem(1307, 80),
			new ShopItem(1309, 280),
			new ShopItem(1311, 1000),
			new ShopItem(1313, 1910),
			new ShopItem(1315, 2600),
			new ShopItem(1317, 6350),
			new ShopItem(1319, 62000),
			new ShopItem(1215, 30000),
			new ShopItem(1305, 95000),
			new ShopItem(1377, 195000),
			new ShopItem(4587, 100000),
			new ShopItem(1434, 50000),
			new ShopItem(11037, 26500)
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
			new ShopItem(892, 400),
			new ShopItem(841, 50),
			new ShopItem(843, 120),
			new ShopItem(849, 200),
			new ShopItem(853, 400),
			new ShopItem(861, 1540),
			new ShopItem(9140, 50),
			new ShopItem(9141, 100),
			new ShopItem(9142, 150),
			new ShopItem(9143, 400),
			new ShopItem(9144, 600),
			new ShopItem(9177, 100),
			new ShopItem(9179, 300),
			new ShopItem(9181, 700),
			new ShopItem(9183, 2100),
			new ShopItem(9185, 6000),
			new ShopItem(863, 55),
			new ShopItem(865, 110),
			new ShopItem(866, 165),
			new ShopItem(867, 450),
			new ShopItem(868, 1000),
			new ShopItem(807, 150),
			new ShopItem(808, 750),
			new ShopItem(809, 1500),
			new ShopItem(810, 2500),
			new ShopItem(811, 3000),//
			new ShopItem(12865, 18460),
			new ShopItem(12867, 21684),
			new ShopItem(12869, 26013),
			new ShopItem(12871, 31226),
			new ShopItem(1063, 23),
			new ShopItem(1095, 26),
			new ShopItem(1129, 27),
			new ShopItem(10499, 1300),
			new ShopItem(3749, 78000)
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
//                    new ShopItem(21880,1920),
			new ShopItem(1381, 1500),
			new ShopItem(1383, 1500),
			new ShopItem(1385, 1500),
			new ShopItem(1387, 1500),
			new ShopItem(1391, 6820),
			new ShopItem(4675, 78000),
			new ShopItem(2415, 75000),
			new ShopItem(2416, 75000),
			new ShopItem(2417, 75000),
			new ShopItem(4089, 15000),
			new ShopItem(4091, 80000),
			new ShopItem(4093, 80000),
			new ShopItem(4095, 10000),
			new ShopItem(4097, 10000),
			new ShopItem(23113, 195000),//mystic set (blue)
			new ShopItem(4099, 15000),
			new ShopItem(4101, 80000),
			new ShopItem(4103, 80000),
			new ShopItem(4105, 10000),
			new ShopItem(4107, 10000),
			new ShopItem(23116, 195000),//mystic set (dark)
			new ShopItem(4109, 15000),
			new ShopItem(4111, 80000),
			new ShopItem(4113, 80000),
			new ShopItem(4115, 10000),
			new ShopItem(4117, 10000),
			new ShopItem(23110, 195000),//mystic set (light)
			new ShopItem(23047, 15000),
			new ShopItem(23050, 80000),
			new ShopItem(23053, 80000),
			new ShopItem(23056, 10000),
			new ShopItem(23059, 10000),
			new ShopItem(23119, 195000),//mystic set (dusk)
			new ShopItem(7398, 104000),
			new ShopItem(7399, 156000),
			new ShopItem(7400, 19500),
			new ShopItem(542, 39),
			new ShopItem(544, 52),
			new ShopItem(6109, 750),
			new ShopItem(6107, 750),
			new ShopItem(6108, 750),
			new ShopItem(6110, 750),
			new ShopItem(6106, 750),
			new ShopItem(6111, 450)
		}
	),
	BOSS_STORE(
		4,
		Currency.BOSS_POINTS,
		new ShopItem[]{
			new ShopItem(6199, 50),
			new ShopItem(11942, 50),
			new ShopItem(989, 6),
			new ShopItem(24777, 250),
			new ShopItem(23808, 250),
			new ShopItem(537, 1),
			new ShopItem(11230, 1),
			new ShopItem(11889, 275),
			new ShopItem(22951, 50),
			new ShopItem(19564, 25),
			new ShopItem(21907, 200),
			new ShopItem(12791, 200),
			new ShopItem(12785, 500),
			new ShopItem(6758, 50)

		}
	),
	PVM_STORE(
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
	PK_POINT_STORE(
		6,
		Currency.BLOOD_MONEY,
		new ShopItem[]{
			new ShopItem(20595, 1500),
			new ShopItem(6918, 2000),
			new ShopItem(20128, 1500),
			new ShopItem(4708, 2500),
			new ShopItem(4745, 2500),
			new ShopItem(4716, 2500),
			new ShopItem(4753, 2500),
			new ShopItem(4724, 2500),
			new ShopItem(4732, 2500),
			new ShopItem(21298, 2000),
			new ShopItem(20517, 1500),
			new ShopItem(6916, 2000),
			new ShopItem(20131, 1500),
			new ShopItem(4712, 2500),
			new ShopItem(4749, 2500),
			new ShopItem(4720, 2500),
			new ShopItem(4757, 2500),
			new ShopItem(4728, 2500),
			new ShopItem(4736, 2500),
			new ShopItem(21301, 2000),
			new ShopItem(20520, 1500),
			new ShopItem(6924, 2000),
			new ShopItem(20137, 1500),
			new ShopItem(4714, 2500),
			new ShopItem(4751, 2500),
			new ShopItem(4722, 2500),
			new ShopItem(4759, 2500),
			new ShopItem(4730, 2500),
			new ShopItem(4738, 2500),
			new ShopItem(21304, 2000),
			new ShopItem(11791, 2500),
			new ShopItem(6914, 2500),
			new ShopItem(6889, 2500),
			new ShopItem(4710, 2500),
			new ShopItem(4747, 2500),
			new ShopItem(4718, 2500),
			new ShopItem(4755, 2500),
			new ShopItem(4726, 2500),
			new ShopItem(4734, 2500),
			new ShopItem(6568, 750),
			new ShopItem(20733, 1250),
			new ShopItem(20739, 1250),
			new ShopItem(12000, 1250),
			new ShopItem(11924, 2500),
			new ShopItem(6524, 2500),
			new ShopItem(12829, 1500),
			new ShopItem(12831, 3000),
			new ShopItem(11284, 5000),
			new ShopItem(11926, 2500),
			new ShopItem(1187, 500),//end
			new ShopItem(6563, 1250),
			new ShopItem(4170, 500),
			new ShopItem(20718, 50),//burnt page
			new ShopItem(20140, 1500),
			new ShopItem(11335, 7500),
			new ShopItem(3140, 1000),
			new ShopItem(4087, 500),
			new ShopItem(4585, 500),//wide
			new ShopItem(11840, 1500),
			new ShopItem(6528, 750),
			new ShopItem(4153, 1250),
			new ShopItem(10887, 1000),
			new ShopItem(11061, 3500),
			new ShopItem(4151, 2500),
			new ShopItem(12006, 5000),
			new ShopItem(11838, 3500),
			new ShopItem(11802, 50000),
			new ShopItem(24777, 20000),
			new ShopItem(12863, 2500),
			new ShopItem(19478, 1500),
			new ShopItem(19481, 7500),
			new ShopItem(21902, 5000),
			new ShopItem(11235, 1500),
			new ShopItem(11230, 25),
			new ShopItem(2581, 5000),
			new ShopItem(2577, 8500),
			new ShopItem(12596, 10000),
			new ShopItem(19994, 9500),
			new ShopItem(6522, 30),
			new ShopItem(21326, 10),
			new ShopItem(11212, 25),
			new ShopItem(19484, 30),
			new ShopItem(20849, 30),
			new ShopItem(22804, 30)
		}
	),
	MISC_STORE(
		7,
		Currency.COINS,
		new ShopItem[]{
			new ShopItem(2436, 2500),
			new ShopItem(2440, 2500),
			new ShopItem(2442, 2500),
			new ShopItem(3040, 2500),
			new ShopItem(2444, 2500),
			new ShopItem(2434, 5000),
			new ShopItem(3024, 9750),
			new ShopItem(10925, 12000),
			new ShopItem(6685, 14775),
			new ShopItem(2446, 1000),
			new ShopItem(2452, 7500),
			new ShopItem(12905, 1500),
			new ShopItem(4417, 2250),
			new ShopItem(333, 49),
			new ShopItem(365, 414),
			new ShopItem(379, 287),
			new ShopItem(373, 596),
			new ShopItem(385, 2105),
			new ShopItem(227, 8),
			new ShopItem(8013, 1237),
			new ShopItem(8008, 1237),
			new ShopItem(8007, 1237),
			new ShopItem(8009, 1237),
			new ShopItem(8010, 1237)

		}
	),
	SKILLING_SUPPLIES(
		8,
		Currency.COINS,
		new ShopItem[]{
			new ShopItem(5341, 600),
			new ShopItem(952, 300),
			new ShopItem(5343, 600),
			new ShopItem(5340, 100),
			new ShopItem(228, 4),
			new ShopItem(10006, 10),
			new ShopItem(10008, 64),
			new ShopItem(10012, 2),
			new ShopItem(11260, 2),
			new ShopItem(10010, 40),
			new ShopItem(1759, 4),
			new ShopItem(9434, 50),
			new ShopItem(1592, 10),
			new ShopItem(1595, 10),
			new ShopItem(1597, 10),
			new ShopItem(11065, 10),
			new ShopItem(5523, 10),
			new ShopItem(303, 10),
			new ShopItem(305, 40),
			new ShopItem(307, 10),
			new ShopItem(309, 10),
			new ShopItem(301, 40),
			new ShopItem(311, 10),
			new ShopItem(11323, 152),
			new ShopItem(313, 6),
			new ShopItem(4, 10),
			new ShopItem(2347, 2),
			new ShopItem(1267, 280),
			new ShopItem(1275, 64000),
			new ShopItem(1349, 112),
			new ShopItem(1359, 25600),
			new ShopItem(946, 12),
			new ShopItem(1733, 2),
			new ShopItem(1734, 2),
			new ShopItem(314, 4),
			new ShopItem(11881, 400),
			new ShopItem(7937, 8),
			new ShopItem(228, 3),
			new ShopItem(11880, 301),
			new ShopItem(233, 177),
			new ShopItem(12859, 450),
			new ShopItem(236, 299),
			new ShopItem(226, 70),
			new ShopItem(224, 70),
			new ShopItem(1976, 20),
			new ShopItem(240, 10),
			new ShopItem(1939, 15),
			new ShopItem(2153, 20),
			new ShopItem(9737, 50),
			new ShopItem(232, 10),
			new ShopItem(2971, 50),
			new ShopItem(242, 30),
			new ShopItem(6050, 1400),
			new ShopItem(246, 1500),
			new ShopItem(6017, 700),
			new ShopItem(3139, 200),
			new ShopItem(248, 100),
			new ShopItem(6694, 1300),
			new ShopItem(6019, 320),
			new ShopItem(6052, 450),
			new ShopItem(5935, 2000),
			new ShopItem(5291, 100),
			new ShopItem(5292, 200),
			new ShopItem(5293, 300),
			new ShopItem(5294, 400),
			new ShopItem(5295, 5000),
			new ShopItem(5296, 1000),
			new ShopItem(5297, 500),
			new ShopItem(5298, 800),
			new ShopItem(5299, 1100),
			new ShopItem(5300, 1000),
			new ShopItem(5312, 600),
			new ShopItem(5313, 1600)
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
	IRONMAN_SHOP(
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
			new ShopItem(301, 1),
			new ShopItem(311, 1),

			new ShopItem(1540, 1000),
			new ShopItem(952, 1),
			new ShopItem(5341, 3),
			new ShopItem(5343, 3),
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
			new ShopItem(2417, 48000),
			new ShopItem(2415, 48000),
			new ShopItem(2416, 48000),
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

	CustomShop(final int SHOP_ID, final Currency currency, ShopItem[] shopItems) {
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
		CustomShop shop = Arrays.stream(CustomShop.values())
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

	public static CustomShop get(int shopId) {
		return Arrays.stream(CustomShop.values())
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
				if (!player.hasInterfaceOpen(Interface.CUSTOM_SHOP, ToplevelComponent.MAINMODAL)) {
					playersToRemove.add(player);
					continue;
				}
				player.getPacketSender().sendItems(10005, getItems());
				player.setShopIdentifier(player.getShopIdentifier());
				player.getPacketSender().sendClientScript(917, "ii", -1, -1);
				player.getPacketSender().sendClientScript(10197);
				player.getPacketSender().sendIfEvents(836, 39, 0,
					127, 1150);
			}
		}
		playersToRemove.stream().filter(Objects::nonNull).forEach(this::removePlayerFromShop);
	}

	public static void openCustomShopViaCmd(Player player) {
		player.getPacketSender().sendString(836, 14, "Melee Store");
		CustomShopInterface.open(player, CustomShop.get(1).getItems());
		CustomShopInterface.handleEnteringShop(player, CustomShop.MELEE_STORE);
		player.setShopIdentifier(1);
	}

	public static void openIronmanShop(Player player) {
		player.getPacketSender().sendString(836, 14, "Ironman Store");
		CustomShopInterface.open(player, CustomShop.get(12).getItems());
		CustomShopInterface.handleEnteringShop(player, CustomShop.IRONMAN_SHOP);
		player.setShopIdentifier(12);
	}


}
