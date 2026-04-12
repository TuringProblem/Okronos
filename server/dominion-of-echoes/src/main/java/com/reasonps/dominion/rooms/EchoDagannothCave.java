package com.reasonps.dominion.rooms;

import com.reasonps.dominion.bosses.dagannoth_kings.kings.EchoDagannothPrimeCombat;
import com.reasonps.dominion.bosses.dagannoth_kings.kings.EchoDagannothRexCombat;
import com.reasonps.dominion.bosses.dagannoth_kings.kings.EchoDagannothSupremeCombat;
import io.ruin.model.World;
import io.ruin.model.activities.bosses.instancetoken.PolygonRegionArea;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.polly.RSPolygon;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static core.combat.api.ReasonUtils.pos;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class EchoDagannothCave extends PolygonRegionArea {

	// Create the bosses
	@Getter private final NPC echoPrime = new NPC(17009);
	@Getter private final NPC echoSupreme = new NPC(17008);
	@Getter private final NPC echoRex = new NPC(17010);

	@Getter
	protected int healthPool = 8_000;
	protected int damageTaken = 0;

	private boolean mapDestroyed = false;

	@Getter
	protected List<NPC> spinolyps = new ArrayList<>();
	@Getter
	protected List<Position> poisonPools = new ArrayList<>();

	public void deductHealth(int amount) {
		healthPool -= amount;
		damageTaken += amount;
	}

	public EchoDagannothCave(Player player) {
		try {
			setMap(new DynamicMap().build(11589, 1));
			setHost(player);
			init();
		}
		catch (DynamicMap.DynamicMapBuildException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] {
			new RSPolygon(new int[][] {
				{ map.convertX(2901), map.convertY(4442) },
				{ map.convertX(2901), map.convertY(4454) },
				{ map.convertX(2904), map.convertY(4460) },
				{ map.convertX(2912), map.convertY(4463) },
				{ map.convertX(2921), map.convertY(4461) },
				{ map.convertX(2926), map.convertY(4456) },
				{ map.convertX(2926), map.convertY(4453) },
				{ map.convertX(2928), map.convertY(4450) },
				{ map.convertX(2928), map.convertY(4445) },
				{ map.convertX(2926), map.convertY(4440) },
				{ map.convertX(2918), map.convertY(4436) },
				{ map.convertX(2910), map.convertY(4436) },
				{ map.convertX(2906), map.convertY(4437) },
				{ map.convertX(2903), map.convertY(4438) }
			})
		};
	}

	@Override
	public void init() {
		new GameObject(-1, pos(map, 19, 33), 10, 0).spawn();
		// Add the bosses to the map
		npcs.add(echoPrime);
		npcs.add(echoSupreme);
		npcs.add(echoRex);

		// Spawn the bosses
		echoPrime.spawn(pos(map, 37, 28));
		echoSupreme.spawn(pos(map, 26, 32));
		echoRex.spawn(pos(map, 30, 37));

		// Lock the bosses
		echoPrime.lock();
		echoSupreme.lock();
		echoRex.lock();

		// Assign the bosses to the instance
		((EchoDagannothPrimeCombat) echoPrime.getCombat()).assignInstance(this);
		((EchoDagannothSupremeCombat) echoSupreme.getCombat()).assignInstance(this);
		((EchoDagannothRexCombat) echoRex.getCombat()).assignInstance(this);

		echoPrime.setHeadIcon(NPC.DefaultHeadIconIndex.MageRangeMelee);
		echoSupreme.setHeadIcon(NPC.DefaultHeadIconIndex.MageRangeMelee);

		poisonPools.clear();
		startPoisonPoolEvent();

		// Spawn Spinolyps
		spinolyps.add(new NPC(14140).spawn(pos(map, 17, 30)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 17, 35)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 18, 39)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 19, 42)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 21, 46)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 24, 47)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 27, 47)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 32, 49)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 36, 47)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 39, 47)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 42, 47)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 46, 44)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 48, 40)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 50, 37)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 50, 34)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 50, 29)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 50, 25)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 44, 20)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 38, 17)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 34, 17)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 30, 17)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 26, 17)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 21, 20)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 19, 22)));
		spinolyps.add(new NPC(14140).spawn(pos(map, 17, 25)));

		npcs.addAll(spinolyps);
	}

	private void startPoisonPoolDamageEvent(Player player) {
		World.startEvent(event -> {
			while (!getEchoRex().getCombat().isDead()) {
				getPoisonPools().forEach(pool -> {
					if (player.getPosition().isAtPosition(pool)) {
						player.hit(new Hit(HitType.VENOM).randDamage(6, 8).ignoreDefence().ignorePrayer());
					}
				});
				event.delay(1);
			}
		}).setCancelCondition(() -> getEchoRex().getCombat().isDead());
	}

	private void startPoisonPoolEvent() {
		World.startEvent(event -> {
			while (!getEchoRex().getCombat().isDead()) {
				getPoisonPools().forEach(pool ->
					World.sendGraphics(1654, 0, 0, pool));
				event.delay(5);
			}
			getPoisonPools().clear();
		}).setCancelCondition(() -> mapDestroyed);
	}

	@Override
	public void movePlayerToInstance(Player player) {
		player.getMovement().teleport(pos(map, 30, 33));
		player.currentDynamicMap = map;
		player.currentMapHandler = this;
		players.add(player);
		startPoisonPoolDamageEvent(player);
		player.getHealthHud().open(true, getEchoPrime().getId(), getHealthPool());
		// kick off a little delay to have them start attacking in order
		World.startEvent(event -> {
			event.delay(1);
			echoPrime.unlock();
			event.delay(1);
			echoSupreme.unlock();
			event.delay(1);
			echoRex.unlock();
		});
	}

	@Override
	public void destroy() {
		super.destroy();
		mapDestroyed = true;
	}

	@Override
	public boolean isCannonRestricted() {
		return true;
	}
}
