package io.ruin.model.combat.special.ranged;

import io.ruin.cache.ObjType;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Projectile;
import io.ruin.model.stat.StatType;

import static io.ruin.model.entity.player.PlayerCombat.getBowHitDelay;
import static io.ruin.model.entity.player.PlayerCombat.getChebyshevDistance;


public class WebweaverBow implements Special {

	private static final Projectile PROJECTILE = new Projectile(2206, 38, 36, 41, 51, 5, 5, 11);

	@Override
	public boolean accept(ObjType def, String name) {
		return def.id == 8874;
	}

	@Override
	public boolean handle(Player player, Entity victim, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(427);
		PROJECTILE.send(player, victim);
		final int distance = getChebyshevDistance(player, victim);
		final int delayTicks = getBowHitDelay(distance);
		int damage = victim.hit(new Hit(player, attackStyle, attackType).randDamage((int) (victim instanceof NPC ? maxDamage * 1.7 : maxDamage * 1.3)).delay(delayTicks));
		player.startEvent(e -> {
			e.delay(delayTicks);
			if (damage > 0) {
				player.getStats().get(StatType.Prayer).restore(damage / 3);
				if (player.getHp() > 0)
					player.hit(new Hit(HitType.HEAL).fixedDamage(damage / 3));
			}
		});
		return true;
	}
	//2227 mage spec maybe? 2226 player gfx for mage spec 2024-2025?
	//2206 white arrow? 2049-2052 2055 bow
	//2080 lightning

	public static ObjType getAmmo(Player player) {
		Item ammo = player.getEquipment().get(Equipment.SLOT_AMMO);
		if (ammo != null) {
			return ObjType.get(ammo.getId());
		}
		return null;
	}

	@Override
	public int getDrainAmount() {
		return 50;
	}

}