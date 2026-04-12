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
import io.ruin.utility.Common;
import io.ruin.utility.Misc;

import java.util.Arrays;
import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-04
 */
public class IcePrisonAttack implements Attack {
	@Override
	public void invoke(Player target, NexCombat combat) {
		final var prisoner = getPossibleTargets(combat).getFirst();
		combat.getNpc().forceText(ICE_PRISON);
		combat.getNpc().getPosition().getRegion().players.forEach(p -> {
			if (!p.getPosition().inBounds(combat.ATTACK_BOUNDS))
				return;
			p.sendMessage("Nex: " + ICE_PRISON);
		});
		combat.getNpc().animate(9186);
		combat.getNpc().getCombat().delayAttack(4);
		World.startEvent(e -> {
			e.delay(4);
			for (Position prisonPos : getIcePrisonPositions(prisoner)) {
				final Tile tile = Tile.get(prisonPos);
				if (tile == null || tile.clipping != 0) {
					continue;
				}
				final GameObject gameObject = new GameObject(ATTACKABLE_STALAGMITE, prisonPos.copy(), 10,
					STALAGMITE_DIRECTION).spawn();
				combat.getStalagmites().add(gameObject);
			} // freeze prison and disable prot prayer
			prisoner.publicSound(168, 1, 0);
			prisoner.graphics(369, 0, 0);
			prisoner.getPrayer().slashPrayers();
			prisoner.sendMessage("You have been frozen and you prayer has been slashed!");
			final int firstStalagCount = Common.countNonNullSpawnedObjects(combat.getStalagmites());
			e.delay(7);
			final int currentStalagCount = Common.countNonNullSpawnedObjects(combat.getStalagmites());
			// System.out.println("Stalag status: " + currentStalagCount + "/" +
			// firstStalagCount);
			if (currentStalagCount == firstStalagCount) {
				// System.out.println("Attempting to hit players inside the stalag bounds");
				combat.getNpc().getPosition().getRegion().players.stream()
					.filter(p -> Misc.getDistance(prisoner.getPosition().copy(), p.getPosition().copy()) <= 5 && !p.dead())
					.forEach(p -> {
						// System.out.println("A1");
						final Bounds iceBounds = getIcePrisonBounds(combat.getNpc());
						if (p.getPosition().inBounds(iceBounds)) { // dude is within containment bounds
							// System.out.println("Hit: " + p.getName());
							p.hit(new Hit(HitType.DAMAGE).randDamage(1, 75).ignorePrayer().ignoreDefence());
							combat.damagedByIce = true;
							p.getPrayer().slashPrayers();
						}
					});
			}
			e.delay(9);
			prisoner.unlock();
			ZarosUtils.removeObjects(combat.getStalagmites());
			combat.getStalagmites().clear();
		});
	}

	private Bounds getIcePrisonBounds(Entity nex) {
		return new Bounds(
			nex.getPosition().getX() - 1,
			nex.getPosition().getY() - 1,
			nex.getPosition().getX() + 1,
			nex.getPosition().getY() + 1,
			0
		);
	}

	private List<Position> getIcePrisonPositions(Entity e) {
		return Arrays.asList(
			Position.of(e.getPosition().getX() - 1, e.getPosition().getY() - 1, e.getHeight()),
			Position.of(e.getPosition().getX() - 1, e.getPosition().getY(), e.getHeight()),
			Position.of(e.getPosition().getX() - 1, e.getPosition().getY() + 1, e.getHeight()),
			Position.of(e.getPosition().getX(), e.getPosition().getY() - 1, e.getHeight()),
			Position.of(e.getPosition().getX() + 1, e.getPosition().getY() - 1, e.getHeight()),
			Position.of(e.getPosition().getX(), e.getPosition().getY() + 1, e.getHeight()),
			Position.of(e.getPosition().getX() + 1, e.getPosition().getY(), e.getHeight()),
			Position.of(e.getPosition().getX() + 1, e.getPosition().getY() + 1, e.getHeight()));
	}
}
