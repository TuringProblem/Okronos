package io.ruin.model.var;

import io.ruin.cache.runetek4.vartype.bit.VarBitType;
import io.ruin.model.entity.player.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO move the definitions to a separate place so they are not called
 * VarPlayerRepository because its weird
 * and make the VarPlayerRepository only send varps
 * For a list of already identified varbits and varps go to chisel @
 * https://chisel.weirdgloop.org/varbs/index
 */
public class VarPlayerRepository {

	private static final Set<VarPlayerRepository> CONFIGS = new HashSet<>();

	private static final int[] SHIFTS = new int[32];

	public static final VarPlayerRepository TEMPOROSS_REWARDS = varpbit(11936, false);

	public static final VarPlayerRepository TEMPOROSS_POINTS = varpbit(11897, false);

	public static final VarPlayerRepository COLLECTION_LOG_COMPLETED = varp(2944, false);

	public static final VarPlayerRepository COLLECTION_LOG_TOTAL = varp(2943, false);

	public static final VarPlayerRepository PLAYER_INFO_COMBAT_LVL = varpbit(13027, true);

	public static final VarPlayerRepository PLAYER_INFO_QUEST_TOTAL = varpbit(13027, true);

	public static final VarPlayerRepository PLAYER_INFO_QUESTS_COMPLETE = varpbit(6347, true);

	public static final VarPlayerRepository WELCOME_SCREEN_LOGIN_TIMER = varpbit(261, true);

	public static final VarPlayerRepository PLUNDER_ROOM = varpbit(2377, false).defaultValue(0);
	public static final VarPlayerRepository JOURNAL_TAB_LEAGUES_ENABLED = varpbit(11697, true).defaultValue(0);

	public static final VarPlayerRepository JOURNAL_TAB_ADVENTURE_PATHS_ENABLED = varpbit(9340, true).defaultValue(0);
	public static final VarPlayerRepository GAUNTLET = varpbit(2322, true).defaultValue(0);

	public static final VarPlayerRepository PLUNDER_THIEF_LVL_REQ = varpbit(2376, false).defaultValue(0);

	public static final VarPlayerRepository SUNLIGHT_SPEAR_STACKS = varp(4600, false).defaultValue(0);

	public static final VarPlayerRepository GAUNTLET_REWARD = varpbit(9179, true);
	public static final VarPlayerRepository COLLECTION_PROGRESS = varp(2943, false);
	public static final VarPlayerRepository COLLECTION_COUNT = varp(2944, false);
	public static final VarPlayerRepository WORLD_MAP_OBJECT = varpbit(3984, true);
	public static final VarPlayerRepository RAIDS_BEAM = varpbit(5456, false);

	public static final VarPlayerRepository FREE_SIGIL_THREE = varp(263, true);

	public static final VarPlayerRepository SIGIL_SLOT_ONE = varpbit(13019, true);
	public static final VarPlayerRepository SIGIL_SLOT_TWO = varpbit(13020, true);
	public static final VarPlayerRepository SIGIL_SLOT_THREE = varpbit(13021, true);
	public static final VarPlayerRepository ANCIENT_MAGIC_SPELLS = varpbit(358, true);// 15

	public static final VarPlayerRepository LUNAR_MAGIC_SPELLS = varpbit(2448, true);// 190

	public static final VarPlayerRepository ARCEEUS_MAGIC_SPELLS = varpbit(12296, true);// 150

	public static final VarPlayerRepository HARMONY_ISLAND_TELEPORT = varpbit(980, true);// 150

	public static final VarPlayerRepository COLLECTION_LOG_KC = varp(2048, false);

	public static final VarPlayerRepository SettingSearch = varpbit(9638, false);

	public static final VarPlayerRepository SettingSearch1 = varpbit(9639, false);

	public static final VarPlayerRepository HIDE_ROOF = varpbit(12378, true);

	public static final VarPlayerRepository OWNAFUCKINGHOUSE = varpbit(2187, true);

	public static final VarPlayerRepository GENDER = varpbit(11697, true);
	public static final VarPlayerRepository BODY_TYPE_BUTTON = varpbit(14021, true);

	public static final VarPlayerRepository GENDER_PRONOUN = varpbit(10988, true).defaultValue(0);
	public static final VarPlayerRepository THEATRE_CHEST_33086 = varpbit(6450, false);
	public static final VarPlayerRepository THEATRE_CHEST_33087 = varpbit(6451, false);
	public static final VarPlayerRepository THEATRE_CHEST_33088 = varpbit(6452, false);
	public static final VarPlayerRepository THEATRE_CHEST_33089 = varpbit(6453, false);
	public static final VarPlayerRepository THEATRE_CHEST_33090 = varpbit(6454, false);
	public static final VarPlayerRepository THEATRE_CHEST_33091 = varpbit(6455, false);

	public static final VarPlayerRepository SHOW_STORE_ORB_MOBILE = varpbit(13036, true).defaultValue(1);
	public static final VarPlayerRepository SHOW_STORE_ORB = varpbit(13037, true).defaultValue(1);// bond
	public static final VarPlayerRepository SETTINGS_SEARCH_LEFT = varpbit(9638, false);
	public static final VarPlayerRepository SETTINGS_SEARCH_RIGHT = varpbit(9639, false);
	public static final VarPlayerRepository SETTINGS_AMOUNT_TYPING = varpbit(9647, false);
	public static final VarPlayerRepository SETTINGS_SHOW_MORE = varpbit(9665, false);

	public static final VarPlayerRepository CHAT_NAME_SET = varpbit(8119, true).defaultValue(1);
	// 1 = false, 0 = true
	public static final VarPlayerRepository HITSPLAT_TINTING = varpbit(10236, true);

	public static final VarPlayerRepository BOSS_HEALTH_OVERLAY = varpbit(12389, true);
	public static final VarPlayerRepository LMS_FOG_COLOUR = varpbit(11865, true);// 0-5
	public static final VarPlayerRepository SOUND_EFFECT_VOLUME = varp(169, true);

	public static final VarPlayerRepository AREA_SOUND_EFFECT_VOLUME = varp(872, true);

	public static final VarPlayerRepository MUSIC_VOLUME = varp(168, true);

	public static final VarPlayerRepository MUSIC_AREA_MODE = varpbit(12233, true);
	public static final VarPlayerRepository MUSIC_UNLOCK_MESSAGE = varpbit(10078, true);
	public static final VarPlayerRepository FRIEND_LOGOUT_MESSAGES = varpbit(12274, true);
	public static final VarPlayerRepository CHAT_EFFECTS = varp(171, true);
	public static final VarPlayerRepository SPLIT_PRIVATE_CHAT = varp(287, true).defaultValue(1);
	public static final VarPlayerRepository HIDE_PRIVATE_CHAT = varpbit(4089, true);
	public static final VarPlayerRepository ENABLE_PRECISE_TIMERS = varpbit(11866, true);
	public static final VarPlayerRepository ENABLE_SEPARATE_TIMER_HOURS = varpbit(11890, true);
	public static final VarPlayerRepository COLLECTION_LOG_NOTIFICATION = varpbit(11959, true).defaultValue(0b11);
	public static final VarPlayerRepository DROP_NOTIFICATIONS = varpbit(5399, true);
	public static final VarPlayerRepository DROP_NOTIFICATION_MIN_VALUE = varpbit(5400, true);
	public static final VarPlayerRepository MINIMUM_ALCH_TRIGGER_VALUE = varpbit(6091, true).defaultValue(30000);
	public static final VarPlayerRepository UNTRADABLE_DROP_NOTIFICATIONS = varpbit(5402, true);
	public static final VarPlayerRepository FILTER_BOSS_KC_SPAM = varpbit(4930, true);

	public static final VarPlayerRepository NOTIFY_COMBAT_CHEEVO_FAILURE = varpbit(12454, true);
	public static final VarPlayerRepository NOTIFY_COMBAT_CHEEVO_REPEAT_FAILURE = varpbit(12455, true);
	public static final VarPlayerRepository NOTIFY_COMBAT_CHEEVO_REPEAT_COMPLETION = varpbit(12456, true);
	public static final VarPlayerRepository AUTOSET_CHATMODE = varpbit(13120, true);
	public static final VarPlayerRepository SETTINGS_OPAQUE_CHAT_PUBLIC = varp(2992, true);
	public static final VarPlayerRepository SETTINGS_OPAQUE_CHAT_PRIVATE = varp(2993, true);

	public static final VarPlayerRepository SETTINGS_OPAQUE_CHAT_AUTO = varp(2994, true);
	public static final VarPlayerRepository SETTINGS_OPAQUE_CHAT_BROADCAST = varp(2995, true);
	public static final VarPlayerRepository SETTINGS_OPAQUE_CHAT_FRIEND = varp(2996, true);

	public static final VarPlayerRepository SETTINGS_OPAQUE_CHAT_CLAN = varp(2997, true);
	public static final VarPlayerRepository SETTINGS_OPAQUE_CHAT_GUEST_CLAN = varp(3060, true);
	public static final VarPlayerRepository SETTINGS_OPAQUE_CHAT_CLAN_BROADCAST = varp(3192, true);
	public static final VarPlayerRepository SETTINGS_OPAQUE_CHAT_IRON_GROUP_CHAT = varp(3191, true);
	public static final VarPlayerRepository SETTINGS_OPAQUE_CHAT_IRON_GROUP_BROADCAST = varp(3193, true);
	public static final VarPlayerRepository SETTINGS_OPAQUE_CHAT_TRADE_REQUEST = varp(2998, true);
	public static final VarPlayerRepository SETTINGS_OPAQUE_CHAT_CHALLENGE_REQUEST = varp(2999, true);
	public static final VarPlayerRepository SETTINGS_TRANSPARENT_CHAT_PUBLIC = varp(3000, true);
	public static final VarPlayerRepository SETTINGS_TRANSPARENT_CHAT_PRIVATE = varp(3001, true);
	public static final VarPlayerRepository SETTINGS_TRANSPARENT_CHAT_AUTO = varp(3002, true);
	public static final VarPlayerRepository SETTINGS_TRANSPARENT_CHAT_BROADCAST = varp(3003, true);
	public static final VarPlayerRepository SETTINGS_TRANSPARENT_CHAT_FRIEND = varp(3004, true);
	public static final VarPlayerRepository SETTINGS_TRANSPARENT_CHAT_CLAN = varp(3005, true);
	public static final VarPlayerRepository SETTINGS_TRANSPARENT_CHAT_GUEST_CLAN = varp(3061, true);
	public static final VarPlayerRepository SETTINGS_TRANSPARENT_CHAT_CLAN_BROADCAST = varp(3195, true);
	public static final VarPlayerRepository SETTINGS_TRANSPARENT_CHAT_IRON_GROUP_CHAT = varp(3194, true);
	public static final VarPlayerRepository SETTINGS_TRANSPARENT_CHAT_IRON_GROUP_BROADCAST = varp(3196, true);
	public static final VarPlayerRepository SETTINGS_TRANSPARENT_CHAT_TRADE_REQUEST = varp(3006, true);
	public static final VarPlayerRepository SETTINGS_TRANSPARENT_CHAT_CHALLENGE_REQUEST = varp(3007, true);
	public static final VarPlayerRepository SETTINGS_SPLIT_CHAT_PRIVATE = varp(3008, true);
	public static final VarPlayerRepository SETTINGS_SPLIT_CHAT_BROADCAST = varp(3009, true);
	public static final VarPlayerRepository PLAYER_ATTACK_OPTION = varp(1107, true).forceSend();
	public static final VarPlayerRepository NPC_ATTACK_OPTION = varp(1306, true).forceSend();
	public static final VarPlayerRepository PK_SKULL_PREVENTION = varpbit(13131, true);
	public static final VarPlayerRepository MOUSE_BUTTONS = varp(170, true);
	public static final VarPlayerRepository MOUSE_CAMERA = varpbit(4134, true);
	public static final VarPlayerRepository SHIFT_DROP = varpbit(5542, true);
	public static final VarPlayerRepository FOLLOWER_PRIORITY = varpbit(5599, true);
	public static final VarPlayerRepository DROP_ITEM_WARNING_ENABLED = varpbit(5411, true);
	public static final VarPlayerRepository CTRLCLICK_INVERT_RUN = varpbit(13132, true);
	public static final VarPlayerRepository HOTKEY_CLOSE_SIDEPANEL = varpbit(4611, true);
	public static final VarPlayerRepository DROP_ITEM_WARNING_VALUE = varpbit(5412, true).defaultValue(30000);
	public static final VarPlayerRepository SETTINGS_CURRENT_CATEGORY = varpbit(9656, true);
	public static final VarPlayerRepository SETTINGS_CURRENT_SETTING = varpbit(9657, true);
	public static final VarPlayerRepository SETTINGS_MAKE_X_DARTS_AND_BOLTS = varpbit(10971, true);
	// editing basevarp 2855
	public static final VarPlayerRepository MEMBERSHIP_DAYS = varp(1780, true);
	/**
	 * Default options
	 */
	public static final VarPlayerRepository BARS_IN_DESPENSER = varpbit(936, false);

	public static final VarPlayerRepository PROFANITY_FILTER = varp(1074, true);

	// TODO display, warnings, interfaces gameplay settings variables

	public static final VarPlayerRepository QUEST_ACTIVE_TAB = varpbit(8168, true);
	public static final VarPlayerRepository CLAN_ACTIVE_TAB = varpbit(13071, true);

	public static final VarPlayerRepository CLEANING_TABLE = varpbit(5801, true);

	public static final VarPlayerRepository HEALTH_HUD_NPC = varp(1683, false);

	public static final VarPlayerRepository HEALTH_HUD_CURRENT = varpbit(6099, false);

	public static final VarPlayerRepository HEALTH_HUD_MAX = varpbit(6100, false);
	public static final VarPlayerRepository HEALTH_HUD_BAR = varpbit(12401, false);
	public static final VarPlayerRepository GIM_SIDE_PANEL = varp(3172, true);

	public static final VarPlayerRepository FRIEND_NOTIFICATION_TIMEOUT = varpbit(1627, true);

	public static final VarPlayerRepository COMBAT_ACHIVEMENTS_TASKS_TIER = varpbit(12858, true);
	public static final VarPlayerRepository COMBAT_ACHIVEMENTS_TASKS_TYPE = varpbit(12859, true);
	public static final VarPlayerRepository COMBAT_ACHIVEMENTS_TASKS_MONSTER = varpbit(12860, true);
	public static final VarPlayerRepository COMBAT_ACHIVEMENTS_TASKS_COMPLETED_TASKS = varpbit(12861, true);
	public static final VarPlayerRepository COMBAT_ACHIVEMENTS_REWARDS = varpbit(12862, true);// max value is 49

	/**
	 * Combat achieve Easy
	 */
	public static final VarPlayerRepository NOXIOUS_FOE = varpbit(12458, true);
	public static final VarPlayerRepository BARROWS_NOVICE = varpbit(12482, true);
	public static final VarPlayerRepository DEFENCE_WHAT_DEFENCE = varpbit(12486, true);
	public static final VarPlayerRepository BIG_BLACK_AND_FIERY = varpbit(12490, true);
	public static final VarPlayerRepository THE_DEMONIC_PUNCHING_BAG = varpbit(12491, true);

	public static final VarPlayerRepository BRYOPHYTA_NOVICE = varpbit(12493, true);

	public static final VarPlayerRepository PROTECTION_FROM_MOSS = varpbit(12495, true);
	public static final VarPlayerRepository PROTECTION_IS_KEY = varpbit(12497, true);
	public static final VarPlayerRepository A_SLOW_DEATH = varpbit(12498, true);
	public static final VarPlayerRepository FIGHTING_AS_INTENTED_II = varpbit(12499, true);
	public static final VarPlayerRepository DERANGED_ARCHAEOLOGIST_NOVICE = varpbit(12534, true);
	public static final VarPlayerRepository THE_WALKING_VOLCANO = varpbit(12538, true);
	public static final VarPlayerRepository A_GREATER_FOE = varpbit(12578, true);
	public static final VarPlayerRepository NOT_SO_GREAT_AFTER_ALL = varpbit(12579, true);
	public static final VarPlayerRepository A_DEMONS_BEST_FRIEND = varpbit(12580, true);
	public static final VarPlayerRepository OBOR_NOVICE = varpbit(12587, true);
	public static final VarPlayerRepository SLEEPING_GIANT = varpbit(12589, true);
	public static final VarPlayerRepository FIGHTING_AS_INTENDED = varpbit(12592, true);
	public static final VarPlayerRepository KING_BLACK_DRAGON_NOVICE = varpbit(12620, true);
	public static final VarPlayerRepository A_SCALEY_ENCOUNTER = varpbit(12632, true);
	public static final VarPlayerRepository GIANT_MOLE_NOVICE = varpbit(12635, true);
	public static final VarPlayerRepository SARACHNIS_NOVICE = varpbit(12664, true);
	public static final VarPlayerRepository WINTERTODT_NOVICE = varpbit(12737, true);
	public static final VarPlayerRepository MUMMY = varpbit(12739, true);
	public static final VarPlayerRepository HANDYMAN = varpbit(12740, true);
	public static final VarPlayerRepository COSY = varpbit(12743, true);
	public static final VarPlayerRepository A_SLITHERY_ENCOUNTER = varpbit(12745, true);
	public static final VarPlayerRepository TEMPOROSS_NOVICE = varpbit(12811, true);
	public static final VarPlayerRepository MASTER_OF_BUCKETS = varpbit(12814, true);
	public static final VarPlayerRepository CALM_BEFORE_THE_STORM = varpbit(12815, true);
	public static final VarPlayerRepository FIRE_IN_THE_HOLE = varpbit(12818, true);
	public static final VarPlayerRepository INTO_THE_DEN_OF_GIANTS = varpbit(12856, true);
	/**
	 * Combat achieve Medium
	 */
	public static final VarPlayerRepository BARROWS_CHAMPION = varpbit(12483, true);
	public static final VarPlayerRepository CANT_TOUCH_ME = varpbit(12484, true);
	public static final VarPlayerRepository PRAY_FOR_SUCCESS = varpbit(12485, true);
	public static final VarPlayerRepository BRUTAL_BIG_BLACK_AND_FIERY = varpbit(12492, true);
	public static final VarPlayerRepository BRYOPHYTA_CHAMPION = varpbit(12494, true);
	public static final VarPlayerRepository QUICK_CUTTER = varpbit(12496, true);

	public static final VarPlayerRepository SKOTIZO_ADEPT = varpbit(12502, true);
	public static final VarPlayerRepository DEMONIC_WEAKENING = varpbit(12504, true);
	public static final VarPlayerRepository DEMONBANE_WEAPONRY = varpbit(12506, true);
	public static final VarPlayerRepository CHAOS_FANATIC_CHAMPION = varpbit(12519, true);
	public static final VarPlayerRepository SORRY_WHAT_WAS_THAT = varpbit(12521, true);
	public static final VarPlayerRepository CRAY_ARCHAEOLOGIST_CHAMPION = varpbit(12528, true);
	public static final VarPlayerRepository MAGE_OF_THE_RUINS = varpbit(12530, true);
	public static final VarPlayerRepository ID_RATHER_NOT_LEARN = varpbit(12531, true);
	public static final VarPlayerRepository DERANGED_ARCHAEOLOGIST_CHAMPION = varpbit(12535, true);
	public static final VarPlayerRepository MAGE_OF_THE_SWAMP = varpbit(12536, true);
	public static final VarPlayerRepository ID_RATHER_BE_ILLITERATE = varpbit(12537, true);
	public static final VarPlayerRepository A_SMASHING_TIME = varpbit(12555, true);
	public static final VarPlayerRepository OBOR_CHAMPION = varpbit(12588, true);
	public static final VarPlayerRepository BACK_TO_THE_WALL = varpbit(12590, true);
	public static final VarPlayerRepository SQUASHING_THE_GIANT = varpbit(12591, true);
	public static final VarPlayerRepository KING_BLACK_DRAGON_CHAMPION = varpbit(12621, true);
	public static final VarPlayerRepository CLAW_CLIPPER = varpbit(12622, true);
	public static final VarPlayerRepository HIDE_PENETRATION = varpbit(12623, true);
	public static final VarPlayerRepository ANTIFIRE_PROTECTION = varpbit(12624, true);
	public static final VarPlayerRepository MASTER_OF_BROAD_WEAPONRY = varpbit(12631, true);
	public static final VarPlayerRepository GIANT_MOLE_CHAMPION = varpbit(12636, true);
	public static final VarPlayerRepository AVOIDING_THOSE_LITTLE_ARMS = varpbit(12640, true);
	public static final VarPlayerRepository DAGANNOTH_PRIME_CHAMPION = varpbit(12655, true);
	public static final VarPlayerRepository DAGANNOTH_REX_CHAMPION = varpbit(12659, true);
	public static final VarPlayerRepository A_FROZEN_KING = varpbit(12662, true);
	public static final VarPlayerRepository SARACHNIS_CHAMPION = varpbit(12665, true);
	public static final VarPlayerRepository NEWSPAPER_ENTHUSIAST = varpbit(12668, true);
	public static final VarPlayerRepository A_FROZEN_FOE_FROM_THE_PAST = varpbit(12681, true);
	public static final VarPlayerRepository DAGANNOTH_SUPREME_CHAMPION = varpbit(12691, true);
	public static final VarPlayerRepository WINTERTODT_CHAMPION = varpbit(12738, true);
	public static final VarPlayerRepository CAN_WE_FIX_IT = varpbit(12741, true);
	public static final VarPlayerRepository LEAVING_NO_ONE_BEHIND = varpbit(12742, true);
	public static final VarPlayerRepository TEMPOROSS_CHAMPION = varpbit(12812, true);
	public static final VarPlayerRepository THE_LONE_ANGLER = varpbit(12817, true);
	public static final VarPlayerRepository SIT_BACK_AND_RELAX = varpbit(12857, true);
	/**
	 * Combat achieve Hard
	 */
	public static final VarPlayerRepository ABYSSAL_ADEPT = varpbit(12459, true);
	public static final VarPlayerRepository THEY_GROW_UP_TOO_FAST = varpbit(12461, true);
	public static final VarPlayerRepository DONT_WHIP_ME = varpbit(12463, true);
	public static final VarPlayerRepository DONT_STOP_MOVING = varpbit(12465, true);
	public static final VarPlayerRepository KREE_ARRA_ADEPT = varpbit(12467, true);
	public static final VarPlayerRepository GENERAL_GRAARDOR_ADEPT = varpbit(12474, true);

	public static final VarPlayerRepository OURG_FREEER = varpbit(12476, true);
	public static final VarPlayerRepository GENERAL_SHOWDOWN = varpbit(12478, true);
	public static final VarPlayerRepository JUST_LIKE_THAT = varpbit(12487, true);
	public static final VarPlayerRepository FAITHLESS_CRYPT_RUN = varpbit(12488, true);
	public static final VarPlayerRepository CALLISTO_ADEPT = varpbit(12500, true);
	public static final VarPlayerRepository SKOTIZO_VETERAN = varpbit(12503, true);
	public static final VarPlayerRepository CHAOS_ELEMENTAL_ADEPT = varpbit(12515, true);
	public static final VarPlayerRepository HOARDER = varpbit(12517, true);
	public static final VarPlayerRepository THE_FLINCHER = varpbit(12518, true);
	public static final VarPlayerRepository CHAOS_FANATIC_ADEPT = varpbit(12520, true);
	public static final VarPlayerRepository PRAYING_TO_THE_GODS = varpbit(12522, true);
	public static final VarPlayerRepository CRAZY_ARCHAEOLOGIST_ADEPT = varpbit(12529, true);
	public static final VarPlayerRepository GROTESQUE_GUARDIANS_ADEPT = varpbit(12540, true);
	public static final VarPlayerRepository DONT_LOOK_AT_THE_ECLIPSE = varpbit(12542, true);
	public static final VarPlayerRepository PRISON_BREAK = varpbit(12543, true);
	public static final VarPlayerRepository GRANITE_FOOTWORK = varpbit(12544, true);
	public static final VarPlayerRepository HEAL_NO_MORE = varpbit(12545, true);
	public static final VarPlayerRepository STATIC_AWARENESS = varpbit(12546, true);
	public static final VarPlayerRepository HESPORI_ADEPT = varpbit(12581, true);
	public static final VarPlayerRepository HESPORISNT = varpbit(12582, true);
	public static final VarPlayerRepository WEED_WHACKER = varpbit(12583, true);
	public static final VarPlayerRepository KALPHITE_QUEEN_ADEPT = varpbit(12615, true);
	public static final VarPlayerRepository CHITIN_PENETRATOR = varpbit(12617, true);
	public static final VarPlayerRepository WHO_IS_THE_KING_NOW = varpbit(12625, true);
	public static final VarPlayerRepository KRAKEN_ADEPT = varpbit(12626, true);
	public static final VarPlayerRepository UNNECESSARY_OPTIMIZATION = varpbit(12627, true);
	public static final VarPlayerRepository KRAKANT_HURT_ME = varpbit(12623, true);
	public static final VarPlayerRepository WHY_ARE_YOU_RUNNING = varpbit(12637, true);
	public static final VarPlayerRepository WHACK_A_MOLE = varpbit(12639, true);
	public static final VarPlayerRepository NIGHTMARE_CHAMPION = varpbit(12641, true);
	public static final VarPlayerRepository DAGANNOTH_PRIME_ADEPT = varpbit(12656, true);
	public static final VarPlayerRepository DAGANNOTH_REX_ADEPT = varpbit(12660, true);
	public static final VarPlayerRepository READY_TO_POUNCE = varpbit(12666, true);
	public static final VarPlayerRepository INSPECT_REPELLENT = varpbit(12667, true);
	public static final VarPlayerRepository COMMANDER_ZILYANA_ADEPT = varpbit(12669, true);
	public static final VarPlayerRepository COMMANDER_ZILYANA_SHOWDOWN = varpbit(12671, true);
	public static final VarPlayerRepository SCORPIA_ADEPT = varpbit(12676, true);
	public static final VarPlayerRepository I_CANT_REACH_THAT = varpbit(12678, true);
	public static final VarPlayerRepository GUARDIANS_NO_MORE = varpbit(12679, true);
	public static final VarPlayerRepository ZULRAH_ADEPT = varpbit(12682, true);
	public static final VarPlayerRepository DAGANNOTH_SUPREME_ADEPT = varpbit(12692, true);
	public static final VarPlayerRepository VENENATIS_ADEPT = varpbit(12722, true);
	public static final VarPlayerRepository VETION_ADEPT = varpbit(12724, true);
	public static final VarPlayerRepository WHY_FLETCH = varpbit(12744, true);
	public static final VarPlayerRepository KRIL_TSUTSAROTH_ADEPT = varpbit(12790, true);
	public static final VarPlayerRepository YARR_NO_MORE = varpbit(12792, true);
	public static final VarPlayerRepository DEMONIC_SHOWDOWN = varpbit(12793, true);
	public static final VarPlayerRepository DEMONBANE_WEAPONRY_II = varpbit(12797, true);
	public static final VarPlayerRepository DRESS_LIKE_YOU_MEAN_IT = varpbit(12813, true);
	public static final VarPlayerRepository WHY_COOK = varpbit(12816, true);
	public static final VarPlayerRepository THEATRE_OF_BLOOD_SM_ADEPT = varpbit(12855, true);
	/**
	 * Combat Achieve Elite
	 */
	public static final VarPlayerRepository ABYSSAL_VETERAN = varpbit(12460, true);
	public static final VarPlayerRepository DEMONIC_REBOUND = varpbit(12464, true);
	public static final VarPlayerRepository PERFECT_SIRE = varpbit(12466, true);
	public static final VarPlayerRepository KREE_ARRA_VETERAN = varpbit(12468, true);
	public static final VarPlayerRepository GENERAL_GRAARDOR_VETERAN = varpbit(12475, true);
	public static final VarPlayerRepository RESPIRATORY_RUNNER = varpbit(12462, true);

	public static final VarPlayerRepository OURG_FREEZER_TWO = varpbit(12477, true);
	public static final VarPlayerRepository REFLECTING_ON_THIS_ENCOUNTER = varpbit(12489, true);
	public static final VarPlayerRepository CALLISTO_VETERAN = varpbit(12501, true);
	public static final VarPlayerRepository DEMON_VETERAN = varpbit(12505, true);
	public static final VarPlayerRepository UP_FOR_THE_CHALLENGE = varpbit(12507, true);
	public static final VarPlayerRepository CERBERUS_VETERAN = varpbit(12509, true);
	public static final VarPlayerRepository GHOST_BUSTER = varpbit(12512, true);
	public static final VarPlayerRepository UNREQUIRED_ANTIFIRE = varpbit(12513, true);
	public static final VarPlayerRepository ANTI_BITE_MECHANICS = varpbit(12514, true);
	public static final VarPlayerRepository CHAOS_ELEMENTAL_VETERAN = varpbit(12516, true);
	public static final VarPlayerRepository CORPOREAL_BEAST_VETERAN = varpbit(12523, true);
	public static final VarPlayerRepository HOT_ON_YOUR_FEET = varpbit(12525, true);
	public static final VarPlayerRepository FINDING_THE_WEAK_SPOT = varpbit(12526, true);
	public static final VarPlayerRepository CHICKEN_KILLER = varpbit(12527, true);
	public static final VarPlayerRepository IF_GORILLAS_COULD_FLY = varpbit(12532, true);
	public static final VarPlayerRepository HITTING_THEM_WHERE_IT_HURTS = varpbit(12533, true);
	public static final VarPlayerRepository GALVEK_SPEED_TRIALIST = varpbit(12539, true);
	public static final VarPlayerRepository GROTESQUE_GUARDIANS_VETERAN = varpbit(12541, true);
	public static final VarPlayerRepository DONE_BEFORE_DUSK = varpbit(12547, true);
	public static final VarPlayerRepository PERFECT_GROTESQUE_GUARDIANS = varpbit(12548, true);
	public static final VarPlayerRepository GROTESQUE_GUARDIANS_SPEED_TRIALIST = varpbit(12550, true);
	public static final VarPlayerRepository FROM_DUSK = varpbit(12553, true);
	public static final VarPlayerRepository CORRUPTED_GAUNTLET_VETERAN = varpbit(12556, true);
	public static final VarPlayerRepository THREE_TWO_ONE_MAGE = varpbit(12559, true);
	public static final VarPlayerRepository GAUNTLET_ADEPT = varpbit(12567, true);
	public static final VarPlayerRepository THREE_TWO_ONE_RANGE = varpbit(12569, true);
	public static final VarPlayerRepository EGNIOL_DIET = varpbit(12572, true);
	public static final VarPlayerRepository CRYSTALLINE_WARRIOR = varpbit(12573, true);
	public static final VarPlayerRepository WOLF_PUNCHER = varpbit(12574, true);
	public static final VarPlayerRepository GLOUGH_SPEED_TRIALIST = varpbit(12577, true);
	public static final VarPlayerRepository PLANT_BASED_DIET = varpbit(12584, true);
	public static final VarPlayerRepository HESPORI_SPEED_TRIALIST = varpbit(12585, true);
	public static final VarPlayerRepository ALCHEMICAL_MASTER = varpbit(12593, true);
	public static final VarPlayerRepository FIGHT_CAVES_VETERAN = varpbit(12605, true);
	public static final VarPlayerRepository A_NEAR_MISS = varpbit(12607, true);
	public static final VarPlayerRepository FACING_JAD_HEAD_ON = varpbit(12611, true);
	public static final VarPlayerRepository KALPHITE_QUEEN_VETERAN = varpbit(12616, true);
	public static final VarPlayerRepository INSECT_DEFLECTION = varpbit(12618, true);
	public static final VarPlayerRepository PRAYER_SMASHER = varpbit(12619, true);
	public static final VarPlayerRepository TEN_TACLES = varpbit(12629, true);
	public static final VarPlayerRepository MIMIC_ADEPT = varpbit(12634, true);
	public static final VarPlayerRepository HARD_HITTER = varpbit(12638, true);
	public static final VarPlayerRepository NIGHTMARE_ADEPT = varpbit(12642, true);
	public static final VarPlayerRepository EXPLOSION = varpbit(12645, true);
	public static final VarPlayerRepository SLEEP_TIGHT = varpbit(12647, true);
	public static final VarPlayerRepository NIGHTMARE_5_SCALE_SPEED_TRIALIST = varpbit(12652, true);
	public static final VarPlayerRepository DEATH_TO_THE_SEER_KING = varpbit(12657, true);
	public static final VarPlayerRepository FROM_ONE_KING_TO_ANOTHER = varpbit(12658, true);
	public static final VarPlayerRepository DEATH_TO_THE_WARRIOR_KING = varpbit(12661, true);
	public static final VarPlayerRepository TOPPLING_THE_DIARCHY = varpbit(12663, true);
	public static final VarPlayerRepository COMMANDER_ZILYANA_VETERAN = varpbit(12670, true);
	public static final VarPlayerRepository REMINISCE = varpbit(12674, true);
	public static final VarPlayerRepository SCORPIA_VETERAN = varpbit(12677, true);
	public static final VarPlayerRepository FRAGMENT_OF_SEREN_SPEED_TRIALIST = varpbit(12680, true);
	public static final VarPlayerRepository ZULRAH_VETERAN = varpbit(12683, true);
	public static final VarPlayerRepository SNAKE_REBOUND = varpbit(12685, true);
	public static final VarPlayerRepository SNAKE_SNAKE_SNAAAAAAAAKE = varpbit(12686, true);
	public static final VarPlayerRepository ZULRAH_SPEED_TRIALIST = varpbit(12688, true);
	public static final VarPlayerRepository DEATH_TO_THE_ARCHER_KING = varpbit(12693, true);
	public static final VarPlayerRepository RAPID_SUCCESSION = varpbit(12694, true);
	public static final VarPlayerRepository THEATRE_OF_BLOOD_VETERAN = varpbit(12695, true);
	public static final VarPlayerRepository THERMONUCLEAR_ADEPT = varpbit(12719, true);
	public static final VarPlayerRepository HAZARD_PREVENTION = varpbit(12720, true);
	public static final VarPlayerRepository SPECD_OUT = varpbit(12721, true);
	public static final VarPlayerRepository VENENATIS_VETERAN = varpbit(12723, true);
	public static final VarPlayerRepository VETERAN = varpbit(12725, true);
	public static final VarPlayerRepository VORKATH_VETERAN = varpbit(12726, true);
	public static final VarPlayerRepository ZOMBIE_DESTROYER = varpbit(12729, true);
	public static final VarPlayerRepository STICK_EM_WITH_THE_POINTY_END = varpbit(12731, true);
	public static final VarPlayerRepository DUST_SEEKER = varpbit(12750, true);
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_VETERAN = varpbit(12757, true);
	public static final VarPlayerRepository PERFECTLY_BALANCED = varpbit(12760, true);
	public static final VarPlayerRepository TOGETHER_WE_FALL = varpbit(12761, true);
	public static final VarPlayerRepository MUTTA_DIET = varpbit(12765, true);
	public static final VarPlayerRepository REDEMPTION_ENTHUSIAST = varpbit(12766, true);
	public static final VarPlayerRepository DANCING_WITH_STATUES = varpbit(12769, true);
	public static final VarPlayerRepository UNDYING_RAID_TEAM = varpbit(12770, true);
	public static final VarPlayerRepository SHAYZIEN_SPECIALIST = varpbit(12772, true);
	public static final VarPlayerRepository CRYO_NO_MORE = varpbit(12774, true);
	public static final VarPlayerRepository BLIZZARD_DODGER = varpbit(12778, true);
	public static final VarPlayerRepository KILL_IT_WITH_FIRE = varpbit(12779, true);
	public static final VarPlayerRepository ZALCANO_VETERAN = varpbit(12786, true);
	public static final VarPlayerRepository PERFECT_ZALCANO = varpbit(12787, true);
	public static final VarPlayerRepository TEAM_PLAYER = varpbit(12788, true);
	public static final VarPlayerRepository THE_SPURNED_HERO = varpbit(12789, true);
	public static final VarPlayerRepository KRIL_TSUTSAROTH_VETERAN = varpbit(12791, true);
	public static final VarPlayerRepository THE_BANE_OF_DEMONS = varpbit(12794, true);
	public static final VarPlayerRepository DEMONIC_DEFENCE = varpbit(12795, true);
	public static final VarPlayerRepository HALF_WAY_THERE = varpbit(12799, true);
	public static final VarPlayerRepository THE_II_JAD_CHALLENGE = varpbit(12819, true);
	public static final VarPlayerRepository TZHAAR_KET_RAK_SPEED_TRIALIST = varpbit(12822, true);
	public static final VarPlayerRepository FACING_JAD_HEAD_ON_III = varpbit(12825, true);
	public static final VarPlayerRepository ANTICOAGULANTS = varpbit(12844, true);
	public static final VarPlayerRepository APPROPRIATE_TOOLS = varpbit(12845, true);
	public static final VarPlayerRepository THEY_WONT_EXPECT_THIS = varpbit(12845, true);
	public static final VarPlayerRepository CHALLY_TIME = varpbit(12847, true);
	public static final VarPlayerRepository NYLOCAS_ON_THE_ROCKS = varpbit(12848, true);
	public static final VarPlayerRepository JUST_TO_BE_SAFE = varpbit(12849, true);
	public static final VarPlayerRepository DONT_LOOK_AT_ME = varpbit(12850, true);
	public static final VarPlayerRepository NO_PILLAR = varpbit(12851, true);
	public static final VarPlayerRepository ATTACK_STEP_WAIT = varpbit(12852, true);
	public static final VarPlayerRepository PASS_IT_ON = varpbit(12853, true);
	/**
	 * Combat Achieve Master
	 */
	public static final VarPlayerRepository COLLATERAL_DAMAGE = varpbit(12469, true);
	public static final VarPlayerRepository SWOOP_NO_MORE = varpbit(12471, true);
	public static final VarPlayerRepository PRECISE_POSITIONING = varpbit(12508, true);
	public static final VarPlayerRepository CERBERUS_MASTER = varpbit(12510, true);
	public static final VarPlayerRepository AROOO_NO_MORE = varpbit(12511, true);
	public static final VarPlayerRepository CORPOREAL_BEAST_MASTER = varpbit(12524, true);

	public static final VarPlayerRepository PERFECT_GROTESQUE_GUARDIANS_II = varpbit(12549, true);
	public static final VarPlayerRepository GROTESQUE_GURADIANS_SPEED_CHASER = varpbit(12551, true);
	public static final VarPlayerRepository TIL_DAWN = varpbit(12554, true);
	public static final VarPlayerRepository CORRUPTED_GAUNTLET_MASTER = varpbit(12557, true);
	public static final VarPlayerRepository PERFECT_CORRUPTED_HUNLLEF = varpbit(12560, true);
	public static final VarPlayerRepository DEFENCE_DOESNT_MATTER_II = varpbit(12561, true);
	public static final VarPlayerRepository CORRUPTED_WARRIOR = varpbit(12563, true);
	public static final VarPlayerRepository CORRUPTED_GAUNTLET_SPEED_CHASER = varpbit(12565, true);
	public static final VarPlayerRepository GAUNTLET_MASTER = varpbit(12568, true);
	public static final VarPlayerRepository PERFECT_CRSTALLINE_HUNLLEF = varpbit(12570, true);
	public static final VarPlayerRepository DEFENCE_DOESNT_MATTER = varpbit(12571, true);
	public static final VarPlayerRepository GAUNTLET_SPEED_CHASER = varpbit(12575, true);
	public static final VarPlayerRepository HESPORI_SPEED_CHASER = varpbit(12586, true);
	public static final VarPlayerRepository ALCHEMICAL_GRANDMASTER = varpbit(12594, true);
	public static final VarPlayerRepository UNREQUIRED_ANTIPOISONS = varpbit(12595, true);
	public static final VarPlayerRepository LIGHTNING_LURE = varpbit(12596, true);
	public static final VarPlayerRepository DONT_FLAME_ME = varpbit(12597, true);
	public static final VarPlayerRepository MIXING_CORRECTLY = varpbit(12598, true);
	public static final VarPlayerRepository THE_FLAME_SKIPPER = varpbit(12599, true);
	public static final VarPlayerRepository ALCLEANICAL_HYDRA = varpbit(12600, true);
	public static final VarPlayerRepository ALCHEMICAL_SPEED_CHASER = varpbit(12602, true);
	public static final VarPlayerRepository WORKING_OVERTIME = varpbit(12604, true);
	public static final VarPlayerRepository FIGHT_CAVES_MASTER = varpbit(12606, true);
	public static final VarPlayerRepository DENYING_THE_HEALERS = varpbit(12608, true);
	public static final VarPlayerRepository YOU_DIDNT_SAY_ANYTHING_ABOUT_A_BAT = varpbit(12610, true);
	public static final VarPlayerRepository FIGHT_CAVES_SPEED_CHASER = varpbit(12613, true);
	public static final VarPlayerRepository ONE_HUNDRED_TENTACLES = varpbit(12630, true);
	public static final VarPlayerRepository NIGHTMARE_VETERAN = varpbit(12643, true);
	public static final VarPlayerRepository PERFECT_NIGHTMARE = varpbit(12646, true);
	public static final VarPlayerRepository NIGHTMARE_SOLO_SPEED_TRIALIST = varpbit(12649, true);
	public static final VarPlayerRepository NIGHTMARE_SOLO_SPEED_CHASER = varpbit(12650, true);
	public static final VarPlayerRepository NIGHTMARE_FIVE_SCALE_SPEED_CHASER = varpbit(12653, true);
	public static final VarPlayerRepository MOVING_COLLATERAL = varpbit(12673, true);
	public static final VarPlayerRepository ZULRAH_MASTER = varpbit(12684, true);
	public static final VarPlayerRepository PERFECT_ZULRAH = varpbit(12687, true);
	public static final VarPlayerRepository ZULRAH_SPEED_CHASER = varpbit(12689, true);
	public static final VarPlayerRepository THEATRE_OF_BLOOD_MASTER = varpbit(12697, true);
	public static final VarPlayerRepository POP_IT = varpbit(12699, true);
	public static final VarPlayerRepository A_TIMELY_SNACK = varpbit(12700, true);
	public static final VarPlayerRepository PERFECT_MAIDEN = varpbit(12702, true);
	public static final VarPlayerRepository PERFECT_BLOAT = varpbit(12703, true);
	public static final VarPlayerRepository PERFECT_NYLOCAS = varpbit(12704, true);
	public static final VarPlayerRepository PERFECT_SOTESTEG = varpbit(12705, true);
	public static final VarPlayerRepository PERFECT_XARPUS = varpbit(12706, true);
	public static final VarPlayerRepository PERFECT_VERZIK = varpbit(12707, true);
	public static final VarPlayerRepository CANT_DRAIN_THIS = varpbit(12708, true);
	public static final VarPlayerRepository CAN_YOU_DANCE = varpbit(12709, true);
	public static final VarPlayerRepository BACK_IN_MY_DAY = varpbit(12711, true);
	public static final VarPlayerRepository THEATRE_TRIO_SPEED_CHASER = varpbit(12713, true);
	public static final VarPlayerRepository THEATRE_FOUR_SCALE_SPEED_CHASER = varpbit(12715, true);
	public static final VarPlayerRepository THEATRE_FIVE_SCALE_SPEED_CHASER = varpbit(12717, true);
	public static final VarPlayerRepository VORKATH_MASTER = varpbit(12727, true);
	public static final VarPlayerRepository THE_WALK = varpbit(12728, true);
	public static final VarPlayerRepository DODGING_THE_DRAGON = varpbit(12730, true);
	public static final VarPlayerRepository VORKATH_SPEED_CHASER = varpbit(12734, true);
	public static final VarPlayerRepository EXTENDED_ENCOUNTER = varpbit(12736, true);
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_CM_MASTER = varpbit(12746, true);
	public static final VarPlayerRepository IMMORTAL_RAID_TEAM = varpbit(12748, true);
	public static final VarPlayerRepository IMMORTAL_RAIDER = varpbit(12749, true);
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_SOLO_CM_SPEED_CHASER = varpbit(12751, true);
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_FIVE_SCALE_CM_SPEED_CHASER = varpbit(12753, true);
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_CM_TRIO_SPEED_CHASER = varpbit(12755, true);
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_MASTER = varpbit(12758, true);
	public static final VarPlayerRepository NO_TIME_FOR_DEATH = varpbit(12762, true);
	public static final VarPlayerRepository PUTTING_IT_OLM_ON_THE_LINE = varpbit(12763, true);
	public static final VarPlayerRepository A_NOT_SO_SPECIAL_LIZARD = varpbit(12764, true);
	public static final VarPlayerRepository STOP_DROP_AND_ROLL = varpbit(12767, true);
	public static final VarPlayerRepository ANVIL_NO_MORE = varpbit(12768, true);
	public static final VarPlayerRepository UNDYING_RAIDER = varpbit(12771, true);
	public static final VarPlayerRepository PLAYING_WITH_LASERS = varpbit(12773, true);
	public static final VarPlayerRepository PERFECT_OLM_SOLO = varpbit(12775, true);
	public static final VarPlayerRepository PERFECT_OLM_TRIO = varpbit(12776, true);
	public static final VarPlayerRepository BLIND_SPOT = varpbit(12777, true);
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_SOLO_SPEED_CHASER = varpbit(12780, true);
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_FIVE_SCALE_SPEED_CHASER = varpbit(12782, true);
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_TRIO_SPEED_CHASER = varpbit(12784, true);
	public static final VarPlayerRepository NIBBLERS_BEGONE = varpbit(12804, true);
	public static final VarPlayerRepository THE_IV_JAD_CHALLENGE = varpbit(12820, true);
	public static final VarPlayerRepository TZHAAR_KET_RAK_SPEED_CHASER = varpbit(12823, true);
	public static final VarPlayerRepository FACING_JAD_HEAD_ON_IV = varpbit(12826, true);
	public static final VarPlayerRepository SUPPLIES_WHO_NEEDS_EM = varpbit(12827, true);
	public static final VarPlayerRepository MULTI_STYLE_SPECIALIST = varpbit(12829, true);
	public static final VarPlayerRepository HARD_MORE_COMPLETED_IT = varpbit(12839, true);
	public static final VarPlayerRepository THEATRE_OF_BLOOD_SM_SPEED_CHASER = varpbit(12854, true);
	/**
	 * Combat Achieve GrandMaster
	 */
	public static final VarPlayerRepository THE_WORST_RANGED_WEAPON = varpbit(12472, true);
	public static final VarPlayerRepository FEATHER_HUNTER = varpbit(12473, true);
	public static final VarPlayerRepository DEFENCE_MATTERS = varpbit(12479, true);
	public static final VarPlayerRepository KEEP_AWAY = varpbit(12480, true);
	public static final VarPlayerRepository OURG_KILLER = varpbit(12481, true);
	public static final VarPlayerRepository GROTESQUE_GUARDIANS_SPEED_RUNNER = varpbit(12552, true);

	public static final VarPlayerRepository CORRUPTED_GAUNTLET_GRANDMASTER = varpbit(12558, true);
	public static final VarPlayerRepository ENGINOL_DIET_II = varpbit(12562, true);
	public static final VarPlayerRepository WOLF_PUNCHER_II = varpbit(12564, true);
	public static final VarPlayerRepository CORRUPTED_GAUNTLET_SPEED_RUNNER = varpbit(12566, true);
	public static final VarPlayerRepository GAUNTLET_SPEED_RUNNER = varpbit(12576, true);
	public static final VarPlayerRepository NO_PRESSURE = varpbit(12601, true);
	public static final VarPlayerRepository ALCHEMICAL_SPEED_RUNNER = varpbit(12603, true);
	public static final VarPlayerRepository DENYING_THE_HEALERS_II = varpbit(12609, true);
	public static final VarPlayerRepository NO_TIME_FOR_A_DRINK = varpbit(12612, true);
	public static final VarPlayerRepository FIGHT_CAVES_SPEED_RUNNER = varpbit(12614, true);
	public static final VarPlayerRepository TERRIBLE_PARENT = varpbit(12644, true);
	public static final VarPlayerRepository A_LONG_TRIP = varpbit(12648, true);
	public static final VarPlayerRepository NIGHTMARE_SOLO_SPEED_RUNNER = varpbit(12651, true);
	public static final VarPlayerRepository NIGHTMARE_FIVE_SCALE_SPEED_RUNNER = varpbit(12654, true);
	public static final VarPlayerRepository ANIMAL_WHISPERER = varpbit(12672, true);
	public static final VarPlayerRepository PEACH_CONJURER = varpbit(12675, true);
	public static final VarPlayerRepository ZULRAH_SPEED_RUNNER = varpbit(12690, true);
	public static final VarPlayerRepository THEATRE_OF_BLOOD_GRANDMASTER = varpbit(12698, true);
	public static final VarPlayerRepository PERFECT_THEATRE = varpbit(12701, true);
	public static final VarPlayerRepository MORYTANIA_ONLY = varpbit(12710, true);
	public static final VarPlayerRepository THEATRE_DUO_SPEED_RUNNER = varpbit(12712, true);
	public static final VarPlayerRepository THEATRE_TRIO_SPEED_RUNNER = varpbit(12714, true);
	public static final VarPlayerRepository THEATRE_FOUR_SCALE_SPEED_RUNNER = varpbit(12716, true);
	public static final VarPlayerRepository THEATRE_FIVE_SCALE_SPEED_RUNNER = varpbit(12718, true);
	public static final VarPlayerRepository FAITHLESS_ENCOUNTER = varpbit(12732, true);
	public static final VarPlayerRepository THE_FREMENNIK_WAY = varpbit(12733, true);
	public static final VarPlayerRepository VORKATH_SPEED_RUNNER = varpbit(12735, true);
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_CM_GRANDMASTER = varpbit(12747, true);
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_CM_SOLO_SPEED_RUNNER = varpbit(12752, true);
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_CM_FIVE_SCALE_SPEED_RUNNER = varpbit(12754, true);
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_CM_TRIO_SPEED_RUNNER = varpbit(12756, true);
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_GRANDMASTER = varpbit(12759, true);
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_SOLO_SPEED_RUNNER = varpbit(12781, true);
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_FIVE_SCALE_SPEED_RUNNER = varpbit(12783, true);
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_TRIO_SPEED_RUNNER = varpbit(12785, true);
	public static final VarPlayerRepository DEMON_WHISPERER = varpbit(12796, true);
	public static final VarPlayerRepository ASH_COLLECTOR = varpbit(12798, true);
	public static final VarPlayerRepository INFERNO_GRANDMASTER = varpbit(12800, true);
	public static final VarPlayerRepository THE_FLOOR_IS_LAVA = varpbit(12801, true);
	public static final VarPlayerRepository PLAYING_WITH_JADS = varpbit(12802, true);
	public static final VarPlayerRepository NO_LUCK_REQUIRED = varpbit(12803, true);
	public static final VarPlayerRepository WASNT_EVEN_CLOSE = varpbit(12805, true);
	public static final VarPlayerRepository BUDGET_SETUP = varpbit(12806, true);
	public static final VarPlayerRepository NIBBLER_CHASER = varpbit(12807, true);
	public static final VarPlayerRepository FACING_JAD_HEAD_ON_II = varpbit(12808, true);
	public static final VarPlayerRepository JAD_WHAT_ARE_YOU_DOING_HERE = varpbit(12809, true);
	public static final VarPlayerRepository INFERNO_SPEED_RUNNER = varpbit(12810, true);
	public static final VarPlayerRepository THE_VI_JAD_CHALLENGE = varpbit(12820, true);
	public static final VarPlayerRepository TZHAAR_KET_RAK_SPEED_RUNNER = varpbit(12824, true);
	public static final VarPlayerRepository IT_WASNT_A_FLUKE = varpbit(12828, true);
	public static final VarPlayerRepository STOP_RIGHT_THERE = varpbit(12830, true);
	public static final VarPlayerRepository PERSONAL_SPACE = varpbit(12831, true);
	public static final VarPlayerRepository ROYAL_AFFAIRS = varpbit(12832, true);
	public static final VarPlayerRepository HARDER_MODE_I = varpbit(12833, true);
	public static final VarPlayerRepository HARDER_MODE_II = varpbit(12834, true);
	public static final VarPlayerRepository NYLO_SNIPER = varpbit(12835, true);
	public static final VarPlayerRepository TEAM_WORK_MAKES_THE_DREAM_WORK = varpbit(12836, true);
	public static final VarPlayerRepository HARDER_MODE_III = varpbit(12837, true);
	public static final VarPlayerRepository WARN_ANNAKARL_TAB = varpbit(2324, true); // untested
	public static final VarPlayerRepository FRIEND_LOGIN_LOUT_MESSAGES = varpbit(12274, true); // untested
	public static final VarPlayerRepository SHOW_QUESTS_YOU_LACK_THE_REQUIREMENTS_FOR = varpbit(13778, true); // untested;
	public static final VarPlayerRepository SHOW_QUESTS_YOU_LACK_THE_RECOMMENDATIONS_FOR = varpbit(13779, true); // untested;
	public static final VarPlayerRepository QUEST_LINE_SORTING = varpbit(13772, true); // untested;
	public static final VarPlayerRepository PACK_LIKE_A_YAK = varpbit(12838, true);
	public static final VarPlayerRepository THEATRE_HM_TRIO_SPEED_RUNNER = varpbit(12840, true);
	public static final VarPlayerRepository THEATRE_HM_FOUR_SCALE_SPEED_RUNNER = varpbit(12841, true);
	public static final VarPlayerRepository THEATRE_HM_FIVE_SCALE_SPEED_RUNNER = varpbit(12842, true);
	public static final VarPlayerRepository THEATRE_OF_BLOOD_HM_GRANDMASTER = varpbit(12843, true);

	/**
	 * Combat achievements overview (interface 717)
	 */
	public static final VarPlayerRepository COMBAT_ACHIVEMENTS_OVERVIEW_EASY = varpbit(12885, true);// max value is 33

	public static final VarPlayerRepository COMBAT_ACHIVEMENTS_OVERVIEW_MEDIUM = varpbit(12886, true);// max value is 41
	public static final VarPlayerRepository COMBAT_ACHIVEMENTS_OVERVIEW_HARD = varpbit(12887, true);// max value is 58
	public static final VarPlayerRepository COMBAT_ACHIVEMENTS_OVERVIEW_ELITE = varpbit(12888, true);// max value is 109
	public static final VarPlayerRepository COMBAT_ACHIVEMENTS_OVERVIEW_MASTER = varpbit(12889, true);// max value is 96
	public static final VarPlayerRepository COMBAT_ACHIVEMENTS_OVERVIEW_GRANDMASTER = varpbit(12890, true);// max value is
	// 73

	/**
	 * Combat Achievements bosses
	 */
	public static final VarPlayerRepository CHAMBERS_OF_XERIC = varpbit(12891, true);// max value of 29
	public static final VarPlayerRepository CHAMBERS_OF_XERIC_CHALLENGE_MODE = varpbit(12892, true);// max value of 11
	public static final VarPlayerRepository BARROWS = varpbit(12893, true);// max value of 7
	public static final VarPlayerRepository KREE_ARRA = varpbit(12894, true);// max value of 7
	public static final VarPlayerRepository GENERAL_GRAARDOR = varpbit(12895, true);// max value of 8
	public static final VarPlayerRepository COMMANDER_ZILYANA = varpbit(12896, true);// max value of 7

	public static final VarPlayerRepository KRIL_TSUTSAROTH = varpbit(12897, true);// max value of 9
	public static final VarPlayerRepository DAGANNOTH_PRIME = varpbit(12898, true);// max value of 4
	public static final VarPlayerRepository DAGANNOTH_REX = varpbit(12899, true);// max value of 4
	public static final VarPlayerRepository DAGANNOTH_SUPREME = varpbit(12900, true);// max value of 4
	public static final VarPlayerRepository CALLISTO = varpbit(12901, true);// max value of 2
	public static final VarPlayerRepository VENENATIS = varpbit(12902, true);// max value of 2
	public static final VarPlayerRepository VETION = varpbit(12903, true);// max value of 2
	public static final VarPlayerRepository CHAOS_ELEMENTAL = varpbit(12904, true);// max value of 4
	public static final VarPlayerRepository KING_BLACK_DRAGON = varpbit(12905, true);// max value of 6
	public static final VarPlayerRepository GIANT_MOLE = varpbit(12906, true);// max value of 6
	public static final VarPlayerRepository KALPHITE_QUEEN = varpbit(12907, true);// max value of 5
	public static final VarPlayerRepository CORPOREAL_BEAST = varpbit(12908, true);// max value of 5
	public static final VarPlayerRepository ZULRAH = varpbit(12909, true);// max value of 9
	public static final VarPlayerRepository CHAOS_FANATIC = varpbit(12910, true);// max value of 4
	public static final VarPlayerRepository SCORPIA = varpbit(12911, true);// max value of 4
	public static final VarPlayerRepository CRAZY_ARCHAEOLOGIST = varpbit(12912, true);// max value of 4
	public static final VarPlayerRepository TZTOK_JAD = varpbit(12913, true);// max value of 10
	public static final VarPlayerRepository KRAKEN = varpbit(12914, true);// max value of 5
	public static final VarPlayerRepository THERMONUCLEAR_SMOKE_DEVIL = varpbit(12915, true);// max value of 3
	public static final VarPlayerRepository CERBERUS = varpbit(12916, true);// max value of 6
	public static final VarPlayerRepository ABYSSAL_SIRE = varpbit(12917, true);// max value of 8
	public static final VarPlayerRepository SKOTIZO = varpbit(12918, true);// max value of 7
	public static final VarPlayerRepository WINTERTODT = varpbit(12919, true);// max value of 8
	public static final VarPlayerRepository OBOR = varpbit(12920, true);// max value of 6
	public static final VarPlayerRepository TZKAL_ZUK = varpbit(12921, true);// max value of 12
	public static final VarPlayerRepository DERANGED_ARCHAEOLOGIST = varpbit(12922, true);// max value of 4
	public static final VarPlayerRepository GROTESQUE_GUARDIANS = varpbit(12923, true);// max value of 15
	public static final VarPlayerRepository VORKATH = varpbit(12924, true);// max value of 11
	public static final VarPlayerRepository BRYOPHYTA = varpbit(12925, true);// max value of 7
	public static final VarPlayerRepository ALCHEMICAL_HYDRA = varpbit(12926, true);// max value of 12
	public static final VarPlayerRepository HESPORI = varpbit(12927, true);// max value of 6
	public static final VarPlayerRepository THE_MIMIC = varpbit(12928, true);// max value of 1
	public static final VarPlayerRepository SARACHNIS = varpbit(12929, true);// max value of 5
	public static final VarPlayerRepository ZALCANO = varpbit(12930, true);// max value of 4
	public static final VarPlayerRepository CRYSTALLINE_HUNLLEF = varpbit(12931, true);// max value of 10
	public static final VarPlayerRepository CORRUPTED_HUNLLEF = varpbit(12932, true);// max value of 11
	public static final VarPlayerRepository THE_NIGHTMARE = varpbit(12934, true);// max value of 14
	public static final VarPlayerRepository THEATRE_OF_BLOOD_ACHIEVEMENT = varpbit(12935, true);// max value of 24
	public static final VarPlayerRepository TEMPOROSS = varpbit(12936, true);// max value of 8
	public static final VarPlayerRepository TZHAAR_KET_RAKS_CHALLENGES = varpbit(12937, true);// max value of 11
	public static final VarPlayerRepository THEATRE_OF_BLOOD_HARD_MODE = varpbit(12938, true);// max value of 14
	public static final VarPlayerRepository THEATRE_OF_BLOOD_ENTRY_MODE = varpbit(12939, true);// max value of 12
	public static final VarPlayerRepository COLLECTION_LOG_ACHIEVEMENTS = varpbit(6906, true);// max value is 36 THIS IS
	// SENT WHEN YOU CLICK
	// "Collection-Log" on
	// interface 713
	/**
	 * Bird houses
	 */
	public static final VarPlayerRepository BIRD_HOUSE_MEADOW_NORTH = varp(1626, true);
	public static final VarPlayerRepository BIRD_HOUSE_MEADOW_SOUTH = varp(1627, true);
	public static final VarPlayerRepository BIRD_HOUSE_VALLEY_NORTH = varp(1628, true);
	public static final VarPlayerRepository BIRD_HOUSE_VALLEY_SOUTH = varp(1629, true);
	/**
	 * Imbued Heart & other cooldowns
	 */
	public static final VarPlayerRepository IMBUED_HEART_COOLDOWN = varpbit(5361, false);

	public static final VarPlayerRepository TAINTED_WAND_COOLDOWN = varpbit(5362, false);

	public static final VarPlayerRepository CRYSTAL_SAW_COOLDOWN = varpbit(5363, false);
	/**
	 * Fossil Storage
	 */

	public static final VarPlayerRepository SMALL_FOSSLISED_LIMBS = varpbit(5832, true);
	public static final VarPlayerRepository SMALL_FOSSLISED_SPINE = varpbit(5833, true);
	public static final VarPlayerRepository SMALL_FOSSLISED_RIBS = varpbit(5834, true);
	public static final VarPlayerRepository SMALL_FOSSLISED_PELVIS = varpbit(5835, true);
	public static final VarPlayerRepository SMALL_FOSSLISED_SKULL = varpbit(5836, true);
	public static final VarPlayerRepository MEDIUM_FOSSLISED_LIMBS = varpbit(5837, true);
	public static final VarPlayerRepository MEDIUM_FOSSLISED_SPINE = varpbit(5838, true);
	public static final VarPlayerRepository MEDIUM_FOSSLISED_RIBS = varpbit(5839, true);
	public static final VarPlayerRepository MEDIUM_FOSSLISED_PELVIS = varpbit(5840, true);
	public static final VarPlayerRepository MEDIUM_FOSSLISED_SKULL = varpbit(5841, true);
	public static final VarPlayerRepository LARGE_FOSSLISED_LIMBS = varpbit(5842, true);
	public static final VarPlayerRepository LARGE_FOSSLISED_SPINE = varpbit(5843, true);
	public static final VarPlayerRepository LARGE_FOSSLISED_RIBS = varpbit(5844, true);
	public static final VarPlayerRepository LARGE_FOSSLISED_PELVIS = varpbit(5845, true);
	public static final VarPlayerRepository LARGE_FOSSLISED_SKULL = varpbit(5846, true);
	public static final VarPlayerRepository RARE_FOSSLISED_LIMBS = varpbit(5852, true);
	public static final VarPlayerRepository RARE_FOSSLISED_SPINE = varpbit(5853, true);
	public static final VarPlayerRepository RARE_FOSSLISED_RIBS = varpbit(5854, true);
	public static final VarPlayerRepository RARE_FOSSLISED_PELVIS = varpbit(5855, true);
	public static final VarPlayerRepository RARE_FOSSLISED_SKULL = varpbit(5856, true);
	public static final VarPlayerRepository RARE_FOSSLISED_TUSK = varpbit(5952, true);
	public static final VarPlayerRepository UNIDENTIFIED_SMALL_FOSSIL = varpbit(5828, true);
	public static final VarPlayerRepository UNIDENTIFIED_MEDIUM_FOSSIL = varpbit(5829, true);
	public static final VarPlayerRepository UNIDENTIFIED_LARGE_FOSSIL = varpbit(5830, true);
	public static final VarPlayerRepository UNIDENTIFIED_RARE_FOSSIL = varpbit(5831, true);
	public static final VarPlayerRepository FOSSILISED_ROOTS = varpbit(5847, true);
	public static final VarPlayerRepository FOSSILISED_STUMP = varpbit(5848, true);
	public static final VarPlayerRepository FOSSILISED_BRANCH = varpbit(5849, true);
	public static final VarPlayerRepository FOSSILISED_LEAF = varpbit(5850, true);
	public static final VarPlayerRepository FOSSILISED_MUSHROOM = varpbit(5851, true);
	/**
	 * Achievement Diaries
	 */

	public static final VarPlayerRepository KARAMJA_EASY = varpbit(2423, true);
	public static final VarPlayerRepository KARAMJA_MEDIUM = varpbit(6288, true);
	public static final VarPlayerRepository KARAMJA_HARD = varpbit(6289, true);
	public static final VarPlayerRepository KARAMJA_ELITE = varpbit(6290, true);
	public static final VarPlayerRepository KARAMJA_EASY_COMPLETED = varpbit(3577, true);
	public static final VarPlayerRepository KARAMJA_MEDIUM_COMPLETED = varpbit(3598, true);
	public static final VarPlayerRepository KARAMJA_HARD_COMPLETED = varpbit(3610, true);
	public static final VarPlayerRepository KARAMJA_ELITE_COMPLETED = varpbit(4567, true);
	public static final VarPlayerRepository ARDOUGNE_EASY_COMPLETED = varpbit(4499, true);
	public static final VarPlayerRepository ARDOUGNE_MEDIUM_COMPLETED = varpbit(4500, true);

	public static final VarPlayerRepository ARDOUGNE_HARD_COMPLETED = varpbit(4501, true);
	public static final VarPlayerRepository ARDOUGNE_ELITE_COMPLETED = varpbit(4502, true);
	public static final VarPlayerRepository ARDOUGNE_EASY = varpbit(6291, true);
	public static final VarPlayerRepository ARDOUGNE_MEDIUM = varpbit(6292, true);

	public static final VarPlayerRepository ARDOUGNE_HARD = varpbit(6293, true);
	public static final VarPlayerRepository ARDOUGNE_ELITE = varpbit(6294, true);
	public static final VarPlayerRepository DESERT_EASY_COMPLETED = varpbit(4523, true);
	public static final VarPlayerRepository DESERT_MEDIUM_COMPLETED = varpbit(4524, true);

	public static final VarPlayerRepository DESERT_HARD_COMPLETED = varpbit(4525, true);
	public static final VarPlayerRepository DESERT_ELITE_COMPLETED = varpbit(4526, true);
	public static final VarPlayerRepository DESERT_EASY = varpbit(6295, true);
	public static final VarPlayerRepository DESERT_MEDIUM = varpbit(6296, true);

	public static final VarPlayerRepository DESERT_HARD = varpbit(6297, true);
	public static final VarPlayerRepository DESERT_ELITE = varpbit(6298, true);
	public static final VarPlayerRepository FALADOR_EASY_COMPLETED = varpbit(4503, true);
	public static final VarPlayerRepository FALADOR_MEDIUM_COMPLETED = varpbit(4504, true);

	public static final VarPlayerRepository FALADOR_HARD_COMPLETED = varpbit(4505, true);
	public static final VarPlayerRepository FALADOR_ELITE_COMPLETED = varpbit(4506, true);
	public static final VarPlayerRepository FALADOR_EASY = varpbit(6299, true);
	public static final VarPlayerRepository FALADOR_MEDIUM = varpbit(6300, true);

	public static final VarPlayerRepository FALADOR_HARD = varpbit(6301, true);
	public static final VarPlayerRepository FALADOR_ELITE = varpbit(6302, true);
	public static final VarPlayerRepository FREMMY_EASY_COMPLETED = varpbit(4531, true);
	public static final VarPlayerRepository FREMMY_MEDIUM_COMPLETED = varpbit(4532, true);

	public static final VarPlayerRepository FREMMY_HARD_COMPLETED = varpbit(4533, true);
	public static final VarPlayerRepository FREMMY_ELITE_COMPLETED = varpbit(4534, true);
	public static final VarPlayerRepository FREMMY_EASY = varpbit(6303, true);
	public static final VarPlayerRepository FREMMY_MEDIUM = varpbit(6304, true);

	public static final VarPlayerRepository FREMMY_HARD = varpbit(6305, true);
	public static final VarPlayerRepository FREMMY_ELITE = varpbit(6306, true);
	public static final VarPlayerRepository KANDARIN_EASY_COMPLETED = varpbit(4515, true);
	public static final VarPlayerRepository KANDARIN_MEDIUM_COMPLETED = varpbit(4516, true);

	public static final VarPlayerRepository KANDARIN_HARD_COMPLETED = varpbit(4517, true);
	public static final VarPlayerRepository KANDARIN_ELITE_COMPLETED = varpbit(4518, true);
	public static final VarPlayerRepository KANDARIN_EASY = varpbit(6307, true);
	public static final VarPlayerRepository KANDARIN_MEDIUM = varpbit(6308, true);

	public static final VarPlayerRepository KANDARIN_HARD = varpbit(6309, true);
	public static final VarPlayerRepository KANDARIN_ELITE = varpbit(6310, true);
	public static final VarPlayerRepository LUMBRIDGE_EASY_COMPLETED = varpbit(4535, true);
	public static final VarPlayerRepository LUMBRIDGE_MEDIUM_COMPLETED = varpbit(4536, true);

	public static final VarPlayerRepository LUMBRIDGE_HARD_COMPLETED = varpbit(4537, true);
	public static final VarPlayerRepository LUMBRIDGE_ELITE_COMPLETED = varpbit(4538, true);
	public static final VarPlayerRepository LUMBRIDGE_EASY = varpbit(6311, true);
	public static final VarPlayerRepository LUMBRIDGE_MEDIUM = varpbit(6312, true);

	public static final VarPlayerRepository LUMBRIDGE_HARD = varpbit(6313, true);
	public static final VarPlayerRepository LUMBRIDGE_ELITE = varpbit(6314, true);
	public static final VarPlayerRepository MORYTANIA_EASY_COMPLETED = varpbit(4527, true);
	public static final VarPlayerRepository MORYTANIA_MEDIUM_COMPLETED = varpbit(4528, true);

	public static final VarPlayerRepository MORYTANIA_HARD_COMPLETED = varpbit(4529, true);
	public static final VarPlayerRepository MORYTANIA_ELITE_COMPLETED = varpbit(4530, true);
	public static final VarPlayerRepository MORYTANIA_EASY = varpbit(6315, true);
	public static final VarPlayerRepository MORYTANIA_MEDIUM = varpbit(6316, true);

	public static final VarPlayerRepository MORYTANIA_HARD = varpbit(6317, true);
	public static final VarPlayerRepository MORYTANIA_ELITE = varpbit(6318, true);
	public static final VarPlayerRepository VARROCK_EASY_COMPLETED = varpbit(4519, true);
	public static final VarPlayerRepository VARROCK_MEDIUM_COMPLETED = varpbit(4520, true);

	public static final VarPlayerRepository VARROCK_HARD_COMPLETED = varpbit(4521, true);
	public static final VarPlayerRepository VARROCK_ELITE_COMPLETED = varpbit(4522, true);
	public static final VarPlayerRepository VARROCK_EASY = varpbit(6319, true);
	public static final VarPlayerRepository VARROCK_MEDIUM = varpbit(6320, true);

	public static final VarPlayerRepository VARROCK_HARD = varpbit(6321, true);
	public static final VarPlayerRepository VARROCK_ELITE = varpbit(6322, true);
	public static final VarPlayerRepository WILDERNESS_EASY_COMPLETED = varpbit(4507, true);
	public static final VarPlayerRepository WILDERNESS_MEDIUM_COMPLETED = varpbit(4508, true);

	public static final VarPlayerRepository WILDERNESS_HARD_COMPLETED = varpbit(4509, true);
	public static final VarPlayerRepository WILDERNESS_ELITE_COMPLETED = varpbit(4510, true);
	public static final VarPlayerRepository WILDERNESS_EASY = varpbit(6323, true);
	public static final VarPlayerRepository WILDERNESS_MEDIUM = varpbit(6324, true);

	public static final VarPlayerRepository WILDERNESS_HARD = varpbit(6325, true);
	public static final VarPlayerRepository WILDERNESS_ELITE = varpbit(6326, true);
	public static final VarPlayerRepository WESTERN_PROV_EASY_COMPLETED = varpbit(4511, true);
	public static final VarPlayerRepository WESTERN_PROV_MEDIUM_COMPLETED = varpbit(4512, true);

	public static final VarPlayerRepository WESTERN_PROV_HARD_COMPLETED = varpbit(4513, true);
	public static final VarPlayerRepository WESTERN_PROV_ELITE_COMPLETED = varpbit(4514, true);
	public static final VarPlayerRepository WESTERN_PROV_EASY = varpbit(6327, true);
	public static final VarPlayerRepository WESTERN_PROV_MEDIUM = varpbit(6328, true);

	public static final VarPlayerRepository WESTERN_PROV_HARD = varpbit(6329, true);
	public static final VarPlayerRepository WESTERN_PROV_ELITE = varpbit(6330, true);
	public static final VarPlayerRepository KOUREND_EASY_COMPLETED = varpbit(7929, true);
	public static final VarPlayerRepository KOUREND_MEDIUM_COMPLETED = varpbit(7930, true);

	public static final VarPlayerRepository KOUREND_HARD_COMPLETED = varpbit(7931, true);
	public static final VarPlayerRepository KOUREND_ELITE_COMPLETED = varpbit(7932, true);
	public static final VarPlayerRepository KOUREND_EASY = varpbit(7933, true);
	public static final VarPlayerRepository KOUREND_MEDIUM = varpbit(7934, true);

	public static final VarPlayerRepository KOUREND_HARD = varpbit(7935, true);
	public static final VarPlayerRepository KOUREND_ELITE = varpbit(7936, true);
	public static final VarPlayerRepository ACCEPT_AID = varpbit(4180, true);
	public static final VarPlayerRepository RUNNING = varp(173, true).defaultValue(1);

	public static final VarPlayerRepository HAS_DISPLAY_NAME = varpbit(8199, true).defaultValue(1).forceSend();
	/**
	 * Advanced options
	 */

	public static final VarPlayerRepository TRANSPARENT_SIDE_PANEL = varpbit(4609, true);
	public static final VarPlayerRepository REMAINING_XP_TOOLTIP = varpbit(4181, true);
	public static final VarPlayerRepository PRAYER_TOOLTIPS = varpbit(5711, true);

	public static final VarPlayerRepository SPECIAL_ATTACK_TOOLTIPS = varpbit(5712, true);

	public static final VarPlayerRepository HIDE_DATA_ORBS = varpbit(4084, true).defaultValue(0);

	public static final VarPlayerRepository TRANSPARENT_CHATBOX = varpbit(4608, true);

	public static final VarPlayerRepository CLICK_THROUGH_CHATBOX = varpbit(2570, true);

	public static final VarPlayerRepository SIDE_PANELS = varpbit(4607, true);

	public static final VarPlayerRepository HOTKEY_CLOSING_PANELS = varpbit(4611, true);

	public static final VarPlayerRepository SIDEBAR_INDICATOR = varpbit(3756, false);

	public static final VarPlayerRepository CHATBOX_SCROLLBAR = varpbit(6374, true);

	public static final VarPlayerRepository ZOOMING_DISABLED = varpbit(6357, true).defaultValue(0);

	/**
	 * House options
	 */

	public static final VarPlayerRepository BUILDING_MODE = varpbit(2176, true);

	public static final VarPlayerRepository RENDER_DOORS_MODE = varpbit(6269, true);

	public static final VarPlayerRepository TELEPORT_INSIDE = varpbit(4744, true);

	public static final VarPlayerRepository HOUSE_VIEWER_SELECTED = varpbit(5329, false);
	public static final VarPlayerRepository HOUSE_VIEWER_HIGHLIGHTED = varpbit(5330, false);
	public static final VarPlayerRepository HOUSE_VIEWER_ROOM_ROTATION = varpbit(5331, false);

	public static final VarPlayerRepository HOUSE_VIEWER_ALLOW_ROTATION = varpbit(5332, false);
	public static final VarPlayerRepository HOUSE_VIEWER_ROOM_ID = varpbit(5333, false);
	public static final VarPlayerRepository PETS_ROAMING_DISABLED = varpbit(2013, true);
	public static final VarPlayerRepository HIRED_SERVANT = varpbit(2190, false);

	// these varps are kinda volatile, used by many interfaces for different shit
	public static final VarPlayerRepository VARP_261 = varp(261, true).defaultValue(0);
	public static final VarPlayerRepository LECTERN_EAGLE = VARP_261;
	public static final VarPlayerRepository MAGE_ARENA_POINTS = VARP_261;
	public static final VarPlayerRepository GENDER_VAR = VARP_261;
	public static final VarPlayerRepository SLAYER_REWARDS_ASSIGNMENT_COUNT = VARP_261;
	public static final VarPlayerRepository FREE_SIGIL_ONE = VARP_261;


	public static final VarPlayerRepository SLAYER_REWARDS_TASK_INDEX = varp(262, true);
	public static final VarPlayerRepository SLAYER_REWARDS_TASK_INDEX2 = varp(263, true);
	public static final VarPlayerRepository LECTERN_DEMON = varp(262, true).defaultValue(0);
	public static final VarPlayerRepository ACTIVE_SELECTED_PRESET_VARP = varp(262, false);
	public static final VarPlayerRepository FREE_SIGIL_TWO = VARP_261;

	/**
	 * Music player
	 */

	public static final VarPlayerRepository[] MUSIC_UNLOCKS = {
			varp(20, true),
			varp(21, true),
			varp(22, true),
			varp(23, true),
			varp(24, true),
			varp(25, true),
			varp(298, true),
			varp(311, true),
			varp(346, true),
			varp(414, true),
			varp(464, true),
			varp(598, true),
			varp(662, true),
			varp(721, true),
			varp(906, true),
			varp(1009, true),
			varp(1338, true),
			varp(1681, true),
			varp(2065, true),
			varp(2237, true),
			varp(2950, true),
			varp(3418, true),
			varp(3575, true),
			varp(4066, true),
			varp(4411, true),
	};

	public static final VarPlayerRepository MUSIC_PREFERENCE = varp(18, true);
	public static final VarPlayerRepository MUSIC_LOOP = varpbit(4137, true);
	/**
	 * World switcher
	 */

	public static final VarPlayerRepository WORLD_SWITCHER_SETTINGS = varpbit(4596, true);
	public static final VarPlayerRepository WORLD_SWITCHER_FAVOURITE_ONE = varpbit(4597, true);
	public static final VarPlayerRepository WORLD_SWITCHER_FAVOURITE_TWO = varpbit(4598, true);

	/**
	 * Keybind options
	 */

	public static final VarPlayerRepository[] KEYBINDS = {
			varpbit(4675, true).defaultValue(1), // Combat
			varpbit(4676, true).defaultValue(2), // Stats
			varpbit(4677, true).defaultValue(3), // Quests
			varpbit(4678, true).defaultValue(13), // Inventory
			varpbit(4679, true).defaultValue(4), // Equipment

			varpbit(4680, true).defaultValue(5), // Prayer
			varpbit(4682, true).defaultValue(6), // Magic
			varpbit(4684, true).defaultValue(8), // Friends
			varpbit(4685, true).defaultValue(9), // Ignores
			varpbit(4689, true).defaultValue(0), // Logout

			varpbit(4686, true).defaultValue(10), // Options
			varpbit(4687, true).defaultValue(11), // Emotes
			varpbit(4683, true).defaultValue(7), // Clan
			varpbit(4688, true).defaultValue(12), // Music
	};

	public static final VarPlayerRepository ESCAPE_CLOSES = varpbit(4681, true);

	/**
	 * Combat Options
	 */

	public static final VarPlayerRepository ATTACK_SET = varp(43, true);

	/**
	 * Display name options
	 */

	// varpbit 5605 enables look up

	public static final VarPlayerRepository AUTO_RETALIATE = varp(172, true);
	public static final VarPlayerRepository WEAPON_TYPE = varpbit(357, false);
	public static final VarPlayerRepository SPECIAL_ENERGY = varp(300, true).defaultValue(1000);

	public static final VarPlayerRepository SPECIAL_ACTIVE = varp(301, false);

	public static final VarPlayerRepository SPECIAL_ORB_STATE = varpbit(8121, false).defaultValue(2);

	public static final VarPlayerRepository TARGET_OVERLAY_CUR = varpbit(5653, false);

	public static final VarPlayerRepository TARGET_OVERLAY_MAX = varpbit(5654, false);

	/**
	 * Skill guide
	 */

	public static final VarPlayerRepository SKILL_GUIDE_STAT = varpbit(4371, false);

	public static final VarPlayerRepository SKILL_GUIDE_CAT = varpbit(4372, false);

	/**
	 * Bank
	 */

	public static final VarPlayerRepository BANK_TAB = varpbit(4150, false);

	public static final VarPlayerRepository BANK_LAST_X = varpbit(3960, true);

	public static final VarPlayerRepository BANK_INSERT_MODE = varpbit(3959, true);

	public static final VarPlayerRepository BANK_ALWAYS_PLACEHOLDERS = varpbit(3755, true);

	public static final VarPlayerRepository BANK_TAB_DISPLAY = varpbit(4170, true);

	public static final VarPlayerRepository BANK_INCINERATOR = varpbit(5102, true);

	public static final VarPlayerRepository BANK_DEPOSIT_EQUIPMENT = varpbit(5364, true);

	public static final VarPlayerRepository[] BANK_TAB_SIZES = {
			varpbit(4171, true), varpbit(4172, true), varpbit(4173, true),
			varpbit(4174, true), varpbit(4175, true), varpbit(4176, true),
			varpbit(4177, true), varpbit(4178, true), varpbit(4179, true),
	};

	public static final VarPlayerRepository BANK_DEFAULT_QUANTITY = varpbit(6590, true);

	/**
	 * Prayer
	 */

	public static final VarPlayerRepository QUICK_PRAYERS_ACTIVE = varpbit(4102, false);

	public static final VarPlayerRepository QUICK_PRAYING = varpbit(4103, false);

	public static final VarPlayerRepository CHIVALRY_PIETY_UNLOCK = varpbit(3909, false).defaultValue(8);

	public static final VarPlayerRepository RIGOUR_UNLOCK = varpbit(5451, true);

	public static final VarPlayerRepository AUGURY_UNLOCK = varpbit(5452, true);

	public static final VarPlayerRepository PRESERVE_UNLOCK = varpbit(5453, true);

	/**
	 * Runecrafting
	 */
	public static final VarPlayerRepository AIR_TIARA_UNLOCK = varpbit(607, true).defaultValue(0); // why default 0? it's
	// already 0?

	public static final VarPlayerRepository MIND_TIARA_UNLOCK = varpbit(608, true).defaultValue(0); // why default 0? it's
	// already 0?

	/**
	 * Rune pouch
	 */

	public static final VarPlayerRepository RUNE_POUCH_TYPES = varp(1139, false);

	public static final VarPlayerRepository RUNE_POUCH_AMOUNTS = varp(1140, false);

	public static final VarPlayerRepository RUNE_POUCH_LEFT_TYPE = varpbit(29, true);

	public static final VarPlayerRepository RUNE_POUCH_ADD_AMOUNT = varpbit(16201, true).defaultValue(0);

	public static final VarPlayerRepository RUNE_POUCH_LEFT_AMOUNT = varpbit(1624, true);

	public static final VarPlayerRepository RUNE_POUCH_MIDDLE_TYPE = varpbit(1622, true);

	public static final VarPlayerRepository RUNE_POUCH_MIDDLE_AMOUNT = varpbit(1625, true);

	public static final VarPlayerRepository RUNE_POUCH_RIGHT_TYPE = varpbit(1623, true);
	public static final VarPlayerRepository RUNE_POUCH_RIGHT_AMOUNT = varpbit(1626, true);

	public static final VarPlayerRepository RUNE_POUCH_RUNE1 = varpbit(29, true);
	public static final VarPlayerRepository RUNE_POUCH_AMOUNT1 = varpbit(1624, true);

	public static final VarPlayerRepository RUNE_POUCH_RUNE2 = varpbit(1622, true);
	public static final VarPlayerRepository RUNE_POUCH_AMOUNT2 = varpbit(1625, true);

	public static final VarPlayerRepository RUNE_POUCH_RUNE3 = varpbit(1623, true);
	public static final VarPlayerRepository RUNE_POUCH_AMOUNT3 = varpbit(1626, true);

	public static final VarPlayerRepository RUNE_POUCH_RUNE4 = varpbit(14285, true);
	public static final VarPlayerRepository RUNE_POUCH_AMOUNT4 = varpbit(14286, true);
	/**
	 * Misc
	 */

	public static final VarPlayerRepository GAME_FILTER = varpbit(26, true);
	public static final VarPlayerRepository MAGIC_BOOK = varpbit(4070, true);
	public static final VarPlayerRepository AUTOCAST = varpbit(276, true);
	public static final VarPlayerRepository DEFENSIVE_CAST = varpbit(2668, true);
	public static final VarPlayerRepository AUTOCAST_SET = varp(664, false);
	public static final VarPlayerRepository SMITHING_TYPE = varpbit(3216, false);
	public static final VarPlayerRepository SMITHING_QUANITY = varp(2224, false);

	public static final VarPlayerRepository ABYSS_MAP = varpbit(625, false);

	public static final VarPlayerRepository PLAYER_PRIORITY = varp(1075, false);

	public static final VarPlayerRepository STAMINA_POTION = varpbit(25, true);

	public static final VarPlayerRepository LOCK_CAMERA = varpbit(4606, false);

	public static final VarPlayerRepository MULTI_ZONE = varpbit(4605, false);
	public static final VarPlayerRepository MY_TRADE_MODIFIED = varpbit(4374, false);
	public static final VarPlayerRepository OTHER_TRADE_MODIFIED = varpbit(4375, false);

	public static final VarPlayerRepository HAM_HIDEOUT_ENTRANCE = varpbit(235, false);

	public static final VarPlayerRepository VENG_COOLDOWN = varpbit(2451, false);
	public static final VarPlayerRepository TELE_BLOCK = varpbit(4163, false);

	public static final VarPlayerRepository INFERNO_ENTRANCE = varpbit(5646, true).defaultValue(2); // uhh todo

	public static final VarPlayerRepository INFERNO_PILLAR_DAMAGE_WEST = varpbit(5655, false);

	public static final VarPlayerRepository INFERNO_PILLAR_DAMAGE_SOUTH = varpbit(5656, false);

	public static final VarPlayerRepository INFERNO_PILLAR_DAMAGE_EAST = varpbit(5657, false);

	public static final VarPlayerRepository PVP_KILLS = varpbit(8376, true);

	public static final VarPlayerRepository PVP_DEATHS = varpbit(8375, true);

	public static final VarPlayerRepository PVP_KD_OVERLAY = varpbit(4143, true).defaultValue(1);

	public static final VarPlayerRepository IN_PVP_AREA = varpbit(8121, true).defaultValue(0);

	public static final VarPlayerRepository WILDERNESS_TIMER = varpbit(877, false);

	public static final VarPlayerRepository ARDOUGNE_LEVER_UNLOCK = varpbit(4470, false).defaultValue(1);

	public static final VarPlayerRepository LARRANS_CHEST = varpbit(6583, false).defaultValue(0);

	/**
	 * Wilderness
	 */

	public static final VarPlayerRepository SINGLES_PLUS = varpbit(5961, true);

	public static final VarPlayerRepository FEROX_ENCLAVE = varpbit(10530, true);

	/**
	 * Slayer
	 */
	public static final VarPlayerRepository SLAYER_UNLOCKED_HELM = varpbit(3202, true);

	public static final VarPlayerRepository RING_BLING = varpbit(3207, true);

	public static final VarPlayerRepository BROADER_FLETCHING = varpbit(3208, true);

	public static final VarPlayerRepository KING_BLACK_BONNET = varpbit(5080, true);

	public static final VarPlayerRepository KALPHITE_KHAT = varpbit(5081, true);

	public static final VarPlayerRepository UNHOLY_HELMET = varpbit(5082, true);

	public static final VarPlayerRepository BIGGER_AND_BADDER = varpbit(5358, true);
	public static final VarPlayerRepository SEEING_RED = varpbit(2462, true);
	public static final VarPlayerRepository UNLOCK_BLOCK_TASK_SIX = varpbit(4538, true).defaultValue(1).forceSend();
	public static final VarPlayerRepository UNLOCK_DULY_NOTED = varpbit(4589, true);
	public static final VarPlayerRepository GARGOYLE_SMASHER = varpbit(4027, true);
	public static final VarPlayerRepository SLUG_SALTER = varpbit(4028, true);
	public static final VarPlayerRepository REPTILE_FREEZER = varpbit(4029, true);
	public static final VarPlayerRepository SHROOM_SPRAYER = varpbit(4030, true);
	public static final VarPlayerRepository NEED_MORE_DARKNESS = varpbit(4031, true);
	public static final VarPlayerRepository ANKOU_VERY_MUCH = varpbit(4085, true);
	public static final VarPlayerRepository SUQ_ANOTHER_ONE = varpbit(4086, true);
	public static final VarPlayerRepository FIRE_AND_DARKNESS = varpbit(4087, true);
	public static final VarPlayerRepository PEDAL_TO_THE_METALS = varpbit(4088, true);
	public static final VarPlayerRepository AUGMENT_MY_ABBIES = varpbit(4090, true);
	public static final VarPlayerRepository ITS_DARK_IN_HERE = varpbit(4091, true);
	public static final VarPlayerRepository GREATER_CHALLENGE = varpbit(4092, true);
	public static final VarPlayerRepository I_HOPE_YOU_MITH_ME = varpbit(4094, true);
	public static final VarPlayerRepository WATCH_THE_BIRDIE = varpbit(4095, true);
	public static final VarPlayerRepository HOT_STUFF = varpbit(4691, true);
	public static final VarPlayerRepository LIKE_A_BOSS = varpbit(4724, true);
	public static final VarPlayerRepository BLEED_ME_DRY = varpbit(4746, true);
	public static final VarPlayerRepository SMELL_YA_LATER = varpbit(4747, true);
	public static final VarPlayerRepository BIRDS_OF_A_FEATHER = varpbit(4748, true);
	public static final VarPlayerRepository I_REALLY_MITH_YOU = varpbit(4749, true);
	public static final VarPlayerRepository HORRORIFIC = varpbit(4750, true);
	public static final VarPlayerRepository TO_DUST_YOU_SHALL_RETURN = varpbit(4751, true);
	public static final VarPlayerRepository WYVER_NOTHER_ONE = varpbit(4752, true);
	public static final VarPlayerRepository GET_SMASHED = varpbit(4753, true);
	public static final VarPlayerRepository NECHS_PLEASE = varpbit(4754, true);
	public static final VarPlayerRepository KRACK_ON = varpbit(4755, true);
	public static final VarPlayerRepository SPIRITUAL_FERVOUR = varpbit(4757, true);
	public static final VarPlayerRepository REPTILE_GOT_RIPPED = varpbit(4996, true);
	public static final VarPlayerRepository GET_SCABARIGHT_ON_IT = varpbit(5359, true);
	public static final VarPlayerRepository WYVER_NOTHER_TWO = varpbit(5733, true);
	public static final VarPlayerRepository DARK_MANTLE = varpbit(5631, true);
	public static final VarPlayerRepository UNDEAD_HEAD = varpbit(6096, true);
	public static final VarPlayerRepository USE_MORE_HEAD = varpbit(6570, true);
	public static final VarPlayerRepository STOP_THE_WYVERN = varpbit(240, true);
	public static final VarPlayerRepository DOUBLE_TROUBLE = varpbit(6485, true);
	public static final VarPlayerRepository ADA_MIND_SOME_MORE = varpbit(1, true);
	public static final VarPlayerRepository RUUUUUNE = varpbit(1, true);
	public static final VarPlayerRepository BASILOCKED = varpbit(9456, true);
	public static final VarPlayerRepository TWISTEDVISION = varpbit(10104, true);
	public static final VarPlayerRepository ACTUALVAMPSLAY = varpbit(10388, true);
	public static final VarPlayerRepository TASKSTORAGE = varpbit(12442, true);
	public static final VarPlayerRepository WILDYMORESLAY = varpbit(13636, true);
	public static final VarPlayerRepository BASILONGER = varpbit(9455, true);
	public static final VarPlayerRepository STORE_TASK_AMOUNT = varp(265, true);
	public static final VarPlayerRepository SLAYER_MASTER = varpbit(4067, true);
	public static final VarPlayerRepository SLAYER_POINTS = varpbit(4068, true);
	public static final VarPlayerRepository SLAYER_TASK_AMOUNT = varp(16002, true);
	public static final VarPlayerRepository SLAYER_TASK = varp(16003, true);
	public static final VarPlayerRepository BOSS_TASK = varp(16004, true);
	public static final VarPlayerRepository EYE_SEE_YOU_VARP = varp(11023, true);
	public static final VarPlayerRepository SLAYER_LONGER_ARAXYTES = varp(11022, true);

	public static final VarPlayerRepository[] BLOCKED_TASKS = {
			varpbit(3209, true),
			varpbit(3210, true),
			varpbit(3211, true),
			varpbit(3212, true),
			varpbit(4441, true),
			varpbit(5023, true),
			varpbit(15366, true)
	};
	/**
	 * Quest points
	 */
	public static final VarPlayerRepository QUEST_POINTS = varp(101, true).defaultValue(300).forceSend();
	/**
	 * Farming
	 */
	public static final VarPlayerRepository FARMING_PATCH_1 = varpbit(4771, false); // Spirit Tree, Fruit Tree, Wood Tree,

	// Spirit Tree(Fally)
	public static final VarPlayerRepository FARMING_PATCH_2 = varpbit(4772, false); // Spirit Tree(Brimhaven), Allotment,
	// // Guild Bush
	public static final VarPlayerRepository FARMING_PATCH_3 = varpbit(4773, false); // Flower Patch // Guild Allotment
	public static final VarPlayerRepository FARMING_PATCH_4 = varpbit(4774, false); // Herb Patch // Guild Allotment

	public static final VarPlayerRepository FARMING_COMPOST_BIN = varpbit(4775, false); // Guild Herb Patch

	public static final VarPlayerRepository FARMING_PATCH_5 = varpbit(7905, false); // guild tree patch

	public static final VarPlayerRepository FARMING_PATCH_6 = varpbit(7906, false); // Guild Flower Patch
	public static final VarPlayerRepository FARMING_PATCH_7 = varpbit(7907, false);
	public static final VarPlayerRepository FARMING_PATCH_8 = varpbit(7908, false);
	public static final VarPlayerRepository FARMING_PATCH_9 = varpbit(7909, false); // Guild Fruit Tree Patch
	public static final VarPlayerRepository FARMING_PATCH_10 = varpbit(7904, false);// Guild Cactus
	public static final VarPlayerRepository FARMING_PATCH_11 = varpbit(7910, false);
	public static final VarPlayerRepository FARMING_PATCH_12 = varpbit(7911, false);
	public static final VarPlayerRepository FARMING_PATCH_13 = varpbit(7912, false);
	public static final VarPlayerRepository STORAGE_RAKE = varpbit(1435, true);
	public static final VarPlayerRepository STORAGE_SEED_DIBBER = varpbit(1436, true);
	public static final VarPlayerRepository STORAGE_SPADE = varpbit(1437, true);
	public static final VarPlayerRepository STORAGE_SECATEURS = varpbit(1438, true);
	public static final VarPlayerRepository STORAGE_SECATEURS_TYPE = varpbit(1848, true); // 0 for normal, 1 for magic
	public static final VarPlayerRepository STORAGE_WATERING_CAN = varpbit(1439, true);

	public static final VarPlayerRepository STORAGE_TROWEL = varpbit(1440, true);
	public static final VarPlayerRepository STORAGE_EMPTY_BUCKET_1 = varpbit(1441, true); // 5 bits
	public static final VarPlayerRepository STORAGE_EMPTY_BUCKET_2 = varpbit(4731, true); // 3 bits
	public static final VarPlayerRepository STORAGE_COMPOST_1 = varpbit(1442, true); // 8 bits
	public static final VarPlayerRepository STORAGE_COMPOST_2 = varpbit(6266, true); // 2 bits
	public static final VarPlayerRepository STORAGE_SUPERCOMPOST_1 = varpbit(1443, true); // 8 bits
	public static final VarPlayerRepository STORAGE_SUPERCOMPOST_2 = varpbit(6267, true); // 2 bits
	public static final VarPlayerRepository STORAGE_PLANT_CURE = varpbit(6268, true);
	public static final VarPlayerRepository STORAGE_ULTRACOMPOST = varpbit(5732, true);
	public static final VarPlayerRepository STORAGE_BOTTOMLESS_COMPOST = varpbit(7915, true);
	/**
	 * Motherlode mine
	 */
	public static final VarPlayerRepository PAY_DIRT_IN_SACK = varpbit(5558, true);
	/**
	 * Barrows
	 */

	public static final VarPlayerRepository AHRIM_KILLED = varpbit(457, true);
	public static final VarPlayerRepository DHAROK_KILLED = varpbit(458, true);
	public static final VarPlayerRepository GUTHAN_KILLED = varpbit(459, true);
	public static final VarPlayerRepository KARIL_KILLED = varpbit(460, true);
	public static final VarPlayerRepository TORAG_KILLED = varpbit(461, true);

	public static final VarPlayerRepository VERAC_KILLED = varpbit(462, true);

	public static final VarPlayerRepository BARROWS_CHEST = varpbit(1394, false);
	/**
	 * Fairy ring
	 */
	public static final VarPlayerRepository FAIRY_RING_LEFT = varpbit(3985, true);
	public static final VarPlayerRepository FAIRY_RING_MIDDLE = varpbit(3986, true);
	public static final VarPlayerRepository FAIRY_RING_RIGHT = varpbit(3987, true);
	public static final VarPlayerRepository FAIRY_RING_LAST_DESTINATION = varpbit(5374, true);
	/**
	 * Canoe station
	 */
	public static final VarPlayerRepository LUMBRIDGE_CANOE = varpbit(1839, true);
	public static final VarPlayerRepository CHAMPION_GUILD_CANOE = varpbit(1840, true);

	public static final VarPlayerRepository BARBARIAN_VILLAGE_CANOE = varpbit(1841, true);
	public static final VarPlayerRepository EDGEVILLE_CANOE = varpbit(1842, true);
	public static final VarPlayerRepository WILDERNESS_CHINS_CANOE = varpbit(1843, true);
	/**
	 * Godwars
	 */
	public static final VarPlayerRepository GWD_SARADOMIN_KC = varpbit(3972, true);

	public static final VarPlayerRepository GWD_ARMADYL_KC = varpbit(3973, true);
	public static final VarPlayerRepository GWD_BANDOS_KC = varpbit(3975, true);
	public static final VarPlayerRepository GWD_ZAMORAK_KC = varpbit(3976, true);
	public static final VarPlayerRepository GWD_ZAROS_KC = varpbit(13080, true);
	public static final VarPlayerRepository SARADOMINS_LIGHT = varpbit(4733, true);

	public static final VarPlayerRepository GODWARS_DUNGEON = varpbit(3966, true);
	public static final VarPlayerRepository GODWARS_SARADOMIN_FIRST_ROPE = varpbit(3967, true);
	public static final VarPlayerRepository GODWARS_SARADOMIN_SECOND_ROPE = varpbit(3968, true);
	/**
	 * Poison
	 */
	public static final VarPlayerRepository POISONED = varp(102, true);
	/**
	 * Disease
	 */
	public static final VarPlayerRepository DISEASED = varp(456, true);
	/**
	 * Exp Counter
	 */
	public static final VarPlayerRepository XP_COUNTER_SHOWN = varpbit(4702, true);
	public static final VarPlayerRepository XP_COUNTER_POSITION = varpbit(4692, true);
	public static final VarPlayerRepository XP_COUNTER_SIZE = varpbit(4693, true);
	public static final VarPlayerRepository XP_COUNTER_DURATION = varpbit(4694, true);

	public static final VarPlayerRepository XP_COUNTER_COLOUR = varpbit(4695, true);

	public static final VarPlayerRepository XP_COUNTER_GROUP = varpbit(4696, true);
	public static final VarPlayerRepository XP_COUNTER_COUNTER = varpbit(4697, true);
	public static final VarPlayerRepository XP_COUNTER_PROGRESS_BAR = varpbit(4698, true);
	public static final VarPlayerRepository XP_COUNTER_SPEED = varpbit(4722, true);
	/**
	 * Kalphite lair
	 */
	public static final VarPlayerRepository KALPHITE_LAIR_ROPE_INTERIOR = varpbit(11705, true);
	public static final VarPlayerRepository KALPHITE_LAIR_ROPE_EXTERIOR = varpbit(4586, true);
	/**
	 * Bounty hunter
	 */
	public static final VarPlayerRepository BOUNTY_HUNTER_RISK = varpbit(1538, false);
	public static final VarPlayerRepository BOUNTY_HUNTER_EMBLEM = varpbit(4162, false);
	public static final VarPlayerRepository BOUNTY_HUNTER_RECORD_OVERLAY = varpbit(1621, true);
	public static final VarPlayerRepository BOUNTY_HUNTER_ROGUE_KILLS = varp(1134, true);

	public static final VarPlayerRepository BOUNTY_HUNTER_ROGUE_RECORD = varp(1133, true);
	public static final VarPlayerRepository BOUNTY_HUNTER_TARGET_KILLS = varp(1136, true);

	public static final VarPlayerRepository BOUNTY_HUNTER_TARGET_RECORD = varp(1135, true);
	public static final VarPlayerRepository BOUNTY_HUNTER_TELEPORT = varpbit(2010, true);
	/**
	 * Chatbox interface setting
	 */
	public static final VarPlayerRepository CHATBOX_INTERFACE_USE_FULL_FRAME = varpbit(10670, false);
	/**
	 * Pets
	 */
	public static final VarPlayerRepository PET_NPC_INDEX = varp(447, false);
	/*
	 * public static final Config INSURED_PET_DAGANNOTH_SUPREME = varpbit(4338,
	 * true);
	 * public static final Config INSURED_PET_DAGANNOTH_PRIME = varpbit(4339, true);
	 * public static final Config INSURED_PET_DAGANNOTH_REX = varpbit(4340, true);
	 * public static final Config INSURED_PET_PENANCE_QUEEN = varpbit(4341, true);
	 * public static final Config INSURED_PET_KREEARRA = varpbit(4342, true);
	 * public static final Config INSURED_PET_GRAARDOR = varpbit(4343, true);
	 * public static final Config INSURED_PET_ZILYANA = varpbit(4344, true);
	 * public static final Config INSURED_PET_KRIL = varpbit(4345, true);
	 * public static final Config INSURED_PET_MOLE = varpbit(4346, true);
	 * public static final Config INSURED_PET_KBD = varpbit(4347, true);
	 * public static final Config INSURED_PET_KQ = varpbit(4348, true);
	 * public static final Config INSURED_PET_SMOKE_DEVIL = varpbit(4349, true);
	 * public static final Config INSURED_PET_KRAKEN = varpbit(4350, true);
	 * public static final Config INSURED_PET_CHOMPY = varpbit(4445, true);
	 * public static final Config INSURED_PET_CALLISTO = varpbit(4568, true);
	 * public static final Config INSURED_PET_VENENATIS = varpbit(4429, true);
	 * public static final Config INSURED_PET_VETION_PURPLE = varpbit(4569, true);
	 * public static final Config INSURED_PET_SCORPIA = varpbit(4570, true);
	 * public static final Config INSURED_PET_JAD = varpbit(4699, true);
	 * public static final Config INSURED_PET_CERBERUS = varpbit(4726, true);
	 * public static final Config INSURED_PET_HERON = varpbit(4846, true);
	 * public static final Config INSURED_PET_GOLEM = varpbit(4847, true);
	 * public static final Config INSURED_PET_BEAVER = varpbit(4848, true);
	 * public static final Config INSURED_PET_CHINCHOMPA = varpbit(4849, true);
	 * public static final Config INSURED_PET_ABYSSAL = varpbit(204, true);
	 * public static final Config INSURED_PET_CORE = varpbit(997, true);
	 * public static final Config INSURED_PET_ZULRAH = varpbit(1526, true);
	 * public static final Config INSURED_PET_CHAOS_ELE = varpbit(3962, true);
	 * public static final Config INSURED_PET_GIANT_SQUIRREL = varpbit(2169, true);
	 * public static final Config INSURED_PET_TANGLEROOT = varpbit(2170, true);
	 * public static final Config INSURED_PET_RIFT_GUARDIAN = varpbit(2171, true);
	 * public static final Config INSURED_PET_ROCKY = varpbit(2172, true);
	 * public static final Config INSURED_PET_BLOODHOUND = varpbit(5181, true);
	 * public static final Config INSURED_PET_PHOENIX = varpbit(5363, true);
	 * public static final Config INSURED_PET_OLMLET = varpbit(5448, true);
	 * public static final Config INSURED_PET_SKOTOS = varpbit(5632, true);
	 * public static final Config INSURED_PET_JAL_NIB_REK = varpbit(5644, true);
	 * public static final Config INSURED_PET_MIDNIGHT = varpbit(6013, true);
	 * public static final Config INSURED_PET_HERBI = varpbit(5735, true);
	 */
	/**
	 * Corp beast damage counter
	 */
	public static final VarPlayerRepository CORPOREAL_BEAST_DAMAGE = varp(1142, false);
	/**
	 * Raid party configs
	 */
	public static final VarPlayerRepository RAIDS_PREFERRED_PARTY_SIZE = varp(1432, false);
	public static final VarPlayerRepository RAIDS_PREFERRED_COMBAT_LEVEL = varpbit(5426, false);
	public static final VarPlayerRepository RAIDS_PREFERRED_SKILL_TOTAL = varpbit(5427, false);

	public static final VarPlayerRepository RAIDS_PARTY = varp(1427, false).defaultValue(-1);
	public static final VarPlayerRepository RAIDS_TAB_ICON = varpbit(5432, false).defaultValue(-1);

	public static final VarPlayerRepository RAIDS_STAGE = varp(1430, false).defaultValue(-1);

	public static final VarPlayerRepository RAIDS_PARTY_POINTS = varpbit(5431, false);
	public static final VarPlayerRepository RAIDS_PERSONAL_POINTS = varpbit(5422, false);
	public static final VarPlayerRepository RAIDS_TIMER = varpbit(6386, false);
	public static final VarPlayerRepository RAIDS_STORAGE_WARNING_DISMISSED = varpbit(5509, false);
	public static final VarPlayerRepository RAIDS_STORAGE_PRIVATE_INVENTORY = varpbit(3459, false);
	/**
	 * Gamemode (ironman)
	 */
	public static final VarPlayerRepository CHAT_ICONS = varpbit(15024, true);

	public static final VarPlayerRepository SECONDARY_CHAT_ICONS = varpbit(16001, true);
	public static final VarPlayerRepository TITLE = varpbit(16000, true);
	public static final VarPlayerRepository IRONMAN_MODE = varpbit(1777, true);

	/**
	 * Used to toggle the friends/ignore list frame icon
	 */
	public static final VarPlayerRepository FRIENDS_AND_IGNORE_TOGGLE = varpbit(6516, true);
	/**
	 * Emotes
	 */
	public static final VarPlayerRepository UNLOCK_FLAP_EMOTE = varpbit(2309, true);

	public static final VarPlayerRepository UNLOCK_SLAP_HEAD_EMOTE = varpbit(2310, true);
	public static final VarPlayerRepository UNLOCK_IDEA_EMOTE = varpbit(2311, true);
	public static final VarPlayerRepository UNLOCK_STAMP_EMOTE = varpbit(2312, true);

	public static final VarPlayerRepository UNLOCK_GOBLIN_BOW_AND_SALUTE_EMOTE = varpbit(532, true);

	public static final VarPlayerRepository EMOTES = varp(313, true);

	/**
	 * Magic book
	 */
	public static final VarPlayerRepository DISABLE_SPELL_FILTERING = varpbit(6718, true);
	public static final VarPlayerRepository SHOW_COMBAT_SPELLS = varpbit(6605, true);
	public static final VarPlayerRepository SHOW_UTILITY_SPELLS = varpbit(6606, true);
	public static final VarPlayerRepository SHOW_SPELLS_LACK_LEVEL = varpbit(6607, true);
	public static final VarPlayerRepository SHOW_SPELLS_LACK_RUNES = varpbit(6608, true);
	public static final VarPlayerRepository SHOW_TELEPORT_SPELLS = varpbit(6609, true);

	public static final VarPlayerRepository SHOW_SPELLS_LACK_REQS = varpbit(12137, true);
	public static final VarPlayerRepository IN_WILDERNESS = varpbit(5963, true);
	public static final VarPlayerRepository BLIGHTED_SACK_ENABLED = varpbit(11028, true);

	/**
	 * Clan Wars
	 */
	public static final VarPlayerRepository CLAN_WARS_GAME_END = varpbit(4270, false);
	public static final VarPlayerRepository CLAN_WARS_MELEE_DISABLED = varpbit(4271, false);
	public static final VarPlayerRepository CLAN_WARS_RANGING_DISABLED = varpbit(4272, false);
	public static final VarPlayerRepository CLAN_WARS_MAGIC_DISABLED = varpbit(4273, false);
	public static final VarPlayerRepository CLAN_WARSS_PRAYER_DISABLED = varpbit(4274, false);
	public static final VarPlayerRepository CLAN_WARS_FOOD_DISABLED = varpbit(4275, false);
	public static final VarPlayerRepository CLAN_WARS_DRINKS_DISABLED = varpbit(4276, false);
	public static final VarPlayerRepository CLAN_WARS_SPECIAL_ATTACKS_DISABLED = varpbit(4277, false);
	public static final VarPlayerRepository CLAN_WARS_STRAGGLERS = varpbit(4278, false);
	public static final VarPlayerRepository CLAN_WARS_IGNORE_FREEZING = varpbit(4279, false);
	public static final VarPlayerRepository CLAN_WARS_PJ_TIMER = varpbit(4280, false);
	public static final VarPlayerRepository CLAN_WARS_ALLOW_TRIDENT = varpbit(4281, false);
	public static final VarPlayerRepository CLAN_WARS_SINGLE_SPELLS = varpbit(4282, false);
	public static final VarPlayerRepository CLAN_WARS_ARENA = varpbit(4283, false);
	public static final VarPlayerRepository CLAN_WARS_ACCEPT = varpbit(4285, false);
	public static final VarPlayerRepository CLAN_WARS_COUNTDOWN_TIMER = varpbit(4286, false);
	public static final VarPlayerRepository CLAN_WARS_TEAM_1_COUNT = varpbit(4287, false);
	public static final VarPlayerRepository CLAN_WARS_TEAM_2_COUNT = varpbit(4288, false);
	public static final VarPlayerRepository CLAN_WARS_ACTIVE_TEAM = varpbit(4289, false);
	/**
	 * Catacombs entrances
	 */
	public static final VarPlayerRepository CATACOMBS_ENTRANCE_NW = varpbit(5090, true);
	public static final VarPlayerRepository CATACOMBS_ENTRANCE_SW = varpbit(5088, true);
	public static final VarPlayerRepository CATACOMBS_ENTRANCE_SE = varpbit(5087, true);
	public static final VarPlayerRepository CATACOMBS_ENTRANCE_NE = varpbit(5089, true);
	/**
	 * Death storage
	 */
	public static final VarPlayerRepository DEATH_STORAGE_TYPE = varp(261, true);
	public static final VarPlayerRepository SACRIFICE_ITEM_PRICE = varp(264, true);

	/**
	 * Theatre of Blood 1=In Party, 2=Inside/Spectator, 3=Dead Spectating
	 */
	public static final VarPlayerRepository THEATRE_OF_BLOOD = varpbit(6440, false);
	/**
	 * Theatre of Blood party hud, 0 = List, 1 = Orbs
	 */
	public static final VarPlayerRepository THEATRE_HUD_STATE = varpbit(6441, false);
	public static final VarPlayerRepository BLOAT_DOOR = varpbit(6447, false);
	public static final VarPlayerRepository THEATRE_HP_BAR = varpbit(6447, false);

	public static final VarPlayerRepository THEATRE_HP_BAR_CURRENT = varpbit(6448, false);

	public static final VarPlayerRepository THEATRE_HP_BAR_MAXHP = varpbit(6449, false);// dont seem to work

	public static final VarPlayerRepository TOB_PARTY_LEADER = varp(1740, false).defaultValue(-1);
	/**
	 * Theatre of Blood orb varbits each number stands for the player's health on a
	 * scale of 1-27 (I think), 0 hides the orb
	 */
	public static final VarPlayerRepository THEATRE_OF_BLOOD_ORB_1 = varpbit(6442, false);
	public static final VarPlayerRepository THEATRE_OF_BLOOD_ORB_2 = varpbit(6443, false);

	public static final VarPlayerRepository THEATRE_OF_BLOOD_ORB_3 = varpbit(6444, false);
	public static final VarPlayerRepository THEATRE_OF_BLOOD_ORB_4 = varpbit(6445, false);
	public static final VarPlayerRepository THEATRE_OF_BLOOD_ORB_5 = varpbit(6446, false);

	/**
	 * Collection log
	 */
	public static final VarPlayerRepository COLLECTION_LOG_KC_TAB = varp(2048, false);

	/**
	 * Hp bar for zalcano/KROTA
	 */

	public static final VarPlayerRepository COMBAT_CURRENT_HP = varpbit(6099, true);
	public static final VarPlayerRepository HPBARSETNAME = varp(1683, true);
	public static final VarPlayerRepository COMBAT_MAX_HP = varpbit(6100, true);
	public static final VarPlayerRepository IMPREGNANTED = varpbit(10151, false);
	/**
	 * KROTA Zone
	 */
	public static final VarPlayerRepository NMZ_COFFER_STATUS = varpbit(3957, false);

	public static final VarPlayerRepository NMZ_COFFER_AMT = varpbit(3948, false);

	public static final VarPlayerRepository NMZ_ABSORPTION = varpbit(3956, false);
	public static final VarPlayerRepository NMZ_POINTS = varpbit(3949, false);
	public static final VarPlayerRepository NMZ_REWARD_POINTS_TOTAL = varp(1060, true);

	public static final VarPlayerRepository DIZANA_QUIVER_AMMO_ID = varp(4142, true).defaultValue(-1);
	public static final VarPlayerRepository DIZANA_QUIVER_AMMO_AMOUNT = varp(4141, true).defaultValue(0);

	public static void register() {
		int offset = 2;
		for (int i_4_ = 0; i_4_ < 32; i_4_++) {
			SHIFTS[i_4_] = offset - 1;
			offset += offset;
		}
	}

	public static void appendPersistentVarp(int id) {
		appendPersistentVarp(id, 0);
	}

	public static void appendPersistentVarp(int id, int defaultValue) {
		var varp = varp(id, true).defaultValue(defaultValue);
		appendPersistent(varp);
	}

	public static void appendPersistentVarbit(int id) {
		appendPersistentVarbit(id, 0);
	}

	public static void appendPersistentVarbit(int id, int defaultValue) {
		var varp = varpbit(id, true).defaultValue(defaultValue);
		appendPersistent(varp);
	}

	public static void appendPersistent(VarPlayerRepository varp) {
		CONFIGS.add(varp);
	}

	/**
	 * Creation
	 */

	public static VarPlayerRepository varp(int id, boolean save) {
		return create(id, null, save, true);
	}

	public static VarPlayerRepository varpbit(int id, boolean save) {
		return create(id, VarBitType.get(id), save, true);
	}

	public static VarPlayerRepository create(int id, VarBitType bit, boolean save, boolean store) {
		VarPlayerRepository config = new VarPlayerRepository();
		config.id = id;
		config.bit = bit;
		config.save = save;
		if (store) {
			appendPersistent(config);
		}
		return config;
	}

	/**
	 * Loading
	 */

	public static void save(Player player) {
		for (VarPlayerRepository config : CONFIGS) {
			if (!config.save) {
				continue;
			}
			int saveId = config.id << 16 | (config.bit != null ? 1 : 0);
			player.savedConfigs.put(saveId, config.get(player));
		}
	}

	public static void load(Player player) {
		for (VarPlayerRepository config : CONFIGS) {

			if (!config.save) {
				if (config.defaultValue != 0) {
					config.set(player, config.defaultValue);
				}
				config.update(player);
				continue;
			}

			int saveId = config.id << 16 | (config.bit != null ? 1 : 0);
			Integer savedValue = player.savedConfigs.get(saveId);
			if (savedValue == null) {
				if (config.defaultValue != 0) {
					config.set(player, config.defaultValue);
				}
				config.update(player);
				continue;
			}

			config.set(player, savedValue);
			config.update(player);
		}
	}

	public int id;
	public VarBitType bit;
	private boolean save;
	private int defaultValue;

	public VarPlayerRepository defaultValue(int defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	public VarPlayerRepository forceSend() {
		return this;
	}

	public void update(Player player) {
		player.updateVarp(bit == null ? id : bit.varpId);
	}

	public int toggle(Player player) {
		if (get(player) == 0) {
			set(player, 1);
			return 1;
		} else {
			set(player, 0);
			return 0;
		}
	}

	public int increment(Player player, int amount) {
		int newValue = get(player) + amount;
		set(player, newValue);
		return newValue;
	}

	public void reset(Player player) {
		set(player, defaultValue);
	}

	public void set(Player player, int value) {
		if (bit != null) {
			int varpId = bit.varpId;
			int least = bit.leastSigBit;
			int most = bit.mostSigBit;
			int shift = SHIFTS[most - least];
			if (value < 0 || value > shift)
				value = 0;
			int varpValue = player.varps[varpId];
			shift <<= least;
			player.varps[varpId] = ((varpValue & (~shift)) | value << least & shift);
			player.updateVarp(varpId);
		} else {
			player.varps[id] = value;
			player.updateVarp(id);
		}
	}

	public static int getVarpBitValue(int varValue, int least, int most) {
		int shift = SHIFTS[most - least];
		return varValue >> least & shift;
	}

	public int get(Player player) {
		if (bit != null) {
			int varpId = bit.varpId;
			int least = bit.leastSigBit;
			int most = bit.mostSigBit;
			int shift = SHIFTS[most - least];
			return player.varps[varpId] >> least & shift;
		}
		return player.varps[id];
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((bit == null) ? 0 : bit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VarPlayerRepository other = (VarPlayerRepository) obj;
		if (id != other.id)
			return false;
		if (bit == null) {
			if (other.bit != null)
				return false;
		} else if (!bit.equals(other.bit))
			return false;
		return true;
	}

}
