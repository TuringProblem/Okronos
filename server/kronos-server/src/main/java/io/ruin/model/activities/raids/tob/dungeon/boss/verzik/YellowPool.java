package io.ruin.model.activities.raids.tob.dungeon.boss.verzik;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.route.routes.ProjectileRoute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YellowPool {
	private int POOL_GFX = 1595;
	private static final Projectile SPIDER_PROJ = new Projectile(1596, 100, 0, 0, 220, 0, 50, 0).regionBased();

	private int ticks = 0;
	private boolean isActive = false;

	private NPC npc;

	List<Player> localPlayers;
	static HashMap<Position, Boolean> poolPositions = new HashMap<>();

	HashMap<Player, Boolean> playerProtectionCheck = new HashMap<>();
	HashMap<Player, Position> playerProtectionPosition = new HashMap<>();

	Bounds bossRoom;


	YellowPool(List<Player> localPlayers, NPC npc, Bounds bossRoom) {
		this.localPlayers = localPlayers;
		this.npc = npc;
		this.bossRoom = bossRoom;
		isActive = true;
		for (int i = 0; i < localPlayers.size(); i++) {
			poolPositions.put(bossRoom.randomPosition(), false);
		}
	}

	public void setActive(List<Player> localPlayers, NPC npc, Bounds bossRoom) {
		isActive = true;
		ticks = 0;
		poolPositions.clear();
		playerProtectionCheck.clear();
		playerProtectionPosition.clear();
		this.localPlayers = localPlayers;
		this.npc = npc;
		this.bossRoom = bossRoom;
		for (int i = 0; i < localPlayers.size(); i++) {
			poolPositions.put(bossRoom.randomPosition(), false);
		}
	}

	public void process() {
		if (ticks > 15) {
			isActive = false;
			return;
		}
		if (isActive) {
			for (Map.Entry<Player, Position> entry : playerProtectionPosition.entrySet()) {
				if (poolPositions.get(entry.getValue())) {
					localPlayers.forEach(p -> {
						if (p == entry.getKey()) {
							if (p.getPosition() != entry.getValue()) {
								poolPositions.replace(entry.getValue(), false);
							}
						}
					});
				}
			}
			for (Map.Entry<Position, Boolean> entry : poolPositions.entrySet()) {
				Position key = entry.getKey();
				World.sendGraphics(POOL_GFX, 0, 0, key);
			}

			ticks++;
			for (Player player :
				localPlayers) {
				if (playerProtectionPosition.get(player) != null) {
					if (playerProtectionPosition.get(player) != player.getPosition()) {
						playerProtectionCheck.replace(player, false);
						playerProtectionPosition.remove(player);
					}
				}
				boolean isProtected = false;
				for (Map.Entry<Position, Boolean> entry : poolPositions.entrySet()) {
					Position pos = entry.getKey();
					if (player.getPosition().isWithinDistance(pos, 0)) {
						isProtected = true;
						playerProtectionPosition.put(player, pos);
					}
				}
				playerProtectionCheck.put(player, isProtected);
			}

			localPlayers.forEach(p -> {
				if (ticks == 7) {
					if (ProjectileRoute.allow(npc, p)) {
						SPIDER_PROJ.send(npc, p);
					}
				} else if (ticks == 11) {
					if (!playerProtectionCheck.get(p)) {
						VerzikCombat verzikCombat = (VerzikCombat) npc.getCombat();
						verzikCombat.damagedPlayer = true;
						p.hit(new Hit(HitType.DAMAGE).randDamage(50, 70));
					}
				}
			});
		}
	}


}
