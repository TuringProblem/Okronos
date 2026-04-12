package io.ruin.model.combat.special.ranged;

import io.ruin.cache.ObjType;
import io.ruin.model.World;
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

public class ScorchingBow implements Special {
	Projectile projectile = new Projectile(2807, 40, 36, 20, 33, 3, 15, 11);
	@Override
	public boolean accept(ObjType def, String name) {
		return def.id == 29591;
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle style, AttackType type, int maxDamage) {
		Item ammo = player.getEquipment().get(Equipment.SLOT_AMMO);
		if (ammo == null || ammo.getAmount() < 1) {
			player.sendMessage("You need at least two arrows in your quiver to use this special attack.");
			return false;
		}
		if(target.isPlayer()) {
			player.sendMessage("This can't be used on players!");
			return false;
		}
		if(target.isNpc() && !target.npc.getDef().demon) {
			player.sendMessage("Scorching shackles won't work against a non-demon enemy.");
			return false;
		}
		World.startEvent(e -> {
			player.animate(426);
			int delay = projectile.send(player, target);
			e.delay(World.getTicks(delay) + 1);
			target.graphics(2808);
			target.burnEvent(target, 5, 10, 12, 4);
			target.freeze(12, target);
			Hit hit = new Hit(player, style, type).randDamage(maxDamage).boostAttack(0.3);
			target.hit(hit);
		});
		return true;
	}

	@Override
	public int getDrainAmount() {
		return 25;
	}

}
