package io.ruin.model.skills.agility.shortcut;

import com.google.common.collect.Lists;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.stat.StatType;
import io.ruin.utility.PredicateCheck;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public enum SteppingStone {

	LUMBRIDGE_CAVE(1, 1,
		Position.of(3204, 9572), Position.of(3208, 9572),
		Position.of(3206, 9572)),

	LUMBRIDGE_CAVE2(1, 1,
		Position.of(3221, 9556), Position.of(3221, 9553),
		Position.of(3221, 9554)),

	BRIMHAVEN_STONES12(12, 1,
		Position.of(2649, 9562), Position.of(2647, 9557),
		Position.of(2649, 9561), Position.of(2649, 9560), Position.of(2648, 9560), Position.of(2647, 9560), Position.of(2647, 9559), Position.of(2647, 9558)),

	CORSAIR_COVE_STONES15(15, 1,
		Position.of(1981, 8998, 1), Position.of(1981, 8994, 1),
		Position.of(1981, 8996, 1)),

	KARAMJA_STONES30(30, 1,
		Position.of(2925, 2951), Position.of(2925, 2947),
		Position.of(2925, 2950), Position.of(2925, 2949), Position.of(2925, 2948)),

	DRAYNOR_STONES31(31, 1,
		Position.of(3149, 3363), Position.of(3154, 3363),
		Position.of(3150, 3363), Position.of(3151, 3363), Position.of(3152, 3363), Position.of(3153, 3363)),

	ZEAH_E_STONES40(40, 1,
		Position.of(1610, 3570), Position.of(1614, 3570),
		Position.of(1612, 3570)),

	ZEAH_W_STONES40(40, 1,
		Position.of(1607, 3571), Position.of(1603, 3571),
		Position.of(1605, 3571)),

	ZEAH_STONES45(45, 1,
		Position.of(1720, 3551), Position.of(1724, 3551),
		Position.of(1722, 3551)),

	MORTMYRE_STONES50(50, 1,
		Position.of(3417, 3325), Position.of(3421, 3323),
		Position.of(3419, 3325)),

	MISCELLANIA_STONES55(55, 1,
		Position.of(2573, 3859), Position.of(2575, 3861),
		Position.of(2573, 3861)),

	MOS_LEHARMLESS_STONES60(60, 1,
		Position.of(3715, 2969), Position.of(3708, 2969),
		Position.of(3711, 2969)),

	LUMBRIDGE_STONES66(66, 1,
		Position.of(3212, 3137), Position.of(3214, 3132),
		Position.of(3214, 3135)),

	WILDERNESS_LAVADRAG_STONES74(74, 1,
		Position.of(3201, 3807), Position.of(3201, 3810),
		Position.of(3201, 3808)),

	ZULRAH_STONES76(76, 1,
		Position.of(2160, 3072), Position.of(2154, 3072),
		Position.of(2157, 3072)),

	SHILO_STONES77(77, 1,
		Position.of(2863, 2971), Position.of(2863, 2976),
		Position.of(2863, 2974)),

	WILDERNESS_LAVAMAZE_STONES82(82, 1,
		Position.of(3091, 3882), Position.of(3093, 3879),
		Position.of(3092, 3880)),

	BRIMHAVEN_N_STONES83(83, 1,
		Position.of(2682, 9548), Position.of(2690, 9547),
		Position.of(2684, 9548), Position.of(2686, 9548), Position.of(2688, 9547)),
	BRIMHAVEN_S_STONES83(83, 1,
		Position.of(2695, 9533), Position.of(2697, 9525),
		Position.of(2695, 9531), Position.of(2695, 9529), Position.of(2696, 9527)),
	;

	private SteppingStone(int levelReq, int xp, Position startPosition, Position endPosition, Position... steps) {
		this.xp = xp;
		this.levelReq = levelReq;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.positions = Arrays.asList(steps);
	}

	private SteppingStone(PredicateCheck canUse, int levelReq, int xp, Position startPosition, Position endPosition, Position... steps) {
		this.canUse = canUse;
		this.xp = xp;
		this.levelReq = levelReq;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.positions = Arrays.asList(steps);
	}

	private List<Position> positions;
	private Position startPosition, endPosition;
	private int xp;
	private int levelReq;

	private PredicateCheck canUse;

	public void traverse(Player p, GameObject obj) {
		if (canUse != null) {
			if (!canUse.check(p))
				return;
		}
		if (!p.getStats().check(StatType.Agility, levelReq)) {
			p.sendMessage("You need an agility level of " + levelReq + " to navigate this shortcut!");
			return;
		}
		p.startEvent(e -> {
			p.lock(LockType.FULL_DELAY_DAMAGE);
			List<Position> posCopy = Lists.newArrayList(positions);

			Position first = posCopy.get(0);
			if (posCopy.size() == 1) {
				if (startPosition.equals(p.getPosition())) {
					posCopy.add(endPosition);
				} else {
					posCopy.add(startPosition);
				}
			} else if (obj.getPosition().equals(first)) {
				posCopy.add(endPosition);
			} else {

				Collections.reverse(posCopy);
				posCopy.add(startPosition);
			}

			for (int index = 0; index < posCopy.size(); index++) {
				Position pos = posCopy.get(index);
				int xDiff = pos.getX() - p.getPosition().getX();
				int yDiff = pos.getY() - p.getPosition().getY();

				p.animate(741);
				p.getMovement().force(xDiff, yDiff, 0, 0, 5, 35, Direction.getDirection(p.getPosition(), pos));
				if (index != posCopy.size() - 1) e.delay(2);
				else e.delay(1);
			}

			if (World.isEco())
				p.getStats().addXp(StatType.Agility, xp, true);

			p.unlock();
		});

	}

}
