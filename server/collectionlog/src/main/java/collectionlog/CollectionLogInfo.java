package collectionlog;

import io.ruin.cache.EnumMap;
import io.ruin.cache.ObjType;
import io.ruin.cache.Struct;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.item.Item;
import io.ruin.model.var.VarPlayerRepository;
import lombok.Getter;
import lombok.experimental.ExtensionMethod;

import java.util.*;

@ExtensionMethod(Attributes.class)
@Getter
public enum CollectionLogInfo {

	BOSS(471, 37, new int[] { 10, 11, 12, 29 }, 40697866, 40697867, 40697868, 40697869) {
		@Override
		public int getKillCount(Player player, int slot) {

			switch (slot) {
				case 0:// abyssal sire
					return player.abyssalSireKills.getKills();
				case 1:// alchemical hydra
					return player.alchemicalHydraKills.getKills();
				case 2:// barrows
					return player.barrowsChestsLooted.getKills();
				case 3:// bryophyta
					return player.bryophytaKills.getKills();
				case 4:// callisto
					return player.callistoKills.getKills();
				case 5:// cerberus
					return player.cerberusKills.getKills();
				case 6:// chaos ele
					return player.chaosElementalKills.getKills();
				case 7:// chaos fanatic
					return player.chaosFanaticKills.getKills();
				case 8:// commander zilyana
					return player.commanderZilyanaKills.getKills();
				case 9:// corporeal beast
					return player.corporealBeastKills.getKills();
				case 10:// crazy archaeologist
					return player.crazyArchaeologistKills.getKills();
				case 11:// dagannoth kings
					return player.dagannothRexKills.getKills() & player.dagannothPrimeKills.getKills()
							& player.dagannothSupremeKills.getKills();
				case 12:// the fight caves
					return player.theFightCaveKills.getKills();
				case 13:// the gauntlet
					return 0;
				case 14:// general graardor
					return player.generalGraardorKills.getKills();
				case 15:// giant mole
					return player.giantMoleKills.getKills();
				case 16:// grotesque guardians
					return player.grotesqueGuardianKills.getKills();
				case 17:// hespori
					return player.hesporiKills.getKills();
				case 18:// inferno
					return player.zukKills.getKills();
				case 19:// kalphite queen
					return player.kalphiteQueenKills.getKills();
				case 20:// king black dragon
					return player.kingBlackDragonKills.getKills();
				case 21:// kraken
					return player.krakenKills.getKills();
				case 22:// kree'ara
					return player.kreeArraKills.getKills();
				case 23:// k'ril
					return player.krilTsutsarothKills.getKills();
				case 24:// Nex
					return player.nexKills.getKills();
				case 25:// Nightmare
					return player.nightmareofAshihamaKills.getKills();
				case 26:// obor
					return player.oborKills.getKills();
				case 27:// sarachnis
					return player.sarachnisKills.getKills();
				case 28:// scorpia
					return player.scorpiaKills.getKills();
				case 29:// skotizo
					return player.skotizoKills.getKills();
				case 30:// Tempoross
					return player.temporossWins;
				case 31:// thermo devil
					return player.thermonuclearSmokeDevilKills.getKills();
				case 32:// venenatis
					return player.venenatisKills.getKills();
				case 33:// vet'ion
					return player.vetionKills.getKills();
				case 34:// vorkath
					return player.vorkathKills.getKills();
				case 35:// wintertodt
					return player.wintertodtKills.getKills();
				case 36:// zalcano
					return 0;
				case 37:// zulrah
					return player.zulrahKills.getKills();
				default:
					return 0;
			}
		}
	},
	RAIDS(472, 2, new int[] { 14, 15, 16 }, 40697870, 40697871, 40697872, 40697878) {
		@Override
		public int getKillCount(Player player, int slot) {
			switch (slot) {
				case 0:// cox
					return player.chambersofXericKills.getKills();
				case 1:// tob
					return player.theatreOfBloodKills.getKills();
				default:
					return 0;
			}
		}
	},
	CLUES(473, 9, new int[] { 22, 30, 31 }, 40697879, 40697887, 40697888, 40697880) {
		@Override
		public int getKillCount(Player player, int slot) {
			switch (slot) {
				case 0:// beginner treasure trails
					return player.beginnerClueCount;
				case 1:// easy treasure trails
					return player.easyClueCount;
				case 2:// medium treasure trails
					return player.medClueCount;
				case 3:// hard treasure trails
				case 6:// shared hard
					return player.hardClueCount;
				case 4:// elite treasure trails
				case 7:// shared elite
					return player.eliteClueCount;
				case 5:// master treasure trails
				case 8:// shared master
					return player.masterClueCount;
				case 9:// shared treasure trail rewards
					return CollectionLog.addSumMultipleClues(player);
				default:
					return 0;
			}
		}
	},
	MINIGAMES(474, 18, new int[] { 25, 26, 35 }, 40697881, 40697882, 40697891, 40697883) {
		@Override
		public int getKillCount(Player player, int slot) {
			return 0;
		}
	},
	OTHER(475, 21, new int[] { 27, 32, 33 }, 40697884, 40697889, 40697890, 40697885) {
		@Override
		public int getKillCount(Player player, int slot) {
			switch (slot) {
				case 0:
					return 0;
				case 8:// glough's experiments kills
					return player.demonicGorillaKills.getKills() + player.torturedGorillaKills.getKills();
				case 12:// revenant kills
					return player.revenantKills.getKills();

				default:
					return 0;
			}
		}
	};

	@Getter
	private int categoryStruct;
	private int count;
	private int[] childIds;
	@Getter
	private int[] params;
	private Map<Integer, int[]> items = new HashMap<>();
	private List<Integer> enums = new ArrayList<>();

	CollectionLogInfo(int categoryStruct, int count, int[] childIds, int... params) {
		this.categoryStruct = categoryStruct;
		this.count = count;
		this.childIds = childIds;
		this.params = params;
	}

	public void sendAccessMasks(Player player) {
		for (int i = 0; i < childIds.length; i++) {
			player.getPacketSender().sendIfEvents(Interface.COLLECTION_LOG, childIds[i], 0, count, 2);
		}
	}

	public void sendItems(Player player, int slot) {
		int enumId = enums.get(slot);

		int[] itemIds = getItems(enumId);

		Item[] container = new Item[itemIds.length];

		for (int index = 0; index < itemIds.length; index++) {
			int itemId = itemIds[index];
			int amount = player.collectionLog().getCollected(itemId);

			if (amount > 0) {
				container[index] = new Item(itemId, amount);
			}
		}

		player.getPacketSender().sendItems(-1, -1, 620, container);
	}

	public int[] getItems(int enumId) {
		return items.get(enumId);
	}

	public void sendKillCount(Player player, int slot) {
		VarPlayerRepository.COLLECTION_LOG_KC.set(player, getKillCount(player, slot));
	}

	public int getKillCount(Player player, int slot) {
		return 3;
	}

	private static final int STRUCT_LOG_SUBCATEGORY = 683;
	private static final int STRUCT_LOG_GROUP = 690;

	public static int TOTAL_COLLECTABLES;

	public static void register() {
		for (CollectionLogInfo info : values()) {
			Struct category = Struct.get(info.getCategoryStruct());

			EnumMap subcategories = EnumMap.get(category.getInt(STRUCT_LOG_SUBCATEGORY));

			for (int slot = 0; slot < subcategories.length; slot++) {
				Struct subcategory = Struct.get(subcategories.intValues[slot]);

				EnumMap group = EnumMap.get(subcategory.getInt(STRUCT_LOG_GROUP));
				info.items.put(subcategory.getInt(STRUCT_LOG_GROUP), group.intValues);
				info.enums.add(subcategory.getInt(STRUCT_LOG_GROUP));

				for (int index = 0; index < group.intValues.length; index++) {
					ObjType.get(group.intValues[index]).collectable = true;
					TOTAL_COLLECTABLES++;
				}
			}
		}
	}

}
