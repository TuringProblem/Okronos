package io.ruin.model.combat;

import io.ruin.Server;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.stat.StatType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Combat {

	private transient boolean dead;
	@Getter
	@Setter
	public transient boolean truelyDead;
	protected transient Entity target;
	private transient long lastAttackTick;
	private transient int lastAttackTickDelay;
	public transient int lastPlayerAttacked;
	private transient int attackDelayTicks;
	public transient long lastDefendTick;
	public transient boolean retaliating;
	public transient long lastDefendTickFromPlayer;
	public transient long lastDefendTickFromNPC;
	public transient Entity lastAttacker;
	public transient Entity lastPlayerAttacker;
	public transient Entity lastNPCAttacker;
	public transient HashMap<Integer, Killer> killers = new HashMap<>();
	public transient List<Player> damagedPlayers = new ArrayList<>();

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public boolean isDead() {
		return dead;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	public Entity getTarget() {
		return target;
	}

	public void updateLastAttack(int tickDelay) {
		lastAttackTick = Server.currentTick();
		lastAttackTickDelay = tickDelay;
		lastPlayerAttacked = target == null || // honestly Idk how target can be null...
				target.player == null ? -1 : target.player.getUserId();
	}

	public void delayAttack(int ticks) {
		attackDelayTicks += ticks; // this needs to increment specifically for combo eating
	}

	public boolean isAttacking(int timeoutTicks) {
		return Server.currentTick() - lastAttackTick < timeoutTicks;
	}

	public boolean hasAttackDelay() {
		if (isAttacking(lastAttackTickDelay + attackDelayTicks)) {
			return true;
		}

		attackDelayTicks = 0;
		return false;
	}

	public void updateLastDefend(Entity attacker) {
		lastDefendTick = Server.currentTick();
		lastAttacker = attacker;
		if (attacker.isPlayer()) {
			lastDefendTickFromPlayer = Server.currentTick();
			lastPlayerAttacker = attacker;
		}
		if (attacker.isNpc()) {
			lastDefendTickFromNPC = Server.currentTick();
			lastNPCAttacker = attacker;
		}

	}

	public boolean isDefending(long timeoutTicks) {
		return Server.currentTick() - lastDefendTick < timeoutTicks;
	}

	public boolean isDefendingFromPlayer(long timeoutTicks) {
		return Server.currentTick() - lastDefendTickFromPlayer < timeoutTicks;
	}

	public boolean isDefendingFromNPC(long timeoutTicks) {
		return Server.currentTick() - lastDefendTickFromNPC < timeoutTicks;
	}

	public boolean allowPj(Entity attacker) {
		int ticks = attacker.inMulti() ? 16 : 20;
		// NOTE: shitcode hack:
		// when player attacks npc, that npc should be allowed to attack player back.
		if (this.target == attacker) {
			return true;
		}

		return attacker == lastPlayerAttacker || attacker.isPlayer() && !isDefendingFromPlayer(ticks)
				|| attacker == lastNPCAttacker || !isDefendingFromNPC(16) && !isDefendingFromPlayer(ticks);
	}

	public void addKiller(Entity attacked, Entity attacker, int damage) {
		if (attacker.player == null)
			return;
		if (killers == null)
			killers = new HashMap<>();
		int userId = attacker.player.getUserId();
		Killer k = killers.get(userId);
		if (k == null)
			k = new Killer();
		k.player = attacker.player;
		k.damage += damage;
		killers.put(userId, k);
		if (attacked instanceof Player player) {
			if (!damagedPlayers.contains(player))
				damagedPlayers.add(player);
		}
	}

	public void resetKillers() {
		if (killers != null)
			killers.clear();
	}

	public void removeKillerFromDamagedPlayers() {
		if (damagedPlayers.isEmpty())
			return;
		for (Player player : damagedPlayers) {
			if (player == null)
				continue;
			if (player.getCombat().killers == null)
				continue;
			if (player.getCombat().killers.isEmpty())
				continue;
			player.getCombat().killers.remove(player.getUserId());
		}
	}

	public Killer getKiller() {
		if (killers == null) {
			Server.logWarning("getKiller(): killers were null, no killer was selected!");
			return null;
		}
		Killer highestKiller = null;
		for (Killer killer : killers.values()) {
			if (highestKiller == null || killer.damage > highestKiller.damage)
				highestKiller = killer;
		}
		return highestKiller;
	}

	/**
	 * Reset
	 */

	public void reset() {
		target = null;
	}

	/**
	 * Abstract
	 */

	public abstract boolean allowRetaliate(Entity attacker);

	public abstract void startDeath(Hit killHit);

	public abstract AttackStyle getAttackStyle();

	public abstract AttackType getAttackType();

	public abstract double getLevel(StatType statType);

	public abstract double getBonus(int bonusType);

	public abstract int getDefendAnimation();

	public abstract double getDragonfireResistance();

	public abstract void faceTarget();

	public void defendAnim(final int clientTicks) {
		Entity target = this.target;
		if (target == null)
			return;
		if (target.isAnimating())
			return;
		Combat combat = target.getCombat();
		if (combat == null)
			return;
		int animId = combat.getDefendAnimation();
		if (animId < 0)
			return;
		target.animate(animId, Math.max(clientTicks, 0));
	}

	public void defendAnimTicks(final int serverTicks) {
		defendAnim(Math.max(serverTicks, 0) * 30);
	}

	public void defendAnim() {
		defendAnim(0);
	}

}
