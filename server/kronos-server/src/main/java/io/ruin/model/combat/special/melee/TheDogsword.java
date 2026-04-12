package io.ruin.model.combat.special.melee;

import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.AccurateBlows;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.combat.special.Special;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Misc;

public class TheDogsword implements Special {
	private static final int WEAPON_ID = 30367;
	private static final int ANIMATION_ID = 11913;
	private static final int SOUND_EFX = 3869;
	private static final int GFX_ID = 3059;

	private static final StatType[] DRAIN_ORDER = {
		StatType.Defence,
		StatType.Strength,
		StatType.Attack,
		StatType.Prayer,
		StatType.Magic,
		StatType.Ranged
	};


	@Override
	public boolean accept(ObjType def, String name) {
		return def.id == WEAPON_ID;
	}

	@Override
	public boolean handle(Player player, Entity target, AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		player.animate(ANIMATION_ID);
		player.graphics(GFX_ID);
		player.publicSound(SOUND_EFX);

		var boostAttack = 1.0D;

		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ACCURATE_BLOWS)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ACCURATE_BLOWS);
				var perk = (AccurateBlows) player.getPlayerPerkHandler()
					.getActivePerks(player)
					.get(perkIndex)
					.getPerk(player);
				if (perk != null)
					boostAttack += perk.getAccuracyBoost();
			}
		}

		var dogHit = new Hit(player, attackStyle, attackType)
			.randDamage(maxDamage)
			.boostDamage(0.375) // AGS Boost
			.boostAttack(boostAttack)
			.capDamagePvP(87, target);
		dogHit.postDamage(t -> {
			//check if we failed to hit
			if (dogHit.damage <= 0) return;

			// Stat reduction
			drainStats(player, target, dogHit.damage);

			// Freeze target
			t.graphics(369);
			t.freeze(20, player);

			// heal player 50% of damage
			player.incrementHp((int) Math.ceil(dogHit.damage * 0.50));

			// restore Prayer by 25% damage
			player.getStats()
				.get(StatType.Prayer)
				.restore((int) Math.ceil(dogHit.damage * 0.25), 0);

			// Mark target for blood siphon
			markTargetForBloodSiphon(player, target, attackType);
		});

		target.hit(dogHit);
		return true;
	}

	private void drainStats(Player player, Entity target, int damage) {
		if (target instanceof NPC npc)
			npc.addWeakenedDamage(player, damage);

		if (target.isPlayer()) {
			for (var stat : DRAIN_ORDER) {
				if (stat == StatType.Prayer)
					damage -= target.player.getPrayer().drain(damage);
				else
					damage -= target.player.getStats().get(stat).drain(damage);
				if (damage <= 0) break;
			}
			target.player.sendMessage("You feel drained!");
		}
		else if (target instanceof NPC npc) {
			for (var toDrain : DRAIN_ORDER) {
				var stat = npc.getCombat().getStat(toDrain);
				if (stat == null) continue;

				damage -= stat.drain(damage);
				if (damage <= 0) break;
			}
		}
	}

	private void markTargetForBloodSiphon(Player player, Entity target, AttackType attackType) {
		World.startEvent(event -> {
			if (target.isPlayer())
				target.recolour(0, 200, 0, 6, 28, 112);

			event.delay(6);

			if (Misc.getDistance(player.getPosition().copy(), target.getPosition().copy()) < 5) {
				target.graphics(2001);
				var hit = new Hit(player, AttackStyle.MAGICAL_MELEE, attackType)
					.fixedDamage(25)
					.ignorePrayer()
					.ignoreDefence();

				target.hit(hit);

				var healhit = new Hit(HitType.HEAL)
					.fixedDamage(25)
					.hide();

				player.hit(healhit);
			}
		}).setCancelCondition(() -> {
			if (target == null || (target.isPlayer() && !target.player.isOnline()) || !target.alive()) return true;
			return player == null || !player.alive() || !player.isOnline();
		});
	}

	@Override
	public int getDrainAmount() {
		return 50;
	}

}