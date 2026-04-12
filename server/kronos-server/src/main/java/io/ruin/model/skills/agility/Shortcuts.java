package io.ruin.model.skills.agility;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.skills.agility.shortcut.*;
import io.ruin.model.stat.StatType;

public class Shortcuts {
	public static void register() {


		/**
		 *                                              GRAPPLE
		 * _____________________________________________________________________________________________________________
		 */

		// (Grapple) Scale Falador wall
		ObjectAction.register(17050, 3032, 3389, 0, "grapple", (p, obj) -> Grappling.grapple(p, obj, 11, 19, 37, 4455, 760, 10, Position.of(3032, 3388, 0), Position.of(3032, 3389, 1)));
		ObjectAction.register(17049, 3033, 3390, 0, "grapple", (p, obj) -> Grappling.grapple(p, obj, 11, 19, 37, 4455, 760, 10, Position.of(3033, 3390, 0), Position.of(3033, 3389, 1)));
		ObjectAction.register(17051, 3033, 3390, 1, "jump", ((player, obj) -> {
			player.startEvent(event -> {
				player.animate(2586);
				event.delay(1);
				player.getMovement().teleport(3033, 3390, 0);
			});
		}));
		ObjectAction.register(17052, 3032, 3388, 1, "jump", ((player, obj) -> {
			player.startEvent(event -> {
				player.animate(2586);
				event.delay(1);
				player.getMovement().teleport(3032, 3388, 0);
			});
		}));

		// (Grapple) Scale yanille wall
		ObjectAction.register(17047, 2556, 3075, 0, "grapple", (p, obj) -> Grappling.grapple(p, obj, 11, 19, 37, 4455, 760, 10, Position.of(2556, 3075, 0), Position.of(2556, 3074, 1)));
		ObjectAction.register(17047, 2556, 3072, 0, "grapple", (p, obj) -> Grappling.grapple(p, obj, 11, 19, 37, 4455, 760, 10, Position.of(2556, 3072, 0), Position.of(2556, 3073, 1)));
		ObjectAction.register(17048, 2556, 3072, 1, "jump", ((player, obj) -> {
			player.startEvent(event -> {
				player.animate(2586);
				event.delay(1);
				player.getMovement().teleport(2556, 3072, 0);
			});
		}));
		ObjectAction.register(17048, 2556, 3075, 1, "jump", ((player, obj) -> {
			player.startEvent(event -> {
				player.animate(2586);
				event.delay(1);
				player.getMovement().teleport(2556, 3075, 0);
			});
		}));

		/**
		 *                                              FENCE JUMP
		 * _____________________________________________________________________________________________________________
		 */

		// Varrock south fence jump
		ObjectAction.register(16518, "jump-over", (p, obj) -> JumpShortcut.VARROCK_JUMP13.traverse(p, obj));

		ObjectAction.register(544, "jump", (p, obj) -> JumpShortcut.RELEKKE_JUMP.traverse(p, obj));


		/**
		 *                                              CREVICES
		 * _____________________________________________________________________________________________________________
		 */

		// Dwarven Mine narrow crevice
		ObjectAction.register(16543, 3034, 9806, 0, "squeeze-through", (player, obj) -> {
			squeezeThroughCrack(player, obj, new Position(3028, 9806, 0), 42);
		}); // crack
		ObjectAction.register(16543, 3029, 9806, 0, "squeeze-through", (player, obj) -> {
			squeezeThroughCrack(player, obj, new Position(3035, 9806, 0), 42);

		}); // crack

		// Camelot loose railing
		ObjectAction.register(51, 2662, 3500, 0, "squeeze-through", (p, obj) -> LooseRailing.shortcut(p, obj, 1));

		/**
		 *                                              STILES
		 * _____________________________________________________________________________________________________________
		 */
		// Relekka rock crabs
		ObjectAction.register(41438, 2675, 3707, 0, "climb-over", (p, obj) -> Stile.shortcut(p, obj, 1));


		// Decayin trunk for Deranged archaeologist boss
		ObjectAction.register(31842, 3682, 3716, 0, "climb", (p, obj) -> Stile.shortcutN(p, obj, 1));


		// Stile at Fred the Farmer's sheep field and the stile at Falador cabbage patch
		ObjectAction.register(7527, 3063, 3282, 0, "climb-over", (p, obj) -> Stile.shortcutN(p, obj, 1));

		// Stile at Taverly which is required for the clue scroll south of the long house
		ObjectAction.register(993, 2917, 3438, 0, "climb-over", (p, obj) -> Stile.shortcut(p, obj, 1));

		// Stile into the beahive in Camelot
		ObjectAction.register(993, "climb-over", (p, obj) -> Stile.shortcutN(p, obj, 1));

		//Eagle's peak
		ObjectAction.register(19222, "climb-over", (p, obj) -> Stile.shortcutN(p, obj, 1));

		// Draynor Stile into cabbage field
		ObjectAction.register(7527, "climb-over", (p, obj) -> Stile.shortcut(p, obj, 1));

		// lumberyard stile
		ObjectAction.register(2618, "climb-over", (p, obj) -> Stile.shortcutN(p, obj, 1));

		// Lumbridge Stile into sheep farm
		ObjectAction.register(12892, "climb-over", (p, obj) -> Stile.shortcut(p, obj, 1));


		/**
		 *                                              CLIMBING ROCKS
		 * _____________________________________________________________________________________________________________
		 */
		// Climbing Rocks Lv. 1
		ObjectAction.register(2231, 2792, 2978, 0, "Climb", ClimbingSpots.CAIRN_S_CLIMB1::traverse);
		ObjectAction.register(2231, 2794, 2978, 0, "Climb", ClimbingSpots.CAIRN_S_CLIMB1::traverse);
		ObjectAction.register(2231, 2792, 2979, 0, "Climb", ClimbingSpots.CAIRN_M_CLIMB1::traverse);
		ObjectAction.register(2231, 2794, 2979, 0, "Climb", ClimbingSpots.CAIRN_M_CLIMB1::traverse);
		ObjectAction.register(2231, 2792, 2980, 0, "Climb", ClimbingSpots.CAIRN_N_CLIMB1::traverse);


		// Climbing Rocks Lv. 10
		ObjectAction.register(31757, 2546, 2872, 0, "Climb", (p, obj) -> Stile.shortcutN(p, obj, 10));

		// Climbing Rocks Lv.25
		ObjectAction.register(19849, 2323, 3497, 0, "Climb", ClimbingSpots.EAGLES_TOP_W_CLIMB25::traverse);
		ObjectAction.register(19849, 2324, 3498, 0, "Climb", ClimbingSpots.EAGLES_TOP_E_CLIMB25::traverse);
		ObjectAction.register(19849, 2322, 3501, 0, "Climb", ClimbingSpots.EAGLES_TOP_W_CLIMB25::traverse);

		// Climbing Rocks Lv. 37
		ObjectAction.register(16535, 2489, 3520, 0, "Climb", ClimbingSpots.GNOME_CLIMB37::traverse);
		ObjectAction.register(16534, 2487, 3515, 0, "Climb", ClimbingSpots.GNOME_CLIMB37::traverse);

		// Climbing Rocks Lv. 38
		ObjectAction.register(16550, 3303, 3315, 0, "Climb", ClimbingSpots.AL_KHARID_CLIMB38::traverse);
		ObjectAction.register(16549, 3305, 3315, 0, "Climb", ClimbingSpots.AL_KHARID_CLIMB38::traverse);

		//Climbing Rocks Lv. 41
		ObjectAction.register(16521, 2870, 3671, 0, "Climb", ClimbingSpots.TROLL_CLIMB41::traverse);
		ObjectAction.register(16521, 2871, 3671, 0, "Climb", ClimbingSpots.TROLL_CLIMB41::traverse);

		//Climbing Rocks Lv. 43
		ObjectAction.register(16522, 2878, 3666, 0, "Climb", ClimbingSpots.TROLL_1_CLIMB43::traverse);
		ObjectAction.register(16522, 2878, 3667, 0, "Climb", ClimbingSpots.TROLL_1_CLIMB43::traverse);

		//Climbing Rocks Lv. 43
		ObjectAction.register(3803, 2888, 3661, 0, "Climb", ClimbingSpots.TROLL_3_CLIMB43::traverse);
		ObjectAction.register(3804, 2887, 3661, 0, "Climb", ClimbingSpots.TROLL_2_CLIMB43::traverse);

		//Climbing Rocks Lv. 43
		ObjectAction.register(3803, 2885, 3683, 0, "Climb", ClimbingSpots.TROLL_5_CLIMB43::traverse);
		ObjectAction.register(3804, 2885, 3684, 0, "Climb", ClimbingSpots.TROLL_4_CLIMB43::traverse);

		//Climbing Rocks Lv. 44
		ObjectAction.register(16523, 2908, 3682, 0, "Climb", ClimbingSpots.TROLL_W_CLIMB44::traverse);
		ObjectAction.register(16523, 2909, 3683, 0, "Climb", ClimbingSpots.TROLL_S_CLIMB44::traverse);

		//Climbing Rocks Lv. 47
		ObjectAction.register(16524, 2902, 3680, 0, "Climb", ClimbingSpots.TROLL_CLIMB47::traverse);
		ObjectAction.register(16524, 2901, 3680, 0, "Climb", ClimbingSpots.TROLL_CLIMB47::traverse);


		// Climbing Rocks Lv. 59
		ObjectAction.register(16515, 2344, 3295, 0, "Climb", ClimbingSpots.ARANDAR_CLIMB59::traverse);
		ObjectAction.register(16514, 2346, 3299, 0, "Climb", ClimbingSpots.ARANDAR_CLIMB59::traverse);

		// Climb Rocks Lv. 68
		ObjectAction.register(16515, 2338, 3285, 0, "Climb", ClimbingSpots.ARANDAR_CLIMB68::traverse);
		ObjectAction.register(16514, 2338, 3282, 0, "Climb", ClimbingSpots.ARANDAR_CLIMB68::traverse);

		//Climbing Rocks Lv. 73
		ObjectAction.register(16464, 2843, 3693, 0, "Climb", ClimbingSpots.TROLL_HERB_CLIMB73::traverse);
		ObjectAction.register(16464, 2839, 3693, 0, "Climb", ClimbingSpots.TROLL_HERB_CLIMB73::traverse);

		// Climb Rocks Lv. 85
		ObjectAction.register(16515, 2337, 3253, 0, "Climb", ClimbingSpots.ARANDAR_CLIMB85::traverse);
		ObjectAction.register(16514, 2333, 3252, 0, "Climb", ClimbingSpots.ARANDAR_CLIMB85::traverse);

		/**
		 *                                              BROKEN WALLS
		 * _____________________________________________________________________________________________________________
		 */
		// Falador Agility Shortcut
		ObjectAction.register(24222, "climb-over", (p, obj) -> {
			CrumblingWall.shortcut(p, obj, 5);
		});


		/**
		 *                                              STEPPING STONES
		 * _____________________________________________________________________________________________________________
		 */

		// Stepping Stone Lv. 12
		ObjectAction.register(21738, 2649, 9561, 0, "Jump-from", SteppingStone.BRIMHAVEN_STONES12::traverse);
		ObjectAction.register(21739, 2647, 9558, 0, "Jump-from", SteppingStone.BRIMHAVEN_STONES12::traverse);

		// Stepping Stone Lv. 15
		Tile.getObject(31809, 1981, 8996, 1).skipReachCheck = p -> p.equals(1981, 8994, 1) || p.equals(1981, 8998, 1);
		ObjectAction.register(31809, "Jump-to", SteppingStone.CORSAIR_COVE_STONES15::traverse);

		// Stepping Stone Lv. 30
		ObjectAction.register(23645, "Cross", SteppingStone.KARAMJA_STONES30::traverse);
		ObjectAction.register(23647, "Cross", SteppingStone.KARAMJA_STONES30::traverse);

		// Stepping Stone Lv. 31
		ObjectAction.register(16533, "Jump-onto", SteppingStone.DRAYNOR_STONES31::traverse);

		// Stepping Stone Lv. 40 (EAST STONE)
		Tile.getObject(29729, 1612, 3570, 0).skipReachCheck = p -> p.equals(1610, 3570) || p.equals(1614, 3570);
		ObjectAction.register(29729, "Cross", SteppingStone.ZEAH_E_STONES40::traverse);
		// Stepping Stone Lv. 40 (WEST STONE)
		Tile.getObject(29730, 1605, 3571, 0).skipReachCheck = p -> p.equals(1607, 3571) || p.equals(1603, 3571);
		ObjectAction.register(29730, "Cross", SteppingStone.ZEAH_W_STONES40::traverse);

		// Stepping Stone Lv. 45
		Tile.getObject(29728, 1722, 3551, 0).skipReachCheck = p -> p.equals(1720, 3551) || p.equals(1724, 3551);
		ObjectAction.register(29728, "Cross", SteppingStone.ZEAH_STONES45::traverse);

		// Stepping Stone Lv. 50
		Tile.getObject(13504, 3419, 3325, 0).skipReachCheck = p -> p.equals(3417, 3325) || p.equals(3421, 3323);
		ObjectAction.register(13504, "Cross", SteppingStone.MORTMYRE_STONES50::traverse);

		// Stepping Stone Lv. 55
		Tile.getObject(11768, 2573, 3861, 0).skipReachCheck = p -> p.equals(2573, 3859) || p.equals(2575, 3861);
		ObjectAction.register(11768, "Cross", SteppingStone.MISCELLANIA_STONES55::traverse);

		// Stepping Stone Lv. 60
		Tile.getObject(19042, 3711, 2969, 0).skipReachCheck = p -> p.equals(3708, 2969) || p.equals(3715, 2969);
		ObjectAction.register(19042, "Jump-to", SteppingStone.MOS_LEHARMLESS_STONES60::traverse);

		// Stepping Stone Lv. 66
		Tile.getObject(16513, 3214, 3135, 0).skipReachCheck = p -> p.equals(3212, 3137) || p.equals(3214, 3132);
		ObjectAction.register(16513, "Jump-to", SteppingStone.LUMBRIDGE_STONES66::traverse);

		// Stepping Stone Lv. 74
		Tile.getObject(14918, 3201, 3808, 0).skipReachCheck = p -> p.equals(3201, 3810) || p.equals(3201, 3807);
		ObjectAction.register(14918, "Cross", SteppingStone.WILDERNESS_LAVADRAG_STONES74::traverse);

		// Stepping Stone Lv. 76
		Tile.getObject(10663, 2157, 3072, 0).skipReachCheck = p -> p.equals(2160, 3072) || p.equals(2154, 3072);
		ObjectAction.register(10663, "Cross", SteppingStone.ZULRAH_STONES76::traverse);

		ObjectAction.register(16466, "Cross", SteppingStone.SHILO_STONES77::traverse);

		// Stepping Stone Lv. 82
		Tile.getObject(14917, 3092, 3879, 0).skipReachCheck = p -> p.equals(3091, 3882) || p.equals(3093, 3879);
		ObjectAction.register(14917, "Cross", SteppingStone.WILDERNESS_LAVAMAZE_STONES82::traverse);

		// Stepping Stone Lv. 83 (NORTH STONES)
		Tile.getObject(19040, 2684, 9548, 0).skipReachCheck = p -> p.equals(2682, 9548);
		Tile.getObject(19040, 2688, 9547, 0).skipReachCheck = p -> p.equals(2690, 9547);
		ObjectAction.register(19040, "Cross", SteppingStone.BRIMHAVEN_N_STONES83::traverse);
		// Stepping Stone Lv. 83 (SOUTH STONES)
		Tile.getObject(19040, 2695, 9531, 0).skipReachCheck = p -> p.equals(2695, 9533);
		Tile.getObject(19040, 2696, 9527, 0).skipReachCheck = p -> p.equals(2697, 9525);
		ObjectAction.register(19040, "Cross", SteppingStone.BRIMHAVEN_S_STONES83::traverse);


		ObjectAction.register(16543, "Squeeze-through", CreviceShortcut.FALADOR::squeeze);
		ObjectAction.register(31617, "Pass", CreviceShortcut.SDZ::squeeze);

		/**
		 *                                              UNDER WALL
		 * _____________________________________________________________________________________________________________
		 */

		// Grand Exchange Agility Shortcut
		ObjectAction.register(16529, "climb-into", (p, obj) -> UnderwallTunnel.shortcut(p, obj, 21));
		ObjectAction.register(16530, "climb-into", (p, obj) -> UnderwallTunnel.shortcut(p, obj, 21));

		// Pipe contortion in Brimhaven Dungeon
		// Eagles' Peak Agility Shortcut

		// Underwall tunnel	Falador Agility Shortcut
		ObjectAction.register(16527, "climb-into", (p, obj) -> UnderwallTunnel.shortcutN(p, obj, 26));
		ObjectAction.register(16528, "climb-into", (p, obj) -> UnderwallTunnel.shortcutN(p, obj, 26));

		// Underwall tunnel	Falador Agility Shortcut
		ObjectAction.register(16519, "climb-into", (p, obj) -> UnderwallTunnel.shortcutN(p, obj, 16));
		ObjectAction.register(16520, "climb-into", (p, obj) -> UnderwallTunnel.shortcutN(p, obj, 16));


		/**
		 *                                              LOG BALANCE
		 * _____________________________________________________________________________________________________________
		 */

		// Lob Balance Lv. 1
		ObjectAction.register(23644, "Cross", LogBalance.KARAMJA_LOG1::traverse);

		// Log Balance Lv. 20
		ObjectAction.register(23274, "Walk-across", LogBalance.CAMELOT_LOG20::traverse);

		// Log Balance Lv. 30
		ObjectAction.register(20882, "Walk-across", LogBalance.BRIMHAVEN_LOG30::traverse);
		ObjectAction.register(20884, "Walk-across", LogBalance.BRIMHAVEN_LOG30::traverse);

		// Log Balance Lv. 33
		ObjectAction.register(16548, "Walk-across", LogBalance.ARDY_LOG33::traverse);
		ObjectAction.register(16546, "Walk-across", LogBalance.ARDY_LOG33::traverse);

		// Log Balance Lv. 45
		ObjectAction.register(3931, "Cross", LogBalance.ISAFDAR_1_LOG45::traverse);
		ObjectAction.register(3932, "Cross", LogBalance.ISAFDAR_2_LOG45::traverse);
		ObjectAction.register(3933, "Cross", LogBalance.ISAFDAR_3_LOG45::traverse);

		// Log Balance Lv. 48
		ObjectAction.register(16542, "Walk-across", LogBalance.CAMELOT_LOG48::traverse);
		ObjectAction.register(16540, "Walk-across", LogBalance.CAMELOT_LOG48::traverse);

		/**
		 *                                              ROPE SWING
		 * _____________________________________________________________________________________________________________
		 */

		ObjectAction.register(23570, 2511, 3090, 0, "Swing-on", RopeSwing.OGRE_ISLAND::traverse);

		/**
		 * Balancing Ledges
		 */
		ObjectAction.register(23548, 2580, 9519, 0, "Walk-across", BalancingLedge.YANILLE_DUNGEON_ENTRANCE_1::traverse);
		ObjectAction.register(23548, 2580, 9513, 0, "Walk-across", BalancingLedge.YANILLE_DUNGEON_ENTRANCE_2::traverse);

		ObjectAction.register(16468, 2927, 3522, 0, "Manoeuvre-past", BalancingLedge.GOBLIN_VILLAGE_WALL::goblinwall);
		ObjectAction.register(16468, 2928, 3521, 0, "Manoeuvre-past", BalancingLedge.GOBLIN_VILLAGE_WALL_1::goblinwall);

		/**
		 * Monkeybars
		 */
		ObjectAction.register(23567, 2597, 9494, 0, "Swing across", MonkeyBars.YANILLE_DUNGEON_BARS_1::traverse);
		ObjectAction.register(23567, 2598, 9494, 0, "Swing across", MonkeyBars.YANILLE_DUNGEON_BARS_1::traverse);
		ObjectAction.register(23567, 2597, 9489, 0, "Swing across", MonkeyBars.YANILLE_DUNGEON_BARS_2::traverse);
		ObjectAction.register(23567, 2598, 9489, 0, "Swing across", MonkeyBars.YANILLE_DUNGEON_BARS_2::traverse);

		/**
		 * Pipes
		 */
		ObjectAction.register(23140, 2576, 9506, 0, "Squeeze-through", PipeShortcut.YANILLE_PIPE::traverse);
		ObjectAction.register(23140, 2573, 9506, 0, "Squeeze-through", PipeShortcut.YANILLE_PIPE::traverse);

		/**
		 * Pipes
		 */
		ObjectAction.register(34655, 1346, 10231, 0, "enter", (player, obj) -> {
			player.getMovement().teleport(1316, 10213, 0);
		});

		ObjectAction.register(34655, 1316, 10214, 0, "enter", (player, obj) -> {
			player.getMovement().teleport(1346, 10232, 0);
		});

		Tile.getObject(23140, 2576, 9506, 0).walkTo = new Position(2578, 9506, 0);
		Tile.getObject(23140, 2573, 9506, 0).walkTo = new Position(2572, 9506, 0);


	}

	/**
	 * TODO
	 */
	// (Grapple) Scale Catherby cliffside
	// Cairn Isle rock slide climb
	// Ardougne log balance shortcut
	// Pipe contortion in Brimhaven Dungeon
	// Trollweiss/Rellekka Hunter area cliff scramble
	// (Grapple) Escape from the Water Obelisk Island
	// Gnome Stronghold Shortcut

	// Al Kharid mining pit cliffside scramble


	// (Grapple) Scale Yanille wall
	// Yanille Agility dungeon balance ledge
	// Kourend lake isle jump
	// Trollheim easy cliffside scramble
	// Dwarven Mine narrow crevice
	// Draynor narrow tunnel
	// Trollheim medium cliffside scramble
	// Trollheim advanced cliffside scramble
	// Kourend river jump
	// Tirannwn log balance
	// Cosmic Temple - medium narrow walkway
	// Deep Wilderness Dungeon narrow shortcut
	// Trollheim hard cliffside scramble
	// Log balance to Fremennik Province
	// Contortion in Yanille Dungeon small room
	// Arceuus essence mine boulder leap
	// Stepping stone into Morytania near the Nature Grotto
	// Pipe from Edgeville dungeon to Varrock Sewers
	// Arceuus essence mine eastern scramble
	// (Grapple) Karamja Volcano
	// Motherlode Mine wall shortcut
	// Stepping stone by Miscellania docks
	// Monkey bars under Yanille
	// Stepping stones in the Cave Kraken lake
	// Rellekka east fence shortcut
	// Port Phasmatys Ectopool Shortcut
	// Elven Overpass (Arandar) easy cliffside scramble
	// Wilderness from God Wars Dungeon area climb
	// Squeeze through to God Wars Dungeon surface access
	// Estuary crossing on Mos Le'Harmless
	// Slayer Tower medium spiked chain climb
	// Fremennik Slayer Dungeon narrow crevice
	// Taverley Dungeon lesser demon fence
	// Trollheim Wilderness Route
	// Temple on the Salve to Morytania shortcut
	// Cosmic Temple advanced narrow walkway
	// Lumbridge Swamp to Al Kharid narrow crevice
	// Heroes' Guild tunnel
	// Yanille Dungeon's rubble climb
	// Elven Overpass (Arandar) medium cliffside scramble
	// Arceuus essence mine northern scramble
	// Taverley Dungeon pipe squeeze to Blue dragon lair
	// (Grapple) Cross cave, south of Dorgesh-Kaan
	// Rope descent to Saradomin's Encampment
	// Slayer Tower advanced spiked chain climb
	// Stronghold Slayer Cave wall-climb
	// Troll Stronghold wall-climb
	// Arceuus essence mine western descent
	// Lava Dragon Isle jump
	// Island crossing near Zul-Andra
	// Shilo Village stepping stones over the river
	// Kharazi Jungle vine climb
	// Cave crossing south of Dorgesh-Kaan
	// Taverley Dungeon spiked blades jump
	// Fremennik Slayer Dungeon chasm jump
	// Lava Maze northern jump
	// Brimhaven Dungeon eastern stepping stones
	// Elven Overpass (Arandar) advanced cliffside scramble
	// Kalphite Lair wall shortcut
	// Brimhaven Dungeon vine to baby green dragons
	private static void squeezeThroughCrack(Player player, GameObject crack, Position destination, int levelReq) {
		if (!player.getStats().check(StatType.Agility, levelReq, "use this shortcut"))
			return;
		player.lock();
		player.startEvent(event -> {
			player.face(crack);
			player.animate(746);
			event.delay(1);
			player.getMovement().teleport(destination);
			player.animate(748);
			event.delay(1);
			player.unlock();
		});
	}
}
