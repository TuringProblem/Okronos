package io.ruin.model.shop;

import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum Currency {
	COINS(new ItemCurrencyHandler(ItemID.COINS_995)),
	DTICKETS(new ItemCurrencyHandler(30035)),
	GAUNTLET_SHARDS(new ItemCurrencyHandler(10506)),
	STARDUST(new ItemCurrencyHandler(25527)),
	ADMIN_TOKEN(new ItemCurrencyHandler(ItemID.SURVIVAL_TOKEN)),
	BLOOD_MONEY(new ItemCurrencyHandler(ItemID.BLOOD_MONEY)),
	TOKKUL(new ItemCurrencyHandler(ItemID.TOKKUL)),
	MARKS_OF_GRACE(new ItemCurrencyHandler(ItemID.MARK_OF_GRACE)),
	EASTER_EGGS(new ItemCurrencyHandler(ItemID.EASTER_EGG)),
	MOLCH_PEARLS(new ItemCurrencyHandler(ItemID.MOLCH_PEARL)),
	SURVIVAL_TOKENS(new ItemCurrencyHandler(ItemID.SURVIVAL_TOKEN)),
	GOLDEN_NUGGETS(new ItemCurrencyHandler(ItemID.GOLDEN_NUGGET)),
	WARRIOR_GUILD_TOKEN(new ItemCurrencyHandler(ItemID.WARRIOR_GUILD_TOKEN)),
	VOTE_TICKETS(new ItemCurrencyHandler(ItemID.VOTE_TICKETS)),
	UNIDENTIFIED_MINERALS(new ItemCurrencyHandler(ItemID.UNIDENTIFIED_MINERALS)),
	TASK_POINTS(new CurrencyHandler("daily task points") {
		@Override
		public int getCurrencyCount(Player player) {
			return player.dailyTaskPoints;
		}

		@Override
		public int removeCurrency(Player player, int amount) {
			if (amount > player.dailyTaskPoints) {
				return 0;
			}
			player.dailyTaskPoints -= amount;
			return amount;
		}

		@Override
		public int addCurrency(Player player, int amount) {
			if ((long) player.dailyTaskPoints + (long) amount > Integer.MAX_VALUE) {
				player.dailyTaskPoints = Integer.MAX_VALUE;
			} else {
				player.dailyTaskPoints += amount;
			}
			return amount;
		}
	}),
	WILDERNESS_SLAYER_POINTS(new CurrencyHandler("wilderness slayer points") {
		@Override
		public int getCurrencyCount(Player player) {
			return player.wildernessSlayerPoints;
		}

		@Override
		public int removeCurrency(Player player, int amount) {
			if (amount > player.wildernessSlayerPoints) {
				return 0;
			}
			player.wildernessSlayerPoints -= amount;
			return amount;
		}

		@Override
		public int addCurrency(Player player, int amount) {
			if ((long) player.wildernessSlayerPoints + (long) amount > Integer.MAX_VALUE) {
				player.wildernessSlayerPoints = Integer.MAX_VALUE;
			} else {
				player.wildernessSlayerPoints += amount;
			}
			return amount;
		}
	}),
	MAGE_ARENA_POINTS(new CurrencyHandler("mage arena points") {
		@Override
		public int getCurrencyCount(Player player) {
			return player.mageArenaPoints;
		}

		@Override
		public int removeCurrency(Player player, int amount) {
			if (amount > player.mageArenaPoints) {
				return 0;
			}
			player.mageArenaPoints -= amount;
			return amount;
		}

		@Override
		public int addCurrency(Player player, int amount) {
			if ((long) player.mageArenaPoints + (long) amount > Integer.MAX_VALUE) {
				player.mageArenaPoints = Integer.MAX_VALUE;
			} else {
				player.mageArenaPoints += amount;
			}
			return amount;
		}
	}),
	APPRECIATION_POINTS(new CurrencyHandler("appreciation points") {
		@Override
		public int getCurrencyCount(Player player) {
			return player.afkPoints;
		}

		@Override
		public int removeCurrency(Player player, int amount) {
			if (amount > player.afkPoints) {
				return 0;
			}
			player.afkPoints -= amount;
			return amount;
		}

		@Override
		public int addCurrency(Player player, int amount) {
			if ((long) player.afkPoints + (long) amount > Integer.MAX_VALUE) {
				player.afkPoints = Integer.MAX_VALUE;
			} else {
				player.afkPoints += amount;
			}
			return amount;
		}
	}),
	TOURNAMENT_POINTS(new CurrencyHandler("Tournament points") {
		@Override
		public int getCurrencyCount(Player player) {
			return player.tournamentPoints;
		}

		@Override
		public int removeCurrency(Player player, int amount) {
			if (amount > player.tournamentPoints) {
				return 0;
			}
			player.tournamentPoints -= amount;
			return amount;
		}

		@Override
		public int addCurrency(Player player, int amount) {
			if ((long) player.tournamentPoints + (long) amount > Integer.MAX_VALUE) {
				player.tournamentPoints = Integer.MAX_VALUE;
			} else {
				player.tournamentPoints += amount;
			}
			return amount;
		}
	}),
	BOSS_POINTS(new CurrencyHandler("boss points") {
		@Override
		public int getCurrencyCount(Player player) {
			return player.BossPoints;
		}


		@Override
		public int removeCurrency(Player player, int amount) {
			if (amount > player.BossPoints) {
				return 0;
			}
			player.BossPoints -= amount;
			return amount;
		}

		@Override
		public int addCurrency(Player player, int amount) {
			if ((long) player.BossPoints + (long) amount > Integer.MAX_VALUE) {
				player.BossPoints = Integer.MAX_VALUE;
			} else {
				player.BossPoints += amount;
			}
			return amount;
		}
	}),

	PVM_POINTS(new CurrencyHandler("pvm points") {
		@Override
		public int getCurrencyCount(Player player) {
			return player.PvmPoints;
		}


		@Override
		public int removeCurrency(Player player, int amount) {
			if (amount > player.PvmPoints) {
				return 0;
			}
			player.PvmPoints -= amount;
			return amount;
		}

		@Override
		public int addCurrency(Player player, int amount) {
			if ((long) player.PvmPoints + (long) amount > Integer.MAX_VALUE) {
				player.PvmPoints = Integer.MAX_VALUE;
			} else {
				player.PvmPoints += amount;
			}
			return amount;
		}
	}),
	PEST_CONTROL_POINTS(new CurrencyHandler("pest control points") {
		@Override
		public int getCurrencyCount(Player player) {
			return player.pestPoints;
		}

		@Override
		public int removeCurrency(Player player, int amount) {
			if (amount > player.pestPoints) {
				return 0;
			}
			player.pestPoints -= amount;
			return amount;
		}

		@Override
		public int addCurrency(Player player, int amount) {
			if ((long) player.pestPoints + (long) amount > Integer.MAX_VALUE) {
				player.pestPoints = Integer.MAX_VALUE;
			} else {
				player.pestPoints += amount;
			}
			return amount;
		}
	}),
	WILDERNESS_POINTS(new CurrencyHandler("wilderness points") {

		@Override
		public int getCurrencyCount(Player player) {
			return player.wildernessPoints;
		}

		@Override
		public int removeCurrency(Player player, int amount) {
			if (amount > player.wildernessPoints) {
				return 0;
			}
			player.wildernessPoints -= amount;
			return amount;
		}

		@Override
		public int addCurrency(Player player, int amount) {
			if ((long) player.wildernessPoints + (long) amount > Integer.MAX_VALUE) {
				player.wildernessPoints = Integer.MAX_VALUE;
			} else {
				player.wildernessPoints += amount;
			}
			return amount;
		}
	}),
	WINTERTODT_POINTS(new CurrencyHandler("wintertodt points") {

		@Override
		public int getCurrencyCount(Player player) {
			return player.wintertodtstorePoints;
		}

		@Override
		public int removeCurrency(Player player, int amount) {
			if (amount > player.wintertodtstorePoints) {
				return 0;
			}
			player.wintertodtstorePoints -= amount;
			return amount;
		}

		@Override
		public int addCurrency(Player player, int amount) {
			if ((long) player.wintertodtstorePoints + (long) amount > Integer.MAX_VALUE) {
				player.wintertodtstorePoints = Integer.MAX_VALUE;
			} else {
				player.wintertodtstorePoints += amount;
			}
			return amount;
		}
	}),
	PK(new CurrencyHandler("PK points") {

		@Override
		public int getCurrencyCount(Player player) {
			return player.getPKPoints();
		}

		@Override
		public int removeCurrency(Player player, int amount) {
			if (amount > player.getPKPoints()) {
				return 0;
			}
			player.pkPoints -= amount;
			return amount;
		}

		@Override
		public int addCurrency(Player player, int amount) {
			if ((long) player.pkPoints + (long) amount > Integer.MAX_VALUE) {
				player.pkPoints = Integer.MAX_VALUE;
			} else {
				player.pkPoints += amount;
			}
			return amount;
		}
	}),
	REASON(new CurrencyHandler("reason points") {

		@Override
		public int getCurrencyCount(Player player) {
			return player.getReasonPoints();
		}

		@Override
		public int removeCurrency(Player player, int amount) {
			if (amount > player.getReasonPoints()) {
				return 0;
			}
			player.reasonPoints -= amount;
			return amount;
		}

		@Override
		public int addCurrency(Player player, int amount) {
			if ((long) player.reasonPoints + (long) amount > Integer.MAX_VALUE) {
				player.reasonPoints = Integer.MAX_VALUE;
			} else {
				player.reasonPoints += amount;
			}
			return amount;
		}
	}),
	LOYALTY(new CurrencyHandler("loyalty points") {

		@Override
		public int getCurrencyCount(Player player) {
			return player.getLoyaltyPoints();
		}

		@Override
		public int removeCurrency(Player player, int amount) {
			if (amount > player.getLoyaltyPoints()) {
				return 0;
			}
			player.loyaltyPoints -= amount;
			return amount;
		}

		@Override
		public int addCurrency(Player player, int amount) {
			if ((long) player.loyaltyPoints + (long) amount > Integer.MAX_VALUE) {
				player.loyaltyPoints = Integer.MAX_VALUE;
			} else {
				player.loyaltyPoints += amount;
			}
			return amount;
		}
	}),

	ACHIEVEMENT(new CurrencyHandler("achievement points") {

		@Override
		public int getCurrencyCount(Player player) {
			return player.getAchievementPoints();
		}

		@Override
		public int removeCurrency(Player player, int amount) {
			if (amount > player.getAchievementPoints()) {
				return 0;
			}
			player.achievementPoints -= amount;
			return amount;
		}

		@Override
		public int addCurrency(Player player, int amount) {
			if ((long) player.achievementPoints + (long) amount > Integer.MAX_VALUE) {
				player.achievementPoints = Integer.MAX_VALUE;
			} else {
				player.achievementPoints += amount;
			}
			return amount;
		}
	}),
	DONATOR(new CurrencyHandler("donator points") {

		@Override
		public int getCurrencyCount(Player player) {
			return player.getDonatorPoints();
		}

		@Override
		public int removeCurrency(Player player, int amount) {
			if (amount > player.getDonatorPoints()) {
				return 0;
			}
			player.donatorPoints -= amount;
			return amount;
		}

		@Override
		public int addCurrency(Player player, int amount) {
			if ((long) player.donatorPoints + (long) amount > Integer.MAX_VALUE) {
				player.donatorPoints = Integer.MAX_VALUE;
			} else {
				player.donatorPoints += amount;
			}
			return amount;
		}
	}),
	VOTE(new CurrencyHandler("vote points") {

		@Override
		public int getCurrencyCount(Player player) {
			return player.getVotePoints();
		}

		@Override
		public int removeCurrency(Player player, int amount) {
			if (amount > player.getVotePoints()) {
				return 0;
			}
			player.votePoints -= amount;
			return amount;
		}

		@Override
		public int addCurrency(Player player, int amount) {
			if ((long) player.votePoints + (long) amount > Integer.MAX_VALUE) {
				player.votePoints = Integer.MAX_VALUE;
			} else {
				player.votePoints += amount;
			}
			return amount;
		}
	}),
	SNOWBALL_POINTS(new CurrencyHandler("snowball", "snowballs") {
		@Override
		public int getCurrencyCount(Player player) {
			return player.snowballPoints;
		}

		@Override
		public int removeCurrency(Player player, int amount) {
			if (amount > player.snowballPoints) {
				return 0;
			}
			player.snowballPoints -= amount;
			return amount;
		}

		@Override
		public int addCurrency(Player player, int amount) {
			if ((long) player.snowballPoints + (long) amount > Integer.MAX_VALUE) {
				player.snowballPoints = Integer.MAX_VALUE;
			} else {
				player.snowballPoints += amount;
			}
			return amount;
		}
	});


	Currency(CurrencyHandler currencyHandler) {
		this.currencyHandler = currencyHandler;
	}

	public final CurrencyHandler currencyHandler;


	public static Stream<ItemCurrencyHandler> itemCurrencyStream() {
		return Stream.of(values()).filter(currency -> currency.currencyHandler instanceof ItemCurrencyHandler).map(currency -> (ItemCurrencyHandler) currency.currencyHandler);
	}
}
