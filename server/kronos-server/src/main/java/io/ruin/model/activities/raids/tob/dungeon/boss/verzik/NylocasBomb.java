package io.ruin.model.activities.raids.tob.dungeon.boss.verzik;

import io.ruin.api.utils.Random;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.route.routes.DumbRoute;

public class NylocasBomb {
	private static final int EXPLOSION_ANIMATION = 8006;
	private static final int EXPLOSION_DELAY = 3;
	private static final int ANIMATION_DELAY = 2;
	private static final int MAX_EXPLOSION_RANGE = 4;
	private static final int BASE_DAMAGE = 64;


	private final int id;
	public final NPC npc;
	private final Bounds bossRoom;
	private volatile boolean exploding;
	private boolean isRemoved;
	private Player target;
	private NPC boss;

	public NylocasBomb(int id, Position spawn, Player target, NPC boss) {

		if (spawn == null) {
			throw new IllegalArgumentException("Spawn position cannot be null");
		}
		this.boss = boss;
		this.id = id;
		this.target = target;
		this.exploding = false;
		this.isRemoved = false;


		// Spawn NPC
		this.npc = new NPC(id).spawn(spawn);
		if (this.npc != null && this.npc.getCombat() != null) {
			this.npc.getCombat().setAllowRetaliate(false);
			this.npc.getCombat().setAllowRespawn(false);
		}

		// Calculate boss room bounds
		if (this.npc.getPosition() != null && this.npc.getPosition().getRegion() != null) {
			this.bossRoom = new Bounds(
				this.npc.getPosition().getRegion().baseX + 19,
				this.npc.getPosition().getRegion().baseY + 15,
				this.npc.getPosition().getRegion().baseX + 45,
				this.npc.getPosition().getRegion().baseY + 35,
				this.npc.getPosition().getZ()
			);
		} else {
			throw new IllegalStateException("Invalid NPC position or region");
		}

		updateTarget(target);
	}

	private void updateTarget(Player newTarget) {
		if (npc == null || npc.isRemoved() || exploding) return;

		try {
			// Find new target if current is invalid
			if (!isValidTarget(newTarget)) {
				if (!npc.localPlayers().isEmpty()) {
					newTarget = Random.get(npc.localPlayers());
				}
			}

			if (newTarget != null && isValidTarget(newTarget)) {
				target = newTarget;
				if (npc.getCombat() != null) {
					npc.getCombat().setTarget(target);
				}
				npc.face(target);

				// Route towards target using DumbRoute
				if (target.getPosition() != null) {
					DumbRoute.step(npc, target, 1);
				}
			}
		} catch (Exception e) {
			System.err.println("Error updating Nylocas bomb target: " + e.getMessage());
		}
	}

	private boolean isValidTarget(Player player) {
		return player != null &&
			player.getPosition() != null &&
			bossRoom != null &&
			player.getPosition().inBounds(bossRoom) &&
			!player.dead();
	}

	public void process() {
		if (isRemoved || npc == null || npc.isRemoved()) {
			return;
		}

		try {
			// Update target if needed
			if (!exploding && !isTargetValid()) {
				updateTarget(null);
			}

			// Check for explosion conditions
			if (!exploding) {
				checkAndHandleExplosion();
			}
		} catch (Exception e) {
			System.err.println("Error processing Nylocas bomb: " + e.getMessage());
			cleanup();
		}
	}

	private boolean isTargetValid() {
		return target != null &&
			!target.dead() &&
			target.getPosition() != null &&
			target.getPosition().inBounds(bossRoom);
	}

	private void checkAndHandleExplosion() {
		if (exploding || npc == null || npc.getPosition() == null) return;

		for (Player player : npc.localPlayers()) {
			if (player == null || player.getPosition() == null) continue;

			int distance = player.getPosition().distance(npc.getPosition());
			if (distance < MAX_EXPLOSION_RANGE) {
				startExplosion();
				break;
			}
		}
	}

	private void startExplosion() {
		if (exploding || npc == null) return;
		exploding = true;

		npc.addEvent(event -> {
			try {
				if (npc.getCombat() != null) {
					npc.getCombat().reset();
				}

				event.delay(EXPLOSION_DELAY);
				npc.animate(EXPLOSION_ANIMATION);
				event.delay(ANIMATION_DELAY);

				if (!npc.isRemoved()) {
					applyExplosionDamage();
				}

				cleanup();
			} catch (Exception e) {
				System.err.println("Error during explosion sequence: " + e.getMessage());
				cleanup();
			}
		});
	}

	private void applyExplosionDamage() {
		if (npc == null || npc.isRemoved()) return;

		Position bombPosition = npc.getPosition();
		if (bombPosition == null) return;

		int multiplier = npc.getMaxHp() > 0 ? npc.getHp() / npc.getMaxHp() : 1;

		for (Player player : npc.localPlayers()) {
			if (player == null || player.getPosition() == null) continue;

			int distance = player.getPosition().distance(bombPosition);
			if (distance >= MAX_EXPLOSION_RANGE) continue;


			int damage = calculateDamage(distance, multiplier);
			if (damage > 0 && !player.isInvincible()) {
				VerzikCombat verzikCombat = (VerzikCombat) boss.getCombat();
				verzikCombat.popItFailed = true;
				verzikCombat.damagedPlayer = true;
				player.hit(new Hit(npc, null)
					.fixedDamage(damage));
			}
		}
	}


	private int calculateDamage(int distance, int multiplier) {
		int baseDamage = BASE_DAMAGE >> distance; // Divide by 2 for each tile of distance
		return Math.max(1, baseDamage * multiplier);
	}

	private void cleanup() {
		if (!isRemoved) {
			isRemoved = true;
			if (npc != null && !npc.isRemoved()) {
				npc.remove();
			}
			target = null;
		}
	}

	public boolean isRemoved() {
		return isRemoved;
	}

	public void forceRemove() {
		cleanup();
	}
}
