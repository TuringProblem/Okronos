package io.ruin.model.skills.thieving;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.content.HomeHandler;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerCounter;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.skills.BotPrevention;
import io.ruin.model.stat.StatType;

import static io.ruin.cache.ItemID.COINS_995;

public enum Stall {

	VEGETABLE_STALL(2, 2, 10.0, 52000, "vegetable stall",
		PlayerCounter.VEGETABLE_STALL_THIEVES,
		new int[][]{
			{4706, 634},
			{4708, 634},
		},
		null,
		new LootTable().addTable(1,
			new LootItem(1957, 1, 1),                    //Onion
			new LootItem(1965, 1, 1),                    //Cabbage
			new LootItem(1942, 1, 1),                    //Potato
			new LootItem(1982, 1, 1),                    //Tomato
			new LootItem(1550, 1, 1),                    //Garlic
			new LootItem(COINS_995, 400, 500, 12) //Coins
		)),
	BAKERS_STALL(1, 2, 16.0, 49000, "baker's stall",
		PlayerCounter.BAKER_STALL_THIEVES,
		new int[][]{
			{6163, 6984},
			{11730, 634}
		},
		new Item(22521, 1),
		new LootTable().addTable(1,
			new LootItem(1891, 1, 3),                    //Cake
			new LootItem(2309, 1, 1),                    //Bread
			new LootItem(1901, 1, 1),                    //Chocolate slice
			new LootItem(COINS_995, 3000, 4000, 3) //Coins
		)),
	CRAFTING_STALL(1, 2, 16.0, 49000, "crafting stall",
		PlayerCounter.CRAFTING_STALL_THIEVES,
		new int[][]{
			{4874, 4797},
			{6166, 6984},
		},
		null,
		new LootTable().addTable(1,
			new LootItem(1755, 1, 1),                    //Chisel
			new LootItem(1592, 1, 1),                    //Ring mould
			new LootItem(1597, 1, 1),                    //Necklace mould
			new LootItem(COINS_995, 400, 800, 1) //Coins
		)),
	MONKEY_FOOD_STALL(20, 2, 50, 49000, "food stall",
		PlayerCounter.MONKEY_FOOD_STALL_THIEVES,
		new int[][]{
			{4875, 4797},
		},
		null,
		new LootTable().addTable(1,
			new LootItem(1963, 1, 1),                    //Banana
			new LootItem(COINS_995, 800, 1200, 1) //Coins
		)),
	MONKEY_GENERAL_STALL(35, 2, 100, 49000, "general stall",
		PlayerCounter.MONKEY_GENERAL_STALL_THIEVES,
		new int[][]{
			{4876, 4797},
		},
		null,
		new LootTable().addTable(1,
			new LootItem(1931, 1, 1),                    //Pot
			new LootItem(2347, 1, 1),                    //Hammer
			new LootItem(560, 1, 1),                     //Tinderbox
			new LootItem(COINS_995, 1200, 1600, 4) //Coins
		)),
	TEA_STALL(5, 2, 16.0, 42000, "tea stall",
		PlayerCounter.TEA_STALL_THIEVES,
		new int[][]{
			{635, 634},
			{6574, 6573},
			{20350, 20349}
		},
		null,
		new LootTable().addTable(1,
			new LootItem(712, 1, 2),                     //Cup of tea
			new LootItem(COINS_995, 400, 800, 3) //Coins
		)),
	SILK_STALL(20, 2, 24.0, 42000, "silk stall",
		PlayerCounter.SILK_STALL_THIEVES,
		new int[][]{
			{6165, 6984},
			{11729, 634},
		},
		new Item(22522, 1),
		new LootTable().addTable(1,
			new LootItem(950, 1, 1),                     //Silk
			new LootItem(COINS_995, 6000, 8000, 2) //Coins
		)),
	WINE_STALL(22, 2, 27.0, 41000, "wine stall",
		PlayerCounter.WINE_STALL_THIEVES,
		new int[][]{
			{14011, 634},
		},
		null,
		new LootTable().addTable(1,
			new LootItem(1937, 1, 2),                     //Jug of water
			new LootItem(1993, 1, 1),                     //Jug of wine
			new LootItem(1988, 5, 15, 4),                     //Grapes
			new LootItem(3732, 1, 1),                     //Empty jug
			new LootItem(7919, 1, 1),                     //Bottle of wine
			new LootItem(COINS_995, 600, 1200, 4) //Coins
		)),
	SEED_STALL(27, 2, 10.0, 40000, "seed stall",
		PlayerCounter.SEED_STALL_THIEVES,
		new int[][]{
			{7053, 634},
		},
		null,
		new LootTable().addTable(1,
			new LootItem(5318, 3, 9, 8),                     //Potato seed
			new LootItem(5096, 2, 8),                     //Marigold seed
			new LootItem(5319, 3, 6, 8),                     //Onion seed
			new LootItem(5324, 3, 6, 6),                     //Cabbage seed
			new LootItem(5322, 3, 6, 3),                     //Tomato seed
			new LootItem(5320, 3, 6, 3),                     //Sweetcorn seed
			new LootItem(5097, 1, 3),                     //Rosemary seed
			new LootItem(5098, 1, 3),                     //Nasturtium seed
			new LootItem(5321, 3, 6, 1),                     //Watermelon seed
			new LootItem(5323, 3, 6, 1),                     //Strawberry seed
			new LootItem(COINS_995, 2000, 3000, 60) //Coins
		)),
	FUR_STALL(35, 2, 36.0, 22000, "fur stall",
		PlayerCounter.FUR_STALL_THIEVES,
		new int[][]{
			{4278, 634},
			{11732, 634},
		},
		null,
		new LootTable().addTable(1,
			new LootItem(958, 1, 1),                       //Grey wolf fur
			new LootItem(COINS_995, 2000, 4000, 5) //Coins
		)),
	FISH_STALL(42, 2, 42.0, 21000, "fish stall",
		PlayerCounter.FISH_STALL_THIEVES,
		new int[][]{
			{4277, 634},
			{4705, 634},
			{4707, 634},
		},
		null,
		new LootTable().addTable(1,
			new LootItem(332, 1, 3, 4),                      //Raw salmon
			new LootItem(360, 1, 3, 3),                      //Raw tuna
			new LootItem(378, 1, 2, 2),                      //Raw lobster
			new LootItem(COINS_995, 2200, 4400, 12) //Coins
		)),
	CROSSBOW_STALL(49, 2, 52.0, 20000, "crossbow stall",
		PlayerCounter.CROSSBOW_STALL_THIEVES,
		new int[][]{
			{17031, 6984},
		},
		null,
		new LootTable().addTable(1,
			new LootItem(877, 3, 2),                        //Bronze bolts
			new LootItem(9420, 1, 2),                       //Bronze limbs
			new LootItem(9440, 1, 2),                       //Wooden stock
			new LootItem(9431, 1, 2),                       //Runite stock
			new LootItem(COINS_995, 2500, 5000, 14) //Coins
		)),
	SILVER_STALL(50, 2, 54.0, 19000, "silver stall",
		PlayerCounter.SILVER_STALL_THIEVES,
		new int[][]{
			{6164, 6984},
			{11734, 634},
		},
		new Item(22523, 1),
		new LootTable().addTable(1,
			new LootItem(442, 1, 1),                       //Silver ore
			new LootItem(COINS_995, 8000, 10000, 2)               //Coins
		)),
	SPICE_STALL(65, 2, 81.0, 23000, "spice stall",
		PlayerCounter.SPICE_STALL_THIEVES,
		new int[][]{
			{6572, 6573},
			{11733, 634},
			{20348, 20349},
		},
		null,
		new LootTable().addTable(1,
			new LootItem(2007, 1, 2),                      //Spice
			new LootItem(COINS_995, 2000, 7000, 4)  //Coins
		)),
	MAGIC_STALL(65, 2, 200, 22000, "magic stall",
		PlayerCounter.MAGIC_STALL_THIEVES,
		new int[][]{
			{4877, 4797},
		},
		null,
		new LootTable().addTable(1,
			new LootItem(556, 50, 5),                      //Air rune
			new LootItem(557, 50, 5),                      //Earth rune
			new LootItem(554, 50, 5),                      //Fire rune
			new LootItem(555, 50, 5),                      //Water rune
			new LootItem(563, 25, 1),                      //Law rune
			new LootItem(COINS_995, 2000, 5000, 10) //Coins
		)),
	SCIMITAR_STALL(65, 2, 400, 20000, "scimitar stall",
		PlayerCounter.SCIMITAR_STALL_THIEVES,
		new int[][]{
			{4878, 4797},
		},
		null,
		new LootTable().addTable(1,
			new LootItem(1323, 1, 30),                      //Iron scimitar
			new LootItem(1333, 1, 4),                      //Rune scimitar
			new LootItem(4587, 1, 1),                      //Dragon scimitar
			new LootItem(COINS_995, 4000, 6000, 70) //Coins
		)),
	GEM_STALL(75, 2, 160.0, 18500, "gem stall",
		PlayerCounter.GEM_STALL_THIEVES,
		new int[][]{
			{6162, 6984},
			{11731, 634},
		},
		new Item(22524, 1),
		new LootTable().addTable(1,
			new LootItem(1623, 1, 10),                      //Uncut sapphire
			new LootItem(1621, 1, 8),                      //Uncut emerald
			new LootItem(1619, 1, 6),                      //Uncut ruby
			new LootItem(1617, 1, 4),                      //Uncut diamond
			new LootItem(1631, 1, 1),                      //Uncut dragonstone
			new LootItem(COINS_995, 12000, 15000, 10) //Coins
		)),
	MOR_GEM_STALL(75, 2, 160.0, 18500, "gem stall",
		PlayerCounter.MOR_GEM_STALL_THIEVES,
		new int[][]{
			{30280, 30278}
		},
		null,
		new LootTable().addTable(1,
			new LootItem(1623, 1, 5),                      //Uncut sapphire
			new LootItem(1608, 1, 5),                      //Sapphire
			new LootItem(1621, 1, 4),                      //Uncut emerald
			new LootItem(1606, 1, 4),                      //Emerald
			new LootItem(1619, 1, 3),                      //Uncut ruby
			new LootItem(1617, 1, 2),                      //Uncut diamond
			new LootItem(1602, 1, 2),                      //Diamond
			new LootItem(1631, 1, 1),                      //Uncut dragonstone
			new LootItem(1616, 1, 1),                      //Dragonstone
			new LootItem(COINS_995, 2000, 3000, 3) //Coins
		)),
	ORE_STALL(82, 2, 210.0, 18500, "ore stall",
		PlayerCounter.ORE_STALL_THIEVES,
		new int[][]{
			{30279, 30278}
		},
		null,
		new LootTable().addTable(1,
			new LootItem(COINS_995, 3000, 5000, 3),
			new LootItem(454, 2, 8),  //Coal
			new LootItem(445, 1, 4),  //Gold ore
			new LootItem(448, 1, 3),  //Mithril ore
			new LootItem(450, 1, 2),  //Adamant ore
			new LootItem(452, 1, 1)   //Runite ore
		));

	public final int levelReq, respawnTime, petOdds;
	public final int[][] objIDs;
	public final double experience;
	public final String name;
	public final LootTable lootTable;
	public final PlayerCounter counter;
	public final Item bloodMoneyReward;

	Stall(int levelReq, int respawnTime, double experience, int petOdds, String name, PlayerCounter counter, int[][] objIDs, Item bloodMoneyReward, LootTable lootTable) {
		this.levelReq = levelReq;
		this.respawnTime = respawnTime * 1000 / 600;
		this.experience = experience;
		this.petOdds = petOdds;
		this.name = name;
		this.counter = counter;
		this.objIDs = objIDs;
		this.bloodMoneyReward = bloodMoneyReward;
		this.lootTable = lootTable;
	}

	private static void attempt(Player player, Stall stall, GameObject object, int replacementID) {
		if (player.getPosition().regionId() == 12342) {
			HomeHandler.HandleHomeStall(player, stall, object, replacementID);
			return;
		}
		if (!player.getStats().check(StatType.Thieving, stall.levelReq, "steal from the " + stall.name))
			return;
		if (player.getInventory().isFull()) {
			player.privateSound(2277);
			player.dialogue(new MessageDialogue("Your inventory is too full to hold any more."));
			return;
		}

/*        if(player.getInventory().count(stall.bloodMoneyReward.getId()) >= BloodMoneyPouch.MAX_ALLOWED) {
            player.privateSound(2277);
            player.dialogue(new MessageDialogue("You need to open the bloody pouches you have before attempting this."));
            return;
        }*/

		if (player.edgevilleStallCooldown.isDelayed())
			return;

		if (BotPrevention.isBlocked(player)) {
			player.sendMessage("You can't steal from a stall while a guard is watching you.");
			return;
		}

		player.startEvent(event -> {
			player.sendFilteredMessage("You attempt to steal from the " + stall.name + "...");
			player.lock();
			player.animate(832);
			event.delay(1);
			replaceStall(stall, object, replacementID, player);
			Item loot = stall.lootTable.rollItem();
			player.getInventory().add(loot);
			if (player.getPosition().inBounds(HOME))
				player.edgevilleStallCooldown.delay(3);
			int petOdds = stall.petOdds;
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
				ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				petOdds *= c.getPetChanceBoost();
			}
			if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
				petOdds *= 0.85F;
			if (player.petDropBonus.isDelayed())
				petOdds *= 0.8;
			petOdds *= getPetDonatorBoost(player);
			if (Random.get(petOdds) == 0)
				Pet.ROCKY.unlock(player, 0);
			stall.counter.increment(player, 1);
			player.getStats().addXp(StatType.Thieving, stall.experience, true);
			BotPrevention.attemptBlock(player);
			player.unlock();
		});
	}

	private static double getPetDonatorBoost(Player player) {
		switch (player.getSecondaryGroup()) {
			case SUPER_DONATOR -> {
				return 0.98;
			}
			case ELITE_DONATOR -> {
				return 0.96;
			}
			case NOBLE_DONATOR -> {
				return 0.94;
			}
			case GOLD_DONATOR -> {
				return 0.93;
			}
			case PLATINUM_DONATOR -> {
				return 0.92;
			}
			case LEGENDARY_DONATOR -> {
				return 0.91;
			}
			case SUPREME_DONATOR -> {
				return 0.90;
			}
		}
		return 1;
	}

	private static final Bounds HOME = new Bounds(2002, 3558, 2017, 3573, -1);

	public static void replaceStall(Stall stall, GameObject object, int replacementID, Player player) {
		World.startEvent(e -> {
			if (!player.getPosition().inBounds(HOME)) {
				object.setId(replacementID);
				e.delay(stall.respawnTime);
				object.setId(object.originalId);
			}
		});
	}

	public static void register() {
		for (Stall stall : values()) {
			for (int[] ids : stall.objIDs) {
				ObjectAction.register(ids[0], "steal-from", (player, obj) -> attempt(player, stall, obj, ids[1]));
				ObjectAction.register(ids[0], "steal from", (player, obj) -> attempt(player, stall, obj, ids[1]));
			}
		}

	}
}
