package io.ruin.model.entity.player;

import io.ruin.api.protocol.PlatformInfo;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.SecurityGuard;
import io.ruin.model.activities.blastfurnace.BlastFurnace;
import io.ruin.model.activities.bosses.instancetoken.InstanceTokenInterface;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.activities.dailytasks.DailyTasksInterface;
import io.ruin.model.activities.duelarena.Duel;
import io.ruin.model.activities.gamble.Gamble;
import io.ruin.model.activities.gauntlet.Gauntlet;
import io.ruin.model.activities.moonsofperil.MoonsOfPerilHandler;
import io.ruin.model.activities.newcomertasks.NewcomerTasksInterface;
import io.ruin.model.activities.newshop.NewShopInterface;
import io.ruin.model.activities.perktree.PerkSets;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.PlayerPerk;
import io.ruin.model.activities.perktree.PlayerPerkHandler;
import io.ruin.model.activities.perktree.PlayerPerkInterface;
import io.ruin.model.activities.pestcontrol.PestControlGame;
import io.ruin.model.activities.pvminstances.PVMInstance;
import io.ruin.model.activities.pyramidplunder.PyramidPlunderGame;
import io.ruin.model.activities.raids.chambersrework.CustomXericRaid;
import io.ruin.model.activities.raids.chambersrework.XericParty;
import io.ruin.model.activities.raids.toa.ToAPartyInterface;
import io.ruin.model.activities.raids.toa.ToASuppliesInterface;
import io.ruin.model.activities.raids.toa.ToaApplicantsInterface;
import io.ruin.model.activities.raids.toa.ToaInvocationsInterface;
import io.ruin.model.activities.raids.toa.ToaMembersInterface;
import io.ruin.model.activities.raids.toa.ToaPartyViewInterface;
import io.ruin.model.activities.raids.tob.dungeon.room.TheatreRoom;
import io.ruin.model.activities.raids.tob.party.TheatreParty;
import io.ruin.model.activities.raids.xeric.party.Party;
import io.ruin.model.activities.tempoross.Tempoross;
import io.ruin.model.content.DailyVoteInterface;
import io.ruin.model.content.camelstatue.CamelStatueInterface;
import io.ruin.model.content.combatachievements.CombatAchievementInterface;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.content.equipmentpresets.GearPresetInterface;
import io.ruin.model.content.itembreaking.ItemBreakInterface;
import io.ruin.model.content.itembreaking.ItemUpgradeInterface;
import io.ruin.model.content.upgradesystem.UpgradeSystemInterface;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.HealthHud;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.PetNPC;
import io.ruin.model.entity.shared.UpdateMask;
import io.ruin.model.entity.shared.listeners.ActivatePrayerListener;
import io.ruin.model.entity.shared.listeners.AllowPrayerListener;
import io.ruin.model.entity.shared.listeners.LogoutListener;
import io.ruin.model.entity.shared.listeners.TeleportListener;
import io.ruin.model.entity.shared.masks.ChatUpdate;
import io.ruin.model.entity.shared.masks.MovementModeUpdate;
import io.ruin.model.entity.shared.masks.PlayerOpsUpdate;
import io.ruin.model.entity.shared.masks.TeleportModeUpdate;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.ToplevelInterfaceType;
import io.ruin.model.inter.dialogue.Dialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.dialogue.skill.SkillDialogue;
import io.ruin.model.inter.handlers.DropViewer;
import io.ruin.model.inter.handlers.LootsViewer;
import io.ruin.model.inter.handlers.OptionScroll;
import io.ruin.model.inter.handlers.ServerTeleports;
import io.ruin.model.inter.handlers.TeleInterface;
import io.ruin.model.inter.handlers.shopinterface.CustomShop;
import io.ruin.model.inter.handlers.shopinterface.CustomShop2;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.inter.questtab.QuestTabEntry;
import io.ruin.model.inter.questtab.bestiary.DropTableEntry;
import io.ruin.model.inter.questtab.presets.Preset;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.boxes.mystery.SuperMysteryBox;
import io.ruin.model.item.actions.impl.scratchcard.ScratchCardManager;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.item.containers.Inventory;
import io.ruin.model.item.containers.Trade;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.Region;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.impl.CanoeStation;
import io.ruin.model.shop.Shop;
import io.ruin.model.skills.construction.room.Room;
import io.ruin.model.skills.construction.seat.Seat;
import io.ruin.model.skills.hunter.traps.Trap;
import io.ruin.model.skills.magic.SpellBook;
import io.ruin.model.skills.magic.spells.arceuus.Resurrection;
import io.ruin.model.skills.magic.spells.modern.Teleother;
import io.ruin.model.skills.slayer.SlayerTask;
import io.ruin.model.skills.smithing.SmithBar;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.network.PacketSender;
import io.ruin.utility.TickDelay;
import lombok.Getter;
import lombok.Setter;
import net.rsprot.protocol.api.Session;
import net.rsprot.protocol.api.util.ZonePartialEnclosedCacheBuffer;
import net.rsprot.protocol.game.outgoing.info.npcinfo.NpcInfo;
import net.rsprot.protocol.game.outgoing.info.playerinfo.PlayerInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("exports")
public abstract class PlayerAttributesRuntime extends Entity {
	private static final int INTERFACE_COUNT = 2048;
	private static final int VARPS_COUNT = 30_000;

	public transient int[] updatedVarpIds = new int[VARPS_COUNT];
	public transient boolean[] updatedVarps = new boolean[VARPS_COUNT];
	public transient int[][] lootedPlunderObjects = {
			{ 26580, 0 },
			{ 26600, 0 },
			{ 26601, 0 },
			{ 26603, 0 },
			{ 26604, 0 },
			{ 26606, 0 },
			{ 26607, 0 },
			{ 26608, 0 },
			{ 26609, 0 },
			{ 26610, 0 },
			{ 26611, 0 },
			{ 26612, 0 },
			{ 26613, 0 },
			{ 26616, 0 },
			{ 26626, 0 }
	};

	public transient boolean skull;
	public transient boolean wilderness;
	public transient boolean killedByAPlayer;
	public transient boolean bossMaster = false;
	public transient boolean tankMode = false;
	public transient boolean locatorOrbAvailable = true;
	public transient boolean VoteClaimed = false;
	public transient boolean online = false;
	public transient boolean started = false;
	public transient boolean[] visibleInterfaces = new boolean[INTERFACE_COUNT];
	public transient Set<Integer> activeStaticMapListeners = new HashSet<>();
	public transient List<MapListener> activeMapListeners = new ArrayList<>();
	public transient boolean groupStorageSaving = false;
	public transient boolean retryIntConsumer;
	public transient boolean retryStringConsumer;
	public transient boolean protect;
	public transient boolean inTob = false;
	public transient boolean tobPurpleObtained = false;
	public transient boolean inTheatreParty;
	public transient boolean isCheckingDispenser = false;
	public transient boolean tfa;
	public transient boolean isOperatingPump = false;
	@Getter
	@Setter
	public transient boolean driveBeltBroken;
	public transient boolean npcTarget;
	public transient boolean inTutorial = false;
	public transient boolean singlesPlus;
	public transient boolean tpWildWarn = true;
	public transient boolean teleportsInterface;
	public transient boolean inMonsterViewer = false;
	public transient boolean pvpAttackZone;
	public transient boolean callPet = false;
	public transient boolean showPet = false;
	public transient boolean hidePet = false;
	public transient boolean tempUseRaidPrayers = false;
	public transient boolean usingTournamentOrb = false;
	public transient boolean usingTournamentOrbFromHome = false;
	public transient boolean tournamentPouch = false;
	public transient boolean tournamentRigour = false;
	public transient boolean tournamentAugury = false;
	public transient boolean tournamentPreserve = false;
	public transient boolean insideRaid = false;
	public transient boolean supplyChestRestricted = false;
	public transient boolean advertisingParty = false;
	public transient boolean bloodyKeyRestricted = false;
	public transient boolean dragonfireShieldSpecial = false;
	public transient boolean insideWildernessAgilityCourse = false;
	public transient boolean claimedBox = true;
	public transient boolean easterEgg = false;
	public transient boolean isIdle = false;
	public transient boolean dismissBotPreventionNPC = false;
	public transient boolean overloadBoostActive = false;
	public transient boolean prayerEnhanceBoostActive = false;
	public transient boolean rigging = false;
	public transient boolean bleedActive = false;
	public transient boolean inGamble = false;
	public transient boolean inCox = false;
	public transient boolean eternalResilienceActive = false;
	public transient boolean berserkerRageActive = false;
	public transient boolean godmode = false;

	public transient ActivityTimer grotesqueGuardiansTimer;
	public transient ActivityTimer corruptedHunllefTimer;
	public transient ActivityTimer crystallineHunllefTimer;
	public transient ActivityTimer zulrahTimer;
	public transient ActivityTimer solHereditTimer;
	public transient ActivityTimer yamaTimer;
	public transient ActivityTimer nightmareTimer;
	public transient ActivityTimer phantomMuspahTimer;
	public transient ActivityTimer dukeSucellusTimer;
	public transient ActivityTimer vardorvisTimer;
	public transient ActivityTimer vorkathTimer;
	public transient ActivityTimer whispererTimer;
	public transient ActivityTimer argentavisTimer;
	public transient ActivityTimer galvekTimer;
	public transient ActivityTimer alchemicalHydraTimer;
	public transient ActivityTimer soloOlmTimer;
	public transient ActivityTimer araxxorTimer;
	public transient ActivityTimer scurriusTimer;
	public transient ActivityTimer moonsOfPerilTimer;
	public transient ActivityTimer toaRaidTimer;
	public transient ActivityTimer leviathanTimer;

	public transient TickDelay guardSpawnCoolDown = new TickDelay();
	public transient TickDelay elementalConvergenceDelay = new TickDelay();
	public transient TickDelay soulReaverDelay = new TickDelay();
	public transient TickDelay olmSpecialDelay = new TickDelay();
	public transient TickDelay aoeSwipeDelay = new TickDelay();
	public transient TickDelay doubleTapDelay = new TickDelay();
	public transient TickDelay gauntletBossTimer = new TickDelay();
	public transient TickDelay boneBuryDelay = new TickDelay();
	public transient TickDelay ashScatterDelay = new TickDelay();
	public transient TickDelay doubleDropBonus = new TickDelay();
	public transient TickDelay eatDelay = new TickDelay();
	public transient TickDelay karamDelay = new TickDelay();
	public transient TickDelay potDelay = new TickDelay();
	public transient TickDelay yesDelay = new TickDelay();
	public transient TickDelay noDelay = new TickDelay();
	public transient TickDelay emoteDelay = new TickDelay();
	public transient TickDelay magicImbueEffect = new TickDelay();
	public transient TickDelay galvekEyeCooldown = new TickDelay();
	public transient TickDelay eneryTransferCooldown = new TickDelay();
	public transient TickDelay superiorPotionCooldown = new TickDelay();
	public transient TickDelay superiorHeartCooldown = new TickDelay();
	public transient TickDelay magicImbueHeartCooldown = new TickDelay();
	public transient TickDelay rangedImbueHeartCooldown = new TickDelay();
	public transient TickDelay strengthImbueHeartCooldown = new TickDelay();
	public transient TickDelay overloadImbueHeartCooldown = new TickDelay();
	public transient TickDelay magicTaintedWandCooldown = new TickDelay();
	public transient TickDelay overloadTaintedWandCooldown = new TickDelay();
	public transient TickDelay overloadHeartCooldown = new TickDelay();
	public transient TickDelay saltCooldown = new TickDelay();
	public transient TickDelay rangersHeartCooldown = new TickDelay();
	public transient TickDelay combatantHeartCooldown = new TickDelay();
	public transient TickDelay crystalSawCooldown = new TickDelay();
	public transient TickDelay acceptDelay = new TickDelay();
	public transient TickDelay tokenEvent = new TickDelay();
	public transient TickDelay godwarsAltarCooldown = new TickDelay();
	public transient TickDelay blackChinchompaBoost = new TickDelay();
	public transient TickDelay darkCrabBoost = new TickDelay();
	public transient TickDelay alchDelay = new TickDelay();
	public transient TickDelay superheatDelay = new TickDelay();
	public transient TickDelay sotdDelay = new TickDelay();
	public transient TickDelay soulflameHornBoost = new TickDelay();
	public transient TickDelay rockCakeDelay = new TickDelay();
	public transient TickDelay locatorOrbDelay = new TickDelay();
	public transient TickDelay first3 = new TickDelay();
	public transient TickDelay elderChaosDruidTeleport = new TickDelay();
	public transient TickDelay expBonus = new TickDelay();
	public transient TickDelay petDropBonus = new TickDelay();
	public transient TickDelay rareDropBonus = new TickDelay();
	public transient TickDelay recentlyEquipped = new TickDelay();
	public transient TickDelay nurseSpecialRefillCooldown = new TickDelay();
	public transient TickDelay vestasSpearSpecial = new TickDelay();
	public transient TickDelay morrigansAxeSpecial = new TickDelay();
	public transient TickDelay dragonfireShieldCooldown = new TickDelay();
	public transient TickDelay presetDelay = new TickDelay();
	public transient TickDelay riskProtectionExpirationDelay = new TickDelay();
	public transient TickDelay snowballCooldown = new TickDelay();
	public transient TickDelay specTeleportDelay = new TickDelay();
	public transient TickDelay edgevilleStallCooldown = new TickDelay();
	public transient TickDelay botPreventionJailDelay = new TickDelay();
	public transient TickDelay botPreventionNpcShoutDelay = new TickDelay();
	public transient TickDelay eternalResilienceDelay = new TickDelay();
	public transient TickDelay spectralGuardianDelay = new TickDelay();
	public transient TickDelay weatherWizardDelay = new TickDelay();
	public transient TickDelay dragonWrathDelay = new TickDelay();
	public transient TickDelay burningWrathDelay = new TickDelay();
	public transient TickDelay berkserkerRageDelay = new TickDelay();
	public transient TickDelay timeManipulatorDelay = new TickDelay();
	public transient TickDelay poisonDamageDelay = new TickDelay();
	public transient TickDelay liquidAdrenalineDelay = new TickDelay();
	public transient TickDelay silkDressingDelay = new TickDelay();
	public transient TickDelay blessedScarabDelay = new TickDelay();
	public transient TickDelay xLogDelay;

	public transient int instanceId = 0;
	public transient int gambleId = 0;
	public transient int targetOverlayResetTicks;
	public transient int supplyChestDamage = 0;
	public transient int supplyChestPoints = 0;
	public transient int viewingTheatreSlot;
	public transient int houseBuildPointX, houseBuildPointY, houseBuildPointZ;
	public transient int specialRestoreTicks;
	public transient int selectedSettingMenu = -1;
	public transient int selectedSettingChild = -1;
	public transient int pvpCombatLevel;
	public transient int totalStaked = 0;
	public transient int bloodMoneyStaked = 0;
	public transient int bloodyTokensStaked = 0;
	public transient int wildernessLevel = -1;
	public transient int teleportSelectedCategory = -1;
	public transient int nextDefenderId = -1;
	public transient int wintertodtPoints = 0;
	public transient int activeKillLogSlot = -1;
	public transient int toaInvo = 0;
	public transient int toaPetRate = 1000;
	public transient int doePetRate = 1000;
	@Getter
	@Setter
	public transient int toaSuppliesClaimed = 0;
	public transient int cachedRunePouchTypes = 0;
	public transient int cachedRunePouchAmounts = 0;
	public transient int bloodyFragments = 0;
	public transient int selectedCreditPackage = 0;
	public transient int selectedPaymentMethod = 0;
	public transient int idleTicks = 0;
	public transient int pestActivityScore = 0;
	public transient int selectedWidgetId = 0;
	public transient int lmsSessionKills = 0;
	public transient int pkModeTutorialOp = 0;
	public transient int botWarnings = 0;
	public transient int randomEventPrevent = 0;
	public transient int ipAddressInt;
	public transient int shopIdentifier = -1;
	public transient int userId = 0;
	public transient int unreadPMs = 0;
	public transient Integer[][] visibleInterfaceIds = new Integer[INTERFACE_COUNT][];
	public transient int dialogueStage = 0;
	public transient int updatedVarpCount = 0;
	public volatile Player.LogoutStage logoutProcessStage = Player.LogoutStage.NoLogout;
	public transient int lastSoulStackRemoval = 0;
	public transient int lastSunlightStackRemoval = 0;

	public transient boolean inKbdInstance = false;
	public transient int privateGrotesqueGuardianKills = 0;
	public transient int privateKrakenKills = 0;
	public transient int hydraInstanceKills = 0;
	public transient int perfectDukeKills = 0;
	public transient int perfectLeviathanKills = 0;
	public transient int perfectVardorvisKills = 0;
	public transient int perfectWhispererKills = 0;
	public transient int dodgingTheDragonKills = 0;
	public transient int privateVorkathKills = 0;
	public transient boolean attackedZilyana = false;
	public transient boolean attackedKree = false;
	public transient boolean privateZilyanaInstance = false;
	public transient boolean privateBandosInstance = false;
	public transient int keepAwayKills = 0;
	public transient boolean privateKreeInstance = false;
	public transient boolean privateKrilInstance = false;
	public transient int recentSnakelingKills = 0;
	public transient int recentDkKills = 0;
	public transient int recentDkKills2 = 0;
	public transient int huskKills = 0;
	public transient int privateKBDKills = 0;
	public transient int privateZilyKills = 0;
	public transient int privateBandosKills = 0;
	public transient int privateKrilKills = 0;
	public transient int privateKreeKills = 0;
	public transient int privatePhantomMuspahKills = 0;
	public transient boolean killedAllMeleeBarrowsWithoutBeingHit = true;
	public transient boolean killedAllBarrowsWithoutDamaged = true;

	public transient long advertisementStartTick = 0L;
	public transient long lastActionTime = 0;
	public transient long lastBoxHeal = 0;
	public transient long loginTime = 0L;

	public transient NPC examineMonster = null;
	public transient NPC botPreventionNPC = null;
	public transient NPC teleportsWizard = null;

	public transient Player examinePlayer;

	public transient Room[] houseViewerRooms;
	public transient Room houseViewerRoom;

	public transient Position viewingOrbLocation = null;
	public transient Position lastAlchPosition = null;
	public transient Position currentToaEnterPosition = null;

	public transient Consumer<Integer> consumerInt;
	public transient Consumer<String> consumerString;

	public transient List<Item> theatreReward = new ArrayList<>();
	public transient List<Item> toaChaosLoot = new ArrayList<>();
	public transient List<Item> toaPowerLoot = new ArrayList<>();
	public transient List<Item> toaLifeLoot = new ArrayList<>();
	public transient List<Item> toaReward = new ArrayList<>();
	@Getter
	public transient List<Item> toaSupplies = new ArrayList<>();

	public transient List<CombatAchievements> combatAchievementsList = new ArrayList<>();
	public transient List<StatType> combatStats = List.of(
			StatType.Attack,
			StatType.Strength,
			StatType.Defence,
			StatType.Ranged,
			StatType.Magic);
	public transient List<ServerTeleports> searchTeles = new ArrayList<>();
	public transient List<ServerTeleports> teleportFavourites = new ArrayList<>();
	public transient List<DropTableEntry> dropTableSearchResults;
	public transient List<Function<Player, KillCounter>> activeKillLogList = null;
	public transient List<QuestTabEntry> bestiarySearchResults = null;

	@Getter
	@Setter
	public transient PetNPC petNPC = null;
	public transient PetNPC familiarNPC = null;

	public transient PlayerAction[] actions = new PlayerAction[8];
	public transient PlayerUpdater updater = null;
	public transient PlayerNPCUpdater npcUpdater = null;
	public transient PlayerOpsUpdate playerOpsUpdate = null;
	public transient PlayerInfo rsprotPlayerInfo;

	public transient List<PerkSets> activePerkSetsList = new ArrayList<>();
	public transient List<Perks> ownedPerksList = new ArrayList<>();
	public transient List<Perks> activePerksList = new ArrayList<>();
	public transient PlayerPerkInterface playerPerkInterface;
	public transient PlayerPerkHandler playerPerkHandler;
	public transient Map<Integer, PlayerPerk> ownedPlayerPerks = new HashMap<>();

	public transient Dialogue[] dialogues = null;
	public transient Dialogue lastDialogue = null;
	public transient OptionsDialogue optionsDialogue = null;
	public transient SkillDialogue skillDialogue = null;
	public transient YesNoDialogue yesNoDialogue = null;

	public transient int dealsInterContainerId = 2300;
	public transient boolean dealsClosed = false;
	public transient Map<Integer, Integer> freeContainerSlots = new HashMap<>();
	public transient List<Item> dealItems = new ArrayList<>();

	public transient final TeleInterface teleportInterface = new TeleInterface();
	public transient LogoutListener logoutListener;
	public transient AllowPrayerListener allowPrayerListener;
	public transient ActivatePrayerListener activatePrayerListener;
	public transient TeleportListener teleportListener;
	public transient JournalTab.Section currentSection;
	public transient DailyVoteInterface dailyVote;
	public transient NewcomerTasksInterface newcomerTaskInterface;
	public transient DailyTasksInterface dailyTasksInterface;
	public transient NewShopInterface newShopInterface = new NewShopInterface();
	public transient HashMap<Integer, Integer> raidPrivateStorageItems = new HashMap<>();
	public transient Tempoross temporossGame;
	public transient Gauntlet gauntlet;
	public transient ScratchCardManager scratchCardManager = new ScratchCardManager();
	public transient Entity targetOverlayTarget;
	public transient TheatreRoom theatreRoom;
	public transient BlastFurnace.Ore currentBlastFurnaceOre;
	public transient VarPlayerRepository selectedKeybindConfig;
	public transient SmithBar smithBar;
	public transient Teleother teleotherActive;
	public transient GameObject fairyRing;
	public transient CanoeStation canoeStation;
	public transient SpellBook spellbookSwapOriginalBook;
	public transient ArrayList<Trap> traps = new ArrayList<>(5);
	public transient SlayerTask slayerTask;
	public transient OptionScroll optionScroll;
	public transient Party raidsParty = null;
	public transient XericParty party = null;
	public transient Party viewingParty = null;
	public transient Title title = null;
	public transient Seat seat = null;
	public transient Preset lastPresetUsed = null;
	public transient StatType selectedSkillLampSkill = null;
	public transient PVMInstance currentInstance = null;
	public transient Resurrection.Thrall thrall = null;
	public transient PestControlGame pestGame = null;
	public transient PyramidPlunderGame pyramidPlunderGame = null;
	public transient MapHandler currentInstanceHandler = null;
	public transient CustomXericRaid customXericRaid = null;
	public transient SecurityGuard securityGuard = null;
	public transient MoonsOfPerilHandler moonsOfPerilHandler = null;
	public transient InstanceTokenInterface instanceTokenInterface;
	public transient TheatreParty currentParty = null;
	public transient Map<Integer, Boolean> combatAchievementRewardsClaimed = new HashMap<>();
	public transient LootsViewer lootsViewer;
	public transient DropViewer dropViewer;
	public transient UpgradeSystemInterface upgradeSystemInterface;
	public transient ArrayList<String> DiaryRewards = new ArrayList<>();
	public transient Gamble gamble;
	public transient Shop viewingShop;
	public transient SuperMysteryBox box = new SuperMysteryBox();
	public transient DynamicMap currentDynamicMap = null;
	public transient MapHandler currentMapHandler = null;
	@Getter
	@Setter
	public transient CustomShop activeCustomShop = null;
	@Getter
	@Setter
	public transient CustomShop2 activeCustomShop2 = null;
	public transient PlatformInfo platformInfo = null;
	public transient SecondaryGroup clientGroups = null;
	public transient PacketSender packetSender = null;
	public transient ToplevelInterfaceType toplevel;
	public transient InterfaceHandler[] activeInterfaceHandlers = new InterfaceHandler[ToplevelComponent.VALUES.length];
	public transient Region lastRegion = null;
	public transient ArrayList<Region> regions = new ArrayList<>();
	public transient UpdateMask[] masks = null;
	public transient ChatUpdate chatUpdate = null;
	public transient MovementModeUpdate movementModeUpdate = null;
	public transient TeleportModeUpdate teleportModeUpdate = null;
	public transient Equipment temporaryEquipment = null;
	public transient Inventory temporaryInventory = null;
	public transient Trade trade = null;
	public transient Duel duel = null;

	public transient Session<Player> rsprotSession;
	public transient NpcInfo rsprotNPCInfo;
	public transient ToAPartyInterface toaPartyInterface;
	public transient CombatAchievementInterface combatAchievementInterface;
	public transient ToaPartyViewInterface toaPartyViewInterface;
	public transient ToaInvocationsInterface toaInvocationsInterface;
	public transient ToaMembersInterface membersInterface;
	public transient ToaApplicantsInterface toaApplicantsInterface;
	public transient ToASuppliesInterface toASuppliesInterface;
	public transient ItemBreakInterface itemBreakInterface;
	public transient ItemUpgradeInterface itemUpgradeInterface;
	public transient GearPresetInterface gearPresetInterface;
	public transient CamelStatueInterface camelStatueInterface;

	protected Runnable onDialogueContinued;

	public transient boolean madeEgniolPotion = false;
	public transient int attunedWeaponsMade = 0;
	public transient boolean gauntletArmourMade = false;


	public transient long lastAutochatMessage = 0;

	public transient int[] lastLoginKeys = new int[0];
	public transient long lastSessionId = 0L;
	public transient long lastPacketTime = 0L;

	@Getter
	public transient HealthHud healthHud;

	public long lastAutoChatMessageElapsed() {
		return System.currentTimeMillis() - lastAutochatMessage;
	}

	public long lastPacketTimeElapsed() {
		return System.currentTimeMillis() - this.lastPacketTime;
	}
}
