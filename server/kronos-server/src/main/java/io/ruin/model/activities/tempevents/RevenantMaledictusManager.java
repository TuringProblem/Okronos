package io.ruin.model.activities.tempevents;


import io.ruin.api.utils.Random;
import io.ruin.cache.Icon;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.Killer;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.jewellery.AmuletOfAvarice;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.utility.Broadcast;
import io.ruin.utility.TickDelay;

public class RevenantMaledictusManager extends TemporaryEvent {
	enum SpawnPositions {
		REV_DEMON_CHAMBER(new Position(3178, 10189, 0), "North"),
		REV_DRAGON_CHAMBER(new Position(3240, 10203, 0), "North"),
		NORTH_REV_IMP(new Position(3215, 10194, 0), "North"),
		REV_HELLHOUND(new Position(3244, 10169, 0), "Middle"),
		EAST_REV_PYREFIEND(new Position(3251, 10145, 0), "Middle"),
		WEST_REV_PYREFIEND(new Position(3175, 10154, 0), "Middle"),
		REV_DARK_BEAST_CHAMBER(new Position(3207, 10163, 0), "Middle"),
		NORTH_REV_ORK(new Position(3223, 10136, 0), "Middle"),
		SOUTH_REV_ORK(new Position(3214, 10098, 0), "South"),
		SOUTH_REV_DEMON(new Position(3159, 10114, 0), "South"),
		OUTSIDE_SOUTH_REV_DEMON(new Position(3187, 10119, 0), "South");

		Position position;
		String location;

		SpawnPositions(Position position, String location) {
			this.position = position;
			this.location = location;
		}
	}

	boolean alive = false;
	TickDelay aliveTimer = new TickDelay();
	final int MALEDICTUS = 11246;
	int totalCombatOfSlainRevs = 0;
	int spawnRateDivision = 3;

	NPC maledictus;

	private static final Bounds REV_CAVE = new Bounds(3138, 10050, 3261, 10237, -1);

	public void init() {
		World.revenantMaledictusEvent = this;
		totalCombatOfSlainRevs = 0;
		eventDuration = 12000;
		start();
	}


	private SpawnPositions getSpawnPosition() {
		return Random.get(SpawnPositions.values());
	}


	private void spawnBoss() {
		if (alive) return;
		alive = true;
		aliveTimer.delaySeconds(600);
		SpawnPositions spawnPositions = getSpawnPosition();
		totalCombatOfSlainRevs = 0;
		Position spawnPos = spawnPositions.position;
		maledictus = new NPC(MALEDICTUS).spawn(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(),
			Direction.SOUTH, 0);
		maledictus.deathEndListener = this::handleDeath;

		Broadcast.WORLD.sendNews(Icon.WILDERNESS, "</col>[<shad=8A0011>Wilderness</shad>] <shad=9B9B9B>The Revenant Maledictus has spawned towards the " +
			spawnPositions.location + " of the revenant cave!");

	}

	private void handleDeath(Entity entity, Killer killer, Hit hit) {
		alive = false;
		maledictus.remove();
		if (AmuletOfAvarice.isWearingAmuletOfAvarice(killer.player)) {
			killer.player.getCombat().forinthySkullDelay = 3000;
			killer.player.getAppearance().setSkullIcon(3);
		}
	}

	@Override
	protected void endEvent() {
		spawnBoss();
	}

	@Override
	protected void process() {
		if (aliveTimer.remaining() < 1)
			despawnBoss();
		if (alive && maledictus != null) {
			for (Player player :
				REV_CAVE.getRegion().players) {
				player.getPacketSender().sendHintIcon(maledictus.getAbsX(), maledictus.getAbsY());
			}
		}
	}

	@Override
	public void start() {
		World.startEvent(e -> {
			while (eventDuration-- > 0) {
				e.delay(1);
				process();
				if (eventDuration <= 0)
					endEvent();
			}
		});
	}

	private void despawnBoss() {
		if (alive && !maledictus.getCombat().isDefending(17)) {
			alive = false;
			maledictus.remove();
			Broadcast.WORLD.sendNews(Icon.WILDERNESS, "</col>[<shad=8A0011>Wilderness</shad>] <shad=9B9B9B>The Revenant Maledictus has grown bored of waiting around and has left the area.");
		}
	}


	public void rollSpawnChance(NPC npc) {
		if (alive || npc.getId() == MALEDICTUS) return;
		totalCombatOfSlainRevs += npc.getDef().combatLevel;
		if (Random.get(150) == 0) {
			spawnBoss();
		}
	}
}


