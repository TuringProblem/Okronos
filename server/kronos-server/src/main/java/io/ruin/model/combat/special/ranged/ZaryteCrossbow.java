package io.ruin.model.combat.special.ranged;

import io.ruin.cache.ObjType;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.AccurateBlows;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Projectile;

import static io.ruin.model.entity.player.PlayerCombat.getBowHitDelay;
import static io.ruin.model.entity.player.PlayerCombat.getChebyshevDistance;

/**
 * @author R-Y-M-R
 * @date 3/7/2022
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 */
public class ZaryteCrossbow implements Special {

	private static final Projectile PROJECTILE = new Projectile(1995, 38, 36, 41, 51, 5, 5, 11);

	// The Zaryte crossbow has a special attack, Evoke, which consumes 75% of the player's special attack energy.
	// The special attack provides doubled accuracy on the next shot,
	// and guarantees the special effect of any enchanted bolts used, providing that the player lands a successful hit on the target.

	@Override
	public boolean accept(ObjType def, String name) {
		return def.id == 26374;
	}

	@Override
	public boolean handle(Player player, Entity victim, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.set(io.ruin.model.item.actions.impl.ZaryteCrossbow.SPEC_ATTRIBUTE, true);

		player.animate(9168);
		PROJECTILE.send(player, victim);
		final int distance = getChebyshevDistance(player, victim);
		final int delayTicks = getBowHitDelay(distance);

		try {
			ObjType rangedAmmo = getAmmo(player);
			double boostAttack = 1;
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
				if (victim.isNpc()) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
					AccurateBlows c = (AccurateBlows) player.getPlayerPerkHandler().
						getActivePerks(player).get(perkIndex).getPerk(player);
					boostAttack += c.getAccuracyBoost();
				}
			}
			Hit hit = new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostAttack(boostAttack).ignoreDefence().delay(delayTicks).setRangedAmmo(rangedAmmo);
			if (rangedAmmo.rangedAmmo != null) {
				if (rangedAmmo.rangedAmmo.effect != null) {
					rangedAmmo.rangedAmmo.effect.apply(victim, hit);
				}
			}
			victim.hit(hit);
		} catch (NullPointerException n) { // Should never happen. But handle it anyways.
			Hit hit = new Hit(player, attackStyle, attackType).randDamage(maxDamage).boostAttack(1.0).ignoreDefence().delay(delayTicks);
			victim.hit(hit);
			player.sendMessage("Zaryte crossbow couldn't proc your bolts. Submit a bug report with your bolts!");
			System.out.println("Handled Zaryte Crossbow NPE.");
			n.printStackTrace();
		}
		player.set(io.ruin.model.item.actions.impl.ZaryteCrossbow.SPEC_ATTRIBUTE, false);
		return true;
	}

	public static ObjType getAmmo(Player player) {
		Item ammo = player.getEquipment().get(Equipment.SLOT_AMMO);
		if (ammo != null) {
			return ObjType.get(ammo.getId());
		}
		return null;
	}

	@Override
	public int getDrainAmount() {
		return 75;
	}

}