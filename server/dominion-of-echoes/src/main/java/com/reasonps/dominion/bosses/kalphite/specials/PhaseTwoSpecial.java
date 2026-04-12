package com.reasonps.dominion.bosses.kalphite.specials;

import com.reasonps.dominion.bosses.IDirectional;
import com.reasonps.dominion.bosses.kalphite.EchoKalphiteQueen;
import com.reasonps.dominion.bosses.kalphite.Form;
import com.reasonps.dominion.bosses.kalphite.SpecialAttack;
import com.reasonps.dominion.bosses.kalphite.attacks.RangedBuzzAttack;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Glabay | Glabay-Studios
 * @project kronos
 * @social Discord: Glabay
 * @since 2025-05-22
 */
public class PhaseTwoSpecial implements SpecialAttack, IDirectional {

	private final int MAX_DISTANCE = 7;

	@Override
	public void invoke(Player target, NPCCombat bossCombat) {
		int ANIMATION_ID = 6234;
		bossCombat.getNpc().animate(ANIMATION_ID);
		// Lock the boss, but allow her to still take damage if the player is able to :P
		bossCombat.getNpc().lock(LockType.MOVEMENT);
		var tilesForLightning = new AtomicReference<>(
				getTilesForLightning(target, bossCombat.getNpc().getPosition(), MAX_DISTANCE));
		bossCombat.getNpc().startEvent(event -> {
			if (target.getPosition().distance(bossCombat.getNpc().getCentrePosition()) > 6)
				new RangedBuzzAttack().invoke(target, bossCombat);
			event.delay(ATTACK_DELAY);
			castLightning(Form.SECOND, target, tilesForLightning.get());
			tilesForLightning.set(getTilesForLightning(target, bossCombat.getNpc().getPosition(), MAX_DISTANCE));
			if (target.getPosition().distance(bossCombat.getNpc().getCentrePosition()) > 6)
				new RangedBuzzAttack().invoke(target, bossCombat);
			event.delay(ATTACK_DELAY);
			castLightning(Form.SECOND, target, tilesForLightning.get());
			tilesForLightning.set(getTilesForLightning(target, bossCombat.getNpc().getPosition(), MAX_DISTANCE));
			if (target.getPosition().distance(bossCombat.getNpc().getCentrePosition()) > 6)
				new RangedBuzzAttack().invoke(target, bossCombat);
			event.delay(ATTACK_DELAY);
			castLightning(Form.SECOND, target, tilesForLightning.get());
			tilesForLightning.set(getTilesForLightning(target, bossCombat.getNpc().getPosition(), MAX_DISTANCE));
			if (target.getPosition().distance(bossCombat.getNpc().getCentrePosition()) > 6)
				new RangedBuzzAttack().invoke(target, bossCombat);
			event.delay(ATTACK_DELAY);
			castLightning(Form.SECOND, target, tilesForLightning.get());
			tilesForLightning.set(getTilesForLightning(target, bossCombat.getNpc().getPosition(), MAX_DISTANCE));
			if (target.getPosition().distance(bossCombat.getNpc().getCentrePosition()) > 6)
				new RangedBuzzAttack().invoke(target, bossCombat);
			event.delay(ATTACK_DELAY);
			castLightning(Form.SECOND, target, tilesForLightning.get());
			tilesForLightning.set(getTilesForLightning(target, bossCombat.getNpc().getPosition(), MAX_DISTANCE));
			if (target.getPosition().distance(bossCombat.getNpc().getCentrePosition()) > 6)
				new RangedBuzzAttack().invoke(target, bossCombat);
			event.delay(ATTACK_DELAY);
			castLightning(Form.SECOND, target, tilesForLightning.get());
			tilesForLightning.set(getTilesForLightning(target, bossCombat.getNpc().getPosition(), MAX_DISTANCE));
			if (target.getPosition().distance(bossCombat.getNpc().getCentrePosition()) > 6)
				new RangedBuzzAttack().invoke(target, bossCombat);
			event.delay(ATTACK_DELAY);

			((EchoKalphiteQueen) bossCombat).performingSpecial = false;
			bossCombat.getNpc().unlock();
		}).setCancelCondition(() -> bossCombat.isDead() || bossCombat.getNpc().isRemoved());
	}

	List<Position> getTilesForLightning(Player target, Position source, int maxDistance) {
		var startingTile = Position.of(source.getX() + 2, source.getY() + 2);
		var direction = Direction.getDirection(startingTile, target.getPosition());
		var tiles = switch (direction) {
			case WEST, EAST -> getHorizontalLightningTiles(direction, startingTile, maxDistance);
			case NORTH, SOUTH -> getVerticalLightningTiles(direction, startingTile, maxDistance);
			case NORTH_WEST, NORTH_EAST, SOUTH_WEST, SOUTH_EAST ->
				getDiagonalLightningTiles(direction, startingTile, maxDistance);

		};
		return new ArrayList<>(tiles);
	}

	private List<Position> getHorizontalLightningTiles(Direction direction, Position source, int maxDistance) {
		var hotSpots = new ArrayList<Position>();
		for (int i = 0; i < maxDistance; i++) {
			hotSpots.add(source.translated(getOffsetForX(direction, i), 1));
			hotSpots.add(source.translated(getOffsetForX(direction, i), 0));
			hotSpots.add(source.translated(getOffsetForX(direction, i), -1));
		}
		return hotSpots;
	}

	private List<Position> getVerticalLightningTiles(Direction direction, Position source, int maxDistance) {
		var hotSpots = new ArrayList<Position>();
		for (int i = 0; i < maxDistance; i++) {
			hotSpots.add(source.translated(1, getOffsetForY(direction, i)));
			hotSpots.add(source.translated(0, getOffsetForY(direction, i)));
			hotSpots.add(source.translated(-1, getOffsetForY(direction, i)));
		}
		return hotSpots;
	}

	private List<Position> getDiagonalLightningTiles(Direction direction, Position source, int maxDistance) {
		var hotSpots = new ArrayList<Position>();
		for (int i = 0; i < maxDistance; i++) {
			int xOffset = getOffsetForDiagonalX(direction, i);
			int yOffset = getOffsetForDiagonalY(direction, i);

			hotSpots.add(source.translated(xOffset + getPerpendicularOffsetX(direction),
					yOffset + getPerpendicularOffsetY(direction)));
			hotSpots.add(source.translated(xOffset, yOffset));
			hotSpots.add(source.translated(xOffset - getPerpendicularOffsetX(direction),
					yOffset - getPerpendicularOffsetY(direction)));
		}
		return hotSpots;
	}
}
