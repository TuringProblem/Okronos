package io.ruin.model.activities.miscpvm.slayer;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Projectile;
import io.ruin.model.stat.StatType;

import java.util.Arrays;
import java.util.List;

/**
 * @author Shinatobe - 25/03/2021
 * www.rune-server.ee/members/Shinatobe/
 * @project Gauntlet
 */

public class AncientWyvern extends NPCCombat {

	private static final Projectile PROJECTILE = new Projectile(500, 80, 31, 50, 56, 10, 16, 127);
	private static final List<Integer> SHIELDS = Arrays.asList(21633, 22002, 22003, 21634, 9731, 2890, 11283, 11284);

	private static final int WYVERN_SHIELD = 21633;
	private static final int WYVERN_SHIELD_UNCHARGED = 21634;

	@Override
	public void init() {

	}

	@Override
	public void follow() {
		follow(8);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(8))
			return false;
		if (withinDistance(1) && Random.rollDie(3, 2))
			meleeAttack();
		else if (Random.rollDie(5, 1))
			iceBreath();
		else if (withinDistance(1))
			meleeAttack();
		else
			rangedAttack();
		return true;
	}

	@Override
	public void process() {

	}

	private void meleeAttack() {
		if (withinDistance(1) && Random.rollDie(3, 1))
			basicAttack();
		else if (withinDistance(1) && Random.rollDie(3, 1))
			crush();
		else if (withinDistance(1))
			slash();
	}

	private void crush() {
		npc.animate(7658);
		target.hit(new Hit(npc, AttackStyle.CRUSH).randDamage(info.max_damage));
	}

	private void slash() {
		npc.animate(7654);
		target.hit(new Hit(npc, AttackStyle.SLASH).randDamage(info.max_damage));
	}

	private void rangedAttack() {
		npc.animate(7657);
		int delay = PROJECTILE.send(npc, target);
		int maxDamage = 10;
		Hit hit = new Hit(npc, null).randDamage(maxDamage).clientDelay(delay).ignoreDefence().ignorePrayer();
		target.hit(hit);
	}

	private void iceBreath() {
		npc.animate(7653);
		int maxDamage = 60;
		npc.graphics(1392, 220, 0);

		int shieldId = target.player.getEquipment().getId(Equipment.SLOT_SHIELD);

		if (target.player != null && SHIELDS.contains(shieldId)) {
			target.player.sendMessage("Your shield provides protection from the icy breath!");
			maxDamage = 10;
		} else if (target.player != null) {
			target.player.sendMessage("You have been hit by the icy breath!");
		}

		// Having a wyvern shield (charged or uncharged) protects you from their freezing.
		if (shieldId != WYVERN_SHIELD && shieldId != WYVERN_SHIELD_UNCHARGED) {
			if (Random.rollDie(3, 1))
				target.freeze(3, npc);
			target.graphics(502);
			target.player.sendMessage("You have been frozen!");
		}

		target.hit(new Hit(npc, null).randDamage(maxDamage).ignoreDefence().ignorePrayer());
	}
}
