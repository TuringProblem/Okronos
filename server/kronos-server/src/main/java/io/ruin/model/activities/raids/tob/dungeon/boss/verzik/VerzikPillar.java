package io.ruin.model.activities.raids.tob.dungeon.boss.verzik;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.route.RouteType;
import io.ruin.model.map.route.types.RouteAbsolute;

import java.util.List;

public class VerzikPillar {

	GameObject pillarObj;
	NPC pillarNPC;
	Bounds safeZone;

	NPC verzik;


	VerzikPillar(GameObject pillarObj, NPC pillarNPC, Bounds safeZone, NPC verzik) {
		this.pillarObj = pillarObj;
		this.pillarNPC = pillarNPC;
		this.safeZone = safeZone;
		this.pillarNPC.getCombat().setAllowRetaliate(false);
		this.verzik = verzik;
		this.pillarNPC.deathStartListener = (DeathListener.SimpleKiller) killer -> {
			pillarDestroyed();
		};

	}

	public void pillarDestroyed() {
		VerzikCombat verzikCombat = (VerzikCombat) verzik.getCombat();
		verzikCombat.anyPillarsDestroyed = true;
		pillarNPC.getCombat().setAllowRespawn(false);
		World.startEvent(event -> {
			pillarNPC.transform(8377);
			pillarNPC.animate(8052);
			pillarObj.remove();
			event.delay(3);
			for (Player player :
				verzik.localPlayers()) {
				if (player.getPosition().distance(pillarNPC.getPosition()) < 2) {
					verzikCombat.damagedPlayer = true;
					player.hit(new Hit(HitType.DAMAGE).randDamage(50, 70));
				}
			}
			pillarNPC.remove();
		});
	}


	public int getPlayersBehindPillar(List<Player> players) {
		int playersBehindPillar = 0;
		for (Player player :
			players) {
			if (player.getPosition().inBounds(safeZone))
				playersBehindPillar++;
		}
		return playersBehindPillar;
	}

	public boolean playerBehindPillar(Player player) {
		return player.getPosition().inBounds(safeZone);
	}

	public NPC getPillarNPC() {
		return pillarNPC;
	}


}
