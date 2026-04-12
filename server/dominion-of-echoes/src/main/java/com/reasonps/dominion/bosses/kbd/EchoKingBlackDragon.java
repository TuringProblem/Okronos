package com.reasonps.dominion.bosses.kbd;

import com.reasonps.dominion.bosses.Attack;
import com.reasonps.dominion.bosses.kbd.attacks.*;
import core.task.Continuation;
import io.ruin.api.utils.Random;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class EchoKingBlackDragon extends NPCCombat implements Continuation.Void {

	private boolean hasSpawnedEchBlackDragons = false;
	private final List<NPC> echoBlackDragons = new ArrayList<>(4);
	private final Position[] blackDragonSpawnPositions = new Position[4];
	@Getter
	private final List<Position> venomPools = new ArrayList<>();
	private final List<GameObject> acidPoolsObjects = new ArrayList<>();

	private final List<Attack> availableAttacks = new ArrayList<>(List.of(
		new CorruptingDragonfireAttack(),
		new DragonfireAttack(),
		new IcyBreathAttack(),
		new ShockBreathAttack(),
		new ToxicBreathAttack()
	));

	@Override
	public boolean allowRespawn() {
		return false;
	}

	@Override
	public void init() {
		getNpc().hitsUpdate.hpbarId = 1;
		getNpc().hitListener = new HitListener()
			.preDamage(hit -> {
				// Check if we're spawning minion dragons
				if (isEnraged() && !hasSpawnedEchBlackDragons) {
					npc.forceText("SKREEEEEAAAR!");
					target.player.sendMessage("The dragon has summoned shadows from the echoes of another realm.");
					IntStream.range(0, 4).forEach(i -> {
						// create a new dragon
						var echoBlackDragon = new NPC(17005);
						// add to the list and spawn it
						echoBlackDragons.add(echoBlackDragon.spawn(blackDragonSpawnPositions[i]));
						echoBlackDragon.getCombat().setTarget(target);
						// add a death listener to remove it from the list
						echoBlackDragon.deathEndListener = (e, k, h) ->
							echoBlackDragons.remove(echoBlackDragon);
					});
					hasSpawnedEchBlackDragons = true;
				}
				if (isImmuneToDamage())
					hit.block();
			}).postDamage(hit -> {
				if (hit.attacker != null && hit.attacker.isPlayer()) {
					if (!hit.attacker.player.getHealthHud().isOpened())
						hit.attacker.player.getHealthHud().open(true, npc.getId(), npc.getMaxHp());
					hit.attacker.player.getHealthHud().updateValue(npc.getHp());
				}
			});

		getNpc().deathStartListener = (e, k, h) -> {
			if (!echoBlackDragons.isEmpty()) {
				echoBlackDragons.forEach(NPC::remove);
				echoBlackDragons.clear();
			}
			venomPools.clear();
			acidPoolsObjects.forEach(GameObject::remove);
			acidPoolsObjects.clear();
		};

		var base = getNpc().getPosition().getRegion();
		blackDragonSpawnPositions[0] = Position.of(base.baseX + 18, base.baseY + 11);
		blackDragonSpawnPositions[1] = Position.of(base.baseX + 44, base.baseY + 11);
		blackDragonSpawnPositions[2] = Position.of(base.baseX + 18, base.baseY + 36);
		blackDragonSpawnPositions[3] = Position.of(base.baseX + 44, base.baseY + 36);
	}
	public int getAggressionRange() {
		return 32;
	}

	@Override
	public int getAttackBoundsRange() {
		return 32;
	}

	@Override
	public void follow() {
		follow(8);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(8)) return false;
		// Shuffle the attacks
		Collections.shuffle(availableAttacks);
		// pick a random one
		var selectedAttack = Random.get(availableAttacks);
		// assertion that we didn't pick a null attack
		assert selectedAttack != null: "Echo KBD selected attack is null";
		// invoke an attack
		selectedAttack.invoke(target.player, this);
		return true;
	}

	@Override
	public void process() {
		if (npc.isHidden() || getNpc().getPosition().getRegion().players.isEmpty())
			return;

		for (var pos : venomPools) {
			if (getVenomPool(pos) == null)
				acidPoolsObjects.add(new GameObject(54148, pos, 10, 0).spawn());
		}
		var players = getNpc().getPosition().getRegion().players;
		players.forEach(p -> {
			// are we standing on a venom pool?
			if (getVenomPool(p.getPosition()) != null)
				p.hit(new Hit(HitType.VENOM).randDamage(9, 12));
		});
	}

	private GameObject getVenomPool(Position position) {
		return Tile.getObject(54148, position.getX(), position.getY(), 0);
	}

	private boolean isImmuneToDamage() {
		return !echoBlackDragons.isEmpty();
	}

	private boolean isEnraged() {
		return getNpc().getHp() <= (getNpc().getMaxHp() / 2);
	}

	@Override
	public void call() {
		// TODO: Talk with Polish
	}
}
