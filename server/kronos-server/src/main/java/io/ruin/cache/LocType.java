package io.ruin.cache;

import io.ruin.Server;
import io.ruin.api.buffer.InBuffer;
import io.ruin.api.filestore.IndexFile;
import io.ruin.api.utils.StringUtils;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.map.object.actions.ObjectAction;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * AKA gameobject mapobject definition mapdef objdef
 */
public class LocType {

	public static boolean aBool1550 = false;
	private static byte[] EMPTY_BUFFER = new byte[] { 0 };

	public static final Int2ObjectMap<LocType> LOADED = new Int2ObjectOpenHashMap<>();
	public static final Long2ObjectMap<List<LocType>> nameToTypes = new Long2ObjectOpenHashMap<>();

	public static List<LocType> getByName(long nameLong) {
		return nameToTypes.get(nameLong);
	}

	public static List<LocType> getByName(String name) {
		long nameLong = StringUtils.stringToLong(name);
		return getByName(nameLong);
	}

	public static LocType[] LOADED_EXTRA = new LocType[10];

	@SuppressWarnings("Duplicates")
	public static void load() {
		IndexFile index = Server.fileStore.get(2);
		int size = index.getLastFileId(6) + 1;
		for (int id = 0; id < size; id++) {
			byte[] data = index.getFile(6, id);
			if (data != null) {
				LocType def = new LocType();
				def.id = id;
				def.decode(new InBuffer(data));
				if (def.someFlag) {
					def.clipType = 0;
					def.tall = false;
				}
				def.fixName();
				LOADED.put(id, def);

				if (def.name == null)
					continue;

				long nameLong = StringUtils.stringToLong(def.name);
				List<LocType> types = nameToTypes.computeIfAbsent(nameLong, k -> new ObjectArrayList<>());
				types.add(def);
			}
		}
	}

	public void fixName() {
		if (StringUtils.vowelStart(name))
			descriptiveName = "an " + name;
		else
			descriptiveName = "a " + name;
	}

	public static void forEach(Consumer<LocType> consumer) {
		for (LocType def : LOADED.values()) {
			if (def != null)
				consumer.accept(def);
		}
	}

	public static LocType get(int id) {
		return LOADED.get(id);
	}

	/**
	 * Custom data
	 */

	public String descriptiveName;

	public ObjectAction[] defaultActions;

	public HashMap<Integer, ItemObjectAction> itemActions;

	public ItemObjectAction defaultItemAction;

	public boolean bank;

	/**
	 * Door data
	 */

	public boolean gateType;

	public boolean longGate;

	public int doorOppositeId = -1;

	public boolean doorReversed, doorClosed;

	public int doorOpenSound = -1, doorCloseSound = -1;

	public boolean reversedConstructionDoor;

	/**
	 * Separator
	 */

	public int id;
	public int category;
	public int mapAreaId;
	public boolean randomizeAnimStart = false;
	public int type22Int = -1;
	public int unknownOpcode75 = -1;
	public String name = "null";
	public int xLength = 1;
	public int yLength = 1;
	public int clipType = 2;
	public boolean tall = true;
	public boolean aBool1552 = false;
	public int animationID = -1; // idle animation
	public int anInt1595 = 16;
	public int mapMarkerId = -1;
	public boolean aBool1580 = true;
	public int anInt1578 = -1;
	public boolean type22Boolean = false;
	public int ambientSoundId = -1;
	public int unknownOpcode_78_79 = 0;
	public int anInt1548 = 0;
	public int anInt1571 = 0;
	public int[] soundEffectIds;
	public int[] showIds;
	public String[] options = new String[5];
	public int[] modelTypes;
	public int[] modelIds;
	int anInt1569 = -1;
	boolean aBool1582 = false;
	int unknownOpcode29 = 0;
	int anInt1575 = 0;
	public short[] originalModelColors;
	public short[] modifiedModelColors;
	short[] aShortArray1596;
	short[] aShortArray1563;
	public boolean verticalFlip = false;
	public int render0x1 = 128;
	public int render0x2 = 128;
	public int render0x4 = 128;
	int anInt1584 = 0;
	int anInt1585 = 0;
	int anInt1586 = 0;
	public boolean someFlag = false;
	public int varpBitId = -1;
	public int varpId = -1;

	public int someDirection;

	public Long2ObjectMap<Object> params;

	private void decode(InBuffer in) {
		int lastOpcode = -1;
		for (;;) {
			int opcode = in.readUnsignedByte();
			if (opcode == 0)
				break;
			try {
				decode(in, opcode, lastOpcode);
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
			lastOpcode = opcode;
		}
		if (in.remaining() > 0) {
			new IllegalStateException("Unable to decode: " + id + " " + lastOpcode + " " + in.remaining()).printStackTrace();
			System.exit(-1);
		}
	}

	private void decode(InBuffer in, int opcode, int lastOpcode) {
		if (opcode == 1) {
			int i_7_ = in.readUnsignedByte();
			if (i_7_ > 0) {
				if (modelIds == null || aBool1550) {
					modelTypes = new int[i_7_];
					modelIds = new int[i_7_];
					for (int i_8_ = 0; i_8_ < i_7_; i_8_++) {
						modelIds[i_8_] = in.readUnsignedShort();
						modelTypes[i_8_] = in.readUnsignedByte();
					}
				} else
					in.skip(i_7_ * 3);
			}
		} else if (opcode == 2) {
			name = in.readStringCp1252NullTerminated();
		} else if (opcode == 5) {
			int i_9_ = in.readUnsignedByte();
			if (i_9_ > 0) {
				if (modelIds == null || aBool1550) {
					modelTypes = null;
					modelIds = new int[i_9_];
					for (int i_10_ = 0; i_10_ < i_9_; i_10_++)
						modelIds[i_10_] = in.readUnsignedShort();
				} else
					in.skip(i_9_ * 2);
			}
		} else if (opcode == 14)
			xLength = in.readUnsignedByte();
		else if (opcode == 15)
			yLength = in.readUnsignedByte();
		else if (opcode == 17) {
			clipType = 0;
			tall = false;
		} else if (opcode == 18)
			tall = false;
		else if (opcode == 19)
			type22Int = in.readUnsignedByte();
		else if (opcode == 21)
			anInt1569 = 0;
		else if (opcode == 22)
			aBool1582 = true;
		else if (opcode == 23)
			aBool1552 = true;
		else if (opcode == 24) {
			animationID = in.readUnsignedShort();
			if (animationID == 65535)
				animationID = -1;
		} else if (opcode == 27)
			clipType = 1;
		else if (opcode == 28)
			anInt1595 = in.readUnsignedByte();
		else if (opcode == 29)
			unknownOpcode29 = in.readByte();
		else if (opcode == 39)
			anInt1575 = in.readByte() * 25;
		else if (opcode >= 30 && opcode < 35) {
			options[opcode - 30] = in.readStringCp1252NullTerminated();
			if (options[opcode - 30].equalsIgnoreCase("Hidden"))
				options[opcode - 30] = null;
		} else if (opcode == 40) {
			int i_11_ = in.readUnsignedByte();
			originalModelColors = new short[i_11_];
			modifiedModelColors = new short[i_11_];
			for (int i_12_ = 0; i_12_ < i_11_; i_12_++) {
				originalModelColors[i_12_] = (short) in.readUnsignedShort();
				modifiedModelColors[i_12_] = (short) in.readUnsignedShort();
			}
		} else if (opcode == 41) {
			int i_13_ = in.readUnsignedByte();
			aShortArray1596 = new short[i_13_];
			aShortArray1563 = new short[i_13_];
			for (int i_14_ = 0; i_14_ < i_13_; i_14_++) {
				aShortArray1596[i_14_] = (short) in.readUnsignedShort();
				aShortArray1563[i_14_] = (short) in.readUnsignedShort();
			}
		} else if (opcode == 60) // this was removed
			mapMarkerId = in.readUnsignedShort();
		else if (opcode == 61)
			category = in.readUnsignedShort();
		else if (opcode == 62)
			verticalFlip = true;
		else if (opcode == 64)
			aBool1580 = false;
		else if (opcode == 65)
			render0x1 = in.readUnsignedShort();
		else if (opcode == 66)
			render0x2 = in.readUnsignedShort();
		else if (opcode == 67)
			render0x4 = in.readUnsignedShort();
		else if (opcode == 68)
			anInt1578 = in.readUnsignedShort();
		else if (opcode == 69)
			someDirection = in.readUnsignedByte();
		else if (opcode == 70)
			anInt1584 = in.readShort();
		else if (opcode == 71)
			anInt1585 = in.readShort();
		else if (opcode == 72)
			anInt1586 = in.readShort();
		else if (opcode == 73)
			type22Boolean = true;
		else if (opcode == 74)
			someFlag = true;
		else if (opcode == 75)
			unknownOpcode75 = in.readUnsignedByte();
		else if (opcode == 77 || opcode == 92) {
			varpBitId = in.readUnsignedShort();
			if (varpBitId == 65535)
				varpBitId = -1;
			varpId = in.readUnsignedShort();
			if (varpId == 65535)
				varpId = -1;
			int i_17_ = -1;
			if (opcode == 92) {
				i_17_ = in.readUnsignedShort();
				if (i_17_ == 65535)
					i_17_ = -1;
			}
			int i_18_ = in.readUnsignedByte();
			showIds = new int[i_18_ + 2];
			for (int i_19_ = 0; i_19_ <= i_18_; i_19_++) {
				showIds[i_19_] = in.readUnsignedShort();
				if (showIds[i_19_] == 65535)
					showIds[i_19_] = -1;
			}
			showIds[i_18_ + 1] = i_17_;
		} else if (opcode == 78) {
			ambientSoundId = in.readUnsignedShort();
			unknownOpcode_78_79 = in.readUnsignedByte();
			in.readUnsignedByte(); // ambientSoundRetain
		} else if (opcode == 79) {
			anInt1548 = in.readUnsignedShort();
			anInt1571 = in.readUnsignedShort();
			unknownOpcode_78_79 = in.readUnsignedByte();
			in.readUnsignedByte(); // ambientSoundRetain
			int soundCount = in.readUnsignedByte();
			soundEffectIds = new int[soundCount];
			for (int i_16_ = 0; i_16_ < soundCount; i_16_++)
				soundEffectIds[i_16_] = in.readUnsignedShort();
		} else if (opcode == 81)
			anInt1569 = in.readUnsignedByte() * 256;
		else if (opcode == 82) {
			mapAreaId = in.readUnsignedShort();
		} else if (opcode == 89) {
			randomizeAnimStart = true;
		} else if (opcode == 90) {
			// no clue, some boolean = true
		} else if (opcode == 249) {
			params = in.readStringIntParameters();
		} else {
			throw new IllegalArgumentException("Invalid opcode " + opcode + " (last=" + lastOpcode + ") " +
					"for object ID " + id + " (name=\"" + name + "\")");
		}
	}

	public boolean isClippedDecoration() {
		return type22Int != 0 || clipType == 1 || type22Boolean;
	}

	public boolean hasOption(String... searchOptions) {
		return getOption(searchOptions) != -1;
	}

	public int getOption(String... searchOptions) {
		if (options != null) {
			for (String s : searchOptions) {
				for (int i = 0; i < options.length; i++) {
					String option = options[i];
					if (s.equalsIgnoreCase(option))
						return i + 1;
				}
			}
		}
		return -1;
	}

	private void copy(int id) {
		if (this.id < id) {
			System.err.println("Unable to copy Object where target has lower id.");
			return;
		}

		LocType from = LOADED.get(id);

		try {
			type22Int = from.type22Int;
			unknownOpcode75 = from.unknownOpcode75;
			name = from.name;
			xLength = from.xLength;
			yLength = from.yLength;
			clipType = from.clipType;
			tall = from.tall;
			aBool1552 = from.aBool1552;
			animationID = from.animationID;
			anInt1595 = from.anInt1595;
			mapMarkerId = from.mapMarkerId;
			aBool1580 = from.aBool1580;
			anInt1578 = from.anInt1578;
			type22Boolean = from.type22Boolean;
			ambientSoundId = from.ambientSoundId;
			unknownOpcode_78_79 = from.unknownOpcode_78_79;
			anInt1548 = from.anInt1548;
			anInt1571 = from.anInt1571;
			soundEffectIds = from.soundEffectIds == null ? null
					: Arrays.copyOf(from.soundEffectIds, from.soundEffectIds.length);
			showIds = from.showIds == null ? null : Arrays.copyOf(from.showIds, from.showIds.length);
			options = from.options == null ? null : Arrays.copyOf(from.options, from.options.length);
			modelTypes = from.modelTypes == null ? null : Arrays.copyOf(from.modelTypes, modelTypes.length);
			modelIds = from.modelIds == null ? null : Arrays.copyOf(from.modelIds, from.modelIds.length);
			anInt1569 = from.anInt1569;
			aBool1582 = from.aBool1582;
			unknownOpcode29 = from.unknownOpcode29;
			anInt1575 = from.anInt1575;
			originalModelColors = from.originalModelColors == null ? null
					: Arrays.copyOf(from.originalModelColors, from.originalModelColors.length);
			modifiedModelColors = from.modifiedModelColors == null ? null
					: Arrays.copyOf(from.modifiedModelColors, from.modifiedModelColors.length);
			aShortArray1596 = from.aShortArray1596 == null ? null
					: Arrays.copyOf(from.aShortArray1596, from.aShortArray1596.length);
			aShortArray1563 = from.aShortArray1563 == null ? null
					: Arrays.copyOf(from.aShortArray1563, from.aShortArray1563.length);
			verticalFlip = from.verticalFlip;
			render0x1 = from.render0x1;
			render0x2 = from.render0x2;
			render0x4 = from.render0x4;
			anInt1584 = from.anInt1584;
			anInt1585 = from.anInt1585;
			anInt1586 = from.anInt1586;
			someFlag = from.someFlag;
			varpBitId = from.varpBitId;
			varpId = from.varpId;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
