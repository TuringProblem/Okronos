package io.ruin.model.activities.raids.xeric.chamber.combat;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.utility.Misc;

import java.util.Objects;

public class Guardian extends NPCCombat {

	private static final Projectile PROJECTILE = new Projectile(856, 150, 0, 0, 50, 0, 0, 0);

	public boolean damagedWithRock = false;

	@Override
	public void init() {
		npc.hitListener = new HitListener().preDefend(this::preDefend);
	}

	@Override
	public void startDeath(Hit killHit) {
		setDead(true);
		npc.faceNone(false);
		npc.animate(info.death_animation);
		npc.addEvent(event -> {
			event.delay(3);
			npc.transform(npc.getId() + 2);
		});
		npc.attackNpcListener = (player, npc1, message) -> {
			if (message)
				player.sendMessage("It's already destroyed.");
			return false;
		};
		if (getKiller() != null) {
			super.handleNewDrop(getKiller().player, npc.getId(), getKiller().player.getPosition());
		}
			if (!damagedWithRock) {
				Player p = getKiller().player;
				if(p != null) {
					Objects.requireNonNull(p.combatAchievementsList
						.get(p.getCombatAchievementIndexByOrdinal(CombatAchievements.DANCING_WITH_STATUES.ordinal()))
						.getCombatAchievement()).check(p);
				}
			}

	}

	private void preDefend(Hit hit) {
		if ((hit.attackStyle != null && !hit.attackStyle.isMelee())
				|| hit.attacker != null && hit.attacker.player != null
						&& VarPlayerRepository.WEAPON_TYPE.get(hit.attacker.player) != 11) {
			hit.block();
			if (hit.attacker.player != null)
				hit.attacker.player.sendMessage("The Guardian resists your attack.");
		}
	}

	@Override
	public void follow() {
		// DONT move
	}

	public static void wait(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public boolean attack() {
		if (Misc.getEffectiveDistance(npc, target) > 1)
			return false;
		if (Random.rollDie(6, 1))
			dropBoulder();
		else
			meleeAttack();
		return true;
	}

	@Override
	public void process() {

	}

	private void meleeAttack() {
		int minDamage = 15;
		int maxDamage = 30;
		if (target != null && target.isPlayer() && target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE)) {
			minDamage = 5;
			maxDamage = 19;
		}
		npc.animate(info.attack_animation);
		target.hit(new Hit(npc, AttackStyle.SLASH, AttackType.AGGRESSIVE).randDamage(minDamage, maxDamage).ignoreDefence()
				.ignorePrayer());
	}

	private void dropBoulder() {
		npc.animate(4278);
		Position pos = target.getPosition().copy();
		World.sendGraphics(305, 35, PROJECTILE.send(npc, pos.getX(), pos.getY()), pos);
		npc.addEvent(event -> {
			event.delay(1);
			if (target == null) {
				return;
			}
			npc.localPlayers().forEach(p -> {
				if (p.getPosition().distance(pos) < 1) {
					damagedWithRock = true;
					target.hit(new Hit(npc, AttackStyle.RANGED, null).randDamage(30, 50).ignoreDefence().ignorePrayer());
					wait(100);
				}
			});
		});
	}
}
