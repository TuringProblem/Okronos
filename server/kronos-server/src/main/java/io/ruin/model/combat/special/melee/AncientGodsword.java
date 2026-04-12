package io.ruin.model.combat.special.melee;

import io.ruin.cache.ObjType;
//import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.AccurateBlows;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.utility.Misc;

/**
 * @author R-Y-M-R
 * @date 3/8/2022
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 */
public class AncientGodsword implements Special {
	public static final int id = 26233;
	public static final int GFX_ID = 1996; // blue claws

	@Override
	public boolean accept(ObjType def, String name) {
		return def.id == id;
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(9171);
		player.graphics(GFX_ID);
		player.publicSound(3869);
		double boostAttack = 1;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				boostAttack += c.getAccuracyBoost();
			}
		}
		var damageDealt = target.hit(new Hit(player, attackStyle, attackType)
			.randDamage(maxDamage)
			.boostDamage(0.1)
			.boostAttack(boostAttack)
		);
		if (damageDealt <= 0)
			return true; // we missed
		World.startEvent(event -> {
			if (target.isPlayer()) {
				target.recolour(0, 200, 0, 6, 28, 112);
			}
			event.delay(6);
			if (Misc.getDistance(player.getPosition().copy(), target.getPosition().copy()) < 5) {
				target.graphics(2001);
				Hit hit = new Hit(player, AttackStyle.MAGICAL_MELEE, attackType).fixedDamage(25).ignorePrayer().ignoreDefence();
				target.hit(hit);
				Hit healhit = new Hit(HitType.HEAL).fixedDamage(25).hide();
				player.hit(healhit);
			}
		}).setCancelCondition(() -> {
			if (target == null) {
				return true;
			}
			if (target.isPlayer()) {
				if (!target.player.isOnline()) {
					return true;
				}
			}
			if (!target.alive()) { //if the target is dead
				return true;
			}
			if (player == null) { // if player is null
				return true;
			}
			if (!player.alive()) { // if target is not alive
				return true;
			}
			if (!player.isOnline()) { // if target is offline
				return true;
			}
			return false;
		});
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 50;
	}

}