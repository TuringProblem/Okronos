package io.ruin.model.activities.raids.xeric.chamber;

import io.ruin.model.activities.raids.xeric.chamber.impl.BlankHandler;
import io.ruin.model.activities.raids.xeric.chamber.impl.CheckpointChamber;
import io.ruin.model.activities.raids.xeric.chamber.impl.GuardiansChamber;
import io.ruin.model.activities.raids.xeric.chamber.impl.HunterResourceChamber;
import io.ruin.model.activities.raids.xeric.chamber.impl.LizardmanShamanChamber;
import io.ruin.model.activities.raids.xeric.chamber.impl.MuttadilesChamber;
import io.ruin.model.activities.raids.xeric.chamber.impl.OlmChamber;
import io.ruin.model.activities.raids.xeric.chamber.impl.ScavengerChamber;
import io.ruin.model.activities.raids.xeric.chamber.impl.ScavengerRuinsChamber;
import io.ruin.model.activities.raids.xeric.chamber.impl.SkeletalMysticsChamber;
import io.ruin.model.activities.raids.xeric.chamber.impl.TektonChamber;
import io.ruin.model.activities.raids.xeric.chamber.impl.TightRopeChamber;
import io.ruin.model.activities.raids.xeric.chamber.impl.VanguardsChamber;
import io.ruin.model.activities.raids.xeric.chamber.impl.VasaNistiroChamber;
import io.ruin.model.map.Chunk;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Edu
 */
public enum ChamberDefinition {
	START("Starting", ChamberType.BEGIN, CheckpointChamber.class, new Chunk(408, 648, 0)),
	SCAVENGERS("Scavengers", ChamberType.SCAVENGER, ScavengerChamber.class, new Chunk(408, 652, 0)),
	LIZARDMAN_SHAMANS("Lizardman Shamans", ChamberType.COMBAT, LizardmanShamanChamber.class, new Chunk(408, 656, 0)),
	VASA_NISTIRIO("Vasa Nistirio", ChamberType.COMBAT, VasaNistiroChamber.class, new Chunk(408, 660, 0)),
	VANGUARDS("Vanguards", ChamberType.COMBAT, VanguardsChamber.class, new Chunk(408, 664, 0)),
	//ICE_DEMON("Ice Demon", ChamberType.PUZZLE, IceDemonChamber.class, new Chunk(408, 668, 0)),
	CORRUPT_SCAVENGER("Tightrope", ChamberType.PUZZLE, TightRopeChamber.class, new Chunk(408, 668, 1)),
	SCAVENGER_RUINS("Scavenger Ruins", ChamberType.SCAVENGER, ScavengerRuinsChamber.class, new Chunk(408, 652, 1)),
	SKELETAL_MYSTICS("Skeletal Mystics", ChamberType.COMBAT, SkeletalMysticsChamber.class, new Chunk(408, 656, 1)),
	TEKTON("Tekton", ChamberType.COMBAT, TektonChamber.class, new Chunk(408, 660, 1)),
	MUTTADILES("Muttadiles", ChamberType.COMBAT, MuttadilesChamber.class, new Chunk(408, 664, 1)),
	TIGHTROPE("Tightrope", ChamberType.PUZZLE, TightRopeChamber.class, new Chunk(408, 668, 1)),
	GUARDIANS("Guardians", ChamberType.COMBAT, GuardiansChamber.class, new Chunk(408, 656, 2)),
	//VESPULA("Vespula", ChamberType.COMBAT, VespulaChamber.class, new Chunk(408, 660, 2)),
	//JEWELLED_CRABS("Jewelled crabs", ChamberType.PUZZLE, JewelledCrabsChamber.class, new Chunk(408, 668, 2)),

	FISHING_RESOURCE("Resources (fishing)", ChamberType.RESOURCE, BlankHandler.class, new Chunk(408, 680, 0)),
	HUNTER_RESOURCE("Resources (hunter)", ChamberType.RESOURCE, HunterResourceChamber.class, new Chunk(408, 680, 1)),

	LOWER_LEVEL_START("Lower level start", ChamberType.BEGIN, CheckpointChamber.class, new Chunk(408, 712, 0)),

	UPPER_FLOOR_FINISH("Upper level finish", ChamberType.FINISH, CheckpointChamber.class, new Chunk[]{new Chunk(408, 644, 0)}),
	LOWER_FLOOR_FINISH("Lower level finish", ChamberType.FINISH, CheckpointChamber.class, new Chunk[]{new Chunk(412, 644, 0)}),

	GREAT_OLM("Great Olm", null, OlmChamber.class, new Chunk[]{new Chunk(400, 712, 0)}),

	;

	public String getName() {
		return name;
	}

	public ChamberType getType() {
		return type;
	}

	public Chamber newChamber() {
		try {
			Chamber chamber = handler.getDeclaredConstructor().newInstance();
			chamber.setDefinition(this);
			return chamber;
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			System.out.println("Raids 1: newChamber(): " + handler.getName() + e);
		}
		return null;
	}

	private String name;
	private ChamberType type;
	private Class<? extends Chamber> handler;

	/**
	 * all chambers are 4x4 chunks, so just use the "base" chunk, the most south western one.`
	 * this should either be size 1 or 3.
	 * size 1 for "dead end" rooms like the floor-ending room, and 3 for other rooms following the same logic as they are in the map:
	 * [0] -> exit to the west of the entrance
	 * [1] -> exit on the opposite of the entrance
	 * [2] -> exit to the east of the entrance
	 * (west/east directions assume the room hasn't been rotated, as is their default state in the map)
	 */
	private Chunk[] baseChunks;

	ChamberDefinition(String name, ChamberType type, Class<? extends Chamber> handler, Chunk[] baseChunks) {
		this.name = name;
		this.type = type;
		this.baseChunks = baseChunks;
		this.handler = handler;
	}

	ChamberDefinition(String name, ChamberType type, Class<? extends Chamber> handler, Chunk baseChunk) {
		this.name = name;
		this.type = type;
		this.handler = handler;
		this.baseChunks = new Chunk[3];
		baseChunks[0] = baseChunk;
		baseChunks[1] = new Chunk(baseChunk.x() + 4, baseChunk.y(), baseChunk.z());
		baseChunks[2] = new Chunk(baseChunk.x() + 8, baseChunk.y(), baseChunk.z());
	}

	public Chunk getBaseChunk(int layout) {
		if (layout < 0 || layout >= baseChunks.length)
			throw new IllegalArgumentException();
		return baseChunks[layout];
	}
}
