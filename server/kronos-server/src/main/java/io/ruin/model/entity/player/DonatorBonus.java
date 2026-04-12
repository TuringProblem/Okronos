package io.ruin.model.entity.player;

public enum DonatorBonus implements BonusHandler {
	PRESET_SLOTS {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case SUPER_DONATOR:
					return 7;
				case ELITE_DONATOR:
					return 9;
				case NOBLE_DONATOR:
					return 11;
				case GOLD_DONATOR:
					return 13;
				case PLATINUM_DONATOR:
				case LEGENDARY_DONATOR:
				case SUPREME_DONATOR:
					return 15;
				default:
					return 5;
			}
		}
	},
	PEST_CONTROL_POINT_BONUS {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case DONATOR:
					return 1;
				case SUPER_DONATOR:
					return 2;
				case ELITE_DONATOR:
					return 3;
				case NOBLE_DONATOR:
					return 4;
				case GOLD_DONATOR:
					return 5;
				case PLATINUM_DONATOR:
					return 6;
				case LEGENDARY_DONATOR:
					return 7;
				case SUPREME_DONATOR:
					return 8;
				default:
					return 0;
			}
		}
	},
	PEST_CONTROL_COINS_BONUS {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case DONATOR:
					return 1000;
				case SUPER_DONATOR:
					return 2500;
				case ELITE_DONATOR:
					return 5000;
				case NOBLE_DONATOR:
					return 7500;
				case GOLD_DONATOR:
					return 10000;
				case PLATINUM_DONATOR:
					return 15000;
				case LEGENDARY_DONATOR:
					return 25000;
				case SUPREME_DONATOR:
					return 50000;
				default:
					return 0;
			}
		}
	},
	WARRIOR_GUILD_TOKEN_SAVE_CHANCE {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case DONATOR:
					return 2;
				case SUPER_DONATOR:
					return 5;
				case ELITE_DONATOR:
					return 8;
				case NOBLE_DONATOR:
					return 12;
				case GOLD_DONATOR:
					return 18;
				case PLATINUM_DONATOR:
					return 25;
				case LEGENDARY_DONATOR:
					return 35;
				case SUPREME_DONATOR:
					return 43;
				default:
					return 0;
			}
		}
	},
	CHANCE_ON_SAVING_KEY_FOR_CHESTS {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case DONATOR:
					return 2;
				case SUPER_DONATOR:
					return 4;
				case ELITE_DONATOR:
					return 6;
				case NOBLE_DONATOR:
					return 8;
				case GOLD_DONATOR:
					return 10;
				case PLATINUM_DONATOR:
					return 12;
				case LEGENDARY_DONATOR:
					return 15;
				case SUPREME_DONATOR:
					return 18;
				default:
					return 0;
			}
		}
	},
	VOTE_POINT_BONUS {
		@Override
		public int handleBonus(Player player) { //todo: when voting works
			switch (player.getSecondaryGroup()) {
				case DONATOR:
					return 1;
				case SUPER_DONATOR:
					return 2;
				case ELITE_DONATOR:
					return 3;
				case NOBLE_DONATOR:
					return 4;
				case GOLD_DONATOR:
					return 5;
				case PLATINUM_DONATOR:
					return 6;
				case LEGENDARY_DONATOR:
					return 7;
				case SUPREME_DONATOR:
					return 8;
				default:
					return 0;
			}
		}
	},
	FIGHT_CAVE_AND_INFERNO_START_WAVE {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case DONATOR:
					return 10;
				case SUPER_DONATOR:
					return 15;
				case ELITE_DONATOR:
					return 20;
				case NOBLE_DONATOR:
					return 25;
				case GOLD_DONATOR:
					return 30;
				case PLATINUM_DONATOR:
					return 35;
				case LEGENDARY_DONATOR:
					return 45;
				default:
					return 1;
			}
		}
	},
	YELL_TIMER {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case DONATOR:
					return 60;
				case SUPER_DONATOR:
					return 30;
				case ELITE_DONATOR:
					return 15;
				case NOBLE_DONATOR:
					return 10;
				case GOLD_DONATOR:
				case PLATINUM_DONATOR:
				case LEGENDARY_DONATOR:
					return 0;
				default:
					return -1;
			}
		}
	},
	BONUS_CHANCE_ON_SUPERIOR_SLAYER {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case DONATOR:
					return 5;
				case SUPER_DONATOR:
					return 7;
				case ELITE_DONATOR:
					return 10;
				case NOBLE_DONATOR:
					return 13;
				case GOLD_DONATOR:
					return 15;
				case PLATINUM_DONATOR:
					return 18;
				case LEGENDARY_DONATOR:
					return 21;
				case SUPREME_DONATOR:
					return 25;
				default:
					return 0;
			}
		}
	},
	POINTS_PERCENTAGE_LOST_ON_RAIDS_DEATH_REDUCTION {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case ELITE_DONATOR:
				case NOBLE_DONATOR:
					return 30;
				case GOLD_DONATOR:
					return 20;
				case PLATINUM_DONATOR:
				case LEGENDARY_DONATOR:
				case SUPREME_DONATOR:
					return 10;
				default:
					return 40;
			}
		}
	},
	REDUCTION_OF_CANCEL_SLAYER_TASK {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case SUPER_DONATOR:
					return 3;
				case ELITE_DONATOR:
					return 5;
				case NOBLE_DONATOR:
					return 7;
				case GOLD_DONATOR:
					return 10;
				case PLATINUM_DONATOR:
					return 15;
				case LEGENDARY_DONATOR:
					return 20;
				case SUPREME_DONATOR:
					return 25;
				default:
					return 0;
			}
		}
	},
	NO_FLOOR_DAMAGE_IN_KARUULM_DUNGEON {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case ELITE_DONATOR:
				case NOBLE_DONATOR:
				case GOLD_DONATOR:
				case PLATINUM_DONATOR:
				case LEGENDARY_DONATOR:
				case SUPREME_DONATOR:
					return 1;
				default:
					return 0;
			}
		}
	},
	CHANCE_ON_NOTED_SKILLING_RESOURCE_REWARDS {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case DONATOR:
					return 2;
				case SUPER_DONATOR:
					return 5;
				case ELITE_DONATOR:
					return 8;
				case NOBLE_DONATOR:
					return 12;
				case GOLD_DONATOR:
					return 18;
				case PLATINUM_DONATOR:
					return 25;
				case LEGENDARY_DONATOR:
					return 35;
				case SUPREME_DONATOR:
					return 43;
				default:
					return 0;
			}
		}
	},
	EXTRA_ROLL_AT_WINTERTODT {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case DONATOR:
				case SUPER_DONATOR:
				case ELITE_DONATOR:
					return 1;
				case NOBLE_DONATOR:
				case GOLD_DONATOR:
					return 2;
				case PLATINUM_DONATOR:
				case LEGENDARY_DONATOR:
				case SUPREME_DONATOR:
					return 3;
				default:
					return 0;
			}
		}
	},
	EXTRA_DAILY_CHALLENGES {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case SUPER_DONATOR:
					return 1;
				case ELITE_DONATOR:
					return 2;
				case NOBLE_DONATOR:
					return 2;
				case GOLD_DONATOR:
				case PLATINUM_DONATOR:
				case LEGENDARY_DONATOR:
					return 3;
				case SUPREME_DONATOR:
					return 4;
				default:
					return 0;
			}
		}
	},
	COIN_CASKET_DOUBLE_COINS_CHANCE {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case DONATOR:
					return 5;
				case SUPER_DONATOR:
					return 10;
				case ELITE_DONATOR:
					return 15;
				case NOBLE_DONATOR:
					return 20;
				case GOLD_DONATOR:
					return 25;
				case PLATINUM_DONATOR:
					return 30;
				case LEGENDARY_DONATOR:
					return 40;
				case SUPREME_DONATOR:
					return 45;
				default:
					return 0;
			}
		}
	},
	MARKS_OF_GRACE_BONUS_CHANCE {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case DONATOR:
					return 1;
				case SUPER_DONATOR:
				case ELITE_DONATOR:
					return 2;
				case NOBLE_DONATOR:
				case GOLD_DONATOR:
					return 3;
				case PLATINUM_DONATOR:
					return 4;
				case LEGENDARY_DONATOR:
				case SUPREME_DONATOR:
					return 5;
				default:
					return 0;
			}
		}
	},
	DOUBLE_BLOOD_MONEY_CHANCE {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case DONATOR:
					return 2;
				case SUPER_DONATOR:
					return 5;
				case ELITE_DONATOR:
					return 8;
				case NOBLE_DONATOR:
					return 12;
				case GOLD_DONATOR:
					return 18;
				case PLATINUM_DONATOR:
					return 25;
				case LEGENDARY_DONATOR:
					return 35;
				case SUPREME_DONATOR:
					return 40;
				default:
					return 0;
			}
		}
	},
	BONUS_SLAYER_POINTS {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case DONATOR:
					return 1;
				case SUPER_DONATOR:
				case ELITE_DONATOR:
					return 2;
				case NOBLE_DONATOR:
				case GOLD_DONATOR:
					return 3;
				case PLATINUM_DONATOR:
					return 4;
				case LEGENDARY_DONATOR:
					return 5;
				case SUPREME_DONATOR:
					return 6;
				default:
					return 0;
			}
		}
	},
	KILLS_REQUIRED_GODWARS {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case DONATOR:
					return 35;
				case SUPER_DONATOR:
					return 25;
				case ELITE_DONATOR:
					return 20;
				case NOBLE_DONATOR:
					return 15;
				case GOLD_DONATOR:
					return 10;
				case PLATINUM_DONATOR:
					return 5;
				case LEGENDARY_DONATOR:
				case SUPREME_DONATOR:
					return 0;
				default:
					return 40;
			}
		}
	},
	CASH_FROM_SAPPHIRE_STALL {
		@Override
		public int handleBonus(Player player) {
			switch (player.getSecondaryGroup()) {
				case DONATOR:
					return 5;
				case SUPER_DONATOR:
					return 10;
				case ELITE_DONATOR:
					return 10;
				case NOBLE_DONATOR:
					return 10;
				case GOLD_DONATOR:
					return 10;
				case PLATINUM_DONATOR:
					return 10;
				case LEGENDARY_DONATOR:
					return 10;
				default:
					return -1;
			}
		}
	}
}
