package npc.nex.attacks.impl.std;

import io.ruin.model.World;
import npc.nex.attacks.Attack;
import npc.nex.modes.Forms;
import npc.nex.scripts.NexCombat;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.utility.Misc;

import java.util.Arrays;
import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
public class WrathAttack implements Attack {

	@Override
	public void invoke(Player target, NexCombat combat) {
		if (combat.getNpc().getId() == Forms.WRATH_NEX.getNpcId()) {
			var deathPlayers = combat.getNpc().getPosition().getRegion().players;
			combat.getNpc().forceText("Taste my wrath!");
			combat.getNpc().getPosition().getRegion().players.forEach(p -> {
				if (!p.getPosition().inBounds(combat.ATTACK_BOUNDS))
					return;
				p.sendMessage("Nex: Taste my wrath!");
			});
			World.startEvent(e -> {
				e.delay(3);
				var tiles = getWrathTiles(combat.getNpc());
				World.sendGraphics(
					2013,
					0,
					30,
					combat.getNpc().getPosition().getX() + 1,
					combat.getNpc().getPosition().getY() + 1,
					combat.getNpc().getHeight()
				);
				tiles.forEach(tile -> {
					// if we're outside of nex bounds, block the animation
					if (!tile.inBounds(combat.ATTACK_BOUNDS))
						return;

					if (Tile.get(tile).clipping == 0) { // if clipping is good
						WRATH_PROJ.send(combat.getNpc().getPosition(), tile); // send proj
						World.sendGraphics(2014, 0, 4, tile); // send gfx
					}
				});
				e.delay(2);
				deathPlayers.forEach(p -> {
					if (!p.getPosition().inBounds(combat.ATTACK_BOUNDS)) // if player is out of bounds, don't hit them
						return;
					final int dist = calculateWrathDistance(p, combat.getNpc());
					final int currentDist = Misc.getDistance(combat.getNpc().getPosition(), p.getPosition());
					// System.out.print("Wrath dist: " + dist + ", ");
					if (currentDist <= dist) {
						p.hit(new Hit(HitType.DAMAGE).randDamage(30, 40).ignorePrayer().ignoreDefence());
					}
				});
			});
		}
	}

	private int calculateWrathDistance(Player p, Entity entity) {
		int dist = 2;
		if (p.getPosition().getY() >= entity.getPosition().getY())
			dist += 1;
		if (p.getPosition().getX() >= entity.getPosition().getX())
			dist += 1;
		return dist;
	}

	private List<Position> getWrathTiles(Entity e) {
		return Arrays.asList(
			Position.of(e.getPosition().getX() + 1, e.getPosition().getY() - 2, e.getHeight()),
			Position.of(e.getPosition().getX() - 1, e.getPosition().getY() - 1, e.getHeight()),
			Position.of(e.getPosition().getX() + 3, e.getPosition().getY() - 1, e.getHeight()),
			Position.of(e.getPosition().getX() + 3, e.getPosition().getY() - 1, e.getHeight()),
			Position.of(e.getPosition().getX() - 2, e.getPosition().getY() + 1, e.getHeight()),
			Position.of(e.getPosition().getX() + 4, e.getPosition().getY() + 1, e.getHeight()),
			Position.of(e.getPosition().getX() - 1, e.getPosition().getY() + 3, e.getHeight()),
			Position.of(e.getPosition().getX() + 3, e.getPosition().getY() + 3, e.getHeight()),
			Position.of(e.getPosition().getX() + 1, e.getPosition().getY() + 4, e.getHeight())
		);
	}
}
