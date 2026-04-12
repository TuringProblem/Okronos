package io.ruin.model.activities.miscpvm.slayer;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;

public class MarbleGargoyle extends NPCCombat {

	//TODO i cant figure out how to make the mageprojectile come from the middle of the npc instead of the side. but this is done - coins

	private static final int MAGE_HIT_GFX = 363;
	private static final int gfx_RangeId = 1313;
	private static final int gfx_MageId = 1453;
	public static final Projectile mageProjectile = new Projectile(gfx_MageId, 60, 31, 30, 80, 10, 0, 0);
	public static final Projectile rangeProjectile = new Projectile(gfx_RangeId, 20, 31, 30, 30, 10, 0, 32);


	@Override
	public void init() {
	}

	@Override
	public void follow() {
		follow(5);
	}

	@Override
	public boolean attack() {
		if (target.getPosition().isWithinDistance(npc.getPosition(), 1)) {
			basicAttack();
			return true;
		} else {
			if (npc.getPosition().isWithinDistance(target.getPosition()) && Random.rollDie(4)) {
				mageAttack();
				return true;
			}
			if (npc.getPosition().isWithinDistance(target.getPosition())) {
				rangeAttack();
				return true;
			}
		}
		return false;
	}

	@Override
	public void process() {

	}

	public Hit basicAttack() {
		return basicAttack(npc.getDef().animations.attack_animation, info.attack_style, info.max_damage);
	}

	private void mageAttack() {
		npc.animate(7815);
		Position pos = target.getPosition().copy();
		int Delay = mageProjectile.send(npc.getAbsX(), npc.getAbsY(), pos.getX(), pos.getY());
		int tickDelay = (((25 * Delay)) / 600) - 2;
		npc.addEvent(event -> {
			event.delay(tickDelay);
			if (target == null)
				return;
			if (target.getPosition().equals(pos)) {
				World.sendGraphics(MAGE_HIT_GFX, 20, 30, pos.getX(), pos.getY(), pos.getZ());
				target.hit(new Hit(npc, AttackStyle.MAGIC).clientDelay(70, 0).randDamage(38).ignoreDefence());
				target.freeze(3, npc);
				target.player.sendMessage("<col=ff0000>You have been trapped in stone!");
			}
		});
	}

	private void rangeAttack() {
		npc.animate(7811);
		int delay = rangeProjectile.send(npc, target);
		target.hit(new Hit(npc, AttackStyle.RANGED, null).randDamage(info.max_damage).ignoreDefence().clientDelay(delay));
	}
}
