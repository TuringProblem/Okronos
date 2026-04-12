package io.ruin.model.entity.npc;

import discord.webhooks.notifications.RareDropHook;
import io.ruin.Server;
import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.cache.NPCType;
import io.ruin.data.impl.npcs.npc_combat;
import io.ruin.data.impl.npcs.npc_drops_new;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.bosses.Argentavis;
import io.ruin.model.activities.bosses.GiantMole;
import io.ruin.model.activities.bosses.KalphiteQueen;
import io.ruin.model.activities.bosses.Leviathan;
import io.ruin.model.activities.bosses.Skotizo;
import io.ruin.model.activities.bosses.SolHeredit;
import io.ruin.model.activities.bosses.Vardorvis;
import io.ruin.model.activities.bosses.Whisperer;
import io.ruin.model.activities.bosses.corp.CorporealBeast;
import io.ruin.model.activities.bosses.dagannothkings.DagannothPrime;
import io.ruin.model.activities.bosses.dagannothkings.DagannothRex;
import io.ruin.model.activities.bosses.dagannothkings.DagannothSupreme;
import io.ruin.model.activities.bosses.galvek.Galvek;
import io.ruin.model.activities.bosses.grotesqueguardians.Dusk;
import io.ruin.model.activities.bosses.hydra.AlchemicalHydra;
import io.ruin.model.activities.bosses.phantommuspah.PhantomMuspah;
import io.ruin.model.activities.bosses.sarachnis.Sarachnis;
import io.ruin.model.activities.bosses.slayer.Cerberus;
import io.ruin.model.activities.bosses.slayer.Kraken;
import io.ruin.model.activities.bosses.slayer.ThermonuclearSmokeDevil;
import io.ruin.model.activities.bosses.slayer.sire.AbyssalSire;
import io.ruin.model.activities.bosses.tarn.Tarn;
import io.ruin.model.activities.bosses.vorkath.Vorkath;
import io.ruin.model.activities.bosses.zulrah.Zulrah;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.godwars.combat.General;
import io.ruin.model.activities.godwars.combat.impl.armadyl.KreeArra;
import io.ruin.model.activities.godwars.combat.impl.bandos.GeneralGraardor;
import io.ruin.model.activities.godwars.combat.impl.saradomin.CommanderZilyana;
import io.ruin.model.activities.godwars.combat.impl.zamorak.KrilTsutsaroth;
import io.ruin.model.activities.miscpvm.dragons.KingBlackDragon;
import io.ruin.model.activities.perktree.PerkTaskHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.LayingToRest;
import io.ruin.model.activities.perktree.perks.SecuringTheBag;
import io.ruin.model.activities.perktree.perks.SnakeCharmer;
import io.ruin.model.activities.perktree.perks.TheAlchemist;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.activities.perktree.perks.VanquishedFoe;
import io.ruin.model.activities.raids.xeric.chamber.combat.Tekton;
import io.ruin.model.activities.tempevents.RevenantMaledictusManager;
import io.ruin.model.activities.tempevents.summerevent.SummerEvent;
import io.ruin.model.activities.tempevents.summerevent.SummerEventHandler;
import io.ruin.model.activities.wilderness.RevCaves;
import io.ruin.model.activities.wilderness.WalkerSpawnHandler;
import io.ruin.model.activities.wilderness.Wilderness;
import io.ruin.model.activities.wilderness.bosses.ChaosElemental;
import io.ruin.model.activities.wilderness.bosses.scorpia.Scorpia;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Combat;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.combat.Killer;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.content.combatachievements.CombatAchievementSystem;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.content.upgrade.ItemEffect;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Difficulty;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerCombat;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.BoneCrusher;
import io.ruin.model.item.actions.impl.itemeffects.itemhandlers.RespectTheDead;
import io.ruin.model.item.actions.impl.itemeffects.itemhandlers.SiphonTheDead;
import io.ruin.model.item.actions.impl.jewellery.BraceletOfEthereum;
import io.ruin.model.item.actions.impl.jewellery.RingOfWealth;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Graphic;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.Tile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.map.object.actions.impl.dungeons.KourendCatacombs;
import io.ruin.model.map.route.routes.DumbRoute;
import io.ruin.model.map.route.routes.TargetRoute;
import io.ruin.model.skills.magic.spells.arceuus.ReLive;
import io.ruin.model.skills.prayer.Ashes;
import io.ruin.model.skills.prayer.Bone;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.model.skills.slayer.SlayerMaster;
import io.ruin.model.skills.slayer.SuperiorSlayer;
import io.ruin.model.skills.slayer.master.BossMaster;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Broadcast;
import io.ruin.utility.Utils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

import static io.ruin.cache.ItemID.*;

@Slf4j
public abstract class NPCCombat extends Combat {

	public static final Bounds Wildywars = new Bounds(3015, 10117, 3067, 10169, 0);

	@Getter
	protected transient NPC npc;
	protected transient npc_combat.Info info;
	protected transient Stat[] stats;
	private transient int[] bonuses;
	public transient boolean allowRespawn = true;
	transient NPCRareDropTable npcRareDropTable = new NPCRareDropTable();
	@Setter
	@Getter
	private transient boolean allowRetaliate = true;

	public final NPCCombat init(NPC npc, npc_combat.Info info) {
		this.npc = npc;
		this.info = info;
		this.stats = new Stat[] {
				new Stat(info.attack), // 0
				new Stat(info.defence), // 1
				new Stat(info.strength), // 2
				new Stat(info.hitpoints), // 3
				new Stat(info.ranged), // 4
				new Stat(1), // 5 (prayer, not used)
				new Stat(info.magic) // 6
		};
		this.bonuses = new int[] {

				info.stab_attack,
				info.slash_attack,
				info.crush_attack,
				info.magic_attack,
				info.ranged_attack,

				info.stab_defence,
				info.slash_defence,
				info.crush_defence,
				info.magic_defence,
				info.ranged_defence,
		};

		// HOTFIX: shitcode
		if (npc.spawnPosition == null) {
			return this;
		}
		init();
		return this;
	}

	public void update(npc_combat.Info info) {
		this.info = info;
		this.stats = new Stat[] {
				new Stat(info.attack), // 0
				new Stat(info.defence), // 1
				new Stat(info.strength), // 2
				new Stat(info.hitpoints), // 3
				new Stat(info.ranged), // 4
				new Stat(1), // 5 (prayer, not used)
				new Stat(info.magic) // 6
		};
		this.bonuses = new int[] {

				info.stab_attack,
				info.slash_attack,
				info.crush_attack,
				info.magic_attack,
				info.ranged_attack,

				info.stab_defence,
				info.slash_defence,
				info.crush_defence,
				info.magic_defence,
				info.ranged_defence,
		};
		this.updateInfo(info);
	}

	public void updateInfo(npc_combat.Info newInfo) { // only used when transforming!
		info = newInfo;
		if (stats[0].fixedLevel != newInfo.attack)
			stats[0] = new Stat(newInfo.attack);
		if (stats[1].fixedLevel != newInfo.defence)
			stats[1] = new Stat(newInfo.defence);
		if (stats[2].fixedLevel != newInfo.strength)
			stats[2] = new Stat(newInfo.strength);
		if (stats[3].fixedLevel != newInfo.hitpoints)
			stats[3] = new Stat(newInfo.hitpoints);
		if (stats[4].fixedLevel != newInfo.ranged)
			stats[4] = new Stat(newInfo.ranged);
		if (stats[6].fixedLevel != newInfo.magic)
			stats[6] = new Stat(newInfo.magic);

		this.bonuses = new int[] { // bonuses cannot be changed so we can just set to the new ones

				info.stab_attack,
				info.slash_attack,
				info.crush_attack,
				info.magic_attack,
				info.ranged_attack,

				info.stab_defence,
				info.slash_defence,
				info.crush_defence,
				info.magic_defence,
				info.ranged_defence,
		};
	}

	public List<Item> getPotentialDrops(Player player, int npcId) {
		List<npc_drops_new.DropTable> dropInformation = npc_drops_new.getDropInformationNPCCombat(npcId);

		if (dropInformation == null || dropInformation.isEmpty()) {
			return null;
		}

		List<Item> potentialDrops = new ArrayList<>();
		Item closestItemToPassing = null;
		int closestDropRate = Integer.MAX_VALUE;

		for (npc_drops_new.DropTable drop : dropInformation) {
			if (drop == null)
				continue;
			int dropRate = drop.dropRate;

			if (dropRate <= 1) {
				continue;
			}
			if (npcId != 319 && (npc.getDef() != null && !npc.getDef().name.equalsIgnoreCase("nex")))
				dropRate /= 2;

			if (player.dropRateBoostTimer.remaining() > 0)
				dropRate *= 0.95;

			int amount = Random.get(drop.minAmount, drop.maxAmount);
			float dropRateBonus = 1 - (player.calculateDropRate() / 100f);
			dropRate *= dropRateBonus;

			if (Random.get(dropRate) == 0) {
				Item item = new Item(drop.itemid, amount);
				if (drop.broadcast != null) {
					item.lootBroadcast = drop.broadcast;
				}
				potentialDrops.add(item);
			} else {
				if (dropRate < closestDropRate) {
					closestItemToPassing = new Item(drop.itemid, amount);
					closestDropRate = dropRate;

					if (drop.broadcast != null) {
						closestItemToPassing.lootBroadcast = drop.broadcast;
					}
				}
			}
		}

		if (potentialDrops.isEmpty() && closestItemToPassing != null) {
			// Include the broadcast mode in the item construction

			potentialDrops.add(closestItemToPassing);
		}
		return potentialDrops;
	}

	public List<Item> getGuaranteedDrops(Player player, int npcId) {
		List<npc_drops_new.DropTable> dropInformation = npc_drops_new.getDropInformationNPCCombat(npcId);
		if (dropInformation == null || dropInformation.isEmpty()) {
			return null;
		}

		List<Item> guaranteedDrops = new ArrayList<>();

		for (npc_drops_new.DropTable drop : dropInformation) {
			if (drop.dropRate <= 1) {
				guaranteedDrops.add(new Item(drop.itemid, Random.get(drop.minAmount, drop.maxAmount)));
			}
		}
		return guaranteedDrops;
	}

	public static boolean rollExtraDonatorDrop(Player player) {
		int percentChance = 0;
		switch (player.getSecondaryGroup()) {
			case DONATOR -> percentChance = 2;
			case SUPER_DONATOR -> percentChance = 3;
			case ELITE_DONATOR -> percentChance = 4;
			case NOBLE_DONATOR -> percentChance = 5;
			case GOLD_DONATOR -> percentChance = 6;
			case PLATINUM_DONATOR -> percentChance = 7;
			case LEGENDARY_DONATOR -> percentChance = 8;
			case SUPREME_DONATOR -> percentChance = 10;
		}
		if (percentChance == 0)
			return false;
		return Random.get(1, 100) <= percentChance;
	}

	public void handleNewDrop(Player player, int npcID, Position dropPosition) {
		if (npc.getDef() != null && npc.getDef().name.equalsIgnoreCase("dawn"))
			return;

		if (npc.getHp() > 200) {
			player.getSecurityGuard().rollGuardSpawnChance(player, 50);
		} else {
			player.getSecurityGuard().rollGuardSpawnChance(player, 100);
		}

		List<Item> guaranteedDrops = getGuaranteedDrops(player, npcID);
		List<Item> itemsToDrop = new ArrayList<>();

		if (npc.getId() == 12191 || npc.getId() == 12192 || npc.getId() == 8095 || npcID == 8096 || npcID == 8097
				|| npcID == 8098)
			dropPosition = player.getPosition();

		if (Tile.get(dropPosition, true).clipping == 0)
			dropPosition = getClosestDropPositionWithClipping(player, dropPosition);

		if (guaranteedDrops != null) {
			itemsToDrop.addAll(guaranteedDrops);
		}
		SummerEvent.handleKill(player, npc.getDef().name);

		if (npc.getDef().name.contains("kraken") || npc.getDef().name.contains("Kraken")) {
			dropPosition = player.getPosition();
		}

		if (npc.getDef().name.contains("leviathan") || npc.getDef().name.contains("Leviathan")) {
			dropPosition = new Position(player.getPosition().getRegion().baseX + 27,
					player.getPosition().getRegion().baseY + 35, player.getPosition().getZ());
		}

		if (npc.getDef().name.equalsIgnoreCase("zulrah")) {
			dropPosition = player.getPosition();
		}

		long playtime = player.playTime * Server.tickMs();
		long days = TimeUnit.MILLISECONDS.toDays(playtime);

		if (npc.getId() > 8063 && npc.getId() < 8067 && days < 1) {
			rollNewcomerCatalystDrop(player, npc);
		}

		rollMediumCashDrop(player, npc);
		rollCommonCashDrop(player, npc);

		var rolls = dropRolls(player, npc);

		for (int i = 0; i < rolls; i++) {
			List<Item> potentialDrops = getPotentialDrops(player, npcID);
			if (potentialDrops == null || potentialDrops.isEmpty()) {
				continue;
			}

			Item item = Random.get(potentialDrops);
			boolean halfKeyDrop = item.getId() == LOOP_HALF_OF_KEY || item.getId() == TOOTH_HALF_OF_KEY;

			if (item.getId() == 2722) {
				Objects.requireNonNull(player.combatAchievementsList
						.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.FEELING_LUCKY.ordinal()))
						.getCombatAchievement()).check(player);
			}

			if (halfKeyDrop && player.totalDonated >= 500) {
				item.setId(CRYSTAL_KEY);
			}

			boolean hydraRingDrop = item.getId() == HYDRAS_EYE || item.getId() == HYDRAS_FANG || item.getId() == HYDRAS_HEART;

			if (hydraRingDrop) {
				if (player.lastHydraRingDrop != -1) {
					if (player.lastHydraRingDrop == HYDRAS_EYE) {
						item.setId(HYDRAS_FANG);
						player.lastHydraRingDrop = HYDRAS_FANG;
					} else if (player.lastHydraRingDrop == HYDRAS_FANG) {
						item.setId(HYDRAS_HEART);
						player.lastHydraRingDrop = HYDRAS_HEART;
					} else {
						item.setId(HYDRAS_EYE);
						player.lastHydraRingDrop = HYDRAS_EYE;
					}
				} else {
					item.setId(HYDRAS_EYE);
					player.lastHydraRingDrop = HYDRAS_EYE;
				}
			}
			itemsToDrop.add(item);

		}
		if (!itemsToDrop.isEmpty()) {
			Killer killer = new Killer();
			killer.player = player;
			handleDrop(killer, dropPosition, player, itemsToDrop);
			SummerEventHandler.rollForSummerNPCSpawn(player, npc);
		}
		/*
		 * Handle giving player vorkaths head after 50 kills.
		 */
		vorkathHead(dropPosition, player);
		NPCType def = npc.getDef();

		/*
		 * Gives players PVM Points
		 */
		// PvmPoints.addPoints(pKiller, npc);
		/*
		 * Gives players Boss Points
		 */
		// BossPoints.addPoints(pKiller, npc);

		if (def.name.contains("Revenant") && def.id != 11246) {
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.REVENANT_ADEPT.ordinal()))
					.getCombatAchievement()).check(player);
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.REVENANT_NOVICE.ordinal()))
					.getCombatAchievement()).check(player);
			if (World.revenantMaledictusEvent instanceof RevenantMaledictusManager) {
				((RevenantMaledictusManager) World.revenantMaledictusEvent).rollSpawnChance(npc);
			}
		}
		/*
		 * Casket loots
		 */
		// GoldCasket.drop(pKiller, npc, dropPosition);
		/*
		 * Catacombs loot
		 */
		KourendCatacombs.drop(player, npc, dropPosition);

		/*
		 * Roll for OSRS wilderness key
		 */
		// if (World.wildernessKeyEvent)
		// WildernessKey.rollForWildernessBossKill(pKiller, npc);

		/*
		 * PvP Item loots
		 */
		// Wilderness.rollPvPItemDrop(pKiller, npc, dropPosition);

		/*
		 * Roll for wilderness clue key
		 */
		// Wilderness.rollClueKeyDrop(pKiller, npc, dropPosition);

		/*
		 * Blood Money
		 */
		// Wilderness.bloodMoneyDrop(pKiller, npc);

		switch (npc.getId()) {
			case 9040:
			case 9041:
			case 9042:
			case 9026:
			case 9027:
			case 9028:
				player.gauntlet.rollTier1MonsterDrop(player, npc);
				break;
			case 9043:
			case 9044:
			case 9045:
			case 9029:
			case 9030:
			case 9031:
				player.gauntlet.rollTier2MonsterDrop(player, npc);
				break;
			case 9046:
			case 9047:
			case 9048:
			case 9032:
			case 9033:
			case 9034:
				player.gauntlet.rollDemiBossDrop(player, npc);
				break;
		}
		int baseBeginnerClueReward = 40;
		int baseEasyClueReward = 40;
		int baseMediumClueReward = 50;
		int baseHardClueReward = 60;
		int baseEliteClueReward = 150;
		int baseMasterClueReward = 250;

		if (CombatAchievementSystem.getTier(player.combatAchievementPoints) == CombatAchievement.Tier.GRANDMASTER) {
			baseBeginnerClueReward = 25;
			baseEasyClueReward = 25;
			baseMediumClueReward = 30;
			baseHardClueReward = 35;
			baseEliteClueReward = 100;
			baseMasterClueReward = 150;
		} else if (CombatAchievementSystem.getTier(player.combatAchievementPoints) == CombatAchievement.Tier.MASTER) {
			baseBeginnerClueReward = 30;
			baseEasyClueReward = 30;
			baseMediumClueReward = 35;
			baseHardClueReward = 40;
			baseEliteClueReward = 120;
			baseMasterClueReward = 200;
		} else if (CombatAchievementSystem.getTier(player.combatAchievementPoints) == CombatAchievement.Tier.ELITE) {
			baseBeginnerClueReward = 30;
			baseEasyClueReward = 30;
			baseMediumClueReward = 35;
			baseHardClueReward = 40;
			baseEliteClueReward = 120;
		} else if (CombatAchievementSystem.getTier(player.combatAchievementPoints) == CombatAchievement.Tier.HARD) {
			baseBeginnerClueReward = 30;
			baseEasyClueReward = 30;
			baseMediumClueReward = 35;
			baseHardClueReward = 40;
		} else if (CombatAchievementSystem.getTier(player.combatAchievementPoints) == CombatAchievement.Tier.MEDIUM) {
			baseBeginnerClueReward = 30;
			baseEasyClueReward = 30;
			baseMediumClueReward = 35;
		} else if (CombatAchievementSystem.getTier(player.combatAchievementPoints) == CombatAchievement.Tier.EASY) {
			baseBeginnerClueReward = 30;
			baseEasyClueReward = 30;
		}
		if (player.getEquipment().get(Equipment.SLOT_RING) != null
				&& AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_RING),
						AttributeTypes.CLUE_HUNTER)) {
			int level = AttributeExtensions.getCharges(AttributeTypes.CLUE_HUNTER,
					player.getEquipment().get(Equipment.SLOT_RING));
			double multiplier = 1 - (level * 0.1);
			baseEasyClueReward *= multiplier;
			baseMediumClueReward *= multiplier;
			baseHardClueReward *= multiplier;
			baseBeginnerClueReward *= multiplier;
			baseEliteClueReward *= multiplier;
			baseMasterClueReward *= multiplier;
		}
		int beginnerClueReward = Random.get(baseBeginnerClueReward);
		int easyClueReward = Random.get(baseEasyClueReward);
		int mediumClueReward = Random.get(baseMediumClueReward);
		int hardClueReward = Random.get(baseHardClueReward);
		int eliteClueReward = Random.get(baseEliteClueReward);
		int masterClueReward = Random.get(baseMasterClueReward);

		if (def.combatLevel < 25 && beginnerClueReward == 1) {
			this.dropItemOrAddToInv(23182, 1, player, dropPosition, () -> {
				var ring = player.getEquipment().get(Equipment.SLOT_RING);
				return ring != null && AttributeExtensions.hasAttribute(ring, AttributeTypes.CLUE_HUNTER);
			});
		}

		if (def.combatLevel > 25 && def.combatLevel < 50 && easyClueReward == 1) {
			this.dropItemOrAddToInv(2677, 1, player, dropPosition, () -> {
				var ring = player.getEquipment().get(Equipment.SLOT_RING);
				return ring != null && AttributeExtensions.hasAttribute(ring, AttributeTypes.CLUE_HUNTER);
			});
		}

		if (def.combatLevel > 50 && def.combatLevel < 80 && mediumClueReward == 1) {
			this.dropItemOrAddToInv(2801, 1, player, dropPosition, () -> {
				var ring = player.getEquipment().get(Equipment.SLOT_RING);
				return ring != null && AttributeExtensions.hasAttribute(ring, AttributeTypes.CLUE_HUNTER);
			});
		}

		if (def.combatLevel > 80 && hardClueReward == 1) {
			Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.FEELING_LUCKY.ordinal()))
					.getCombatAchievement()).check(player);

			this.dropItemOrAddToInv(2722, 1, player, dropPosition, () -> {
				var ring = player.getEquipment().get(Equipment.SLOT_RING);
				return ring != null && AttributeExtensions.hasAttribute(ring, AttributeTypes.CLUE_HUNTER);
			});
		}

		if (def.combatLevel > 140 && eliteClueReward == 1) {
			this.dropItemOrAddToInv(12073, 1, player, dropPosition, () -> {
				var ring = player.getEquipment().get(Equipment.SLOT_RING);
				return ring != null && AttributeExtensions.hasAttribute(ring, AttributeTypes.CLUE_HUNTER);
			});
		}

		if (def.combatLevel > 140 && masterClueReward == 1) {
			this.dropItemOrAddToInv(19835, 1, player, dropPosition, () -> {
				var ring = player.getEquipment().get(Equipment.SLOT_RING);
				return ring != null && AttributeExtensions.hasAttribute(ring, AttributeTypes.CLUE_HUNTER);
			});
		}

		if (def.combatLevel >= 100) {
			int combatLevel = def.combatLevel;
			int hpLevel = npc.getMaxHp();
			int percentageBoost = player.getDifficulty().GetPercentagePointBoost();
			int points = (int) ((combatLevel + hpLevel) / 6 * 1.25);
			if (player.getDifficulty() == Difficulty.EASY)
				percentageBoost = 1;
			int percentageOfPoints = points / percentageBoost;
			int maxPoints = points + percentageOfPoints;
			int minPoints = (int) (maxPoints * 0.8);
			int pointsReward = (int) (Random.get(minPoints, maxPoints) * player.pointMultiplier());
			switch (CombatAchievementSystem.getTier(player.combatAchievementPoints)) {
				case ELITE -> pointsReward = (int) (pointsReward * 1.05);
				case MASTER -> pointsReward = (int) (pointsReward * 1.1);
				case GRANDMASTER -> pointsReward = (int) (pointsReward * 1.2);
			}
			player.updateReasonPoints(pointsReward);
			player.sendFilteredMessage("You receive <col=000000><shad=29F1FE>" + pointsReward
					+ " Reason points<col=000000></shad> for killing a " + def.name + ".");
			if (Random.get(0, 250) == 0)
				npcRareDropTable.HandleDrop(player, 2, npc);
		} else if (def.combatLevel < 100 && Random.get(0, 250) == 0)
			npcRareDropTable.HandleDrop(player, 1, npc);

		if (def.name.equalsIgnoreCase("sand crab") || def.name.equalsIgnoreCase("rock crab")) {
			player.crabKills++;
			if (player.crabKills == Achievements.ARE_YOU_SHORE_ABOUT_THIS.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
						+ Achievements.ARE_YOU_SHORE_ABOUT_THIS.name());
		}

		if (def.name.equalsIgnoreCase("lava dragon")) {
			player.lavaDragonsKilled++;
			if (player.lavaDragonsKilled == Achievements.WATCH_YOUR_SURROUNDINGS.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
						+ Achievements.WATCH_YOUR_SURROUNDINGS.getAchievementName());
		}
		if (def.name.equalsIgnoreCase("chaos elemental")) {
			if (player.chaosElementalKills.getKills() == Achievements.RELAX_YOURE_ALMOST_DONE.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
						+ Achievements.WATCH_YOUR_SURROUNDINGS.getAchievementName());

		}
		if (def.name.equalsIgnoreCase("Kraken")) {
			if (player.krakenKills.getKills() == Achievements.FROM_THE_KRAK_OF_DAWN.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
						+ Achievements.FROM_THE_KRAK_OF_DAWN.getAchievementName());
		}
		if (def.name.equalsIgnoreCase("Vorkath")) {
			if (player.vorkathKills.getKills() == Achievements.PULL_THE_VORK_OUT.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
						+ Achievements.PULL_THE_VORK_OUT.getAchievementName());
		}
		if (def.name.equalsIgnoreCase("Galvek")) {
			if (player.galvekKills.getKills() == Achievements.FACING_YOUR_FEARS.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
						+ Achievements.FACING_YOUR_FEARS.getAchievementName());
		}
		if (def.name.equalsIgnoreCase("Alchemical Hydra")) {
			if (player.alchemicalHydraKills.getKills() == Achievements.IT_HAS_HOW_MANY_HEADS.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
						+ Achievements.IT_HAS_HOW_MANY_HEADS.getAchievementName());
		}
		if (def.name.equalsIgnoreCase("Zulrah")) {
			DailyTasks.handleTaskProgression(player, "zulrah");
			if (player.zulrahKills.getKills() == Achievements.CUT_THE_HEAD_OFF_THE_SNAKE.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
						+ Achievements.CUT_THE_HEAD_OFF_THE_SNAKE.getAchievementName());
		}
		if (def.name.contains("Revenant")) {
			player.revenantKillcount++;
			if (player.revenantKillcount == Achievements.PRAY_TO_RNJESUS.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
						+ Achievements.PRAY_TO_RNJESUS.getAchievementName());
		}

		if (def.id == 3129 || def.id == 2215 || def.id == 3162 || def.id == 2205) {
			int totalKills = (player.krilTsutsarothKills.getKills() + player.commanderZilyanaKills.getKills()
					+ player.generalGraardorKills.getKills() + player.kreeArraKills.getKills());
			if (totalKills == Achievements.SACRELIGIOUS.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
						+ Achievements.SACRELIGIOUS.getAchievementName());
		}

		/*
		 * Resource packs
		 */
		Wilderness.resourcePackWithBoss(player, npc);
		if (player != null) {
			if (player.wildernessLevel >= 1) {
				if (npc.getDef().combatLevel > 10) {
					if (Random.get(15) == 1) {
						if (!player.getBank().contains(11941) && !player.getInventory().contains(11941)
								&& !player.getBank().contains(22586) && !player.getInventory().contains(22586)) {
							this.dropItem(11941, 1, player, dropPosition);
						}
					}
				}
			}
		}
	}

	private Position getClosestDropPositionWithClipping(Player player, Position dropPosition) {
		int x = dropPosition.getX();
		int y = dropPosition.getY();
		int z = dropPosition.getZ();
		int regionX = dropPosition.getRegion().baseX;
		int regionY = dropPosition.getRegion().baseY;
		int regionZ = player.getPosition().getZ();
		int[] xOffsets = { 0, 1, 0, -1, 0 };
		int[] yOffsets = { 0, 0, 1, 0, -1 };
		for (int i = 0; i < xOffsets.length; i++) {
			int newX = x + xOffsets[i];
			int newY = y + yOffsets[i];
			if (Tile.get(newX, newY, z, true).clipping == 0) {
				return new Position(newX, newY, z);
			}
		}
		return new Position(regionX, regionY, regionZ);
	}

	private void dropCatalystWeapon(Player player, NPC npc) {
		Item item;
		if (npc.getId() == 8065) {
			item = new Item(30315, 1);
			AttributeExtensions.setCharges(item, 10000);
			this.dropItem(item, player);
		} else if (npc.getId() == 8066) {
			item = new Item(30312, 1);
			AttributeExtensions.setCharges(item, 10000);
			this.dropItem(item, player);
		} else {
			item = new Item(30309, 1);
			AttributeExtensions.setCharges(item, 10000);
			this.dropItem(item, player);
		}
		Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(),
				" received <shad=D80808>" + item.getAmount() + "x " + item.getDef().name.toLowerCase() + "</shad> from a "
						+ npc.getDef().name.toLowerCase() + "! (<col=FC0101>"
						+ (npc.getDef().killCounter.apply(player).getKills() + 1) + " KC<col=000000>)");
		String message = player.getName() + " just received ";
		message += item.getDef().descriptiveName;

		RareDropHook.sendDiscordMessage(() -> {
			var jsonObject = new JSONObject();
			jsonObject.put("player", player.getName());
			jsonObject.put("game_mode", player.getGameMode());
			jsonObject.put("item_id", item.getId());
			jsonObject.put("item_name", item.getDef().name);
			jsonObject.put("source", npc.getDef().descriptiveName);
			jsonObject.put("total_attempts", Utils.formatMoneyString(getNpc().getDef().killCounter.apply(player).getKills() +
					1));
			return jsonObject;
		});
	}

	private void rollCommonCashDrop(Player player, NPC npc) {
		if (npc.getPosition() == null)
			return;
		Position dropPosition = npc.getPosition().copy();
		if (npc.getDef().name.equalsIgnoreCase("Zulrah") || npc.getDef().name.equalsIgnoreCase("Kraken"))
			dropPosition = player.getPosition();
		List<String> commonBosses = Arrays.asList(
				"abyssal sire",
				"alchemical hydra", "cerberus", "corporeal beast", "dagannoth prime",
				"dagannoth rex", "dagannoth supreme", "general graardor",
				"kalphite queen", "kraken", "kree'arra", "sarachnis", "skotizo", "thermonuclear smoke devil",
				"vorkath", "zulrah", "kril tsutsaroth", "commander zilyana", "callisto", "argentavis", "phantom muspah",
				"vet'ion", "venenatis", "chaos elemental", "chaos fanatic", "scorpia", "crazy archaeologist",
				"king black dragon");
		if (Tile.get(dropPosition, true).clipping == 0)
			dropPosition = player.getPosition();

		int baseChance = 9;
		switch (player.getSecondaryGroup()) {
			case ELITE_DONATOR -> baseChance = 8;
			case NOBLE_DONATOR -> baseChance = 7;
			case GOLD_DONATOR -> baseChance = 7;
			case PLATINUM_DONATOR -> baseChance = 6;
			case LEGENDARY_DONATOR -> baseChance = 5;
			case SUPREME_DONATOR -> baseChance = 4;
		}

		for (String boss : commonBosses) {
			if (npc.getDef().name.equalsIgnoreCase(boss) && Random.get(baseChance) == 0) {
				if (player.totalDonated >= 250) {
					this.dropItemOrAddToInv(995, Random.get(4) == 0 ? 5_000_000 : 2_000_000, player, player.getPosition());
				} else {
					this.dropItem(995, Random.get(4) == 0 ? 2_000_000 : 500_000, player, dropPosition);
				}
				return;
			}
		}
	}

	private void rollMediumCashDrop(Player player, NPC npc) {
		if (npc.getPosition() == null)
			return;
		Position dropPosition = npc.getPosition().copy();
		if (npc.getDef().name.equalsIgnoreCase("Zulrah") || npc.getDef().name.equalsIgnoreCase("The Leviathan")
				|| npc.getDef().name.equalsIgnoreCase("Galvek") || npc.getDef().name.equalsIgnoreCase("Kraken"))
			dropPosition = player.getPosition();
		List<String> commonBosses = Arrays.asList(
				"abyssal sire",
				"alchemical hydra", "cerberus", "corporeal beast",
				"general graardor",
				"kalphite queen", "kraken", "kree'arra", "sarachnis", "skotizo", "thermonuclear smoke devil",
				"vorkath", "zulrah", "kril tsutsaroth", "commander zilyana", "callisto", "argentavis", "phantom muspah", "nex",
				"the leviathan",
				"vardorvis", "the whisperer", "duke sucellus", "nightmare", "galvek", "vet'ion", "venenatis", "chaos elemental",
				"chaos fanatic", "scorpia", "crazy archaeologist", "king black dragon");
		if (Tile.get(dropPosition, true).clipping == 0)
			dropPosition = player.getPosition();

		int baseChance = 50;
		int baseRareChance = 499;
		switch (player.getSecondaryGroup()) {
			case DONATOR -> {
				baseChance = 45;
				baseRareChance = 450;
			}
			case SUPER_DONATOR -> {
				baseChance = 40;
				baseRareChance = 430;
			}
			case ELITE_DONATOR -> {
				baseChance = 37;
				baseRareChance = 390;
			}
			case NOBLE_DONATOR -> {
				baseChance = 35;
				baseRareChance = 370;
			}
			case GOLD_DONATOR -> {
				baseChance = 32;
				baseRareChance = 350;
			}
			case PLATINUM_DONATOR -> {
				baseChance = 30;
				baseRareChance = 330;
			}
			case LEGENDARY_DONATOR -> {
				baseChance = 28;
				baseRareChance = 310;
			}
			case SUPREME_DONATOR -> {
				baseChance = 25;
				baseRareChance = 290;
			}
		}
		for (String boss : commonBosses) {
			if (npc.getDef().name.equalsIgnoreCase(boss)) {
				if (Random.get(baseChance) == 0) {
					var amount = Random.get(4) == 0 ? 5_000_000 : 2_000_000;
					if (player.totalDonated >= 250)
						this.dropItemOrAddToInv(995, amount, player, player.getPosition());
					else
						this.dropItem(995, amount, player, dropPosition);
				}

				if (Random.get(baseRareChance) == 0) {
					var amount = Random.get(4) == 0 ? 50_000_000 : 25_000_000;
					if (player.totalDonated >= 250)
						this.dropItemOrAddToInv(995, amount, player, player.getPosition());
					else
						this.dropItem(995, amount, player, dropPosition);
				}
			}
		}
	}

	private void rollNewcomerCatalystDrop(Player player, NPC npc) {
		float baseWeaponRate = 50;
		baseWeaponRate *= player.getDifficulty().GetDropRate();
		if (Random.get(3) == 0) {
			Item casket = new Item(25590, 1);
			this.dropItem(casket, player);
		}
		if (Random.get((int) baseWeaponRate) == 0) {
			dropCatalystWeapon(player, npc);
		}
		float baseRate = 12;
		baseRate *= player.getDifficulty().GetDropRate();
		if (Random.get((int) baseRate) != 0)
			return;
		if (player.playTime / (24 * 3600) > 1)
			return;
		LootTable table = new LootTable().addTable(1,
				new LootItem(25590, 1, 1, 1),
				new LootItem(PERK_POINT_SCROLL, 1, 1, 1),
				new LootItem(ItemID.SMALL_EXP_LAMP, 1, 1, 1),
				new LootItem(ItemID.DULL_MINERALS, 5, 30, 1),
				new LootItem(ItemID.SHINY_MINERALS, 2, 18, 1));
		Item item = table.rollItem();
		this.dropItem(item, player);
	}

	/**
	 * Following
	 */
	public final void follow0() {
		checkAggression();
		// npc.getDef().ignoreOccupiedTiles = true; // TODO Configure
		// ignoreOccupiedTiles
		if (target == null || npc.isLocked()) // why can an npc be locked but still have a target.. hmm..
			return;
		if (!canAttack(target)) {
			reset();
			return;
		}
		follow();
	}

	protected void follow(int distance) {
		DumbRoute.step(npc, target, distance);
	}

	protected boolean withinDistance(int distance) {
		return target != null && DumbRoute.withinDistance(npc, target, distance);
	}

	/**
	 * Attacking
	 */
	public final void attack0() {
		if (target == null || hasAttackDelay() || npc.isLocked() || !attack())
			return;
		updateLastAttack(info.attack_ticks);
	}

	public boolean canAttack(Entity target) {
		if (isDead()) {
			return false;
		}

		if (target == null || target.isHidden()) {
			return false;
		}

		if (target.getCombat() == null) {
			return false;
		}

		if (!RevCaves.allowNPCAttack(this.npc, target)) {
			return false;
		}

		if (target.getCombat().isDead()) {
			return false;
		}

		if (!multiCheck(target)) {
			return false;
		}

		if (npc._targetPlayer == null) {
			if (!npc.getPosition().isWithinDistance(target.getPosition()))
				return false;
			Bounds attackBounds = npc.attackBounds;
			if (attackBounds != null && !npc.getPosition().inBounds(attackBounds)) {
				DumbRoute.route(npc, npc.spawnPosition.getX(), npc.spawnPosition.getY());
				// possibly consider resetting the monster to prevent abusing this mechanic
				return false;
			}
		}
		return true;
	}

	public boolean multiCheck(Entity target) {
		final Bounds argentavisLair = new Bounds(2131, 5523, 2159, 5546, 3);
		List<Integer> regionIds = Arrays.asList(
				10063,
				9808,
				14745,
				10064);

		if (regionIds.contains(npc.getPosition().regionId())) {
			return true;
		}

		if (target.player != null) {
			if (target.player.gauntlet != null) {
				if (target.player.gauntlet.inGauntlet) {
					return true;
				}
			}
		}

		if (target.getPosition().inBounds(argentavisLair)) {
			return true;
		}

		if (target.getPosition().getRegion().id == 10056) {
			return true;
		}

		if (npc != null && npc.getDef() != null) {

			if (npc.getDef().name.contains("Dusk") || npc.getDef().name.contains("Dawn")
					|| npc.getDef().name.contains("Zebak") || npc.getDef().name.contains("akkha")
					|| npc.getDef().name.contains("Core") || npc.getDef().name.contains("Elidinis")
					|| npc.getDef().name.contains("Tumeken"))
				return true;

			if (npc.getId() == 763 || npc.getId() == 762 || npc.getId() == 8367 || npc.getId() == 11778
					|| npc.getId() == 11783 || npc.getId() == 11782 || npc.getId() == 11797 || npc.getId() == 11794
					|| npc.getId() == 12223 || npc.getId() == 12226 || npc.getId() == 11790 || npc.getId() == 11792
					|| npc.getId() == 13668 || npc.getId() == 13670 || npc.getId() == 13671 || npc.getId() == 13672
					|| npc.getId() == 13673 || npc.getId() == 13674 || npc.getId() == 13675 || npc.getId() == 7223
					|| npc.getId() == 7221
					|| npc.getId() == 11779 || npc.getId() == 11783 || npc.getId() == 11782 || npc.getId() == 11723
					|| npc.getId() == 11727 || npc.getId() == 11728 || npc.getId() == 11729 || npc.getId() == 11726
					|| npc.getId() == 11725 || npc.getId() == 11724 || npc.getId() == 11719)
				return true;

			if (npc.getDef().name.toLowerCase().contains("phantom muspah"))
				return true;

			if (npc.getId() == 12336 || npc.getId() == 11774 || npc.getId() == 11775 || npc.getId() == 11772
					|| npc.getId() == 11761 || npc.getId() == 763 || npc.getId() == 762 || npc.getId() == 6477
					|| npc.getId() == 11903 || npc.getId() == 7903)
				return true;

		}

		if (npc.inMulti()) {
			return true;
		}

		return target.getCombat().allowPj(npc);
	}

	public Hit basicAttack() {
		return basicAttack(info.attack_animation, info.attack_style, info.max_damage);
	}

	protected Hit basicAttack(int animation, AttackStyle attackStyle, int maxDamage) {
		npc.animate(animation);
		defendAnim();
		Hit hit = new Hit(npc, attackStyle, null).randDamage(maxDamage);
		if (target != null) {
			faceTarget();
			target.hit(hit);
		}
		return hit;
	}

	protected Hit projectileAttack(Projectile projectile, int animation, AttackStyle attackStyle, int maxDamage) {
		return projectileAttack(projectile, animation, Graphic.builder().id(-1).build(), attackStyle, maxDamage);
	}

	protected Hit projectileAttack(Projectile projectile, int animation, Graphic hitgfx, AttackStyle attackStyle,
			int maxDamage) {
		return projectileAttack(projectile, animation, hitgfx, Graphic.builder().id(-1).build(), attackStyle, maxDamage,
				false);
	}

	protected Hit projectileAttack(Projectile projectile, int animation, Graphic hitgfx, Graphic splashgfx,
			AttackStyle attackStyle, int maxDamage, boolean ignorePrayer) {
		if (animation != -1)
			npc.animate(animation);
		projectile.send(npc, target);
		final int distance = PlayerCombat.getChebyshevDistance(npc, target);
		final int delayTicks = attackStyle.isRanged() ? PlayerCombat.getBowHitDelay(distance)
				: PlayerCombat.getMagicHitDelay(distance);
		Hit hit = new Hit(npc, attackStyle, null).randDamage(maxDamage).delay(delayTicks);
		hit.afterPostDamage(e -> {
			boolean splash = hit.isBlocked();
			if (target != null) {
				target.graphics(
						splash ? splashgfx.getId() : hitgfx.getId(),
						splash ? splashgfx.getHeight() : hitgfx.getHeight(),
						splash ? splashgfx.getDelay() : hitgfx.getDelay());
				if (splash ? splashgfx.getSoundId() != -1 : hitgfx.getSoundId() != -1) {
					target.publicSound(
							splash ? splashgfx.getSoundId() : hitgfx.getSoundId(),
							splash ? splashgfx.getSoundType() : hitgfx.getSoundType(),
							splash ? splashgfx.getSoundDelay() : hitgfx.getSoundDelay());
				}
			}
		});

		target.hit(hit);
		if (attackStyle == null || !attackStyle.isMagical()) {
			defendAnimTicks(delayTicks - 1);
		}
		return hit;
	}

	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		npc.faceNone(!isDead());
		TargetRoute.reset(npc);
	}

	protected double getPetDonatorBoost(Player player) {
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

	protected boolean timerStarted = false;

	/**
	 * Death
	 */
	@Override
	public void startDeath(Hit killHit) {
		if (npc.getId() == 11772) {
			World.startEvent(e -> {
				npc.deathStartListener.handle(npc, getKiller(), killHit);
				e.delay(1);
				npc.deathEndListener.handle(npc, getKiller(), killHit);
			});
			return;
		}
		setDead(true);
		if (target != null)
			reset();
		Killer killer = getKiller();
		if (npc.getId() == 7221) {
			if (killer.player.scurriusTimer != null) {
				killer.player.scurriusBestTime = killer.player.scurriusTimer.stop(killer.player,
						killer.player.scurriusBestTime);
			}
			timerStarted = false;
		}
		if (npc.deathStartListener != null) {
			npc.deathStartListener.handle(npc, killer, killHit);
			if (npc.isRemoved())
				return;
		}
		World.startEvent(event -> {
			try {
				npc.lock();
				event.delay(1);

				if (info.death_animation != -1) {
					npc.animate(info.death_animation);
				}

				if (info.death_ticks > 0) {
					event.delay(info.death_ticks);
				}

				if (killer != null) {
					handleNewDrop(killer.player, npc.getId(), npc.getPosition());
				}

				if (killer != null && killer.player != null) {
					if (SuperiorSlayer.isSuperiorCreature(npc.getId())) {
						killer.player.superiorCreatureKills.increment(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.SUPERIOR_CREATURE_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.SUPERIOR_CREATURE_NOVICE.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.SUPERIOR_CREATURE_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					DailyTasks.handleTaskKill(killer.player, npc.getId());
					PerkTaskHandler.handleMonsterKill(killer.player, npc.getId());
					PerkTaskHandler.handleMonsterKill(killer.player, npc.getDef().name.toLowerCase());
					DailyTasks.handleTaskDecrement(killer.player, npc.getId());
					DailyTasks.handleTaskDecrement(killer.player, npc.getDef().name.toLowerCase());

					var killerPerks = killer.player.getPlayerPerkHandler();

					if (killerPerks.getActivePerks(killer.player).contains(Perks.VANQUISHED_FOE)) {
						int perkIndex = killer.player.getPlayerPerkHandler().getActivePerkIndex(killer.player,
								Perks.VANQUISHED_FOE);
						VanquishedFoe c = (VanquishedFoe) killer.player.getPlayerPerkHandler().getActivePerks(killer.player)
								.get(perkIndex).getPerk(killer.player);
						if (killer.player.getHp() > 0) {
							killer.player.hit(new Hit(HitType.HEAL).fixedDamage((int) (npc.getMaxHp() * c.getHealAmount())));
						}
					}
					SlayerMaster.handle(killer.player, npc);
					if (npc.getDef().name.equalsIgnoreCase("Catalyst Brute")
							|| npc.getDef().name.equalsIgnoreCase("Catalyst Mager")
							|| npc.getDef().name.equalsIgnoreCase("Catalyst Ranger")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.CATALYST_CONVERTER.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					if (npc.getId() == 8063) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.ZOMBIE_DESTROYER.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					if (npc.getDef().name.equalsIgnoreCase("Elder Chaos Druid")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.ELDER_CHAOS_DRUID_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					if (npc.getDef().name.equalsIgnoreCase("Chaos Fanatic")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.CHAOS_FANATIC_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(
										killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.CHAOS_FANATIC_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					if (npc.getDef().name.equalsIgnoreCase("Dagannoth Rex")) {
						killer.player.recentDkKills++;
						killer.player.recentDkKills2++;
						if (killer.player.recentDkKills >= 2) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(
											killer.player
													.getCombatAchievementIndexByOrdinal(CombatAchievements.TOPPLING_THE_DIARCHY.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (killer.player.recentDkKills2 >= 3) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.RAPID_SUCCESSION.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						World.startEvent(e -> {
							e.delay(3);
							killer.player.recentDkKills--;
						});
						World.startEvent(e -> {
							e.delay(15);
							killer.player.recentDkKills2--;
						});
						boolean primeTargettingPlayer = false;
						boolean supremeTargettingPlayer = false;
						for (NPC n : npc.localNpcs()) {
							if (n.getCombat() instanceof DagannothPrime prime) {
								String targetName = prime.getLastPlayerAttacked();
								if (targetName.equalsIgnoreCase(killer.player.getName()) && prime.getLastAttacked() < 7) {
									primeTargettingPlayer = true;
								}
							} else if (n.getCombat() instanceof DagannothSupreme) {
								String targetName = n.getCombat().target == null ? "" : n.getCombat().target.player.getName();
								if (targetName.equalsIgnoreCase(killer.player.getName())) {
									supremeTargettingPlayer = true;
								}
							}
						}
						if (supremeTargettingPlayer && primeTargettingPlayer) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(CombatAchievements.DEATH_TO_THE_WARRIOR_KING.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}

					}
					if (npc.getDef().name.equalsIgnoreCase("Dagannoth Prime")) {
						killer.player.recentDkKills++;
						killer.player.recentDkKills2++;
						if (killer.player.recentDkKills >= 2) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(
											killer.player
													.getCombatAchievementIndexByOrdinal(CombatAchievements.TOPPLING_THE_DIARCHY.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (killer.player.recentDkKills2 >= 3) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.RAPID_SUCCESSION.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						World.startEvent(e -> {
							e.delay(3);
							killer.player.recentDkKills--;
						});
						World.startEvent(e -> {
							e.delay(15);
							killer.player.recentDkKills2--;
						});
						boolean rexTargettingPlayer = false;
						boolean supremeTargettingPlayer = false;
						for (NPC n : npc.localNpcs()) {
							if (n.getCombat() instanceof DagannothRex) {
								String targetName = n.getCombat().target == null ? "" : n.getCombat().target.player.getName();
								if (targetName.equalsIgnoreCase(killer.player.getName())) {
									rexTargettingPlayer = true;
								}
							} else if (n.getCombat() instanceof DagannothSupreme) {
								String targetName = n.getCombat().target == null ? "" : n.getCombat().target.player.getName();
								if (targetName.equalsIgnoreCase(killer.player.getName())) {
									supremeTargettingPlayer = true;
								}
							}
						}
						if (supremeTargettingPlayer && rexTargettingPlayer) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(CombatAchievements.DEATH_TO_THE_SEER_KING.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
					}
					if (npc.getDef().name.equalsIgnoreCase("Dagannoth Supreme")) {
						killer.player.recentDkKills++;
						killer.player.recentDkKills2++;
						if (killer.player.recentDkKills2 >= 3) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.RAPID_SUCCESSION.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (killer.player.recentDkKills >= 2) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(
											killer.player
													.getCombatAchievementIndexByOrdinal(CombatAchievements.TOPPLING_THE_DIARCHY.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						World.startEvent(e -> {
							e.delay(3);
							killer.player.recentDkKills--;
						});
						World.startEvent(e -> {
							e.delay(15);
							killer.player.recentDkKills2--;
						});
						boolean primeTargettingPlayer = false;
						boolean rexTargettingPlayer = false;

						for (NPC n : killer.player.localNpcs()) {
							if (n.getCombat() instanceof DagannothPrime prime) {
								String targetName = prime.getLastPlayerAttacked();
								if (targetName.equalsIgnoreCase(killer.player.getName()) && prime.getLastAttacked() < 7) {
									primeTargettingPlayer = true;
								}
							} else if (n.getCombat() instanceof DagannothRex) {
								String targetName = n.getCombat().target == null ? "" : n.getCombat().target.player.getName();
								if (targetName.equalsIgnoreCase(killer.player.getName())) {
									rexTargettingPlayer = true;
								}
							}
						}
						if (primeTargettingPlayer && rexTargettingPlayer) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(CombatAchievements.DEATH_TO_THE_ARCHER_KING.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}

					}
					if (npc.getDef().name.equalsIgnoreCase("Corporeal beast")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.CORPOREAL_BEAST_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(
										killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.CORPOREAL_BEAST_MASTER.ordinal()))
								.getCombatAchievement()).check(killer.player);
						CorporealBeast corporealBeast = (CorporealBeast) npc.getCombat();
						if (corporealBeast.hitters.size() == 1) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.CHICKEN_KILLER.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (corporealBeast.hotOnYourFeet) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.HOT_ON_YOUR_FEET.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}

					}
					if (npc.getDef().name.equalsIgnoreCase("Commander Zilyana")) {
						if (killer.player.privateZilyanaInstance) {
							killer.player.privateZilyKills++;
							if (killer.player.privateZilyKills >= 50) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.PEACH_CONJURER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						if (!killer.player.attackedZilyana && killer.player.privateZilyanaInstance) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.MOVING_COLLATERAL.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						killer.player.attackedZilyana = false;
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.COMMANDER_ZILYANA_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.COMMANDER_ZILYANA_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.COMMANDER_ZILYANA_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
						CommanderZilyana zilyana = (CommanderZilyana) npc.getCombat();
						if (!zilyana.attackedWithNonMelee) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.REMINISCE.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!zilyana.animalWhispererFailed && killer.player.privateZilyanaInstance) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.ANIMAL_WHISPERER.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						boolean minionsAllDead = true;
						for (NPC n : npc.localNpcs()) {
							if (n.getId() >= 2206 && n.getId() < 2209 && n.getHp() > 0) {
								minionsAllDead = false;
								break;
							}
						}
						if (minionsAllDead) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(
											killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.COMMANDER_SHOWDOWN.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
					}
					if (npc.getDef().name.equalsIgnoreCase("Snakeling")) {
						killer.player.recentSnakelingKills++;
						if (killer.player.recentSnakelingKills >= 3) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.SNAKE_SNAKE_SNAKE.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						World.startEvent(e -> {
							killer.player.recentSnakelingKills--;
						});
					}
					if (npc.getDef().name.equalsIgnoreCase("Zulrah")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.ZULRAH_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.ZULRAH_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.ZULRAH_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.ZULRAH_MASTER.ordinal()))
								.getCombatAchievement()).check(killer.player);
						if (ActivityTimer.timeInSeconds(killer.player.zulrahBestTime) < 30) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("Zulrah");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.ZULRAH_SPEED_TRIALIST.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						if (ActivityTimer.timeInSeconds(killer.player.zulrahBestTime) < 15) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("Zulrah");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.ZULRAH_SPEED_CHASER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						if (ActivityTimer.timeInSeconds(killer.player.zulrahBestTime) < 8) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("Zulrah");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.ZULRAH_SPEED_RUNNER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						Zulrah zulrah = (Zulrah) npc.getCombat();
						if (!zulrah.damagedPlayer) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_ZULRAH.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
					}
					if (npc.getDef().name.equalsIgnoreCase("The Whisperer")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.THE_WHISPERER_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(
										killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.THE_WHISPERER_MASTER.ordinal()))
								.getCombatAchievement()).check(killer.player);
						if (killer.player.whispererTimer != null)
							killer.player.whispererBestTime = killer.player.whispererTimer.stop(killer.player,
									killer.player.whispererBestTime);
						if (ActivityTimer.timeInSeconds(killer.player.whispererBestTime) < 50) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("The Whisperer");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.THE_WHISPERER_SPEED_TRIALIST.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						Whisperer whisperer = (Whisperer) npc.getCombat();
						if (!whisperer.damagedPlayer) {
							killer.player.perfectWhispererKills++;
							if (killer.player.perfectWhispererKills >= 5) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_WHISPERER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						} else {
							killer.player.perfectWhispererKills = 0;
						}
						if (ActivityTimer.timeInSeconds(killer.player.whispererBestTime) < 40) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("The Whisperer");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.THE_WHISPERER_SPEED_CHASER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						if (ActivityTimer.timeInSeconds(killer.player.whispererBestTime) < 30) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("The Whisperer");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.THE_WHISPERER_SPEED_RUNNER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
					}
					if (npc.getDef().name.equalsIgnoreCase("Vorkath")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.VORKATH_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.VORKATH_MASTER.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Vorkath vorkath = (Vorkath) npc.getCombat();
						if (!vorkath.usedNonMelee) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(CombatAchievements.STICK_EM_WITH_THE_POINTY_END.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!vorkath.usedNonFist) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.THE_FREMENNIK_WAY.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (killer.player.vorkathTimer != null)
							killer.player.vorkathBestTime = killer.player.vorkathTimer.stop(killer.player,
									killer.player.vorkathBestTime);
						if (ActivityTimer.timeInSeconds(killer.player.vorkathBestTime) < 20) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("Vorkath");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.VORKATH_SPEED_CHASER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						if (ActivityTimer.timeInSeconds(killer.player.vorkathBestTime) < 12) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("Vorkath");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.VORKATH_SPEED_RUNNER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						killer.player.privateVorkathKills++;
						if (killer.player.privateVorkathKills >= 25) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(
											killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.EXTENDED_ENCOUNTER.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!vorkath.damagedWithSpecial) {
							killer.player.dodgingTheDragonKills++;
							if (killer.player.dodgingTheDragonKills >= 5) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(
												killer.player
														.getCombatAchievementIndexByOrdinal(CombatAchievements.DODGING_THE_DRAGON.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						} else {
							killer.player.dodgingTheDragonKills = 0;
						}
					}
					if (npc.getDef().name.equalsIgnoreCase("Vardorvis")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.VARDORVIS_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.VARDORVIS_MASTER.ordinal()))
								.getCombatAchievement()).check(killer.player);
						if (killer.player.vardorvisTimer != null)
							killer.player.vardorvisBestTime = killer.player.vardorvisTimer.stop(killer.player,
									killer.player.vardorvisBestTime);

						Vardorvis vardorvis = (Vardorvis) npc.getCombat();
						if (!vardorvis.damagedPlayer) {
							killer.player.perfectVardorvisKills++;
							if (killer.player.perfectVardorvisKills >= 5) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_VARDORVIS.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						} else {
							killer.player.perfectVardorvisKills = 0;
						}
						if (ActivityTimer.timeInSeconds(killer.player.vardorvisBestTime) < 45) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("Vardorvis");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.VARDORVIS_SPEED_TRIALIST.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						if (ActivityTimer.timeInSeconds(killer.player.vardorvisBestTime) < 35) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("Vardorvis");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.VARDORVIS_SPEED_CHASER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						if (ActivityTimer.timeInSeconds(killer.player.vardorvisBestTime) < 25) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("Vardorvis");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.VARDORVIS_SPEED_RUNNER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
					}
					if (npc.getDef().name.equalsIgnoreCase("The Leviathan")) {
						if (killer.player.leviathanTimer != null)
							killer.player.leviathanBestTime = killer.player.leviathanTimer.stop(killer.player,
									killer.player.leviathanBestTime);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.LEVIATHAN_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.LEVIATHAN_MASTER.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Leviathan leviathan = (Leviathan) npc.getCombat();
						if (!leviathan.damagedPlayer) {
							killer.player.perfectLeviathanKills++;
							if (killer.player.perfectLeviathanKills >= 5) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_LEVIATHAN.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						} else {
							killer.player.perfectLeviathanKills = 0;
						}
						if (ActivityTimer.timeInSeconds(killer.player.leviathanBestTime) < 60) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("The Leviathan");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.LEVIATHAN_SPEED_TRIALIST.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						if (ActivityTimer.timeInSeconds(killer.player.leviathanBestTime) < 45) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("The Leviathan");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.LEVIATHAN_SPEED_CHASER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						if (ActivityTimer.timeInSeconds(killer.player.leviathanBestTime) < 35) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("The Leviathan");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.LEVIATHAN_SPEED_RUNNER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
					}
					if (npc.getDef().name.equalsIgnoreCase("Kree'arra")) {
						if (!killer.player.attackedKree && killer.player.privateKreeInstance) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.COLLATERAL_DAMAGE.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (killer.player.privateKreeInstance) {
							KreeArra kreeArra = (KreeArra) npc.getCombat();
							if (!kreeArra.attackedWithMelee) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.SWOOP_NO_MORE.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						if (killer.player.privateKreeInstance) {
							killer.player.privateKreeKills++;
							if (killer.player.privateKreeKills >= 50) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.FEATHER_HUNTER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						killer.player.attackedKree = false;
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.KREEARRA_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.KREEARRA_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.KREEARRA_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
						boolean minionsAllDead = true;
						for (NPC n : npc.localNpcs()) {
							if (n.getId() >= 3163 && n.getId() < 3166 && n.getHp() > 0) {
								minionsAllDead = false;
								break;
							}
						}
						if (minionsAllDead) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.AIRBORNE_SHOWDOWN.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}

					}

					if (npc.getDef().name.equalsIgnoreCase("Warped Jelly")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.JELLY_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					if (npc.getDef().name.equalsIgnoreCase("K'ril Tsutsaroth")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.KRIL_TSUTSAROTH_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.KRIL_TSUTSAROTH_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.KRIL_TSUTSAROTH_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
						if (killer.player.getEquipment().get(Equipment.SLOT_WEAPON) != null
								&& killer.player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == ARCLIGHT) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(
											killer.player
													.getCombatAchievementIndexByOrdinal(CombatAchievements.DEMONBANE_WEAPONRYII.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (killer.player.privateKrilInstance) {
							killer.player.privateKrilKills++;
							if (killer.player.privateKrilKills >= 50) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.ASH_COLLECTOR.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						KrilTsutsaroth tsutsaroth = (KrilTsutsaroth) npc.getCombat();
						if (!tsutsaroth.bodyGuardsHitPlayer && killer.player.privateKrilInstance) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.DEMON_WHISPERER.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (killer.player.getPosition().getRegion().id != 11603) {
							if (!tsutsaroth.usedMelee) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.DEMONIC_DEFENCE.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						if (!tsutsaroth.usedSpecial) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.YARR_NO_MORE.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						boolean minionsAllDead = true;
						for (NPC n : npc.localNpcs()) {
							if (n.getId() >= 3130 && n.getId() < 3133 && n.getHp() > 0) {
								minionsAllDead = false;
								break;
							}
						}
						if (minionsAllDead) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.DEMONIC_SHOWDOWN.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
					}
					if (npc.getDef().name.equalsIgnoreCase("General Graardor")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.GENERAL_GRAARDOR_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.GENERAL_GRAARDOR_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(
										killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.GENERAL_GRAARDOR_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						GeneralGraardor graardor = (GeneralGraardor) npc.getCombat();
						if (killer.player.privateBandosInstance) {
							killer.player.privateBandosKills++;
							if (killer.player.privateBandosKills >= 50) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.OURG_KILLER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						if (!graardor.hitPlayer) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.OURG_FREEZER_II.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!graardor.keepAwayFailed && killer.player.privateBandosInstance) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.KEEP_AWAY.ordinal()))
									.getCombatAchievement()).check(killer.player);
							killer.player.keepAwayKills++;
							if (killer.player.keepAwayKills >= 2) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.DEFENCE_MATTERS.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						} else
							killer.player.keepAwayKills = 0;
						if (npc.isFrozen()) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.OURG_FREEZER.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						boolean minionsAllDead = true;
						for (General.Lieutenant n : graardor.getLieutenants()) {
							if (n.npc.getHp() > 0) {
								minionsAllDead = false;
								break;
							}
						}
						if (minionsAllDead) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.GENERAL_SHOWDOWN.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
					}
					if (npc.getDef().name.equalsIgnoreCase("Chaos Elemental")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.CHAOS_ELEMENTAL_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.CHAOS_ELEMENTAL_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.CHAOS_ELEMENTAL_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);

						ChaosElemental t = (ChaosElemental) npc.getCombat();
						if (!t.damagedPlayer) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.THE_FLINCHER.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.unequippedItem) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.HOARDER.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
					}
					if (npc.getDef().name.equalsIgnoreCase("Armoured Zombie")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.ARMOURED_ZOMBIE_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					if (npc.getDef().name.equalsIgnoreCase("Basilisk Knight")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.BASILISK_KNIGHT_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					if (npc.getDef().name.equalsIgnoreCase("Kalphite Queen")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(
										killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.KALPHITE_QUEEN_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.KALPHITE_QUEEN_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(
										killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.KALPHITE_QUEEN_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
						if (npc.getId() != 6501) { // since the Echo variant has the same name
							KalphiteQueen t = (KalphiteQueen) npc.getCombat();
							if (!t.usedNonFlail) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.PRAYER_SMASHER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
							if (t.defenceLowered) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.CHITIN_PENETRATOR.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
					}
					if (npc.getDef().name.equalsIgnoreCase("Vyrewatch Sentinel")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.VYREWATCH_SENTINEL_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					if (npc.getDef().name.equalsIgnoreCase("Dusk")) {
						killer.player.privateGrotesqueGuardianKills++;
						if (killer.player.privateGrotesqueGuardianKills >= 10) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.FROM_DUSK.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (killer.player.privateGrotesqueGuardianKills >= 20) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.TIL_DAWN.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (killer.player.grotesqueGuardiansTimer != null)
							killer.player.grotesqueGuardiansBestTime = killer.player.grotesqueGuardiansTimer.stop(killer.player,
									killer.player.grotesqueGuardiansBestTime);
						if (ActivityTimer.timeInSeconds(killer.player.grotesqueGuardiansBestTime) < 60) {
							Objects
									.requireNonNull(
											killer.player.combatAchievementsList.get(killer.player.getCombatAchievementIndexByOrdinal(
													CombatAchievements.GROTESQUE_GUARDIANS_SPEED_TRIALIST.ordinal())).getCombatAchievement())
									.check(killer.player);
						}
						if (ActivityTimer.timeInSeconds(killer.player.grotesqueGuardiansBestTime) < 45) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(
													CombatAchievements.GROTESQUE_GUARDIANS_SPEED_CHASER.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (ActivityTimer.timeInSeconds(killer.player.grotesqueGuardiansBestTime) < 35) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(
													CombatAchievements.GROTESQUE_GUARDIANS_SPEED_RUNNER.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.GROTESQUE_GUARDIANS_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.GROTESQUE_GUARDIANS_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.GROTESQUE_GUARDIANS_NOVICE.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.GROTESQUE_GUARDIANS_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Dusk t = (Dusk) npc.getCombat();
						if (!t.damagedByRock) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.GRANITE_FOOTWORK.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
					}
					if (npc.getDef().name.equalsIgnoreCase("Malakar")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.MALAKAR_MASTER.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					if (npc.getDef().name.equalsIgnoreCase("Ophidia")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.OPHIDIA_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.OPHIDIA_MASTER.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.OPHIDIA_GRANDMASTER.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Tarn t = (Tarn) npc.getCombat();
						if (t.activeSnakelings.size() >= 2) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(CombatAchievements.HEALING_WONT_SAVE_YOU.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.damagedPlayer) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_OPHIDIA.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}

						t.activeSnakelings.forEach(n -> {
							n.remove();
						});
						t.activeSnakelings.clear();
					}
					if (npc.getDef().name.equalsIgnoreCase("Galvek")) {
						if (killer.player.galvekTimer != null)
							killer.player.galvekBestTime = killer.player.galvekTimer.stop(killer.player,
									killer.player.galvekBestTime);
						if (ActivityTimer.timeInSeconds(killer.player.galvekBestTime) <= 120) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("Galvek");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.GALVEK_SPEED_TRIALIST.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.GALVEK_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.GALVEK_MASTER.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.GALVEK_GRANDMASTER.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					if (npc.getDef().name.equalsIgnoreCase("Alchemical Hydra")) {
						killer.player.hydraInstanceKills++;
						if (killer.player.hydraInstanceKills >= 30) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.WORKING_OVERTIME.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (killer.player.alchemicalHydraTimer != null)
							killer.player.alchemicalHydraBestTime = killer.player.alchemicalHydraTimer.stop(killer.player,
									killer.player.alchemicalHydraBestTime);
						if (ActivityTimer.timeInSeconds(killer.player.alchemicalHydraBestTime) < 45) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(CombatAchievements.ALCHEMICAL_HYDRA_SPEED_TRIALIST.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (ActivityTimer.timeInSeconds(killer.player.alchemicalHydraBestTime) < 30) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(CombatAchievements.ALCHEMICAL_HYDRA_SPEED_RUNNER.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						AlchemicalHydra t = (AlchemicalHydra) npc.getCombat();
						if (!t.noPressureFailed) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.NO_PRESSURE.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.damagedByPoison) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(CombatAchievements.UNREQUIRED_ANTIPOISONS.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.damagedPlayer) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.ALCLEANICAL_HYDRA.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.damagedByLightning) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.LIGHTNING_LURE.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.flameSpawned) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.THE_FLAME_SKIPPER.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.empowered) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.MIXING_CORRECTLY.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.hitByFlame) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.DONT_FLAME_ME.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(
										killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.ALCHEMICAL_HYDRA_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.ALCHEMICAL_HYDRA_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.ALCHEMICAL_MASTER.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					if (npc.getDef().name.equalsIgnoreCase("Jal-Zek")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.HALFWAY_THERE.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					if (npc.getDef().name.equalsIgnoreCase("Abyssal Sire")) {
						AbyssalSire t = (AbyssalSire) npc.getCombat();
						if (!t.damagedByMiasma) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.DONT_STOP_MOVING.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (t.totalStuns < 2) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(
											killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.RESPIRATORY_RUNNER.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.scionsMatured) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(CombatAchievements.THEY_GROW_UP_TOO_FAST.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.damagedByExplosion && !t.damagedOffPrayer && !t.damagedByMiasma && t.damageWithTentacle) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_SIRE.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.damageWithTentacle) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.DONT_WHIP_ME.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.ABYSSAL_SIRE_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.ABYSSAL_SIRE_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(
										killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.ABYSSAL_SIRE_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					if (npc.getDef().name.equalsIgnoreCase("Demonic gorilla")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.DEMONIC_GORILLA_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(
										killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.DEMONIC_GORILLA_NOVICE.ordinal()))
								.getCombatAchievement()).check(killer.player);
						if (killer.player.getEquipment().get(Equipment.SLOT_WEAPON) != null
								&& killer.player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == ARCLIGHT) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(CombatAchievements.HITTING_THEM_WHERE_IT_HURTS.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
					}
					if (npc.getDef().name.equalsIgnoreCase("Callisto")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.CALLISTO_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.CALLISTO_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.CALLISTO_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					if (npc.getDef().name.equalsIgnoreCase("Vet'ion")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.VETION_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.VETION_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.VETION_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}

					if (npc.getDef().name.equalsIgnoreCase("Tekton")) {
						Tekton t = (Tekton) npc.getCombat();
						if (!t.damagedPlayer) {
							npc.getPosition().getRegion().players.forEach(p -> {
								Objects.requireNonNull(p.combatAchievementsList
										.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.BLIND_SPOT.ordinal()))
										.getCombatAchievement()).check(p);
							});
						}
						if (!t.returnedToAnvil) {
							npc.getPosition().getRegion().players.forEach(p -> {
								Objects.requireNonNull(p.combatAchievementsList
										.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.ANVIL_NO_MORE.ordinal()))
										.getCombatAchievement()).check(p);
							});
						}
					}
					if (npc.getDef().name.equalsIgnoreCase("Cerberus")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.CERBERUS_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.CERBERUS_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.CERBERUS_MASTER.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.CERBERUS_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
						if (npc.getId() != 17000) {
							Cerberus t = (Cerberus) npc.getCombat();
							if (!t.damagedByGhosts) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.GHOST_BUSTER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
							if (!t.damagedByLava) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.UNREQUIRED_ANTIFIRE.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
							if (!t.ghostsSpawned) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.AROO_NO_MORE.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
							if (t.ghostsBlocked >= 6) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.SOUL_NEGATION.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
					}
					if (npc.getDef().name.equalsIgnoreCase("Scorpia")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.SCORPIA_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.SCORPIA_NOVICE.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.SCORPIA_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Scorpia t = (Scorpia) npc.getCombat();
						if (!t.dealtDamage) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.I_CANT_REACH_THAT.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.killedAnyGuardians) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.GUARDIANS_NO_MORE.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
					}
					if (npc.getDef().name.equalsIgnoreCase("Venenatis")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.VENENATIS_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.VENENATIS_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.VENENATIS_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					if (npc.getDef().name.equalsIgnoreCase("Crazy archaeologist")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.CRAZY_ARCHAEOLOGIST_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					if (npc.getDef().name.equalsIgnoreCase("Phantom Muspah")) {
						killer.player.privatePhantomMuspahKills++;
						if (killer.player.privatePhantomMuspahKills >= 10) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.ESSENCE_FARMER.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (killer.player.phantomMuspahTimer != null)
							killer.player.phantomMuspahBestTime = killer.player.phantomMuspahTimer.stop(killer.player,
									killer.player.phantomMuspahBestTime);
						if (ActivityTimer.timeInSeconds(killer.player.phantomMuspahBestTime) < 60) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("Phantom Muspah");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.PHANTOM_MUSPAH_SPEED_TRIALIST.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						if (ActivityTimer.timeInSeconds(killer.player.phantomMuspahBestTime) < 45) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("Phantom Muspah");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.PHANTOM_MUSPAH_SPEED_CHASER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						if (ActivityTimer.timeInSeconds(killer.player.phantomMuspahBestTime) < 35) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("Phantom Muspah");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.PHANTOM_MUSPAH_SPEED_RUNNER.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						PhantomMuspah t = (PhantomMuspah) npc.getCombat();
						if (!t.damagedPlayer) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(CombatAchievements.WALK_STRAIGHT_PRAY_TRUE.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.targetUsedRun) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.TAKING_IT_SLOW.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(
										killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.PHANTOM_MUSPAH_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.PHANTOM_MUSPAH_NOVICE.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.PHANTOM_MUSPAH_MASTER.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}
					if (npc.getDef().name.equalsIgnoreCase("Kraken")) {
						if (killer.player.getPosition().getRegion().id != 9116) {
							killer.player.privateKrakenKills++;
							if (killer.player.privateKrakenKills >= 50) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.TENTACLES.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
							if (killer.player.privateKrakenKills >= 100) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.ONE_HUNDRED_TENTACLES.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.KRAKEN_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.KRAKEN_NOVICE.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.KRAKEN_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Kraken kraken = (Kraken) npc.getCombat();
						if (kraken.allTentaclesDead) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(CombatAchievements.UNNECESSARY_OPTIMISATION.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}

					}
					if (npc.getDef().name.equalsIgnoreCase("Greater demon")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.GREATER_DEMON_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
					} else if (npc.getDef().name.equalsIgnoreCase("Fire giant")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.FIRE_GIANT_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
					} else if (npc.getDef().name.equalsIgnoreCase("Black dragon")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.BLACK_DRAGON_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
					} else if (npc.getDef().name.equalsIgnoreCase("Hellhound")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.HELLHOUND_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
					} else if (npc.getDef().name.equalsIgnoreCase("Hydra")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.HYDRA_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
					} else if (npc.getDef().name.equalsIgnoreCase("Brutal Black Dragon")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.INTO_THE_BRUTAL_DARKNESS.ordinal()))
								.getCombatAchievement()).check(killer.player);
					} else if (npc.getDef().name.equalsIgnoreCase("Thermonuclear smoke devil")) {
						ThermonuclearSmokeDevil t = (ThermonuclearSmokeDevil) npc.getCombat();
						if (!t.damagedPlayer) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.HAZARD_PREVENTION.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.damagedByNonSpecial) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.SPECD_OUT.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.playerAboveZeroPrayerPoints) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.DEVIL_NO_PRAYER.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
					} else if (npc.getDef().name.equalsIgnoreCase("Giant Mole")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.GIANT_MOLE_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.GIANT_MOLE_VETERAN.ordinal()))
								.getCombatAchievement()).check(killer.player);
						GiantMole t = (GiantMole) npc.getCombat();
						if (t.hitsTaken <= 4) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.HARD_HITTER.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.damagedPlayer) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(CombatAchievements.AVOIDING_THOSE_LITTLE_ARMS.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
					} else if (npc.getDef().name.equalsIgnoreCase("Argentavis")) {
						if (killer.player.argentavisTimer != null)
							killer.player.argentavisBestTime = killer.player.argentavisTimer.stop(killer.player,
									killer.player.argentavisBestTime);
						if (ActivityTimer.timeInSeconds(killer.player.argentavisBestTime) <= 30) {
							boolean slayerTask = killer.player.bossSlayerName != null
									&& killer.player.bossSlayerName.equalsIgnoreCase("Argentavis");
							if (!slayerTask) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.ARGENTAVIS_SPEED_TRIALIST.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.ARGENTAVIS_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.ARGENTAVIS_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Argentavis t = (Argentavis) npc.getCombat();
						if (!t.playerDamagedByNado) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(
											killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.AVOIDING_THE_STORM.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
					} else if (npc.getDef().name.equalsIgnoreCase("Dagannoth Supreme")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.DAGANNOTH_SUPREME_NOVICE.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.DAGANNOTH_SUPREME_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
					} else if (npc.getDef().name.equalsIgnoreCase("Dagannoth Rex")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(
										killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.DAGANNOTH_REX_NOVICE.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(
										killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.DAGANNOTH_REX_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						if (npc.isFrozen()) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.A_FROZEN_KING.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
					} else if (npc.getDef().name.equalsIgnoreCase("Dagannoth Prime")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(
										killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.DAGANNOTH_PRIME_NOVICE.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.DAGANNOTH_PRIME_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
					} else if (npc.getDef().name.equalsIgnoreCase("Skotizo")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.SKOTIZO_ADEPT.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.SKOTIZO_NOVICE.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Skotizo t = (Skotizo) npc.getCombat();
						int killHitWeaponId = killer.player.getEquipment() == null ? -1
								: killer.player.getEquipment().get(Equipment.SLOT_WEAPON).getId();
						boolean chinchompa = killer.player.getEquipment() == null ? false
								: killer.player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == RED_CHINCHOMPA_10034
										|| killer.player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == CHINCHOMPA_10033
										|| killer.player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == BLACK_CHINCHOMPA
										|| killer.player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 30476;
						if (chinchompa) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(CombatAchievements.PRECISE_POSITIONING.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (killHitWeaponId == ARCLIGHT || killHitWeaponId == DARKLIGHT) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(CombatAchievements.EXPOSING_HIM_TO_HIS_KRYPTONITE.ordinal()))
									.getCombatAchievement()).check(killer.player);

						}
						if (!t.damagedPlayer) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.DEMON_EVASION.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.equippedDemonBaneWeapon) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(
											killer.player
													.getCombatAchievementIndexByOrdinal(CombatAchievements.UP_FOR_THE_CHALLENGE.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (t.allAltarsDorment()) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.DEMONIC_WEAKENING.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (t.altarsKilled < 1) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(
											killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.THEY_WONT_HELP_YOU.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}

					} else if (npc.getDef().name.equalsIgnoreCase("Sarachnis")) {
						if (killHit.attackStyle == AttackStyle.CRUSH) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(
											killer.player
													.getCombatAchievementIndexByOrdinal(CombatAchievements.NEWSPAPER_ENTHUSIAST.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						Sarachnis t = (Sarachnis) npc.getCombat();
						if (!t.attackedWithRangeTwice) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.READY_TO_POUNCE.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.dealtDamage) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.INSECT_REPELLENT.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
					} else if (npc.getId() != 17011 && npc.getDef().name.equalsIgnoreCase("Sol Heredit")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.SPORTSMANSHIP.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.SOL_HEREDIT_MASTER.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.SOL_HEREDIT_GRANDMASTER.ordinal()))
								.getCombatAchievement()).check(killer.player);
						SolHeredit t = (SolHeredit) npc.getCombat();
						if (!t.usedNonSolWeapon) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(
											killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.I_BROUGHT_MINE_TOO.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.damagedPlayer) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_FOOTWORK.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.playerRan) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player
											.getCombatAchievementIndexByOrdinal(CombatAchievements.SLOW_DANCING_IN_THE_SAND.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
					} else if (npc.getId() != 17004 && npc.getDef().name.equalsIgnoreCase("King Black Dragon")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.KING_BLACK_DRAGON_NOVICE.ordinal()))
								.getCombatAchievement()).check(killer.player);
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(killer.player
										.getCombatAchievementIndexByOrdinal(CombatAchievements.KING_BLACK_DRAGON_CHAMPION.ordinal()))
								.getCombatAchievement()).check(killer.player);
						KingBlackDragon t = (KingBlackDragon) npc.getCombat();
						if (killer.player.inKbdInstance) {
							killer.player.privateKBDKills++;
							if (killer.player.privateKBDKills >= 10) {
								Objects.requireNonNull(killer.player.combatAchievementsList
										.get(killer.player
												.getCombatAchievementIndexByOrdinal(CombatAchievements.WHO_IS_THE_KING_NOW.ordinal()))
										.getCombatAchievement()).check(killer.player);
							}
						}
						if (killer.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE)) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.CLAW_CLIPPER.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (killHit.attackStyle == AttackStyle.STAB) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.HIDE_PENETRATION.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
						if (!t.playerUsedOverheads) {
							Objects.requireNonNull(killer.player.combatAchievementsList
									.get(
											killer.player
													.getCombatAchievementIndexByOrdinal(CombatAchievements.NO_PROTECTION_NEEDED.ordinal()))
									.getCombatAchievement()).check(killer.player);
						}
					}

					if (killer.player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
						if (killer.player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == LEAFBLADED_SWORD
								&& npc.getDef().name.equalsIgnoreCase("Turoth")) {
							CombatAchievementSystem.completeAchievement(killer.player, CombatAchievement.Tier.EASY,
									CombatAchievements.HOPE_THIS_ONE_WORKS);
						}
						if (killer.player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == LEAFBLADED_BATTLEAXE
								&& npc.getDef().name.equalsIgnoreCase("Kurask")) {
							CombatAchievementSystem.completeAchievement(killer.player, CombatAchievement.Tier.EASY,
									CombatAchievements.RETURNING_THE_AXE);
						}
					}
					if (npc.getDef().name.equalsIgnoreCase("cave horror")) {
						Objects.requireNonNull(killer.player.combatAchievementsList
								.get(
										killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.RETURNING_THE_HORROR.ordinal()))
								.getCombatAchievement()).check(killer.player);
					}

					BossMaster.handleBossSlayerKill(killer.player, npc);

					var weapon = killer.player.getEquipment().get(Equipment.SLOT_WEAPON);
					if (weapon != null) {
						if (AttributeExtensions.hasAttribute(weapon, AttributeTypes.SIPHON_THE_DEAD)) {
							SiphonTheDead.siphonHealth(killer.player, weapon, npc.getMaxHp());
						}
					}
					if (npc.getDef().killCounter != null) {
						npc.getDef().killCounter.apply(killer.player).increment(killer.player);
					}
					if (npc.getId() == 499) {
						killer.player.thermonuclearSmokeDevilKills.increment(killer.player);
					}

					if (info != null && info.pet != null) {
						float petDropAverage = info.pet.dropAverage;
						float dropRateBonus = 1 - (killer.player.calculateDropRate() / 100f);
						petDropAverage *= dropRateBonus;
						if (killer.player.dropRateBoostTimer.remaining() > 0)
							petDropAverage *= 0.95f;
						if (killerPerks.getActivePerks(killer.player).contains(Perks.THE_PET_HUNTER)) {
							int perkIndex = killer.player.getPlayerPerkHandler().getActivePerkIndex(killer.player,
									Perks.THE_PET_HUNTER);
							ThePetHunter c = (ThePetHunter) killer.player.getPlayerPerkHandler().getActivePerks(killer.player)
									.get(perkIndex).getPerk(killer.player);
							assert c != null;
							petDropAverage *= (float) c.getPetChanceBoost();
						}
						if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
							petDropAverage *= 0.85F;
						if (killer.player.petDropBonus.isDelayed())
							petDropAverage *= 0.8f;

						petDropAverage *= getPetDonatorBoost(killer.player);

						for (int i = 0; i < killer.player.petRolls; i++) {
							if (info.pet != null && Random.get((int) petDropAverage) == 0) {
								info.pet.unlock(killer.player, npc.getId());
								break;
							}
						}
						// DailyTask.checkNPCKill(killer.player, npc);
					}
				}
				if (npc.deathEndListener != null) {
					npc.deathEndListener.handle(npc, killer, killHit);
					if (npc.isRemoved()) {
						return;
					}
				} else if (info.respawn_ticks < 0 && npc.getId() != 8390) {
					npc.remove();
					return;
				}
				if (npc.getId() != 13668 &&
						npc.getId() != 13669 &&
						npc.getId() != 7553 &&
						npc.getId() != 7555 &&
						npc.getId() != 8390) {
					npc.remove();

					if (!allowRespawn()) {
						return;
					}

					int respawnTicks = info.respawn_ticks;
					if (respawnTicks > 30) {
						respawnTicks = 12;
					}
					event.delay(respawnTicks);
					respawn();
				}

			} catch (Exception e) {
				npc.remove();
				log.error("Unable to properly start death for an npc: " + this.npc.captureState(), e);
			}
		});
	}

	public final void respawn() {
		if (Arrays.stream(WalkerSpawnHandler.WALKERS).anyMatch(n -> n == npc.getId())) {
			this.npc.remove();
			return;
		}

		if (npc.getId() == 7852 || npc.getId() == 7884) {
			return;
		}

		combatAchievementsBullshit();

		// SANITY: sometimes could ppen, depends on external logic
		if (!npc.isRemoved()) {
			log.warn("Tried to respawn npc that was not removed. npc: " + npc.captureState(), new IllegalStateException());
			npc.remove();
		}
		npc.animate(info.spawn_animation);
		npc.getPosition().set(npc.spawnPosition);
		npc.setWeakenedDamage(0);
		npc.clearHits();
		resetKillers();
		restore();
		setDead(false);
		npc.setHidden(false);
		if (npc.respawnListener != null) {
			npc.respawnListener.onRespawn(npc);
		}
		npc.unlock();
		npc.addToWorld();
	}

	private void combatAchievementsBullshit() {
		if (npc.getCombat() instanceof CommanderZilyana) {
			((CommanderZilyana) npc.getCombat()).attackedWithNonMelee = false;
			((CommanderZilyana) npc.getCombat()).animalWhispererFailed = false;
		}
		if (npc.getCombat() instanceof Tarn) {
			((Tarn) npc.getCombat()).damagedPlayer = false;
		}
		if (npc.getCombat() instanceof Vardorvis) {
			((Vardorvis) npc.getCombat()).damagedPlayer = false;
			((Vardorvis) npc.getCombat()).timerStarted = false;
		}
		if (npc.getCombat() instanceof Leviathan) {
			((Leviathan) npc.getCombat()).unconventionalFailed = false;
			((Leviathan) npc.getCombat()).inRally = false;
			((Leviathan) npc.getCombat()).inSpecial = false;
			((Leviathan) npc.getCombat()).specialsDone = 0;
			((Leviathan) npc.getCombat()).rallyTickDelay = 3;
			((Leviathan) npc.getCombat()).totalRallyStyles = 2;
			((Leviathan) npc.getCombat()).rallyCount = 6;
			((Leviathan) npc.getCombat()).order = 1;
			((Leviathan) npc.getCombat()).damagedPlayer = false;
		}
		if (npc.getCombat() instanceof AlchemicalHydra) {
			((AlchemicalHydra) npc.getCombat()).timerStarted = false;
			((AlchemicalHydra) npc.getCombat()).resetCombatAchievementVariables();
		}
		if (npc.getCombat() instanceof Argentavis) {
			((Argentavis) npc.getCombat()).timerStarted = false;
		}
		if (npc.getCombat() instanceof Vorkath) {
			((Vorkath) npc.getCombat()).resetVariables();
		}
		if (npc.getCombat() instanceof PhantomMuspah) {
			((PhantomMuspah) npc.getCombat()).resetVariables();
		}
		if (npc.getCombat() instanceof GiantMole) {
			((GiantMole) npc.getCombat()).damagedPlayer = false;
			((GiantMole) npc.getCombat()).hitsTaken = 0;
		}
		if (npc.getCombat() instanceof GeneralGraardor) {
			((GeneralGraardor) npc.getCombat()).hitPlayer = false;
			((GeneralGraardor) npc.getCombat()).keepAwayFailed = false;
		}
		if (npc.getCombat() instanceof ThermonuclearSmokeDevil) {
			((ThermonuclearSmokeDevil) npc.getCombat()).resetCombatAchievementVariables();
		}
		if (npc.getCombat() instanceof Galvek) {
			((Galvek) npc.getCombat()).timerStarted = false;
		}
		if (npc.getCombat() instanceof AbyssalSire) {
			((AbyssalSire) npc.getCombat()).scionsMatured = false;
			((AbyssalSire) npc.getCombat()).damagedByMiasma = false;
			((AbyssalSire) npc.getCombat()).damagedByExplosion = false;
			((AbyssalSire) npc.getCombat()).damagedOffPrayer = false;
			((AbyssalSire) npc.getCombat()).damageWithTentacle = false;
			((AbyssalSire) npc.getCombat()).totalStuns = 0;
		}
		if (npc.getCombat() instanceof CorporealBeast) {
			((CorporealBeast) npc.getCombat()).hitters.clear();
			((CorporealBeast) npc.getCombat()).hotOnYourFeet = true;
		}
		if (npc.getCombat() instanceof Scorpia) {
			((Scorpia) npc.getCombat()).killedAnyGuardians = false;
			((Scorpia) npc.getCombat()).dealtDamage = false;
		}
		if (npc.getCombat() instanceof Sarachnis) {
			((Sarachnis) npc.getCombat()).attackedWithRangeTwice = false;
			((Sarachnis) npc.getCombat()).dealtDamage = false;
		}
		if (npc.getCombat() instanceof Kraken) {
			((Kraken) npc.getCombat()).allTentaclesDead = false;
		}
		if (npc.getCombat() instanceof KrilTsutsaroth) {
			((KrilTsutsaroth) npc.getCombat()).usedSpecial = false;
			((KrilTsutsaroth) npc.getCombat()).usedMelee = false;
			((KrilTsutsaroth) npc.getCombat()).bodyGuardsHitPlayer = false;
		}
		if (npc.getCombat() instanceof KalphiteQueen) {
			((KalphiteQueen) npc.getCombat()).defenceLowered = false;
			((KalphiteQueen) npc.getCombat()).usedNonFlail = false;
		}
		if (npc.getCombat() instanceof Cerberus) {
			((Cerberus) npc.getCombat()).damagedByGhosts = false;
			((Cerberus) npc.getCombat()).damagedByLava = false;
			((Cerberus) npc.getCombat()).ghostsBlocked = 0;
			((Cerberus) npc.getCombat()).ghostsSpawned = false;
		}
		if (npc.getCombat() instanceof ChaosElemental) {
			((ChaosElemental) npc.getCombat()).unequippedItem = false;
			((ChaosElemental) npc.getCombat()).damagedPlayer = false;
		}
		if (npc.getCombat() instanceof Whisperer whisper) {
			whisper.resetVariables();
		}
	}

	public void setAllowRespawn(boolean allowRespawn) {
		this.allowRespawn = allowRespawn;
	}

	public boolean allowRespawn() {
		return allowRespawn;
	}

	public Player player;

	private void vorkathHead(Position dropPosition, Player pKiller) {
		if (pKiller.vorkathKills.getKills() == 50 && !pKiller.obtained50KCVorkathHead) {
			Item item = new Item(VORKATHS_HEAD);
			this.dropItem(item, pKiller, dropPosition);
			pKiller.obtained50KCVorkathHead = true;
			pKiller.addToCollectionLog(item);
		}
	}

	/**
	 * @killer - the killer of this npc egligible for drops.
	 * @pKillerLastHit = the player that made the last hit
	 * @items - items to drop
	 */
	private void handleDrop(Killer killer, Position dropPosition, Player pKillerLastHit, List<Item> items) {

		if (pKillerLastHit.getPosition().inBounds(Wildywars) && pKillerLastHit.wildernessLevel > 0) { // set the bounds

			boolean debug = false;
			boolean dropKey = Random.get(1, debug ? 1 : 40) == 1;
			int keyId = 11942;
			int amount = 1;

			if (dropKey) {
				items.add(new Item(keyId, amount));
			}

		}

		killer.player.addToCollectionLog(items.toArray(new Item[0]));
		for (Item item : items) {
			int itemId = item.getId();
			if (item.getDef().isNote())
				itemId = item.getId() - 1;

			if (killer.player.getEquipment().get(Equipment.SLOT_RING) != null
					&& AttributeExtensions.hasAttribute(killer.player.getEquipment().get(Equipment.SLOT_RING),
							AttributeTypes.CLUE_HUNTER)
					&& item.getId() == 23182) {
				if (killer.player.getInventory().hasRoomFor(23182))
					killer.player.getInventory().add(23182, 1);
				else
					new GroundItem(23182, 1).owner(killer.player).position(dropPosition).spawn();
				continue;
			}

			if (killer.player.getEquipment().get(Equipment.SLOT_RING) != null
					&& AttributeExtensions.hasAttribute(killer.player.getEquipment().get(Equipment.SLOT_RING),
							AttributeTypes.CLUE_HUNTER)
					&& item.getId() == 2677) {
				if (killer.player.getInventory().hasRoomFor(2677))
					killer.player.getInventory().add(2677, 1);
				else
					new GroundItem(2677, 1).owner(killer.player).position(dropPosition).spawn();
				continue;
			}
			if (killer.player.getEquipment().get(Equipment.SLOT_RING) != null
					&& AttributeExtensions.hasAttribute(killer.player.getEquipment().get(Equipment.SLOT_RING),
							AttributeTypes.CLUE_HUNTER)
					&& item.getId() == 2801) {
				if (killer.player.getInventory().hasRoomFor(2801))
					killer.player.getInventory().add(2801, 1);
				else
					new GroundItem(2801, 1).owner(killer.player).position(dropPosition).spawn();
				continue;
			}
			if (killer.player.getEquipment().get(Equipment.SLOT_RING) != null
					&& AttributeExtensions.hasAttribute(killer.player.getEquipment().get(Equipment.SLOT_RING),
							AttributeTypes.CLUE_HUNTER)
					&& item.getId() == 2722) {
				this.dropItemOrAddToInv(2722, 1, killer.player, dropPosition);
				continue;
			}
			if (killer.player.getEquipment().get(Equipment.SLOT_RING) != null
					&& AttributeExtensions.hasAttribute(killer.player.getEquipment().get(Equipment.SLOT_RING),
							AttributeTypes.CLUE_HUNTER)
					&& item.getId() == 12073) {
				this.dropItemOrAddToInv(12073, 1, killer.player, dropPosition);
				continue;
			}
			if (killer.player.getEquipment().get(Equipment.SLOT_RING) != null
					&& AttributeExtensions.hasAttribute(killer.player.getEquipment().get(Equipment.SLOT_RING),
							AttributeTypes.CLUE_HUNTER)
					&& item.getId() == 19835) {
				this.dropItemOrAddToInv(19835, 1, killer.player, dropPosition);
				continue;
			}

			if (item.getId() == 21820) {
				if (BraceletOfEthereum.handleEthereumDrop(pKillerLastHit, item)) {
					continue;
				}
			}
			if (item.getId() == COINS_995) {
				if (RingOfWealth.check(pKillerLastHit, item)) {
					pKillerLastHit.getInventory().addOrDrop(item);
					continue;
				}
			}
			boolean dropItem = true;
			for (Item equipment : pKillerLastHit.getEquipment().getItems()) {
				if (equipment != null && equipment.getDef() != null) {
					List<String> upgrades = AttributeExtensions.getEffectUpgrades(equipment);
					boolean hasEffect = upgrades != null;
					if (hasEffect) {
						for (String s : upgrades) {
							try {
								if (s.equalsIgnoreCase("empty"))
									continue;
								ItemEffect effect = ItemEffect.valueOf(s);
								dropItem = effect.getUpgrade().modifyDroppedItem(pKillerLastHit, item);
							} catch (Exception ex) {
								System.err.println("Unknown upgrade { " + s + " } found!");
								ex.printStackTrace();
							}
						}
					}
				}
			}

			/*
			 * Donator Benefit: [Noted dragon bones in wilderness]
			 */
			if (pKillerLastHit.wildernessLevel > 5 && pKillerLastHit.getEquipment().contains(22557)) {
				pKillerLastHit.getCombat().skullNormal();
				if (item.getDef().notedId > -1 && !item.getDef().isNote()) {
					item.setId(item.getDef().notedId);
				}
			}
			if (killer.player.getPlayerPerkHandler().getActivePerks(killer.player).contains(Perks.SNAKE_CHARMER)) {
				if (item.getId() == ItemID.ZULRAHS_SCALES)
					item.setAmount(item.getAmount() * 2);
			}
			if (killer.player.getPlayerPerkHandler().getActivePerks(killer.player).contains(Perks.THE_ALCHEMIST)) {
				int perkIndex = killer.player.getPlayerPerkHandler().getActivePerkIndex(killer.player, Perks.THE_ALCHEMIST);
				TheAlchemist c = (TheAlchemist) killer.player.getPlayerPerkHandler().getActivePerks(killer.player)
						.get(perkIndex).getPerk(killer.player);
				if (item.getId() == 995) {
					assert c != null;
					item.setAmount((int) (item.getAmount() * c.goldDroppedMultiplier()));
				}
			}

			if (item.getId() == 534 || item.getId() == 536 || item.getId() == 6812 || item.getId() == 11943
					|| item.getId() == 22124) {
				if (pKillerLastHit.isSapphire() && pKillerLastHit.wildernessLevel > 0) {
					item.setId(item.getDef().notedId);
				}
			}

			/*
			 * Donator Benefit: [Noted herbs in wilderness]
			 */
			if (item.getDef().name.toLowerCase().contains("grimy")) {
				if (pKillerLastHit.isDiamond() && pKillerLastHit.wildernessLevel > 0) {
					if (item.getDef().notedId > -1 && !item.getDef().isNote())
						item.setId(item.getDef().notedId);
				}
			}

			if (item.getDef().name.toLowerCase().contains("statius") ||
					item.getDef().name.toLowerCase().contains("vesta") ||
					item.getDef().name.toLowerCase().contains("zuriel")) {
				pKillerLastHit.sendMessage("You have been red skulled and tele-blocked because of your loot!");
				pKillerLastHit.getCombat().teleblock();
			}

			if (item.getId() == 11942) {
				pKillerLastHit.sendMessage("You just received an Ecumenical Key drop!");
			}

			/*
			 * Modify drop for specific npc
			 */
			if (npc.dropListener != null)
				npc.dropListener.dropping(killer, item);

			/*
			 * Global Broadcast
			 */
			if (item.lootBroadcast != null || item.getDef().dropAnnounce
					&& item.getId() != 21009 && item.getId() != 4151 && item.getId() != HOLY_ELIXIR
					&& item.getId() != DRAGON_HARPOON && item.getId() != 21892) {
				getRareDropAnnounce(pKillerLastHit, item, npc);
			}

			/*
			 * Local Broadcast!
			 */
			if (info.local_loot) {
				getLocalAnnounce(pKillerLastHit, item);
			}

			if (pKillerLastHit.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
				Bone bone = Bone.get(item.getId());
				Ashes ashes = Ashes.get(item.getId());
				if (AttributeExtensions.hasAttribute(
						pKillerLastHit.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.RESPECT_FOR_THE_DEAD)) {
					if (bone != null) {
						RespectTheDead.getExperience(pKillerLastHit, pKillerLastHit.getEquipment().get(Equipment.SLOT_WEAPON),
								bone.exp);
						dropItem = false;
					}
					if (ashes != null) {
						RespectTheDead.getExperience(pKillerLastHit, pKillerLastHit.getEquipment().get(Equipment.SLOT_WEAPON),
								ashes.exp);
						dropItem = false;
					}
				}
			}
			if (killer.player.getPlayerPerkHandler().getActivePerks(killer.player).contains(Perks.LAYING_TO_REST)) {
				int perkIndex = killer.player.getPlayerPerkHandler().getActivePerkIndex(killer.player, Perks.LAYING_TO_REST);
				LayingToRest c = (LayingToRest) killer.player.getPlayerPerkHandler().getActivePerks(killer.player)
						.get(perkIndex).getPerk(killer.player);
				Bone bone = Bone.get(item.getId());
				if (bone != null) {
					if (bone.id == item.getId()) {
						int addition = c.getPrayerPointsAdditionMultiplier(killer.player, bone.exp);
						dropItem = false;
						killer.player.getStats().addXp(StatType.Prayer, bone.exp, true);
						int newLevel = killer.player.getStats().get(StatType.Prayer).currentLevel + addition;
						if (newLevel > killer.player.getStats().get(StatType.Prayer).fixedLevel)
							newLevel = killer.player.getStats().get(StatType.Prayer).fixedLevel;
						killer.player.getStats().get(StatType.Prayer).alter(newLevel);
					}
				}
			}

			if (BoneCrusher.has(pKillerLastHit)) {
				Bone bone = Bone.get(item.getId());

				if (bone != null && BoneCrusher.degrade(pKillerLastHit, bone)) {
					dropItem = false;
				}
			}
			if (pKillerLastHit.getInventory().contains(ItemID.SOUL_BEARER)) {
				for (ReLive.MonsterData data : ReLive.MonsterData.VALUES) {
					for (int headId : data.headId) {
						if (headId == item.getId()) {
							pKillerLastHit.getSoulBearer().sendEnsouledHeadToBank(pKillerLastHit, item);
							dropItem = false;
						}
					}
				}
			}

			int[] uniqueDrops = { 6571, /* Uncut Onyx */ 11286, /* Draconic Visage */ 11335, /* Dragon Full Helm */ 11785,
					/* Armadyl Crossbow */ 11810, /* Armadyl Hilt */ 11812, /* Bandos Hilt */ 11814,
					/* Saradomin Hilt */ 11816, /* Zamorak Hilt */ 11824, /* Zamorakian Spear */ 11826,
					/* Armadyl Helmet */ 11828, /* Armadyl Chestplate */ 11830, /* Armadyl Chainskirt */ 11832,
					/* Bandos Chestplate */ 11834, /* Bandos Tassets */ 11905, /* Trident of the Seas (Full) */ 11920,
					/* Dragon Pickaxe */ 12002, /* Occult Necklace */ 12004, /* Kraken Tentacle */ 12601,
					/* Ring of the Gods */ 12603, /* Tyrannical Ring */ 12605, /* Treasonous Ring */ 12819,
					/* Elysian Sigil */ 12823, 30187, 30420, /* divine sigil /* Spectral Sigil */ 12827, /* Arcane Sigil */ 12922,
					/* Tanzanite Fang */ 12927, /* Serpentine Visage */ 12932, /* Magic Fang */ 13200,
					/* Tanzanite Mutagen */ 13201, /* Magma Mutagen */ 13227, /* Eternal Crystal */ 13229,
					/* Pegasian Crystal */ 13231, /* Primordial Crystal */ 13233, /* Smouldering Stone */ 13265,
					/* Abyssal Dagger */ 13273, /* Unsired */ 19529, /* Zenyte Shard */ 19589, /* Heavy Frame */ 21637,
					/* Wyvern Visage */ 22302, /* Ancient Effigy */ 22305, /* Ancient Relic */ 22325,
					/* Scythe of Vitur */ 22542, /* Viggora's Chainmace (U) */ 22547, /* Craw's Bow (U) */ 22552,
					/* Thammaron's Sceptre (U) */ 22610, /* Vesta's Spear */ 22613, /* VLS */ 22616,
					/* Vesta's Chainboy */ 22619, /* Vesta's Plateskirt */ /* Stat Warhammer */ 22625,
					/* Stat Full Helm */ 22628, /* Stat Platebody */ 22631, /* Stat Platelegs */ 22638,
					/* Morrigan's Coif */ 22641, /* Morrigan's Leather Body */ 22644, /* Morrigan's Leather Chaps */ 22647,
					/* Zuriel's Staff */ 22650, /* Zuriel's Hood */ 22653, /* Zuriel's Robe Top */ 22656,
					/* Zuriel's Robe Bottom */ 22966 /* Hydra's Claw */, 26372, 27226, 27229, 27232, 27277, 25985, 25975, 30420,
					30187, };

			/*
			 * Spawn the item on the ground.
			 */
			if (dropItem) {
				if (item.getDef().isNote() || item.getDef().stackable || item.getAmount() == 1) {
					this.dropItem(item, pKillerLastHit, dropPosition);
				} else {
					for (int i = 0; i < item.getAmount(); i++) {
						if (pKillerLastHit.getEquipment().get(Equipment.SLOT_RING) != null
								&& AttributeExtensions.hasAttribute(pKillerLastHit.getEquipment().get(Equipment.SLOT_RING),
										AttributeTypes.CLUE_HUNTER)) {
							if (item.getDef().name.contains("reward scroll")) {
								if (pKillerLastHit.getInventory().hasRoomFor(item.getId())) {
									pKillerLastHit.getInventory().add(item.getId(), 1);
								} else {
									this.dropItem(item.getId(), 1, pKillerLastHit, dropPosition);
								}
							} else
								this.dropItem(item.getId(), 1, pKillerLastHit, dropPosition);
						} else {
							this.dropItem(item.getId(), 1, pKillerLastHit, dropPosition);
						}
					}
				}
			}
		}
	}

	public void getRareDropAnnounce(Player pKiller, Item item, NPC npc) {
		int amount = item.getAmount();
		String message = pKiller.getName() + " just received ";
		if (amount > 1)
			message += NumberUtils.formatNumber(amount) + " x " + item.getDef().name;
		else
			message += item.getDef().descriptiveName;
		if (item.lootBroadcast != null) {
			if (npc.getDef().killCounter == null || npc.getDef().killCounter.apply(pKiller.player) == null)
				Broadcast.WORLD.sendNewsDropMessage(pKiller.player, Icon.ADMINISTRATOR,
						"<col=000000>" + pKiller.player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
								+ item.getDef().name.toLowerCase() + "</shad> from a " + npc.getDef().name.toLowerCase() + "!");
			else
				Broadcast.WORLD.sendNewsDropMessage(pKiller.player, Icon.ADMINISTRATOR,
						"<col=000000>" + pKiller.player.getName(),
						" received <shad=D80808>" + item.getAmount() + "x " + item.getDef().name.toLowerCase() + "</shad> from a "
								+ npc.getDef().name.toLowerCase() + "! (<col=FC0101>"
								+ (npc.getDef().killCounter.apply(pKiller.player).getKills() + 1) + " KC<col=000000>)");

		} else {
			if (npc.getDef().killCounter == null || npc.getDef().killCounter.apply(pKiller.player) == null)
				Broadcast.WORLD.sendNewsDropMessage(pKiller.player, Icon.ADMINISTRATOR,
						"<col=000000>" + pKiller.player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
								+ item.getDef().name.toLowerCase() + "</shad> from a " + npc.getDef().name.toLowerCase() + "!");
			else
				Broadcast.WORLD.sendNewsDropMessage(pKiller.player, Icon.ADMINISTRATOR,
						"<col=000000>" + pKiller.player.getName(),
						" received <shad=D80808>" + item.getAmount() + "x " + item.getDef().name.toLowerCase() + "</shad> from a "
								+ npc.getDef().name.toLowerCase() + "! (<col=FC0101>"
								+ (npc.getDef().killCounter.apply(pKiller.player).getKills() + 1) + " KC<col=000000>)");

		}
		if (npc.getDef().killCounter == null || npc.getDef().killCounter.apply(pKiller.player) == null) {

			RareDropHook.sendDiscordMessage(() -> {
				var jsonObject = new JSONObject();
				jsonObject.put("player", pKiller.player.getName());
				jsonObject.put("game_mode", pKiller.player.getGameMode());
				jsonObject.put("item_id", item.getId());
				jsonObject.put("item_name", item.getDef().name);
				jsonObject.put("source", npc.getDef().descriptiveName);
				jsonObject.put("total_attempts", Utils.formatMoneyString(-1));
				return jsonObject;
			});

		} else {

			RareDropHook.sendDiscordMessage(() -> {
				var jsonObject = new JSONObject();
				jsonObject.put("player", pKiller.player.getName());
				jsonObject.put("game_mode", pKiller.player.getGameMode());
				jsonObject.put("item_id", item.getId());
				jsonObject.put("item_name", item.getDef().name);
				jsonObject.put("source", npc.getDef().descriptiveName);
				jsonObject.put("total_attempts",
						Utils.formatMoneyString(getNpc().getDef().killCounter.apply(pKiller.player).getKills() + 1));
				return jsonObject;
			});
		}
	}

	private void getLocalAnnounce(Player pKiller, Item item) {
		npc.localPlayers().forEach(p -> p.sendFilteredMessage(Color.DARK_GREEN.wrap(pKiller.getName() + " received a drop: "
				+ NumberUtils.formatNumber(item.getAmount()) + " x " + item.getDef().name)));
	}

	public Position getDropPosition() {
		return npc.getPosition();
	}

	public void restore() {
		for (Stat stat : stats)
			stat.restore();
		npc.resetFreeze();
		npc.cureVenom(0);
	}

	/**
	 * Misc
	 */
	@Override
	public boolean allowRetaliate(Entity attacker) {
		if (npc._targetPlayer != null && attacker != npc._targetPlayer) // npc has a designated target
			return false;
		if (npc.isLocked())
			return false;
		if (!allowRetaliate)
			return false;
		if (target != null && target.getCombat().getTarget() == npc) { // npc is being attacked by target
			boolean prioritizePlayer = target.npc != null && attacker.player != null; // target is an npc and attacker is a
			// player
			if (!prioritizePlayer)
				return false;
		}
		return true;
	}

	@Override
	public AttackStyle getAttackStyle() {
		return info.attack_style;
	}

	@Override
	public AttackType getAttackType() {
		return null;
	}

	@Override
	public double getLevel(StatType statType) {
		int i = statType.ordinal();
		return i >= stats.length ? 0 : stats[i].currentLevel;
	}

	public Stat getStat(StatType statType) {
		return stats[statType.ordinal()];
	}

	@Override
	public double getBonus(int bonusType) {
		return bonusType >= bonuses.length ? 0 : bonuses[bonusType];
	}

	@Override
	public Killer getKiller() {
		if (npc._targetPlayer != null && npc._targetPlayer instanceof Player) {
			Killer killer = new Killer();
			killer.player = (Player) npc._targetPlayer;
			return killer;
		}
		return super.getKiller();
	}

	@Override
	public int getDefendAnimation() {
		return info.defend_animation;
	}

	public int getMaxDamage() {
		return info.max_damage;
	}

	public npc_combat.Info getInfo() {
		return info;
	}

	@Override
	public double getDragonfireResistance() {
		return 0;
	}

	public final void checkAggression() {
		if (target == null && isAggressive()) {
			target = findAggressionTarget();
			if (target != null)
				faceTarget();
		}
	}

	protected Entity findAggressionTarget() {
		final NPC npc = this.npc;
		if (npc.hasTarget()) {
			return null;
		}

		final List<Player> localPlayers = npc.localPlayers();
		if (localPlayers.isEmpty()) {
			return null;
		}

		for (final Player localPlayer : localPlayers) {
			if (canAggro(localPlayer)) {
				return localPlayer;
			}
		}

		return null;
	}

	protected int getAggressiveLevel() {
		if (npc.wildernessSpawnLevel > 0)
			return Integer.MAX_VALUE;
		else if (info.aggressive_level == -1)
			return npc.getDef().combatLevel * 2;
		else
			return info.aggressive_level;
	}

	public boolean isAggressive() {
		return getInfo().aggressive_level != 0;
	}

	protected boolean canAggro(final Player player) {
		int aggroLevel = getAggressiveLevel();
		if (npc.getDef().name.equalsIgnoreCase("Egg"))
			return false;
		return player.getCombat().getLevel() <= aggroLevel // in level range
				&& canAttack(player) // can attack
				&& !player.isIdle // player isn't idl
				&& (player.inMulti() || !player.getCombat().isDefending(12))
				&& DumbRoute.withinDistance(npc, player, getAggressionRange())
				&& (npc.inMulti() || (StreamSupport.stream(npc.localNpcs().spliterator(), false)
						.noneMatch(n -> n.getCombat() != null && n.getCombat().getTarget() == player
								&& !n.getCombat().isAttacking(10) && !n.getMovement().isAtDestination())))
				&& (npc.aggressionImmunity == null || !npc.aggressionImmunity.test(player));
	}

	public int getAggressionRange() {
		switch (npc.getId()) {
			case 2218: // kk lets actually test
			case 2217:
			case 2216:
			case 3163:
			case 3165:
			case 3164:
				return 13;
		}
		return npc.wildernessSpawnLevel > 0 ? 2 : 4; // just for gw or generally, might be smarter to do them seperate yeh
	}

	public int getAttackBoundsRange() {
		return 12;
	}

	@Override
	public void faceTarget() {
		if (npc.getId() != 11730 && npc.getId() != 11761 && npc.getId() != 11731 && npc.getId() != 11733
				&& npc.getId() != 11722)
			npc.face(target);
	}

	public boolean targetIsNotInBossRegion() {
		if (target == null)
			return true;
		var playerRegion = target.player.getPosition().region();
		var bossRegion = npc.getPosition().region();
		return playerRegion != bossRegion;
	}

	/**
	 * Handler functions
	 */
	public abstract void init();

	public abstract void follow();

	public abstract boolean attack();

	public int getRandomDropCount() {
		return info.random_drop_count;
	}

	public abstract void process();

	private void dropItemOrAddToInv(int id, int amount, Player owner, Supplier<Boolean> invOnlyIf) {
		this.dropItemOrAddToInv(id, amount, owner, this.npc.getPosition().copy(), invOnlyIf);
	}

	private void dropItemOrAddToInv(int id, int amount, Player owner, Position position, Supplier<Boolean> invOnlyIf) {
		var inv = owner.getInventory();
		if (!inv.hasRoomFor(id, amount)) {
			this.dropItem(id, amount, owner, position);
			return;
		}

		if (!invOnlyIf.get()) {
			this.dropItem(id, amount, owner, position);
			return;
		}

		inv.add(id, amount);
	}

	private void dropItemOrAddToInv(int id, int amount, Player owner, Position position) {
		var inv = owner.getInventory();
		if (inv.hasRoomFor(id)) {
			inv.add(id, amount);
			return;
		}
		this.dropItem(id, amount, owner, position);
	}

	private void dropItem(int id, int amount, Player owner) {
		this.dropItem(id, amount, owner, this.npc.getPosition().copy());
	}

	private void dropItem(Item item, Player owner) {
		this.dropItem(item, owner, this.npc.getPosition());
	}

	private void dropItem(Item item, Player owner, Position position) {
		new GroundItem(item).owner(owner).position(position).spawn();
		this.notifyDrop(owner, item.getId(), item.getAmount());
	}

	private void dropItem(int id, int amount, Player owner, Position position) {
		new GroundItem(id, amount).owner(owner).position(position).spawn();
		this.notifyDrop(owner, id, amount);
	}

	private void notifyDrop(Player owner, int id, int amount) {
		owner.getPacketSender().sendClientScript(7192, new Object[] {
				this.npc.getId(), 1, id, amount
		});
	}

	private static int dropRolls(Player player, NPC npc) {
		var nDef = npc.getDef();
		var nName = nDef == null ? "" : nDef.name;
		var pPerks = player.getPlayerPerkHandler();

		var rolls = 1;

		if (nName.equalsIgnoreCase("Dusk")) {
			rolls = 2;
		}

		if (player.getEquipment().get(Equipment.SLOT_RING) != null
				&& player.getEquipment().get(Equipment.SLOT_RING).getId() == 30592 && Random.get(3) == 0) {
			rolls++;
		}

		if (player.doubleDropBonus.remaining() > 0) {
			rolls *= 2;
		}

		if (rollExtraDonatorDrop(player)) {
			rolls++;
		}

		if (CamelStatueHandler.isRewardActive(CamelStatueRewards.ADDITIONAL_ROLL_CHANCE) && Random.get(2) == 0) {
			rolls++;
		}

		if (pPerks.getActivePerks(player).contains(Perks.SECURING_THE_BAG)) {
			var perkIndex = pPerks.getActivePerkIndex(player, Perks.SECURING_THE_BAG);
			var c = (SecuringTheBag) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
			if (Random.rollPercent(c.getAnotherRollChance())) {
				rolls++;
			}
		}

		if (npc.getDef().name.equalsIgnoreCase("zulrah")) {
			rolls++;

			if (pPerks.getActivePerks(player).contains(Perks.SNAKE_CHARMER)) {
				var perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.SNAKE_CHARMER);
				var c = (SnakeCharmer) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
				if (Random.rollPercent(c.getAdditionalDropChance())) {
					rolls++;
				}
			}
		}

		if (npc.getId() > 8063 && npc.getId() < 8067) {
			rolls++;
		}
		return rolls;
	}
}
