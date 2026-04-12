package com.reasonps.dominion.bosses.cerberus;

import com.reasonps.dominion.bosses.Attack;
import com.reasonps.dominion.bosses.cerberus.attacks.*;
import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.utility.TickDelay;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-25
 */
public class EchoCerberus extends NPCCombat {

	public final TickDelay summonSoulsCooldown = new TickDelay();

	@Getter
	private final List<Position> lavaPoolPositions = new ArrayList<>();

	private Attack currentAttack = null;
	private int attackCounter;

	public boolean damagedByGhosts = false;
	public boolean damagedByLava = false;
	public int ghostsBlocked = 0;
	public boolean ghostsSpawned = false;

	@Override
	public void init() {
		reload();
		npc.hitsUpdate.hpbarId = 1;
		npc.hitListener = new HitListener()
			.preDamage(hit -> {
				if (hit.damage >= 120)
					hit.damage = 120;
			})
			.postDamage(hit -> {
				if (hit.attacker != null && hit.attacker.isPlayer()) {
					if (!hit.attacker.player.getHealthHud().isOpened())
						hit.attacker.player.getHealthHud().open(true, npc.getId(), npc.getMaxHp());
					hit.attacker.player.getHealthHud().updateValue(npc.getHp());
				}
			});
	}

	private void reload() {
		lavaPoolPositions.clear();
		attackCounter = -1;
		currentAttack = Random.get(List.of(
			new MagicAttack(),
			new MeleeAttack(),
			new RangedAttack()
		));
	}

	@Override
	public boolean allowRespawn() {
		return false;
	}

	@Override
	public int getAttackBoundsRange() {
		return 20;
	}

	@Override
	public void follow() {
		follow(3);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(12)) return false;

		// Increment the attackCounter
		attackCounter++;

		// After each eighth attack or when damaged below half-health, she will summon ghosts
		if (attackCounter % 8 == 0 || (npc.getHp() < (npc.getMaxHp() / 2) && summonSoulsCooldown.finished()))
			new SoulSummonAttack().invoke(target.player, this);

		// Every fourth and eighth attack she will instead summon two lava pools
		if (attackCounter == 6 || attackCounter == 12) {
			new LavaAttack().invoke(target.player, this);
			return true;
		}

		// Her regular attack uses a single style for eight attacks
		if (attackCounter < 8) {
			currentAttack.invoke(target.player, this);
			return true;
		}

		// They start with magic...
		if (attackCounter == 9) {
			currentAttack = new MagicAttack();
			currentAttack.invoke(target.player, this);
			return true;
		}
		// ...circling through magic, ranged, and mêlée in that order.
		if (currentAttack instanceof MagicAttack)
			currentAttack = new RangedAttack();
		else if (currentAttack instanceof RangedAttack)
			currentAttack = new MeleeAttack();
		else if (currentAttack instanceof MeleeAttack)
			currentAttack = new MagicAttack();

		currentAttack.invoke(target.player, this);
		return true;
	}

	@Override
	public void process() {
		if (!isDead() && target != null && !lavaPoolPositions.isEmpty()) {
			lavaPoolPositions.forEach(position -> {
				// Flash the spotAnim
				World.sendGraphics(LavaAttack.LAVA_GFX, 0, 0, position);
				// check if we're damaging the player
				if (target.getPosition().isAtPosition(position)) {
					damagedByLava = true;
					target.hit(new Hit().randDamage(15, 18));
				}
			});
		}
	}
}