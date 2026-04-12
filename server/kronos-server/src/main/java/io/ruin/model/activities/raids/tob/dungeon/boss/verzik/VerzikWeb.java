package io.ruin.model.activities.raids.tob.dungeon.boss.verzik;

import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.map.object.GameObject;

import java.util.ArrayList;
import java.util.List;

public class VerzikWeb {

	GameObject webObject;
	public NPC webNPC;

	List<Player> trappedPlayer = new ArrayList<>();

	int freezeTimer = 15;
	boolean frozenPlayer = false;
	public boolean webDestroyed = false;

	int timeAlive = 0;

	public VerzikWeb(GameObject webObject, NPC webNPC) {
		this.webObject = webObject;
		this.webNPC = webNPC;
		this.webNPC.setHidden(true);
		this.webNPC.deathStartListener = (DeathListener.SimpleKiller) killer -> {
			webDestroyed();
		};
	}

	public void process(List<Player> localPlayers) {
		if (frozenPlayer)
			webNPC.setHidden(false);
		if (timeAlive++ >= 45)
			webDestroyed();
		if (frozenPlayer) {
			freezeTimer--;
			if (freezeTimer <= 0)
				webDestroyed();
		}
		if (!webDestroyed) {
			for (Player player :
				localPlayers) {
				float distance = webObject.getPosition().distance(player.getPosition());
				if (distance < 1) {
					if (!trappedPlayer.contains(player)) {
						this.webNPC.setHidden(false);
						player.freeze(45, webNPC);
						player.hit(new Hit().fixedDamage(20).ignorePrayer().ignoreDefence());
						trappedPlayer.add(player);
						frozenPlayer = true;
					}
				}
			}
		}
	}

	public void webDestroyed() {
		if (!webDestroyed) {
			webDestroyed = true;
			webNPC.remove();
			webObject.remove();
			for (Player player :
				trappedPlayer) {
				player.unlock();
				player.resetFreeze();
			}
		}
	}
}
