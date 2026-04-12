package npc.nex.attacks.impl.spec;

import io.ruin.model.World;
import npc.nex.attacks.Attack;
import npc.nex.scripts.NexCombat;
import npc.nex.utils.ZarosUtils;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;

import java.util.Arrays;
import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-04
 */
public class ContainmentAttack implements Attack {
	@Override
	public void invoke(Player target, NexCombat combat) {
		combat.getNpc().forceText(CONTAIN);
		combat.getNpc().getPosition().getRegion().players.forEach(p -> {
			if (!p.getPosition().inBounds(combat.ATTACK_BOUNDS))
				return;
			p.sendMessage("Nex: " + CONTAIN);
		});
		combat.getNpc().animate(9186);
		combat.getNpc().getCombat().delayAttack(4);
		World.startEvent(e -> {
			e.delay(4);
			for (Position offSetPos : getContainmentTiles(combat.getNpc())) {
				final Tile tile = Tile.get(offSetPos);
				if (tile == null || tile.clipping != 0) {
					continue;
				}
				final GameObject gameObject = new GameObject(UNATTACKABLE_STALAGMITE, offSetPos.copy(), 10,
					STALAGMITE_DIRECTION).spawn();
				combat.getIcicles().add(gameObject);
			}
			combat.getNpc().getPosition().getRegion().players.forEach(p -> {
				final Bounds containmentBounds = getContainmentBounds(combat.getNpc());
				if (p.getPosition().inBounds(containmentBounds)) { // dude is within containment bounds
					p.hit(new Hit(HitType.DAMAGE).randDamage(1, 60).ignorePrayer().ignoreDefence());
					p.getPrayer().slashPrayers();
					combat.damagedByIce = true;
				}

			});
			e.delay(10);
			ZarosUtils.removeObjects(combat.getIcicles());
			combat.getIcicles().clear();
		});
	}

	private Bounds getContainmentBounds(Entity nex) {
		return new Bounds(
			nex.getPosition().getX() - 1,
			nex.getPosition().getY() - 1,
			nex.getPosition().getX() + 4,
			nex.getPosition().getY() + 4,
			0);
	}

	private List<Position> getContainmentTiles(Entity e) {
		return Arrays.asList(
			Position.of(e.getPosition().getX() - 1, e.getPosition().getY() - 1, e.getHeight()),
			Position.of(e.getPosition().getX(), e.getPosition().getY() - 1, e.getHeight()),
			Position.of(e.getPosition().getX() + 1, e.getPosition().getY() - 1, e.getHeight()),
			Position.of(e.getPosition().getX() + 2, e.getPosition().getY() - 1, e.getHeight()),
			Position.of(e.getPosition().getX() + 3, e.getPosition().getY() - 1, e.getHeight()),
			Position.of(e.getPosition().getX() - 1, e.getPosition().getY() + 3, e.getHeight()),
			Position.of(e.getPosition().getX(), e.getPosition().getY() + 3, e.getHeight()),
			Position.of(e.getPosition().getX() + 1, e.getPosition().getY() + 3, e.getHeight()),
			Position.of(e.getPosition().getX() + 2, e.getPosition().getY() + 3, e.getHeight()),
			Position.of(e.getPosition().getX() + 3, e.getPosition().getY() + 3, e.getHeight()),
			Position.of(e.getPosition().getX() + -1, e.getPosition().getY(), e.getHeight()),
			Position.of(e.getPosition().getX() + -1, e.getPosition().getY() + 1, e.getHeight()),
			Position.of(e.getPosition().getX() + -1, e.getPosition().getY() + 2, e.getHeight()),
			Position.of(e.getPosition().getX() + 3, e.getPosition().getY(), e.getHeight()),
			Position.of(e.getPosition().getX() + 3, e.getPosition().getY() + 1, e.getHeight()),
			Position.of(e.getPosition().getX() + 3, e.getPosition().getY() + 2, e.getHeight())
		);
	}
}
