package io.ruin.model.activities.miscpvm.slayer;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.stat.StatType;

public class BasiliskSentinel extends NPCCombat {
	private static final StatType[] DRAIN = {StatType.Attack, StatType.Strength, StatType.Defence, StatType.Ranged, StatType.Magic};


	private static final int STONE_HIT_GFX = 0;//TODO: Find correct GFX
	private static final int gfx_MageId = 1735;

	private static final int gfx_StoneId = 1737;

	public static final Projectile stoneProjectile = new Projectile(gfx_StoneId, 33, 25, 30, 30, 10, 15, 64);
	public static final Projectile mageProjectile = new Projectile(gfx_MageId, 33, 25, 30, 30, 10, 15, 64);
	private final int anim_MageID = 8500;


	@Override
	public void init() {

	}

	@Override
	public void follow() {
		follow(1);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(8))
			return false;
		if (Random.rollDie(3, 1))
			mageAttack();
		if (Random.rollDie(4, 1))
			stoneAttack();
		else if (withinDistance(1))
			basicAttack();
		if (target.player != null && target.player.getEquipment().getId(Equipment.SLOT_SHIELD) != 4156) {
			for (StatType statType : DRAIN) {
				target.player.getStats().get(statType).drain(4);
			}
			target.hit(new Hit(npc, null).randDamage(2, 5).ignoreDefence().ignorePrayer());
			target.player.sendMessage("<col=ff0000>The basilisk's piercing gaze drains your stats!");
			target.player.sendMessage("<col=ff0000>A mirror shield can protect you from this attack.");
		}
		return true;

	}

	@Override
	public void process() {

	}

	private void mageAttack() {
		npc.animate(anim_MageID);
		int delay = mageProjectile.send(npc, target);
		target.hit(new Hit(npc, AttackStyle.MAGIC, null).randDamage(info.max_damage).clientDelay(delay));
	}

	private void stoneAttack() {
		npc.animate(anim_MageID);
		Position pos = target.getPosition().copy();
		int Delay = stoneProjectile.send(npc.getAbsX(), npc.getAbsY(), pos.getX(), pos.getY());
		int tickDelay = (((25 * Delay)) / 600) - 2;
		npc.addEvent(event -> {
			event.delay(tickDelay);
			if (target == null)
				return;
			if (target.getPosition().equals(pos)) {
				World.sendGraphics(STONE_HIT_GFX, 20, 30, pos.getX(), pos.getY(), pos.getZ());
				target.hit(new Hit(npc, AttackStyle.MAGIC).clientDelay(70, 0).randDamage(74).ignoreDefence());
				target.freeze(3, npc);
				target.player.sendMessage("<col=ff0000>You have been trapped in stone!");
			}
		});
	}
}
